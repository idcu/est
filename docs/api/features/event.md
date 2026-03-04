# Event 事件总线 API

事件总线提供本地和异步事件处理能力，支持虚拟线程。

## 核心接口

```java
public interface EventBus {
    <T> void subscribe(Class<T> eventType, EventListener<T> listener);
    <T> void unsubscribe(Class<T> eventType, EventListener<T> listener);
    <T> void publish(T event);
    EventStats stats();
}
```

## 本地事件总线 (LocalEventBus)

同步处理事件，适合单机应用。

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

// 创建本地事件总线
EventBus eventBus = LocalEvents.create();

// 订阅事件
eventBus.subscribe(String.class, event -> {
    System.out.println("Received: " + event);
});

// 发布事件
eventBus.publish("Hello, World!");
```

## 异步事件总线 (AsyncEventBus)

异步处理事件，支持虚拟线程。

```java
import ltd.idcu.est.features.event.async.AsyncEvents;

// 创建异步事件总线
EventBus asyncBus = AsyncEvents.create();

// 使用虚拟线程
EventBus virtualThreadBus = AsyncEvents.createWithVirtualThreads();

// 订阅异步事件
asyncBus.subscribe(UserCreatedEvent.class, event -> {
    // 异步处理
    sendWelcomeEmail(event.getUser());
});

// 发布事件（异步处理）
asyncBus.publish(new UserCreatedEvent(user));
```

## 事件统计

```java
EventStats stats = eventBus.stats();
System.out.println("Published: " + stats.getPublishedCount());
System.out.println("Handled: " + stats.getHandledCount());
```

## 自定义事件

```java
public class OrderCreatedEvent {
    private final String orderId;
    private final BigDecimal amount;
    
    // constructor, getters
}

// 订阅自定义事件
eventBus.subscribe(OrderCreatedEvent.class, event -> {
    processOrder(event.getOrderId());
});
```
