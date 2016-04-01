/*
 * Copyright (c) 2011-2016 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *      The Eclipse Public License is available at
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *      The Apache License v2.0 is available at
 *      http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.ext.circuitbreaker;

import io.vertx.codegen.annotations.CacheReturn;
import io.vertx.codegen.annotations.Fluent;
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
   * Closes the circuit breaker. It stops sending events on its state on the event bus.
   * This method is not related to the {@code close} state of the circuit breaker. To set the circuit breaker in the
   * {@code close} state, use {@link #reset()}.
   */
  @Fluent
  CircuitBreaker close();

  /**
   * Sets a {@link Handler} invoked when the circuit breaker state switches to open.
   *
   * @param handler the handler, must not be {@code null}
   * @return the current {@link CircuitBreaker}
   */
  @Fluent
  CircuitBreaker openHandler(Handler<Void> handler);

  /**
   * Sets a {@link Handler} invoked when the circuit breaker state switches to half-open.
   *
   * @param handler the handler, must not be {@code null}
   * @return the current {@link CircuitBreaker}
   */
  @Fluent
  CircuitBreaker halfOpenHandler(Handler<Void> handler);

  /**
   * Sets a {@link Handler} invoked when the circuit breaker state switches to close.
   *
   * @param handler the handler, must not be {@code null}
   * @return the current {@link CircuitBreaker}
   */
  @Fluent
  CircuitBreaker closeHandler(Handler<Void> handler);

  /**
   * Sets a {@link Handler} invoked when the bridge is open to handle the "request".
   *
   * @param handler the handler, must not be {@code null}
   * @return the current {@link CircuitBreaker}
   */
  @Fluent
  CircuitBreaker fallbackHandler(Handler<Void> handler);

  /**
   * Resets the circuit breaker state (number of failure set to 0 and state set to closed).
   *
   * @return the current {@link CircuitBreaker}
   */
  @Fluent
  CircuitBreaker reset();

  /**
   * Explicitly opens the circuit.
   *
   * @return the current {@link CircuitBreaker}
   */
  @Fluent
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
  @Fluent
  CircuitBreaker executeBlocking(Handler<Void> code);

  /**
   * Executes the given code with the control of the circuit breaker and use the given fallback is the circuit is open.
   *
   * @param code the code
   * @return the current {@link CircuitBreaker}
   */
  @Fluent
  CircuitBreaker executeBlockingWithFallback(Handler<Void> code, Handler<Void> fallback);

  /**
   * Executes the given code with the control of the circuit breaker. The code is asynchronous. Completion is
   * detected using the given {@link Future}.
   *
   * @param code the code
   * @return the current {@link CircuitBreaker}
   */
  @Fluent
  <T> CircuitBreaker execute(Handler<Future<T>> code);

  /**
   * Executes the given code with the control of the circuit breaker. The code is asynchronous. Completion is
   * detected using the given {@link Future}. If the circuit is open, this method executes the given fallback.
   *
   * @param code the code
   * @return the current {@link CircuitBreaker}
   */
  @Fluent
  <T> CircuitBreaker executeWithFallback(Handler<Future<T>> code,
                                         Handler<Void> fallback);

  /**
   * @return the name of the circuit breaker.
   */
  @CacheReturn
  String name();
}
