package io.vertx.ext.circuitbreaker.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.circuitbreaker.CircuitBreaker;
import io.vertx.ext.circuitbreaker.CircuitBreakerOptions;
import io.vertx.ext.circuitbreaker.CircuitBreakerState;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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

  private Handler<Void> openHandler = NOOP;
  private Handler<Void> halfOpenHandler = NOOP;
  private Handler<Void> closeHandler = NOOP;
  private Handler<Void> fallback = NOOP;

  private CircuitBreakerState state = CLOSED;
  private long failures = 0;
  private AtomicInteger passed = new AtomicInteger();


  public CircuitBreakerImpl(String name, Vertx vertx, CircuitBreakerOptions options) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(vertx);
    if (options == null) {
      this.options = new CircuitBreakerOptions();
    } else {
      this.options = options;
    }
    this.vertx = vertx;
    this.name = name;
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
  public CircuitBreaker fallbackHandler(Handler<Void> handler) {
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

  private void sendUpdateOnEventBus() {
    vertx.eventBus().publish("circuit-breaker[" + name + "]", new JsonObject()
        .put("name", name)
        .put("state", state)
        .put("failures", failures)
        .put("node", vertx.isClustered() ? ((VertxInternal) vertx).getClusterManager().getNodeID() : "local"));
  }

  @Override
  public synchronized CircuitBreaker open() {
    state = OPEN;
    openHandler.handle(null);
    sendUpdateOnEventBus();

    // Set up the attempt reset timer
    long period = options.getResetTimeoutInMs();
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

  @Override
  public CircuitBreaker executeSynchronousBlock(Handler<Void> code) {
    return executeSynchronousCodeWithFallback(code, fallback);
  }

  @Override
  public CircuitBreaker executeSynchronousCodeWithFallback(Handler<Void> code, Handler<Void> fallback) {
    CircuitBreakerState currentState;
    synchronized (this) {
      currentState = state;
    }

    if (currentState == CLOSED) {
      executeCode(code);
    } else if (currentState == OPEN) {
      fallback.handle(null);
    } else if (currentState == HALF_OPEN) {
      if (passed.incrementAndGet() == 1) {
        try {
          code.handle(null);
          reset();
        } catch (Throwable e) {
          open();
          if (options.isFallbackOnFailure()) {
            fallback.handle(null);
          }
          throw e;
        }
      } else {
        fallback.handle(null);
      }
    }
    return this;
  }

  @Override
  public CircuitBreaker executeAsynchronousCode(Handler<Future> code) {
    return executeAsynchronousCodeWithFallback(code, fallback);
  }

  @Override
  public CircuitBreaker executeAsynchronousCodeWithFallback(Handler<Future> code, Handler<Void> fallback) {
    CircuitBreakerState currentState;
    synchronized (this) {
      currentState = state;
    }

    AtomicBoolean timeoutFailure = new AtomicBoolean();
    Future future = Future.future();
    future.setHandler(new Handler<AsyncResult>() {
      @Override
      public void handle(AsyncResult event) {
        if (event.failed()) {
          incrementFailures();
          if (options.isFallbackOnFailure()) {
            fallback.handle(null);
          }
        } else if (!timeoutFailure.get()) {
          reset();
        }
      }
    });

    if (currentState == CLOSED) {
      executeCode(code, future, timeoutFailure, fallback);
    } else if (currentState == OPEN) {
      fallback.handle(null);
    } else if (currentState == HALF_OPEN) {
      if (passed.incrementAndGet() == 1) {
        future.setHandler(new Handler<AsyncResult>() {
          @Override
          public void handle(AsyncResult result) {
            if (result.failed()) {
              open();
              if (options.isFallbackOnFailure()) {
                fallback.handle(null);
              }
            } else if (!timeoutFailure.get()) {
              reset();
            }
          }
        });
        executeCode(code, future, timeoutFailure, fallback);
      } else {
        // Not selected, fallback.
        fallback.handle(null);
      }
    }
    return this;
  }

  @Override
  public String name() {
    return name;
  }

  private void executeCode(Handler<Void> code) {
    AtomicBoolean completed = new AtomicBoolean(false);
    if (options.getTimeoutInMs() != -1) {
      vertx.setTimer(options.getTimeoutInMs(), (l) -> {
        // check it has completed or failed.
        if (!completed.get()) {
          incrementFailures();
          if (options.isFallbackOnFailure()) {
            fallback.handle(null);
          }
        }
      });
    }

    try {
      code.handle(null);
    } catch (Throwable e) {
      incrementFailures();
      if (options.isFallbackOnFailure()) {
        fallback.handle(null);
      }
      throw e;
    } finally {
      completed.set(true);
    }
  }

  private void executeCode(Handler<Future> code, Future completionFuture,
                           AtomicBoolean timeoutFailure, Handler<Void>
      fallback) {
    AtomicBoolean completed = new AtomicBoolean(false);
    if (options.getTimeoutInMs() != -1) {
      vertx.setTimer(options.getTimeoutInMs(), (l) -> {
        // check it has completed or failed.
        if (!completed.get() || (completionFuture != null && !completionFuture.isComplete())) {
          timeoutFailure.set(true);
          incrementFailures();
          if (options.isFallbackOnFailure()) {
            fallback.handle(null);
          }
        }
      });
    }

    try {
      code.handle(completionFuture);
    } catch (Throwable e) {
      incrementFailures();
      if (options.isFallbackOnFailure()) {
        fallback.handle(null);
      }
      throw e;
    } finally {
      completed.set(true);
    }
  }

  private synchronized void incrementFailures() {
    failures++;
    if (failures >= options.getMaxFailures()) {
      open();
    }
  }
}
