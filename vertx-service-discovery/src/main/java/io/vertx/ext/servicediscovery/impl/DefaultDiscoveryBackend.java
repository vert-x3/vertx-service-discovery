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

package io.vertx.ext.servicediscovery.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.servicediscovery.Record;
import io.vertx.ext.servicediscovery.spi.DiscoveryBackend;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DefaultDiscoveryBackend implements DiscoveryBackend {
  private AsyncMap<String, String> registry;

  @Override
  public void init(Vertx vertx, JsonObject config) {
    this.registry = new AsyncMap<>(vertx, "service.registry");
  }

  @Override
  public void store(Record record, Handler<AsyncResult<Record>> resultHandler) {
    String uuid = UUID.randomUUID().toString();
    if (record.getRegistration() != null) {
      throw new IllegalArgumentException("The record has already been registered");
    }

    record.setRegistration(uuid);
    registry.put(uuid, record.toJson().encode(), ar -> {
      if (ar.succeeded()) {
        resultHandler.handle(Future.succeededFuture(record));
      } else {
        resultHandler.handle(Future.failedFuture(ar.cause()));
      }
    });
  }

  @Override
  public void remove(Record record, Handler<AsyncResult<Record>> resultHandler) {
    Objects.requireNonNull(record.getRegistration(), "No registration id in the record");
    remove(record.getRegistration(), resultHandler);
  }

  @Override
  public void remove(String uuid, Handler<AsyncResult<Record>> resultHandler) {
    Objects.requireNonNull(uuid, "No registration id in the record");
    registry.remove(uuid, ar -> {
      if (ar.succeeded()) {
        if (ar.result() == null) {
          // Not found
          resultHandler.handle(Future.failedFuture("Record '" + uuid + "' not found"));
        } else {
          resultHandler.handle(Future.succeededFuture(
              new Record(new JsonObject(ar.result()))));
        }
      } else {
        resultHandler.handle(Future.failedFuture(ar.cause()));
      }
    });
  }

  @Override
  public void update(Record record, Handler<AsyncResult<Void>> resultHandler) {
    Objects.requireNonNull(record.getRegistration(), "No registration id in the record");
    registry.put(record.getRegistration(), record.toJson().encode(), ar -> {
      if (ar.succeeded()) {
        resultHandler.handle(Future.succeededFuture());
      } else {
        resultHandler.handle(Future.failedFuture(ar.cause()));
      }
    });
  }

  @Override
  public void getRecords(Handler<AsyncResult<List<Record>>> resultHandler) {
    registry.getAll(ar -> {
      if (ar.succeeded()) {
        resultHandler.handle(Future.succeededFuture(ar.result().values().stream()
            .map(s -> new Record(new JsonObject(s)))
            .collect(Collectors.toList())));
      } else {
        resultHandler.handle(Future.failedFuture(ar.cause()));
      }
    });
  }

  @Override
  public void getRecord(String uuid, Handler<AsyncResult<Record>> resultHandler) {
    registry.get(uuid, ar -> {
      if (ar.succeeded()) {
        if (ar.result() != null) {
          resultHandler.handle(Future.succeededFuture(new Record(new JsonObject(ar.result()))));
        } else {
          resultHandler.handle(Future.succeededFuture(null));
        }
      } else {
        resultHandler.handle(Future.failedFuture(ar.cause()));
      }
    });
  }
}
