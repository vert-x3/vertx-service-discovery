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
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.streams.WriteStream;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.spi.ServiceImporter;
import io.vertx.servicediscovery.spi.ServicePublisher;
import io.vertx.servicediscovery.spi.ServiceType;
import io.vertx.servicediscovery.types.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

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

  private final static Logger LOGGER = LoggerFactory.getLogger(KubernetesServiceImporter.class.getName());

  private ServicePublisher publisher;
  private String namespace;
  private List<Record> records = new CopyOnWriteArrayList<>();
  private WebClient client;

  private static final String OPENSHIFT_KUBERNETES_TOKEN_FILE = "/var/run/secrets/kubernetes.io/serviceaccount/token";

  @Override
  public void start(Vertx vertx, ServicePublisher publisher, JsonObject configuration,
                    Future<Void> completion) {
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
      port = Integer.valueOf(p);
    }

    String host = conf.getString("host");
    String h = System.getenv("KUBERNETES_SERVICE_HOST");
    if (h != null) {
      host = h;
    }

    client = WebClient.create(vertx,
      new WebClientOptions()
        .setTrustAll(true)
        .setSsl(conf.getBoolean("ssl", true))
        .setDefaultHost(host)
        .setDefaultPort(port)
        .setFollowRedirects(true)
    );

    // Retrieve token
    Future<String> retrieveTokenFuture = getToken(vertx, conf);

    // 1) get kubernetes auth info
    this.namespace = conf.getString("namespace", getNamespaceOrDefault());
    LOGGER.info("Kubernetes discovery configured for namespace: " + namespace);
    LOGGER.info("Kubernetes master url: http" + (conf.getBoolean("ssl", true) ? "s" : "") + "//" + host + ":" + port);

    retrieveTokenFuture
      .map(t -> {
        LOGGER.info("Kubernetes discovery: Bearer Token { " + t + " }");
        return t;
      })
      .compose(this::retrieveServices)
      .map(list -> {
        LOGGER.info("Kubernetes initial import of " + list.size() + " services");
        List<Future> publications = new ArrayList<>();
        list.forEach(s -> {
          JsonObject svc = ((JsonObject) s);
          Record record = createRecord(svc);
          if (addRecordIfNotContained(record)) {
            Future<Record> fut = Future.future();
            publishRecord(record, fut.completer());
            publications.add(fut);
          }
        });
        return publications;
      })
      .compose(CompositeFuture::all)
      .setHandler(ar -> {
        if (ar.succeeded()) {
          LOGGER.info("Kubernetes importer instantiated with " + records.size() + " services imported");
          completion.complete();
        } else {
          LOGGER.error("Error while interacting with kubernetes", ar.cause());
          completion.fail(ar.cause());
        }
      });
  }

  private Future<JsonArray> retrieveServices(String token) {
    Future<JsonArray> future = Future.future();
    String path = "/api/v1/namespaces/" + namespace + "/services";
    client.get(path)
      .putHeader("Authorization", "Bearer " + token)
      .send(resp -> {
        if (resp.failed()) {
          future.fail(resp.cause());
        } else if (resp.result().statusCode() != 200) {
          future.fail("Unable to retrieve services from namespace " + namespace + ", status code: "
            + resp.result().statusCode() + ", content: " + resp.result().bodyAsString());
        } else {
          HttpResponse<Buffer> response = resp.result();
          JsonArray items = response.bodyAsJsonObject().getJsonArray("items");
          if (items == null) {
            future.fail("Unable to retrieve services from namespace " + namespace + " - no items");
          } else {
            future.complete(items);
            watch(client, token, response.bodyAsJsonObject().getJsonObject("metadata").getString("resourceVersion"));
          }
        }
      });
    return future;
  }

  private void watch(WebClient client, String token, String resourceVersion) {
    String path = "/api/v1/namespaces/" + namespace + "/services";
    client.get(path)
      .addQueryParam("watch", "true")
      .addQueryParam("resourceVersion", resourceVersion)
      .as(BodyCodec.pipe(new WriteStream<Buffer>() {
        @Override
        public WriteStream<Buffer> exceptionHandler(Handler<Throwable> handler) {
          return this;
        }

        @Override
        public WriteStream<Buffer> write(Buffer data) {
          String[] chunks = data.toString().split("\n");

          Buffer current = Buffer.buffer();

          for (String chunk: chunks) {
            Optional<JsonObject> maybeJson = maybeJson(current, chunk);
            if (maybeJson.isPresent()) {
              current = Buffer.buffer();
              onChunk(maybeJson.get());
            } else {
              current.appendString(chunk);
            }
          }
          return this;
        }

        @Override
        public void end() {

        }

        @Override
        public WriteStream<Buffer> setWriteQueueMaxSize(int maxSize) {
          return this;
        }

        @Override
        public boolean writeQueueFull() {
          return false;
        }

        @Override
        public WriteStream<Buffer> drainHandler(Handler<Void> handler) {
          return this;
        }
      }))
      .putHeader("Authorization", "Bearer " + token)
      .send(ar -> {
        if (ar.failed()) {
          LOGGER.error("Unable to setup the watcher on the service list", ar.cause());
        } else {
          LOGGER.info("Watching services from namespace " + namespace);
        }
      });
  }

  /**
   * Checks whether or not the given buffer + chunk is a valid JSON object.
   * @param buffer the current, may be empty, must not be {@code null}
   * @param chunk the chunk, may be empty, must not be {@code null}
   * @return an Optional wrapping the JSON object on success.
   */
  private Optional<JsonObject> maybeJson(Buffer buffer, String chunk) {
    Buffer buff = buffer.copy().appendString(chunk);
    try {
      return Optional.of(buff.toJsonObject());
    } catch (DecodeException e) {
      // Not a valid JSON, waiting for the next chunk
      return Optional.empty();
    }
  }

  private void onChunk(JsonObject json) {
    String type = json.getString("type");
    if (type == null) {
      return;
    }
    JsonObject service = json.getJsonObject("object");
    switch (type) {
      case "ADDED":
        // new service
        Record record = createRecord(service);
        if (addRecordIfNotContained(record)) {
          LOGGER.info("Adding service "  + record.getName());
          publishRecord(record, null);
        }
        break;
      case "DELETED":
      case "ERROR":
        // remove service
        record = createRecord(service);
        LOGGER.info("Removing service "  + record.getName());
        Record storedRecord = removeRecordIfContained(record);
        if (storedRecord != null) {
          unpublishRecord(storedRecord);
        }
        break;
      case "MODIFIED":
        record = createRecord(service);
        LOGGER.info("Modifying service "  + record.getName());
        storedRecord = removeRecordIfContained(record);
        if (storedRecord != null) {
          publishRecord(record, null);
        }
    }
  }

  private Future<String> getToken(Vertx vertx, JsonObject conf) {
    Future<String> result = Future.future();

    String token = conf.getString("token");
    if (token != null && !token.trim().isEmpty()) {
      result.complete(token);
      return result;
    }

    // Read from file
    vertx.fileSystem().readFile(OPENSHIFT_KUBERNETES_TOKEN_FILE, ar -> {
      if (ar.failed()) {
        result.tryFail(ar.cause());
      } else {
        result.tryComplete(ar.result().toString());
      }
    });

    return result;
  }

  private void publishRecord(Record record, Handler<AsyncResult<Record>> completionHandler) {
    publisher.publish(record, ar -> {
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

  private synchronized boolean addRecordIfNotContained(Record record) {
    for (Record rec : records) {
      if (areTheSameService(rec, record)) {
        return false;
      }
    }
    return records.add(record);
  }

  private String getNamespaceOrDefault() {
    // Kubernetes with Fabric8 build
    String namespace = System.getenv("KUBERNETES_NAMESPACE");
    if (namespace == null) {
      // oc / docker build
      namespace = System.getenv("OPENSHIFT_BUILD_NAMESPACE");
      if (namespace == null) {
        namespace = "default";
      }
    }
    return namespace;
  }

  private boolean areTheSameService(Record record1, Record record2) {
    String uuid = record1.getMetadata().getString("kubernetes.uuid", "");
    String uuid2 = record2.getMetadata().getString("kubernetes.uuid", "");
    String endpoint = record1.getLocation().getString(Record.ENDPOINT, "");
    String endpoint2 = record2.getLocation().getString(Record.ENDPOINT, "");

    // Check the uuid and location
    return uuid.equals(uuid2) && endpoint.equals(endpoint2);
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
    record.getMetadata().put("kubernetes.uuid", metadata.getString("uid"));

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
          location.put("internal-port", (Integer) targetPort);
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

      if (isTrue(record.getMetadata().getString("ssl")) || p != null && p == 443) {
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

  private static boolean isTrue(String ssl) {
    return "true".equalsIgnoreCase(ssl);
  }


  @Override
  public void close(Handler<Void> completionHandler) {
    synchronized (this) {
      if (client != null) {
        client.close();
        client = null;
      }
    }

    if (completionHandler != null) {
      completionHandler.handle(null);
    }
  }

  private void unpublishRecord(Record record) {
    publisher.unpublish(record.getRegistration(), ar -> {
      if (ar.failed()) {
        LOGGER.error("Cannot unregister kubernetes service", ar.cause());
      } else {
        LOGGER.info("Kubernetes service unregistered from the vert.x registry: " + record.toJson());
      }
    });
  }

  private Record removeRecordIfContained(Record record) {
    for (Record rec : records) {
      if (areTheSameService(rec, record)) {
        records.remove(rec);
        return rec;
      }
    }
    return null;
  }
}
