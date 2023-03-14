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

package io.vertx.servicediscovery.docker;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

/**
 * Checks the import of service record from the environment variables set by Docker.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DockerLinksServiceImporterTest {

  private static Properties properties;

  @BeforeClass
  public static void load() {
    properties = new Properties();
    properties.put("DISCOVERY_REDIS_ENV_REDIS_VERSION", "3.0.7");
    properties.put("CONSOLIDATION_ENV_JAVA_HOME", "/usr/lib/jvm/java-8-openjdk-amd64");
    properties.put("DISCOVERY_REDIS_PORT_6379_TCP_PORT", "6379");
    properties.put("DISCOVERY_REDIS_ENV_REDIS_DOWNLOAD_URL", "http://download.redis.io/releases/redis-3.0.7.tar.gz");
    properties.put("DISCOVERY_REDIS_ENV_REDIS_DOWNLOAD_SHA1", "e56b4b7e033ae8dbf311f9191cf6fdf3ae974d1c");
    properties.put("CONSOLIDATION_PORT", "tcp://172.17.0.3:8080");
    properties.put("DISCOVERY_REDIS_PORT_6379_TCP_ADDR", "172.17.0.2");
    properties.put("CONSOLIDATION_PORT_8080_TCP", "tcp://172.17.0.3:8080");
    properties.put("CONSOLIDATION_NAME", "/portfolio/CONSOLIDATION");
    properties.put("CONSOLIDATION_PORT_8080_TCP_PORT", "8080");
    properties.put("DISCOVERY_REDIS_NAME", "/portfolio/DISCOVERY_REDIS");
    properties.put("CONSOLIDATION_PORT_8080_TCP_ADDR", "172.17.0.3");
    properties.put("CONSOLIDATION_ENV_JAVA_VERSION", "8u72");
    properties.put("CONSOLIDATION_ENV_LANG", "C.UTF-8");
    properties.put("CONSOLIDATION_ENV_SERVICE_TYPE", "http-endpoint");
    properties.put("DISCOVERY_REDIS_PORT", "tcp://172.17.0.2:6379");
    properties.put("DISCOVERY_REDIS_ENV_GOSU_VERSION", "1.7");
    properties.put("DISCOVERY_REDIS_PORT_6379_TCP_PROTO", "tcp");
    properties.put("CONSOLIDATION_PORT_8080_TCP_PROTO", "tcp");
    properties.put("JAVA_DEBIAN_VERSION", "8u72-b15-1~bpo8+1");
    properties.put("CONSOLIDATION_ENV_JAVA_DEBIAN_VERSION", "8u72-b15-1~bpo8+1");
    properties.put("CONSOLIDATION_ENV_CA_CERTIFICATES_JAVA_VERSION", "20140324");
    properties.put("DISCOVERY_REDIS_PORT_6379_TCP", "tcp://172.17.0.2:6379");

    Properties current = System.getProperties();
    current.putAll(properties);
    System.setProperties(current);
  }

  @AfterClass
  public static void unload() {
    for (Object key : properties.keySet()) {
      System.clearProperty((String) key);
    }
  }

  @Test
  public void testImport() throws InterruptedException {
    Vertx vertx = Vertx.vertx();
    ServiceDiscovery service = ServiceDiscovery.create(vertx);
    service.registerServiceImporter(new DockerLinksServiceImporter(), new JsonObject());

    List<Record> records = new ArrayList<>();

    vertx.setPeriodic(100, l -> {
      if (records.size() >= 2) {
        vertx.cancelTimer(l);
      } else {
        service.getRecords(new JsonObject()).onComplete(ar -> {
          records.clear();
          records.addAll(ar.result());
        });
      }
    });

    await().until(() -> records.size() == 2);

    for (Record record : records) {
      switch (record.getName()) {
        case "CONSOLIDATION":
          assertConsolidation(record);
          break;
        case "DISCOVERY_REDIS":
          assertRedis(record);
          break;
        default:
          fail("Unexpected record name");
          break;
      }
    }

  }

  private void assertRedis(Record record) {
    assertThat(record.getName()).isEqualTo("DISCOVERY_REDIS");
    assertThat(record.getLocation().getString("endpoint")).isEqualToIgnoringCase("tcp://172.17.0.2:6379");
    assertThat(record.getMetadata().getString("PORT_6379_TCP_PORT")).isEqualToIgnoringCase("6379");
  }

  private void assertConsolidation(Record record) {
    assertThat(record.getName()).isEqualTo("CONSOLIDATION");
    assertThat(record.getLocation().getString("endpoint")).isEqualToIgnoringCase("http://172.17.0.3:8080");
  }

}
