package ltd.idcu.est.examples.basic.event;

import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.api.EventStats;
import ltd.idcu.est.features.event.local.LocalEvents;

public class Event06_Stats {
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        eventBus.subscribe("event_a", (event, data) -> {});
        eventBus.subscribe("event_a", (event, data) -> {});
        eventBus.subscribe("event_b", (event, data) -> {});
        
        eventBus.publish("event_a", "data1");
        eventBus.publish("event_a", "data2");
        eventBus.publish("event_b", "data3");
        
        EventStats stats = eventBus.getStats();
        
        System.out.println("=== 事件统计信息 ===");
        System.out.println("已发布事件总数: " + stats.getPublishedCount());
        System.out.println("处理成功事件数: " + stats.getSuccessCount());
        System.out.println("处理失败事件数: " + stats.getFailureCount());
        System.out.println("总监听器数: " + eventBus.getTotalSubscriberCount());
        System.out.println("event_a 的监听器数: " + eventBus.getSubscriberCount("event_a"));
    }
}
