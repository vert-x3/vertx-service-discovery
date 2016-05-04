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

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.discovery.DiscoveryService;
import io.vertx.ext.discovery.Record;
import io.vertx.ext.jdbc.JDBCClient;

import java.util.Objects;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface JDBCDataSource extends DataSource {

  String DEFAULT_TYPE = "jdbc";

  static Record createRecord(String name, JsonObject location, JsonObject metadata) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(location);

    Record record = new Record().setName(name)
        .setType(TYPE)
        .setLocation(location);

    if (metadata != null) {
      record.setMetadata(metadata);
    }

    record.setMetadata(new JsonObject().put(DS_TYPE, DEFAULT_TYPE));

    return record;
  }

  static Record createRecord(String name, String jdbcUrl, JsonObject metadata) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(jdbcUrl);

    Record record = new Record().setName(name)
        .setType(TYPE)
        .setLocation(new JsonObject().put(Record.ENDPOINT, jdbcUrl));

    if (metadata != null) {
      record.setMetadata(metadata);
    }

    record.setMetadata(new JsonObject().put(DS_TYPE, DEFAULT_TYPE));

    return record;
  }

  /**
   * Convenient method that looks for a JDBC datasource source and provides the configured {@link io.vertx.ext.jdbc.JDBCClient}. The
   * async result is marked as failed is there are no matching services, or if the lookup fails.
   *
   * @param vertx         The vert.x instance
   * @param discovery     The discovery service
   * @param filter        The filter, optional
   * @param resultHandler the result handler
   * @param <T>           the class of the message
   */
  static <T> void get(Vertx vertx, DiscoveryService discovery, JsonObject filter,
                      Handler<AsyncResult<JDBCClient>> resultHandler) {
    discovery.getRecord(filter, ar -> {
      if (ar.failed() || ar.result() == null) {
        resultHandler.handle(Future.failedFuture("No matching record"));
      } else {
        resultHandler.handle(Future.succeededFuture(DiscoveryService.getServiceReference(vertx, ar.result()).get()));
      }
    });
  }


  static <T> void get(Vertx vertx, DiscoveryService discovery, JsonObject filter, JsonObject consumerConfiguration,
                      Handler<AsyncResult<JDBCClient>> resultHandler) {
    discovery.getRecord(filter, ar -> {
      if (ar.failed() || ar.result() == null) {
        resultHandler.handle(Future.failedFuture("No matching record"));
      } else {
        resultHandler.handle(Future.succeededFuture(DiscoveryService.getServiceReference(vertx,
            ar.result(), consumerConfiguration).get()));
      }
    });
  }
}


