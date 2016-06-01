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

package io.vertx.servicediscovery.docker;

import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ContainerPort;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.spi.ServiceType;
import io.vertx.servicediscovery.types.HttpEndpoint;
import io.vertx.servicediscovery.types.HttpLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represent a service exposed by a Docker container.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DockerService {

  private final String name;
  private final String host;
  private String containerId;

  private List<String> containerNames;

  private List<Record> records = new ArrayList<>();

  public DockerService(Container container, String host) {
    this.host = host;
    containerId = container.getId();
    containerNames = Arrays.stream(container.getNames()).collect(Collectors.toList());
    if (!containerNames.isEmpty()) {
      name = containerNames.get(0);
    } else {
      name = containerId;
    }

    for (ContainerPort port : container.getPorts()) {
      Record record = createRecord(container, port);
      if (record != null) {
        records.add(record);
      }
    }

  }

  public List<Record> records() {
    return records;
  }

  public String name() {
    return name;
  }

  public List<String> names() {
    return containerNames;
  }

  public String id() {
    return containerId;
  }


  public Record createRecord(Container container, ContainerPort port) {
    Record record = new Record()
        .setName(name);

    Map<String, String> labels = container.getLabels();
    if (labels != null) {
      for (Map.Entry<String, String> entry : labels.entrySet()) {
        record.getMetadata().put(entry.getKey(), entry.getValue());
      }
    }

    JsonArray names = new JsonArray();
    containerNames.forEach(names::add);
    record.getMetadata().put("docker.names", names);
    record.getMetadata().put("docker.name", name);
    record.getMetadata().put("docker.id", containerId);

    String type = labels != null ? labels.get("service.type") : ServiceType.UNKNOWN;
    if (type == null) {
      type = ServiceType.UNKNOWN;
    }

    switch (type) {
      case "http-endpoint":
        return manageHttpService(record, port, labels);
      default:
        return manageUnknownService(record, port);
    }
  }

  private Record manageUnknownService(Record record, ContainerPort port) {
    if (port.getPublicPort() == null || port.getPublicPort() == 0) {
      return null;
    }
    JsonObject location = new JsonObject();
    location.put("port", port.getPublicPort());
    location.put("internal-port", port.getPrivatePort());
    location.put("type", port.getType());
    location.put("ip", host);

    return record.setLocation(location).setType(ServiceType.UNKNOWN);
  }

  private static Record manageHttpService(Record record, ContainerPort port, Map<String, String> labels) {
    if (port.getPublicPort() == null || port.getPublicPort() == 0) {
      return null;
    }
    record.setType(HttpEndpoint.TYPE);
    HttpLocation location = new HttpLocation()
        .setHost(port.getIp())
        .setPort(port.getPublicPort());

    if (isTrue(labels, "ssl")  || port.getPrivatePort() == 443) {
      location.setSsl(true);
    }
    return record.setLocation(location.toJson());
  }

  private static boolean isTrue(Map<String, String> labels, String key) {
    return labels != null && "true".equalsIgnoreCase(labels.get(key));
  }
}

