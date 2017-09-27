package io.vertx.servicediscovery.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.core.shareddata.LocalMap;

import java.util.*;

/**
 * Wraps a local map into an AsyncMap.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class LocalAsyncMap<K, V> implements AsyncMap<K, V> {

  private final LocalMap<K, V> local;

  public LocalAsyncMap(LocalMap<K, V> local) {
    this.local = local;
  }

  @Override
  public void get(K k, Handler<AsyncResult<V>> handler) {
    try {
      V v = local.get(k);
      handler.handle(Future.succeededFuture(v));
    } catch (Exception e) {
      handler.handle(Future.failedFuture(e));
    }
  }

  @Override
  public void put(K k, V v, Handler<AsyncResult<Void>> handler) {
    try {
      local.put(k, v);
      handler.handle(Future.succeededFuture());
    } catch (Exception e) {
      handler.handle(Future.failedFuture(e));
    }
  }

  @Override
  public void put(K k, V v, long l, Handler<AsyncResult<Void>> handler) {
    handler.handle(Future.failedFuture(new UnsupportedOperationException("put with ttl not supported")));
  }

  @Override
  public void putIfAbsent(K k, V v, Handler<AsyncResult<V>> handler) {
    try {
      local.putIfAbsent(k, v);
      handler.handle(Future.succeededFuture());
    } catch (Exception e) {
      handler.handle(Future.failedFuture(e));
    }
  }

  @Override
  public void putIfAbsent(K k, V v, long l, Handler<AsyncResult<V>> handler) {
    handler.handle(Future.failedFuture(new UnsupportedOperationException("putIfAbsent with ttl not supported")));

  }

  @Override
  public void remove(K k, Handler<AsyncResult<V>> handler) {
    try {
      V v = local.remove(k);
      handler.handle(Future.succeededFuture(v));
    } catch (Exception e) {
      handler.handle(Future.failedFuture(e));
    }
  }

  @Override
  public void removeIfPresent(K k, V v, Handler<AsyncResult<Boolean>> handler) {
    try {
      boolean present = local.removeIfPresent(k, v);
      handler.handle(Future.succeededFuture(present));
    } catch (Exception e) {
      handler.handle(Future.failedFuture(e));
    }
  }

  @Override
  public void replace(K k, V v, Handler<AsyncResult<V>> handler) {
    try {
      V replaced = local.replace(k, v);
      handler.handle(Future.succeededFuture(replaced));
    } catch (Exception e) {
      handler.handle(Future.failedFuture(e));
    }
  }

  @Override
  public void replaceIfPresent(K k, V v, V v1, Handler<AsyncResult<Boolean>> handler) {
    try {
      boolean replaced = local.replace(k, v, v1);
      handler.handle(Future.succeededFuture(replaced));
    } catch (Exception e) {
      handler.handle(Future.failedFuture(e));
    }
  }

  @Override
  public void clear(Handler<AsyncResult<Void>> handler) {
    try {
      local.clear();
      handler.handle(Future.succeededFuture());
    } catch (Exception e) {
      handler.handle(Future.failedFuture(e));
    }
  }

  @Override
  public void size(Handler<AsyncResult<Integer>> handler) {
    try {
      int size = local.size();
      handler.handle(Future.succeededFuture(size));
    } catch (Exception e) {
      handler.handle(Future.failedFuture(e));
    }
  }

  @Override
  public void keys(Handler<AsyncResult<Set<K>>> handler) {
    try {
      Set<K> keys = local.keySet();
      handler.handle(Future.succeededFuture(keys));
    } catch (Exception e) {
      handler.handle(Future.failedFuture(e));
    }
  }

  @Override
  public void values(Handler<AsyncResult<List<V>>> handler) {
    try {
      Collection<V> values = local.values();
      handler.handle(Future.succeededFuture(new ArrayList<>(values)));
    } catch (Exception e) {
      handler.handle(Future.failedFuture(e));
    }
  }

  @Override
  public void entries(Handler<AsyncResult<Map<K, V>>> handler) {
    try {
      Set<Map.Entry<K, V>> entries = local.entrySet();
      Map<K, V> map = new LinkedHashMap<>();
      entries.forEach(entry -> map.put(entry.getKey(), entry.getValue()));
      handler.handle(Future.succeededFuture(map));
    } catch (Exception e) {
      handler.handle(Future.failedFuture(e));
    }
  }
}
