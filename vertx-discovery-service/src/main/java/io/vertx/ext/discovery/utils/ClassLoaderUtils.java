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
