package io.vertx.servicediscovery.zookeeper;

import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.Repeat;
import io.vertx.ext.unit.junit.RepeatRule;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.servicediscovery.Record;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.test.TestingServer;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@RunWith(VertxUnitRunner.class)
public class ZookeeperBridgeTest {


  @Rule
  public RepeatRule rule = new RepeatRule();

  private TestingServer zkTestServer;
  private CuratorFramework cli;
  private ServiceDiscovery<String> discovery;
  private Vertx vertx;
  private io.vertx.servicediscovery.ServiceDiscovery sd;

  @Before
  public void startZookeeper() throws Exception {
    zkTestServer = new TestingServer(2181);
    cli = CuratorFrameworkFactory.newClient(zkTestServer.getConnectString(), new RetryOneTime(2000));
    cli.start();

    discovery = ServiceDiscoveryBuilder.builder(String.class)
      .client(cli)
      .basePath("/discovery")
      .watchInstances(true)
      .build();

    discovery.start();
    vertx = Vertx.vertx();
    sd = io.vertx.servicediscovery.ServiceDiscovery.create(vertx);
  }

  @After
  public void stopZookeeper() throws IOException {
    discovery.close();
    sd.close();
    cli.close();
    zkTestServer.stop();
    vertx.close();
  }

  @Test
  public void testRegistration(TestContext tc) throws Exception {
    Async async = tc.async();

    UriSpec uriSpec = new UriSpec("{scheme}://foo.com:{port}");
    ServiceInstance<String> instance = ServiceInstance.<String>builder()
      .name("foo-service")
      .payload(new JsonObject().put("foo", "bar").encodePrettily())
      .port((int) (65535 * Math.random()))
      .uriSpec(uriSpec)
      .build();

    discovery.registerService(instance);
    sd.registerServiceImporter(
      new ZookeeperServiceImporter(),
      new JsonObject().put("connection", zkTestServer.getConnectString())).onComplete(
      v -> {
        if (v.failed()) {
          v.cause().printStackTrace();
        }
        tc.assertTrue(v.succeeded());

        sd.getRecords(x -> true).onComplete(l -> {
          if (l.failed()) {
            l.cause().printStackTrace();
          }
          tc.assertTrue(l.succeeded());
          tc.assertTrue(l.result().size() == 1);
          tc.assertEquals("foo-service", l.result().get(0).getName());
          async.complete();
        });

      });
  }

  @Test
  public void testServiceArrival(TestContext tc) throws Exception {
    Async async = tc.async();

    UriSpec uriSpec = new UriSpec("{scheme}://foo.com:{port}");
    ServiceInstance<String> instance = ServiceInstance.<String>builder()
      .name("foo-service")
      .payload(new JsonObject().put("foo", "bar").encodePrettily())
      .port(8080)
      .uriSpec(uriSpec)
      .build();

    sd.registerServiceImporter(
      new ZookeeperServiceImporter(),
      new JsonObject().put("connection", zkTestServer.getConnectString())).onComplete(
      tc.asyncAssertSuccess(v -> {
        sd.getRecords(x -> true).onComplete(tc.asyncAssertSuccess(l -> {
          tc.assertTrue(l.size() == 0);

          vertx.executeBlocking(() -> {
            this.discovery.registerService(instance);
            return null;
          }).onComplete(tc.asyncAssertSuccess(v2 -> {
            waitUntil(() -> serviceLookup(sd, 1), tc.asyncAssertSuccess(v3 -> {
              async.complete();
            }));
          }));
        }));
      }));
  }

  @Test
  public void testArrivalDepartureAndComeBack(TestContext tc) throws Exception {
    Async async = tc.async();

    UriSpec uriSpec = new UriSpec("{scheme}://foo.com:{port}");
    ServiceInstance<String> instance = ServiceInstance.<String>builder()
      .name("foo-service")
      .payload(new JsonObject().put("foo", "bar").encodePrettily())
      .port(8080)
      .uriSpec(uriSpec)
      .build();

    sd.registerServiceImporter(
      new ZookeeperServiceImporter(),
      new JsonObject().put("connection", zkTestServer.getConnectString())).onComplete(
      tc.asyncAssertSuccess(v1 -> {
        sd.getRecords(x -> true).onComplete(tc.asyncAssertSuccess(l -> {
          tc.assertTrue(l.size() == 0);
          vertx.executeBlocking(() -> {
            this.discovery.registerService(instance);
            return null;
          }).onComplete(tc.asyncAssertSuccess(v2 -> {
            waitUntil(() -> serviceLookup(sd, 1), tc.asyncAssertSuccess(v3 -> {
              // Leave
              vertx.executeBlocking(() -> {
                this.discovery.unregisterService(instance);
                return null;
              }).onComplete(tc.asyncAssertSuccess(v4 -> {
                waitUntil(() -> serviceLookup(sd, 0), tc.asyncAssertSuccess(v5 -> {
                  vertx.executeBlocking(() -> {
                    this.discovery.registerService(instance);
                    return null;
                  }).onComplete(ar3 -> waitUntil(() -> serviceLookup(sd, 1), tc.asyncAssertSuccess(v6 -> {
                    async.complete();
                  })));
                }));
              }));
            }));
          }));
        }));
      }));
  }

