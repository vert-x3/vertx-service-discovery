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

package io.vertx.groovy.servicediscovery.spi;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import io.vertx.servicediscovery.Record
import io.vertx.core.Handler
/**
 * The service exporter allows integrate other discovery technologies with the Vert.x service discovery. It maps
 * entries from another technology to a  and maps  to a publication in this other
 * technology. The exporter is one side of a service discovery bridge.
*/
@CompileStatic
public class ServiceExporter {
  private final def io.vertx.servicediscovery.spi.ServiceExporter delegate;
  public ServiceExporter(Object delegate) {
    this.delegate = (io.vertx.servicediscovery.spi.ServiceExporter) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Notify a new record has been published, the record's registration can be used to uniquely
   * identify the record
   * @param record the record (see <a href="../../../../../../../cheatsheet/Record.html">Record</a>)
   */
  public void onPublish(Map<String, Object> record = [:]) {
    delegate.onPublish(record != null ? new io.vertx.servicediscovery.Record(io.vertx.lang.groovy.InternalHelper.toJsonObject(record)) : null);
  }
  /**
   * Notify an existing record has been updated, the record's registration can be used to uniquely
   * identify the record
   * @param record the record (see <a href="../../../../../../../cheatsheet/Record.html">Record</a>)
   */
  public void onUpdate(Map<String, Object> record = [:]) {
    delegate.onUpdate(record != null ? new io.vertx.servicediscovery.Record(io.vertx.lang.groovy.InternalHelper.toJsonObject(record)) : null);
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
   * @param closeHandler the handle to be notified when exporter is closed
   */
  public void close(Handler<Void> closeHandler) {
    delegate.close(closeHandler);
  }
}
