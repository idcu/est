package ltd.idcu.est.event.local;

import ltd.idcu.est.event.api.AbstractEventBus;
import ltd.idcu.est.event.api.DefaultEvent;
import ltd.idcu.est.event.api.Event;
import ltd.idcu.est.event.api.EventConfig;
import ltd.idcu.est.event.api.EventException;
import ltd.idcu.est.event.api.EventListener;
import ltd.idcu.est.utils.common.AssertUtils;
import ltd.idcu.est.utils.common.StringUtils;

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
    public <T> void publish(String eventType, T data, Object source) {
        AssertUtils.isTrue(StringUtils.isNotBlank(eventType), "Event type cannot be null or empty");
        
        stats.incrementPublishedCount();
        
        java.util.List<ListenerWrapper<?>> eventListeners = listeners.get(eventType);
        if (eventListeners == null || eventListeners.isEmpty()) {
            return;
        }
        
        Event event = DefaultEvent.of(eventType, data, source);
        
        for (ListenerWrapper<?> wrapper : eventListeners) {
            deliverEventToListener(wrapper, event, data);
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
