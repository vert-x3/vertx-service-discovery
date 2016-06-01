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

package io.vertx.groovy.ext.servicediscovery.types;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import io.vertx.groovy.ext.servicediscovery.DiscoveryService
import io.vertx.ext.servicediscovery.Record
import io.vertx.core.json.JsonObject
import io.vertx.core.AsyncResult
import io.vertx.ext.servicediscovery.spi.ServiceType
import io.vertx.core.Handler
/**
 *  for event bus services (service proxies).
 * Consumers receive a service proxy to use the service.
*/
@CompileStatic
public class EventBusService {
  private final def io.vertx.ext.servicediscovery.types.EventBusService delegate;
  public EventBusService(Object delegate) {
    this.delegate = (io.vertx.ext.servicediscovery.types.EventBusService) delegate;
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
   * @return the created record (see <a href="../../../../../../../../cheatsheet/Record.html">Record</a>)
   */
  public static Map<String, Object> createRecord(String name, String address, String itf, Map<String, Object> metadata) {
    def ret = (Map<String, Object>)InternalHelper.wrapObject(io.vertx.ext.servicediscovery.types.EventBusService.createRecord(name, address, itf, metadata != null ? new io.vertx.core.json.JsonObject(metadata) : null)?.toJson());
    return ret;
  }
  /**
   * Lookup for a service record and if found, retrieve it and return the service object (used to consume the service).
   * This is a convenient method to avoid explicit lookup and then retrieval of the service.
   * @param discovery the discovery service
   * @param filter the filter to select the service
   * @param resultHandler the result handler
   */
  public static <T> void getProxy(DiscoveryService discovery, Map<String, Object> filter, Handler<AsyncResult<T>> resultHandler) {
    io.vertx.ext.servicediscovery.types.EventBusService.getProxy(discovery != null ? (io.vertx.ext.servicediscovery.DiscoveryService)discovery.getDelegate() : null, filter != null ? new io.vertx.core.json.JsonObject(filter) : null, resultHandler != null ? new Handler<AsyncResult<java.lang.Object>>() {
      public void handle(AsyncResult<java.lang.Object> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((Object) InternalHelper.wrapObject(ar.result())));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
  }
  public static <T> void getProxy(DiscoveryService discovery, String serviceInterface, String proxyInterface, Handler<AsyncResult<T>> resultHandler) {
    io.vertx.ext.servicediscovery.types.EventBusService.getProxy(discovery != null ? (io.vertx.ext.servicediscovery.DiscoveryService)discovery.getDelegate() : null, serviceInterface, proxyInterface, resultHandler != null ? new Handler<AsyncResult<java.lang.Object>>() {
      public void handle(AsyncResult<java.lang.Object> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((Object) InternalHelper.wrapObject(ar.result())));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
  }
  public static <T> void getProxy(DiscoveryService discovery, Map<String, Object> filter, String proxyClass, Handler<AsyncResult<T>> resultHandler) {
    io.vertx.ext.servicediscovery.types.EventBusService.getProxy(discovery != null ? (io.vertx.ext.servicediscovery.DiscoveryService)discovery.getDelegate() : null, filter != null ? new io.vertx.core.json.JsonObject(filter) : null, proxyClass, resultHandler != null ? new Handler<AsyncResult<java.lang.Object>>() {
      public void handle(AsyncResult<java.lang.Object> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((Object) InternalHelper.wrapObject(ar.result())));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
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
    io.vertx.ext.servicediscovery.types.EventBusService.getProxy(discovery != null ? (io.vertx.ext.servicediscovery.DiscoveryService)discovery.getDelegate() : null, itf, resultHandler != null ? new Handler<AsyncResult<java.lang.Object>>() {
      public void handle(AsyncResult<java.lang.Object> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((Object) InternalHelper.wrapObject(ar.result())));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
  }
}
