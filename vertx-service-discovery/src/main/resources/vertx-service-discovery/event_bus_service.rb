require 'vertx-service-discovery/service_discovery'
require 'vertx/util/utils.rb'
# Generated from io.vertx.servicediscovery.types.EventBusService
module VertxServiceDiscovery
  #   for event bus services (service proxies).
  #  Consumers receive a service proxy to use the service.
  class EventBusService
    # @private
    # @param j_del [::VertxServiceDiscovery::EventBusService] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxServiceDiscovery::EventBusService] the underlying java delegate
    def j_del
      @j_del
    end
    @@j_api_type = Object.new
    def @@j_api_type.accept?(obj)
      obj.class == EventBusService
    end
    def @@j_api_type.wrap(obj)
      EventBusService.new(obj)
    end
    def @@j_api_type.unwrap(obj)
      obj.j_del
    end
    def self.j_api_type
      @@j_api_type
    end
    def self.j_class
      Java::IoVertxServicediscoveryTypes::EventBusService.java_class
    end
    #  Sugar method to creates a record for this type.
    #  <p>
    #  The java interface is added to the metadata in the `service.interface` key.
    # @param [String] name the name of the service.
    # @param [String] address the event bus address on which the service available
    # @param [String] itf the Java interface (name)
    # @param [Hash{String => Object}] metadata the metadata
    # @return [Hash] the created record
    def self.create_record(name=nil,address=nil,itf=nil,metadata=nil)
      if name.class == String && address.class == String && itf.class == String && metadata.class == Hash && !block_given?
        return Java::IoVertxServicediscoveryTypes::EventBusService.java_method(:createRecord, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::IoVertxCoreJson::JsonObject.java_class]).call(name,address,itf,::Vertx::Util::Utils.to_json_object(metadata)) != nil ? JSON.parse(Java::IoVertxServicediscoveryTypes::EventBusService.java_method(:createRecord, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::IoVertxCoreJson::JsonObject.java_class]).call(name,address,itf,::Vertx::Util::Utils.to_json_object(metadata)).toJson.encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling create_record(#{name},#{address},#{itf},#{metadata})"
    end
    # @overload getProxy(discovery,filter,resultHandler)
    #   @param [::VertxServiceDiscovery::ServiceDiscovery] discovery the service discovery instance
    #   @param [Hash{String => Object}] filter the filter to select the service
    #   @yield the result handler
    # @overload getProxy(discovery,itf,resultHandler)
    #   @param [::VertxServiceDiscovery::ServiceDiscovery] discovery the service discovery instance
    #   @param [String] itf the service interface
    #   @yield the result handler
    # @overload getProxy(discovery,serviceInterface,proxyInterface,resultHandler)
    #   @param [::VertxServiceDiscovery::ServiceDiscovery] discovery 
    #   @param [String] serviceInterface 
    #   @param [String] proxyInterface 
    #   @yield 
    # @overload getProxy(discovery,filter,proxyClass,resultHandler)
    #   @param [::VertxServiceDiscovery::ServiceDiscovery] discovery 
    #   @param [Hash{String => Object}] filter 
    #   @param [String] proxyClass 
    #   @yield 
    # @return [void]
    def self.get_proxy(param_1=nil,param_2=nil,param_3=nil)
      if param_1.class.method_defined?(:j_del) && param_2.class == Hash && block_given? && param_3 == nil
        return Java::IoVertxServicediscoveryTypes::EventBusService.java_method(:getProxy, [Java::IoVertxServicediscovery::ServiceDiscovery.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1.j_del,::Vertx::Util::Utils.to_json_object(param_2),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.from_object(ar.result) : nil) }))
      elsif param_1.class.method_defined?(:j_del) && param_2.class == String && block_given? && param_3 == nil
        return Java::IoVertxServicediscoveryTypes::EventBusService.java_method(:getProxy, [Java::IoVertxServicediscovery::ServiceDiscovery.java_class,Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1.j_del,param_2,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.from_object(ar.result) : nil) }))
      elsif param_1.class.method_defined?(:j_del) && param_2.class == String && param_3.class == String && block_given?
        return Java::IoVertxServicediscoveryTypes::EventBusService.java_method(:getProxy, [Java::IoVertxServicediscovery::ServiceDiscovery.java_class,Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1.j_del,param_2,param_3,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.from_object(ar.result) : nil) }))
      elsif param_1.class.method_defined?(:j_del) && param_2.class == Hash && param_3.class == String && block_given?
        return Java::IoVertxServicediscoveryTypes::EventBusService.java_method(:getProxy, [Java::IoVertxServicediscovery::ServiceDiscovery.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1.j_del,::Vertx::Util::Utils.to_json_object(param_2),param_3,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.from_object(ar.result) : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling get_proxy(#{param_1},#{param_2},#{param_3})"
    end
  end
end
