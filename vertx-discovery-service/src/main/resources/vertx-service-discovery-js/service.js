/*
 * Copyright (c) 2011-2016 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *      The Eclipse Public License is available at
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *      The Apache License v2.0 is available at
 *      http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */

/** @module vertx-service-discovery-js/service */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JService = io.vertx.ext.discovery.Service;

/**

 @class
*/
var Service = function(j_val) {

  var j_service = j_val;
  var that = this;

  /**

   @public

   @return {Object}
   */
  this.get = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnTypeUnknown(j_service["get()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   */
  this.release = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_service["release()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_service;
};

// We export the Constructor function
module.exports = Service;