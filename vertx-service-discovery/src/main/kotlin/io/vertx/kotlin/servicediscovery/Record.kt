package io.vertx.kotlin.servicediscovery

import io.vertx.servicediscovery.Record
import io.vertx.servicediscovery.Status

fun Record(
    location: io.vertx.core.json.JsonObject? = null,
  metadata: io.vertx.core.json.JsonObject? = null,
  name: String? = null,
  registration: String? = null,
  status: Status? = null,
  type: String? = null): Record = io.vertx.servicediscovery.Record().apply {

  if (location != null) {
    this.location = location
  }

  if (metadata != null) {
    this.metadata = metadata
  }

  if (name != null) {
    this.name = name
  }

  if (registration != null) {
    this.registration = registration
  }

  if (status != null) {
    this.status = status
  }

  if (type != null) {
    this.type = type
  }

}

