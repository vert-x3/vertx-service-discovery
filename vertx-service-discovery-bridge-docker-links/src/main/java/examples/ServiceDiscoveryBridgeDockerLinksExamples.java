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

package examples;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.docker.DockerLinksServiceImporter;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ServiceDiscoveryBridgeDockerLinksExamples {

  public void register(Vertx vertx) {
    ServiceDiscovery.create(vertx)
        .registerServiceImporter(new DockerLinksServiceImporter(), new JsonObject());
  }
}
