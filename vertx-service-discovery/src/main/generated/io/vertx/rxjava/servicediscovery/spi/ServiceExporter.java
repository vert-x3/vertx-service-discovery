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
import java.util.List;
import io.vertx.servicediscovery.Record;

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

  public void onPublication() { 
    delegate.onPublication();
  }

  public void init(List<Record> records) { 
    delegate.init(records);
  }

  public void close() { 
    delegate.close();
  }


  public static ServiceExporter newInstance(io.vertx.servicediscovery.spi.ServiceExporter arg) {
    return arg != null ? new ServiceExporter(arg) : null;
  }
}
