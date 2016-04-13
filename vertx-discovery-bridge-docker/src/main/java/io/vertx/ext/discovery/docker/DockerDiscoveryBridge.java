/*
 * Copyright (c) 2011-2016 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *      The Eclipse Public License is available at
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *      The Apache License v2.0 is available at
 *      http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.ext.discovery.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.discovery.spi.DiscoveryBridge;
import io.vertx.ext.discovery.DiscoveryService;
import io.vertx.ext.discovery.Record;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * A discovery bridge collecting services from Docker, and importing them in the Vert.x
 * discovery infrastructure.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DockerDiscoveryBridge implements DiscoveryBridge {

  private final static Logger LOGGER = LoggerFactory.getLogger(DockerDiscoveryBridge.class);

  private long timer;
  private DockerClient client;

  private List<DockerService> services = new ArrayList<>();
  private DiscoveryService discovery;
  private Vertx vertx;
  private String host;

  /**
   * Starts the bridge.
   *
   * @param vertx             the vert.x instance
   * @param discovery         the discovery service
   * @param configuration     the bridge configuration if any
   * @param completionHandler handler called when the bridge has been initialized
   */
  @Override
  public void start(Vertx vertx, DiscoveryService discovery, JsonObject configuration, Handler<AsyncResult<Void>> completionHandler) {
    this.discovery = discovery;
    this.vertx = vertx;
    DockerClientConfig.DockerClientConfigBuilder builder =
        DockerClientConfig.createDefaultConfigBuilder();
    String dockerCertPath = configuration.getString("docker-cert-path");
    String dockerCfgPath = configuration.getString("docker-cfg-path");
    String email = configuration.getString("docker-registry-email");
    String password = configuration.getString("docker-registry-password");
    String username = configuration.getString("docker-registry-username");
    String host = configuration.getString("docker-host");
    boolean tlsVerify = configuration.getBoolean("docker-tls-verify", true);
    String registry
        = configuration.getString("docker-registry-url", "https://index.docker.io/v1/");
    String version = configuration.getString("version");

    if (dockerCertPath != null) {
      builder.withDockerCertPath(dockerCertPath);
    }
    if (dockerCfgPath != null) {
      builder.withDockerConfig(dockerCfgPath);
    }
    if (email != null) {
      builder.withRegistryEmail(email);
    }
    if (password != null) {
      builder.withRegistryPassword(password);
    }
    if (username != null) {
      builder.withRegistryUsername(username);
    }
    if (host != null) {
      builder.withDockerHost(host);
    }
    if (registry != null) {
      builder.withRegistryUrl(registry);
    }
    if (version != null) {
      builder.withApiVersion(version);
    }
    builder.withDockerTlsVerify(tlsVerify);


    DockerClientConfig config = builder.build();
    if (config.getDockerHost().getScheme().equalsIgnoreCase("unix")) {
      try {
        this.host = InetAddress.getLocalHost().getHostAddress();
      } catch (UnknownHostException e) {
        completionHandler.handle(Future.failedFuture(e));
      }
    } else {
      this.host = config.getDockerHost().getHost();
    }
    client = DockerClientBuilder.getInstance(config).build();

    long period = configuration.getLong("scan-period", 3000L);
    if (period > 0) {
      timer = vertx.setPeriodic(period, l -> {
        scan(null);
      });
    }
    scan(completionHandler);
  }

  synchronized void scan(Handler<AsyncResult<Void>> completion) {
    vertx.<List<Container>>executeBlocking(
        future -> {
          try {
            future.complete(client.listContainersCmd().withStatusFilter("running").exec());
          } catch (Exception e) {
            future.fail(e);
          }
        },
        ar -> {
          if (ar.failed()) {
            completion.handle(Future.failedFuture(ar.cause()));
            return;
          }

          List<Container> running = ar.result();
          List<DockerService> toRemove = new ArrayList<>();

          // Detect lost containers
          services.stream()
              .filter(service -> isNotRunning(service.id(), running))
              .forEach(service -> {
                unpublish(service);
                toRemove.add(service);
              });
          services.removeAll(toRemove);

          if (running != null) {
            // Detect new containers
            running.stream()
                .filter(container -> !isKnown(container))
                .forEach(container -> {
                  DockerService service = new DockerService(container, host);
                  if (!service.records().isEmpty()) {
                    services.add(service);
                    publish(service);
                  }
                });
          }

          if (completion != null) {
            completion.handle(Future.succeededFuture());
          }
        }
    );
  }

  private void publish(DockerService service) {
    for (Record record : service.records()) {
      discovery.publish(record, ar -> {
        if (ar.succeeded()) {
          record.setRegistration(ar.result().getRegistration());
          LOGGER.info("Service from container " + service.id() + " on location "
              + record.getLocation() + " has been published");
        } else {
          LOGGER.error("Service from container " + service.id() + " on location "
              + record.getLocation() + " could not have been published", ar.cause());
        }
      });
    }
  }

  private void unpublish(DockerService service) {
    for (Record record : service.records()) {
      discovery.unpublish(record.getRegistration(), ar -> {
        LOGGER.info("Service from container " + service.id()
            + " on location " + record.getLocation() + " has been unpublished");
      });
    }
  }

  private boolean isKnown(Container container) {
    for (DockerService service : services) {
      if (service.id().equalsIgnoreCase(container.getId())) {
        return true;
      }
    }
    return false;
  }

  private boolean isNotRunning(String containerId, List<Container> running) {
    if (running == null) {
      // No running container
      return true;
    }

    for (Container container : running) {
      if (container.getId().equalsIgnoreCase(containerId)) {
        // Found in the running list
        return false;
      }
    }

    // Not found in the running list
    return true;
  }

  /**
   * Stops the bridge.
   *
   * @param vertx     the vert.x instance
   * @param discovery the discovery service
   */
  @Override
  public void stop(Vertx vertx, DiscoveryService discovery) {
    vertx.cancelTimer(timer);
    try {
      client.close();
    } catch (IOException e) {
      throw new RuntimeException("Exception caught while closing the docker client", e);
    }
  }

  synchronized List<DockerService> getServices() {
    return new ArrayList<>(services);
  }
}
