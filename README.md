# vertx-microservice-toolbox

A set of Vert.x component to build your reactive microservice applications.

## Circuit Breaker

The `vertx-circuit-breaker` is an implementation of the circuit breaker pattern for Vert.x. It keeps track of the number of failures and _open the circuit_ when a threshold is reached. Optionally, a fallback is executed.

Supported failures are:

* failures reported by your code in a `Future`
* exception thrown by your code
* uncompleted futures (timeout)

## Discovery service

The `vertx-discovery-service` provides a discovery infrastructure to register and discover the services exposed by your microservice applications. It can be message sources (entities publishing message on the event bus), REST endpoints, service proxies, or anything you want as you can add your own type.

The discovery service can be extended using bridges to import services from Docker, Kubernates, Consul...

## Kubernetes Discovery Bridge

The `vertx-discovery-bridge-kubernetes` is a discovery bridge importing the services from Kubernetes in the Vert.x 
discovery service.
