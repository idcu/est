package ltd.idcu.est.event.async;

import ltd.idcu.est.event.api.EventBus;
import ltd.idcu.est.event.api.EventConfig;
import ltd.idcu.est.event.api.EventListener;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class AsyncEvents {
    
    private AsyncEvents() {
    }
    
    public static EventBus newAsyncEventBus() {
        return new AsyncEventBus();
    }
    
    public static EventBus newAsyncEventBus(EventConfig config) {
        return new AsyncEventBus(config);
    }
    
    public static EventBus newVirtualThreadEventBus() {
        return new AsyncEventBus(EventConfig.virtualThreads());
    }
    
    public static EventBus newFixedThreadEventBus(int poolSize) {
        return new AsyncEventBus(EventConfig.fixedThreads(poolSize));
    }
    
    @SuppressWarnings("unchecked")
    public static <T> void subscribe(EventBus bus, String eventType, Consumer<T> handler) {
        bus.subscribe(eventType, (event, data) -> handler.accept((T) data));
    }
    
    @SuppressWarnings("unchecked")
    public static <T> void subscribe(EventBus bus, String eventType, int priority, Consumer<T> handler) {
        bus.subscribe(eventType, priority, (event, data) -> handler.accept((T) data));
    }
    
    public static <T> CompletableFuture<T> publishAsync(EventBus bus, String eventType, T data) {
        return bus.publishAsync(eventType, data).thenApply(v -> data);
    }
    
    public static <T> CompletableFuture<T> publishAsync(EventBus bus, String eventType, T data, Object source) {
        return bus.publishAsync(eventType, data, source).thenApply(v -> data);
    }
    
    public static AsyncEventBusBuilder builder() {
        return new AsyncEventBusBuilder();
    }
    
    public static class AsyncEventBusBuilder {
        private int threadPoolSize = Runtime.getRuntime().availableProcessors();
        private boolean useVirtualThreads = true;
        private int maxListenersPerEvent = 1000;
        private boolean propagateExceptions = false;
        
        public AsyncEventBusBuilder threadPoolSize(int size) {
            this.threadPoolSize = size;
            return this;
        }
        
        public AsyncEventBusBuilder useVirtualThreads(boolean use) {
            this.useVirtualThreads = use;
            return this;
        }
        
        public AsyncEventBusBuilder maxListenersPerEvent(int max) {
            this.maxListenersPerEvent = max;
            return this;
        }
        
        public AsyncEventBusBuilder propagateExceptions(boolean propagate) {
            this.propagateExceptions = propagate;
            return this;
        }
        
        public EventBus build() {
            EventConfig config = new EventConfig()
                    .setThreadPoolSize(threadPoolSize)
                    .setUseVirtualThreads(useVirtualThreads)
                    .setMaxListenersPerEvent(maxListenersPerEvent)
                    .setPropagateExceptions(propagateExceptions);
            return new AsyncEventBus(config);
        }
    }
}
