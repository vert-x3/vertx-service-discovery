package io.vertx.servicediscovery.polyglot;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.servicediscovery.types.HttpEndpoint;
import io.vertx.servicediscovery.types.MessageSource;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.impl.DiscoveryImpl;
import io.vertx.servicediscovery.service.HelloService;
import io.vertx.servicediscovery.service.HelloServiceImpl;
import io.vertx.servicediscovery.types.JDBCDataSource;
import io.vertx.servicediscovery.types.RedisDataSource;
import io.vertx.serviceproxy.ProxyHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@RunWith(VertxUnitRunner.class)
public class PolyglotUsageTest {

  protected Vertx vertx;
  protected ServiceDiscovery discovery;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    discovery = new DiscoveryImpl(vertx, new ServiceDiscoveryOptions());
    HelloService svc = new HelloServiceImpl();
    ProxyHelper.registerService(HelloService.class, vertx, svc, "my-service");

    AtomicBoolean ready1 = new AtomicBoolean();
    AtomicBoolean ready2 = new AtomicBoolean();
    AtomicBoolean ready3 = new AtomicBoolean();
    AtomicBoolean ready4 = new AtomicBoolean();
    AtomicBoolean ready5 = new AtomicBoolean();
    AtomicBoolean ready6 = new AtomicBoolean();
    discovery.publish(
      HttpEndpoint.createRecord("my-http-service", "localhost", 8080, "/"),
      ar -> ready1.set(ar.succeeded()));
    discovery.publish(
      EventBusService.createRecord("my-service", "my-service", HelloService.class.getName()),
      ar -> ready2.set(ar.succeeded()));
    discovery.publish(
      JDBCDataSource.createRecord("my-data-source",
        new JsonObject().put("url", "jdbc:hsqldb:file:target/dumb-db;shutdown=true"),
        new JsonObject().put("database", "some-raw-data")),
      ar -> ready3.set(ar.succeeded())
    );
    discovery.publish(
      MessageSource.createRecord("my-message-source-1", "source1"),
      ar -> ready4.set(ar.succeeded())
    );
    discovery.publish(
      MessageSource.createRecord("my-message-source-2", "source2", JsonObject.class.getName()),
      ar -> ready5.set(ar.succeeded())
    );
    discovery.publish(
      RedisDataSource.createRecord("my-redis-data-source",
        new JsonObject().put("url", "localhost"),
        new JsonObject().put("database", "some-raw-data")),
      ar -> ready6.set(ar.succeeded())
    );

    await().untilAtomic(ready1, is(true));
    await().untilAtomic(ready2, is(true));
    await().untilAtomic(ready3, is(true));
    await().untilAtomic(ready4, is(true));
    await().untilAtomic(ready5, is(true));
    await().untilAtomic(ready6, is(true));
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
  public void testJava(TestContext tc) {
    Async deployment = tc.async();
    Async http_ref = tc.async();
    Async http_sugar = tc.async();
    Async svc_ref = tc.async();
    Async svc_sugar = tc.async();
    Async ds_ref = tc.async();
    Async ds_sugar = tc.async();
    Async ms_ref = tc.async();
    Async ms_sugar = tc.async();
    Async redis_ref = tc.async();
    Async redis_sugar = tc.async();

    vertx.deployVerticle(MyVerticle.class.getName(), deployed -> {

      vertx.eventBus().<JsonObject>send("http-ref", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client").contains("HttpClient"));
        tc.assertTrue(reply.result().body().getString("direct").contains("HttpClient"));
        http_ref.complete();
      });

      vertx.eventBus().<JsonObject>send("http-sugar", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client").contains("HttpClient"));
        http_sugar.complete();
      });

      vertx.eventBus().<JsonObject>send("service-sugar", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client").contains("HelloServiceVertxEBProxy"));
        svc_sugar.complete();
      });

