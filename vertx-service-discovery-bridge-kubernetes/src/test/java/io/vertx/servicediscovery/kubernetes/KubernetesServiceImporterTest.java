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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.api.model.*;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.spi.ServiceType;
import io.vertx.servicediscovery.types.HttpEndpoint;
import io.vertx.servicediscovery.types.JDBCDataSource;
import io.vertx.servicediscovery.types.MongoDataSource;
import io.vertx.servicediscovery.types.RedisDataSource;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class KubernetesServiceImporterTest {

  private static final ObjectMapper mapper = new ObjectMapper();

  @Test
  public void testRecordCreation() throws Exception {
    ObjectMeta metadata = new ObjectMeta();
    metadata.setName("my-service");
    metadata.setUid("uuid");
    metadata.setNamespace("my-project");

    ServiceSpec spec = new ServiceSpec();
    ServicePort port = new ServicePort();
    port.setTargetPort(new IntOrString(8080));
    port.setPort(1524);
    spec.setPorts(Collections.singletonList(port));

    Service service = new Service();
    service.setMetadata(metadata);
    service.setSpec(spec);

    Record record = KubernetesServiceImporter.createRecord(new JsonObject(mapper.writeValueAsString(service)));
    assertThat(record).isNotNull();
    assertThat(record.getName()).isEqualTo("my-service");
    assertThat(record.getMetadata().getString("kubernetes.name")).isEqualTo("my-service");
    assertThat(record.getMetadata().getString("kubernetes.namespace")).isEqualTo("my-project");
    assertThat(record.getMetadata().getString("kubernetes.uuid")).isEqualTo("uuid");
    assertThat(record.getType()).isEqualTo(ServiceType.UNKNOWN);
    assertThat(record.getLocation().getInteger("port")).isEqualTo(1524);
  }

  @Test
  public void testHttpRecordCreation() throws Exception {
    Service service = getHttpService();

    Record record = KubernetesServiceImporter.createRecord(new JsonObject(mapper.writeValueAsString(service)));
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

    Service service = new Service();
    service.setMetadata(metadata);
    service.setSpec(spec);
    return service;
  }

  @Test
  public void testHttpWithSSLRecordCreation() throws Exception {
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

    Service service = new Service();
    service.setMetadata(metadata);
    service.setSpec(spec);

    Record record = KubernetesServiceImporter.createRecord(new JsonObject(mapper.writeValueAsString(service)));
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
  public void testServiceTypeDetection() throws Exception {
    Map<String, String> labels = new LinkedHashMap<>();

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

    Service service = new Service();
    service.setMetadata(metadata);
    service.setSpec(spec);

    assertThat(KubernetesServiceImporter.discoveryType(new JsonObject(mapper.writeValueAsString(service)), new Record()))
      .isEqualTo(HttpEndpoint.TYPE);

    port.setPort(443);
    assertThat(KubernetesServiceImporter.discoveryType(new JsonObject(mapper.writeValueAsString(service)), new Record()))
      .isEqualTo(HttpEndpoint.TYPE);

    port.setPort(433);
    assertThat(KubernetesServiceImporter.discoveryType(new JsonObject(mapper.writeValueAsString(service)), new Record()))
      .isEqualTo(ServiceType.UNKNOWN);

    port.setPort(8888);
    assertThat(KubernetesServiceImporter.discoveryType(new JsonObject(mapper.writeValueAsString(service)), new Record()))
      .isEqualTo(HttpEndpoint.TYPE);

    port.setPort(8080);
    assertThat(KubernetesServiceImporter.discoveryType(new JsonObject(mapper.writeValueAsString(service)), new Record()))
      .isEqualTo(HttpEndpoint.TYPE);

    port.setPort(9000);
    assertThat(KubernetesServiceImporter.discoveryType(new JsonObject(mapper.writeValueAsString(service)), new Record()))
      .isEqualTo(HttpEndpoint.TYPE);

    port.setPort(80);
    assertThat(KubernetesServiceImporter.discoveryType(new JsonObject(mapper.writeValueAsString(service)), new Record()))
      .isEqualTo(HttpEndpoint.TYPE);

    port.setPort(6379);
    assertThat(KubernetesServiceImporter.discoveryType(new JsonObject(mapper.writeValueAsString(service)), new Record()))
      .isEqualTo(RedisDataSource.TYPE);

    port.setPort(3306);
    assertThat(KubernetesServiceImporter.discoveryType(new JsonObject(mapper.writeValueAsString(service)), new Record()))
      .isEqualTo(JDBCDataSource.TYPE);

    port.setPort(27017);
    assertThat(KubernetesServiceImporter.discoveryType(new JsonObject(mapper.writeValueAsString(service)), new Record()))
      .isEqualTo(MongoDataSource.TYPE);

    port.setPort(27018);
    assertThat(KubernetesServiceImporter.discoveryType(new JsonObject(mapper.writeValueAsString(service)), new Record()))
      .isEqualTo(MongoDataSource.TYPE);

    port.setPort(27019);
    assertThat(KubernetesServiceImporter.discoveryType(new JsonObject(mapper.writeValueAsString(service)), new Record()))
      .isEqualTo(MongoDataSource.TYPE);

  }

  @Test
  public void testHttpExternalServiceRecordCreation() throws Exception {

    int servicePort = 8080;
    Service service = getExternalService(servicePort);

    Record record = KubernetesServiceImporter.createRecord(new JsonObject(mapper.writeValueAsString(service)));
    assertThat(record).isNotNull();
    assertThat(record.getName()).isEqualTo("my-service");
    assertThat(record.getMetadata().getString("kubernetes.name")).isEqualTo("my-service");
    assertThat(record.getMetadata().getString("kubernetes.namespace")).isEqualTo("my-project");
    assertThat(record.getMetadata().getString("kubernetes.uuid")).isEqualTo("uuid");
    assertThat(record.getType()).isEqualTo(HttpEndpoint.TYPE);
    assertThat(record.getLocation().getString("host")).isEqualTo("my-external-service");
    assertThat(record.getLocation().getInteger("port")).isEqualTo(servicePort);
    assertThat(record.getLocation().getBoolean("ssl")).isFalse();
    assertThat(record.getLocation().getString("endpoint")).isEqualTo("http://my-external-service:" + servicePort);

  }

  @Test
  public void testHttpWithSSLExternalServiceRecordCreation() throws Exception {

    int servicePort = 443;
    Service service = getExternalService(servicePort);

    Record record = KubernetesServiceImporter.createRecord(new JsonObject(mapper.writeValueAsString(service)));
    assertThat(record).isNotNull();
    assertThat(record.getName()).isEqualTo("my-service");
    assertThat(record.getMetadata().getString("kubernetes.name")).isEqualTo("my-service");
    assertThat(record.getMetadata().getString("kubernetes.namespace")).isEqualTo("my-project");
    assertThat(record.getMetadata().getString("kubernetes.uuid")).isEqualTo("uuid");
    assertThat(record.getType()).isEqualTo(HttpEndpoint.TYPE);
    assertThat(record.getLocation().getString("host")).isEqualTo("my-external-service");
    assertThat(record.getLocation().getInteger("port")).isEqualTo(servicePort);
    assertThat(record.getLocation().getBoolean("ssl")).isTrue();
    assertThat(record.getLocation().getString("endpoint")).isEqualTo("https://my-external-service:" + servicePort);

  }

  @Test
  public void testUnknownExternalServiceRecordCreation() throws Exception {

    // JDBC Example
    int servicePort = 5432;
    Service service = getExternalService(servicePort);

    Record record = KubernetesServiceImporter.createRecord(new JsonObject(mapper.writeValueAsString(service)));
    assertThat(record).isNotNull();
    assertThat(record.getName()).isEqualTo("my-service");
    assertThat(record.getMetadata().getString("kubernetes.name")).isEqualTo("my-service");
    assertThat(record.getMetadata().getString("kubernetes.namespace")).isEqualTo("my-project");
    assertThat(record.getMetadata().getString("kubernetes.uuid")).isEqualTo("uuid");
    assertThat(record.getType()).isEqualTo(JDBCDataSource.TYPE);
    assertThat(record.getLocation().getString("host")).isEqualTo("my-external-service");
    assertThat(record.getLocation().getInteger("port")).isEqualTo(servicePort);

  }

  private Service getExternalService(int port) {

    ObjectMeta metadata = new ObjectMeta();
    metadata.setName("my-service");
    metadata.setUid("uuid");
    metadata.setNamespace("my-project");

    ServiceSpec serviceSpec = new ServiceSpec();
    ServicePort servicePort = new ServicePort();
    servicePort.setPort(port);
    serviceSpec.setType("ExternalName");
    serviceSpec.setExternalName("my-external-service");
    serviceSpec.setPorts(Collections.singletonList(servicePort));

    Service service = new Service();
    service.setMetadata(metadata);
    service.setSpec(serviceSpec);
    return service;

  }

}
