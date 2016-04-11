/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
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
   * Creates a new instance of {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}, with default options.
   * @param name the name
   * @param vertx the Vert.x instance
   * @return the created instance
   */
  public static CircuitBreaker create(String name, Vertx vertx) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.circuitbreaker.CircuitBreaker.create(name, (io.vertx.core.Vertx)vertx.getDelegate()), io.vertx.ext.circuitbreaker.groovy.CircuitBreaker.class);
    return ret;
  }
  /**
   * Closes the circuit breaker. It stops sending events on its state on the event bus.
   * This method is not related to the <code>close</code> state of the circuit breaker. To set the circuit breaker in the
   * <code>close</code> state, use {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker#reset}.
   * @return 
   */
  public CircuitBreaker close() {
    this.delegate.close();
    return this;
  }
  /**
   * Sets a  invoked when the circuit breaker state switches to open.
   * @param handler the handler, must not be <code>null</code>
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public CircuitBreaker openHandler(Handler<Void> handler) {
    this.delegate.openHandler(handler);
    return this;
  }
  /**
   * Sets a  invoked when the circuit breaker state switches to half-open.
   * @param handler the handler, must not be <code>null</code>
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public CircuitBreaker halfOpenHandler(Handler<Void> handler) {
    this.delegate.halfOpenHandler(handler);
    return this;
  }
  /**
   * Sets a  invoked when the circuit breaker state switches to close.
   * @param handler the handler, must not be <code>null</code>
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public CircuitBreaker closeHandler(Handler<Void> handler) {
    this.delegate.closeHandler(handler);
    return this;
  }
  /**
   * Sets a  invoked when the bridge is open to handle the "request".
   * @param handler the handler, must not be <code>null</code>
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public CircuitBreaker fallbackHandler(Handler<Void> handler) {
    this.delegate.fallbackHandler(handler);
    return this;
  }
  /**
   * Resets the circuit breaker state (number of failure set to 0 and state set to closed).
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public CircuitBreaker reset() {
    this.delegate.reset();
    return this;
  }
  /**
   * Explicitly opens the circuit.
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public CircuitBreaker open() {
    this.delegate.open();
    return this;
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
   * Executes the given code with the control of the circuit breaker. The code is blocking. Failures are detected by
   * catching thrown exceptions or timeout.
   *
   * Be aware that the code is called using the caller thread, so it may be the event loop. So, unlike the
   *  method using a <em>worker</em> to execute the code, this method
   * uses the caller thread.
   * @param code the code
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public CircuitBreaker executeBlocking(Handler<Void> code) {
    this.delegate.executeBlocking(code);
    return this;
  }
  /**
   * Executes the given code with the control of the circuit breaker and use the given fallback is the circuit is open.
   * The code is blocking. Failures are detected by catching thrown exceptions or timeout.
   *
   * Be aware that the code is called using the caller thread, so it may be the event loop. So, unlike the
   *  method using a <em>worker</em> to execute the code, this method
   * uses the caller thread.
   * @param code the code
   * @param fallback 
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public CircuitBreaker executeBlockingWithFallback(Handler<Void> code, Handler<Void> fallback) {
    this.delegate.executeBlockingWithFallback(code, fallback);
    return this;
  }
  /**
   * Executes the given code with the control of the circuit breaker. The code is non-blocking and reports the
   * completion (success, result, failure) with the given .
   *
   * Be aware that the code is called using the caller thread, so it may be the event loop.
   * @param code the code
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public <T> CircuitBreaker execute(Handler<Future<T>> code) {
    this.delegate.execute(new Handler<io.vertx.core.Future<java.lang.Object>>() {
      public void handle(io.vertx.core.Future<java.lang.Object> event) {
        code.handle(new io.vertx.groovy.core.Future(event));
      }
    });
    return this;
  }
  /**
   * Executes the given code with the control of the circuit breaker. The code is non-blocking and reports the
   * completion (success, result, failure) with the given .
   *
   * Be aware that the code is called using the caller thread, so it may be the event loop.
   *
   * If the circuit is open, this method executes the given fallback.
   * @param code the code
   * @param fallback 
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public <T> CircuitBreaker executeWithFallback(Handler<Future<T>> code, Handler<Void> fallback) {
    this.delegate.executeWithFallback(new Handler<io.vertx.core.Future<java.lang.Object>>() {
      public void handle(io.vertx.core.Future<java.lang.Object> event) {
        code.handle(new io.vertx.groovy.core.Future(event));
      }
    }, fallback);
    return this;
  }
  /**
   * @return the name of the circuit breaker.
   * @return 
   */
  public String name() {
    if (cached_0 != null) {
      return cached_0;
    }
    def ret = this.delegate.name();
    cached_0 = ret;
    return ret;
  }
  private String cached_0;
}
