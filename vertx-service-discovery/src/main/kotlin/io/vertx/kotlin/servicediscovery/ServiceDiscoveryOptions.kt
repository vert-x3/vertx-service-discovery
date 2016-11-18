package io.vertx.kotlin.servicediscovery

import io.vertx.servicediscovery.ServiceDiscoveryOptions

fun ServiceDiscoveryOptions(
        announceAddress: String? = null,
    backendConfiguration: io.vertx.core.json.JsonObject? = null,
    name: String? = null,
    usageAddress: String? = null): ServiceDiscoveryOptions = io.vertx.servicediscovery.ServiceDiscoveryOptions().apply {

    if (announceAddress != null) {
        this.announceAddress = announceAddress
    }

    if (backendConfiguration != null) {
        this.backendConfiguration = backendConfiguration
    }

    if (name != null) {
        this.name = name
    }

    if (usageAddress != null) {
        this.usageAddress = usageAddress
    }

}

