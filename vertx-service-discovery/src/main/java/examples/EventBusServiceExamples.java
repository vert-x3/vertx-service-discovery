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

import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.EventBusService;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class EventBusServiceExamples {

  public void example1(ServiceDiscovery discovery) {
    Record record = EventBusService.createRecord(
        "some-eventbus-service", // The service name
        "address", // the service address,
        "examples.MyService", // the service interface as string
        new JsonObject()
            .put("some-metadata", "some value")
    );

    discovery.publish(record).onComplete(ar -> {
      // ...
    });
  }

  public void example2(ServiceDiscovery discovery) {
    // Get the record
    discovery.getRecord(new JsonObject().put("name", "some-eventbus-service")).onComplete(ar -> {
      if (ar.succeeded() && ar.result() != null) {
        // Retrieve the service reference
        ServiceReference reference = discovery.getReference(ar.result());
        // Retrieve the service object
        MyService service = reference.getAs(MyService.class);

        // Dont' forget to release the service
        reference.release();
      }
    });
  }

  // Java only
  public void example3(ServiceDiscovery discovery) {
    EventBusService.getProxy(discovery, MyService.class).onComplete(ar -> {
      if (ar.succeeded()) {
        MyService service = ar.result();

        // Dont' forget to release the service
        ServiceDiscovery.releaseServiceObject(discovery, service);
      }
    });
  }

  public void example31(ServiceDiscovery discovery) {
    EventBusService.getServiceProxyWithJsonFilter(discovery,
      new JsonObject().put("service.interface", "org.acme.MyService"), // The java interface
      MyService.class).onComplete( // The expect client
      ar -> {
      if (ar.succeeded()) {
        MyService service = ar.result();

        // Dont' forget to release the service
        ServiceDiscovery.releaseServiceObject(discovery, service);
      }
    });
  }

}
