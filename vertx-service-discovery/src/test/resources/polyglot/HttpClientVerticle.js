var ServiceDiscovery = require("vertx-service-discovery-js/service_discovery");
var discovery = ServiceDiscovery.create(vertx);


vertx.eventBus().consumer("ref", function (message) {
  discovery.getRecord(function(rec) { return rec.name === "my-http-service"}, function(rec, err) {
    if (err) {
      message.reply("FAIL - no http service");
    } else {
      var reference = discovery.getReference(rec);
      if (! reference) {
        message.reply("FAIL - reference is null");
      } else {
        var client = reference.get();
        if (! client) {
          message.reply("FAIL - client is null");
        } else {
          message.reply("OK - " + client)
        }
      }
    }
  });
});

