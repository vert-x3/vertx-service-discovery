package io.vertx.ext.discovery;

/**
 * Service publication status.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public enum Status {

  /**
   * The service is published and is accessible.
   */
  UP,
  /**
   * The service has been withdrawn, it is not accessible anymore.
   */
  DOWN,
  /**
   * The service is still published, but not accessible (maintenance).
   */
  OUT_OF_SERVICE,
  /**
   * Unknown status.
   */
  UNKNOWN
}
