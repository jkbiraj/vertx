package com.app.vertx.web;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class WatchListAPI {

  private static final Logger LOG = LoggerFactory.getLogger(WatchListAPI.class);


  public static void attach(Router parentRouter) {

    final HashMap<UUID, WatchList> watchListPerAccount = new HashMap<UUID, WatchList>();

    String path = "/account/watchlist/:accountId";
    parentRouter.get(path).handler(context -> {
      var accountId = context.pathParam("accountId");
      LOG.debug("{} for account {}", context.normalizedPath(), accountId);
      var watchList = Optional.ofNullable(watchListPerAccount.get(UUID.fromString("accountId")));

      if (watchList.isEmpty()) {
        context.response()
          .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
          .end(new JsonObject().put("message", "watchlist for account " + accountId + " not available")
            .put("path", context.normalizedPath()).toBuffer());
        return;
      }

      final JsonObject response = watchList.get().toJasonObject();

      LOG.info("Path {} responds with {}", context.normalizedPath(), response.encode());

      context.response().end(response.toBuffer());
    });

    parentRouter.put(path).handler(context -> {

      var accountId = context.pathParam("accountId");
      LOG.debug("{} for account {}", context.normalizedPath(), accountId);

      JsonObject requestBody = context.body().asJsonObject();
      WatchList watchList = requestBody.mapTo(WatchList.class);
      watchListPerAccount.put(UUID.fromString(accountId), watchList);

      context.response().end(requestBody.toBuffer());
    });

    parentRouter.delete(path).handler(context -> {

    });

  }

}
