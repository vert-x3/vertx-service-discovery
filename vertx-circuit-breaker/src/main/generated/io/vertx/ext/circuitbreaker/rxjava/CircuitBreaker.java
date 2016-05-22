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
import java.util.function.Function;

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
   * Executes the given operation with the circuit breaker control. The operation is generally calling an
   * <em>external</em> system. The operation receives a  object as parameter and <strong>must</strong>
   * call  when the operation has terminated successfully. The operation must also
   * call  in case of failure.
   * <p>
   * The operation is not invoked if the circuit breaker is open, and the given fallback is called immediately. The
   * circuit breaker also monitor the completion of the operation before a configure timeout. The operation is
   * considered as failed if it does not terminate in time.
   * <p>
   * This method returns a  object to retrieve the status and result of the operation, with the status
   * being a success or a failure. If the fallback is called, the returned future is successfully completed with the
   * value returned from the fallback. If the fallback throws an exception, the returned future is marked as failed.
   * @param operation the operation
   * @param fallback the fallback function. It gets an exception as parameter and returns the <em>fallback</em> result
   * @return a future object completed when the operation or its fallback completes
   */
  public <T> Future<T> executeWithFallback(Handler<Future<T>> operation, Function<Throwable,T> fallback) { 
    Future<T> ret = Future.newInstance(delegate.executeWithFallback(new Handler<io.vertx.core.Future<T>>() {
      public void handle(io.vertx.core.Future<T> event) {
        operation.handle(Future.newInstance(event));
      }
    }, new java.util.function.Function<java.lang.Throwable,T>() {
      public T apply(java.lang.Throwable arg) {
        T ret = fallback.apply(arg);
        return ret;
      }
    }));
    return ret;
  }

  /**
   * Same as {@link io.vertx.ext.circuitbreaker.rxjava.CircuitBreaker#executeWithFallback} but using the circuit breaker default fallback.
   * @param operation the operation
   * @return a future object completed when the operation or its fallback completes
   */
  public <T> Future<T> execute(Handler<Future<T>> operation) { 
    Future<T> ret = Future.newInstance(delegate.execute(new Handler<io.vertx.core.Future<T>>() {
      public void handle(io.vertx.core.Future<T> event) {
        operation.handle(Future.newInstance(event));
      }
    }));
    return ret;
  }

  /**
   * Same as {@link io.vertx.ext.circuitbreaker.rxjava.CircuitBreaker#executeAndReportWithFallback} but using the circuit breaker default
   * fallback.
   * @param resultFuture the future on which the operation result is reported
   * @param operation the operation
   * @return the current {@link io.vertx.ext.circuitbreaker.rxjava.CircuitBreaker}
   */
  public <T> CircuitBreaker executeAndReport(Future<T> resultFuture, Handler<Future<T>> operation) { 
    delegate.executeAndReport((io.vertx.core.Future<T>)resultFuture.getDelegate(), new Handler<io.vertx.core.Future<T>>() {
      public void handle(io.vertx.core.Future<T> event) {
        operation.handle(Future.newInstance(event));
      }
    });
    return this;
  }

  /**
   * Executes the given operation with the circuit breaker control. The operation is generally calling an
   * <em>external</em> system. The operation receives a  object as parameter and <strong>must</strong>
   * call  when the operation has terminated successfully. The operation must also
   * call  in case of failure.
   * <p>
   * The operation is not invoked if the circuit breaker is open, and the given fallback is called immediately. The
   * circuit breaker also monitor the completion of the operation before a configure timeout. The operation is
   * considered as failed if it does not terminate in time.
   * <p>
   * Unlike {@link io.vertx.ext.circuitbreaker.rxjava.CircuitBreaker#executeWithFallback},  this method does return a  object, but
   * let the caller pass a  object on which the result is reported. If the fallback is called, the future
   * is successfully completed with the value returned by the fallback function. If the fallback throws an exception,
   * the future is marked as failed.
   * @param resultFuture the future on which the operation result is reported
   * @param operation the operation
   * @param fallback the fallback function. It gets an exception as parameter and returns the <em>fallback</em> result
   * @return the current {@link io.vertx.ext.circuitbreaker.rxjava.CircuitBreaker}
   */
  public <T> CircuitBreaker executeAndReportWithFallback(Future<T> resultFuture, Handler<Future<T>> operation, Function<Throwable,T> fallback) { 
    delegate.executeAndReportWithFallback((io.vertx.core.Future<T>)resultFuture.getDelegate(), new Handler<io.vertx.core.Future<T>>() {
      public void handle(io.vertx.core.Future<T> event) {
        operation.handle(Future.newInstance(event));
      }
    }, new java.util.function.Function<java.lang.Throwable,T>() {
      public T apply(java.lang.Throwable arg) {
        T ret = fallback.apply(arg);
        return ret;
      }
    });
    return this;
  }

  /**
   * Sets a <em>default</em>  invoked when the bridge is open to handle the "request", or on failure
   * if {@link io.vertx.ext.circuitbreaker.CircuitBreakerOptions} is enabled.
   * <p>
   * The function gets the exception as parameter and returns the <em>fallback</em> result.
   * @param handler the handler
   * @return the current {@link io.vertx.ext.circuitbreaker.rxjava.CircuitBreaker}
   */
  public <T> CircuitBreaker fallback(Function<Throwable,T> handler) { 
    delegate.fallback(new java.util.function.Function<java.lang.Throwable,T>() {
      public T apply(java.lang.Throwable arg) {
        T ret = handler.apply(arg);
        return ret;
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
