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

package io.vertx.servicediscovery.types.impl;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisOptions;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.AbstractServiceReference;
import io.vertx.servicediscovery.types.RedisDataSource;

import java.util.Objects;

/**
 * The implementation of {@link RedisDataSource}.
 *
 * @author <a href="http://www.sczyh30.com">Eric Zhao</a>
 */
public class RedisDataSourceImpl implements RedisDataSource {

  @Override
  public String name() {
    return RedisDataSource.TYPE;
  }

  @Override
  public ServiceReference get(Vertx vertx, ServiceDiscovery discovery, Record record, JsonObject configuration) {
    Objects.requireNonNull(vertx);
    Objects.requireNonNull(record);
    Objects.requireNonNull(discovery);
    return new RedisServiceReference(vertx, discovery, record, configuration);
  }

  /**
   * A {@link ServiceReference} on a Redis data source. When retrieved it provides a {@link Redis}.
   */
  private class RedisServiceReference extends AbstractServiceReference<Redis> {
    private final JsonObject config;

    RedisServiceReference(Vertx vertx, ServiceDiscovery discovery, Record record, JsonObject config) {
      super(vertx, discovery, record);
      this.config = config;
    }

    /**
     * Creates a Redis client for the service.
     *
     * @return the Redis client, configured to access the service
     */
    @Override
    protected Redis retrieve() {
      JsonObject result = record().getMetadata().copy();
      result.mergeIn(record().getLocation());

      if (config != null) {
        result.mergeIn(config);
      }

      return Redis.createClient(vertx, new RedisOptions(result));
    }

    @Override
    protected void onClose() {
      service.close();
    }
  }
}
