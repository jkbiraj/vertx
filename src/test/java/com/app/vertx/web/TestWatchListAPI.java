package com.app.vertx.web;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@ExtendWith(VertxExtension.class)
public class TestWatchListAPI {

  private static final Logger LOG = LoggerFactory.getLogger(TestWatchListAPI.class);

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new WebVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void adds_and_returns_watchlist_for_account(Vertx vertx, VertxTestContext testContext) throws Throwable {
    var client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(WebVerticle.PORT));
    UUID accountId = UUID.randomUUID();

    client.put("/account/watchlist/" + accountId).
      sendJsonObject(body()).
      onComplete(testContext.succeeding(response -> {
        var json = response.bodyAsJsonObject();
        LOG.info("response: {}", json);
        Assertions.assertEquals("", json.encode());
        Assertions.assertEquals(200, response.statusCode());
        testContext.completeNow();
      }));
  }

  private JsonObject body() {
    return new WatchList(List.of(
      new Asset("AMZN"),
      new Asset("TSLA")))
      .toJasonObject();
  }
}
