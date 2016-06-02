require 'vertx/util/utils.rb'
# Generated from io.vertx.servicediscovery.spi.ServiceExporter
module VertxServiceDiscovery
  #  The service exporter allows integrate other discovery technologies with the Vert.x service discovery. It maps
  #  entries from another technology to a  and maps  to a publication in this other
  #  technology. The exporter is one side of a service discovery bridge.
  class ServiceExporter
    # @private
    # @param j_del [::VertxServiceDiscovery::ServiceExporter] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxServiceDiscovery::ServiceExporter] the underlying java delegate
    def j_del
      @j_del
    end
    # @return [void]
    def on_publication
      if !block_given?
        return @j_del.java_method(:onPublication, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling on_publication()"
    end
    # @param [Array<Hash>] records 
    # @return [void]
    def init(records=nil)
      if records.class == Array && !block_given?
        return @j_del.java_method(:init, [Java::JavaUtil::List.java_class]).call(records.map { |element| Java::IoVertxServicediscovery::Record.new(::Vertx::Util::Utils.to_json_object(element)) })
      end
      raise ArgumentError, "Invalid arguments when calling init(records)"
    end
    # @return [void]
    def close
      if !block_given?
        return @j_del.java_method(:close, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling close()"
    end
  end
end
