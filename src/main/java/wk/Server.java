package wk;

import javax.xml.ws.Response;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class Server extends AbstractVerticle {
    public void start(Future<Void> fut) {
        Router router = Router.router(vertx);
        router.get("/").handler(context -> {
            context.response().putHeader("Content-Type", "text/plain").end("Home");
        });

        router.get("/hello").handler(context -> {
            context.response().putHeader("Content-Type", "text/plain").end("Hello");
        });

        router.get("/add").handler(context -> {
            context.response().putHeader("Content-Type", "application/json")
                    .end(new JsonObject().put("result", 1 + 1).encode());
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