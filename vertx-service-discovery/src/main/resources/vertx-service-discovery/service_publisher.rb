require 'vertx/util/utils.rb'
# Generated from io.vertx.servicediscovery.spi.ServicePublisher
module VertxServiceDiscovery
  #  The publisher is used by the importer to publish or unpublish records.
  class ServicePublisher
    # @private
    # @param j_del [::VertxServiceDiscovery::ServicePublisher] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxServiceDiscovery::ServicePublisher] the underlying java delegate
    def j_del
      @j_del
    end
    @@j_api_type = Object.new
    def @@j_api_type.accept?(obj)
      obj.class == ServicePublisher
    end
    def @@j_api_type.wrap(obj)
      ServicePublisher.new(obj)
    end
    def @@j_api_type.unwrap(obj)
      obj.j_del
    end
    def self.j_api_type
      @@j_api_type
    end
    def self.j_class
      Java::IoVertxServicediscoverySpi::ServicePublisher.java_class
    end
    #  Publishes a record.
    # @param [Hash] record the record
    # @yield handler called when the operation has completed (successfully or not). In case of success, the passed record has a registration id required to modify and un-register the service.
    # @return [void]
    def publish(record=nil)
      if record.class == Hash && block_given?
        return @j_del.java_method(:publish, [Java::IoVertxServicediscovery::Record.java_class,Java::IoVertxCore::Handler.java_class]).call(Java::IoVertxServicediscovery::Record.new(::Vertx::Util::Utils.to_json_object(record)),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result != nil ? JSON.parse(ar.result.toJson.encode) : nil : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling publish(#{record})"
    end
    #  Un-publishes a record.
    # @param [String] id the registration id
    # @yield handler called when the operation has completed (successfully or not).
    # @return [void]
    def unpublish(id=nil)
      if id.class == String && block_given?
        return @j_del.java_method(:unpublish, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(id,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling unpublish(#{id})"
    end
  end
end
