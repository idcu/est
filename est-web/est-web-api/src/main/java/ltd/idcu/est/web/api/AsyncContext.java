package ltd.idcu.est.web.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public interface AsyncContext {

    Request getRequest();

    Response getResponse();

    boolean isAsyncStarted();

    boolean isAsyncComplete();

    void dispatch();

    void complete();

    void setTimeout(long timeout);

    long getTimeout();

    void addListener(AsyncListener listener);

    void start(Runnable runnable);

    void start(Runnable runnable, Executor executor);

    <T> void complete(CompletableFuture<T> future);

    interface AsyncListener {
        void onComplete(AsyncContext context);

        void onTimeout(AsyncContext context);

        void onError(AsyncContext context, Throwable throwable);
    }
}
