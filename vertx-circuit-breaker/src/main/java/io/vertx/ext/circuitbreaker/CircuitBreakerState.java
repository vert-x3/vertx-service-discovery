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

/**
 * Circuit breaker states.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public enum CircuitBreakerState {
  /**
   * The {@code OPEN} state. The circuit breaker is executing the fallback, and switches to the {@link #HALF_OPEN}
   * state after the specified time.
   */
  OPEN,
  /**
   * The {@code CLOSED} state. The circuit breaker lets invocations pass and collects the failures. IF the number of
   * failures reach the specified threshold, the cricuit breaker switches to the {@link #OPEN} state.
   */
  CLOSED,
  /**
   * The {@code HALF_OPEN} state. The circuit breaker has been opened, and is now checking the current situation. It
   * lets pass the next invocation and determines from the result (failure or success) if the circuit breaker can
   * be switched to the {@link #CLOSED} state again.
   */
  HALF_OPEN
}
