package io.vertx.ext.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ProxyHelper;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class HelloServiceImpl implements HelloService {

  private final String msg;
  private MessageConsumer<JsonObject> service;

  public HelloServiceImpl() {
    this("Hello");
  }

  public HelloServiceImpl(String msg) {
    this.msg = msg;
  }

  public void start(Vertx vertx, String address) {
    service = ProxyHelper.registerService(HelloService.class, vertx, this, address);
  }

  public void stop() {
    ProxyHelper.unregisterService(service);
  }

  @Override
  public void hello(String name, Handler<AsyncResult<String>> resultHandler) {
    resultHandler.handle(Future.succeededFuture(msg + " " + name));
  }
}
