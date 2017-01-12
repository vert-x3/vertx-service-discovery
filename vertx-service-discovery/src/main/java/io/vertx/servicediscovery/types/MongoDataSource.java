package io.vertx.servicediscovery.types;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.spi.ServiceType;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface MongoDataSource extends ServiceType {

  String TYPE = "mongo";

  /**
   * Convenient method to create a record for a Mongo data source.
   *
   * @param name     the service name
   * @param location the location of the service (e.g. url, port...)
   * @param metadata additional metadata
   * @return the created record
   */
  static Record createRecord(String name, JsonObject location, JsonObject metadata) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(location);

    Record record = new Record().setName(name)
      .setType(TYPE)
      .setLocation(location);

    if (metadata != null) {
      record.setMetadata(metadata);
    }

    return record;
  }

  /**
   * Convenient method that looks for a Mongo datasource source and provides the configured {@link io.vertx.ext.mongo.MongoClient}. The
   * async result is marked as failed is there are no matching services, or if the lookup fails.
   *
   * @param discovery     The service discovery instance
   * @param filter        The filter, optional
   * @param resultHandler The result handler
   */
  static void getMongoClient(ServiceDiscovery discovery, JsonObject filter,
                             Handler<AsyncResult<MongoClient>> resultHandler) {
    discovery.getRecord(filter, ar -> {
      if (ar.failed() || ar.result() == null) {
        resultHandler.handle(Future.failedFuture("No matching record"));
      } else {
        resultHandler.handle(Future.succeededFuture(discovery.getReference(ar.result()).get()));
      }
    });
  }

  /**
   * Convenient method that looks for a Mongo datasource source and provides the configured
   * {@link io.vertx.ext.mongo.MongoClient}. The
   * async result is marked as failed is there are no matching services, or if the lookup fails.
   *
   * @param discovery     The service discovery instance
   * @param filter        The filter
   * @param resultHandler The result handler
   */
  static void getMongoClient(ServiceDiscovery discovery, Function<Record, Boolean> filter,
                             Handler<AsyncResult<MongoClient>> resultHandler) {
    discovery.getRecord(filter, ar -> {
      if (ar.failed() || ar.result() == null) {
        resultHandler.handle(Future.failedFuture("No matching record"));
      } else {
        resultHandler.handle(Future.succeededFuture(discovery.getReference(ar.result()).get()));
      }
    });
  }


  /**
   * Convenient method that looks for a Mongo datasource source and provides the configured {@link io.vertx.ext.mongo.MongoClient}. The
   * async result is marked as failed is there are no matching services, or if the lookup fails.
   *
   * @param discovery             The service discovery instance
   * @param filter                The filter, optional
   * @param consumerConfiguration the consumer configuration
   * @param resultHandler         the result handler
   */
  static void getMongoClient(ServiceDiscovery discovery, JsonObject filter, JsonObject consumerConfiguration,
                             Handler<AsyncResult<MongoClient>> resultHandler) {
    discovery.getRecord(filter, ar -> {
      if (ar.failed() || ar.result() == null) {
        resultHandler.handle(Future.failedFuture("No matching record"));
      } else {
        resultHandler.handle(Future.succeededFuture(
          discovery.getReferenceWithConfiguration(ar.result(), consumerConfiguration).get()));
      }
    });
  }
}
