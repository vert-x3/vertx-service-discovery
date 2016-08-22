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
import io.vertx.rxjava.ext.jdbc.JDBCClient;
import io.vertx.servicediscovery.types.DataSource;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.servicediscovery.types.JDBCDataSource original} non RX-ified interface using Vert.x codegen.
 */

public class JDBCDataSource {

  final io.vertx.servicediscovery.types.JDBCDataSource delegate;

  public JDBCDataSource(io.vertx.servicediscovery.types.JDBCDataSource delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public static Record createRecord(String name, JsonObject location, JsonObject metadata) { 
    Record ret = io.vertx.servicediscovery.types.JDBCDataSource.createRecord(name, location, metadata);
    return ret;
  }

  /**
   * Convenient method that looks for a JDBC datasource source and provides the configured {@link io.vertx.rxjava.ext.jdbc.JDBCClient}. The
   * async result is marked as failed is there are no matching services, or if the lookup fails.
   * @param discovery The service discovery instance
   * @param filter The filter, optional
   * @param resultHandler The result handler
   */
  public static void getJDBCClient(ServiceDiscovery discovery, JsonObject filter, Handler<AsyncResult<JDBCClient>> resultHandler) { 
    io.vertx.servicediscovery.types.JDBCDataSource.getJDBCClient((io.vertx.servicediscovery.ServiceDiscovery)discovery.getDelegate(), filter, new Handler<AsyncResult<io.vertx.ext.jdbc.JDBCClient>>() {
      public void handle(AsyncResult<io.vertx.ext.jdbc.JDBCClient> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture(JDBCClient.newInstance(ar.result())));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    });
  }

  /**
   * Convenient method that looks for a JDBC datasource source and provides the configured {@link io.vertx.rxjava.ext.jdbc.JDBCClient}. The
   * async result is marked as failed is there are no matching services, or if the lookup fails.
   * @param discovery The service discovery instance
   * @param filter The filter, optional
   * @return 
   */
  public static Observable<JDBCClient> getJDBCClientObservable(ServiceDiscovery discovery, JsonObject filter) { 
    io.vertx.rx.java.ObservableFuture<JDBCClient> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    getJDBCClient(discovery, filter, resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Convenient method that looks for a JDBC datasource source and provides the configured {@link io.vertx.rxjava.ext.jdbc.JDBCClient}. The
   * async result is marked as failed is there are no matching services, or if the lookup fails.
   * @param discovery The service discovery instance
   * @param filter The filter, optional
   * @param consumerConfiguration the consumer configuration
   * @param resultHandler the result handler
   */
  public static void getJDBCClient(ServiceDiscovery discovery, JsonObject filter, JsonObject consumerConfiguration, Handler<AsyncResult<JDBCClient>> resultHandler) { 
    io.vertx.servicediscovery.types.JDBCDataSource.getJDBCClient((io.vertx.servicediscovery.ServiceDiscovery)discovery.getDelegate(), filter, consumerConfiguration, new Handler<AsyncResult<io.vertx.ext.jdbc.JDBCClient>>() {
      public void handle(AsyncResult<io.vertx.ext.jdbc.JDBCClient> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture(JDBCClient.newInstance(ar.result())));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    });
  }

  /**
   * Convenient method that looks for a JDBC datasource source and provides the configured {@link io.vertx.rxjava.ext.jdbc.JDBCClient}. The
   * async result is marked as failed is there are no matching services, or if the lookup fails.
   * @param discovery The service discovery instance
   * @param filter The filter, optional
   * @param consumerConfiguration the consumer configuration
   * @return 
   */
  public static Observable<JDBCClient> getJDBCClientObservable(ServiceDiscovery discovery, JsonObject filter, JsonObject consumerConfiguration) { 
    io.vertx.rx.java.ObservableFuture<JDBCClient> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    getJDBCClient(discovery, filter, consumerConfiguration, resultHandler.toHandler());
    return resultHandler;
  }


  public static JDBCDataSource newInstance(io.vertx.servicediscovery.types.JDBCDataSource arg) {
    return arg != null ? new JDBCDataSource(arg) : null;
  }
}
