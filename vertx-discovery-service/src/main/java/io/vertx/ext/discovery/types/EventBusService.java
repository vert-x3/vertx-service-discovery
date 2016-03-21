package io.vertx.ext.discovery.types;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.discovery.DiscoveryService;
import io.vertx.ext.discovery.Record;
import io.vertx.ext.discovery.ServiceReference;
import io.vertx.ext.discovery.spi.ServiceType;
import io.vertx.ext.discovery.utils.ClassLoaderUtils;
import io.vertx.serviceproxy.ProxyHelper;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Implementation of {@link ServiceType} for event bus services (service proxies).
 * Consumers receive a service proxy to use the service.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class EventBusService implements ServiceType {

  /**
   * Name of the type.
   */
  public static final String TYPE = "eventbus-service-proxy";

  /**
   * Sugar method to creates a record for this type.
   * <p>
   * The java interface is added to the metadata in the `service.interface` key.
   *
   * @param name     the name of the service.
   * @param itf      the Java interface
   * @param address  the event bus address on which the service available
   * @param metadata the metadata
   * @return the created record
   */
  public static Record createRecord(String name, Class itf, String address, JsonObject metadata) {
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
        .setMetadata(meta.put("service.interface", itf.getName()))
        .setLocation(new JsonObject().put(Record.ENDPOINT, address));
  }

  /**
   * Same as {@link #createRecord(String, Class, String, JsonObject)} but without metadata.
   *
   * @param name    the name of the service
   * @param itf     the Java interface
   * @param address the event bus address on which the service available
   * @return the created record
   */
  public static Record createRecord(String name, Class itf, String address) {
    return createRecord(name, itf, address, null);
  }

  /**
   * Stores the service bindings, i.e. the service that has been acquired by the consumer using this instance of
   * event bus service type.
   */
  private static final Set<ServiceReference> BINDINGS = new ConcurrentHashSet<>();

  /**
   * Retrieves the bindings - for testing purpose only.
   *
   * @return a copy of the bindings.
   */
  static Set<ServiceReference> bindings() {
    return new LinkedHashSet<>(BINDINGS);
  }

  /**
   * Lookup for a service record and if found, retrieve it and return the service object (used to consume the service).
   * This is a convenient method to avoid explicit lookup and then retrieval of the service.
   *
   * @param vertx         the vert.x instance
   * @param discovery     the discovery service
   * @param filter        the filter to select the service
   * @param resultHandler the result handler
   * @param <T>           the service interface
   */
  public static <T> void get(Vertx vertx, DiscoveryService discovery, JsonObject filter, Handler<AsyncResult<T>>
      resultHandler) {
    discovery.getRecord(filter, ar -> {
      if (ar.failed()) {
        resultHandler.handle(Future.failedFuture(ar.cause()));
      } else {
        if (ar.result() == null) {
          resultHandler.handle(Future.failedFuture("Cannot find service matching with " + filter));
        } else {
          ServiceReference service = DiscoveryService.getServiceReference(vertx, ar.result());
          BINDINGS.add(service);
          resultHandler.handle(Future.succeededFuture(service.get()));
        }
      }
    });
  }

  /**
   * Lookup for a service record and if found, retrieve it and return the service object (used to consume the service).
   * This is a convenient method to avoid explicit lookup and then retrieval of the service. A filter based on the
   * request interface is used.
   *
   * @param vertx         the vert.x instance
   * @param discovery     the discovery service
   * @param itf           the service interface
   * @param resultHandler the result handler
   * @param <T>           the service interface
   */
  public static <T> void get(Vertx vertx, DiscoveryService discovery, Class<T> itf, Handler<AsyncResult<T>>
      resultHandler) {
    JsonObject filter = new JsonObject().put("service.interface", itf.getName());
    get(vertx, discovery, filter, resultHandler);
  }

  @Override
  public String name() {
    return TYPE;
  }

  @Override
  public ServiceReference get(Vertx vertx, Record record) {
    return new EventBusServiceReference(vertx, record);
  }

  /**
   * Convenient method to release a used service object.
   *
   * @param svcObject the service object
   */
  public static void release(Object svcObject) {
    for (ServiceReference svc : BINDINGS) {
      if (svc.get().equals(svcObject)) {
        BINDINGS.remove(svc);
        return;
      }
    }
  }

  /**
   * Implementation of {@link ServiceReference} for event bus service proxies.
   */
  private class EventBusServiceReference implements ServiceReference {

    private final Record record;
    private final Vertx vertx;
    private Object proxy;


    EventBusServiceReference(Vertx vertx, Record record) {
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
     * Build the service proxy and return it. If already built, it returns the cached one.
     *
     * @param <T> the service interface
     * @return the proxy
     */
    @Override
    public synchronized <T> T get() {
      if (proxy != null) {
        return (T) proxy;
      }

      String className = record.getMetadata().getString("service.interface");
      Objects.requireNonNull(className);
      Class<T> itf = ClassLoaderUtils.load(className, this.getClass().getClassLoader());
      if (itf == null) {
        throw new IllegalStateException("Cannot load class " + className);
      } else {
        T proxy = ProxyHelper.createProxy(itf, vertx, record.getLocation().getString(Record.ENDPOINT));
        this.proxy = proxy;
        return proxy;
      }
    }

    /**
     * Invalidates the proxy.
     */
    @Override
    public synchronized void release() {
      proxy = null;
    }
  }
}
