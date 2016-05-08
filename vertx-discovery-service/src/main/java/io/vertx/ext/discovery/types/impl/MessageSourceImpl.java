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

package io.vertx.ext.discovery.types.impl;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.discovery.DiscoveryService;
import io.vertx.ext.discovery.Record;
import io.vertx.ext.discovery.ServiceReference;
import io.vertx.ext.discovery.types.AbstractServiceReference;
import io.vertx.ext.discovery.types.MessageSource;

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
  public ServiceReference get(Vertx vertx, DiscoveryService discovery, Record record, JsonObject configuration) {
    Objects.requireNonNull(vertx);
    Objects.requireNonNull(record);
    Objects.requireNonNull(discovery);
    return new MessageSourceReference(vertx, discovery, record);
  }

  /**
   * Implementation of {@link ServiceReference} for data producer.
   */
  private class MessageSourceReference extends AbstractServiceReference<MessageConsumer> {

    MessageSourceReference(Vertx vertx, DiscoveryService discovery, Record record) {
      super(vertx, discovery, record);
    }

    /**
     * Creates the event bus consumer for the service.
     *
     * @return the consumer
     */
    @Override
    public MessageConsumer retrieve() {
      return vertx.eventBus().consumer(record().getLocation().getString(Record.ENDPOINT));
    }

    @Override
    protected void close() {
      service.unregister();
    }
  }
}
