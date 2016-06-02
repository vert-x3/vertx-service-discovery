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

import io.vertx.core.Vertx;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.impl.DiscoveryImpl;

/**
 * A class to simplify the implementation of service reference.
 * It stores the service object once retrieved. This class handles the synchronization, so callbacks are called with
 * the monitor lock to avoid concurrent accesses.
 *
 * @param <T> the type of service object
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public abstract class AbstractServiceReference<T> implements ServiceReference {

  private final ServiceDiscovery discovery;

  protected T service;

  private final Record record;

  protected final Vertx vertx;

  /**
   * Creates a new instance of {@link AbstractServiceReference}.
   *
   * @param vertx  the vert.x instance
   * @param record the service record
   */
  public AbstractServiceReference(Vertx vertx, ServiceDiscovery discovery, Record record) {
    this.record = record;
    this.discovery = discovery;
    this.vertx = vertx;
  }

  /**
   * Returns the service object. If not retrieved or released, it returns {@code null}.
   *
   * @param <X> the type of result.
   * @return the cached service object, {@code null} if none
   */
  @Override
  public synchronized <X> X cached() {
    return (X) service;
  }

  /**
   * Gets the service object. If not retrieved, call {@link #retrieve()}, otherwise returned the cached value.
   *
   * @param <X> the type of result
   * @return the service object
   */
  @Override
  public synchronized <X> X get() {
    if (service == null) {
      service = retrieve();
    }
    return cached();
  }

  /**
   * Method to implement to retrieve the service object. It can be a proxy creation, or a new client. This method is
   * called once, then the return is cached.
   *
   * @return the service object
   */
  protected abstract T retrieve();

  /**
   * Callback that let you cleanup the service object. This callback is only called if the service objects has been
   * retrieved.
   */
  protected void close() {
    // Do nothing by default.
  }

  @Override
  public Record record() {
    return record;
  }

  /**
   * If the service object has been retrieved, calls {@link #close} and release the reference. Otherwise, does nothing.
   */
  @Override
  public synchronized void release() {
    ((DiscoveryImpl) discovery).unbind(this);
    if (service != null) {
      close();
      service = null;
    }
  }
}
