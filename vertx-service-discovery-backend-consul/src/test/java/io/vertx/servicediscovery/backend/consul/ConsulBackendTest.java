package io.vertx.servicediscovery.backend.consul;

import com.pszymczyk.consul.junit.ConsulResource;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackend;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackendTest;
import org.junit.After;
import org.junit.ClassRule;

/**
 * @author <a href="mailto:cafeinoman@openaliasbox.org>Francois Delalleau</a>
 */
public class ConsulBackendTest extends ServiceDiscoveryBackendTest {


  @ClassRule
  public static final ConsulResource consul = new ConsulResource();
  private ConsulBackendService backend;

  @After
  public void close() {
    if (backend != null) {
      backend.close();
    }
  }

  @Override
  protected ServiceDiscoveryBackend createBackend() {
    backend = new ConsulBackendService();
    backend.init(vertx, new ConsulClientOptions()
      .setHost("localhost")
      .setPort(consul.getHttpPort())
      .toJson());
    return backend;
  }
}
