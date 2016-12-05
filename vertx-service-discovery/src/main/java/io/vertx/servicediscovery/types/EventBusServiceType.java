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

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.http.HttpClient;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.spi.ServiceType;

/**
 * TODO
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface EventBusServiceType<T> extends ServiceType<T> {

  @Override
  T getService(ServiceReference<T> ref);

  @Override
  T cachedService(ServiceReference<T> ref);
}
