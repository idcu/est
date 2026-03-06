package ltd.idcu.est.examples.basic.event;

import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

public class Event01_FirstExample {
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        eventBus.subscribe("order_placed", (event, orderData) -> {
            System.out.println("财务部收到订单: " + orderData);
            System.out.println("开始记账...");
        });
        
        eventBus.subscribe("order_placed", (event, orderData) -> {
            System.out.println("仓库收到订单: " + orderData);
            System.out.println("准备发货...");
        });
        
        System.out.println("=== 销售部发布订单事件 ===");
        eventBus.publish("order_placed", "订单号: 1001, 商品: 笔记本电脑");
        
        System.out.println("\n✅ 程序执行完成！");
    }
}
