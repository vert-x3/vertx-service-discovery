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

package io.vertx.servicediscovery.backend.redis;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.SocketAddress;
import io.vertx.redis.client.*;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackend;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static io.vertx.redis.client.Request.cmd;
import static io.vertx.redis.client.Command.*;

/**
 * An implementation of the discovery backend based on Redis.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class RedisBackendService implements ServiceDiscoveryBackend {

  private Redis redis;
  private String key;

  private static final int DISCONNECTED = 0;
  private static final int CONNECTING = 1;
  private static final int CONNECTED = 2;

  private final AtomicInteger state = new AtomicInteger();

  @Override
  public void init(Vertx vertx, JsonObject configuration) {
    key = configuration.getString("key", "records");

    System.out.println(configuration.encodePrettily());

    if (configuration.containsKey("host") || configuration.containsKey("port")) {
      redis = Redis.createClient(vertx, new RedisOptions(configuration).setEndpoint(
        SocketAddress.inetSocketAddress(
          configuration.getInteger("port", 6379),
          configuration.getString("host", "localhost"))
      ));
    } else {
      redis = Redis.createClient(vertx, new RedisOptions(configuration));
    }
  }

  private void redisCall(Request request, Handler<AsyncResult<Response>> handler) {
    if (state.compareAndSet(DISCONNECTED, CONNECTING)) {
      redis
        .connect(connect -> {
          if (connect.succeeded()) {
            if (state.compareAndSet(CONNECTING, CONNECTED)) {
              // send the reques
              redis.send(request, handler);
            } else {
              handler.handle(Future.failedFuture("Redis client backend Illegal state (expected: CONNECTING)"));
            }
          } else {
            // fail the connection
            state.set(DISCONNECTED);
            handler.handle(Future.failedFuture(connect.cause()));
          }
        })
        .exceptionHandler(ex -> {
          // fail the connection
          state.set(DISCONNECTED);
        });

      return;
    }

    if (state.get() == CONNECTING) {
      handler.handle(Future.failedFuture("Redis client backend Illegal state (expected: CONNECTED)"));
      return;
    }

    if (state.get() == CONNECTED) {
      redis.send(request, handler);
    }
  }

  @Override
  public void store(Record record, Handler<AsyncResult<Record>> resultHandler) {
    if (record.getRegistration() != null) {
      resultHandler.handle(Future.failedFuture("The record has already been registered"));
      return;
    }
    String uuid = UUID.randomUUID().toString();
    record.setRegistration(uuid);

    redisCall(cmd(HSET).arg(key).arg(uuid).arg(record.toJson().encode()), ar -> {
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

    redisCall(cmd(HGET).arg(key).arg(uuid), ar -> {
      if (ar.succeeded()) {
        if (ar.result() != null) {
          redisCall(cmd(HDEL).arg(key).arg(uuid), deletion -> {
            if (deletion.succeeded()) {
              resultHandler.handle(Future.succeededFuture(new Record(new JsonObject(ar.result().toBuffer()))));
            } else {
              resultHandler.handle(Future.failedFuture(deletion.cause()));
            }
          });
        } else {
          resultHandler.handle(Future.failedFuture("Record '" + uuid + "' not found"));
        }
      } else {
        resultHandler.handle(Future.failedFuture(ar.cause()));
      }
    });
  }

  @Override
  public void update(Record record, Handler<AsyncResult<Void>> resultHandler) {
    Objects.requireNonNull(record.getRegistration(), "No registration id in the record");
    redisCall(cmd(HSET).arg(key).arg(record.getRegistration()).arg(record.toJson().encode()), ar -> {
      if (ar.succeeded()) {
        resultHandler.handle(Future.succeededFuture());
      } else {
        resultHandler.handle(Future.failedFuture(ar.cause()));
      }
    });
  }

  @Override
  public void getRecords(Handler<AsyncResult<List<Record>>> resultHandler) {
    redisCall(cmd(HGETALL).arg(key), ar -> {
      if (ar.succeeded()) {
        Response entries = ar.result();
        resultHandler.handle(Future.succeededFuture(entries.getKeys().stream()
          .map(key -> new Record(new JsonObject(entries.get(key).toBuffer())))
          .collect(Collectors.toList())));
      } else {
        resultHandler.handle(Future.failedFuture(ar.cause()));
      }
    });
  }

  @Override
  public void getRecord(String uuid, Handler<AsyncResult<Record>> resultHandler) {
    redisCall(cmd(HGET).arg(key).arg(uuid), ar -> {
      if (ar.succeeded()) {
        if (ar.result() != null) {
          resultHandler.handle(Future.succeededFuture(new Record(new JsonObject(ar.result().toBuffer()))));
        } else {
          resultHandler.handle(Future.succeededFuture(null));
        }
      } else {
        resultHandler.handle(Future.failedFuture(ar.cause()));
      }
    });
  }
}
