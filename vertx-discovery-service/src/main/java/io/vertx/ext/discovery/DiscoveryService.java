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

package io.vertx.ext.discovery;

import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.discovery.impl.DiscoveryImpl;
import io.vertx.ext.discovery.spi.DiscoveryBridge;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Discovery service main entry point.
 * <p>
 * The discovery service is an infrastructure that let you publish and find `services`. A `service` is a discoverable
 * functionality. It can be qualified by its type, metadata, and location. So a `service` can be a database, a
 * service proxy, a HTTP endpoint. It does not have to be a vert.x entity, but can be anything. Each service is
 * described by a {@link Record}.
 * <p>
 * The discovery service implements the interactions defined in the service-oriented computing. And to some extend,
 * also provides the dynamic service-oriented computing interaction. So, application can react to arrival and
 * departure of services.
 * <p>
 * A service provider can:
 * <p>
 * * publish a service record
 * * un-publish a published record
 * * update the status of a published service (down, out of service...)
 * <p>
 * A service consumer can:
 * <p>
 * * lookup for services
 * * bind to a selected service (it gets a {@link ServiceReference}) and use it
 * * release the service once the consumer is done with it
 * * listen for arrival, departure and modification of services.
 * <p>
 * Consumer would 1) lookup for service record matching their need, 2) retrieve the {@link ServiceReference} that give access
 * to the service, 3) get a service object to access the service, 4) release the service object once done.
 * <p>
 * A state above, the central piece of information shared by the providers and consumers are {@link Record records}.
 * <p>
 * Providers and consumers must create their own {@link DiscoveryService} instance. These instances are collaborating
 * in background (distributed structure) to keep the set of services in sync.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface DiscoveryService {

  /**
   * Creates an instance of {@link DiscoveryService}.
   *
   * @param vertx   the vert.x instance
   * @param options the discovery options
   * @return the create discovery service.
   */
  static DiscoveryService create(Vertx vertx, DiscoveryOptions options) {
    return new DiscoveryImpl(vertx, options);
  }

  /**
   * Creates a new instance of {@link DiscoveryService} using the default configuration.
   *
   * @param vertx the vert.x instance
   * @return the created instance
   */
  static DiscoveryService create(Vertx vertx) {
    return create(vertx, new DiscoveryOptions());
  }

  /**
   * Gets a service reference from the given record.
   *
   * @param record the chosen record
   * @return the service reference, that allows retrieving the service object. Once called the service reference is
   * cached, and need to be released.
   */
  ServiceReference getReference(Record record);

  /**
   * Gets a service reference from the given record, the reference is configured with the given json object.
   *
   * @param record        the chosen record
   * @param configuration the configuration
   * @return the service reference, that allows retrieving the service object. Once called the service reference is
   * cached, and need to be released.
   */
  ServiceReference getReferenceWithConfiguration(Record record, JsonObject configuration);

  /**
   * Releases the service reference.
   *
   * @param reference the reference to release, must not be {@code null}
   * @return whether or not the reference has been released.
   */
  boolean release(ServiceReference reference);

  /**
   * Registers a discovery bridge. Bridges let you integrate other discovery technologies in this discovery service.
   *
   * @param bridge        the bridge
   * @param configuration the optional configuration
   * @return the current {@link DiscoveryService}
   */
  @GenIgnore
  DiscoveryService registerDiscoveryBridge(DiscoveryBridge bridge, JsonObject configuration);

  /**
   * Closes the discovery service
   */
  void close();

  /**
   * Publishes a record.
   *
   * @param record        the record
   * @param resultHandler handler called when the operation has completed (successfully or not). In case of success,
   *                      the passed record has a registration id required to modify and un-register the service.
   */
  void publish(Record record, Handler<AsyncResult<Record>> resultHandler);

  /**
   * Un-publishes a record.
   *
   * @param id            the registration id
   * @param resultHandler handler called when the operation has completed (successfully or not).
   */
  void unpublish(String id, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Lookups for a single record.
   * <p>
   * Filters are expressed using a Json object. Each entry of the given filter will be checked against the record.
   * All entry must match exactly the record. The entry can use the special "*" value to denotes a requirement on the
   * key, but not on the value.
   * <p>
   * Let's take some example:
   * <pre>
   *   { "name" = "a" } => matches records with name set fo "a"
   *   { "color" = "*" } => matches records with "color" set
   *   { "color" = "red" } => only matches records with "color" set to "red"
   *   { "color" = "red", "name" = "a"} => only matches records with name set to "a", and color set to "red"
   * </pre>
   * <p>
   * If the filter is not set ({@code null} or empty), it accepts all records.
   * <p>
   * This method returns the first matching record.
   *
   * @param filter        the filter.
   * @param resultHandler handler called when the lookup has been completed. When there are no matching record, the
   *                      operation succeed, but the async result has no result.
   */
  void getRecord(JsonObject filter, Handler<AsyncResult<Record>> resultHandler);

  /**
   * Lookups for a set of records. Unlike {@link #getRecord(JsonObject, Handler)}, this method returns all matching
   * records.
   *
   * @param filter        the filter - see {@link #getRecord(JsonObject, Handler)}
   * @param resultHandler handler called when the lookup has been completed. When there are no matching record, the
   *                      operation succeed, but the async result has an empty list as result.
   */
  void getRecords(JsonObject filter, Handler<AsyncResult<List<Record>>> resultHandler);

  /**
   * Updates the given record. The record must has been published, and has it's registration id set.
   *
   * @param record        the updated record
   * @param resultHandler handler called when the lookup has been completed.
   */
  void update(Record record, Handler<AsyncResult<Record>> resultHandler);

  /**
   * @return the set of service references retrieved by this discovery service.
   */
  Set<ServiceReference> bindings();
}
