# EST Event - 事件总线系统

## 📚 目录

- [快速入门](#快速入门)
- [基础篇](#基础篇)
- [进阶篇](#进阶篇)
- [最佳实践](#最佳实践)

---

## 🚀 快速入门

### 什么是事件总线？

想象一下，你在一个大型公司里工作。公司有很多部门：销售部、财务部、人事部、技术部等等。

以前，当销售部接到一个订单时，他们需要：
1. 打电话给财务部，让他们记账
2. 发邮件给仓库，让他们准备发货
3. 发短信给客服，让他们跟进客户

每个部门都要手动通知，非常麻烦！

**事件总线**就像公司的广播系统：
- 销售部只需要喊一声："我们接到订单啦！"（发布事件）
- 财务部听到广播，自动开始记账（监听事件）
- 仓库听到广播，自动准备发货（监听事件）
- 客服听到广播，自动跟进客户（监听事件）

这样是不是方便多了？

### 第一个例子

让我们用 3 分钟写一个简单的事件总线程序！

首先，在你的 `pom.xml` 中添加依赖：

```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-event-api</artifactId>
    <version>2.0.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-event-local</artifactId>
    <version>2.0.0</version>
</dependency>
```

然后创建一个简单的 Java 类：

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

public class EventFirstExample {
    public static void main(String[] args) {
        // 1. 创建一个事件总线（就像搭建广播系统）
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        // 2. 订阅事件（就像打开收音机收听广播）
        eventBus.subscribe("order_placed", (event, orderData) -> {
            System.out.println("财务部收到订单: " + orderData);
            System.out.println("开始记账...");
        });
        
        eventBus.subscribe("order_placed", (event, orderData) -> {
            System.out.println("仓库收到订单: " + orderData);
            System.out.println("准备发货...");
        });
        
        // 3. 发布事件（就像通过广播系统说话）
        System.out.println("=== 销售部发布订单事件 ===");
        eventBus.publish("order_placed", "订单号: 1001, 商品: 笔记本电脑");
        
        System.out.println("\n✅ 程序执行完成！");
    }
}
```

运行这个程序，你会看到：

```
=== 销售部发布订单事件 ===
财务部收到订单: 订单号: 1001, 商品: 笔记本电脑
开始记账...
仓库收到订单: 订单号: 1001, 商品: 笔记本电脑
准备发货...

✅ 程序执行完成！
```

🎉 恭喜你！你已经学会了使用事件总线！

---

## 📖 基础篇

### 1. 核心概念

在深入学习之前，让我们先理解几个核心概念：

| 概念 | 说明 | 生活类比 |
|------|------|----------|
| **事件类型** | 事件的种类，比如 "order_placed"、"user_registered" | 广播频道，比如"新闻频道"、"音乐频道" |
| **事件数据** | 事件携带的具体信息 | 广播的内容，比如"今天天气晴朗" |
| **事件监听器** | 处理事件的代码 | 收听广播的人 |
| **事件总线** | 管理事件发布和订阅的核心组件 | 广播系统 |
| **发布** | 发送事件到事件总线 | 在广播系统中说话 |
| **订阅** | 注册监听器来接收事件 | 打开收音机收听某个频道 |

### 2. 创建事件总线

EST Event 提供了两种主要的事件总线实现：

#### 2.1 本地事件总线（LocalEventBus）

适用于单进程内的事件通信：

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.api.EventConfig;
import ltd.idcu.est.features.event.local.LocalEvents;

public class EventBusCreationExample {
    public static void main(String[] args) {
        // 方式一：使用默认配置创建
        EventBus bus1 = LocalEvents.newLocalEventBus();
        
        // 方式二：使用自定义配置创建
        EventConfig config = new EventConfig()
                .setMaxListenersPerEvent(500)  // 每个事件最多500个监听器
                .setPropagateExceptions(true);  // 传播异常
        
        EventBus bus2 = LocalEvents.newLocalEventBus(config);
        
        // 方式三：使用 Builder 模式创建
        EventBus bus3 = LocalEvents.builder()
                .maxListenersPerEvent(1000)
                .propagateExceptions(false)
                .build();
        
        System.out.println("✅ 三种事件总线创建方式都成功了！");
    }
}
```

#### 2.2 异步事件总线（AsyncEventBus）

适用于需要异步处理事件的场景：

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.async.AsyncEvents;

public class AsyncEventBusExample {
    public static void main(String[] args) {
        // 创建异步事件总线
        EventBus asyncBus = AsyncEvents.newAsyncEventBus();
        
        // 使用方式和本地事件总线一样
        asyncBus.subscribe("task_completed", (event, data) -> {
            System.out.println("异步处理任务完成: " + data);
        });
        
        asyncBus.publish("task_completed", "数据导出任务");
        
        System.out.println("✅ 异步事件总线创建成功！");
    }
}
```

### 3. 订阅事件

订阅事件有多种方式，让我们一一学习：

#### 3.1 基本订阅

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.api.EventListener;
import ltd.idcu.est.features.event.local.LocalEvents;

public class SubscribeExample {
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        // 方式一：使用 Lambda 表达式
        eventBus.subscribe("user_login", (event, userData) -> {
            System.out.println("用户登录: " + userData);
        });
        
        // 方式二：使用 Consumer（通过 LocalEvents 工具类）
        LocalEvents.subscribe(eventBus, "user_login", userData -> {
            System.out.println("另一个监听器: " + userData);
        });
        
        // 方式三：使用完整的 EventListener 接口
        EventListener<String> listener = (event, data) -> {
            System.out.println("事件类型: " + event.getEventType());
            System.out.println("事件数据: " + data);
        };
        eventBus.subscribe("user_login", listener);
        
        // 测试发布
        eventBus.publish("user_login", "张三");
    }
}
```

#### 3.2 带优先级的订阅

你可以为监听器设置优先级，优先级高的监听器会先执行：

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

public class PrioritySubscribeExample {
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        // 优先级数字越小，优先级越高
        eventBus.subscribe("order_placed", 1, (event, order) -> {
            System.out.println("[优先级 1] 验证订单...");
        });
        
        eventBus.subscribe("order_placed", 2, (event, order) -> {
            System.out.println("[优先级 2] 记录订单...");
        });
        
        eventBus.subscribe("order_placed", 3, (event, order) -> {
            System.out.println("[优先级 3] 发送通知...");
        });
        
        // 发布事件，看看执行顺序
        eventBus.publish("order_placed", "订单 1001");
    }
}
```

运行结果：
```
[优先级 1] 验证订单...
[优先级 2] 记录订单...
[优先级 3] 发送通知...
```

### 4. 发布事件

#### 4.1 同步发布

同步发布意味着事件会在当前线程中立即处理：

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

public class SyncPublishExample {
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        eventBus.subscribe("message", (event, msg) -> {
            System.out.println("收到消息: " + msg);
        });
        
        // 同步发布，会等待所有监听器处理完才返回
        System.out.println("开始发布事件...");
        eventBus.publish("message", "Hello, Event Bus!");
        System.out.println("事件发布完成！");
    }
}
```

#### 4.2 异步发布

异步发布意味着事件会在后台线程中处理：

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.async.AsyncEvents;

import java.util.concurrent.CompletableFuture;

public class AsyncPublishExample {
    public static void main(String[] args) throws Exception {
        EventBus eventBus = AsyncEvents.newAsyncEventBus();
        
        eventBus.subscribe("long_task", (event, task) -> {
            System.out.println("开始处理任务: " + task);
            try {
                Thread.sleep(1000); // 模拟耗时操作
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("任务处理完成: " + task);
        });
        
        // 异步发布，立即返回
        System.out.println("开始异步发布事件...");
        CompletableFuture<Void> future = eventBus.publishAsync("long_task", "数据导入");
        System.out.println("异步发布完成，继续执行其他操作...");
        
        // 等待异步处理完成
        future.get();
        System.out.println("✅ 所有操作完成！");
    }
}
```

#### 4.3 带事件源的发布

你可以指定事件的来源：

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

public class PublishWithSourceExample {
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        eventBus.subscribe("data_changed", (event, data) -> {
            System.out.println("数据变化: " + data);
            System.out.println("事件来源: " + event.getSource());
        });
        
        // 发布时指定来源
        Object source1 = "用户界面";
        eventBus.publish("data_changed", "用户更新了姓名", source1);
        
        Object source2 = "后台服务";
        eventBus.publish("data_changed", "系统自动同步数据", source2);
    }
}
```

### 5. 取消订阅

当你不再需要监听某个事件时，可以取消订阅：

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.api.EventListener;
import ltd.idcu.est.features.event.local.LocalEvents;

public class UnsubscribeExample {
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        // 创建监听器
        EventListener<String> listener1 = (event, data) -> {
            System.out.println("监听器 1: " + data);
        };
        
        EventListener<String> listener2 = (event, data) -> {
            System.out.println("监听器 2: " + data);
        };
        
        // 订阅
        eventBus.subscribe("test_event", listener1);
        eventBus.subscribe("test_event", listener2);
        
        // 第一次发布
        System.out.println("=== 第一次发布 ===");
        eventBus.publish("test_event", "消息 A");
        
        // 取消 listener1 的订阅
        eventBus.unsubscribe("test_event", listener1);
        
        // 第二次发布
        System.out.println("\n=== 第二次发布（listener1 已取消） ===");
        eventBus.publish("test_event", "消息 B");
        
        // 取消该事件类型的所有监听器
        eventBus.unsubscribeAll("test_event");
        
        // 第三次发布
        System.out.println("\n=== 第三次发布（所有监听器已取消） ===");
        eventBus.publish("test_event", "消息 C");
        
        // 检查是否有监听器
        System.out.println("\n是否有监听器: " + eventBus.hasSubscribers("test_event"));
    }
}
```

---

## 🔧 进阶篇

### 1. 事件统计

EventBus 提供了强大的统计功能，可以帮助你监控事件的处理情况：

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.api.EventStats;
import ltd.idcu.est.features.event.local.LocalEvents;

public class EventStatsExample {
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        // 订阅一些事件
        eventBus.subscribe("event_a", (event, data) -> {});
        eventBus.subscribe("event_a", (event, data) -> {});
        eventBus.subscribe("event_b", (event, data) -> {});
        
        // 发布一些事件
        eventBus.publish("event_a", "data1");
        eventBus.publish("event_a", "data2");
        eventBus.publish("event_b", "data3");
        
        // 获取统计信息
        EventStats stats = eventBus.getStats();
        
        System.out.println("=== 事件统计信息 ===");
        System.out.println("已发布事件总数: " + stats.getPublishedCount());
        System.out.println("处理成功事件数: " + stats.getSuccessCount());
        System.out.println("处理失败事件数: " + stats.getFailureCount());
        System.out.println("总监听器数: " + eventBus.getTotalSubscriberCount());
        System.out.println("event_a 的监听器数: " + eventBus.getSubscriberCount("event_a"));
    }
}
```

### 2. 事件配置

你可以通过 EventConfig 来配置事件总线的行为：

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.api.EventConfig;
import ltd.idcu.est.features.event.local.LocalEvents;

import java.util.concurrent.Executors;

public class EventConfigExample {
    public static void main(String[] args) {
        // 创建自定义配置
        EventConfig config = new EventConfig()
                // 线程池大小
                .setThreadPoolSize(4)
                // 是否使用虚拟线程（Java 21+）
                .setUseVirtualThreads(true)
                // 每个事件最多的监听器数量
                .setMaxListenersPerEvent(500)
                // 是否传播异常（true 表示监听器异常会影响后续监听器）
                .setPropagateExceptions(false)
                // 监听器超时时间（毫秒）
                .setListenerTimeout(10000)
                // 自定义线程池
                .setExecutorService(Executors.newCachedThreadPool());
        
        // 使用配置创建事件总线
        EventBus eventBus = LocalEvents.newLocalEventBus(config);
        
        System.out.println("✅ 自定义配置的事件总线创建成功！");
        System.out.println("配置: " + config);
    }
}
```

### 3. 异常处理

你可以配置如何处理监听器中的异常：

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.api.EventConfig;
import ltd.idcu.est.features.event.local.LocalEvents;

public class ExceptionHandlingExample {
    public static void main(String[] args) {
        // 配置 1：不传播异常（默认）- 一个监听器失败不影响其他监听器
        System.out.println("=== 测试 1：不传播异常 ===");
        EventConfig config1 = new EventConfig().setPropagateExceptions(false);
        EventBus bus1 = LocalEvents.newLocalEventBus(config1);
        
        bus1.subscribe("test", (event, data) -> {
            System.out.println("监听器 1 执行");
            throw new RuntimeException("监听器 1 出错了！");
        });
        
        bus1.subscribe("test", (event, data) -> {
            System.out.println("监听器 2 执行（仍然会执行）");
        });
        
        bus1.publish("test", "数据");
        
        // 配置 2：传播异常 - 一个监听器失败会阻止后续监听器
        System.out.println("\n=== 测试 2：传播异常 ===");
        EventConfig config2 = new EventConfig().setPropagateExceptions(true);
        EventBus bus2 = LocalEvents.newLocalEventBus(config2);
        
        bus2.subscribe("test", (event, data) -> {
            System.out.println("监听器 A 执行");
            throw new RuntimeException("监听器 A 出错了！");
        });
        
        bus2.subscribe("test", (event, data) -> {
            System.out.println("监听器 B 执行（不会执行）");
        });
        
        try {
            bus2.publish("test", "数据");
        } catch (Exception e) {
            System.out.println("捕获到异常: " + e.getMessage());
        }
    }
}
```

### 4. 自定义事件

你可以创建自定义的事件类来封装更复杂的事件信息：

```java
import ltd.idcu.est.features.event.api.Event;
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

import java.time.LocalDateTime;

// 自定义事件数据类
class OrderEvent {
    private String orderId;
    private String productName;
    private double amount;
    private LocalDateTime timestamp;
    
    public OrderEvent(String orderId, String productName, double amount) {
        this.orderId = orderId;
        this.productName = productName;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters
    public String getOrderId() { return orderId; }
    public String getProductName() { return productName; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        return "OrderEvent{" +
                "orderId='" + orderId + '\'' +
                ", productName='" + productName + '\'' +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}

public class CustomEventExample {
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        // 订阅自定义事件
        eventBus.subscribe("order_created", (event, orderData) -> {
            System.out.println("收到订单创建事件:");
            System.out.println("  订单号: " + orderData.getOrderId());
            System.out.println("  商品: " + orderData.getProductName());
            System.out.println("  金额: " + orderData.getAmount());
            System.out.println("  时间: " + orderData.getTimestamp());
        });
        
        // 发布自定义事件
        OrderEvent order = new OrderEvent("ORD-001", "智能手机", 2999.0);
        eventBus.publish("order_created", order);
    }
}
```

### 5. 与 EST Collection 集成

事件总线可以和 EST Collection 完美结合，处理批量事件：

```java
import ltd.idcu.est.collection.api.Seqs;
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

import java.util.List;

public class EventCollectionIntegrationExample {
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        // 订阅事件
        eventBus.subscribe("user_notification", (event, user) -> {
            System.out.println("发送通知给: " + user);
        });
        
        // 使用 Collection 批量发布事件
        List<String> users = List.of("张三", "李四", "王五", "赵六");
        
        System.out.println("=== 批量发送通知 ===");
        Seqs.of(users)
                .filter(user -> !user.equals("王五"))  // 过滤掉王五
                .forEach(user -> eventBus.publish("user_notification", user));
        
        System.out.println("\n✅ 批量通知发送完成！");
    }
}
```

---

## 💡 最佳实践

### 1. 事件命名规范

好的事件命名能让代码更易读：

```java
// ✅ 推荐：使用过去式，表示某事已经发生
"order_placed"       // 订单已创建
"user_registered"    // 用户已注册
"payment_completed"  // 支付已完成
"email_sent"         // 邮件已发送

// ❌ 不推荐：使用命令式
"place_order"        // 容易混淆是命令还是事件
"register_user"      // 同上
```

### 2. 事件数据设计

事件数据应该包含足够的信息，但不要过度：

```java
// ✅ 好的设计：包含必要的信息
class OrderPlacedEvent {
    private String orderId;
    private String customerId;
    private List<OrderItem> items;
    private double totalAmount;
    // ...
}

// ❌ 不好的设计：信息太少
class OrderPlacedEvent {
    private String orderId;  // 只有订单 ID，监听器还得去查询
}

// ❌ 不好的设计：信息太多
class OrderPlacedEvent {
    private String orderId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customerAddress;
    // ... 太多了！
}
```

### 3. 监听器设计原则

```java
// ✅ 好的监听器：职责单一
eventBus.subscribe("order_placed", (event, order) -> {
    // 只负责发送确认邮件
    emailService.sendOrderConfirmation(order.getCustomerEmail(), order);
});

eventBus.subscribe("order_placed", (event, order) -> {
    // 只负责更新库存
    inventoryService.updateStock(order.getItems());
});

// ❌ 不好的监听器：职责太多
eventBus.subscribe("order_placed", (event, order) -> {
    // 一个监听器做了太多事
    emailService.sendOrderConfirmation(...);
    inventoryService.updateStock(...);
    accountingService.recordTransaction(...);
    notificationService.sendPushNotification(...);
});
```

### 4. 错误处理策略

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandlingBestPractice {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandlingBestPractice.class);
    
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        // ✅ 在监听器内部处理异常
        eventBus.subscribe("order_placed", (event, order) -> {
            try {
                // 尝试发送邮件
                emailService.sendOrderConfirmation(order);
            } catch (Exception e) {
                // 记录日志，但不要影响其他监听器
                logger.error("发送订单确认邮件失败", e);
                // 可以考虑重试或降级处理
                retryService.scheduleRetry(() -> emailService.sendOrderConfirmation(order));
            }
        });
    }
    
    // 模拟的服务
    private static class emailService {
        static void sendOrderConfirmation(Object order) {
            // 发送邮件
        }
    }
    
    private static class retryService {
        static void scheduleRetry(Runnable task) {
            // 调度重试
        }
    }
}
```

### 5. 性能优化建议

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.api.EventConfig;
import ltd.idcu.est.features.event.async.AsyncEvents;

public class PerformanceOptimization {
    public static void main(String[] args) {
        // ✅ 对于耗时操作，使用异步事件总线
        EventBus asyncBus = AsyncEvents.newAsyncEventBus();
        
        asyncBus.subscribe("report_generated", (event, report) -> {
            // 耗时的 PDF 生成操作
            generatePdfReport(report);
        });
        
        // 异步发布，不阻塞主线程
        asyncBus.publishAsync("report_generated", salesReport);
        
        // ✅ 合理配置线程池
        EventConfig config = new EventConfig()
                .setThreadPoolSize(Runtime.getRuntime().availableProcessors() * 2)
                .setUseVirtualThreads(true);  // Java 21+ 使用虚拟线程
    }
    
    private static void generatePdfReport(Object report) {
        // 生成 PDF
    }
    
    private static Object salesReport = new Object();
}
```

### 6. 完整示例：电商订单系统

让我们用事件总线构建一个简化的电商订单系统：

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

import java.util.ArrayList;
import java.util.List;

// 订单类
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
    
    // Getters
    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public List<String> getItems() { return items; }
    public double getTotalAmount() { return totalAmount; }
}

