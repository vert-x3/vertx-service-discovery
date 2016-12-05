require 'vertx-service-discovery/service_reference'
require 'vertx/message_consumer'
require 'vertx/util/utils.rb'
# Generated from io.vertx.servicediscovery.types.MessageSourceType
module VertxServiceDiscovery
  #  TODO
  class MessageSourceType
    # @private
    # @param j_del [::VertxServiceDiscovery::MessageSourceType] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxServiceDiscovery::MessageSourceType] the underlying java delegate
    def j_del
      @j_del
    end
    @@j_api_type = Object.new
    def @@j_api_type.accept?(obj)
      obj.class == MessageSourceType
    end
    def @@j_api_type.wrap(obj)
      MessageSourceType.new(obj)
    end
    def @@j_api_type.unwrap(obj)
      obj.j_del
    end
    def self.j_api_type
      @@j_api_type
    end
    def self.j_class
      Java::IoVertxServicediscoveryTypes::MessageSourceType.java_class
    end
    # @param [::VertxServiceDiscovery::ServiceReference] ref 
    # @return [::Vertx::MessageConsumer]
    def get_service(ref=nil)
      if ref.class.method_defined?(:j_del) && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getService, [Java::IoVertxServicediscovery::ServiceReference.java_class]).call(ref.j_del),::Vertx::MessageConsumer)
      end
      raise ArgumentError, "Invalid arguments when calling get_service(#{ref})"
    end
    # @param [::VertxServiceDiscovery::ServiceReference] ref 
    # @return [::Vertx::MessageConsumer]
    def cached_service(ref=nil)
      if ref.class.method_defined?(:j_del) && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:cachedService, [Java::IoVertxServicediscovery::ServiceReference.java_class]).call(ref.j_del),::Vertx::MessageConsumer)
      end
      raise ArgumentError, "Invalid arguments when calling cached_service(#{ref})"
    end
  end
end
