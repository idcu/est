package ltd.idcu.est.examples.basic.event;

import ltd.idcu.est.event.api.EventBus;
import ltd.idcu.est.event.api.EventListener;
import ltd.idcu.est.event.local.LocalEvents;

public class Event03_Subscribe {
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        eventBus.subscribe("user_login", (event, userData) -> {
            System.out.println("用户登录: " + userData);
        });
        
        LocalEvents.subscribe(eventBus, "user_login", userData -> {
            System.out.println("另一个监听器: " + userData);
        });
        
        EventListener<String> listener = (event, data) -> {
            System.out.println("事件类型: " + event.getEventType());
            System.out.println("事件数据: " + data);
        };
        eventBus.subscribe("user_login", listener);
        
        eventBus.publish("user_login", "张三");
    }
}
