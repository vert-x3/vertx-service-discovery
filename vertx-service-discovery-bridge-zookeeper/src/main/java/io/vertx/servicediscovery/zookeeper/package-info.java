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
 * = Vert.x Discovery Bridge - Zookeeper
 *
 * This discovery bridge imports services from https://zookeeper.apache.org/[Apache Zookeeper] into the Vert.x service
 * discovery. The bridge uses the http://curator.apache.org/curator-x-discovery/[Curator extension for service discovery].
 *
 * Service description are read as JSON Object (merged in the Vert.x service record metadata). The service type is
 * deduced from this description by reading the `service-type`.
 *
 * == Using the bridge
 *
 * To use this Vert.x discovery bridge, add the following dependency to the _dependencies_ section of your build
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
 * Then, when creating the service discovery registers this bridge as follows:
 *
 * [source, $lang]
 * ----
 * {@link examples.Examples#register(io.vertx.core.Vertx)}
 * ----
 *
 * Only the `connection` configuration is mandatory. It's the connection _string_ of the Zookeeper server.
 *
 * In addition you can configure:
 *
 * * `maxRetries`: the number of connection attempt, 3 by default
 * * `baseSleepTimeBetweenRetries`: the amount of milliseconds to wait between retries (exponential backoff strategy).
 * 1000 ms by default.
 * * `basePath`: the Zookeeper path in which the service are stored. Default to `/discovery`.
 * * `connectionTimeoutMs`: the connection timeout in milliseconds. Defaults to 1000.
 * * `canBeReadOnly` : whether or not the backend support the _read-only_ mode (defaults to true)
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#register2(io.vertx.core.Vertx)}
 * ----
 *
 */
@ModuleGen(name = "vertx-service-discovery-zookeeper", groupPackage = "io.vertx")
@Document(fileName = "index.adoc")
package io.vertx.servicediscovery.zookeeper;

import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.docgen.Document;