// 订单服务
class OrderService {
    private EventBus eventBus;
    
    public OrderService(EventBus eventBus) {
        this.eventBus = eventBus;
    }
    
    public void createOrder(Order order) {
        System.out.println("📦 创建订单: " + order.getId());
        
        // 发布订单创建事件
        eventBus.publish("order_created", order);
    }
}

// 邮件服务
class EmailService {
    public EmailService(EventBus eventBus) {
        eventBus.subscribe("order_created", (event, order) -> {
            System.out.println("📧 发送订单确认邮件给客户: " + order.getCustomerId());
        });
    }
}

// 库存服务
class InventoryService {
    public InventoryService(EventBus eventBus) {
        eventBus.subscribe("order_created", (event, order) -> {
            System.out.println("📦 更新库存: " + order.getItems());
        });
    }
}

// 会计服务
class AccountingService {
    public AccountingService(EventBus eventBus) {
        eventBus.subscribe("order_created", (event, order) -> {
            System.out.println("💰 记录交易: 金额 " + order.getTotalAmount());
        });
    }
}

// 主程序
public class ECommerceExample {
    public static void main(String[] args) {
        // 创建事件总线
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        // 初始化各个服务
        OrderService orderService = new OrderService(eventBus);
        new EmailService(eventBus);
        new InventoryService(eventBus);
        new AccountingService(eventBus);
        
        // 创建订单
        System.out.println("=== 电商订单系统示例 ===\n");
        
        List<String> items = new ArrayList<>();
        items.add("笔记本电脑");
        items.add("无线鼠标");
        
        Order order = new Order("ORD-2024-001", "CUST-001", items, 5999.0);
        orderService.createOrder(order);
        
        System.out.println("\n✅ 订单处理完成！");
    }
}
```

运行结果：
```
=== 电商订单系统示例 ===

📦 创建订单: ORD-2024-001
📧 发送订单确认邮件给客户: CUST-001
📦 更新库存: [笔记本电脑, 无线鼠标]
💰 记录交易: 金额 5999.0

✅ 订单处理完成！
```

---

## 🎯 总结

恭喜你！你已经完整学习了 EST Event 事件总线系统！

让我们回顾一下重点：

1. **核心概念**：事件类型、事件数据、监听器、事件总线
2. **基本操作**：创建事件总线、订阅事件、发布事件、取消订阅
3. **高级功能**：事件统计、自定义配置、异常处理、异步处理
4. **最佳实践**：事件命名、数据设计、监听器职责、错误处理

事件总线是解耦系统组件的强大工具，好好利用它，你的代码会更加灵活和可维护！

下一章，我们将学习 EST Logging 日志系统，不见不散！🎉
