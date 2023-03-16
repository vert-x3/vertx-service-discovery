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
import io.vertx.core.Handler;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
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
   * @return a future notified with the client
   */
  static Future<HttpClient> getClient(ServiceDiscovery discovery, JsonObject filter) {
    return discovery.getRecord(filter).flatMap(res -> {
      if (res == null) {
        return Future.failedFuture("No matching records");
      } else {
        return Future.succeededFuture(discovery.getReference(res).get());
      }
    });
  }

  /**
   * Convenient method that looks for a HTTP endpoint and provides the configured {@linkWebClient}. The async result
   * is marked as failed is there are no matching services, or if the lookup fails.
   *
   * @param discovery     The service discovery instance
   * @param filter        The filter, optional
   * @return a future notified with the client
   */
  static Future<WebClient> getWebClient(ServiceDiscovery discovery, JsonObject filter) {
    return getWebClient(discovery, filter, (JsonObject) null);
  }

  /**
   * Convenient method that looks for a HTTP endpoint and provides the configured {@link HttpClient}. The async result
   * is marked as failed is there are no matching services, or if the lookup fails. This method accepts a
   * configuration for the HTTP client
   *
   * @param discovery     The service discovery instance
   * @param filter        The filter, optional
   * @param conf          the configuration of the client
   * @return a future notified with the client
   */
  static Future<HttpClient> getClient(ServiceDiscovery discovery, JsonObject filter, JsonObject conf) {
    return discovery.getRecord(filter).flatMap(res -> {
      if (res == null) {
        return Future.failedFuture("No matching records");
      } else {
        return Future.succeededFuture(discovery.getReferenceWithConfiguration(res, conf).get());
      }
    });
  }

  /**
   * Convenient method that looks for a HTTP endpoint and provides the configured {@link WebClient}. The async result
   * is marked as failed is there are no matching services, or if the lookup fails. This method accepts a
   * configuration for the HTTP client
   *
   * @param discovery     The service discovery instance
   * @param filter        The filter, optional
   * @param conf          the configuration of the client
   * @return a future notified with the client
   */
  static Future<WebClient> getWebClient(ServiceDiscovery discovery, JsonObject filter, JsonObject conf) {
    return discovery.getRecord(filter).flatMap(res -> {
      if (res == null) {
        return Future.failedFuture("No matching records");
      }
      if (conf == null || conf.isEmpty()) {
        return Future.succeededFuture(discovery.getReference(res).getAs(WebClient.class));
      } else {
        return Future.succeededFuture(discovery.getReferenceWithConfiguration(res, conf).getAs(WebClient.class));
      }
    });
  }

  /**
   * Convenient method that looks for a HTTP endpoint and provides the configured {@link HttpClient}. The async result
   * is marked as failed is there are no matching services, or if the lookup fails.
   *
   * @param discovery     The service discovery instance
   * @param filter        The filter
   * @return a future notified with the client
   */
  static Future<HttpClient> getClient(ServiceDiscovery discovery, Function<Record, Boolean> filter) {
    return discovery.getRecord(filter).flatMap(res -> {
      if (res == null) {
        return Future.failedFuture("No matching records");
      } else {
        return Future.succeededFuture(discovery.getReference(res).get());
      }
    });
  }

  /**
   * Convenient method that looks for a HTTP endpoint and provides the configured {@link WebClient}. The async result
   * is marked as failed is there are no matching services, or if the lookup fails.
   *
   * @param discovery     The service discovery instance
   * @param filter        The filter
   * @return a future notified with the client
   */
  static Future<WebClient> getWebClient(ServiceDiscovery discovery, Function<Record, Boolean> filter) {
    return getWebClient(discovery, filter, (JsonObject) null);
  }

  /**
   * Convenient method that looks for a HTTP endpoint and provides the configured {@link HttpClient}. The async result
   * is marked as failed is there are no matching services, or if the lookup fails. This method accepts a
   * configuration for the HTTP client.
   *
   * @param discovery     The service discovery instance
   * @param filter        The filter
   * @param conf          the configuration of the client
   * @return a future notified with the client
   */
  static Future<HttpClient> getClient(ServiceDiscovery discovery, Function<Record, Boolean> filter, JsonObject conf) {
    return discovery.getRecord(filter).flatMap(res -> {
      if (res == null) {
        return Future.failedFuture("No matching records");
      } else {
        return Future.succeededFuture(discovery.getReferenceWithConfiguration(res, conf).get());
      }
    });
  }

  /**
   * Convenient method that looks for a HTTP endpoint and provides the configured {@link WebClient}. The async result
   * is marked as failed is there are no matching services, or if the lookup fails. This method accepts a
   * configuration for the HTTP client.
   *
   * @param discovery     The service discovery instance
   * @param filter        The filter
   * @param conf          the configuration of the client
   * @return a future notified with the client
   */
  static Future<WebClient> getWebClient(ServiceDiscovery discovery, Function<Record, Boolean> filter, JsonObject conf) {
    return discovery.getRecord(filter).flatMap(res -> {
      if (res == null) {
        return Future.failedFuture("No matching records");
      }
      if (conf == null || conf.isEmpty()) {
        return Future.succeededFuture(discovery.getReference(res).getAs(WebClient.class));
      } else {
        return Future.succeededFuture(discovery.getReferenceWithConfiguration(res, conf).getAs(WebClient.class));
      }
    });
  }
}
