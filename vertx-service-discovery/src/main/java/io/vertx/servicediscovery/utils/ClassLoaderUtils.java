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

package io.vertx.servicediscovery.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ClassLoaderUtils {

  private static final ClassLoader CURRENT = ClassLoaderUtils.class.getClassLoader();


  public static <T> Class<T> load(String className, ClassLoader classLoader) {
    Objects.requireNonNull(className);

    // First try with the current classloader
    Class<T> loaded = tryToLoad(className, CURRENT);

    // If not found, delegate to the given classloader (if set).
    if (loaded == null && classLoader != null) {
      return tryToLoad(className, classLoader);
    }

    // Last attempt, try with the TCCL
    if (loaded == null && Thread.currentThread().getContextClassLoader() != null) {
      return tryToLoad(className, Thread.currentThread().getContextClassLoader());
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

  public static <X, T> X createWithDelegate(Class<X> x, T svc) {
    try {
      Optional<Constructor<?>> maybe = Arrays.stream(x.getConstructors())
        .filter(c -> c.getParameterCount() == 1)
        .filter(c -> c.getParameterTypes()[0].isAssignableFrom(svc.getClass()))
        .findFirst();

      if (maybe.isPresent()) {
        return (X) maybe.get().newInstance(svc);
      } else {
        return null;
      }
    } catch (Exception e) {
      return null;
    }
  }

  public static Object extractDelegate(Object object) {
    if (object == null) {
      return null;
    }

    if (object.getClass().getName().equals("jdk.nashorn.api.scripting.ScriptObjectMirror")) {
      return extractDelegateFromJsObject(object);
    }

    if (object.getClass().getName().startsWith("org.jruby.RubyObjectVar")) {
      return extractDelegateFromJRubyObject(object);
    }

    return extractDelegateFromRX(object);
  }

  private static Object extractDelegateFromRX(Object object) {
    try {
      Method method = object.getClass().getMethod("getDelegate");
      return method.invoke(object);
    } catch (NoSuchMethodException e) {
      // Not a RX object
      return null;
    }  catch (Exception e) {
      throw new RuntimeException("Unable to extract the delegate from the given RX object", e);
    }
  }

  public static Object extractDelegateFromJRubyObject(Object object) {
    try {
      Method method = object.getClass().getMethod("callMethod", String.class);
      return method.invoke(object, "j_del");
    } catch (Exception e) {
      throw new RuntimeException("Unable to extract the delegate from the given Ruby object", e);
    }
  }

  public static Object extractDelegateFromJsObject(Object object) {
    try {
      Method method = object.getClass().getMethod("getMember", String.class);
      return method.invoke(object, "_jdel");
    } catch (Exception e) {
      throw new RuntimeException("Unable to extract the delegate from the given JavaScript object", e);
    }
  }
}
