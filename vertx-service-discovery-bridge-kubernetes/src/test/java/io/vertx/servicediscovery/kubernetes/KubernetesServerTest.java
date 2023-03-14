package io.vertx.servicediscovery.kubernetes;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.NamespacedKubernetesClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesMockServer;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@RunWith(VertxUnitRunner.class)
public class KubernetesServerTest {

  private Vertx vertx;
  private KubernetesMockServer server;
  private NamespacedKubernetesClient client;
  private int port;

  @Before
  public void setUp(TestContext tc) throws MalformedURLException {
    vertx = Vertx.vertx();
    vertx.exceptionHandler(tc.exceptionHandler());

    Service svc1 = getSimpleService("uuid-2", "my-service");
    Service svc2 = getHttpService();
    Service svc3 = getSimpleService("uid-3", "service3");

    server = getServer();

    server.expect().get().withPath("/api/v1/namespaces/default/services").andReturn(200, new ServiceListBuilder()
      .addToItems(svc1, svc2).withNewMetadata("1234", "/self").build()).always();

    server.expect().get().withPath("/api/v1/namespaces/default/services?watch=true&allowWatchBookmarks=true&resourceVersion=1234")
      .andReturnChunked(200
        , new WatchEvent(svc3, "ADDED"), "\n"
        , new WatchEvent(svc1, "DELETED"), "\n"
        , new WatchEvent(getUpdatedHttpService(), "MODIFIED"), "\n"
        , new WatchEvent(addLabelFooBar(getSimpleService("uid-3", "service3")), "MODIFIED"), "\n"
      ).once();

    Service svc96 = getService96();
    server.expect().get().withPath("/api/v1/namespaces/issue96/services").andReturn(200, new ServiceListBuilder()
      .addToItems(svc96)
      .withNewMetadata("1235", "/self").build()).always();
    server.expect().get().withPath("/api/v1/namespaces/issue96/services?watch=true&allowWatchBookmarks=true&resourceVersion=1235")
      .andReturnChunked(200,
        new WatchEvent(getUpdatedService96(), "MODIFIED"), "\n",
        new WatchEvent(getUpdatedService96(), "DELETED"), "\n").once();

    server.init();
    client = server.createClient();
    port = new URL(client.getConfiguration().getMasterUrl()).getPort();
  }

  @After
  public void tearDown() {
    vertx.exceptionHandler(null);
    server.destroy();
    vertx.close();
  }

  public KubernetesMockServer getServer() {
    return new KubernetesMockServer(false);
  }

  private JsonObject config() {
    String token = client.getConfiguration().getOauthToken();
    if (token == null) {
      token = "some-token";
    }
    return new JsonObject()
      .put("token", token)
      .put("host", "localhost")
      .put("ssl", false)
      .put("port", port);
  }

