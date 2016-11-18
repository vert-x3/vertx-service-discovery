package io.vertx.kotlin.servicediscovery.types

import io.vertx.servicediscovery.types.HttpLocation

fun HttpLocation(
        endpoint: String? = null,
    host: String? = null,
    port: Int? = null,
    root: String? = null,
    ssl: Boolean? = null): HttpLocation = io.vertx.servicediscovery.types.HttpLocation().apply {

    if (endpoint != null) {
        this.endpoint = endpoint
    }

    if (host != null) {
        this.host = host
    }

    if (port != null) {
        this.port = port
    }

    if (root != null) {
        this.root = root
    }

    if (ssl != null) {
        this.isSsl = ssl
    }

}

