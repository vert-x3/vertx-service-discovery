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
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@RunWith(VertxUnitRunner.class)
public class AsyncMapTest {

  Vertx vertx;
  AsyncMap<String, String> map;

  @Before
  public void setUp(TestContext context) {
    Async async = context.async();
    createVertx()
      .onSuccess(instance -> vertx = instance)
      .compose(v -> getAsyncMap())
      .onSuccess(m -> {
        map = m;
        async.complete();
      })
      .onFailure(context::fail);
    async.await();
  }

  protected Future<AsyncMap<String, String>> getAsyncMap() {
    return vertx.sharedData().getLocalAsyncMap("some-name");
  }

  protected Future<Vertx> createVertx() {
    return Future.succeededFuture(Vertx.vertx());
  }

  @After
  public void tearDown(TestContext context) {
    vertx.close().onComplete(context.asyncAssertSuccess());
  }

  @Test
  public void testPutGetAndRemove(TestContext context) {
    Async async = context.async();

    map.size().onComplete(s -> {
      context.assertTrue(s.succeeded());
      context.assertEquals(s.result(), 0);

      map.put("key", "value").onComplete(v ->
          map.size().onComplete(s2 -> {
            context.assertTrue(s2.succeeded());
            context.assertEquals(s2.result(), 1);

            map.get("key").onComplete(value -> {
              context.assertTrue(value.succeeded());
              context.assertEquals(value.result(), "value");

              map.remove("key").onComplete(old -> {
                context.assertTrue(old.succeeded());
                context.assertEquals(old.result(), "value");

                map.size().onComplete(s3 -> {
                  context.assertTrue(s3.succeeded());
                  context.assertEquals(s3.result(), 0);

                  async.complete();
                });
              });
            });
          }));
    });
  }

  @Test
  public void testKeySetAndValues(TestContext context) {
    Async async = context.async();

    map.keys().onComplete(set -> {
      context.assertTrue(set.succeeded());
      context.assertEquals(set.result().size(), 0);

      map.values().onComplete(list -> {
        context.assertTrue(list.succeeded());
        context.assertEquals(list.result().size(), 0);

        map.put("k1", "v1").onComplete(v ->
            map.put("k2", "v2").onComplete(v2 ->
                map.keys().onComplete(set2 -> {
                  context.assertTrue(set2.succeeded());
                  context.assertEquals(set2.result().size(), 2);

                  map.values().onComplete(list2 -> {
                    context.assertTrue(list2.succeeded());
                    context.assertEquals(list2.result().size(), 2);

                    map.entries().onComplete(map -> {
                      context.assertTrue(map.succeeded());
                      context.assertEquals(map.result().size(), 2);

                      context.assertTrue(map.result().containsKey("k1")
                          && map.result().containsKey("k2"));

                      context.assertTrue(map.result().containsValue("v1")
                          && map.result().containsValue("v2"));

                      async.complete();
                    });
                  });
                })));
      });

    });
  }

}
