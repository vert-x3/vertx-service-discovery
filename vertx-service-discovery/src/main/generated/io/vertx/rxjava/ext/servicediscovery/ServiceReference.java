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

package io.vertx.rxjava.ext.servicediscovery;

import java.util.Map;
import rx.Observable;
import io.vertx.ext.servicediscovery.Record;

/**
 * Once a consumer has chosen a service, it builds a {@link io.vertx.rxjava.ext.servicediscovery.ServiceReference} managing the binding with the chosen
 * service provider.
 * <p>
 * The reference lets the consumer:
 * * access the service (via a proxy or a client) with the {@link io.vertx.rxjava.ext.servicediscovery.ServiceReference#get} method
 * * release the reference - so the binding between the consumer and the provider is removed
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.servicediscovery.ServiceReference original} non RX-ified interface using Vert.x codegen.
 */

public class ServiceReference {

  final io.vertx.ext.servicediscovery.ServiceReference delegate;

  public ServiceReference(io.vertx.ext.servicediscovery.ServiceReference delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * @return the service record.
   * @return 
   */
  public Record record() { 
    if (cached_0 != null) {
      return cached_0;
    }
    Record ret = delegate.record();
    cached_0 = ret;
    return ret;
  }

  /**
   * Gets the object to access the service. It can be a proxy, a client or whatever object. The type depends on the
   * service type and the server itself.
   * @return the object to access the service
   */
  public <T> T get() { 
    T ret = (T) delegate.get();
    return ret;
  }

  /**
   * Gets the service object if already retrieved. It won't try to acquire the service object if not retrieved yet.
   * @return the object, <code>null</code> if not yet retrieved
   */
  public <T> T cached() { 
    T ret = (T) delegate.cached();
    return ret;
  }

  /**
   * Releases the reference. Once released, the consumer must not use the reference anymore.
   * This method must be idempotent and defensive, as multiple call may happen.
   */
  public void release() { 
    delegate.release();
  }

  private Record cached_0;

  public static ServiceReference newInstance(io.vertx.ext.servicediscovery.ServiceReference arg) {
    return arg != null ? new ServiceReference(arg) : null;
  }
}
