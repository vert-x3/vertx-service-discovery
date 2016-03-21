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

package io.vertx.ext.discovery;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.discovery.Record}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.discovery.Record} original class using Vert.x codegen.
 */
public class RecordConverter {

  public static void fromJson(JsonObject json, Record obj) {
    if (json.getValue("location") instanceof JsonObject) {
      obj.setLocation(((JsonObject)json.getValue("location")).copy());
    }
    if (json.getValue("metadata") instanceof JsonObject) {
      obj.setMetadata(((JsonObject)json.getValue("metadata")).copy());
    }
    if (json.getValue("name") instanceof String) {
      obj.setName((String)json.getValue("name"));
    }
    if (json.getValue("registration") instanceof String) {
      obj.setRegistration((String)json.getValue("registration"));
    }
    if (json.getValue("status") instanceof String) {
      obj.setStatus(io.vertx.ext.discovery.Status.valueOf((String)json.getValue("status")));
    }
    if (json.getValue("type") instanceof String) {
      obj.setType((String)json.getValue("type"));
    }
  }

  public static void toJson(Record obj, JsonObject json) {
    if (obj.getLocation() != null) {
      json.put("location", obj.getLocation());
    }
    if (obj.getMetadata() != null) {
      json.put("metadata", obj.getMetadata());
    }
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
    if (obj.getRegistration() != null) {
      json.put("registration", obj.getRegistration());
    }
    if (obj.getStatus() != null) {
      json.put("status", obj.getStatus().name());
    }
    if (obj.getType() != null) {
      json.put("type", obj.getType());
    }
  }
}