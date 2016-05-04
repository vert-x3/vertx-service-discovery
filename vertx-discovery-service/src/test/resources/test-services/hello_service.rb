require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.service.HelloService
module TestServices
  #  @author <a href="http://escoffier.me">Clement Escoffier</a>
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
    # @param [Hash{String => Object}] name 
    # @yield 
    # @return [void]
    def hello(name=nil)
      if name.class == Hash && block_given?
        return @j_del.java_method(:hello, [Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Handler.java_class]).call(::Vertx::Util::Utils.to_json_object(name),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling hello(name)"
    end
  end
end
