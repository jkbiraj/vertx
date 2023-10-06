package com.app.vertx.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;


public class TestVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(TestVerticle.class);

  //main method is to just entry point to run the app
  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();//Create Vertx instance with default options
    vertx.deployVerticle(new TestVerticle());
  }


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.debug("Hello from Verticle {}", getClass().getName());
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
