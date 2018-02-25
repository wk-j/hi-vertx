package wk;

import io.vertx.core.Vertx;

public class Main {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new Server(), ar -> {
            if (ar.failed()) {
                ar.cause().printStackTrace();
            }
        });
    }
}