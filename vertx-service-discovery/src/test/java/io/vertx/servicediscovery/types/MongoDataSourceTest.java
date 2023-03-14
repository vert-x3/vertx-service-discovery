package io.vertx.servicediscovery.types;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.impl.DiscoveryImpl;
import org.junit.*;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.jayway.awaitility.Awaitility.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.core.Is.*;

/**
 * Check the behavior of the Mongo data source.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class MongoDataSourceTest {

  @ClassRule
  public static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

  private Vertx vertx;
  private ServiceDiscovery discovery;

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
  public void test() throws InterruptedException {
    Record record = MongoDataSource.createRecord("some-mongo-db",
      new JsonObject().put("connection_string", mongoDBContainer.getReplicaSetUrl()),
      new JsonObject().put("database", "some-raw-data"));

    discovery.publish(record);
    await().until(() -> record.getRegistration() != null);

    AtomicReference<Record> found = new AtomicReference<>();
    discovery.getRecord(new JsonObject().put("name", "some-mongo-db")).onComplete(ar -> {
      found.set(ar.result());
    });

    await().until(() -> found.get() != null);

    ServiceReference service = discovery.getReference(found.get());
    MongoClient client = service.get();
    AtomicBoolean success = new AtomicBoolean();
    client.getCollections().onComplete(ar -> {
      success.set(ar.succeeded());
    });

    await().untilAtomic(success, is(true));
    service.release();
    // Just there to be sure we can call it twice
    service.release();
  }

  @Test
  public void testMissing() throws InterruptedException {
    AtomicReference<Throwable> expected = new AtomicReference<>();
    MongoDataSource.getMongoClient(discovery,
      new JsonObject().put("name", "some-mongo-db")).onComplete(
      ar -> {
        expected.set(ar.cause());
      });

    await().until(() -> expected.get() != null);
    assertThat(expected.get().getMessage()).contains("record");
  }


  @Test
  public void testWithSugar() throws InterruptedException {
    Record record = MongoDataSource.createRecord("some-mongo-db",
      new JsonObject().put("connection_string", mongoDBContainer.getReplicaSetUrl()),
      new JsonObject().put("database", "some-raw-data"));

    discovery.publish(record).onComplete(r -> { });
    await().until(() -> record.getRegistration() != null);


    AtomicBoolean success = new AtomicBoolean();
    MongoDataSource.getMongoClient(discovery, new JsonObject().put("name", "some-mongo-db")).onComplete(
      ar -> {
        MongoClient client = ar.result();

        client.getCollections().onComplete(coll -> {
          client.close();
          success.set(coll.succeeded());
        });
      });
    await().untilAtomic(success, is(true));

  }
}
