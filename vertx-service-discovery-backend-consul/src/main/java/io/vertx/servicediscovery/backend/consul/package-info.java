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
 * === Consul backend
 * <p>
 * The service discovery has a plug-able backend using the {@link io.vertx.servicediscovery.spi.ServiceDiscoveryBackend} SPI. This is an implementation of the SPI based
 * on Consul.
 * <p>
 * ==== Using the Redis backend
 * <p>
 * To use the Consul backend, add the following dependency to the _dependencies_ section of your build
 * descriptor:
 * <p>
 * * Maven (in your `pom.xml`):
 * <p>
 * [source,xml,subs="+attributes"]
 * ----
 * <dependency>
 * <groupId>${maven.groupId}</groupId>
 * <artifactId>${maven.artifactId}</artifactId>
 * <version>${maven.version}</version>
 * </dependency>
 * ----
 * <p>
 * * Gradle (in your `build.gradle` file):
 * <p>
 * [source,groovy,subs="+attributes"]
 * ----
 * compile '${maven.groupId}:${maven.artifactId}:${maven.version}'
 * ----
 * <p>
 * Be aware that you can have only a single implementation of the SPI in your _classpath_. If none,
 * the default backend is used.
 * <p>
 * ==== Configuration
 * <p>
 * The backend is based on the http://vertx.io/docs/vertx-consul-client/java[vertx-consul-client].
 * The configuration is the client configuration.
 * <p>
 * Here is an example:
 * <p>
 * [source,$lang]
 * ----
 * {@link examples.Examples#configuration1(io.vertx.core.Vertx)}
 * ----
 */
@ModuleGen(name = "vertx-service-discovery-backend-consul", groupPackage = "io.vertx")
@Document(fileName = "consul-backend.adoc")
package io.vertx.servicediscovery.backend.consul;

import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.docgen.Document;
