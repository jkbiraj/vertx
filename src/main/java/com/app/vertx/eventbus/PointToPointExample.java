package com.app.vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class PointToPointExample {

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new Sender());
    vertx.deployVerticle(new Receiver());
  }

  static class Sender extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.setPeriodic(1000, handler -> vertx.eventBus().send(Sender.class.getName(), "Hello from Sender!!!"));
    }
  }

  static class Receiver extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      System.out.println("in Receiver");
      vertx.eventBus().<String>consumer(Sender.class.getName(), handler -> System.out.println("Received message from Sender: " + handler.body()));
    }
  }

}
