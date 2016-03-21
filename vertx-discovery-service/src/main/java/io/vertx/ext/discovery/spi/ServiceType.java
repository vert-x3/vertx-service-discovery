package io.vertx.ext.discovery.spi;

import io.vertx.core.Vertx;
import io.vertx.ext.discovery.Record;
import io.vertx.ext.discovery.ServiceReference;

/**
 * Represents a service type.
 * <p>
 * A service type is for example: `http-endpoint`, `data source`, `message source`. It
 * defines what kind of resources is accessed through the service. For example, in the case of a `http endpoint`, the
 * service object is a HTTP client. For `message source` it would a a consumer on which you set a handler receiving
 * the message.
 * <p>
 * You can define your own service type by implementing this interface and configure the SPI file
 * (META-INF/services/io.vertx.ext.discovery.spi.ServiceType) with your own implementation.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public interface ServiceType {

  /**
   * Unknown type.
   */
  String UNKNOWN = "unknown";

  /**
   * @return the name of the type.
   */
  String name();

  /**
   * Gets the `service` for the given record. The record's type must match the current type. From the
   * returned {@link ServiceReference}, the consumer can start using the service and release it.
   *
   * @param vertx  the vert.x instance
   * @param record the record
   * @return the retrieved {@link ServiceReference}
   */
  ServiceReference get(Vertx vertx, Record record);

}
