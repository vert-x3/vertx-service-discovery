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

package io.vertx.ext.discovery.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.discovery.*;
import io.vertx.ext.discovery.spi.DiscoveryBackend;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DiscoveryImpl implements DiscoveryService {

  private final Vertx vertx;
  private final String announce;
  private final DiscoveryBackend backend;

  private final Set<DiscoveryBridge> bridges = new CopyOnWriteArraySet<>();
  private final static Logger LOGGER = LoggerFactory.getLogger(DiscoveryImpl.class.getName());


  public DiscoveryImpl(Vertx vertx, DiscoveryOptions options) {
    this.vertx = vertx;
    this.announce = options.getAnnounceAddress();

    this.backend = getBackend();
    this.backend.init(vertx);

  }

  private DiscoveryBackend getBackend() {
    ServiceLoader<DiscoveryBackend> backends = ServiceLoader.load(DiscoveryBackend.class);
    Iterator<DiscoveryBackend> iterator = backends.iterator();
    if (!iterator.hasNext()) {
      return new DefaultDiscoveryBackend();
    } else {
      return iterator.next();
    }
  }


  @Override
  public DiscoveryService registerDiscoveryBridge(DiscoveryBridge bridge, JsonObject configuration) {
    JsonObject conf;
    if (configuration == null) {
      conf = new JsonObject();
    } else {
      conf = configuration;
    }
    vertx.<Void>executeBlocking(
        future -> {
          bridge.start(vertx, this, conf, (ar) -> {
            if (ar.failed()) {
              future.fail(ar.cause());
            } else {
              bridges.add(bridge);
              future.complete();
            }
          });
        },
        ar -> {
          if (ar.failed()) {
            LOGGER.error("Cannot start the discovery bridge " + bridge, ar.cause());
          } else {
            LOGGER.info("Discovery bridge " + bridge + " started");
          }
        }
    );
    return this;
  }

  @Override
  public void close() {
    LOGGER.info("Stopping discovery service");
    for (DiscoveryBridge bridge : bridges) {
      bridge.stop(vertx, this);
    }
  }

  @Override
  public void publish(Record record, Handler<AsyncResult<Record>> resultHandler) {
    backend.store(record.setStatus(Status.UP), resultHandler);
    Record announcedRecord = new Record(record);
    announcedRecord
        .setRegistration(null)
        .setStatus(Status.UP);
    vertx.eventBus().publish(announce, announcedRecord.toJson());
  }

  @Override
  public void unpublish(String id, Handler<AsyncResult<Void>> resultHandler) {
    backend.remove(id, record -> {
      if (record.failed()) {
        resultHandler.handle(Future.failedFuture(record.cause()));
        return;
      }
      Record announcedRecord = new Record(record.result());
      announcedRecord
          .setRegistration(null)
          .setStatus(Status.DOWN);
      vertx.eventBus().publish(announce, announcedRecord.toJson());
      resultHandler.handle(Future.succeededFuture());
    });

  }

  @Override
  public void getRecord(JsonObject filter,
                        Handler<AsyncResult<Record>> resultHandler) {
    if (filter.getString("status") == null) {
      filter.put("status", Status.UP.name());
    }
    backend.getRecords(list -> {
      if (list.failed()) {
        resultHandler.handle(Future.failedFuture(list.cause()));
      } else {
        Optional<Record> any = list.result().stream()
            .filter(record -> record.match(filter))
            .findAny();
        if (any.isPresent()) {
          resultHandler.handle(Future.succeededFuture(any.get()));
        } else {
          resultHandler.handle(Future.succeededFuture(null));
        }
      }
    });
  }

  @Override
  public void getRecords(JsonObject filter, Handler<AsyncResult<List<Record>>> resultHandler) {
    if (filter.getValue("status") == null) {
      filter.put("status", Status.UP.name());
    }
    backend.getRecords(list -> {
      if (list.failed()) {
        resultHandler.handle(Future.failedFuture(list.cause()));
      } else {
        List<Record> match = list.result().stream()
            .filter(record -> record.match(filter)).collect(Collectors.toList());
        resultHandler.handle(Future.succeededFuture(match));
      }
    });
  }

  @Override
  public void update(Record record, Handler<AsyncResult<Record>> resultHandler) {
    backend.update(record, ar -> {
      if (ar.failed()) {
        resultHandler.handle(Future.failedFuture(ar.cause()));
      } else {
        resultHandler.handle(Future.succeededFuture(record));
      }
    });

    Record announcedRecord = new Record(record);
    vertx.eventBus().publish(announce, announcedRecord.toJson());
  }
}

