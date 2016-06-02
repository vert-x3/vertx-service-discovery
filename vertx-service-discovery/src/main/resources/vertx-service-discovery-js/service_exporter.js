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

/** @module vertx-service-discovery-js/service_exporter */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JServiceExporter = io.vertx.servicediscovery.spi.ServiceExporter;
var Record = io.vertx.servicediscovery.Record;

/**
 The service exporter allows integrate other discovery technologies with the Vert.x service discovery. It maps
 @class
*/
var ServiceExporter = function(j_val) {

  var j_serviceExporter = j_val;
  var that = this;

  /**

   @public

   */
  this.onPublication = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_serviceExporter["onPublication()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param records {Array.<Object>} 
   */
  this.init = function(records) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0] instanceof Array) {
      j_serviceExporter["init(java.util.List)"](utils.convParamListDataObject(records, function(json) { return new Record(json); }));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_serviceExporter["close()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_serviceExporter;
};

// We export the Constructor function
module.exports = ServiceExporter;