# Vert.x Service Discovery

[![Build Status](https://github.com/vert-x3/vertx-service-discovery/workflows/CI/badge.svg?branch=3.9)](https://github.com/vert-x3/vertx-service-discovery/actions?query=workflow%3ACI)

The `vertx-service-discovery` provides a discovery infrastructure to register and discover the services exposed by your
microservice applications. It can be message sources (entities publishing message on the event bus), REST endpoints,
service proxies, or anything you want as you can add your own type.

The service discovery can be extended using bridges to import services from Docker, Kubernetes, Consul...

You can find the [full documentation](http://vertx.io/docs/#microservices) on the Vert.x website.

## Kubernetes Discovery Bridge

The `vertx-discovery-bridge-kubernetes` is a discovery bridge importing the services from Kubernetes in the Vert.x service discovery.

## Docker Discovery Bridge

The `vertx-discovery-bridge-docker` is a discovery bridge importing the services from Docker container in the Vert.x service discovery. This bridge is experimental.

Another bridge (`vertx-discovery-bridge-docker-links`) analyzes the docker links (environment variable) to import the
 _linked_ services in the discovery infrastructure.

## Consul Discovery Bridge

The `vertx-discovery-bridge-consul` is a discovery bridge importing the services from a _consul.io_ agent.

## Discovery Backend - Redis

The `vertx-discovery-backend-redis` is another implementation of the Vert.x discovery backend (where service record 
are stored) using Redis instead of a distributed map.
