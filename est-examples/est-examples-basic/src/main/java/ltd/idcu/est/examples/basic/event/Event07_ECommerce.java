package ltd.idcu.est.examples.basic.event;

import ltd.idcu.est.event.api.EventBus;
import ltd.idcu.est.event.local.LocalEvents;

import java.util.ArrayList;
import java.util.List;

class Order {
    private String id;
    private String customerId;
    private List<String> items;
    private double totalAmount;
    
    public Order(String id, String customerId, List<String> items, double totalAmount) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.totalAmount = totalAmount;
    }
    
    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public List<String> getItems() { return items; }
    public double getTotalAmount() { return totalAmount; }
}

class OrderService {
    private EventBus eventBus;
    
    public OrderService(EventBus eventBus) {
        this.eventBus = eventBus;
    }
    
    public void createOrder(Order order) {
        System.out.println("📦 创建订单: " + order.getId());
        eventBus.publish("order_created", order);
    }
}

class EmailService {
    public EmailService(EventBus eventBus) {
        eventBus.subscribe("order_created", (event, order) -> {
            System.out.println("📧 发送订单确认邮件给客户: " + order.getCustomerId());
        });
    }
}

class InventoryService {
    public InventoryService(EventBus eventBus) {
        eventBus.subscribe("order_created", (event, order) -> {
            System.out.println("📦 更新库存: " + order.getItems());
        });
    }
}

class AccountingService {
    public AccountingService(EventBus eventBus) {
        eventBus.subscribe("order_created", (event, order) -> {
            System.out.println("💰 记录交易: 金额 " + order.getTotalAmount());
        });
    }
}

public class Event07_ECommerce {
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        OrderService orderService = new OrderService(eventBus);
        new EmailService(eventBus);
        new InventoryService(eventBus);
        new AccountingService(eventBus);
        
        System.out.println("=== 电商订单系统示例 ===\n");
        
        List<String> items = new ArrayList<>();
        items.add("笔记本电�?);
        items.add("无线鼠标");
        
        Order order = new Order("ORD-2024-001", "CUST-001", items, 5999.0);
        orderService.createOrder(order);
        
        System.out.println("\n�?订单处理完成�?);
    }
}
