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

import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.MessageSource;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class MessageSourceExamples {

  public void example1(ServiceDiscovery discovery) {
    Record record = MessageSource.createRecord(
        "some-message-source-service", // The service name
        "some-address" // The event bus address
    );

    discovery.publish(record).onComplete(ar -> {
      // ...
    });

    record = MessageSource.createRecord(
        "some-other-message-source-service", // The service name
        "some-address", // The event bus address
        "examples.MyData" // The payload type
    );
  }

  public void example2(ServiceDiscovery discovery) {
    // Get the record
    discovery.getRecord(new JsonObject().put("name", "some-message-source-service")).onComplete(ar -> {
      if (ar.succeeded() && ar.result() != null) {
        // Retrieve the service reference
        ServiceReference reference = discovery.getReference(ar.result());
        // Retrieve the service object
        MessageConsumer<JsonObject> consumer = reference.getAs(MessageConsumer.class);

        // Attach a message handler on it
        consumer.handler(message -> {
          // message handler
          JsonObject payload = message.body();
        });
      }
    });
  }

  public void example3(ServiceDiscovery discovery) {
    MessageSource.<JsonObject>getConsumer(discovery, new JsonObject().put("name", "some-message-source-service")).onComplete(ar -> {
      if (ar.succeeded()) {
        MessageConsumer<JsonObject> consumer = ar.result();

        // Attach a message handler on it
        consumer.handler(message -> {
          // message handler
          JsonObject payload = message.body();
        });
        // ...
      }
    });
  }

}
