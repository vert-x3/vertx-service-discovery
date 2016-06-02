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

import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.api.model.ServicePort;
import io.fabric8.kubernetes.client.*;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBridge;
import io.vertx.servicediscovery.spi.ServicePublisher;
import io.vertx.servicediscovery.spi.ServiceType;
import io.vertx.servicediscovery.types.HttpEndpoint;
import io.vertx.servicediscovery.types.HttpLocation;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A discovery bridge listening for kubernetes services and publishing them in the Vert.x service discovery.
 * This bridge only supports the importation of services from kubernetes in vert.x (and not the opposite).
 * <p>
 * The bridge is configured using:
 * <p>
 * * the oauth token (using the content of `/var/run/secrets/kubernetes.io/serviceaccount/token` by default)
 * * the namespace in which the service are searched (defaults to `default`).
 * <p>
 * Be aware that the application must have access to Kubernetes and must be able to read the chosen namespace.
 * <p>
 * {@link Record} are created from Kubernetes Service. The service type is deduced from the `service.type` label. If
 * not set, the service is imported as `unknown`. Only `http-endpoint` are supported for now.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class KubernetesServiceDiscoveryBridge implements Watcher<Service>, ServiceDiscoveryBridge {

  private final static Logger LOGGER = LoggerFactory.getLogger(KubernetesServiceDiscoveryBridge.class.getName());

  private KubernetesClient client;
  private ServicePublisher publisher;
  private String namespace;
  private List<Record> records = new CopyOnWriteArrayList<>();
  private Watch watcher;

  @Override
  public void start(Vertx vertx, ServicePublisher publisher, JsonObject configuration,
                    Future<Void> completion) {
    this.publisher = publisher;

    JsonObject conf;
    if (configuration == null) {
      conf = new JsonObject();
    } else {
      conf = configuration;
    }

    // 1) get kubernetes auth info
    this.namespace = conf.getString("namespace", "default");
    LOGGER.info("Kubernetes discovery configured for namespace: " + namespace);
    String master = conf.getString("master",
        KubernetesUtils.getDefaultKubernetesMasterUrl());
    LOGGER.info("Kubernetes url: " + master);
    vertx.<KubernetesClient>executeBlocking(
        future -> {
          String accountToken = conf.getString("token");
          if (accountToken == null) {
            accountToken = KubernetesUtils.getTokenFromFile();
          }
          LOGGER.info("Kubernetes discovery: Bearer Token { " + accountToken + " }");

          Config config = new ConfigBuilder()
              .withOauthToken(accountToken)
              .withMasterUrl(master)
              .withTrustCerts(true)
              .build();
          DefaultKubernetesClient kubernetesClient = null;
          try {
            kubernetesClient = new DefaultKubernetesClient(config);
            ServiceList list = kubernetesClient.services().inNamespace(namespace).list();
            synchronized (KubernetesServiceDiscoveryBridge.this) {
              watcher = kubernetesClient.services().inNamespace(namespace)
                  .watch(this);
              for (Service service : list.getItems()) {
                Record record = createRecord(service);
                if (addRecordIfNotContained(record)) {
                  publishRecord(record);
                }
              }
            }
            future.complete(kubernetesClient);
          } catch (KubernetesClientException e) {
            if (kubernetesClient != null) {
              kubernetesClient.close();
            }
            future.fail(e);
          }
        },
        ar -> {
          if (ar.succeeded()) {
            this.client = ar.result();
            LOGGER.info("Kubernetes client instantiated");
            completion.complete();
          } else {
            LOGGER.error("Error while interacting with kubernetes", ar.cause());
            completion.fail(ar.cause());
          }
        }
    );
  }

  private void publishRecord(Record record) {
    publisher.publish(record, ar -> {
      if (ar.succeeded()) {
        LOGGER.info("Kubernetes service published in the vert.x service registry: "
            + record.toJson());
      } else {
        LOGGER.error("Kubernetes service not published in the vert.x service registry",
            ar.cause());
      }
    });
  }

  private synchronized boolean addRecordIfNotContained(Record record) {
    for (Record rec : records) {
      if (areTheSameService(rec, record)) {
        return false;
      }
    }
    return records.add(record);
  }

  private boolean areTheSameService(Record record1, Record record2) {
    String uuid = record1.getMetadata().getString("kubernetes.uuid", "");
    String uuid2 = record2.getMetadata().getString("kubernetes.uuid", "");
    String endpoint = record1.getLocation().getString(Record.ENDPOINT, "");
    String endpoint2 = record2.getLocation().getString(Record.ENDPOINT, "");

    // Check the uuid and location
    return uuid.equals(uuid2) && endpoint.equals(endpoint2);
  }

  static Record createRecord(Service service) {
    Record record = new Record()
        .setName(service.getMetadata().getName());

    Map<String, String> labels = service.getMetadata().getLabels();
    if (labels != null) {
      for (Map.Entry<String, String> entry : labels.entrySet()) {
        record.getMetadata().put(entry.getKey(), entry.getValue());
      }
    }

    record.getMetadata().put("kubernetes.namespace", service.getMetadata().getNamespace());
    record.getMetadata().put("kubernetes.name", service.getMetadata().getName());
    record.getMetadata().put("kubernetes.uuid", service.getMetadata().getUid());

    String type = labels != null ? labels.get("service-type") : ServiceType.UNKNOWN;
    if (type == null) {
      type = ServiceType.UNKNOWN;
    }

    switch (type) {
      case "http-endpoint":
        manageHttpService(record, service, labels);
        break;
      default:
        manageUnknownService(record, service, type);
        break;
    }

    return record;
  }

  private static void manageUnknownService(Record record, Service service, String type) {
    List<ServicePort> ports = service.getSpec().getPorts();
    if (ports != null && !ports.isEmpty()) {
      if (ports.size() > 1) {
        LOGGER.warn("More than one ports has been found for " + service.getMetadata().getName() + " - taking the " +
            "first one to build the record location");
      }
      ServicePort port = ports.get(0);
      JsonObject location = new JsonObject();
      if (port.getTargetPort().getIntVal() != null) {
        location.put("internal-port", port.getTargetPort().getIntVal());
      }
      location.put("port", port.getPort());
      location.put("name", port.getName());
      location.put("protocol", port.getProtocol());
      location.put("host", service.getSpec().getClusterIP());

      record.setLocation(location).setType(type);
    } else {
      throw new IllegalStateException("Cannot extract the location from the service " + service.getMetadata()
          .getName() + " - no port");
    }
  }

  private static void manageHttpService(Record record, Service service, Map<String, String> labels) {
    List<ServicePort> ports = service.getSpec().getPorts();
    if (ports != null && !ports.isEmpty()) {

      if (ports.size() > 1) {
        LOGGER.warn("More than one port has been found for " + service.getMetadata().getName() + " - taking the first" +
            " one to extract the HTTP endpoint location");
      }

      ServicePort port = ports.get(0);
      record.setType(HttpEndpoint.TYPE);
      HttpLocation location = new HttpLocation()
          .setHost(service.getSpec().getClusterIP())
          .setPort(port.getPort());

      if (isTrue(labels, "ssl") || port.getPort() != null && port.getPort() == 443) {
        location.setSsl(true);
      }
      record.setLocation(location.toJson());
    } else {
      throw new IllegalStateException("Cannot extract the HTTP URL from the service " + service.getMetadata()
          .getName() + " - no port");
    }
  }


  @Override
  public void stop(Vertx vertx, ServicePublisher publisher, Future<Void> future) {
    synchronized (this) {
      if (watcher != null) {
        watcher.close();
        watcher = null;
      }

      if (client != null) {
        client.close();
        client = null;
      }
    }

    future.complete();
  }

  private static boolean isTrue(Map<String, String> labels, String key) {
    return labels != null && "true".equalsIgnoreCase(labels.get(key));
  }


  @Override
  public synchronized void eventReceived(Action action, Service service) {
    switch (action) {
      case ADDED:
        // new service
        Record record = createRecord(service);
        if (addRecordIfNotContained(record)) {
          publishRecord(record);
        }
        break;
      case DELETED:
      case ERROR:
        // remove service
        record = createRecord(service);
        Record storedRecord = removeRecordIfContained(record);
        if (storedRecord != null) {
          unpublishRecord(storedRecord);
        }
        break;
      case MODIFIED:
        record = createRecord(service);
        storedRecord = removeRecordIfContained(record);
        if (storedRecord != null) {
          publishRecord(record);
        }
    }

  }

  private void unpublishRecord(Record record) {
    publisher.unpublish(record.getRegistration(), ar -> {
      if (ar.failed()) {
        LOGGER.error("Cannot unregister kubernetes service", ar.cause());
      } else {
        LOGGER.info("Kubernetes service unregistered from the vert.x registry: " + record.toJson());
      }
    });
  }

  private Record removeRecordIfContained(Record record) {
    for (Record rec : records) {
      if (areTheSameService(rec, record)) {
        records.remove(rec);
        return rec;
      }
    }
    return null;
  }

  @Override
  public void onClose(KubernetesClientException e) {
    // rather bad, un-publish all the services
    records.forEach(this::unpublishRecord);
  }
}
