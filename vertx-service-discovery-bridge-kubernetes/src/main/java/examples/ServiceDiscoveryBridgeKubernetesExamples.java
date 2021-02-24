/*
 * Copyright 2021 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package examples;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.kubernetes.KubernetesServiceImporter;

@SuppressWarnings("unused")
public class ServiceDiscoveryBridgeKubernetesExamples {

  public void register(ServiceDiscovery serviceDiscovery) {
    JsonObject defaultConf = new JsonObject();
    serviceDiscovery.registerServiceImporter(new KubernetesServiceImporter(), defaultConf);
  }


  public void register2(Vertx vertx) {
    ServiceDiscovery.create(vertx)
      .registerServiceImporter(new KubernetesServiceImporter(),
        new JsonObject()
          .put("connection", "127.0.0.1:2181")
          .put("maxRetries", 5)
          .put("baseSleepTimeBetweenRetries", 2000)
          .put("basePath", "/services")
      );
  }
}
