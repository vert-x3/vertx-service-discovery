package io.vertx.servicediscovery.types;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import io.vertx.rxjava.servicediscovery.service.HelloService;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class RXHelloServiceConsumer extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

    io.vertx.rxjava.servicediscovery.types.EventBusService.getServiceProxyWithJsonFilter(
      discovery,
      new JsonObject().put("service.interface", io.vertx.servicediscovery.service.HelloService.class.getName()),
      HelloService.class, // service interface
      ar -> {
        if (ar.failed()) {
          vertx.eventBus().send("result",
            new JsonObject().put("status", "ko").put("message", ar.cause().getMessage()));
        } else {
          HelloService hello = ar.result();
          hello.hello(new JsonObject().put("name", "vert.x"), result -> {
            if (result.failed()) {
              vertx.eventBus().send("result", new JsonObject()
                .put("status", "ko")
                .put("message", result.cause().getMessage()));
            } else {
              vertx.eventBus().send("result", new JsonObject()
                .put("status", "ok")
                .put("message", result.result()));

              ServiceDiscovery.releaseServiceObject(discovery, hello);
            }
          });
        }
      });
  }

}
