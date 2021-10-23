package io.vertx.servicediscovery.kubernetes;

import io.vertx.codegen.annotations.DataObject;

import java.util.Map;

@DataObject
public class KubernetesService {
  private final String name;
  private final String host;
  private Map<String, String> selectors;

  public KubernetesService(final String name, final String host) {
    this.name = name;
    this.host = host;
  }
}
