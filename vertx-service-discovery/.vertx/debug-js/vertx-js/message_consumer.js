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

/** @module vertx-js/message_consumer */
var utils = require('vertx-js/util/utils');
var ReadStream = require('vertx-js/read_stream');
var Message = require('vertx-js/message');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JMessageConsumer = io.vertx.core.eventbus.MessageConsumer;

/**

 @class
*/
var MessageConsumer = function(j_val) {

  var j_messageConsumer = j_val;
  var that = this;
  ReadStream.call(this, j_val);

  /**

   @public
   @param handler {function} 
   @return {MessageConsumer}
   */
  this.exceptionHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_messageConsumer["exceptionHandler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnThrowable(jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {MessageConsumer}
   */
  this.handler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_messageConsumer["handler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnVertxGen(jVal, Message));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {MessageConsumer}
   */
  this.pause = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_messageConsumer["pause()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {MessageConsumer}
   */
  this.resume = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_messageConsumer["resume()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param endHandler {function} 
   @return {MessageConsumer}
   */
  this.endHandler = function(endHandler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_messageConsumer["endHandler(io.vertx.core.Handler)"](endHandler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return a read stream for the body of the message stream.

   @public

   @return {ReadStream}
   */
  this.bodyStream = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_messageConsumer["bodyStream()"](), ReadStream);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return true if the current consumer is registered

   @public

   @return {boolean}
   */
  this.isRegistered = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_messageConsumer["isRegistered()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return The address the handler was registered with.

   @public

   @return {string}
   */
  this.address = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_messageConsumer["address()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set the number of messages this registration will buffer when this stream is paused. The default
   value is <code>0</code>. When a new value is set, buffered messages may be discarded to reach
   the new value.

   @public
   @param maxBufferedMessages {number} the maximum number of messages that can be buffered 
   @return {MessageConsumer} this registration
   */
  this.setMaxBufferedMessages = function(maxBufferedMessages) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return utils.convReturnVertxGen(j_messageConsumer["setMaxBufferedMessages(int)"](maxBufferedMessages), MessageConsumer);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the maximum number of messages that can be buffered when this stream is paused

   @public

   @return {number}
   */
  this.getMaxBufferedMessages = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_messageConsumer["getMaxBufferedMessages()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Optional method which can be called to indicate when the registration has been propagated across the cluster.

   @public
   @param completionHandler {function} the completion handler 
   */
  this.completionHandler = function(completionHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_messageConsumer["completionHandler(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        completionHandler(null, null);
      } else {
        completionHandler(null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Unregisters the handler which created this registration

   @public
   @param completionHandler {function} the handler called when the unregister is done. For example in a cluster when all nodes of the event bus have been unregistered. 
   */
  this.unregister = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_messageConsumer["unregister()"]();
    }  else if (__args.length === 1 && typeof __args[0] === 'function') {
      j_messageConsumer["unregister(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        __args[0](null, null);
      } else {
        __args[0](null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_messageConsumer;
};

// We export the Constructor function
module.exports = MessageConsumer;