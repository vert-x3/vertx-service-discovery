package examples;


import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;

public class ServiceDiscoveryBackendZookeeperExamples {

  public void configuration1(Vertx vertx) {
    ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions()
        .setBackendConfiguration(
            new JsonObject()
                .put("connection", "127.0.0.1:2181")
        ));
  }

  public void configuration2(Vertx vertx) {
    ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions()
        .setBackendConfiguration(
            new JsonObject()
                .put("connection", "127.0.0.1:2181")
                .put("ephemeral", true)
                .put("guaranteed", true)
                .put("basePath", "/services/my-backend")
        ));
  }
}
