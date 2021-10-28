package io.vertx.servicediscovery;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link io.vertx.servicediscovery.Record}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.servicediscovery.Record} original class using Vert.x codegen.
 */
public class RecordConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Record obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "location":
          if (member.getValue() instanceof JsonObject) {
            obj.setLocation(((JsonObject)member.getValue()).copy());
          }
          break;
        case "metadata":
          if (member.getValue() instanceof JsonObject) {
            obj.setMetadata(((JsonObject)member.getValue()).copy());
          }
          break;
        case "name":
          if (member.getValue() instanceof String) {
            obj.setName((String)member.getValue());
          }
          break;
        case "registration":
          if (member.getValue() instanceof String) {
            obj.setRegistration((String)member.getValue());
          }
          break;
        case "status":
          if (member.getValue() instanceof String) {
            obj.setStatus(io.vertx.servicediscovery.Status.valueOf((String)member.getValue()));
          }
          break;
        case "type":
          if (member.getValue() instanceof String) {
            obj.setType((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(Record obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(Record obj, java.util.Map<String, Object> json) {
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
