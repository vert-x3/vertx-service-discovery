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

package io.vertx.ext.discovery.utils;

import java.util.Objects;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ClassLoaderUtils {

  private static final ClassLoader CURRENT = ClassLoaderUtils.class.getClassLoader();


  public static <T> Class<T> load(String className, ClassLoader classLoader) {
    Objects.requireNonNull(className);

    // First try with the current classloader
    Class<T> loaded = tryToLoad(className, CURRENT);
    if (loaded == null && classLoader != null) {
      // If not found, delegate to the given classloader (if set).
      return tryToLoad(className, classLoader);
    }
    return loaded;
  }

  private static <T> Class<T> tryToLoad(String className, ClassLoader classLoader) {
    try {
      return (Class<T>) classLoader.loadClass(className);
    } catch (ClassNotFoundException e) {
      return null;
    }
  }
}
