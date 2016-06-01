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
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.servicediscovery.DiscoveryOptions;
import io.vertx.ext.servicediscovery.DiscoveryService;
import io.vertx.ext.servicediscovery.Record;
import io.vertx.ext.servicediscovery.ServiceReference;
import io.vertx.ext.servicediscovery.impl.DiscoveryImpl;
import io.vertx.ext.servicediscovery.types.MessageSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
public class MessageSourceTest {

  private Vertx vertx;
  private DiscoveryService discovery;

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
  public void test() throws InterruptedException {
    Random random = new Random();
    vertx.setPeriodic(10, l -> {
      vertx.eventBus().publish("data", random.nextDouble());
    });

    Record record = MessageSource.createRecord("Hello", "data");

    discovery.publish(record, (r) -> {
    });
    await().until(() -> record.getRegistration() != null);

    AtomicReference<Record> found = new AtomicReference<>();
    discovery.getRecord(new JsonObject().put("name", "Hello"), ar -> {
      found.set(ar.result());
    });

    await().until(() -> found.get() != null);

    ServiceReference service = discovery.getReference(found.get());
    MessageConsumer<Double> consumer = service.get();

    List<Double> data = new ArrayList<>();
    consumer.handler(message -> {
      data.add(message.body());
    });
    await().until(() -> ! data.isEmpty());
    service.release();
    int size = data.size();
    Thread.sleep(500);
    assertThat(data.size()).isEqualTo(size);

    // Just there to be sure we can call it twice
    service.release();
  }
}
