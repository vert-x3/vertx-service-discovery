/*
 * Copyright (c) 2011-2016 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *      The Eclipse Public License is available at
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *      The Apache License v2.0 is available at
 *      http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.servicediscovery;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.impl.DiscoveryImpl;
import io.vertx.servicediscovery.types.HttpEndpoint;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static com.jayway.awaitility.Awaitility.await;
import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Test the {@link Record} class behavior.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class RecordTest {

  private Vertx vertx;
  private ServiceDiscovery discovery;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    discovery = new DiscoveryImpl(vertx, new ServiceDiscoveryOptions());
  }

  @After
  public void tearDown() {
    discovery.close();
    AtomicBoolean completed = new AtomicBoolean();
    vertx.close().onComplete((v) -> completed.set(true));
    await().untilAtomic(completed, is(true));

    Assertions.assertThat(discovery.bindings()).isEmpty();
  }


  @Test
  public void testMatch() {
    Record record = new Record().setName("Name");

    assertThat(record.match(new JsonObject().put("name", "Name"))).isTrue();
    assertThat(record.match(new JsonObject().put("name", "Name-2"))).isFalse();

    record.setStatus(Status.UP);
    assertThat(record.match(new JsonObject().put("status", "Up"))).isTrue();
    assertThat(record.match(new JsonObject().put("status", "Down"))).isFalse();
    assertThat(record.match(new JsonObject().put("status", "Up").put("name", "Name"))).isTrue();
    assertThat(record.match(new JsonObject().put("status", "Down").put("name", "Name"))).isFalse();

    record.setRegistration("the-registration");
    assertThat(record.match(new JsonObject().put("registration", "the-registration"))).isTrue();
    assertThat(record.match(new JsonObject().put("registration", "wrong"))).isFalse();

    record.getMetadata().put("foo", "bar").put("key", 2);
    assertThat(record.match(new JsonObject().put("foo", "bar"))).isTrue();
    assertThat(record.match(new JsonObject().put("foo", "bar2"))).isFalse();
    assertThat(record.match(new JsonObject().put("foo", "bar").put("other", "nope"))).isFalse();
    assertThat(record.match(new JsonObject().put("foo", "bar").put("other", "*"))).isFalse();
    assertThat(record.match(new JsonObject().put("foo", "bar").put("key", 2))).isTrue();
    assertThat(record.match(new JsonObject().put("foo", "*").put("key", 2))).isTrue();
  }

  @Test
  public void TestTypeMatch() {
    Record record = new Record().setName("Name").setType(HttpEndpoint.TYPE);

    assertThat(record.match(new JsonObject().put("name", "Name").put("type", "any"))).isFalse();
    assertThat(record.match(new JsonObject().put("name", "Name").put("type", HttpEndpoint.TYPE))).isTrue();
    assertThat(record.match(new JsonObject().put("type", HttpEndpoint.TYPE))).isTrue();

    assertThat(record.match(new JsonObject().put("name", "Name").put("type", "*"))).isTrue();
  }

  @Test
  public void testMatchWithFilterFunction() {
    Record record1 = new Record().setName("Name");
    Record record2 = new Record().setName("foo").setMetadata(new JsonObject().put("key", "A"));
    Record record3 = new Record().setName("bar").setMetadata(new JsonObject().put("key", "B"));
    Record record4 = new Record().setName("bob").setMetadata(new JsonObject().put("key", "B"))
        .setStatus(Status.OUT_OF_SERVICE);
    Record record5 = new Record().setName("baz").setMetadata(new JsonObject().put("key", "A"));

    AtomicBoolean done = new AtomicBoolean();
    discovery.publish(record1)
      .compose(res -> discovery.publish(record2))
      .compose(res -> discovery.publish(record3))
      .compose(res -> discovery.publish(record4))
      .compose(res -> discovery.publish(record5))
      .onComplete(ar -> done.set(true));

    await().untilAtomic(done, is(true));


    AtomicReference<Record> match = new AtomicReference<>();
    done.set(false);
    discovery.getRecord(r -> r.getName().equals("Name")).onComplete(result -> {
      match.set(result.result());
      done.set(true);
    });
    await().untilAtomic(match, is(notNullValue()));
    assertThat(match.get().getRegistration()).isNotNull().isEqualTo(record1.getRegistration());

    match.set(null);
    done.set(false);
    discovery.getRecord(r -> r.getName().equals("Name-nope")).onComplete(result -> {
      match.set(result.result());
      done.set(true);
    });
    await().untilAtomic(done, is(true));
    assertThat(match.get()).isNull();

    match.set(null);
    done.set(false);
    discovery.getRecord(r -> "A".equals(r.getMetadata().getString("key"))).onComplete(result -> {
      match.set(result.result());
      done.set(true);
    });
    await().untilAtomic(done, is(true));
    assertThat(match.get()).isNotNull();
    assertThat(match.get().getName()).isIn("foo", "baz");

    match.set(null);
    done.set(false);
    discovery.getRecord(r -> "A".equals(r.getMetadata().getString("key")) && r.getName().equals("baz")).onComplete(
        result -> {
          match.set(result.result());
          done.set(true);
        });
    await().untilAtomic(done, is(true));
    assertThat(match.get()).isNotNull();
    assertThat(match.get().getName()).isEqualToIgnoringCase("baz");


    match.set(null);
    done.set(false);
    discovery.getRecord(r -> "A".equals(r.getMetadata().getString("key")) && r.getName().equals("boom")).onComplete(
        result -> {
          match.set(result.result());
          done.set(true);
        });
    await().untilAtomic(done, is(true));
    assertThat(match.get()).isNull();

    match.set(null);
    done.set(false);
    discovery.getRecord(r -> true).onComplete(
        result -> {
          match.set(result.result());
          done.set(true);
        });
    await().untilAtomic(done, is(true));
    assertThat(match.get()).isNotNull();

    List<Record> matches = new ArrayList<>();
    done.set(false);
    discovery.getRecords(r -> true).onComplete(
        result -> {
          matches.addAll(result.result());
          done.set(true);
        });
    await().untilAtomic(done, is(true));
    Assertions.assertThat(matches).hasSize(4);

    matches.clear();
    done.set(false);
    discovery.getRecords(r -> true, true).onComplete(
        result -> {
          matches.addAll(result.result());
          done.set(true);
        });
    await().untilAtomic(done, is(true));
    Assertions.assertThat(matches).hasSize(5);

    matches.clear();
    done.set(false);
    discovery.getRecords(r -> r.getName().equals("Name")).onComplete(
        result -> {
          matches.addAll(result.result());
          done.set(true);
        });
    await().untilAtomic(done, is(true));
    Assertions.assertThat(matches).hasSize(1);

    matches.clear();
    done.set(false);
    discovery.getRecords(r -> r.getName().equals("Name-Nope")).onComplete(
        result -> {
          matches.addAll(result.result());
          done.set(true);
        });
    await().untilAtomic(done, is(true));
    Assertions.assertThat(matches).hasSize(0);

    matches.clear();
    done.set(false);
    discovery.getRecords(r -> "A".equals(r.getMetadata().getString("key"))).onComplete(
        result -> {
          matches.addAll(result.result());
          done.set(true);
        });
    await().untilAtomic(done, is(true));
    Assertions.assertThat(matches).hasSize(2);

    matches.clear();
    done.set(false);
    discovery.getRecords(r -> "B".equals(r.getMetadata().getString("key"))).onComplete(
        result -> {
          matches.addAll(result.result());
          done.set(true);
        });
    await().untilAtomic(done, is(true));
    Assertions.assertThat(matches).hasSize(1);

    matches.clear();
    done.set(false);
    discovery.getRecords(r -> "B".equals(r.getMetadata().getString("key")), true).onComplete(
        result -> {
          matches.addAll(result.result());
          done.set(true);
        });
    await().untilAtomic(done, is(true));
    Assertions.assertThat(matches).hasSize(2);

    try {
      discovery.getRecord((Function<Record, Boolean>) null);
      fail("NPE expected");
    } catch (NullPointerException e) {
      // OK
    }

    try {
      discovery.getRecords((Function<Record, Boolean>) null);
      fail("NPE expected");
    } catch (NullPointerException e) {
      // OK
    }

    // Null json filter
    matches.clear();
    done.set(false);
    discovery.getRecords((JsonObject) null).onComplete(
        result -> {
          matches.addAll(result.result());
          done.set(true);
        });
    await().untilAtomic(done, is(true));
    Assertions.assertThat(matches).hasSize(4);

    matches.clear();
    done.set(false);
    discovery.getRecord((JsonObject) null).onComplete(
        result -> {
          matches.add(result.result());
          done.set(true);
        });
    await().untilAtomic(done, is(true));
    Assertions.assertThat(matches).hasSize(1);

    // Test with status set to *
    matches.clear();
    done.set(false);
    discovery.getRecords(new JsonObject().put("status", "*")).onComplete(
        result -> {
          matches.addAll(result.result());
          done.set(true);
        });
    await().untilAtomic(done, is(true));
    Assertions.assertThat(matches).hasSize(5);
  }

  @Test
  public void testReferenceEqualityAndHashcode() {
    String recordJson =
        "  {\n" +
            "    \"name\": \"theServiceDiscovery\",\n" +
            "    \"type\": \"theServiceType\",\n" +
            "    \"location\": {},\n" +
            "    \"metadata\": {},\n" +
            "    \"registration\": \"theUUID\",\n" +
            "    \"status\": \"UNKNOWN\"\n" +
            "  }";
    Record record1 = new Record(new JsonObject(recordJson));
    Record record2 = new Record(new JsonObject(recordJson));
    Assertions.assertThat(record1).isEqualTo(record2);
    Assertions.assertThat(record1.hashCode()).isEqualTo(record2.hashCode());
  }
}
