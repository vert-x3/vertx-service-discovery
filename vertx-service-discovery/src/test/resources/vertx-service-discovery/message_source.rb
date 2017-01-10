require 'vertx-service-discovery/service_discovery'
require 'vertx/message_consumer'
require 'vertx/util/utils.rb'
# Generated from io.vertx.servicediscovery.types.MessageSource
module VertxServiceDiscovery
  #  Service type for data producer. Providers are publishing data to a specific event bus address.
  class MessageSource
    # @private
    # @param j_del [::VertxServiceDiscovery::MessageSource] the java delegate
    def initialize(j_del, j_arg_X=nil)
      @j_del = j_del
      @j_arg_X = j_arg_X != nil ? j_arg_X : ::Vertx::Util::unknown_type
    end
    # @private
    # @return [::VertxServiceDiscovery::MessageSource] the underlying java delegate
    def j_del
      @j_del
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
