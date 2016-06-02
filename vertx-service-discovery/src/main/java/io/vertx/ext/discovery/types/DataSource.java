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

package io.vertx.ext.discovery.types;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.discovery.ServiceDiscovery;
import io.vertx.ext.discovery.Record;
import io.vertx.ext.discovery.spi.ServiceType;

import java.util.Objects;

/**
 * Parent interface for data source services. Data source services are databases. They have a "sub-type" indicating
 * the technology they use.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public interface DataSource extends ServiceType {

  String DS_TYPE = "datasource.type";

  String TYPE = "datasource";

  /**
   * Gets the metadata of a data source service. These metadata contains the location and the service metadata. This
   * method may be used to create a client to access the data source.
   *
   * @param discovery     the service discovery instance
   * @param filter        the filter
   * @param resultHandler the result handler, called with a failed result if no service matches.
   */
  static void getDataSourceMetadata(ServiceDiscovery discovery, JsonObject filter,
                                    Handler<AsyncResult<JsonObject>> resultHandler) {
    discovery.getRecord(filter, ar -> {
      if (ar.failed() || ar.result() == null) {
        resultHandler.handle(Future.failedFuture("No matching record"));
      } else {
        Record record = ar.result();
        JsonObject result = record.getMetadata().copy();
        result.mergeIn(record.getLocation());
        resultHandler.handle(Future.succeededFuture(result));
      }
    });
  }

  /**
   * Create a record representing a data source.
   *
   * @param name     the name of the service
   * @param type     the type of data source (MySQL, Redis, Mongo...)
   * @param location the location of the data source
   * @param metadata additional metadata
   * @return the created record
   */
  static Record createRecord(String name, String type, JsonObject location, JsonObject metadata) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(type);
    Objects.requireNonNull(location);

    Record record = new Record().setName(name)
        .setType(TYPE)
        .setLocation(location);

    if (metadata != null) {
      record.setMetadata(metadata);
    }

    record.setMetadata(new JsonObject().put(DS_TYPE, type));

    return record;
  }


}
