package io.vertx.ext.circuitbreaker;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.circuitbreaker.impl.CircuitBreakerImpl;

/**
 * An implementation of the circuit breaker pattern for Vert.x
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public interface CircuitBreaker {

  /**
   * Creates a new instance of {@link CircuitBreaker}.
   *
   * @param name    the name
   * @param vertx   the Vert.x instance
   * @param options the configuration option
   * @return the created instance
   */
  static CircuitBreaker create(String name, Vertx vertx, CircuitBreakerOptions options) {
    return new CircuitBreakerImpl(name, vertx, options);
  }

  /**
   * Creates a new instance of {@link CircuitBreaker}, with default options
   *
   * @param name  the name
   * @param vertx the Vert.x instance
   * @return the created instance
   */
  static CircuitBreaker create(String name, Vertx vertx) {
    return new CircuitBreakerImpl(name, vertx, new CircuitBreakerOptions());
  }

  /**
   * Sets a {@link Handler} invoked when the circuit breaker state switches to open.
   *
   * @param handler the handler, must not be {@code null}
   * @return the current {@link CircuitBreaker}
   */
  CircuitBreaker openHandler(Handler<Void> handler);

  /**
   * Sets a {@link Handler} invoked when the circuit breaker state switches to half-open.
   *
   * @param handler the handler, must not be {@code null}
   * @return the current {@link CircuitBreaker}
   */
  CircuitBreaker halfOpenHandler(Handler<Void> handler);

  /**
   * Sets a {@link Handler} invoked when the circuit breaker state switches to close.
   *
   * @param handler the handler, must not be {@code null}
   * @return the current {@link CircuitBreaker}
   */
  CircuitBreaker closeHandler(Handler<Void> handler);

  /**
   * Sets a {@link Handler} invoked when the bridge is open to handle the "request".
   *
   * @param handler the handler, must not be {@code null}
   * @return the current {@link CircuitBreaker}
   */
  CircuitBreaker fallbackHandler(Handler<Void> handler);

  /**
   * Resets the circuit breaker state (number of failure set to 0 and state set to closed).
   *
   * @return the current {@link CircuitBreaker}
   */
  CircuitBreaker reset();

  /**
   * Explicitly opens the circuit.
   *
   * @return the current {@link CircuitBreaker}
   */
  CircuitBreaker open();

  /**
   * @return the current state.
   */
  CircuitBreakerState state();

  /**
   * @return the current number of failures.
   */
  long failureCount();

  /**
   * Executes the given code with the control of the circuit breaker.
   *
   * @param code the code
   * @return the current {@link CircuitBreaker}
   */
  CircuitBreaker executeSynchronousBlock(Handler<Void> code);

  /**
   * Executes the given code with the control of the circuit breaker and use the given fallback is the circuit is open.
   *
   * @param code the code
   * @return the current {@link CircuitBreaker}
   */
  CircuitBreaker executeSynchronousCodeWithFallback(Handler<Void> code, Handler<Void> fallback);

  /**
   * Executes the given code with the control of the circuit breaker. The code is asynchronous. Completion is
   * detected using the given {@link Future}.
   *
   * @param code the code
   * @return the current {@link CircuitBreaker}
   */
  CircuitBreaker executeAsynchronousCode(Handler<Future> code);

  /**
   * Executes the given code with the control of the circuit breaker. The code is asynchronous. Completion is
   * detected using the given {@link Future}. If the circuit is open, this method executes the given fallback.
   *
   * @param code the code
   * @return the current {@link CircuitBreaker}
   */
  CircuitBreaker executeAsynchronousCodeWithFallback(Handler<Future> code,
                                                     Handler<Void> fallback);


  /**
   * @return the name of the circuit breaker.
   */
  String name();
}
