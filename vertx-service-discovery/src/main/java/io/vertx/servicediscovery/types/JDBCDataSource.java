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

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.Record;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.servicediscovery.spi.ServiceType;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface JDBCDataSource extends ServiceType {

  String TYPE = "jdbc";


  static Record createRecord(String name, JsonObject location, JsonObject metadata) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(location);

    Record record = new Record().setName(name)
      .setType(TYPE)
      .setLocation(location);

    if (metadata != null) {
      record.setMetadata(metadata);
    }

    return record;
  }

  /**
   * Convenient method that looks for a JDBC datasource source and provides the configured {@link io.vertx.ext.jdbc.JDBCClient}. The
   * async result is marked as failed is there are no matching services, or if the lookup fails.
   *
   * @param discovery     The service discovery instance
   * @param filter        The filter, optional
   * @param resultHandler The result handler
   * @deprecated use {@link #getJDBCClient(ServiceDiscovery, JsonObject)} instead
   */
  @Deprecated
  static void getJDBCClient(ServiceDiscovery discovery, JsonObject filter,
                            Handler<AsyncResult<JDBCClient>> resultHandler) {
    getJDBCClient(discovery, filter).onComplete(resultHandler);
  }

  /**
   * Like {@link #getJDBCClient(ServiceDiscovery, JsonObject, Handler)} but returns a future of the result
   */
  static Future<JDBCClient> getJDBCClient(ServiceDiscovery discovery, JsonObject filter) {
    return discovery.getRecord(filter).flatMap(res -> {
      if (res == null) {
        return Future.failedFuture("No matching records");
      } else {
        return Future.succeededFuture(discovery.getReference(res).get());
      }
    });
  }

  /**
   * Convenient method that looks for a JDBC datasource source and provides the configured {@link io.vertx.ext.jdbc.JDBCClient}. The
   * async result is marked as failed is there are no matching services, or if the lookup fails.
   *
   * @param discovery     The service discovery instance
   * @param filter        The filter (must not be {@code null})
   * @param resultHandler The result handler
   * @deprecated use {@link #getJDBCClient(ServiceDiscovery, Function)} instead
   */
  @Deprecated
  static void getJDBCClient(ServiceDiscovery discovery, Function<Record, Boolean> filter,
                            Handler<AsyncResult<JDBCClient>> resultHandler) {
    getJDBCClient(discovery, filter).onComplete(resultHandler);
  }

  /**
   * Like {@link #getJDBCClient(ServiceDiscovery, Function, Handler)} but returns a future of the result
   */
  static Future<JDBCClient> getJDBCClient(ServiceDiscovery discovery, Function<Record, Boolean> filter) {
    return discovery.getRecord(filter).flatMap(res -> {
      if (res == null) {
        return Future.failedFuture("No matching records");
      } else {
        return Future.succeededFuture(discovery.getReference(res).get());
      }
    });
  }

  /**
   * Convenient method that looks for a JDBC datasource source and provides the configured {@link io.vertx.ext.jdbc.JDBCClient}. The
   * async result is marked as failed is there are no matching services, or if the lookup fails.
   *
   * @param discovery             The service discovery instance
   * @param filter                The filter, optional
   * @param consumerConfiguration the consumer configuration
   * @param resultHandler         the result handler
   * @deprecated use {@link #getJDBCClient(ServiceDiscovery, JsonObject, JsonObject)} instead
   */
  @Deprecated
  static void getJDBCClient(ServiceDiscovery discovery, JsonObject filter, JsonObject consumerConfiguration,
                            Handler<AsyncResult<JDBCClient>> resultHandler) {
    getJDBCClient(discovery, filter, consumerConfiguration).onComplete(resultHandler);
  }

  /**
   * Like {@link #getJDBCClient(ServiceDiscovery, JsonObject, JsonObject, Handler)} but returns a future of the result
   */
  static Future<JDBCClient> getJDBCClient(ServiceDiscovery discovery, JsonObject filter, JsonObject consumerConfiguration) {
    return discovery.getRecord(filter).flatMap(res -> {
      if (res == null) {
        return Future.failedFuture("No matching records");
      } else {
        return Future.succeededFuture(discovery.getReferenceWithConfiguration(res, consumerConfiguration).get());
      }
    });
  }

  /**
   * Convenient method that looks for a JDBC datasource source and provides the configured {@link io.vertx.ext.jdbc.JDBCClient}. The
   * async result is marked as failed is there are no matching services, or if the lookup fails.
   *
   * @param discovery             The service discovery instance
   * @param filter                The filter, must not be {@code null}
   * @param consumerConfiguration the consumer configuration
   * @param resultHandler         the result handler
   * @deprecated use {@link #getJDBCClient(ServiceDiscovery, Function, JsonObject)} instead
   */
  @Deprecated
  static void getJDBCClient(ServiceDiscovery discovery, Function<Record, Boolean> filter, JsonObject consumerConfiguration,
                            Handler<AsyncResult<JDBCClient>> resultHandler) {
    getJDBCClient(discovery, filter, consumerConfiguration).onComplete(resultHandler);
  }

  /**
   * Like {@link #getJDBCClient(ServiceDiscovery, Function, JsonObject, Handler)} but returns a future of the result
   */
  static Future<JDBCClient> getJDBCClient(ServiceDiscovery discovery, Function<Record, Boolean> filter, JsonObject consumerConfiguration) {
    return discovery.getRecord(filter).flatMap(res -> {
      if (res == null) {
        return Future.failedFuture("No matching records");
      } else {
        return Future.succeededFuture(discovery.getReferenceWithConfiguration(res, consumerConfiguration).get());
      }
    });
  }
}


