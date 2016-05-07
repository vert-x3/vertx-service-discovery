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

import io.vertx.core.Vertx;
import io.vertx.ext.discovery.Record;
import io.vertx.ext.discovery.ServiceReference;

/**
 * A class to simplify the implementation of service reference.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public abstract class AbstractServiceReference<T> implements ServiceReference {

  protected T service;

  private final Record record;

  protected final Vertx vertx;

  public AbstractServiceReference(Vertx vertx, Record record) {
    this.record = record;
    this.vertx = vertx;
  }

  @Override
  public synchronized <X> X cached() {
    return (X) service;
  }

  @Override
  public synchronized <X> X get() {
    if (service == null) {
      service = retrieve();
    }
    return cached();
  }

  protected abstract T retrieve();

  protected void close() {
    // Do nothing by default.
  }

  @Override
  public Record record() {
    return record;
  }

  @Override
  public synchronized void release() {
    if (service != null) {
      close();
      service = null;
    }
  }
}
