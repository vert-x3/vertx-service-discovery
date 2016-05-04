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
 * = Vert.x Discovery Service
 * 
 * The discovery service provides an infrastructure to publish and discover various resources, such as service proxies,
 * HTTP endpoints, data sources... These resources are called `services`. A `service` is a discoverable
 * functionality. It can be qualified by its type, metadata, and location. So a `service` can be a database, a
 * service proxy, a HTTP endpoint. It does not have to be a vert.x entity, but can be anything. Each service is
 * described by a {@link io.vertx.ext.discovery.Record}.
 * 
 * The discovery service implements the interactions defined in the service-oriented computing. And to some extend,
 * also provides the dynamic service-oriented computing interaction. So, application can react to arrival and
 * departure of services.
 * 
 * A service provider can:
 * 
 * * publish a service record
 * * un-publish a published record
 * * update the status of a published service (down, out of service...)
 * 
 * A service consumer can:
 * 
 * * lookup for services
 * * bind to a selected service (it gets a {@link io.vertx.ext.discovery.ServiceReference}) and use it
 * * release the service once the consumer is done with it
 * * listen for arrival, departure and modification of services.
 * 
 * Consumer would 1) lookup for service record matching their need, 2) retrieve the
 * {@link io.vertx.ext.discovery.ServiceReference} that give access to the service, 3) get a service object to access
 * the service, 4) release the service object once done.
 * 
 * A state above, the central piece of information shared by the providers and consumers are
 * {@link io.vertx.ext.discovery.Record records}.
 * 
 * Providers and consumers must create their own {@link io.vertx.ext.discovery.DiscoveryService} instance. These
 * instances are collaborating in background (distributed structure) to keep the set of services in sync.
 * 
 * The discovery service supports bridges to import and export services from / to other discovery technologies.
 * 
 * == Using the discovery service
 * 
 * To use the Vert.x discovery service, add the following dependency to the _dependencies_ section of your build
 * descriptor:
 * 
 * * Maven (in your `pom.xml`):
 * 
 * [source,xml,subs="+attributes"]
 * ----
 * <dependency>
 * <groupId>${maven.groupId}</groupId>
 * <artifactId>${maven.artifactId}</artifactId>
 * <version>${maven.version}</version>
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
 * == Overall concepts
 * 
 * The discovery mechanism is based on a few concepts explained in this section.
 * 
 * === Service records
 * 
 * A service {@link io.vertx.ext.discovery.Record} is an object that describes a service published by a service
 * provider. It contains a name, some metadata, a location object (describing where is the service). This record is
 * the only objects shared by the provider (having published it) and the consumer (retrieve it when doing a lookup).
 * 
 * The metadata and even the location format depends on the service type.
 * 
 * A record is published when the provider is ready to be used, and withdrawn when the service provider is stopping.
 * 
 * === Service Provider and publisher
 * 
 * A service provider is an entity providing a _service_. The publisher is responsible for publishing a record
 * describing the provider. It may be a single entity (a provider publishing itself) or a different entity.
 * 
 * === Service Consumer
 * 
 * Service consumers search for services in the discovery service. Each lookup retrieves 0..n
 * {@link io.vertx.ext.discovery.Record}. From these records, a consumer can build a
 * {@link io.vertx.ext.discovery.ServiceReference}. This reference let the consumer manages the binding with the
 * provider. It can retrieve the _service object_, or release the service.
 * 
 * === Service object
 * 
 * The service object is the object that give access to a service. It has various form, such as a proxy, a client, or
 * may even be non existent for some service type. The nature of the service object depends of the service type.
 * 
 * === Service types
 * 
 * Services are resources, and it exists a wide variety of resources. They can be functional services, databases,
 * REST APIs, and so on. The Vert.x discovery service has the concept of service types to handle this heterogeneity.
 * Each type defines:
 * 
 * * how the service is located (URI, event bus address, IP / DNS...)
 * * the nature of the service object (service proxy, HTTP client, message consumer...)
 * 
 * Some service types are provided by the the discovery service, but you can add your own.
 * 
 * === Service event
 * 
 * Every time a service provider is published or withdrawn, an event is fired on the event bus. This event contains
 * the record that has been modified.
 * 
 * === Backend
 * 
 * The discovery service used a distributed structure to store the records. So, all member of the cluster are access
 * to all the records. This is the default backend implementation. You can implement your own by implementing the
 * {@link io.vertx.ext.discovery.spi.DiscoveryBackend} SPI.
 * 
 * == Creating the discovery service
 * 
 * Publishers and consumers must create their own {@link io.vertx.ext.discovery.DiscoveryService} instance to use the
 * discovery infrastructure:
 * 
 * [source,$lang]
 * ----
 * {@link examples.Examples#example1(io.vertx.core.Vertx)}
 * ----
 * 
 * By default, the announce address (the event bus address on which service events are sent is: `vertx.discovery
 * .announce`.
 * 
 * When you don't need the discovery service, don't forget to close it. It closes the different discovery bridge you
 * have configured.
 * 
 * == Publishing services
 * 
 * Once you have a discovery service instance, you can start to publish services. The process is the following:
 * 
 * 1. create a record for a specific service provider
 * 2. publish this record
 * 3. keep the published record that is used to un-publish a service or modify it.
 * 
 * To create records, you can either use the {@link io.vertx.ext.discovery.Record} class, or use convenient method
 * from the service type.
 * 
 * [source,$lang]
 * ----
 * {@link examples.Examples#example2(io.vertx.core.Vertx, io.vertx.ext.discovery.DiscoveryService)}
 * ----
 * 
 * It is important to keep a reference on the returned records, as this record has been extended by a `registration id`.
 * 
 * == Withdrawing services
 * 
 * To withdraw (un-publish) a record, use:
 * 
 * [source,$lang]
 * ----
 * {@link examples.Examples#example3(io.vertx.core.Vertx, io.vertx.ext.discovery.DiscoveryService, io.vertx.ext.discovery.Record)}
 * ----
 * 
 * == Looking for service
 * 
 * On the consumer side, the first thing to do is to lookup for records. You can search for a single record or all
 * the matching ones. In the first case, the first matching record is returned.
 * 
 * Consumer can pass a filter to select the service. This filter is a JSON object. Each entry of the given filter
 * are checked against the record. All entry must match exactly the record. The entry can use the special `*` value
 * to denotes a requirement on the key, but not on the value.
 * 
 * Let's take some example:
 * ----
 * { "name" = "a" } => matches records with name set fo "a"
 * { "color" = "*" } => matches records with "color" set
 * { "color" = "red" } => only matches records with "color" set to "red"
 * { "color" = "red", "name" = "a"} => only matches records with name set to "a", and color set to "red"
 * ----
 * 
 * If the filter is not set ({@code null} or empty), it accepts all records.
 * 
 * Here are some examples:
 * 
 * [source,$lang]
 * ----
 * {@link examples.Examples#example4(io.vertx.core.Vertx, io.vertx.ext.discovery.DiscoveryService)}
 * ----
 * 
 * == Using a service
 * 
 * Once you have chosen the {@link io.vertx.ext.discovery.Record}, you can retrieve a
 * {@link io.vertx.ext.discovery.ServiceReference} and then the service object:
 * 
 * [source,$lang]
 * ----
 * {@link examples.Examples#example5(io.vertx.core.Vertx, io.vertx.ext.discovery.Record)}
 * ----
 * 
 * Don't forget to release the reference once done.
 * 
 * == Type of services
 * 
 * A said above, the discovery service has the service type concept to manage the heterogeneity of the different
 * services.
 * 
 * Are provided by default:
 * 
 * * {@link io.vertx.ext.discovery.types.HttpEndpoint} - for REST API, the service object is a
 * {@link io.vertx.core.http.HttpClient} configured on the host and port (the location is the url).
 * * {@link io.vertx.ext.discovery.types.EventBusService} - for service proxies, the service object is a proxy. Its
 * type is the proxies interface (the location is the address).
 * * {@link io.vertx.ext.discovery.types.MessageSource} - for message source (publisher), the service object is a
 * {@link io.vertx.core.eventbus.MessageConsumer} (the location is the address).
 * 
 * Some records may have no type. It is not possible to retrieve a reference for these records, but you can build the
 * connection details from the `location` object in the record.
 * 
 * You can create your own service type by implementing the {@link io.vertx.ext.discovery.spi.ServiceType} SPI.
 * 
 * == Listening for service arrivals and departures
 * 
 * Every time a provider is published or removed, an event is published on the _vertx.discovery.announce_ address.
 * This address is configurable from the {@link io.vertx.ext.discovery.DiscoveryOptions}.
 * 
 * The received record has a `status` field indicating the new state of the record:
 * 
 * * `UP` : the service is available, you can start using it
 * * `DOWN` : the service is not available anymore, you should not use it anymore
 * * `OUT_OF_SERVICE` : the service is not running, you should not use it anymore, but it may come back later.
 * 
 * == Service discovery bridges
 * 
 * Bridges let import and export services from / to other discovery mechanism such as Docker, Kubernates, Consul...
 * Each bridge decides how the services are imported and exported. It does not have to be bi-directional.
 * 
 * You can provide your own bridge by implementing the {@link io.vertx.ext.discovery.spi.DiscoveryBridge} interface and
 * register it using
 * {@link io.vertx.ext.discovery.DiscoveryService#registerDiscoveryBridge(DiscoveryBridge, io.vertx.core.json.JsonObject)}.
 * 
 * Notice than in a cluster, only one member needs to register the bridge as the records are accessible by all members.
 */
@ModuleGen(name = "vertx-service-discovery", groupPackage = "io.vertx.ext.discovery")
@Document(fileName = "index.ad")
package io.vertx.ext.discovery;

import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.docgen.Document;
import io.vertx.ext.discovery.spi.DiscoveryBridge;