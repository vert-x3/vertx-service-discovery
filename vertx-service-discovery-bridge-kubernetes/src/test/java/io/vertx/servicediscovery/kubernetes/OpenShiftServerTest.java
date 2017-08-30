package io.vertx.servicediscovery.kubernetes;

import io.fabric8.kubernetes.client.server.mock.KubernetesMockServer;
import io.fabric8.openshift.client.server.mock.OpenShiftMockServer;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.runner.RunWith;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@RunWith(VertxUnitRunner.class)
public class OpenShiftServerTest extends KubernetesServerTest {

  @Override
  public KubernetesMockServer getServer() {
    return new OpenShiftMockServer(false);
  }
}
