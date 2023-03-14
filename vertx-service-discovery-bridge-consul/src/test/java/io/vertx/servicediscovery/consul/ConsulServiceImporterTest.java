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

package io.vertx.servicediscovery.consul;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.HttpEndpoint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Test against a mock Consul server.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ConsulServiceImporterTest {


  private Vertx vertx;

  private List<JsonObject> services = new ArrayList<>();
  private HttpServer server;
  private ServiceDiscovery discovery;

  @Before
  public void setUp() {
    services.clear();
    vertx = Vertx.vertx();

    AtomicBoolean done = new AtomicBoolean();
    server = vertx.createHttpServer()
        .requestHandler(request -> {
          if (request.path().equals("/v1/catalog/services")) {
            JsonObject result = new JsonObject();
            services.forEach(object ->
                result.put(object.getJsonObject("Service").getString("Service"), object.getJsonArray("tags", new JsonArray())));
            request.response().putHeader("X-Consul-Index", "42").end(result.encodePrettily());
          } else if (request.path().startsWith("/v1/health/service/")) {
            String service = request.path().substring("/v1/health/service/".length());
            JsonArray value = find(service);
            if (value != null) {
              request.response().putHeader("X-Consul-Index", "42").end(value.encodePrettily());
            } else {
              request.response().setStatusCode(404).end();
            }
          } else {
            request.response().setStatusCode(404).end();
          }
        });
    server.listen(5601).onComplete(ar -> done.set(ar.succeeded()));

    await().untilAtomic(done, is(true));
  }

  @After
  public void tearDown() {
    if (discovery != null) {
      discovery.close();
    }
    AtomicBoolean done = new AtomicBoolean();
    server.close().onComplete(ar -> done.set(true));
    await().untilAtomic(done, is(true));
    done.set(false);
    vertx.close().onComplete(ar -> done.set(true));
    await().untilAtomic(done, is(true));
  }

  @Test
  public void testBasicImport() {
    services.add(buildService("10.1.10.12", "redis", "redis", null, 8000, "passing"));

    discovery = ServiceDiscovery.create(vertx);
    discovery.registerServiceImporter(new ConsulServiceImporter(),
            new JsonObject().put("host", "localhost").put("port", 5601));

    await().until(() -> getAllRecordsBlocking().size() > 0);
    List<Record> list = getAllRecordsBlocking();

    assertThat(list).hasSize(1);

    Record record = list.get(0);
    assertThat(record.getLocation().getString("host")).isEqualTo("10.1.10.12");
    assertThat(record.getLocation().getInteger("port")).isEqualTo(8000);
    assertThat(record.getLocation().getString("path")).isNull();
    assertThat(record.getName()).isEqualTo("redis");
    assertThat(record.getRegistration()).isNotEmpty();
  }

  @Test
  public void testBasicImportWithEmptyServiceAddress() {
    JsonObject svc = buildService("10.1.10.12", "redis", "redis", null, 8000, "passing");
    services.add(svc.mergeIn(new JsonObject("{\"Service\":{\"Address\":\"\"}}"), true));

    discovery = ServiceDiscovery.create(vertx);
    discovery.registerServiceImporter(new ConsulServiceImporter(),
            new JsonObject().put("host", "localhost").put("port", 5601));

    await().until(() -> getAllRecordsBlocking().size() > 0);
    List<Record> list = getAllRecordsBlocking();

    assertThat(list).hasSize(1);

    Record record = list.get(0);
    assertThat(record.getLocation().getString("host")).isEqualTo("10.1.10.12");
    assertThat(record.getLocation().getInteger("port")).isEqualTo(8000);
    assertThat(record.getLocation().getString("path")).isNull();
    assertThat(record.getName()).isEqualTo("redis");
    assertThat(record.getRegistration()).isNotEmpty();
  }

  @Test
  public void testBasicImportWithNullServiceAddress() {
    JsonObject svc = buildService("10.1.10.12", "redis", "redis", null, 8000, "passing");
    services.add(svc.mergeIn(new JsonObject("{\"Service\":{\"Address\":null}}"), true));

    discovery = ServiceDiscovery.create(vertx);
    discovery.registerServiceImporter(new ConsulServiceImporter(),
            new JsonObject().put("host", "localhost").put("port", 5601));

    await().until(() -> getAllRecordsBlocking().size() > 0);
    List<Record> list = getAllRecordsBlocking();

    assertThat(list).hasSize(1);

    Record record = list.get(0);
    assertThat(record.getLocation().getString("host")).isEqualTo("10.1.10.12");
    assertThat(record.getLocation().getInteger("port")).isEqualTo(8000);
    assertThat(record.getLocation().getString("path")).isNull();
    assertThat(record.getName()).isEqualTo("redis");
    assertThat(record.getRegistration()).isNotEmpty();
  }

  @Test
  public void testDoesNotImportServicesWithWarningStatus() {
    // add 2 services so we can await on 1 being added below
    services.add(buildService("10.1.10.12", "redis", "redis", null, 8000, "passing"));
    services.add(buildService("10.1.10.12", "warning", "warning", null, 8001, "warning"));

    discovery = ServiceDiscovery.create(vertx);
    discovery.registerServiceImporter(new ConsulServiceImporter(),
        new JsonObject().put("host", "localhost").put("port", 5601));

    await().until(() -> getAllRecordsBlocking().size() > 0);
    List<Record> list = getAllRecordsBlocking();

    assertThat(list).hasSize(1);
  }

  @Test
  public void testImportServicesWithWarningStatusThreshold() {
    services.add(buildService("10.1.10.12", "redis", "redis", null, 8000, "passing"));
    services.add(buildService("10.1.10.12", "warning", "warning", null, 8001, "warning"));

    discovery = ServiceDiscovery.create(vertx);
    discovery.registerServiceImporter(new ConsulServiceImporter(),
        new JsonObject().put("host", "localhost").put("port", 5601).put("up_threshold", "warning"));

    await().until(() -> getAllRecordsBlocking().size() > 0);
    List<Record> list = getAllRecordsBlocking();

    assertThat(list).hasSize(2);
  }

  @Test
  public void testHttpImport() {
    services.add(buildService("172.17.0.2", "web", "web", new String[]{"rails", "http-endpoint"}, 80, "passing"));

    discovery = ServiceDiscovery.create(vertx);
    discovery.registerServiceImporter(new ConsulServiceImporter(),
            new JsonObject().put("host", "localhost").put("port", 5601));

    await().until(() -> getAllRecordsBlocking().size() > 0);
    List<Record> list = getAllRecordsBlocking();

    assertThat(list).hasSize(1);

    assertThat(list.get(0).getType()).isEqualTo(HttpEndpoint.TYPE);
    assertThat(list.get(0).getLocation().getString("endpoint")).isEqualTo("http://172.17.0.2:80");
  }

  @Test
  public void testDeparture() {
    services.add(buildService("10.1.10.12", "redis", "redis", null, 8000, "passing"));

    AtomicBoolean initialized = new AtomicBoolean();
    vertx.runOnContext(v -> {
      discovery = ServiceDiscovery.create(vertx);
      discovery.registerServiceImporter(new ConsulServiceImporter(),
        new JsonObject().put("host", "localhost").put("port", 5601).put("scan-period", 100))
        .onComplete(x -> initialized.set(true));
    });

    await().untilAtomic(initialized, is(true));

    await().until(() -> getAllRecordsBlocking().size() > 0);
    List<Record> list = getAllRecordsBlocking();

    assertThat(list).hasSize(1);

    list.clear();
    services.clear();

    await().until(() -> getAllRecordsBlocking().size() == 0);

    assertThat(getAllRecordsBlocking()).isEmpty();
  }

  @Test
  public void testArrivalFollowedByADeparture() {
    JsonObject service = buildService("172.17.0.2", "web", "web", new String[]{"rails", "http-endpoint"}, 80,"passing");
    services.add(buildService("10.1.10.12", "redis", "redis", null, 8000,"passing"));

    AtomicBoolean initialized = new AtomicBoolean();
    vertx.runOnContext(v -> {
        discovery = ServiceDiscovery.create(vertx);
        discovery.registerServiceImporter(new ConsulServiceImporter(),
          new JsonObject().put("host", "localhost").put("port", 5601).put("scan-period", 100)).onComplete(
          x -> initialized.set(true));
      }
    );

    await().untilAtomic(initialized, is(true));

    await().until(() -> getAllRecordsBlocking().size() > 0);
    List<Record> list = getAllRecordsBlocking();

    assertThat(list).hasSize(1);

    services.add(service);

    await().until(() -> getAllRecordsBlocking().size() == 2);

    services.remove(service);

    await().until(() -> getAllRecordsBlocking().size() == 1);
  }

  @Test
  public void testAServiceBeingTwiceInConsul() {
    services.add(buildService("10.4.7.221", "ubuntu221:mysql:3306", "db", new String[] {"master", "backups"}, 32769, "passing"));
    services.add(buildService("10.4.7.220", "ubuntu220:mysql:3306", "db", new String[] {"master", "backups"}, 32771, "passing"));

    discovery = ServiceDiscovery.create(vertx);
    discovery.registerServiceImporter(new ConsulServiceImporter(),
            new JsonObject().put("host", "localhost").put("port", 5601));

    await().until(() -> getAllRecordsBlocking().size() > 0);
    List<Record> list = getAllRecordsBlocking();

    assertThat(list).hasSize(2);
  }

  private JsonArray find(String service) {
    JsonArray array = new JsonArray();
    services.stream().filter(json -> json.getJsonObject("Service").getString("Service").equalsIgnoreCase(service)).forEach(array::add);
    if (! array.isEmpty()) {
      return array;
    }
    return null;
  }

  private List<Record> getAllRecordsBlocking() {
    CountDownLatch latch = new CountDownLatch(1);
    List<Record> list = new ArrayList<>();

    discovery.getRecords((JsonObject) null).onComplete(ar -> {
      list.addAll(ar.result());
      latch.countDown();
    });

    try {
      latch.await();
    } catch (InterruptedException e) {
      // Ignore it.
    }
    return list;
  }

  private JsonObject buildService(String address, String id, String name, String[] tags, int port, String status){
    String tagString = "null";
    if (tags != null && tags.length > 0){
      StringBuilder tagBuilder = new StringBuilder();
      for (String tag : tags){
        tagBuilder.append(",\"").append(tag).append("\"");
      }
      tagString = "[" + tagBuilder.substring(1) + "]";
    }

    return new JsonObject( "  {\n" +
      "    \"Node\": " + aNodeJson(address) + ",\n" +
      "    \"Service\": {" +
      "      \"Address\": \"" + address + "\",\n" +
      "      \"ID\": \"" + id + "\",\n" +
      "      \"Service\": \"" + name + "\",\n" +
      "      \"Tags\": " + tagString + ",\n" +
      "      \"Port\": " + Integer.toString(port) + "\n" +
      "    },\n" +
      "    \"Checks\":[\n" +
              aCheckJson(name, id, tagString, status) + "," +
              aCheckJson(name, "","[]", "passing") +
      "     ]\n" +
      "  }");
  }

  private String aNodeJson(String address){
    return "{" +
      "\"ID\": \"0e95f792-357d-1901-d2d4-b6ae8bd3e881\",\n" +
      "\"Node\": \"6c3429f04f15\",\n" +
      "\"Address\": \"" + address + "\",\n" +
      "\"Datacenter\": \"dc1\",\n" +
      "\"TaggedAddresses\": {\n" +
      "  \"lan\": \"" + address + "\",\n" +
      "  \"wan\": \"" + address + "\"\n" +
      "}\n" +
      "}";
  }

  private String aCheckJson(String serviceName, String id, String tags, String status){
    return "{\n" +
      "        \"Node\": \"6c3429f04f15\",\n" +
      "        \"CheckID\": \"service:" + serviceName + "\",\n" +
      "        \"Name\": \"Service check\",\n" +
      "        \"Status\": \"" + status + "\",\n" +
      "        \"Notes\": \"\",\n" +
      "        \"Output\": \"\",\n" +
      "        \"ServiceID\": \"" + id + "\",\n" +
      "        \"ServiceName\": \"" + serviceName + "\",\n" +
      "        \"ServiceTags\": " + tags + "\n" +
      "}";
  }

}
