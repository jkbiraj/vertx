package com.app.vertx.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class TestVerticle2 extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
//    super.start(startPromise);
    System.out.println("Hello from Verticle " + getClass().getName());
    vertx.deployVerticle(new TestVerticle21(), event -> {
      System.out.println("Deployed "+TestVerticle21.class.getName());
      vertx.undeploy(event.result());

    });
    vertx.deployVerticle(new TestVerticle22(), event -> System.out.println("Deployed "+TestVerticle22.class.getName()));
    startPromise.complete();
  }
}
