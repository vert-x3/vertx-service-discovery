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

package io.vertx.servicediscovery.backend.redis;

import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackend;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackendTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import redis.embedded.RedisServer;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class RedisBackendConfiguredInJsonTest extends ServiceDiscoveryBackendTest {

  private static final Integer PORT = 6380;
  private static RedisServer server;

  @BeforeClass
  static public void startRedis() throws Exception {
    System.out.println("Creating redis server on port: " + PORT);
    server = new RedisServer(PORT);
    System.out.println("Created embedded redis server on port " + PORT);
    server.start();
  }

  @AfterClass
  static public void stopRedis() throws Exception {
    server.stop();
  }

  @Override
  protected ServiceDiscoveryBackend createBackend() {
    RedisBackendService backend = new RedisBackendService();
    backend.init(vertx, new JsonObject().put("connectionString", "redis://localhost:" + PORT).put("key", "vertx"));
    return backend;
  }

}
