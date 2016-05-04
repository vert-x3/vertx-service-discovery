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

package io.vertx.ext.discovery.types.impl;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.discovery.Record;
import io.vertx.ext.discovery.ServiceReference;
import io.vertx.ext.discovery.types.DataSource;
import io.vertx.ext.discovery.types.JDBCDataSource;
import io.vertx.ext.jdbc.JDBCClient;

/**
 * The implementation of {@link JDBCDataSource}.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class JDBCDataSourceImpl implements JDBCDataSource {
  @Override
  public String name() {
    return DataSource.TYPE;
  }

  @Override
  public ServiceReference get(Vertx vertx, Record record, JsonObject config) {
    return new JdbcServiceReference(vertx, record, config);
  }

  /**
   * A reference on a JDBC data source. When retrieved it provides a {@link JDBCClient}. The _shared_ aspect of the
   * client depends on the {@code shared} flag put in the record's metadata (non shared by default).
   */
  private class JdbcServiceReference implements ServiceReference {
    private final Vertx vertx;
    private final Record record;
    private final JsonObject config;

    private JDBCClient client;

    JdbcServiceReference(Vertx vertx, Record record, JsonObject config) {
      this.record = record;
      this.vertx = vertx;
      this.config = config;
    }

    @Override
    public Record record() {
      return record;
    }

    @Override
    public synchronized <T> T get() {
      if (client == null) {
        JsonObject result = record.getMetadata().copy();
        result.mergeIn(record.getLocation());

        if (config != null) {
          result.mergeIn(config);
        }

        if (result.getBoolean("shared", false)) {
          client = JDBCClient.createShared(vertx, result);
        } else {
          client = JDBCClient.createNonShared(vertx, result);
        }
      }
      return (T) client;
    }

    @Override
    public synchronized void release() {
      if (client != null) {
        client.close();
      }
      client = null;
    }
  }
}
