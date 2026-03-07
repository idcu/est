package ltd.idcu.est.features.event.api;

@FunctionalInterface
public interface EventListener<T> {
    
    void onEvent(Event event, T data);
}
