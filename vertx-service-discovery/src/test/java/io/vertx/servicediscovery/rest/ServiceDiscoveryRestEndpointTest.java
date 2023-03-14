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

package io.vertx.servicediscovery.rest;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.*;
import io.vertx.servicediscovery.impl.DiscoveryImpl;
import io.vertx.servicediscovery.service.HelloService;
import io.vertx.servicediscovery.service.HelloServiceImpl;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.serviceproxy.ProxyHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.awaitility.Awaitility.await;
import static io.vertx.servicediscovery.Restafari.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ServiceDiscoveryRestEndpointTest {

  protected Vertx vertx;
  protected ServiceDiscovery discovery;
  private HttpServer http;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    discovery = new DiscoveryImpl(vertx, new ServiceDiscoveryOptions());

    Router router = Router.router(vertx);
    ServiceDiscoveryRestEndpoint.create(router, discovery);

    AtomicBoolean done = new AtomicBoolean();
    http = vertx.createHttpServer().requestHandler(router);
    http.listen(8080).onComplete(ar -> done.set(ar.succeeded()));

    await().untilAtomic(done, is(true));
  }

  @After
  public void tearDown() {
    discovery.close();

    AtomicBoolean completed = new AtomicBoolean();
    http.close().onComplete(ar -> completed.set(true));
    await().untilAtomic(completed, is(true));

    completed.set(false);
    vertx.close().onComplete((v) -> completed.set(true));
    await().untilAtomic(completed, is(true));
  }

  @Test
  public void testThatWeGetThePublishedServices() {
    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");
    Record record = new Record()
      .setName("Hello")
      .setLocation(new JsonObject().put(Record.ENDPOINT, "address"));

    discovery.publish(record);
    await().until(() -> record.getRegistration() != null);

    Restafari.Response response = get("/discovery");
    JsonArray services = new JsonArray(response.asString());

    assertThat(services.size()).isEqualTo(1);
    Record rec = new Record(services.getJsonObject(0));
    assertThat(rec.getStatus()).isEqualTo(Status.UP);
    assertThat(rec.getRegistration()).isNotNull();
    assertThat(rec.getName()).isEqualTo("Hello");

    AtomicBoolean done = new AtomicBoolean();
    discovery.unpublish(record.getRegistration()).onComplete(ar -> done.set(true));
    await().untilAtomic(done, is(true));

    response = get("/discovery");
    services = new JsonArray(response.asString());

    assertThat(services.size()).isEqualTo(0);

  }

  @Test
  public void testThatWeGetTheTwoPublishedServicesWithMetadata() {
    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");

    Record record1 = EventBusService.createRecord("Hello", "address", HelloService.class,
      new JsonObject().put("key", "foo"));
    Record record2 = EventBusService.createRecord("Hello-2", "address", HelloService.class,
      new JsonObject().put("key", "bar"));

    discovery.publish(record1);
    discovery.publish(record2);

    await().until(() -> record1.getRegistration() != null);
    await().until(() -> record2.getRegistration() != null);

    Restafari.Response response = get("/discovery");
    JsonArray services = new JsonArray(response.asString());

    assertThat(services.size()).isEqualTo(2);

    for (Object json : services) {
      Record rec = new Record((JsonObject) json);
      assertThat(rec.getStatus()).isEqualTo(Status.UP);
      assertThat(rec.getRegistration()).isNotNull();
      assertThat(rec.getName()).startsWith("Hello");
      assertThat(rec.getMetadata().getString("key")).isNotNull();

      get("/discovery/" + rec.getRegistration()).then().body("name", not(nullValue()));
    }
  }

  @Test
  public void testPublicationAndUnpublicationFromTheRestAPI() {
    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");
    Record record = new Record()
      .setName("Hello")
      .setLocation(new JsonObject().put(Record.ENDPOINT, "address"));

    Restafari.Response response1 = given().request().body(record.toJson().toString()).post("/discovery");
    assertThat(response1.getStatusCode()).isEqualTo(201);
    String reg = new JsonObject(response1.asString()).getString("registration");
    assertThat(reg).isNotNull();

    Restafari.Response response = get("/discovery");
    JsonArray services = new JsonArray(response.asString());

    assertThat(services.size()).isEqualTo(1);
    Record rec = new Record(services.getJsonObject(0));
    assertThat(rec.getStatus()).isEqualTo(Status.UP);
    assertThat(rec.getRegistration()).isEqualTo(reg);
    assertThat(rec.getName()).isEqualTo("Hello");

    Restafari.Response response2 = delete("/discovery/" + reg);
    assertThat(response2.getStatusCode()).isEqualTo(204);

    response = get("/discovery");
    services = new JsonArray(response.asString());

    assertThat(services.size()).isEqualTo(0);

    // Missing...
    response2 = delete("/discovery/" + reg);
    assertThat(response2.getStatusCode()).isEqualTo(500);
  }

  @Test
  public void testUpdate() throws UnsupportedEncodingException {
    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");
    Record record = new Record()
      .setName("Hello")
      .setLocation(new JsonObject().put(Record.ENDPOINT, "address"));

    discovery.publish(record);
    await().until(() -> record.getRegistration() != null);

    Record retrieved = retrieve(record.getRegistration());
    assertThat(retrieved.getStatus()).isEqualTo(Status.UP);

    retrieved.setStatus(Status.OUT_OF_SERVICE).getMetadata().put("foo", "bar");

    Restafari.Response response = given().body(retrieved.toJson().toString())
      .put("/discovery/" + record.getRegistration());
    assertThat(response.getStatusCode()).isEqualTo(200);
    retrieved = new Record(new JsonObject(response.asString()));

    assertThat(retrieved.getStatus()).isEqualTo(Status.OUT_OF_SERVICE);
    assertThat(retrieved.getMetadata().getString("foo")).isEqualTo("bar");

    // Check that we cannot find the service without specifying the Status
    response = get("/discovery/");
    JsonArray services = new JsonArray(response.asString());
    assertThat(services.size()).isEqualTo(0);

    services = given()
      .param("query", "{\"status\":\"*\"}")
      .get("/discovery")
      .asJsonArray();

    assertThat(services.size()).isEqualTo(1);
  }

  @Test
  public void testLookupWithQuery() throws UnsupportedEncodingException {
    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");

    Record record1 = EventBusService.createRecord("Hello", "address", HelloService.class,
      new JsonObject().put("key", "foo"));
    Record record2 = EventBusService.createRecord("Hello-2", "address", HelloService.class,
      new JsonObject().put("key", "bar"));

    discovery.publish(record1);
    discovery.publish(record2);

    await().until(() -> record1.getRegistration() != null);
    await().until(() -> record2.getRegistration() != null);


    JsonArray services =
      given()
        .param("query", "{\"name\":\"Hello\"}")
        .get("/discovery")
        .asJsonArray();

    assertThat(services.size()).isEqualTo(1);
  }

  @Test
  public void testLookupWithNonMatchingQuery() throws UnsupportedEncodingException {
    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");

    Record record1 = EventBusService.createRecord("Hello", "address", HelloService.class,
      new JsonObject().put("key", "foo"));
    Record record2 = EventBusService.createRecord("Hello-2", "address", HelloService.class,
      new JsonObject().put("key", "bar"));

    discovery.publish(record1);
    discovery.publish(record2);

    await().until(() -> record1.getRegistration() != null);
    await().until(() -> record2.getRegistration() != null);

    JsonArray services = given()
      .param("query", "{\"stuff\":\"*\"}")
      .get("/discovery")
      .asJsonArray();

    assertThat(services.size()).isEqualTo(0);
  }

  @Test
  public void testFailedPublication() {
    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");
    Record record = new Record()
      .setName("Hello")
      .setRegistration("this-is-not-allowed")
      .setLocation(new JsonObject().put(Record.ENDPOINT, "address"));

    Restafari.Response response = given().request().body(record.toJson().toString()).post("/discovery");
    assertThat(response.getStatusCode()).isEqualTo(500);
  }

  @Test
  public void testRetrievingMissingRecord() {
    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");
    Record record = new Record()
      .setName("Hello")
      .setLocation(new JsonObject().put(Record.ENDPOINT, "address"));

    discovery.publish(record);
    await().until(() -> record.getRegistration() != null);

    Record retrieved = retrieve(record.getRegistration());
    assertThat(retrieved.getStatus()).isEqualTo(Status.UP);

    // Unregister it
    Restafari.Response response2 = delete("/discovery/" + record.getRegistration());
    assertThat(response2.getStatusCode()).isEqualTo(204);

    Restafari.Response response = get("/discovery/" + record.getRegistration());
    assertThat(response.getStatusCode()).isEqualTo(404);
  }

  @Test
  public void testUpdateWithUUIDMismatch() throws UnsupportedEncodingException {
    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");
    Record record = new Record()
      .setName("Hello")
      .setLocation(new JsonObject().put(Record.ENDPOINT, "address"));

    discovery.publish(record);
    await().until(() -> record.getRegistration() != null);

    Record retrieved = retrieve(record.getRegistration());
    assertThat(retrieved.getStatus()).isEqualTo(Status.UP);

    retrieved.setStatus(Status.OUT_OF_SERVICE).setRegistration("not-the-right-one").getMetadata().put("foo", "bar");

    Restafari.Response response = given().body(retrieved.toJson().toString())
      .put("/discovery/" + record.getRegistration());
    assertThat(response.getStatusCode()).isEqualTo(400);
  }

  private Record retrieve(String uuid) {
    return new Record(new JsonObject(get("/discovery/" + uuid).asString()));
  }

}
