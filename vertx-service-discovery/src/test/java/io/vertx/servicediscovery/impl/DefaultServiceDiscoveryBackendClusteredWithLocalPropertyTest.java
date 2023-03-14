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

package io.vertx.servicediscovery.impl;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import org.junit.After;
import org.junit.Before;

import static com.jayway.awaitility.Awaitility.await;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DefaultServiceDiscoveryBackendClusteredWithLocalPropertyTest extends DefaultServiceDiscoveryBackendTest {


  @Before
  public void setUp() {
    System.setProperty("vertx-service-discovery-backend-local", "true");
    backend = new DefaultServiceDiscoveryBackend();
    VertxOptions options = new VertxOptions();
    options.getEventBusOptions().setHost("127.0.0.1");
    Vertx.clusteredVertx(options).onComplete(ar -> {
      backend.init(ar.result(), new JsonObject());
      vertx = ar.result();
    });
    await().until(() -> vertx != null);
  }

  @After
  public void cleanup() {
    System.clearProperty("vertx-service-discovery-backend-local");
  }


}
