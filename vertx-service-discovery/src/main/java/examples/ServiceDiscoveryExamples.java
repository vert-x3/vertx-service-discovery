/*
 * Copyright (c) 2011-2016 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *      The Eclipse Public License is available at
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *      The Apache License v2.0 is available at
 *      http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */

package examples;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.HttpEndpoint;
import io.vertx.ext.jdbc.JDBCClient;

import java.util.List;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ServiceDiscoveryExamples {

  public void example1(Vertx vertx) {
    // Use default configuration
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

    // Customize the configuration
    discovery = ServiceDiscovery.create(vertx,
        new ServiceDiscoveryOptions()
            .setAnnounceAddress("service-announce")
            .setName("my-name"));

    // Do something...

    discovery.close();
  }

  public void example2(ServiceDiscovery discovery) {
    // Manual record creation
    Record record = new Record()
        .setType("eventbus-service-proxy")
        .setLocation(new JsonObject().put("endpoint", "the-service-address"))
        .setName("my-service")
        .setMetadata(new JsonObject().put("some-label", "some-value"));

    discovery.publish(record).onComplete(ar -> {
      if (ar.succeeded()) {
        // publication succeeded
        Record publishedRecord = ar.result();
      } else {
        // publication failed
      }
    });

    // Record creation from a type
    record = HttpEndpoint.createRecord("some-rest-api", "localhost", 8080, "/api");
    discovery.publish(record).onComplete(ar -> {
      if (ar.succeeded()) {
        // publication succeeded
        Record publishedRecord = ar.result();
      } else {
        // publication failed
      }
    });
  }

  public void example3(ServiceDiscovery discovery, Record record) {

    discovery.unpublish(record.getRegistration()).onComplete(ar -> {
      if (ar.succeeded()) {
        // Ok
      } else {
        // cannot un-publish the service, may have already been removed, or the record is not published
      }
    });
  }

  public void example4(ServiceDiscovery discovery) {
    // Get any record
    discovery.getRecord(r -> true).onComplete(ar -> {
      if (ar.succeeded()) {
        if (ar.result() != null) {
          // we have a record
        } else {
          // the lookup succeeded, but no matching service
        }
      } else {
        // lookup failed
      }
    });

    discovery.getRecord((JsonObject) null).onComplete(ar -> {
      if (ar.succeeded()) {
        if (ar.result() != null) {
          // we have a record
        } else {
          // the lookup succeeded, but no matching service
        }
      } else {
        // lookup failed
      }
    });


    // Get a record by name
    discovery.getRecord(r -> r.getName().equals("some-name")).onComplete(ar -> {
      if (ar.succeeded()) {
        if (ar.result() != null) {
          // we have a record
        } else {
          // the lookup succeeded, but no matching service
        }
      } else {
        // lookup failed
      }
    });

    discovery.getRecord(new JsonObject().put("name", "some-service")).onComplete(ar -> {
      if (ar.succeeded()) {
        if (ar.result() != null) {
          // we have a record
        } else {
          // the lookup succeeded, but no matching service
        }
      } else {
        // lookup failed
      }
    });

    // Get all records matching the filter
    discovery.getRecords(r -> "some-value".equals(r.getMetadata().getString("some-label"))).onComplete(ar -> {
      if (ar.succeeded()) {
        List<Record> results = ar.result();
        // If the list is not empty, we have matching record
        // Else, the lookup succeeded, but no matching service
      } else {
        // lookup failed
      }
    });


    discovery.getRecords(new JsonObject().put("some-label", "some-value")).onComplete(ar -> {
      if (ar.succeeded()) {
        List<Record> results = ar.result();
        // If the list is not empty, we have matching record
        // Else, the lookup succeeded, but no matching service
      } else {
        // lookup failed
      }
    });


  }

  public void example5(ServiceDiscovery discovery, Record record1, Record record2) {
    ServiceReference reference1 = discovery.getReference(record1);
    ServiceReference reference2 = discovery.getReference(record2);

    // Then, gets the service object, the returned type depends on the service type:
    // For http endpoint:
    HttpClient client = reference1.getAs(HttpClient.class);
    // For message source
    MessageConsumer consumer = reference2.getAs(MessageConsumer.class);

    // When done with the service
    reference1.release();
    reference2.release();
  }

  public void example51(ServiceDiscovery discovery, Record record, JsonObject conf) {
    ServiceReference reference = discovery.getReferenceWithConfiguration(record, conf);

    // Then, gets the service object, the returned type depends on the service type:
    // For http endpoint:
    JDBCClient client = reference.getAs(JDBCClient.class);

    // Do something with the client...

    // When done with the service
    reference.release();
  }


}
