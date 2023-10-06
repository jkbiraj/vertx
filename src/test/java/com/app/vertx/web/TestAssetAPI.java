package com.app.vertx.web;

import com.app.vertx.MainVerticle;
import io.vertx.core.Vertx;
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

import java.util.Arrays;
import java.util.List;

@ExtendWith(VertxExtension.class)
public class TestAssetAPI {

  private static final Logger LOG = LoggerFactory.getLogger(TestAssetAPI.class);

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new WebVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void returns_all_assets(Vertx vertx, VertxTestContext testContext) throws Throwable {
//    testContext.completeNow();
    var client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(WebVerticle.PORT));
    client.get("/asset").send().onComplete(testContext.succeeding(response -> {
      var json = response.bodyAsJsonArray();
      LOG.info("response: {}", json);
      Assertions.assertEquals("[{\"name\":\"AAPL\"},{\"name\":\"AMZN\"},{\"name\":\"FB\"},{\"name\":\"NFLX\"},{\"name\":\"GOOG\"},{\"name\":\"TSLA\"},{\"name\":\"MSFT\"}]", json.encode());
      Assertions.assertEquals(200, response.statusCode());
      testContext.completeNow();
    }));
  }
}
