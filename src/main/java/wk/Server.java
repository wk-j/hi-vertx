package wk;

import io.netty.util.internal.SocketUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
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

        router.post("/upload").handler(context -> {
            HttpServerResponse response = context.response();
            HttpServerRequest request = context.request();
            request.setExpectMultipart(true);

            request.uploadHandler(uploadRequest -> {
                uploadRequest.endHandler(v -> {
                    String text = request.getFormAttribute("text");
                    response.setChunked(true).end(new JsonObject().put("success", true).put("text", text).encode());
                });

                uploadRequest.streamToFileSystem("temp/README.md");
            });
            response.putHeader("Content-Type", "application/json");
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