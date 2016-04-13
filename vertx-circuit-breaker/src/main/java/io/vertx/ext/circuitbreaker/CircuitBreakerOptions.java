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

package io.vertx.ext.circuitbreaker;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Circuit breaker configuration options. All time are given in milliseconds.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@DataObject(generateConverter = true)
public class CircuitBreakerOptions {

  /**
   * Default timeout in milliseconds.
   */
  public static final long DEFAULT_TIMEOUT = 10000L;

  /**
   * Default number of failures.
   */
  public static final int DEFAULT_MAX_FAILURES = 5;

  /**
   * Default value of the fallback on failure property.
   */
  private static final boolean DEFAULT_FALLBACK_ON_FAILURE = false;

  /**
   * Default time before it attempts to re-close the circuit (half-open state) in milliseconds.
   */
  private static final long DEFAULT_RESET_TIMEOUT = 30000;

  /**
   * Default address on which the circuit breakers are sending their update.
   */
  private static final String DEFAULT_NOTIFICATION_ADDRESS = "vertx.circuit-breaker";

  /**
   * Default notification period  in milliseconds.
   */
  private static final long DEFAULT_NOTIFICATION_PERIOD = 2000;

  private long timeout = DEFAULT_TIMEOUT;

  private int maxFailures = DEFAULT_MAX_FAILURES;

  private boolean fallbackOnFailure = DEFAULT_FALLBACK_ON_FAILURE;

  private long resetTimeout = DEFAULT_RESET_TIMEOUT;

  private String notificationAddress = DEFAULT_NOTIFICATION_ADDRESS;

  private long notificationPeriod = DEFAULT_NOTIFICATION_PERIOD;

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
    this.timeout = other.timeout;
    this.maxFailures = other.maxFailures;
    this.fallbackOnFailure = other.fallbackOnFailure;
    this.notificationAddress = other.notificationAddress;
    this.notificationPeriod = other.notificationPeriod;
    this.resetTimeout = other.resetTimeout;
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
  public long getTimeout() {
    return timeout;
  }

  /**
   * Sets the timeout in milliseconds. If an action is not completed before this timeout, the action is considered as
   * a failure.
   *
   * @param timeoutInMs the timeout, -1 to disable the timeout
   * @return the current {@link CircuitBreakerOptions} instance
   */
  public CircuitBreakerOptions setTimeout(long timeoutInMs) {
    this.timeout = timeoutInMs;
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
  public long getResetTimeout() {
    return resetTimeout;
  }

  /**
   * Sets the time in ms before it attempts to re-close the circuit (by going to the hal-open state). If the cricuit
   * is closed when the timeout is reached, nothing happens. {@code -1} disables this feature.
   *
   * @param resetTimeout the time in ms
   * @return the current {@link CircuitBreakerOptions} instance
   */
  public CircuitBreakerOptions setResetTimeout(long resetTimeout) {
    this.resetTimeout = resetTimeout;
    return this;
  }

  /**
   * @return the eventbus address on which the circuit breaker events are published. {@code null} if this feature has
   * been disabled.
   */
  public String getNotificationAddress() {
    return notificationAddress;
  }

  /**
   * Sets the event bus address on which the circuit breaker publish its state change.
   *
   * @param notificationAddress the address, {@code null} to disable this feature.
   * @return the current {@link CircuitBreakerOptions} instance
   */
  public CircuitBreakerOptions setNotificationAddress(String notificationAddress) {
    this.notificationAddress = notificationAddress;
    return this;
  }

  /**
   * @return the the period in milliseconds where the circuit breaker send a notification about its state.
   */
  public long getNotificationPeriod() {
    return notificationPeriod;
  }

  /**
   * Configures the period in milliseconds where the circuit breaker send a notification on the event bus with its
   * current state.
   *
   * @param notificationPeriod the period, 0 to disable this feature.
   * @return the current {@link CircuitBreaker} instance
   */
  public CircuitBreakerOptions setNotificationPeriod(long notificationPeriod) {
    this.notificationPeriod = notificationPeriod;
    return this;
  }
}
