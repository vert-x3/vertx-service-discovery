package io.vertx.servicediscovery;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter and mapper for {@link io.vertx.servicediscovery.ServiceDiscoveryOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.servicediscovery.ServiceDiscoveryOptions} original class using Vert.x codegen.
 */
public class ServiceDiscoveryOptionsConverter {

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, ServiceDiscoveryOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "announceAddress":
          if (member.getValue() instanceof String) {
            obj.setAnnounceAddress((String)member.getValue());
          }
          break;
        case "backendConfiguration":
          if (member.getValue() instanceof JsonObject) {
            obj.setBackendConfiguration(((JsonObject)member.getValue()).copy());
          }
          break;
        case "name":
          if (member.getValue() instanceof String) {
            obj.setName((String)member.getValue());
          }
          break;
        case "usageAddress":
          if (member.getValue() instanceof String) {
            obj.setUsageAddress((String)member.getValue());
          }
          break;
      }
    }
  }

   static void toJson(ServiceDiscoveryOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(ServiceDiscoveryOptions obj, java.util.Map<String, Object> json) {
    if (obj.getAnnounceAddress() != null) {
      json.put("announceAddress", obj.getAnnounceAddress());
    }
    if (obj.getBackendConfiguration() != null) {
      json.put("backendConfiguration", obj.getBackendConfiguration());
    }
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
    if (obj.getUsageAddress() != null) {
      json.put("usageAddress", obj.getUsageAddress());
    }
  }
}
