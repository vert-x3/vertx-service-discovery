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

/** @module vertx-js/http_server_request_stream */
var utils = require('vertx-js/util/utils');
var HttpServerRequest = require('vertx-js/http_server_request');
var ReadStream = require('vertx-js/read_stream');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JHttpServerRequestStream = io.vertx.core.http.HttpServerRequestStream;

/**

 @class
*/
var HttpServerRequestStream = function(j_val) {

  var j_httpServerRequestStream = j_val;
  var that = this;
  ReadStream.call(this, j_val);

  /**

   @public
   @param handler {function} 
   @return {HttpServerRequestStream}
   */
  this.exceptionHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_httpServerRequestStream["exceptionHandler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnThrowable(jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {HttpServerRequestStream}
   */
  this.handler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_httpServerRequestStream["handler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnVertxGen(jVal, HttpServerRequest));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {HttpServerRequestStream}
   */
  this.pause = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_httpServerRequestStream["pause()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {HttpServerRequestStream}
   */
  this.resume = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_httpServerRequestStream["resume()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param endHandler {function} 
   @return {HttpServerRequestStream}
   */
  this.endHandler = function(endHandler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_httpServerRequestStream["endHandler(io.vertx.core.Handler)"](endHandler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_httpServerRequestStream;
};

// We export the Constructor function
module.exports = HttpServerRequestStream;