package io.vertx.servicediscovery.types.impl;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.AbstractServiceReference;
import io.vertx.servicediscovery.types.MongoDataSource;

import java.util.Objects;

/**
 * The implementation of {@link MongoDataSource}.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class MongoDataSourceImpl implements MongoDataSource {

  @Override
  public String name() {
    return MongoDataSource.TYPE;
  }

  @Override
  public ServiceReference get(Vertx vertx, ServiceDiscovery discovery, Record record, JsonObject configuration) {
    Objects.requireNonNull(vertx);
    Objects.requireNonNull(record);
    Objects.requireNonNull(discovery);
    return new MongoServiceReference(vertx, discovery, record, configuration);
  }

  /**
   * A reference on a Mongo data source. When retrieved it provides a {@link MongoClient}. The _shared_ aspect of the
   * client depends on the {@code shared} flag put in the record's metadata (non shared by default).
   */
  private class MongoServiceReference extends AbstractServiceReference<MongoClient> {

    private final JsonObject config;

    MongoServiceReference(Vertx vertx, ServiceDiscovery discovery, Record record, JsonObject config) {
      super(vertx, discovery, record);
      this.config = config;
    }

    @Override
    public MongoClient retrieve() {
      JsonObject result = record().getMetadata().copy();
      result.mergeIn(record().getLocation());

      if (config != null) {
        result.mergeIn(config);
      }

      if (result.getBoolean("shared", false)) {
        return MongoClient.createShared(vertx, result);
      } else {
        return MongoClient.create(vertx, result);
      }
    }

    @Override
    protected void onClose() {
      service.close();
    }
  }
}
