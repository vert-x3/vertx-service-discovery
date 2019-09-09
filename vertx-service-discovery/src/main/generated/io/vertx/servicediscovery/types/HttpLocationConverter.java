package io.vertx.servicediscovery.types;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import io.vertx.core.spi.json.JsonMapper;

/**
 * Converter and mapper for {@link io.vertx.servicediscovery.types.HttpLocation}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.servicediscovery.types.HttpLocation} original class using Vert.x codegen.
 */
public class HttpLocationConverter implements JsonMapper<HttpLocation, JsonObject> {

  public static final HttpLocationConverter INSTANCE = new HttpLocationConverter();

  @Override public JsonObject serialize(HttpLocation value) { return (value != null) ? value.toJson() : null; }

  @Override public HttpLocation deserialize(JsonObject value) { return (value != null) ? new HttpLocation(value) : null; }

  @Override public Class<HttpLocation> getTargetClass() { return HttpLocation.class; }

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, HttpLocation obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "endpoint":
          if (member.getValue() instanceof String) {
            obj.setEndpoint((String)member.getValue());
          }
          break;
        case "host":
          if (member.getValue() instanceof String) {
            obj.setHost((String)member.getValue());
          }
          break;
        case "port":
          if (member.getValue() instanceof Number) {
            obj.setPort(((Number)member.getValue()).intValue());
          }
          break;
        case "root":
          if (member.getValue() instanceof String) {
            obj.setRoot((String)member.getValue());
          }
          break;
        case "ssl":
          if (member.getValue() instanceof Boolean) {
            obj.setSsl((Boolean)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(HttpLocation obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(HttpLocation obj, java.util.Map<String, Object> json) {
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
