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

/**
 * == Vert.x Circuit Breaker
 *
 * Vert.x Circuit Breaker is an implementation of the Circuit Breaker _pattern_ for Vert.x. It keeps track of the
 * number of failures and _open the circuit_ when a threshold is reached. Optionally, a fallback is executed.
 *
 * Supported failures are:
 *
 * * failures reported by your code in a {@link io.vertx.core.Future}
 * * exception thrown by your code
 * * uncompleted futures (timeout)
 *
 * Operations guarded by a circuit breaker are intended to by non-blocking and asynchronous in oter to benefits from
 * the Vert.x execution model.
 *
 * == Using the vert.x circuit breaker
 *
 * To use the Vert.x Circuit Breaker, add the following dependency to the _dependencies_ section of your build
 * descriptor:
 *
 * * Maven (in your `pom.xml`):
 *
 * [source,xml,subs="+attributes"]
 * ----
 * <dependency>
 *   <groupId>${maven.groupId}</groupId>
 *   <artifactId>${maven.artifactId}</artifactId>
 *   <version>${maven.version}</version>
 * </dependency>
 * ----
 *
 * * Gradle (in your `build.gradle` file):
 *
 * [source,groovy,subs="+attributes"]
 * ----
 * compile '${maven.groupId}:${maven.artifactId}:${maven.version}'
 * ----
 *
 * == Using the circuit breaker
 *
 * To use the circuit breaker you need to:
 *
 * 1. Create a circuit breaker, with the configuration you want (timeout, number of failure before opening the circuit)
 * 2. Execute some code using the breaker
 *
 * Here is an example:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#example1(io.vertx.core.Vertx)}
 * ----
 *
 * The executed block receives a {@link io.vertx.core.Future} object as parameter, to denote the
 * success or failure of the operation as well as the result. For example in the following example, the result is the
 * output of a REST endpoint invocation:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#example2(io.vertx.core.Vertx)}
 * ----
 *
 * The result of the operation is provided using the:
 *
 * * returned {@link io.vertx.core.Future} when calling `execute` methods
 * * provided {@link io.vertx.core.Future} when calling the `executeAndReport` methods
 *
 * Optionally, you can provide a fallback executed when the circuit is open:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#example3(io.vertx.core.Vertx)}
 * ----
 *
 * The fallback is called whenever the circuit is open, or if the
 * {@link io.vertx.ext.circuitbreaker.CircuitBreakerOptions#isFallbackOnFailure()} is enabled. When a fallback is
 * set, the result is using the output of the fallback function. The fallback function takes as parameter a
 * {@link java.lang.Throwable} object and returned an object of the expected type.
 *
 * The fallback can also be set on the {@link io.vertx.ext.circuitbreaker.CircuitBreaker} object directly:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#example4(io.vertx.core.Vertx)}
 * ----
 *
 * == Callbacks
 *
 * You can also configures callbacks invoked when the circuit is opened or closed:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#example5(io.vertx.core.Vertx)}
 * ----
 *
 * You can also be notified when the circuit breaker decide to attempt to reset (half-open state). You can register
 * such as callback with {@link io.vertx.ext.circuitbreaker.CircuitBreaker#halfOpenHandler(io.vertx.core.Handler)}.
 *
 * == Event bus notification
 *
 * Every time the circuit state changes, an event is published on the event bus. The address on which the event are
 * sent is configurable with
 * {@link io.vertx.ext.circuitbreaker.CircuitBreakerOptions#setNotificationAddress(java.lang.String)}. If `null` is
 * passed to this method, the notifications are disabled. By default, the used address is `vertx.circuit-breaker`.
 *
 * Each event contains a Json Object with:
 *
 * * `state` : the new circuit breaker state (`OPEN`, `CLOSED`, `HALF_OPEN`)
 * * `name` : the name of the circuit breaker
 * * `failures` : the number of failures
 * * `node` : the identifier of the node (`local` is Vert.x is not running in cluster mode)
 *
 * == The half-open state
 *
 * When the circuit is “open,” calls to the circuit breaker fail immediately, without any attempt to execute the real
 * operation. After a suitable amount of time (configured from
 * {@link io.vertx.ext.circuitbreaker.CircuitBreakerOptions#setResetTimeout(long)}, the circuit breaker decides that the
 * operation has a chance of succeeding, so it goes into the {@code half-open} state. In this state, the next call to the
 * circuit breaker is allowed to execute the dangerous operation. Should the call succeed, the circuit breaker resets
 * and returns to the {@code closed} state, ready for more routine operation. If this trial call fails, however, the circuit
 * breaker returns to the {@code open} state until another timeout elapses.
 *
 *
 * [language, java]
 * ----
 * == Using Netflix Hystrix
 *
 * https://github.com/Netflix/Hystrix[Hystrix] provides an implementation of the circuit breaker pattern. You can use
 * Hystrix with Vert.x instead of this circuit breaker or in combination of. This section describes the tricks
 * to use Hystrix in a vert.x application.
 *
 * First you would need to add the Hystrix dependency to your classpath or build descriptor. Refer to the Hystrix
 * page to details. Them, you need to isolate the "protected" call in a `Command`. Once you have your command, you
 * can execute it:
 *
 * [source, $lang]
 * \----
 * {@link examples.hystrix.HystrixExamples#exampleHystrix1()}
 * \----
 *
 * However, the command execution is blocking, so have to call the command execution either in an `executeBlocking`
 * block or in a worker verticle:
 *
 * [source, $lang]
 * \----
 * {@link examples.hystrix.HystrixExamples#exampleHystrix2(io.vertx.core.Vertx)}
 * \----
 *
 * If you use the async support of Hystrix, be careful that callbacks are not called in a vert.x thread and you have
 * to keep a reference on the context before the execution (with {@link io.vertx.core.Vertx#getOrCreateContext()},
 * and in the callback, switch back to the event loop using
 * {@link io.vertx.core.Vertx#runOnContext(io.vertx.core.Handler)}. Without this, you are loosing the Vert.x
 * concurrency model and have to manage the synchronization and ordering yourself:
 *
 * [source, $lang]
 * \----
 * {@link examples.hystrix.HystrixExamples#exampleHystrix3(io.vertx.core.Vertx)}
 * \----
 * ----
 *
 */
@ModuleGen(name = "vertx-circuit-breaker", groupPackage = "io.vertx.ext.circuitbreaker")
@Document
package io.vertx.ext.circuitbreaker;

import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.docgen.Document;
