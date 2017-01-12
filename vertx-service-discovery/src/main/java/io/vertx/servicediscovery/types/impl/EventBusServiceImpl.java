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
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.AbstractServiceReference;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.servicediscovery.utils.ClassLoaderUtils;
import io.vertx.serviceproxy.ProxyHelper;

import java.util.Objects;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class EventBusServiceImpl<T> implements EventBusService {

  @Override
  public String name() {
    return TYPE;
  }

  @Override
  public ServiceReference get(Vertx vertx, ServiceDiscovery discovery, Record record, JsonObject
    configuration) {
    Objects.requireNonNull(vertx);
    Objects.requireNonNull(record);
    Objects.requireNonNull(discovery);
    return new EventBusServiceReference(vertx, discovery, record, configuration);
  }

  /**
   * Implementation of {@link ServiceReference} for event bus service proxies.
   */
  private class EventBusServiceReference extends AbstractServiceReference<T> {

    private final DeliveryOptions deliveryOptions;
    private final String serviceInterface;

    EventBusServiceReference(Vertx vertx, ServiceDiscovery discovery, Record record, JsonObject conf) {
      super(vertx, discovery, record);
      this.serviceInterface = record.getMetadata().getString("service.interface");
      if (conf != null) {
        this.deliveryOptions = new DeliveryOptions(conf);
      } else {
        this.deliveryOptions = null;
      }
      Objects.requireNonNull(serviceInterface);
    }


    /**
     * Build the service proxy and return it. If already built, it returns the cached one.
     *
     * @return the proxy
     */
    @Override
    public synchronized T retrieve() {
      Class<T> itf = ClassLoaderUtils.load(serviceInterface, this.getClass().getClassLoader());
      if (itf == null) {
        throw new IllegalStateException("Cannot load class " + serviceInterface);
      } else {
        return ProxyHelper.createProxy(itf, vertx, record().getLocation().getString(Record.ENDPOINT), deliveryOptions);
      }
    }
  }
}

