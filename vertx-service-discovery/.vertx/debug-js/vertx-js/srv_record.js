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

/** @module vertx-js/srv_record */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JSrvRecord = io.vertx.core.dns.SrvRecord;

/**
 Represent a Service-Record (SRV) which was resolved for a domain.

 @class
*/
var SrvRecord = function(j_val) {

  var j_srvRecord = j_val;
  var that = this;

  /**
   Returns the priority for this service record.

   @public

   @return {number}
   */
  this.priority = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_srvRecord["priority()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns the weight of this service record.

   @public

   @return {number}
   */
  this.weight = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_srvRecord["weight()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns the port the service is running on.

   @public

   @return {number}
   */
  this.port = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_srvRecord["port()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns the name for the server being queried.

   @public

   @return {string}
   */
  this.name = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_srvRecord["name()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns the protocol for the service being queried (i.e. "_tcp").

   @public

   @return {string}
   */
  this.protocol = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_srvRecord["protocol()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns the service's name (i.e. "_http").

   @public

   @return {string}
   */
  this.service = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_srvRecord["service()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns the name of the host for the service.

   @public

   @return {string}
   */
  this.target = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_srvRecord["target()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_srvRecord;
};

// We export the Constructor function
module.exports = SrvRecord;