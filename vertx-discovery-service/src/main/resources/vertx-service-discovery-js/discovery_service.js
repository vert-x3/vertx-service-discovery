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

/** @module vertx-service-discovery-js/discovery_service */
var utils = require('vertx-js/util/utils');
var Vertx = require('vertx-js/vertx');
var ServiceReference = require('vertx-service-discovery-js/service_reference');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JDiscoveryService = io.vertx.ext.discovery.DiscoveryService;
var DiscoveryOptions = io.vertx.ext.discovery.DiscoveryOptions;
var Record = io.vertx.ext.discovery.Record;

/**
 Discovery service main entry point.
 <p>
 The discovery service is an infrastructure that let you publish and find `services`. A `service` is a discoverable
 functionality. It can be qualified by its type, metadata, and location. So a `service` can be a database, a
 service proxy, a HTTP endpoint. It does not have to be a vert.x entity, but can be anything. Each service is
 @class
*/
var DiscoveryService = function(j_val) {

  var j_discoveryService = j_val;
  var that = this;

  /**
   Gets a service reference from the given record.

   @public
   @param record {Object} the chosen record 
   @return {ServiceReference} the service reference, that allows retrieving the service object. Once called the service reference is cached, and need to be released.
   */
  this.getReference = function(record) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'object' && __args[0] != null)) {
      return utils.convReturnVertxGen(j_discoveryService["getReference(io.vertx.ext.discovery.Record)"](record != null ? new Record(new JsonObject(JSON.stringify(record))) : null), ServiceReference);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Gets a service reference from the given record, the reference is configured with the given json object.

   @public
   @param record {Object} the chosen record 
   @param configuration {Object} the configuration 
   @return {ServiceReference} the service reference, that allows retrieving the service object. Once called the service reference is cached, and need to be released.
   */
  this.getReferenceWithConfiguration = function(record, configuration) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && (typeof __args[1] === 'object' && __args[1] != null)) {
      return utils.convReturnVertxGen(j_discoveryService["getReferenceWithConfiguration(io.vertx.ext.discovery.Record,io.vertx.core.json.JsonObject)"](record != null ? new Record(new JsonObject(JSON.stringify(record))) : null, utils.convParamJsonObject(configuration)), ServiceReference);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Releases the service reference.

   @public
   @param reference {ServiceReference} the reference to release, must not be <code>null</code> 
   @return {boolean} whether or not the reference has been released.
   */
  this.release = function(reference) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      return j_discoveryService["release(io.vertx.ext.discovery.ServiceReference)"](reference._jdel);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Closes the discovery service

   @public

   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_discoveryService["close()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Publishes a record.

   @public
   @param record {Object} the record 
   @param resultHandler {function} handler called when the operation has completed (successfully or not). In case of success, the passed record has a registration id required to modify and un-register the service. 
   */
  this.publish = function(record, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_discoveryService["publish(io.vertx.ext.discovery.Record,io.vertx.core.Handler)"](record != null ? new Record(new JsonObject(JSON.stringify(record))) : null, function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnDataObject(ar.result()), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Un-publishes a record.

   @public
   @param id {string} the registration id 
   @param resultHandler {function} handler called when the operation has completed (successfully or not). 
   */
  this.unpublish = function(id, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_discoveryService["unpublish(java.lang.String,io.vertx.core.Handler)"](id, function(ar) {
      if (ar.succeeded()) {
        resultHandler(null, null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Lookups for a single record.
   <p>
   Filters are expressed using a Json object. Each entry of the given filter will be checked against the record.
   All entry must match exactly the record. The entry can use the special "*" value to denotes a requirement on the
   key, but not on the value.
   <p>
   Let's take some example:
   <pre>
     { "name" = "a" } => matches records with name set fo "a"
     { "color" = "*" } => matches records with "color" set
     { "color" = "red" } => only matches records with "color" set to "red"
     { "color" = "red", "name" = "a"} => only matches records with name set to "a", and color set to "red"
   </pre>
   <p>
   If the filter is not set (<code>null</code> or empty), it accepts all records.
   <p>
   This method returns the first matching record.

   @public
   @param filter {Object} the filter. 
   @param resultHandler {function} handler called when the lookup has been completed. When there are no matching record, the operation succeed, but the async result has no result. 
   */
  this.getRecord = function(filter, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_discoveryService["getRecord(io.vertx.core.json.JsonObject,io.vertx.core.Handler)"](utils.convParamJsonObject(filter), function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnDataObject(ar.result()), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Lookups for a set of records. Unlike {@link DiscoveryService#getRecord}, this method returns all matching
   records.

   @public
   @param filter {Object} the filter - see {@link DiscoveryService#getRecord} 
   @param resultHandler {function} handler called when the lookup has been completed. When there are no matching record, the operation succeed, but the async result has an empty list as result. 
   */
  this.getRecords = function(filter, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_discoveryService["getRecords(io.vertx.core.json.JsonObject,io.vertx.core.Handler)"](utils.convParamJsonObject(filter), function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnListSetDataObject(ar.result()), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Updates the given record. The record must has been published, and has it's registration id set.

   @public
   @param record {Object} the updated record 
   @param resultHandler {function} handler called when the lookup has been completed. 
   */
  this.update = function(record, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_discoveryService["update(io.vertx.ext.discovery.Record,io.vertx.core.Handler)"](record != null ? new Record(new JsonObject(JSON.stringify(record))) : null, function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnDataObject(ar.result()), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the set of service references retrieved by this discovery service.

   @public

   @return {Array.<ServiceReference>}
   */
  this.bindings = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnListSetVertxGen(j_discoveryService["bindings()"](), ServiceReference);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_discoveryService;
};

/**
 Creates an instance of {@link DiscoveryService}.

 @memberof module:vertx-service-discovery-js/discovery_service
 @param vertx {Vertx} the vert.x instance 
 @param options {Object} the discovery options 
 @return {DiscoveryService} the create discovery service.
 */
DiscoveryService.create = function() {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
    return utils.convReturnVertxGen(JDiscoveryService["create(io.vertx.core.Vertx)"](__args[0]._jdel), DiscoveryService);
  }else if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && (typeof __args[1] === 'object' && __args[1] != null)) {
    return utils.convReturnVertxGen(JDiscoveryService["create(io.vertx.core.Vertx,io.vertx.ext.discovery.DiscoveryOptions)"](__args[0]._jdel, __args[1] != null ? new DiscoveryOptions(new JsonObject(JSON.stringify(__args[1]))) : null), DiscoveryService);
  } else throw new TypeError('function invoked with invalid arguments');
};

/**
 Release the service object retrieved using <code>get</code> methods from the service type interface.
 It searches for the reference associated with the given object and release it.

 @memberof module:vertx-service-discovery-js/discovery_service
 @param discovery {DiscoveryService} the discovery service 
 @param svcObject {Object} the service object 
 */
DiscoveryService.releaseServiceObject = function(discovery, svcObject) {
  var __args = arguments;
  if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] !== 'function') {
    JDiscoveryService["releaseServiceObject(io.vertx.ext.discovery.DiscoveryService,java.lang.Object)"](discovery._jdel, utils.convParamTypeUnknown(svcObject));
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = DiscoveryService;