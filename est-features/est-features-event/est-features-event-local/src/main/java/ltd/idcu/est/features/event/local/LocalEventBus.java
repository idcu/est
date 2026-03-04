package ltd.idcu.est.features.event.local;

import ltd.idcu.est.features.event.api.AbstractEventBus;
import ltd.idcu.est.features.event.api.DefaultEvent;
import ltd.idcu.est.features.event.api.Event;
import ltd.idcu.est.features.event.api.EventConfig;
import ltd.idcu.est.features.event.api.EventException;
import ltd.idcu.est.features.event.api.EventListener;

import java.util.concurrent.CompletableFuture;

public class LocalEventBus extends AbstractEventBus {
    
    public LocalEventBus() {
        this(EventConfig.defaultConfig());
    }
    
    public LocalEventBus(EventConfig config) {
        super(config);
    }
    
    @Override
    public <T> void publish(String eventType, T data) {
        publish(eventType, data, null);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> void publish(String eventType, T data, Object source) {
        if (eventType == null || eventType.isEmpty()) {
            throw new IllegalArgumentException("Event type cannot be null or empty");
        }
        
        stats.incrementPublishedCount();
        
        java.util.List<ListenerWrapper<?>> eventListeners = listeners.get(eventType);
        if (eventListeners == null || eventListeners.isEmpty()) {
            return;
        }
        
        Event event = DefaultEvent.of(eventType, data, source);
        
        for (ListenerWrapper<?> wrapper : eventListeners) {
            long start = System.currentTimeMillis();
            try {
                EventListener<T> listener = (EventListener<T>) wrapper.getListener();
                listener.onEvent(event, data);
                stats.incrementDeliveredCount();
            } catch (Exception e) {
                stats.incrementFailedCount();
                if (config.isPropagateExceptions()) {
                    throw new EventException("Error delivering event: " + eventType, e);
                }
            } finally {
                stats.addDeliveryTime(System.currentTimeMillis() - start);
            }
        }
    }
    
    @Override
    public <T> CompletableFuture<Void> publishAsync(String eventType, T data) {
        return publishAsync(eventType, data, null);
    }
    
    @Override
    public <T> CompletableFuture<Void> publishAsync(String eventType, T data, Object source) {
        publish(eventType, data, source);
        return CompletableFuture.completedFuture(null);
    }
}
