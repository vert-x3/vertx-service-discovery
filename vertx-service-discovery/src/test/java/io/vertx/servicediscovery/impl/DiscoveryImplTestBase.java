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

package io.vertx.servicediscovery.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.servicediscovery.*;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.service.HelloService;
import io.vertx.servicediscovery.service.HelloServiceImpl;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackend;
import io.vertx.servicediscovery.spi.ServiceExporter;
import io.vertx.servicediscovery.spi.ServiceImporter;
import io.vertx.servicediscovery.spi.ServicePublisher;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.servicediscovery.types.HttpEndpoint;
import io.vertx.serviceproxy.ProxyHelper;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public abstract class DiscoveryImplTestBase {


  protected Vertx vertx;
  protected ServiceDiscovery discovery;

  @After
  public void tearDown() {
    discovery.close();
    AtomicBoolean completed = new AtomicBoolean();
    vertx.close().onComplete((v) -> completed.set(true));
    await().untilAtomic(completed, is(true));

    Assertions.assertThat(discovery.bindings()).isEmpty();
  }

  @Test
  public void testPublicationAndLookupById() {
    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");
    Record record = new Record()
      .setName("Hello")
      .setType(EventBusService.TYPE)
      .setLocation(new JsonObject().put(Record.ENDPOINT, "address"))
      .setMetadata(new JsonObject().put("service.interface", HelloService.class.getName()));

    discovery.publish(record);
    await().until(() -> record.getRegistration() != null);

    AtomicReference<Record> found = new AtomicReference<>();
    discovery.getRecord(record.getRegistration()).onComplete(ar -> {
      found.set(ar.result());
    });

    await().until(() -> found.get() != null);
    assertThat(found.get().getLocation().getString("endpoint")).isEqualTo("address");
    assertThat(discovery.options().toJson()).isNotEmpty();
  }

  @Test
  public void testPublicationAndSimpleLookup() {
    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");
    Record record = new Record()
      .setName("Hello")
      .setType(EventBusService.TYPE)
      .setLocation(new JsonObject().put(Record.ENDPOINT, "address"))
      .setMetadata(new JsonObject().put("service.interface", HelloService.class.getName()));

    discovery.publish(record);
    await().until(() -> record.getRegistration() != null);

    AtomicReference<Record> found = new AtomicReference<>();
    discovery.getRecord(new JsonObject().put("name", "Hello")).onComplete(ar -> {
      found.set(ar.result());
    });

    await().until(() -> found.get() != null);
    assertThat(found.get().getLocation().getString("endpoint")).isEqualTo("address");
    assertThat(discovery.options().toJson()).isNotEmpty();

    ServiceReference reference = discovery.getReference(found.get());
    assertThat(reference).isNotNull();
    HelloService service = reference.get();
    assertThat(service).isNotNull();
    AtomicReference<String> result = new AtomicReference<>();
    service.hello(new JsonObject().put("name", "foo")).onComplete(ar -> result.set(ar.result()));
    await().until(() -> result.get() != null);
    assertThat(result.get()).isEqualToIgnoringCase("stuff foo");

    Assertions.assertThat(discovery.bindings()).hasSize(1);

    AtomicBoolean done = new AtomicBoolean();
    discovery.unpublish(record.getRegistration()).onComplete(v -> done.set(v.succeeded()));

    await().untilAtomic(done, is(true));

    found.set(null);
    done.set(false);
    discovery.getRecord(new JsonObject().put("name", "Hello")).onComplete(ar -> {
      found.set(ar.result());
      done.set(true);
    });

    await().untilAtomic(done, is(true));
    assertThat(found.get()).isNull();
  }

  @Test
  public void testPublicationAndFilteredLookup() {
    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");
    Record record = new Record()
      .setName("Hello")
      .setMetadata(new JsonObject().put("key", "A"))
      .setLocation(new JsonObject().put(Record.ENDPOINT, "address"));
    Record record2 = new Record()
      .setName("Hello-2")
      .setMetadata(new JsonObject().put("key", "B"))
      .setLocation(new JsonObject().put(Record.ENDPOINT, "address2"));
    discovery.publish(record);
    discovery.publish(record2);
    await().until(() -> record.getRegistration() != null);
    await().until(() -> record2.getRegistration() != null);

    AtomicReference<Record> found = new AtomicReference<>();
    discovery.getRecord(new JsonObject().put("key", "A")).onComplete(ar -> {
      found.set(ar.result());
    });
    await().until(() -> found.get() != null);
    assertThat(found.get().getLocation().getString("endpoint")).isEqualTo("address");

    found.set(null);
    discovery.getRecord(new JsonObject().put("key", "B")).onComplete(ar -> {
      found.set(ar.result());
    });
    await().until(() -> found.get() != null);
    assertThat(found.get().getLocation().getString("endpoint")).isEqualTo("address2");

    found.set(null);
    AtomicBoolean done = new AtomicBoolean();
    discovery.getRecord(new JsonObject().put("key", "C")).onComplete(ar -> {
      found.set(ar.result());
      done.set(true);
    });
    await().untilAtomic(done, is(true));
    assertThat(found.get()).isNull();

    found.set(null);
    done.set(false);
    discovery.getRecord(new JsonObject().put("key", "B").put("foo", "bar")).onComplete(ar -> {
      found.set(ar.result());
      done.set(true);
    });
    await().untilAtomic(done, is(true));
    assertThat(found.get()).isNull();
  }

  @Test
  public void testAnnounce() {
    List<Record> announces = new ArrayList<>();

    vertx.eventBus().consumer(ServiceDiscoveryOptions.DEFAULT_ANNOUNCE_ADDRESS,
      msg -> announces.add(new Record((JsonObject) msg.body())));

    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");
    Record record = new Record()
      .setName("Hello")
      .setMetadata(new JsonObject().put("key", "A"))
      .setLocation(new JsonObject().put(Record.ENDPOINT, "address"));
    Record record2 = new Record()
      .setName("Hello-2")
      .setMetadata(new JsonObject().put("key", "B"))
      .setLocation(new JsonObject().put(Record.ENDPOINT, "address2"));
    discovery.publish(record);
    discovery.publish(record2);
    await().until(() -> record.getRegistration() != null);
    await().until(() -> record2.getRegistration() != null);

    await().until(() -> announces.size() == 2);
    for (Record rec : announces) {
      assertThat(rec.getStatus()).isEqualTo(Status.UP);
    }

    discovery.unpublish(record2.getRegistration());

    await().until(() -> announces.size() == 3);
    assertThat(announces.get(2).getStatus()).isEqualTo(Status.DOWN);
  }

  @Test
  public void testAnnouncementComesAfterPublishIsComplete() {
    ServiceDiscoveryBackend slowBackend = new ServiceDiscoveryBackend() {

      private AsyncMap<String, String> registry;

      @Override
      public String name() {
        return "slow-backend";
      }

      @Override
      public void init(Vertx vertx, JsonObject config) {
        this.registry = vertx.sharedData().<String, String>getLocalAsyncMap("service.registry").result();
      }

      @Override
      public void store(Record record, Handler<AsyncResult<Record>> resultHandler) {
        String uuid = UUID.randomUUID().toString();
        if (record.getRegistration() != null) {
          throw new IllegalArgumentException("The record has already been registered");
        }

        record.setRegistration(uuid);
        registry.put(uuid, record.toJson().encode()).onComplete(ar -> {
          // Put takes some time to complete
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

          if (ar.succeeded()) {
            resultHandler.handle(Future.succeededFuture(record));
          } else {
            resultHandler.handle(Future.failedFuture(ar.cause()));
          }
        });
      }

      @Override
      public void remove(Record record, Handler<AsyncResult<Record>> resultHandler) {

      }

      @Override
      public void remove(String uuid, Handler<AsyncResult<Record>> resultHandler) {

      }

      @Override
      public void update(Record record, Handler<AsyncResult<Void>> resultHandler) {

      }

      @Override
      public void getRecords(Handler<AsyncResult<List<Record>>> resultHandler) {

      }

      @Override
      public void getRecord(String uuid, Handler<AsyncResult<Record>> resultHandler) {

      }
    };

    ServiceDiscovery discovery = new DiscoveryImpl(vertx,
      new ServiceDiscoveryOptions().setBackendConfiguration(
        new JsonObject().put("backend-name", "slow-backend")), slowBackend);

    List<Record> announces = new ArrayList<>();

    vertx.eventBus().consumer(ServiceDiscoveryOptions.DEFAULT_ANNOUNCE_ADDRESS,
      msg -> announces.add(new Record((JsonObject) msg.body())));

    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");
    Record record = new Record()
      .setName("Hello")
      .setMetadata(new JsonObject().put("key", "A"))
      .setLocation(new JsonObject().put(Record.ENDPOINT, "address"));

    // Publish
    AtomicBoolean done = new AtomicBoolean();
    done.set(false);
    discovery.publish(record).onComplete(r -> {
      done.set(r.succeeded());
    });

    // When publication announced
    await().until(() -> announces.size() == 1);

    // Asset record publication complete
    assertThat(done.get()).isTrue();
  }

  @Test
  public void testServiceUsage() throws InterruptedException {
    List<JsonObject> usages = new ArrayList<>();


    vertx.eventBus().<JsonObject>consumer(ServiceDiscoveryOptions.DEFAULT_USAGE_ADDRESS,
      msg -> usages.add(msg.body()));

    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");

    Record record = new Record()
      .setName("Hello")
      .setMetadata(new JsonObject()
        .put("key", "A")
        .put("service.interface", HelloService.class.getName()))
      .setType(EventBusService.TYPE)
      .setLocation(new JsonObject().put(Record.ENDPOINT, "address"));
    discovery.publish(record);
    await().until(() -> record.getRegistration() != null);

    ServiceReference reference = discovery.getReference(record);
    await().until(() -> usages.size() == 1);

    assertThat(usages.get(0).getJsonObject(ServiceDiscovery.EVENT_RECORD).getJsonObject("location").getString(Record.ENDPOINT))
      .isEqualToIgnoringCase("address");
    assertThat(usages.get(0).getString(ServiceDiscovery.EVENT_TYPE)).isEqualTo(ServiceDiscovery.EVENT_TYPE_BIND);
    assertThat(usages.get(0).getString(ServiceDiscovery.EVENT_ID)).isNotNull().isNotEmpty();

    assertThat((HelloService) reference.cached()).isNull();
    assertThat((HelloService) reference.get()).isNotNull();
    assertThat((HelloService) reference.cached()).isNotNull();

    reference.release();
    Assertions.assertThat(discovery.bindings()).isEmpty();
    await().until(() -> usages.size() == 2);
    assertThat(usages.get(1).getJsonObject(ServiceDiscovery.EVENT_RECORD).getJsonObject("location").getString(Record.ENDPOINT))
      .isEqualToIgnoringCase("address");
    assertThat(usages.get(1).getString(ServiceDiscovery.EVENT_TYPE)).isEqualTo(ServiceDiscovery.EVENT_TYPE_RELEASE);
    assertThat(usages.get(1).getString(ServiceDiscovery.EVENT_ID)).isNotNull().isNotEmpty();

    // Check that even if we release the reference another time the service event is not send a second time.
    reference.release();
    Assertions.assertThat(discovery.bindings()).isEmpty();
    Thread.sleep(100);
    assertThat(usages).hasSize(2);
  }

  @Test
  public void testBridges() {
    AtomicBoolean closed = new AtomicBoolean();
    AtomicBoolean registered = new AtomicBoolean();
    ServiceImporter bridge = new ServiceImporter() {
      @Override
      public void start(Vertx vertx, ServicePublisher publisher, JsonObject configuration, Promise<Void> future) {
        Record rec1 = HttpEndpoint.createRecord("static-record-1", "acme.org");
        Record rec2 = HttpEndpoint.createRecord("static-record-2", "example.com");
        publisher.publish(rec1).onComplete(
          ar -> publisher.publish(rec2).onComplete(ar2 -> {
            registered.set(true);
            future.complete();
          }));
      }

      @Override
      public void close(Handler<Void> closeHandler) {
        closed.set(true);
        closeHandler.handle(null);
      }
    };

    discovery.registerServiceImporter(bridge, null);

    await().untilAtomic(registered, is(true));

    AtomicReference<Record> record1 = new AtomicReference<>();
    AtomicReference<Record> record2 = new AtomicReference<>();
    discovery.getRecord(new JsonObject().put("name", "static-record-1")).onComplete(found -> {
      record1.set(found.result());
    });

    discovery.getRecord(new JsonObject().put("name", "static-record-2")).onComplete(found -> {
      record2.set(found.result());
    });


    assertThat(record1).isNotNull();
    assertThat(record2).isNotNull();

    discovery.close();

    await().untilAtomic(closed, is(true));
  }

  @Test
  public void testName() {
    ServiceDiscoveryOptions options = new ServiceDiscoveryOptions().setName("my-name");
    assertThat(options.getName()).isEqualToIgnoringCase("my-name");
  }

  private static class TestServiceExporter implements ServiceExporter {

    private Map<String, Record> state = new HashMap<>();
    private boolean closed;

    @Override
    public void onPublish(Record record) {
      state.put(record.getRegistration(), new Record(record));
    }

    @Override
    public void onUpdate(Record record) {
      state.put(record.getRegistration(), new Record(record));
    }

    @Override
    public void onUnpublish(String id) {
      state.remove(id);
    }

    @Override
    public void close(Handler<Void> closeHandler) {
      closed = true;
    }

    @Override
    public void init(Vertx vertx, ServicePublisher publisher, JsonObject configuration, Promise<Void> future) {
      future.complete(null);
    }
  }

  @Test
  public void testExporter() {
    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");
    Record record = new Record()
      .setName("Hello")
      .setType(EventBusService.TYPE)
      .setLocation(new JsonObject().put(Record.ENDPOINT, "address"))
      .setMetadata(new JsonObject().put("foo", "foo_value_1"));

    TestServiceExporter exporter = new TestServiceExporter();
    discovery.registerServiceExporter(exporter, new JsonObject());

    discovery.publish(record).onComplete((r) -> {
    });
    await().until(() -> exporter.state.size() > 0);
    String id = exporter.state.keySet().iterator().next();
    assertNotNull(id);
    Record exported = exporter.state.get(id);
    assertEquals("Hello", exported.getName());
    assertEquals(EventBusService.TYPE, exported.getType());
    assertEquals(Status.UP, exported.getStatus());
    assertEquals(new JsonObject().put(Record.ENDPOINT, "address"), exported.getLocation());
    assertEquals(new JsonObject().put("foo", "foo_value_1"), exported.getMetadata());

    AtomicBoolean updated = new AtomicBoolean();
    discovery.update(new Record(record).setMetadata(new JsonObject().put("foo", "foo_value_2"))).onComplete(ar -> updated.set(true));
    await().until(updated::get);
    assertNotSame(exporter.state.get(id), exported);
    exported = exporter.state.get(id);
    assertEquals("Hello", exported.getName());
    assertEquals(EventBusService.TYPE, exported.getType());
    assertEquals(Status.UP, exported.getStatus());
    assertEquals(new JsonObject().put(Record.ENDPOINT, "address"), exported.getLocation());
    assertEquals(new JsonObject().put("foo", "foo_value_2"), exported.getMetadata());

    AtomicBoolean removed = new AtomicBoolean();
    discovery.unpublish(id).onComplete(ar -> removed.set(true));
    await().until(removed::get);
    assertEquals(Collections.emptyMap(), exporter.state);

    discovery.close();
    assertTrue(exporter.closed);
  }

  @Test
  public void testPublicationWithoutStatus() {
    AtomicReference<Record> ref = new AtomicReference<>();
    Record record = HttpEndpoint.createRecord("some-service", "localhost");
    discovery.publish(record).onComplete(ar -> {
      ref.set(ar.result());
    });

    await().untilAtomic(ref, is(notNullValue()));

    assertThat(ref.get().getRegistration()).isNotNull();
    assertThat(ref.get().getStatus()).isEqualTo(Status.UP);
  }

  @Test
  public void testPublicationWithStatusUp() {
    AtomicReference<Record> ref = new AtomicReference<>();
    Record record = HttpEndpoint.createRecord("some-service", "localhost").setStatus(Status.UP);
    discovery.publish(record).onComplete(ar -> {
      ref.set(ar.result());
    });

    await().untilAtomic(ref, is(notNullValue()));

    assertThat(ref.get().getRegistration()).isNotNull();
    assertThat(ref.get().getStatus()).isEqualTo(Status.UP);
  }

  @Test
  public void testPublicationWithStatusUnknown() {
    AtomicReference<Record> ref = new AtomicReference<>();
    Record record = HttpEndpoint.createRecord("some-service", "localhost").setStatus(Status.UNKNOWN);
    discovery.publish(record).onComplete(ar -> {
      ref.set(ar.result());
    });

    await().untilAtomic(ref, is(notNullValue()));

    assertThat(ref.get().getRegistration()).isNotNull();
    assertThat(ref.get().getStatus()).isEqualTo(Status.UP);
  }

  @Test
  public void testPublicationWithStatusDown() {
    AtomicReference<Record> ref = new AtomicReference<>();
    Record record = HttpEndpoint.createRecord("some-service", "localhost").setStatus(Status.DOWN);
    discovery.publish(record).onComplete(ar -> {
      ref.set(ar.result());
    });

    await().untilAtomic(ref, is(notNullValue()));

    assertThat(ref.get().getRegistration()).isNotNull();
    assertThat(ref.get().getStatus()).isEqualTo(Status.DOWN);
  }

  @Test
  public void testPublicationWithStatusOutOfService() {
    AtomicReference<Record> ref = new AtomicReference<>();
    Record record = HttpEndpoint.createRecord("some-service", "localhost").setStatus(Status.OUT_OF_SERVICE);
    discovery.publish(record).onComplete(ar -> {
      ref.set(ar.result());
    });

    await().untilAtomic(ref, is(notNullValue()));

    assertThat(ref.get().getRegistration()).isNotNull();
    assertThat(ref.get().getStatus()).isEqualTo(Status.OUT_OF_SERVICE);
  }


}
