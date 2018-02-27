package examples;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;

/**
 * @author <a href="mailto:cafeinoman@openaliasbox.org>Francois Delalleau</a>
 */
public class ServiceDiscoveryBackendConsulExamples {

  public void configuration1(Vertx vertx) {
    ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions()
      .setBackendConfiguration(
        new JsonObject()
          .put("defaultHost", "127.0.0.1")
          .put("dc", "my-dc")
      ));
  }

}
