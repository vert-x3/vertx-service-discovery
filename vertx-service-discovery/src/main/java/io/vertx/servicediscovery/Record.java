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

import java.util.Objects;

/**
 * Describes a `service`. The record is the only piece of information shared between consumer and provider. It should
 * contains enough metadata to let consumer find the service they want.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@DataObject(generateConverter = true)
public class Record {

  public static final String ENDPOINT = "endpoint";

  private JsonObject location;

  private JsonObject metadata = new JsonObject();

  private String name;

  private Status status = Status.UNKNOWN;

  private String registration;

  private String type;

  /**
   * Creates a new {@link Record}.
   */
  public Record() {
    // empty constructor.
  }

  /**
   * Creates a new {@link Record} from its json representation.
   *
   * @param json the json object
   */
  public Record(JsonObject json) {
    RecordConverter.fromJson(json, this);
  }

  /**
   * @return the JSON representation of the current {@link Record}.
   */
  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    RecordConverter.toJson(this, json);
    return json;
  }

  /**
   * Creates a new {@link Record} by copying the values from another {@link Record}.
   *
   * @param other the record to copy
   */
  public Record(Record other) {
    this.location = other.location;
    this.metadata = other.metadata;
    this.name = other.name;
    this.status = other.status;
    this.registration = other.registration;
    this.type = other.type;
  }

  /**
   * @return the json object describing the location of the service. By convention, this json object should contain
   * the {@link #ENDPOINT} entry.
   */
  public JsonObject getLocation() {
    return location;
  }

  /**
   * Sets the json object describing the location of the service. By convention, this json object should contain
   * the {@link #ENDPOINT} entry.
   *
   * @param location the location
   * @return the current {@link Record}
   */
  public Record setLocation(JsonObject location) {
    this.location = location;
    return this;
  }

  /**
   * Gets the metadata attached to the record.
   *
   * @return the metadata, cannot be {@code null}.
   */
  public JsonObject getMetadata() {
    return metadata;
  }

  public Record setMetadata(JsonObject metadata) {
    this.metadata = metadata;
    return this;
  }

  /**
   * Gets the name of the service. It can reflect the service name of the name of the provider.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the service. It can reflect the service name of the name of the provider.
   *
   * @param name the name
   * @return the current {@link Record}
   */
  public Record setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Gets the current status of the service.
   *
   * @return the status.
   */
  public Status getStatus() {
    return status;
  }

  /**
   * Sets the status of the service. When published, the status is set to{@link Status#UP}. When withdrawn, the
   * status is set to {@link Status#DOWN}.
   *
   * @param status the status, must not be {@code null}
   * @return the current {@link Record}
   */
  public Record setStatus(Status status) {
    Objects.requireNonNull(status);
    this.status = status;
    return this;
  }

  /**
   * Sets the registration id. This method is called when the service is published.
   *
   * @param reg the registration id
   * @return the current {@link Record}
   */
  public Record setRegistration(String reg) {
    this.registration = reg;
    return this;
  }

  /**
   * Gets the registration id if any. Getting a {@code null} result means that the record has not been published.
   *
   * @return the registration id.
   */
  public String getRegistration() {
    return registration;
  }

  /**
   * Gets the service type. The type represents what kind of "resource" is represented by this record. For example it
   * can be "http-endpoint", "database", "message-source"... The set of types is extensible.
   * <p>
   * The type defines how the the service object is retrieved, and also manages the binding. Some records may have no
   * type and let the consumer manage how the service is used.
   *
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the type of service.
   *
   * @param type the type
   * @return the current {@link Record}
   */
  public Record setType(String type) {
    this.type = type;
    return this;
  }

  /**
   * Checks whether or not the current {@link Record} matches the filter.
   *
   * @param filter the filter
   * @return whether or not the record matches the filter
   */
  public boolean match(JsonObject filter) {
    for (String key : filter.fieldNames()) {
      boolean match;
      switch (key) {
        case "name":
          match = match(getName(), filter.getString("name"));
          break;
        case "registration":
          match = match(getRegistration(), filter.getString("registration"));
          break;
        case "status":
          match = match(getStatus().name(), filter.getString("status"));
          break;
        case "type":
          match = match(getType(), filter.getString("type"));
          break;
        default:
          // metadata
          match = match(getMetadata().getValue(key), filter.getValue(key));
          break;
      }

      if (!match) {
        return false;
      }
    }

    return true;
  }

  private boolean match(Object actual, Object expected) {
    return actual != null
        && ("*".equals(expected) ||
        (actual instanceof String ?
            ((String) actual).equalsIgnoreCase(expected.toString()) : actual.equals(expected)));
  }

  /**
   * Checks whether or not the current object is equal to the given object.
   *
   * @param o the object to compare to this service record
   * @return {@code true} when equal, {@code false} otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Record)) return false;
    Record other = (Record) o;
    return (name == null ? other.name == null : name.equals(other.name)) &&
        (type == null ? other.type == null : type.equals(other.type)) &&
        (location == null ? other.location == null : location.equals(other.location)) &&
        (metadata == null ? other.metadata == null : metadata.equals(other.metadata)) &&
        (registration == null ? other.registration == null : registration.equals(other.registration)) &&
        (status == null ? other.status == null : status.equals(other.status));
  }

  /**
   * @return the hash code of the current service record
   */
  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (name == null ? 0 : name.hashCode());
    result = 31 * result + (type == null ? 0 : type.hashCode());
    result = 31 * result + (location == null ? 0 : location.hashCode());
    result = 31 * result + (metadata == null ? 0 : metadata.hashCode());
    result = 31 * result + (registration == null ? 0 : registration.hashCode());
    result = 31 * result + (status == null ? 0 : status.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "Record[" + toJson() + "]";
  }
}
