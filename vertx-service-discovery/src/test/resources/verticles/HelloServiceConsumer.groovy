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
package verticles;

import io.vertx.groovy.servicediscovery.ServiceDiscovery
import io.vertx.groovy.servicediscovery.types.EventBusService
import io.vertx.groovy.servicediscovery.service.HelloService

def discovery = ServiceDiscovery.create(vertx);
EventBusService.<HelloService> getProxy(
        discovery,
        io.vertx.servicediscovery.service.HelloService.class.getName(), // service interface
        HelloService.class.getName(), // client class
        { ar ->
          if (ar.failed()) {
            vertx.eventBus().send("result", [
                    "status" : "ko",
                    "message": ar.cause().getMessage()
            ])
          } else {
            HelloService hello = ar.result();
            hello.hello(['name': "vert.x"], { result ->
              if (result.failed()) {
                vertx.eventBus().send("result", [
                        "status" : "ko",
                        "message": result.cause().getMessage()
                ])
              } else {
                vertx.eventBus().send("result", [
                        "status" : "ok",
                        "message": result.result()
                ])
                ServiceDiscovery.releaseServiceObject(discovery, hello);
              }
            })
          }
        }
)