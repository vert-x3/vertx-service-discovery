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

package io.vertx.ext.service.groovy;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
*/
@CompileStatic
public class HelloService {
  private final def io.vertx.ext.service.HelloService delegate;
  public HelloService(Object delegate) {
    this.delegate = (io.vertx.ext.service.HelloService) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public void hello(String name, Handler<AsyncResult<String>> resultHandler) {
    this.delegate.hello(name, resultHandler);
  }
}
