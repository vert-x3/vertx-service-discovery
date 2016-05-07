/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.ext.discovery.groovy;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import io.vertx.groovy.core.Vertx
import io.vertx.ext.discovery.Record
import java.util.Set
import java.util.List
import io.vertx.ext.discovery.DiscoveryOptions
import io.vertx.core.json.JsonObject
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
/**
 * Discovery service main entry point.
 * <p>
 * The discovery service is an infrastructure that let you publish and find `services`. A `service` is a discoverable
 * functionality. It can be qualified by its type, metadata, and location. So a `service` can be a database, a
 * service proxy, a HTTP endpoint. It does not have to be a vert.x entity, but can be anything. Each service is
 * described by a <a href="../../../../../../../cheatsheet/Record.html">Record</a>.
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
 * * bind to a selected service (it gets a {@link io.vertx.ext.discovery.groovy.ServiceReference}) and use it
 * * release the service once the consumer is done with it
 * * listen for arrival, departure and modification of services.
 * <p>
 * Consumer would 1) lookup for service record matching their need, 2) retrieve the {@link io.vertx.ext.discovery.groovy.ServiceReference} that give access
 * to the service, 3) get a service object to access the service, 4) release the service object once done.
 * <p>
 * A state above, the central piece of information shared by the providers and consumers are <a href="../../../../../../../cheatsheet/Record.html">Record</a>.
 * <p>
 * Providers and consumers must create their own {@link io.vertx.ext.discovery.groovy.DiscoveryService} instance. These instances are collaborating
 * in background (distributed structure) to keep the set of services in sync.
*/
@CompileStatic
public class DiscoveryService {
  private final def io.vertx.ext.discovery.DiscoveryService delegate;
  public DiscoveryService(Object delegate) {
    this.delegate = (io.vertx.ext.discovery.DiscoveryService) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Creates an instance of {@link io.vertx.ext.discovery.groovy.DiscoveryService}.
   * @param vertx the vert.x instance
   * @param options the discovery options (see <a href="../../../../../../../cheatsheet/DiscoveryOptions.html">DiscoveryOptions</a>)
   * @return the create discovery service.
   */
  public static DiscoveryService create(Vertx vertx, Map<String, Object> options) {
    def ret = InternalHelper.safeCreate(io.vertx.ext.discovery.DiscoveryService.create(vertx != null ? (io.vertx.core.Vertx)vertx.getDelegate() : null, options != null ? new io.vertx.ext.discovery.DiscoveryOptions(new io.vertx.core.json.JsonObject(options)) : null), io.vertx.ext.discovery.groovy.DiscoveryService.class);
    return ret;
  }
  /**
   * Creates a new instance of {@link io.vertx.ext.discovery.groovy.DiscoveryService} using the default configuration.
   * @param vertx the vert.x instance
   * @return the created instance
   */
  public static DiscoveryService create(Vertx vertx) {
    def ret = InternalHelper.safeCreate(io.vertx.ext.discovery.DiscoveryService.create(vertx != null ? (io.vertx.core.Vertx)vertx.getDelegate() : null), io.vertx.ext.discovery.groovy.DiscoveryService.class);
    return ret;
  }
  /**
   * Gets a service reference from the given record.
   * @param record the chosen record (see <a href="../../../../../../../cheatsheet/Record.html">Record</a>)
   * @return the service reference, that allows retrieving the service object. Once called the service reference is cached, and need to be released.
   */
  public ServiceReference getReference(Map<String, Object> record = [:]) {
    def ret = InternalHelper.safeCreate(delegate.getReference(record != null ? new io.vertx.ext.discovery.Record(new io.vertx.core.json.JsonObject(record)) : null), io.vertx.ext.discovery.groovy.ServiceReference.class);
    return ret;
  }
  /**
   * Gets a service reference from the given record, the reference is configured with the given json object.
   * @param record the chosen record (see <a href="../../../../../../../cheatsheet/Record.html">Record</a>)
   * @param configuration the configuration
   * @return the service reference, that allows retrieving the service object. Once called the service reference is cached, and need to be released.
   */
  public ServiceReference getReferenceWithConfiguration(Map<String, Object> record = [:], Map<String, Object> configuration) {
    def ret = InternalHelper.safeCreate(delegate.getReferenceWithConfiguration(record != null ? new io.vertx.ext.discovery.Record(new io.vertx.core.json.JsonObject(record)) : null, configuration != null ? new io.vertx.core.json.JsonObject(configuration) : null), io.vertx.ext.discovery.groovy.ServiceReference.class);
    return ret;
  }
  /**
   * Releases the service reference.
   * @param reference the reference to release, must not be <code>null</code>
   * @return whether or not the reference has been released.
   */
  public boolean release(ServiceReference reference) {
    def ret = delegate.release(reference != null ? (io.vertx.ext.discovery.ServiceReference)reference.getDelegate() : null);
    return ret;
  }
  /**
   * Closes the discovery service
   */
  public void close() {
    delegate.close();
  }
  /**
   * Publishes a record.
   * @param record the record (see <a href="../../../../../../../cheatsheet/Record.html">Record</a>)
   * @param resultHandler handler called when the operation has completed (successfully or not). In case of success, the passed record has a registration id required to modify and un-register the service.
   */
  public void publish(Map<String, Object> record = [:], Handler<AsyncResult<Map<String, Object>>> resultHandler) {
    delegate.publish(record != null ? new io.vertx.ext.discovery.Record(new io.vertx.core.json.JsonObject(record)) : null, resultHandler != null ? new Handler<AsyncResult<io.vertx.ext.discovery.Record>>() {
      public void handle(AsyncResult<io.vertx.ext.discovery.Record> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((Map<String, Object>)InternalHelper.wrapObject(ar.result()?.toJson())));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
  }
  /**
   * Un-publishes a record.
   * @param id the registration id
   * @param resultHandler handler called when the operation has completed (successfully or not).
   */
  public void unpublish(String id, Handler<AsyncResult<Void>> resultHandler) {
    delegate.unpublish(id, resultHandler);
  }
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
   * If the filter is not set (<code>null</code> or empty), it accepts all records.
   * <p>
   * This method returns the first matching record.
   * @param filter the filter.
   * @param resultHandler handler called when the lookup has been completed. When there are no matching record, the operation succeed, but the async result has no result.
   */
  public void getRecord(Map<String, Object> filter, Handler<AsyncResult<Map<String, Object>>> resultHandler) {
    delegate.getRecord(filter != null ? new io.vertx.core.json.JsonObject(filter) : null, resultHandler != null ? new Handler<AsyncResult<io.vertx.ext.discovery.Record>>() {
      public void handle(AsyncResult<io.vertx.ext.discovery.Record> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((Map<String, Object>)InternalHelper.wrapObject(ar.result()?.toJson())));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
  }
  /**
   * Lookups for a set of records. Unlike {@link io.vertx.ext.discovery.groovy.DiscoveryService#getRecord}, this method returns all matching
   * records.
   * @param filter the filter - see {@link io.vertx.ext.discovery.groovy.DiscoveryService#getRecord}
   * @param resultHandler handler called when the lookup has been completed. When there are no matching record, the operation succeed, but the async result has an empty list as result.
   */
  public void getRecords(Map<String, Object> filter, Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    delegate.getRecords(filter != null ? new io.vertx.core.json.JsonObject(filter) : null, resultHandler != null ? new Handler<AsyncResult<java.util.List<io.vertx.ext.discovery.Record>>>() {
      public void handle(AsyncResult<java.util.List<io.vertx.ext.discovery.Record>> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((List)ar.result()?.collect({(Map<String, Object>)InternalHelper.wrapObject(it?.toJson())})));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
  }
  /**
   * Updates the given record. The record must has been published, and has it's registration id set.
   * @param record the updated record (see <a href="../../../../../../../cheatsheet/Record.html">Record</a>)
   * @param resultHandler handler called when the lookup has been completed.
   */
  public void update(Map<String, Object> record = [:], Handler<AsyncResult<Map<String, Object>>> resultHandler) {
    delegate.update(record != null ? new io.vertx.ext.discovery.Record(new io.vertx.core.json.JsonObject(record)) : null, resultHandler != null ? new Handler<AsyncResult<io.vertx.ext.discovery.Record>>() {
      public void handle(AsyncResult<io.vertx.ext.discovery.Record> ar) {
        if (ar.succeeded()) {
          resultHandler.handle(io.vertx.core.Future.succeededFuture((Map<String, Object>)InternalHelper.wrapObject(ar.result()?.toJson())));
        } else {
          resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    } : null);
  }
  /**
   * @return the set of service references retrieved by this discovery service.
   * @return 
   */
  public Set<ServiceReference> bindings() {
    def ret = (Set)delegate.bindings()?.collect({InternalHelper.safeCreate(it, io.vertx.ext.discovery.groovy.ServiceReference.class)}) as Set;
    return ret;
  }
  /**
   * Release the service object retrieved using <code>get</code> methods from the service type interface.
   * It searches for the reference associated with the given object and release it.
   * @param discovery the discovery service
   * @param svcObject the service object
   */
  public static void releaseServiceObject(DiscoveryService discovery, Object svcObject) {
    io.vertx.ext.discovery.DiscoveryService.releaseServiceObject(discovery != null ? (io.vertx.ext.discovery.DiscoveryService)discovery.getDelegate() : null, svcObject != null ? InternalHelper.unwrapObject(svcObject) : null);
  }
}
