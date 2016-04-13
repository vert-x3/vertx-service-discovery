/*
 * Copyright 2014 Red Hat, Inc.
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

/** @module vertx-service-discovery-js/event_bus_service */
var utils = require('vertx-js/util/utils');
var DiscoveryService = require('vertx-service-discovery-js/discovery_service');
var Vertx = require('vertx-js/vertx');
var ServiceReference = require('vertx-service-discovery-js/service_reference');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JEventBusService = io.vertx.ext.discovery.types.EventBusService;
var Record = io.vertx.ext.discovery.Record;

/**

 @class
*/
var EventBusService = function(j_val) {

  var j_eventBusService = j_val;
  var that = this;

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_eventBusService;
};

/**
 Sugar method to creates a record for this type.
 <p>
 The java interface is added to the metadata in the `service.interface` key.

 @memberof module:vertx-service-discovery-js/event_bus_service
 @param name {string} the name of the service. 
 @param address {string} the event bus address on which the service available 
 @param itf {string} the Java interface (name) 
 @param metadata {Object} the metadata 
 @return {Object} the created record
 */
EventBusService.createRecord = function(name, address, itf, metadata) {
  var __args = arguments;
  if (__args.length === 4 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'string' && (typeof __args[3] === 'object' && __args[3] != null)) {
    return utils.convReturnDataObject(JEventBusService["createRecord(java.lang.String,java.lang.String,java.lang.String,io.vertx.core.json.JsonObject)"](name, address, itf, utils.convParamJsonObject(metadata)));
  } else throw new TypeError('function invoked with invalid arguments');
};

/**
 Retrieves the bindings - for testing purpose only.

 @memberof module:vertx-service-discovery-js/event_bus_service

 @return {Array.<ServiceReference>} a copy of the bindings.
 */
EventBusService.bindings = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return utils.convReturnListSetVertxGen(JEventBusService["bindings()"](), ServiceReference);
  } else throw new TypeError('function invoked with invalid arguments');
};

/**
 Lookup for a service record and if found, retrieve it and return the service object (used to consume the service).
 This is a convenient method to avoid explicit lookup and then retrieval of the service. A filter based on the
 request interface is used.

 @memberof module:vertx-service-discovery-js/event_bus_service
 @param vertx {Vertx} the vert.x instance 
 @param discovery {DiscoveryService} the discovery service 
 @param itf {string} the service interface 
 @param resultHandler {function} the result handler 
 */
EventBusService.get = function(vertx, discovery, itf, resultHandler) {
  var __args = arguments;
  if (__args.length === 4 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'object' && __args[1]._jdel && typeof __args[2] === 'string' && typeof __args[3] === 'function') {
    JEventBusService["get(io.vertx.core.Vertx,io.vertx.ext.discovery.DiscoveryService,java.lang.String,io.vertx.core.Handler)"](vertx._jdel, discovery._jdel, itf, function(ar) {
    if (ar.succeeded()) {
      resultHandler(utils.convReturnTypeUnknown(ar.result()), null);
    } else {
      resultHandler(null, ar.cause());
    }
  });
  } else throw new TypeError('function invoked with invalid arguments');
};

/**
 Convenient method to release a used service object.

 @memberof module:vertx-service-discovery-js/event_bus_service
 @param svcObject {Object} the service object 
 */
EventBusService.release = function(svcObject) {
  var __args = arguments;
  if (__args.length === 1 && true) {
    JEventBusService["release(java.lang.Object)"](utils.convParamTypeUnknown(svcObject));
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = EventBusService;