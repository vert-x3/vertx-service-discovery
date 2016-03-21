package io.vertx.ext.discovery.spi;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.discovery.Record;

import java.util.List;

/**
 * By default the discovery service use a distributed map to store the records. But this backend can be replaced. To
 * replace a backend implement this interface and configure the SPI to point to your implementation.
 * <p>
 * check {@link io.vertx.ext.discovery.impl.DefaultDiscoveryBackend} for more details.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public interface DiscoveryBackend {

  /**
   * Initializes the backend.
   *
   * @param vertx the vert.x instance
   */
  void init(Vertx vertx);

  /**
   * Stores a record.
   *
   * @param record        the record
   * @param resultHandler the completion handler
   */
  void store(Record record, Handler<AsyncResult<Record>> resultHandler);

  /**
   * Removes a record.
   *
   * @param record        the record
   * @param resultHandler the completion handler
   */
  void remove(Record record, Handler<AsyncResult<Record>> resultHandler);

  /**
   * Removes a records based on its UUID.
   *
   * @param uuid          the uuid / registration id
   * @param resultHandler the completion handler
   */
  void remove(String uuid, Handler<AsyncResult<Record>> resultHandler);

  /**
   * Updates a record
   *
   * @param record        the record to update
   * @param resultHandler the completion handler
   */
  void update(Record record, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Gets all the records
   *
   * @param resultHandler the result handler
   */
  void getRecords(Handler<AsyncResult<List<Record>>> resultHandler);

  /**
   * Get the record with the given uuid.
   *
   * @param uuid          the uuid / registration id
   * @param resultHandler the result handler
   */
  void getRecord(String uuid, Handler<AsyncResult<Record>> resultHandler);

}
