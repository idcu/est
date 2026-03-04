package ltd.idcu.est.web;

import ltd.idcu.est.web.api.AsyncContext;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DefaultAsyncContext implements AsyncContext {

    private final Request request;
    private final Response response;
    private final List<AsyncListener> listeners = new ArrayList<>();
    private final Executor defaultExecutor = Executors.newVirtualThreadPerTaskExecutor();
    private volatile boolean asyncStarted = false;
    private volatile boolean asyncComplete = false;
    private long timeout = 30000L;
    private final Runnable completeCallback;

    public DefaultAsyncContext(Request request, Response response, Runnable completeCallback) {
        this.request = request;
        this.response = response;
        this.completeCallback = completeCallback;
        this.asyncStarted = true;
    }

    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    public Response getResponse() {
        return response;
    }

    @Override
    public boolean isAsyncStarted() {
        return asyncStarted;
    }

    @Override
    public boolean isAsyncComplete() {
        return asyncComplete;
    }

    @Override
    public void dispatch() {
        throw new UnsupportedOperationException("Dispatch not implemented");
    }

    @Override
    public void complete() {
        if (!asyncComplete) {
            asyncComplete = true;
            asyncStarted = false;
            notifyComplete();
            if (completeCallback != null) {
                completeCallback.run();
            }
        }
    }

    @Override
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public long getTimeout() {
        return timeout;
    }

    @Override
    public void addListener(AsyncListener listener) {
        listeners.add(listener);
    }

    @Override
    public void start(Runnable runnable) {
        start(runnable, defaultExecutor);
    }

    @Override
    public void start(Runnable runnable, Executor executor) {
        executor.execute(() -> {
            try {
                runnable.run();
            } catch (Throwable t) {
                notifyError(t);
            }
        });
    }

    @Override
    public <T> void complete(CompletableFuture<T> future) {
        future.whenComplete((result, throwable) -> {
            if (throwable != null) {
                notifyError(throwable);
            }
            complete();
        });
    }

    private void notifyComplete() {
        listeners.forEach(listener -> listener.onComplete(this));
    }

    public void notifyError(Throwable throwable) {
        listeners.forEach(listener -> listener.onError(this, throwable));
    }

    public void notifyTimeout() {
        listeners.forEach(listener -> listener.onTimeout(this));
    }
}
