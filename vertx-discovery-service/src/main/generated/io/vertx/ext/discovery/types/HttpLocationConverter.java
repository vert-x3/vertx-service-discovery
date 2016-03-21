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

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.discovery.types.HttpLocation}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.discovery.types.HttpLocation} original class using Vert.x codegen.
 */
public class HttpLocationConverter {

  public static void fromJson(JsonObject json, HttpLocation obj) {
    if (json.getValue("endpoint") instanceof String) {
      obj.setEndpoint((String)json.getValue("endpoint"));
    }
    if (json.getValue("host") instanceof String) {
      obj.setHost((String)json.getValue("host"));
    }
    if (json.getValue("port") instanceof Number) {
      obj.setPort(((Number)json.getValue("port")).intValue());
    }
    if (json.getValue("root") instanceof String) {
      obj.setRoot((String)json.getValue("root"));
    }
    if (json.getValue("ssl") instanceof Boolean) {
      obj.setSsl((Boolean)json.getValue("ssl"));
    }
  }

  public static void toJson(HttpLocation obj, JsonObject json) {
    if (obj.getEndpoint() != null) {
      json.put("endpoint", obj.getEndpoint());
    }
    if (obj.getHost() != null) {
      json.put("host", obj.getHost());
    }
    json.put("port", obj.getPort());
    if (obj.getRoot() != null) {
      json.put("root", obj.getRoot());
    }
    json.put("ssl", obj.isSsl());
  }
}