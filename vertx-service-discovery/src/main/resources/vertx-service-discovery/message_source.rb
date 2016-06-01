require 'vertx-service-discovery/discovery_service'
require 'vertx/message_consumer'
require 'vertx/util/utils.rb'
# Generated from io.vertx.servicediscovery.types.MessageSource
module VertxServiceDiscovery
  #  Service type for data producer. Providers are publishing data to a specific event bus address.
  class MessageSource
    # @private
    # @param j_del [::VertxServiceDiscovery::MessageSource] the java delegate
    def initialize(j_del)
      @j_del = j_del
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
      raise ArgumentError, "Invalid arguments when calling create_record(name,address,type,metadata)"
    end
    #  Convenient method that looks for a message source and provides the configured . The
    #  async result is marked as failed is there are no matching services, or if the lookup fails.
    # @param [::VertxServiceDiscovery::DiscoveryService] discovery The discovery service
    # @param [Hash{String => Object}] filter The filter, optional
    # @yield the result handler
    # @return [void]
    def self.get_consumer(discovery=nil,filter=nil)
      if discovery.class.method_defined?(:j_del) && filter.class == Hash && block_given?
        return Java::IoVertxServicediscoveryTypes::MessageSource.java_method(:getConsumer, [Java::IoVertxServicediscovery::DiscoveryService.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Handler.java_class]).call(discovery.j_del,::Vertx::Util::Utils.to_json_object(filter),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::Vertx::MessageConsumer) : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling get_consumer(discovery,filter)"
    end
  end
end
