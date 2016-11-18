require 'vertx-jdbc/jdbc_client'
require 'vertx-service-discovery/service_discovery'
require 'vertx/util/utils.rb'
# Generated from io.vertx.servicediscovery.types.JDBCDataSource
module VertxServiceDiscovery
  class JDBCDataSource
    # @private
    # @param j_del [::VertxServiceDiscovery::JDBCDataSource] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxServiceDiscovery::JDBCDataSource] the underlying java delegate
    def j_del
      @j_del
    end
    @@j_api_type = Object.new
    def @@j_api_type.accept?(obj)
      obj.class == JDBCDataSource
    end
    def @@j_api_type.wrap(obj)
      JDBCDataSource.new(obj)
    end
    def @@j_api_type.unwrap(obj)
      obj.j_del
    end
    def self.j_api_type
      @@j_api_type
    end
    def self.j_class
      Java::IoVertxServicediscoveryTypes::JDBCDataSource.java_class
    end
    # @param [String] name 
    # @param [Hash{String => Object}] location 
    # @param [Hash{String => Object}] metadata 
    # @return [Hash]
    def self.create_record(name=nil,location=nil,metadata=nil)
      if name.class == String && location.class == Hash && metadata.class == Hash && !block_given?
        return Java::IoVertxServicediscoveryTypes::JDBCDataSource.java_method(:createRecord, [Java::java.lang.String.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCoreJson::JsonObject.java_class]).call(name,::Vertx::Util::Utils.to_json_object(location),::Vertx::Util::Utils.to_json_object(metadata)) != nil ? JSON.parse(Java::IoVertxServicediscoveryTypes::JDBCDataSource.java_method(:createRecord, [Java::java.lang.String.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCoreJson::JsonObject.java_class]).call(name,::Vertx::Util::Utils.to_json_object(location),::Vertx::Util::Utils.to_json_object(metadata)).toJson.encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling create_record(#{name},#{location},#{metadata})"
    end
    #  Convenient method that looks for a JDBC datasource source and provides the configured {::VertxJdbc::JDBCClient}. The
    #  async result is marked as failed is there are no matching services, or if the lookup fails.
    # @param [::VertxServiceDiscovery::ServiceDiscovery] discovery The service discovery instance
    # @param [Hash{String => Object}] filter The filter, optional
    # @param [Hash{String => Object}] consumerConfiguration the consumer configuration
    # @yield the result handler
    # @return [void]
    def self.get_jdbc_client(discovery=nil,filter=nil,consumerConfiguration=nil)
      if discovery.class.method_defined?(:j_del) && filter.class == Hash && block_given? && consumerConfiguration == nil
        return Java::IoVertxServicediscoveryTypes::JDBCDataSource.java_method(:getJDBCClient, [Java::IoVertxServicediscovery::ServiceDiscovery.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Handler.java_class]).call(discovery.j_del,::Vertx::Util::Utils.to_json_object(filter),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxJdbc::JDBCClient) : nil) }))
      elsif discovery.class.method_defined?(:j_del) && filter.class == Hash && consumerConfiguration.class == Hash && block_given?
        return Java::IoVertxServicediscoveryTypes::JDBCDataSource.java_method(:getJDBCClient, [Java::IoVertxServicediscovery::ServiceDiscovery.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Handler.java_class]).call(discovery.j_del,::Vertx::Util::Utils.to_json_object(filter),::Vertx::Util::Utils.to_json_object(consumerConfiguration),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxJdbc::JDBCClient) : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling get_jdbc_client(#{discovery},#{filter},#{consumerConfiguration})"
    end
  end
end
