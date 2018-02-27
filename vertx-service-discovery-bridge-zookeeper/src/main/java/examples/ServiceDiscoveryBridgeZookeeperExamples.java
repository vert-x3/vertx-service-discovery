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
import io.vertx.servicediscovery.zookeeper.ZookeeperServiceImporter;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ServiceDiscoveryBridgeZookeeperExamples {

  public void register(Vertx vertx) {
    ServiceDiscovery.create(vertx)
        .registerServiceImporter(new ZookeeperServiceImporter(),
            new JsonObject()
                .put("connection", "127.0.0.1:2181"));
  }


  public void register2(Vertx vertx) {
    ServiceDiscovery.create(vertx)
        .registerServiceImporter(new ZookeeperServiceImporter(),
            new JsonObject()
                .put("connection", "127.0.0.1:2181")
                .put("maxRetries", 5)
                .put("baseSleepTimeBetweenRetries", 2000)
                .put("basePath", "/services")
        );
  }
}
