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

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Some test to demonstrate how Hystrix can be used.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class HystrixTest {

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
        Thread.sleep(2000);
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

    client = vertx.createHttpClient(new HttpClientOptions().setDefaultPort(8080).setDefaultHost("localhost"));
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
  public void testOk() throws Exception {
    AtomicReference<String> result = new AtomicReference<>();

    vertx.runOnContext(v -> {

      // Blocking call..., need to run in an executeBlocking
      vertx.<String>executeBlocking(
          future -> {
            HttpClientCommand command = new HttpClientCommand(client, "/");
            future.complete(command.execute());
          },
          ar -> result.set(ar.result())
      );
    });

    await().until(() -> result.get() != null);
    assertThat(result.get()).isEqualToIgnoringCase("hello");

    result.set(null);
    vertx.runOnContext(v -> {

      // Blocking call..., need to run in an executeBlocking
      vertx.<String>executeBlocking(
          future -> {
            HttpClientCommand command = new HttpClientCommand(client, "/");
            try {
              future.complete(command.queue().get());
            } catch (Exception e) {
              future.fail(e);
            }
          },
          ar -> result.set(ar.result())
      );
    });

    await().until(() -> result.get() != null);
    assertThat(result.get()).isEqualToIgnoringCase("hello");
  }

  @Test
  public void testFailure() throws Exception {
    AtomicReference<String> result = new AtomicReference<>();

    vertx.runOnContext(v -> {

      // Blocking call..., need to run in an executeBlocking

      vertx.<String>executeBlocking(
          future -> {
            HttpClientCommand command = new HttpClientCommand(client, "/error");
            future.complete(command.execute());
          },
          ar -> result.set(ar.result())
      );
    });

    await().until(() -> result.get() != null);
    assertThat(result.get()).isEqualToIgnoringCase("fallback");

    result.set(null);
    vertx.runOnContext(v -> {

      // Blocking call..., need to run in an executeBlocking
      vertx.<String>executeBlocking(
          future -> {
            HttpClientCommand command = new HttpClientCommand(client, "/error");
            try {
              future.complete(command.queue().get());
            } catch (Exception e) {
              future.fail(e);
            }
          },
          ar -> result.set(ar.result())
      );
    });

    await().until(() -> result.get() != null);
    assertThat(result.get()).isEqualToIgnoringCase("fallback");
  }

  @Test
  public void testObservable() throws Exception {
    AtomicReference<String> result = new AtomicReference<>();

    vertx.runOnContext(v -> {
      Context context = vertx.getOrCreateContext();
      HttpClientCommand command = new HttpClientCommand(client, "/");
      command.observe().subscribe(s -> {
        context.runOnContext(v2 -> checkSetter(result, s));
      });
    });

    await().until(() -> result.get() != null);
    assertThat(result.get()).isEqualToIgnoringCase("hello");
  }

  private void checkSetter(AtomicReference<String> ref, String value) {
    if (Context.isOnEventLoopThread()) {
      ref.set(value);
    } else {
      ref.set("Not on the event loop");
    }
  }
}
