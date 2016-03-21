package examples;

import io.vertx.core.Vertx;
import io.vertx.ext.circuitbreaker.CircuitBreaker;
import io.vertx.ext.circuitbreaker.CircuitBreakerOptions;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Examples {

  public void example1(Vertx vertx) {
    CircuitBreaker breaker = CircuitBreaker.create("my-circuit-breaker", vertx,
        new CircuitBreakerOptions()
            .setMaxFailures(5) // number of failure before opening the circuit
            .setTimeoutInMs(2000) // consider a failure if the operation does not succeed in time
            .setFallbackOnFailure(true) // do we call the fallback on failure
            .setResetTimeoutInMs(10000) // time spent in open state before attempting to re-try
    );

    breaker.executeSynchronousBlock(v -> {
      // some code executing with the breaker
      // if this code fails, the breker increased the
      // number of failures
    });
  }

  public void example2(Vertx vertx) {
    CircuitBreaker breaker = CircuitBreaker.create("my-circuit-breaker", vertx,
        new CircuitBreakerOptions().setMaxFailures(5).setTimeoutInMs(2000)
    );

    breaker.executeAsynchronousCode(future -> {
      vertx.createHttpClient().getNow(8080, "localhost", "/", response -> {
        if (response.statusCode() != 200) {
          future.fail("HTTP error");
        } else {
          // Do something with the response
          future.complete();
        }
      });
    });
  }

  public void example3(Vertx vertx) {
    CircuitBreaker breaker = CircuitBreaker.create("my-circuit-breaker", vertx,
        new CircuitBreakerOptions().setMaxFailures(5).setTimeoutInMs(2000)
    );

    breaker.executeAsynchronousCodeWithFallback(
        future -> {
          vertx.createHttpClient().getNow(8080, "localhost", "/", response -> {
            if (response.statusCode() != 200) {
              future.fail("HTTP error");
            } else {
              // Do something with the response
              future.complete();
            }
          });
        }, v -> {
          // Executed when the circuit is opened
        });
  }

  public void example4(Vertx vertx) {
    CircuitBreaker breaker = CircuitBreaker.create("my-circuit-breaker", vertx,
        new CircuitBreakerOptions().setMaxFailures(5).setTimeoutInMs(2000)
    ).fallbackHandler(v -> {
      // Executed when the circuit is opened.
    });

    breaker.executeAsynchronousCode(
        future -> {
          vertx.createHttpClient().getNow(8080, "localhost", "/", response -> {
            if (response.statusCode() != 200) {
              future.fail("HTTP error");
            } else {
              // Do something with the response
              future.complete();
            }
          });
        });
  }

  public void example5(Vertx vertx) {
    CircuitBreaker breaker = CircuitBreaker.create("my-circuit-breaker", vertx,
        new CircuitBreakerOptions().setMaxFailures(5).setTimeoutInMs(2000)
    ).openHandler(v -> {
      System.out.println("Circuit opened");
    }).closeHandler(v -> {
      System.out.println("Circuit closed");
    });

    breaker.executeAsynchronousCode(
        future -> {
          vertx.createHttpClient().getNow(8080, "localhost", "/", response -> {
            if (response.statusCode() != 200) {
              future.fail("HTTP error");
            } else {
              // Do something with the response
              future.complete();
            }
          });
        });
  }
}
