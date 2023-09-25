package com.app.vertx.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.UUID;


public class TestVerticle extends AbstractVerticle {

//  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class); //TODO: Fix Logger using slf4j

  //main method is to just entry point to run the app
  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();//Create Vertx instance with default options
    vertx.deployVerticle(new TestVerticle());
  }


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    System.out.println("Hello from Verticle " + getClass().getName());
    vertx.deployVerticle(new TestVerticle2()); //Deploy verticles of verticles
    vertx.deployVerticle(new TestVerticle3());
    vertx.deployVerticle(TestVerticleN.class.getName(),
      new DeploymentOptions().
        setInstances(3).
        setConfig(new JsonObject()
          .put("id", UUID.randomUUID().toString())
          .put("name", TestVerticleN.class.getSimpleName())));
    startPromise.complete();
  }
}
