package io.vertx.servicediscovery.polyglot;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.impl.DiscoveryImpl;
import io.vertx.servicediscovery.service.HelloService;
import io.vertx.servicediscovery.service.HelloServiceImpl;
import io.vertx.servicediscovery.types.*;
import io.vertx.serviceproxy.ServiceBinder;
import org.junit.*;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@RunWith(VertxUnitRunner.class)
public class PolyglotUsageTest {

  @ClassRule
  public static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

  protected Vertx vertx;
  protected ServiceDiscovery discovery;

  @BeforeClass
  public static void beforeClass() throws Exception {
    mongoDBContainer.start();
  }

  @AfterClass
  public static void afterClass() {
    mongoDBContainer.stop();
  }

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    discovery = new DiscoveryImpl(vertx, new ServiceDiscoveryOptions());
    HelloService svc = new HelloServiceImpl();
    new ServiceBinder(vertx).setAddress("my-service").register(HelloService.class, svc);

    AtomicBoolean httpEndpointPublished = new AtomicBoolean();
    AtomicBoolean serviceProxyPublished = new AtomicBoolean();
    AtomicBoolean messageSource1Published = new AtomicBoolean();
    AtomicBoolean messageSource2Published = new AtomicBoolean();
    AtomicBoolean redisDataSourcePublished = new AtomicBoolean();
    AtomicBoolean mongoDataSourcePublished = new AtomicBoolean();

    discovery.publish(
      HttpEndpoint.createRecord("my-http-service", "localhost", 8080, "/")).onComplete(
      ar -> httpEndpointPublished.set(ar.succeeded()));

    discovery.publish(
      EventBusService.createRecord("my-service", "my-service", HelloService.class.getName())).onComplete(
      ar -> serviceProxyPublished.set(ar.succeeded()));

    discovery.publish(
      MessageSource.createRecord("my-message-source-1", "source1")).onComplete(
      ar -> messageSource1Published.set(ar.succeeded())
    );

    discovery.publish(
      MessageSource.createRecord("my-message-source-2", "source2", JsonObject.class.getName())).onComplete(
      ar -> messageSource2Published.set(ar.succeeded())
    );

    discovery.publish(
      RedisDataSource.createRecord("my-redis-data-source",
        new JsonObject().put("url", "localhost"),
        new JsonObject().put("database", "some-raw-data"))).onComplete(
      ar -> redisDataSourcePublished.set(ar.succeeded())
    );

    discovery.publish(
      MongoDataSource.createRecord("my-mongo-data-source",
        new JsonObject().put("connection_string", mongoDBContainer.getReplicaSetUrl()),
        new JsonObject().put("database", "some-raw-data"))).onComplete(
      ar -> mongoDataSourcePublished.set(ar.succeeded())
    );

    await().untilAtomic(httpEndpointPublished, is(true));
    await().untilAtomic(serviceProxyPublished, is(true));
    await().untilAtomic(messageSource1Published, is(true));
    await().untilAtomic(messageSource2Published, is(true));
    await().untilAtomic(redisDataSourcePublished, is(true));
    await().untilAtomic(mongoDataSourcePublished, is(true));
  }

  @After
  public void tearDown() {
    discovery.close();
    AtomicBoolean completed = new AtomicBoolean();
    vertx.close().onComplete((v) -> completed.set(true));
    await().untilAtomic(completed, is(true));

    assertThat(discovery.bindings()).isEmpty();
  }


  @Test
  public void testJava(TestContext tc) {

    vertx.deployVerticle(MyVerticle.class.getName()).onComplete(tc.asyncAssertSuccess(id -> {
      vertx.eventBus().<JsonObject>request("http-ref", "")
        .onComplete(tc.asyncAssertSuccess(reply -> {
          tc.assertTrue(reply.body().getString("client").contains("HttpClient"));
          tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        }));

      vertx.eventBus().<JsonObject>request("http-sugar", "")
        .onComplete(tc.asyncAssertSuccess(reply -> {
          tc.assertTrue(reply.body().getString("client").contains("HttpClient"));
          tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        }));

      vertx.eventBus().<JsonObject>request("web-ref", "")
        .onComplete(tc.asyncAssertSuccess(reply -> {
          tc.assertTrue(reply.body().getString("client").contains("WebClient"));
          tc.assertTrue(reply.body().getString("direct").contains("HttpClient"));
          tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        }));

      vertx.eventBus().<JsonObject>request("web-sugar", "")
        .onComplete(tc.asyncAssertSuccess(reply -> {
          tc.assertTrue(reply.body().getString("client").contains("WebClient"));
          tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        }));

      vertx.eventBus().<JsonObject>request("service-sugar", "")
        .onComplete(tc.asyncAssertSuccess(reply -> {
          tc.assertTrue(reply.body().getString("client").contains("HelloServiceVertxEBProxy"));
          tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        }));

      vertx.eventBus().<JsonObject>request("service-ref", "")
        .onComplete(tc.asyncAssertSuccess(reply -> {
          tc.assertTrue(reply.body().getString("client").contains("HelloServiceVertxEBProxy"));
          tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        }));

      vertx.eventBus().<JsonObject>request("redis-sugar", "")
        .onComplete(tc.asyncAssertSuccess(reply -> {
          tc.assertTrue(reply.body().getString("client").contains("RedisClient"));
          tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        }));

      vertx.eventBus().<JsonObject>request("redis-ref", "")
        .onComplete(tc.asyncAssertSuccess(reply -> {
          tc.assertTrue(reply.body().getString("client").contains("RedisClient"));
          tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        }));

      vertx.eventBus().<JsonObject>request("mongo-sugar", "").onComplete(tc
        .asyncAssertSuccess(reply -> {
          tc.assertTrue(reply.body().getString("client").contains("MongoClientImpl"));
          tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        }));

      vertx.eventBus().<JsonObject>request("mongo-ref", "")
        .onComplete(tc.asyncAssertSuccess(reply -> {
          tc.assertTrue(reply.body().getString("client").contains("MongoClientImpl"));
          tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        }));

      vertx.eventBus().<JsonObject>request("source1-sugar", "")
        .onComplete(tc.asyncAssertSuccess(reply -> {
          tc.assertTrue(reply.body().getString("client").contains("MessageConsumerImpl"));
          tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        }));

      vertx.eventBus().<JsonObject>request("source1-ref", "").onComplete(tc.asyncAssertSuccess(reply -> {
        tc.assertTrue(reply.body().getString("client").contains("MessageConsumerImpl"));
        tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
      }));
    }));
  }
}
