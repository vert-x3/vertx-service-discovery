package examples;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.MongoDataSource;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class MongoDataSourceExamples {

  public void example1(ServiceDiscovery discovery) {
    Record record = MongoDataSource.createRecord(
      "some-data-source-service", // The service name
      new JsonObject().put("connection_string", "some mongo connection"), // The location
      new JsonObject().put("some-metadata", "some-value") // Some metadata
    );

    discovery.publish(record).onComplete(ar -> {
      // ...
    });
  }

  public void example2(ServiceDiscovery discovery) {
    // Get the record
    discovery.getRecord(
      new JsonObject().put("name", "some-data-source-service")).onComplete(ar -> {
        if (ar.succeeded() && ar.result() != null) {
          // Retrieve the service reference
          ServiceReference reference = discovery.getReferenceWithConfiguration(
            ar.result(), // The record
            new JsonObject().put("username", "clement").put("password", "*****")); // Some additional metadata

          // Retrieve the service object
          MongoClient client = reference.get();

          // ...

          // when done
          reference.release();
        }
      });
  }

  public void example3(ServiceDiscovery discovery) {
    MongoDataSource.getMongoClient(discovery,
      new JsonObject().put("name", "some-data-source-service"),
      new JsonObject().put("username", "clement").put("password", "*****") // Some additional metadata
    ).onComplete(ar -> {
        if (ar.succeeded()) {
          MongoClient client = ar.result();

          // ...

          // Dont' forget to release the service
          ServiceDiscovery.releaseServiceObject(discovery, client);

        }
      });
  }
}
