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
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;

/**
 * The service importer allows integrate other discovery technologies with the Vert.x service discovery. It maps
 * entries from another technology to a {@link Record} and maps {@link Record} to a publication in this other
 * technology. The importer is one side of a service discovery bridge.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface ServiceImporter {

  /**
   * Starts the importer.
   *
   * @param vertx         the vertx instance
   * @param publisher     the service publisher instance
   * @param configuration the bridge configuration if any
   * @param future        a future on which the bridge must report the completion of the starting
   */
  void start(Vertx vertx, ServicePublisher publisher, JsonObject configuration,
             Promise<Void> future);

  /**
   * Closes the importer
   *
   * @param closeHandler the handle to be notified when importer is closed, may be {@code null}
   */
  default void close(Handler<Void> closeHandler) {
    closeHandler.handle(null);
  }

}
