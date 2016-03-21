package io.vertx.ext.discovery;

import io.vertx.codegen.annotations.VertxGen;

/**
 * Once a consumer has chosen a service, it builds a {@link ServiceReference} managing the binding with the chosen
 * service provider.
 * <p>
 * The reference lets the consumer:
 * * access the service (via a proxy or a client) with the {@link #get()} method
 * * release the reference - so the binding between the consumer and the provider is removed
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface ServiceReference {

  /**
   * @return the service record.
   */
  Record record();

  /**
   * Gets the object to access the service. It can be a proxy, a client or whatever object. The type depends on the
   * service type and the server itself.
   *
   * @param <T> the type
   * @return the object to access the service
   */
  <T> T get();

  /**
   * Releases the reference. Once released, the consumer must not use the reference anymore.
   */
  void release();

}