      vertx.eventBus().<JsonObject>send("service-ref", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client").contains("HelloServiceVertxEBProxy"));
        tc.assertTrue(reply.result().body().getString("direct").contains("HelloServiceVertxEBProxy"));
        svc_ref.complete();
      });

      vertx.eventBus().<JsonObject>send("ds-sugar", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client").contains("JDBCClientImpl"));
        ds_sugar.complete();
      });

      vertx.eventBus().<JsonObject>send("ds-ref", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client").contains("JDBCClientImpl"));
        tc.assertTrue(reply.result().body().getString("direct").contains("JDBCClientImpl"));
        ds_ref.complete();
      });

      vertx.eventBus().<JsonObject>send("redis-sugar", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client").contains("RedisClientImpl"));
        redis_sugar.complete();
      });

      vertx.eventBus().<JsonObject>send("redis-ref", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client").contains("RedisClientImpl"));
        tc.assertTrue(reply.result().body().getString("direct").contains("RedisClientImpl"));
        redis_ref.complete();
      });

      vertx.eventBus().<JsonObject>send("source1-sugar", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client").contains("HandlerRegistration"));
        ms_sugar.complete();
      });

      vertx.eventBus().<JsonObject>send("source1-ref", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client").contains("HandlerRegistration"));
        tc.assertTrue(reply.result().body().getString("direct").contains("HandlerRegistration"));
        ms_ref.complete();
      });

      deployment.complete();
    });
  }

  @Test
  public void testJavaScript(TestContext tc) {
    Async deployment = tc.async();
    Async http_ref = tc.async();
    Async http_sugar = tc.async();
    Async svc_ref = tc.async();
    Async svc_sugar = tc.async();
    Async ds_ref = tc.async();
    Async ds_sugar = tc.async();
    Async ms_ref = tc.async();
    Async ms_sugar = tc.async();
    Async redis_ref = tc.async();
    Async redis_sugar = tc.async();

    vertx.deployVerticle("polyglot/my-verticle.js", deployed -> {

      vertx.eventBus().<JsonObject>send("http-ref", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("ref_del").contains("HttpEndpointReference"));
        tc.assertTrue(reply.result().body().getString("client_del").contains("HttpClient"));
        http_ref.complete();
      });

      vertx.eventBus().<JsonObject>send("http-sugar", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client_del").contains("HttpClient"));
        http_sugar.complete();
      });

      vertx.eventBus().<JsonObject>send("service-sugar", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client_del").contains("HelloServiceVertxEBProxy"));
        tc.assertTrue(reply.result().body().getString("client").contains("[object"));
        svc_sugar.complete();
      });

      vertx.eventBus().<JsonObject>send("service-ref", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("ref_del").contains("EventBusServiceReference"));
        tc.assertTrue(reply.result().body().getString("client_del").contains("HelloServiceVertxEBProxy"));
        tc.assertTrue(reply.result().body().getString("client").contains("[object"));
        svc_ref.complete();
      });

      vertx.eventBus().<JsonObject>send("ds-sugar", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client_del").contains("JDBCClient"));
        ds_sugar.complete();
      });

      vertx.eventBus().<JsonObject>send("ds-ref", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("ref_del").contains("JdbcServiceReference"));
        tc.assertTrue(reply.result().body().getString("client_del").contains("JDBCClientImpl"));
        ds_ref.complete();
      });

      vertx.eventBus().<JsonObject>send("redis-sugar", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client_del").contains("RedisClient"));
        redis_sugar.complete();
      });

      vertx.eventBus().<JsonObject>send("redis-ref", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("ref_del").contains("RedisServiceReference"));
        tc.assertTrue(reply.result().body().getString("client_del").contains("RedisClientImpl"));
        redis_ref.complete();
      });

      vertx.eventBus().<JsonObject>send("source1-sugar", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client_del").contains("HandlerRegistration"));
        ms_sugar.complete();
      });

      vertx.eventBus().<JsonObject>send("source1-ref", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("ref_del").contains("MessageSourceReference"));
        tc.assertTrue(reply.result().body().getString("client_del").contains("HandlerRegistration"));
        ms_ref.complete();
      });

      deployment.complete();
    });
  }

  @Test
  public void testRX(TestContext tc) {
    Async deployment = tc.async();
    Async http_ref = tc.async();
    Async http_sugar = tc.async();
    Async svc_ref = tc.async();
    Async svc_sugar = tc.async();
    Async ds_ref = tc.async();
    Async ds_sugar = tc.async();
    Async ms_ref = tc.async();
    Async ms_sugar = tc.async();
    Async redis_ref = tc.async();
    Async redis_sugar = tc.async();

    vertx.deployVerticle(MyRXVerticle.class.getName(), deployed -> {

      vertx.eventBus().<JsonObject>send("http-ref", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client").contains("HttpClient"));
//        tc.assertTrue(reply.result().body().getString("direct").contains("HttpClient"));
        http_ref.complete();
      });

      vertx.eventBus().<JsonObject>send("http-sugar", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client").contains("HttpClient"));
        http_sugar.complete();
      });

      vertx.eventBus().<JsonObject>send("service-sugar", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client").contains("HelloService"));
        tc.assertTrue(reply.result().body().getString("client").contains("rx"));
        svc_sugar.complete();
      });

      vertx.eventBus().<JsonObject>send("service-ref", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client").contains("HelloServiceVertxEBProxy"));
        tc.assertTrue(reply.result().body().getString("direct").contains("HelloServiceVertxEBProxy"));
        svc_ref.complete();
      });

      vertx.eventBus().<JsonObject>send("ds-sugar", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client").contains("JDBCClientImpl"));
        ds_sugar.complete();
      });

      vertx.eventBus().<JsonObject>send("ds-ref", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client").contains("JDBCClientImpl"));
        tc.assertTrue(reply.result().body().getString("direct").contains("JDBCClientImpl"));
        ds_ref.complete();
      });

      vertx.eventBus().<JsonObject>send("redis-sugar", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client").contains("RedisClientImpl"));
        redis_sugar.complete();
      });

      vertx.eventBus().<JsonObject>send("redis-ref", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client").contains("RedisClientImpl"));
        tc.assertTrue(reply.result().body().getString("direct").contains("RedisClientImpl"));
        redis_ref.complete();
      });

      vertx.eventBus().<JsonObject>send("source1-sugar", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client").contains("HandlerRegistration"));
        ms_sugar.complete();
      });

      vertx.eventBus().<JsonObject>send("source1-ref", "", reply -> {
        tc.assertTrue(reply.succeeded());
        tc.assertTrue(reply.result().body().getString("client").contains("HandlerRegistration"));
        tc.assertTrue(reply.result().body().getString("direct").contains("HandlerRegistration"));
        ms_ref.complete();
      });

      deployment.complete();
    });
  }
}
