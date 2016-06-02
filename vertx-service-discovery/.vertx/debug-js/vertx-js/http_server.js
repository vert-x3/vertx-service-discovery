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

/** @module vertx-js/http_server */
var utils = require('vertx-js/util/utils');
var HttpServerRequest = require('vertx-js/http_server_request');
var ServerWebSocket = require('vertx-js/server_web_socket');
var Measured = require('vertx-js/measured');
var HttpServerRequestStream = require('vertx-js/http_server_request_stream');
var HttpConnection = require('vertx-js/http_connection');
var ServerWebSocketStream = require('vertx-js/server_web_socket_stream');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JHttpServer = io.vertx.core.http.HttpServer;

/**
 An HTTP and WebSockets server.
 <p>
 @class
*/
var HttpServer = function(j_val) {

  var j_httpServer = j_val;
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
      return j_httpServer["isMetricsEnabled()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Return the request stream for the server. As HTTP requests are received by the server,
   instances of {@link HttpServerRequest} will be created and passed to the stream .

   @public

   @return {HttpServerRequestStream} the request stream
   */
  this.requestStream = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedrequestStream == null) {
        that.cachedrequestStream = utils.convReturnVertxGen(j_httpServer["requestStream()"](), HttpServerRequestStream);
      }
      return that.cachedrequestStream;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set the request handler for the server to <code>requestHandler</code>. As HTTP requests are received by the server,
   instances of {@link HttpServerRequest} will be created and passed to this handler.

   @public
   @param handler {function} 
   @return {HttpServer} a reference to this, so the API can be used fluently
   */
  this.requestHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_httpServer["requestHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(jVal, HttpServerRequest));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a connection handler for the server.

   @public
   @param handler {function} 
   @return {HttpServer} a reference to this, so the API can be used fluently
   */
  this.connectionHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_httpServer["connectionHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(jVal, HttpConnection));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Return the websocket stream for the server. If a websocket connect handshake is successful a
   new {@link ServerWebSocket} instance will be created and passed to the stream .

   @public

   @return {ServerWebSocketStream} the websocket stream
   */
  this.websocketStream = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedwebsocketStream == null) {
        that.cachedwebsocketStream = utils.convReturnVertxGen(j_httpServer["websocketStream()"](), ServerWebSocketStream);
      }
      return that.cachedwebsocketStream;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set the websocket handler for the server to <code>wsHandler</code>. If a websocket connect handshake is successful a
   new {@link ServerWebSocket} instance will be created and passed to the handler.

   @public
   @param handler {function} 
   @return {HttpServer} a reference to this, so the API can be used fluently
   */
  this.websocketHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_httpServer["websocketHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(jVal, ServerWebSocket));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Like {@link HttpServer#listen} but supplying a handler that will be called when the server is actually
   listening (or has failed).

   @public
   @param port {number} the port to listen on 
   @param host {string} the host to listen on 
   @param listenHandler {function} the listen handler 
   @return {HttpServer}
   */
  this.listen = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_httpServer["listen()"]();
      return that;
    }  else if (__args.length === 1 && typeof __args[0] ==='number') {
      j_httpServer["listen(int)"](__args[0]);
      return that;
    }  else if (__args.length === 1 && typeof __args[0] === 'function') {
      j_httpServer["listen(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        __args[0](utils.convReturnVertxGen(ar.result(), HttpServer), null);
      } else {
        __args[0](null, ar.cause());
      }
    });
      return that;
    }  else if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] === 'string') {
      j_httpServer["listen(int,java.lang.String)"](__args[0], __args[1]);
      return that;
    }  else if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] === 'function') {
      j_httpServer["listen(int,io.vertx.core.Handler)"](__args[0], function(ar) {
      if (ar.succeeded()) {
        __args[1](utils.convReturnVertxGen(ar.result(), HttpServer), null);
      } else {
        __args[1](null, ar.cause());
      }
    });
      return that;
    }  else if (__args.length === 3 && typeof __args[0] ==='number' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_httpServer["listen(int,java.lang.String,io.vertx.core.Handler)"](__args[0], __args[1], function(ar) {
      if (ar.succeeded()) {
        __args[2](utils.convReturnVertxGen(ar.result(), HttpServer), null);
      } else {
        __args[2](null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Like {@link HttpServer#close} but supplying a handler that will be called when the server is actually closed (or has failed).

   @public
   @param completionHandler {function} the handler 
   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_httpServer["close()"]();
    }  else if (__args.length === 1 && typeof __args[0] === 'function') {
      j_httpServer["close(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        __args[0](null, null);
      } else {
        __args[0](null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   The actual port the server is listening on. This is useful if you bound the server specifying 0 as port number
   signifying an ephemeral port

   @public

   @return {number} the actual port the server is listening on.
   */
  this.actualPort = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_httpServer["actualPort()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_httpServer;
};

// We export the Constructor function
module.exports = HttpServer;