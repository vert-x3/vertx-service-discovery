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

package io.vertx.servicediscovery.impl;

import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.spi.ServiceType;
import org.junit.Test;

/**
 * Check the behavior of the service types.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ServiceTypesTest {


  @Test(expected = IllegalArgumentException.class)
  public void unknown() {
    Record record = new Record();
    record.setType(ServiceType.UNKNOWN);

    ServiceTypes.get(record);
  }

  @Test(expected = IllegalArgumentException.class)
  public void notAKnownType() {
    Record record = new Record();
    record.setType("bob");

    ServiceTypes.get(record);
  }

}