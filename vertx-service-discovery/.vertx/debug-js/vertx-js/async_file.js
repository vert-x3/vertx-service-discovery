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

/** @module vertx-js/async_file */
var utils = require('vertx-js/util/utils');
var Buffer = require('vertx-js/buffer');
var WriteStream = require('vertx-js/write_stream');
var ReadStream = require('vertx-js/read_stream');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JAsyncFile = io.vertx.core.file.AsyncFile;

/**
 Represents a file on the file-system which can be read from, or written to asynchronously.
 <p>
 @class
*/
var AsyncFile = function(j_val) {

  var j_asyncFile = j_val;
  var that = this;
  ReadStream.call(this, j_val);
  WriteStream.call(this, j_val);

  /**
   Same as {@link AsyncFile#end} but writes some data to the stream before ending.

   @public
   @param t {Buffer} 
   */
  this.end = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_asyncFile["end()"]();
    }  else if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_asyncFile["end(io.vertx.core.buffer.Buffer)"](__args[0]._jdel);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   This will return <code>true</code> if there are more bytes in the write queue than the value set using {@link AsyncFile#setWriteQueueMaxSize}

   @public

   @return {boolean} true if write queue is full
   */
  this.writeQueueFull = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_asyncFile["writeQueueFull()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {AsyncFile}
   */
  this.handler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_asyncFile["handler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnVertxGen(jVal, Buffer));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {AsyncFile}
   */
  this.pause = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_asyncFile["pause()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {AsyncFile}
   */
  this.resume = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_asyncFile["resume()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param endHandler {function} 
   @return {AsyncFile}
   */
  this.endHandler = function(endHandler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_asyncFile["endHandler(io.vertx.core.Handler)"](endHandler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Write a {@link Buffer} to the file at position <code>position</code> in the file, asynchronously.
   <p>
   If <code>position</code> lies outside of the current size
   of the file, the file will be enlarged to encompass it.
   <p>
   When multiple writes are invoked on the same file
   there are no guarantees as to order in which those writes actually occur
   <p>
   The handler will be called when the write is complete, or if an error occurs.

   @public
   @param buffer {Buffer} the buffer to write 
   @param position {number} the position in the file to write it at 
   @param handler {function} the handler to call when the write is complete 
   @return {AsyncFile} a reference to this, so the API can be used fluently
   */
  this.write = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_asyncFile["write(io.vertx.core.buffer.Buffer)"](__args[0]._jdel);
      return that;
    }  else if (__args.length === 3 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] ==='number' && typeof __args[2] === 'function') {
      j_asyncFile["write(io.vertx.core.buffer.Buffer,long,io.vertx.core.Handler)"](__args[0]._jdel, __args[1], function(ar) {
      if (ar.succeeded()) {
        __args[2](null, null);
      } else {
        __args[2](null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param maxSize {number} 
   @return {AsyncFile}
   */
  this.setWriteQueueMaxSize = function(maxSize) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_asyncFile["setWriteQueueMaxSize(int)"](maxSize);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {AsyncFile}
   */
  this.drainHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_asyncFile["drainHandler(io.vertx.core.Handler)"](handler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {AsyncFile}
   */
  this.exceptionHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_asyncFile["exceptionHandler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnThrowable(jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Close the file. The actual close happens asynchronously.
   The handler will be called when the close is complete, or an error occurs.

   @public
   @param handler {function} the handler 
   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_asyncFile["close()"]();
    }  else if (__args.length === 1 && typeof __args[0] === 'function') {
      j_asyncFile["close(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        __args[0](null, null);
      } else {
        __args[0](null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Reads <code>length</code> bytes of data from the file at position <code>position</code> in the file, asynchronously.
   <p>
   The read data will be written into the specified <code>Buffer buffer</code> at position <code>offset</code>.
   <p>
   If data is read past the end of the file then zero bytes will be read.<p>
   When multiple reads are invoked on the same file there are no guarantees as to order in which those reads actually occur.
   <p>
   The handler will be called when the close is complete, or if an error occurs.

   @public
   @param buffer {Buffer} the buffer to read into 
   @param offset {number} the offset into the buffer where the data will be read 
   @param position {number} the position in the file where to start reading 
   @param length {number} the number of bytes to read 
   @param handler {function} the handler to call when the write is complete 
   @return {AsyncFile} a reference to this, so the API can be used fluently
   */
  this.read = function(buffer, offset, position, length, handler) {
    var __args = arguments;
    if (__args.length === 5 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] ==='number' && typeof __args[2] ==='number' && typeof __args[3] ==='number' && typeof __args[4] === 'function') {
      j_asyncFile["read(io.vertx.core.buffer.Buffer,int,long,int,io.vertx.core.Handler)"](buffer._jdel, offset, position, length, function(ar) {
      if (ar.succeeded()) {
        handler(utils.convReturnVertxGen(ar.result(), Buffer), null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Same as {@link AsyncFile#flush} but the handler will be called when the flush is complete or if an error occurs

   @public
   @param handler {function} 
   @return {AsyncFile}
   */
  this.flush = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_asyncFile["flush()"]();
      return that;
    }  else if (__args.length === 1 && typeof __args[0] === 'function') {
      j_asyncFile["flush(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        __args[0](null, null);
      } else {
        __args[0](null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets the position from which data will be read from when using the file as a {@link ReadStream}.

   @public
   @param readPos {number} the position in the file 
   @return {AsyncFile} a reference to this, so the API can be used fluently
   */
  this.setReadPos = function(readPos) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_asyncFile["setReadPos(long)"](readPos);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets the position from which data will be written when using the file as a {@link WriteStream}.

   @public
   @param writePos {number} the position in the file 
   @return {AsyncFile} a reference to this, so the API can be used fluently
   */
  this.setWritePos = function(writePos) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_asyncFile["setWritePos(long)"](writePos);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets the buffer size that will be used to read the data from the file. Changing this value will impact how much
   the data will be read at a time from the file system.

   @public
   @param readBufferSize {number} the buffer size 
   @return {AsyncFile} a reference to this, so the API can be used fluently
   */
  this.setReadBufferSize = function(readBufferSize) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_asyncFile["setReadBufferSize(int)"](readBufferSize);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_asyncFile;
};

// We export the Constructor function
module.exports = AsyncFile;