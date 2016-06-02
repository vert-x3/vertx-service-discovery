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

package io.vertx.ext.servicediscovery;

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
public interface ServiceReference {

  /**
   * @return the service record.
   */
  @CacheReturn
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
   * Gets the service object if already retrieved. It won't try to acquire the service object if not retrieved yet.
   *
   * @param <T> the type
   * @return the object, {@code null} if not yet retrieved
   */
  <T> T cached();

  /**
   * Releases the reference. Once released, the consumer must not use the reference anymore.
   * This method must be idempotent and defensive, as multiple call may happen.
   */
  void release();

}
