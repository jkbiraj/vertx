package com.app.vertx;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class TestFuturePromiseExample {

  @Test
  void TestPromiseSuccess(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    System.out.println(Thread.currentThread().getName() + ": Start");

    vertx.setTimer(500, id -> {
      promise.complete(Thread.currentThread().getName() + ": Success");
      System.out.println(Thread.currentThread().getName() + ": Success");
      context.completeNow();
    });
    System.out.println(Thread.currentThread().getName() + ": End");
  }

  @Test
  void TestPromiseFailure(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    System.out.println(Thread.currentThread().getName() + ": Start");

    vertx.setTimer(500, id -> {
      promise.fail(new RuntimeException("Failed!!!"));
      System.out.println(Thread.currentThread().getName() + ": Failed");
      context.completeNow();
    });
    System.out.println(Thread.currentThread().getName() + ": End");
  }

  @Test
  void TestFutureSuccess(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    System.out.println(Thread.currentThread().getName() + ": Start");

    vertx.setTimer(500, id -> {
      promise.complete(Thread.currentThread().getName() + ": Success");
      System.out.println(Thread.currentThread().getName() + ": Timer done");
    });
    final Future<String> future = promise.future();
    future.onSuccess(result -> {
      System.out.println(Thread.currentThread().getName() + ": Result from future: " + result);
      System.out.println(Thread.currentThread().getName() + ": End");
      context.completeNow();
    }).onFailure(context::failNow);
  }

  @Test
  void TestFutureFailure(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    System.out.println(Thread.currentThread().getName() + ": Start");

    vertx.setTimer(500, id -> {
      promise.fail(new RuntimeException("Failed!!!"));
      System.out.println(Thread.currentThread().getName() + ": Timer done");
    });
    final Future<String> future = promise.future();
    future.onSuccess(context::failNow).onFailure(error -> {
      System.out.println(Thread.currentThread().getName() + ": Result from future: " + error);
      System.out.println(Thread.currentThread().getName() + ": End");
      context.completeNow();
    });
  }

  @Test
  void TestFutureMap(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    System.out.println(Thread.currentThread().getName() + ": Start");

    vertx.setTimer(500, id -> {
      promise.complete(Thread.currentThread().getName() + ": Success");
      System.out.println(Thread.currentThread().getName() + ": Timer done");
    });

    final Future<String> future = promise.future();
    future.map(asString -> {
        System.out.println(Thread.currentThread().getName() + ": Map String to JsonObject");
        return new JsonObject().put("key", asString);
      })
      .map(jsonObject -> new JsonArray().add(jsonObject))
      .onSuccess(result -> {
        System.out.println(Thread.currentThread().getName() + ": Result from future: " + result);
        System.out.println(Thread.currentThread().getName() + ": End");
        context.completeNow();
      })
      .onFailure(context::failNow);
  }

  @Test
  void TestFutureCoordination(Vertx vertx, VertxTestContext context) {
    vertx.createHttpServer()
      .requestHandler(request -> System.out.println(Thread.currentThread().getName() + ": " + request))
      .listen(8484)
      .compose(server -> {
        System.out.println(Thread.currentThread().getName() + ": Another task");
        return Future.succeededFuture(server);
      })
      .compose(server -> {
        System.out.println(Thread.currentThread().getName() + ": Even more");
        return Future.succeededFuture(server);
      })
      .onFailure(context::failNow)
      .onSuccess(server -> {
        System.out.println(Thread.currentThread().getName() + ": Server started on port " + server.actualPort());
        context.completeNow();
      });
  }

  @Test
  void TestFutureComposition(Vertx vertx, VertxTestContext context) {
    var one = Promise.promise();
    var two = Promise.promise();
    var three = Promise.promise();

    var futureOne = one.future();
    var futureTwo = two.future();
    var futureThree = three.future();

//    Future.all(futureOne, futureTwo, futureThree)
    Future.any(futureOne, futureTwo, futureThree)
      .onFailure(context::failNow)
      .onSuccess(result -> {
        System.out.println(Thread.currentThread().getName() + ": Success");
        context.completeNow();
      });

    vertx.setTimer(500, id -> {
      one.complete();
      two.complete();
      three.fail("Three Failed!!!");
    });
  }

}


