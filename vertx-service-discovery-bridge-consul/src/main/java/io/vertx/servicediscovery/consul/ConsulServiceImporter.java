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

package io.vertx.servicediscovery.consul;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.consul.*;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.impl.ServiceTypes;
import io.vertx.servicediscovery.spi.ServiceImporter;
import io.vertx.servicediscovery.spi.ServicePublisher;
import io.vertx.servicediscovery.spi.ServiceType;
import io.vertx.servicediscovery.types.HttpLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A discovery bridge importing services from Consul.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ConsulServiceImporter implements ServiceImporter {

  private ServicePublisher publisher;
  private ConsulClient client;

  private final static Logger LOGGER = LoggerFactory.getLogger(ConsulServiceImporter.class);

  private final List<ImportedConsulService> imports = new ArrayList<>();
  private long scanTask = -1;
  private Vertx vertx;
  private String upThreshold;

  @Override
  public void start(Vertx vertx, ServicePublisher publisher, JsonObject configuration, Promise<Void> completion) {
    this.vertx = vertx;
    this.publisher = publisher;
    this.upThreshold = configuration.getString("up_threshold", "passing");

    ConsulClientOptions opts = new ConsulClientOptions()
      .setHost(configuration.getString("host", "localhost"))
      .setPort(configuration.getInteger("port", 8500))
      .setDc(configuration.getString("dc"))
      .setAclToken(configuration.getString("acl_token"));

    client = ConsulClient.create(vertx, opts);

    Promise<List<ImportedConsulService>> imports = Promise.promise();

    retrieveServicesFromConsul(imports);

    imports.future().onComplete(ar -> {
      if (ar.succeeded()) {
        Integer period = configuration.getInteger("scan-period", 2000);
        if (period != 0) {
          scanTask = vertx.setPeriodic(period, l -> {
            Promise<List<ImportedConsulService>> promise = Promise.promise();
            promise.future().onComplete(ar2 -> {
              if (ar2.failed()) {
                LOGGER.warn("Consul importation has failed", ar.cause());
              }
            });
            retrieveServicesFromConsul(promise);
          });
        }
        completion.complete();
      } else {
        completion.fail(ar.cause());
      }
    });

  }


  private Handler<Throwable> getErrorHandler(Promise future) {
    return t -> {
      if (future != null) {
        future.tryFail(t);
      } else {
        LOGGER.error(t);
      }
    };
  }

  private void retrieveServicesFromConsul(Promise<List<ImportedConsulService>> completed) {
    client.catalogServices().onComplete(ar -> {
      if (ar.succeeded()) {
        retrieveIndividualServices(ar.result(), completed);
      } else {
        completed.fail(ar.cause());
      }
    });
  }

  private boolean isCheckOK(CheckStatus checkStatus){
    if (this.upThreshold.equals("passing")){
      return checkStatus.equals(CheckStatus.PASSING);
    }
    if (this.upThreshold.equals("warning")) {
      return checkStatus.equals(CheckStatus.WARNING) || checkStatus.equals(CheckStatus.PASSING);
    }
    return true;
  }

  private void retrieveIndividualServices(ServiceList list, Promise<List<ImportedConsulService>> completed) {
    List<Future> futures = new ArrayList<>();
    list.getList().forEach(service -> {

      Promise<List<ImportedConsulService>> promise = Promise.promise();
      client.healthServiceNodes(service.getName(),false).onComplete(ar -> {
        if (ar.succeeded()) {
          importService(ar.result().getList(), promise);
        } else {
          completed.fail(ar.cause());
        }
      });

      futures.add(promise.future());
    });

    CompositeFuture.all(futures).onComplete(ar -> {
      if (ar.failed()) {
        LOGGER.error("Fail to retrieve the services from consul", ar.cause());
      } else {
        List<ImportedConsulService> services =
            futures.stream().map(future -> ((Future<List<ImportedConsulService>>) future).result())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<String> retrievedIds = services.stream().map(ImportedConsulService::id).collect(Collectors.toList());

        synchronized (ConsulServiceImporter.this) {

          List<String> existingIds = imports.stream().map(ImportedConsulService::id).collect(Collectors.toList());

          LOGGER.trace("Imported services: " + existingIds + ", Retrieved services form Consul: " + retrievedIds);

          services.forEach(svc -> {
            String id = svc.id();

            if (!existingIds.contains(id)) {
              LOGGER.info("Imported service: " + id);
              imports.add(svc);
            }
          });

          List<ImportedConsulService> toRemove = new ArrayList<>();
          imports.forEach(svc -> {
            if (!retrievedIds.contains(svc.id())) {
              LOGGER.info("Unregistering " + svc.id());
              toRemove.add(svc);
              svc.unregister(publisher, null);
            }
          });

          imports.removeAll(toRemove);
        }
      }

      if (ar.succeeded()) {
        completed.complete();
      } else {
        completed.fail(ar.cause());
      }
    });
  }

  private void importService(List<ServiceEntry> list, Promise<List<ImportedConsulService>> future) {
    if (list.isEmpty()) {
      future.fail("no service with the given name");
    } else {
      List<ServiceEntry> serviceEntries = list.stream()
        .filter(serviceEntry ->
          serviceEntry.getChecks().stream().allMatch(check -> isCheckOK(check.getStatus()))
        )
        .collect(Collectors.toList());

      List<ImportedConsulService> importedServices = new ArrayList<>();

      List<Future> registrations = new ArrayList<>();
      for (int i = 0; i < serviceEntries.size(); i++) {
        Promise<Void> registration = Promise.promise();

        ServiceEntry consulService = serviceEntries.get(i);
        String id = consulService.getService().getId();
        String name = consulService.getService().getName();
        Record record = createRecord(consulService.getNode(), consulService.getService());

        // the id must be unique, so check if the service has already being imported
        ImportedConsulService imported = getImportedServiceById(id);
        if (imported != null) {
          importedServices.add(imported);
          registration.complete();
        } else {
          LOGGER.info("Importing service " + record.getName() + " (" + id + ")"
              + " from consul");
          ImportedConsulService service = new ImportedConsulService(name, id, record);
          Promise<ImportedConsulService> promise = Promise.promise();
          promise.future().onComplete(res -> {
            if (res.succeeded()) {
              importedServices.add(res.result());
              registration.complete();
            } else {
              registration.fail(res.cause());
            }
          });
          service.register(publisher, promise);
        }
        registrations.add(registration.future());
      }

      CompositeFuture.all(registrations).onComplete(ar -> {
        if (ar.succeeded()) {
          future.complete(importedServices);
        } else {
          future.fail(ar.cause());
        }
      });
    }
  }

  private Record createRecord(Node node, Service service) {
    String address = service.getAddress();
    if (address == null || address.isEmpty()) {
      address = node.getAddress();
    }

    int port = service.getPort();

    JsonObject metadata = service.toJson();
    if (service.getTags() != null) {
      service.getTags().forEach(tag -> metadata.put(tag, true));
    }

    Record record = new Record()
        .setName(service.getName())
        .setMetadata(metadata);

    // To determine the record type, check if we have a tag with a "type" name
    record.setType(ServiceType.UNKNOWN);
    ServiceTypes.all().forEachRemaining(type -> {
      if (metadata.getBoolean(type.name(), false)) {
        record.setType(type.name());
      }
    });

    JsonObject location = new JsonObject();
    location.put("host", address);
    location.put("port", port);

    // Manage HTTP endpoint
    if (record.getType().equals("http-endpoint")) {
      if (metadata.getBoolean("ssl", false)) {
        location.put("ssl", true);
      }
      location = new HttpLocation(location).toJson();
    }

    record.setLocation(location);
    return record;
  }

  private synchronized ImportedConsulService getImportedServiceById(String id) {
    for (ImportedConsulService svc : imports) {
      if (svc.id().equals(id)) {
        return svc;
      }
    }
    return null;
  }

  @Override
  public synchronized void close(Handler<Void> completionHandler) {
    if (scanTask != -1) {
      vertx.cancelTimer(scanTask);
    }
    // Remove all the services that has been imported
    List<Future> list = new ArrayList<>();
    imports.forEach(imported -> {
      Promise<Void> promise = Promise.promise();
      promise.future().onComplete(ar -> {
        LOGGER.info("Unregistering " + imported.name());
        if (ar.succeeded()) {
          list.add(Future.succeededFuture());
        } else {
          list.add(Future.failedFuture(ar.cause()));
        }
      });
      imported.unregister(publisher, promise);
    });

    CompositeFuture.all(list).onComplete(ar -> {
      clearImportedServices();
      if (ar.succeeded()) {
        LOGGER.info("Successfully closed the service importer " + this);
      } else {
        LOGGER.error("A failure has been caught while stopping " + this, ar.cause());
      }
      if (completionHandler != null) {
        completionHandler.handle(null);
      }
    });
  }

  private synchronized void clearImportedServices(){
    imports.clear();
  }
}
