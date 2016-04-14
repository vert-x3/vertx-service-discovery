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
 * = Vert.x Discovery Bridge - Kubernetes
 *
 * This discovery bridge imports services from Kubernetes (or Openshift v3) into the Vert.x discovery service.
 * Kubernetes services are mapped to {@link io.vertx.ext.discovery.Record}. This bridge only
 * supports the importation of services from kubernetes in vert.x (and not the opposite).
 *
 * {@link io.vertx.ext.discovery.Record} are created from Kubernetes Service. The service type is deduced from the `service.type` label. If
 * not set, the service is imported as `unknown`. Only `http-endpoint` are supported for now.
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
 * == Configuring the bridge
 *
 * The bridge is configured using:
 *
 * * the oauth token (using the content of `/var/run/secrets/kubernetes.io/serviceaccount/token` by default)
 * * the namespace in which the service are searched (defaults to `default`).
 *
 * Be aware that the application must have access to Kubernetes and must be able to read the chosen namespace.
 *
 * == The Service to Record mapping
 *
 * The record is created as follows:
 *
 * * the service type is deduced from the `service.type` label. If this label is not set the service type is set to
 * `unknown`
 * * the record's name is the service's name
 * * the labels of the service are mapped to metadata
 * * in addition are added: `kubernetes.uuid`, `kubernetes.namespace`, `kubernetes.name`
 * * the location is deduced from the **first** port of the service
 *
 * For HTTP endpoints, the `ssl` (`https`) attribute is set to `true` if the service has the `ssl` label set to `true`.
 *
 * == Dynamics
 *
 * The bridge imports all services on `start` and removes them on `stop`. In between it watches the Kubernetes
 * services and add the new ones and removes the deleted ones.
 */
@ModuleGen(name = "vertx-service-discovery-kubernetes", groupPackage = "io.vertx.ext.discovery.kubernetes")
@Document(fileName = "index.ad")
package io.vertx.ext.discovery.kubernetes;

import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.docgen.Document;