require 'vertx-service-discovery/service_reference'
require 'vertx-redis/redis_client'
require 'vertx-service-discovery/service_discovery'
require 'vertx-service-discovery/redis_data_source_type'
require 'vertx/util/utils.rb'
# Generated from io.vertx.servicediscovery.types.RedisDataSource
module VertxServiceDiscovery
  #  Service type for Redis data source.
  class RedisDataSource < ::VertxServiceDiscovery::RedisDataSourceType
    # @private
    # @param j_del [::VertxServiceDiscovery::RedisDataSource] the java delegate
    def initialize(j_del)
      super(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxServiceDiscovery::RedisDataSource] the underlying java delegate
    def j_del
      @j_del
    end
    @@j_api_type = Object.new
    def @@j_api_type.accept?(obj)
      obj.class == RedisDataSource
    end
    def @@j_api_type.wrap(obj)
      RedisDataSource.new(obj)
    end
    def @@j_api_type.unwrap(obj)
      obj.j_del
    end
    def self.j_api_type
      @@j_api_type
    end
    def self.j_class
      Java::IoVertxServicediscoveryTypes::RedisDataSource.java_class
    end
    # @param [::VertxServiceDiscovery::ServiceReference] ref 
    # @return [::VertxRedis::RedisClient]
    def get_service(ref=nil)
      if ref.class.method_defined?(:j_del) && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getService, [Java::IoVertxServicediscovery::ServiceReference.java_class]).call(ref.j_del),::VertxRedis::RedisClient)
      end
      raise ArgumentError, "Invalid arguments when calling get_service(#{ref})"
    end
    # @param [::VertxServiceDiscovery::ServiceReference] ref 
    # @return [::VertxRedis::RedisClient]
    def cached_service(ref=nil)
      if ref.class.method_defined?(:j_del) && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:cachedService, [Java::IoVertxServicediscovery::ServiceReference.java_class]).call(ref.j_del),::VertxRedis::RedisClient)
      end
      raise ArgumentError, "Invalid arguments when calling cached_service(#{ref})"
    end
    # @return [::VertxServiceDiscovery::RedisDataSourceType]
    def self.service_type
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxServicediscoveryTypes::RedisDataSource.java_method(:serviceType, []).call(),::VertxServiceDiscovery::RedisDataSourceType)
      end
      raise ArgumentError, "Invalid arguments when calling service_type()"
    end
    #  Convenient method to create a record for a Redis data source.
    # @param [String] name the service name
    # @param [Hash{String => Object}] location the location of the service (e.g. url, port...)
    # @param [Hash{String => Object}] metadata additional metadata
    # @return [Hash] the created record
    def self.create_record(name=nil,location=nil,metadata=nil)
      if name.class == String && location.class == Hash && metadata.class == Hash && !block_given?
        return Java::IoVertxServicediscoveryTypes::RedisDataSource.java_method(:createRecord, [Java::java.lang.String.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCoreJson::JsonObject.java_class]).call(name,::Vertx::Util::Utils.to_json_object(location),::Vertx::Util::Utils.to_json_object(metadata)) != nil ? JSON.parse(Java::IoVertxServicediscoveryTypes::RedisDataSource.java_method(:createRecord, [Java::java.lang.String.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCoreJson::JsonObject.java_class]).call(name,::Vertx::Util::Utils.to_json_object(location),::Vertx::Util::Utils.to_json_object(metadata)).toJson.encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling create_record(#{name},#{location},#{metadata})"
    end
    #  Convenient method that looks for a Redis data source and provides the configured {::VertxRedis::RedisClient}.
    #  The async result is marked as failed is there are no matching services, or if the lookup fails.
    # @overload getRedisClient(discovery,filter,resultHandler)
    #   @param [::VertxServiceDiscovery::ServiceDiscovery] discovery The service discovery instance
    #   @param [Hash{String => Object}] filter The filter, optional
    #   @yield The result handler
    # @overload getRedisClient(discovery,filter,resultHandler)
    #   @param [::VertxServiceDiscovery::ServiceDiscovery] discovery The service discovery instance
    #   @param [Proc] filter The filter, cannot be <code>null</code>
    #   @yield The result handler
    # @overload getRedisClient(discovery,filter,consumerConfiguration,resultHandler)
    #   @param [::VertxServiceDiscovery::ServiceDiscovery] discovery The service discovery instance
    #   @param [Hash{String => Object}] filter The filter, optional
    #   @param [Hash{String => Object}] consumerConfiguration The additional consumer configuration
    #   @yield The result handler
    # @overload getRedisClient(discovery,filter,consumerConfiguration,resultHandler)
    #   @param [::VertxServiceDiscovery::ServiceDiscovery] discovery The service discovery instance
    #   @param [Proc] filter The filter, cannot be <code>null</code>
    #   @param [Hash{String => Object}] consumerConfiguration The additional consumer configuration
    #   @yield The result handler
    # @return [void]
    def self.get_redis_client(param_1=nil,param_2=nil,param_3=nil)
      if param_1.class.method_defined?(:j_del) && param_2.class == Hash && block_given? && param_3 == nil
        return Java::IoVertxServicediscoveryTypes::RedisDataSource.java_method(:getRedisClient, [Java::IoVertxServicediscovery::ServiceDiscovery.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1.j_del,::Vertx::Util::Utils.to_json_object(param_2),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxRedis::RedisClient) : nil) }))
      elsif param_1.class.method_defined?(:j_del) && param_2.class == Proc && block_given? && param_3 == nil
        return Java::IoVertxServicediscoveryTypes::RedisDataSource.java_method(:getRedisClient, [Java::IoVertxServicediscovery::ServiceDiscovery.java_class,Java::JavaUtilFunction::Function.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1.j_del,(Proc.new { |event| param_2.call(event != nil ? JSON.parse(event.toJson.encode) : nil) }),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxRedis::RedisClient) : nil) }))
      elsif param_1.class.method_defined?(:j_del) && param_2.class == Hash && param_3.class == Hash && block_given?
        return Java::IoVertxServicediscoveryTypes::RedisDataSource.java_method(:getRedisClient, [Java::IoVertxServicediscovery::ServiceDiscovery.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1.j_del,::Vertx::Util::Utils.to_json_object(param_2),::Vertx::Util::Utils.to_json_object(param_3),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxRedis::RedisClient) : nil) }))
      elsif param_1.class.method_defined?(:j_del) && param_2.class == Proc && param_3.class == Hash && block_given?
        return Java::IoVertxServicediscoveryTypes::RedisDataSource.java_method(:getRedisClient, [Java::IoVertxServicediscovery::ServiceDiscovery.java_class,Java::JavaUtilFunction::Function.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1.j_del,(Proc.new { |event| param_2.call(event != nil ? JSON.parse(event.toJson.encode) : nil) }),::Vertx::Util::Utils.to_json_object(param_3),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxRedis::RedisClient) : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling get_redis_client(#{param_1},#{param_2},#{param_3})"
    end
  end
end
