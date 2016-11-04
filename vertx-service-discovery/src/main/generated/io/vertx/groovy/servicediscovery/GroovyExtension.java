package io.vertx.groovy.servicediscovery;
public class GroovyExtension {
  public static io.vertx.servicediscovery.ServiceReference getReference(io.vertx.servicediscovery.ServiceDiscovery j_receiver, java.util.Map<String, Object> record) {
    return io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.getReference(record != null ? new io.vertx.servicediscovery.Record(io.vertx.lang.groovy.ConversionHelper.toJsonObject(record)) : null));
  }
  public static io.vertx.servicediscovery.ServiceReference getReferenceWithConfiguration(io.vertx.servicediscovery.ServiceDiscovery j_receiver, java.util.Map<String, Object> record, java.util.Map<String, Object> configuration) {
    return io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.getReferenceWithConfiguration(record != null ? new io.vertx.servicediscovery.Record(io.vertx.lang.groovy.ConversionHelper.toJsonObject(record)) : null,
      configuration != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(configuration) : null));
  }
  public static io.vertx.servicediscovery.ServiceDiscovery registerServiceImporter(io.vertx.servicediscovery.ServiceDiscovery j_receiver, io.vertx.servicediscovery.spi.ServiceImporter importer, java.util.Map<String, Object> configuration) {
    return io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.registerServiceImporter(importer,
      configuration != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(configuration) : null));
  }
  public static io.vertx.servicediscovery.ServiceDiscovery registerServiceImporter(io.vertx.servicediscovery.ServiceDiscovery j_receiver, io.vertx.servicediscovery.spi.ServiceImporter importer, java.util.Map<String, Object> configuration, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Void>> completionHandler) {
    return io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.registerServiceImporter(importer,
      configuration != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(configuration) : null,
      completionHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Void>>() {
      public void handle(io.vertx.core.AsyncResult<java.lang.Void> ar) {
        completionHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.wrap(event)));
      }
    } : null));
  }
  public static io.vertx.servicediscovery.ServiceDiscovery registerServiceExporter(io.vertx.servicediscovery.ServiceDiscovery j_receiver, io.vertx.servicediscovery.spi.ServiceExporter exporter, java.util.Map<String, Object> configuration) {
    return io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.registerServiceExporter(exporter,
      configuration != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(configuration) : null));
  }
  public static io.vertx.servicediscovery.ServiceDiscovery registerServiceExporter(io.vertx.servicediscovery.ServiceDiscovery j_receiver, io.vertx.servicediscovery.spi.ServiceExporter exporter, java.util.Map<String, Object> configuration, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Void>> completionHandler) {
    return io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.registerServiceExporter(exporter,
      configuration != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(configuration) : null,
      completionHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Void>>() {
      public void handle(io.vertx.core.AsyncResult<java.lang.Void> ar) {
        completionHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.wrap(event)));
      }
    } : null));
  }
  public static void publish(io.vertx.servicediscovery.ServiceDiscovery j_receiver, java.util.Map<String, Object> record, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.Map<String, Object>>> resultHandler) {
    j_receiver.publish(record != null ? new io.vertx.servicediscovery.Record(io.vertx.lang.groovy.ConversionHelper.toJsonObject(record)) : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.servicediscovery.Record>>() {
      public void handle(io.vertx.core.AsyncResult<io.vertx.servicediscovery.Record> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))));
      }
    } : null);
  }
  public static void getRecord(io.vertx.servicediscovery.ServiceDiscovery j_receiver, java.util.Map<String, Object> filter, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.Map<String, Object>>> resultHandler) {
    j_receiver.getRecord(filter != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(filter) : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.servicediscovery.Record>>() {
      public void handle(io.vertx.core.AsyncResult<io.vertx.servicediscovery.Record> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))));
      }
    } : null);
  }
  public static void getRecord(io.vertx.servicediscovery.ServiceDiscovery j_receiver, java.util.function.Function<java.util.Map<String, Object>, java.lang.Boolean> filter, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.Map<String, Object>>> resultHandler) {
    j_receiver.getRecord(filter != null ? new java.util.function.Function<io.vertx.servicediscovery.Record, java.lang.Boolean>() {
      public java.lang.Boolean apply(io.vertx.servicediscovery.Record t) {
        java.util.Map<String, Object> o = io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(t, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
        java.lang.Boolean p = filter.apply(o);
        return p;
      }
    } : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.servicediscovery.Record>>() {
      public void handle(io.vertx.core.AsyncResult<io.vertx.servicediscovery.Record> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))));
      }
    } : null);
  }
  public static void getRecord(io.vertx.servicediscovery.ServiceDiscovery j_receiver, java.util.function.Function<java.util.Map<String, Object>, java.lang.Boolean> filter, boolean includeOutOfService, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.Map<String, Object>>> resultHandler) {
    j_receiver.getRecord(filter != null ? new java.util.function.Function<io.vertx.servicediscovery.Record, java.lang.Boolean>() {
      public java.lang.Boolean apply(io.vertx.servicediscovery.Record t) {
        java.util.Map<String, Object> o = io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(t, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
        java.lang.Boolean p = filter.apply(o);
        return p;
      }
    } : null,
      includeOutOfService,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.servicediscovery.Record>>() {
      public void handle(io.vertx.core.AsyncResult<io.vertx.servicediscovery.Record> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))));
      }
    } : null);
  }
  public static void getRecords(io.vertx.servicediscovery.ServiceDiscovery j_receiver, java.util.Map<String, Object> filter, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<java.util.Map<String, Object>>>> resultHandler) {
    j_receiver.getRecords(filter != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(filter) : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<io.vertx.servicediscovery.Record>>>() {
      public void handle(io.vertx.core.AsyncResult<java.util.List<io.vertx.servicediscovery.Record>> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, list -> list.stream().map(elt -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(elt, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))).collect(java.util.stream.Collectors.toList()))));
      }
    } : null);
  }
  public static void getRecords(io.vertx.servicediscovery.ServiceDiscovery j_receiver, java.util.function.Function<java.util.Map<String, Object>, java.lang.Boolean> filter, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<java.util.Map<String, Object>>>> resultHandler) {
    j_receiver.getRecords(filter != null ? new java.util.function.Function<io.vertx.servicediscovery.Record, java.lang.Boolean>() {
      public java.lang.Boolean apply(io.vertx.servicediscovery.Record t) {
        java.util.Map<String, Object> o = io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(t, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
        java.lang.Boolean p = filter.apply(o);
        return p;
      }
    } : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<io.vertx.servicediscovery.Record>>>() {
      public void handle(io.vertx.core.AsyncResult<java.util.List<io.vertx.servicediscovery.Record>> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, list -> list.stream().map(elt -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(elt, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))).collect(java.util.stream.Collectors.toList()))));
      }
    } : null);
  }
  public static void getRecords(io.vertx.servicediscovery.ServiceDiscovery j_receiver, java.util.function.Function<java.util.Map<String, Object>, java.lang.Boolean> filter, boolean includeOutOfService, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<java.util.Map<String, Object>>>> resultHandler) {
    j_receiver.getRecords(filter != null ? new java.util.function.Function<io.vertx.servicediscovery.Record, java.lang.Boolean>() {
      public java.lang.Boolean apply(io.vertx.servicediscovery.Record t) {
        java.util.Map<String, Object> o = io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(t, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
        java.lang.Boolean p = filter.apply(o);
        return p;
      }
    } : null,
      includeOutOfService,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.List<io.vertx.servicediscovery.Record>>>() {
      public void handle(io.vertx.core.AsyncResult<java.util.List<io.vertx.servicediscovery.Record>> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, list -> list.stream().map(elt -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(elt, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))).collect(java.util.stream.Collectors.toList()))));
      }
    } : null);
  }
  public static void update(io.vertx.servicediscovery.ServiceDiscovery j_receiver, java.util.Map<String, Object> record, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.Map<String, Object>>> resultHandler) {
    j_receiver.update(record != null ? new io.vertx.servicediscovery.Record(io.vertx.lang.groovy.ConversionHelper.toJsonObject(record)) : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.servicediscovery.Record>>() {
      public void handle(io.vertx.core.AsyncResult<io.vertx.servicediscovery.Record> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))));
      }
    } : null);
  }
  public static java.util.Map<String, Object> options(io.vertx.servicediscovery.ServiceDiscovery j_receiver) {
    return io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(j_receiver.options(), a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
  }
  public static void init(io.vertx.servicediscovery.spi.ServiceExporter j_receiver, io.vertx.core.Vertx vertx, io.vertx.servicediscovery.spi.ServicePublisher publisher, java.util.Map<String, Object> configuration, io.vertx.core.Future<java.lang.Void> future) {
    j_receiver.init(vertx,
      publisher,
      configuration != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(configuration) : null,
      future);
  }
  public static void onPublish(io.vertx.servicediscovery.spi.ServiceExporter j_receiver, java.util.Map<String, Object> record) {
    j_receiver.onPublish(record != null ? new io.vertx.servicediscovery.Record(io.vertx.lang.groovy.ConversionHelper.toJsonObject(record)) : null);
  }
  public static void onUpdate(io.vertx.servicediscovery.spi.ServiceExporter j_receiver, java.util.Map<String, Object> record) {
    j_receiver.onUpdate(record != null ? new io.vertx.servicediscovery.Record(io.vertx.lang.groovy.ConversionHelper.toJsonObject(record)) : null);
  }
  public static void start(io.vertx.servicediscovery.spi.ServiceImporter j_receiver, io.vertx.core.Vertx vertx, io.vertx.servicediscovery.spi.ServicePublisher publisher, java.util.Map<String, Object> configuration, io.vertx.core.Future<java.lang.Void> future) {
    j_receiver.start(vertx,
      publisher,
      configuration != null ? io.vertx.lang.groovy.ConversionHelper.toJsonObject(configuration) : null,
      future);
  }
  public static void publish(io.vertx.servicediscovery.spi.ServicePublisher j_receiver, java.util.Map<String, Object> record, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.Map<String, Object>>> resultHandler) {
    j_receiver.publish(record != null ? new io.vertx.servicediscovery.Record(io.vertx.lang.groovy.ConversionHelper.toJsonObject(record)) : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.servicediscovery.Record>>() {
      public void handle(io.vertx.core.AsyncResult<io.vertx.servicediscovery.Record> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(event, a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()))));
      }
    } : null);
  }
  public static java.util.Map<String, Object> record(io.vertx.servicediscovery.ServiceReference j_receiver) {
    return io.vertx.lang.groovy.ConversionHelper.applyIfNotNull(j_receiver.record(), a -> io.vertx.lang.groovy.ConversionHelper.fromJsonObject(a.toJson()));
  }
  public static <T>java.lang.Object get(io.vertx.servicediscovery.ServiceReference j_receiver) {
    return io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.get());
  }
  public static <T>java.lang.Object cached(io.vertx.servicediscovery.ServiceReference j_receiver) {
    return io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.cached());
  }
}
