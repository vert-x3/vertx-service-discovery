package io.vertx.servicediscovery.backend.zookeeper;

import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.Status;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackend;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackendTest;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.zookeeper.KeeperException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ZookeeperBackendServiceTest extends ServiceDiscoveryBackendTest {

  private static final Integer DEFAULT_PORT = 2181;
  private static TestingServer server;

  @BeforeClass
  public static void startZookeeper() throws Exception {
    server = new TestingServer(DEFAULT_PORT);
    server.start();
  }

  @AfterClass
  public static void stopZookeeper() throws IOException {
    server.stop();
  }

  @Override
  protected ServiceDiscoveryBackend createBackend() {
    ZookeeperBackendService backend = new ZookeeperBackendService();
    backend.init(vertx, new JsonObject()
        .put("connection",  server.getConnectString())
        .put("baseSleepTimeBetweenRetries",  10)
        .put("connectionTimeoutMs",  1000));
    return backend;
  }

  @Test
  public void testReconnection() throws Exception {
    Record record = new Record().setName("my-service").setStatus(Status.UP);
    assertThat(record.getRegistration()).isNull();

    // Insertion
    AtomicReference<Record> reference = new AtomicReference<>();
    backend.store(record, ar -> {
      if (!ar.succeeded()) {
        ar.cause().printStackTrace();
      }
      reference.set(ar.result());
    });

    await().until(() -> reference.get() != null);
    assertThat(reference.get().getName()).isEqualToIgnoringCase("my-service");
    assertThat(reference.get().getRegistration()).isNotNull();
    record = reference.get();

    // Retrieve
    reference.set(null);
    backend.getRecord(record.getRegistration(), ar -> reference.set(ar.result()));
    await().until(() -> reference.get() != null);
    assertThat(reference.get().getName()).isEqualToIgnoringCase("my-service");
    assertThat(reference.get().getRegistration()).isNotNull();

    server.stop();

    AtomicReference<Throwable> error = new AtomicReference<>();
    backend.getRecord(record.getRegistration(), ar -> error.set(ar.cause()));
    await().until(() -> error.get() != null);
    assertThat(error.get()).isInstanceOf(KeeperException.ConnectionLossException.class);

    server.restart();
    CuratorFramework client = CuratorFrameworkFactory.builder()
        .connectString(server.getConnectString())
        .retryPolicy(new ExponentialBackoffRetry(10, 3))
        .build();
    client.start();
    // Wait until we have been connected to the server
    client.blockUntilConnected();

    reference.set(null);
    backend.getRecord(record.getRegistration(), ar -> {
      if (ar.failed()) {
        ar.cause().printStackTrace();
      }
      reference.set(ar.result());
    });


    await().until(() -> reference.get() != null);
    assertThat(reference.get().getName()).isEqualToIgnoringCase("my-service");
    assertThat(reference.get().getRegistration()).isNotNull();

    server.stop();

    // Remove
    AtomicBoolean completed = new AtomicBoolean();
    completed.set(false);
    backend.remove(record, ar -> completed.set(ar.failed()));
    await().untilAtomic(completed, is(true));

    server.restart();

    completed.set(false);
    backend.remove(record, ar -> completed.set(ar.succeeded()));
    await().untilAtomic(completed, is(true));

    client.close();
  }
}