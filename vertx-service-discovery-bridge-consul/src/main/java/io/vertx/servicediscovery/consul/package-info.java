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
 * === Consul bridge
 *
 * This discovery bridge imports services from http://consul.io[Consul] into the Vert.x service discovery. The bridge
 * connects to a Consul agent (server) and periodically scan for services:
 *
 * * new services are imported
 * * services in maintenance mode or that has been removed from consul are removed
 *
 * This bridge uses the HTTP API for Consul. It does not export to Consul and does not support service modification.
 *
 * The service type is deduced from `tags`. If a `tag` matches a known service type, this service type will be used.
 * If not, the service is imported as `unknown`. Only `http-endpoint` is supported for now.
 *
 *
 * ==== Using the bridge
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
 * You can configure the:
 *
 * * agent host using the `host` property, it defaults to `localhost`
 * * agent port using the `port` property, it defaults to 8500
 * * acl token using the `acl_token` property, it defaults to null
 * * scan period using the `scan-period` property. The time is set in ms, and is 2000 ms by default
 *
 */
@ModuleGen(name = "vertx-service-discovery-consul", groupPackage = "io.vertx")
@Document(fileName = "consul-bridge.adoc")
package io.vertx.servicediscovery.consul;

import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.docgen.Document;
