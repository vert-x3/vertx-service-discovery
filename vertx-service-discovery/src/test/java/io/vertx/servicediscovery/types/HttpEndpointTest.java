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

package io.vertx.servicediscovery.types;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.Router;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.impl.DiscoveryImpl;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Test the publication and the consumption of the HTTP services.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@RunWith(VertxUnitRunner.class)
public class HttpEndpointTest {

  private Vertx vertx;
  private ServiceDiscovery discovery;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    discovery = new DiscoveryImpl(vertx, new ServiceDiscoveryOptions());

    Router router = Router.router(vertx);
    router.get("/foo").handler(ctxt -> {
      ctxt.response().end("hello");
    });

    AtomicBoolean done = new AtomicBoolean();
    vertx.createHttpServer().requestHandler(router::accept).listen(8080, ar -> {
      done.set(ar.succeeded());
    });

    await().untilAtomic(done, is(true));
  }

  @After
  public void tearDown() {
    discovery.close();
    AtomicBoolean completed = new AtomicBoolean();
    vertx.close((v) -> completed.set(true));
    await().untilAtomic(completed, is(true));

    Assertions.assertThat(discovery.bindings()).isEmpty();
  }


  @Test
  public void testPublicationAndConsumption(TestContext context) {
    Async async = context.async();

    // Publish the service
    Record record = HttpEndpoint.createRecord("hello-service", "localhost", 8080, "/foo");
    discovery.publish(record, rec -> {
      Record published = rec.result();

      discovery.getRecord(new JsonObject().put("name", "hello-service"), found -> {
        context.assertTrue(found.succeeded());
        context.assertTrue(found.result() != null);
        Record match = found.result();
        ServiceReference reference = discovery.getReference( match);
        context.assertEquals(reference.record().getLocation().getString("endpoint"), "http://localhost:8080/foo");
        context.assertFalse(reference.record().getLocation().getBoolean("ssl"));
        HttpClient client = reference.get();
        HttpClient client2 = reference.get();
        context.assertTrue(client == client2);

        client.getNow("/foo", response -> {
          context.assertEquals(response.statusCode(), 200);
          response.bodyHandler(body -> {
            context.assertEquals(body.toString(), "hello");

            reference.release();

            discovery.unpublish(published.getRegistration(), v -> {
              async.complete();
            });
          });
        });
      });
    });
  }

  @Test
  public void testRecordCreation() {
    Record record = HttpEndpoint.createRecord("some-name", "123.456.789.111", 80, null);
    assertThat(record.getLocation().getString(Record.ENDPOINT)).isEqualTo("http://123.456.789.111:80/");

    record = HttpEndpoint.createRecord("some-name", "123.456.789.111", 80, "foo");
    assertThat(record.getLocation().getString(Record.ENDPOINT)).isEqualTo("http://123.456.789.111:80/foo");

    record = HttpEndpoint.createRecord("some-name", "123.456.789.111", 80, "foo", new JsonObject().put("language", "en"));
    assertThat(record.getLocation().getString(Record.ENDPOINT)).isEqualTo("http://123.456.789.111:80/foo");
    assertThat(record.getMetadata().getString("language")).isEqualTo("en");

    record = HttpEndpoint.createRecord("some-name", "acme.org");
    assertThat(record.getLocation().getString(Record.ENDPOINT)).isEqualTo("http://acme.org:80/");

    record = HttpEndpoint.createRecord("http-bin", true, "httpbin.org", 443, "/get", null);
    ServiceReference reference = discovery.getReference( record);

    HttpClient client = reference.get();
    AtomicReference<JsonObject> resp = new AtomicReference<>();
    client.getNow("/get", response -> {
      response.bodyHandler(body -> {
        resp.set(body.toJsonObject());
      });
    });

    await().until(() -> resp.get() != null);
    assertThat(resp.get().getString("url")).isEqualTo("https://httpbin.org/get");
  }

  @Test
  public void testWithJavaScript(TestContext context) {
    Async async = context.async();

    vertx.eventBus().<JsonObject>consumer("result", message -> {
      context.assertEquals("ok", message.body().getString("status"));
      context.assertEquals("hello", message.body().getString("message"));
      vertx.undeploy("verticles/HttpEndpointConsumer.js");
      async.complete();
    });

    // Publish the service
    Record record = HttpEndpoint.createRecord("hello-service", "localhost", 8080, "/foo");
    discovery.publish(record, rec -> {
      vertx.deployVerticle("verticles/HttpEndpointConsumer.js", context.asyncAssertSuccess());
    });
  }
}
