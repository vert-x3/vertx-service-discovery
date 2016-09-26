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
import io.vertx.core.Handler;
import io.vertx.rxjava.core.Future;

/**
 * The service importer allows integrate other discovery technologies with the Vert.x service discovery. It maps
 * entries from another technology to a  and maps  to a publication in this other
 * technology. The importer is one side of a service discovery bridge.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.servicediscovery.spi.ServiceImporter original} non RX-ified interface using Vert.x codegen.
 */

public class ServiceImporter {

  final io.vertx.servicediscovery.spi.ServiceImporter delegate;

  public ServiceImporter(io.vertx.servicediscovery.spi.ServiceImporter delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Starts the importer.
   * @param vertx the vertx instance
   * @param publisher the service discovery instance
   * @param configuration the bridge configuration if any
   * @param future a future on which the bridge must report the completion of the starting
   */
  public void start(Vertx vertx, ServicePublisher publisher, JsonObject configuration, Future<Void> future) { 
    delegate.start((io.vertx.core.Vertx)vertx.getDelegate(), (io.vertx.servicediscovery.spi.ServicePublisher)publisher.getDelegate(), configuration, (io.vertx.core.Future<java.lang.Void>)future.getDelegate());
  }

  /**
   * Closes the importer
   * @param closeHandler the handle to be notified when importer is closed, may be <code>null</code>
   */
  public void close(Handler<Void> closeHandler) { 
    delegate.close(closeHandler);
  }


  public static ServiceImporter newInstance(io.vertx.servicediscovery.spi.ServiceImporter arg) {
    return arg != null ? new ServiceImporter(arg) : null;
  }
}
