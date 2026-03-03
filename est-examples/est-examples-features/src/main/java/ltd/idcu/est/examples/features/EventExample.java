package ltd.idcu.est.examples.features;

import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

public class EventExample {
    public static void main(String[] args) {
        // 创建本地事件总线
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        // 注册事件监听器
        LocalEvents.subscribe(eventBus, "message", (String message) -> {
            System.out.println("Received event: " + message);
        });
        
        // 发布事件
        eventBus.publish("message", "Hello, Event System!");
        eventBus.publish("message", "Another event");
    }
}