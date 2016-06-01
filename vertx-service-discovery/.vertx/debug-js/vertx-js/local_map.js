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

/** @module vertx-js/local_map */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JLocalMap = io.vertx.core.shareddata.LocalMap;

/**
 Local maps can be used to share data safely in a single Vert.x instance.
 <p>
 @class
*/
var LocalMap = function(j_val) {

  var j_localMap = j_val;
  var that = this;

  /**
   Get a value from the map

   @public
   @param key {Object} the key 
   @return {Object} the value, or null if none
   */
  this.get = function(key) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] !== 'function') {
      return utils.convReturnTypeUnknown(j_localMap["get(java.lang.Object)"](utils.convParamTypeUnknown(key)));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Put an entry in the map

   @public
   @param key {Object} the key 
   @param value {Object} the value 
   @return {Object} return the old value, or null if none
   */
  this.put = function(key, value) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] !== 'function' && typeof __args[1] !== 'function') {
      return utils.convReturnTypeUnknown(j_localMap["put(java.lang.Object,java.lang.Object)"](utils.convParamTypeUnknown(key), utils.convParamTypeUnknown(value)));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Remove an entry from the map

   @public
   @param key {Object} the key 
   @return {Object} the old value
   */
  this.remove = function(key) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] !== 'function') {
      return utils.convReturnTypeUnknown(j_localMap["remove(java.lang.Object)"](utils.convParamTypeUnknown(key)));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Clear all entries in the map

   @public

   */
  this.clear = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_localMap["clear()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Get the size of the map

   @public

   @return {number} the number of entries in the map
   */
  this.size = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_localMap["size()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return true if there are zero entries in the map

   @public

   @return {boolean}
   */
  this.isEmpty = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_localMap["isEmpty()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Put the entry only if there is no existing entry for that key

   @public
   @param key {Object} the key 
   @param value {Object} the value 
   @return {Object} the old value or null, if none
   */
  this.putIfAbsent = function(key, value) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] !== 'function' && typeof __args[1] !== 'function') {
      return utils.convReturnTypeUnknown(j_localMap["putIfAbsent(java.lang.Object,java.lang.Object)"](utils.convParamTypeUnknown(key), utils.convParamTypeUnknown(value)));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Remove the entry only if there is an entry with the specified key and value

   @public
   @param key {Object} the key 
   @param value {Object} the value 
   @return {boolean} true if removed
   */
  this.removeIfPresent = function(key, value) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] !== 'function' && typeof __args[1] !== 'function') {
      return j_localMap["removeIfPresent(java.lang.Object,java.lang.Object)"](utils.convParamTypeUnknown(key), utils.convParamTypeUnknown(value));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Replace the entry only if there is an existing entry with the specified key and value

   @public
   @param key {Object} the key 
   @param oldValue {Object} the old value 
   @param newValue {Object} the new value 
   @return {boolean} true if removed
   */
  this.replaceIfPresent = function(key, oldValue, newValue) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] !== 'function' && typeof __args[1] !== 'function' && typeof __args[2] !== 'function') {
      return j_localMap["replaceIfPresent(java.lang.Object,java.lang.Object,java.lang.Object)"](utils.convParamTypeUnknown(key), utils.convParamTypeUnknown(oldValue), utils.convParamTypeUnknown(newValue));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Replace the entry only if there is an existing entry with the key

   @public
   @param key {Object} the key 
   @param value {Object} the new value 
   @return {Object} the old value
   */
  this.replace = function(key, value) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] !== 'function' && typeof __args[1] !== 'function') {
      return utils.convReturnTypeUnknown(j_localMap["replace(java.lang.Object,java.lang.Object)"](utils.convParamTypeUnknown(key), utils.convParamTypeUnknown(value)));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Close and release the map

   @public

   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_localMap["close()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_localMap;
};

// We export the Constructor function
module.exports = LocalMap;