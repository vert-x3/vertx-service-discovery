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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.jayway.awaitility.Awaitility.*;
import static org.assertj.core.api.Assertions.*;

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
  public void tearDown() throws Exception {
    discovery.close();
    vertx.close().toCompletionStage().toCompletableFuture().get(20, TimeUnit.SECONDS);
    assertThat(discovery.bindings()).isEmpty();
  }

  @Test
  public void test() throws Exception {
    Record record = MongoDataSource.createRecord("some-mongo-db",
      new JsonObject().put("connection_string", mongoDBContainer.getReplicaSetUrl()),
      new JsonObject().put("database", "some-raw-data"));

    discovery.publish(record);
    await().until(() -> record.getRegistration() != null);

    Record found = discovery.getRecord(new JsonObject().put("name", "some-mongo-db"))
      .toCompletionStage()
      .toCompletableFuture()
      .get(20, TimeUnit.SECONDS);

    ServiceReference service = discovery.getReference(found);
    MongoClient client = service.get();
    client.getCollections()
      .toCompletionStage()
      .toCompletableFuture()
      .get(20, TimeUnit.SECONDS);

    service.release();
    // Just there to be sure we can call it twice
    service.release();
  }

  @Test
  public void testMissing() throws InterruptedException, TimeoutException {
    try {
      MongoDataSource.getMongoClient(discovery,
        new JsonObject().put("name", "some-mongo-db")).toCompletionStage().toCompletableFuture().get(20, TimeUnit.SECONDS);
      fail("Expected failure");
    } catch (ExecutionException e) {
      assertThat(e.getCause().getMessage()).contains("record");
    }
  }


  @Test
  public void testWithSugar() {
    Record record = MongoDataSource.createRecord("some-mongo-db",
      new JsonObject().put("connection_string", mongoDBContainer.getReplicaSetUrl()),
      new JsonObject().put("database", "some-raw-data"));

    discovery.publish(record).onComplete(r -> { });
    await().until(() -> record.getRegistration() != null);


    MongoDataSource.getMongoClient(discovery, new JsonObject().put("name", "some-mongo-db"))
      .compose(client -> client
        .getCollections()
        .compose(res -> client.close())
      );
  }
}
