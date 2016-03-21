/*
 * Copyright (c) 2011-$tody.year The original author or authors
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

package io.vertx.ext.discovery.types;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.discovery.Record;
import io.vertx.ext.discovery.ServiceReference;
import io.vertx.ext.discovery.spi.ServiceType;

import java.util.Objects;

/**
 * Service type for data producer. Providers are publishing data to a specific event bus address.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class MessageSource implements ServiceType {

  public static final String TYPE = "message-source";

  @Override
  public String name() {
    return TYPE;
  }

  @Override
  public ServiceReference get(Vertx vertx, Record record) {
    return new MessageSourceReference(vertx, record);
  }

  /**
   * Create a record representing a data producer.
   *
   * @param name     the name of the service
   * @param address  the address on which the data is sent
   * @param type     the type of payload
   * @param metadata additional metadata
   * @return the created record
   */
  public static Record createRecord(String name, String address, Class type, JsonObject metadata) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(address);
    Record record = new Record().setName(name)
        .setType(TYPE)
        .setLocation(new JsonObject().put(Record.ENDPOINT, address));

    if (metadata != null) {
      record.setMetadata(metadata);
    }

    if (type != null) {
      record.setMetadata(new JsonObject().put("message.type", type.getName()));
    }

    return record;
  }

  /**
   * Same as {@link #createRecord(String, String, Class, JsonObject)} without additional metadata.
   *
   * @param name    the name of the service
   * @param address the address on which the data is sent
   * @param type    the type of payload
   * @return the created record
   */
  public static Record createRecord(String name, String address, Class type) {
    return createRecord(name, address, type, null);
  }

  /**
   * Same as {@link #createRecord(String, String, Class, JsonObject)} without additional metadata, and no type for
   * the payload.
   *
   * @param name    the name of the service
   * @param address the address on which the data is sent.
   * @return the created record
   */
  public static Record createRecord(String name, String address) {
    return createRecord(name, address, null);
  }

  /**
   * Implementation of {@link ServiceReference} for data producer.
   */
  private class MessageSourceReference implements ServiceReference {

    private final Record record;
    private final Vertx vertx;
    private MessageConsumer consumer;

    MessageSourceReference(Vertx vertx, Record record) {
      this.vertx = vertx;
      this.record = record;
    }

    /**
     * @return the service record.
     */
    @Override
    public Record record() {
      return record;
    }

    /**
     * Creates the event bus consumer for the service. If already created, reuse the same.
     *
     * @param <T> the type of the message payload.
     * @return the consumer
     */
    @Override
    public synchronized <T> T get() {
      if (consumer != null) {
        return (T) consumer;
      }
      consumer = vertx.eventBus().consumer(record.getLocation().getString(Record.ENDPOINT));
      return (T) consumer;
    }

    /**
     * Releases the consumer.
     */
    @Override
    public synchronized void release() {
      consumer.unregister();
      consumer = null;
    }
  }
}
