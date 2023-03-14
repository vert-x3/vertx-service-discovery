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

package io.vertx.servicediscovery.rest;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Objects;

/**
 * Allows publishing the vert.x service discovery as a REST endpoint. It supports retrieving services,
 * but also publish, withdraw and modify services.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ServiceDiscoveryRestEndpoint {

  public static final String DEFAULT_ROOT = "/discovery";
  private final ServiceDiscovery discovery;


  /**
   * Creates the REST endpoint using the default root ({@code /discovery}).
   *
   * @param router    the vert.x web router
   * @param discovery the service discovery instance
   * @return the created endpoint
   */
  public static ServiceDiscoveryRestEndpoint create(Router router, ServiceDiscovery discovery) {
    return new ServiceDiscoveryRestEndpoint(router, discovery, DEFAULT_ROOT);
  }

  /**
   * Creates the REST endpoint using the given root.
   *
   * @param router    the vert.x web router
   * @param discovery the service discovery instance
   * @param root      the endpoint path (root)
   * @return the created endpoint
   */
  public static ServiceDiscoveryRestEndpoint create(Router router, ServiceDiscovery discovery, String root) {
    return new ServiceDiscoveryRestEndpoint(router, discovery, root);
  }

  /**
   * Creates a new {@link ServiceDiscoveryRestEndpoint}.
   *
   * @param router    the router
   * @param discovery the service discovery instance
   * @param root      the root
   */
  private ServiceDiscoveryRestEndpoint(Router router, ServiceDiscovery discovery, String root) {
    Objects.requireNonNull(router);
    Objects.requireNonNull(discovery);
    Objects.requireNonNull(root);
    this.discovery = discovery;
    registerRoutes(router, root);
  }


  /**
   * Registers the routes.
   *
   * @param router the router
   * @param root   the root
   */
  private void registerRoutes(Router router, String root) {
    // Get all and query
    router.get(root).handler(this::all);

    // Get one
    router.get(root + "/:uuid").handler(this::one);

    // Unpublish
    router.delete(root + "/:uuid").handler(this::unpublish);

    // Publish
    router.route().handler(BodyHandler.create());
    router.post(root).handler(this::publish);

    // Update
    router.put(root + "/:uuid").handler(this::update);
  }


  private void update(RoutingContext routingContext) {
    String uuid = routingContext.request().getParam("uuid");
    JsonObject json = routingContext.body().asJsonObject();
    Record record = new Record(json);

    if (!uuid.equals(record.getRegistration())) {
      routingContext.fail(400);
      return;
    }

    discovery.update(record).onComplete(ar -> {
      if (ar.failed()) {
        routingContext.fail(ar.cause());
      } else {
        routingContext.response().setStatusCode(200)
            .putHeader("Content-Type", "application/json")
            .end(ar.result().toJson().toString());
      }
    });
  }

  private void unpublish(RoutingContext routingContext) {
    String uuid = routingContext.request().getParam("uuid");
    discovery.unpublish(uuid).onComplete(ar -> {
      if (ar.failed()) {
        routingContext.fail(ar.cause());
      } else {
        routingContext.response().setStatusCode(204).end();
      }
    });
  }

  private void one(RoutingContext routingContext) {
    discovery.getRecord(new JsonObject().put("registration", routingContext.request().getParam("uuid"))).onComplete(ar -> {
      if (ar.failed()) {
        routingContext.fail(ar.cause());
      } else {
        if (ar.result() == null) {
          routingContext.response().setStatusCode(404).end();
        } else {
          routingContext.response().setStatusCode(200)
              .putHeader("Content-Type", "application/json")
              .end(ar.result().toJson().toString());
        }
      }
    });
  }

  private void publish(RoutingContext routingContext) {
    JsonObject json = routingContext.body().asJsonObject();
    Record record = new Record(json);
    discovery.publish(record).onComplete(ar -> {
      if (ar.failed()) {
        routingContext.fail(ar.cause());
      } else {
        routingContext.response().setStatusCode(201)
            .putHeader("Content-Type", "application/json")
            .end(ar.result().toJson().toString());
      }
    });
  }


  private void all(RoutingContext routingContext) {
    String query = routingContext.request().params().get("query");
    JsonObject filter = new JsonObject();
    if (query != null) {
      try {
        String decoded = URLDecoder.decode(query, "UTF-8");
        filter = new JsonObject(decoded);
      } catch (UnsupportedEncodingException e) {
        routingContext.fail(e);
        return;
      }
    }
    discovery.getRecords(filter).onComplete(ar -> {
      if (ar.failed()) {
        routingContext.fail(ar.cause());
      } else {
        JsonArray jsonArray = ar.result().stream()
          .map(Record::toJson)
          .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
        routingContext.json(jsonArray);
      }
    });
  }

}
