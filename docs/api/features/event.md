# Event 事件总线 API

事件总线提供本地和异步事件处理能力，支持虚拟线程。

## 核心概念

| 概念 | 说明 |
|------|------|
| **EventBus** | 事件总线接口，用于发布和订阅事件 |
| **Event** | 事件接口，包含事件类型、时间戳、源和负载 |
| **EventListener** | 事件监听器函数式接口 |
| **EventStats** | 事件统计信息 |
| **LocalEventBus** | 本地同步事件总线 |
| **AsyncEventBus** | 异步事件总线 |

---

## 本地事件总线 (LocalEventBus)

同步处理事件，适合单机应用。

```java
import ltd.idcu.est.features.event.api.Event;
import ltd.idcu.est.features.event.api.EventListener;
import ltd.idcu.est.features.event.local.LocalEventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

// 创建本地事件总线
LocalEventBus eventBus = LocalEvents.create();

// 订阅事件（带优先级）
eventBus.subscribe("user.created", 1, new EventListener<String>() {
    @Override
    public void onEvent(Event event, String data) {
        System.out.println("收到用户创建事件: " + data);
        System.out.println("事件类型: " + event.getType());
        System.out.println("事件时间: " + event.getTimestamp());
        System.out.println("事件来源: " + event.getSource());
    }
});

// 使用 Lambda 表达式订阅
eventBus.subscribe("order.created", (event, orderId) -> {
    System.out.println("收到订单创建事件: " + orderId);
});

// 发布事件
eventBus.publish("user.created", "user-123");
eventBus.publish("order.created", "order-456", "OrderService");

// 检查是否有订阅者
System.out.println("user.created 有订阅者: " + eventBus.hasSubscribers("user.created"));
System.out.println("user.created 订阅者数量: " + eventBus.getSubscriberCount("user.created"));
System.out.println("总订阅者数量: " + eventBus.getTotalSubscriberCount());
```

---

## 异步事件总线 (AsyncEventBus)

异步处理事件，支持虚拟线程。

```java
import ltd.idcu.est.features.event.api.Event;
import ltd.idcu.est.features.event.async.AsyncEventBus;
import ltd.idcu.est.features.event.async.AsyncEvents;

import java.util.concurrent.CompletableFuture;

// 创建异步事件总线
AsyncEventBus asyncBus = AsyncEvents.create();

// 使用虚拟线程创建异步事件总线
AsyncEventBus virtualThreadBus = AsyncEvents.createWithVirtualThreads();

// 订阅异步事件
asyncBus.subscribe("email.send", (event, email) -> {
    System.out.println("异步发送邮件: " + email);
    // 模拟耗时操作
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
    System.out.println("邮件发送完成: " + email);
});

// 发布事件（异步处理）
asyncBus.publish("email.send", "user@example.com");
System.out.println("事件已发布，继续执行...");

// 异步发布并等待完成
CompletableFuture<Void> future = asyncBus.publishAsync("email.send", "admin@example.com");
future.thenRun(() -> System.out.println("异步事件处理完成"));

// 等待所有事件处理完成
future.join();
```

---

## 事件监听器优先级

```java
import ltd.idcu.est.features.event.local.LocalEventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

LocalEventBus eventBus = LocalEvents.create();

// 优先级数字越小，优先级越高
eventBus.subscribe("log.message", 1, (event, message) -> {
    System.out.println("[高优先级] 处理日志: " + message);
});

eventBus.subscribe("log.message", 10, (event, message) -> {
    System.out.println("[低优先级] 处理日志: " + message);
});

// 发布事件，高优先级监听器先执行
eventBus.publish("log.message", "系统启动");
```

---

## 取消订阅

```java
import ltd.idcu.est.features.event.api.EventListener;
import ltd.idcu.est.features.event.local.LocalEventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

LocalEventBus eventBus = LocalEvents.create();

// 创建监听器
EventListener<String> listener = (event, data) -> {
    System.out.println("收到: " + data);
};

// 订阅
eventBus.subscribe("test.event", listener);

// 发布事件
eventBus.publish("test.event", "Hello");

// 取消单个监听器
eventBus.unsubscribe("test.event", listener);

// 取消某事件类型的所有监听器
eventBus.unsubscribeAll("test.event");

// 取消所有监听器
eventBus.unsubscribeAll();
```

---

## 事件统计

```java
import ltd.idcu.est.features.event.api.EventStats;
import ltd.idcu.est.features.event.local.LocalEventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

LocalEventBus eventBus = LocalEvents.create();

// 订阅一些事件
eventBus.subscribe("event.a", (event, data) -> {});
eventBus.subscribe("event.b", (event, data) -> {});

// 发布事件
for (int i = 0; i < 10; i++) {
    eventBus.publish("event.a", "data-" + i);
}
for (int i = 0; i < 5; i++) {
    eventBus.publish("event.b", "data-" + i);
}

// 获取统计信息
EventStats stats = eventBus.getStats();
System.out.println("发布事件总数: " + stats.getPublishCount());
System.out.println("处理事件总数: " + stats.getHandleCount());
System.out.println("失败事件总数: " + stats.getFailureCount());
System.out.println("平均处理时间: " + stats.getAverageHandleTimeMs() + "ms");
System.out.println("统计开始时间: " + stats.getStartTime());
```

