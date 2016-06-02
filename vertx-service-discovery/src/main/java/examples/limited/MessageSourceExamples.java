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

import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.types.MessageSource;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class MessageSourceExamples {

  public void example1(ServiceDiscovery discovery) {
    Record record1 = MessageSource.createRecord(
        "some-message-source-service", // The service name
        "some-address", // The event bus address
        JsonObject.class // The message payload type
    );

    Record record2 = MessageSource.createRecord(
        "some-other-message-source-service", // The service name
        "some-address", // The event bus address
        JsonObject.class, // The message payload type
        new JsonObject().put("some-metadata", "some value")
    );
  }

}
