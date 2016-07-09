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

/** @module vertx-js/vertx */
var utils = require('vertx-js/util/utils');
var DatagramSocket = require('vertx-js/datagram_socket');
var HttpServer = require('vertx-js/http_server');
var Context = require('vertx-js/context');
var Future = require('vertx-js/future');
var SharedData = require('vertx-js/shared_data');
var WorkerExecutor = require('vertx-js/worker_executor');
var TimeoutStream = require('vertx-js/timeout_stream');
var DnsClient = require('vertx-js/dns_client');
var EventBus = require('vertx-js/event_bus');
var Measured = require('vertx-js/measured');
var NetServer = require('vertx-js/net_server');
var FileSystem = require('vertx-js/file_system');
var NetClient = require('vertx-js/net_client');
var HttpClient = require('vertx-js/http_client');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JVertx = io.vertx.core.Vertx;
var NetServerOptions = io.vertx.core.net.NetServerOptions;
var DeploymentOptions = io.vertx.core.DeploymentOptions;
var VertxOptions = io.vertx.core.VertxOptions;
var HttpServerOptions = io.vertx.core.http.HttpServerOptions;
var HttpClientOptions = io.vertx.core.http.HttpClientOptions;
var DatagramSocketOptions = io.vertx.core.datagram.DatagramSocketOptions;
var NetClientOptions = io.vertx.core.net.NetClientOptions;

