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

/** @module vertx-service-discovery-js/jdbc_data_source */
var utils = require('vertx-js/util/utils');
var JDBCClient = require('vertx-jdbc-js/jdbc_client');
var DiscoveryService = require('vertx-service-discovery-js/discovery_service');
var Vertx = require('vertx-js/vertx');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JJDBCDataSource = io.vertx.ext.discovery.types.JDBCDataSource;
var Record = io.vertx.ext.discovery.Record;

/**

 @class
*/
var JDBCDataSource = function(j_val) {

  var j_jDBCDataSource = j_val;
  var that = this;

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_jDBCDataSource;
};

/**

 @memberof module:vertx-service-discovery-js/jdbc_data_source
 @param name {string} 
 @param jdbcUrl {string} 
 @param metadata {Object} 
 @return {Object}
 */
JDBCDataSource.createRecord = function() {
  var __args = arguments;
  if (__args.length === 3 && typeof __args[0] === 'string' && (typeof __args[1] === 'object' && __args[1] != null) && (typeof __args[2] === 'object' && __args[2] != null)) {
    return utils.convReturnDataObject(JJDBCDataSource["createRecord(java.lang.String,io.vertx.core.json.JsonObject,io.vertx.core.json.JsonObject)"](__args[0], utils.convParamJsonObject(__args[1]), utils.convParamJsonObject(__args[2])));
  }else if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && (typeof __args[2] === 'object' && __args[2] != null)) {
    return utils.convReturnDataObject(JJDBCDataSource["createRecord(java.lang.String,java.lang.String,io.vertx.core.json.JsonObject)"](__args[0], __args[1], utils.convParamJsonObject(__args[2])));
  } else throw new TypeError('function invoked with invalid arguments');
};

/**

 @memberof module:vertx-service-discovery-js/jdbc_data_source
 @param vertx {Vertx} 
 @param discovery {DiscoveryService} 
 @param filter {Object} 
 @param consumerConfiguration {Object} 
 @param resultHandler {function} 
 */
JDBCDataSource.getJDBCClient = function() {
  var __args = arguments;
  if (__args.length === 4 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'object' && __args[1]._jdel && (typeof __args[2] === 'object' && __args[2] != null) && typeof __args[3] === 'function') {
    JJDBCDataSource["getJDBCClient(io.vertx.core.Vertx,io.vertx.ext.discovery.DiscoveryService,io.vertx.core.json.JsonObject,io.vertx.core.Handler)"](__args[0]._jdel, __args[1]._jdel, utils.convParamJsonObject(__args[2]), function(ar) {
    if (ar.succeeded()) {
      __args[3](utils.convReturnVertxGen(ar.result(), JDBCClient), null);
    } else {
      __args[3](null, ar.cause());
    }
  });
  }else if (__args.length === 5 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'object' && __args[1]._jdel && (typeof __args[2] === 'object' && __args[2] != null) && (typeof __args[3] === 'object' && __args[3] != null) && typeof __args[4] === 'function') {
    JJDBCDataSource["getJDBCClient(io.vertx.core.Vertx,io.vertx.ext.discovery.DiscoveryService,io.vertx.core.json.JsonObject,io.vertx.core.json.JsonObject,io.vertx.core.Handler)"](__args[0]._jdel, __args[1]._jdel, utils.convParamJsonObject(__args[2]), utils.convParamJsonObject(__args[3]), function(ar) {
    if (ar.succeeded()) {
      __args[4](utils.convReturnVertxGen(ar.result(), JDBCClient), null);
    } else {
      __args[4](null, ar.cause());
    }
  });
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = JDBCDataSource;