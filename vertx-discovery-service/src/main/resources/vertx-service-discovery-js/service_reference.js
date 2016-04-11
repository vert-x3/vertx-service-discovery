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

/** @module vertx-service-discovery-js/service_reference */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JServiceReference = io.vertx.ext.discovery.ServiceReference;
var Record = io.vertx.ext.discovery.Record;

/**

 @class
*/
var ServiceReference = function(j_val) {

  var j_serviceReference = j_val;
  var that = this;

  /**
   @return the service record.

   @public

   @return {Object}
   */
  this.record = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedrecord == null) {
        that.cachedrecord = utils.convReturnDataObject(j_serviceReference["record()"]());
      }
      return that.cachedrecord;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Gets the object to access the service. It can be a proxy, a client or whatever object. The type depends on the
   service type and the server itself.

   @public

   @return {Object} the object to access the service
   */
  this.get = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnTypeUnknown(j_serviceReference["get()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Releases the reference. Once released, the consumer must not use the reference anymore.

   @public

   */
  this.release = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_serviceReference["release()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_serviceReference;
};

// We export the Constructor function
module.exports = ServiceReference;