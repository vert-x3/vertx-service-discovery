require 'vertx-service-discovery/discovery_service'
require 'vertx/vertx'
require 'vertx-service-discovery/service_reference'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.discovery.types.EventBusService
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
        return Java::IoVertxExtDiscoveryTypes::EventBusService.java_method(:createRecord, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::IoVertxCoreJson::JsonObject.java_class]).call(name,address,itf,::Vertx::Util::Utils.to_json_object(metadata)) != nil ? JSON.parse(Java::IoVertxExtDiscoveryTypes::EventBusService.java_method(:createRecord, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::IoVertxCoreJson::JsonObject.java_class]).call(name,address,itf,::Vertx::Util::Utils.to_json_object(metadata)).toJson.encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling create_record(name,address,itf,metadata)"
    end
    #  Retrieves the bindings - for testing purpose only.
    # @return [Set<::VertxServiceDiscovery::ServiceReference>] a copy of the bindings.
    def self.bindings
      if !block_given?
        return ::Vertx::Util::Utils.to_set(Java::IoVertxExtDiscoveryTypes::EventBusService.java_method(:bindings, []).call()).map! { |elt| ::Vertx::Util::Utils.safe_create(elt,::VertxServiceDiscovery::ServiceReference) }
      end
      raise ArgumentError, "Invalid arguments when calling bindings()"
    end
    #  Lookup for a service record and if found, retrieve it and return the service object (used to consume the service).
    #  This is a convenient method to avoid explicit lookup and then retrieval of the service. A filter based on the
    #  request interface is used.
    # @param [::Vertx::Vertx] vertx the vert.x instance
    # @param [::VertxServiceDiscovery::DiscoveryService] discovery the discovery service
    # @param [String] itf the service interface
    # @yield the result handler
    # @return [void]
    def self.get(vertx=nil,discovery=nil,itf=nil)
      if vertx.class.method_defined?(:j_del) && discovery.class.method_defined?(:j_del) && itf.class == String && block_given?
        return Java::IoVertxExtDiscoveryTypes::EventBusService.java_method(:get, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxExtDiscovery::DiscoveryService.java_class,Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(vertx.j_del,discovery.j_del,itf,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.from_object(ar.result) : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling get(vertx,discovery,itf)"
    end
    #  Convenient method to release a used service object.
    # @param [Object] svcObject the service object
    # @return [void]
    def self.release(svcObject=nil)
      if (svcObject.class == String  || svcObject.class == Hash || svcObject.class == Array || svcObject.class == NilClass || svcObject.class == TrueClass || svcObject.class == FalseClass || svcObject.class == Fixnum || svcObject.class == Float) && !block_given?
        return Java::IoVertxExtDiscoveryTypes::EventBusService.java_method(:release, [Java::java.lang.Object.java_class]).call(::Vertx::Util::Utils.to_object(svcObject))
      end
      raise ArgumentError, "Invalid arguments when calling release(svcObject)"
    end
  end
end
