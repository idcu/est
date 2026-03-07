package ltd.idcu.est.examples.basic.event;

import ltd.idcu.est.event.api.EventBus;
import ltd.idcu.est.event.api.EventConfig;
import ltd.idcu.est.event.local.LocalEvents;

public class Event02_CreateEventBus {
    public static void main(String[] args) {
        EventBus bus1 = LocalEvents.newLocalEventBus();
        
        EventConfig config = new EventConfig()
                .setMaxListenersPerEvent(500)
                .setPropagateExceptions(true);
        
        EventBus bus2 = LocalEvents.newLocalEventBus(config);
        
        EventBus bus3 = LocalEvents.builder()
                .maxListenersPerEvent(1000)
                .propagateExceptions(false)
                .build();
        
        System.out.println("�?三种事件总线创建方式都成功了�?);
    }
}
