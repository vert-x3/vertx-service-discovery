package io.vertx.servicediscovery.backend.consul;

import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackend;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackendTest;
import org.junit.After;
import org.junit.Before;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:cafeinoman@openaliasbox.org>Francois Delalleau</a>
 */
public class ConsulBackendTest extends ServiceDiscoveryBackendTest {


  private static final String CONSUL_VERSION = "0.7.5";

  private GenericContainer<?> CONSUL_CONTAINER;

  @Override
  public void setUp() throws Exception {
    CONSUL_CONTAINER = new GenericContainer<>(DockerImageName.parse("consul:" + CONSUL_VERSION))
      .withExposedPorts(8500);

    CONSUL_CONTAINER.start();
    CountDownLatch latch = new CountDownLatch(1);
    CONSUL_CONTAINER.followOutput(frame -> {
      if (frame.getUtf8String().contains("cluster leadership acquired")) {
        latch.countDown();
      }
      System.out.print("CONSUL: " + frame.getUtf8String());
    });
    try {
      latch.await(30, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    super.setUp();
  }

  @Override
  public void tearDown() {
    super.tearDown();
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
