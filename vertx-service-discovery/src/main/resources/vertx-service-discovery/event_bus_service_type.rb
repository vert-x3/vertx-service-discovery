require 'vertx-service-discovery/service_reference'
require 'vertx/util/utils.rb'
# Generated from io.vertx.servicediscovery.types.EventBusServiceType
module VertxServiceDiscovery
  #  TODO
  class EventBusServiceType
    # @private
    # @param j_del [::VertxServiceDiscovery::EventBusServiceType] the java delegate
    def initialize(j_del, j_arg_T=nil)
      @j_del = j_del
      @j_arg_T = j_arg_T != nil ? j_arg_T : ::Vertx::Util::unknown_type
    end
    # @private
    # @return [::VertxServiceDiscovery::EventBusServiceType] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [::VertxServiceDiscovery::ServiceReference] ref 
    # @return [Object]
    def get_service(ref=nil)
      if ref.class.method_defined?(:j_del) && !block_given?
        return @j_arg_T.wrap(@j_del.java_method(:getService, [Java::IoVertxServicediscovery::ServiceReference.java_class]).call(ref.j_del))
      end
      raise ArgumentError, "Invalid arguments when calling get_service(#{ref})"
    end
    # @param [::VertxServiceDiscovery::ServiceReference] ref 
    # @return [Object]
    def cached_service(ref=nil)
      if ref.class.method_defined?(:j_del) && !block_given?
        return @j_arg_T.wrap(@j_del.java_method(:cachedService, [Java::IoVertxServicediscovery::ServiceReference.java_class]).call(ref.j_del))
      end
      raise ArgumentError, "Invalid arguments when calling cached_service(#{ref})"
    end
  end
end
