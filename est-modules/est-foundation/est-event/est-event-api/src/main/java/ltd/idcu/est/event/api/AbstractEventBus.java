package ltd.idcu.est.event.api;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractEventBus implements EventBus {
    
    protected final Map<String, List<ListenerWrapper<?>>> listeners;
    protected final EventConfig config;
    protected final EventStats stats;
    
    protected AbstractEventBus(EventConfig config) {
        this.config = config;
        this.listeners = new ConcurrentHashMap<>();
        this.stats = new EventStats();
    }
    
    @Override
    public <T> void subscribe(String eventType, EventListener<T> listener) {
        subscribe(eventType, 0, listener);
    }
    
    @Override
    public <T> void subscribe(String eventType, int priority, EventListener<T> listener) {
        if (eventType == null || eventType.isEmpty()) {
            throw new IllegalArgumentException("Event type cannot be null or empty");
        }
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        }
        
        listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>());
        List<ListenerWrapper<?>> eventListeners = listeners.get(eventType);
        
        if (eventListeners.size() >= config.getMaxListenersPerEvent()) {
            throw new EventException("Maximum listeners limit reached for event type: " + eventType);
        }
        
        ListenerWrapper<T> wrapper = new ListenerWrapper<>(listener, priority);
        int insertIndex = 0;
        for (; insertIndex < eventListeners.size(); insertIndex++) {
            if (eventListeners.get(insertIndex).getPriority() < priority) {
                break;
            }
        }
        eventListeners.add(insertIndex, wrapper);
    }
    
    @Override
    public <T> void unsubscribe(String eventType, EventListener<T> listener) {
        if (eventType == null || listener == null) {
            return;
        }
        
        List<ListenerWrapper<?>> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.removeIf(w -> w.getListener().equals(listener));
        }
    }
    
    @Override
    public void unsubscribeAll(String eventType) {
        if (eventType == null) {
            return;
        }
        listeners.remove(eventType);
    }
    
    @Override
    public void unsubscribeAll() {
        listeners.clear();
    }
    
    @Override
    public boolean hasSubscribers(String eventType) {
        List<ListenerWrapper<?>> eventListeners = listeners.get(eventType);
        return eventListeners != null && !eventListeners.isEmpty();
    }
    
    @Override
    public int getSubscriberCount(String eventType) {
        List<ListenerWrapper<?>> eventListeners = listeners.get(eventType);
        return eventListeners != null ? eventListeners.size() : 0;
    }
    
    @Override
    public int getTotalSubscriberCount() {
        return listeners.values().stream()
                .mapToInt(List::size)
                .sum();
    }
    
    @Override
    public EventStats getStats() {
        return stats.snapshot();
    }
    
    public Set<String> getEventTypes() {
        return new HashSet<>(listeners.keySet());
    }
    
    @SuppressWarnings("unchecked")
    protected <T> void deliverEventToListener(ListenerWrapper<?> wrapper, Event event, T data) {
        long start = System.currentTimeMillis();
        try {
            EventListener<T> listener = (EventListener<T>) wrapper.getListener();
            listener.onEvent(event, data);
            stats.incrementDeliveredCount();
        } catch (Exception e) {
            stats.incrementFailedCount();
            if (config.isPropagateExceptions()) {
                throw new EventException("Error delivering event: " + event.getType(), e);
            }
        } finally {
            stats.addDeliveryTime(System.currentTimeMillis() - start);
        }
    }
    
    protected static class ListenerWrapper<T> {
        private final EventListener<T> listener;
        private final int priority;
        
        ListenerWrapper(EventListener<T> listener, int priority) {
            this.listener = listener;
            this.priority = priority;
        }
        
        public EventListener<T> getListener() {
            return listener;
        }
        
        public int getPriority() {
            return priority;
        }
    }
}
