#
# Copyright (c) 2011-2016 The original author or authors
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# and Apache License v2.0 which accompanies this distribution.
#
#      The Eclipse Public License is available at
#      http://www.eclipse.org/legal/epl-v10.html
#
#      The Apache License v2.0 is available at
#      http://www.opensource.org/licenses/apache2.0.php
#
# You may elect to redistribute this code under either of these licenses.
#

require 'vertx/vertx'
require 'vertx-service-discovery/service_discovery'
require 'test-services/hello_service'
require 'vertx-service-discovery/event_bus_service'

discovery = VertxServiceDiscovery::ServiceDiscovery.create($vertx)

VertxServiceDiscovery::EventBusService.get_service_proxy_with_json_filter(discovery, {
  'service.interface' => "io.vertx.servicediscovery.service.HelloService"
}, TestServices::HelloService) { |ar_err,ar|
    service = ar
    eb = $vertx.event_bus
    service.hello('name' => 'vert.x') { |err, res|
        eb.send('result', 'message' => res)
    }
    VertxServiceDiscovery::ServiceDiscovery.release_service_object(discovery, service)
}
