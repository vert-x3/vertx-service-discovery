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

package io.vertx.groovy.servicediscovery.types;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import io.vertx.groovy.servicediscovery.ServiceReference
import io.vertx.servicediscovery.spi.ServiceType
import io.vertx.groovy.core.http.HttpClient
/**
 *  for HTTP endpoint (REST api).
 * Consumers receive a HTTP client configured with the host and port of the endpoint.
*/
@CompileStatic
public class HttpEndpointType {
  private final def io.vertx.servicediscovery.types.HttpEndpointType delegate;
  public HttpEndpointType(Object delegate) {
    this.delegate = (io.vertx.servicediscovery.types.HttpEndpointType) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public HttpClient getService(ServiceReference ref) {
    def ret = InternalHelper.safeCreate(delegate.getService(ref != null ? (io.vertx.servicediscovery.ServiceReference)ref.getDelegate() : null), io.vertx.groovy.core.http.HttpClient.class);
    return ret;
  }
  public HttpClient cachedService(ServiceReference ref) {
    def ret = InternalHelper.safeCreate(delegate.cachedService(ref != null ? (io.vertx.servicediscovery.ServiceReference)ref.getDelegate() : null), io.vertx.groovy.core.http.HttpClient.class);
    return ret;
  }
}
