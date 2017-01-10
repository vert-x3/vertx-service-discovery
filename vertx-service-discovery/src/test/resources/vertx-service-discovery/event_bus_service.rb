require 'vertx-service-discovery/service_discovery'
require 'vertx/util/utils.rb'
# Generated from io.vertx.servicediscovery.types.EventBusService
module VertxServiceDiscovery
  #   for event bus services (service proxies).
  #  Consumers receive a service proxy to use the service.
  class EventBusService
    # @private
    # @param j_del [::VertxServiceDiscovery::EventBusService] the java delegate
    def initialize(j_del, j_arg_T=nil)
      @j_del = j_del
      @j_arg_T = j_arg_T != nil ? j_arg_T : ::Vertx::Util::unknown_type
    end
    # @private
    # @return [::VertxServiceDiscovery::EventBusService] the underlying java delegate
    def j_del
      @j_del
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
      if name.class == String && address.class == String && itf.class == String && !block_given? && metadata == nil
        return Java::IoVertxServicediscoveryTypes::EventBusService.java_method(:createRecord, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::java.lang.String.java_class]).call(name,address,itf) != nil ? JSON.parse(Java::IoVertxServicediscoveryTypes::EventBusService.java_method(:createRecord, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::java.lang.String.java_class]).call(name,address,itf).toJson.encode) : nil
      elsif name.class == String && address.class == String && itf.class == String && metadata.class == Hash && !block_given?
        return Java::IoVertxServicediscoveryTypes::EventBusService.java_method(:createRecord, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::IoVertxCoreJson::JsonObject.java_class]).call(name,address,itf,::Vertx::Util::Utils.to_json_object(metadata)) != nil ? JSON.parse(Java::IoVertxServicediscoveryTypes::EventBusService.java_method(:createRecord, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::IoVertxCoreJson::JsonObject.java_class]).call(name,address,itf,::Vertx::Util::Utils.to_json_object(metadata)).toJson.encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling create_record(#{name},#{address},#{itf},#{metadata})"
    end
    #  Lookup for a service record and if found, retrieve it and return the service object (used to consume the service).
    #  This is a convenient method to avoid explicit lookup and then retrieval of the service. This method requires to
    #  have the <code>clientClass</code> set with the expected set of client. This is important for usages not using Java so
    #  you can pass the expected type.
    # @param [::VertxServiceDiscovery::ServiceDiscovery] discovery the service discovery
    # @param [Proc] filter the filter
    # @param [Nil] clientClass the client class
    # @yield the result handler
    # @return [Object] <code>null</code> - do not use
    def self.get_service_proxy(discovery=nil,filter=nil,clientClass=nil)
      if discovery.class.method_defined?(:j_del) && filter.class == Proc && clientClass.class == Class && block_given?
        return ::Vertx::Util::Utils.v_type_of(clientClass).wrap(Java::IoVertxServicediscoveryTypes::EventBusService.java_method(:getServiceProxy, [Java::IoVertxServicediscovery::ServiceDiscovery.java_class,Java::JavaUtilFunction::Function.java_class,Java::JavaLang::Class.java_class,Java::IoVertxCore::Handler.java_class]).call(discovery.j_del,(Proc.new { |event| filter.call(event != nil ? JSON.parse(event.toJson.encode) : nil) }),::Vertx::Util::Utils.j_class_of(clientClass),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.v_type_of(clientClass).wrap(ar.result) : nil) })))
      end
      raise ArgumentError, "Invalid arguments when calling get_service_proxy(#{discovery},#{filter},#{clientClass})"
    end
    #  Lookup for a service record and if found, retrieve it and return the service object (used to consume the service).
    #  This is a convenient method to avoid explicit lookup and then retrieval of the service. This method requires to
    #  have the <code>clientClass</code> set with the expected set of client. This is important for usages not using Java so
    #  you can pass the expected type.
    # @param [::VertxServiceDiscovery::ServiceDiscovery] discovery the service discovery
    # @param [Hash{String => Object}] filter the filter as json object
    # @param [Nil] clientClass the client class
    # @yield the result handler
    # @return [Object] <code>null</code> - do not use
    def self.get_service_proxy_with_json_filter(discovery=nil,filter=nil,clientClass=nil)
      if discovery.class.method_defined?(:j_del) && filter.class == Hash && clientClass.class == Class && block_given?
        return ::Vertx::Util::Utils.v_type_of(clientClass).wrap(Java::IoVertxServicediscoveryTypes::EventBusService.java_method(:getServiceProxyWithJsonFilter, [Java::IoVertxServicediscovery::ServiceDiscovery.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::JavaLang::Class.java_class,Java::IoVertxCore::Handler.java_class]).call(discovery.j_del,::Vertx::Util::Utils.to_json_object(filter),::Vertx::Util::Utils.j_class_of(clientClass),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.v_type_of(clientClass).wrap(ar.result) : nil) })))
      end
      raise ArgumentError, "Invalid arguments when calling get_service_proxy_with_json_filter(#{discovery},#{filter},#{clientClass})"
    end
  end
end
