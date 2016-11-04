package io.vertx.groovy.servicediscovery.service;
public class GroovyExtension {
  public static void hello(io.vertx.servicediscovery.service.HelloService j_receiver, java.util.Map<String, Object> name, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.String>> resultHandler) {
    j_receiver.hello(name != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(name) : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.String>>() {
      public void handle(io.vertx.core.AsyncResult<java.lang.String> ar) {
        resultHandler.handle(ar.map(event -> event));
      }
    } : null);
  }
}
