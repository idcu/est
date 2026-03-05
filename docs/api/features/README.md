# 功能模块 API

功能模块提供各种常用的应用功能，包括缓存、事件、日志、数据访问等。

## 模块列表

| 模块 | 说明 |
|------|------|
| [Cache](./cache.md) | 缓存系统（内存、文件、Redis） |
| [Event](./event.md) | 事件总线（本地、异步） |
| [Logging](./logging.md) | 日志系统（控制台、文件） |
| [Data](./data.md) | 数据访问（JDBC、内存、MongoDB、Redis） |
| [Security](./security.md) | 安全认证（基础、JWT、OAuth2） |
| [Scheduler](./scheduler.md) | 调度系统（Cron、固定间隔） |
| [Monitor](./monitor.md) | 监控系统（JVM、系统） |
| [Messaging](./messaging.md) | 消息系统（ActiveMQ、Kafka、RabbitMQ、Redis等） |
| [AI](./ai.md) | AI助手（快速参考、代码生成、最佳实践） |

## 通用设计模式

每个功能模块都遵循统一的结构：

```
est-features-xxx/
├── est-features-xxx-api/      # 接口定义
└── est-features-xxx-yyy/      # 具体实现
```

## 使用示例

### 缓存

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.MemoryCaches;

Cache<String, String> cache = MemoryCaches.create();
cache.put("key", "value", 3600);
String value = cache.get("key");
```

### 事件

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

EventBus eventBus = LocalEvents.create();
eventBus.subscribe(String.class, event -> {
    System.out.println("Received: " + event);
});
eventBus.publish("Hello!");
```

### 日志

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

Logger logger = ConsoleLogs.create(MyClass.class);
logger.info("Hello, logging!");
```
