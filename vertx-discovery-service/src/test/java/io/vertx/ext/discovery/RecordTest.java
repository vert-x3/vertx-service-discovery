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

package io.vertx.ext.discovery;

import io.vertx.core.json.JsonObject;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class RecordTest {

  @Test
  public void testMatch() {
    Record record = new Record().setName("Name");

    assertThat(record.match(new JsonObject().put("name", "Name"))).isTrue();
    assertThat(record.match(new JsonObject().put("name", "Name-2"))).isFalse();

    record.setStatus(Status.UP);
    assertThat(record.match(new JsonObject().put("status", "Up"))).isTrue();
    assertThat(record.match(new JsonObject().put("status", "Down"))).isFalse();
    assertThat(record.match(new JsonObject().put("status", "Up").put("name", "Name"))).isTrue();
    assertThat(record.match(new JsonObject().put("status", "Down").put("name", "Name"))).isFalse();

    record.setRegistration("the-registration");
    assertThat(record.match(new JsonObject().put("registration", "the-registration"))).isTrue();
    assertThat(record.match(new JsonObject().put("registration", "wrong"))).isFalse();

    record.getMetadata().put("foo", "bar").put("key", 2);
    assertThat(record.match(new JsonObject().put("foo", "bar"))).isTrue();
    assertThat(record.match(new JsonObject().put("foo", "bar2"))).isFalse();
    assertThat(record.match(new JsonObject().put("foo", "bar").put("other", "nope"))).isFalse();
    assertThat(record.match(new JsonObject().put("foo", "bar").put("other", "*"))).isFalse();
    assertThat(record.match(new JsonObject().put("foo", "bar").put("key", 2))).isTrue();
    assertThat(record.match(new JsonObject().put("foo", "*").put("key", 2))).isTrue();
  }

}