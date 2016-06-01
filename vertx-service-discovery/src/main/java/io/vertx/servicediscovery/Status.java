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

package io.vertx.servicediscovery;

import io.vertx.codegen.annotations.VertxGen;

/**
 * Service publication status.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@VertxGen
public enum Status {

  /**
   * The service is published and is accessible.
   */
  UP,
  /**
   * The service has been withdrawn, it is not accessible anymore.
   */
  DOWN,
  /**
   * The service is still published, but not accessible (maintenance).
   */
  OUT_OF_SERVICE,
  /**
   * Unknown status.
   */
  UNKNOWN
}
