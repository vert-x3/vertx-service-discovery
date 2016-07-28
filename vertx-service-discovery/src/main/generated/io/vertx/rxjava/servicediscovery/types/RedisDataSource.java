/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.rxjava.servicediscovery.types;

import java.util.Map;
import rx.Observable;
import io.vertx.servicediscovery.types.DataSource;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.redis.RedisClient;
import io.vertx.servicediscovery.Record;
import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * Service type for Redis data source.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.servicediscovery.types.RedisDataSource original} non RX-ified interface using Vert.x codegen.
 */

public class RedisDataSource {

  final io.vertx.servicediscovery.types.RedisDataSource delegate;

  public RedisDataSource(io.vertx.servicediscovery.types.RedisDataSource delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Convenient method to create a record for a Redis data source.
   * @param name the service name
   * @param location the location of the service (e.g. url, port...)
   * @param metadata additional metadata
   * @return the created record
   */
  public static Record createRecord(String name, JsonObject location, JsonObject metadata) { 
    Record ret = io.vertx.servicediscovery.types.RedisDataSource.createRecord(name, location, metadata);
    return ret;
  }

  /**
   * Convenient method that looks for a Redis data source and provides the configured {@link io.vertx.rxjava.redis.RedisClient}.
   * The async result is marked as failed is there are no matching services, or if the lookup fails.
   * @param discovery The service discovery instance
   * @param filter The filter, optional
   * @param resultHandler The result handler
   */
  public static void getRedisClient(ServiceDiscovery discovery, JsonObject filter, Handler<AsyncResult<RedisClient>> resultHandler) { 
    io.vertx.servicediscovery.types.RedisDataSource.getRedisClient((io.vertx.servicediscovery.ServiceDiscovery)discovery.getDelegate(), filter, new Handler<AsyncResult<io.vertx.redis.RedisClient>>() {
      public void handle(AsyncResult<io.vertx.redis.RedisClient> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture(RedisClient.newInstance(ar.result())));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    });
  }

  /**
   * Convenient method that looks for a Redis data source and provides the configured {@link io.vertx.rxjava.redis.RedisClient}.
   * The async result is marked as failed is there are no matching services, or if the lookup fails.
   * @param discovery The service discovery instance
   * @param filter The filter, optional
   * @return 
   */
  public static Observable<RedisClient> getRedisClientObservable(ServiceDiscovery discovery, JsonObject filter) { 
    io.vertx.rx.java.ObservableFuture<RedisClient> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    getRedisClient(discovery, filter, resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Convenient method that looks for a Redis data source and provides the configured {@link io.vertx.rxjava.redis.RedisClient}.
   * The async result is marked as failed is there are no matching services, or if the lookup fails.
   * @param discovery The service discovery instance
   * @param filter The filter, optional
   * @param consumerConfiguration The additional consumer configuration
   * @param resultHandler The result handler
   */
  public static void getRedisClient(ServiceDiscovery discovery, JsonObject filter, JsonObject consumerConfiguration, Handler<AsyncResult<RedisClient>> resultHandler) { 
    io.vertx.servicediscovery.types.RedisDataSource.getRedisClient((io.vertx.servicediscovery.ServiceDiscovery)discovery.getDelegate(), filter, consumerConfiguration, new Handler<AsyncResult<io.vertx.redis.RedisClient>>() {
      public void handle(AsyncResult<io.vertx.redis.RedisClient> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture(RedisClient.newInstance(ar.result())));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    });
  }

  /**
   * Convenient method that looks for a Redis data source and provides the configured {@link io.vertx.rxjava.redis.RedisClient}.
   * The async result is marked as failed is there are no matching services, or if the lookup fails.
   * @param discovery The service discovery instance
   * @param filter The filter, optional
   * @param consumerConfiguration The additional consumer configuration
   * @return 
   */
  public static Observable<RedisClient> getRedisClientObservable(ServiceDiscovery discovery, JsonObject filter, JsonObject consumerConfiguration) { 
    io.vertx.rx.java.ObservableFuture<RedisClient> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    getRedisClient(discovery, filter, consumerConfiguration, resultHandler.toHandler());
    return resultHandler;
  }


  public static RedisDataSource newInstance(io.vertx.servicediscovery.types.RedisDataSource arg) {
    return arg != null ? new RedisDataSource(arg) : null;
  }
}
