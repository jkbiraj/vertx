package com.app.vertx.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class TestVerticle3 extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
//    super.start(startPromise);
    System.out.println("Hello from Verticle "+getClass().getName());
    startPromise.complete();
  }
}
