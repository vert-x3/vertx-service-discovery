/*
 * Copyright (c) 2011-2016 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *      The Eclipse Public License is available at
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *      The Apache License v2.0 is available at
 *      http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */

package examples.hystrix;

import com.netflix.hystrix.HystrixCommand;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import rx.Observer;

/**
 * Examples for Hystrix
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class HystrixExamples {

  public void exampleHystrix1() {
    HystrixCommand<String> someCommand = getSomeCommandInstance();
    String result = someCommand.execute();
  }

  public void exampleHystrix2(Vertx vertx) {
    HystrixCommand<String> someCommand = getSomeCommandInstance();
    vertx.<String>executeBlocking(
        future -> future.complete(someCommand.execute()),
        ar -> {
          // back on the event loop
          String result = ar.result();
        }
    );
  }

  public void exampleHystrix3(Vertx vertx) {
    vertx.runOnContext(v -> {
      Context context = vertx.getOrCreateContext();
      HystrixCommand<String> command = getSomeCommandInstance();
      command.observe().subscribe(result -> {
        context.runOnContext(v2 -> {
          // Back on context (event loop or worker)
          String r = result;
        });
      });
    });
  }

  private HystrixCommand<String> getSomeCommandInstance() {
    return null;
  }
}
