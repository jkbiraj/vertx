package com.app.vertx.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestVerticle3 extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(TestVerticle2.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
//    super.start(startPromise);
    LOG.debug("Hello from Verticle {}", getClass().getName());
    startPromise.complete();
  }
}
