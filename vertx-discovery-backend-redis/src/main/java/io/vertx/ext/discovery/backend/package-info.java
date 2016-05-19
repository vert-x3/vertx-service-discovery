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
 * == Vert.x Discovery Backend - Redis
 *
 * The discovery service has a plug-able backend using the {@link io.vertx.ext.discovery.spi.DiscoveryBackend} SPI. This is an implementation of the SPI based
 * on Redis.
 *
 * == Using the Redis backend
 *
 * To use the Redis backend, add the following dependency to the _dependencies_ section of your build
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
 * The backend is based on the http://vertx.io/docs/vertx-redis-client/java[vertx-redis-client].
 * The configuration is the client configuration as well as `key` indicating in which _key_ on Redis
 * the records are stored.
 *
 * Here is an example:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#configuration1(io.vertx.core.Vertx)}
 * ----
 *
 */
@ModuleGen(name = "vertx-discovery-backend-redis", groupPackage = "io.vertx.ext.discovery.backend")
@Document(fileName = "index.adoc")
package io.vertx.ext.discovery.backend;

import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.docgen.Document;