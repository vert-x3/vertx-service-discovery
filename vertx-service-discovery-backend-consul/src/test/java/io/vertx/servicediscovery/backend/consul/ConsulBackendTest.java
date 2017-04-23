package io.vertx.servicediscovery.backend.consul;

import fr.javacrea.vertx.consul.test.utils.ConsulTestEnv;
import io.vertx.core.Vertx;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackend;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackendTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.core.Is.is;

/**
 * @author <a href="mailto:cafeinoman@openaliasbox.org>Francois Delalleau</a>
 */
public class ConsulBackendTest extends ServiceDiscoveryBackendTest {

  private static ConsulTestEnv env;
  private static ConsulClientOptions options;

  @BeforeClass
  static public void startConsul() throws Exception {
    AtomicBoolean done = new AtomicBoolean(false);
    env = ConsulTestEnv.create(Vertx.vertx());
    env.startOne(null, true, null, null).setHandler(ar -> {
      if (ar.succeeded()) {
        env.clientOptionsForNode(ar.result(), null).setHandler(opt -> {
          if (opt.succeeded()) {
            options = opt.result();
            done.set(true);
          } else {
            opt.cause().printStackTrace();
            throw new RuntimeException(opt.cause());
          }
        });
      } else {
        throw new RuntimeException(ar.cause());
      }
    });
    await().atMost(4, TimeUnit.MINUTES).untilAtomic(done, is(true)); // download of the binaries can take long time
  }

  @AfterClass
  static public void stopConsul() {
    AtomicBoolean done = new AtomicBoolean(false);
    env.close(n -> done.set(true));
    await().atMost(4, TimeUnit.MINUTES).untilAtomic(done, is(true));
  }

  @Override
  protected ServiceDiscoveryBackend createBackend() {
    ConsulBackendService backend = new ConsulBackendService();
    backend.init(vertx, options.toJson());
    return backend;
  }
}
