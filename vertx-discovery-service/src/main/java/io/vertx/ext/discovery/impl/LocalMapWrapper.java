package io.vertx.ext.discovery.impl;

import io.vertx.core.shareddata.LocalMap;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Wraps a map on top of a local map.
 */
public class LocalMapWrapper<K, V> implements Map<K, V> {

  private final LocalMap<K, V> local;

  public LocalMapWrapper(LocalMap<K, V> local) {
    this.local = local;
  }


  public boolean replaceIfPresent(K key, V oldValue, V newValue) {
    return local.replaceIfPresent(key, oldValue, newValue);
  }

  public void close() {
    local.close();
  }

  public boolean removeIfPresent(K key, V value) {
    return local.removeIfPresent(key, value);
  }

  @Override
  public Collection<V> values() {
    return local.values();
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    Set<Entry<K, V>> entries = new LinkedHashSet<>();
    for (K key : local.keySet()) {
      entries.add(new Entry<K, V>() {
        @Override
        public K getKey() {
          return key;
        }

        @Override
        public V getValue() {
          return local.get(key);
        }

        @Override
        public V setValue(V value) {
          return local.put(key, value);
        }
      });
    }
    return entries;
  }

  @Override
  public int size() {
    return local.size();
  }

  @Override
  public V replace(K key, V value) {
    return local.replace(key, value);
  }

  @Override
  public V putIfAbsent(K key, V value) {
    return local.putIfAbsent(key, value);
  }

  @Override
  public V put(K key, V value) {
    return local.put(key, value);
  }

  @Override
  public V remove(Object key) {
    return local.remove((K) key);
  }


  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
      local.put(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public Set<K> keySet() {
    return local.keySet();
  }

  @Override
  public boolean isEmpty() {
    return local.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return local.keySet().contains(key);
  }


  @Override
  public boolean containsValue(Object value) {
    return local.values().contains(value);
  }

  @Override
  public V get(Object key) {
    return local.get((K) key);
  }

  /**
   * Clear all entries in the map
   */
  @Override
  public void clear() {
    local.clear();
  }
}
