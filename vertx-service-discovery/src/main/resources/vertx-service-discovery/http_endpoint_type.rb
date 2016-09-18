require 'vertx-service-discovery/service_reference'
require 'vertx/http_client'
require 'vertx/util/utils.rb'
# Generated from io.vertx.servicediscovery.types.HttpEndpointType
module VertxServiceDiscovery
  #   for HTTP endpoint (REST api).
  #  Consumers receive a HTTP client configured with the host and port of the endpoint.
  class HttpEndpointType
    # @private
    # @param j_del [::VertxServiceDiscovery::HttpEndpointType] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxServiceDiscovery::HttpEndpointType] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [::VertxServiceDiscovery::ServiceReference] ref 
    # @return [::Vertx::HttpClient]
    def get_service(ref=nil)
      if ref.class.method_defined?(:j_del) && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getService, [Java::IoVertxServicediscovery::ServiceReference.java_class]).call(ref.j_del),::Vertx::HttpClient)
      end
      raise ArgumentError, "Invalid arguments when calling get_service(ref)"
    end
    # @param [::VertxServiceDiscovery::ServiceReference] ref 
    # @return [::Vertx::HttpClient]
    def cached_service(ref=nil)
      if ref.class.method_defined?(:j_del) && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:cachedService, [Java::IoVertxServicediscovery::ServiceReference.java_class]).call(ref.j_del),::Vertx::HttpClient)
      end
      raise ArgumentError, "Invalid arguments when calling cached_service(ref)"
    end
  end
end
