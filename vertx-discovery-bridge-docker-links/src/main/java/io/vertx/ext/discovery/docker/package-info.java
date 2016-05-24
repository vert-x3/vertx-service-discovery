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
 * = Vert.x Discovery Bridge - Docker Links
 *
 * This discovery bridge imports services from Docker Links into the Vert.x discovery service. When you link a Docker
 * container to another Docker container, Docker injects a set of environment variables. This bridge analyzes these
 * environment variables and imports service record for each link. The service type is deduced from the `service.type`
 * label. If not set, the service is imported as `unknown`. Only `http-endpoint` are supported for now.
 *
 * As the links are created when the container starts, the imported records are created when the bridge starts and
 * do not change afterwards.
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
 * Then, when creating the discovery service registers this bridge as follows:
 *
 * [source, $lang]
 * ----
 * {@link examples.Examples#register(io.vertx.core.Vertx)}
 * ----
 *
 * The bridge does not need any further configuration.
 *
 */
@Document(fileName = "index.adoc")
package io.vertx.ext.discovery.docker;

import io.vertx.docgen.Document;