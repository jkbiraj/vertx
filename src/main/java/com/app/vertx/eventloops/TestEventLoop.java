package com.app.vertx.eventloops;

import io.vertx.core.*;

import java.util.concurrent.TimeUnit;

public class TestEventLoop extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx(new VertxOptions()
      .setMaxEventLoopExecuteTime(500)
      .setMaxEventLoopExecuteTimeUnit(TimeUnit.MILLISECONDS)
      .setBlockedThreadCheckInterval(1)
      .setBlockedThreadCheckIntervalUnit(TimeUnit.SECONDS)
      .setEventLoopPoolSize(2));
    vertx.deployVerticle(TestEventLoop.class.getName(), new DeploymentOptions().setInstances(4));
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
//    super.start(startPromise);
    System.out.println("Start " + getClass().getName());
    startPromise.complete();
//    Thread.sleep(5000);
  }
}
