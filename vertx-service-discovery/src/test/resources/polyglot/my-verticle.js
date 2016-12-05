var ServiceDiscovery = require("vertx-service-discovery-js/service_discovery");
var HttpEndpoint = require("vertx-service-discovery-js/http_endpoint");
var EventBusService = require("vertx-service-discovery-js/event_bus_service");
var MessageSource = require("vertx-service-discovery-js/message_source");
var DataSource = require("vertx-service-discovery-js/jdbc_data_source");
var RedisDataSource = require("vertx-service-discovery-js/redis_data_source");
var HelloService = require("test-services-js/hello_service");


var discovery = ServiceDiscovery.create(vertx);

vertx.eventBus().consumer("http-ref", function (message) {
  discovery.getRecord(function (rec) {
    return rec.name === "my-http-service"
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
        var client = HttpEndpoint.serviceType().getService(reference);

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

vertx.eventBus().consumer("http-sugar", function (message) {
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
        result.client_del = res._jdel.toString();
        message.reply(result);
      }
    }
  });
});

vertx.eventBus().consumer("service-sugar", function (message) {
  EventBusService.getProxy(discovery, function (rec) {
    return rec.name === "my-service"
  }, function (res, err) {
    if (err) {
      message.reply("FAIL - no service");
    } else {
      var result = {};
      if (!res) {
        message.reply("FAIL - client is null");
      } else {
        var proxy = new HelloService(res);
        result.client = "" + proxy;
        result.client_del = "" + proxy._jdel;
        message.reply(result)
      }
    }
  });
});

vertx.eventBus().consumer("service-ref", function (message) {
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
        var client = EventBusService.serviceType().getService(reference);
        var proxy = new HelloService(client);

        if (!client) {
          message.reply("FAIL - client is null");
        } else {
          result.client = "" + proxy;
          result.client_del = "" + proxy._jdel;
          message.reply(result);
          reference.release();
        }
      }
    }
  });
});


vertx.eventBus().consumer("ds-ref", function (message) {
  discovery.getRecord(function (rec) {
    return rec.name === "my-data-source"
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
        var client = DataSource.serviceType().getService(reference);

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

vertx.eventBus().consumer("ds-sugar", function (message) {
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
        result.client_del = res._jdel.toString();
        message.reply(result);
      }
    }
  });
});

vertx.eventBus().consumer("redis-ref", function (message) {
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
        var client = RedisDataSource.serviceType().getService(reference);

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

vertx.eventBus().consumer("redis-sugar", function (message) {
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
        result.client_del = res._jdel.toString();
        message.reply(result);
      }
    }
  });
});

vertx.eventBus().consumer("source1-ref", function (message) {
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
        var client = MessageSource.serviceType().getService(reference);

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

vertx.eventBus().consumer("source1-sugar", function (message) {
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



