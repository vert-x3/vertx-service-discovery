# Vert.x Discovery Service

The `vertx-discovery-service` provides a discovery infrastructure to register and discover the services exposed by your
microservice applications. It can be message sources (entities publishing message on the event bus), REST endpoints,
service proxies, or anything you want as you can add your own type.

The discovery service can be extended using bridges to import services from Docker, Kubernetes, Consul...

## Kubernetes Discovery Bridge

The `vertx-discovery-bridge-kubernetes` is a discovery bridge importing the services from Kubernetes in the Vert.x 
discovery service.

## Docker Discovery Bridge

The `vertx-discovery-bridge-docker` is a discovery bridge importing the services from Docker container in the Vert.x 
discovery service. This bridge is experimental.

Another bridge (`vertx-discovery-bridge-docker-links`) analyzes the docker links (environment variable) to import the
 _linked_ services in the discovery infrastructure.

## Consul Discovery Bridge

The `vertx-discovery-bridge-consul` is a discovery bridge importing the services from a _consul.io_ agent.

## Discovery Backend - Redis

The `vertx-discovery-backend-redis` is another implementation of the Vert.x discovery backend (where service record 
are stored) using Redis instead of a distributed map.
