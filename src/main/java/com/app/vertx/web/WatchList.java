package com.app.vertx.web;

import io.vertx.core.json.JsonObject;
import lombok.Value;

import java.util.List;

@Value
public class WatchList {

  List<Asset> assets;

  JsonObject toJasonObject() {
    return JsonObject.mapFrom(this);
  }
}
