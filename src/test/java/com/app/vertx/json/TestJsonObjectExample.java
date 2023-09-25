package com.app.vertx.json;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class TestJsonObjectExample {

  @Test
  void jsonObjectCanBeMapped() {
    final JsonObject jsonObject = new JsonObject();
    jsonObject.put("id", 1);
    jsonObject.put("name", "TonyStark");
    jsonObject.put("lovesVertx", true);

    final String encodedJsonObject = jsonObject.encode();
    Assertions.assertEquals("{\"id\":1,\"name\":\"TonyStark\",\"lovesVertx\":true}", encodedJsonObject);

    final JsonObject decodedJsonObject = new JsonObject(encodedJsonObject);
    Assertions.assertEquals(jsonObject, decodedJsonObject);
  }

  @Test
  void jsonObjectFromMap() {
    final Map<String, Object> map = new HashMap<>();
    map.put("id", 1);
    map.put("name", "TonyStark");
    map.put("lovesVertx", true);

    final JsonObject jsonObject = new JsonObject(map);

    Assertions.assertEquals(map, jsonObject.getMap());
    Assertions.assertEquals(1, jsonObject.getInteger("id"));
    Assertions.assertEquals("TonyStark", jsonObject.getString("name"));
    Assertions.assertEquals(true, jsonObject.getBoolean("lovesVertx"));

  }


  @Test
  void jsonArrayCanBeMapped() {
    final JsonArray jsonArray = new JsonArray()
      .add(new JsonObject().put("id", 1))
      .add(new JsonObject().put("id", 2))
      .add(new JsonObject().put("id", 3));
//      .add("randomValue");

    Assertions.assertEquals("[{\"id\":1},{\"id\":2},{\"id\":3}]", jsonArray.encode());
  }

  @Test
  void canMapJavaObjects() {
    final Person person = new Person(1, "TonyStark", true);
    final JsonObject tony = JsonObject.mapFrom(person);

    Assertions.assertEquals(person.getId(), tony.getInteger("id"));
    Assertions.assertEquals(person.getName(), tony.getString("name"));
    Assertions.assertEquals(person.isLovesVertx(), tony.getBoolean("lovesVertx"));

    final Person person2 = tony.mapTo(Person.class);
    Assertions.assertEquals(person.getId(), person2.getId());
    Assertions.assertEquals(person.getName(), person2.getName());
    Assertions.assertEquals(person.isLovesVertx(), person2.isLovesVertx());


  }
}
