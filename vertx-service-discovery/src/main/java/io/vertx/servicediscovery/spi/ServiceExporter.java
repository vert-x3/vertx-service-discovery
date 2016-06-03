package io.vertx.servicediscovery.spi;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.servicediscovery.Record;

import java.util.List;

/**
 * The service exporter allows integrate other discovery technologies with the Vert.x service discovery. It maps
 * entries from another technology to a {@link Record} and maps {@link Record} to a publication in this other
 * technology. The exporter is one side of a service discovery bridge.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface ServiceExporter {

  /**
   * Notify a new record has been published, the record's registration can be used to uniquely
   * identify the record
   *
   * @param record the record
   */
  void onPublish(Record record);

  /**
   * Notify an existing record has been updated, the record's registration can be used to uniquely
   * identify the record
   *
   * @param record the record
   */
  void onUpdate(Record record);

  /**
   * Notify an existing record has been removed
   *
   * @param id the record registration id
   */
  void onUnpublish(String id);

  /**
   * Close the exporter
   *
   * @param closeHandler the handle to be notified when exporter is closed
   */
  void close(Handler<Void> closeHandler);

}
