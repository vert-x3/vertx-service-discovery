package io.vertx.servicediscovery.types;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.Command;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisConnection;
import io.vertx.redis.client.Request;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.impl.DiscoveryImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.embedded.RedisServer;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Check the behavior of the Redis data source.
 *
 * @author Eric Zhao
 */
public class RedisDataSourceTest {

  private static RedisServer server;

  private Vertx vertx;
  private ServiceDiscovery discovery;

  @BeforeClass
  static public void startRedis() throws Exception {
    server = new RedisServer(6379);
    System.out.println("Created embedded redis server on port 6379");
    server.start();
  }

  @AfterClass
  static public void stopRedis() throws Exception {
    server.stop();
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
    vertx.close((v) -> completed.set(true));
    await().untilAtomic(completed, is(true));

    assertThat(discovery.bindings()).isEmpty();
  }

  @Test
  public void test() {
    Record record = RedisDataSource.createRecord("some-redis-data-source",
      new JsonObject().put("endpoint", "redis://localhost:6379"),
      new JsonObject().put("database", "some-raw-data"));

    discovery.publish(record, r -> {
    });
    await().until(() -> record.getRegistration() != null);

    AtomicReference<Record> found = new AtomicReference<>();
    discovery.getRecord(new JsonObject().put("name", "some-redis-data-source"), ar -> {
      found.set(ar.result());
    });

    await().until(() -> found.get() != null);
    ServiceReference service = discovery.getReference(found.get());
    Redis client = service.get();
    AtomicBoolean success = new AtomicBoolean();
    client.connect(connect -> {
      if (connect.succeeded()) {
        RedisConnection conn = connect.result();
        conn.send(Request.cmd(Command.PING), ar -> {
          if (ar.succeeded()) {
            client.close();
            success.set(ar.succeeded());
          }
        });
      }
    });

    await().untilAtomic(success, is(true));
    service.release();
    // Just there to be sure we can call it twice
    service.release();
  }

  @Test
  public void testMissing() throws InterruptedException {
    AtomicReference<Throwable> expected = new AtomicReference<>();
    RedisDataSource.getRedisClient(discovery,
      new JsonObject().put("name", "some-redis-data-source"),
      ar -> {
        expected.set(ar.cause());
      });

    await().until(() -> expected.get() != null);
    assertThat(expected.get().getMessage()).contains("record");
  }

  @Test
  public void testWithSugar() throws InterruptedException {
    Record record = RedisDataSource.createRecord("some-redis-data-source",
      new JsonObject().put("endpoint", "redis://localhost:6379"),
      new JsonObject().put("database", "some-raw-data"));

    discovery.publish(record, r -> {
    });
    await().until(() -> record.getRegistration() != null);


    AtomicBoolean success = new AtomicBoolean();
    RedisDataSource.getRedisClient(discovery,
      new JsonObject().put("name", "some-redis-data-source"), ar -> {
        Redis client = ar.result();
        client.connect(connect -> {
          if (connect.succeeded()) {
            RedisConnection conn = connect.result();
            conn.send(Request.cmd(Command.PING), ar1 -> {
              if (ar1.succeeded()) {
                client.close();
                success.set(ar.succeeded());
              }
            });
          }
        });
      });
    await().untilAtomic(success, is(true));
  }

}
