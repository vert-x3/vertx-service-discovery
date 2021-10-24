package io.vertx.servicediscovery.kubernetes;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;

import static io.vertx.core.http.HttpMethod.GET;

class KubernetesClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(KubernetesClient.class.getName());
  private static final String OPENSHIFT_KUBERNETES_TOKEN_FILE = "/var/run/secrets/kubernetes.io/serviceaccount/token";

  private final ContextInternal context;
  private HttpClient client;
  private String namespace;
  private String token;


  static Future<KubernetesClient> create(final ContextInternal context, final JsonObject config) {
    KubernetesClient client = new KubernetesClient(context, config);
    return client.retrieveToken(config).map(client);
  }

  private KubernetesClient(final ContextInternal context, final JsonObject config) {
    this.context = context;

    final JsonObject conf = (config != null) ? config : new JsonObject();
    boolean isSsl = conf.getBoolean("ssl", true);
    String host = getHost(conf);
    int port = getPort(conf, isSsl);
    client = context.owner().createHttpClient(new HttpClientOptions()
      .setTrustAll(true)
      .setSsl(isSsl)
      .setDefaultHost(host)
      .setDefaultPort(port)
    );

    this.namespace = conf.getString("namespace", getNamespaceOrDefault());

    LOGGER.info("Kubernetes discovery configured for namespace: " + namespace);
    LOGGER.info("Kubernetes master url: http" + (isSsl ? "s" : "") + "//" + host + ":" + port);
  }

  public String getNamespace() {
    return namespace;
  }

  /**
   * Returns a Kubernetes {@code ServiceList} for the namespace.
   *
   * @see <a href="https://kubernetes.io/docs/reference/kubernetes-api/service-resources/service-v1/#list-list-or-watch-objects-of-kind-service">Kuberenetes API: List Service operation</a>
   */
  public Future<JsonObject> listServices() {
    String path = "/api/v1/namespaces/" + namespace + "/services";
    return client.request(GET, path)
      .compose(this::sendRequest)
      .compose(response -> response.body().compose(body -> {
        if (response.statusCode() != 200) {
          return context.failedFuture("Unable to list services from namespace. [namespace=" + namespace + ", statusCode=" + response.statusCode() + ", content=" + body.toString() + "]");
        } else {
          return context.succeededFuture(body.toJsonObject());
        }
      }));
  }

  public Future<Void> watchServices(final String resourceVersion, final Handler<Buffer> bodyHandler) {
    String path = "/api/v1/namespaces/" + namespace + "/services?"
      + "watch=true"
      + "&allowWatchBookmarks=true"
      + "&resourceVersion=" + resourceVersion;

    return client.request(GET, path)
      .compose(this::sendRequest)
      .compose(response -> {
        if (response.statusCode() != 200) {
          return response.body().compose(body -> context.failedFuture("Unable to watch services. [namespace=" + namespace + ", statusCode=" + response.statusCode() + ", content=" + body.toString() + "]"));
        } else {
          Promise<Void> promise = Promise.promise();
          LOGGER.info("Watching services... [namespace=" + namespace + "]");
          response.exceptionHandler(t -> promise.tryComplete())
            .endHandler(v -> promise.tryComplete())
            .handler(bodyHandler);
          return promise.future();
        }
      });
  }

  /**
   * Returns a Kubernetes {@code PodList} for the namespace.
   *
   * @see <a href="https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.19/#list-pod-v1-core">Kuberenetes API: List Pods operation</a>
   */
  public Future<JsonObject> listPods() {
    String path = "/api/v1/namespaces/" + namespace + "/pods";
    return client.request(GET, path)
      .compose(this::sendRequest)
      .compose(response -> response.body().compose(body -> {
        if (response.statusCode() != 200) {
          return context.failedFuture("Unable to list pods. [namespace=" + namespace + ", statusCode=" + response.statusCode() + ", content=" + body.toString() + "]");
        } else {
          return context.succeededFuture(body.toJsonObject());
        }
      }));
  }

  public Future<Void> watchPods(final String resourceVersion, final Handler<Buffer> bodyHandler) {
    String path = "/api/v1/namespaces/" + namespace + "/pods?"
      + "watch=true"
      + "&allowWatchBookmarks=true"
      + "&resourceVersion=" + resourceVersion;

    return client.request(GET, path)
      .compose(this::sendRequest)
      .compose(response -> {
        if (response.statusCode() != 200) {
          return response.body().compose(body -> context.failedFuture("Unable to watch pods. [namespace=" + namespace + ", statusCode=" + response.statusCode() + ", content=" + body.toString() + "]"));
        } else {
          Promise<Void> promise = Promise.promise();
          LOGGER.info("Watching pods... [namespace=" + namespace + "]");
          response.exceptionHandler(t -> promise.tryComplete())
            .endHandler(v -> promise.tryComplete())
            .handler(bodyHandler);
          return promise.future();
        }
      });
  }

  public void close(final Handler<Void> completionHandler) {
    if (context != null) {
      context.runOnContext(v -> {
        client.close();
        client = null;
        if (completionHandler != null) {
          completionHandler.handle(null);
        }
      });
    } else if (completionHandler != null) {
      completionHandler.handle(null);
    }
  }

  private Future<HttpClientResponse> sendRequest(final HttpClientRequest request) {
    return request.setFollowRedirects(true)
      .putHeader("Authorization", "Bearer " + token)
      .send();
  }


  private static String getHost(final JsonObject config) {
    String host = System.getenv("KUBERNETES_SERVICE_HOST");
    if (host == null) {
      host = config.getString("host");
    }
    return host;
  }

  private static int getPort(final JsonObject config, final boolean isSsl) {
    String p = System.getenv("KUBERNETES_SERVICE_PORT");
    if (p != null) {
      return Integer.parseInt(p);
    }

    int port = config.getInteger("port", 0);
    if (port == 0) {
      port = isSsl ? 443 : 80;
    }
    return port;
  }

  private static String getNamespaceOrDefault() {
    // Kubernetes with Fabric8 build
    String ns = System.getenv("KUBERNETES_NAMESPACE");
    if (ns == null) {
      // oc / docker build
      ns = System.getenv("OPENSHIFT_BUILD_NAMESPACE");
      if (ns == null) {
        ns = "default";
      }
    }
    return ns;
  }


  private Future<Void> retrieveToken(final JsonObject config) {
    Future<String> result;
    String token = config.getString("token");
    if (token != null && !token.trim().isEmpty()) {
      result = context.succeededFuture(token);
    } else {
      result = context.owner().fileSystem().readFile(OPENSHIFT_KUBERNETES_TOKEN_FILE).map(Buffer::toString);
    }
    return result.onSuccess(tk -> this.token = tk).mapEmpty();
  }
}
