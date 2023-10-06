package com.app.vertx.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestVerticle2 extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(TestVerticle2.class);


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.debug("Hello from Verticle {}", getClass().getName());
    vertx.deployVerticle(new TestVerticle21(), event -> {
      LOG.debug("Deployed {}", TestVerticle21.class.getName());
      vertx.undeploy(event.result());

    });
    vertx.deployVerticle(new TestVerticle22(), event -> LOG.debug("Deployed {}", TestVerticle22.class.getName()));
    startPromise.complete();
  }
}
