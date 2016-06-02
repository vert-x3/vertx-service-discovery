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

/** @module vertx-js/http_client_request */
var utils = require('vertx-js/util/utils');
var HttpClientResponse = require('vertx-js/http_client_response');
var Buffer = require('vertx-js/buffer');
var HttpFrame = require('vertx-js/http_frame');
var WriteStream = require('vertx-js/write_stream');
var MultiMap = require('vertx-js/multi_map');
var ReadStream = require('vertx-js/read_stream');
var HttpConnection = require('vertx-js/http_connection');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JHttpClientRequest = io.vertx.core.http.HttpClientRequest;

/**
 Represents a client-side HTTP request.
 <p>
 @class
*/
var HttpClientRequest = function(j_val) {

  var j_httpClientRequest = j_val;
  var that = this;
  WriteStream.call(this, j_val);
  ReadStream.call(this, j_val);

  /**
   This will return <code>true</code> if there are more bytes in the write queue than the value set using {@link HttpClientRequest#setWriteQueueMaxSize}

   @public

   @return {boolean} true if write queue is full
   */
  this.writeQueueFull = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_httpClientRequest["writeQueueFull()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {HttpClientRequest}
   */
  this.exceptionHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_httpClientRequest["exceptionHandler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnThrowable(jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Write a String to the request body, encoded using the encoding <code>enc</code>.

   @public
   @param chunk {string} 
   @param enc {string} 
   @return {HttpClientRequest} @return a reference to this, so the API can be used fluently
   */
  this.write = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_httpClientRequest["write(io.vertx.core.buffer.Buffer)"](__args[0]._jdel);
      return that;
    }  else if (__args.length === 1 && typeof __args[0] === 'string') {
      j_httpClientRequest["write(java.lang.String)"](__args[0]);
      return that;
    }  else if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'string') {
      j_httpClientRequest["write(java.lang.String,java.lang.String)"](__args[0], __args[1]);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param maxSize {number} 
   @return {HttpClientRequest}
   */
  this.setWriteQueueMaxSize = function(maxSize) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_httpClientRequest["setWriteQueueMaxSize(int)"](maxSize);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {HttpClientRequest}
   */
  this.drainHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_httpClientRequest["drainHandler(io.vertx.core.Handler)"](handler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {HttpClientRequest}
   */
  this.handler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_httpClientRequest["handler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnVertxGen(jVal, HttpClientResponse));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {HttpClientRequest}
   */
  this.pause = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_httpClientRequest["pause()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {HttpClientRequest}
   */
  this.resume = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_httpClientRequest["resume()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param endHandler {function} 
   @return {HttpClientRequest}
   */
  this.endHandler = function(endHandler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_httpClientRequest["endHandler(io.vertx.core.Handler)"](endHandler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   If chunked is true then the request will be set into HTTP chunked mode

   @public
   @param chunked {boolean} true if chunked encoding 
   @return {HttpClientRequest} a reference to this, so the API can be used fluently
   */
  this.setChunked = function(chunked) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='boolean') {
      j_httpClientRequest["setChunked(boolean)"](chunked);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return Is the request chunked?

   @public

   @return {boolean}
   */
  this.isChunked = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_httpClientRequest["isChunked()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   The HTTP method for the request.

   @public

   @return {Object}
   */
  this.method = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnEnum(j_httpClientRequest["method()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the raw value of the method this request sends

   @public

   @return {string}
   */
  this.getRawMethod = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_httpClientRequest["getRawMethod()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set the value the method to send when the method  is used.

   @public
   @param method {string} the raw method 
   @return {HttpClientRequest} a reference to this, so the API can be used fluently
   */
  this.setRawMethod = function(method) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_httpClientRequest["setRawMethod(java.lang.String)"](method);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return The URI of the request.

   @public

   @return {string}
   */
  this.uri = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_httpClientRequest["uri()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return The path part of the uri. For example /somepath/somemorepath/someresource.foo

   @public

   @return {string}
   */
  this.path = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_httpClientRequest["path()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the query part of the uri. For example someparam=32&amp;someotherparam=x

   @public

   @return {string}
   */
  this.query = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_httpClientRequest["query()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set the request host.<p/>
  
   For HTTP2 it sets the  pseudo header otherwise it sets the  header

   @public
   @param host {string} 
   @return {HttpClientRequest}
   */
  this.setHost = function(host) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_httpClientRequest["setHost(java.lang.String)"](host);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the request host. For HTTP2 it returns the  pseudo header otherwise it returns the  header

   @public

   @return {string}
   */
  this.getHost = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_httpClientRequest["getHost()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return The HTTP headers

   @public

   @return {MultiMap}
   */
  this.headers = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedheaders == null) {
        that.cachedheaders = utils.convReturnVertxGen(j_httpClientRequest["headers()"](), MultiMap);
      }
      return that.cachedheaders;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Put an HTTP header

   @public
   @param name {string} The header name 
   @param value {string} The header value 
   @return {HttpClientRequest} a reference to this, so the API can be used fluently
   */
  this.putHeader = function(name, value) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'string') {
      j_httpClientRequest["putHeader(java.lang.String,java.lang.String)"](name, value);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   If you send an HTTP request with the header <code>Expect</code> set to the value <code>100-continue</code>
   and the server responds with an interim HTTP response with a status code of <code>100</code> and a continue handler
   has been set using this method, then the <code>handler</code> will be called.
   <p>
   You can then continue to write data to the request body and later end it. This is normally used in conjunction with
   the {@link HttpClientRequest#sendHead} method to force the request header to be written before the request has ended.

   @public
   @param handler {function} 
   @return {HttpClientRequest} a reference to this, so the API can be used fluently
   */
  this.continueHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_httpClientRequest["continueHandler(io.vertx.core.Handler)"](handler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Like {@link HttpClientRequest#sendHead} but with an handler after headers have been sent. The handler will be called with
   the <a href="../../enums.html#HttpVersion">HttpVersion</a> if it can be determined or null otherwise.<p>

   @public
   @param completionHandler {function} 
   @return {HttpClientRequest}
   */
  this.sendHead = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_httpClientRequest["sendHead()"]();
      return that;
    }  else if (__args.length === 1 && typeof __args[0] === 'function') {
      j_httpClientRequest["sendHead(io.vertx.core.Handler)"](function(jVal) {
      __args[0](utils.convReturnEnum(jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Same as {@link HttpClientRequest#end} but writes a String with the specified encoding

   @public
   @param chunk {string} 
   @param enc {string} 
   */
  this.end = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_httpClientRequest["end()"]();
    }  else if (__args.length === 1 && typeof __args[0] === 'string') {
      j_httpClientRequest["end(java.lang.String)"](__args[0]);
    }  else if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_httpClientRequest["end(io.vertx.core.buffer.Buffer)"](__args[0]._jdel);
    }  else if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'string') {
      j_httpClientRequest["end(java.lang.String,java.lang.String)"](__args[0], __args[1]);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set's the amount of time after which if the request does not return any data within the timeout period an
   TimeoutException will be passed to the exception handler (if provided) and
   the request will be closed.
   <p>
   Calling this method more than once has the effect of canceling any existing timeout and starting
   the timeout from scratch.

   @public
   @param timeoutMs {number} The quantity of time in milliseconds. 
   @return {HttpClientRequest} a reference to this, so the API can be used fluently
   */
  this.setTimeout = function(timeoutMs) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_httpClientRequest["setTimeout(long)"](timeoutMs);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a push handler for this request.<p/>
  
   The handler is called when the client receives a <i>push promise</i> from the server. The handler can be called
   multiple times, for each push promise.<p/>
  
   The handler is called with a <i>read-only</i> {@link HttpClientRequest}, the following methods can be called:<p/>
  
   <ul>
     <li>{@link HttpClientRequest#method}</li>
     <li>{@link HttpClientRequest#uri}</li>
     <li>{@link HttpClientRequest#headers}</li>
     <li>{@link HttpClientRequest#getHost}</li>
   </ul>
  
   In addition the handler should call the {@link HttpClientRequest#handler} method to set an handler to
   process the response.<p/>

   @public
   @param handler {function} the handler 
   @return {HttpClientRequest} a reference to this, so the API can be used fluently
   */
  this.pushHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_httpClientRequest["pushHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(jVal, HttpClientRequest));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Reset this stream with the error <code>code</code>.

   @public
   @param code {number} the error code 
   */
  this.reset = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_httpClientRequest["reset()"]();
    }  else if (__args.length === 1 && typeof __args[0] ==='number') {
      j_httpClientRequest["reset(long)"](__args[0]);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the {@link HttpConnection} associated with this request

   @public

   @return {HttpConnection}
   */
  this.connection = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedconnection == null) {
        that.cachedconnection = utils.convReturnVertxGen(j_httpClientRequest["connection()"](), HttpConnection);
      }
      return that.cachedconnection;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a connection handler called when an HTTP connection has been established.

   @public
   @param handler {function} the handler 
   @return {HttpClientRequest} a reference to this, so the API can be used fluently
   */
  this.connectionHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_httpClientRequest["connectionHandler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnVertxGen(jVal, HttpConnection));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Write an HTTP/2 frame to the request, allowing to extend the HTTP/2 protocol.<p>
  
   The frame is sent immediatly and is not subject to flow control.<p>
  
   This method must be called after the request headers have been sent and only for the protocol HTTP/2.
   The {@link HttpClientRequest#sendHead} should be used for this purpose.

   @public
   @param type {number} the 8-bit frame type 
   @param flags {number} the 8-bit frame flags 
   @param payload {Buffer} the frame payload 
   @return {HttpClientRequest} a reference to this, so the API can be used fluently
   */
  this.writeCustomFrame = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_httpClientRequest["writeCustomFrame(io.vertx.core.http.HttpFrame)"](__args[0]._jdel);
      return that;
    }  else if (__args.length === 3 && typeof __args[0] ==='number' && typeof __args[1] ==='number' && typeof __args[2] === 'object' && __args[2]._jdel) {
      j_httpClientRequest["writeCustomFrame(int,int,io.vertx.core.buffer.Buffer)"](__args[0], __args[1], __args[2]._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the id of the stream of this response,  when it is not yet determined, i.e
           the request has not been yet sent or it is not supported HTTP/1.x

   @public

   @return {number}
   */
  this.streamId = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_httpClientRequest["streamId()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_httpClientRequest;
};

// We export the Constructor function
module.exports = HttpClientRequest;