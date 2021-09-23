package io.vertx.servicediscovery.backend.consul;

import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackend;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackendTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

/**
 * @author <a href="mailto:cafeinoman@openaliasbox.org>Francois Delalleau</a>
 */
public class ConsulBackendTest extends ServiceDiscoveryBackendTest {


  private static final String CONSUL_VERSION = "0.7.5";

  static GenericContainer<?> CONSUL_CONTAINER;

  @BeforeClass
  public static void start() {
    CONSUL_CONTAINER = new GenericContainer<>(DockerImageName.parse("consul:" + CONSUL_VERSION))
      .withExposedPorts(8500);

    CONSUL_CONTAINER.start();
    // CONSUL_CONTAINER.waitingFor(Wait.forLogMessage("cluster leadership acquired", 1));
    CONSUL_CONTAINER.followOutput(frame -> {
      System.out.print("CONSUL: " + frame.getUtf8String());
    });

  }

  @AfterClass
  public static void stop() {
    if (CONSUL_CONTAINER != null) {
      GenericContainer<?> container = CONSUL_CONTAINER;
      CONSUL_CONTAINER = null;
      container.stop();
    }
  }

  @After
  public void close() {
    if (backend != null) {
      ((ConsulBackendService)backend).close();
    }
  }

  @Override
  protected ServiceDiscoveryBackend createBackend() {
    backend = new ConsulBackendService();
    backend.init(vertx, new ConsulClientOptions()
      .setHost("localhost")
      .setPort(CONSUL_CONTAINER.getMappedPort(8500))
      .toJson());
    return backend;
  }
}
