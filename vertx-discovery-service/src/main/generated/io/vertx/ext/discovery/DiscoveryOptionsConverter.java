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
 * Converter for {@link io.vertx.ext.discovery.DiscoveryOptions}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.discovery.DiscoveryOptions} original class using Vert.x codegen.
 */
public class DiscoveryOptionsConverter {

  public static void fromJson(JsonObject json, DiscoveryOptions obj) {
    if (json.getValue("announceAddress") instanceof String) {
      obj.setAnnounceAddress((String)json.getValue("announceAddress"));
    }
  }

  public static void toJson(DiscoveryOptions obj, JsonObject json) {
    if (obj.getAnnounceAddress() != null) {
      json.put("announceAddress", obj.getAnnounceAddress());
    }
  }
}