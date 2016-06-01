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

package io.vertx.servicediscovery.spi;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.DiscoveryService;
import io.vertx.servicediscovery.Record;

/**
 * Discovery bridge allows integrate other discovery technologies with the Vert.x discovery service. It maps entries
 * from another technology to a {@link Record} and maps {@link Record} to a publication in this other technology.
 * Each bridge can decide which services needs to be imported and exported. It can also implement only on way.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface DiscoveryBridge {

  /**
   * Starts the bridge.
   *
   * @param vertx             the vertx instance
   * @param discovery         the discovery service
   * @param configuration     the bridge configuration if any
   * @param future            a future on which the bridge must report the completion of the starting
   *                          process
   */
  void start(Vertx vertx, DiscoveryService discovery, JsonObject configuration,
             Future<Void> future);

  /**
   * Stops the bridge.
   *
   * @param vertx     the vertx instance
   * @param discovery the discovery service
   * @param future    the future on which the bridge must report the completion of the stopping process
   */
  void stop(Vertx vertx, DiscoveryService discovery, Future<Void> future);

}
