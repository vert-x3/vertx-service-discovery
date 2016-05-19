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

package io.vertx.ext.discovery.consul;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.discovery.DiscoveryService;
import io.vertx.ext.discovery.Record;
import io.vertx.ext.discovery.impl.ServiceTypes;
import io.vertx.ext.discovery.spi.DiscoveryBridge;
import io.vertx.ext.discovery.spi.ServiceType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ConsulDiscoveryBridge implements DiscoveryBridge {

  // TODO periodic scan
  // TODO maintenance


  private DiscoveryService discovery;
  private HttpClient client;

  private final static Logger LOGGER = LoggerFactory.getLogger(ConsulDiscoveryBridge.class);

  private final List<ImportedConsulService> imports = new ArrayList<>();
  private String dc;

  @Override
  public void start(Vertx vertx, DiscoveryService discovery, JsonObject configuration, Future<Void> completion) {
    this.discovery = discovery;

    HttpClientOptions options = new HttpClientOptions(configuration);
    String host = configuration.getString("host", "localhost");
    int port = configuration.getInteger("port", 8500);

    options.setDefaultHost(host);
    options.setDefaultPort(port);

    dc = configuration.getString("dc");
    client = vertx.createHttpClient(options);

    Future<Void> imports = Future.future();
    Future<Void> exports = Future.future();

    retrieveServicesFromConsul(imports);

    CompositeFuture.all(imports, exports).setHandler(ar -> {
      if (ar.succeeded()) {
        completion.complete();
      } else {
        completion.fail(ar.cause());
      }
    });
  }


  private Handler<Throwable> getErrorHandler(Future future) {
    return t -> {
      if (future != null) {
        future.fail(t);
      } else {
        LOGGER.error(t);
      }
    };
  }

  private void retrieveServicesFromConsul(Future<Void> completed) {
    String path = "/v1/catalog/services";
    if (dc != null) {
      path += "?dc=" + dc;
    }

    Handler<Throwable> error = getErrorHandler(completed);

    client.get(path)
        .exceptionHandler(error)
        .handler(response -> {
          System.out.println(response.statusMessage());
          response
              .exceptionHandler(error)
              .bodyHandler(buffer -> {
                System.out.println(buffer);
                retrieveIndividualServices(buffer.toJsonObject(), completed);
              });
        })
        .end();
  }

  private void retrieveIndividualServices(JsonObject jsonObject, Future<Void> completed) {

    List<Future> futures = new ArrayList<>();
    jsonObject.fieldNames().stream().forEach(name -> {
      System.out.println(name);
      Future future = Future.future();
      Handler<Throwable> error = getErrorHandler(future);
      String path = "/v1/catalog/service/" + name;
      if (dc != null) {
        path += "?dc=" + dc;
      }

      client.get(path)
          .exceptionHandler(error)
          .handler(response -> {
            System.out.println(response.statusMessage());
            response.exceptionHandler(error)
                .bodyHandler(buffer -> {
                  System.out.println(buffer);
                  importService(buffer.toJsonObject(), future);
                });
          })
          .end();

      futures.add(future);
    });

    CompositeFuture.all(futures).setHandler(ar -> {
      if (ar.succeeded()) {
        completed.succeeded();
      } else {
        completed.fail(ar.cause());
      }
    });
  }

  private void importService(JsonObject jsonObject, Future<Void> future) {
    String address = jsonObject.getString("Address");
    String name = jsonObject.getString("ServiceName");
    JsonArray tags = jsonObject.getJsonArray("ServiceTags");
    if (tags == null) {
      tags = new JsonArray();
    }
    String path = jsonObject.getString("ServiceAddress");
    int port = jsonObject.getInteger("ServicePort");

    JsonObject metadata = jsonObject.copy();
    System.out.println(tags);
    tags.stream().forEach(tag -> metadata.put((String) tag, true));

    Record record = new Record()
        .setName(name)
        .setMetadata(metadata);

    // To determine the record type, check if we have a tag with a "type" name
    record.setType(ServiceType.UNKNOWN);
    ServiceTypes.all().forEachRemaining(type -> {
      if (metadata.getBoolean(type.name(), false)) {
        record.setType(type.name());
      }
    });

    JsonObject location = new JsonObject();
    location.put("host", address);
    location.put("port", port);
    if (path != null) {
      location.put("path", path);
    }

    // Manage HTTP endpoint
    if (record.getType().equals("http-endpoint")) {
      if (path != null) {
        location.put("root", path);
      }
      if (metadata.getBoolean("ssl", false)) {
        location.put("ssl", true);
      }
    }

    LOGGER.info("Importing service " + record.getName() + " from consul");
    imports.add(new ImportedConsulService(name, record).register(discovery, future));

  }

  @Override
  public void stop(Vertx vertx, DiscoveryService discovery, Future<Void> future) {
    // Remove all the services that has been imported
    List<Future> list = new ArrayList<>();
    imports.stream().forEach(imported -> {
      imported.unregister(discovery, ar -> {
        LOGGER.info("Unregistering " + imported.name());
        if (ar.succeeded()) {
          list.add(Future.succeededFuture());
        } else {
          list.add(Future.failedFuture(ar.cause()));
        }
      });
    });

    CompositeFuture.all(list).setHandler(ar -> {
      if (ar.succeeded()) {
        future.complete();
      } else {
        future.fail(ar.cause());
      }
    });
  }
}