/**
 The entry point into the Vert.x Core API.
 <p>
 You use an instance of this class for functionality including:
 <ul>
   <li>Creating TCP clients and servers</li>
   <li>Creating HTTP clients and servers</li>
   <li>Creating DNS clients</li>
   <li>Creating Datagram sockets</li>
   <li>Setting and cancelling periodic and one-shot timers</li>
   <li>Getting a reference to the event bus API</li>
   <li>Getting a reference to the file system API</li>
   <li>Getting a reference to the shared data API</li>
   <li>Deploying and undeploying verticles</li>
 </ul>
 <p>
 Most functionality in Vert.x core is fairly low level.
 <p>
 @class
*/
var Vertx = function(j_val) {

  var j_vertx = j_val;
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
      return j_vertx["isMetricsEnabled()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Gets the current context, or creates one if there isn't one

   @public

   @return {Context} The current context (created if didn't exist)
   */
  this.getOrCreateContext = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_vertx["getOrCreateContext()"](), Context);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Create a TCP/SSL server using the specified options

   @public
   @param options {Object} the options to use 
   @return {NetServer} the server
   */
  this.createNetServer = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_vertx["createNetServer()"](), NetServer);
    }  else if (__args.length === 1 && (typeof __args[0] === 'object' && __args[0] != null)) {
      return utils.convReturnVertxGen(j_vertx["createNetServer(io.vertx.core.net.NetServerOptions)"](__args[0] != null ? new NetServerOptions(new JsonObject(JSON.stringify(__args[0]))) : null), NetServer);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Create a TCP/SSL client using the specified options

   @public
   @param options {Object} the options to use 
   @return {NetClient} the client
   */
  this.createNetClient = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_vertx["createNetClient()"](), NetClient);
    }  else if (__args.length === 1 && (typeof __args[0] === 'object' && __args[0] != null)) {
      return utils.convReturnVertxGen(j_vertx["createNetClient(io.vertx.core.net.NetClientOptions)"](__args[0] != null ? new NetClientOptions(new JsonObject(JSON.stringify(__args[0]))) : null), NetClient);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Create an HTTP/HTTPS server using the specified options

   @public
   @param options {Object} the options to use 
   @return {HttpServer} the server
   */
  this.createHttpServer = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_vertx["createHttpServer()"](), HttpServer);
    }  else if (__args.length === 1 && (typeof __args[0] === 'object' && __args[0] != null)) {
      return utils.convReturnVertxGen(j_vertx["createHttpServer(io.vertx.core.http.HttpServerOptions)"](__args[0] != null ? new HttpServerOptions(new JsonObject(JSON.stringify(__args[0]))) : null), HttpServer);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Create a HTTP/HTTPS client using the specified options

   @public
   @param options {Object} the options to use 
   @return {HttpClient} the client
   */
  this.createHttpClient = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_vertx["createHttpClient()"](), HttpClient);
    }  else if (__args.length === 1 && (typeof __args[0] === 'object' && __args[0] != null)) {
      return utils.convReturnVertxGen(j_vertx["createHttpClient(io.vertx.core.http.HttpClientOptions)"](__args[0] != null ? new HttpClientOptions(new JsonObject(JSON.stringify(__args[0]))) : null), HttpClient);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Create a datagram socket using the specified options

   @public
   @param options {Object} the options to use 
   @return {DatagramSocket} the socket
   */
  this.createDatagramSocket = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_vertx["createDatagramSocket()"](), DatagramSocket);
    }  else if (__args.length === 1 && (typeof __args[0] === 'object' && __args[0] != null)) {
      return utils.convReturnVertxGen(j_vertx["createDatagramSocket(io.vertx.core.datagram.DatagramSocketOptions)"](__args[0] != null ? new DatagramSocketOptions(new JsonObject(JSON.stringify(__args[0]))) : null), DatagramSocket);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Get the filesystem object. There is a single instance of FileSystem per Vertx instance.

   @public

   @return {FileSystem} the filesystem object
   */
  this.fileSystem = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedfileSystem == null) {
        that.cachedfileSystem = utils.convReturnVertxGen(j_vertx["fileSystem()"](), FileSystem);
      }
      return that.cachedfileSystem;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Get the event bus object. There is a single instance of EventBus per Vertx instance.

   @public

   @return {EventBus} the event bus object
   */
  this.eventBus = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedeventBus == null) {
        that.cachedeventBus = utils.convReturnVertxGen(j_vertx["eventBus()"](), EventBus);
      }
      return that.cachedeventBus;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Create a DNS client to connect to a DNS server at the specified host and port

   @public
   @param port {number} the port 
   @param host {string} the host 
   @return {DnsClient} the DNS client
   */
  this.createDnsClient = function(port, host) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] === 'string') {
      return utils.convReturnVertxGen(j_vertx["createDnsClient(int,java.lang.String)"](port, host), DnsClient);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Get the shared data object. There is a single instance of SharedData per Vertx instance.

   @public

   @return {SharedData} the shared data object
   */
  this.sharedData = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedsharedData == null) {
        that.cachedsharedData = utils.convReturnVertxGen(j_vertx["sharedData()"](), SharedData);
      }
      return that.cachedsharedData;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a one-shot timer to fire after <code>delay</code> milliseconds, at which point <code>handler</code> will be called with
   the id of the timer.

   @public
   @param delay {number} the delay in milliseconds, after which the timer will fire 
   @param handler {function} the handler that will be called with the timer ID when the timer fires 
   @return {number} the unique ID of the timer
   */
  this.setTimer = function(delay, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] === 'function') {
      return j_vertx["setTimer(long,io.vertx.core.Handler)"](delay, function(jVal) {
      handler(utils.convReturnLong(jVal));
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns a one-shot timer as a read stream. The timer will be fired after <code>delay</code> milliseconds after
   the  has been called.

   @public
   @param delay {number} the delay in milliseconds, after which the timer will fire 
   @return {TimeoutStream} the timer stream
   */
  this.timerStream = function(delay) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return utils.convReturnVertxGen(j_vertx["timerStream(long)"](delay), TimeoutStream);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a periodic timer to fire every <code>delay</code> milliseconds, at which point <code>handler</code> will be called with
   the id of the timer.

   @public
   @param delay {number} the delay in milliseconds, after which the timer will fire 
   @param handler {function} the handler that will be called with the timer ID when the timer fires 
   @return {number} the unique ID of the timer
   */
  this.setPeriodic = function(delay, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] === 'function') {
      return j_vertx["setPeriodic(long,io.vertx.core.Handler)"](delay, function(jVal) {
      handler(utils.convReturnLong(jVal));
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns a periodic timer as a read stream. The timer will be fired every <code>delay</code> milliseconds after
   the  has been called.

   @public
   @param delay {number} the delay in milliseconds, after which the timer will fire 
   @return {TimeoutStream} the periodic stream
   */
  this.periodicStream = function(delay) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return utils.convReturnVertxGen(j_vertx["periodicStream(long)"](delay), TimeoutStream);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Cancels the timer with the specified <code>id</code>.

   @public
   @param id {number} The id of the timer to cancel 
   @return {boolean} true if the timer was successfully cancelled, or false if the timer does not exist.
   */
  this.cancelTimer = function(id) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return j_vertx["cancelTimer(long)"](id);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Puts the handler on the event queue for the current context so it will be run asynchronously ASAP after all
   preceeding events have been handled.

   @public
   @param action {function} - a handler representing the action to execute 
   */
  this.runOnContext = function(action) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_vertx["runOnContext(io.vertx.core.Handler)"](action);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Like {@link Vertx#close} but the completionHandler will be called when the close is complete

   @public
   @param completionHandler {function} The handler will be notified when the close is complete. 
   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_vertx["close()"]();
    }  else if (__args.length === 1 && typeof __args[0] === 'function') {
      j_vertx["close(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        __args[0](null, null);
      } else {
        __args[0](null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Like {@link Vertx#deployVerticle} but <a href="../../dataobjects.html#DeploymentOptions">DeploymentOptions</a> are provided to configure the
   deployment.

   @public
   @param name {string} the name 
   @param options {Object} the deployment options. 
   @param completionHandler {function} a handler which will be notified when the deployment is complete 
   */
  this.deployVerticle = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_vertx["deployVerticle(java.lang.String)"](__args[0]);
    }  else if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_vertx["deployVerticle(java.lang.String,io.vertx.core.Handler)"](__args[0], function(ar) {
      if (ar.succeeded()) {
        __args[1](ar.result(), null);
      } else {
        __args[1](null, ar.cause());
      }
    });
    }  else if (__args.length === 2 && typeof __args[0] === 'string' && (typeof __args[1] === 'object' && __args[1] != null)) {
      j_vertx["deployVerticle(java.lang.String,io.vertx.core.DeploymentOptions)"](__args[0], __args[1] != null ? new DeploymentOptions(new JsonObject(JSON.stringify(__args[1]))) : null);
    }  else if (__args.length === 3 && typeof __args[0] === 'string' && (typeof __args[1] === 'object' && __args[1] != null) && typeof __args[2] === 'function') {
      j_vertx["deployVerticle(java.lang.String,io.vertx.core.DeploymentOptions,io.vertx.core.Handler)"](__args[0], __args[1] != null ? new DeploymentOptions(new JsonObject(JSON.stringify(__args[1]))) : null, function(ar) {
      if (ar.succeeded()) {
        __args[2](ar.result(), null);
      } else {
        __args[2](null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Like [#undeploy(String)] {@link Vertx} but the completionHandler will be notified when the undeployment is complete.

   @public
   @param deploymentID {string} the deployment ID 
   @param completionHandler {function} a handler which will be notified when the undeployment is complete 
   */
  this.undeploy = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_vertx["undeploy(java.lang.String)"](__args[0]);
    }  else if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_vertx["undeploy(java.lang.String,io.vertx.core.Handler)"](__args[0], function(ar) {
      if (ar.succeeded()) {
        __args[1](null, null);
      } else {
        __args[1](null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Return a Set of deployment IDs for the currently deployed deploymentIDs.

   @public

   @return {Array.<string>} Set of deployment IDs
   */
  this.deploymentIDs = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnSet(j_vertx["deploymentIDs()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Is this Vert.x instance clustered?

   @public

   @return {boolean} true if clustered
   */
  this.isClustered = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_vertx["isClustered()"]();
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
      j_vertx["executeBlocking(io.vertx.core.Handler,io.vertx.core.Handler)"](function(jVal) {
      __args[0](utils.convReturnVertxGen(jVal, Future));
    }, function(ar) {
      if (ar.succeeded()) {
        __args[1](utils.convReturnTypeUnknown(ar.result()), null);
      } else {
        __args[1](null, ar.cause());
      }
    });
    }  else if (__args.length === 3 && typeof __args[0] === 'function' && typeof __args[1] ==='boolean' && typeof __args[2] === 'function') {
      j_vertx["executeBlocking(io.vertx.core.Handler,boolean,io.vertx.core.Handler)"](function(jVal) {
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
   Create a named worker executor, the executor should be closed when it's not needed anymore to release
   resources.<p/>
  
   This method can be called mutiple times with the same <code>name</code>. Executors with the same name will share
   the same worker pool. The worker pool size and max execute time are set when the worker pool is created and
   won't change after.<p>
  
   The worker pool is released when all the {@link WorkerExecutor} sharing the same name are closed.

   @public
   @param name {string} the name of the worker executor 
   @param poolSize {number} the size of the pool 
   @param maxExecuteTime {number} the value of max worker execute time, in ms 
   @return {WorkerExecutor} the named worker executor
   */
  this.createSharedWorkerExecutor = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnVertxGen(j_vertx["createSharedWorkerExecutor(java.lang.String)"](__args[0]), WorkerExecutor);
    }  else if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] ==='number') {
      return utils.convReturnVertxGen(j_vertx["createSharedWorkerExecutor(java.lang.String,int)"](__args[0], __args[1]), WorkerExecutor);
    }  else if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] ==='number' && typeof __args[2] ==='number') {
      return utils.convReturnVertxGen(j_vertx["createSharedWorkerExecutor(java.lang.String,int,long)"](__args[0], __args[1], __args[2]), WorkerExecutor);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a default exception handler for {@link Context}, set on  at creation.

   @public
   @param handler {function} the exception handler 
   @return {Vertx} a reference to this, so the API can be used fluently
   */
  this.exceptionHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_vertx["exceptionHandler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnThrowable(jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_vertx;
};

/**
 Creates a non clustered instance using the specified options

 @memberof module:vertx-js/vertx
 @param options {Object} the options to use 
 @return {Vertx} the instance
 */
Vertx.vertx = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return utils.convReturnVertxGen(JVertx["vertx()"](), Vertx);
  }else if (__args.length === 1 && (typeof __args[0] === 'object' && __args[0] != null)) {
    return utils.convReturnVertxGen(JVertx["vertx(io.vertx.core.VertxOptions)"](__args[0] != null ? new VertxOptions(new JsonObject(JSON.stringify(__args[0]))) : null), Vertx);
  } else throw new TypeError('function invoked with invalid arguments');
};

/**
 Creates a clustered instance using the specified options.
 <p>
 The instance is created asynchronously and the resultHandler is called with the result when it is ready.

 @memberof module:vertx-js/vertx
 @param options {Object} the options to use 
 @param resultHandler {function} the result handler that will receive the result 
 */
Vertx.clusteredVertx = function(options, resultHandler) {
  var __args = arguments;
  if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
    JVertx["clusteredVertx(io.vertx.core.VertxOptions,io.vertx.core.Handler)"](options != null ? new VertxOptions(new JsonObject(JSON.stringify(options))) : null, function(ar) {
    if (ar.succeeded()) {
      resultHandler(utils.convReturnVertxGen(ar.result(), Vertx), null);
    } else {
      resultHandler(null, ar.cause());
    }
  });
  } else throw new TypeError('function invoked with invalid arguments');
};

/**
 Gets the current context

 @memberof module:vertx-js/vertx

 @return {Context} The current context or null if no current context
 */
Vertx.currentContext = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return utils.convReturnVertxGen(JVertx["currentContext()"](), Context);
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = Vertx;