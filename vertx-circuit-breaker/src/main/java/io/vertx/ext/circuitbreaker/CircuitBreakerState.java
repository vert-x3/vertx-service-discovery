package io.vertx.ext.circuitbreaker;

/**
 * Circuit breaker states.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public enum CircuitBreakerState {
  OPEN,
  CLOSED,
  HALF_OPEN
}
