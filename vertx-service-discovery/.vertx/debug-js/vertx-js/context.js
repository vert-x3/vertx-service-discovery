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

/** @module vertx-js/context */
var utils = require('vertx-js/util/utils');
var Vertx = require('vertx-js/vertx');
var Future = require('vertx-js/future');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JContext = io.vertx.core.Context;

/**

 @class
*/
var Context = function(j_val) {

  var j_context = j_val;
  var that = this;

  /**
   Run the specified action asynchronously on the same context, some time after the current execution has completed.

   @public
   @param action {function} the action to run 
   */
  this.runOnContext = function(action) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_context["runOnContext(io.vertx.core.Handler)"](action);
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

   @public
   @param blockingCodeHandler {function} handler representing the blocking code to run 
   @param ordered {boolean} if true then if executeBlocking is called several times on the same context, the executions for that context will be executed serially, not in parallel. if false then they will be no ordering guarantees 
   @param resultHandler {function} handler that will be called when the blocking code is complete 
   */
  this.executeBlocking = function() {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'function' && typeof __args[1] === 'function') {
      j_context["executeBlocking(io.vertx.core.Handler,io.vertx.core.Handler)"](function(jVal) {
      __args[0](utils.convReturnVertxGen(jVal, Future));
    }, function(ar) {
      if (ar.succeeded()) {
        __args[1](utils.convReturnTypeUnknown(ar.result()), null);
      } else {
        __args[1](null, ar.cause());
      }
    });
    }  else if (__args.length === 3 && typeof __args[0] === 'function' && typeof __args[1] ==='boolean' && typeof __args[2] === 'function') {
      j_context["executeBlocking(io.vertx.core.Handler,boolean,io.vertx.core.Handler)"](function(jVal) {
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
   If the context is associated with a Verticle deployment, this returns the deployment ID of that deployment.

   @public

   @return {string} the deployment ID of the deployment or null if not a Verticle deployment
   */
  this.deploymentID = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_context["deploymentID()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   If the context is associated with a Verticle deployment, this returns the configuration that was specified when
   the verticle was deployed.

   @public

   @return {Object} the configuration of the deployment or null if not a Verticle deployment
   */
  this.config = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnJson(j_context["config()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   The process args

   @public

   @return {Array.<string>}
   */
  this.processArgs = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_context["processArgs()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Is the current context an event loop context?
   <p>
   NOTE! when running blocking code using {@link Vertx#executeBlocking} from a
   standard (not worker) verticle, the context will still an event loop context and this 
   will return true.

   @public

   @return {boolean} true if false otherwise
   */
  this.isEventLoopContext = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_context["isEventLoopContext()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Is the current context a worker context?
   <p>
   NOTE! when running blocking code using {@link Vertx#executeBlocking} from a
   standard (not worker) verticle, the context will still an event loop context and this 
   will return false.

   @public

   @return {boolean} true if the current context is a worker context, false otherwise
   */
  this.isWorkerContext = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_context["isWorkerContext()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Is the current context a multi-threaded worker context?

   @public

   @return {boolean} true if the current context is a multi-threaded worker context, false otherwise
   */
  this.isMultiThreadedWorkerContext = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_context["isMultiThreadedWorkerContext()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Get some data from the context.

   @public
   @param key {string} the key of the data 
   @return {Object} the data
   */
  this.get = function(key) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnTypeUnknown(j_context["get(java.lang.String)"](key));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Put some data in the context.
   <p>
   This can be used to share data between different handlers that share a context

   @public
   @param key {string} the key of the data 
   @param value {Object} the data 
   */
  this.put = function(key, value) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] !== 'function') {
      j_context["put(java.lang.String,java.lang.Object)"](key, utils.convParamTypeUnknown(value));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Remove some data from the context.

   @public
   @param key {string} the key to remove 
   @return {boolean} true if removed successfully, false otherwise
   */
  this.remove = function(key) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return j_context["remove(java.lang.String)"](key);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return The Vertx instance that created the context

   @public

   @return {Vertx}
   */
  this.owner = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_context["owner()"](), Vertx);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return  the number of instances of the verticle that were deployed in the deployment (if any) related
   to this context

   @public

   @return {number}
   */
  this.getInstanceCount = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_context["getInstanceCount()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set an exception handler called when the context runs an action throwing an uncaught throwable.<p/>
  
   When this handler is called, {@link Vertx#currentContext} will return this context.

   @public
   @param handler {function} the exception handler 
   @return {Context} a reference to this, so the API can be used fluently
   */
  this.exceptionHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_context["exceptionHandler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnThrowable(jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_context;
};

/**
 Is the current thread a worker thread?
 <p>
 NOTE! This is not always the same as calling {@link Context#isWorkerContext}. If you are running blocking code
 from an event loop context, then this will return true but {@link Context#isWorkerContext} will return false.

 @memberof module:vertx-js/context

 @return {boolean} true if current thread is a worker thread, false otherwise
 */
Context.isOnWorkerThread = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return JContext["isOnWorkerThread()"]();
  } else throw new TypeError('function invoked with invalid arguments');
};

/**
 Is the current thread an event thread?
 <p>
 NOTE! This is not always the same as calling {@link Context#isEventLoopContext}. If you are running blocking code
 from an event loop context, then this will return false but {@link Context#isEventLoopContext} will return true.

 @memberof module:vertx-js/context

 @return {boolean} true if current thread is a worker thread, false otherwise
 */
Context.isOnEventLoopThread = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return JContext["isOnEventLoopThread()"]();
  } else throw new TypeError('function invoked with invalid arguments');
};

/**
 Is the current thread a Vert.x thread? That's either a worker thread or an event loop thread

 @memberof module:vertx-js/context

 @return {boolean} true if current thread is a Vert.x thread, false otherwise
 */
Context.isOnVertxThread = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return JContext["isOnVertxThread()"]();
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = Context;