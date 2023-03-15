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

package examples;

import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.Redis;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.RedisDataSource;

/**
 * @author Eric Zhao
 */
public class RedisDataSourceExamples {

  public void example1(ServiceDiscovery discovery) {
    Record record = RedisDataSource.createRecord(
      "some-redis-data-source-service", // The service name
      new JsonObject().put("url", "localhost"), // The location
      new JsonObject().put("some-metadata", "some-value") // Some metadata
    );

    discovery.publish(record).onComplete(ar -> {
      // ...
    });
  }

  public void example2(ServiceDiscovery discovery) {
    // Get the record
    discovery.getRecord(
      new JsonObject().put("name", "some-redis-data-source-service")).onComplete(ar -> {
        if (ar.succeeded() && ar.result() != null) {
          // Retrieve the service reference
          ServiceReference reference = discovery.getReference(ar.result());

          // Retrieve the service instance
          Redis client = reference.getAs(Redis.class);

          // ...

          // when done
          reference.release();
        }
      });
  }

  public void example3(ServiceDiscovery discovery) {
    RedisDataSource.getRedisClient(discovery,
      new JsonObject().put("name", "some-redis-data-source-service")).onComplete(ar -> {
        if (ar.succeeded()) {
          Redis client = ar.result();

          // ...

          // Dont' forget to release the service
          ServiceDiscovery.releaseServiceObject(discovery, client);

        }
      });
  }
}
