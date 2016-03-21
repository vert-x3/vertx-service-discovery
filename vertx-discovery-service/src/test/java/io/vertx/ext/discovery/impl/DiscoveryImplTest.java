/*
 * Copyright (c) 2011-$tody.year The original author or authors
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

package io.vertx.ext.discovery.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.discovery.*;
import io.vertx.ext.discovery.types.HttpEndpoint;
import io.vertx.ext.service.HelloService;
import io.vertx.ext.service.HelloServiceImpl;
import io.vertx.serviceproxy.ProxyHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DiscoveryImplTest {


  protected Vertx vertx;
  protected DiscoveryService discovery;

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
  public void testPublicationAndSimpleLookup() {
    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");
    Record record = new Record()
        .setName("Hello")
        .setLocation(new JsonObject().put(Record.ENDPOINT, "address"));

    discovery.publish(record, (r) -> {
    });
    await().until(() -> record.getRegistration() != null);

    AtomicReference<Record> found = new AtomicReference<>();
    discovery.getRecord(new JsonObject().put("name", "Hello"), ar -> {
      found.set(ar.result());
    });

    await().until(() -> found.get() != null);
    assertThat(found.get().getLocation().getString("endpoint")).isEqualTo("address");

    AtomicBoolean done = new AtomicBoolean();
    discovery.unpublish(record.getRegistration(), v -> {
      done.set(v.succeeded());
    });

    await().untilAtomic(done, is(true));

    found.set(null);
    done.set(false);
    discovery.getRecord(new JsonObject().put("name", "Hello"), ar -> {
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
    discovery.publish(record, (r) -> {
    });
    discovery.publish(record2, (r) -> {
    });
    await().until(() -> record.getRegistration() != null);
    await().until(() -> record2.getRegistration() != null);

    AtomicReference<Record> found = new AtomicReference<>();
    discovery.getRecord(new JsonObject().put("key", "A"), ar -> {
      found.set(ar.result());
    });
    await().until(() -> found.get() != null);
    assertThat(found.get().getLocation().getString("endpoint")).isEqualTo("address");

    found.set(null);
    discovery.getRecord(new JsonObject().put("key", "B"), ar -> {
      found.set(ar.result());
    });
    await().until(() -> found.get() != null);
    assertThat(found.get().getLocation().getString("endpoint")).isEqualTo("address2");

    found.set(null);
    AtomicBoolean done = new AtomicBoolean();
    discovery.getRecord(new JsonObject().put("key", "C"), ar -> {
      found.set(ar.result());
      done.set(true);
    });
    await().untilAtomic(done, is(true));
    assertThat(found.get()).isNull();

    found.set(null);
    done.set(false);
    discovery.getRecord(new JsonObject().put("key", "B").put("foo", "bar"), ar -> {
      found.set(ar.result());
      done.set(true);
    });
    await().untilAtomic(done, is(true));
    assertThat(found.get()).isNull();
  }

  @Test
  public void testAnnounce() {
    List<Record> announces = new ArrayList<>();

    vertx.eventBus().consumer(DiscoveryOptions.DEFAULT_ANNOUNCE_ADDRESS,
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
    discovery.publish(record, (r) -> {
    });
    discovery.publish(record2, (r) -> {
    });
    await().until(() -> record.getRegistration() != null);
    await().until(() -> record2.getRegistration() != null);

    await().until(() -> announces.size() == 2);
    for (Record rec : announces) {
      assertThat(rec.getStatus()).isEqualTo(Status.UP);
    }

    discovery.unpublish(record2.getRegistration(), v -> {

    });

    await().until(() -> announces.size() == 3);
    assertThat(announces.get(2).getStatus()).isEqualTo(Status.DOWN);
  }

  @Test
  public void testBridges() {
    AtomicBoolean closed = new AtomicBoolean();
    AtomicBoolean registered = new AtomicBoolean();
    DiscoveryBridge bridge = new DiscoveryBridge() {
      @Override
      public void start(Vertx vertx, DiscoveryService discovery, JsonObject configuration, Handler<AsyncResult<Void>> completionHandler) {
        Record rec1 = HttpEndpoint.createRecord("static-record-1", "acme.org");
        Record rec2 = HttpEndpoint.createRecord("static-record-2", "example.com");
        discovery.publish(rec1, ar -> {
          discovery.publish(rec2, ar2 -> {
            completionHandler.handle(Future.succeededFuture());
            registered.set(true);
          });
        });
      }

      @Override
      public void stop(Vertx vertx, DiscoveryService discovery) {
        closed.set(true);
      }
    };

    discovery.registerDiscoveryBridge(bridge, null);

    await().untilAtomic(registered, is(true));

    AtomicReference<Record> record1 = new AtomicReference<>();
    AtomicReference<Record> record2 = new AtomicReference<>();
    discovery.getRecord(new JsonObject().put("name", "static-record-1"), found -> {
      record1.set(found.result());
    });

    discovery.getRecord(new JsonObject().put("name", "static-record-2"), found -> {
      record2.set(found.result());
    });


    assertThat(record1).isNotNull();
    assertThat(record2).isNotNull();

    discovery.close();

    await().untilAtomic(closed, is(true));
  }

}