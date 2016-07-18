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
import io.vertx.servicediscovery.Record;
import io.vertx.core.Handler;
import io.vertx.rxjava.core.Future;

/**
 * The service exporter allows integrate other discovery technologies with the Vert.x service discovery. It maps
 * entries from another technology to a  and maps  to a publication in this other
 * technology. The exporter is one side of a service discovery bridge.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.servicediscovery.spi.ServiceExporter original} non RX-ified interface using Vert.x codegen.
 */

public class ServiceExporter {

  final io.vertx.servicediscovery.spi.ServiceExporter delegate;

  public ServiceExporter(io.vertx.servicediscovery.spi.ServiceExporter delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Starts the exporter.
   * @param vertx the vertx instance
   * @param publisher the service discovery instance
   * @param configuration the bridge configuration if any
   * @param future a future on which the bridge must report the completion of the starting
   */
  public void init(Vertx vertx, ServicePublisher publisher, JsonObject configuration, Future<Void> future) { 
    delegate.init((io.vertx.core.Vertx)vertx.getDelegate(), (io.vertx.servicediscovery.spi.ServicePublisher)publisher.getDelegate(), configuration, (io.vertx.core.Future<java.lang.Void>)future.getDelegate());
  }

  /**
   * Notify a new record has been published, the record's registration can be used to uniquely
   * identify the record
   * @param record the record
   */
  public void onPublish(Record record) { 
    delegate.onPublish(record);
  }

  /**
   * Notify an existing record has been updated, the record's registration can be used to uniquely
   * identify the record
   * @param record the record
   */
  public void onUpdate(Record record) { 
    delegate.onUpdate(record);
  }

  /**
   * Notify an existing record has been removed
   * @param id the record registration id
   */
  public void onUnpublish(String id) { 
    delegate.onUnpublish(id);
  }

  /**
   * Close the exporter
   * @param closeHandler the handle to be notified when exporter is closed, may be <code>null</code>
   */
  public void close(Handler<Void> closeHandler) { 
    delegate.close(closeHandler);
  }


  public static ServiceExporter newInstance(io.vertx.servicediscovery.spi.ServiceExporter arg) {
    return arg != null ? new ServiceExporter(arg) : null;
  }
}
