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
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.spi.ServiceType;

import java.util.Objects;
import java.util.function.Function;

/**
 * {@link ServiceType} for event bus services (service proxies).
 * Consumers receive a service proxy to use the service.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface EventBusService extends ServiceType {

  /**
   * Name of the type.
   */
  String TYPE = "eventbus-service-proxy";

  /**
   * Sugar method to creates a record for this type.
   * <p>
   * The java interface is added to the metadata in the `service.interface` key.
   *
   * @param name     the name of the service.
   * @param address  the event bus address on which the service available
   * @param itf      the Java interface
   * @param metadata the metadata
   * @return the created record
   */
  @GenIgnore
  static Record createRecord(String name, String address, Class itf, JsonObject metadata) {
    return createRecord(name, address, itf.getName(), metadata);
  }

  /**
   * Sugar method to creates a record for this type.
   * <p>
   * The java interface is added to the metadata in the `service.interface` key.
   *
   * @param name     the name of the service.
   * @param address  the event bus address on which the service available
   * @param itf      the Java interface (name)
   * @param metadata the metadata
   * @return the created record
   */
  static Record createRecord(String name, String address, String itf, JsonObject metadata) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(itf);
    Objects.requireNonNull(address);

    JsonObject meta;
    if (metadata == null) {
      meta = new JsonObject();
    } else {
      meta = metadata.copy();
    }

    return new Record()
      .setType(TYPE)
      .setName(name)
      .setMetadata(meta.put("service.interface", itf))
      .setLocation(new JsonObject().put(Record.ENDPOINT, address));
  }

  /**
   * Same as {@link #createRecord(String, String, Class, JsonObject)} but without metadata.
   *
   * @param name    the name of the service
   * @param itf     the Java interface
   * @param address the event bus address on which the service available
   * @return the created record
   */
  @GenIgnore
  static Record createRecord(String name, String address, Class itf) {
    return createRecord(name, address, itf, null);
  }

  /**
   * Lookup for a service record and if found, retrieve it and return the service object (used to consume the service).
   * This is a convenient method to avoid explicit lookup and then retrieval of the service. A filter based on the
   * request interface is used.
   *
   * @param discovery     the service discovery instance
   * @param itf           the service interface
   * @return a future notified with the client
   * @param <T>           the service interface
   * @return {@code null}
   */
  @GenIgnore // Java only
  static <T> Future<T> getProxy(ServiceDiscovery discovery, Class<T> itf) {
    return Future.future(resultHandler -> {
      JsonObject filter = new JsonObject().put("service.interface", itf.getName());
      discovery.getRecord(filter).onComplete(ar -> {
        if (ar.failed()) {
          resultHandler.handle(Future.failedFuture(ar.cause()));
        } else {
          if (ar.result() == null) {
            resultHandler.handle(Future.failedFuture("Cannot find service matching with " + filter));
          } else {
            ServiceReference service = discovery.getReference(ar.result());
            resultHandler.handle(Future.succeededFuture(service.get()));
          }
        }
      });
    });
  }

  /**
   * Lookup for a service record and if found, retrieve it and return the service object (used to consume the service).
   * This is a convenient method to avoid explicit lookup and then retrieval of the service. A filter based on the
   * request interface is used.
   *
   * @param discovery     the service discovery instance
   * @param itf           the service interface
   * @param conf          the configuration for message delivery
   * @return a future notified with the client
   * @param <T>           the service interface
   * @return {@code null}
   */
  @GenIgnore // Java only
  static <T> Future<T> getProxy(ServiceDiscovery discovery, Class<T> itf, JsonObject conf) {
    return Future.future(resultHandler -> {
      JsonObject filter = new JsonObject().put("service.interface", itf.getName());
      discovery.getRecord(filter).onComplete(ar -> {
        if (ar.failed()) {
          resultHandler.handle(Future.failedFuture(ar.cause()));
        } else {
          if (ar.result() == null) {
            resultHandler.handle(Future.failedFuture("Cannot find service matching with " + filter));
          } else {
            ServiceReference service = discovery.getReferenceWithConfiguration(ar.result(), conf);
            resultHandler.handle(Future.succeededFuture(service.get()));
          }
        }
      });
    });
  }

  /**
   * Lookup for a service record and if found, retrieve it and return the service object (used to consume the service).
   * This is a convenient method to avoid explicit lookup and then retrieval of the service. This method requires to
   * have the {@code clientClass} set with the expected set of client. This is important for usages not using Java so
   * you can pass the expected type.
   *
   * @param discovery     the service discovery
   * @param filter        the filter
   * @param clientClass   the client class
   * @return a future notified with the client
   * @param <T>           the type of the client class
   * @return {@code null} - do not use
   */
  static <T> Future<T> getServiceProxy(ServiceDiscovery discovery,
                               Function<Record, Boolean> filter,
                               Class<T> clientClass) {
    return Future.future(resultHandler -> {
      discovery.getRecord(filter).onComplete(ar -> {
        if (ar.failed()) {
          resultHandler.handle(Future.failedFuture(ar.cause()));
        } else {
          if (ar.result() == null) {
            resultHandler.handle(Future.failedFuture("Cannot find service matching with " + filter));
          } else {
            ServiceReference service = discovery.getReference(ar.result());
            resultHandler.handle(Future.succeededFuture(service.getAs(clientClass)));
          }
        }
      });
    });
  }

  /**
   * Lookup for a service record and if found, retrieve it and return the service object (used to consume the service).
   * This is a convenient method to avoid explicit lookup and then retrieval of the service. This method requires to
   * have the {@code clientClass} set with the expected set of client. This is important for usages not using Java so
   * you can pass the expected type.
   *
   * @param discovery     the service discovery
   * @param filter        the filter
   * @param clientClass   the client class
   * @param conf          the configuration for message delivery
   * @return a future notified with the client
   * @param <T>           the type of the client class
   * @return {@code null} - do not use
   */
  static <T> Future<T> getServiceProxy(ServiceDiscovery discovery,
                               Function<Record, Boolean> filter,
                               Class<T> clientClass,
                               JsonObject conf) {
    return Future.future(resultHandler -> {
      discovery.getRecord(filter).onComplete(ar -> {
        if (ar.failed()) {
          resultHandler.handle(Future.failedFuture(ar.cause()));
        } else {
          if (ar.result() == null) {
            resultHandler.handle(Future.failedFuture("Cannot find service matching with " + filter));
          } else {
            ServiceReference service = discovery.getReferenceWithConfiguration(ar.result(), conf);
            resultHandler.handle(Future.succeededFuture(service.getAs(clientClass)));
          }
        }
      });
    });
  }

  /**
   * Lookup for a service record and if found, retrieve it and return the service object (used to consume the service).
   * This is a convenient method to avoid explicit lookup and then retrieval of the service. This method requires to
   * have the {@code clientClass} set with the expected set of client. This is important for usages not using Java so
   * you can pass the expected type.
   *
   * @param discovery     the service discovery
   * @param filter        the filter as json object
   * @param clientClass   the client class
   * @return a future notified with the client
   * @param <T>           the type of the client class
   * @return {@code null} - do not use
   */
  static <T> Future<T> getServiceProxyWithJsonFilter(ServiceDiscovery discovery,
                                             JsonObject filter,
                                             Class<T> clientClass) {
    return Future.future(resultHandler -> {
      discovery.getRecord(filter).onComplete(ar -> {
        if (ar.failed()) {
          resultHandler.handle(Future.failedFuture(ar.cause()));
        } else {
          if (ar.result() == null) {
            resultHandler.handle(Future.failedFuture("Cannot find service matching with " + filter));
          } else {
            ServiceReference service = discovery.getReference(ar.result());
            resultHandler.handle(Future.succeededFuture(service.getAs(clientClass)));
          }
        }
      });
    });
  }

  /**
   * Lookup for a service record and if found, retrieve it and return the service object (used to consume the service).
   * This is a convenient method to avoid explicit lookup and then retrieval of the service. This method requires to
   * have the {@code clientClass} set with the expected set of client. This is important for usages not using Java so
   * you can pass the expected type.
   *
   * @param discovery     the service discovery
   * @param filter        the filter as json object
   * @param clientClass   the client class
   * @param conf          the configuration for message delivery
   * @return a future notified with the client
   * @param <T>           the type of the client class
   * @return {@code null} - do not use
   */
  static <T> Future<T> getServiceProxyWithJsonFilter(ServiceDiscovery discovery,
                                             JsonObject filter,
                                             Class<T> clientClass,
                                             JsonObject conf) {
    return Future.future(resultHandler -> {
      discovery.getRecord(filter).onComplete(ar -> {
        if (ar.failed()) {
          resultHandler.handle(Future.failedFuture(ar.cause()));
        } else {
          if (ar.result() == null) {
            resultHandler.handle(Future.failedFuture("Cannot find service matching with " + filter));
          } else {
            ServiceReference service = discovery.getReferenceWithConfiguration(ar.result(), conf);
            resultHandler.handle(Future.succeededFuture(service.getAs(clientClass)));
          }
        }
      });
    });
  }

  /**
   * Creates a record based on the parameters.
   *
   * @param name      the service name
   * @param address   the address
   * @param classname the payload class
   * @return the record
   */
  static Record createRecord(String name, String address, String classname) {
    return createRecord(name, address, classname, null);
  }
}
