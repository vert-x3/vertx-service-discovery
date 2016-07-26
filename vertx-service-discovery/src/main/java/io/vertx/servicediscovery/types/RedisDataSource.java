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
import io.vertx.redis.RedisClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;

import java.util.Objects;

/**
 * Service type for Redis data source.
 *
 * @author Eric Zhao
 */
@VertxGen
public interface RedisDataSource extends DataSource {

  String TYPE = "datasource.redis";

  String DEFAULT_TYPE = "redis";

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

  static void getRedisClient(ServiceDiscovery discovery, JsonObject filter,
                             Handler<AsyncResult<RedisClient>> resultHandler) {
    discovery.getRecord(filter, ar -> {
      if (ar.failed() || ar.result() == null) {
        resultHandler.handle(Future.failedFuture("No matching record"));
      } else {
        resultHandler.handle(Future.succeededFuture(discovery.getReference(ar.result()).get()));
      }
    });
  }

  static void getRedisClient(ServiceDiscovery discovery, JsonObject filter, JsonObject consumerConfiguration,
                            Handler<AsyncResult<RedisClient>> resultHandler) {
    discovery.getRecord(filter, ar -> {
      if (ar.failed() || ar.result() == null) {
        resultHandler.handle(Future.failedFuture("No matching record"));
      } else {
        resultHandler.handle(Future.succeededFuture(
          discovery.getReferenceWithConfiguration(ar.result(), consumerConfiguration).get()));
      }
    });
  }
}
