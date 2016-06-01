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

/** @module vertx-js/message */
var utils = require('vertx-js/util/utils');
var MultiMap = require('vertx-js/multi_map');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JMessage = io.vertx.core.eventbus.Message;
var DeliveryOptions = io.vertx.core.eventbus.DeliveryOptions;

/**
 Represents a message that is received from the event bus in a handler.
 <p>
 @class
*/
var Message = function(j_val) {

  var j_message = j_val;
  var that = this;

  /**
   The address the message was sent to

   @public

   @return {string}
   */
  this.address = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_message["address()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Multi-map of message headers. Can be empty

   @public

   @return {MultiMap} the headers
   */
  this.headers = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_message["headers()"](), MultiMap);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   The body of the message. Can be null.

   @public

   @return {Object} the body, or null.
   */
  this.body = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedbody == null) {
        that.cachedbody = utils.convReturnTypeUnknown(j_message["body()"]());
      }
      return that.cachedbody;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   The reply address. Can be null.

   @public

   @return {string} the reply address, or null, if message was sent without a reply handler.
   */
  this.replyAddress = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_message["replyAddress()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   The same as <code>reply(R message, DeliveryOptions)</code> but you can specify handler for the reply - i.e.
   to receive the reply to the reply.

   @public
   @param message {Object} the reply message 
   @param options {Object} the delivery options 
   @param replyHandler {function} the reply handler for the reply. 
   */
  this.reply = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] !== 'function') {
      j_message["reply(java.lang.Object)"](utils.convParamTypeUnknown(__args[0]));
    }  else if (__args.length === 2 && typeof __args[0] !== 'function' && typeof __args[1] === 'function') {
      j_message["reply(java.lang.Object,io.vertx.core.Handler)"](utils.convParamTypeUnknown(__args[0]), function(ar) {
      if (ar.succeeded()) {
        __args[1](utils.convReturnVertxGen(ar.result(), Message), null);
      } else {
        __args[1](null, ar.cause());
      }
    });
    }  else if (__args.length === 2 && typeof __args[0] !== 'function' && (typeof __args[1] === 'object' && __args[1] != null)) {
      j_message["reply(java.lang.Object,io.vertx.core.eventbus.DeliveryOptions)"](utils.convParamTypeUnknown(__args[0]), __args[1] != null ? new DeliveryOptions(new JsonObject(JSON.stringify(__args[1]))) : null);
    }  else if (__args.length === 3 && typeof __args[0] !== 'function' && (typeof __args[1] === 'object' && __args[1] != null) && typeof __args[2] === 'function') {
      j_message["reply(java.lang.Object,io.vertx.core.eventbus.DeliveryOptions,io.vertx.core.Handler)"](utils.convParamTypeUnknown(__args[0]), __args[1] != null ? new DeliveryOptions(new JsonObject(JSON.stringify(__args[1]))) : null, function(ar) {
      if (ar.succeeded()) {
        __args[2](utils.convReturnVertxGen(ar.result(), Message), null);
      } else {
        __args[2](null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Signal to the sender that processing of this message failed.
   <p>
   If the message was sent specifying a result handler
   the handler will be called with a failure corresponding to the failure code and message specified here.

   @public
   @param failureCode {number} A failure code to pass back to the sender 
   @param message {string} A message to pass back to the sender 
   */
  this.fail = function(failureCode, message) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] === 'string') {
      j_message["fail(int,java.lang.String)"](failureCode, message);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_message;
};

// We export the Constructor function
module.exports = Message;