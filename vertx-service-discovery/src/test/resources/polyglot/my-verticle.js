var ServiceDiscovery = require("vertx-service-discovery-js/service_discovery");
var HttpEndpoint = require("vertx-service-discovery-js/http_endpoint");
var EventBusService = require("vertx-service-discovery-js/event_bus_service");
var MessageSource = require("vertx-service-discovery-js/message_source");
var DataSource = require("vertx-service-discovery-js/jdbc_data_source");
var RedisDataSource = require("vertx-service-discovery-js/redis_data_source");
var MongoDataSource = require("vertx-service-discovery-js/mongo_data_source");
var HelloService = require("test-services-js/hello_service");
var HttpClient = require("vertx-js/http_client");
var MessageConsumer = require("vertx-js/message_consumer");
var RedisClient = require("vertx-redis-js/redis_client");
var MongoClient = require("vertx-mongo-js/mongo_client");
var JDBCClient = require("vertx-jdbc-js/jdbc_client");

function getVertx() {
  return vertx;
}

var discovery = ServiceDiscovery.create(getVertx());

function getDelegate(obj) {
  return "" + obj._jdel;
}

getVertx().eventBus().consumer("http-ref", function (message) {
  discovery.getRecord(function (rec) {
    return rec.name === "my-http-service"
  }, function (rec, err) {
    var result = {};
    if (err) {
      message.reply("FAIL - no http service");
    } else {
      var reference = discovery.getReference(rec);
      result.ref_del = getDelegate(reference);
      if (!reference) {
        message.reply("FAIL - reference is null");
      } else {
        var client = reference.getAs(HttpClient);
        if (!client) {
          message.reply("FAIL - client is null");
        } else {
          result.client_del = getDelegate(client);
          result.cached_del = getDelegate(reference.cachedAs(HttpClient));
          message.reply(result);
          reference.release();
        }
      }
    }
  });
});

getVertx().eventBus().consumer("http-sugar", function (message) {
  HttpEndpoint.getClient(discovery, function (rec) {
    return rec.name === "my-http-service"
  }, function (res, err) {
    if (err) {
      message.reply("FAIL - no http service");
    } else {
      var result = {};
      if (!res) {
        message.reply("FAIL - client is null");
      } else {
        result.client_del = getDelegate(res);
        message.reply(result);
      }
    }
  });
});

getVertx().eventBus().consumer("service-sugar", function (message) {
  EventBusService.getServiceProxy(discovery, function (rec) {
      return rec.name === "my-service"
    },
    HelloService,
    function (res, err) {
      if (err) {
        message.reply("FAIL - no service");
      } else {
        var result = {};
        if (!res) {
          message.reply("FAIL - client is null");
        } else {
          result.client = res.toString();
          result.client_del = getDelegate(res);
          message.reply(result)
        }
      }
    });
});

getVertx().eventBus().consumer("service-ref", function (message) {
  discovery.getRecord(function (rec) {
    return rec.name === "my-service"
  }, function (rec, err) {
    var result = {};
    if (err) {
      message.reply("FAIL - no service");
    } else {
      var reference = discovery.getReference(rec);
      result.ref_del = reference._jdel.toString();
      if (!reference) {
        message.reply("FAIL - reference is null");
      } else {
        // Must create the object.
        var proxy = reference.getAs(HelloService);

        if (!proxy) {
          message.reply("FAIL - client is null");
        } else {
          result.client = proxy.toString();
          result.client_del = getDelegate(proxy);
          result.cached_del = getDelegate(reference.cachedAs(HelloService));
          message.reply(result);
          reference.release();
        }
      }
    }
  });
});


getVertx().eventBus().consumer("ds-ref", function (message) {
  discovery.getRecord(function (rec) {
    return rec.name === "my-data-source"
  }, function (rec, err) {
    var result = {};
    if (err) {
      message.reply("FAIL - no http service");
    } else {
      var reference = discovery.getReference(rec);
      result.ref_del = getDelegate(reference);
      if (!reference) {
        message.reply("FAIL - reference is null");
      } else {
        var client = reference.getAs(JDBCClient);

        if (!client) {
          message.reply("FAIL - client is null");
        } else {
          result.client_del = getDelegate(client);
          message.reply(result);
          reference.release();
        }
      }
    }
  });
});

