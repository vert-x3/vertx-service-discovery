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

package io.vertx.ext.circuitbreaker.rxjava;

import java.util.Map;
import rx.Observable;
import io.vertx.ext.circuitbreaker.CircuitBreakerState;
import io.vertx.rxjava.core.Vertx;
import io.vertx.ext.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.Handler;
import io.vertx.rxjava.core.Future;

/**
 * An implementation of the circuit breaker pattern for Vert.x
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.circuitbreaker.CircuitBreaker original} non RX-ified interface using Vert.x codegen.
 */

public class CircuitBreaker {

  final io.vertx.ext.circuitbreaker.CircuitBreaker delegate;

  public CircuitBreaker(io.vertx.ext.circuitbreaker.CircuitBreaker delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Creates a new instance of {@link io.vertx.ext.circuitbreaker.rxjava.CircuitBreaker}.
   * @param name the name
   * @param vertx the Vert.x instance
   * @param options the configuration option
   * @return the created instance
   */
  public static CircuitBreaker create(String name, Vertx vertx, CircuitBreakerOptions options) { 
    CircuitBreaker ret = CircuitBreaker.newInstance(io.vertx.ext.circuitbreaker.CircuitBreaker.create(name, (io.vertx.core.Vertx)vertx.getDelegate(), options));
    return ret;
  }

  /**
   * Creates a new instance of {@link io.vertx.ext.circuitbreaker.rxjava.CircuitBreaker}, with default options.
   * @param name the name
   * @param vertx the Vert.x instance
   * @return the created instance
   */
  public static CircuitBreaker create(String name, Vertx vertx) { 
    CircuitBreaker ret = CircuitBreaker.newInstance(io.vertx.ext.circuitbreaker.CircuitBreaker.create(name, (io.vertx.core.Vertx)vertx.getDelegate()));
    return ret;
  }

  /**
   * Closes the circuit breaker. It stops sending events on its state on the event bus.
   * This method is not related to the <code>close</code> state of the circuit breaker. To set the circuit breaker in the
   * <code>close</code> state, use {@link io.vertx.ext.circuitbreaker.rxjava.CircuitBreaker#reset}.
   * @return 
   */
  public CircuitBreaker close() { 
    delegate.close();
    return this;
  }

  /**
   * Sets a  invoked when the circuit breaker state switches to open.
   * @param handler the handler, must not be <code>null</code>
   * @return the current {@link io.vertx.ext.circuitbreaker.rxjava.CircuitBreaker}
   */
  public CircuitBreaker openHandler(Handler<Void> handler) { 
    delegate.openHandler(new Handler<java.lang.Void>() {
      public void handle(java.lang.Void event) {
        handler.handle(event);
      }
    });
    return this;
  }

  /**
   * Sets a  invoked when the circuit breaker state switches to half-open.
   * @param handler the handler, must not be <code>null</code>
   * @return the current {@link io.vertx.ext.circuitbreaker.rxjava.CircuitBreaker}
   */
  public CircuitBreaker halfOpenHandler(Handler<Void> handler) { 
    delegate.halfOpenHandler(new Handler<java.lang.Void>() {
      public void handle(java.lang.Void event) {
        handler.handle(event);
      }
    });
    return this;
  }

  /**
   * Sets a  invoked when the circuit breaker state switches to close.
   * @param handler the handler, must not be <code>null</code>
   * @return the current {@link io.vertx.ext.circuitbreaker.rxjava.CircuitBreaker}
   */
  public CircuitBreaker closeHandler(Handler<Void> handler) { 
    delegate.closeHandler(new Handler<java.lang.Void>() {
      public void handle(java.lang.Void event) {
        handler.handle(event);
      }
    });
    return this;
  }

  /**
   * Sets a  invoked when the bridge is open to handle the "request".
   * @param handler the handler, must not be <code>null</code>
   * @return the current {@link io.vertx.ext.circuitbreaker.rxjava.CircuitBreaker}
   */
  public CircuitBreaker fallbackHandler(Handler<Void> handler) { 
    delegate.fallbackHandler(new Handler<java.lang.Void>() {
      public void handle(java.lang.Void event) {
        handler.handle(event);
      }
    });
    return this;
  }

  /**
   * Resets the circuit breaker state (number of failure set to 0 and state set to closed).
   * @return the current {@link io.vertx.ext.circuitbreaker.rxjava.CircuitBreaker}
   */
  public CircuitBreaker reset() { 
    delegate.reset();
    return this;
  }

  /**
   * Explicitly opens the circuit.
   * @return the current {@link io.vertx.ext.circuitbreaker.rxjava.CircuitBreaker}
   */
  public CircuitBreaker open() { 
    delegate.open();
    return this;
  }

  /**
   * @return the current state.
   * @return 
   */
  public CircuitBreakerState state() { 
    CircuitBreakerState ret = delegate.state();
    return ret;
  }

  /**
   * @return the current number of failures.
   * @return 
   */
  public long failureCount() { 
    long ret = delegate.failureCount();
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
   * @return the current {@link io.vertx.ext.circuitbreaker.rxjava.CircuitBreaker}
   */
  public CircuitBreaker executeBlocking(Handler<Void> code) { 
    delegate.executeBlocking(new Handler<java.lang.Void>() {
      public void handle(java.lang.Void event) {
        code.handle(event);
      }
    });
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
   * @return the current {@link io.vertx.ext.circuitbreaker.rxjava.CircuitBreaker}
   */
  public CircuitBreaker executeBlockingWithFallback(Handler<Void> code, Handler<Void> fallback) { 
    delegate.executeBlockingWithFallback(new Handler<java.lang.Void>() {
      public void handle(java.lang.Void event) {
        code.handle(event);
      }
    }, new Handler<java.lang.Void>() {
      public void handle(java.lang.Void event) {
        fallback.handle(event);
      }
    });
    return this;
  }

  /**
   * Executes the given code with the control of the circuit breaker. The code is non-blocking and reports the
   * completion (success, result, failure) with the given .
   *
   * Be aware that the code is called using the caller thread, so it may be the event loop.
   * @param code the code
   * @return the current {@link io.vertx.ext.circuitbreaker.rxjava.CircuitBreaker}
   */
  public <T> CircuitBreaker execute(Handler<Future<T>> code) { 
    delegate.execute(new Handler<io.vertx.core.Future<T>>() {
      public void handle(io.vertx.core.Future<T> event) {
        code.handle(Future.newInstance(event));
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
   * @return the current {@link io.vertx.ext.circuitbreaker.rxjava.CircuitBreaker}
   */
  public <T> CircuitBreaker executeWithFallback(Handler<Future<T>> code, Handler<Void> fallback) { 
    delegate.executeWithFallback(new Handler<io.vertx.core.Future<T>>() {
      public void handle(io.vertx.core.Future<T> event) {
        code.handle(Future.newInstance(event));
      }
    }, new Handler<java.lang.Void>() {
      public void handle(java.lang.Void event) {
        fallback.handle(event);
      }
    });
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
    String ret = delegate.name();
    cached_0 = ret;
    return ret;
  }

  private String cached_0;

  public static CircuitBreaker newInstance(io.vertx.ext.circuitbreaker.CircuitBreaker arg) {
    return arg != null ? new CircuitBreaker(arg) : null;
  }
}
