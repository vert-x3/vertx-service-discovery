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

/** @module vertx-js/shared_data */
var utils = require('vertx-js/util/utils');
var AsyncMap = require('vertx-js/async_map');
var Lock = require('vertx-js/lock');
var LocalMap = require('vertx-js/local_map');
var Counter = require('vertx-js/counter');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JSharedData = io.vertx.core.shareddata.SharedData;

/**
 Shared data allows you to share data safely between different parts of your application in a safe way.
 <p>
 Shared data provides:
 <ul>
   <li>Cluster wide maps which can be accessed from any node of the cluster</li>
   <li>Cluster wide locks which can be used to give exclusive access to resources across the cluster</li>
   <li>Cluster wide counters used to maintain counts consistently across the cluster</li>
   <li>Local maps for sharing data safely in the same Vert.x instance</li>
 </ul>
 <p>
 Please see the documentation for more information.

 @class
*/
var SharedData = function(j_val) {

  var j_sharedData = j_val;
  var that = this;

  /**
   Get the cluster wide map with the specified name. The map is accessible to all nodes in the cluster and data
   put into the map from any node is visible to to any other node.

   @public
   @param name {string} the name of the map 
   @param resultHandler {function} the map will be returned asynchronously in this handler 
   */
  this.getClusterWideMap = function(name, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_sharedData["getClusterWideMap(java.lang.String,io.vertx.core.Handler)"](name, function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnVertxGen(ar.result(), AsyncMap), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Get a cluster wide lock with the specified name. The lock will be passed to the handler when it is available.

   @public
   @param name {string} the name of the lock 
   @param resultHandler {function} the handler 
   */
  this.getLock = function(name, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_sharedData["getLock(java.lang.String,io.vertx.core.Handler)"](name, function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnVertxGen(ar.result(), Lock), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Like {@link SharedData#getLock} but specifying a timeout. If the lock is not obtained within the timeout
   a failure will be sent to the handler

   @public
   @param name {string} the name of the lock 
   @param timeout {number} the timeout in ms 
   @param resultHandler {function} the handler 
   */
  this.getLockWithTimeout = function(name, timeout, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] ==='number' && typeof __args[2] === 'function') {
      j_sharedData["getLockWithTimeout(java.lang.String,long,io.vertx.core.Handler)"](name, timeout, function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnVertxGen(ar.result(), Lock), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Get a cluster wide counter. The counter will be passed to the handler.

   @public
   @param name {string} the name of the counter. 
   @param resultHandler {function} the handler 
   */
  this.getCounter = function(name, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_sharedData["getCounter(java.lang.String,io.vertx.core.Handler)"](name, function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnVertxGen(ar.result(), Counter), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Return a <code>LocalMap</code> with the specific <code>name</code>.

   @public
   @param name {string} the name of the map 
   @return {LocalMap} the msp
   */
  this.getLocalMap = function(name) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnVertxGen(j_sharedData["getLocalMap(java.lang.String)"](name), LocalMap);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_sharedData;
};

// We export the Constructor function
module.exports = SharedData;