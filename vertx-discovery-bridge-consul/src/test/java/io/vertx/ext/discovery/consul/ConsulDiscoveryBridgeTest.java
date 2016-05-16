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

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.discovery.DiscoveryService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.core.Is.is;

/**
 * Test against a mock Consul server
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ConsulDiscoveryBridgeTest {


  private Vertx vertx;

  private List<JsonObject> services = new ArrayList<>();
  private HttpServer server;
  private DiscoveryService discovery;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();

    AtomicBoolean done = new AtomicBoolean();
    server = vertx.createHttpServer()
        .requestHandler(request -> {
          System.out.println(request.path());
          if (request.path().equals("/v1/catalog/services")) {
            JsonObject result = new JsonObject();
            services.stream().forEach(object -> {
              result.put(object.getString("ServiceName"), object.getJsonArray("tags", new JsonArray()));
            });
            request.response().end(result.encodePrettily());
          } else if (request.path().startsWith("/v1/catalog/service/")) {
            String service = request.path().substring("/v1/catalog/service/".length());
            System.out.println("getting details about " + service);
            JsonObject value = find(service);
            if (value != null) {
              request.response().end(value.encodePrettily());
            } else {
              request.response().setStatusCode(404).end();
            }
          }
        })
        .listen(5601, ar -> {
          done.set(ar.succeeded());
        });

    await().untilAtomic(done, is(true));
  }

  @After
  public void tearDown() {
    if (discovery != null) {
      discovery.close();
    }
    AtomicBoolean done = new AtomicBoolean();
    server.close(ar -> {
      done.set(true);
    });
    await().untilAtomic(done, is(true));
    done.set(false);
    vertx.close(ar -> {
      done.set(true);
    });
    await().untilAtomic(done, is(true));
  }

  @Test
  public void test() throws InterruptedException {
    services.add(new JsonObject("  {\n" +
        "    \"Node\": \"foobar\",\n" +
        "    \"Address\": \"10.1.10.12\",\n" +
        "    \"ServiceID\": \"redis\",\n" +
        "    \"ServiceName\": \"redis\",\n" +
        "    \"ServiceTags\": null,\n" +
        "    \"ServiceAddress\": \"\",\n" +
        "    \"ServicePort\": 8000\n" +
        "  }"));

    DiscoveryService.create(vertx)
        .registerDiscoveryBridge(new ConsulDiscoveryBridge(),
            new JsonObject().put("host", "localhost").put("port", 5601));

    Thread.sleep(5000);
  }

  private JsonObject find(String service) {
    for (JsonObject json : services) {
      if (json.getString("ServiceName").equalsIgnoreCase(service)) {
        return json;
      }
    }
    return null;
  }


}