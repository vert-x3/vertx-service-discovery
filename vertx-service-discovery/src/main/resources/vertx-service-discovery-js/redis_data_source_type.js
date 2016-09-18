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

/** @module vertx-service-discovery-js/redis_data_source_type */
var utils = require('vertx-js/util/utils');
var ServiceReference = require('vertx-service-discovery-js/service_reference');
var RedisClient = require('vertx-redis-js/redis_client');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JRedisDataSourceType = io.vertx.servicediscovery.types.RedisDataSourceType;

/**
 Service type for Redis data source.

 @class
*/
var RedisDataSourceType = function(j_val) {

  var j_redisDataSourceType = j_val;
  var that = this;

  /**

   @public
   @param ref {ServiceReference} 
   @return {RedisClient}
   */
  this.getService = function(ref) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      return utils.convReturnVertxGen(j_redisDataSourceType["getService(io.vertx.servicediscovery.ServiceReference)"](ref._jdel), RedisClient);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param ref {ServiceReference} 
   @return {RedisClient}
   */
  this.cachedService = function(ref) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      return utils.convReturnVertxGen(j_redisDataSourceType["cachedService(io.vertx.servicediscovery.ServiceReference)"](ref._jdel), RedisClient);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_redisDataSourceType;
};

// We export the Constructor function
module.exports = RedisDataSourceType;