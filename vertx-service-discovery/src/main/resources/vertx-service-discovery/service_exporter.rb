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
    #  Notify a new record has been published, the record's registration can be used to uniquely
    #  identify the record
    # @param [Hash] record the record
    # @return [void]
    def on_publish(record=nil)
      if record.class == Hash && !block_given?
        return @j_del.java_method(:onPublish, [Java::IoVertxServicediscovery::Record.java_class]).call(Java::IoVertxServicediscovery::Record.new(::Vertx::Util::Utils.to_json_object(record)))
      end
      raise ArgumentError, "Invalid arguments when calling on_publish(record)"
    end
    #  Notify an existing record has been updated, the record's registration can be used to uniquely
    #  identify the record
    # @param [Hash] record the record
    # @return [void]
    def on_update(record=nil)
      if record.class == Hash && !block_given?
        return @j_del.java_method(:onUpdate, [Java::IoVertxServicediscovery::Record.java_class]).call(Java::IoVertxServicediscovery::Record.new(::Vertx::Util::Utils.to_json_object(record)))
      end
      raise ArgumentError, "Invalid arguments when calling on_update(record)"
    end
    #  Notify an existing record has been removed
    # @param [String] id the record registration id
    # @return [void]
    def on_unpublish(id=nil)
      if id.class == String && !block_given?
        return @j_del.java_method(:onUnpublish, [Java::java.lang.String.java_class]).call(id)
      end
      raise ArgumentError, "Invalid arguments when calling on_unpublish(id)"
    end
    #  Close the exporter
    # @yield the handle to be notified when exporter is closed
    # @return [void]
    def close
      if block_given?
        return @j_del.java_method(:close, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
      end
      raise ArgumentError, "Invalid arguments when calling close()"
    end
  end
end
