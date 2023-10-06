package com.app.vertx.customcodec;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPongExample {

  private static final Logger LOG = LoggerFactory.getLogger(PingPongExample.class);

  static String ADDRESS = PingVerticle.class.getName();

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new PingVerticle(), handler -> {
      if (handler.failed()) {
        LOG.error("Ping deployment failure:", handler.cause());
      }
    });
    vertx.deployVerticle(new PongVerticle(), handler -> {
      if (handler.failed()) {
        LOG.error("Pong deployment failure:", handler.cause());
      }
    });

  }

  static class PingVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(PingVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      var eventBus = vertx.eventBus();
      final Ping message = new Ping("Hello", true);
      LOG.debug("Sending message...");
      //Register only once
      eventBus.registerDefaultCodec(Ping.class, new LocalMessageCodec<>(Ping.class));
      eventBus.<Pong>request(ADDRESS, message, event -> {
        if (event.failed()) {
          LOG.error("Request send failure:", event.cause());
        }
        LOG.debug("Response received: {}", event.result().body());
      });
      startPromise.complete();
    }
  }

  static class PongVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(PongVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

      var eventBus = vertx.eventBus();
      eventBus.registerDefaultCodec(Pong.class, new LocalMessageCodec<>(Pong.class));
      eventBus.<Ping>consumer(ADDRESS, handler -> {
        LOG.debug("EventBus message received: {}", handler.body());
        handler.reply(new Pong(11));
      }).exceptionHandler(handler -> LOG.error("Error occurred while consuming:", handler));
      startPromise.complete();
    }
  }

}
