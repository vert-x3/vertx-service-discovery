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

package io.vertx.servicediscovery.backend.consul;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.consul.Check;
import io.vertx.ext.consul.CheckList;
import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.CheckStatus;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.Service;
import io.vertx.ext.consul.ServiceList;
import io.vertx.ext.consul.ServiceOptions;
import io.vertx.ext.consul.ServiceQueryOptions;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.Status;
import io.vertx.servicediscovery.impl.ServiceTypes;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackend;
import io.vertx.servicediscovery.spi.ServiceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * An implementation of the discovery backend based on Consul. Relies on {@link ConsulClient}.
 *
 * @author <a href="mailto:cafeinoman@openaliasbox.org>Francois Delalleau</a>
 */
public class ConsulBackendService implements ServiceDiscoveryBackend {

  private ConsulClient client;

  @Override
  public void init(Vertx vertx, JsonObject config) {
    ConsulClientOptions opt = new ConsulClientOptions(config);
    this.client = ConsulClient.create(vertx, opt);
  }

  @Override
  public void store(Record record, Handler<AsyncResult<Record>> resultHandler) {
    String uuid = UUID.randomUUID().toString();
    if (record.getRegistration() != null) {
      throw new IllegalArgumentException("The record has already been registered");
    }
    ServiceOptions serviceOptions = recordToServiceOptions(record, uuid);
    record.setRegistration(serviceOptions.getId());
    Promise<Void> registration = Promise.promise();
    client.registerService(serviceOptions).onComplete(registration);
    registration.future().map(record).onComplete(resultHandler);
  }

  @Override
  public void remove(Record record, Handler<AsyncResult<Record>> resultHandler) {
    Objects.requireNonNull(record.getRegistration(), "No registration id in the record");
    Promise<Void> deregistration = Promise.promise();
    client.deregisterService(record.getRegistration()).onComplete(deregistration);
    deregistration.future().map(record).onComplete(resultHandler);
  }

  @Override
  public void remove(String uuid, Handler<AsyncResult<Record>> resultHandler) {
    Objects.requireNonNull(uuid, "No registration id in the record");
    getRecord(uuid, asyncRecord -> {
      if (asyncRecord.succeeded()) {
        remove(asyncRecord.result(), resultHandler);
      } else {
        resultHandler.handle(Future.failedFuture(asyncRecord.cause()));
      }
    });
  }

  @Override
  public void update(Record record, Handler<AsyncResult<Void>> resultHandler) {
    Objects.requireNonNull(record.getRegistration(), "No registration id in the record");
    client.registerService(recordToServiceOptions(record, null)).onComplete(resultHandler);
  }

  @Override
  public void getRecords(Handler<AsyncResult<List<Record>>> resultHandler) {
    Promise<ServiceList> nameList = Promise.promise();
    client.catalogServices().onComplete(nameList);
    nameList.future().map(ServiceList::getList)
      .map(l -> {
        List<Future> recordFutureList = new ArrayList<>();
        l.forEach(s -> {
          if (!"consul".equals(s.getName())) {
            ServiceQueryOptions opt = new ServiceQueryOptions();
            if (!s.getTags().isEmpty()) {
              opt.setTag(s.getTags().get(0));
            }
            Promise<ServiceList> serviceList = Promise.promise();
            client.catalogServiceNodesWithOptions(s.getName(), opt).onComplete(serviceList);
            recordFutureList.add(serviceList.future());
          }
        });
        return recordFutureList;
      })
      .compose(CompositeFuture::all)
      .map(c -> c.<ServiceList>list().stream().flatMap(l -> l.getList().stream()).map(this::serviceToRecord).collect(Collectors.toList()))
      .compose(CompositeFuture::all)
      .map(c -> c.list().stream().map(o -> (Record) o).collect(Collectors.toList()))
      .onComplete(resultHandler);

  }

  @Override
  public void getRecord(String uuid, Handler<AsyncResult<Record>> resultHandler) {
    Promise<List<Record>> recordList = Promise.promise();
    getRecords(recordList);
    recordList.future().map(l -> l.stream().filter(r -> uuid.equals(r.getRegistration())).findFirst().orElse(null)).onComplete(resultHandler);
  }

  public void close() {
    client.close();
  }

  private ServiceOptions recordToServiceOptions(Record record, String uuid) {
    ServiceOptions serviceOptions = new ServiceOptions();
    serviceOptions.setName(record.getName());
    JsonArray tags = new JsonArray();
    if (record.getMetadata() != null) {
      tags.addAll(record.getMetadata().getJsonArray("tags", new JsonArray()));
      //only put CheckOptions to newly registered services
      if (record.getRegistration() == null) {
        serviceOptions.setCheckOptions(new CheckOptions(record.getMetadata().getJsonObject("checkoptions", new JsonObject())));
        record.getMetadata().remove("checkoptions");
      }
      record.getMetadata().remove("tags");
      //add metadata object to the tags, so it can be retrieved
      tags.add("metadata:" + record.getMetadata().encode());
    }
    if (record.getRegistration() != null) {
      serviceOptions.setId(record.getRegistration());
    } else {
      serviceOptions.setId(uuid);
    }
    //add the record type to the tags
    if (!tags.contains(record.getType()) && record.getType() != null) {
      tags.add(record.getType());
    }
    // only put address and port if this is an HTTP endpoint, to be sure that's an IP address, so we can use DNS queries
    if (record.getLocation() != null) {
      if (record.getLocation().containsKey("host")) {
        serviceOptions.setAddress(record.getLocation().getString("host"));
      }
      if (record.getLocation().containsKey("port")) {
        serviceOptions.setPort(record.getLocation().getInteger("port"));
      }
      //add location object to the tags, so it can be retrieved
      tags.add("location:" + record.getLocation().encode());
    }
    serviceOptions.setTags(tags.stream().map(String::valueOf).collect(Collectors.toList()));
    return serviceOptions;
  }


  private Future serviceToRecord(Service service) {
    //use the checks to set the record status
    Promise<CheckList> checkListFuture = Promise.promise();
    client.healthChecks(service.getName()).onComplete(checkListFuture);
    return checkListFuture.future().map(cl -> cl.getList().stream().map(Check::getStatus).allMatch(CheckStatus.PASSING::equals))
      .map(st -> st ? new Record().setStatus(Status.UP) : new Record().setStatus(Status.DOWN))
      .map(record -> {
        record.setMetadata(new JsonObject());
        record.setLocation(new JsonObject());
        record.setName(service.getName());
        record.setRegistration(service.getId());
        List<String> tags = service.getTags();
        record.setType(ServiceType.UNKNOWN);
        ServiceTypes.all().forEachRemaining(type -> {
          if (service.getTags().contains(type.name())) {
            record.setType(type.name());
            tags.remove(type.name());
          }
        });
        //retrieve the metadata object
        tags.stream().filter(t -> t.startsWith("metadata:")).map(s -> s.substring("metadata:".length())).map(JsonObject::new).forEach(json -> record.getMetadata().mergeIn(json));
        //retrieve the location object
        tags.stream().filter(t -> t.startsWith("location:")).map(s -> s.substring("location:".length())).map(JsonObject::new).forEach(json -> record.getLocation().mergeIn(json));
        record.getMetadata().put("tags", new JsonArray(tags.stream().filter(t -> !t.startsWith("metadata:") && !t.startsWith("location:")).collect(Collectors.toList())));
        return record;
      });
  }
}
