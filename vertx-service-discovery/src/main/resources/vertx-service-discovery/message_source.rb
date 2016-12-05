require 'vertx-service-discovery/service_reference'
require 'vertx-service-discovery/message_source_type'
require 'vertx-service-discovery/service_discovery'
require 'vertx/message_consumer'
require 'vertx/util/utils.rb'
# Generated from io.vertx.servicediscovery.types.MessageSource
module VertxServiceDiscovery
  #  Service type for data producer. Providers are publishing data to a specific event bus address.
  class MessageSource < ::VertxServiceDiscovery::MessageSourceType
    # @private
    # @param j_del [::VertxServiceDiscovery::MessageSource] the java delegate
    def initialize(j_del)
      super(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxServiceDiscovery::MessageSource] the underlying java delegate
    def j_del
      @j_del
    end
    @@j_api_type = Object.new
    def @@j_api_type.accept?(obj)
      obj.class == MessageSource
    end
    def @@j_api_type.wrap(obj)
      MessageSource.new(obj)
    end
    def @@j_api_type.unwrap(obj)
      obj.j_del
    end
    def self.j_api_type
      @@j_api_type
    end
    def self.j_class
      Java::IoVertxServicediscoveryTypes::MessageSource.java_class
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
    # @return [::VertxServiceDiscovery::MessageSourceType]
    def self.service_type
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxServicediscoveryTypes::MessageSource.java_method(:serviceType, []).call(),::VertxServiceDiscovery::MessageSourceType)
      end
      raise ArgumentError, "Invalid arguments when calling service_type()"
    end
    #  Create a record representing a data producer.
    # @param [String] name the name of the service
    # @param [String] address the address on which the data is sent
    # @param [String] type the type of payload (fully qualified name of the class)
    # @param [Hash{String => Object}] metadata additional metadata
    # @return [Hash] the created record
    def self.create_record(name=nil,address=nil,type=nil,metadata=nil)
      if name.class == String && address.class == String && !block_given? && type == nil && metadata == nil
        return Java::IoVertxServicediscoveryTypes::MessageSource.java_method(:createRecord, [Java::java.lang.String.java_class,Java::java.lang.String.java_class]).call(name,address) != nil ? JSON.parse(Java::IoVertxServicediscoveryTypes::MessageSource.java_method(:createRecord, [Java::java.lang.String.java_class,Java::java.lang.String.java_class]).call(name,address).toJson.encode) : nil
      elsif name.class == String && address.class == String && type.class == String && !block_given? && metadata == nil
        return Java::IoVertxServicediscoveryTypes::MessageSource.java_method(:createRecord, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::java.lang.String.java_class]).call(name,address,type) != nil ? JSON.parse(Java::IoVertxServicediscoveryTypes::MessageSource.java_method(:createRecord, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::java.lang.String.java_class]).call(name,address,type).toJson.encode) : nil
      elsif name.class == String && address.class == String && type.class == String && metadata.class == Hash && !block_given?
        return Java::IoVertxServicediscoveryTypes::MessageSource.java_method(:createRecord, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::IoVertxCoreJson::JsonObject.java_class]).call(name,address,type,::Vertx::Util::Utils.to_json_object(metadata)) != nil ? JSON.parse(Java::IoVertxServicediscoveryTypes::MessageSource.java_method(:createRecord, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::IoVertxCoreJson::JsonObject.java_class]).call(name,address,type,::Vertx::Util::Utils.to_json_object(metadata)).toJson.encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling create_record(#{name},#{address},#{type},#{metadata})"
    end
    #  Convenient method that looks for a message source and provides the configured . The
    #  async result is marked as failed is there are no matching services, or if the lookup fails.
    # @overload getConsumer(discovery,filter,resultHandler)
    #   @param [::VertxServiceDiscovery::ServiceDiscovery] discovery The service discovery instance
    #   @param [Hash{String => Object}] filter The filter, optional
    #   @yield The result handler
    # @overload getConsumer(discovery,filter,resultHandler)
    #   @param [::VertxServiceDiscovery::ServiceDiscovery] discovery The service discovery instance
    #   @param [Proc] filter The filter, must not be <code>null</code>
    #   @yield The result handler
    # @return [void]
    def self.get_consumer(param_1=nil,param_2=nil)
      if param_1.class.method_defined?(:j_del) && param_2.class == Hash && block_given?
        return Java::IoVertxServicediscoveryTypes::MessageSource.java_method(:getConsumer, [Java::IoVertxServicediscovery::ServiceDiscovery.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1.j_del,::Vertx::Util::Utils.to_json_object(param_2),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::Vertx::MessageConsumer, nil) : nil) }))
      elsif param_1.class.method_defined?(:j_del) && param_2.class == Proc && block_given?
        return Java::IoVertxServicediscoveryTypes::MessageSource.java_method(:getConsumer, [Java::IoVertxServicediscovery::ServiceDiscovery.java_class,Java::JavaUtilFunction::Function.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1.j_del,(Proc.new { |event| param_2.call(event != nil ? JSON.parse(event.toJson.encode) : nil) }),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::Vertx::MessageConsumer, nil) : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling get_consumer(#{param_1},#{param_2})"
    end
  end
end
