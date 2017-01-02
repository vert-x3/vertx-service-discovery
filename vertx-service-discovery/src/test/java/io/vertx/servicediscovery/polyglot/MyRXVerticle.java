package io.vertx.servicediscovery.polyglot;

import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.rxjava.core.eventbus.MessageConsumer;
import io.vertx.rxjava.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.jdbc.JDBCClient;
import io.vertx.rxjava.redis.RedisClient;
import io.vertx.rxjava.servicediscovery.ServiceDiscovery;
import io.vertx.rxjava.servicediscovery.ServiceReference;
import io.vertx.rxjava.servicediscovery.service.HelloService;
import io.vertx.rxjava.servicediscovery.types.*;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class MyRXVerticle extends AbstractVerticle {


  @Override
  public void start() throws Exception {
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx);
    EventBus eb = vertx.eventBus();

    eb.consumer("http-ref", message ->
      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("my-http-service"), ar -> {
        if (ar.failed()) {
          message.reply("FAIL - No service");
        } else {
          JsonObject result = new JsonObject();
          ServiceReference reference = discovery.getReference(ar.result());
          if (reference == null) {
            message.reply("FAIL - reference is null");
          } else {
            HttpClient client = HttpEndpoint.serviceType().getService(reference);
            result.put("client", client.toString());
            //System.out.println(reference.get());
            //result.put("direct", reference.get().getDelegate().toString());
            message.reply(result);
          }
        }
      }));

    eb.consumer("http-sugar", message -> {
      JsonObject result = new JsonObject();
      HttpEndpoint.getClient(discovery, record -> record.getName().equalsIgnoreCase("my-http-service"),
        ar -> {
          if (ar.failed()) {
            message.reply("FAIL - no service");
          } else {
            HttpClient client = ar.result();
            result.put("client", client.toString());
            message.reply(result);
          }
        });
    });

    eb.consumer("service-ref", message ->
      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("my-service"), ar -> {
        if (ar.failed()) {
          message.reply("FAIL - No service");
        } else {
          JsonObject result = new JsonObject();
          ServiceReference reference = discovery.getReference(ar.result());
          if (reference == null) {
            message.reply("FAIL - reference is null");
          } else {
            HelloService client = EventBusService.serviceType().getObject(reference, HelloService.class);
//            HelloService direct = reference.get();
            //HelloService client = reference.(HelloService.class);
            result.put("client", client.toString());
//            result.put("direct", direct.toString());
            message.reply(result);
          }
        }
      }));

    eb.consumer("service-sugar", message -> {
      JsonObject result = new JsonObject();
      EventBusService.getProxy(discovery, record -> record.getName().equalsIgnoreCase("my-service"),
        ar -> {
          if (ar.failed()) {
            message.reply("FAIL - no service");
          } else {
            HelloService client = new HelloService((io.vertx.servicediscovery.service.HelloService) ar.result());
            result.put("client", client.toString());
            message.reply(result);
          }
        });
    });

    eb.consumer("ds-ref", message ->
      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("my-data-source"), ar -> {
        if (ar.failed()) {
          message.reply("FAIL - No service");
        } else {
          JsonObject result = new JsonObject();
          ServiceReference<JDBCClient> reference = discovery.getReference(ar.result());
          if (reference == null) {
            message.reply("FAIL - reference is null");
          } else {
            JDBCClient client = JDBCDataSource.serviceType().getService(reference);
            result.put("client", client.toString());
            result.put("direct", reference.get().toString());
            message.reply(result);
          }
        }
      }));

    eb.consumer("ds-sugar", message -> {
      JsonObject result = new JsonObject();
      JDBCDataSource.getJDBCClient(discovery, record -> record.getName().equalsIgnoreCase("my-data-source"),
        ar -> {
          if (ar.failed()) {
            message.reply("FAIL - no service");
          } else {
            JDBCClient client = ar.result();
            result.put("client", client.toString());
            message.reply(result);
          }
        });
    });

    eb.consumer("redis-ref", message ->
      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("my-redis-data-source"), ar -> {
        if (ar.failed()) {
          message.reply("FAIL - No service");
        } else {
          JsonObject result = new JsonObject();
          ServiceReference<RedisClient> reference = discovery.getReference(ar.result());
          if (reference == null) {
            message.reply("FAIL - reference is null");
          } else {
            RedisClient client = RedisDataSource.serviceType().getService(reference);
            result.put("client", client.toString());
            result.put("direct", reference.get().toString());
            message.reply(result);
          }
        }
      }));

    eb.consumer("redis-sugar", message -> {
      JsonObject result = new JsonObject();
      RedisDataSource.getRedisClient(discovery, record -> record.getName().equalsIgnoreCase("my-redis-data-source"),
        ar -> {
          if (ar.failed()) {
            message.reply("FAIL - no service");
          } else {
            RedisClient client = ar.result();
            result.put("client", client.toString());
            message.reply(result);
          }
        });
    });

    eb.consumer("source1-ref", message ->
      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("my-message-source-1"), ar -> {
        if (ar.failed()) {
          message.reply("FAIL - No service");
        } else {
          JsonObject result = new JsonObject();
          ServiceReference<MessageConsumer> reference = discovery.getReference(ar.result());
          if (reference == null) {
            message.reply("FAIL - reference is null");
          } else {
            MessageConsumer client = MessageSource.serviceType().getService(reference);
            result.put("client", client.toString());
            result.put("direct", reference.get().toString());
            message.reply(result);
          }
        }
      }));

    eb.consumer("source1-sugar", message -> {
      JsonObject result = new JsonObject();
      MessageSource.getConsumer(discovery, record -> record.getName().equalsIgnoreCase("my-message-source-1"),
        ar -> {
          if (ar.failed()) {
            message.reply("FAIL - no service");
          } else {
            MessageConsumer client = ar.result();
            result.put("client", client.toString());
            message.reply(result);
          }
        });
    });


  }
}
