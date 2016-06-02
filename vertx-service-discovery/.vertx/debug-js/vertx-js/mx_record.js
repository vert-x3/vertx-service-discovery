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

/** @module vertx-js/mx_record */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JMxRecord = io.vertx.core.dns.MxRecord;

/**
 Represent a Mail-Exchange-Record (MX) which was resolved for a domain.

 @class
*/
var MxRecord = function(j_val) {

  var j_mxRecord = j_val;
  var that = this;

  /**
   The priority of the MX record.

   @public

   @return {number}
   */
  this.priority = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_mxRecord["priority()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   The name of the MX record

   @public

   @return {string}
   */
  this.name = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_mxRecord["name()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_mxRecord;
};

// We export the Constructor function
module.exports = MxRecord;