package io.vertx.servicediscovery.spi;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.servicediscovery.Record;

import java.util.List;

/**
 * The service exporter allows integrate other discovery technologies with the Vert.x service discovery. It maps
 * entries from another technology to a {@link Record} and maps {@link Record} to a publication in this other
 * technology. The exporter is one side of a service discovery bridge.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface ServiceExporter {

  void onPublication();

  void init(List<Record> records);

  void close();

}
