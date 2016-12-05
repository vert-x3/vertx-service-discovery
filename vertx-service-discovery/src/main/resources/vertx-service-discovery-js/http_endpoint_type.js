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

/** @module vertx-service-discovery-js/http_endpoint_type */
var utils = require('vertx-js/util/utils');
var ServiceReference = require('vertx-service-discovery-js/service_reference');
var HttpClient = require('vertx-js/http_client');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JHttpEndpointType = io.vertx.servicediscovery.types.HttpEndpointType;

/**

 @class
*/
var HttpEndpointType = function(j_val) {

  var j_httpEndpointType = j_val;
  var that = this;

  /**

   @public
   @param ref {ServiceReference} 
   @return {HttpClient}
   */
  this.getService = function(ref) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      return utils.convReturnVertxGen(HttpClient, j_httpEndpointType["getService(io.vertx.servicediscovery.ServiceReference)"](ref._jdel));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param ref {ServiceReference} 
   @return {HttpClient}
   */
  this.cachedService = function(ref) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      return utils.convReturnVertxGen(HttpClient, j_httpEndpointType["cachedService(io.vertx.servicediscovery.ServiceReference)"](ref._jdel));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_httpEndpointType;
};

HttpEndpointType._jclass = utils.getJavaClass("io.vertx.servicediscovery.types.HttpEndpointType");
HttpEndpointType._jtype = {
  accept: function(obj) {
    return HttpEndpointType._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(HttpEndpointType.prototype, {});
    HttpEndpointType.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
HttpEndpointType._create = function(jdel) {
  var obj = Object.create(HttpEndpointType.prototype, {});
  HttpEndpointType.apply(obj, arguments);
  return obj;
}
module.exports = HttpEndpointType;