/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.ext.circuitbreaker;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.circuitbreaker.CircuitBreakerOptions}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.circuitbreaker.CircuitBreakerOptions} original class using Vert.x codegen.
 */
public class CircuitBreakerOptionsConverter {

  public static void fromJson(JsonObject json, CircuitBreakerOptions obj) {
    if (json.getValue("fallbackOnFailure") instanceof Boolean) {
      obj.setFallbackOnFailure((Boolean)json.getValue("fallbackOnFailure"));
    }
    if (json.getValue("maxFailures") instanceof Number) {
      obj.setMaxFailures(((Number)json.getValue("maxFailures")).intValue());
    }
    if (json.getValue("notificationAddress") instanceof String) {
      obj.setNotificationAddress((String)json.getValue("notificationAddress"));
    }
    if (json.getValue("notificationPeriod") instanceof Number) {
      obj.setNotificationPeriod(((Number)json.getValue("notificationPeriod")).longValue());
    }
    if (json.getValue("resetTimeout") instanceof Number) {
      obj.setResetTimeout(((Number)json.getValue("resetTimeout")).longValue());
    }
    if (json.getValue("timeout") instanceof Number) {
      obj.setTimeout(((Number)json.getValue("timeout")).longValue());
    }
  }

  public static void toJson(CircuitBreakerOptions obj, JsonObject json) {
    json.put("fallbackOnFailure", obj.isFallbackOnFailure());
    json.put("maxFailures", obj.getMaxFailures());
    if (obj.getNotificationAddress() != null) {
      json.put("notificationAddress", obj.getNotificationAddress());
    }
    json.put("notificationPeriod", obj.getNotificationPeriod());
    json.put("resetTimeout", obj.getResetTimeout());
    json.put("timeout", obj.getTimeout());
  }
}