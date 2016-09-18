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

var ServiceDiscovery = require("vertx-service-discovery-js/service_discovery");
var HttpEndpoint = require("vertx-service-discovery-js/http_endpoint.js");
var discovery = ServiceDiscovery.create(vertx);
var eb = vertx.eventBus();

discovery.getRecord({"name" : "hello-service"}, function(ar, ar_err) {
    var reference = discovery.getReference(ar);
    var type = HttpEndpoint.serviceType();
    var client = type.getService(reference);
    client.getNow("/foo", function(response) {
        if (response.statusCode() == 200) {
            response.bodyHandler(function(body) {
              eb.send("result", {
                "status": "ok",
                "message": body.toString()
              });
            });
        } else {
            eb.send("result", {
                "status" : "ko"
            });
        }
    });
});