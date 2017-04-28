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

package io.vertx.servicediscovery.types.impl;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.AbstractServiceReference;
import io.vertx.servicediscovery.types.MessageSource;

import java.util.Objects;

/**
 * Service type for data producer. Providers are publishing data to a specific event bus address.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class MessageSourceImpl implements MessageSource {

  public static final String TYPE = "message-source";

  @Override
  public String name() {
    return TYPE;
  }

  @Override
  public ServiceReference
    get(Vertx vertx, ServiceDiscovery discovery, Record record, JsonObject configuration) {

    Objects.requireNonNull(vertx);
    Objects.requireNonNull(record);
    Objects.requireNonNull(discovery);
    return new MessageSourceReference(vertx, discovery, record);
  }

  /**
   * Implementation of {@link ServiceReference} for data producer.
   */
  private class MessageSourceReference<X> extends AbstractServiceReference<MessageConsumer<X>> {

    MessageSourceReference(Vertx vertx, ServiceDiscovery discovery, Record record) {
      super(vertx, discovery, record);
    }

    /**
     * Creates the event bus consumer for the service.
     *
     * @return the consumer
     */
    @Override
    public MessageConsumer<X> retrieve() {
      return vertx.eventBus().consumer(record().getLocation().getString(Record.ENDPOINT));
    }

    @Override
    protected void onClose() {
      service.unregister();
    }
  }
}
