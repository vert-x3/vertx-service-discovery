require 'vertx/vertx'
require 'vertx/future'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.circuitbreaker.CircuitBreaker
module VertxCircuitBreaker
  #  An implementation of the circuit breaker pattern for Vert.x
  class CircuitBreaker
    # @private
    # @param j_del [::VertxCircuitBreaker::CircuitBreaker] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxCircuitBreaker::CircuitBreaker] the underlying java delegate
    def j_del
      @j_del
    end
    #  Creates a new instance of {::VertxCircuitBreaker::CircuitBreaker}.
    # @param [String] name the name
    # @param [::Vertx::Vertx] vertx the Vert.x instance
    # @param [Hash] options the configuration option
    # @return [::VertxCircuitBreaker::CircuitBreaker] the created instance
    def self.create(name=nil,vertx=nil,options=nil)
      if name.class == String && vertx.class.method_defined?(:j_del) && !block_given? && options == nil
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtCircuitbreaker::CircuitBreaker.java_method(:create, [Java::java.lang.String.java_class,Java::IoVertxCore::Vertx.java_class]).call(name,vertx.j_del),::VertxCircuitBreaker::CircuitBreaker)
      elsif name.class == String && vertx.class.method_defined?(:j_del) && options.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtCircuitbreaker::CircuitBreaker.java_method(:create, [Java::java.lang.String.java_class,Java::IoVertxCore::Vertx.java_class,Java::IoVertxExtCircuitbreaker::CircuitBreakerOptions.java_class]).call(name,vertx.j_del,Java::IoVertxExtCircuitbreaker::CircuitBreakerOptions.new(::Vertx::Util::Utils.to_json_object(options))),::VertxCircuitBreaker::CircuitBreaker)
      end
      raise ArgumentError, "Invalid arguments when calling create(name,vertx,options)"
    end
    #  Sets a  invoked when the circuit breaker state switches to open.
    # @yield the handler, must not be <code>null</code>
    # @return [::VertxCircuitBreaker::CircuitBreaker] the current {::VertxCircuitBreaker::CircuitBreaker}
    def open_handler
      if block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:openHandler, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield }),::VertxCircuitBreaker::CircuitBreaker)
      end
      raise ArgumentError, "Invalid arguments when calling open_handler()"
    end
    #  Sets a  invoked when the circuit breaker state switches to half-open.
    # @yield the handler, must not be <code>null</code>
    # @return [::VertxCircuitBreaker::CircuitBreaker] the current {::VertxCircuitBreaker::CircuitBreaker}
    def half_open_handler
      if block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:halfOpenHandler, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield }),::VertxCircuitBreaker::CircuitBreaker)
      end
      raise ArgumentError, "Invalid arguments when calling half_open_handler()"
    end
    #  Sets a  invoked when the circuit breaker state switches to close.
    # @yield the handler, must not be <code>null</code>
    # @return [::VertxCircuitBreaker::CircuitBreaker] the current {::VertxCircuitBreaker::CircuitBreaker}
    def close_handler
      if block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:closeHandler, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield }),::VertxCircuitBreaker::CircuitBreaker)
      end
      raise ArgumentError, "Invalid arguments when calling close_handler()"
    end
    #  Sets a  invoked when the bridge is open to handle the "request".
    # @yield the handler, must not be <code>null</code>
    # @return [::VertxCircuitBreaker::CircuitBreaker] the current {::VertxCircuitBreaker::CircuitBreaker}
    def fallback_handler
      if block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:fallbackHandler, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield }),::VertxCircuitBreaker::CircuitBreaker)
      end
      raise ArgumentError, "Invalid arguments when calling fallback_handler()"
    end
    #  Resets the circuit breaker state (number of failure set to 0 and state set to closed).
    # @return [::VertxCircuitBreaker::CircuitBreaker] the current {::VertxCircuitBreaker::CircuitBreaker}
    def reset
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:reset, []).call(),::VertxCircuitBreaker::CircuitBreaker)
      end
      raise ArgumentError, "Invalid arguments when calling reset()"
    end
    #  Explicitly opens the circuit.
    # @return [::VertxCircuitBreaker::CircuitBreaker] the current {::VertxCircuitBreaker::CircuitBreaker}
    def open
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:open, []).call(),::VertxCircuitBreaker::CircuitBreaker)
      end
      raise ArgumentError, "Invalid arguments when calling open()"
    end
    #  @return the current state.
    # @return [:OPEN,:CLOSED,:HALF_OPEN]
    def state
      if !block_given?
        return @j_del.java_method(:state, []).call().name.intern
      end
      raise ArgumentError, "Invalid arguments when calling state()"
    end
    #  @return the current number of failures.
    # @return [Fixnum]
    def failure_count
      if !block_given?
        return @j_del.java_method(:failureCount, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling failure_count()"
    end
    #  Executes the given code with the control of the circuit breaker.
    # @yield the code
    # @return [::VertxCircuitBreaker::CircuitBreaker] the current {::VertxCircuitBreaker::CircuitBreaker}
    def execute_synchronous_block
      if block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:executeSynchronousBlock, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield }),::VertxCircuitBreaker::CircuitBreaker)
      end
      raise ArgumentError, "Invalid arguments when calling execute_synchronous_block()"
    end
    #  Executes the given code with the control of the circuit breaker and use the given fallback is the circuit is open.
    # @param [Proc] code the code
    # @yield 
    # @return [::VertxCircuitBreaker::CircuitBreaker] the current {::VertxCircuitBreaker::CircuitBreaker}
    def execute_synchronous_code_with_fallback(code=nil)
      if code.class == Proc && block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:executeSynchronousCodeWithFallback, [Java::IoVertxCore::Handler.java_class,Java::IoVertxCore::Handler.java_class]).call(code,Proc.new { yield }),::VertxCircuitBreaker::CircuitBreaker)
      end
      raise ArgumentError, "Invalid arguments when calling execute_synchronous_code_with_fallback(code)"
    end
    #  Executes the given code with the control of the circuit breaker. The code is asynchronous. Completion is
    #  detected using the given .
    # @yield the code
    # @return [::VertxCircuitBreaker::CircuitBreaker] the current {::VertxCircuitBreaker::CircuitBreaker}
    def execute_asynchronous_code
      if block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:executeAsynchronousCode, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::Vertx::Future)) })),::VertxCircuitBreaker::CircuitBreaker)
      end
      raise ArgumentError, "Invalid arguments when calling execute_asynchronous_code()"
    end
    #  Executes the given code with the control of the circuit breaker. The code is asynchronous. Completion is
    #  detected using the given . If the circuit is open, this method executes the given fallback.
    # @param [Proc] code the code
    # @yield 
    # @return [::VertxCircuitBreaker::CircuitBreaker] the current {::VertxCircuitBreaker::CircuitBreaker}
    def execute_asynchronous_code_with_fallback(code=nil)
      if code.class == Proc && block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:executeAsynchronousCodeWithFallback, [Java::IoVertxCore::Handler.java_class,Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| code.call(::Vertx::Util::Utils.safe_create(event,::Vertx::Future)) }),Proc.new { yield }),::VertxCircuitBreaker::CircuitBreaker)
      end
      raise ArgumentError, "Invalid arguments when calling execute_asynchronous_code_with_fallback(code)"
    end
    #  @return the name of the circuit breaker.
    # @return [String]
    def name
      if !block_given?
        return @j_del.java_method(:name, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling name()"
    end
  end
end
