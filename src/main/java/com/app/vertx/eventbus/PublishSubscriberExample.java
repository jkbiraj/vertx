package com.app.vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class PublishSubscriberExample {

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new Publisher());
    vertx.deployVerticle(new SubscriberA());
    vertx.deployVerticle(new SubscriberB());
  }

  static class Publisher extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.setPeriodic(1000, handler -> {
        vertx.eventBus().publish(Publisher.class.getName(), "Hello from Publisher...!");
      });
    }
  }


  static class SubscriberA extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(Publisher.class.getName(), handler -> System.out.println("Message consumed by SubscriberA: " + handler.body()));
    }
  }

  static class SubscriberB extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(Publisher.class.getName(), handler -> System.out.println("Message consumed by SubscriberB: " + handler.body()));
    }
  }
}
