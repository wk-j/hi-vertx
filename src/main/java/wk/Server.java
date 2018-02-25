package wk;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

public class Server extends AbstractVerticle {
    public void start(Future<Void> fut) {
        Router router = Router.router(vertx);
        router.route("/").handler(context -> {
            HttpServerResponse response = context.response();
            response.putHeader("Content-Type", "text/plain").end("Home");
        });

        router.route("/hello").handler(context -> {
            HttpServerResponse response = context.response();
            response.putHeader("Content-Type", "text/plain").end("Hello");
        });

        vertx.createHttpServer().requestHandler(router::accept).listen(config().getInteger("http.port", 8000),
                result -> {
                    if (result.succeeded()) {
                        fut.complete();
                    } else {
                        fut.fail(result.cause());
                    }
                });
    }
}