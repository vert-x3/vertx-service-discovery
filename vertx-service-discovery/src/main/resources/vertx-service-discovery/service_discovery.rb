require 'vertx-service-discovery/service_reference'
require 'vertx/vertx'
require 'vertx-service-discovery/service_importer'
require 'vertx-service-discovery/service_exporter'
require 'vertx/util/utils.rb'
# Generated from io.vertx.servicediscovery.ServiceDiscovery
module VertxServiceDiscovery
  #  Service Discovery main entry point.
  #  <p>
  #  The service discovery is an infrastructure that let you publish and find `services`. A `service` is a discoverable
  #  functionality. It can be qualified by its type, metadata, and location. So a `service` can be a database, a
  #  service proxy, a HTTP endpoint. It does not have to be a vert.x entity, but can be anything. Each service is
  #  described by a {Hash}.
  #  <p>
  #  The service discovery implements the interactions defined in the service-oriented computing. And to some extend,
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
  #  Providers and consumers must create their own {::VertxServiceDiscovery::ServiceDiscovery} instance. These instances are collaborating
  #  in background (distributed structure) to keep the set of services in sync.
  class ServiceDiscovery
    # @private
    # @param j_del [::VertxServiceDiscovery::ServiceDiscovery] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxServiceDiscovery::ServiceDiscovery] the underlying java delegate
    def j_del
      @j_del
    end
    @@j_api_type = Object.new
    def @@j_api_type.accept?(obj)
      obj.class == ServiceDiscovery
    end
    def @@j_api_type.wrap(obj)
      ServiceDiscovery.new(obj)
    end
    def @@j_api_type.unwrap(obj)
      obj.j_del
    end
    def self.j_api_type
      @@j_api_type
    end
    def self.j_class
      Java::IoVertxServicediscovery::ServiceDiscovery.java_class
    end
    #  Creates an instance of {::VertxServiceDiscovery::ServiceDiscovery}.
    # @param [::Vertx::Vertx] vertx the vert.x instance
    # @param [Hash] options the discovery options
    # @yield completion handler called when the service discovery has been initialized. This includes the initialization of the service importer registered from the SPI.
    # @return [::VertxServiceDiscovery::ServiceDiscovery] the created instance, should not be used to retrieve services before the invocation of the completion handler.
    def self.create(vertx=nil,options=nil)
      if vertx.class.method_defined?(:j_del) && !block_given? && options == nil
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxServicediscovery::ServiceDiscovery.java_method(:create, [Java::IoVertxCore::Vertx.java_class]).call(vertx.j_del),::VertxServiceDiscovery::ServiceDiscovery)
      elsif vertx.class.method_defined?(:j_del) && options.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxServicediscovery::ServiceDiscovery.java_method(:create, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxServicediscovery::ServiceDiscoveryOptions.java_class]).call(vertx.j_del,Java::IoVertxServicediscovery::ServiceDiscoveryOptions.new(::Vertx::Util::Utils.to_json_object(options))),::VertxServiceDiscovery::ServiceDiscovery)
      elsif vertx.class.method_defined?(:j_del) && block_given? && options == nil
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxServicediscovery::ServiceDiscovery.java_method(:create, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxCore::Handler.java_class]).call(vertx.j_del,(Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxServiceDiscovery::ServiceDiscovery)) })),::VertxServiceDiscovery::ServiceDiscovery)
      elsif vertx.class.method_defined?(:j_del) && options.class == Hash && block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxServicediscovery::ServiceDiscovery.java_method(:create, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxServicediscovery::ServiceDiscoveryOptions.java_class,Java::IoVertxCore::Handler.java_class]).call(vertx.j_del,Java::IoVertxServicediscovery::ServiceDiscoveryOptions.new(::Vertx::Util::Utils.to_json_object(options)),(Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxServiceDiscovery::ServiceDiscovery)) })),::VertxServiceDiscovery::ServiceDiscovery)
      end
      raise ArgumentError, "Invalid arguments when calling create(#{vertx},#{options})"
    end
    #  Gets a service reference from the given record.
    # @param [Hash] record the chosen record
    # @return [::VertxServiceDiscovery::ServiceReference] the service reference, that allows retrieving the service object. Once called the service reference is cached, and need to be released.
    def get_reference(record=nil)
      if record.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getReference, [Java::IoVertxServicediscovery::Record.java_class]).call(Java::IoVertxServicediscovery::Record.new(::Vertx::Util::Utils.to_json_object(record))),::VertxServiceDiscovery::ServiceReference)
      end
      raise ArgumentError, "Invalid arguments when calling get_reference(#{record})"
    end
    #  Gets a service reference from the given record, the reference is configured with the given json object.
    # @param [Hash] record the chosen record
    # @param [Hash{String => Object}] configuration the configuration
    # @return [::VertxServiceDiscovery::ServiceReference] the service reference, that allows retrieving the service object. Once called the service reference is cached, and need to be released.
    def get_reference_with_configuration(record=nil,configuration=nil)
      if record.class == Hash && configuration.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getReferenceWithConfiguration, [Java::IoVertxServicediscovery::Record.java_class,Java::IoVertxCoreJson::JsonObject.java_class]).call(Java::IoVertxServicediscovery::Record.new(::Vertx::Util::Utils.to_json_object(record)),::Vertx::Util::Utils.to_json_object(configuration)),::VertxServiceDiscovery::ServiceReference)
      end
      raise ArgumentError, "Invalid arguments when calling get_reference_with_configuration(#{record},#{configuration})"
    end
    #  Releases the service reference.
    # @param [::VertxServiceDiscovery::ServiceReference] reference the reference to release, must not be <code>null</code>
    # @return [true,false] whether or not the reference has been released.
    def release?(reference=nil)
      if reference.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:release, [Java::IoVertxServicediscovery::ServiceReference.java_class]).call(reference.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling release?(#{reference})"
    end
    #  Registers a discovery service importer. Importers let you integrate other discovery technologies in this service
    #  discovery.
    # @param [::VertxServiceDiscovery::ServiceImporter] importer the service importer
    # @param [Hash{String => Object}] configuration the optional configuration
    # @yield handler call when the importer has finished its initialization and initial imports
    # @return [::VertxServiceDiscovery::ServiceDiscovery] the current {::VertxServiceDiscovery::ServiceDiscovery}
    def register_service_importer(importer=nil,configuration=nil)
      if importer.class.method_defined?(:j_del) && configuration.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:registerServiceImporter, [Java::IoVertxServicediscoverySpi::ServiceImporter.java_class,Java::IoVertxCoreJson::JsonObject.java_class]).call(importer.j_del,::Vertx::Util::Utils.to_json_object(configuration)),::VertxServiceDiscovery::ServiceDiscovery)
      elsif importer.class.method_defined?(:j_del) && configuration.class == Hash && block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:registerServiceImporter, [Java::IoVertxServicediscoverySpi::ServiceImporter.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Handler.java_class]).call(importer.j_del,::Vertx::Util::Utils.to_json_object(configuration),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) })),::VertxServiceDiscovery::ServiceDiscovery)
      end
      raise ArgumentError, "Invalid arguments when calling register_service_importer(#{importer},#{configuration})"
    end
    #  Registers a discovery bridge. Exporters let you integrate other discovery technologies in this service
    #  discovery.
    # @param [::VertxServiceDiscovery::ServiceExporter] exporter the service exporter
    # @param [Hash{String => Object}] configuration the optional configuration
    # @yield handler notified when the exporter has been correctly initialized.
    # @return [::VertxServiceDiscovery::ServiceDiscovery] the current {::VertxServiceDiscovery::ServiceDiscovery}
    def register_service_exporter(exporter=nil,configuration=nil)
      if exporter.class.method_defined?(:j_del) && configuration.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:registerServiceExporter, [Java::IoVertxServicediscoverySpi::ServiceExporter.java_class,Java::IoVertxCoreJson::JsonObject.java_class]).call(exporter.j_del,::Vertx::Util::Utils.to_json_object(configuration)),::VertxServiceDiscovery::ServiceDiscovery)
      elsif exporter.class.method_defined?(:j_del) && configuration.class == Hash && block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:registerServiceExporter, [Java::IoVertxServicediscoverySpi::ServiceExporter.java_class,Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Handler.java_class]).call(exporter.j_del,::Vertx::Util::Utils.to_json_object(configuration),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) })),::VertxServiceDiscovery::ServiceDiscovery)
      end
      raise ArgumentError, "Invalid arguments when calling register_service_exporter(#{exporter},#{configuration})"
    end
    #  Closes the service discovery
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
        return @j_del.java_method(:publish, [Java::IoVertxServicediscovery::Record.java_class,Java::IoVertxCore::Handler.java_class]).call(Java::IoVertxServicediscovery::Record.new(::Vertx::Util::Utils.to_json_object(record)),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result != nil ? JSON.parse(ar.result.toJson.encode) : nil : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling publish(#{record})"
    end
    #  Un-publishes a record.
    # @param [String] id the registration id
    # @yield handler called when the operation has completed (successfully or not).
    # @return [void]
    def unpublish(id=nil)
      if id.class == String && block_given?
        return @j_del.java_method(:unpublish, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(id,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling unpublish(#{id})"
    end
    #  Lookups for a single record.
    #  <p>
    #  The filter is a  taking a {Hash} as argument and returning a boolean. You should see it
    #  as an <code>accept</code> method of a filter. This method return a record passing the filter.
    #  <p>
    #  Unlike {::VertxServiceDiscovery::ServiceDiscovery#get_record}, this method may accept records with a <code>OUT OF SERVICE</code>
    #  status, if the <code>includeOutOfService</code> parameter is set to <code>true</code>.
    # @overload getRecord(filter,resultHandler)
    #   @param [Hash{String => Object}] filter the filter.
    #   @yield handler called when the lookup has been completed. When there are no matching record, the operation succeeds, but the async result has no result (<code>null</code>).
    # @overload getRecord(filter,resultHandler)
    #   @param [Proc] filter the filter, must not be <code>null</code>. To return all records, use a function accepting all records
    #   @yield the result handler called when the lookup has been completed. When there are no matching record, the operation succeed, but the async result has no result.
    # @overload getRecord(filter,includeOutOfService,resultHandler)
    #   @param [Proc] filter the filter, must not be <code>null</code>. To return all records, use a function accepting all records
    #   @param [true,false] includeOutOfService whether or not the filter accepts <code>OUT OF SERVICE</code> records
    #   @yield the result handler called when the lookup has been completed. When there are no matching record, the operation succeed, but the async result has no result.
    # @return [void]
    def get_record(param_1=nil,param_2=nil)
      if param_1.class == Hash && block_given? && param_2 == nil
        return @j_del.java_method(:getRecord, [Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Handler.java_class]).call(::Vertx::Util::Utils.to_json_object(param_1),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result != nil ? JSON.parse(ar.result.toJson.encode) : nil : nil) }))
      elsif param_1.class == Proc && block_given? && param_2 == nil
        return @j_del.java_method(:getRecord, [Java::JavaUtilFunction::Function.java_class,Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| param_1.call(event != nil ? JSON.parse(event.toJson.encode) : nil) }),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result != nil ? JSON.parse(ar.result.toJson.encode) : nil : nil) }))
      elsif param_1.class == Proc && (param_2.class == TrueClass || param_2.class == FalseClass) && block_given?
        return @j_del.java_method(:getRecord, [Java::JavaUtilFunction::Function.java_class,Java::boolean.java_class,Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| param_1.call(event != nil ? JSON.parse(event.toJson.encode) : nil) }),param_2,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result != nil ? JSON.parse(ar.result.toJson.encode) : nil : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling get_record(#{param_1},#{param_2})"
    end
    #  Lookups for a set of records. Unlike {::VertxServiceDiscovery::ServiceDiscovery#get_record}, this method returns all matching
    #  records.
    #  <p>
    #  The filter is a  taking a {Hash} as argument and returning a boolean. You should see it
    #  as an <code>accept</code> method of a filter. This method return a record passing the filter.
    #  <p>
    #  Unlike {::VertxServiceDiscovery::ServiceDiscovery#get_records}, this method may accept records with a <code>OUT OF SERVICE</code>
    #  status, if the <code>includeOutOfService</code> parameter is set to <code>true</code>.
    # @overload getRecords(filter,resultHandler)
    #   @param [Hash{String => Object}] filter the filter - see {::VertxServiceDiscovery::ServiceDiscovery#get_record}
    #   @yield handler called when the lookup has been completed. When there are no matching record, the operation succeed, but the async result has an empty list as result.
    # @overload getRecords(filter,resultHandler)
    #   @param [Proc] filter the filter, must not be <code>null</code>. To return all records, use a function accepting all records
    #   @yield handler called when the lookup has been completed. When there are no matching record, the operation succeed, but the async result has an empty list as result.
    # @overload getRecords(filter,includeOutOfService,resultHandler)
    #   @param [Proc] filter the filter, must not be <code>null</code>. To return all records, use a function accepting all records
    #   @param [true,false] includeOutOfService whether or not the filter accepts <code>OUT OF SERVICE</code> records
    #   @yield handler called when the lookup has been completed. When there are no matching record, the operation succeed, but the async result has an empty list as result.
    # @return [void]
    def get_records(param_1=nil,param_2=nil)
      if param_1.class == Hash && block_given? && param_2 == nil
        return @j_del.java_method(:getRecords, [Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Handler.java_class]).call(::Vertx::Util::Utils.to_json_object(param_1),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil } : nil) }))
      elsif param_1.class == Proc && block_given? && param_2 == nil
        return @j_del.java_method(:getRecords, [Java::JavaUtilFunction::Function.java_class,Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| param_1.call(event != nil ? JSON.parse(event.toJson.encode) : nil) }),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil } : nil) }))
      elsif param_1.class == Proc && (param_2.class == TrueClass || param_2.class == FalseClass) && block_given?
        return @j_del.java_method(:getRecords, [Java::JavaUtilFunction::Function.java_class,Java::boolean.java_class,Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| param_1.call(event != nil ? JSON.parse(event.toJson.encode) : nil) }),param_2,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil } : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling get_records(#{param_1},#{param_2})"
    end
    #  Updates the given record. The record must has been published, and has it's registration id set.
    # @param [Hash] record the updated record
    # @yield handler called when the lookup has been completed.
    # @return [void]
    def update(record=nil)
      if record.class == Hash && block_given?
        return @j_del.java_method(:update, [Java::IoVertxServicediscovery::Record.java_class,Java::IoVertxCore::Handler.java_class]).call(Java::IoVertxServicediscovery::Record.new(::Vertx::Util::Utils.to_json_object(record)),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result != nil ? JSON.parse(ar.result.toJson.encode) : nil : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling update(#{record})"
    end
    # @return [Set<::VertxServiceDiscovery::ServiceReference>] the set of service references retrieved by this service discovery.
    def bindings
      if !block_given?
        return ::Vertx::Util::Utils.to_set(@j_del.java_method(:bindings, []).call()).map! { |elt| ::Vertx::Util::Utils.safe_create(elt,::VertxServiceDiscovery::ServiceReference) }
      end
      raise ArgumentError, "Invalid arguments when calling bindings()"
    end
    # @return [Hash] the discovery options. Modifying the returned object would not update the discovery service configuration. This object should be considered as read-only.
    def options
      if !block_given?
        return @j_del.java_method(:options, []).call() != nil ? JSON.parse(@j_del.java_method(:options, []).call().toJson.encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling options()"
    end
    #  Release the service object retrieved using <code>get</code> methods from the service type interface.
    #  It searches for the reference associated with the given object and release it.
    # @param [::VertxServiceDiscovery::ServiceDiscovery] discovery the service discovery
    # @param [Object] svcObject the service object
    # @return [void]
    def self.release_service_object(discovery=nil,svcObject=nil)
      if discovery.class.method_defined?(:j_del) && ::Vertx::Util::unknown_type.accept?(svcObject) && !block_given?
        return Java::IoVertxServicediscovery::ServiceDiscovery.java_method(:releaseServiceObject, [Java::IoVertxServicediscovery::ServiceDiscovery.java_class,Java::java.lang.Object.java_class]).call(discovery.j_del,::Vertx::Util::Utils.to_object(svcObject))
      end
      raise ArgumentError, "Invalid arguments when calling release_service_object(#{discovery},#{svcObject})"
    end
  end
end
