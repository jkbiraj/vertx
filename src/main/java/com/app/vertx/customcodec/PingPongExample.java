package com.app.vertx.customcodec;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class PingPongExample {

  static String ADDRESS = PingVerticle.class.getName();

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new PingVerticle(), handler -> {
      if (handler.failed()) {
        System.out.println("Ping deployment failure: " + handler.cause());
      }
    });
    vertx.deployVerticle(new PongVerticle(), handler -> {
      if (handler.failed()) {
        System.out.println("Pong deployment failure: " + handler.cause());
      }
    });

  }

  static class PingVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      var eventBus = vertx.eventBus();
      final Ping message = new Ping("Hello", true);
      System.out.println("Sending message...");
      //Register only once
      eventBus.registerDefaultCodec(Ping.class, new LocalMessageCodec<>(Ping.class));
      eventBus.<Pong>request(ADDRESS, message, event -> {
        if (event.failed()) {
          System.out.println("Request send failure: " + event.cause());
        }
        System.out.println("Response received: " + event.result().body());
      });
      startPromise.complete();
    }
  }

  static class PongVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {

      var eventBus = vertx.eventBus();
      eventBus.registerDefaultCodec(Pong.class, new LocalMessageCodec<>(Pong.class));
      eventBus.<Ping>consumer(ADDRESS, handler -> {
        System.out.println("EventBus message received: " + handler.body());
        handler.reply(new Pong(11));
      }).exceptionHandler(handler -> System.out.println("Error occurred while consuming: " + handler));
      startPromise.complete();
    }
  }

}
