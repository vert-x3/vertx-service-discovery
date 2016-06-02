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
import io.vertx.servicediscovery.spi.ServiceType;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.rxjava.core.eventbus.MessageConsumer;

/**
 * Service type for data producer. Providers are publishing data to a specific event bus address.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.servicediscovery.types.MessageSource original} non RX-ified interface using Vert.x codegen.
 */

public class MessageSource {

  final io.vertx.servicediscovery.types.MessageSource delegate;

  public MessageSource(io.vertx.servicediscovery.types.MessageSource delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Create a record representing a data producer.
   * @param name the name of the service
   * @param address the address on which the data is sent
   * @param type the type of payload (fully qualified name of the class)
   * @param metadata additional metadata
   * @return the created record
   */
  public static Record createRecord(String name, String address, String type, JsonObject metadata) { 
    Record ret = io.vertx.servicediscovery.types.MessageSource.createRecord(name, address, type, metadata);
    return ret;
  }

  /**
   * Same as {@link io.vertx.rxjava.servicediscovery.types.MessageSource#createRecord} without additional metadata.
   * @param name the name of the service
   * @param address the address on which the data is sent
   * @param type the type of payload
   * @return the created record
   */
  public static Record createRecord(String name, String address, String type) { 
    Record ret = io.vertx.servicediscovery.types.MessageSource.createRecord(name, address, type);
    return ret;
  }

  /**
   * Same as {@link io.vertx.rxjava.servicediscovery.types.MessageSource#createRecord} without additional metadata, and no type for
   * the payload.
   * @param name the name of the service
   * @param address the address on which the data is sent.
   * @return the created record
   */
  public static Record createRecord(String name, String address) { 
    Record ret = io.vertx.servicediscovery.types.MessageSource.createRecord(name, address);
    return ret;
  }

  /**
   * Convenient method that looks for a message source and provides the configured . The
   * async result is marked as failed is there are no matching services, or if the lookup fails.
   * @param discovery The service discovery instance
   * @param filter The filter, optional
   * @param resultHandler The result handler
   */
  public static <T> void getConsumer(ServiceDiscovery discovery, JsonObject filter, Handler<AsyncResult<MessageConsumer<T>>> resultHandler) { 
    io.vertx.servicediscovery.types.MessageSource.getConsumer((io.vertx.servicediscovery.ServiceDiscovery)discovery.getDelegate(), filter, new Handler<AsyncResult<io.vertx.core.eventbus.MessageConsumer<T>>>() {
      public void handle(AsyncResult<io.vertx.core.eventbus.MessageConsumer<T>> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture(MessageConsumer.newInstance(ar.result())));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    });
  }

  /**
   * Convenient method that looks for a message source and provides the configured . The
   * async result is marked as failed is there are no matching services, or if the lookup fails.
   * @param discovery The service discovery instance
   * @param filter The filter, optional
   * @return 
   */
  public static <T> Observable<MessageConsumer<T>> getConsumerObservable(ServiceDiscovery discovery, JsonObject filter) { 
    io.vertx.rx.java.ObservableFuture<MessageConsumer<T>> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    getConsumer(discovery, filter, resultHandler.toHandler());
    return resultHandler;
  }


  public static MessageSource newInstance(io.vertx.servicediscovery.types.MessageSource arg) {
    return arg != null ? new MessageSource(arg) : null;
  }
}
