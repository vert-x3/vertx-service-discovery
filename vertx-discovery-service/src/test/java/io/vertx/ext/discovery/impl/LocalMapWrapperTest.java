/*
 * Copyright (c) 2011-$tody.year The original author or authors
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

import io.vertx.core.Vertx;
import io.vertx.core.shareddata.LocalMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;

/**
 * Checks the wrapper exposing the Map interface out of a local map.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class LocalMapWrapperTest {

  private Vertx vertx;
  private LocalMapWrapper<String, String> map;
  private LocalMap<String, String> local;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    local = vertx.sharedData().getLocalMap("test-local-map");
    map = new LocalMapWrapper<>(local);
  }

  @After
  public void tearDown() {
    map.close();
    vertx.close();
  }

  @Test
  public void testCreation() {
    assertThat(map).isNotNull();
    assertThat(map).isEmpty();
    assertThat(map.values()).hasSize(0);
    assertThat(map.keySet()).hasSize(0);
    assertThat(map.size()).isEqualTo(0);
  }


  @Test
  public void testPut() {
    map.put("key", "value");
    assertThat(map).contains(entry("key", "value"));
    assertThat(local.get("key")).isEqualTo("value");
    assertThat(map.values()).hasSize(1);
    assertThat(map.keySet()).hasSize(1);
    assertThat(map.size()).isEqualTo(1);

    LocalMap<Object, Object> same = vertx.sharedData().getLocalMap("test-local-map");
    assertThat(same.get("key")).isEqualTo("value");
  }

  @Test
  public void testReplaceIfPresent() {
    assertThat(map.replaceIfPresent("key", "junk", "value")).isFalse();
    map.put("key", "junk");
    assertThat(map.replaceIfPresent("key", "junk", "value")).isTrue();
    assertThat(map).contains(entry("key", "value"));
    assertThat(local.get("key")).isEqualTo("value");
  }

  @Test
  public void testRemoveIfPresent() {
    assertThat(map.removeIfPresent("key", "value")).isFalse();
    map.put("key", "value");
    assertThat(map.removeIfPresent("key", "value")).isTrue();
    assertThat(map).doesNotContain(entry("key", "value"));
    assertThat(local.get("key")).isNull();
  }

  @Test
  public void testPutIfAbsent() {
    assertThat(map.putIfAbsent("key", "value")).isNull();
    assertThat(map).hasSize(1);
    assertThat(map.putIfAbsent("key", "value2")).isEqualTo("value");
    assertThat(map).hasSize(1);
    assertThat(map).contains(entry("key", "value"));
  }

  @Test
  public void testReplace() {
    assertThat(map.replace("key", "value")).isNull();
    assertThat(map).hasSize(0);
    map.put("key", "junk");
    assertThat(map.replace("key", "value")).isEqualTo("junk");
    assertThat(map).contains(entry("key", "value")).hasSize(1);
  }

  @Test
  public void testPutAll() {
    map.put("k", "v");
    map.put("a", "b");

    Map<String, String> anotherMap = new HashMap<>();
    anotherMap.put("key", "value");
    anotherMap.put("k", "v2");

    map.putAll(anotherMap);

    assertThat(map).hasSize(3)
        .contains(entry("k", "v2"), entry("key", "value"), entry("a", "b"));

    map.clear();
    assertThat(map.values()).hasSize(0);
    assertThat(map.keySet()).hasSize(0);
    assertThat(map.size()).isEqualTo(0);
  }



}