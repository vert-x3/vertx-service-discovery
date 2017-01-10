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

package io.vertx.servicediscovery.spi;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;

/**
 * Represents a service type.
 * <p>
 * A service type is for example: `http-endpoint`, `data source`, `message source`. It
 * defines what kind of resources is accessed through the service. For example, in the case of a `http endpoint`, the
 * service object is a HTTP client. For `message source` it would a a consumer on which you set a handler receiving
 * the message.
 * <p>
 * You can define your own service type by implementing this interface and configure the SPI file
 * (META-INF/services/io.vertx.servicediscovery.spi.ServiceType) with your own implementation.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public interface ServiceType {

  /**
   * Unknown type.
   */
  String UNKNOWN = "unknown";

  /**
   * @return the name of the type.
   */
  String name();

  /**
   * Gets the `service` for the given record. The record's type must match the current type. From the
   * returned {@link ServiceReference}, the consumer can start using the service and release it.
   *
   * @param vertx         the vert.x instance
   * @param discovery     the discovery instance requesting the reference
   * @param record        the record
   * @param configuration some optional configuration, maybe be {@code null} or empty
   * @return the retrieved {@link ServiceReference}
   */
  ServiceReference get(Vertx vertx, ServiceDiscovery discovery, Record record, JsonObject configuration);

}
