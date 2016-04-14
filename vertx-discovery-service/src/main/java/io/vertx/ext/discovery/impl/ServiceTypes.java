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

package io.vertx.ext.discovery.impl;

import io.vertx.ext.discovery.Record;
import io.vertx.ext.discovery.spi.ServiceType;

import java.util.Objects;
import java.util.ServiceLoader;

/**
 * Classes responsible for finding the service type implementations on the classpath.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ServiceTypes {

  public static ServiceType get(Record record) {
    synchronized (ServiceTypes.class) {
      if (types == null  || ! types.iterator().hasNext())  {
        types = ServiceLoader.load(ServiceType.class);
      }

      String type = record.getType();
      Objects.requireNonNull(type);

      for (ServiceType next : types) {
        if (next.name().equalsIgnoreCase(type)) {
          return next;
        }
      }

      throw new IllegalArgumentException("Unsupported service type " + type);
    }
  }

  private static ServiceLoader<ServiceType> types;
}
