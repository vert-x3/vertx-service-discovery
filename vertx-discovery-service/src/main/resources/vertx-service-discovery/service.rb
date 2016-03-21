require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.discovery.Service
module VertxServiceDiscovery
  #  @author <a href="http://escoffier.me">Clement Escoffier</a>
  class Service
    # @private
    # @param j_del [::VertxServiceDiscovery::Service] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxServiceDiscovery::Service] the underlying java delegate
    def j_del
      @j_del
    end
    # @return [Object]
    def get
      if !block_given?
        return ::Vertx::Util::Utils.from_object(@j_del.java_method(:get, []).call())
      end
      raise ArgumentError, "Invalid arguments when calling get()"
    end
    # @return [void]
    def release
      if !block_given?
        return @j_del.java_method(:release, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling release()"
    end
  end
end
