/*
 * Copyright (c) 2011-2016 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *      The Eclipse Public License is available at
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *      The Apache License v2.0 is available at
 *      http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.ext.discovery.hazelcast;

import com.hazelcast.config.NetworkConfig;
import com.hazelcast.logging.ILogger;
import com.hazelcast.nio.Address;
import com.hazelcast.spi.discovery.AbstractDiscoveryStrategy;
import com.hazelcast.spi.discovery.DiscoveryNode;
import com.hazelcast.spi.discovery.SimpleDiscoveryNode;
import io.fabric8.kubernetes.api.model.EndpointAddress;
import io.fabric8.kubernetes.api.model.EndpointSubset;
import io.fabric8.kubernetes.api.model.Endpoints;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.vertx.ext.discovery.kubernetes.KubernetesUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Discovery SPI implementation of Hazelcast to support Kubernetes-based discovery. This implementation
 * is very close to the "official" hazelcast plugin, when remove some limitations such as the Kubernetes
 * master url, and does not rely on DNS (but on service lookup), and is vert.x centric.
 * <p>
 * It works as follows:
 * <p>
 * * when the discovery strategy is instantiated, it resolved the known nodes
 * * known nodes are found by doing a Kubernetes query: it looks for all endpoints (~pods) attached to
 * a specific service (`vertx-eventbus` by default).
 * * To be retrieved, each pod needs to be associated with the service, so must be
 * <strong>selected</strong> by the service.
 * <p>
 * Th service must a <strong>headless</strong> service. It is a recommended to use a label-based
 * selector such as <code></code>vertx-cluster:true</code>.
 * <p>
 * By default it uses the port 5701 to connected. If the endpoints defines the
 * <code>hazelcast-service-port</code>, the indicated value is used.
 * <p>
 * Can be configured:
 * <p>
 * * "namespace" : the kubernetes namespace / project, "default" by default
 * * "service-name" : the name of the service, "vertx-eventbus" by default.
 * * "kubernetes-master" : the url of the Kubernetes master, by default it builds the url from the
 * {@code KUBERNETES_SERVICE_HOST} and {@code KUBERNETES_SERVICE_PORT}.
 * * "kubernetes-token" : the bearer token to use to connect to Kubernetes, it uses the content of the
 * {@code /var/run/secrets/kubernetes.io/serviceaccount/token} file by default.
 * <p>
 * If you use Openshift and follow the service name convention, you just need to configure the
 * {@code namespace}.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
class HazelcastKubernetesDiscoveryStrategy extends AbstractDiscoveryStrategy {

  public static final String KUBERNETES_SYSTEM_PREFIX = "hazelcast.kubernetes.";
  private static final String HAZELCAST_SERVICE_PORT = "hazelcast-service-port";

  private final String namespace;
  private final String service;
  private final DefaultKubernetesClient client;

  /**
   * Creates a new instance of {@link HazelcastKubernetesDiscoveryStrategy}. This constructor is
   * called by {@link HazelcastKubernetesDiscoveryStrategyFactory}.
   *
   * @param logger        the logger
   * @param configuration the configuration
   */
  HazelcastKubernetesDiscoveryStrategy(ILogger logger, Map<String, Comparable> configuration) {
    super(logger, configuration);

    this.namespace = getOrDefault(KUBERNETES_SYSTEM_PREFIX,
        HazelcastKubernetesDiscoveryStrategyFactory.NAMESPACE, "default");

    String master = getOrDefault(KUBERNETES_SYSTEM_PREFIX,
        HazelcastKubernetesDiscoveryStrategyFactory.KUBERNETES_MASTER,
        KubernetesUtils.getDefaultKubernetesMasterUrl());

    this.service = getOrDefault(KUBERNETES_SYSTEM_PREFIX,
        HazelcastKubernetesDiscoveryStrategyFactory.SERVICE_NAME,
        "vertx-eventbus");

    String token = getOrDefault(KUBERNETES_SYSTEM_PREFIX,
        HazelcastKubernetesDiscoveryStrategyFactory.KUBERNETES_TOKEN, null);
    if (token == null) {
      token = KubernetesUtils.getTokenFromFile();
    }

    Config config = new ConfigBuilder()
        .withOauthToken(token)
        .withMasterUrl(master)
        .withTrustCerts(true)
        .build();
    client = new DefaultKubernetesClient(config);
  }

  /**
   * Just starts the Kubernetes discovery.
   */
  @Override
  public void start() {
    getLogger().info("Starting the Kubernetes-based discovery");
  }

  /**
   * Discovers the nodes. It queries all endpoints (pods) associated with the specified service name
   * in the specified namespace.
   *
   * @return the list of discovery nodes
   */
  @Override
  public Iterable<DiscoveryNode> discoverNodes() {
    Endpoints endpoints = client.endpoints().inNamespace(namespace).withName(service).get();
    if (endpoints == null) {
      getLogger().info("No endpoints for service " + service + " in namespace " + namespace);
      return Collections.emptyList();
    }

    List<DiscoveryNode> nodes = new ArrayList<>();
    for (EndpointSubset endpointSubset : endpoints.getSubsets()) {
      for (EndpointAddress endpointAddress : endpointSubset.getAddresses()) {
        Map<String, Object> properties = endpointAddress.getAdditionalProperties();

        String ip = endpointAddress.getIp();
        InetAddress inetAddress = extractAddress(ip);
        int port = getServicePort(properties);

        Address address = new Address(inetAddress, port);
        nodes.add(new SimpleDiscoveryNode(address, properties));
      }
    }

    getLogger().info("Resolved nodes: " + nodes.stream().map(DiscoveryNode::getPublicAddress).collect(Collectors.toList()));

    return nodes;
  }


  @Override
  public void destroy() {
    // Do nothing.
  }

  private InetAddress extractAddress(String address) {
    if (address == null) {
      return null;
    }

    try {
      return InetAddress.getByName(address);
    } catch (UnknownHostException e) {
      getLogger().warning("Address '" + address + "' could not be resolved");
    }
    return null;
  }

  private int getServicePort(Map<String, Object> properties) {
    int port = NetworkConfig.DEFAULT_PORT;
    if (properties != null) {
      String servicePort = (String) properties.get(HAZELCAST_SERVICE_PORT);
      if (servicePort != null) {
        port = Integer.parseInt(servicePort);
      }
    }
    return port;
  }
}
