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

/** @module vertx-js/multi_map */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JMultiMap = io.vertx.core.MultiMap;

/**
 This class represents a MultiMap of String keys to a List of String values.
 <p>
 It's useful in Vert.x to represent things in Vert.x like HTTP headers and HTTP parameters which allow
 multiple values for keys.

 @class
*/
var MultiMap = function(j_val) {

  var j_multiMap = j_val;
  var that = this;

  /**
   Returns the value of with the specified name.  If there are
   more than one values for the specified name, the first value is returned.

   @public
   @param name {string} The name of the header to search 
   @return {string} The first header value or <code>null</code> if there is no such entry
   */
  this.get = function(name) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return j_multiMap["get(java.lang.String)"](name);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns the values with the specified name

   @public
   @param name {string} The name to search 
   @return {Array.<string>} A immutable List of values which will be empty if no values are found
   */
  this.getAll = function(name) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return j_multiMap["getAll(java.lang.String)"](name);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Checks to see if there is a value with the specified name

   @public
   @param name {string} The name to search for 
   @return {boolean} true if at least one entry is found
   */
  this.contains = function(name) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return j_multiMap["contains(java.lang.String)"](name);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Return true if empty

   @public

   @return {boolean}
   */
  this.isEmpty = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_multiMap["isEmpty()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Gets a immutable Set of all names

   @public

   @return {Array.<string>} A Set of all names
   */
  this.names = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnSet(j_multiMap["names()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Adds a new value with the specified name and value.

   @public
   @param name {string} The name 
   @param value {string} The value being added 
   @return {MultiMap} a reference to this, so the API can be used fluently
   */
  this.add = function(name, value) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'string') {
      j_multiMap["add(java.lang.String,java.lang.String)"](name, value);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Adds all the entries from another MultiMap to this one

   @public
   @param map {MultiMap} 
   @return {MultiMap} a reference to this, so the API can be used fluently
   */
  this.addAll = function(map) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_multiMap["addAll(io.vertx.core.MultiMap)"](map._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Sets a value under the specified name.
   <p>
   If there is an existing header with the same name, it is removed.

   @public
   @param name {string} The name 
   @param value {string} The value 
   @return {MultiMap} a reference to this, so the API can be used fluently
   */
  this.set = function(name, value) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'string') {
      j_multiMap["set(java.lang.String,java.lang.String)"](name, value);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Cleans this instance.

   @public
   @param map {MultiMap} 
   @return {MultiMap} a reference to this, so the API can be used fluently
   */
  this.setAll = function(map) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_multiMap["setAll(io.vertx.core.MultiMap)"](map._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Removes the value with the given name

   @public
   @param name {string} The name of the value to remove 
   @return {MultiMap} a reference to this, so the API can be used fluently
   */
  this.remove = function(name) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_multiMap["remove(java.lang.String)"](name);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Removes all

   @public

   @return {MultiMap} a reference to this, so the API can be used fluently
   */
  this.clear = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_multiMap["clear()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Return the number of keys.

   @public

   @return {number}
   */
  this.size = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_multiMap["size()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_multiMap;
};

/**
 Create a multi-map implementation with case insensitive keys, for instance it can be used to hold some HTTP headers.

 @memberof module:vertx-js/multi_map

 @return {MultiMap} the multi-map
 */
MultiMap.caseInsensitiveMultiMap = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return utils.convReturnVertxGen(JMultiMap["caseInsensitiveMultiMap()"](), MultiMap);
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = MultiMap;