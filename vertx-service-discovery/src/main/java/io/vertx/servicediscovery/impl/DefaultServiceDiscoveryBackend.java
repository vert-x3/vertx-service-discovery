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
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackend;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DefaultServiceDiscoveryBackend implements ServiceDiscoveryBackend {
  private static final String MESSAGE_NO_REG_ID = "No registration id in the record";
  private AsyncMap<String, String> registry;
  private Vertx vertx;

  @Override
  public void init(Vertx vertx, JsonObject config) {
    this.vertx = vertx;
    if (!vertx.isClustered() || useLocalBackend()) {
      registry = vertx.sharedData().<String, String>getLocalAsyncMap("service.registry").result();
    }
  }

  /**
   * @return whether we should use a local backend even in clustered mode.
   */
  private boolean useLocalBackend() {
    String property = System.getProperty("vertx-service-discovery-backend-local");
    if (property != null) {
      return "true".equalsIgnoreCase(property);
    }
    property = System.getenv("VERTX-SERVICE-DISCOVERY-BACKEND-LOCAL");
    return "true".equalsIgnoreCase(property);
  }

  @Override
  public void store(Record record, Handler<AsyncResult<Record>> resultHandler) {
    String uuid = UUID.randomUUID().toString();
    if (record.getRegistration() != null) {
      throw new IllegalArgumentException("The record has already been registered");
    }
    record.setRegistration(uuid);
    retrieveRegistry(reg -> {
      if (reg.failed()) {
        resultHandler.handle(failure(reg.cause()));
      } else {
        reg.result().put(uuid, record.toJson().encode(), ar -> {
          if (ar.succeeded()) {
            resultHandler.handle(Future.succeededFuture(record));
          } else {
            resultHandler.handle(Future.failedFuture(ar.cause()));
          }
        });
      }
    });
  }

  private synchronized void retrieveRegistry(Handler<AsyncResult<AsyncMap<String, String>>> handler) {
    if (registry != null) {
      handler.handle(Future.succeededFuture(registry));
    } else {
      vertx.sharedData().<String, String>getClusterWideMap("service.registry", ar -> {
        synchronized (DefaultServiceDiscoveryBackend.class) {
          if (ar.failed()) {
            handler.handle(ar);
          } else {
            registry = ar.result();
            handler.handle(Future.succeededFuture(registry));
          }
        }
      });
    }
  }

  @Override
  public void remove(Record record, Handler<AsyncResult<Record>> resultHandler) {
    Objects.requireNonNull(record.getRegistration(), MESSAGE_NO_REG_ID);
    remove(record.getRegistration(), resultHandler);
  }

  private static <T> Future<T> failure(Throwable e) {
    return Future.failedFuture(new Exception("Unable to retrieve the registry", e));
  }

  @Override
  public void remove(String uuid, Handler<AsyncResult<Record>> resultHandler) {
    Objects.requireNonNull(uuid, MESSAGE_NO_REG_ID);
    retrieveRegistry(reg -> {
        if (reg.failed()) {
          resultHandler.handle(failure(reg.cause()));
        } else {
          reg.result().remove(uuid, ar -> {
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
      }
    );
  }

  @Override
  public void update(Record record, Handler<AsyncResult<Void>> resultHandler) {
    Objects.requireNonNull(record.getRegistration(), MESSAGE_NO_REG_ID);
    retrieveRegistry(reg -> {
        if (reg.failed()) {
          resultHandler.handle(failure(reg.cause()));
        } else {
          reg.result().put(record.getRegistration(), record.toJson().encode(), ar -> {
            if (ar.succeeded()) {
              resultHandler.handle(Future.succeededFuture());
            } else {
              resultHandler.handle(Future.failedFuture(ar.cause()));
            }
          });
        }
      }
    );
  }

  @Override
  public void getRecords(Handler<AsyncResult<List<Record>>> resultHandler) {
    retrieveRegistry(reg -> {
        if (reg.failed()) {
          resultHandler.handle(failure(reg.cause()));
        } else {
          reg.result().entries(ar -> {
            if (ar.succeeded()) {
              resultHandler.handle(Future.succeededFuture(ar.result().values().stream()
                .map(s -> new Record(new JsonObject(s)))
                .collect(Collectors.toList())));
            } else {
              resultHandler.handle(Future.failedFuture(ar.cause()));
            }
          });
        }
      }
    );
  }

  @Override
  public void getRecord(String uuid, Handler<AsyncResult<Record>> resultHandler) {
    retrieveRegistry(reg -> {
      if (reg.failed()) {
        resultHandler.handle(failure(reg.cause()));
      } else {
        reg.result().get(uuid, ar -> {
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
    });
  }
}