---

## 自定义事件数据

```java
import ltd.idcu.est.features.event.local.LocalEventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

// 定义自定义事件数据类
class User {
    private final String id;
    private final String name;
    private final String email;
    
    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    
    @Override
    public String toString() {
        return "User{id='" + id + "', name='" + name + "', email='" + email + "'}";
    }
}

// 使用自定义事件数据
LocalEventBus eventBus = LocalEvents.create();

// 订阅用户事件
eventBus.subscribe("user.registered", (event, user) -> {
    System.out.println("新用户注册: " + user);
    System.out.println("用户ID: " + user.getId());
    System.out.println("用户名: " + user.getName());
    System.out.println("邮箱: " + user.getEmail());
});

// 发布用户注册事件
User newUser = new User("1001", "张三", "zhangsan@example.com");
eventBus.publish("user.registered", newUser, "UserService");
```

---

## 完整示例：电商系统事件处理

```java
import ltd.idcu.est.features.event.api.Event;
import ltd.idcu.est.features.event.api.EventListener;
import ltd.idcu.est.features.event.async.AsyncEventBus;
import ltd.idcu.est.features.event.async.AsyncEvents;
import ltd.idcu.est.features.event.local.LocalEventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

import java.math.BigDecimal;

// 订单类
class Order {
    private final String orderId;
    private final String userId;
    private final BigDecimal amount;
    
    public Order(String orderId, String userId, BigDecimal amount) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
    }
    
    public String getOrderId() { return orderId; }
    public String getUserId() { return userId; }
    public BigDecimal getAmount() { return amount; }
}

public class ECommerceEventExample {
    public static void main(String[] args) throws InterruptedException {
        // 创建本地事件总线用于同步处理
        LocalEventBus localBus = LocalEvents.create();
        
        // 创建异步事件总线用于异步处理
        AsyncEventBus asyncBus = AsyncEvents.create();
        
        // 1. 订单创建事件 - 同步处理库存
        localBus.subscribe("order.created", 1, (event, order) -> {
            System.out.println("[同步] 检查库存: " + order.getOrderId());
            // 库存检查逻辑
        });
        
        // 2. 订单支付事件 - 异步发送邮件和通知
        asyncBus.subscribe("order.paid", (event, order) -> {
            System.out.println("[异步] 发送支付确认邮件: " + order.getOrderId());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("[异步] 邮件发送完成");
        });
        
        asyncBus.subscribe("order.paid", (event, order) -> {
            System.out.println("[异步] 发送短信通知: " + order.getOrderId());
        });
        
        // 3. 订单完成事件 - 记录日志和统计
        localBus.subscribe("order.completed", (event, order) -> {
            System.out.println("[同步] 记录订单日志: " + order.getOrderId());
            System.out.println("[同步] 更新销售统计: " + order.getAmount());
        });
        
        // 模拟电商流程
        System.out.println("=== 开始电商流程 ===\n");
        
        Order order = new Order("ORD-001", "USER-123", new BigDecimal("99.99"));
        
        // 订单创建
        System.out.println("1. 创建订单...");
        localBus.publish("order.created", order, "OrderService");
        
        // 订单支付
        System.out.println("\n2. 支付订单...");
        asyncBus.publish("order.paid", order, "PaymentService");
        
        // 订单完成
        System.out.println("\n3. 完成订单...");
        localBus.publish("order.completed", order, "OrderService");
        
        // 等待异步事件处理完成
        Thread.sleep(1000);
        
        System.out.println("\n=== 流程完成 ===");
        
        // 显示统计信息
        System.out.println("\n=== 事件统计 ===");
        System.out.println("本地事件总线统计: " + localBus.getStats());
        System.out.println("异步事件总线统计: " + asyncBus.getStats());
    }
}
```

---

## 最佳实践

1. **选择合适的事件总线**：
   - 使用 LocalEventBus 处理需要立即响应的同步操作
   - 使用 AsyncEventBus 处理耗时操作（如发送邮件、调用外部API）

2. **合理使用优先级**：
   - 为关键操作设置更高的优先级
   - 避免过多的优先级层级

3. **异常处理**：
   - 在监听器中捕获和处理异常
   - 避免异常影响其他监听器

4. **监控事件统计**：
   - 定期检查事件统计信息
   - 关注失败事件和处理时间

5. **事件命名规范**：
   - 使用点分隔的命名方式（如 `user.created`、`order.paid`）
   - 保持命名一致性和可读性
