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

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.impl.DefaultServiceDiscoveryBackend;

import java.util.List;

/**
 * By default the service discovery uses a distributed map to store the records. But this backend can be replaced. To
 * replace a backend implement this interface and configure the SPI to point to your implementation.
 * <p>
 * check {@link DefaultServiceDiscoveryBackend} for more details.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public interface ServiceDiscoveryBackend {

  /**
   * @return the name of the implementation, used to select the right one (when there are several implementation on
   * the classpath). By default it ruturns the class name of the implementation (as String).
   */
  default String name() {
    return this.getClass().getName();
  }

  /**
   * Initializes the backend.
   *
   * @param vertx             the vert.x instance
   * @param config            the configuration if any.
   */
  void init(Vertx vertx, JsonObject config);

  /**
   * Stores a record.
   *
   * @param record        the record
   * @param resultHandler the completion handler
   */
  void store(Record record, Handler<AsyncResult<Record>> resultHandler);

  /**
   * Removes a record.
   *
   * @param record        the record
   * @param resultHandler the completion handler
   */
  void remove(Record record, Handler<AsyncResult<Record>> resultHandler);

  /**
   * Removes a records based on its UUID.
   *
   * @param uuid          the uuid / registration id
   * @param resultHandler the completion handler
   */
  void remove(String uuid, Handler<AsyncResult<Record>> resultHandler);

  /**
   * Updates a record
   *
   * @param record        the record to update
   * @param resultHandler the completion handler
   */
  void update(Record record, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Gets all the records
   *
   * @param resultHandler the result handler
   */
  void getRecords(Handler<AsyncResult<List<Record>>> resultHandler);

  /**
   * Get the record with the given uuid.
   *
   * @param uuid          the uuid / registration id
   * @param resultHandler the result handler
   */
  void getRecord(String uuid, Handler<AsyncResult<Record>> resultHandler);

}
