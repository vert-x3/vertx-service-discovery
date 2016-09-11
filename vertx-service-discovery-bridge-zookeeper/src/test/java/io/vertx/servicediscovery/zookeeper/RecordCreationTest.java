package io.vertx.servicediscovery.zookeeper;

import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class RecordCreationTest {

  @Test
  public void testUnknownRecordCreation() throws Exception {
    UriSpec uriSpec = new UriSpec("{scheme}://foo.com:{ssl-port}");
    ServiceInstance<JsonObject> instance = ServiceInstance.<JsonObject>builder()
        .name("foo-service")
        .payload(new JsonObject().put("foo", "bar"))
        .sslPort(42406)
        .uriSpec(uriSpec)
        .build();

    Record record = ZookeeperServiceImporter.createRecordForInstance(instance);
    assertThat(record.getName()).isEqualTo("foo-service");
    assertThat(record.getType()).isEqualTo("unknown");
    assertThat(record.getMetadata().getString("foo")).isEqualTo("bar");
    assertThat(record.getMetadata().getString("zookeeper-id")).isNotEmpty();
    assertThat(record.getLocation())
        .contains(entry("endpoint", "https://foo.com:42406"))
        .contains(entry("ssl-port", 42406));
  }

  @Test
  public void testNoUriSpec() throws Exception {
    ServiceInstance<JsonObject> instance = ServiceInstance.<JsonObject>builder()
        .name("foo-service")
        .payload(new JsonObject().put("foo", "bar"))
        .sslPort(42406)
        .address("localhost")
        .build();

    Record record = ZookeeperServiceImporter.createRecordForInstance(instance);
    assertThat(record.getName()).isEqualTo("foo-service");
    assertThat(record.getType()).isEqualTo("unknown");
    assertThat(record.getMetadata().getString("foo")).isEqualTo("bar");
    assertThat(record.getMetadata().getString("zookeeper-id")).isNotEmpty();
    assertThat(record.getLocation())
        .contains(entry("endpoint", "https://localhost:42406"))
        .contains(entry("ssl-port", 42406));
  }

  @Test
  public void testHttpRecordCreation() throws Exception {
    UriSpec uriSpec = new UriSpec("{scheme}://example.com:{port}/foo");
    ServiceInstance<JsonObject> instance = ServiceInstance.<JsonObject>builder()
        .name("foo-service")
        .payload(new JsonObject().put("foo", "bar").put("service-type", "http-endpoint"))
        .port(8080)
        .uriSpec(uriSpec)
        .build();

    Record record = ZookeeperServiceImporter.createRecordForInstance(instance);
    assertThat(record.getName()).isEqualTo("foo-service");
    assertThat(record.getType()).isEqualTo("http-endpoint");
    assertThat(record.getMetadata().getString("foo")).isEqualTo("bar");
    assertThat(record.getMetadata().getString("zookeeper-id")).isNotEmpty();
    assertThat(record.getLocation())
        .contains(entry("endpoint", "http://example.com:8080/foo"))
        .contains(entry("port", 8080));
  }

}
