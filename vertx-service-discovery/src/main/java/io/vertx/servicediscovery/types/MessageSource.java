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

package io.vertx.servicediscovery.types;

import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Future;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.spi.ServiceType;

import java.util.Objects;
import java.util.function.Function;

/**
 * Service type for data producer. Providers are publishing data to a specific event bus address.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface MessageSource extends ServiceType {

  String TYPE = "message-source";

  /**
   * Create a record representing a data producer.
   *
   * @param name     the name of the service
   * @param address  the address on which the data is sent
   * @param type     the type of payload (fully qualified name of the class)
   * @param metadata additional metadata
   * @return the created record
   */
  static Record createRecord(String name, String address, String type, JsonObject metadata) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(address);
    Record record = new Record().setName(name)
        .setType(TYPE)
        .setLocation(new JsonObject().put(Record.ENDPOINT, address));

    if (metadata != null) {
      record.setMetadata(metadata);
    }

    if (type != null) {
      record.getMetadata().put("message.type", type);
    }

    return record;
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
  @GenIgnore
  static Record createRecord(String name, String address, Class type, JsonObject metadata) {
    return createRecord(name, address, type != null ? type.getName() : null, metadata);
  }

  /**
   * Same as {@link #createRecord(String, String, Class, JsonObject)} without additional metadata.
   *
   * @param name    the name of the service
   * @param address the address on which the data is sent
   * @param type    the type of payload
   * @return the created record
   */
  @GenIgnore
  static Record createRecord(String name, String address, Class type) {
    return createRecord(name, address, type, null);
  }

  /**
   * Same as {@link #createRecord(String, String, String, JsonObject)} without additional metadata.
   *
   * @param name    the name of the service
   * @param address the address on which the data is sent
   * @param type    the type of payload
   * @return the created record
   */
  static Record createRecord(String name, String address, String type) {
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
  static Record createRecord(String name, String address) {
    return createRecord(name, address, (String) null);
  }

  /**
   * Convenient method that looks for a message source and provides the configured {@link MessageConsumer}. The
   * async result is marked as failed is there are no matching services, or if the lookup fails.
   *
   * @param discovery     The service discovery instance
   * @param filter        The filter, optional
   * @return a future notified with the client
   */
  static <T> Future<MessageConsumer<T>> getConsumer(ServiceDiscovery discovery, JsonObject filter) {
    return discovery.getRecord(filter).flatMap(res -> {
      if (res == null) {
        return Future.failedFuture("No matching records");
      } else {
        return Future.succeededFuture(discovery.getReference(res).get());
      }
    });
  }

  /**
   * Convenient method that looks for a message source and provides the configured {@link MessageConsumer}. The
   * async result is marked as failed is there are no matching services, or if the lookup fails.
   *
   * @param discovery     The service discovery instance
   * @param filter        The filter, must not be {@code null}
   * @return a future notified with the client
   */
  static <T> Future<MessageConsumer<T>> getConsumer(ServiceDiscovery discovery, Function<Record, Boolean> filter) {
    return discovery.getRecord(filter).flatMap(res -> {
      if (res == null) {
        return Future.failedFuture("No matching records");
      } else {
        return Future.succeededFuture(discovery.getReference(res).get());
      }
    });
  }
}
