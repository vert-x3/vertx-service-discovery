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

/**
 * == Vert.x Discovery Backend - Zookeeper
 *
 * The service discovery has a plug-able backend using the {@link io.vertx.servicediscovery.spi.ServiceDiscoveryBackend} SPI. This is an implementation of the SPI based
 * on Apache Zookeeper.
 *
 * == Using the Zookeeper backend
 *
 * To use the Zookeeper backend, add the following dependency to the _dependencies_ section of your build
 * descriptor:
 *
 * * Maven (in your `pom.xml`):
 *
 * [source,xml,subs="+attributes"]
 * ----
 * <dependency>
 *   <groupId>${maven.groupId}</groupId>
 *   <artifactId>${maven.artifactId}</artifactId>
 *   <version>${maven.version}</version>
 * </dependency>
 * ----
 *
 * * Gradle (in your `build.gradle` file):
 *
 * [source,groovy,subs="+attributes"]
 * ----
 * compile '${maven.groupId}:${maven.artifactId}:${maven.version}'
 * ----
 *
 * Be aware that you can have only a single implementation of the SPI in your _classpath_. If none,
 * the default backend is used.
 *
 * == Configuration
 *
 * There is a single mandatory configuration attribute: `connection`. Connection is the Zookeeper connection _string_.
 *
 * Here is an example:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#configuration1(io.vertx.core.Vertx)}
 * ----
 *
 * Additionally you can configure:
 *
 * * `maxRetries`: the number of connection attempt, 3 by default
 * * `baseSleepTimeBetweenRetries`: the amount of milliseconds to wait between retries (exponential backoff strategy).
 * 1000 ms by default.
 * * `connectionTimeoutMs`: the connection timeout in milliseconds. Defaults to 1000.
 * * `canBeReadOnly` : whether or not the backend support the _read-only_ mode (defaults to false)
 * * `basePath`: the Zookeeper path in which the service records are stored. Default to `/services`.
 * * `ephemeral`: whether or not the created nodes are ephemeral nodes (see
 * https://zookeeper.apache.org/doc/r3.4.5/zookeeperOver.html#Nodes+and+ephemeral+nodes). `false` by default
 * * `guaranteed`: whether or not to guarantee the node deletion even in case of failure. `false` by default
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#configuration2(io.vertx.core.Vertx)}
 * ----
 *
 * == How are stored the records
 *
 * The records are stored in individual nodes structured as follows:
 *
 * [source]
 * ----
 * basepath (/services/)
 *    |
 *    |- record 1 registration id => the record 1 is the data of this node
 *    |- record 2 registration id => the record 2 is the data of this node
 * ----
 *
 */
@ModuleGen(name = "vertx-service-discovery-backend-zookeeper", groupPackage = "io.vertx")
@Document(fileName = "index.adoc")
package io.vertx.servicediscovery.backend.zookeeper;

import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.docgen.Document;