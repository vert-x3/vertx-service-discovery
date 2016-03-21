package io.vertx.ext.discovery.impl;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.runner.RunWith;

import static com.jayway.awaitility.Awaitility.await;

/**
 * Test the async map when running in clustered mode.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@RunWith(VertxUnitRunner.class)
public class ClusteredAsyncMapTest extends AsyncMapTest {
  @Before
  public void setUp() {
    Vertx.clusteredVertx(new VertxOptions(), ar -> vertx = ar.result());
    await().until(() -> vertx != null);
    map = new AsyncMap<>(vertx, "some-name");
  }
}