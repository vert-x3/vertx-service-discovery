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

package io.vertx.servicediscovery.docker;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.spi.ServiceImporter;
import io.vertx.servicediscovery.spi.ServicePublisher;
import io.vertx.servicediscovery.spi.ServiceType;
import io.vertx.servicediscovery.types.HttpEndpoint;
import io.vertx.servicediscovery.types.HttpLocation;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A discovery bridge analyzing environment variables to detect Docker links. It imports found links as services in
 * the discovery infrastructure. As they are mapped from environment variable the set of imported service does not
 * change after the initialization.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DockerLinksServiceImporter implements ServiceImporter {
  /**
   * The service discovery instance. Immutable once set.
   */
  private ServicePublisher publisher;

  /**
   * The list of records that are imported. Content staled once initialized.
   */
  private List<Record> records = new ArrayList<>();

  private final static Logger LOGGER = LoggerFactory.getLogger(DockerLinksServiceImporter.class);

  @Override
  public void start(Vertx vertx, ServicePublisher publisher, JsonObject configuration,
                    Promise<Void> completion) {
    this.publisher = publisher;

    synchronized (this) {
      lookup(completion);
    }
  }

  private void lookup(Promise<Void> completion) {
    Map<String, String> variables = getVariables();

    // Find names
    List<String> links = variables.keySet().stream()
        .filter(key -> key.endsWith("_NAME"))
        .map(key -> extractLinkName(key, variables))
        .filter(key -> key != null)
        .collect(Collectors.toList());

    LOGGER.info("Docker links: " + links);

    for (String link : links) {
      try {
        Record record = createRecord(link,
            variables.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(link + "_"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        LOGGER.info("Record created from link " + link + " : " + record);
        publisher.publish(record).onComplete(ar -> {
          if (ar.succeeded()) {
            records.add(ar.result());
            LOGGER.info("Service imported from Docker link : " + link + " with endpoint set to "
                + ar.result().getLocation().getString(Record.ENDPOINT));
          } else {
            LOGGER.error("Publication of the docker link " + link + " as a service failed", ar.cause());
          }
        });
      } catch (URISyntaxException e) {
        if (completion != null) {
          completion.fail(e);
        } else {
          throw new IllegalStateException("Cannot extract service record from variables for " + link, e);
        }
      }
    }

    if (completion != null) {
      completion.complete();
    }
  }

  private String extractLinkName(String key, Map<String, String> variables) {
    // We know it ends with _NAME;
    String name = key.substring(0, key.length() - "_NAME".length());
    if (name.isEmpty()) {
      // Weird case the key is just _NAME
      return null;
    } else {
      String port = name + "_PORT";
      if (variables.containsKey(port)) {
        return name;
      } else {
        // Not a valid link
        return null;
      }
    }
  }

  private Map<String, String> getVariables() {
    LinkedHashMap<String, String> vars = new LinkedHashMap<>();
    vars.putAll(System.getenv());
    System.getProperties().entrySet().forEach(entry ->
        vars.put(entry.getKey().toString(), entry.getValue().toString()));
    return vars;
  }

  @Override
  public void close(Handler<Void> completionHandler) {
    List<Future> list = new ArrayList<>();
    for (Record record : records) {
      publisher.unpublish(record.getRegistration()).onComplete(v -> list.add(v.succeeded() ? Future.succeededFuture() : Future.failedFuture(v.cause())));
    }

    CompositeFuture.all(list).onComplete(ar -> {
          if (ar.succeeded()) {
            LOGGER.info("Successfully closed the service importer " + this);
          } else {
            LOGGER.error("A failure has been caught while stopping " + this, ar.cause());
          }
          if (completionHandler != null) {
            completionHandler.handle(null);
          }
        }
    );
  }

  private Record createRecord(String name, Map<String, String> variables) throws URISyntaxException {
    Record record = new Record()
        .setName(name);

    // Add as metadata all entries
    variables.entrySet().forEach(entry -> {
      if (entry.getKey().startsWith(name + "_")) {
        String label = entry.getKey().substring((name + "_").length());
        record.getMetadata().put(label, entry.getValue());
      }
    });

    String type = variables.get(name + "_ENV_SERVICE_TYPE");
    if (type == null) {
      type = ServiceType.UNKNOWN;
    } else {
      LOGGER.info("Service type for " + name + " : " + type);
    }

    URI url = new URI(variables.get(name + "_PORT"));
    switch (type) {
      case "http-endpoint":
        HttpLocation http = new HttpLocation();
        http.setHost(url.getHost());
        http.setPort(url.getPort());
        if (isTrue(variables, name + "_ENV_SSL")) {
          http.setSsl(true);
        }
        record.setType(HttpEndpoint.TYPE);
        record.setLocation(http.toJson());
        break;
      default:
        JsonObject location = new JsonObject();
        location
            .put("endpoint", url.toString())
            .put("port", url.getPort())
            .put("host", url.getHost())
            .put("proto", url.getScheme());
        record.setType(type);
        record.setLocation(location);
    }

    return record;
  }

  private static boolean isTrue(Map<String, String> labels, String key) {
    return labels != null && "true".equalsIgnoreCase(labels.get(key));
  }
}
