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

package io.vertx.rxjava.servicediscovery.spi;

import java.util.Map;
import rx.Observable;
import io.vertx.rxjava.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import io.vertx.rxjava.core.Future;

/**
 * Service Discovery bridge allows integrate other discovery technologies with the Vert.x service discovery. It maps
 * entries from another technology to a  and maps  to a publication in this other
 * technology. Each bridge can decide which services needs to be imported and exported. It can also implement only on
 * way.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.servicediscovery.spi.ServiceDiscoveryBridge original} non RX-ified interface using Vert.x codegen.
 */

public class ServiceDiscoveryBridge {

  final io.vertx.servicediscovery.spi.ServiceDiscoveryBridge delegate;

  public ServiceDiscoveryBridge(io.vertx.servicediscovery.spi.ServiceDiscoveryBridge delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Starts the bridge.
   * @param vertx the vertx instance
   * @param discovery the service discovery instance
   * @param configuration the bridge configuration if any
   * @param future a future on which the bridge must report the completion of the starting process
   */
  public void start(Vertx vertx, ServiceDiscovery discovery, JsonObject configuration, Future<Void> future) { 
    delegate.start((io.vertx.core.Vertx)vertx.getDelegate(), (io.vertx.servicediscovery.ServiceDiscovery)discovery.getDelegate(), configuration, (io.vertx.core.Future<java.lang.Void>)future.getDelegate());
  }

  /**
   * Stops the bridge.
   * @param vertx the vertx instance
   * @param discovery the service discovery instance
   * @param future the future on which the bridge must report the completion of the stopping process
   */
  public void stop(Vertx vertx, ServiceDiscovery discovery, Future<Void> future) { 
    delegate.stop((io.vertx.core.Vertx)vertx.getDelegate(), (io.vertx.servicediscovery.ServiceDiscovery)discovery.getDelegate(), (io.vertx.core.Future<java.lang.Void>)future.getDelegate());
  }


  public static ServiceDiscoveryBridge newInstance(io.vertx.servicediscovery.spi.ServiceDiscoveryBridge arg) {
    return arg != null ? new ServiceDiscoveryBridge(arg) : null;
  }
}
