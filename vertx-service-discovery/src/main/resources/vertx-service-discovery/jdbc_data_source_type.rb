require 'vertx-jdbc/jdbc_client'
require 'vertx-service-discovery/service_reference'
require 'vertx/util/utils.rb'
# Generated from io.vertx.servicediscovery.types.JDBCDataSourceType
module VertxServiceDiscovery
  #  TODO
  class JDBCDataSourceType
    # @private
    # @param j_del [::VertxServiceDiscovery::JDBCDataSourceType] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxServiceDiscovery::JDBCDataSourceType] the underlying java delegate
    def j_del
      @j_del
    end
    @@j_api_type = Object.new
    def @@j_api_type.accept?(obj)
      obj.class == JDBCDataSourceType
    end
    def @@j_api_type.wrap(obj)
      JDBCDataSourceType.new(obj)
    end
    def @@j_api_type.unwrap(obj)
      obj.j_del
    end
    def self.j_api_type
      @@j_api_type
    end
    def self.j_class
      Java::IoVertxServicediscoveryTypes::JDBCDataSourceType.java_class
    end
    # @param [::VertxServiceDiscovery::ServiceReference] ref 
    # @return [::VertxJdbc::JDBCClient]
    def get_service(ref=nil)
      if ref.class.method_defined?(:j_del) && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getService, [Java::IoVertxServicediscovery::ServiceReference.java_class]).call(ref.j_del),::VertxJdbc::JDBCClient)
      end
      raise ArgumentError, "Invalid arguments when calling get_service(#{ref})"
    end
    # @param [::VertxServiceDiscovery::ServiceReference] ref 
    # @return [::VertxJdbc::JDBCClient]
    def cached_service(ref=nil)
      if ref.class.method_defined?(:j_del) && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:cachedService, [Java::IoVertxServicediscovery::ServiceReference.java_class]).call(ref.j_del),::VertxJdbc::JDBCClient)
      end
      raise ArgumentError, "Invalid arguments when calling cached_service(#{ref})"
    end
  end
end
