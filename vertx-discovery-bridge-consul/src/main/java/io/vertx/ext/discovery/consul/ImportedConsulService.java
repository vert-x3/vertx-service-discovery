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

package io.vertx.ext.discovery.consul;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.discovery.DiscoveryService;
import io.vertx.ext.discovery.Record;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ImportedConsulService {


  private final String name;
  private final Record record;

  public ImportedConsulService(String name, Record record) {
    this.name = name;
    this.record = record;
  }

  public String name() {
    return name;
  }

  public ImportedConsulService register(DiscoveryService discovery, Future<Void> completion) {
    discovery.publish(record, ar -> {
      if (ar.succeeded()) {
        record.setRegistration(ar.result().getRegistration());
        completion.complete();
      } else {
        completion.fail(ar.cause());
      }
    });
    return this;
  }

  public void unregister(DiscoveryService discovery, Handler<AsyncResult<Void>> resultHandler) {
    discovery.unpublish(record.getRegistration(), ar -> {
      if (ar.succeeded()) {
        record.setRegistration(null);
      }
      resultHandler.handle(ar);
    });
  }

}
