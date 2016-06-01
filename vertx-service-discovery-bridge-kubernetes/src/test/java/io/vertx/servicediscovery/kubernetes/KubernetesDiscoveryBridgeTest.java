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

package io.vertx.servicediscovery.kubernetes;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.Watcher;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.DiscoveryService;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.Status;
import io.vertx.servicediscovery.spi.ServiceDiscovery;
import io.vertx.servicediscovery.spi.ServiceType;
import io.vertx.servicediscovery.types.HttpEndpoint;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class KubernetesDiscoveryBridgeTest {

  @Test
  public void testRecordCreation() {
    ObjectMeta metadata = new ObjectMeta();
    metadata.setName("my-service");
    metadata.setUid("uuid");
    metadata.setNamespace("my-project");

    ServiceSpec spec = new ServiceSpec();
    ServicePort port = new ServicePort();
    port.setTargetPort(new IntOrString(8080));
    port.setPort(8080);
    spec.setPorts(Collections.singletonList(port));

    Service service = mock(Service.class);
    when(service.getMetadata()).thenReturn(metadata);
    when(service.getSpec()).thenReturn(spec);

    Record record = KubernetesDiscoveryBridge.createRecord(service);
    assertThat(record).isNotNull();
    assertThat(record.getName()).isEqualTo("my-service");
    assertThat(record.getMetadata().getString("kubernetes.name")).isEqualTo("my-service");
    assertThat(record.getMetadata().getString("kubernetes.namespace")).isEqualTo("my-project");
    assertThat(record.getMetadata().getString("kubernetes.uuid")).isEqualTo("uuid");
    assertThat(record.getType()).isEqualTo(ServiceType.UNKNOWN);
    assertThat(record.getLocation().getInteger("port")).isEqualTo(8080);
  }

  @Test
  public void testHttpRecordCreation() {
    Service service = getHttpService();

    Record record = KubernetesDiscoveryBridge.createRecord(service);
    assertThat(record).isNotNull();
    assertThat(record.getName()).isEqualTo("my-service");
    assertThat(record.getMetadata().getString("kubernetes.name")).isEqualTo("my-service");
    assertThat(record.getMetadata().getString("kubernetes.namespace")).isEqualTo("my-project");
    assertThat(record.getMetadata().getString("kubernetes.uuid")).isEqualTo("uuid");
    assertThat(record.getType()).isEqualTo(HttpEndpoint.TYPE);
    assertThat(record.getLocation().getInteger("port")).isEqualTo(8080);
    assertThat(record.getLocation().getBoolean("ssl")).isFalse();
  }

  private Service getHttpService() {
    Map<String, String> labels = new LinkedHashMap<>();
    labels.put("service-type", "http-endpoint");

    ObjectMeta metadata = new ObjectMeta();
    metadata.setName("my-service");
    metadata.setUid("uuid");
    metadata.setNamespace("my-project");
    metadata.setLabels(labels);

    ServiceSpec spec = new ServiceSpec();
    ServicePort port = new ServicePort();
    port.setTargetPort(new IntOrString(80));
    port.setPort(8080);
    spec.setPorts(Collections.singletonList(port));

    Service service = mock(Service.class);
    when(service.getMetadata()).thenReturn(metadata);
    when(service.getSpec()).thenReturn(spec);
    return service;
  }

  @Test
  public void testHttpWithSSLRecordCreation() {
    Map<String, String> labels = new LinkedHashMap<>();
    labels.put("service-type", "http-endpoint");
    labels.put("ssl", "true");

    ObjectMeta metadata = new ObjectMeta();
    metadata.setName("my-service");
    metadata.setUid("uuid");
    metadata.setNamespace("my-project");
    metadata.setLabels(labels);

    ServiceSpec spec = new ServiceSpec();
    ServicePort port = new ServicePort();
    port.setTargetPort(new IntOrString(8080));
    port.setPort(8080);
    spec.setPorts(Collections.singletonList(port));

    Service service = mock(Service.class);
    when(service.getMetadata()).thenReturn(metadata);
    when(service.getSpec()).thenReturn(spec);

    Record record = KubernetesDiscoveryBridge.createRecord(service);
    assertThat(record).isNotNull();
    assertThat(record.getName()).isEqualTo("my-service");
    assertThat(record.getMetadata().getString("kubernetes.name")).isEqualTo("my-service");
    assertThat(record.getMetadata().getString("kubernetes.namespace")).isEqualTo("my-project");
    assertThat(record.getMetadata().getString("kubernetes.uuid")).isEqualTo("uuid");
    assertThat(record.getType()).isEqualTo(HttpEndpoint.TYPE);
    assertThat(record.getLocation().getInteger("port")).isEqualTo(8080);
    assertThat(record.getLocation().getBoolean("ssl")).isTrue();
  }

  @Test
  public void testArrivalAndDeparture() {
    Vertx vertx = Vertx.vertx();
    AtomicReference<Record> record = new AtomicReference<>();
    vertx.eventBus().consumer("vertx.discovery.announce", message -> {
      record.set(new Record((JsonObject) message.body()));
    });
    ServiceDiscovery discovery = (ServiceDiscovery) DiscoveryService.create(vertx);
    KubernetesDiscoveryBridge bridge = new KubernetesDiscoveryBridge();
    Future<Void> future = Future.future();
    bridge.start(vertx, discovery, new JsonObject().put("token", "a token"), future);
    future.setHandler(ar -> {

    });
    bridge.eventReceived(Watcher.Action.ADDED, getHttpService());

    await().until(() -> record.get() != null);
    assertThat(record.get().getStatus()).isEqualTo(Status.UP);

    record.set(null);
    bridge.eventReceived(Watcher.Action.DELETED, getHttpService());
    await().until(() -> record.get() != null);
    assertThat(record.get().getStatus()).isEqualTo(Status.DOWN);

    bridge.stop(vertx, discovery, Future.future());

  }

}