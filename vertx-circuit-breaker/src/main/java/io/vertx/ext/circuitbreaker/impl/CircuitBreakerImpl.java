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

package io.vertx.ext.circuitbreaker.impl;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.circuitbreaker.CircuitBreaker;
import io.vertx.ext.circuitbreaker.CircuitBreakerOptions;
import io.vertx.ext.circuitbreaker.CircuitBreakerState;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static io.vertx.ext.circuitbreaker.CircuitBreakerState.*;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class CircuitBreakerImpl implements CircuitBreaker {

  private static final Handler<Void> NOOP = (v) -> {
    // Nothing...
  };

  private final Vertx vertx;
  private final CircuitBreakerOptions options;
  private final String name;
  private final long periodicUpdateTask;

  private Handler<Void> openHandler = NOOP;
  private Handler<Void> halfOpenHandler = NOOP;
  private Handler<Void> closeHandler = NOOP;
  private Function fallback = null;

  private CircuitBreakerState state = CLOSED;
  private long failures = 0;
  private AtomicInteger passed = new AtomicInteger();


  public CircuitBreakerImpl(String name, Vertx vertx, CircuitBreakerOptions options) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(vertx);
    if (options == null) {
      this.options = new CircuitBreakerOptions();
    } else {
      this.options = new CircuitBreakerOptions(options);
    }
    this.vertx = vertx;
    this.name = name;
    sendUpdateOnEventBus();

    if (this.options.getNotificationPeriod() > 0) {
      this.periodicUpdateTask = vertx.setPeriodic(this.options.getNotificationPeriod(), l -> sendUpdateOnEventBus());
    } else {
      this.periodicUpdateTask = -1;
    }
  }

  @Override
  public CircuitBreaker close() {
    if (this.periodicUpdateTask != -1) {
      vertx.cancelTimer(this.periodicUpdateTask);
    }
    return this;
  }

  @Override
  public synchronized CircuitBreaker openHandler(Handler<Void> handler) {
    Objects.requireNonNull(handler);
    openHandler = handler;
    return this;
  }

  @Override
  public synchronized CircuitBreaker halfOpenHandler(Handler<Void> handler) {
    Objects.requireNonNull(handler);
    halfOpenHandler = handler;
    return this;
  }

  @Override
  public synchronized CircuitBreaker closeHandler(Handler<Void> handler) {
    Objects.requireNonNull(handler);
    closeHandler = handler;
    return this;
  }

  @Override
  public <T> CircuitBreaker fallback(Function<Throwable, T> handler) {
    Objects.requireNonNull(handler);
    fallback = handler;
    return this;
  }

  @Override
  public synchronized CircuitBreaker reset() {
    failures = 0;

    if (state == CLOSED) {
      // Do nothing else.
      return this;
    }

    state = CLOSED;
    closeHandler.handle(null);
    sendUpdateOnEventBus();
    return this;
  }

  private synchronized void sendUpdateOnEventBus() {
    String address = options.getNotificationAddress();
    if (address != null) {
      vertx.eventBus().publish(address, new JsonObject()
          .put("name", name)
          .put("state", state)
          .put("failures", failures)
          .put("node", vertx.isClustered() ? ((VertxInternal) vertx).getClusterManager().getNodeID() : "local"));
    }
  }

  @Override
  public synchronized CircuitBreaker open() {
    state = OPEN;
    openHandler.handle(null);
    sendUpdateOnEventBus();

    // Set up the attempt reset timer
    long period = options.getResetTimeout();
    if (period != -1) {
      vertx.setTimer(period, l -> attemptReset());
    }

    return this;
  }

  @Override
  public synchronized long failureCount() {
    return failures;
  }

  @Override
  public synchronized CircuitBreakerState state() {
    return state;
  }

  private synchronized CircuitBreaker attemptReset() {
    if (state == OPEN) {
      passed.set(0);
      state = HALF_OPEN;
      halfOpenHandler.handle(null);
      sendUpdateOnEventBus();
    }
    return this;
  }

  //TODO Change fallback to receive the reason

  @Override
  public <T> CircuitBreaker executeAndReportWithFallback(
      Future<T> userFuture,
      Handler<Future<T>> operation,
      Function<Throwable, T> fallback) {

    CircuitBreakerState currentState;
    synchronized (this) {
      currentState = state;
    }

    // this future object track the completion of the operation
    // it completes the userFuture when required
    // This future is marked as failed on operation failures and timeout.
    Future<T> operationResult = Future.future();
    operationResult.setHandler(event -> {
      if (event.failed()) {
        incrementFailures();
        if (options.isFallbackOnFailure()) {
          invokeFallback(event.cause(), userFuture, fallback);
        } else {
          userFuture.fail(event.cause());
        }
      } else {
        reset();
        userFuture.complete(event.result());
      }
      // Else the operation has been canceled because of a time out.
    });

    if (currentState == CLOSED) {
      executeOperation(operation, operationResult);
    } else if (currentState == OPEN) {
      // Fallback immediately
      invokeFallback(new RuntimeException("open circuit"), userFuture, fallback);
    } else if (currentState == HALF_OPEN) {
      if (passed.incrementAndGet() == 1) {
        operationResult.setHandler(event -> {
          if (event.failed()) {
            open();
            if (options.isFallbackOnFailure()) {
              invokeFallback(event.cause(), userFuture, fallback);
            } else {
              userFuture.fail(event.cause());
            }
          } else {
            reset();
            userFuture.complete(event.result());
          }
        });
        // Execute the operation
        executeOperation(operation, operationResult);
      } else {
        // Not selected, fallback.
        invokeFallback(new RuntimeException("open circuit"), userFuture, fallback);
      }
    }
    return this;
  }

  private <T> void invokeFallback(Throwable reason, Future<T> userFuture, Function<Throwable, T> fallback) {
    if (fallback == null) {
      // No fallback, mark the user future as failed.
      userFuture.fail(reason);
      return;
    }

    try {
      T apply = fallback.apply(reason);
      userFuture.complete(apply);
    } catch (Exception e) {
      userFuture.fail(e);
    }
  }

  private <T> void executeOperation(Handler<Future<T>> operation, Future<T> operationResult) {
    // Execute the operation
    if (options.getTimeout() != -1) {
      vertx.setTimer(options.getTimeout(), (l) -> {
        // Check if the operation has not already been completed
        if (!operationResult.isComplete()) {
          operationResult.fail("operation timeout");
        }
        // Else  Operation has completed
      });
    }
    try {
      operation.handle(operationResult);
    } catch (Throwable e) {
      if (! operationResult.isComplete()) {
        operationResult.fail(e);
      }
    }
  }

  @Override
  public <T> Future<T> executeWithFallback(Handler<Future<T>> operation, Function<Throwable, T> fallback) {
    Future<T> future = Future.future();
    executeAndReportWithFallback(future, operation, fallback);
    return future;
  }

  public <T> Future<T> execute(Handler<Future<T>> operation) {
    return executeWithFallback(operation, fallback);
  }

  @Override
  public <T> CircuitBreaker executeAndReport(Future<T> resultFuture, Handler<Future<T>> operation) {
    return executeAndReportWithFallback(resultFuture, operation, fallback);
  }

  @Override
  public String name() {
    return name;
  }

  private synchronized void incrementFailures() {
    failures++;
    if (failures >= options.getMaxFailures()) {
      if (state != OPEN) {
        open();
      } else {
        // No need to do it in the previous case, open() do it.
        // If open has been called, no need to send update, it will be done by the `open` method.
        sendUpdateOnEventBus();
      }
    } else {
      // Number of failure has changed, send update.
      sendUpdateOnEventBus();
    }
  }
}
