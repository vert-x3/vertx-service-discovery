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
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.discovery.DiscoveryOptions;
import io.vertx.ext.discovery.DiscoveryService;
import io.vertx.ext.discovery.Record;
import io.vertx.ext.discovery.ServiceReference;
import io.vertx.ext.discovery.types.HttpEndpoint;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.groovy.core.eventbus.MessageConsumer;

import java.util.List;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Examples {

  public void example1(Vertx vertx) {
    // Use default configuration
    DiscoveryService service = DiscoveryService.create(vertx);

    // Customize the configuration
    DiscoveryService service2 = DiscoveryService.create(vertx,
        new DiscoveryOptions()
            .setAnnounceAddress("service-announce")
            .setName("my-name"));

    // Do something...

    service.close();
    service2.close();
  }

  public void example2(DiscoveryService service) {
    // Manual record creation
    Record record = new Record()
        .setType("eventbus-service-proxy")
        .setLocation(new JsonObject().put("endpoint", "the-service-address"))
        .setName("my-service")
        .setMetadata(new JsonObject().put("some-label", "some-value"));

    service.publish(record, ar -> {
      if (ar.succeeded()) {
        // publication succeeded
        Record publishedRecord = ar.result();
      } else {
        // publication failed
      }
    });

    // Record creation from a type
    record = HttpEndpoint.createRecord("some-rest-api", "localhost", 8080, "/api");
    service.publish(record, ar -> {
      if (ar.succeeded()) {
        // publication succeeded
        Record publishedRecord = ar.result();
      } else {
        // publication failed
      }
    });
  }

  public void example3(DiscoveryService service, Record record) {

    service.unpublish(record.getRegistration(), ar -> {
      if (ar.succeeded()) {
        // Ok
      } else {
        // cannot un-publish the service, may have already been removed, or the record is not published
      }
    });
  }

  public void example4(DiscoveryService service) {
    // Get any record
    service.getRecord(r -> true, ar -> {
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

    service.getRecord((JsonObject) null, ar -> {
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
    service.getRecord(r -> r.getName().equals("some-name"), ar -> {
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

    service.getRecord(new JsonObject().put("name", "some-service"), ar -> {
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
    service.getRecords(r -> "some-value".equals(r.getMetadata().getString("some-label")), ar -> {
      if (ar.succeeded()) {
        List<Record> results = ar.result();
        // If the list is not empty, we have matching record
        // Else, the lookup succeeded, but no matching service
      } else {
        // lookup failed
      }
    });


    service.getRecords(new JsonObject().put("some-label", "some-value"), ar -> {
      if (ar.succeeded()) {
        List<Record> results = ar.result();
        // If the list is not empty, we have matching record
        // Else, the lookup succeeded, but no matching service
      } else {
        // lookup failed
      }
    });


  }

  public void example5(DiscoveryService discovery, Record record) {
    ServiceReference reference = discovery.getReference(record);

    // Then, gets the service object, the returned type depends on the service type:
    // For http endpoint:
    HttpClient client = reference.get();
    // For message source
    MessageConsumer consumer = reference.get();

    // When done with the service
    reference.release();
  }

  public void example51(DiscoveryService discovery, Record record, JsonObject conf) {
    ServiceReference reference = discovery.getReferenceWithConfiguration(record, conf);

    // Then, gets the service object, the returned type depends on the service type:
    // For http endpoint:
    JDBCClient client = reference.get();

    // Do something with the client...

    // When done with the service
    reference.release();
  }


}
