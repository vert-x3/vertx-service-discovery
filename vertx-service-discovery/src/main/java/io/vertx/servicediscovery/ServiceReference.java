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

import io.vertx.codegen.annotations.CacheReturn;
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
public interface ServiceReference extends AutoCloseable {

  /**
   * @return the service record.
   */
  @CacheReturn
  Record record();

  /**
   * Gets the object to access the service. It can be a proxy, a client or whatever object. The type depends on the
   * service type and the server itself. This method returns the Java version and primary facet of the object, use
   * {@link #getAs(Class)} to retrieve the polyglot instance of the object or another facet..
   *
   * @return the object to access the service
   */
  <T> T get();

  /**
   * Gets the object to access the service. It can be a proxy, a client or whatever object. The type depends on the
   * service type and the server itself. This method wraps the service object into the desired type.
   *
   * @param x   the type of object
   * @param <X> the type of object
   * @return the object to access the service wrapped to the given type
   */
  <X> X getAs(Class<X> x);

  /**
   * Gets the service object if already retrieved. It won't try to acquire the service object if not retrieved yet.
   * Unlike {@link #cached()}, this method return the warpped object to the desired (given) type.
   *
   * @param x   the type of object
   * @param <X> the type of object
   * @return the object, {@code null} if not yet retrieved
   */
  <X> X cachedAs(Class<X> x);

  /**
   * Gets the service object if already retrieved. It won't try to acquire the service object if not retrieved yet.
   *
   * @return the object, {@code null} if not yet retrieved
   */
  <T> T cached();

  /**
   * Releases the reference. Once released, the consumer must not use the reference anymore.
   * This method must be idempotent and defensive, as multiple call may happen.
   */
  void release();

  /**
   * Checks whether or not the service reference has the given service object.
   *
   * @param object the service object, must not be {@code null}
   * @return {@code true} if the service reference service object is equal to the given object, {@code false} otherwise.
   */
  boolean isHolding(Object object);
}
