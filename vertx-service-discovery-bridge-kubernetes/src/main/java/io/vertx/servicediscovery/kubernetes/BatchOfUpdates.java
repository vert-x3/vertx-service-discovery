package io.vertx.servicediscovery.kubernetes;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

class BatchOfUpdates {
  final Vertx vertx;
  final long timerId;
  final List<JsonObject> objects = new ArrayList<>();

  public BatchOfUpdates(Vertx vertx, long timerId) {
    this.vertx = vertx;
    this.timerId = timerId;
  }

  public void cancel() {
    vertx.cancelTimer(timerId);
  }
}
