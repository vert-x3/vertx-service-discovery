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

package io.vertx.rxjava.servicediscovery.types;

import java.util.Map;
import rx.Observable;
import io.vertx.rxjava.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.spi.ServiceType;
import io.vertx.rxjava.core.eventbus.MessageConsumer;

/**
 * Service type for data producer. Providers are publishing data to a specific event bus address.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.servicediscovery.types.MessageSourceType original} non RX-ified interface using Vert.x codegen.
 */

public class MessageSourceType {

  final io.vertx.servicediscovery.types.MessageSourceType delegate;

  public MessageSourceType(io.vertx.servicediscovery.types.MessageSourceType delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public MessageConsumer getService(ServiceReference ref) { 
    MessageConsumer ret = MessageConsumer.newInstance(delegate.getService((io.vertx.servicediscovery.ServiceReference)ref.getDelegate()));
    return ret;
  }

  public MessageConsumer cachedService(ServiceReference ref) { 
    MessageConsumer ret = MessageConsumer.newInstance(delegate.cachedService((io.vertx.servicediscovery.ServiceReference)ref.getDelegate()));
    return ret;
  }


  public static MessageSourceType newInstance(io.vertx.servicediscovery.types.MessageSourceType arg) {
    return arg != null ? new MessageSourceType(arg) : null;
  }
}
