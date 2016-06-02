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

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Represents the location of a HTTP endpoint. This object (its json representation) will be used as "location" in a
 * service record.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@DataObject(generateConverter = true)
public class HttpLocation {

  private String host;
  private int port;
  private String root = "";
  private String endpoint;
  private boolean ssl = false;


  /**
   * Creates a new {@link HttpLocation} instance.
   */
  public HttpLocation() {
    // empty constructor
  }

  /**
   * Creates a new {@link HttpLocation} instance by copying another instance.
   *
   * @param other the instance fo copy
   */
  public HttpLocation(HttpLocation other) {
    this.host = other.host;
    this.port = other.port;
    this.root = other.root;
    this.ssl = other.ssl;
  }

  /**
   * Creates a new {@link HttpLocation} from the given json object
   *
   * @param json the json object
   */
  public HttpLocation(JsonObject json) {
    this();
    HttpLocationConverter.fromJson(json, this);
  }

  /**
   * @return a json representation of the current {@link HttpLocation}.
   */
  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    HttpLocationConverter.toJson(this, json);
    return json;
  }

  /**
   * @return the host
   */
  public String getHost() {
    return host;
  }

  /**
   * Sets the host.
   *
   * @param host the host
   * @return the current {@link HttpLocation}
   */
  public HttpLocation setHost(String host) {
    this.host = host;
    updateLocation();
    return this;
  }

  /**
   * Sets the endpoint, which is the URL of the service. The endpoint is automatically computed when you use the
   * other `setX` method.
   *
   * @param endpoint the endpoint
   * @return the current {@link HttpLocation}
   */
  public HttpLocation setEndpoint(String endpoint) {
    this.endpoint = endpoint;
    return this;
  }

  /**
   * @return the URL of the service
   */
  public String getEndpoint() {
    return endpoint;
  }

  /**
   * @return the port.
   */
  public int getPort() {
    return port;
  }

  /**
   * Sets the port
   *
   * @param port the port
   * @return the current {@link HttpLocation}
   */
  public HttpLocation setPort(int port) {
    this.port = port;
    updateLocation();
    return this;
  }

  /**
   * @return the path of the service (root)
   */
  public String getRoot() {
    updateLocation();
    return root;
  }

  /**
   * Sets the path of the service (root)
   *
   * @param root the root
   * @return the current {@link HttpLocation}
   */
  public HttpLocation setRoot(String root) {
    if (root.startsWith("/")) {
      this.root = root;
    } else {
      this.root = "/" + root;
    }
    updateLocation();
    return this;
  }

  private void updateLocation() {
    setEndpoint("http" + (isSsl() ? "s" : "") + "://" + host + ":" + port + root);
  }

  /**
   * Sets whether or not the HTTP service is using {@code https}.
   *
   * @param ssl {@code true} to denotes that the service use {@code https}
   * @return the current {@link HttpLocation}
   */
  public HttpLocation setSsl(boolean ssl) {
    this.ssl = ssl;
    updateLocation();
    return this;
  }

  /**
   * @return {@code true} if the location is using {@code https}, {@code false} otherwise.
   */
  public boolean isSsl() {
    return ssl;
  }
}
