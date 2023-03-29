package io.vertx.servicediscovery.zookeeper;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.apache.curator.x.discovery.ServiceType;
import org.apache.curator.x.discovery.UriSpec;
import org.apache.curator.x.discovery.details.InstanceSerializer;

import java.io.ByteArrayOutputStream;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class JsonObjectSerializer implements InstanceSerializer<JsonObject> {

  @Override
  public byte[] serialize(ServiceInstance<JsonObject> instance) throws Exception {
    JsonObject json = new JsonObject()
      .put("name", instance.getName())
        .put("id", instance.getId())
        .put("address", instance.getAddress())
        .put("port", instance.getPort())
        .put("sslPort", instance.getSslPort())
        .put("payload", instance.getPayload())
        .put("registrationTimeUTC", instance.getRegistrationTimeUTC())
        .put("serviceType", instance.getServiceType().name())
        .put("uriSpec", instance.getUriSpec().build()) // ?
        .put("enabled", instance.isEnabled());

    return json.toBuffer().getBytes();
  }

  public ServiceInstance<JsonObject> deserialize(byte[] bytes) throws Exception {
    JsonObject json = new JsonObject (Buffer.buffer(bytes));
    ServiceInstanceBuilder<JsonObject> builder = ServiceInstance.<JsonObject>builder()
        .address(json.getString("address"))
        .id(json.getString("id"))
        .name(json.getString("name"))
        .payload(json.getJsonObject("payload"))
        .registrationTimeUTC(json.getLong("registrationTimeUTC"))
        .serviceType(ServiceType.valueOf(json.getString("serviceType")));

    if (json.containsKey("sslPort")) {
      builder.sslPort(json.getInteger("sslPort"));
    }
    if (json.containsKey("port")) {
      builder.port(json.getInteger("port"));
    }
    if (json.containsKey("uriSpec")) {
      builder.uriSpec(new UriSpec(json.getString("uriSpec")));
    }
    return builder.build();
  }
}

