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

package io.vertx.servicediscovery.kubernetes;

import io.vertx.core.*;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.parsetools.JsonParser;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.Status;
import io.vertx.servicediscovery.spi.ServiceImporter;
import io.vertx.servicediscovery.spi.ServicePublisher;
import io.vertx.servicediscovery.spi.ServiceType;
import io.vertx.servicediscovery.types.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Boolean.parseBoolean;
import static java.util.stream.Collectors.toSet;

/**
 * A discovery bridge listening for kubernetes services and publishing them in the Vert.x service discovery.
 * This bridge only supports the importation of services from kubernetes in vert.x (and not the opposite).
 * <p>
 * The bridge is configured using:
 * <p>
 * * the oauth token (using the content of `/var/run/secrets/kubernetes.io/serviceaccount/token` by default)
 * * the namespace in which the service are searched (defaults to `default`).
 * <p>
 * Be aware that the application must have access to Kubernetes and must be able to read the chosen namespace.
 * <p>
 * {@link Record} are created from Kubernetes Service. The service type is deduced from the `service-type` label. If
 * not set, the service is imported as `unknown`. Only `http-endpoint` are supported for now.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class KubernetesServiceImporter implements ServiceImporter {

  private static final Logger LOGGER = LoggerFactory.getLogger(KubernetesServiceImporter.class.getName());

  private static final Set<String> SUPPORTED_EVENT_TYPES = Stream.of(
    "BOOKMARK",
    "ADDED",
    "DELETED",
    "ERROR",
    "MODIFIED"
  ).collect(toSet());

  public static final String KUBERNETES_UUID = "kubernetes.uuid";

  private final Map<RecordKey, Record> records = new HashMap<>();
  private final Map<RecordKey, ServiceSelector> selectorsByService = new HashMap<>();
  private final Map<RecordKey, List<String>> runningPodsByService = new HashMap<>();
  private final Map<String, List<RecordKey>> servicesByPod = new HashMap<>();

  private ContextInternal context;
  private ServicePublisher publisher;
  private KubernetesClient client;
  private String lastServiceResourceVersion;
  private String lastPodResourceVersion;
  private BatchOfUpdates batchOfUpdates;

  private volatile boolean stop;

  @Override
  public void start(final Vertx vertx, final ServicePublisher publisher, final JsonObject configuration, final Promise<Void> completion) {
    context = (ContextInternal) vertx.getOrCreateContext();
    context.runOnContext(v -> init(publisher, configuration, completion));
  }

  private void init(final ServicePublisher publisher, final JsonObject configuration, final Promise<Void> completion) {
    this.publisher = publisher;

    KubernetesClient.create(context, configuration)
      .onSuccess(c -> this.client = c)
      // Initial import and publish of services.
      .compose(v -> retrieveServices())
      .onSuccess(items -> LOGGER.info("Kubernetes initial import of " + items.size() + " services"))
      .compose(this::publishRecords)
      .onComplete(ar -> {
        if (ar.succeeded()) {
          LOGGER.info("Kubernetes importer instantiated with " + records.size() + " services imported");
          completion.complete();
          watchServices();
          fetchAndWatchPods();
        } else {
          LOGGER.error("Error while interacting with kubernetes", ar.cause());
          completion.fail(ar.cause());
        }
      });
  }

  @Override
  public void close(final Handler<Void> completionHandler) {
    stop = true;
    if (context != null) {
      context.runOnContext(v -> {
        if (batchOfUpdates != null) {
          batchOfUpdates.cancel();
        }
        client.close(completionHandler);
        client = null;
        if (completionHandler != null) {
          completionHandler.handle(null);
        }
      });
    } else if (completionHandler != null) {
      completionHandler.handle(null);
    }
  }


  /* ------------------------------------------------------------------------ */
  /*                              RECORD METHODS                              */
  /* ------------------------------------------------------------------------ */

  Record createRecord(final JsonObject service) {
    JsonObject metadata = service.getJsonObject("metadata");
    Record record = new Record()
      .setName(metadata.getString("name"));

    RecordKey key = new RecordKey(record);
    List<String> pods = runningPodsByService.get(key);
    if (pods == null) {
      record.setStatus(Status.UNKNOWN);
    } else if (pods.isEmpty()) {
      record.setStatus(Status.OUT_OF_SERVICE);
    } else {
      record.setStatus(Status.UP);
    }

    JsonObject labels = metadata.getJsonObject("labels");
    if (labels != null) {
      labels.forEach(entry -> record.getMetadata().put(entry.getKey(), entry.getValue().toString()));
    }

    JsonObject selector = service.getJsonObject("spec").getJsonObject("selector");
    if (selector != null) {
      record.getMetadata().put("selector", selector);
    }

    record.getMetadata().put("kubernetes.namespace", metadata.getString("namespace"));
    record.getMetadata().put("kubernetes.name", metadata.getString("name"));
    record.getMetadata().put(KUBERNETES_UUID, metadata.getString("uid"));

    String type = record.getMetadata().getString("service-type");

    // If not set, try to discovery it
    if (type == null) {
      type = discoveryType(service, record);
    }

    switch (type) {
      case HttpEndpoint.TYPE:
        manageHttpService(record, service);
        break;
      // TODO Add JDBC client, redis and mongo
      default:
        manageUnknownService(record, service, type);
        break;
    }

    return record;
  }

  static String discoveryType(final JsonObject service, final Record record) {
    JsonObject spec = service.getJsonObject("spec");
    JsonArray ports = spec.getJsonArray("ports");
    if (ports == null || ports.isEmpty()) {
      return ServiceType.UNKNOWN;
    }

    if (ports.size() > 1) {
      LOGGER.warn("More than one ports has been found for " + record.getName() + " - taking the " +
        "first one to build the record location");
    }

    JsonObject port = ports.getJsonObject(0);
    int p = port.getInteger("port");

    // Http
    if (p == 80 || p == 443 || p >= 8080 && p <= 9000) {
      return HttpEndpoint.TYPE;
    }

    // PostGreSQL
    if (p == 5432 || p == 5433) {
      return JDBCDataSource.TYPE;
    }

    // MySQL
    if (p == 3306 || p == 13306) {
      return JDBCDataSource.TYPE;
    }

    // Redis
    if (p == 6379) {
      return RedisDataSource.TYPE;
    }

    // Mongo
    if (p == 27017 || p == 27018 || p == 27019) {
      return MongoDataSource.TYPE;
    }

    return ServiceType.UNKNOWN;
  }

  private CompositeFuture publishRecords(final JsonArray items) {
    List<Future> publications = new ArrayList<>();
    items.forEach(s -> {
      JsonObject svc = ((JsonObject) s);
      Record record = createRecord(svc);
      if (addRecordIfNotContained(record)) {
        Promise<Record> promise = context.promise();
        publishRecord(record, promise);
        publications.add(promise.future());
      }
    });
    return CompositeFuture.all(publications);
  }

  private void publishRecord(final Record record, final Handler<AsyncResult<Record>> completionHandler) {
    publisher.publish(record, ar -> {
      if (completionHandler != null) {
        completionHandler.handle(ar);
      }
      if (ar.succeeded()) {
        RecordKey key = new RecordKey(record);
        ServiceSelector selector = ServiceSelector.of(record);
        selectorsByService.put(key, selector);

        LOGGER.info("Kubernetes service published in the vert.x service registry: " + record.toJson());
      } else {
        LOGGER.error("Kubernetes service not published in the vert.x service registry", ar.cause());
      }
    });
  }

  private void unpublishRecord(final Record record, final Handler<Void> completionHandler) {
    publisher.unpublish(record.getRegistration(), ar -> {
      if (ar.failed()) {
        LOGGER.error("Cannot unregister kubernetes service", ar.cause());
      } else {
        LOGGER.info("Kubernetes service unregistered from the vert.x registry: " + record.toJson());
        if (completionHandler != null) {
          completionHandler.handle(null);
        }
      }
      RecordKey key = new RecordKey(record);
      selectorsByService.remove(key);
    });
  }

  private boolean addRecordIfNotContained(final Record record) {
    return records.putIfAbsent(new RecordKey(record), record) == null;
  }

  private Record removeRecordIfContained(final Record record) {
    return records.remove(new RecordKey(record));
  }

  private Record replaceRecordIfContained(final Record record) {
    RecordKey key = new RecordKey(record);
    Record old = records.remove(key);
    if (old != null) {
      records.put(key, record);
    }
    return old;
  }


  /* ------------------------------------------------------------------------ */
  /*                              SERVICE METHODS                             */
  /* ------------------------------------------------------------------------ */

  private Future<JsonArray> retrieveServices() {
    return client.listServices().compose(serviceList -> {
      lastServiceResourceVersion = serviceList.getJsonObject("metadata").getString("resourceVersion");
      JsonArray items = serviceList.getJsonArray("items");
      if (!serviceList.containsKey("items")) {
        return context.failedFuture("Unable to retrieve services: no items. [namespace=" + client.getNamespace() + "]");
      } else {
        return context.succeededFuture(items);
      }
    });
  }

  /**
   * Handles service events incrementally using Kubernetes' 'watch' feed.
   * <p>
   * If the connection is interrupted, the client will resume watching from the {@link #lastServiceResourceVersion}.
   * However, if the connection fails it will call {@link #fetchAndWatchServices()} to re-process the entire {@code ServiceList}.
   */
  private void watchServices() {
    if (stop) {
      return;
    }

    JsonParser parser = JsonParser.newParser().objectValueMode()
      .handler(event -> addToBatch(event.objectValue()));

    client.watchServices(lastServiceResourceVersion, parser)
      .onComplete(res -> {
        if (res.succeeded()) {
          watchServices();
        } else {
          LOGGER.error("Failure while watching service list:", res.cause());
          fetchAndWatchServices();
        }
      });
  }

  /**
   * Fetch and publish the latest services from Kubernetes.
   * <p>
   * Ideally the initial fetch succeeds and we handle updates incrementally with {@link #watchServices()};
   * if not, it will periodically poll the {@code /services} endpoint.
   */
  private void fetchAndWatchServices() {
    if (!stop) {
      context.setTimer(2000, l -> {
        retrieveServices()
          .compose(this::publishRecords)
          .onComplete(res -> {
            if (res.succeeded()) {
              watchServices();
            } else {
              fetchAndWatchServices();
            }
          });
      });
    }
  }

  /**
   * Process events in batches to avoid redundant publications.
   *
   * @see <a href="https://github.com/vert-x3/vertx-service-discovery/issues/146">vertx-service-discovery issue #146</a>
   */
  private void addToBatch(final JsonObject json) {
    if (batchOfUpdates == null) {
      long timerId = context.setTimer(500, l -> processBatch());
      batchOfUpdates = new BatchOfUpdates(context.owner(), timerId);
    }
    batchOfUpdates.objects.add(json);
  }

  private void processBatch() {
    Map<Object, JsonObject> objects = compact(batchOfUpdates.objects);
    batchOfUpdates = null;
    for (JsonObject json : objects.values()) {
      onChunk(json);
    }
  }

  private Map<Object, JsonObject> compact(final List<JsonObject> source) {
    Map<Object, JsonObject> res = new HashMap<>();
    for (JsonObject json : source) {
      String type = json.getString("type");
      if (type == null || !SUPPORTED_EVENT_TYPES.contains(type)) {
        continue;
      }
      JsonObject object = json.getJsonObject("object");
      if ("BOOKMARK".equals(type)) {
        res.merge("BOOKMARK", json, (oldVal, newVal) -> newVal);
      } else {
        RecordKey key = new RecordKey(createRecord(object));
        if ("DELETED".equals(type) || "ERROR".equals(type)) {
          res.put(key, json);
        } else {
          JsonObject oldVal = res.get(key);
          if (oldVal == null) {
            res.put(key, json);
          } else {
            oldVal.put("object", object);
          }
        }
      }
    }
    return res;
  }

  private void onChunk(JsonObject json) {
    String type = json.getString("type");
    JsonObject object = json.getJsonObject("object");
    switch (type) {
      case "BOOKMARK":
        lastServiceResourceVersion = object.getJsonObject("metadata").getString("resourceVersion");
        break;
      case "ADDED":
        // new service
        Record record = createRecord(object);
        if (addRecordIfNotContained(record)) {
          LOGGER.info("Adding service " + record.getName());
          publishRecord(record, null);
        }
        break;
      case "DELETED":
      case "ERROR":
        // remove service
        record = createRecord(object);
        LOGGER.info("Removing service " + record.getName());
        Record storedRecord = removeRecordIfContained(record);
        if (storedRecord != null) {
          unpublishRecord(storedRecord, null);
        }
        break;
      case "MODIFIED":
        record = createRecord(object);
        LOGGER.info("Modifying service " + record.getName());
        storedRecord = replaceRecordIfContained(record);
        if (storedRecord != null) {
          unpublishRecord(storedRecord, x -> publishRecord(record, null));
        }
    }
  }


  /* ------------------------------------------------------------------------ */
  /*                                POD METHODS                               */
  /* ------------------------------------------------------------------------ */

  private Future<JsonArray> retrievePods() {
    return client.listPods().compose(podList -> {
      lastPodResourceVersion = podList.getJsonObject("metadata").getString("resourceVersion");
      JsonArray items = podList.getJsonArray("items");
      if (!podList.containsKey("items") || podList.getJsonArray("items").isEmpty()) {
        return context.failedFuture("Unable to retrieve pods: no items. [namespace=" + client.getNamespace() + "].");
      } else {
        return context.succeededFuture(items);
      }
    });
  }

  private void watchPods() {
    if (stop) {
      return;
    }

    JsonParser parser = JsonParser.newParser().objectValueMode()
      .handler(event -> associatePod(event.objectValue()));

    client.watchPods(lastPodResourceVersion, parser)
      .onComplete(res -> {
        if (res.succeeded()) {
          watchPods();
        } else {
          LOGGER.error("Failure while watching pods list", res.cause());
          fetchAndWatchPods();
        }
      });
  }

  /**
   * Fetch the current pods from Kubernetes.
   * <p>
   * Ideally the initial fetch succeeds and we handle updates incrementally with {@link #watchServices()};
   * if not, it will periodically poll the {@code /services} endpoint.
   */
  private void fetchAndWatchPods() {
    if (!stop) {
      context.setTimer(2000, l -> retrievePods().onComplete(res -> {
        if (res.succeeded()) {
          associatePods(res.result());
          watchPods();
        } else {
          fetchAndWatchPods();
        }
      }));
    }
  }

  private void associatePods(final JsonArray pods) {
    for (Object pod : pods) {
      associatePod((JsonObject) pod);
    }
  }

  private void associatePod(final JsonObject pod) {
    String podId = pod.getJsonObject("metadata").getString(KUBERNETES_UUID);
    boolean isTracked = servicesByPod.containsKey(podId);

    String status = pod.getJsonObject("status").getString("phase");
    switch (status) {
      case "Running":
        JsonObject metadata = pod.getJsonObject("metadata");
        if (!metadata.containsKey("labels")) {
          return;
        }

        Map<String, String> labels = metadata.getJsonObject("labels").stream()
          .filter(entry -> entry.getValue() instanceof String)
          .collect(Collectors.toMap(Map.Entry::getKey, entry -> (String) entry.getValue()));

        for (Map.Entry<RecordKey, ServiceSelector> entry : selectorsByService.entrySet()) {
          ServiceSelector selector = entry.getValue();
          if (selector.matchesLabels(labels)) {
            RecordKey key = entry.getKey();
            addPodForService(key, podId);
          }
        }
        break;
      case "Pending":
      case "Succeeded":
      case "Failed":
        // The pod is no longer running: remove it from all services.
        if (isTracked) {
          removePodFromServices(podId);
        }
        break;
    }
  }

  private Future<Void> addPodForService(final RecordKey key, final String podId) {
    List<String> runningPods = runningPodsByService.computeIfAbsent(key, k -> new ArrayList<>());
    runningPods.add(podId);
    servicesByPod.computeIfAbsent(podId, k -> new ArrayList<>())
      .add(key);

    if (runningPods.size() == 1) {
      return updateRecordStatus(key, Status.UP);
    } else {
      return context.succeededFuture();
    }
  }

  private CompositeFuture removePodFromServices(final String podId) {
    List<Future> updates = new ArrayList<>();

    List<RecordKey> keys = servicesByPod.remove(podId);
    keys.forEach(key -> {
      List<String> runningPods = runningPodsByService.get(key);
      runningPods.remove(podId);
      if (runningPods.isEmpty()) {
        updates.add(updateRecordStatus(key, Status.OUT_OF_SERVICE));
      }
    });

    return CompositeFuture.all(updates);
  }

  private Future<Void> updateRecordStatus(final RecordKey key, final Status status) {
    Promise<Record> promise = context.promise();
    Record record = records.get(key);
    if (record.getStatus() == status) {
      promise.complete();
    } else {
      LOGGER.info("Modifying service: changing status to " + status + ". [id=" + record.getName() + "]");
      record.setStatus(status);
      Record storedRecord = replaceRecordIfContained(record);
      if (storedRecord != null) {
        unpublishRecord(storedRecord, x -> publishRecord(record, promise));
      }
    }
    return promise.future().mapEmpty();
  }

  /* ------------------------------------------------------------------------ */
  /*                              HELPER METHODS                              */
  /* ------------------------------------------------------------------------ */

  private static void manageUnknownService(final Record record, final JsonObject service, final String type) {
    JsonObject spec = service.getJsonObject("spec");
    JsonArray ports = spec.getJsonArray("ports");
    if (ports != null && !ports.isEmpty()) {
      if (ports.size() > 1) {
        LOGGER.warn("More than one ports has been found for " + record.getName() + " - taking the " +
          "first one to build the record location");
      }
      JsonObject port = ports.getJsonObject(0);
      JsonObject location = port.copy();

      if (isExternalService(service)) {
        location.put("host", spec.getString("externalName"));
      } else {
        //Number or name of the port to access on the pods targeted by the service.
        Object targetPort = port.getValue("targetPort");
        if (targetPort instanceof Integer) {
          location.put("internal-port", targetPort);
        }
        location.put("host", spec.getString("clusterIP"));
      }

      record.setLocation(location).setType(type);
    } else {
      throw new IllegalStateException("Cannot extract the location from the service " + record + " - no port");
    }
  }

  private static void manageHttpService(final Record record, final JsonObject service) {
    JsonObject spec = service.getJsonObject("spec");
    JsonArray ports = spec.getJsonArray("ports");

    if (ports != null && !ports.isEmpty()) {
      if (ports.size() > 1) {
        LOGGER.warn("More than one port has been found for " + record.getName() + " - taking the first" +
          " one to extract the HTTP endpoint location");
      }

      JsonObject port = ports.getJsonObject(0);
      Integer p = port.getInteger("port");

      record.setType(HttpEndpoint.TYPE);

      HttpLocation location = new HttpLocation(port.copy());

      if (isExternalService(service)) {
        location.setHost(spec.getString("externalName"));
      } else {
        location.setHost(spec.getString("clusterIP"));
      }

      if (parseBoolean(record.getMetadata().getString("ssl")) || p != null && p == 443) {
        location.setSsl(true);
      }
      record.setLocation(location.toJson());
    } else {
      throw new IllegalStateException("Cannot extract the HTTP URL from the service " + record + " - no port");
    }
  }

  private static boolean isExternalService(final JsonObject service) {
    return service.containsKey("spec") && service.getJsonObject("spec").containsKey("type") && service.getJsonObject("spec").getString("type").equals("ExternalName");
  }
}
