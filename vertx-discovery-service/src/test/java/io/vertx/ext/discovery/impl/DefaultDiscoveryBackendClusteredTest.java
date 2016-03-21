package io.vertx.ext.discovery.impl;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.junit.Before;

import static com.jayway.awaitility.Awaitility.await;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DefaultDiscoveryBackendClusteredTest extends DefaultDiscoveryBackendTest {


  @Before
  public void setUp() {
    Vertx.clusteredVertx(new VertxOptions().setClusterHost("127.0.0.1"), ar -> {
      vertx = ar.result();
    });
    await().until(() -> vertx != null);
    backend = new DefaultDiscoveryBackend();
    backend.init(vertx);
  }


}