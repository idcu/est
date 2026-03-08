package ltd.idcu.est.event.api;

@FunctionalInterface
public interface EventListener<T> {
    
    void onEvent(Event event, T data);
}
