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
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.spi.ServiceType;

import java.util.Objects;
import java.util.function.Function;

/**
 * {@link ServiceType} for HTTP endpoint (REST api).
 * Consumers receive a HTTP client configured with the host and port of the endpoint.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface HttpEndpoint extends ServiceType {

  String TYPE = "http-endpoint";

  /**
   * Convenient method to create a record for a HTTP endpoint.
   *
   * @param name     the service name
   * @param host     the host (IP or DNS name), it must be the _public_ IP / name
   * @param port     the port, it must be the _public_ port
   * @param root     the path of the service, "/" if not set
   * @param metadata additional metadata
   * @return the created record
   */
  static Record createRecord(String name, String host, int port, String root, JsonObject metadata) {
    return createRecord(name, false, host, port, root, metadata);
  }

  /**
   * Same as {@link #createRecord(String, String, int, String, JsonObject)} but let you configure whether or not the
   * service is using {@code https}.
   *
   * @param name     the service name
   * @param ssl      whether or not the service is using HTTPS
   * @param host     the host (IP or DNS name), it must be the _public_ IP / name
   * @param port     the port, it must be the _public_ port
   * @param root     the path of the service, "/" if not set
   * @param metadata additional metadata
   * @return the created record
   */
  static Record createRecord(String name, boolean ssl, String host, int port, String root, JsonObject metadata) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(host);
    if (root == null) {
      root = "/";
    }
    Record record = new Record().setName(name)
        .setType(TYPE)
        .setLocation(new HttpLocation()
            .setSsl(ssl).setHost(host).setPort(port).setRoot(root).toJson());

    if (metadata != null) {
      record.setMetadata(metadata);
    }

    return record;
  }

  /**
   * Same as {@link #createRecord(String, String, int, String, JsonObject)} but without metadata.
   *
   * @param name the service name
   * @param host the host, must be public
   * @param port the port
   * @param root the root, if not set "/" is used
   * @return the created record
   */
  static Record createRecord(String name, String host, int port, String root) {
    return createRecord(name, host, port, root, null);
  }

  /**
   * Same as {@link #createRecord(String, String, int, String, JsonObject)} but without metadata, using the port 80
   * and using "/" as root.
   *
   * @param name the name
   * @param host the host
   * @return the created record
   */
  static Record createRecord(String name, String host) {
    return createRecord(name, host, 80, "/", null);
  }

  /**
   * Convenient method that looks for a HTTP endpoint and provides the configured {@link HttpClient}. The async result
   * is marked as failed is there are no matching services, or if the lookup fails.
   *
   * @param discovery     The service discovery instance
   * @param filter        The filter, optional
   * @param resultHandler The result handler
   */
  static void getClient(ServiceDiscovery discovery, JsonObject filter, Handler<AsyncResult<HttpClient>>
      resultHandler) {
    discovery.getRecord(filter, ar -> {
      if (ar.failed() || ar.result() == null) {
        resultHandler.handle(Future.failedFuture("No matching record"));
      } else {
        resultHandler.handle(Future.succeededFuture(discovery.getReference(ar.result()).get()));
      }
    });
  }

  /**
   * Convenient method that looks for a HTTP endpoint and provides the configured {@link HttpClient}. The async result
   * is marked as failed is there are no matching services, or if the lookup fails.
   *
   * @param discovery     The service discovery instance
   * @param filter        The filter
   * @param resultHandler The result handler
   */
  static void getClient(ServiceDiscovery discovery, Function<Record, Boolean> filter, Handler<AsyncResult<HttpClient>>
      resultHandler) {
    discovery.getRecord(filter, ar -> {
      if (ar.failed() || ar.result() == null) {
        resultHandler.handle(Future.failedFuture("No matching record"));
      } else {
        resultHandler.handle(Future.succeededFuture(discovery.getReference(ar.result()).get()));
      }
    });
  }
}
