/*
 * Copyright (c) 2011-$tody.year The original author or authors
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

package io.vertx.ext.circuitbreaker.groovy;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import io.vertx.ext.circuitbreaker.CircuitBreakerState
import io.vertx.groovy.core.Vertx
import io.vertx.ext.circuitbreaker.CircuitBreakerOptions
import io.vertx.core.Handler
import io.vertx.groovy.core.Future
/**
 * An implementation of the circuit breaker pattern for Vert.x
*/
@CompileStatic
public class CircuitBreaker {
  private final def io.vertx.ext.circuitbreaker.CircuitBreaker delegate;
  public CircuitBreaker(Object delegate) {
    this.delegate = (io.vertx.ext.circuitbreaker.CircuitBreaker) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Creates a new instance of {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}.
   * @param name the name
   * @param vertx the Vert.x instance
   * @param options the configuration option (see <a href="../../../../../../../cheatsheet/CircuitBreakerOptions.html">CircuitBreakerOptions</a>)
   * @return the created instance
   */
  public static CircuitBreaker create(String name, Vertx vertx, Map<String, Object> options) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.circuitbreaker.CircuitBreaker.create(name, (io.vertx.core.Vertx)vertx.getDelegate(), options != null ? new io.vertx.ext.circuitbreaker.CircuitBreakerOptions(new io.vertx.core.json.JsonObject(options)) : null), io.vertx.ext.circuitbreaker.groovy.CircuitBreaker.class);
    return ret;
  }
  /**
   * Creates a new instance of {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}, with default options
   * @param name the name
   * @param vertx the Vert.x instance
   * @return the created instance
   */
  public static CircuitBreaker create(String name, Vertx vertx) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.circuitbreaker.CircuitBreaker.create(name, (io.vertx.core.Vertx)vertx.getDelegate()), io.vertx.ext.circuitbreaker.groovy.CircuitBreaker.class);
    return ret;
  }
  /**
   * Sets a  invoked when the circuit breaker state switches to open.
   * @param handler the handler, must not be <code>null</code>
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public CircuitBreaker openHandler(Handler<Void> handler) {
    def ret= InternalHelper.safeCreate(this.delegate.openHandler(handler), io.vertx.ext.circuitbreaker.groovy.CircuitBreaker.class);
    return ret;
  }
  /**
   * Sets a  invoked when the circuit breaker state switches to half-open.
   * @param handler the handler, must not be <code>null</code>
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public CircuitBreaker halfOpenHandler(Handler<Void> handler) {
    def ret= InternalHelper.safeCreate(this.delegate.halfOpenHandler(handler), io.vertx.ext.circuitbreaker.groovy.CircuitBreaker.class);
    return ret;
  }
  /**
   * Sets a  invoked when the circuit breaker state switches to close.
   * @param handler the handler, must not be <code>null</code>
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public CircuitBreaker closeHandler(Handler<Void> handler) {
    def ret= InternalHelper.safeCreate(this.delegate.closeHandler(handler), io.vertx.ext.circuitbreaker.groovy.CircuitBreaker.class);
    return ret;
  }
  /**
   * Sets a  invoked when the bridge is open to handle the "request".
   * @param handler the handler, must not be <code>null</code>
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public CircuitBreaker fallbackHandler(Handler<Void> handler) {
    def ret= InternalHelper.safeCreate(this.delegate.fallbackHandler(handler), io.vertx.ext.circuitbreaker.groovy.CircuitBreaker.class);
    return ret;
  }
  /**
   * Resets the circuit breaker state (number of failure set to 0 and state set to closed).
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public CircuitBreaker reset() {
    def ret= InternalHelper.safeCreate(this.delegate.reset(), io.vertx.ext.circuitbreaker.groovy.CircuitBreaker.class);
    return ret;
  }
  /**
   * Explicitly opens the circuit.
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public CircuitBreaker open() {
    def ret= InternalHelper.safeCreate(this.delegate.open(), io.vertx.ext.circuitbreaker.groovy.CircuitBreaker.class);
    return ret;
  }
  /**
   * @return the current state.
   * @return 
   */
  public CircuitBreakerState state() {
    def ret = this.delegate.state();
    return ret;
  }
  /**
   * @return the current number of failures.
   * @return 
   */
  public long failureCount() {
    def ret = this.delegate.failureCount();
    return ret;
  }
  /**
   * Executes the given code with the control of the circuit breaker.
   * @param code the code
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public CircuitBreaker executeSynchronousBlock(Handler<Void> code) {
    def ret= InternalHelper.safeCreate(this.delegate.executeSynchronousBlock(code), io.vertx.ext.circuitbreaker.groovy.CircuitBreaker.class);
    return ret;
  }
  /**
   * Executes the given code with the control of the circuit breaker and use the given fallback is the circuit is open.
   * @param code the code
   * @param fallback 
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public CircuitBreaker executeSynchronousCodeWithFallback(Handler<Void> code, Handler<Void> fallback) {
    def ret= InternalHelper.safeCreate(this.delegate.executeSynchronousCodeWithFallback(code, fallback), io.vertx.ext.circuitbreaker.groovy.CircuitBreaker.class);
    return ret;
  }
  /**
   * Executes the given code with the control of the circuit breaker. The code is asynchronous. Completion is
   * detected using the given .
   * @param code the code
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public CircuitBreaker executeAsynchronousCode(Handler<Future> code) {
    def ret= InternalHelper.safeCreate(this.delegate.executeAsynchronousCode(new Handler<io.vertx.core.Future>() {
      public void handle(io.vertx.core.Future event) {
        code.handle(new io.vertx.groovy.core.Future(event));
      }
    }), io.vertx.ext.circuitbreaker.groovy.CircuitBreaker.class);
    return ret;
  }
  /**
   * Executes the given code with the control of the circuit breaker. The code is asynchronous. Completion is
   * detected using the given . If the circuit is open, this method executes the given fallback.
   * @param code the code
   * @param fallback 
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public CircuitBreaker executeAsynchronousCodeWithFallback(Handler<Future> code, Handler<Void> fallback) {
    def ret= InternalHelper.safeCreate(this.delegate.executeAsynchronousCodeWithFallback(new Handler<io.vertx.core.Future>() {
      public void handle(io.vertx.core.Future event) {
        code.handle(new io.vertx.groovy.core.Future(event));
      }
    }, fallback), io.vertx.ext.circuitbreaker.groovy.CircuitBreaker.class);
    return ret;
  }
  /**
   * @return the name of the circuit breaker.
   * @return 
   */
  public String name() {
    def ret = this.delegate.name();
    return ret;
  }
}
