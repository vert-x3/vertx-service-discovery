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

package io.vertx.servicediscovery.consul;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.spi.ServicePublisher;

import java.util.Objects;

/**
 * Structure holding a service imported from Consul and published in the Vert.x service discovery.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ImportedConsulService {

  private final String name;
  private final Record record;
  private final String id;

  /**
   * Creates a new instance of {@link ImportedConsulService}.
   *
   * @param name   the service name
   * @param id     the service id, may be the name
   * @param record the record (not yet registered)
   */
  public ImportedConsulService(String name, String id, Record record) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(id);
    Objects.requireNonNull(record);
    this.name = name;
    this.record = record;
    this.id = id;
  }

  /**
   * @return the name
   */
  public String name() {
    return name;
  }

  /**
   * Registers the service and completes the given future when done.
   *
   * @param publisher  the service publisher instance
   * @param completion the completion future
   * @return the current {@link ImportedConsulService}
   */
  public ImportedConsulService register(ServicePublisher publisher, Promise<ImportedConsulService> completion) {
    publisher.publish(record).onComplete(ar -> {
      if (ar.succeeded()) {
        record.setRegistration(ar.result().getRegistration());
        completion.complete(this);
      } else {
        completion.fail(ar.cause());
      }
    });
    return this;
  }

  /**
   * Unregisters the service and completes the given future when done, if not {@code null}
   *
   * @param publiher  the service publisher instance
   * @param completion the completion future
   */
  public void unregister(ServicePublisher publiher, Promise<Void> completion) {
    if (record.getRegistration() != null) {
      publiher.unpublish(record.getRegistration()).onComplete(ar -> {
        if (ar.succeeded()) {
          record.setRegistration(null);
        }
        if (completion != null) {
          completion.complete();
        }
      });
    } else {
      if (completion != null) {
        completion.fail("Record not published");
      }
    }
  }

  /**
   * @return the id
   */
  String id() {
    return id;
  }
}
