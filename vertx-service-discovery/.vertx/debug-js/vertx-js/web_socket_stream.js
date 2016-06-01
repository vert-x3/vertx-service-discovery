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

/** @module vertx-js/web_socket_stream */
var utils = require('vertx-js/util/utils');
var ReadStream = require('vertx-js/read_stream');
var WebSocket = require('vertx-js/web_socket');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JWebSocketStream = io.vertx.core.http.WebSocketStream;

/**

 @class
*/
var WebSocketStream = function(j_val) {

  var j_webSocketStream = j_val;
  var that = this;
  ReadStream.call(this, j_val);

  /**

   @public
   @param handler {function} 
   @return {WebSocketStream}
   */
  this.exceptionHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_webSocketStream["exceptionHandler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnThrowable(jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {WebSocketStream}
   */
  this.handler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_webSocketStream["handler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnVertxGen(jVal, WebSocket));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {WebSocketStream}
   */
  this.pause = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_webSocketStream["pause()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {WebSocketStream}
   */
  this.resume = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_webSocketStream["resume()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param endHandler {function} 
   @return {WebSocketStream}
   */
  this.endHandler = function(endHandler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_webSocketStream["endHandler(io.vertx.core.Handler)"](endHandler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_webSocketStream;
};

// We export the Constructor function
module.exports = WebSocketStream;