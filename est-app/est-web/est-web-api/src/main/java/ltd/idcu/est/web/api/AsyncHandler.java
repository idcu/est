package ltd.idcu.est.web.api;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface AsyncHandler {
    CompletableFuture<Void> handle(AsyncContext context) throws Exception;
}
