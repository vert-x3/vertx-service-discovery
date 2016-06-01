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

package io.vertx.servicediscovery.kubernetes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility methods for Kubernetes.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class KubernetesUtils {

  private static final String OPENSHIFT_KUBERNETES_TOKEN_FILE = "/var/run/secrets/kubernetes.io/serviceaccount/token";

  /**
   * Computes the default master url based on the {@code KUBERNETES_SERVICE_HOST} and
   * {@code KUBERNETES_SERVICE_PORT} environment variables
   *
   * @return the computed url
   */
  public static String getDefaultKubernetesMasterUrl() {
    return "https://" + System.getenv().get("KUBERNETES_SERVICE_HOST")
        + ":" + System.getenv("KUBERNETES_SERVICE_PORT");
  }

  /**
   * Gets the token stored in the {@code /var/run/secrets/kubernetes.io/serviceaccount/token}.
   *
   * @return the token
   */
  public static String getTokenFromFile() {
    InputStream is = null;
    try {
      File file = new File(OPENSHIFT_KUBERNETES_TOKEN_FILE);
      byte[] data = new byte[(int) file.length()];
      is = new FileInputStream(file);
      int count = is.read(data);
      if (count <= 0) {
        return null;
      } else {
        return new String(data);
      }
    } catch (IOException e) {
      throw new RuntimeException("Could not get token file", e);
    } finally {
      closeQuietly(is);
    }
  }

  private static void closeQuietly(InputStream is) {
    if (is != null) {
      try {
        is.close();
      } catch (IOException e) {
        // Ignore it.
      }
    }
  }
}
