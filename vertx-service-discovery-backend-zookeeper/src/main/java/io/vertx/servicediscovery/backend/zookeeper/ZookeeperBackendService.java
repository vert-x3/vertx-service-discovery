package io.vertx.servicediscovery.backend.zookeeper;

import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.spi.ServiceDiscoveryBackend;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.DeleteBuilder;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ZookeeperBackendService implements ServiceDiscoveryBackend, ConnectionStateListener {

  private static final Charset CHARSET = Charset.forName("UTF-8");
  private CuratorFramework client;
  private String basePath;
  private boolean ephemeral;
  private boolean guaranteed;
  private Vertx vertx;
  private ConnectionState connectionState = ConnectionState.LOST;
  private boolean canBeReadOnly;
  private int connectionTimeoutMs;

  @Override
  public void init(Vertx vertx, JsonObject configuration) {
    this.vertx = vertx;
    String connection = Objects.requireNonNull(configuration.getString("connection"));
    int maxRetries = configuration.getInteger("maxRetries", 3);
    int baseGraceBetweenRetries = configuration
        .getInteger("baseSleepTimeBetweenRetries", 1000);
    canBeReadOnly = configuration.getBoolean("canBeReadOnly", false);
    connectionTimeoutMs = configuration.getInteger("connectionTimeoutMs", 1000);
    basePath = configuration.getString("basePath", "/services");
    ephemeral = configuration.getBoolean("ephemeral", false);
    guaranteed = configuration.getBoolean("guaranteed", false);

    client = CuratorFrameworkFactory.builder()
        .canBeReadOnly(canBeReadOnly)
        .connectString(connection)
        .connectionTimeoutMs(connectionTimeoutMs)
        .retryPolicy(new ExponentialBackoffRetry(baseGraceBetweenRetries, maxRetries))
        .build();
    client.getConnectionStateListenable().addListener(this);
    client.start();
  }

  @Override
  public void store(Record record, Handler<AsyncResult<Record>> resultHandler) {
    if (record.getRegistration() != null) {
      resultHandler.handle(Future.failedFuture("The record has already been registered"));
      return;
    }
    String uuid = UUID.randomUUID().toString();
    record.setRegistration(uuid);

    String content = record.toJson().encode();
    Context context = Vertx.currentContext();

    ensureConnected(x -> {
      if (x.failed()) {
        resultHandler.handle(Future.failedFuture(x.cause()));
      } else {
        try {
          client.create()
              .creatingParentsIfNeeded()
              .withMode(ephemeral ? CreateMode.EPHEMERAL : CreateMode.PERSISTENT)
              .inBackground((curatorFramework, curatorEvent)
                  -> callback(context, record, resultHandler, curatorEvent))
              .withUnhandledErrorListener((s, throwable)
                  -> resultHandler.handle(Future.failedFuture(throwable)))
              .forPath(getPath(record.getRegistration()), content.getBytes(CHARSET));
        } catch (Exception e) {
          resultHandler.handle(Future.failedFuture(e));
        }
      }
    });
  }

  private String getPath(String registration) {
    return basePath + "/" + registration;
  }

  private void callback(Context context, Record record, Handler<AsyncResult<Record>> resultHandler, CuratorEvent curatorEvent) {
    runOnContextIfPossible(context, () -> {
      if (curatorEvent.getResultCode() == KeeperException.Code.OK.intValue()) {
        resultHandler.handle(Future.succeededFuture(record));
      } else {
        KeeperException.Code code =
            KeeperException.Code.get(curatorEvent.getResultCode());
        resultHandler.handle(Future.failedFuture(KeeperException.create(code)));
      }
    });
  }

  @Override
  public void remove(Record record, Handler<AsyncResult<Record>> resultHandler) {
    Objects.requireNonNull(record.getRegistration(), "No registration id in the record");
    remove(record.getRegistration(), resultHandler);
  }

  @Override
  public void remove(String uuid, Handler<AsyncResult<Record>> resultHandler) {
    Objects.requireNonNull(uuid, "No registration id in the record");
    Context context = Vertx.currentContext();

    ensureConnected(x -> {
      if (x.failed()) {
        resultHandler.handle(Future.failedFuture(x.cause()));
      } else {
        getRecordById(context, uuid, record -> {
          if (record == null) {
            resultHandler.handle(Future.failedFuture("Unknown registration " + uuid));
          } else {
            try {
              DeleteBuilder delete = client.delete();
              if (guaranteed) {
                delete.guaranteed();
              }
              delete
                  .deletingChildrenIfNeeded()
                  .inBackground((curatorFramework, curatorEvent)
                      -> callback(context, record, resultHandler, curatorEvent))

                  .withUnhandledErrorListener((s, throwable)
                      -> resultHandler.handle(Future.failedFuture(throwable)))

                  .forPath(getPath(uuid));
            } catch (Exception e) {
              resultHandler.handle(Future.failedFuture(e));
            }
          }
        });
      }
    });
  }

  private void getRecordById(Context context, String uuid, Handler<Record> handler) {
    ensureConnected(x -> {
      if (x.failed()) {
        handler.handle(null);
      } else {
        try {
          client.getData()
              .inBackground((curatorFramework, curatorEvent)
                  -> runOnContextIfPossible(context, () -> {
                if (curatorEvent.getResultCode() == KeeperException.Code.OK.intValue()) {
                  JsonObject json
                      = new JsonObject(new String(curatorEvent.getData(), CHARSET));
                  handler.handle(new Record(json));
                } else {
                  handler.handle(null);
                }
              }))
              .forPath(getPath(uuid));
        } catch (Exception e) {
          handler.handle(null);
        }
      }
    });

  }

  private void runOnContextIfPossible(Context context, Runnable runnable) {
    if (context != null) {
      context.runOnContext(v -> runnable.run());
    } else {
      runnable.run();
    }
  }

  @Override
  public void update(Record record, Handler<AsyncResult<Void>> resultHandler) {
    Objects.requireNonNull(record.getRegistration(), "No registration id in the record");
    Context context = Vertx.currentContext();
    ensureConnected(x -> {
      if (x.failed()) {
        resultHandler.handle(Future.failedFuture(x.cause()));
      } else {
        try {
          client.setData()
              .inBackground((framework, event)
                  -> runOnContextIfPossible(context, () -> {
                if (event.getResultCode() == KeeperException.Code.OK.intValue()) {
                  resultHandler.handle(Future.succeededFuture());
                } else {
                  KeeperException.Code code = KeeperException.Code.get(event.getResultCode());
                  resultHandler.handle(Future.failedFuture(KeeperException.create(code)));
                }
              }))
              .withUnhandledErrorListener((message, e) -> resultHandler.handle(Future.failedFuture(e)))
              .forPath(getPath(record.getRegistration()),
                  record.toJson().encode().getBytes(CHARSET));
        } catch (Exception e) {
          resultHandler.handle(Future.failedFuture(e));
        }
      }
    });
  }

  @Override
  public void getRecords(Handler<AsyncResult<List<Record>>> resultHandler) {
    Context context = Vertx.currentContext();
    ensureConnected(
        x -> {
          if (x.failed()) {
            resultHandler.handle(Future.failedFuture(x.cause()));
          } else {
            try {
              client.getChildren()
                  .inBackground((fmk, event) -> {
                    List<String> children = event.getChildren();
                    List<Future> futures = new ArrayList<>();
                    for (String child : children) {
                      Promise<Record> promise = Promise.promise();
                      getRecord(child, promise);
                      futures.add(promise.future());
                    }

                    CompositeFuture.all(futures)
                        .onComplete(
                            ar -> runOnContextIfPossible(context, () -> {
                              if (ar.failed()) {
                                resultHandler.handle(Future.failedFuture(ar.cause()));
                              } else {
                                List<Record> records = new ArrayList<>();
                                for (Future future : futures) {
                                  records.add((Record) future.result());
                                }
                                resultHandler.handle(Future.succeededFuture(records));
                              }
                            }));
                  })
                  .withUnhandledErrorListener((message, e) -> resultHandler.handle(Future.failedFuture(e)))
                  .forPath(basePath);
            } catch (Exception e) {
              resultHandler.handle(Future.failedFuture(e));
            }
          }
        }
    );

  }

  @Override
  public void getRecord(String uuid, Handler<AsyncResult<Record>> handler) {
    Objects.requireNonNull(uuid);
    Context context = Vertx.currentContext();

    ensureConnected(x -> {
      if (x.failed()) {
        handler.handle(Future.failedFuture(x.cause()));
      } else {
        try {
          client.getData()
              .inBackground((fmk, curatorEvent)
                  -> runOnContextIfPossible(context, () -> {
                if (curatorEvent.getResultCode() == KeeperException.Code.OK.intValue()) {
                  JsonObject json
                      = new JsonObject(new String(curatorEvent.getData(), CHARSET));
                  handler.handle(Future.succeededFuture(new Record(json)));
                } else if (curatorEvent.getResultCode() == KeeperException.Code.NONODE.intValue()) {
                  handler.handle(Future.succeededFuture(null));
                } else {
                  KeeperException.Code code = KeeperException.Code.get(curatorEvent.getResultCode());
                  handler.handle(Future.failedFuture(KeeperException.create(code)));
                }
              }))
              .withUnhandledErrorListener((message, e) -> handler.handle(Future.failedFuture(e)))
              .forPath(getPath(uuid));
        } catch (Exception e) {
          handler.handle(Future.failedFuture(e));
        }
      }
    });
  }

  @Override
  public synchronized void stateChanged(CuratorFramework client, ConnectionState newState) {
    connectionState = newState;
  }

  private synchronized void ensureConnected(Handler<AsyncResult<Void>> handler) {
    switch (connectionState) {
      case CONNECTED:
      case RECONNECTED:
        handler.handle(Future.succeededFuture());
        break;
      case READ_ONLY:
        if (canBeReadOnly) {
          handler.handle(Future.succeededFuture());
          break;
        } // Else attempt to reconnect
      case LOST:
      case SUSPENDED:
        vertx.executeBlocking(
            future -> {
              try {
                if (client.blockUntilConnected(connectionTimeoutMs, TimeUnit.MILLISECONDS)) {
                  future.complete();
                } else {
                  future.fail(new TimeoutException());
                }
              } catch (Exception e) {
                future.fail(e);
              }
            }).onComplete(ar -> {
              if (ar.failed()) {
                handler.handle(Future.failedFuture(KeeperException.create(KeeperException.Code.CONNECTIONLOSS)));
              } else {
                handler.handle(Future.succeededFuture());
              }
            });
        break;
    }

  }
}
