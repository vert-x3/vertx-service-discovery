package io.vertx.ext.circuitbreaker.impl;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.circuitbreaker.CircuitBreaker;
import io.vertx.ext.circuitbreaker.CircuitBreakerOptions;
import io.vertx.ext.circuitbreaker.CircuitBreakerState;
import io.vertx.ext.web.Router;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class CircuitBreakerWithHTTPTest {
  private Vertx vertx;
  private HttpServer http;
  private HttpClient client;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    Router router = Router.router(vertx);
    router.route(HttpMethod.GET, "/").handler(ctxt -> {
      ctxt.response().setStatusCode(200).end("hello");
    });
    router.route(HttpMethod.GET, "/error").handler(ctxt -> {
      ctxt.response().setStatusCode(500).end("failed !");
    });
    router.route(HttpMethod.GET, "/long").handler(ctxt -> {
      try {
        Thread.currentThread().sleep(2000);
      } catch (Exception e) {
        // Ignored.
      }
      ctxt.response().setStatusCode(200).end("hello");
    });

    AtomicBoolean done = new AtomicBoolean();
    http = vertx.createHttpServer().requestHandler(router::accept).listen(8080, ar -> {
      done.set(ar.succeeded());
    });

    await().untilAtomic(done, is(true));

    client = vertx.createHttpClient();
  }

  @After
  public void tearDown() {
    AtomicBoolean completed = new AtomicBoolean();
    http.close(ar -> {
      completed.set(true);
    });
    await().untilAtomic(completed, is(true));

    completed.set(false);
    vertx.close((v) -> completed.set(true));
    await().untilAtomic(completed, is(true));

    client.close();
  }

  @Test
  public void testOk() {
    CircuitBreaker breaker = CircuitBreaker.create("test", vertx, new CircuitBreakerOptions());
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);

    AtomicReference<HttpClientResponse> reference = new AtomicReference<>();
    breaker.executeSynchronousBlock(v -> {
      client.getNow(8080, "localhost", "/", reference::set);
    });

    await().untilAtomic(reference, is(not(nullValue())));

    assertThat(reference.get()).isNotNull();
    assertThat(reference.get().statusCode()).isEqualTo(200);

    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);
  }

  @Test
  public void testFailure() {
    CircuitBreakerOptions options = new CircuitBreakerOptions();
    CircuitBreaker breaker = CircuitBreaker.create("test", vertx, options);
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);

    AtomicInteger count = new AtomicInteger();


    for (int i = 0; i < options.getMaxFailures(); i++) {
      breaker.executeAsynchronousCode(future -> {
        client.getNow(8080, "localhost", "/error", response -> {
          if (response.statusCode() != 200) {
            future.fail("http error");
          } else {
            future.complete();
          }
          count.incrementAndGet();
        });
      });
    }

    await().untilAtomic(count, is(options.getMaxFailures()));
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.OPEN);

    AtomicBoolean spy = new AtomicBoolean();
    breaker.executeAsynchronousCodeWithFallback(future -> {
      client.getNow(8080, "localhost", "/error", response -> {
        if (response.statusCode() != 200) {
          future.fail("http error");
        } else {
          future.complete();
        }
      });
    }, v -> {
      spy.set(true);
    });

    await().untilAtomic(spy, is(true));
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.OPEN);

  }

  @Test
  public void testTimeout() {
    CircuitBreakerOptions options = new CircuitBreakerOptions().setTimeoutInMs(100).setMaxFailures(2);
    CircuitBreaker breaker = CircuitBreaker.create("test", vertx, options);
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.CLOSED);

    AtomicInteger count = new AtomicInteger();


    for (int i = 0; i < options.getMaxFailures(); i++) {
      breaker.executeAsynchronousCode(future -> {
        client.getNow(8080, "localhost", "/long", response -> {
          count.incrementAndGet();
          future.complete();
        });
      });
    }

    await().untilAtomic(count, is(options.getMaxFailures()));
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.OPEN);

    AtomicBoolean spy = new AtomicBoolean();
    breaker.executeAsynchronousCodeWithFallback(future -> {
      client.getNow(8080, "localhost", "/long", response -> {
        System.out.println("Got response");
        future.complete();
      });
    }, v -> {
      spy.set(true);
    });

    await().untilAtomic(spy, is(true));
    assertThat(breaker.state()).isEqualTo(CircuitBreakerState.OPEN);
  }


}