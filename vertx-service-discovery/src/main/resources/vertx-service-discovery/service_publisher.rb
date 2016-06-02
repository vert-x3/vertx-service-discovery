require 'vertx/util/utils.rb'
# Generated from io.vertx.servicediscovery.spi.ServicePublisher
module VertxServiceDiscovery
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
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
    #  Publishes a record.
    # @param [Hash] record the record
    # @yield handler called when the operation has completed (successfully or not). In case of success, the passed record has a registration id required to modify and un-register the service.
    # @return [void]
    def publish(record=nil)
      if record.class == Hash && block_given?
        return @j_del.java_method(:publish, [Java::IoVertxServicediscovery::Record.java_class,Java::IoVertxCore::Handler.java_class]).call(Java::IoVertxServicediscovery::Record.new(::Vertx::Util::Utils.to_json_object(record)),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result != nil ? JSON.parse(ar.result.toJson.encode) : nil : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling publish(record)"
    end
    #  Un-publishes a record.
    # @param [String] id the registration id
    # @yield handler called when the operation has completed (successfully or not).
    # @return [void]
    def unpublish(id=nil)
      if id.class == String && block_given?
        return @j_del.java_method(:unpublish, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(id,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling unpublish(id)"
    end
  end
end
