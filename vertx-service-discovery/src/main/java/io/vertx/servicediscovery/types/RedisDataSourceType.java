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
import io.vertx.redis.RedisClient;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.spi.ServiceType;

/**
 * Service type for Redis data source.
 *
 * @author <a href="http://www.sczyh30.com">Eric Zhao</a>
 */
@VertxGen
public interface RedisDataSourceType extends ServiceType<RedisClient> {
  @Override
  RedisClient getService(ServiceReference ref);
  @Override
  RedisClient cachedService(ServiceReference ref);
}
