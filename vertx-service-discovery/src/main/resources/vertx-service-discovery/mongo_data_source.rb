require 'vertx-mongo/mongo_client'
require 'vertx-service-discovery/service_discovery'
require 'vertx/util/utils.rb'
# Generated from io.vertx.servicediscovery.types.MongoDataSource
module VertxServiceDiscovery
  class MongoDataSource
    # @private
    # @param j_del [::VertxServiceDiscovery::MongoDataSource] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxServiceDiscovery::MongoDataSource] the underlying java delegate
    def j_del
      @j_del
    end
    @@j_api_type = Object.new
    def @@j_api_type.accept?(obj)
      obj.class == MongoDataSource
    end
    def @@j_api_type.wrap(obj)
      MongoDataSource.new(obj)
    end
    def @@j_api_type.unwrap(obj)
      obj.j_del
    end
    def self.j_api_type
      @@j_api_type
    end
    def self.j_class
      Java::IoVertxServicediscoveryTypes::MongoDataSource.java_class
    end
    #  Convenient method to create a record for a Mongo data source.
    # @param [String] name the service name
    # @param [Hash{String => Object}] location the location of the service (e.g. url, port...)
    # @param [Hash{String => Object}] metadata additional metadata
    # @return [Hash] the created record
    def self.create_record(name=nil,location=nil,metadata=nil)
      if name.class == String && location.class == Hash && metadata.class == Hash && !block_given?
        return Java::IoVertxServicediscoveryTypes::MongoDataSource.java_method(:createRecord, [Java::java.lang.String.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCoreJson::JsonObject.java_class]).call(name,::Vertx::Util::Utils.to_json_object(location),::Vertx::Util::Utils.to_json_object(metadata)) != nil ? JSON.parse(Java::IoVertxServicediscoveryTypes::MongoDataSource.java_method(:createRecord, [Java::java.lang.String.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCoreJson::JsonObject.java_class]).call(name,::Vertx::Util::Utils.to_json_object(location),::Vertx::Util::Utils.to_json_object(metadata)).toJson.encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling create_record(#{name},#{location},#{metadata})"
    end
    #  Convenient method that looks for a Mongo datasource source and provides the configured {::VertxMongo::MongoClient}. The
    #  async result is marked as failed is there are no matching services, or if the lookup fails.
    # @overload getMongoClient(discovery,filter,resultHandler)
    #   @param [::VertxServiceDiscovery::ServiceDiscovery] discovery The service discovery instance
    #   @param [Hash{String => Object}] filter The filter, optional
    #   @yield The result handler
    # @overload getMongoClient(discovery,filter,resultHandler)
    #   @param [::VertxServiceDiscovery::ServiceDiscovery] discovery The service discovery instance
    #   @param [Proc] filter The filter
    #   @yield The result handler
    # @overload getMongoClient(discovery,filter,consumerConfiguration,resultHandler)
    #   @param [::VertxServiceDiscovery::ServiceDiscovery] discovery The service discovery instance
    #   @param [Hash{String => Object}] filter The filter, optional
    #   @param [Hash{String => Object}] consumerConfiguration the consumer configuration
    #   @yield the result handler
    # @return [void]
    def self.get_mongo_client(param_1=nil,param_2=nil,param_3=nil)
      if param_1.class.method_defined?(:j_del) && param_2.class == Hash && block_given? && param_3 == nil
        return Java::IoVertxServicediscoveryTypes::MongoDataSource.java_method(:getMongoClient, [Java::IoVertxServicediscovery::ServiceDiscovery.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1.j_del,::Vertx::Util::Utils.to_json_object(param_2),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxMongo::MongoClient) : nil) }))
      elsif param_1.class.method_defined?(:j_del) && param_2.class == Proc && block_given? && param_3 == nil
        return Java::IoVertxServicediscoveryTypes::MongoDataSource.java_method(:getMongoClient, [Java::IoVertxServicediscovery::ServiceDiscovery.java_class,Java::JavaUtilFunction::Function.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1.j_del,(Proc.new { |event| param_2.call(event != nil ? JSON.parse(event.toJson.encode) : nil) }),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxMongo::MongoClient) : nil) }))
      elsif param_1.class.method_defined?(:j_del) && param_2.class == Hash && param_3.class == Hash && block_given?
        return Java::IoVertxServicediscoveryTypes::MongoDataSource.java_method(:getMongoClient, [Java::IoVertxServicediscovery::ServiceDiscovery.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1.j_del,::Vertx::Util::Utils.to_json_object(param_2),::Vertx::Util::Utils.to_json_object(param_3),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxMongo::MongoClient) : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling get_mongo_client(#{param_1},#{param_2},#{param_3})"
    end
  end
end
