package io.vertx.ext.circuitbreaker.impl;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.circuitbreaker.CircuitBreaker;
import io.vertx.ext.circuitbreaker.CircuitBreakerOptions;
import io.vertx.ext.circuitbreaker.CircuitBreakerState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class CircuitBreakerImplTest {
  private Vertx vertx;

  // TODO test with event bus

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
  }

  @After
  public void tearDown() {
    AtomicBoolean completed = new AtomicBoolean();
    vertx.close(ar -> completed.set(ar.succeeded()));
    await().untilAtomic(completed, is(true));
  }

  @Test
  public void testCreationWithDefault() {
    CircuitBreaker breaker = CircuitBreaker.create("name", vertx);
    assertThat(breaker.name()).isEqualTo("name");
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);
  }

  @Test
  public void testSynchronousOk() {
    CircuitBreaker breaker = CircuitBreaker.create("test", vertx, new CircuitBreakerOptions());
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);

    AtomicBoolean called = new AtomicBoolean();
    breaker.executeSynchronousBlock(v -> called.set(true));

    assertThat(called.get()).isTrue();
  }

  @Test
  public void testAsynchronousOk() {
    CircuitBreaker breaker = CircuitBreaker.create("test", vertx, new CircuitBreakerOptions());
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);

    AtomicBoolean called = new AtomicBoolean();
    breaker.executeAsynchronousCode(future -> {
      vertx.setTimer(100, l -> {
        called.set(true);
        future.complete();
      });
    });

    await().until(called::get);
  }

  @Test
  public void testOpenAndCloseHandler() {
    AtomicInteger spyOpen = new AtomicInteger();
    AtomicInteger spyClosed = new AtomicInteger();
    CircuitBreaker breaker = CircuitBreaker.create("name", vertx, new CircuitBreakerOptions().setResetTimeoutInMs(-1))
        .openHandler((v) -> spyOpen.incrementAndGet())
        .closeHandler((v) -> spyClosed.incrementAndGet());

    assertThat(spyOpen.get()).isEqualTo(0);
    assertThat(spyClosed.get()).isEqualTo(0);

    // First failure
    try {
      breaker.executeSynchronousBlock(v -> {
        throw new RuntimeException("oh no, but this is expected");
      });
    } catch (RuntimeException e) {
      // Ignore it
    }

    assertThat(spyOpen.get()).isEqualTo(0);
    assertThat(spyClosed.get()).isEqualTo(0);
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);

    for (int i = 1; i < CircuitBreakerOptions.DEFAULT_MAX_FAILURES; i++) {
      try {
        breaker.executeSynchronousBlock(v -> {
          throw new RuntimeException("oh no, but this is expected");
        });
      } catch (RuntimeException e) {
        // Ignore it
      }
    }
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.OPEN);
    assertThat(spyOpen.get()).isEqualTo(1);

    breaker.reset();
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);
    assertThat(spyOpen.get()).isEqualTo(1);
    assertThat(spyClosed.get()).isEqualTo(1);
  }

  @Test
  public void testExceptionOnSynchronousCode() {
    AtomicBoolean called = new AtomicBoolean(false);
    CircuitBreakerOptions options = new CircuitBreakerOptions().setResetTimeoutInMs(-1);
    CircuitBreaker breaker = CircuitBreaker.create("test", vertx, options)
        .fallbackHandler(v -> {
          called.set(true);
        });
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);

    for (int i = 0; i < options.getMaxFailures(); i++) {
      try {
        breaker.executeSynchronousBlock(v -> {
          throw new RuntimeException("oh no, but this is expected");
        });
      } catch (RuntimeException e) {
        // Ignore it
      }
    }
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.OPEN);
    assertThat(called.get()).isEqualTo(false);

    AtomicBoolean spy = new AtomicBoolean();
    breaker.executeSynchronousBlock(v -> {
      spy.set(true);
    });
    assertThat(spy.get()).isEqualTo(false);
    assertThat(called.get()).isEqualTo(true);
  }

  @Test
  public void testExceptionOnAsynchronousCode() {
    AtomicBoolean called = new AtomicBoolean(false);
    CircuitBreakerOptions options = new CircuitBreakerOptions().setResetTimeoutInMs(-1);
    CircuitBreaker breaker = CircuitBreaker.create("test", vertx, options)
        .fallbackHandler(v -> {
          called.set(true);
        });
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);

    for (int i = 0; i < options.getMaxFailures(); i++) {
      try {
        breaker.executeAsynchronousCode(future -> {
          throw new RuntimeException("oh no, but this is expected");
        });
      } catch (RuntimeException e) {
        // Ignore it
      }
    }
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.OPEN);
    assertThat(called.get()).isEqualTo(false);

    AtomicBoolean spy = new AtomicBoolean();
    breaker.executeSynchronousBlock(v -> {
      spy.set(true);
    });
    assertThat(spy.get()).isEqualTo(false);
    assertThat(called.get()).isEqualTo(true);
  }

  @Test
  public void testFailureOnAsynchronousCode() {
    AtomicBoolean called = new AtomicBoolean(false);
    CircuitBreakerOptions options = new CircuitBreakerOptions().setResetTimeoutInMs(-1);
    CircuitBreaker breaker = CircuitBreaker.create("test", vertx, options)
        .fallbackHandler(v -> {
          called.set(true);
        });
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);

    for (int i = 0; i < options.getMaxFailures(); i++) {
      breaker.executeAsynchronousCode(future -> {
        vertx.setTimer(100, l -> {
          future.fail("expected failure");
        });
      });
    }
    await().until(() -> breaker.state() == CircuitBreakerState.OPEN);
    assertThat(called.get()).isEqualTo(false);

    AtomicBoolean spy = new AtomicBoolean();
    breaker.executeAsynchronousCode(future -> {
      vertx.setTimer(100, l -> {
        future.fail("expected failure");
        spy.set(true);
      });
    });
    await().untilAtomic(called, is(true));
    assertThat(spy.get()).isEqualTo(false);
  }

  @Test
  public void testResetAttemptOnSynchronousCode() {
    AtomicBoolean called = new AtomicBoolean(false);
    CircuitBreakerOptions options = new CircuitBreakerOptions().setResetTimeoutInMs(100);
    CircuitBreaker breaker = CircuitBreaker.create("test", vertx, options)
        .fallbackHandler(v -> {
          called.set(true);
        });
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);

    for (int i = 0; i < options.getMaxFailures(); i++) {
      try {
        breaker.executeSynchronousBlock(v -> {
          throw new RuntimeException("oh no, but this is expected");
        });
      } catch (RuntimeException e) {
        // Ignore it
      }
    }
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.OPEN);
    assertThat(called.get()).isEqualTo(false);

    await().until(() -> breaker.state() == CircuitBreakerState.HALF_OPEN);

    AtomicBoolean spy = new AtomicBoolean();
    breaker.executeSynchronousBlock(v -> {
      spy.set(true);
    });
    assertThat(spy.get()).isEqualTo(true);
    assertThat(called.get()).isEqualTo(false);
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);
  }

  @Test
  public void testResetAttemptOnAsynchronousCode() {
    AtomicBoolean called = new AtomicBoolean(false);
    CircuitBreakerOptions options = new CircuitBreakerOptions().setResetTimeoutInMs(200);
    CircuitBreaker breaker = CircuitBreaker.create("test", vertx, options)
        .fallbackHandler(v -> {
          called.set(true);
        });
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);

    for (int i = 0; i < options.getMaxFailures(); i++) {
      breaker.executeAsynchronousCode(future -> {
        vertx.setTimer(100, l -> {
          future.fail("expected failure");
        });
      });
    }
    await().until(() -> breaker.state() == CircuitBreakerState.OPEN);
    assertThat(called.get()).isEqualTo(false);

    await().until(() -> breaker.state() == CircuitBreakerState.HALF_OPEN);

    AtomicBoolean spy = new AtomicBoolean();
    breaker.executeAsynchronousCode(future -> {
      vertx.setTimer(100, l -> {
        future.complete();
        spy.set(true);
      });
    });
    await().untilAtomic(spy, is(true));
    assertThat(called.get()).isEqualTo(false);
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);
  }

  @Test
  public void testResetAttemptThatFailsOnSynchronousCode() {
    AtomicBoolean called = new AtomicBoolean(false);
    CircuitBreakerOptions options = new CircuitBreakerOptions()
        .setResetTimeoutInMs(100)
        .setFallbackOnFailure(true);
    CircuitBreaker breaker = CircuitBreaker.create("test", vertx, options)
        .fallbackHandler(v -> {
          called.set(true);
        });
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);

    for (int i = 0; i < options.getMaxFailures(); i++) {
      try {
        breaker.executeSynchronousBlock(v -> {
          throw new RuntimeException("oh no, but this is expected");
        });
      } catch (RuntimeException e) {
        // Ignore it
      }
    }
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.OPEN);
    assertThat(called.get()).isEqualTo(true);

    await().until(() -> breaker.state() == CircuitBreakerState.HALF_OPEN);
    called.set(false);

    try {
      breaker.executeSynchronousBlock(v -> {
        throw new RuntimeException("oh no, but this is expected");
      });
    } catch (RuntimeException e) {
      // Ignore it
    }
    assertThat(called.get()).isEqualTo(true);
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.OPEN);
  }

  @Test
  public void testResetAttemptThatFailsOnAsynchronousCode() {
    AtomicBoolean called = new AtomicBoolean(false);
    CircuitBreakerOptions options = new CircuitBreakerOptions()
        .setResetTimeoutInMs(100)
        .setFallbackOnFailure(true);
    CircuitBreaker breaker = CircuitBreaker.create("test", vertx, options)
        .fallbackHandler(v -> {
          called.set(true);
        });
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);

    for (int i = 0; i < options.getMaxFailures(); i++) {
      breaker.executeAsynchronousCode(future -> {
        vertx.setTimer(100, l -> {
          future.fail("expected failure");
        });
      });
    }
    await().until(() -> breaker.state() == CircuitBreakerState.OPEN);
    assertThat(called.get()).isEqualTo(true);

    await().until(() -> breaker.state() == CircuitBreakerState.HALF_OPEN);
    called.set(false);

    breaker.executeAsynchronousCode(future -> {
      vertx.setTimer(10, l -> {
        future.fail("expected failure");
      });
    });
    await().untilAtomic(called, is(true));
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.OPEN);
  }


  @Test
  public void testTimeoutOnSynchronousCode() {
    AtomicBoolean called = new AtomicBoolean(false);
    CircuitBreakerOptions options = new CircuitBreakerOptions().setTimeoutInMs(100);
    CircuitBreaker breaker = CircuitBreaker.create("test", vertx, options)
        .fallbackHandler(v -> {
          called.set(true);
        });
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);

    for (int i = 0; i < options.getMaxFailures(); i++) {
      breaker.executeSynchronousBlock(v -> {
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      });
    }

    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.OPEN);
    assertThat(called.get()).isEqualTo(false);

    AtomicBoolean spy = new AtomicBoolean();
    breaker.executeSynchronousBlock(v -> {
      spy.set(true);
    });
    assertThat(spy.get()).isEqualTo(false);
    assertThat(called.get()).isEqualTo(true);
  }

  @Test
  public void testTimeoutOnSynchronousCodeWithFallbackCalled() {
    AtomicBoolean called = new AtomicBoolean(false);
    CircuitBreakerOptions options = new CircuitBreakerOptions().setTimeoutInMs(100)
        .setFallbackOnFailure(true);
    CircuitBreaker breaker = CircuitBreaker.create("test", vertx, options)
        .fallbackHandler(v -> {
          called.set(true);
        });
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);

    for (int i = 0; i < options.getMaxFailures(); i++) {
      breaker.executeSynchronousBlock(v -> {
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      });
    }

    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.OPEN);
    assertThat(called.get()).isEqualTo(true);

    AtomicBoolean spy = new AtomicBoolean();
    breaker.executeSynchronousBlock(v -> {
      spy.set(true);
    });
    assertThat(spy.get()).isEqualTo(false);
    assertThat(called.get()).isEqualTo(true);
  }

  @Test
  public void testTimeoutOnAsynchronousCode() {
    AtomicBoolean called = new AtomicBoolean(false);
    CircuitBreakerOptions options = new CircuitBreakerOptions()
        .setTimeoutInMs(100)
        .setResetTimeoutInMs(-1);
    CircuitBreaker breaker = CircuitBreaker.create("test", vertx, options)
        .fallbackHandler(v -> {
          called.set(true);
        });
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);

    for (int i = 0; i < options.getMaxFailures(); i++) {
      breaker.executeAsynchronousCode(future -> {
        // Do nothing with the future, this is a very bad thing.
      });
    }

    await().until(() -> breaker.state() == CircuitBreakerState.OPEN);
    assertThat(called.get()).isEqualTo(false);

    breaker.executeAsynchronousCode(future -> {
      // Do nothing with the future, this is a very bad thing.
    });
    // Immediate fallback
    assertThat(called.get()).isEqualTo(true);
  }

  @Test
  public void testResetAttemptOnTimeout() {
    AtomicBoolean called = new AtomicBoolean(false);
    AtomicBoolean hasBeenOpened = new AtomicBoolean(false);
    CircuitBreakerOptions options = new CircuitBreakerOptions()
        .setResetTimeoutInMs(100)
        .setTimeoutInMs(10)
        .setFallbackOnFailure(true);
    CircuitBreaker breaker = CircuitBreaker.create("test", vertx, options)
        .fallbackHandler(v -> {
          called.set(true);
        })
        .openHandler(v -> {
          hasBeenOpened.set(true);
        });
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);

    for (int i = 0; i < options.getMaxFailures(); i++) {
      breaker.executeAsynchronousCode(future -> {
        // Do nothing with the future, this is a very bad thing.
      });
    }
    await().untilAtomic(hasBeenOpened, is(true));
    assertThat(called.get()).isEqualTo(true);

    await().until(() -> breaker.state() == CircuitBreakerState.HALF_OPEN);
    called.set(false);

    breaker.executeAsynchronousCode(Future::complete);
    await().until(() -> breaker.state() == CircuitBreakerState.CLOSED);
    await().untilAtomic(called, is(false));
  }

  @Test
  public void testResetAttemptThatFailsOnTimeout() {
    AtomicBoolean called = new AtomicBoolean(false);
    AtomicBoolean hasBeenOpened = new AtomicBoolean(false);
    CircuitBreakerOptions options = new CircuitBreakerOptions()
        .setResetTimeoutInMs(100)
        .setTimeoutInMs(10)
        .setFallbackOnFailure(true);
    CircuitBreaker breaker = CircuitBreaker.create("test", vertx, options)
        .fallbackHandler(v -> {
          called.set(true);
        })
        .openHandler(v -> {
          hasBeenOpened.set(true);
        });
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);

    for (int i = 0; i < options.getMaxFailures(); i++) {
      breaker.executeAsynchronousCode(future -> {
        // Do nothing with the future, this is a very bad thing.
      });
    }
    await().untilAtomic(hasBeenOpened, is(true));
    assertThat(called.get()).isEqualTo(true);

    await().until(() -> breaker.state() == CircuitBreakerState.HALF_OPEN);
    called.set(false);

    breaker.executeAsynchronousCode(future -> {
      // Do nothing with the future, this is a very bad thing.
    });
    await().untilAtomic(called, is(true));
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.OPEN);
  }

  @Test
  public void testThatOnlyOneRequestIsCheckedInHalfOpen() {
    AtomicBoolean called = new AtomicBoolean(false);
    AtomicBoolean hasBeenOpened = new AtomicBoolean(false);
    CircuitBreakerOptions options = new CircuitBreakerOptions()
        .setResetTimeoutInMs(1000)
        .setFallbackOnFailure(true);
    CircuitBreaker breaker = CircuitBreaker.create("test", vertx, options)
        .fallbackHandler(v -> {
          called.set(true);
        })
        .openHandler(v -> {
          hasBeenOpened.set(true);
        });
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);

    for (int i = 0; i < options.getMaxFailures(); i++) {
      breaker.executeAsynchronousCode(future -> {
        future.fail("expected failure");
      });
    }
    await().untilAtomic(hasBeenOpened, is(true));
    assertThat(called.get()).isEqualTo(true);

    await().until(() -> breaker.state() == CircuitBreakerState.HALF_OPEN);
    called.set(false);

    AtomicInteger fallbackCalled = new AtomicInteger();
    for (int i = 0; i < options.getMaxFailures(); i++) {
      breaker.executeAsynchronousCodeWithFallback(future -> {
        vertx.setTimer(500, l -> {
          future.complete();
        });
      }, v -> {
        fallbackCalled.incrementAndGet();
      });
    }

    await().until(() -> breaker.state() == CircuitBreakerState.CLOSED);
    assertThat(fallbackCalled.get()).isEqualTo(options.getMaxFailures() - 1);

  }
}