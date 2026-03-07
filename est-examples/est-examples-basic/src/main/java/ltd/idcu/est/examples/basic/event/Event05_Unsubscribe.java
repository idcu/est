package ltd.idcu.est.examples.basic.event;

import ltd.idcu.est.event.api.EventBus;
import ltd.idcu.est.event.api.EventListener;
import ltd.idcu.est.event.local.LocalEvents;

public class Event05_Unsubscribe {
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        EventListener<String> listener1 = (event, data) -> {
            System.out.println("监听�?1: " + data);
        };
        
        EventListener<String> listener2 = (event, data) -> {
            System.out.println("监听�?2: " + data);
        };
        
        eventBus.subscribe("test_event", listener1);
        eventBus.subscribe("test_event", listener2);
        
        System.out.println("=== 第一次发�?===");
        eventBus.publish("test_event", "消息 A");
        
        eventBus.unsubscribe("test_event", listener1);
        
        System.out.println("\n=== 第二次发布（listener1 已取消） ===");
        eventBus.publish("test_event", "消息 B");
        
        eventBus.unsubscribeAll("test_event");
        
        System.out.println("\n=== 第三次发布（所有监听器已取消） ===");
        eventBus.publish("test_event", "消息 C");
        
        System.out.println("\n是否有监听器: " + eventBus.hasSubscribers("test_event"));
    }
}
