package io.vertx.ext.circuitbreaker;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Circuit breaker configuration options.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@DataObject(generateConverter = true)
public class CircuitBreakerOptions {

  /**
   * Default timeout.
   */
  public static final long DEFAULT_TIMEOUT_IN_MS = 10000L;

  /**
   * Default number of failures.
   */
  public static final int DEFAULT_MAX_FAILURES = 5;

  /**
   * Default value of the fallback on failure property.
   */
  private static final boolean DEFAULT_FALLBACK_ON_FAILURE = false;

  /**
   * Default time before it attempts to re-close the circuit (half-open state).
   */
  private static final long DEFAULT_RESET_TIMEOUT_IN_MS = 30000;

  private long timeoutInMs = DEFAULT_TIMEOUT_IN_MS;

  private int maxFailures = DEFAULT_MAX_FAILURES;

  private boolean fallbackOnFailure = DEFAULT_FALLBACK_ON_FAILURE;

  private long resetTimeout = DEFAULT_RESET_TIMEOUT_IN_MS;

  /**
   * Creates a new instance of {@link CircuitBreakerOptions} using the default values.
   */
  public CircuitBreakerOptions() {
    // Empty constructor
  }

  /**
   * Creates a new instance of {@link CircuitBreakerOptions} by copying the other instance.
   *
   * @param other the instance fo copy
   */
  public CircuitBreakerOptions(CircuitBreakerOptions other) {
    this.timeoutInMs = other.timeoutInMs;
    this.maxFailures = other.maxFailures;
    this.fallbackOnFailure = other.fallbackOnFailure;
  }

  /**
   * Creates a new instance of {@link CircuitBreakerOptions} from the given json object.
   *
   * @param json the json object
   */
  public CircuitBreakerOptions(JsonObject json) {
    this();
    CircuitBreakerOptionsConverter.fromJson(json, this);
  }

  /**
   * @return a json object representing the current configuration.
   */
  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    CircuitBreakerOptionsConverter.toJson(this, json);
    return json;
  }

  /**
   * @return the maximum number of failures before opening the circuit.
   */
  public int getMaxFailures() {
    return maxFailures;
  }

  /**
   * Sets the maximum number of failures before opening the circuit.
   *
   * @param maxFailures the number of failures.
   * @return the current {@link CircuitBreakerOptions} instance
   */
  public CircuitBreakerOptions setMaxFailures(int maxFailures) {
    this.maxFailures = maxFailures;
    return this;
  }

  /**
   * @return the configured timeout in milliseconds.
   */
  public long getTimeoutInMs() {
    return timeoutInMs;
  }

  /**
   * Sets the timeout in milliseconds. If an action is not completed before this timeout, the action is considered as
   * a failure.
   *
   * @param timeoutInMs the timeout, -1 to disable the timeout
   * @return the current {@link CircuitBreakerOptions} instance
   */
  public CircuitBreakerOptions setTimeoutInMs(long timeoutInMs) {
    this.timeoutInMs = timeoutInMs;
    return this;
  }

  /**
   * @return whether or not the fallback is executed on failures, even when the circuit is closed.
   */
  public boolean isFallbackOnFailure() {
    return fallbackOnFailure;
  }

  /**
   * Sets whether or not the fallback is executed on failure, even when the circuit is closed.
   *
   * @param fallbackOnFailure {@code true} to enable it.
   * @return the current {@link CircuitBreakerOptions} instance
   */
  public CircuitBreakerOptions setFallbackOnFailure(boolean fallbackOnFailure) {
    this.fallbackOnFailure = fallbackOnFailure;
    return this;
  }

  /**
   * @return the time in milliseconds before it attempts to re-close the circuit (by going to the half-open state).
   */
  public long getResetTimeoutInMs() {
    return resetTimeout;
  }

  /**
   * Sets the time in ms before it attempts to re-close the circuit (by going to the hal-open state). If the cricuit
   * is closed when the timeout is reached, nothing happens. {@code -1} disables this feature.
   *
   * @param resetTimeout the time in ms
   * @return the current {@link CircuitBreakerOptions} instance
   */
  public CircuitBreakerOptions setResetTimeoutInMs(long resetTimeout) {
    this.resetTimeout = resetTimeout;
    return this;
  }
}
