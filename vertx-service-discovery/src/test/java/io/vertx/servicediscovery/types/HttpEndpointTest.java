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
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
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

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

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
    vertx.createHttpServer().requestHandler(router).listen(8080).onComplete(ar -> {
      done.set(ar.succeeded());
    });

    await().untilAtomic(done, is(true));
  }

  @After
  public void tearDown() {
    discovery.close();
    AtomicBoolean completed = new AtomicBoolean();
    vertx.close().onComplete((v) -> completed.set(true));
    await().untilAtomic(completed, is(true));

    Assertions.assertThat(discovery.bindings()).isEmpty();
  }


  @Test
  public void testPublicationAndConsumption(TestContext context) {
    Async async = context.async();

    // Publish the service
    Record record = HttpEndpoint.createRecord("hello-service", "localhost", 8080, "/foo");
    discovery.publish(record).onComplete(rec -> {
      Record published = rec.result();

      discovery.getRecord(new JsonObject().put("name", "hello-service")).onComplete(found -> {
        context.assertTrue(found.succeeded());
        context.assertTrue(found.result() != null);
        Record match = found.result();
        ServiceReference reference = discovery.getReference(match);
        context.assertEquals(reference.record().getLocation().getString("endpoint"), "http://localhost:8080/foo");
        context.assertFalse(reference.record().getLocation().getBoolean("ssl"));
        HttpClient client = reference.get();
        HttpClient client2 = reference.get();
        context.assertTrue(client == client2);

        client.request(HttpMethod.GET, "/foo").compose(request -> request
          .send()
          .compose(response -> {
            context.assertEquals(response.statusCode(), 200);
            return response.body();
          })).onComplete(context.asyncAssertSuccess(body -> {
          context.assertEquals(body.toString(), "hello");
        })).onComplete(ar -> {
          reference.release();
          discovery.unpublish(published.getRegistration()).onComplete(v -> {
            async.complete();
          });
        });
      });
    });
  }

  @Test
  public void testPublicationAndConsumptionAsWebClient(TestContext context) {
    Async async = context.async();

    // Publish the service
    Record record = HttpEndpoint.createRecord("hello-service", "localhost", 8080, "/foo");
    discovery.publish(record).onComplete(rec -> {
      Record published = rec.result();

      discovery.getRecord(new JsonObject().put("name", "hello-service")).onComplete(found -> {
        context.assertTrue(found.succeeded());
        context.assertTrue(found.result() != null);
        Record match = found.result();
        ServiceReference reference = discovery.getReference(match);
        context.assertEquals(reference.record().getLocation().getString("endpoint"), "http://localhost:8080/foo");
        context.assertFalse(reference.record().getLocation().getBoolean("ssl"));
        WebClient client = reference.getAs(WebClient.class);
        WebClient client2 = reference.cachedAs(WebClient.class);
        context.assertTrue(client == client2);

        client.get("/foo")
          .send().onComplete(response -> {
            if (response.failed()) {
              context.fail(response.cause());
            } else {
              HttpResponse<Buffer> resp = response.result();
              context.assertEquals(resp.statusCode(), 200);
              context.assertEquals(resp.body().toString(), "hello");
              reference.release();

              discovery.unpublish(published.getRegistration()).onComplete(v -> async.complete());
            }
          });
      });
    });
  }

  @Test
  public void testAutoCloseable(TestContext context) {
    Async async = context.async();

    // Publish the service
    Record record = HttpEndpoint.createRecord("hello-service", "localhost", 8080, "/foo");
    discovery.publish(record).onComplete(rec -> {
      Record published = rec.result();

      discovery.getRecord(new JsonObject().put("name", "hello-service")).onComplete(found -> {
        context.assertTrue(found.succeeded());
        context.assertTrue(found.result() != null);
        Record match = found.result();

        try (ServiceReference reference = discovery.getReference(match)) {
          context.assertEquals(reference.record().getLocation().getString("endpoint"), "http://localhost:8080/foo");
          context.assertFalse(reference.record().getLocation().getBoolean("ssl"));
          WebClient client = reference.getAs(WebClient.class);
          WebClient client2 = reference.cachedAs(WebClient.class);
          context.assertTrue(client == client2);
        } catch (Exception e) {
          context.fail(e);
        }

        discovery.unpublish(published.getRegistration()).onComplete(v -> async.complete());
      });
    });
  }

  @Test
  public void testPublicationAndConsumptionWithConfiguration(TestContext context) {
    Async async = context.async();

    // Publish the service
    Record record = HttpEndpoint.createRecord("hello-service", "localhost", 8080, "/foo");
    discovery.publish(record).onComplete(rec -> {
      Record published = rec.result();

      HttpEndpoint.getClient(discovery, new JsonObject().put("name", "hello-service"), new JsonObject().put
        ("keepAlive", false)).onComplete(found -> {
        context.assertTrue(found.succeeded());
        context.assertTrue(found.result() != null);
        HttpClient client = found.result();
        client.request(HttpMethod.GET, "/foo").compose(request -> {
          return request.send().compose(response -> {
            context.assertEquals(response.statusCode(), 200);
            context.assertEquals(response.getHeader("connection"), "close");
            return response.body();
          });
        }).onComplete(context.asyncAssertSuccess(body -> {
          context.assertEquals(body.toString(), "hello");
        })).onComplete(ar -> {
          ServiceDiscovery.releaseServiceObject(discovery, client);
          discovery.unpublish(published.getRegistration()).onComplete(v -> async.complete());
        });
      });
    });
  }

  @Test
  public void testPublicationAndConsumptionWithConfigurationAsWebClient(TestContext context) {
    Async async = context.async();

    // Publish the service
    Record record = HttpEndpoint.createRecord("hello-service", "localhost", 8080, "/foo");
    discovery.publish(record).onComplete(rec -> {
      Record published = rec.result();

      HttpEndpoint.getWebClient(discovery,
        new JsonObject().put("name", "hello-service"),
        new JsonObject().put("keepAlive", false)).onComplete(found -> {
          context.assertTrue(found.succeeded());
          context.assertTrue(found.result() != null);
          context.assertTrue(found.result() instanceof WebClient);
          WebClient client = found.result();
          client.get("/foo").send().onComplete(ar -> {
            if (ar.failed()) {
              context.fail(ar.cause());
            }
            HttpResponse<Buffer> response = ar.result();
            context.assertEquals(response.statusCode(), 200);
            context.assertEquals(response.getHeader("connection"), "close");
            context.assertEquals(response.body().toString(), "hello");

            ServiceDiscovery.releaseServiceObject(discovery, client);
            discovery.unpublish(published.getRegistration()).onComplete(v -> async.complete());
          });
        });
    });
  }

  @Test
  public void testPublicationAndConsumptionAsWebClientViaHttpEndpoint(TestContext context) {
    Async async = context.async();

    // Publish the service
    Record record = HttpEndpoint.createRecord("hello-service", "localhost", 8080, "/foo");
    discovery.publish(record).onComplete(rec -> {
      Record published = rec.result();

      HttpEndpoint.getWebClient(discovery,
        new JsonObject().put("name", "hello-service")).onComplete(found -> {
          context.assertTrue(found.succeeded());
          context.assertTrue(found.result() != null);
          context.assertTrue(found.result() instanceof WebClient);
          WebClient client = found.result();
          client.get("/foo").send().onComplete(ar -> {
            if (ar.failed()) {
              context.fail(ar.cause());
            }
            HttpResponse<Buffer> response = ar.result();
            context.assertEquals(response.statusCode(), 200);
            context.assertEquals(response.body().toString(), "hello");

            ServiceDiscovery.releaseServiceObject(discovery, client);
            discovery.unpublish(published.getRegistration()).onComplete(v -> async.complete());
          });
        });
    });
  }

  @Test
  public void testRecordCreation(TestContext testContext) {
    Record record = HttpEndpoint.createRecord("some-name", "123.456.789.111", 80, null);
    assertThat(record.getLocation().getString(Record.ENDPOINT)).isEqualTo("http://123.456.789.111:80/");

    record = HttpEndpoint.createRecord("some-name", "123.456.789.111", 80, "foo");
    assertThat(record.getLocation().getString(Record.ENDPOINT)).isEqualTo("http://123.456.789.111:80/foo");

    record = HttpEndpoint.createRecord("some-name", "123.456.789.111", 80, "foo", new JsonObject().put("language", "en"));
    assertThat(record.getLocation().getString(Record.ENDPOINT)).isEqualTo("http://123.456.789.111:80/foo");
    assertThat(record.getMetadata().getString("language")).isEqualTo("en");

    record = HttpEndpoint.createRecord("some-name", "acme.org");
    assertThat(record.getLocation().getString(Record.ENDPOINT)).isEqualTo("http://acme.org:80/");

    vertx.createHttpServer(new HttpServerOptions()
      .setHost("127.0.0.1")
    ).requestHandler(request -> {
      request.response().end(new JsonObject().put("url", request.absoluteURI()).encode());
    }).listen(0).onComplete(testContext.asyncAssertSuccess(server -> {

      Record sslRecord = HttpEndpoint.createRecord("http-bin", false, "127.0.0.1", server.actualPort(), "/get", null);
      ServiceReference reference = discovery.getReferenceWithConfiguration(sslRecord, new HttpClientOptions()
        .toJson());

      WebClient webClient = WebClient.wrap(reference.get());
      webClient.get("/get").as(BodyCodec.jsonObject()).send().onComplete(testContext.asyncAssertSuccess(resp -> {
        assertEquals("http://127.0.0.1:" + server.actualPort() + "/get", resp.body().getString("url"));
      }));
    }));
  }
}
