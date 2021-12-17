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
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.ServiceDiscovery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DockerBridgeTest {


  private Vertx vertx;
  private ServiceDiscovery discovery;
  private DockerServiceImporter bridge;
  private DockerClient client;

  @Before
  public void setUp() {
    init();

    DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();

    client = DockerClientImpl.getInstance(config, new ApacheDockerHttpClient.Builder()
      .dockerHost(config.getDockerHost())
      .sslConfig(config.getSSLConfig())
      .maxConnections(100)
      .connectionTimeout(Duration.ofSeconds(30))
      .responseTimeout(Duration.ofSeconds(45))
      .build());
    List<Container> running = client.listContainersCmd().withStatusFilter(Collections.singletonList("running")).exec();
    if (running != null) {
      running.forEach(container -> client.stopContainerCmd(container.getId()).exec());
    }

    vertx = Vertx.vertx();
    discovery = ServiceDiscovery.create(vertx);
    bridge = new DockerServiceImporter();
    discovery.registerServiceImporter(bridge,
        new JsonObject().put("scan-period", -1));

    await().until(() -> bridge.started);
  }

  private void init() {
    File home = new File(System.getProperty("user.home"));
    String os = System.getProperty("os.name");
    if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
      System.setProperty("DOCKER_HOST", "tcp://localhost:2376");
      System.setProperty("DOCKER_CERT_PATH", new File(home, ".docker/certs").getAbsolutePath());
    } else {
      System.setProperty("DOCKER_HOST", "tcp://192.168.99.100:2376");
      System.setProperty("DOCKER_CERT_PATH",
          new File(home, ".docker/machine/machines/dev").getAbsolutePath());
    }
  }

  @After
  public void tearDown() throws IOException {
    List<Container> running = client.listContainersCmd()
        .withStatusFilter(Collections.singletonList("running")).exec();
    if (running != null) {
      running.forEach(container -> client.stopContainerCmd(container.getId()).exec());
    }
    client.close();
    discovery.close();
    vertx.close();
  }

  @Test
  public void testWithNoContainers() throws InterruptedException {
    AtomicBoolean done = new AtomicBoolean();
    Promise<Void> promise = Promise.promise();
    promise.future().onComplete(ar -> done.set(ar.succeeded()));
    bridge.scan(promise);

    await().untilAtomic(done, is(true));
    assertThat(bridge.getServices()).isEmpty();
  }

  @Test
  public void testWithAContainerWithTwoPorts() throws InterruptedException {
    CreateContainerResponse container = client.createContainerCmd("nginx")
        .withExposedPorts(ExposedPort.tcp(80), ExposedPort.tcp(443))
        .withPortBindings(PortBinding.parse("80"), PortBinding.parse("443"))
        .exec();

    AtomicBoolean done = new AtomicBoolean();
    Promise<Void> promise = Promise.promise();
    promise.future().onComplete(ar -> done.set(ar.succeeded()));
    bridge.scan(promise);
    await().untilAtomic(done, is(true));
    assertThat(bridge.getServices()).hasSize(0);

    done.set(false);
    client.startContainerCmd(container.getId()).exec();
    Promise<Void> promise2 = Promise.promise();
    promise2.future().onComplete(ar -> done.set(ar.succeeded()));
    bridge.scan(promise2);
    await().untilAtomic(done, is(true));

    assertThat(bridge.getServices()).hasSize(1);
    DockerService service = bridge.getServices().get(0);
    assertThat(service.records()).hasSize(2);

    client.stopContainerCmd(container.getId()).exec();


    done.set(false);
    Promise<Void> promise3 = Promise.promise();
    promise3.future().onComplete(ar -> done.set(ar.succeeded()));
    bridge.scan(promise3);
    await().untilAtomic(done, is(true));
    assertThat(bridge.getServices()).hasSize(0);

  }

  @Test
  public void testWithAContainerWithAPort() throws InterruptedException {
    CreateContainerResponse container = client.createContainerCmd("nginx")
        .withExposedPorts(ExposedPort.tcp(80), ExposedPort.tcp(443))
        .withPortBindings(PortBinding.parse("80"))
        .exec();

    AtomicBoolean done = new AtomicBoolean();
    Promise<Void> promise = Promise.promise();
    promise.future().onComplete(ar -> done.set(ar.succeeded()));
    bridge.scan(promise);
    await().untilAtomic(done, is(true));
    assertThat(bridge.getServices()).hasSize(0);

    done.set(false);
    client.startContainerCmd(container.getId()).exec();
    Promise<Void> promise2 = Promise.promise();
    promise2.future().onComplete(ar -> done.set(ar.succeeded()));
    bridge.scan(promise2);
    await().untilAtomic(done, is(true));

    assertThat(bridge.getServices()).hasSize(1);
    DockerService service = bridge.getServices().get(0);
    assertThat(service.records()).hasSize(1);
  }

  @Test
  public void testWithAContainerWithLabels() throws InterruptedException {
    Map<String, String> labels = new LinkedHashMap<>();
    labels.put("service.type", "http-endpoint");
    labels.put("ssl", "true");
    CreateContainerResponse container = client.createContainerCmd("nginx")
        .withExposedPorts(ExposedPort.tcp(80), ExposedPort.tcp(443))
        .withPortBindings(PortBinding.parse("80"))
        .withLabels(labels)
        .exec();

    AtomicBoolean done = new AtomicBoolean();
    Promise<Void> promise = Promise.promise();
    promise.future().onComplete(ar -> done.set(ar.succeeded()));
    bridge.scan(promise);
    await().untilAtomic(done, is(true));
    assertThat(bridge.getServices()).hasSize(0);

    done.set(false);
    client.startContainerCmd(container.getId()).exec();
    Promise<Void> promise2 = Promise.promise();
    promise2.future().onComplete(ar -> done.set(ar.succeeded()));
    bridge.scan(promise2);
    await().untilAtomic(done, is(true));

    assertThat(bridge.getServices()).hasSize(1);
    DockerService service = bridge.getServices().get(0);
    assertThat(service.records()).hasSize(1);
    assertThat(service.records().get(0).getLocation().getString("endpoint")).startsWith("https");
  }


}
