package com.app.vertx.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestVerticle21 extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(TestVerticle21.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
//    super.start(startPromise);
    LOG.debug("Hello from Verticle {}", getClass().getName());
    startPromise.complete();
  }

  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
//    super.stop(stopPromise);
    LOG.debug("Stop {}", getClass().getName());
    stopPromise.complete();
  }
}
