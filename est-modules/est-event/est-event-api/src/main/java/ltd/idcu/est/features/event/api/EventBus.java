package ltd.idcu.est.features.event.api;

import java.util.concurrent.CompletableFuture;

public interface EventBus {
    
    <T> void subscribe(String eventType, EventListener<T> listener);
    
    <T> void subscribe(String eventType, int priority, EventListener<T> listener);
    
    <T> void unsubscribe(String eventType, EventListener<T> listener);
    
    void unsubscribeAll(String eventType);
    
    void unsubscribeAll();
    
    <T> void publish(String eventType, T data);
    
    <T> void publish(String eventType, T data, Object source);
    
    <T> CompletableFuture<Void> publishAsync(String eventType, T data);
    
    <T> CompletableFuture<Void> publishAsync(String eventType, T data, Object source);
    
    boolean hasSubscribers(String eventType);
    
    int getSubscriberCount(String eventType);
    
    int getTotalSubscriberCount();
    
    EventStats getStats();
}
