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

package io.vertx.servicediscovery;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackend;

/**
 * Options to configure the service discovery.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@DataObject(generateConverter = true)
public class ServiceDiscoveryOptions {

  public static final String DEFAULT_ANNOUNCE_ADDRESS = "vertx.discovery.announce";
  public static final String DEFAULT_USAGE_ADDRESS = "vertx.discovery.usage";

  private String announceAddress = DEFAULT_ANNOUNCE_ADDRESS;
  private JsonObject backendConfiguration = new JsonObject();
  private String name = null;
  private String usageAddress = DEFAULT_USAGE_ADDRESS;

  /**
   * Creates a new instance of {@link ServiceDiscoveryOptions} using the default values.
   */
  public ServiceDiscoveryOptions() {
     // Empty constructor.
  }

  /**
   * Creates a new instance of {@link ServiceDiscoveryOptions} by copying the values from another instance.
   *
   * @param other the instance to copy
   */
  public ServiceDiscoveryOptions(ServiceDiscoveryOptions other) {
    this.announceAddress = other.announceAddress;
    this.backendConfiguration = other.backendConfiguration.copy();
    this.name = other.name;
    this.usageAddress = other.usageAddress;
  }

  /**
   * Creates a new instance of {@link ServiceDiscoveryOptions} from its JSON representation.
   *
   * @param json the json object
   */
  public ServiceDiscoveryOptions(JsonObject json) {
    this();
    ServiceDiscoveryOptionsConverter.fromJson(json, this);
  }

  /**
   * Builds the JSON representation for the current {@link ServiceDiscoveryOptions}.
   *
   * @return the json representation
   */
  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    ServiceDiscoveryOptionsConverter.toJson(this, json);
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
   * @return the current {@link ServiceDiscoveryOptions}
   */
  public ServiceDiscoveryOptions setAnnounceAddress(String announceAddress) {
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
   * Sets the configuration passed to the {@link ServiceDiscoveryBackend}.
   * Refer to the backend documentation to get more details on the requirements. The default backend
   * does not need any configuration.
   *
   * @param backendConfiguration the backend configuration
   * @return the current {@link ServiceDiscoveryOptions}
   */
  public ServiceDiscoveryOptions setBackendConfiguration(JsonObject backendConfiguration) {
    if (backendConfiguration == null) {
      this.backendConfiguration = new JsonObject();
    } else {
      this.backendConfiguration = backendConfiguration;
    }
    return this;
  }

  /**
   * Sets the service discovery name used in the service usage events.
   * If not set, the node id is used.
   *
   * @param name the name to use.
   * @return the current {@link ServiceDiscoveryOptions}
   */
  public ServiceDiscoveryOptions setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Gets the service discovery name used in service usage events. If not set the node id is used.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the event bus address on which are sent the service usage events (bind / release).
   */
  public String getUsageAddress() {
    return usageAddress;
  }

  /**
   * Sets the usage address: the event bus address on which are sent the service usage events (bind / release).
   *
   * @param usageAddress the address, {@link null} to disable use service usage tracking
   * @return the current {@link ServiceDiscoveryOptions}
   */
  public ServiceDiscoveryOptions setUsageAddress(String usageAddress) {
    this.usageAddress = usageAddress;
    return this;
  }
}
