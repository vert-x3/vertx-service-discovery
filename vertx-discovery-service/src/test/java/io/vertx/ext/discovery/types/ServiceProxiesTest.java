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

package io.vertx.ext.discovery.types;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.discovery.DiscoveryOptions;
import io.vertx.ext.discovery.DiscoveryService;
import io.vertx.ext.discovery.Record;
import io.vertx.ext.discovery.ServiceReference;
import io.vertx.ext.discovery.impl.DiscoveryImpl;
import io.vertx.ext.service.HelloService;
import io.vertx.ext.service.HelloServiceImpl;
import io.vertx.serviceproxy.ProxyHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ServiceProxiesTest {

  private Vertx vertx;
  private DiscoveryService discovery;

  private JsonObject name = new JsonObject().put("name", "vert.x");

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    discovery = new DiscoveryImpl(vertx, new DiscoveryOptions());
  }

  @After
  public void tearDown() {
    discovery.close();
    AtomicBoolean completed = new AtomicBoolean();
    vertx.close((v) -> completed.set(true));
    await().untilAtomic(completed, is(true));
  }

  @Test
  public void test() {
    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");
    Record record = EventBusService.createRecord("Hello", "address", HelloService.class);

    discovery.publish(record, (r) -> {
    });
    await().until(() -> record.getRegistration() != null);

    AtomicReference<Record> found = new AtomicReference<>();
    discovery.getRecord(new JsonObject().put("name", "Hello"), ar -> {
      found.set(ar.result());
    });

    await().until(() -> found.get() != null);
    assertThat(found.get().getLocation().getString("endpoint")).isEqualTo("address");

    ServiceReference service = discovery.getReference(found.get());
    HelloService hello = service.get();
    AtomicReference<String> result = new AtomicReference<>();
    hello.hello(name, ar -> {
      result.set(ar.result());
    });
    await().untilAtomic(result, not(nullValue()));

    service.release();
  }

  @Test
  public void testUsingGetMethod() {
    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");
    Record record = EventBusService.createRecord("Hello", "address", HelloService.class);

    discovery.publish(record, (r) -> {
    });
    await().until(() -> record.getRegistration() != null);

    AtomicReference<HelloService> found = new AtomicReference<>();
    EventBusService.getProxy(discovery, HelloService.class, ar -> {
      found.set(ar.result());
    });
    await().until(() -> found.get() != null);

    assertThat(discovery.bindings()).hasSize(1);

    HelloService hello = found.get();
    AtomicReference<String> result = new AtomicReference<>();
    hello.hello(name, ar -> {
      result.set(ar.result());
    });
    await().untilAtomic(result, not(nullValue()));

    DiscoveryService.releaseServiceObject(discovery, found.get());

    assertThat(discovery.bindings()).hasSize(0);
  }


  @Test
  public void testWithGroovyConsumer() {
    // Step 1 - register the service
    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");
    Record record = EventBusService.createRecord("Hello", "address", HelloService.class);

    discovery.publish(record, (r) -> {
    });
    await().until(() -> record.getRegistration() != null);

    // Step 2 - register a consumer that get the result
    AtomicReference<JsonObject> result = new AtomicReference<>();
    vertx.eventBus().<JsonObject>consumer("result", message -> {
      result.set(message.body());
    });

    // Step 3 - deploy the verticle
    vertx.deployVerticle("verticles/HelloServiceConsumer.groovy", ar -> {
      if (ar.failed()) {
        // Will fail anyway.
        ar.cause().printStackTrace();
      }
    });

    await().until(() -> result.get() != null);

    assertThat(result.get().getString("status")).isEqualTo("ok");
    assertThat(result.get().getString("message")).isEqualTo("stuff vert.x");
  }

  @Test
  public void testWithJavaScriptConsumer() {
    // Step 1 - register the service
    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");
    Record record = EventBusService.createRecord("Hello", "address", HelloService.class);

    discovery.publish(record, (r) -> {
    });
    await().until(() -> record.getRegistration() != null);

    // Step 2 - register a consumer that get the result
    AtomicReference<JsonObject> result = new AtomicReference<>();
    vertx.eventBus().<JsonObject>consumer("result", message -> {
      result.set(message.body());
    });

    // Step 3 - deploy the verticle
    vertx.deployVerticle("verticles/HelloServiceConsumer.js", ar -> {
      if (ar.failed()) {
        // Will fail anyway.
        ar.cause().printStackTrace();
      }
    });

    await().until(() -> result.get() != null);

    assertThat(result.get().getString("status")).isEqualTo("ok");
    assertThat(result.get().getString("message")).isEqualTo("stuff vert.x");
  }

  @Test
  public void testSeveralCallsToRelease() {
    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");
    Record record = EventBusService.createRecord("Hello", "address", HelloService.class);

    discovery.publish(record, (r) -> {
    });
    await().until(() -> record.getRegistration() != null);

    AtomicReference<Record> found = new AtomicReference<>();
    discovery.getRecord(new JsonObject().put("name", "Hello"), ar -> {
      found.set(ar.result());
    });

    await().until(() -> found.get() != null);

    ServiceReference service = discovery.getReference(found.get());
    HelloService hello = service.get();
    AtomicReference<String> result = new AtomicReference<>();
    hello.hello(name, ar -> {
      result.set(ar.result());
    });
    await().untilAtomic(result, not(nullValue()));

    service.release();
    service.release();
  }

}
