package com.app.vertx.ws;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.ServerWebSocket;

public class WebSocketHandler implements Handler<ServerWebSocket> {

  public static final String PATH = "/ws/simple/prices";
  private final PriceBroadcast broadcast;

  private int counterKey = 0;

  public WebSocketHandler(final Vertx vertx) {
    this.broadcast = new PriceBroadcast(vertx);
  }

  @Override
  public void handle(ServerWebSocket ws) {

    if (PATH.equalsIgnoreCase(ws.path())) {

      System.out.println("Opening WebSocket Connection: " + ws.path() + ", " + ws.binaryHandlerID());
      ws.accept();

      ws.frameHandler(received -> {
        final String msg = received.textData();
        System.out.println("Received message from client: " + msg + " from client: " + ws.textHandlerID());

        if ("disconnect me".equalsIgnoreCase(msg)) {
          System.out.println("Client close requested!");
          ws.close((short) 1000, "Normal Closure");
        } else {
          ws.writeTextMessage("Not supported => (" + msg + ")");
        }
      });

      ws.endHandler(onClose -> {
        System.out.println("Closed " + ws.textHandlerID());
        broadcast.unRegister(counterKey); //TODO: This is invalid implementation, can be updated
      });
      ws.exceptionHandler(err -> System.out.println("Failed: " + err));
      ws.writeTextMessage("Connected!!!");

      broadcast.register(ws, ++counterKey);

    } else {
      System.out.println("Rejected, Wrong path: " + ws.path());
      ws.writeFinalTextFrame("Wrong path. Only " + PATH + " is accepted!");
//      ws.reject();
      ws.close((short) 1000, "Normal Closure");
    }

  }
}
