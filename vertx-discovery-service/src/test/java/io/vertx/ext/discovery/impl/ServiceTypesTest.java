package io.vertx.ext.discovery.impl;

import io.vertx.ext.discovery.Record;
import io.vertx.ext.discovery.spi.ServiceType;
import org.junit.Test;

/**
 * Check the behavior of the service types.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ServiceTypesTest {


  @Test(expected = IllegalArgumentException.class)
  public void unknown() {
    Record record = new Record();
    record.setType(ServiceType.UNKNOWN);

    ServiceTypes.get(record);
  }

  @Test(expected = IllegalArgumentException.class)
  public void notAKnownType() {
    Record record = new Record();
    record.setType("bob");

    ServiceTypes.get(record);
  }

}