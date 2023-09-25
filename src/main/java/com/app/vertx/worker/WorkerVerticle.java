package com.app.vertx.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class WorkerVerticle extends AbstractVerticle {


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
//    super.start(startPromise);
    System.out.println("Deployed as worker verticle");
    startPromise.complete();
    Thread.sleep(5000);
    System.out.println("Blocking operation done");
  }
}
