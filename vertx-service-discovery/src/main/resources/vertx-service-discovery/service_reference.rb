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
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxServiceDiscovery::ServiceReference] the underlying java delegate
    def j_del
      @j_del
    end
    @@j_api_type = Object.new
    def @@j_api_type.accept?(obj)
      obj.class == ServiceReference
    end
    def @@j_api_type.wrap(obj)
      ServiceReference.new(obj)
    end
    def @@j_api_type.unwrap(obj)
      obj.j_del
    end
    def self.j_api_type
      @@j_api_type
    end
    def self.j_class
      Java::IoVertxServicediscovery::ServiceReference.java_class
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
    #  service type and the server itself. This method returns the Java version and primary facet of the object, use
    #  {::VertxServiceDiscovery::ServiceReference#get_as} to retrieve the polyglot instance of the object or another facet..
    # @return [Object] the object to access the service
    def get
      if !block_given?
        return ::Vertx::Util::Utils.from_object(@j_del.java_method(:get, []).call())
      end
      raise ArgumentError, "Invalid arguments when calling get()"
    end
    #  Gets the object to access the service. It can be a proxy, a client or whatever object. The type depends on the
    #  service type and the server itself. This method wraps the service object into the desired type.
    # @param [Nil] x the type of object
    # @return [Object] the object to access the service wrapped to the given type
    def get_as(x=nil)
      if x.class == Class && !block_given?
        return ::Vertx::Util::Utils.v_type_of(x).wrap(@j_del.java_method(:getAs, [Java::JavaLang::Class.java_class]).call(::Vertx::Util::Utils.j_class_of(x)))
      end
      raise ArgumentError, "Invalid arguments when calling get_as(#{x})"
    end
    #  Gets the service object if already retrieved. It won't try to acquire the service object if not retrieved yet.
    #  Unlike {::VertxServiceDiscovery::ServiceReference#cached}, this method return the warpped object to the desired (given) type.
    # @param [Nil] x the type of object
    # @return [Object] the object, <code>null</code> if not yet retrieved
    def cached_as(x=nil)
      if x.class == Class && !block_given?
        return ::Vertx::Util::Utils.v_type_of(x).wrap(@j_del.java_method(:cachedAs, [Java::JavaLang::Class.java_class]).call(::Vertx::Util::Utils.j_class_of(x)))
      end
      raise ArgumentError, "Invalid arguments when calling cached_as(#{x})"
    end
    #  Gets the service object if already retrieved. It won't try to acquire the service object if not retrieved yet.
    # @return [Object] the object, <code>null</code> if not yet retrieved
    def cached
      if !block_given?
        return ::Vertx::Util::Utils.from_object(@j_del.java_method(:cached, []).call())
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
    #  Checks whether or not the service reference has the given service object.
    # @param [Object] object the service object, must not be <code>null</code>
    # @return [true,false] <code>true</code> if the service reference service object is equal to the given object, <code>false</code> otherwise.
    def holding?(object=nil)
      if ::Vertx::Util::unknown_type.accept?(object) && !block_given?
        return @j_del.java_method(:isHolding, [Java::java.lang.Object.java_class]).call(::Vertx::Util::Utils.to_object(object))
      end
      raise ArgumentError, "Invalid arguments when calling holding?(#{object})"
    end
  end
end
