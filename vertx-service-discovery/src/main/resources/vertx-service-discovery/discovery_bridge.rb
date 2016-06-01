require 'vertx-service-discovery/discovery_service'
require 'vertx/vertx'
require 'vertx/future'
require 'vertx/util/utils.rb'
# Generated from io.vertx.servicediscovery.spi.DiscoveryBridge
module VertxServiceDiscovery
  #  Discovery bridge allows integrate other discovery technologies with the Vert.x discovery service. It maps entries
  #  from another technology to a  and maps  to a publication in this other technology.
  #  Each bridge can decide which services needs to be imported and exported. It can also implement only on way.
  class DiscoveryBridge
    # @private
    # @param j_del [::VertxServiceDiscovery::DiscoveryBridge] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxServiceDiscovery::DiscoveryBridge] the underlying java delegate
    def j_del
      @j_del
    end
    #  Starts the bridge.
    # @param [::Vertx::Vertx] vertx the vertx instance
    # @param [::VertxServiceDiscovery::DiscoveryService] discovery the discovery service
    # @param [Hash{String => Object}] configuration the bridge configuration if any
    # @param [::Vertx::Future] future a future on which the bridge must report the completion of the starting process
    # @return [void]
    def start(vertx=nil,discovery=nil,configuration=nil,future=nil)
      if vertx.class.method_defined?(:j_del) && discovery.class.method_defined?(:j_del) && configuration.class == Hash && future.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:start, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxServicediscovery::DiscoveryService.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Future.java_class]).call(vertx.j_del,discovery.j_del,::Vertx::Util::Utils.to_json_object(configuration),future.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling start(vertx,discovery,configuration,future)"
    end
    #  Stops the bridge.
    # @param [::Vertx::Vertx] vertx the vertx instance
    # @param [::VertxServiceDiscovery::DiscoveryService] discovery the discovery service
    # @param [::Vertx::Future] future the future on which the bridge must report the completion of the stopping process
    # @return [void]
    def stop(vertx=nil,discovery=nil,future=nil)
      if vertx.class.method_defined?(:j_del) && discovery.class.method_defined?(:j_del) && future.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:stop, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxServicediscovery::DiscoveryService.java_class,Java::IoVertxCore::Future.java_class]).call(vertx.j_del,discovery.j_del,future.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling stop(vertx,discovery,future)"
    end
  end
end
