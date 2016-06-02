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

/** @module vertx-js/datagram_packet */
var utils = require('vertx-js/util/utils');
var Buffer = require('vertx-js/buffer');
var SocketAddress = require('vertx-js/socket_address');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JDatagramPacket = io.vertx.core.datagram.DatagramPacket;

/**
 A received datagram packet (UDP) which contains the data and information about the sender of the data itself.

 @class
*/
var DatagramPacket = function(j_val) {

  var j_datagramPacket = j_val;
  var that = this;

  /**
   Returns the {@link SocketAddress} of the sender that sent
   this {@link DatagramPacket}.

   @public

   @return {SocketAddress} the address of the sender
   */
  this.sender = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_datagramPacket["sender()"](), SocketAddress);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns the data of the {@link DatagramPacket}

   @public

   @return {Buffer} the data
   */
  this.data = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_datagramPacket["data()"](), Buffer);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_datagramPacket;
};

// We export the Constructor function
module.exports = DatagramPacket;