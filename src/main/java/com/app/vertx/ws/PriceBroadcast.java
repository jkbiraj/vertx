package com.app.vertx.ws;

import io.vertx.core.Vertx;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PriceBroadcast {
  private final Map<Integer, ServerWebSocket> connectedClients = new HashMap<>();


  public PriceBroadcast(final Vertx vertx) {
    periodicUpdate(vertx);
  }

  private void periodicUpdate(final Vertx vertx) {
    vertx.setPeriodic(Duration.ofSeconds(1).toMillis(), id -> {
      System.out.println("Push update to " + connectedClients.size() + " clients!");
      connectedClients.values().forEach(ws -> {
        ws.writeTextMessage(new JsonObject()
            .put("symbol", "AMZN")
            .put("value", new Random().nextInt(1000))
          .toString());
      });
    });
  }

  public void register(final ServerWebSocket ws, int counterKey) {
    connectedClients.put(counterKey, ws);
  }

  public void unRegister(int counterKey) {
    connectedClients.remove(counterKey);
  }
}
