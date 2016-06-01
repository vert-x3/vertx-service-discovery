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

/** @module vertx-js/worker_executor */
var utils = require('vertx-js/util/utils');
var Measured = require('vertx-js/measured');
var Future = require('vertx-js/future');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JWorkerExecutor = io.vertx.core.WorkerExecutor;

/**
 An executor for executing blocking code in Vert.x .<p>

 @class
*/
var WorkerExecutor = function(j_val) {

  var j_workerExecutor = j_val;
  var that = this;
  Measured.call(this, j_val);

  /**
   Whether the metrics are enabled for this measured object

   @public

   @return {boolean} true if the metrics are enabled
   */
  this.isMetricsEnabled = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_workerExecutor["isMetricsEnabled()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Safely execute some blocking code.
   <p>
   Executes the blocking code in the handler <code>blockingCodeHandler</code> using a thread from the worker pool.
   <p>
   When the code is complete the handler <code>resultHandler</code> will be called with the result on the original context
   (e.g. on the original event loop of the caller).
   <p>
   A <code>Future</code> instance is passed into <code>blockingCodeHandler</code>. When the blocking code successfully completes,
   the handler should call the {@link Future#complete} or {@link Future#complete} method, or the {@link Future#fail}
   method if it failed.
   <p>
   In the <code>blockingCodeHandler</code> the current context remains the original context and therefore any task
   scheduled in the <code>blockingCodeHandler</code> will be executed on the this context and not on the worker thread.

   @public
   @param blockingCodeHandler {function} handler representing the blocking code to run 
   @param ordered {boolean} if true then if executeBlocking is called several times on the same context, the executions for that context will be executed serially, not in parallel. if false then they will be no ordering guarantees 
   @param resultHandler {function} handler that will be called when the blocking code is complete 
   */
  this.executeBlocking = function() {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'function' && typeof __args[1] === 'function') {
      j_workerExecutor["executeBlocking(io.vertx.core.Handler,io.vertx.core.Handler)"](function(jVal) {
      __args[0](utils.convReturnVertxGen(jVal, Future));
    }, function(ar) {
      if (ar.succeeded()) {
        __args[1](utils.convReturnTypeUnknown(ar.result()), null);
      } else {
        __args[1](null, ar.cause());
      }
    });
    }  else if (__args.length === 3 && typeof __args[0] === 'function' && typeof __args[1] ==='boolean' && typeof __args[2] === 'function') {
      j_workerExecutor["executeBlocking(io.vertx.core.Handler,boolean,io.vertx.core.Handler)"](function(jVal) {
      __args[0](utils.convReturnVertxGen(jVal, Future));
    }, __args[1], function(ar) {
      if (ar.succeeded()) {
        __args[2](utils.convReturnTypeUnknown(ar.result()), null);
      } else {
        __args[2](null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Close the executor.

   @public

   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_workerExecutor["close()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_workerExecutor;
};

// We export the Constructor function
module.exports = WorkerExecutor;