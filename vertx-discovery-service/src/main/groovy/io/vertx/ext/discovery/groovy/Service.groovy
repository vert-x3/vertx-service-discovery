/*
 * Copyright (c) 2011-$tody.year The original author or authors
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

package io.vertx.ext.discovery.groovy;
import groovy.transform.CompileStatic
import io.vertx.ext.discovery.ServiceReference
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
*/
@CompileStatic
public class Service {
  private final def ServiceReference delegate;
  public Service(Object delegate) {
    this.delegate = (ServiceReference) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public <T> T get() {
    // This cast is cleary flawed
    def ret = (T) InternalHelper.wrapObject(this.delegate.get());
    return ret;
  }
  public void release() {
    this.delegate.release();
  }
}
