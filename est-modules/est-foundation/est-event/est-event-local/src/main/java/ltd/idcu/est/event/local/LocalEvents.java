package ltd.idcu.est.event.local;

import ltd.idcu.est.event.api.EventBus;
import ltd.idcu.est.event.api.EventConfig;
import ltd.idcu.est.event.api.EventListener;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class LocalEvents {
    
    private LocalEvents() {
    }
    
    public static EventBus newLocalEventBus() {
        return new LocalEventBus();
    }
    
    public static EventBus newLocalEventBus(EventConfig config) {
        return new LocalEventBus(config);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> void subscribe(EventBus bus, String eventType, Consumer<T> handler) {
        bus.subscribe(eventType, (event, data) -> handler.accept((T) data));
    }
    
    @SuppressWarnings("unchecked")
    public static <T> void subscribe(EventBus bus, String eventType, int priority, Consumer<T> handler) {
        bus.subscribe(eventType, priority, (event, data) -> handler.accept((T) data));
    }
    
    public static <T> CompletableFuture<T> publishAndWait(EventBus bus, String eventType, T data) {
        bus.publish(eventType, data);
        return CompletableFuture.completedFuture(data);
    }
    
    public static LocalEventBusBuilder builder() {
        return new LocalEventBusBuilder();
    }
    
    public static class LocalEventBusBuilder {
        private int maxListenersPerEvent = 1000;
        private boolean propagateExceptions = false;
        
        public LocalEventBusBuilder maxListenersPerEvent(int max) {
            this.maxListenersPerEvent = max;
            return this;
        }
        
        public LocalEventBusBuilder propagateExceptions(boolean propagate) {
            this.propagateExceptions = propagate;
            return this;
        }
        
        public EventBus build() {
            EventConfig config = new EventConfig()
                    .setMaxListenersPerEvent(maxListenersPerEvent)
                    .setPropagateExceptions(propagateExceptions);
            return new LocalEventBus(config);
        }
    }
}
