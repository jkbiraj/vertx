package com.app.vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponseExample {

  static String ADDRESS = "my.request.address";

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new RequestVerticle());
    vertx.deployVerticle(new ResponseVerticle());

  }

  static class RequestVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(RequestVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      var eventBus = vertx.eventBus();
      LOG.debug("Sending message...");
      eventBus.<JsonArray>request(ADDRESS, new JsonObject().put("message", "Hello world!").put("version", 1), event -> {
        LOG.debug("Response received: {}", event.result().body());
      });
    }
  }

  static class ResponseVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

      var eventBus = vertx.eventBus();
      eventBus.<JsonObject>consumer(ADDRESS, handler -> {
        LOG.debug("EventBus message received: {}", handler.body());
        handler.reply(new JsonArray().add("one").add("two").add("three"));
      });
      startPromise.complete();
    }
  }

}
