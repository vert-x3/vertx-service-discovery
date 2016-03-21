/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
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
