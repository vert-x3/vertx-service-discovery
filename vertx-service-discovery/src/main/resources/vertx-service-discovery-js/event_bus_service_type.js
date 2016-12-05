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

/** @module vertx-service-discovery-js/event_bus_service_type */
var utils = require('vertx-js/util/utils');
var ServiceReference = require('vertx-service-discovery-js/service_reference');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JEventBusServiceType = io.vertx.servicediscovery.types.EventBusServiceType;

/**
 TODO

 @class
*/
var EventBusServiceType = function(j_val, j_arg_0) {

  var j_eventBusServiceType = j_val;
  var that = this;
  var j_T = typeof j_arg_0 !== 'undefined' ? j_arg_0 : utils.unknown_jtype;

  /**

   @public
   @param ref {ServiceReference} 
   @return {Object}
   */
  this.getService = function(ref) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      return j_T.wrap(j_eventBusServiceType["getService(io.vertx.servicediscovery.ServiceReference)"](ref._jdel));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param ref {ServiceReference} 
   @return {Object}
   */
  this.cachedService = function(ref) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      return j_T.wrap(j_eventBusServiceType["cachedService(io.vertx.servicediscovery.ServiceReference)"](ref._jdel));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_eventBusServiceType;
};

EventBusServiceType._jclass = utils.getJavaClass("io.vertx.servicediscovery.types.EventBusServiceType");
EventBusServiceType._jtype = {
  accept: function(obj) {
    return EventBusServiceType._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(EventBusServiceType.prototype, {});
    EventBusServiceType.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
EventBusServiceType._create = function(jdel) {
  var obj = Object.create(EventBusServiceType.prototype, {});
  EventBusServiceType.apply(obj, arguments);
  return obj;
}
module.exports = EventBusServiceType;