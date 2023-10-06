package com.app.vertx.eventloops;

import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TestEventLoop extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(TestEventLoop.class);

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
    LOG.debug("Start {}", getClass().getName());
    startPromise.complete();
//    Thread.sleep(5000);
  }
}
