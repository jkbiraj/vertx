package com.app.vertx.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestWorker extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(TestWorker.class);

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
//    executeBlockingCode();
  }

//  private void executeBlockingCode() {
//    vertx.executeBlocking(event -> {
//      LOG.debug("Executing blocking code");
//      try {
//        Thread.sleep(5000);
////        event.complete();
//        event.fail("force failed!!!");
//      } catch (InterruptedException e) {
////        throw new RuntimeException(e);
//        LOG.error("Failed:", e);
//        event.fail(e);
//      }
//    }, result -> {
//      if (result.succeeded())
//        LOG.debug("Blocking code done");
//      else
//        LOG.error("Blocking call failed due to: ", result.cause());
//    });
//  }
}
