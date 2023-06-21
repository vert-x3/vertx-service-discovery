/*
 * Copyright (c) 2011-2023 The original author or authors
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
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.Status;
import io.vertx.test.core.VertxTestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SlowDiscoveryBackendTest extends VertxTestBase {

  @Test
  public void testAnnouncementComesAfterUpdateIsComplete() throws Throwable {
    String RECORD_NAME = "Hello";

    ServiceDiscovery discovery = ServiceDiscovery.create(vertx,
      new ServiceDiscoveryOptions().setBackendConfiguration(
        new JsonObject().put("backend-name", SlowUpdateBackend.class.getName())
      ));

    SlowUpdateConsumer consumer = new SlowUpdateConsumer(RECORD_NAME);
    consumer.init(vertx, discovery);

    // Publish
    consumer.reset();
    AtomicBoolean failed = new AtomicBoolean(false);
    CountDownLatch publishLatch = new CountDownLatch(1);
    discovery.publish(new Record().setName(RECORD_NAME)).onComplete(ar -> {
      if (ar.failed()) {
        failed.set(true);
      }
      publishLatch.countDown();
    });

    assertTrue(publishLatch.await(5, TimeUnit.SECONDS));
    assertFalse(failed.get());
    consumer.awaitAndAssert(Status.UP);  // success

    // Update
    consumer.reset();
    CountDownLatch updateLatch = new CountDownLatch(1);
    discovery.getRecord(rec -> RECORD_NAME.equals(rec.getName()), true).onComplete(ar -> {
      if (ar.failed()) {
        failed.set(true);
        updateLatch.countDown();
      } else {
        discovery.update(ar.result().setStatus(Status.OUT_OF_SERVICE)).onComplete(ar2 -> {
          if (ar2.failed()) {
            failed.set(true);
          }
          updateLatch.countDown();
        });
      }
    });

    assertTrue(updateLatch.await(5, TimeUnit.SECONDS));
    assertFalse(failed.get());
    consumer.awaitAndAssert(Status.OUT_OF_SERVICE);  // fail
  }

  public static class SlowUpdateBackend extends DefaultServiceDiscoveryBackend {

    volatile Vertx vertx;

    @Override
    public void init(Vertx vertx, JsonObject config) {
      super.init(vertx, config);
      this.vertx = vertx;
    }

    @Override
    public void update(Record record, Handler<AsyncResult<Void>> resultHandler) {
      // delay the update to simulate the slow backend
      vertx.setTimer(500, ign -> super.update(record, resultHandler));
    }
  }

  private static class SlowUpdateConsumer {

    final String recordName;
    final AtomicBoolean failed;

    Record retrievedRecord;
    Record announcedRecord;
    CountDownLatch latch;

    SlowUpdateConsumer(String recordName) {
      this.recordName = recordName;
      this.failed = new AtomicBoolean(false);
    }

    void init(Vertx vertx, ServiceDiscovery discovery) {
      vertx.eventBus().consumer(ServiceDiscoveryOptions.DEFAULT_ANNOUNCE_ADDRESS, msg -> {
        announcedRecord = new Record((JsonObject) msg.body());
        discovery.getRecord(r -> recordName.equals(r.getName()), true).onComplete(ar -> {
          if (ar.succeeded()) {
            retrievedRecord = ar.result();
          } else {
            failed.set(true);
          }
          latch.countDown();
        });
      });
    }

    public void reset() {
      retrievedRecord = null;
      announcedRecord = null;
      latch = new CountDownLatch(1);
      failed.set(false);
    }

    public void awaitAndAssert(Status status) throws Throwable {
      Assert.assertTrue(latch.await(5, TimeUnit.SECONDS));
      Assert.assertFalse(failed.get());
      Assert.assertNotNull(retrievedRecord);
      Assert.assertNotNull(announcedRecord);
      Assert.assertEquals(recordName, announcedRecord.getName());
      Assert.assertEquals(status, announcedRecord.getStatus());
      Assert.assertEquals(recordName, retrievedRecord.getName());
      // this assertion will fail on update, the retrievedRecord.getStatus() has old value UP instead of OUT_OF_SERVICE
      Assert.assertEquals(status, retrievedRecord.getStatus());
    }
  }
}
