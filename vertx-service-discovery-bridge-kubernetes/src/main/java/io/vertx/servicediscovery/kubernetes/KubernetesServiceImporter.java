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
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.parsetools.JsonParser;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.spi.ServiceImporter;
import io.vertx.servicediscovery.spi.ServicePublisher;
import io.vertx.servicediscovery.spi.ServiceType;
import io.vertx.servicediscovery.types.*;

import java.util.*;
import java.util.stream.Stream;

import static io.vertx.core.http.HttpMethod.GET;
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

  private ContextInternal context;
  private ServicePublisher publisher;
  private String token;
  private String namespace;
  private HttpClient client;
  private String lastResourceVersion;
  private BatchOfUpdates batchOfUpdates;

  private volatile boolean stop;

  private static final String OPENSHIFT_KUBERNETES_TOKEN_FILE = "/var/run/secrets/kubernetes.io/serviceaccount/token";

  @Override
  public void start(Vertx vertx, ServicePublisher publisher, JsonObject configuration, Promise<Void> completion) {
    context = (ContextInternal) vertx.getOrCreateContext();
    context.runOnContext(v -> init(publisher, configuration, completion));
  }

  private void init(ServicePublisher publisher, JsonObject configuration, Promise<Void> completion) {
    this.publisher = publisher;

    JsonObject conf;
    if (configuration == null) {
      conf = new JsonObject();
    } else {
      conf = configuration;
    }

    int port = conf.getInteger("port", 0);
    if (port == 0) {
      if (conf.getBoolean("ssl", true)) {
        port = 443;
      } else {
        port = 80;
      }
    }

    String p = System.getenv("KUBERNETES_SERVICE_PORT");
    if (p != null) {
      port = Integer.parseInt(p);
    }

    String host = conf.getString("host");
    String h = System.getenv("KUBERNETES_SERVICE_HOST");
    if (h != null) {
      host = h;
    }

    client = context.owner().createHttpClient(new HttpClientOptions()
      .setTrustAll(true)
      .setSsl(conf.getBoolean("ssl", true))
      .setDefaultHost(host)
      .setDefaultPort(port)
    );

    // Retrieve token
    Future<Void> retrieveTokenFuture = retrieveToken(conf);

    // 1) get kubernetes auth info
    this.namespace = conf.getString("namespace", getNamespaceOrDefault());
    LOGGER.info("Kubernetes discovery configured for namespace: " + namespace);
    LOGGER.info("Kubernetes master url: http" + (conf.getBoolean("ssl", true) ? "s" : "") + "//" + host + ":" + port);

    retrieveTokenFuture
      .compose(v -> retrieveServices())
      .onSuccess(items -> LOGGER.info("Kubernetes initial import of " + items.size() + " services"))
      .compose(this::publishRecords)
      .onComplete(ar -> {
        if (ar.succeeded()) {
          LOGGER.info("Kubernetes importer instantiated with " + records.size() + " services imported");
          completion.complete();
          watch();
        } else {
          LOGGER.error("Error while interacting with kubernetes", ar.cause());
          completion.fail(ar.cause());
        }
      });
  }

  private Future<JsonArray> retrieveServices() {
    String path = "/api/v1/namespaces/" + namespace + "/services";
    return client.request(GET, path).compose(request -> {
      request.setFollowRedirects(true);
      request.putHeader("Authorization", "Bearer " + token);
      return request.send();
    }).compose(response -> {
      return response.body().compose(body -> {
        if (response.statusCode() != 200) {
          return context.failedFuture("Unable to retrieve services from namespace " + namespace + ", status code: "
            + response.statusCode() + ", content: " + body.toString());
        } else {
          JsonObject serviceList = body.toJsonObject();
          lastResourceVersion = serviceList.getJsonObject("metadata").getString("resourceVersion");
          JsonArray items = serviceList.getJsonArray("items");
          if (!serviceList.containsKey("items")) {
            return context.failedFuture("Unable to retrieve services from namespace " + namespace + " - no items");
          } else {
            return context.succeededFuture(items);
          }
        }
      });
    });
  }

  private CompositeFuture publishRecords(JsonArray items) {
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

  private void watch() {
    if (stop) {
      return;
    }
    String path = "/api/v1/namespaces/" + namespace + "/services?"
      + "watch=true"
      + "&"
      + "allowWatchBookmarks=true"
      + "&"
      + "resourceVersion=" + lastResourceVersion;

    JsonParser parser = JsonParser.newParser().objectValueMode()
      .handler(event -> addToBatch(event.objectValue()));

    client.request(GET, path).compose(request -> {
      request.setFollowRedirects(true);
      request.putHeader("Authorization", "Bearer " + token);
      return request.send();
    }).compose(response -> {
      Promise<Void> promise = Promise.promise();
      if (response.statusCode() == 200) {
        LOGGER.info("Watching services from namespace " + namespace);
        response
          .exceptionHandler(t -> promise.tryComplete())
          .endHandler(v -> promise.tryComplete())
          .handler(parser);
      } else {
        promise.fail("");
      }
      return promise.future();
    }).onComplete(res -> {
      if (res.succeeded()) {
        watch();
      } else {
        LOGGER.error("Failure while watching service list", res.cause());
        fetchAndWatch();
      }
    });
  }

  private void fetchAndWatch() {
    if (!stop) {
      context.setTimer(2000, l -> {
        retrieveServices()
          .compose(this::publishRecords)
          .onComplete(res -> {
            if (res.succeeded()) {
              watch();
            } else {
              fetchAndWatch();
            }
          });
      });
    }
  }

  private void addToBatch(JsonObject json) {
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

  private Map<Object, JsonObject> compact(List<JsonObject> source) {
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
        lastResourceVersion = object.getJsonObject("metadata").getString("resourceVersion");
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

  private Future<Void> retrieveToken(JsonObject conf) {
    Future<String> result;
    String token = conf.getString("token");
    if (token != null && !token.trim().isEmpty()) {
      result = context.succeededFuture(token);
    } else {
      result = context.owner().fileSystem().readFile(OPENSHIFT_KUBERNETES_TOKEN_FILE).map(Buffer::toString);
    }
    return result.onSuccess(tk -> this.token = tk).mapEmpty();
  }

  private void publishRecord(Record record, Handler<AsyncResult<Record>> completionHandler) {
    publisher.publish(record).onComplete(ar -> {
      if (completionHandler != null) {
        completionHandler.handle(ar);
      }
      if (ar.succeeded()) {
        LOGGER.info("Kubernetes service published in the vert.x service registry: "
          + record.toJson());
      } else {
        LOGGER.error("Kubernetes service not published in the vert.x service registry",
          ar.cause());
      }
    });
  }

  private boolean addRecordIfNotContained(Record record) {
    return records.putIfAbsent(new RecordKey(record), record) == null;
  }

  private String getNamespaceOrDefault() {
    // Kubernetes with Fabric8 build
    String ns = System.getenv("KUBERNETES_NAMESPACE");
    if (ns == null) {
      // oc / docker build
      ns = System.getenv("OPENSHIFT_BUILD_NAMESPACE");
      if (ns == null) {
        ns = "default";
      }
    }
    return ns;
  }

  static Record createRecord(JsonObject service) {
    JsonObject metadata = service.getJsonObject("metadata");
    Record record = new Record()
      .setName(metadata.getString("name"));

    JsonObject labels = metadata.getJsonObject("labels");

    if (labels != null) {
      labels.forEach(entry -> record.getMetadata().put(entry.getKey(), entry.getValue().toString()));
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

  static String discoveryType(JsonObject service, Record record) {
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

  private static void manageUnknownService(Record record, JsonObject service, String type) {
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

  private static void manageHttpService(Record record, JsonObject service) {
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

  private static boolean isExternalService(JsonObject service) {
    return service.containsKey("spec") && service.getJsonObject("spec").containsKey("type") && service.getJsonObject("spec").getString("type").equals("ExternalName");
  }


  @Override
  public void close(Handler<Void> completionHandler) {
    stop = true;
    if (context != null) {
      context.runOnContext(v -> {
        if (batchOfUpdates != null) {
          batchOfUpdates.cancel();
        }
        client.close();
        client = null;
        if (completionHandler != null) {
          completionHandler.handle(null);
        }
      });
    } else if (completionHandler != null) {
      completionHandler.handle(null);
    }
  }

  private void unpublishRecord(Record record, Handler<Void> completionHandler) {
    publisher.unpublish(record.getRegistration()).onComplete(ar -> {
      if (ar.failed()) {
        LOGGER.error("Cannot unregister kubernetes service", ar.cause());
      } else {
        LOGGER.info("Kubernetes service unregistered from the vert.x registry: " + record.toJson());
        if (completionHandler != null) {
          completionHandler.handle(null);
        }
      }
    });
  }

  private Record removeRecordIfContained(Record record) {
    return records.remove(new RecordKey(record));
  }

  private Record replaceRecordIfContained(Record record) {
    RecordKey key = new RecordKey(record);
    Record old = records.remove(key);
    if (old != null) {
      records.put(key, record);
    }
    return old;
  }

  private static class RecordKey {
    final String uuid;
    final String endpoint;

    RecordKey(Record record) {
      this.uuid = Objects.requireNonNull(record.getMetadata().getString(KUBERNETES_UUID));
      this.endpoint = record.getLocation().getString(Record.ENDPOINT, "");
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      RecordKey recordKey = (RecordKey) o;

      return uuid.equals(recordKey.uuid) && endpoint.equals(recordKey.endpoint);
    }

    @Override
    public int hashCode() {
      int result = uuid.hashCode();
      result = 31 * result + endpoint.hashCode();
      return result;
    }

    @Override
    public String toString() {
      return "RecordKey{" + "uuid='" + uuid + '\'' + ", endpoint='" + endpoint + '\'' + '}';
    }
  }

  private static class BatchOfUpdates {
    final Vertx vertx;
    final long timerId;
    final List<JsonObject> objects = new ArrayList<>();

    public BatchOfUpdates(Vertx vertx, long timerId) {
      this.vertx = vertx;
      this.timerId = timerId;
    }

    public void cancel() {
      vertx.cancelTimer(timerId);
    }
  }
}
