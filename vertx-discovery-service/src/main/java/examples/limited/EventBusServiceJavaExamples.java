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

package examples.limited;

import examples.MyService;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.discovery.DiscoveryService;
import io.vertx.ext.discovery.Record;
import io.vertx.ext.discovery.ServiceReference;
import io.vertx.ext.discovery.types.EventBusService;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class EventBusServiceJavaExamples {

  public void example1(DiscoveryService discovery) {
    Record record = EventBusService.createRecord(
        "some-eventbus-service", // The service name
        "address", // the service address,
        MyService.class // the service interface
    );

    discovery.publish(record, ar -> {
      // ...
    });

  }

  public void example2(DiscoveryService discovery) {
    // Get the record
    discovery.getRecord(new JsonObject().put("name", "some-eventbus-service"), ar -> {
      if (ar.succeeded() && ar.result() != null) {
        // Retrieve the service reference
        ServiceReference reference = discovery.getReference(ar.result());
        // Retrieve the service object
        MyService service = reference.get();

        // Dont' forget to release the service
        reference.release();
      }
    });
  }

  // Java only
  public void example3(DiscoveryService discovery) {
    EventBusService.getProxy(discovery, MyService.class, ar -> {
      if (ar.succeeded()) {
        MyService service = ar.result();

        // Dont' forget to release the service
        DiscoveryService.releaseServiceObject(discovery, service);
      }
    });
  }

}
