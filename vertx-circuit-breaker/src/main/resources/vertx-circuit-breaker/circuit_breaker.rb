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
    #  Closes the circuit breaker. It stops sending events on its state on the event bus.
    #  This method is not related to the <code>close</code> state of the circuit breaker. To set the circuit breaker in the
    #  <code>close</code> state, use {::VertxCircuitBreaker::CircuitBreaker#reset}.
    # @return [self]
    def close
      if !block_given?
        @j_del.java_method(:close, []).call()
        return self
      end
      raise ArgumentError, "Invalid arguments when calling close()"
    end
    #  Sets a  invoked when the circuit breaker state switches to open.
    # @yield the handler, must not be <code>null</code>
    # @return [self]
    def open_handler
      if block_given?
        @j_del.java_method(:openHandler, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
        return self
      end
      raise ArgumentError, "Invalid arguments when calling open_handler()"
    end
    #  Sets a  invoked when the circuit breaker state switches to half-open.
    # @yield the handler, must not be <code>null</code>
    # @return [self]
    def half_open_handler
      if block_given?
        @j_del.java_method(:halfOpenHandler, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
        return self
      end
      raise ArgumentError, "Invalid arguments when calling half_open_handler()"
    end
    #  Sets a  invoked when the circuit breaker state switches to close.
    # @yield the handler, must not be <code>null</code>
    # @return [self]
    def close_handler
      if block_given?
        @j_del.java_method(:closeHandler, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
        return self
      end
      raise ArgumentError, "Invalid arguments when calling close_handler()"
    end
    #  Executes the given operation with the circuit breaker control. The operation is generally calling an
    #  <em>external</em> system. The operation receives a  object as parameter and <strong>must</strong>
    #  call  when the operation has terminated successfully. The operation must also
    #  call  in case of failure.
    #  <p>
    #  The operation is not invoked if the circuit breaker is open, and the given fallback is called immediately. The
    #  circuit breaker also monitor the completion of the operation before a configure timeout. The operation is
    #  considered as failed if it does not terminate in time.
    #  <p>
    #  This method returns a  object to retrieve the status and result of the operation, with the status
    #  being a success or a failure. If the fallback is called, the returned future is successfully completed with the
    #  value returned from the fallback. If the fallback throws an exception, the returned future is marked as failed.
    # @param [Proc] operation the operation
    # @yield the fallback function. It gets an exception as parameter and returns the <em>fallback</em> result
    # @return [::Vertx::Future] a future object completed when the operation or its fallback completes
    def execute_with_fallback(operation=nil,fallback=nil)
      if operation.class == Proc && block_given? && fallback == nil
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:executeWithFallback, [Java::IoVertxCore::Handler.java_class,Java::JavaUtilFunction::Function.java_class]).call((Proc.new { |event| operation.call(::Vertx::Util::Utils.safe_create(event,::Vertx::Future)) }),(Proc.new { |event| ::Vertx::Util::Utils.to_object(yield(::Vertx::Util::Utils.from_throwable(event))) })),::Vertx::Future)
      end
      raise ArgumentError, "Invalid arguments when calling execute_with_fallback(operation,fallback)"
    end
    #  Same as {::VertxCircuitBreaker::CircuitBreaker#execute_with_fallback} but using the circuit breaker default fallback.
    # @yield the operation
    # @return [::Vertx::Future] a future object completed when the operation or its fallback completes
    def execute
      if block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:execute, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::Vertx::Future)) })),::Vertx::Future)
      end
      raise ArgumentError, "Invalid arguments when calling execute()"
    end
    #  Same as {::VertxCircuitBreaker::CircuitBreaker#execute_and_report_with_fallback} but using the circuit breaker default
    #  fallback.
    # @param [::Vertx::Future] resultFuture the future on which the operation result is reported
    # @yield the operation
    # @return [self]
    def execute_and_report(resultFuture=nil)
      if resultFuture.class.method_defined?(:j_del) && block_given?
        @j_del.java_method(:executeAndReport, [Java::IoVertxCore::Future.java_class,Java::IoVertxCore::Handler.java_class]).call(resultFuture.j_del,(Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::Vertx::Future)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling execute_and_report(resultFuture)"
    end
    #  Executes the given operation with the circuit breaker control. The operation is generally calling an
    #  <em>external</em> system. The operation receives a  object as parameter and <strong>must</strong>
    #  call  when the operation has terminated successfully. The operation must also
    #  call  in case of failure.
    #  <p>
    #  The operation is not invoked if the circuit breaker is open, and the given fallback is called immediately. The
    #  circuit breaker also monitor the completion of the operation before a configure timeout. The operation is
    #  considered as failed if it does not terminate in time.
    #  <p>
    #  Unlike {::VertxCircuitBreaker::CircuitBreaker#execute_with_fallback},  this method does return a  object, but
    #  let the caller pass a  object on which the result is reported. If the fallback is called, the future
    #  is successfully completed with the value returned by the fallback function. If the fallback throws an exception,
    #  the future is marked as failed.
    # @param [::Vertx::Future] resultFuture the future on which the operation result is reported
    # @param [Proc] operation the operation
    # @yield the fallback function. It gets an exception as parameter and returns the <em>fallback</em> result
    # @return [self]
    def execute_and_report_with_fallback(resultFuture=nil,operation=nil,fallback=nil)
      if resultFuture.class.method_defined?(:j_del) && operation.class == Proc && block_given? && fallback == nil
        @j_del.java_method(:executeAndReportWithFallback, [Java::IoVertxCore::Future.java_class,Java::IoVertxCore::Handler.java_class,Java::JavaUtilFunction::Function.java_class]).call(resultFuture.j_del,(Proc.new { |event| operation.call(::Vertx::Util::Utils.safe_create(event,::Vertx::Future)) }),(Proc.new { |event| ::Vertx::Util::Utils.to_object(yield(::Vertx::Util::Utils.from_throwable(event))) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling execute_and_report_with_fallback(resultFuture,operation,fallback)"
    end
    #  Sets a <em>default</em>  invoked when the bridge is open to handle the "request", or on failure
    #  if {Hash#is_fallback_on_failure} is enabled.
    #  <p>
    #  The function gets the exception as parameter and returns the <em>fallback</em> result.
    # @yield the handler
    # @return [self]
    def fallback(handler=nil)
      if block_given? && handler == nil
        @j_del.java_method(:fallback, [Java::JavaUtilFunction::Function.java_class]).call((Proc.new { |event| ::Vertx::Util::Utils.to_object(yield(::Vertx::Util::Utils.from_throwable(event))) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling fallback(handler)"
    end
    #  Resets the circuit breaker state (number of failure set to 0 and state set to closed).
    # @return [self]
    def reset
      if !block_given?
        @j_del.java_method(:reset, []).call()
        return self
      end
      raise ArgumentError, "Invalid arguments when calling reset()"
    end
    #  Explicitly opens the circuit.
    # @return [self]
    def open
      if !block_given?
        @j_del.java_method(:open, []).call()
        return self
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
    #  @return the name of the circuit breaker.
    # @return [String]
    def name
      if !block_given?
        if @cached_name != nil
          return @cached_name
        end
        return @cached_name = @j_del.java_method(:name, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling name()"
    end
  end
end
