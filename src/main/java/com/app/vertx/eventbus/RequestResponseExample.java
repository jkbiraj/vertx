package com.app.vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class RequestResponseExample {

  static String ADDRESS = "my.request.address";

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new RequestVerticle());
    vertx.deployVerticle(new ResponseVerticle());

  }

  static class RequestVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      var eventBus = vertx.eventBus();
      System.out.println("Sending message...");
      eventBus.<JsonArray>request(ADDRESS, new JsonObject().put("message", "Hello world!").put("version", 1), event -> {
        System.out.println("Response received: " + event.result().body());
      });
    }
  }

  static class ResponseVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {

      var eventBus = vertx.eventBus();
      eventBus.<JsonObject>consumer(ADDRESS, handler -> {
        System.out.println("EventBus message received: " + handler.body());
        handler.reply(new JsonArray().add("one").add("two").add("three"));
      });
      startPromise.complete();
    }
  }

}
