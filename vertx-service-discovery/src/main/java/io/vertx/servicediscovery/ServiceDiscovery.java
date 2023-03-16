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

package io.vertx.servicediscovery;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.impl.DiscoveryImpl;
import io.vertx.servicediscovery.spi.ServiceExporter;
import io.vertx.servicediscovery.spi.ServiceImporter;
import io.vertx.servicediscovery.utils.ClassLoaderUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * Service Discovery main entry point.
 * <p>
 * The service discovery is an infrastructure that let you publish and find `services`. A `service` is a discoverable
 * functionality. It can be qualified by its type, metadata, and location. So a `service` can be a database, a
 * service proxy, a HTTP endpoint. It does not have to be a vert.x entity, but can be anything. Each service is
 * described by a {@link Record}.
 * <p>
 * The service discovery implements the interactions defined in the service-oriented computing. And to some extend,
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
 * Providers and consumers must create their own {@link ServiceDiscovery} instance. These instances are collaborating
 * in background (distributed structure) to keep the set of services in sync.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface ServiceDiscovery {

  /**
   * Creates an instance of {@link ServiceDiscovery}.
   *
   * @param vertx   the vert.x instance
   * @param options the discovery options
   * @return the created service discovery instance.
   */
  static ServiceDiscovery create(Vertx vertx, ServiceDiscoveryOptions options) {
    return new DiscoveryImpl(vertx, options);
  }

  /**
   * Creates a new instance of {@link ServiceDiscovery} using the default configuration.
   *
   * @param vertx the vert.x instance
   * @return the created instance
   */
  static ServiceDiscovery create(Vertx vertx) {
    return create(vertx, new ServiceDiscoveryOptions());
  }

  /**
   * Constant for type field of usage events.
   *
   * @see #EVENT_TYPE_BIND
   * @see #EVENT_TYPE_RELEASE
   */
  String EVENT_TYPE = "type";

  /**
   * Constant for event type `Bind`.
   *
   * @see #EVENT_TYPE
   */
  String EVENT_TYPE_BIND = "bind";

  /**
   * Constant for event type `Release`.
   *
   * @see #EVENT_TYPE
   */
  String EVENT_TYPE_RELEASE = "release";

  /**
   * Constant for record field of usage events.
   */
  String EVENT_RECORD = "record";

  /**
   * Constant for id field of usage events.
   */
  String EVENT_ID = "id";

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
   * Registers a discovery service importer. Importers let you integrate other discovery technologies in this service
   * discovery.
   *
   * @param importer          the service importer
   * @param configuration     the optional configuration
   * @return a future notified when the importer has finished its initialization and
   *                          initial imports
   */
  Future<Void> registerServiceImporter(ServiceImporter importer, JsonObject configuration);

  /**
   * Registers a discovery bridge. Exporters let you integrate other discovery technologies in this service
   * discovery.
   *
   * @param exporter          the service exporter
   * @param configuration     the optional configuration
   * @return a future notified when the exporter has been correctly initialized.
   */
  Future<Void> registerServiceExporter(ServiceExporter exporter, JsonObject configuration);

  /**
   * Closes the service discovery
   */
  void close();

  /**
   * Publishes a record.
   *
   * @param record        the record
   * @return a future notified when the operation has completed (successfully or not). In case of success,
   *                      the passed record has a registration id required to modify and un-register the service.
   */
  Future<Record> publish(Record record);


  /**
   * Un-publishes a record.
   *
   * @param id            the registration id
   * @return a future notified when the operation has completed (successfully or not).
   */
  Future<Void> unpublish(String id);

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
   * @return a future notified when the lookup has been completed. When there are no matching record, the
   *                      operation succeeds, but the async result has no result ({@code null}).
   */
  Future<@Nullable Record> getRecord(JsonObject filter);

  /**
   * Looks up for a single record by its registration {@code id}.
   * <p>
   * When there are no matching record, the operation succeeds, but the async result has no result ({@code null}).
   *
   * @param id            the registration id
   * @return a future notified when the lookup has been completed
   * @see Record#getRegistration()
   */
  Future<@Nullable Record> getRecord(String id);


  /**
   * Lookups for a single record.
   * <p>
   * The filter is a {@link Function} taking a {@link Record} as argument and returning a boolean. You should see it
   * as an {@code accept} method of a filter. This method return a record passing the filter.
   * <p>
   * This method only looks for records with a {@code UP} status.
   *
   * @param filter        the filter, must not be {@code null}. To return all records, use a function accepting all records
   * @return a future notified when the lookup has been completed. When there are no matching
   *                      record, the operation succeed, but the async result has no result.
   */
  Future<@Nullable Record> getRecord(Function<Record, Boolean> filter);

  /**
   * Lookups for a single record.
   * <p>
   * The filter is a {@link Function} taking a {@link Record} as argument and returning a boolean. You should see it
   * as an {@code accept} method of a filter. This method return a record passing the filter.
   * <p>
   * Unlike {@link #getRecord(Function)}, this method may accept records with a {@code OUT OF SERVICE}
   * status, if the {@code includeOutOfService} parameter is set to {@code true}.
   *
   * @param filter              the filter, must not be {@code null}. To return all records, use a function accepting all records
   * @param includeOutOfService whether or not the filter accepts  {@code OUT OF SERVICE} records
   * @return a future notified when the lookup has been completed. When there are no matching
   *                            record, the operation succeed, but the async result has no result.
   */
  Future<@Nullable Record> getRecord(Function<Record, Boolean> filter, boolean includeOutOfService);

  /**
   * Lookups for a set of records. Unlike {@link #getRecord(JsonObject)}, this method returns all matching
   * records.
   *
   * @param filter        the filter - see {@link #getRecord(JsonObject)}
   * @return a future notified when the lookup has been completed. When there are no matching record, the
   *                      operation succeed, but the async result has an empty list as result.
   */
  Future<List<Record>> getRecords(JsonObject filter);

  /**
   * Lookups for a set of records. Unlike {@link #getRecord(Function)}, this method returns all matching
   * records.
   * <p>
   * The filter is a {@link Function} taking a {@link Record} as argument and returning a boolean. You should see it
   * as an {@code accept} method of a filter. This method return a record passing the filter.
   * <p>
   * This method only looks for records with a {@code UP} status.
   *
   * @param filter        the filter, must not be {@code null}. To return all records, use a function accepting all records
   * @return a future notified when the lookup has been completed. When there are no matching record, the
   *                      operation succeed, but the async result has an empty list as result.
   */
  Future<List<Record>> getRecords(Function<Record, Boolean> filter);

  /**
   * Lookups for a set of records. Unlike {@link #getRecord(Function)}, this method returns all matching
   * records.
   * <p>
   * The filter is a {@link Function} taking a {@link Record} as argument and returning a boolean. You should see it
   * as an {@code accept} method of a filter. This method return a record passing the filter.
   * <p>
   * Unlike {@link #getRecords(Function)}, this method may accept records with a {@code OUT OF SERVICE}
   * status, if the {@code includeOutOfService} parameter is set to {@code true}.
   *
   * @param filter              the filter, must not be {@code null}. To return all records, use a function accepting all records
   * @param includeOutOfService whether the filter accepts  {@code OUT OF SERVICE} records
   * @return a future notified when the lookup has been completed. When there are no matching record, the
   *                            operation succeed, but the async result has an empty list as result.
   */
  Future<List<Record>> getRecords(Function<Record, Boolean> filter, boolean includeOutOfService);

  /**
   * Updates the given record. The record must has been published, and has it's registration id set.
   *
   * @param record        the updated record
   * @return a future notified when the lookup has been completed.
   */
  Future<Record> update(Record record);

  /**
   * @return the set of service references retrieved by this service discovery.
   */
  Set<ServiceReference> bindings();

  /**
   * @return the discovery options. Modifying the returned object would not update the discovery service
   * configuration. This object should be considered as read-only.
   */
  ServiceDiscoveryOptions options();

  /**
   * Release the service object retrieved using {@code get} methods from the service type interface.
   * It searches for the reference associated with the given object and release it.
   *
   * @param discovery the service discovery
   * @param svcObject the service object
   */
  static void releaseServiceObject(ServiceDiscovery discovery, Object svcObject) {
    Objects.requireNonNull(discovery);
    Objects.requireNonNull(svcObject);

    Object svc = ClassLoaderUtils.extractDelegate(svcObject);
    if (svc == null) {
      svc = svcObject;
    }

    Collection<ServiceReference> references = discovery.bindings();
    Object finalSvc = svc;
    references.stream().filter(ref -> ref.isHolding(finalSvc)).forEach(ServiceReference::release);
  }
}
