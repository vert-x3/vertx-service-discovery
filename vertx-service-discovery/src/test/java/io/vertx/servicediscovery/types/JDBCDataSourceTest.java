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

package io.vertx.servicediscovery.types;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.impl.DiscoveryImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Check the behavior of the JDBC data source.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class JDBCDataSourceTest {

  private Vertx vertx;
  private ServiceDiscovery discovery;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    discovery = new DiscoveryImpl(vertx, new ServiceDiscoveryOptions());
  }

  @After
  public void tearDown() {
    discovery.close();
    AtomicBoolean completed = new AtomicBoolean();
    vertx.close().onComplete((v) -> completed.set(true));
    await().untilAtomic(completed, is(true));

    assertThat(discovery.bindings()).isEmpty();
  }

  @Test
  public void test() throws InterruptedException {
    JsonObject conf = new JsonObject()
        .put("driverclass", "org.hsqldb.jdbcDriver");

    Record record = JDBCDataSource.createRecord("some-hsql-db",
        new JsonObject().put("url", "jdbc:hsqldb:file:target/dumb-db;shutdown=true"),
        new JsonObject().put("database", "some-raw-data"));

    discovery.publish(record);
    await().until(() -> record.getRegistration() != null);

    AtomicReference<Record> found = new AtomicReference<>();
    discovery.getRecord(new JsonObject().put("name", "some-hsql-db")).onComplete(ar -> {
      found.set(ar.result());
    });

    await().until(() -> found.get() != null);

    ServiceReference service = discovery.getReferenceWithConfiguration(found.get(), conf);
    JDBCClient client = service.get();
    AtomicBoolean success = new AtomicBoolean();
    client.getConnection(ar -> {
      if (ar.succeeded()) {
        ar.result().close();
      }
      success.set(ar.succeeded());
    });

    await().untilAtomic(success, is(true));
    service.release();
    // Just there to be sure we can call it twice
    service.release();
  }

  @Test
  public void testMissing() throws InterruptedException {
    AtomicReference<Throwable> expected = new AtomicReference<>();
    JDBCDataSource.getJDBCClient(discovery,
        new JsonObject().put("name", "some-hsql-db")).onComplete(
        ar -> {
          expected.set(ar.cause());
        });

    await().until(() -> expected.get() != null);
    assertThat(expected.get().getMessage()).contains("record");
  }


  @Test
  public void testWithSugar() throws InterruptedException {
    JsonObject conf = new JsonObject()
        .put("driverclass", "org.hsqldb.jdbcDriver");

    Record record = JDBCDataSource.createRecord("some-hsql-db",
        new JsonObject().put("url", "jdbc:hsqldb:file:target/dumb-db;shutdown=true"),
        new JsonObject().put("database", "some-raw-data"));

    discovery.publish(record);
    await().until(() -> record.getRegistration() != null);


    AtomicBoolean success = new AtomicBoolean();
    JDBCDataSource.getJDBCClient(discovery, new JsonObject().put("name", "some-hsql-db"), conf).onComplete(
        ar -> {
          JDBCClient client = ar.result();
          client.getConnection(conn -> {
            if (ar.succeeded()) {
              conn.result().close();
            }
            client.close();
            success.set(conn.succeeded());
          });
        });
    await().untilAtomic(success, is(true));

  }

  @Test
  public void testWithSugarWithoutConsumerConf() throws InterruptedException {
    Record record = JDBCDataSource.createRecord("some-hsql-db",
        new JsonObject().put("url", "jdbc:hsqldb:file:target/dumb-db;shutdown=true"),
        new JsonObject().put("database", "some-raw-data").put("driverclass", "org.hsqldb.jdbcDriver"));

    discovery.publish(record);
    await().until(() -> record.getRegistration() != null);


    AtomicBoolean success = new AtomicBoolean();
    JDBCDataSource.getJDBCClient(discovery, new JsonObject().put("name", "some-hsql-db")).onComplete(
        ar -> {
          JDBCClient client = ar.result();
          client.getConnection(conn -> {
            if (ar.succeeded()) {
              conn.result().close();
            }
            client.close();
            success.set(conn.succeeded());
          });
        });
    await().untilAtomic(success, is(true));
  }
}
