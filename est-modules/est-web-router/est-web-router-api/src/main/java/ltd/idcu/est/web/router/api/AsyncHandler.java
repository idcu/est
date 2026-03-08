package ltd.idcu.est.web.router.api;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface AsyncHandler {
    CompletableFuture<Void> handle(Object context) throws Exception;
}
