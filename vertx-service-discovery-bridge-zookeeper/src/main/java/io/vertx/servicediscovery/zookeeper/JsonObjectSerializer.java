package io.vertx.servicediscovery.zookeeper;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.jackson.DatabindCodec;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.apache.curator.x.discovery.details.InstanceSerializer;

import java.io.ByteArrayOutputStream;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class JsonObjectSerializer implements InstanceSerializer<JsonObject> {

  private final ObjectMapper mapper = DatabindCodec.mapper();
  private final JavaType type;

  public JsonObjectSerializer() {
    this.type = this.mapper.getTypeFactory().constructType(ServiceInstance.class);
  }

  @Override
  public byte[] serialize(ServiceInstance<JsonObject> instance) throws Exception {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    this.mapper.writeValue(out, instance);
    return out.toByteArray();
  }

  public ServiceInstance<JsonObject> deserialize(byte[] bytes) throws Exception {
    ServiceInstance rawServiceInstance = this.mapper.readValue(bytes, this.type);
    ServiceInstanceBuilder<JsonObject> builder = ServiceInstance.<JsonObject>builder()
        .address(rawServiceInstance.getAddress())
        .id(rawServiceInstance.getId())
        .name(rawServiceInstance.getName())
        .payload(new JsonObject(rawServiceInstance.getPayload().toString()))
        .registrationTimeUTC(rawServiceInstance.getRegistrationTimeUTC())
        .serviceType(rawServiceInstance.getServiceType());

    if (rawServiceInstance.getSslPort() != null) {
      builder.sslPort(rawServiceInstance.getSslPort());
    }
    if (rawServiceInstance.getPort() != null) {
      builder.sslPort(rawServiceInstance.getPort());
    }
    if (rawServiceInstance.getUriSpec() != null) {
      builder.uriSpec(rawServiceInstance.getUriSpec());
    }
    return builder.build();
  }
}

