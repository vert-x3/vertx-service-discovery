require 'vertx/vertx'
require 'vertx/future'
require 'vertx-service-discovery/service_publisher'
require 'vertx/util/utils.rb'
# Generated from io.vertx.servicediscovery.spi.ServiceImporter
module VertxServiceDiscovery
  #  The service importer allows integrate other discovery technologies with the Vert.x service discovery. It maps
  #  entries from another technology to a  and maps  to a publication in this other
  #  technology. The importer is one side of a service discovery bridge.
  class ServiceImporter
    # @private
    # @param j_del [::VertxServiceDiscovery::ServiceImporter] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxServiceDiscovery::ServiceImporter] the underlying java delegate
    def j_del
      @j_del
    end
    #  Starts the importer.
    # @param [::Vertx::Vertx] vertx the vertx instance
    # @param [::VertxServiceDiscovery::ServicePublisher] publisher the service discovery instance
    # @param [Hash{String => Object}] configuration the bridge configuration if any
    # @param [::Vertx::Future] future a future on which the bridge must report the completion of the starting
    # @return [void]
    def start(vertx=nil,publisher=nil,configuration=nil,future=nil)
      if vertx.class.method_defined?(:j_del) && publisher.class.method_defined?(:j_del) && configuration.class == Hash && future.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:start, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxServicediscoverySpi::ServicePublisher.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Future.java_class]).call(vertx.j_del,publisher.j_del,::Vertx::Util::Utils.to_json_object(configuration),future.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling start(vertx,publisher,configuration,future)"
    end
    #  Closes the importer
    # @yield the handle to be notified when importer is closed, may be <code>null</code>
    # @return [void]
    def close
      if block_given?
        return @j_del.java_method(:close, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
      end
      raise ArgumentError, "Invalid arguments when calling close()"
    end
  end
end