getVertx().eventBus().consumer("ds-sugar", function (message) {
  DataSource.getJDBCClient(discovery, function (rec) {
    return rec.name === "my-data-source"
  }, function (res, err) {
    if (err) {
      message.reply("FAIL - no service");
    } else {
      var result = {};
      if (!res) {
        message.reply("FAIL - client is null");
      } else {
        result.client_del = getDelegate(res);
        message.reply(result);
      }
    }
  });
});



getVertx().eventBus().consumer("redis-ref", function (message) {
  discovery.getRecord(function (rec) {
    return rec.name === "my-redis-data-source"
  }, function (rec, err) {
    var result = {};
    if (err) {
      message.reply("FAIL - no service");
    } else {
      var reference = discovery.getReference(rec);
      result.ref_del = "" + reference._jdel;
      if (!reference) {
        message.reply("FAIL - reference is null");
      } else {
        var client = reference.getAs(RedisClient);
        if (!client) {
          message.reply("FAIL - client is null");
        } else {
          result.client_del = "" + client._jdel;
          message.reply(result);
          reference.release();
        }
      }
    }
  });
});

getVertx().eventBus().consumer("redis-sugar", function (message) {
  RedisDataSource.getRedisClient(discovery, function (rec) {
    return rec.name === "my-redis-data-source"
  }, function (res, err) {
    if (err) {
      message.reply("FAIL - no service");
    } else {
      var result = {};
      if (!res) {
        message.reply("FAIL - client is null");
      } else {
        result.client_del = getDelegate(res);
        message.reply(result);
      }
    }
  });
});

getVertx().eventBus().consumer("mongo-ref", function (message) {
  discovery.getRecord(function (rec) {
    return rec.name === "my-mongo-data-source"
  }, function (rec, err) {
    var result = {};
    if (err) {
      message.reply("FAIL - no service");
    } else {
      var reference = discovery.getReference(rec);
      result.ref_del = "" + reference._jdel;
      if (!reference) {
        message.reply("FAIL - reference is null");
      } else {
        var client = reference.getAs(MongoClient);
        if (!client) {
          message.reply("FAIL - client is null");
        } else {
          result.client_del = "" + client._jdel;
          message.reply(result);
          reference.release();
        }
      }
    }
  });
});

getVertx().eventBus().consumer("mongo-sugar", function (message) {
  MongoDataSource.getMongoClient(discovery, function (rec) {
    return rec.name === "my-mongo-data-source"
  }, function (res, err) {
    if (err) {
      message.reply("FAIL - no service");
    } else {
      var result = {};
      if (!res) {
        message.reply("FAIL - client is null");
      } else {
        result.client_del = getDelegate(res);
        message.reply(result);
      }
    }
  });
});

getVertx().eventBus().consumer("source1-ref", function (message) {
  discovery.getRecord(function (rec) {
    return rec.name === "my-message-source-1"
  }, function (rec, err) {
    var result = {};
    if (err) {
      message.reply("FAIL - no http service");
    } else {
      var reference = discovery.getReference(rec);
      result.ref_del = "" + reference._jdel;
      if (!reference) {
        message.reply("FAIL - reference is null");
      } else {
        var client = reference.getAs(MessageConsumer);

        if (!client) {
          message.reply("FAIL - client is null");
        } else {
          result.client_del = "" + client._jdel;
          message.reply(result);
          reference.release();
        }
      }
    }
  });
});

getVertx().eventBus().consumer("source1-sugar", function (message) {
  MessageSource.getConsumer(discovery, function (rec) {
    return rec.name === "my-message-source-1"
  }, function (res, err) {
    if (err) {
      message.reply("FAIL - no service");
    } else {
      var result = {};
      if (!res) {
        message.reply("FAIL - client is null");
      } else {
        result.client_del = res._jdel.toString();
        message.reply(result);
      }
    }
  });
});



