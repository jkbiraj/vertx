package com.app.vertx.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class TestWorker extends AbstractVerticle {

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new TestWorker());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.deployVerticle(new WorkerVerticle(),
      new DeploymentOptions()
        .setWorker(true)
        .setWorkerPoolSize(1)
        .setWorkerPoolName("my-worker-verticle"));
    startPromise.complete();
    executeBlockingCode();
  }

  private void executeBlockingCode() {
    vertx.executeBlocking(event -> {
      System.out.println("Executing blocking code");
      try {
        Thread.sleep(5000);
        event.complete();
//        event.fail("force failed!!!");
      } catch (InterruptedException e) {
//        throw new RuntimeException(e);
        System.out.println("Failed: " + e);
        event.fail(e);
      }
    }, result -> {
      if (result.succeeded())
        System.out.println("Blocking code done");
      else
        System.out.println("Blocking call failed due to: " + result.cause());
    });
  }
}
