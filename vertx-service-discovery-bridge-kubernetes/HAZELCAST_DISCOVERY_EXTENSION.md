# Hazelcast Discovery extension

This component also contain an extension to Hazelcast to enable the vert.x cluster creation based on Kubernetes. It
lets you create Vert.x cluster on Kubernetes or in Openshift v3.

It's a Hazelcast discovery SPI implementation of Hazelcast to support Kubernetes-based discovery. This implementation
is very close to the "official" hazelcast plugin, when remove some limitations such as the Kubernetes master url, and
does not rely on DNS (but on Kubernetes service lookup), and is **specific** to vert.x.

It works as follows:

* when the discovery strategy is instantiated, it resolved the known nodes
* known nodes are found by doing a Kubernetes query: it looks for all endpoints (~services) with a specific label
(`vertx-cluster`=`true`). The query is made on the label name and label value.

By default, it uses the port 5701 to connected. If the endpoints defines the `hazelcast-service-port`, the indicated
value is used.

## Using the Hazelcast discovery extension

First you need to add this component to your fat jar (or classpath). If you use Maven, it can be done by adding the
following dependencies:

```
<dependency>
  <groupId>io.vertx</groupId>
  <artifactId>vertx-discovery-bridge-kubernetes</artifactId>
  <version>THE_LAST_VERSION</version>
</dependency>
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-api</artifactId>
  <version>1.7.21</version>
</dependency>
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-simple</artifactId>
  <version>1.7.21</version>
</dependency>
```

Then, you need a specific `cluster.xml` with the following content:

```
<?xml version="1.0" encoding="UTF-8"?>
<hazelcast xsi:schemaLocation="http://www.hazelcast.com/schema/config hazelcast-config-3.6.xsd"
           xmlns="http://www.hazelcast.com/schema/config"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <properties>
    <property name="hazelcast.memcache.enabled">false</property>
    <property name="hazelcast.rest.enabled">false</property>
    <property name="hazelcast.wait.seconds.before.join">0</property>
    <property name="hazelcast.logging.type">jdk</property>

    <property name="hazelcast.health.monitoring.delay.seconds">2</property>

    <property name="hazelcast.max.no.heartbeat.seconds">5</property>
    <property name="hazelcast.max.no.master.confirmation.seconds">10</property>
    <property name="hazelcast.master.confirmation.interval.seconds">10</property>
    <property name="hazelcast.member.list.publish.interval.seconds">10</property>
    <property name="hazelcast.connection.monitor.interval">10</property>
    <property name="hazelcast.connection.monitor.max.faults">2</property>
    <property name="hazelcast.partition.migration.timeout">10</property>
    <property name="hazelcast.migration.min.delay.on.member.removed.seconds">3</property>

    <!-- at the moment the discovery needs to be activated explicitly -->
    <property name="hazelcast.discovery.enabled">true</property>

  </properties>
  <network>
    <port auto-increment="true" port-count="10000">5701</port>
    <outbound-ports>
      <ports>0</ports>
    </outbound-ports>
    <join>
      <multicast enabled="false"/>

      <tcp-ip enabled="false"/>
      <discovery-strategies>
        <discovery-strategy enabled="true"
                            class="io.vertx.servicediscovery.hazelcast.HazelcastKubernetesDiscoveryStrategy">
          <properties>
            <property name="namespace">clement-reactive-msa-demo</property>
          </properties>
        </discovery-strategy>
      </discovery-strategies>
    </join>

    <interfaces enabled="false">
      <interface>10.10.1.*</interface>
    </interfaces>
  </network>
  <partition-group enabled="false"/>
  <executor-service name="default">
    <pool-size>16</pool-size>
    <!--Queue capacity. 0 means Integer.MAX_VALUE.-->
    <queue-capacity>0</queue-capacity>
  </executor-service>
  <map name="__vertx.subs">
    <backup-count>1</backup-count>
    <time-to-live-seconds>0</time-to-live-seconds>
    <max-idle-seconds>0</max-idle-seconds>
    <max-size policy="PER_NODE">0</max-size>
    <eviction-percentage>25</eviction-percentage>
    <merge-policy>com.hazelcast.map.merge.LatestUpdateMapMergePolicy</merge-policy>
  </map>
  <semaphore name="__vertx.*">
    <initial-permits>1</initial-permits>
  </semaphore>
</hazelcast>
```

Two parts are important regarding the Kubernetes discovery:

` <property name="hazelcast.discovery.enabled">true</property>` require to enable the custom discovery

```
<discovery-strategies>
    <discovery-strategy enabled="true"
                        class="io.vertx.servicediscovery.hazelcast.HazelcastKubernetesDiscoveryStrategy">
      <properties>
        <property name="namespace">your_namespace</property>
      </properties>
    </discovery-strategy>
</discovery-strategies>
```

This enables the custom discovery and configure it. Configuration is passed as a set of properties.

## Configuration

Can be configured:

* `namespace` : the kubernetes namespace / project, by default it tries to read the
`KUBERNETES_NAMESPACE` and `OPENSHIFT_BUILD_NAMESPACE`
 environment variables. If none are defined, it uses `default`
* `service-label-name` : the name of the label to look for, `vertx-cluster` by default.
* `service-label-name` : the name of the label to look for, `true` by default.
* `kubernetes-master` : the url of the Kubernetes master, by default it builds the url from the
* `KUBERNETES_SERVICE_HOST` and `KUBERNETES_SERVICE_PORT`.
* `kubernetes-token` : the bearer token to use to connect to Kubernetes, it uses the content of the
 `/var/run/secrets/kubernetes.io/serviceaccount/token` file by default.

If you use Openshift and follow the service name convention, you just need to configure the `namespace`. Even this
 can be omitted if the `KUBERNETES_NAMESPACE` or `OPENSHIFT_BUILD_NAMESPACE` environment variables are set.
