require 'vertx-redis/redis_client'
require 'vertx-service-discovery/service_discovery'
require 'vertx/util/utils.rb'
# Generated from io.vertx.servicediscovery.types.RedisDataSource
module VertxServiceDiscovery
  #  Service type for Redis data source.
  class RedisDataSource
    # @private
    # @param j_del [::VertxServiceDiscovery::RedisDataSource] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxServiceDiscovery::RedisDataSource] the underlying java delegate
    def j_del
      @j_del
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
      raise ArgumentError, "Invalid arguments when calling create_record(name,location,metadata)"
    end
    #  Convenient method that looks for a Redis data source and provides the configured {::VertxRedis::RedisClient}.
    #  The async result is marked as failed is there are no matching services, or if the lookup fails.
    # @param [::VertxServiceDiscovery::ServiceDiscovery] discovery The service discovery instance
    # @param [Hash{String => Object}] filter The filter, optional
    # @param [Hash{String => Object}] consumerConfiguration The additional consumer configuration
    # @yield The result handler
    # @return [void]
    def self.get_redis_client(discovery=nil,filter=nil,consumerConfiguration=nil)
      if discovery.class.method_defined?(:j_del) && filter.class == Hash && block_given? && consumerConfiguration == nil
        return Java::IoVertxServicediscoveryTypes::RedisDataSource.java_method(:getRedisClient, [Java::IoVertxServicediscovery::ServiceDiscovery.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Handler.java_class]).call(discovery.j_del,::Vertx::Util::Utils.to_json_object(filter),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxRedis::RedisClient) : nil) }))
      elsif discovery.class.method_defined?(:j_del) && filter.class == Hash && consumerConfiguration.class == Hash && block_given?
        return Java::IoVertxServicediscoveryTypes::RedisDataSource.java_method(:getRedisClient, [Java::IoVertxServicediscovery::ServiceDiscovery.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Handler.java_class]).call(discovery.j_del,::Vertx::Util::Utils.to_json_object(filter),::Vertx::Util::Utils.to_json_object(consumerConfiguration),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxRedis::RedisClient) : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling get_redis_client(discovery,filter,consumerConfiguration)"
    end
  end
end
