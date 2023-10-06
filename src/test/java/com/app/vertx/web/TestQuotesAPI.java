package com.app.vertx.web;

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

@ExtendWith(VertxExtension.class)
public class TestQuotesAPI {

  private static final Logger LOG = LoggerFactory.getLogger(TestQuotesAPI.class);

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new WebVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void returns_quote_for_assets(Vertx vertx, VertxTestContext testContext) throws Throwable {
    var client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(WebVerticle.PORT));
    client.get("/quotes/AMZN").send().onComplete(testContext.succeeding(response -> {
      var json = response.bodyAsJsonObject();
      LOG.info("response: {}", json);
      Assertions.assertEquals("{\"name\":\"AMZN\"}", json.getJsonObject("asset").encode());
      Assertions.assertEquals(200, response.statusCode());
      testContext.completeNow();
    }));
  }

  @Test
  void returns_not_found_for_unknown_assets(Vertx vertx, VertxTestContext testContext) throws Throwable {
    var client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(WebVerticle.PORT));
    client.get("/quotes/UNKNOWN").send().onComplete(testContext.succeeding(response -> {
      var json = response.bodyAsJsonObject();
      LOG.info("response: {}", json);
      Assertions.assertEquals(404, response.statusCode());
      Assertions.assertEquals("{\"message\":\"quote for asset UNKNOWN not available\",\"path\":\"/quotes/UNKNOWN\"}", json.encode());
      testContext.completeNow();
    }));
  }
}
