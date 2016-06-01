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

/** @module vertx-js/http_server_file_upload */
var utils = require('vertx-js/util/utils');
var Buffer = require('vertx-js/buffer');
var ReadStream = require('vertx-js/read_stream');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JHttpServerFileUpload = io.vertx.core.http.HttpServerFileUpload;

/**
 Represents an file upload from an HTML FORM.

 @class
*/
var HttpServerFileUpload = function(j_val) {

  var j_httpServerFileUpload = j_val;
  var that = this;
  ReadStream.call(this, j_val);

  /**

   @public
   @param handler {function} 
   @return {HttpServerFileUpload}
   */
  this.exceptionHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_httpServerFileUpload["exceptionHandler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnThrowable(jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {HttpServerFileUpload}
   */
  this.handler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_httpServerFileUpload["handler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnVertxGen(jVal, Buffer));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param endHandler {function} 
   @return {HttpServerFileUpload}
   */
  this.endHandler = function(endHandler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_httpServerFileUpload["endHandler(io.vertx.core.Handler)"](endHandler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {HttpServerFileUpload}
   */
  this.pause = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_httpServerFileUpload["pause()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {HttpServerFileUpload}
   */
  this.resume = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_httpServerFileUpload["resume()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Stream the content of this upload to the given file on storage.

   @public
   @param filename {string} the name of the file 
   @return {HttpServerFileUpload}
   */
  this.streamToFileSystem = function(filename) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_httpServerFileUpload["streamToFileSystem(java.lang.String)"](filename);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the filename which was used when upload the file.

   @public

   @return {string}
   */
  this.filename = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_httpServerFileUpload["filename()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the name of the attribute

   @public

   @return {string}
   */
  this.name = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_httpServerFileUpload["name()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return  the content type for the upload

   @public

   @return {string}
   */
  this.contentType = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_httpServerFileUpload["contentType()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the contentTransferEncoding for the upload

   @public

   @return {string}
   */
  this.contentTransferEncoding = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_httpServerFileUpload["contentTransferEncoding()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the charset for the upload

   @public

   @return {string}
   */
  this.charset = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_httpServerFileUpload["charset()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   The size of the upload may not be available until it is all read.
   Check {@link HttpServerFileUpload#isSizeAvailable} to determine this

   @public

   @return {number} the size of the upload (in bytes)
   */
  this.size = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_httpServerFileUpload["size()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return true if the size of the upload can be retrieved via {@link HttpServerFileUpload#size}.

   @public

   @return {boolean}
   */
  this.isSizeAvailable = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_httpServerFileUpload["isSizeAvailable()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_httpServerFileUpload;
};

// We export the Constructor function
module.exports = HttpServerFileUpload;