package ltd.idcu.est.examples.basic.event;

import ltd.idcu.est.event.api.EventBus;
import ltd.idcu.est.event.local.LocalEvents;

public class Event04_Publish {
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        eventBus.subscribe("message", (event, msg) -> {
            System.out.println("收到消息: " + msg);
        });
        
        System.out.println("开始发布事�?..");
        eventBus.publish("message", "Hello, Event Bus!");
        System.out.println("事件发布完成�?);
    }
}
