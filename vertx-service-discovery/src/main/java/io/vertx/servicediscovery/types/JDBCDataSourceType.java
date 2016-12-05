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
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.spi.ServiceType;

/**
 * TODO
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface JDBCDataSourceType extends ServiceType<JDBCClient> {

  @Override
  JDBCClient getService(ServiceReference<JDBCClient> ref);

  @Override
  JDBCClient cachedService(ServiceReference<JDBCClient> ref);
}
