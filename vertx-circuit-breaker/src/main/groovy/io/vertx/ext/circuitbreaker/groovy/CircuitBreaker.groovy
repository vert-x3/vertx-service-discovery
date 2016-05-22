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
import java.util.function.Function
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
    def ret = InternalHelper.safeCreate(io.vertx.ext.circuitbreaker.CircuitBreaker.create(name, vertx != null ? (io.vertx.core.Vertx)vertx.getDelegate() : null, options != null ? new io.vertx.ext.circuitbreaker.CircuitBreakerOptions(new io.vertx.core.json.JsonObject(options)) : null), io.vertx.ext.circuitbreaker.groovy.CircuitBreaker.class);
    return ret;
  }
  /**
   * Creates a new instance of {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}, with default options.
   * @param name the name
   * @param vertx the Vert.x instance
   * @return the created instance
   */
  public static CircuitBreaker create(String name, Vertx vertx) {
    def ret = InternalHelper.safeCreate(io.vertx.ext.circuitbreaker.CircuitBreaker.create(name, vertx != null ? (io.vertx.core.Vertx)vertx.getDelegate() : null), io.vertx.ext.circuitbreaker.groovy.CircuitBreaker.class);
    return ret;
  }
  /**
   * Closes the circuit breaker. It stops sending events on its state on the event bus.
   * This method is not related to the <code>close</code> state of the circuit breaker. To set the circuit breaker in the
   * <code>close</code> state, use {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker#reset}.
   * @return 
   */
  public CircuitBreaker close() {
    delegate.close();
    return this;
  }
  /**
   * Sets a  invoked when the circuit breaker state switches to open.
   * @param handler the handler, must not be <code>null</code>
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public CircuitBreaker openHandler(Handler<Void> handler) {
    delegate.openHandler(handler);
    return this;
  }
  /**
   * Sets a  invoked when the circuit breaker state switches to half-open.
   * @param handler the handler, must not be <code>null</code>
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public CircuitBreaker halfOpenHandler(Handler<Void> handler) {
    delegate.halfOpenHandler(handler);
    return this;
  }
  /**
   * Sets a  invoked when the circuit breaker state switches to close.
   * @param handler the handler, must not be <code>null</code>
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public CircuitBreaker closeHandler(Handler<Void> handler) {
    delegate.closeHandler(handler);
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
  public <T> Future<T> executeWithFallback(Handler<Future<T>> operation, java.util.function.Function<Throwable, T> fallback) {
    def ret = InternalHelper.safeCreate(delegate.executeWithFallback(operation != null ? new Handler<io.vertx.core.Future<java.lang.Object>>(){
      public void handle(io.vertx.core.Future<java.lang.Object> event) {
        operation.handle(InternalHelper.safeCreate(event, io.vertx.groovy.core.Future.class));
      }
    } : null, fallback != null ? new java.util.function.Function<java.lang.Throwable, java.lang.Object>(){
      public java.lang.Object apply(java.lang.Throwable arg_) {
        def ret = fallback.apply(arg_);
        return ret != null ? InternalHelper.unwrapObject(ret) : null;
      }
    } : null), io.vertx.groovy.core.Future.class);
    return ret;
  }
  /**
   * Same as {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker#executeWithFallback} but using the circuit breaker default fallback.
   * @param operation the operation
   * @return a future object completed when the operation or its fallback completes
   */
  public <T> Future<T> execute(Handler<Future<T>> operation) {
    def ret = InternalHelper.safeCreate(delegate.execute(operation != null ? new Handler<io.vertx.core.Future<java.lang.Object>>(){
      public void handle(io.vertx.core.Future<java.lang.Object> event) {
        operation.handle(InternalHelper.safeCreate(event, io.vertx.groovy.core.Future.class));
      }
    } : null), io.vertx.groovy.core.Future.class);
    return ret;
  }
  /**
   * Same as {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker#executeAndReportWithFallback} but using the circuit breaker default
   * fallback.
   * @param resultFuture the future on which the operation result is reported
   * @param operation the operation
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public <T> CircuitBreaker executeAndReport(Future<T> resultFuture, Handler<Future<T>> operation) {
    delegate.executeAndReport(resultFuture != null ? (io.vertx.core.Future<T>)resultFuture.getDelegate() : null, operation != null ? new Handler<io.vertx.core.Future<java.lang.Object>>(){
      public void handle(io.vertx.core.Future<java.lang.Object> event) {
        operation.handle(InternalHelper.safeCreate(event, io.vertx.groovy.core.Future.class));
      }
    } : null);
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
   * Unlike {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker#executeWithFallback},  this method does return a  object, but
   * let the caller pass a  object on which the result is reported. If the fallback is called, the future
   * is successfully completed with the value returned by the fallback function. If the fallback throws an exception,
   * the future is marked as failed.
   * @param resultFuture the future on which the operation result is reported
   * @param operation the operation
   * @param fallback the fallback function. It gets an exception as parameter and returns the <em>fallback</em> result
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public <T> CircuitBreaker executeAndReportWithFallback(Future<T> resultFuture, Handler<Future<T>> operation, java.util.function.Function<Throwable, T> fallback) {
    delegate.executeAndReportWithFallback(resultFuture != null ? (io.vertx.core.Future<T>)resultFuture.getDelegate() : null, operation != null ? new Handler<io.vertx.core.Future<java.lang.Object>>(){
      public void handle(io.vertx.core.Future<java.lang.Object> event) {
        operation.handle(InternalHelper.safeCreate(event, io.vertx.groovy.core.Future.class));
      }
    } : null, fallback != null ? new java.util.function.Function<java.lang.Throwable, java.lang.Object>(){
      public java.lang.Object apply(java.lang.Throwable arg_) {
        def ret = fallback.apply(arg_);
        return ret != null ? InternalHelper.unwrapObject(ret) : null;
      }
    } : null);
    return this;
  }
  /**
   * Sets a <em>default</em>  invoked when the bridge is open to handle the "request", or on failure
   * if <a href="../../../../../../../cheatsheet/CircuitBreakerOptions.html">CircuitBreakerOptions</a> is enabled.
   * <p>
   * The function gets the exception as parameter and returns the <em>fallback</em> result.
   * @param handler the handler
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public <T> CircuitBreaker fallback(java.util.function.Function<Throwable, T> handler) {
    delegate.fallback(handler != null ? new java.util.function.Function<java.lang.Throwable, java.lang.Object>(){
      public java.lang.Object apply(java.lang.Throwable arg_) {
        def ret = handler.apply(arg_);
        return ret != null ? InternalHelper.unwrapObject(ret) : null;
      }
    } : null);
    return this;
  }
  /**
   * Resets the circuit breaker state (number of failure set to 0 and state set to closed).
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
   */
  public CircuitBreaker reset() {
    delegate.reset();
    return this;
  }
  /**
   * Explicitly opens the circuit.
   * @return the current {@link io.vertx.ext.circuitbreaker.groovy.CircuitBreaker}
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
    def ret = delegate.state();
    return ret;
  }
  /**
   * @return the current number of failures.
   * @return 
   */
  public long failureCount() {
    def ret = delegate.failureCount();
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
    def ret = delegate.name();
    cached_0 = ret;
    return ret;
  }
  private String cached_0;
}
