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
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.rxjava.core.http.HttpClient;

/**
 *  for HTTP endpoint (REST api).
 * Consumers receive a HTTP client configured with the host and port of the endpoint.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.servicediscovery.types.HttpEndpoint original} non RX-ified interface using Vert.x codegen.
 */

public class HttpEndpoint {

  final io.vertx.servicediscovery.types.HttpEndpoint delegate;

  public HttpEndpoint(io.vertx.servicediscovery.types.HttpEndpoint delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public static HttpEndpointType serviceType() { 
    HttpEndpointType ret = HttpEndpointType.newInstance(io.vertx.servicediscovery.types.HttpEndpoint.serviceType());
    return ret;
  }

  /**
   * Convenient method to create a record for a HTTP endpoint.
   * @param name the service name
   * @param host the host (IP or DNS name), it must be the _public_ IP / name
   * @param port the port, it must be the _public_ port
   * @param root the path of the service, "/" if not set
   * @param metadata additional metadata
   * @return the created record
   */
  public static Record createRecord(String name, String host, int port, String root, JsonObject metadata) { 
    Record ret = io.vertx.servicediscovery.types.HttpEndpoint.createRecord(name, host, port, root, metadata);
    return ret;
  }

  /**
   * Same as {@link io.vertx.rxjava.servicediscovery.types.HttpEndpoint#createRecord} but let you configure whether or not the
   * service is using <code>https</code>.
   * @param name the service name
   * @param ssl whether or not the service is using HTTPS
   * @param host the host (IP or DNS name), it must be the _public_ IP / name
   * @param port the port, it must be the _public_ port
   * @param root the path of the service, "/" if not set
   * @param metadata additional metadata
   * @return the created record
   */
  public static Record createRecord(String name, boolean ssl, String host, int port, String root, JsonObject metadata) { 
    Record ret = io.vertx.servicediscovery.types.HttpEndpoint.createRecord(name, ssl, host, port, root, metadata);
    return ret;
  }

  /**
   * Same as {@link io.vertx.rxjava.servicediscovery.types.HttpEndpoint#createRecord} but without metadata.
   * @param name the service name
   * @param host the host, must be public
   * @param port the port
   * @param root the root, if not set "/" is used
   * @return the created record
   */
  public static Record createRecord(String name, String host, int port, String root) { 
    Record ret = io.vertx.servicediscovery.types.HttpEndpoint.createRecord(name, host, port, root);
    return ret;
  }

  /**
   * Same as {@link io.vertx.rxjava.servicediscovery.types.HttpEndpoint#createRecord} but without metadata, using the port 80
   * and using "/" as root.
   * @param name the name
   * @param host the host
   * @return the created record
   */
  public static Record createRecord(String name, String host) { 
    Record ret = io.vertx.servicediscovery.types.HttpEndpoint.createRecord(name, host);
    return ret;
  }

  /**
   * Convenient method that looks for a HTTP endpoint and provides the configured . The async result
   * is marked as failed is there are no matching services, or if the lookup fails.
   * @param discovery The service discovery instance
   * @param filter The filter, optional
   * @param resultHandler The result handler
   */
  public static void getClient(ServiceDiscovery discovery, JsonObject filter, Handler<AsyncResult<HttpClient>> resultHandler) { 
    io.vertx.servicediscovery.types.HttpEndpoint.getClient((io.vertx.servicediscovery.ServiceDiscovery)discovery.getDelegate(), filter, new Handler<AsyncResult<io.vertx.core.http.HttpClient>>() {
      public void handle(AsyncResult<io.vertx.core.http.HttpClient> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture(HttpClient.newInstance(ar.result())));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    });
  }

  /**
   * Convenient method that looks for a HTTP endpoint and provides the configured . The async result
   * is marked as failed is there are no matching services, or if the lookup fails.
   * @param discovery The service discovery instance
   * @param filter The filter, optional
   * @return 
   */
  public static Observable<HttpClient> getClientObservable(ServiceDiscovery discovery, JsonObject filter) { 
    io.vertx.rx.java.ObservableFuture<HttpClient> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    getClient(discovery, filter, resultHandler.toHandler());
    return resultHandler;
  }


  public static HttpEndpoint newInstance(io.vertx.servicediscovery.types.HttpEndpoint arg) {
    return arg != null ? new HttpEndpoint(arg) : null;
  }
}
