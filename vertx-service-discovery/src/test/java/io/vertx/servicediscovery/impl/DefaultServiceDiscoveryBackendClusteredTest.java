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
import org.junit.Before;

import static com.jayway.awaitility.Awaitility.await;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DefaultServiceDiscoveryBackendClusteredTest extends DefaultServiceDiscoveryBackendTest {


  @Before
  public void setUp() {
    Vertx.clusteredVertx(new VertxOptions().setClusterHost("127.0.0.1"), ar -> {
      vertx = ar.result();
    });
    await().until(() -> vertx != null);
    backend = new DefaultServiceDiscoveryBackend();
    backend.init(vertx, new JsonObject());
  }


}