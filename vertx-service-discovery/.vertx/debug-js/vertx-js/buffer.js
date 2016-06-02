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

/** @module vertx-js/buffer */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JBuffer = io.vertx.core.buffer.Buffer;

/**
 Most data is shuffled around inside Vert.x using buffers.
 <p>
 A buffer is a sequence of zero or more bytes that can read from or written to and which expands automatically as
 necessary to accommodate any bytes written to it. You can perhaps think of a buffer as smart byte array.
 <p>
 Please consult the documentation for more information on buffers.

 @class
*/
var Buffer = function(j_val) {

  var j_buffer = j_val;
  var that = this;

  /**
   Returns a <code>String</code> representation of the Buffer with the encoding specified by <code>enc</code>

   @public
   @param enc {string} 
   @return {string}
   */
  this.toString = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_buffer["toString()"]();
    }  else if (__args.length === 1 && typeof __args[0] === 'string') {
      return j_buffer["toString(java.lang.String)"](__args[0]);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns a Json object representation of the Buffer

   @public

   @return {Object}
   */
  this.toJsonObject = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnJson(j_buffer["toJsonObject()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns a Json array representation of the Buffer

   @public

   @return {todo}
   */
  this.toJsonArray = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnJson(j_buffer["toJsonArray()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns the <code>byte</code> at position <code>pos</code> in the Buffer.

   @public
   @param pos {number} 
   @return {number}
   */
  this.getByte = function(pos) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return j_buffer["getByte(int)"](pos);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns the unsigned <code>byte</code> at position <code>pos</code> in the Buffer, as a <code>short</code>.

   @public
   @param pos {number} 
   @return {number}
   */
  this.getUnsignedByte = function(pos) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return j_buffer["getUnsignedByte(int)"](pos);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns the <code>int</code> at position <code>pos</code> in the Buffer.

   @public
   @param pos {number} 
   @return {number}
   */
  this.getInt = function(pos) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return j_buffer["getInt(int)"](pos);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Gets a 32-bit integer at the specified absolute <code>index</code> in this buffer with Little Endian Byte Order.

   @public
   @param pos {number} 
   @return {number}
   */
  this.getIntLE = function(pos) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return j_buffer["getIntLE(int)"](pos);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns the unsigned <code>int</code> at position <code>pos</code> in the Buffer, as a <code>long</code>.

   @public
   @param pos {number} 
   @return {number}
   */
  this.getUnsignedInt = function(pos) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return j_buffer["getUnsignedInt(int)"](pos);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns the unsigned <code>int</code> at position <code>pos</code> in the Buffer, as a <code>long</code> in Little Endian Byte Order.

   @public
   @param pos {number} 
   @return {number}
   */
  this.getUnsignedIntLE = function(pos) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return j_buffer["getUnsignedIntLE(int)"](pos);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns the <code>long</code> at position <code>pos</code> in the Buffer.

   @public
   @param pos {number} 
   @return {number}
   */
  this.getLong = function(pos) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return j_buffer["getLong(int)"](pos);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Gets a 64-bit long integer at the specified absolute <code>index</code> in this buffer in Little Endian Byte Order.

   @public
   @param pos {number} 
   @return {number}
   */
  this.getLongLE = function(pos) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return j_buffer["getLongLE(int)"](pos);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns the <code>double</code> at position <code>pos</code> in the Buffer.

   @public
   @param pos {number} 
   @return {number}
   */
  this.getDouble = function(pos) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return j_buffer["getDouble(int)"](pos);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns the <code>float</code> at position <code>pos</code> in the Buffer.

   @public
   @param pos {number} 
   @return {number}
   */
  this.getFloat = function(pos) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return j_buffer["getFloat(int)"](pos);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns the <code>short</code> at position <code>pos</code> in the Buffer.

   @public
   @param pos {number} 
   @return {number}
   */
  this.getShort = function(pos) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return j_buffer["getShort(int)"](pos);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Gets a 16-bit short integer at the specified absolute <code>index</code> in this buffer in Little Endian Byte Order.

   @public
   @param pos {number} 
   @return {number}
   */
  this.getShortLE = function(pos) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return j_buffer["getShortLE(int)"](pos);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns the unsigned <code>short</code> at position <code>pos</code> in the Buffer, as an <code>int</code>.

   @public
   @param pos {number} 
   @return {number}
   */
  this.getUnsignedShort = function(pos) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return j_buffer["getUnsignedShort(int)"](pos);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Gets an unsigned 16-bit short integer at the specified absolute <code>index</code> in this buffer in Little Endian Byte Order.

   @public
   @param pos {number} 
   @return {number}
   */
  this.getUnsignedShortLE = function(pos) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return j_buffer["getUnsignedShortLE(int)"](pos);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Gets a 24-bit medium integer at the specified absolute <code>index</code> in this buffer.

   @public
   @param pos {number} 
   @return {number}
   */
  this.getMedium = function(pos) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return j_buffer["getMedium(int)"](pos);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Gets a 24-bit medium integer at the specified absolute <code>index</code> in this buffer in the Little Endian Byte Order.

   @public
   @param pos {number} 
   @return {number}
   */
  this.getMediumLE = function(pos) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return j_buffer["getMediumLE(int)"](pos);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Gets an unsigned 24-bit medium integer at the specified absolute <code>index</code> in this buffer.

   @public
   @param pos {number} 
   @return {number}
   */
  this.getUnsignedMedium = function(pos) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return j_buffer["getUnsignedMedium(int)"](pos);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Gets an unsigned 24-bit medium integer at the specified absolute <code>index</code> in this buffer in Little Endian Byte Order.

   @public
   @param pos {number} 
   @return {number}
   */
  this.getUnsignedMediumLE = function(pos) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return j_buffer["getUnsignedMediumLE(int)"](pos);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns a copy of a sub-sequence the Buffer as a {@link Buffer} starting at position <code>start</code>
   and ending at position <code>end - 1</code>

   @public
   @param start {number} 
   @param end {number} 
   @return {Buffer}
   */
  this.getBuffer = function(start, end) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] ==='number') {
      return utils.convReturnVertxGen(j_buffer["getBuffer(int,int)"](start, end), Buffer);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns a copy of a sub-sequence the Buffer as a <code>String</code> starting at position <code>start</code>
   and ending at position <code>end - 1</code> interpreted as a String in the specified encoding

   @public
   @param start {number} 
   @param end {number} 
   @param enc {string} 
   @return {string}
   */
  this.getString = function() {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] ==='number') {
      return j_buffer["getString(int,int)"](__args[0], __args[1]);
    }  else if (__args.length === 3 && typeof __args[0] ==='number' && typeof __args[1] ==='number' && typeof __args[2] === 'string') {
      return j_buffer["getString(int,int,java.lang.String)"](__args[0], __args[1], __args[2]);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Appends the specified <code>Buffer</code> starting at the <code>offset</code> using <code>len</code> to the end of this Buffer. The buffer will expand as necessary to accommodate
   any bytes written.<p>
   Returns a reference to <code>this</code> so multiple operations can be appended together.

   @public
   @param buff {Buffer} 
   @param offset {number} 
   @param len {number} 
   @return {Buffer}
   */
  this.appendBuffer = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_buffer["appendBuffer(io.vertx.core.buffer.Buffer)"](__args[0]._jdel);
      return that;
    }  else if (__args.length === 3 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] ==='number' && typeof __args[2] ==='number') {
      j_buffer["appendBuffer(io.vertx.core.buffer.Buffer,int,int)"](__args[0]._jdel, __args[1], __args[2]);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Appends the specified <code>byte</code> to the end of the Buffer. The buffer will expand as necessary to accommodate any bytes written.<p>
   Returns a reference to <code>this</code> so multiple operations can be appended together.

   @public
   @param b {number} 
   @return {Buffer}
   */
  this.appendByte = function(b) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_buffer["appendByte(byte)"](b);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Appends the specified unsigned <code>byte</code> to the end of the Buffer. The buffer will expand as necessary to accommodate any bytes written.<p>
   Returns a reference to <code>this</code> so multiple operations can be appended together.

   @public
   @param b {number} 
   @return {Buffer}
   */
  this.appendUnsignedByte = function(b) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_buffer["appendUnsignedByte(short)"](b);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Appends the specified <code>int</code> to the end of the Buffer. The buffer will expand as necessary to accommodate any bytes written.<p>
   Returns a reference to <code>this</code> so multiple operations can be appended together.

   @public
   @param i {number} 
   @return {Buffer}
   */
  this.appendInt = function(i) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_buffer["appendInt(int)"](i);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Appends the specified <code>int</code> to the end of the Buffer in the Little Endian Byte Order. The buffer will expand as necessary to accommodate any bytes written.<p>
   Returns a reference to <code>this</code> so multiple operations can be appended together.

   @public
   @param i {number} 
   @return {Buffer}
   */
  this.appendIntLE = function(i) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_buffer["appendIntLE(int)"](i);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Appends the specified unsigned <code>int</code> to the end of the Buffer. The buffer will expand as necessary to accommodate any bytes written.<p>
   Returns a reference to <code>this</code> so multiple operations can be appended together.

   @public
   @param i {number} 
   @return {Buffer}
   */
  this.appendUnsignedInt = function(i) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_buffer["appendUnsignedInt(long)"](i);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Appends the specified unsigned <code>int</code> to the end of the Buffer in the Little Endian Byte Order. The buffer will expand as necessary to accommodate any bytes written.<p>
   Returns a reference to <code>this</code> so multiple operations can be appended together.

   @public
   @param i {number} 
   @return {Buffer}
   */
  this.appendUnsignedIntLE = function(i) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_buffer["appendUnsignedIntLE(long)"](i);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Appends the specified 24bit <code>int</code> to the end of the Buffer. The buffer will expand as necessary to accommodate any bytes written.<p>
   Returns a reference to <code>this</code> so multiple operations can be appended together.

   @public
   @param i {number} 
   @return {Buffer}
   */
  this.appendMedium = function(i) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_buffer["appendMedium(int)"](i);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Appends the specified 24bit <code>int</code> to the end of the Buffer in the Little Endian Byte Order. The buffer will expand as necessary to accommodate any bytes written.<p>
   Returns a reference to <code>this</code> so multiple operations can be appended together.

   @public
   @param i {number} 
   @return {Buffer}
   */
  this.appendMediumLE = function(i) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_buffer["appendMediumLE(int)"](i);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Appends the specified <code>long</code> to the end of the Buffer. The buffer will expand as necessary to accommodate any bytes written.<p>
   Returns a reference to <code>this</code> so multiple operations can be appended together.

   @public
   @param l {number} 
   @return {Buffer}
   */
  this.appendLong = function(l) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_buffer["appendLong(long)"](l);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Appends the specified <code>long</code> to the end of the Buffer in the Little Endian Byte Order. The buffer will expand as necessary to accommodate any bytes written.<p>
   Returns a reference to <code>this</code> so multiple operations can be appended together.

   @public
   @param l {number} 
   @return {Buffer}
   */
  this.appendLongLE = function(l) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_buffer["appendLongLE(long)"](l);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Appends the specified <code>short</code> to the end of the Buffer.The buffer will expand as necessary to accommodate any bytes written.<p>
   Returns a reference to <code>this</code> so multiple operations can be appended together.

   @public
   @param s {number} 
   @return {Buffer}
   */
  this.appendShort = function(s) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_buffer["appendShort(short)"](s);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Appends the specified <code>short</code> to the end of the Buffer in the Little Endian Byte Order.The buffer will expand as necessary to accommodate any bytes written.<p>
   Returns a reference to <code>this</code> so multiple operations can be appended together.

   @public
   @param s {number} 
   @return {Buffer}
   */
  this.appendShortLE = function(s) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_buffer["appendShortLE(short)"](s);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Appends the specified unsigned <code>short</code> to the end of the Buffer.The buffer will expand as necessary to accommodate any bytes written.<p>
   Returns a reference to <code>this</code> so multiple operations can be appended together.

   @public
   @param s {number} 
   @return {Buffer}
   */
  this.appendUnsignedShort = function(s) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_buffer["appendUnsignedShort(int)"](s);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Appends the specified unsigned <code>short</code> to the end of the Buffer in the Little Endian Byte Order.The buffer will expand as necessary to accommodate any bytes written.<p>
   Returns a reference to <code>this</code> so multiple operations can be appended together.

   @public
   @param s {number} 
   @return {Buffer}
   */
  this.appendUnsignedShortLE = function(s) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_buffer["appendUnsignedShortLE(int)"](s);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Appends the specified <code>float</code> to the end of the Buffer. The buffer will expand as necessary to accommodate any bytes written.<p>
   Returns a reference to <code>this</code> so multiple operations can be appended together.

   @public
   @param f {number} 
   @return {Buffer}
   */
  this.appendFloat = function(f) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_buffer["appendFloat(float)"](f);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Appends the specified <code>double</code> to the end of the Buffer. The buffer will expand as necessary to accommodate any bytes written.<p>
   Returns a reference to <code>this</code> so multiple operations can be appended together.

   @public
   @param d {number} 
   @return {Buffer}
   */
  this.appendDouble = function(d) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_buffer["appendDouble(double)"](d);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Appends the specified <code>String</code> to the end of the Buffer with the encoding as specified by <code>enc</code>.<p>
   The buffer will expand as necessary to accommodate any bytes written.<p>
   Returns a reference to <code>this</code> so multiple operations can be appended together.<p>

   @public
   @param str {string} 
   @param enc {string} 
   @return {Buffer}
   */
  this.appendString = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_buffer["appendString(java.lang.String)"](__args[0]);
      return that;
    }  else if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'string') {
      j_buffer["appendString(java.lang.String,java.lang.String)"](__args[0], __args[1]);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets the <code>byte</code> at position <code>pos</code> in the Buffer to the value <code>b</code>.<p>
   The buffer will expand as necessary to accommodate any value written.

   @public
   @param pos {number} 
   @param b {number} 
   @return {Buffer}
   */
  this.setByte = function(pos, b) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] ==='number') {
      j_buffer["setByte(int,byte)"](pos, b);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets the unsigned <code>byte</code> at position <code>pos</code> in the Buffer to the value <code>b</code>.<p>
   The buffer will expand as necessary to accommodate any value written.

   @public
   @param pos {number} 
   @param b {number} 
   @return {Buffer}
   */
  this.setUnsignedByte = function(pos, b) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] ==='number') {
      j_buffer["setUnsignedByte(int,short)"](pos, b);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets the <code>int</code> at position <code>pos</code> in the Buffer to the value <code>i</code>.<p>
   The buffer will expand as necessary to accommodate any value written.

   @public
   @param pos {number} 
   @param i {number} 
   @return {Buffer}
   */
  this.setInt = function(pos, i) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] ==='number') {
      j_buffer["setInt(int,int)"](pos, i);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets the <code>int</code> at position <code>pos</code> in the Buffer to the value <code>i</code> in the Little Endian Byte Order.<p>
   The buffer will expand as necessary to accommodate any value written.

   @public
   @param pos {number} 
   @param i {number} 
   @return {Buffer}
   */
  this.setIntLE = function(pos, i) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] ==='number') {
      j_buffer["setIntLE(int,int)"](pos, i);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets the unsigned <code>int</code> at position <code>pos</code> in the Buffer to the value <code>i</code>.<p>
   The buffer will expand as necessary to accommodate any value written.

   @public
   @param pos {number} 
   @param i {number} 
   @return {Buffer}
   */
  this.setUnsignedInt = function(pos, i) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] ==='number') {
      j_buffer["setUnsignedInt(int,long)"](pos, i);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets the unsigned <code>int</code> at position <code>pos</code> in the Buffer to the value <code>i</code> in the Little Endian Byte Order.<p>
   The buffer will expand as necessary to accommodate any value written.

   @public
   @param pos {number} 
   @param i {number} 
   @return {Buffer}
   */
  this.setUnsignedIntLE = function(pos, i) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] ==='number') {
      j_buffer["setUnsignedIntLE(int,long)"](pos, i);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets the 24bit <code>int</code> at position <code>pos</code> in the Buffer to the value <code>i</code>.<p>
   The buffer will expand as necessary to accommodate any value written.

   @public
   @param pos {number} 
   @param i {number} 
   @return {Buffer}
   */
  this.setMedium = function(pos, i) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] ==='number') {
      j_buffer["setMedium(int,int)"](pos, i);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets the 24bit <code>int</code> at position <code>pos</code> in the Buffer to the value <code>i</code>. in the Little Endian Byte Order<p>
   The buffer will expand as necessary to accommodate any value written.

   @public
   @param pos {number} 
   @param i {number} 
   @return {Buffer}
   */
  this.setMediumLE = function(pos, i) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] ==='number') {
      j_buffer["setMediumLE(int,int)"](pos, i);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets the <code>long</code> at position <code>pos</code> in the Buffer to the value <code>l</code>.<p>
   The buffer will expand as necessary to accommodate any value written.

   @public
   @param pos {number} 
   @param l {number} 
   @return {Buffer}
   */
  this.setLong = function(pos, l) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] ==='number') {
      j_buffer["setLong(int,long)"](pos, l);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets the <code>long</code> at position <code>pos</code> in the Buffer to the value <code>l</code> in the Little Endian Byte Order.<p>
   The buffer will expand as necessary to accommodate any value written.

   @public
   @param pos {number} 
   @param l {number} 
   @return {Buffer}
   */
  this.setLongLE = function(pos, l) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] ==='number') {
      j_buffer["setLongLE(int,long)"](pos, l);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets the <code>double</code> at position <code>pos</code> in the Buffer to the value <code>d</code>.<p>
   The buffer will expand as necessary to accommodate any value written.

   @public
   @param pos {number} 
   @param d {number} 
   @return {Buffer}
   */
  this.setDouble = function(pos, d) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] ==='number') {
      j_buffer["setDouble(int,double)"](pos, d);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets the <code>float</code> at position <code>pos</code> in the Buffer to the value <code>f</code>.<p>
   The buffer will expand as necessary to accommodate any value written.

   @public
   @param pos {number} 
   @param f {number} 
   @return {Buffer}
   */
  this.setFloat = function(pos, f) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] ==='number') {
      j_buffer["setFloat(int,float)"](pos, f);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets the <code>short</code> at position <code>pos</code> in the Buffer to the value <code>s</code>.<p>
   The buffer will expand as necessary to accommodate any value written.

   @public
   @param pos {number} 
   @param s {number} 
   @return {Buffer}
   */
  this.setShort = function(pos, s) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] ==='number') {
      j_buffer["setShort(int,short)"](pos, s);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets the <code>short</code> at position <code>pos</code> in the Buffer to the value <code>s</code> in the Little Endian Byte Order.<p>
   The buffer will expand as necessary to accommodate any value written.

   @public
   @param pos {number} 
   @param s {number} 
   @return {Buffer}
   */
  this.setShortLE = function(pos, s) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] ==='number') {
      j_buffer["setShortLE(int,short)"](pos, s);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets the unsigned <code>short</code> at position <code>pos</code> in the Buffer to the value <code>s</code>.<p>
   The buffer will expand as necessary to accommodate any value written.

   @public
   @param pos {number} 
   @param s {number} 
   @return {Buffer}
   */
  this.setUnsignedShort = function(pos, s) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] ==='number') {
      j_buffer["setUnsignedShort(int,int)"](pos, s);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets the unsigned <code>short</code> at position <code>pos</code> in the Buffer to the value <code>s</code> in the Little Endian Byte Order.<p>
   The buffer will expand as necessary to accommodate any value written.

   @public
   @param pos {number} 
   @param s {number} 
   @return {Buffer}
   */
  this.setUnsignedShortLE = function(pos, s) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] ==='number') {
      j_buffer["setUnsignedShortLE(int,int)"](pos, s);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets the bytes at position <code>pos</code> in the Buffer to the bytes represented by the <code>Buffer b</code> on the given <code>offset</code> and <code>len</code>.<p>
   The buffer will expand as necessary to accommodate any value written.

   @public
   @param pos {number} 
   @param b {Buffer} 
   @param offset {number} 
   @param len {number} 
   @return {Buffer}
   */
  this.setBuffer = function() {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] === 'object' && __args[1]._jdel) {
      j_buffer["setBuffer(int,io.vertx.core.buffer.Buffer)"](__args[0], __args[1]._jdel);
      return that;
    }  else if (__args.length === 4 && typeof __args[0] ==='number' && typeof __args[1] === 'object' && __args[1]._jdel && typeof __args[2] ==='number' && typeof __args[3] ==='number') {
      j_buffer["setBuffer(int,io.vertx.core.buffer.Buffer,int,int)"](__args[0], __args[1]._jdel, __args[2], __args[3]);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets the bytes at position <code>pos</code> in the Buffer to the value of <code>str</code> encoded in encoding <code>enc</code>.<p>
   The buffer will expand as necessary to accommodate any value written.

   @public
   @param pos {number} 
   @param str {string} 
   @param enc {string} 
   @return {Buffer}
   */
  this.setString = function() {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] === 'string') {
      j_buffer["setString(int,java.lang.String)"](__args[0], __args[1]);
      return that;
    }  else if (__args.length === 3 && typeof __args[0] ==='number' && typeof __args[1] === 'string' && typeof __args[2] === 'string') {
      j_buffer["setString(int,java.lang.String,java.lang.String)"](__args[0], __args[1], __args[2]);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns the length of the buffer, measured in bytes.
   All positions are indexed from zero.

   @public

   @return {number}
   */
  this.length = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_buffer["length()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns a copy of the entire Buffer.

   @public

   @return {Buffer}
   */
  this.copy = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_buffer["copy()"](), Buffer);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns a slice of this buffer. Modifying the content
   of the returned buffer or this buffer affects each other's content
   while they maintain separate indexes and marks.

   @public
   @param start {number} 
   @param end {number} 
   @return {Buffer}
   */
  this.slice = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_buffer["slice()"](), Buffer);
    }  else if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] ==='number') {
      return utils.convReturnVertxGen(j_buffer["slice(int,int)"](__args[0], __args[1]), Buffer);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_buffer;
};

/**
 Create a new buffer from a string and using the specified encoding.
 The string will be encoded into the buffer using the specified encoding.

 @memberof module:vertx-js/buffer
 @param string {string} the string 
 @param enc {string} 
 @return {Buffer} the buffer
 */
Buffer.buffer = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return utils.convReturnVertxGen(JBuffer["buffer()"](), Buffer);
  }else if (__args.length === 1 && typeof __args[0] ==='number') {
    return utils.convReturnVertxGen(JBuffer["buffer(int)"](__args[0]), Buffer);
  }else if (__args.length === 1 && typeof __args[0] === 'string') {
    return utils.convReturnVertxGen(JBuffer["buffer(java.lang.String)"](__args[0]), Buffer);
  }else if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'string') {
    return utils.convReturnVertxGen(JBuffer["buffer(java.lang.String,java.lang.String)"](__args[0], __args[1]), Buffer);
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = Buffer;