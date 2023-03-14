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

package io.vertx.servicediscovery.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.spi.ServiceImporter;
import io.vertx.servicediscovery.spi.ServicePublisher;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A discovery bridge collecting services from Docker, and importing them in the Vert.x
 * discovery infrastructure.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DockerServiceImporter implements ServiceImporter {

  private final static Logger LOGGER = LoggerFactory.getLogger(DockerServiceImporter.class);

  private long timer;
  private DockerClient client;

  private List<DockerService> services = new ArrayList<>();
  private ServicePublisher publisher;
  private Vertx vertx;
  private String host;

  volatile boolean started;

  /**
   * Starts the bridge.
   *
   * @param vertx         the vert.x instance
   * @param publisher     the service discovery instance
   * @param configuration the bridge configuration if any
   * @param completion    future to assign with completion status
   */
  @Override
  public void start(Vertx vertx, ServicePublisher publisher, JsonObject configuration, Promise<Void> completion) {
    this.publisher = publisher;
    this.vertx = vertx;
    DefaultDockerClientConfig.Builder builder = DefaultDockerClientConfig.createDefaultConfigBuilder();
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
        completion.fail(e);
      }
    } else {
      this.host = config.getDockerHost().getHost();
    }

    client = DockerClientImpl.getInstance(config, new ApacheDockerHttpClient.Builder()
      .dockerHost(config.getDockerHost())
      .sslConfig(config.getSSLConfig())
      .maxConnections(100)
      .connectionTimeout(Duration.ofSeconds(30))
      .responseTimeout(Duration.ofSeconds(45))
      .build());

    long period = configuration.getLong("scan-period", 3000L);
    if (period > 0) {
      timer = vertx.setPeriodic(period, l -> {
        scan(null);
      });
    }
    scan(completion);
  }

  synchronized void scan(Promise<Void> completion) {
    vertx.<List<Container>>executeBlocking(
        future -> {
          try {
            future.complete(client.listContainersCmd().withStatusFilter(Collections.singletonList("running")).exec());
          } catch (Exception e) {
            future.fail(e);
          }
        }).onComplete(ar -> {
          if (ar.failed()) {
            if (completion != null) {
              completion.fail(ar.cause());
            } else {
              LOGGER.error("Fail to import services from docker", ar.cause());
            }
            return;
          }
          started = true;
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
            completion.complete();
          }
        }
    );
  }

  private void publish(DockerService service) {
    for (Record record : service.records()) {
      publisher.publish(record).onComplete(ar -> {
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
      publisher.unpublish(record.getRegistration()).onComplete(ar -> {
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

  @Override
  public void close(Handler<Void> completionHandler) {
    vertx.cancelTimer(timer);
    try {
      started = false;
      client.close();
      LOGGER.info("Successfully closed the service importer " + this);
    } catch (IOException e) {
      LOGGER.error("A failure has been caught while stopping " + this, e);
    }
    if (completionHandler != null) {
      completionHandler.handle(null);
    }
  }

  synchronized List<DockerService> getServices() {
    return new ArrayList<>(services);
  }
}
