package ltd.idcu.est.examples.features;

import ltd.idcu.est.event.api.EventBus;
import ltd.idcu.est.event.local.LocalEvents;

public class EventExample {
    public static void main(String[] args) {
        // Create local event bus
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        // Register event listener
        LocalEvents.subscribe(eventBus, "message", (String message) -> {
            System.out.println("Received event: " + message);
        });
        
        // Publish events
        eventBus.publish("message", "Hello, Event System!");
        eventBus.publish("message", "Another event");
    }
}
