package io.vertx.servicediscovery.backend.zookeeper;

import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackend;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackendTest;
import org.apache.curator.test.TestingServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ZookeeperBackendServiceWithEphemeralAndGuaranteedTest extends ServiceDiscoveryBackendTest {

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
        .put("connection", server.getConnectString())
        .put("ephemeral", true)
        .put("guaranteed", true)
        .put("basePath", "/services/my-backend")
    );
    return backend;
  }
}