package com.app.vertx.ws;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.WebSocketConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(WebSocketVerticle.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new WebSocketVerticle());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    WebSocketConnectOptions options = new WebSocketConnectOptions();
    options.setRegisterWriteHandlers(true);

    vertx.createHttpServer().webSocketHandler(new WebSocketHandler(vertx))
      .listen(8888, http -> {
        if (http.succeeded()) {
          startPromise.complete();
          LOG.debug("HTTP server started on port 8888");
        } else {
          startPromise.fail(http.cause());
        }
      });
  }
}
