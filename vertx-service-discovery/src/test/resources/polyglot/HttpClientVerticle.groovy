def discovery = io.vertx.servicediscovery.ServiceDiscovery.create(vertx);


vertx.eventBus().consumer("ref", { message ->
  discovery.getRecord(["name": "my-http-service"], { ar ->
    if (ar.failed()) {
      message.reply("FAIL - no http service")
      return
    } else {
      def record = ar.result();
      def reference = discovery.getReference(record);
      if (!reference) {
        message.reply("FAIL - reference is null")
        return
      }
      def client = reference.get();
      if (!client) {
        message.reply("FAIL - service object null")
        return
      } else {
        message.reply("OK - " + client.getClass().getName())
      }
    }
  })
})


