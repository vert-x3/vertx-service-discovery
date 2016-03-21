package io.vertx.ext.discovery.types;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.discovery.*;
import io.vertx.ext.discovery.impl.DiscoveryImpl;
import io.vertx.ext.service.HelloService;
import io.vertx.ext.service.HelloServiceImpl;
import io.vertx.serviceproxy.ProxyHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ServiceProxiesTest {

  private Vertx vertx;
  private DiscoveryService discovery;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    discovery = new DiscoveryImpl(vertx, new DiscoveryOptions());
  }

  @After
  public void tearDown() {
    discovery.close();
    AtomicBoolean completed = new AtomicBoolean();
    vertx.close((v) -> completed.set(true));
    await().untilAtomic(completed, is(true));
  }

  @Test
  public void test() {
    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");
    Record record = EventBusService.createRecord("Hello", HelloService.class, "address");

    discovery.publish(record, (r) -> {
    });
    await().until(() -> record.getRegistration() != null);

    AtomicReference<Record> found = new AtomicReference<>();
    discovery.getRecord(new JsonObject().put("name", "Hello"), ar -> {
      found.set(ar.result());
    });

    await().until(() -> found.get() != null);
    assertThat(found.get().getLocation().getString("endpoint")).isEqualTo("address");

    ServiceReference service = DiscoveryService.getServiceReference(vertx, found.get());
    HelloService hello = service.get();
    AtomicReference<String> result = new AtomicReference<>();
    hello.hello("vert.x", ar -> {
      result.set(ar.result());
    });
    await().untilAtomic(result, not(nullValue()));

    service.release();
  }

  @Test
  public void testUsingGetMethod() {
    HelloService svc = new HelloServiceImpl("stuff");
    ProxyHelper.registerService(HelloService.class, vertx, svc, "address");
    Record record = EventBusService.createRecord("Hello", HelloService.class, "address");

    discovery.publish(record, (r) -> {
    });
    await().until(() -> record.getRegistration() != null);

    AtomicReference<HelloService> found = new AtomicReference<>();
    EventBusService.get(vertx, discovery, HelloService.class, ar -> {
      found.set(ar.result());
    });
    await().until(() -> found.get() != null);
    assertThat(EventBusService.bindings()).hasSize(1);

    HelloService hello = found.get();
    AtomicReference<String> result = new AtomicReference<>();
    hello.hello("vert.x", ar -> {
      result.set(ar.result());
    });
    await().untilAtomic(result, not(nullValue()));

    EventBusService.release(found.get());

    assertThat(EventBusService.bindings()).isEmpty();
  }
}
