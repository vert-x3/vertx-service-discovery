package io.vertx.servicediscovery.kubernetes;

import io.vertx.servicediscovery.Record;

import java.util.Objects;

import static io.vertx.servicediscovery.kubernetes.KubernetesServiceImporter.KUBERNETES_UUID;

class RecordKey {
  final String uuid;
  final String endpoint;

  RecordKey(Record record) {
    this.uuid = Objects.requireNonNull(record.getMetadata().getString(KUBERNETES_UUID));
    this.endpoint = record.getLocation().getString(Record.ENDPOINT, "");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RecordKey recordKey = (RecordKey) o;

    return uuid.equals(recordKey.uuid) && endpoint.equals(recordKey.endpoint);
  }

  @Override
  public int hashCode() {
    int result = uuid.hashCode();
    result = 31 * result + endpoint.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "RecordKey{" + "uuid='" + uuid + '\'' + ", endpoint='" + endpoint + '\'' + '}';
  }
}
