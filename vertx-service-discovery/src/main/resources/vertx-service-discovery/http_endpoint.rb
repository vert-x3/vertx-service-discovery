require 'vertx-service-discovery/service_discovery'
require 'vertx/http_client'
require 'vertx/util/utils.rb'
# Generated from io.vertx.servicediscovery.types.HttpEndpoint
module VertxServiceDiscovery
  #   for HTTP endpoint (REST api).
  #  Consumers receive a HTTP client configured with the host and port of the endpoint.
  class HttpEndpoint
    # @private
    # @param j_del [::VertxServiceDiscovery::HttpEndpoint] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxServiceDiscovery::HttpEndpoint] the underlying java delegate
    def j_del
      @j_del
    end
    #  Same as {::VertxServiceDiscovery::HttpEndpoint#create_record} but let you configure whether or not the
    #  service is using <code>https</code>.
    # @overload createRecord(name,host)
    #   @param [String] name the name
    #   @param [String] host the host
    # @overload createRecord(name,host,port,root)
    #   @param [String] name the service name
    #   @param [String] host the host, must be public
    #   @param [Fixnum] port the port
    #   @param [String] root the root, if not set "/" is used
    # @overload createRecord(name,host,port,root,metadata)
    #   @param [String] name the service name
    #   @param [String] host the host (IP or DNS name), it must be the _public_ IP / name
    #   @param [Fixnum] port the port, it must be the _public_ port
    #   @param [String] root the path of the service, "/" if not set
    #   @param [Hash{String => Object}] metadata additional metadata
    # @overload createRecord(name,ssl,host,port,root,metadata)
    #   @param [String] name the service name
    #   @param [true,false] ssl whether or not the service is using HTTPS
    #   @param [String] host the host (IP or DNS name), it must be the _public_ IP / name
    #   @param [Fixnum] port the port, it must be the _public_ port
    #   @param [String] root the path of the service, "/" if not set
    #   @param [Hash{String => Object}] metadata additional metadata
    # @return [Hash] the created record
    def self.create_record(param_1=nil,param_2=nil,param_3=nil,param_4=nil,param_5=nil,param_6=nil)
      if param_1.class == String && param_2.class == String && !block_given? && param_3 == nil && param_4 == nil && param_5 == nil && param_6 == nil
        return Java::IoVertxServicediscoveryTypes::HttpEndpoint.java_method(:createRecord, [Java::java.lang.String.java_class,Java::java.lang.String.java_class]).call(param_1,param_2) != nil ? JSON.parse(Java::IoVertxServicediscoveryTypes::HttpEndpoint.java_method(:createRecord, [Java::java.lang.String.java_class,Java::java.lang.String.java_class]).call(param_1,param_2).toJson.encode) : nil
      elsif param_1.class == String && param_2.class == String && param_3.class == Fixnum && param_4.class == String && !block_given? && param_5 == nil && param_6 == nil
        return Java::IoVertxServicediscoveryTypes::HttpEndpoint.java_method(:createRecord, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::int.java_class,Java::java.lang.String.java_class]).call(param_1,param_2,param_3,param_4) != nil ? JSON.parse(Java::IoVertxServicediscoveryTypes::HttpEndpoint.java_method(:createRecord, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::int.java_class,Java::java.lang.String.java_class]).call(param_1,param_2,param_3,param_4).toJson.encode) : nil
      elsif param_1.class == String && param_2.class == String && param_3.class == Fixnum && param_4.class == String && param_5.class == Hash && !block_given? && param_6 == nil
        return Java::IoVertxServicediscoveryTypes::HttpEndpoint.java_method(:createRecord, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::int.java_class,Java::java.lang.String.java_class,Java::IoVertxCoreJson::JsonObject.java_class]).call(param_1,param_2,param_3,param_4,::Vertx::Util::Utils.to_json_object(param_5)) != nil ? JSON.parse(Java::IoVertxServicediscoveryTypes::HttpEndpoint.java_method(:createRecord, [Java::java.lang.String.java_class,Java::java.lang.String.java_class,Java::int.java_class,Java::java.lang.String.java_class,Java::IoVertxCoreJson::JsonObject.java_class]).call(param_1,param_2,param_3,param_4,::Vertx::Util::Utils.to_json_object(param_5)).toJson.encode) : nil
      elsif param_1.class == String && (param_2.class == TrueClass || param_2.class == FalseClass) && param_3.class == String && param_4.class == Fixnum && param_5.class == String && param_6.class == Hash && !block_given?
        return Java::IoVertxServicediscoveryTypes::HttpEndpoint.java_method(:createRecord, [Java::java.lang.String.java_class,Java::boolean.java_class,Java::java.lang.String.java_class,Java::int.java_class,Java::java.lang.String.java_class,Java::IoVertxCoreJson::JsonObject.java_class]).call(param_1,param_2,param_3,param_4,param_5,::Vertx::Util::Utils.to_json_object(param_6)) != nil ? JSON.parse(Java::IoVertxServicediscoveryTypes::HttpEndpoint.java_method(:createRecord, [Java::java.lang.String.java_class,Java::boolean.java_class,Java::java.lang.String.java_class,Java::int.java_class,Java::java.lang.String.java_class,Java::IoVertxCoreJson::JsonObject.java_class]).call(param_1,param_2,param_3,param_4,param_5,::Vertx::Util::Utils.to_json_object(param_6)).toJson.encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling create_record(param_1,param_2,param_3,param_4,param_5,param_6)"
    end
    #  Convenient method that looks for a HTTP endpoint and provides the configured . The async result
    #  is marked as failed is there are no matching services, or if the lookup fails.
    # @overload getClient(discovery,filter,resultHandler)
    #   @param [::VertxServiceDiscovery::ServiceDiscovery] discovery The service discovery instance
    #   @param [Hash{String => Object}] filter The filter, optional
    #   @yield The result handler
    # @overload getClient(discovery,filter,resultHandler)
    #   @param [::VertxServiceDiscovery::ServiceDiscovery] discovery The service discovery instance
    #   @param [Proc] filter The filter
    #   @yield The result handler
    # @return [void]
    def self.get_client(param_1=nil,param_2=nil)
      if param_1.class.method_defined?(:j_del) && param_2.class == Hash && block_given?
        return Java::IoVertxServicediscoveryTypes::HttpEndpoint.java_method(:getClient, [Java::IoVertxServicediscovery::ServiceDiscovery.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1.j_del,::Vertx::Util::Utils.to_json_object(param_2),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::Vertx::HttpClient) : nil) }))
      elsif param_1.class.method_defined?(:j_del) && param_2.class == Proc && block_given?
        return Java::IoVertxServicediscoveryTypes::HttpEndpoint.java_method(:getClient, [Java::IoVertxServicediscovery::ServiceDiscovery.java_class,Java::JavaUtilFunction::Function.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1.j_del,(Proc.new { |event| param_2.call(event != nil ? JSON.parse(event.toJson.encode) : nil) }),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::Vertx::HttpClient) : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling get_client(param_1,param_2)"
    end
  end
end