  private Future<List<Record>> serviceLookup(io.vertx.servicediscovery.ServiceDiscovery discovery, int expected) {
    Promise<List<Record>> promise = Promise.promise();
    discovery.getRecords(x -> true).onComplete(ar -> {
      if (ar.failed()) {
        VertxException failure = VertxException.noStackTrace("service lookup failed: " + ar.cause().getMessage());
        failure.initCause(ar.cause());
        promise.fail(failure);
      } else if (ar.result().size() != expected) {
        promise.fail("service lookup failed: unexpected records " + ar.result() + " != " + expected);
      } else {
        promise.complete(ar.result());
      }
    });
    return promise.future();
  }

  // 1 here, import 1, second arrive, both imported
  @Test
  @Repeat(10)
  public void testServiceArrivalWithSameName(TestContext tc) throws Exception {
    Async async = tc.async();

    UriSpec uriSpec = new UriSpec("{scheme}://foo.com:{port}");
    ServiceInstance<String> instance1 = ServiceInstance.<String>builder()
      .name("foo-service")
      .payload(new JsonObject().put("foo", "bar").encodePrettily())
      .port(8080)
      .uriSpec(uriSpec)
      .build();

    ServiceInstance<String> instance2 = ServiceInstance.<String>builder()
      .name("foo-service")
      .payload(new JsonObject().put("foo", "bar2").encodePrettily())
      .port(8081)
      .uriSpec(uriSpec)
      .build();

    discovery.registerService(instance1);

    sd.registerServiceImporter(
      new ZookeeperServiceImporter(),
      new JsonObject().put("connection", zkTestServer.getConnectString())).onComplete(
      tc.asyncAssertSuccess(v -> {
        waitUntil(() -> serviceLookup(sd, 1), tc.asyncAssertSuccess(list -> {
          tc.assertEquals(list.get(0).getName(), "foo-service");
          vertx.executeBlocking(() -> {
            this.discovery.registerService(instance2);
            return null;
          }).onComplete(tc.asyncAssertSuccess(v2 -> {
            waitUntil(() -> serviceLookup(sd, 2), tc.asyncAssertSuccess(lookup -> {
              tc.assertEquals(lookup.get(0).getName(), "foo-service");
              tc.assertEquals(lookup.get(1).getName(), "foo-service");
              async.complete();
            }));
          }));
        }));
      }));
  }

  private void fetchRecords(AtomicBoolean marker, TestContext tc) {
    sd.getRecords(x -> true).onComplete(l -> {
      if (l.succeeded() && l.result().size() == 1) {
        tc.assertEquals("foo-service", l.result().get(0).getName());
        marker.set(true);
      } else {
        vertx.setTimer(100, x -> fetchRecords(marker, tc));
      }
    });
  }


  @Test
  public void testReconnection(TestContext tc) throws Exception {
    UriSpec uriSpec = new UriSpec("{scheme}://foo.com:{port}");
    ServiceInstance<String> instance = ServiceInstance.<String>builder()
      .name("foo-service")
      .payload(new JsonObject().put("foo", "bar").encodePrettily())
      .port((int) (65535 * Math.random()))
      .uriSpec(uriSpec)
      .build();

    discovery.registerService(instance);


    AtomicBoolean importDone = new AtomicBoolean();
    sd.registerServiceImporter(
      new ZookeeperServiceImporter(),
      new JsonObject()
        .put("connection", zkTestServer.getConnectString())
        .put("connectionTimeoutMs", 10)
        .put("baseSleepTimeBetweenRetries", 10)
        .put("maxRetries", 3)).onComplete(
      v -> {
        if (v.failed()) {
          v.cause().printStackTrace();
          tc.fail(v.cause());
          return;
        }
        tc.assertTrue(v.succeeded());

        // The registration of the service in ZK can take some time,
        // So we must be sure the service is there
        // We retries until it's done. There is a timeout set at 10 seconds.
        fetchRecords(importDone, tc);
      });

    await().untilTrue(importDone);

    // Stop the server
    zkTestServer.stop();

    importDone.set(false);
    sd.getRecords(x -> true).onComplete(l -> {
      if (l.failed()) {
        l.cause().printStackTrace();
      }
      tc.assertTrue(l.succeeded());
      tc.assertTrue(l.result().size() == 1);
      tc.assertEquals("foo-service", l.result().get(0).getName());

      importDone.set(true);
    });

    await().untilAtomic(importDone, is(true));

    zkTestServer.start();

    importDone.set(false);
    sd.getRecords(x -> true).onComplete(l -> {
      if (l.failed()) {
        l.cause().printStackTrace();
      }
      tc.assertTrue(l.succeeded());
      tc.assertTrue(l.result().size() == 1);
      tc.assertEquals("foo-service", l.result().get(0).getName());

      importDone.set(true);
    });

    await().untilAtomic(importDone, is(true));

  }


  private <T> void waitUntil(Supplier<Future<T>> supplier, Handler<AsyncResult<T>> handler) {
    execute(0, supplier, handler);
  }

  private <T> void execute(int counter, Supplier<Future<T>> supplier,
                           Handler<AsyncResult<T>> handler) {
    supplier.get().onComplete(ar -> {
      if (ar.succeeded()) {
        handler.handle(Future.succeededFuture(ar.result()));
      } else {
        if (counter > 20) {
          Exception failure = new Exception("Max attempt reached", ar.cause());
          handler.handle(Future.failedFuture(failure));
        } else {
          vertx.setTimer(100, l -> {
            execute(counter + 1, supplier, handler);
          });
        }
      }
    });
  }
}
