package com.app.vertx.ws;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.*;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@ExtendWith(VertxExtension.class)
public class TestWebSocketVerticle {

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new WebSocketVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  @Test
  void can_connect_to_web_socket_server(Vertx vertx, VertxTestContext testContext) {

    EventBus eb = vertx.eventBus();

      @Override
      public EventBus serializableChecker(Function<String, Boolean> classNamePredicate) {
        return null;
      }
    }
    var client = vertx.createHttpClient();
    client.webSocket(8888, "localhost", WebSocketHandler.PATH)
      .onFailure(testContext::failNow)
      .onComplete(testContext.succeeding(ws -> {
        ws.handler(data -> {
          System.out.println("Received Data: " + data.toString());
          assertEquals("Connected!!!", data.toString());
          client.close();
          testContext.completeNow();
        });
      }));
  }
}
