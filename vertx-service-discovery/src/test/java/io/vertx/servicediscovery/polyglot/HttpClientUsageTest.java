package io.vertx.servicediscovery.polyglot;

import io.vertx.core.Vertx;
import io.vertx.rxjava.servicediscovery.types.HttpEndpoint;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.impl.DiscoveryImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class HttpClientUsageTest {

  protected Vertx vertx;
  protected ServiceDiscovery discovery;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    discovery = new DiscoveryImpl(vertx, new ServiceDiscoveryOptions());

    AtomicBoolean done = new AtomicBoolean();
    discovery.publish(HttpEndpoint.createRecord("my-http-service", "localhost", 8080, "/"), ar -> {
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

    assertThat(discovery.bindings()).isEmpty();
  }

  @Test
  public void testWithReferenceGroovy() {
    AtomicReference<String> response = new AtomicReference<>();
    vertx.deployVerticle("polyglot/HttpClientVerticle.groovy", deployed -> {
      vertx.eventBus().<String>send("ref", "", reply -> {
        response.set(reply.result().body());
      });
    });

    await().untilAtomic(response, is(notNullValue()));

    System.out.println(response.get());
  }

  @Test
  public void testWithReferenceJS() {
    AtomicReference<String> response = new AtomicReference<>();
    vertx.deployVerticle("polyglot/HttpClientVerticle.js", deployed -> {
      vertx.eventBus().<String>send("ref", "", reply -> {
        response.set(reply.result().body());
      });
    });

    await().untilAtomic(response, is(notNullValue()));

    System.out.println(response.get());
  }


}
