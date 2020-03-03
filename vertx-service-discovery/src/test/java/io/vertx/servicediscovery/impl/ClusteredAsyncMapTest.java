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

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.test.fakecluster.FakeClusterManager;
import org.junit.runner.RunWith;

/**
 * Test the async map when running in clustered mode.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@RunWith(VertxUnitRunner.class)
public class ClusteredAsyncMapTest extends AsyncMapTest {

  @Override
  protected Future<Vertx> createVertx() {
    return Vertx.clusteredVertx(new VertxOptions());
  }

  @Override
  protected Future<AsyncMap<String, String>> getAsyncMap() {
    return vertx.sharedData().getClusterWideMap("some-name");
  }

  @Override
  public void tearDown(TestContext context) {
    super.tearDown(context);
    FakeClusterManager.reset();
  }
}
