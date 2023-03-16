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
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.Redis;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.spi.ServiceType;

import java.util.Objects;
import java.util.function.Function;

/**
 * Service type for Redis data source.
 *
 * @author <a href="http://www.sczyh30.com">Eric Zhao</a>
 */
@VertxGen
public interface RedisDataSource extends ServiceType {

  String TYPE = "redis";

  /**
   * Convenient method to create a record for a Redis data source.
   *
   * @param name     the service name
   * @param location the location of the service (e.g. url, port...)
   * @param metadata additional metadata
   * @return the created record
   */
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
   * Convenient method that looks for a Redis data source and provides the configured {@link io.vertx.redis.client.Redis}.
   * The async result is marked as failed is there are no matching services, or if the lookup fails.
   *
   * @param discovery     The service discovery instance
   * @param filter        The filter, optional
   * @return a future notified with the client
   */
  static Future<Redis> getRedisClient(ServiceDiscovery discovery, JsonObject filter) {
    return discovery.getRecord(filter).flatMap(res -> {
      if (res == null) {
        return Future.failedFuture("No matching records");
      } else {
        return Future.succeededFuture(discovery.getReference(res).get());
      }
    });
  }


  /**
   * Convenient method that looks for a Redis data source and provides the configured {@link io.vertx.redis.client.Redis}.
   * The async result is marked as failed is there are no matching services, or if the lookup fails.
   *
   * @param discovery     The service discovery instance
   * @param filter        The filter, cannot be {@code null}
   * @return a future notified with the client
   */
  static Future<Redis> getRedisClient(ServiceDiscovery discovery, Function<Record, Boolean> filter) {
    return discovery.getRecord(filter).flatMap(res -> {
      if (res == null) {
        return Future.failedFuture("No matching records");
      } else {
        return Future.succeededFuture(discovery.getReference(res).get());
      }
    });
  }

  /**
   * Convenient method that looks for a Redis data source and provides the configured {@link io.vertx.redis.client.Redis}.
   * The async result is marked as failed is there are no matching services, or if the lookup fails.
   *
   * @param discovery             The service discovery instance
   * @param filter                The filter, optional
   * @param consumerConfiguration The additional consumer configuration
   * @return a future notified with the client
   */
  static Future<Redis> getRedisClient(ServiceDiscovery discovery, JsonObject filter, JsonObject consumerConfiguration) {
    return discovery.getRecord(filter).flatMap(res -> {
      if (res == null) {
        return Future.failedFuture("No matching records");
      } else {
        return Future.succeededFuture(discovery.getReferenceWithConfiguration(res, consumerConfiguration).get());
      }
    });
  }

  /**
   * Convenient method that looks for a Redis data source and provides the configured {@link io.vertx.redis.client.Redis}.
   * The async result is marked as failed is there are no matching services, or if the lookup fails.
   *
   * @param discovery             The service discovery instance
   * @param filter                The filter, cannot be {@code null}
   * @param consumerConfiguration The additional consumer configuration
   * @return a future notified with the client
   */
  static Future<Redis> getRedisClient(ServiceDiscovery discovery, Function<Record, Boolean> filter, JsonObject
    consumerConfiguration) {
    return discovery.getRecord(filter).flatMap(res -> {
      if (res == null) {
        return Future.failedFuture("No matching records");
      } else {
        return Future.succeededFuture(discovery.getReferenceWithConfiguration(res, consumerConfiguration).get());
      }
    });
  }
}
