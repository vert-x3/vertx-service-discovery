package io.vertx.servicediscovery.backend.consul;

import io.vertx.core.Vertx;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulContext;
import io.vertx.ext.consul.ConsulTestSuite;
import io.vertx.ext.consul.impl.ConsulClientImpl;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackend;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackendTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.function.Function;

/**
 * @author <a href="mailto:cafeinoman@openaliasbox.org>Francois Delalleau</a>
 */
public class ConsulBackendTest extends ServiceDiscoveryBackendTest {

  private static Function<Vertx, ConsulContext> ctxFactory;
  private ConsulContext ctx;

  @BeforeClass
  static public void startConsul() throws Exception {
    ctxFactory = vertx -> new ConsulContext(
      opts -> new ConsulClientImpl(vertx, opts),
      ConsulClient::close
    );
  }

  @AfterClass
  static public void stopConsul() {
    ConsulTestSuite.stopConsul();
  }


  @After
  @Override
  public void tearDown() {
    ctx.stop();
    ctx = null;
    super.tearDown();
  }

  @Override
  protected ServiceDiscoveryBackend createBackend() {
    ctx = ctxFactory.apply(vertx);
    ctx.start();
    ConsulBackendService backend = new ConsulBackendService();
    backend.init(vertx, ctx.writeClientOptions().toJson());
    return backend;
  }
}
