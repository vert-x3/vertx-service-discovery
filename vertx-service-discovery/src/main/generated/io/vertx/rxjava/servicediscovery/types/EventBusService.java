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
import io.vertx.rxjava.servicediscovery.DiscoveryService;
import io.vertx.servicediscovery.spi.ServiceType;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 *  for event bus services (service proxies).
 * Consumers receive a service proxy to use the service.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.servicediscovery.types.EventBusService original} non RX-ified interface using Vert.x codegen.
 */

public class EventBusService {

  final io.vertx.servicediscovery.types.EventBusService delegate;

  public EventBusService(io.vertx.servicediscovery.types.EventBusService delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Sugar method to creates a record for this type.
   * <p>
   * The java interface is added to the metadata in the `service.interface` key.
   * @param name the name of the service.
   * @param address the event bus address on which the service available
   * @param itf the Java interface (name)
   * @param metadata the metadata
   * @return the created record
   */
  public static Record createRecord(String name, String address, String itf, JsonObject metadata) { 
    Record ret = io.vertx.servicediscovery.types.EventBusService.createRecord(name, address, itf, metadata);
    return ret;
  }

  /**
   * Lookup for a service record and if found, retrieve it and return the service object (used to consume the service).
   * This is a convenient method to avoid explicit lookup and then retrieval of the service.
   * @param discovery the discovery service
   * @param filter the filter to select the service
   * @param resultHandler the result handler
   */
  public static <T> void getProxy(DiscoveryService discovery, JsonObject filter, Handler<AsyncResult<T>> resultHandler) { 
    io.vertx.servicediscovery.types.EventBusService.getProxy((io.vertx.servicediscovery.DiscoveryService)discovery.getDelegate(), filter, resultHandler);
  }

  /**
   * Lookup for a service record and if found, retrieve it and return the service object (used to consume the service).
   * This is a convenient method to avoid explicit lookup and then retrieval of the service.
   * @param discovery the discovery service
   * @param filter the filter to select the service
   * @return 
   */
  public static <T> Observable<T> getProxyObservable(DiscoveryService discovery, JsonObject filter) { 
    io.vertx.rx.java.ObservableFuture<T> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    getProxy(discovery, filter, resultHandler.toHandler());
    return resultHandler;
  }

  public static <T> void getProxy(DiscoveryService discovery, String serviceInterface, String proxyInterface, Handler<AsyncResult<T>> resultHandler) { 
    io.vertx.servicediscovery.types.EventBusService.getProxy((io.vertx.servicediscovery.DiscoveryService)discovery.getDelegate(), serviceInterface, proxyInterface, resultHandler);
  }

  public static <T> Observable<T> getProxyObservable(DiscoveryService discovery, String serviceInterface, String proxyInterface) { 
    io.vertx.rx.java.ObservableFuture<T> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    getProxy(discovery, serviceInterface, proxyInterface, resultHandler.toHandler());
    return resultHandler;
  }

  public static <T> void getProxy(DiscoveryService discovery, JsonObject filter, String proxyClass, Handler<AsyncResult<T>> resultHandler) { 
    io.vertx.servicediscovery.types.EventBusService.getProxy((io.vertx.servicediscovery.DiscoveryService)discovery.getDelegate(), filter, proxyClass, resultHandler);
  }

  public static <T> Observable<T> getProxyObservable(DiscoveryService discovery, JsonObject filter, String proxyClass) { 
    io.vertx.rx.java.ObservableFuture<T> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    getProxy(discovery, filter, proxyClass, resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Lookup for a service record and if found, retrieve it and return the service object (used to consume the service).
   * This is a convenient method to avoid explicit lookup and then retrieval of the service. A filter based on the
   * request interface is used.
   * @param discovery the discovery service
   * @param itf the service interface
   * @param resultHandler the result handler
   */
  public static <T> void getProxy(DiscoveryService discovery, String itf, Handler<AsyncResult<T>> resultHandler) { 
    io.vertx.servicediscovery.types.EventBusService.getProxy((io.vertx.servicediscovery.DiscoveryService)discovery.getDelegate(), itf, resultHandler);
  }

  /**
   * Lookup for a service record and if found, retrieve it and return the service object (used to consume the service).
   * This is a convenient method to avoid explicit lookup and then retrieval of the service. A filter based on the
   * request interface is used.
   * @param discovery the discovery service
   * @param itf the service interface
   * @return 
   */
  public static <T> Observable<T> getProxyObservable(DiscoveryService discovery, String itf) { 
    io.vertx.rx.java.ObservableFuture<T> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    getProxy(discovery, itf, resultHandler.toHandler());
    return resultHandler;
  }


  public static EventBusService newInstance(io.vertx.servicediscovery.types.EventBusService arg) {
    return arg != null ? new EventBusService(arg) : null;
  }
}
