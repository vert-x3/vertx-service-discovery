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

/** @module vertx-circuit-breaker-js/circuit_breaker */
var utils = require('vertx-js/util/utils');
var Vertx = require('vertx-js/vertx');
var Future = require('vertx-js/future');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JCircuitBreaker = io.vertx.ext.circuitbreaker.CircuitBreaker;
var CircuitBreakerOptions = io.vertx.ext.circuitbreaker.CircuitBreakerOptions;

/**
 An implementation of the circuit breaker pattern for Vert.x

 @class
*/
var CircuitBreaker = function(j_val) {

  var j_circuitBreaker = j_val;
  var that = this;

  /**
   Sets a  invoked when the circuit breaker state switches to open.

   @public
   @param handler {function} the handler, must not be <code>null</code> 
   @return {CircuitBreaker} the current {@link CircuitBreaker}
   */
  this.openHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      return utils.convReturnVertxGen(j_circuitBreaker["openHandler(io.vertx.core.Handler)"](handler), CircuitBreaker);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets a  invoked when the circuit breaker state switches to half-open.

   @public
   @param handler {function} the handler, must not be <code>null</code> 
   @return {CircuitBreaker} the current {@link CircuitBreaker}
   */
  this.halfOpenHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      return utils.convReturnVertxGen(j_circuitBreaker["halfOpenHandler(io.vertx.core.Handler)"](handler), CircuitBreaker);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets a  invoked when the circuit breaker state switches to close.

   @public
   @param handler {function} the handler, must not be <code>null</code> 
   @return {CircuitBreaker} the current {@link CircuitBreaker}
   */
  this.closeHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      return utils.convReturnVertxGen(j_circuitBreaker["closeHandler(io.vertx.core.Handler)"](handler), CircuitBreaker);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets a  invoked when the bridge is open to handle the "request".

   @public
   @param handler {function} the handler, must not be <code>null</code> 
   @return {CircuitBreaker} the current {@link CircuitBreaker}
   */
  this.fallbackHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      return utils.convReturnVertxGen(j_circuitBreaker["fallbackHandler(io.vertx.core.Handler)"](handler), CircuitBreaker);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Resets the circuit breaker state (number of failure set to 0 and state set to closed).

   @public

   @return {CircuitBreaker} the current {@link CircuitBreaker}
   */
  this.reset = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_circuitBreaker["reset()"](), CircuitBreaker);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Explicitly opens the circuit.

   @public

   @return {CircuitBreaker} the current {@link CircuitBreaker}
   */
  this.open = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_circuitBreaker["open()"](), CircuitBreaker);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the current state.

   @public

   @return {Object}
   */
  this.state = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnEnum(j_circuitBreaker["state()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the current number of failures.

   @public

   @return {number}
   */
  this.failureCount = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_circuitBreaker["failureCount()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Executes the given code with the control of the circuit breaker.

   @public
   @param code {function} the code 
   @return {CircuitBreaker} the current {@link CircuitBreaker}
   */
  this.executeSynchronousBlock = function(code) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      return utils.convReturnVertxGen(j_circuitBreaker["executeSynchronousBlock(io.vertx.core.Handler)"](code), CircuitBreaker);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Executes the given code with the control of the circuit breaker and use the given fallback is the circuit is open.

   @public
   @param code {function} the code 
   @param fallback {function} 
   @return {CircuitBreaker} the current {@link CircuitBreaker}
   */
  this.executeSynchronousCodeWithFallback = function(code, fallback) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'function' && typeof __args[1] === 'function') {
      return utils.convReturnVertxGen(j_circuitBreaker["executeSynchronousCodeWithFallback(io.vertx.core.Handler,io.vertx.core.Handler)"](code, fallback), CircuitBreaker);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Executes the given code with the control of the circuit breaker. The code is asynchronous. Completion is
   detected using the given .

   @public
   @param code {function} the code 
   @return {CircuitBreaker} the current {@link CircuitBreaker}
   */
  this.executeAsynchronousCode = function(code) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      return utils.convReturnVertxGen(j_circuitBreaker["executeAsynchronousCode(io.vertx.core.Handler)"](function(jVal) {
      code(utils.convReturnVertxGen(jVal, Future));
    }), CircuitBreaker);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Executes the given code with the control of the circuit breaker. The code is asynchronous. Completion is
   detected using the given . If the circuit is open, this method executes the given fallback.

   @public
   @param code {function} the code 
   @param fallback {function} 
   @return {CircuitBreaker} the current {@link CircuitBreaker}
   */
  this.executeAsynchronousCodeWithFallback = function(code, fallback) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'function' && typeof __args[1] === 'function') {
      return utils.convReturnVertxGen(j_circuitBreaker["executeAsynchronousCodeWithFallback(io.vertx.core.Handler,io.vertx.core.Handler)"](function(jVal) {
      code(utils.convReturnVertxGen(jVal, Future));
    }, fallback), CircuitBreaker);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the name of the circuit breaker.

   @public

   @return {string}
   */
  this.name = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_circuitBreaker["name()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_circuitBreaker;
};

/**
 Creates a new instance of {@link CircuitBreaker}.

 @memberof module:vertx-circuit-breaker-js/circuit_breaker
 @param name {string} the name 
 @param vertx {Vertx} the Vert.x instance 
 @param options {Object} the configuration option 
 @return {CircuitBreaker} the created instance
 */
CircuitBreaker.create = function() {
  var __args = arguments;
  if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'object' && __args[1]._jdel) {
    return utils.convReturnVertxGen(JCircuitBreaker["create(java.lang.String,io.vertx.core.Vertx)"](__args[0], __args[1]._jdel), CircuitBreaker);
  }else if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'object' && __args[1]._jdel && (typeof __args[2] === 'object' && __args[2] != null)) {
    return utils.convReturnVertxGen(JCircuitBreaker["create(java.lang.String,io.vertx.core.Vertx,io.vertx.ext.circuitbreaker.CircuitBreakerOptions)"](__args[0], __args[1]._jdel, __args[2] != null ? new CircuitBreakerOptions(new JsonObject(JSON.stringify(__args[2]))) : null), CircuitBreaker);
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = CircuitBreaker;