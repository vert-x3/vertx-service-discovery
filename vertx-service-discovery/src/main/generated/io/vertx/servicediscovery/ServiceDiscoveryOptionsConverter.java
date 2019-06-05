package io.vertx.servicediscovery;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import io.vertx.core.spi.json.JsonCodec;

/**
 * Converter and Codec for {@link io.vertx.servicediscovery.ServiceDiscoveryOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.servicediscovery.ServiceDiscoveryOptions} original class using Vert.x codegen.
 */
public class ServiceDiscoveryOptionsConverter implements JsonCodec<ServiceDiscoveryOptions, JsonObject> {

  public static final ServiceDiscoveryOptionsConverter INSTANCE = new ServiceDiscoveryOptionsConverter();

  @Override public JsonObject encode(ServiceDiscoveryOptions value) { return (value != null) ? value.toJson() : null; }

  @Override public ServiceDiscoveryOptions decode(JsonObject value) { return (value != null) ? new ServiceDiscoveryOptions(value) : null; }

  @Override public Class<ServiceDiscoveryOptions> getTargetClass() { return ServiceDiscoveryOptions.class; }

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, ServiceDiscoveryOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "announceAddress":
          if (member.getValue() instanceof String) {
            obj.setAnnounceAddress((String)member.getValue());
          }
          break;
        case "autoRegistrationOfImporters":
          if (member.getValue() instanceof Boolean) {
            obj.setAutoRegistrationOfImporters((Boolean)member.getValue());
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

  public static void toJson(ServiceDiscoveryOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(ServiceDiscoveryOptions obj, java.util.Map<String, Object> json) {
    if (obj.getAnnounceAddress() != null) {
      json.put("announceAddress", obj.getAnnounceAddress());
    }
    json.put("autoRegistrationOfImporters", obj.isAutoRegistrationOfImporters());
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
