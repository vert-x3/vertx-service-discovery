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

import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.HttpEndpoint;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class HTTPEndpointExamples {

  public void example1(ServiceDiscovery discovery) {
    Record record1 = HttpEndpoint.createRecord(
        "some-http-service", // The service name
        "localhost", // The host
        8433, // the port
        "/api" // the root of the service
    );

    discovery.publish(record1, ar -> {
      // ...
    });

    Record record2 = HttpEndpoint.createRecord(
        "some-other-name", // the service name
        true, // whether or not the service requires HTTPs
        "localhost", // The host
        8433, // the port
        "/api", // the root of the service
        new JsonObject().put("some-metadata", "some value")
    );

  }

  public void example2(ServiceDiscovery discovery) {
    // Get the record
    discovery.getRecord(new JsonObject().put("name", "some-http-service"), ar -> {
      if (ar.succeeded()  && ar.result() != null) {
        // Retrieve the service reference
        ServiceReference<HttpClient> reference = discovery.getReference(ar.result());
        // Retrieve the service object
        HttpClient client = reference.get();

        // You need to path the complete path
        client.getNow("/api/persons", response -> {

          // ...

          // Dont' forget to release the service
          reference.release();

        });
      }
    });
  }

  public void example3(ServiceDiscovery discovery) {
    HttpEndpoint.getClient(discovery, new JsonObject().put("name", "some-http-service"), ar -> {
      if (ar.succeeded()) {
        HttpClient client = ar.result();

        // You need to path the complete path
        client.getNow("/api/persons", response -> {

          // ...

          // Dont' forget to release the service
          ServiceDiscovery.releaseServiceObject(discovery, client);

        });
      }
    });
  }

}
