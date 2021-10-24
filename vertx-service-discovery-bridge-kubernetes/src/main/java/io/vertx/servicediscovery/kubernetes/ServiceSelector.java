package io.vertx.servicediscovery.kubernetes;

import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ServiceSelector {
  final Map<String, String> labelConditions;

  static ServiceSelector of(final Record record) {
    JsonObject selector = record.getMetadata().getJsonObject("selector");
    if (selector == null) {
      return null;
    } else {
      return new ServiceSelector(selector.stream()
        .filter(entry -> entry.getValue() instanceof String)
        .collect(Collectors.toMap(Map.Entry::getKey, entry -> (String) entry.getValue())));
    }
  }

  ServiceSelector(final Map<String, String> labels) {
    this.labelConditions = labels;
  }

  public boolean matchesLabels(final Map<String, String> labels) {
    for (Map.Entry<String, String> entry : labelConditions.entrySet()) {
      if (!labels.containsKey(entry.getKey())) {
        return false;
      } else {
        if (!entry.getValue().equals(labels.get(entry.getKey()))) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ServiceSelector that = (ServiceSelector) o;
    return labelConditions.equals(that.labelConditions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(labelConditions);
  }

  @Override
  public String toString() {
    return "ServiceSelector{" +
      "labels=" + labelConditions +
      '}';
  }
}
