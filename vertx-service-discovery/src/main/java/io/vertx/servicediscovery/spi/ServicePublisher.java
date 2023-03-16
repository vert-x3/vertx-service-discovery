package io.vertx.servicediscovery.spi;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.servicediscovery.Record;

/**
 * The publisher is used by the importer to publish or unpublish records.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface ServicePublisher {

  /**
   * Publishes a record.
   *
   * @param record        the record
   * @return a future notified when the operation has completed (successfully or not). In case of success,
   *                      the passed record has a registration id required to modify and un-register the service.
   */
  Future<Record> publish(Record record);

  /**
   * Un-publishes a record.
   *
   * @param id            the registration id
   * @return a future notified when the operation has completed (successfully or not).
   */
  Future<Void> unpublish(String id);

  /**
   * Updates an existing record.
   *
   * @param record        the record
   * @return a future notified when the operation has completed (successfully or not). In case of success,
   *                      the passed record has a registration id required to modify and un-register the service.
   */
  Future<Record> update(Record record);

}
