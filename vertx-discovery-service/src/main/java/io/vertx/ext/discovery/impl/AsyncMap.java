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

package io.vertx.ext.discovery.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.spi.cluster.ClusterManager;

import java.util.*;

/**
 * An asynchronous map facade. The underlying map is either a distributed sync map, or a
 * local map.
 * <p>
 * It does not implement all Map method on purpose, just the required ones.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class AsyncMap<K, V> {


  private final Vertx vertx;
  private final Map<K, V> syncMap;

  /**
   * Creates a new async map.
   *
   * @param vertx the vert.x instance
   * @param name  the name of the underlying structure (either a local map for non-clustered
   *              vert.x, or a sync map for clustered vert.x)
   */
  public AsyncMap(Vertx vertx, String name) {
    this.vertx = vertx;
    ClusterManager clusterManager = ((VertxInternal) vertx).getClusterManager();
    if (clusterManager == null) {
      syncMap = new LocalMapWrapper<>(vertx.sharedData().<K, V>getLocalMap(name));
    } else {
      syncMap = clusterManager.getSyncMap(name);
    }
  }

  public void getAll(Handler<AsyncResult<Map<K, V>>> asyncResultHandler) {
    vertx.<Map<K, V>>executeBlocking(
        future -> {
          Map<K, V> map = new LinkedHashMap<>();
          syncMap.entrySet().stream().forEach(entry -> map.put(entry.getKey(), entry.getValue()));
          future.complete(map);
        },
        asyncResultHandler
    );
  }

  public void keySet(Handler<AsyncResult<Set<K>>> asyncResultHandler) {
    vertx.<Set<K>>executeBlocking(
        future -> future.complete(syncMap.keySet()),
        asyncResultHandler
    );
  }

  public void values(Handler<AsyncResult<List<V>>> asyncResultHandler) {
    vertx.<List<V>>executeBlocking(
        future -> future.complete(new ArrayList<>(syncMap.values())),
        asyncResultHandler
    );
  }

  public void get(K k, Handler<AsyncResult<V>> handler) {
    vertx.<V>executeBlocking(
        future -> future.complete(syncMap.get(k)),
        handler
    );
  }

  public void put(K k, V v, Handler<AsyncResult<Void>> handler) {
    vertx.<Void>executeBlocking(
        future -> {
          syncMap.put(k, v);
          future.complete();
        },
        handler
    );
  }

  public void remove(K k, Handler<AsyncResult<V>> handler) {
    vertx.<V>executeBlocking(
        future -> future.complete(syncMap.remove(k)),
        handler
    );
  }


  public void size(Handler<AsyncResult<Integer>> handler) {
    vertx.<Integer>executeBlocking(
        future -> future.complete(syncMap.size()),
        handler
    );
  }
}