  @Test
  public void testInitialRetrieval(TestContext tc) {
    Async async = tc.async();
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions());
    discovery.registerServiceImporter(new KubernetesServiceImporter(), config().copy().put("namespace", "default")).onComplete(
      ar -> {
        if (ar.failed()) {
          tc.fail(ar.cause());
        } else {
          discovery.getRecords(s -> true).onComplete(res -> {
            if (res.failed()) {
              tc.fail(res.cause());
            } else {
              tc.assertEquals(2, res.result().size());
              async.complete();
            }
          });
        }
      });
  }

  @Test
  public void testWatch() {
    AtomicBoolean done = new AtomicBoolean();
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions());
    discovery.registerServiceImporter(new KubernetesServiceImporter(), config().copy().put("namespace", "default")).onComplete(
      ar -> done.set(ar.succeeded()));

    await().untilAtomic(done, is(true));

    await().until(() -> {
      List<Record> records = getRecordsBlocking(discovery);
      try {
        assertThat(records).hasSize(2).extracting(Record::getName)
          .containsOnly("service3", "my-http-service");
        assertThat(records.stream().filter(record -> record.getName().equals("service3")).findFirst())
          .hasValueSatisfying(record -> assertThat(record.getMetadata().getString("foo")).isEqualTo("bar"));
        return true;
      } catch (Throwable e) {
        return false;
      }
    });
  }

  /**
   * Reproduce issue https://github.com/vert-x3/vertx-service-discovery/issues/96.
   */
  @Test
  public void testWatchWithDeletion() {
    AtomicBoolean done = new AtomicBoolean();
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions());
    discovery.registerServiceImporter(new KubernetesServiceImporter(), config().copy().put("namespace", "issue96")).onComplete(
      ar -> done.set(ar.succeeded()));

    await().untilAtomic(done, is(true));

    await().until(() -> {
      List<Record> records = getRecordsBlocking(discovery);
      try {
        assertThatListDoesNotContain(records, "hello-minikube");
        return true;
      } catch (Throwable e) {
        return false;
      }
    });
  }

  private void assertThatListContains(List<Record> records, String name) {
    for (Record rec : records) {
      if (rec.getName().equalsIgnoreCase(name)) {
        return;
      }
    }
    throw new AssertionError("Cannot find service '" + name + "' in the list");
  }

  private void assertThatListDoesNotContain(List<Record> records, String name) {
    for (Record rec : records) {
      if (rec.getName().equalsIgnoreCase(name)) {
        throw new AssertionError("Found service '" + name + "' in the list");
      }
    }
  }

  private KubernetesResource getUpdatedHttpService() {
    Service service = getHttpService();
    return addLabelFooBar(service);
  }

  private Service addLabelFooBar(Service service) {
    Map<String, String> labels = service.getMetadata().getLabels();
    if (labels == null) {
      labels = new HashMap<>();
      service.getMetadata().setLabels(labels);
    }
    labels.put("foo", "bar");
    return service;
  }

  private List<Record> getRecordsBlocking(ServiceDiscovery discovery) {
    CountDownLatch latch = new CountDownLatch(1);

    List<Record> records = new ArrayList<>();
    discovery.getRecords(s -> true).onComplete(ar -> {
      records.addAll(ar.result());
      latch.countDown();
    });

    try {
      latch.await();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    return records;
  }

  private Service getSimpleService(String uid, String name) {
    ObjectMeta metadata = new ObjectMeta();
    metadata.setName(name);
    metadata.setUid(uid);
    metadata.setNamespace("my-project");

    ServiceSpec spec = new ServiceSpec();
    ServicePort port = new ServicePort();
    port.setTargetPort(new IntOrString(8080));
    port.setPort(1524);
    spec.setPorts(Collections.singletonList(port));

    Service service = new Service();
    service.setMetadata(metadata);
    service.setSpec(spec);
    return service;
  }

  private Service getHttpService() {
    Map<String, String> labels = new LinkedHashMap<>();
    labels.put("service-type", "http-endpoint");

    ObjectMeta metadata = new ObjectMeta();
    metadata.setName("my-http-service");
    metadata.setUid("uuid-1");
    metadata.setNamespace("my-project");
    metadata.setLabels(labels);

    ServiceSpec spec = new ServiceSpec();
    ServicePort port = new ServicePort();
    port.setTargetPort(new IntOrString(80));
    port.setPort(8080);
    spec.setPorts(Collections.singletonList(port));

    Service service = new Service();
    service.setMetadata(metadata);
    service.setSpec(spec);
    return service;
  }

  private Service getService96() {
    Map<String, String> labels = new LinkedHashMap<>();
    labels.put("service-type", "http-endpoint");

    ObjectMeta metadata = new ObjectMeta();
    metadata.setName("hello-minikube");
    metadata.setUid("37c57c1e-deb0-11e8-a8ee-0800274f8294");
    metadata.setNamespace("issue96");
    metadata.setLabels(labels);
    metadata.getAdditionalProperties().put("run", "hello-minikube");

    ServiceSpec spec = new ServiceSpec();
    ServicePort port = new ServicePort();
    port.setTargetPort(new IntOrString(80));
    port.setPort(8080);
    spec.setPorts(Collections.singletonList(port));

    Service service = new Service();
    service.setMetadata(metadata);
    service.setSpec(spec);
    return service;
  }

  private Service getUpdatedService96() {
    return getService96();
  }
}
