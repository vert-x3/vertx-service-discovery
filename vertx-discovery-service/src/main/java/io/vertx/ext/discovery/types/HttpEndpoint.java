/*
 * Copyright (c) 2011-$tody.year The original author or authors
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

package io.vertx.ext.discovery.types;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.discovery.Record;
import io.vertx.ext.discovery.ServiceReference;
import io.vertx.ext.discovery.spi.ServiceType;

import java.util.Objects;

/**
 * Implementation of {@link ServiceType} for HTTP endpoint (REST api).
 * Consumers receive a HTTP client configured with the host and port of the endpoint.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class HttpEndpoint implements ServiceType {

  public static final String TYPE = "http-endpoint";

  @Override
  public String name() {
    return TYPE;
  }

  @Override
  public ServiceReference get(Vertx vertx, Record record) {
    return new HttpEndpointReference(vertx, record);
  }

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
  public static Record createRecord(String name, String host, int port, String root, JsonObject metadata) {
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
  public static Record createRecord(String name, boolean ssl, String host, int port, String root, JsonObject metadata) {
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
  public static Record createRecord(String name, String host, int port, String root) {
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
  public static Record createRecord(String name, String host) {
    return createRecord(name, host, 80, "/", null);
  }

  /**
   * {@link ServiceReference} implementation for the HTTP endpoint.
   */
  private class HttpEndpointReference implements ServiceReference {

    private final Vertx vertx;
    private final HttpLocation location;
    private final Record record;
    private HttpClient client;

    HttpEndpointReference(Vertx vertx, Record record) {
      this.vertx = vertx;
      this.location = new HttpLocation(record.getLocation());
      this.record = record;
    }

    /**
     * @return the service record.
     */
    @Override
    public Record record() {
      return record;
    }

    /**
     * Gets a HTTP client to access the service.
     *
     * @param <T> {@link HttpClient}
     * @return the HTTP client, configured to access the service
     */
    @Override
    public synchronized <T> T get() {
      if (client != null) {
        return (T) client;
      } else {
        HttpClientOptions options = new HttpClientOptions().setDefaultPort(location.getPort())
            .setDefaultHost(location.getHost());
        if (location.isSsl()) {
          options.setSsl(true);
        }

        client = vertx.createHttpClient(options);
        return (T) client;
      }
    }

    /**
     * Closes the client.
     */
    @Override
    public synchronized void release() {
      client.close();
      client = null;
    }
  }
}
