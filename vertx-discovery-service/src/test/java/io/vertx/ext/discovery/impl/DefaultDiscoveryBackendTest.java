package io.vertx.ext.discovery.impl;

import io.vertx.core.Vertx;
import io.vertx.ext.discovery.Record;
import io.vertx.ext.discovery.Status;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DefaultDiscoveryBackendTest {


  protected DefaultDiscoveryBackend backend;
  protected Vertx vertx;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    backend = new DefaultDiscoveryBackend();
    backend.init(vertx);
  }

  @After
  public void tearDown() {
    AtomicBoolean completed = new AtomicBoolean();
    vertx.close(ar -> completed.set(ar.succeeded()));
    await().untilAtomic(completed, is(true));
  }

  @Test
  public void testInsertion() {
    Record record = new Record().setName("my-service").setStatus(Status.UP);
    assertThat(record.getRegistration()).isNull();

    // Insertion
    AtomicReference<Record> reference = new AtomicReference<>();
    backend.store(record, ar -> {
      if (! ar.succeeded()) {
        ar.cause().printStackTrace();
      }
      reference.set(ar.result());
    });

    await().until(() -> reference.get() != null);
    assertThat(reference.get().getName()).isEqualToIgnoringCase("my-service");
    assertThat(reference.get().getRegistration()).isNotNull();
    record = reference.get();

    // Retrieve
    reference.set(null);
    backend.getRecord(record.getRegistration(), ar -> reference.set(ar.result()));
    await().until(() -> reference.get() != null);
    assertThat(reference.get().getName()).isEqualToIgnoringCase("my-service");
    assertThat(reference.get().getRegistration()).isNotNull();

    // Remove
    AtomicBoolean completed = new AtomicBoolean();
    backend.remove(record, ar -> {
      completed.set(ar.succeeded());
    });
    await().untilAtomic(completed, is(true));

    // Retrieve
    completed.set(false);
    reference.set(null);
    backend.getRecord(record.getRegistration(), ar -> {
      if (!ar.succeeded()) {
        ar.cause().printStackTrace();
      }
      completed.set(ar.succeeded());
      reference.set(ar.result());
    });

    await().untilAtomic(completed, is(true));
    assertThat(reference.get()).isNull();
  }

  @Test
  public void testInsertionOfMultipleRecords() {
    Record record1 = new Record().setName("my-service-1").setStatus(Status.UP);
    assertThat(record1.getRegistration()).isNull();
    Record record2 = new Record().setName("my-service-2").setStatus(Status.UP);
    assertThat(record2.getRegistration()).isNull();
    Record record3 = new Record().setName("my-service-3").setStatus(Status.UP);
    assertThat(record3.getRegistration()).isNull();

    // Insertion
    AtomicBoolean completed = new AtomicBoolean();
    backend.store(record1, ar -> {
          backend.store(record2, ar2 -> {
            backend.store(record3, ar3 -> {
              completed.set(ar3.succeeded());
            });
          });
        }
    );

    await().untilAtomic(completed, is(true));

    List<Record> records = new ArrayList<>();
    // Get records
    backend.getRecords(ar -> {
       records.addAll(ar.result());
    });
    await().until(() -> ! records.isEmpty());

    assertThat(records).hasSize(3);

    // Get each records
    for(Record record : records) {
      AtomicReference<Record> retrieved = new AtomicReference<>();
      backend.getRecord(record.getRegistration(), ar -> retrieved.set(ar.result()));
      await().untilAtomic(retrieved, not(nullValue()));
    }
  }

}