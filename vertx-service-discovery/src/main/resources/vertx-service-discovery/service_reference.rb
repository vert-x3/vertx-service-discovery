require 'vertx/util/utils.rb'
# Generated from io.vertx.servicediscovery.ServiceReference
module VertxServiceDiscovery
  #  Once a consumer has chosen a service, it builds a {::VertxServiceDiscovery::ServiceReference} managing the binding with the chosen
  #  service provider.
  #  <p>
  #  The reference lets the consumer:
  #  * access the service (via a proxy or a client) with the {::VertxServiceDiscovery::ServiceReference#get} method
  #  * release the reference - so the binding between the consumer and the provider is removed
  class ServiceReference
    # @private
    # @param j_del [::VertxServiceDiscovery::ServiceReference] the java delegate
    def initialize(j_del, j_arg_T=nil)
      @j_del = j_del
      @j_arg_T = j_arg_T != nil ? j_arg_T : ::Vertx::Util::unknown_type
    end
    # @private
    # @return [::VertxServiceDiscovery::ServiceReference] the underlying java delegate
    def j_del
      @j_del
    end
    # @return [Hash] the service record.
    def record
      if !block_given?
        if @cached_record != nil
          return @cached_record
        end
        return @cached_record = @j_del.java_method(:record, []).call() != nil ? JSON.parse(@j_del.java_method(:record, []).call().toJson.encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling record()"
    end
    #  Gets the object to access the service. It can be a proxy, a client or whatever object. The type depends on the
    #  service type and the server itself.
    # @return [Object] the object to access the service
    def get
      if !block_given?
        return @j_arg_T.wrap(@j_del.java_method(:get, []).call())
      end
      raise ArgumentError, "Invalid arguments when calling get()"
    end
    #  Gets the service object if already retrieved. It won't try to acquire the service object if not retrieved yet.
    # @return [Object] the object, <code>null</code> if not yet retrieved
    def cached
      if !block_given?
        return @j_arg_T.wrap(@j_del.java_method(:cached, []).call())
      end
      raise ArgumentError, "Invalid arguments when calling cached()"
    end
    #  Releases the reference. Once released, the consumer must not use the reference anymore.
    #  This method must be idempotent and defensive, as multiple call may happen.
    # @return [void]
    def release
      if !block_given?
        return @j_del.java_method(:release, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling release()"
    end
  end
end
