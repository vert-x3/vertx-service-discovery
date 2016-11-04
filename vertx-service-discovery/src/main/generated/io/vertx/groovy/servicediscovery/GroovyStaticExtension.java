package io.vertx.groovy.servicediscovery;
public class GroovyStaticExtension {
  public static java.util.Map<String, Object> createRecord(io.vertx.servicediscovery.types.EventBusService j_receiver, java.lang.String name, java.lang.String address, java.lang.String itf, java.util.Map<String, Object> metadata) {
    return io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(io.vertx.servicediscovery.types.EventBusService.createRecord(name,
      address,
      itf,
      metadata != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(metadata) : null), a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
  }
  public static <T>void getProxy(io.vertx.servicediscovery.types.EventBusService j_receiver, io.vertx.servicediscovery.ServiceDiscovery discovery, java.util.Map<String, Object> filter, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Object>> resultHandler) {
    io.vertx.servicediscovery.types.EventBusService.getProxy(discovery,
      filter != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(filter) : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Object>>() {
      public void handle(io.vertx.core.AsyncResult<java.lang.Object> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.wrap(event)));
      }
    } : null);
  }
  public static <T>void getProxy(io.vertx.servicediscovery.types.EventBusService j_receiver, io.vertx.servicediscovery.ServiceDiscovery discovery, java.lang.String serviceInterface, java.lang.String proxyInterface, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Object>> resultHandler) {
    io.vertx.servicediscovery.types.EventBusService.getProxy(discovery,
      serviceInterface,
      proxyInterface,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Object>>() {
      public void handle(io.vertx.core.AsyncResult<java.lang.Object> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.wrap(event)));
      }
    } : null);
  }
  public static <T>void getProxy(io.vertx.servicediscovery.types.EventBusService j_receiver, io.vertx.servicediscovery.ServiceDiscovery discovery, java.util.Map<String, Object> filter, java.lang.String proxyClass, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Object>> resultHandler) {
    io.vertx.servicediscovery.types.EventBusService.getProxy(discovery,
      filter != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(filter) : null,
      proxyClass,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Object>>() {
      public void handle(io.vertx.core.AsyncResult<java.lang.Object> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.wrap(event)));
      }
    } : null);
  }
  public static <T>void getProxy(io.vertx.servicediscovery.types.EventBusService j_receiver, io.vertx.servicediscovery.ServiceDiscovery discovery, java.lang.String itf, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Object>> resultHandler) {
    io.vertx.servicediscovery.types.EventBusService.getProxy(discovery,
      itf,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Object>>() {
      public void handle(io.vertx.core.AsyncResult<java.lang.Object> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.wrap(event)));
      }
    } : null);
  }
  public static java.util.Map<String, Object> createRecord(io.vertx.servicediscovery.types.HttpEndpoint j_receiver, java.lang.String name, java.lang.String host, int port, java.lang.String root, java.util.Map<String, Object> metadata) {
    return io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(io.vertx.servicediscovery.types.HttpEndpoint.createRecord(name,
      host,
      port,
      root,
      metadata != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(metadata) : null), a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
  }
  public static java.util.Map<String, Object> createRecord(io.vertx.servicediscovery.types.HttpEndpoint j_receiver, java.lang.String name, boolean ssl, java.lang.String host, int port, java.lang.String root, java.util.Map<String, Object> metadata) {
    return io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(io.vertx.servicediscovery.types.HttpEndpoint.createRecord(name,
      ssl,
      host,
      port,
      root,
      metadata != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(metadata) : null), a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
  }
  public static java.util.Map<String, Object> createRecord(io.vertx.servicediscovery.types.HttpEndpoint j_receiver, java.lang.String name, java.lang.String host, int port, java.lang.String root) {
    return io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(io.vertx.servicediscovery.types.HttpEndpoint.createRecord(name,
      host,
      port,
      root), a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
  }
  public static java.util.Map<String, Object> createRecord(io.vertx.servicediscovery.types.HttpEndpoint j_receiver, java.lang.String name, java.lang.String host) {
    return io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(io.vertx.servicediscovery.types.HttpEndpoint.createRecord(name,
      host), a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
  }
  public static void getClient(io.vertx.servicediscovery.types.HttpEndpoint j_receiver, io.vertx.servicediscovery.ServiceDiscovery discovery, java.util.Map<String, Object> filter, io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.core.http.HttpClient>> resultHandler) {
    io.vertx.servicediscovery.types.HttpEndpoint.getClient(discovery,
      filter != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(filter) : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.core.http.HttpClient>>() {
      public void handle(io.vertx.core.AsyncResult<io.vertx.core.http.HttpClient> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.wrap(event)));
      }
    } : null);
  }
  public static void getClient(io.vertx.servicediscovery.types.HttpEndpoint j_receiver, io.vertx.servicediscovery.ServiceDiscovery discovery, java.util.function.Function<java.util.Map<String, Object>, java.lang.Boolean> filter, io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.core.http.HttpClient>> resultHandler) {
    io.vertx.servicediscovery.types.HttpEndpoint.getClient(discovery,
      filter != null ? new java.util.function.Function<io.vertx.servicediscovery.Record, java.lang.Boolean>() {
      public java.lang.Boolean apply(io.vertx.servicediscovery.Record t) {
        java.util.Map<String, Object> o = io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(t, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
        java.lang.Boolean p = filter.apply(o);
        return p;
      }
    } : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.core.http.HttpClient>>() {
      public void handle(io.vertx.core.AsyncResult<io.vertx.core.http.HttpClient> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.wrap(event)));
      }
    } : null);
  }
  public static java.util.Map<String, Object> createRecord(io.vertx.servicediscovery.types.JDBCDataSource j_receiver, java.lang.String name, java.util.Map<String, Object> location, java.util.Map<String, Object> metadata) {
    return io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(io.vertx.servicediscovery.types.JDBCDataSource.createRecord(name,
      location != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(location) : null,
      metadata != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(metadata) : null), a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
  }
  public static void getJDBCClient(io.vertx.servicediscovery.types.JDBCDataSource j_receiver, io.vertx.servicediscovery.ServiceDiscovery discovery, java.util.Map<String, Object> filter, io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.ext.jdbc.JDBCClient>> resultHandler) {
    io.vertx.servicediscovery.types.JDBCDataSource.getJDBCClient(discovery,
      filter != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(filter) : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.ext.jdbc.JDBCClient>>() {
      public void handle(io.vertx.core.AsyncResult<io.vertx.ext.jdbc.JDBCClient> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.wrap(event)));
      }
    } : null);
  }
  public static void getJDBCClient(io.vertx.servicediscovery.types.JDBCDataSource j_receiver, io.vertx.servicediscovery.ServiceDiscovery discovery, java.util.Map<String, Object> filter, java.util.Map<String, Object> consumerConfiguration, io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.ext.jdbc.JDBCClient>> resultHandler) {
    io.vertx.servicediscovery.types.JDBCDataSource.getJDBCClient(discovery,
      filter != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(filter) : null,
      consumerConfiguration != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(consumerConfiguration) : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.ext.jdbc.JDBCClient>>() {
      public void handle(io.vertx.core.AsyncResult<io.vertx.ext.jdbc.JDBCClient> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.wrap(event)));
      }
    } : null);
  }
  public static java.util.Map<String, Object> createRecord(io.vertx.servicediscovery.types.MessageSource j_receiver, java.lang.String name, java.lang.String address, java.lang.String type, java.util.Map<String, Object> metadata) {
    return io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(io.vertx.servicediscovery.types.MessageSource.createRecord(name,
      address,
      type,
      metadata != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(metadata) : null), a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
  }
  public static java.util.Map<String, Object> createRecord(io.vertx.servicediscovery.types.MessageSource j_receiver, java.lang.String name, java.lang.String address, java.lang.String type) {
    return io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(io.vertx.servicediscovery.types.MessageSource.createRecord(name,
      address,
      type), a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
  }
  public static java.util.Map<String, Object> createRecord(io.vertx.servicediscovery.types.MessageSource j_receiver, java.lang.String name, java.lang.String address) {
    return io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(io.vertx.servicediscovery.types.MessageSource.createRecord(name,
      address), a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
  }
  public static <T>void getConsumer(io.vertx.servicediscovery.types.MessageSource j_receiver, io.vertx.servicediscovery.ServiceDiscovery discovery, java.util.Map<String, Object> filter, io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.core.eventbus.MessageConsumer<java.lang.Object>>> resultHandler) {
    io.vertx.servicediscovery.types.MessageSource.getConsumer(discovery,
      filter != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(filter) : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.core.eventbus.MessageConsumer<java.lang.Object>>>() {
      public void handle(io.vertx.core.AsyncResult<io.vertx.core.eventbus.MessageConsumer<java.lang.Object>> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.wrap(event)));
      }
    } : null);
  }
  public static java.util.Map<String, Object> createRecord(io.vertx.servicediscovery.types.RedisDataSource j_receiver, java.lang.String name, java.util.Map<String, Object> location, java.util.Map<String, Object> metadata) {
    return io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(io.vertx.servicediscovery.types.RedisDataSource.createRecord(name,
      location != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(location) : null,
      metadata != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(metadata) : null), a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
  }
  public static void getRedisClient(io.vertx.servicediscovery.types.RedisDataSource j_receiver, io.vertx.servicediscovery.ServiceDiscovery discovery, java.util.Map<String, Object> filter, io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.redis.RedisClient>> resultHandler) {
    io.vertx.servicediscovery.types.RedisDataSource.getRedisClient(discovery,
      filter != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(filter) : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.redis.RedisClient>>() {
      public void handle(io.vertx.core.AsyncResult<io.vertx.redis.RedisClient> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.wrap(event)));
      }
    } : null);
  }
  public static void getRedisClient(io.vertx.servicediscovery.types.RedisDataSource j_receiver, io.vertx.servicediscovery.ServiceDiscovery discovery, java.util.Map<String, Object> filter, java.util.Map<String, Object> consumerConfiguration, io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.redis.RedisClient>> resultHandler) {
    io.vertx.servicediscovery.types.RedisDataSource.getRedisClient(discovery,
      filter != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(filter) : null,
      consumerConfiguration != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(consumerConfiguration) : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.redis.RedisClient>>() {
      public void handle(io.vertx.core.AsyncResult<io.vertx.redis.RedisClient> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.wrap(event)));
      }
    } : null);
  }
  public static io.vertx.servicediscovery.ServiceDiscovery create(io.vertx.servicediscovery.ServiceDiscovery j_receiver, io.vertx.core.Vertx vertx, java.util.Map<String, Object> options) {
    return io.vertx.lang.groovy.ConversionHelper.wrap(io.vertx.servicediscovery.ServiceDiscovery.create(vertx,
      options != null ? new io.vertx.servicediscovery.ServiceDiscoveryOptions(io.vertx.lang.groovy.ConversionHelper.toJsonObject(options)) : null));
  }
  public static void releaseServiceObject(io.vertx.servicediscovery.ServiceDiscovery j_receiver, io.vertx.servicediscovery.ServiceDiscovery discovery, java.lang.Object svcObject) {
    io.vertx.servicediscovery.ServiceDiscovery.releaseServiceObject(discovery,
      io.vertx.lang.groovy.ConversionHelper.unwrap(svcObject));
  }
}
