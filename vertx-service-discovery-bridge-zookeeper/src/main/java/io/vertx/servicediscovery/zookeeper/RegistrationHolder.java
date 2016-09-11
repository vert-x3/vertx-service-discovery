package io.vertx.servicediscovery.zookeeper;

import io.vertx.servicediscovery.Record;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
class RegistrationHolder<T> {

  private final T svc;
  private final Record record;

  RegistrationHolder(Record record, T svc) {
    this.record = record;
    this.svc = svc;
  }

  private T svc() {
    return svc;
  }

  Record record() {
    return record;
  }

  static <T> Set<RegistrationHolder<ServiceInstance<T>>> filter(
      Set<RegistrationHolder<ServiceInstance<T>>> registrations,
      Collection<ServiceInstance<T>> instances) {
    List<RegistrationHolder<ServiceInstance<T>>> toRemove = new ArrayList<>();
    for (RegistrationHolder<ServiceInstance<T>> holder : registrations) {
      for (ServiceInstance<T> instance : instances) {
        String id = instance.getId();
        if (holder.svc().getId().equals(id)) {
          toRemove.add(holder);
        }
      }
    }
    registrations.removeAll(toRemove);
    return registrations;
  }

  static <T> Set<ServiceInstance<T>> filter(
      Set<ServiceInstance<T>> instances,
      Set<RegistrationHolder<ServiceInstance<T>>> registrations) {
    List<ServiceInstance<T>> toRemove = new ArrayList<>();

    for (ServiceInstance<T> instance : instances) {
      String id = instance.getId();
      for (RegistrationHolder<ServiceInstance<T>> holder : registrations) {
        if (holder.svc().getId().equals(id)) {
          toRemove.add(instance);
        }
      }
    }

    instances.removeAll(toRemove);
    return instances;
  }
}
