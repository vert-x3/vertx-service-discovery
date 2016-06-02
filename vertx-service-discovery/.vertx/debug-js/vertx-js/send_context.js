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

/** @module vertx-js/send_context */
var utils = require('vertx-js/util/utils');
var Message = require('vertx-js/message');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JSendContext = io.vertx.core.eventbus.SendContext;

/**

 Encapsulates a message being sent from Vert.x. Used with event bus interceptors

 @class
*/
var SendContext = function(j_val) {

  var j_sendContext = j_val;
  var that = this;

  /**
   @return  The message being sent

   @public

   @return {Message}
   */
  this.message = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_sendContext["message()"](), Message);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Call the next interceptor

   @public

   */
  this.next = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_sendContext["next()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {boolean} true if the message is being sent (point to point) or False if the message is being published
   */
  this.send = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_sendContext["send()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_sendContext;
};

// We export the Constructor function
module.exports = SendContext;