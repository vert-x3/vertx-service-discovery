package io.vertx.ext.discovery.impl;

import io.vertx.ext.discovery.Record;
import io.vertx.ext.discovery.spi.ServiceType;

import java.util.Iterator;
import java.util.Objects;
import java.util.ServiceLoader;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ServiceTypes {

  static {
    types = ServiceLoader.load(ServiceType.class);
  }

  public static ServiceType get(Record record) {
    String type = record.getType();
    Objects.requireNonNull(type);

    for (ServiceType next : types) {
      if (next.name().equalsIgnoreCase(type)) {
        return next;
      }
    }

    throw new IllegalArgumentException("Unsupported service type " + type);
  }

  private static ServiceLoader<ServiceType> types;
}
