package io.vertx.servicediscovery.types;

import io.vertx.codegen.annotations.VertxGen;
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
   * @return a future notified with the client
   */
  static Future<MongoClient> getMongoClient(ServiceDiscovery discovery, JsonObject filter) {
    return discovery.getRecord(filter).flatMap(res -> {
      if (res == null) {
        return Future.failedFuture("No matching records");
      } else {
        return Future.succeededFuture(discovery.getReference(res).get());
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
   * @return a future notified with the client
   */
  static Future<MongoClient> getMongoClient(ServiceDiscovery discovery, Function<Record, Boolean> filter) {
    return discovery.getRecord(filter).flatMap(res -> {
      if (res == null) {
        return Future.failedFuture("No matching records");
      } else {
        return Future.succeededFuture(discovery.getReference(res).get());
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
   * @return a future notified with the client
   */
  static Future<MongoClient> getMongoClient(ServiceDiscovery discovery, JsonObject filter, JsonObject consumerConfiguration) {
    return discovery.getRecord(filter).flatMap(res -> {
      if (res == null) {
        return Future.failedFuture("No matching records");
      } else {
        return Future.succeededFuture(discovery.getReferenceWithConfiguration(res, consumerConfiguration).get());
      }
    });
  }
}
