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

/** @module vertx-js/http_client_response */
var utils = require('vertx-js/util/utils');
var Buffer = require('vertx-js/buffer');
var HttpFrame = require('vertx-js/http_frame');
var ReadStream = require('vertx-js/read_stream');
var MultiMap = require('vertx-js/multi_map');
var NetSocket = require('vertx-js/net_socket');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JHttpClientResponse = io.vertx.core.http.HttpClientResponse;

/**
 Represents a client-side HTTP response.
 <p>
 @class
*/
var HttpClientResponse = function(j_val) {

  var j_httpClientResponse = j_val;
  var that = this;
  ReadStream.call(this, j_val);

  /**

   @public

   @return {HttpClientResponse}
   */
  this.resume = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_httpClientResponse["resume()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {HttpClientResponse}
   */
  this.exceptionHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_httpClientResponse["exceptionHandler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnThrowable(jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {HttpClientResponse}
   */
  this.handler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_httpClientResponse["handler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnVertxGen(jVal, Buffer));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {HttpClientResponse}
   */
  this.pause = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_httpClientResponse["pause()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param endHandler {function} 
   @return {HttpClientResponse}
   */
  this.endHandler = function(endHandler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_httpClientResponse["endHandler(io.vertx.core.Handler)"](endHandler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the version of the response

   @public

   @return {Object}
   */
  this.version = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnEnum(j_httpClientResponse["version()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the status code of the response

   @public

   @return {number}
   */
  this.statusCode = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_httpClientResponse["statusCode()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the status message of the response

   @public

   @return {string}
   */
  this.statusMessage = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_httpClientResponse["statusMessage()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the headers

   @public

   @return {MultiMap}
   */
  this.headers = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedheaders == null) {
        that.cachedheaders = utils.convReturnVertxGen(j_httpClientResponse["headers()"](), MultiMap);
      }
      return that.cachedheaders;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Return the first header value with the specified name

   @public
   @param headerName {string} the header name 
   @return {string} the header value
   */
  this.getHeader = function(headerName) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return j_httpClientResponse["getHeader(java.lang.String)"](headerName);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Return the first trailer value with the specified name

   @public
   @param trailerName {string} the trailer name 
   @return {string} the trailer value
   */
  this.getTrailer = function(trailerName) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return j_httpClientResponse["getTrailer(java.lang.String)"](trailerName);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the trailers

   @public

   @return {MultiMap}
   */
  this.trailers = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedtrailers == null) {
        that.cachedtrailers = utils.convReturnVertxGen(j_httpClientResponse["trailers()"](), MultiMap);
      }
      return that.cachedtrailers;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the Set-Cookie headers (including trailers)

   @public

   @return {Array.<string>}
   */
  this.cookies = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedcookies == null) {
        that.cachedcookies = j_httpClientResponse["cookies()"]();
      }
      return that.cachedcookies;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Convenience method for receiving the entire request body in one piece.
   <p>
   This saves you having to manually set a dataHandler and an endHandler and append the chunks of the body until
   the whole body received. Don't use this if your request body is large - you could potentially run out of RAM.

   @public
   @param bodyHandler {function} This handler will be called after all the body has been received 
   @return {HttpClientResponse}
   */
  this.bodyHandler = function(bodyHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_httpClientResponse["bodyHandler(io.vertx.core.Handler)"](function(jVal) {
      bodyHandler(utils.convReturnVertxGen(jVal, Buffer));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set an custom frame handler. The handler will get notified when the http stream receives an custom HTTP/2
   frame. HTTP/2 permits extension of the protocol.

   @public
   @param handler {function} 
   @return {HttpClientResponse} a reference to this, so the API can be used fluently
   */
  this.customFrameHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_httpClientResponse["customFrameHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(jVal, HttpFrame));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Get a net socket for the underlying connection of this request.
   <p>
   USE THIS WITH CAUTION! Writing to the socket directly if you don't know what you're doing can easily break the HTTP protocol
   <p>
   One valid use-case for calling this is to receive the {@link NetSocket} after a HTTP CONNECT was issued to the
   remote peer and it responded with a status code of 200.

   @public

   @return {NetSocket} the net socket
   */
  this.netSocket = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachednetSocket == null) {
        that.cachednetSocket = utils.convReturnVertxGen(j_httpClientResponse["netSocket()"](), NetSocket);
      }
      return that.cachednetSocket;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_httpClientResponse;
};

// We export the Constructor function
module.exports = HttpClientResponse;