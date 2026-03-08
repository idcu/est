package ltd.idcu.est.event.async;

import ltd.idcu.est.event.api.AbstractEventBus;
import ltd.idcu.est.event.api.DefaultEvent;
import ltd.idcu.est.event.api.Event;
import ltd.idcu.est.event.api.EventConfig;
import ltd.idcu.est.event.api.EventException;
import ltd.idcu.est.event.api.EventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class AsyncEventBus extends AbstractEventBus {
    
    private final ExecutorService executor;
    private final AtomicBoolean running;
    
    public AsyncEventBus() {
        this(EventConfig.defaultConfig());
    }
    
    public AsyncEventBus(EventConfig config) {
        super(config);
        this.executor = config.createExecutorService();
        this.running = new AtomicBoolean(true);
    }
    
    @Override
    public <T> void subscribe(String eventType, int priority, EventListener<T> listener) {
        checkRunning();
        super.subscribe(eventType, priority, listener);
    }
    
    @Override
    public <T> void publish(String eventType, T data) {
        publish(eventType, data, null);
    }
    
    @Override
    public <T> void publish(String eventType, T data, Object source) {
        checkRunning();
        
        if (eventType == null || eventType.isEmpty()) {
            throw new IllegalArgumentException("Event type cannot be null or empty");
        }
        
        stats.incrementPublishedCount();
        
        List<ListenerWrapper<?>> eventListeners = listeners.get(eventType);
        if (eventListeners == null || eventListeners.isEmpty()) {
            return;
        }
        
        Event event = DefaultEvent.of(eventType, data, source);
        
        for (ListenerWrapper<?> wrapper : eventListeners) {
            executor.submit(() -> deliverEventToListener(wrapper, event, data));
        }
    }
    
    @Override
    public <T> CompletableFuture<Void> publishAsync(String eventType, T data) {
        return publishAsync(eventType, data, null);
    }
    
    @Override
    public <T> CompletableFuture<Void> publishAsync(String eventType, T data, Object source) {
        checkRunning();
        
        if (eventType == null || eventType.isEmpty()) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Event type cannot be null or empty"));
        }
        
        stats.incrementPublishedCount();
        
        List<ListenerWrapper<?>> eventListeners = listeners.get(eventType);
        if (eventListeners == null || eventListeners.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        
        Event event = DefaultEvent.of(eventType, data, source);
        
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        
        for (ListenerWrapper<?> wrapper : eventListeners) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> deliverEventToListener(wrapper, event, data), executor);
            futures.add(future);
        }
        
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }
    
    public boolean isRunning() {
        return running.get();
    }
    
    public void shutdown() {
        if (running.compareAndSet(true, false)) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public void shutdownNow() {
        running.set(false);
        executor.shutdownNow();
    }
    
    private void checkRunning() {
        if (!running.get()) {
            throw new EventException("EventBus has been shutdown");
        }
    }
}
