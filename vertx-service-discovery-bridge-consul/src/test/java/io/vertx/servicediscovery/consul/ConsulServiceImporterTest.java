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

import java.net.MalformedURLException;
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
                result.put(object.getString("ServiceName"), object.getJsonArray("tags", new JsonArray())));
            request.response().end(result.encodePrettily());
          } else if (request.path().startsWith("/v1/catalog/service/")) {
            String service = request.path().substring("/v1/catalog/service/".length());
            JsonArray value = find(service);
            if (value != null) {
              request.response().end(value.encodePrettily());
            } else {
              request.response().setStatusCode(404).end();
            }
          } else {
            request.response().setStatusCode(404).end();
          }
        })
        .listen(5601, ar -> done.set(ar.succeeded()));

    await().untilAtomic(done, is(true));
  }

  @After
  public void tearDown() {
    if (discovery != null) {
      discovery.close();
    }
    AtomicBoolean done = new AtomicBoolean();
    server.close(ar -> done.set(true));
    await().untilAtomic(done, is(true));
    done.set(false);
    vertx.close(ar -> done.set(true));
    await().untilAtomic(done, is(true));
  }

  @Test
  public void testBasicImport() throws InterruptedException {
    services.add(new JsonObject("  {\n" +
        "    \"Node\": \"foobar\",\n" +
        "    \"Address\": \"10.1.10.12\",\n" +
        "    \"ServiceID\": \"redis\",\n" +
        "    \"ServiceName\": \"redis\",\n" +
        "    \"ServiceTags\": null,\n" +
        "    \"ServiceAddress\": \"\",\n" +
        "    \"ServicePort\": 8000\n" +
        "  }"));

    discovery = ServiceDiscovery.create(vertx)
        .registerServiceImporter(new ConsulServiceImporter(),
            new JsonObject().put("host", "localhost").put("port", 5601));

    await().until(() -> getAllRecordsBlocking().size() > 0);
    List<Record> list = getAllRecordsBlocking();

    assertThat(list).hasSize(1);
  }

  @Test
  public void testHttpImport() throws InterruptedException {
    services.add(new JsonObject("{\n" +
        "  \"Node\" : \"node1\",\n" +
        "  \"Address\" : \"172.17.0.2\",\n" +
        "  \"ServiceID\" : \"web\",\n" +
        "  \"ServiceName\" : \"web\",\n" +
        "  \"ServiceTags\" : [ \"rails\", \"http-endpoint\" ],\n" +
        "  \"ServiceAddress\" : \"\",\n" +
        "  \"ServicePort\" : 80\n" +
        "}"));

    discovery = ServiceDiscovery.create(vertx)
        .registerServiceImporter(new ConsulServiceImporter(),
            new JsonObject().put("host", "localhost").put("port", 5601));

    await().until(() -> getAllRecordsBlocking().size() > 0);
    List<Record> list = getAllRecordsBlocking();

    assertThat(list).hasSize(1);

    assertThat(list.get(0).getType()).isEqualTo(HttpEndpoint.TYPE);
    assertThat(list.get(0).getLocation().getString("endpoint")).isEqualTo("http://172.17.0.2:80/");
  }

  @Test
  public void testDeparture() throws InterruptedException {
    services.add(new JsonObject("  {\n" +
        "    \"Node\": \"foobar\",\n" +
        "    \"Address\": \"10.1.10.12\",\n" +
        "    \"ServiceID\": \"redis\",\n" +
        "    \"ServiceName\": \"redis\",\n" +
        "    \"ServiceTags\": null,\n" +
        "    \"ServiceAddress\": \"\",\n" +
        "    \"ServicePort\": 8000\n" +
        "  }"));

    AtomicBoolean initialized = new AtomicBoolean();
    vertx.runOnContext(v ->
        discovery = ServiceDiscovery.create(vertx)
            .registerServiceImporter(new ConsulServiceImporter(),
                new JsonObject().put("host", "localhost").put("port", 5601).put("scan-period", 100),
        x -> initialized.set(true))
    );

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
  public void testArrivalFollowedByADeparture() throws InterruptedException {
    JsonObject service = new JsonObject("{\n" +
        "  \"Node\" : \"node1\",\n" +
        "  \"Address\" : \"172.17.0.2\",\n" +
        "  \"ServiceID\" : \"web\",\n" +
        "  \"ServiceName\" : \"web\",\n" +
        "  \"ServiceTags\" : [ \"rails\", \"http-endpoint\" ],\n" +
        "  \"ServiceAddress\" : \"\",\n" +
        "  \"ServicePort\" : 80\n" +
        "}");

    services.add(new JsonObject("  {\n" +
        "    \"Node\": \"foobar\",\n" +
        "    \"Address\": \"10.1.10.12\",\n" +
        "    \"ServiceID\": \"redis\",\n" +
        "    \"ServiceName\": \"redis\",\n" +
        "    \"ServiceTags\": null,\n" +
        "    \"ServiceAddress\": \"\",\n" +
        "    \"ServicePort\": 8000\n" +
        "  }"));

    AtomicBoolean initialized = new AtomicBoolean();
    vertx.runOnContext(v ->
        discovery = ServiceDiscovery.create(vertx)
            .registerServiceImporter(new ConsulServiceImporter(),
                new JsonObject().put("host", "localhost").put("port", 5601).put("scan-period", 100),
                x -> initialized.set(true))
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
  public void testAServiceBeingTwiceInConsul() throws InterruptedException {
    services.add(new JsonObject("{\"Node\":\"ubuntu221\",\"Address\":\"10.4.7.221\"," +
        "\"ServiceID\":\"ubuntu221:mysql:3306\",\"ServiceName\":\"db\"," +
        "\"ServiceTags\":[\"master\",\"backups\"],\"ServiceAddress\":\"\",\"ServicePort\":32769}"));
    services.add(new JsonObject("{\"Node\":\"ubuntu220\",\"Address\":\"10.4.7.220\"," +
        "\"ServiceID\":\"ubuntu220:mysql:3306\",\"ServiceName\":\"db\",\"ServiceTags\":[\"master\",\"backups\"]," +
        "\"ServiceAddress\":\"\",\"ServicePort\":32771}"));

    discovery = ServiceDiscovery.create(vertx)
        .registerServiceImporter(new ConsulServiceImporter(),
            new JsonObject().put("host", "localhost").put("port", 5601));

    await().until(() -> getAllRecordsBlocking().size() > 0);
    List<Record> list = getAllRecordsBlocking();

    assertThat(list).hasSize(2);
  }

  private JsonArray find(String service) {
    JsonArray array = new JsonArray();
    services.stream().filter(json -> json.getString("ServiceName").equalsIgnoreCase(service)).forEach(array::add);
    if (! array.isEmpty()) {
      return array;
    }
    return null;
  }

  private List<Record> getAllRecordsBlocking() {
    CountDownLatch latch = new CountDownLatch(1);
    List<Record> list = new ArrayList<>();

    discovery.getRecords((JsonObject) null, ar -> {
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


}