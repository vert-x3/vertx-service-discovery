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

/** @module vertx-js/event_bus */
var utils = require('vertx-js/util/utils');
var Measured = require('vertx-js/measured');
var SendContext = require('vertx-js/send_context');
var Message = require('vertx-js/message');
var MessageConsumer = require('vertx-js/message_consumer');
var MessageProducer = require('vertx-js/message_producer');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JEventBus = io.vertx.core.eventbus.EventBus;
var DeliveryOptions = io.vertx.core.eventbus.DeliveryOptions;

/**
 A Vert.x event-bus is a light-weight distributed messaging system which allows different parts of your application,
 or different applications and services to communicate with each in a loosely coupled way.
 <p>
 An event-bus supports publish-subscribe messaging, point-to-point messaging and request-response messaging.
 <p>
 Message delivery is best-effort and messages can be lost if failure of all or part of the event bus occurs.
 <p>
 Please refer to the documentation for more information on the event bus.

 @class
*/
var EventBus = function(j_val) {

  var j_eventBus = j_val;
  var that = this;
  Measured.call(this, j_val);

  /**
   Whether the metrics are enabled for this measured object

   @public

   @return {boolean} true if the metrics are enabled
   */
  this.isMetricsEnabled = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_eventBus["isMetricsEnabled()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Like {@link EventBus#send} but specifying a <code>replyHandler</code> that will be called if the recipient
   subsequently replies to the message.

   @public
   @param address {string} the address to send it to 
   @param message {Object} the message, may be <code>null</code> 
   @param options {Object} delivery options 
   @param replyHandler {function} reply handler will be called when any reply from the recipient is received, may be <code>null</code> 
   @return {EventBus} a reference to this, so the API can be used fluently
   */
  this.send = function() {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] !== 'function') {
      j_eventBus["send(java.lang.String,java.lang.Object)"](__args[0], utils.convParamTypeUnknown(__args[1]));
      return that;
    }  else if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] !== 'function' && typeof __args[2] === 'function') {
      j_eventBus["send(java.lang.String,java.lang.Object,io.vertx.core.Handler)"](__args[0], utils.convParamTypeUnknown(__args[1]), function(ar) {
      if (ar.succeeded()) {
        __args[2](utils.convReturnVertxGen(ar.result(), Message), null);
      } else {
        __args[2](null, ar.cause());
      }
    });
      return that;
    }  else if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] !== 'function' && (typeof __args[2] === 'object' && __args[2] != null)) {
      j_eventBus["send(java.lang.String,java.lang.Object,io.vertx.core.eventbus.DeliveryOptions)"](__args[0], utils.convParamTypeUnknown(__args[1]), __args[2] != null ? new DeliveryOptions(new JsonObject(JSON.stringify(__args[2]))) : null);
      return that;
    }  else if (__args.length === 4 && typeof __args[0] === 'string' && typeof __args[1] !== 'function' && (typeof __args[2] === 'object' && __args[2] != null) && typeof __args[3] === 'function') {
      j_eventBus["send(java.lang.String,java.lang.Object,io.vertx.core.eventbus.DeliveryOptions,io.vertx.core.Handler)"](__args[0], utils.convParamTypeUnknown(__args[1]), __args[2] != null ? new DeliveryOptions(new JsonObject(JSON.stringify(__args[2]))) : null, function(ar) {
      if (ar.succeeded()) {
        __args[3](utils.convReturnVertxGen(ar.result(), Message), null);
      } else {
        __args[3](null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Like {@link EventBus#publish} but specifying <code>options</code> that can be used to configure the delivery.

   @public
   @param address {string} the address to publish it to 
   @param message {Object} the message, may be <code>null</code> 
   @param options {Object} the delivery options 
   @return {EventBus} a reference to this, so the API can be used fluently
   */
  this.publish = function() {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] !== 'function') {
      j_eventBus["publish(java.lang.String,java.lang.Object)"](__args[0], utils.convParamTypeUnknown(__args[1]));
      return that;
    }  else if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] !== 'function' && (typeof __args[2] === 'object' && __args[2] != null)) {
      j_eventBus["publish(java.lang.String,java.lang.Object,io.vertx.core.eventbus.DeliveryOptions)"](__args[0], utils.convParamTypeUnknown(__args[1]), __args[2] != null ? new DeliveryOptions(new JsonObject(JSON.stringify(__args[2]))) : null);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Create a consumer and register it against the specified address.

   @public
   @param address {string} the address that will register it at 
   @param handler {function} the handler that will process the received messages 
   @return {MessageConsumer} the event bus message consumer
   */
  this.consumer = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnVertxGen(j_eventBus["consumer(java.lang.String)"](__args[0]), MessageConsumer);
    }  else if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      return utils.convReturnVertxGen(j_eventBus["consumer(java.lang.String,io.vertx.core.Handler)"](__args[0], function(jVal) {
      __args[1](utils.convReturnVertxGen(jVal, Message));
    }), MessageConsumer);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Like {@link EventBus#consumer} but the address won't be propagated across the cluster.

   @public
   @param address {string} the address that will register it at 
   @param handler {function} the handler that will process the received messages 
   @return {MessageConsumer} the event bus message consumer
   */
  this.localConsumer = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnVertxGen(j_eventBus["localConsumer(java.lang.String)"](__args[0]), MessageConsumer);
    }  else if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      return utils.convReturnVertxGen(j_eventBus["localConsumer(java.lang.String,io.vertx.core.Handler)"](__args[0], function(jVal) {
      __args[1](utils.convReturnVertxGen(jVal, Message));
    }), MessageConsumer);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Like {@link EventBus#sender} but specifying delivery options that will be used for configuring the delivery of
   the message.

   @public
   @param address {string} the address to send it to 
   @param options {Object} the delivery options 
   @return {MessageProducer} The sender
   */
  this.sender = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnVertxGen(j_eventBus["sender(java.lang.String)"](__args[0]), MessageProducer);
    }  else if (__args.length === 2 && typeof __args[0] === 'string' && (typeof __args[1] === 'object' && __args[1] != null)) {
      return utils.convReturnVertxGen(j_eventBus["sender(java.lang.String,io.vertx.core.eventbus.DeliveryOptions)"](__args[0], __args[1] != null ? new DeliveryOptions(new JsonObject(JSON.stringify(__args[1]))) : null), MessageProducer);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Like {@link EventBus#publisher} but specifying delivery options that will be used for configuring the delivery of
   the message.

   @public
   @param address {string} the address to publish it to 
   @param options {Object} the delivery options 
   @return {MessageProducer} The publisher
   */
  this.publisher = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnVertxGen(j_eventBus["publisher(java.lang.String)"](__args[0]), MessageProducer);
    }  else if (__args.length === 2 && typeof __args[0] === 'string' && (typeof __args[1] === 'object' && __args[1] != null)) {
      return utils.convReturnVertxGen(j_eventBus["publisher(java.lang.String,io.vertx.core.eventbus.DeliveryOptions)"](__args[0], __args[1] != null ? new DeliveryOptions(new JsonObject(JSON.stringify(__args[1]))) : null), MessageProducer);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Add an interceptor that will be called whenever a message is sent from Vert.x

   @public
   @param interceptor {function} the interceptor 
   @return {EventBus} a reference to this, so the API can be used fluently
   */
  this.addInterceptor = function(interceptor) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      return utils.convReturnVertxGen(j_eventBus["addInterceptor(io.vertx.core.Handler)"](function(jVal) {
      interceptor(utils.convReturnVertxGen(jVal, SendContext));
    }), EventBus);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Remove an interceptor

   @public
   @param interceptor {function} the interceptor 
   @return {EventBus} a reference to this, so the API can be used fluently
   */
  this.removeInterceptor = function(interceptor) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      return utils.convReturnVertxGen(j_eventBus["removeInterceptor(io.vertx.core.Handler)"](function(jVal) {
      interceptor(utils.convReturnVertxGen(jVal, SendContext));
    }), EventBus);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_eventBus;
};

// We export the Constructor function
module.exports = EventBus;