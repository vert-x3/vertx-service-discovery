require 'vertx/util/utils.rb'
# Generated from io.vertx.servicediscovery.service.HelloService
module TestServices
  class HelloService
    # @private
    # @param j_del [::TestServices::HelloService] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::TestServices::HelloService] the underlying java delegate
    def j_del
      @j_del
    end
    @@j_api_type = Object.new
    def @@j_api_type.accept?(obj)
      obj.class == HelloService
    end
    def @@j_api_type.wrap(obj)
      HelloService.new(obj)
    end
    def @@j_api_type.unwrap(obj)
      obj.j_del
    end
    def self.j_api_type
      @@j_api_type
    end
    def self.j_class
      Java::IoVertxServicediscoveryService::HelloService.java_class
    end
    # @param [Hash{String => Object}] name 
    # @yield 
    # @return [void]
    def hello(name=nil)
      if name.class == Hash && block_given?
        return @j_del.java_method(:hello, [Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Handler.java_class]).call(::Vertx::Util::Utils.to_json_object(name),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling hello(#{name})"
    end
  end
end
