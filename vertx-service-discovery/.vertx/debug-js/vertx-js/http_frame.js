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

/** @module vertx-js/http_frame */
var utils = require('vertx-js/util/utils');
var Buffer = require('vertx-js/buffer');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JHttpFrame = io.vertx.core.http.HttpFrame;

/**
 An HTTP/2 frame.

 @class
*/
var HttpFrame = function(j_val) {

  var j_httpFrame = j_val;
  var that = this;

  /**
   @return the 8-bit type of the frame

   @public

   @return {number}
   */
  this.type = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedtype == null) {
        that.cachedtype = j_httpFrame["type()"]();
      }
      return that.cachedtype;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the 8-bit flags specific to the frame

   @public

   @return {number}
   */
  this.flags = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedflags == null) {
        that.cachedflags = j_httpFrame["flags()"]();
      }
      return that.cachedflags;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the frame payload

   @public

   @return {Buffer}
   */
  this.payload = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedpayload == null) {
        that.cachedpayload = utils.convReturnVertxGen(j_httpFrame["payload()"](), Buffer);
      }
      return that.cachedpayload;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_httpFrame;
};

// We export the Constructor function
module.exports = HttpFrame;