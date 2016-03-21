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
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import io.vertx.ext.discovery.Record
/**
 * Once a consumer has chosen a service, it builds a {@link io.vertx.ext.discovery.groovy.ServiceReference} managing the binding with the chosen
 * service provider.
 * <p>
 * The reference lets the consumer:
 * * access the service (via a proxy or a client) with the {@link io.vertx.ext.discovery.groovy.ServiceReference#get} method
 * * release the reference - so the binding between the consumer and the provider is removed
*/
@CompileStatic
public class ServiceReference {
  private final def io.vertx.ext.discovery.ServiceReference delegate;
  public ServiceReference(Object delegate) {
    this.delegate = (io.vertx.ext.discovery.ServiceReference) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * @return the service record.
   * @return  (see <a href="../../../../../../../cheatsheet/Record.html">Record</a>)
   */
  public Map<String, Object> record() {
    def ret = (Map<String, Object>)InternalHelper.wrapObject(this.delegate.record()?.toJson());
    return ret;
  }
  /**
   * Gets the object to access the service. It can be a proxy, a client or whatever object. The type depends on the
   * service type and the server itself.
   * @return the object to access the service
   */
  public <T> T get() {
    // This cast is cleary flawed
    def ret = (T) InternalHelper.wrapObject(this.delegate.get());
    return ret;
  }
  /**
   * Releases the reference. Once released, the consumer must not use the reference anymore.
   */
  public void release() {
    this.delegate.release();
  }
}
