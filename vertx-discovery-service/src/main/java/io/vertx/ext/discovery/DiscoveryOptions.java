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

package io.vertx.ext.discovery;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.discovery.spi.DiscoveryBackend;

/**
 * Options to configure the discovery service.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@DataObject(generateConverter = true)
public class DiscoveryOptions {

  public static final String DEFAULT_ANNOUNCE_ADDRESS = "vertx.discovery.announce";

  private String announceAddress = DEFAULT_ANNOUNCE_ADDRESS;
  private JsonObject backendConfiguration = new JsonObject();

  /**
   * Creates a new instance of {@link DiscoveryOptions} using the default values.
   */
  public DiscoveryOptions() {

  }

  /**
   * Creates a new instance of {@link DiscoveryOptions} by copying the values from another instance.
   *
   * @param other the instance to copy
   */
  public DiscoveryOptions(DiscoveryOptions other) {
    this.announceAddress = other.announceAddress;
    this.backendConfiguration = other.backendConfiguration.copy();
  }

  /**
   * Creates a new instance of {@link DiscoveryOptions} from its JSON representation.
   *
   * @param json the json object
   */
  public DiscoveryOptions(JsonObject json) {
    this();
    DiscoveryOptionsConverter.fromJson(json, this);
  }

  /**
   * Builds the JSON representation for the current {@link DiscoveryOptions}.
   *
   * @return the json representation
   */
  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    DiscoveryOptionsConverter.toJson(this, json);
    return json;
  }

  /**
   * @return the event bus address on which the service arrivals, departures and modifications are announced. This
   * address must be consistent in the whole application.
   */
  public String getAnnounceAddress() {
    return announceAddress;
  }

  /**
   * Sets the event bus address on which the service arrivals, departures and modifications are announced. This
   * address must be consistent in the whole application.
   *
   * @param announceAddress the address, must not be {@code null}
   * @return the current {@link DiscoveryOptions}
   */
  public DiscoveryOptions setAnnounceAddress(String announceAddress) {
    this.announceAddress = announceAddress;
    return this;
  }

  /**
   * @return the backend configuration. Cannot be {@code null}.
   */
  public JsonObject getBackendConfiguration() {
    return backendConfiguration;
  }

  /**
   * Sets the configuration passed to the {@link io.vertx.ext.discovery.spi.DiscoveryBackend}.
   * Refer to the backend documentation to get more details on the requirements. The default backend
   * does not need any configuration.
   *
   * @param backendConfiguration the backend configuration
   * @return the current {@link DiscoveryOptions}
   */
  public DiscoveryOptions setBackendConfiguration(JsonObject backendConfiguration) {
    if (backendConfiguration == null) {
      this.backendConfiguration = new JsonObject();
    } else {
      this.backendConfiguration = backendConfiguration;
    }
    return this;
  }
}
