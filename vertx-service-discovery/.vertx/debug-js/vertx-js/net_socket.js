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

/** @module vertx-js/net_socket */
var utils = require('vertx-js/util/utils');
var Buffer = require('vertx-js/buffer');
var WriteStream = require('vertx-js/write_stream');
var ReadStream = require('vertx-js/read_stream');
var SocketAddress = require('vertx-js/socket_address');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JNetSocket = io.vertx.core.net.NetSocket;

/**
 Represents a socket-like interface to a TCP connection on either the
 client or the server side.
 <p>
 @class
*/
var NetSocket = function(j_val) {

  var j_netSocket = j_val;
  var that = this;
  ReadStream.call(this, j_val);
  WriteStream.call(this, j_val);

  /**
   Same as {@link NetSocket#end} but writes some data to the stream before ending.

   @public
   @param t {Buffer} 
   */
  this.end = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_netSocket["end()"]();
    }  else if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_netSocket["end(io.vertx.core.buffer.Buffer)"](__args[0]._jdel);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   This will return <code>true</code> if there are more bytes in the write queue than the value set using {@link NetSocket#setWriteQueueMaxSize}

   @public

   @return {boolean} true if write queue is full
   */
  this.writeQueueFull = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_netSocket["writeQueueFull()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {NetSocket}
   */
  this.exceptionHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_netSocket["exceptionHandler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnThrowable(jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {NetSocket}
   */
  this.handler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_netSocket["handler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnVertxGen(jVal, Buffer));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {NetSocket}
   */
  this.pause = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_netSocket["pause()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {NetSocket}
   */
  this.resume = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_netSocket["resume()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param endHandler {function} 
   @return {NetSocket}
   */
  this.endHandler = function(endHandler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_netSocket["endHandler(io.vertx.core.Handler)"](endHandler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Write a String to the connection, encoded using the encoding <code>enc</code>.

   @public
   @param str {string} the string to write 
   @param enc {string} the encoding to use 
   @return {NetSocket} a reference to this, so the API can be used fluently
   */
  this.write = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_netSocket["write(io.vertx.core.buffer.Buffer)"](__args[0]._jdel);
      return that;
    }  else if (__args.length === 1 && typeof __args[0] === 'string') {
      j_netSocket["write(java.lang.String)"](__args[0]);
      return that;
    }  else if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'string') {
      j_netSocket["write(java.lang.String,java.lang.String)"](__args[0], __args[1]);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param maxSize {number} 
   @return {NetSocket}
   */
  this.setWriteQueueMaxSize = function(maxSize) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_netSocket["setWriteQueueMaxSize(int)"](maxSize);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {NetSocket}
   */
  this.drainHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_netSocket["drainHandler(io.vertx.core.Handler)"](handler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   When a <code>NetSocket</code> is created it automatically registers an event handler with the event bus, the ID of that
   handler is given by <code>writeHandlerID</code>.
   <p>
   Given this ID, a different event loop can send a buffer to that event handler using the event bus and
   that buffer will be received by this instance in its own event loop and written to the underlying connection. This
   allows you to write data to other connections which are owned by different event loops.

   @public

   @return {string} the write handler ID
   */
  this.writeHandlerID = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_netSocket["writeHandlerID()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Same as {@link NetSocket#sendFile} but also takes a handler that will be called when the send has completed or
   a failure has occurred

   @public
   @param filename {string} file name of the file to send 
   @param offset {number} offset 
   @param length {number} length 
   @param resultHandler {function} handler 
   @return {NetSocket} a reference to this, so the API can be used fluently
   */
  this.sendFile = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_netSocket["sendFile(java.lang.String)"](__args[0]);
      return that;
    }  else if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] ==='number') {
      j_netSocket["sendFile(java.lang.String,long)"](__args[0], __args[1]);
      return that;
    }  else if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_netSocket["sendFile(java.lang.String,io.vertx.core.Handler)"](__args[0], function(ar) {
      if (ar.succeeded()) {
        __args[1](null, null);
      } else {
        __args[1](null, ar.cause());
      }
    });
      return that;
    }  else if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] ==='number' && typeof __args[2] ==='number') {
      j_netSocket["sendFile(java.lang.String,long,long)"](__args[0], __args[1], __args[2]);
      return that;
    }  else if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] ==='number' && typeof __args[2] === 'function') {
      j_netSocket["sendFile(java.lang.String,long,io.vertx.core.Handler)"](__args[0], __args[1], function(ar) {
      if (ar.succeeded()) {
        __args[2](null, null);
      } else {
        __args[2](null, ar.cause());
      }
    });
      return that;
    }  else if (__args.length === 4 && typeof __args[0] === 'string' && typeof __args[1] ==='number' && typeof __args[2] ==='number' && typeof __args[3] === 'function') {
      j_netSocket["sendFile(java.lang.String,long,long,io.vertx.core.Handler)"](__args[0], __args[1], __args[2], function(ar) {
      if (ar.succeeded()) {
        __args[3](null, null);
      } else {
        __args[3](null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the remote address for this socket

   @public

   @return {SocketAddress}
   */
  this.remoteAddress = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedremoteAddress == null) {
        that.cachedremoteAddress = utils.convReturnVertxGen(j_netSocket["remoteAddress()"](), SocketAddress);
      }
      return that.cachedremoteAddress;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the local address for this socket

   @public

   @return {SocketAddress}
   */
  this.localAddress = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedlocalAddress == null) {
        that.cachedlocalAddress = utils.convReturnVertxGen(j_netSocket["localAddress()"](), SocketAddress);
      }
      return that.cachedlocalAddress;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Close the NetSocket

   @public

   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_netSocket["close()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a handler that will be called when the NetSocket is closed

   @public
   @param handler {function} the handler 
   @return {NetSocket} a reference to this, so the API can be used fluently
   */
  this.closeHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_netSocket["closeHandler(io.vertx.core.Handler)"](handler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Upgrade channel to use SSL/TLS. Be aware that for this to work SSL must be configured.

   @public
   @param handler {function} the handler will be notified when it's upgraded 
   @return {NetSocket} a reference to this, so the API can be used fluently
   */
  this.upgradeToSsl = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_netSocket["upgradeToSsl(io.vertx.core.Handler)"](handler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return true if this {@link NetSocket} is encrypted via SSL/TLS.

   @public

   @return {boolean}
   */
  this.isSsl = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_netSocket["isSsl()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_netSocket;
};

// We export the Constructor function
module.exports = NetSocket;