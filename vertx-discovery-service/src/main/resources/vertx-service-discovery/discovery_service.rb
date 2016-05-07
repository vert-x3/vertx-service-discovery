require 'vertx/vertx'
require 'vertx-service-discovery/service_reference'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.discovery.DiscoveryService
module VertxServiceDiscovery
  #  Discovery service main entry point.
  #  <p>
  #  The discovery service is an infrastructure that let you publish and find `services`. A `service` is a discoverable
  #  functionality. It can be qualified by its type, metadata, and location. So a `service` can be a database, a
  #  service proxy, a HTTP endpoint. It does not have to be a vert.x entity, but can be anything. Each service is
  #  described by a {Hash}.
  #  <p>
  #  The discovery service implements the interactions defined in the service-oriented computing. And to some extend,
  #  also provides the dynamic service-oriented computing interaction. So, application can react to arrival and
  #  departure of services.
  #  <p>
  #  A service provider can:
  #  <p>
  #  * publish a service record
  #  * un-publish a published record
  #  * update the status of a published service (down, out of service...)
  #  <p>
  #  A service consumer can:
  #  <p>
  #  * lookup for services
  #  * bind to a selected service (it gets a {::VertxServiceDiscovery::ServiceReference}) and use it
  #  * release the service once the consumer is done with it
  #  * listen for arrival, departure and modification of services.
  #  <p>
  #  Consumer would 1) lookup for service record matching their need, 2) retrieve the {::VertxServiceDiscovery::ServiceReference} that give access
  #  to the service, 3) get a service object to access the service, 4) release the service object once done.
  #  <p>
  #  A state above, the central piece of information shared by the providers and consumers are {Hashrecords}.
  #  <p>
  #  Providers and consumers must create their own {::VertxServiceDiscovery::DiscoveryService} instance. These instances are collaborating
  #  in background (distributed structure) to keep the set of services in sync.
  class DiscoveryService
    # @private
    # @param j_del [::VertxServiceDiscovery::DiscoveryService] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxServiceDiscovery::DiscoveryService] the underlying java delegate
    def j_del
      @j_del
    end
    #  Creates an instance of {::VertxServiceDiscovery::DiscoveryService}.
    # @param [::Vertx::Vertx] vertx the vert.x instance
    # @param [Hash] options the discovery options
    # @return [::VertxServiceDiscovery::DiscoveryService] the create discovery service.
    def self.create(vertx=nil,options=nil)
      if vertx.class.method_defined?(:j_del) && !block_given? && options == nil
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtDiscovery::DiscoveryService.java_method(:create, [Java::IoVertxCore::Vertx.java_class]).call(vertx.j_del),::VertxServiceDiscovery::DiscoveryService)
      elsif vertx.class.method_defined?(:j_del) && options.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtDiscovery::DiscoveryService.java_method(:create, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxExtDiscovery::DiscoveryOptions.java_class]).call(vertx.j_del,Java::IoVertxExtDiscovery::DiscoveryOptions.new(::Vertx::Util::Utils.to_json_object(options))),::VertxServiceDiscovery::DiscoveryService)
      end
      raise ArgumentError, "Invalid arguments when calling create(vertx,options)"
    end
    #  Gets a service reference from the given record.
    # @param [Hash] record the chosen record
    # @return [::VertxServiceDiscovery::ServiceReference] the service reference, that allows retrieving the service object. Once called the service reference is cached, and need to be released.
    def get_reference(record=nil)
      if record.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getReference, [Java::IoVertxExtDiscovery::Record.java_class]).call(Java::IoVertxExtDiscovery::Record.new(::Vertx::Util::Utils.to_json_object(record))),::VertxServiceDiscovery::ServiceReference)
      end
      raise ArgumentError, "Invalid arguments when calling get_reference(record)"
    end
    #  Gets a service reference from the given record, the reference is configured with the given json object.
    # @param [Hash] record the chosen record
    # @param [Hash{String => Object}] configuration the configuration
    # @return [::VertxServiceDiscovery::ServiceReference] the service reference, that allows retrieving the service object. Once called the service reference is cached, and need to be released.
    def get_reference_with_configuration(record=nil,configuration=nil)
      if record.class == Hash && configuration.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getReferenceWithConfiguration, [Java::IoVertxExtDiscovery::Record.java_class,Java::IoVertxCoreJson::JsonObject.java_class]).call(Java::IoVertxExtDiscovery::Record.new(::Vertx::Util::Utils.to_json_object(record)),::Vertx::Util::Utils.to_json_object(configuration)),::VertxServiceDiscovery::ServiceReference)
      end
      raise ArgumentError, "Invalid arguments when calling get_reference_with_configuration(record,configuration)"
    end
    #  Releases the service reference.
    # @param [::VertxServiceDiscovery::ServiceReference] reference the reference to release, must not be <code>null</code>
    # @return [true,false] whether or not the reference has been released.
    def release?(reference=nil)
      if reference.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:release, [Java::IoVertxExtDiscovery::ServiceReference.java_class]).call(reference.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling release?(reference)"
    end
    #  Closes the discovery service
    # @return [void]
    def close
      if !block_given?
        return @j_del.java_method(:close, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling close()"
    end
    #  Publishes a record.
    # @param [Hash] record the record
    # @yield handler called when the operation has completed (successfully or not). In case of success, the passed record has a registration id required to modify and un-register the service.
    # @return [void]
    def publish(record=nil)
      if record.class == Hash && block_given?
        return @j_del.java_method(:publish, [Java::IoVertxExtDiscovery::Record.java_class,Java::IoVertxCore::Handler.java_class]).call(Java::IoVertxExtDiscovery::Record.new(::Vertx::Util::Utils.to_json_object(record)),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result != nil ? JSON.parse(ar.result.toJson.encode) : nil : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling publish(record)"
    end
    #  Un-publishes a record.
    # @param [String] id the registration id
    # @yield handler called when the operation has completed (successfully or not).
    # @return [void]
    def unpublish(id=nil)
      if id.class == String && block_given?
        return @j_del.java_method(:unpublish, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(id,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling unpublish(id)"
    end
    #  Lookups for a single record.
    #  <p>
    #  Filters are expressed using a Json object. Each entry of the given filter will be checked against the record.
    #  All entry must match exactly the record. The entry can use the special "*" value to denotes a requirement on the
    #  key, but not on the value.
    #  <p>
    #  Let's take some example:
    #  <pre>
    #    { "name" = "a" } => matches records with name set fo "a"
    #    { "color" = "*" } => matches records with "color" set
    #    { "color" = "red" } => only matches records with "color" set to "red"
    #    { "color" = "red", "name" = "a"} => only matches records with name set to "a", and color set to "red"
    #  </pre>
    #  <p>
    #  If the filter is not set (<code>null</code> or empty), it accepts all records.
    #  <p>
    #  This method returns the first matching record.
    # @param [Hash{String => Object}] filter the filter.
    # @yield handler called when the lookup has been completed. When there are no matching record, the operation succeed, but the async result has no result.
    # @return [void]
    def get_record(filter=nil)
      if filter.class == Hash && block_given?
        return @j_del.java_method(:getRecord, [Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Handler.java_class]).call(::Vertx::Util::Utils.to_json_object(filter),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result != nil ? JSON.parse(ar.result.toJson.encode) : nil : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling get_record(filter)"
    end
    #  Lookups for a set of records. Unlike {::VertxServiceDiscovery::DiscoveryService#get_record}, this method returns all matching
    #  records.
    # @param [Hash{String => Object}] filter the filter - see {::VertxServiceDiscovery::DiscoveryService#get_record}
    # @yield handler called when the lookup has been completed. When there are no matching record, the operation succeed, but the async result has an empty list as result.
    # @return [void]
    def get_records(filter=nil)
      if filter.class == Hash && block_given?
        return @j_del.java_method(:getRecords, [Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Handler.java_class]).call(::Vertx::Util::Utils.to_json_object(filter),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil } : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling get_records(filter)"
    end
    #  Updates the given record. The record must has been published, and has it's registration id set.
    # @param [Hash] record the updated record
    # @yield handler called when the lookup has been completed.
    # @return [void]
    def update(record=nil)
      if record.class == Hash && block_given?
        return @j_del.java_method(:update, [Java::IoVertxExtDiscovery::Record.java_class,Java::IoVertxCore::Handler.java_class]).call(Java::IoVertxExtDiscovery::Record.new(::Vertx::Util::Utils.to_json_object(record)),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result != nil ? JSON.parse(ar.result.toJson.encode) : nil : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling update(record)"
    end
    #  @return the set of service references retrieved by this discovery service.
    # @return [Set<::VertxServiceDiscovery::ServiceReference>]
    def bindings
      if !block_given?
        return ::Vertx::Util::Utils.to_set(@j_del.java_method(:bindings, []).call()).map! { |elt| ::Vertx::Util::Utils.safe_create(elt,::VertxServiceDiscovery::ServiceReference) }
      end
      raise ArgumentError, "Invalid arguments when calling bindings()"
    end
  end
end
