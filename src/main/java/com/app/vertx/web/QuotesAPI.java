package com.app.vertx.web;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class QuotesAPI {

  private static final Logger LOG = LoggerFactory.getLogger(QuotesAPI.class);


  public static void attach(Router parentRouter) {

    Map<String, Quote> cachedQuote = new HashMap<>();
    AssetAPI.ASSETS.forEach(symbol -> cachedQuote.put(symbol, initRandomQuote(symbol)));

    parentRouter.get("/quotes/:asset").handler(context -> {
      final String assetParam = context.pathParam("asset");
      LOG.debug("Asset parameter: {}", assetParam);

      var optionalQuote = Optional.ofNullable(cachedQuote.get(assetParam));

      if (optionalQuote.isEmpty()) {
        context.response()
          .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
          .end(new JsonObject().put("message", "quote for asset " + assetParam + " not available")
            .put("path", context.normalizedPath()).toBuffer());
        return;
      }

      final JsonObject response = optionalQuote.get().toJsonObject();

      LOG.info("Path {} responds with {}", context.normalizedPath(), response.encode());

      context.response().end(response.toBuffer());
    });
  }

  private static Quote initRandomQuote(final String assetParam) {
    return Quote.builder().asset(new Asset(assetParam))
      .ask(randomValue())
      .bid(randomValue())
      .lastPrice(randomValue())
      .volume(randomValue())
      .build();
  }

  private static BigDecimal randomValue() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
  }
}
