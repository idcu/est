# EST Features 功能模块

企业级功能组件集合，提供缓存、事件、日志、数据访问、安全、调度等常用功能。

## 模块列表

| 模块 | 说明 |
|------|------|
| [est-features-cache](./est-features-cache/) | 缓存系统（内存、文件、Redis） |
| [est-features-event](./est-features-event/) | 事件总线（本地、异步） |
| [est-features-logging](./est-features-logging/) | 日志系统（控制台、文件） |
| [est-features-data](./est-features-data/) | 数据访问（JDBC、内存、Redis、MongoDB） |
| [est-features-security](./est-features-security/) | 安全认证（Basic、JWT、API Key、OAuth2） |
| [est-features-messaging](./est-features-messaging/) | 消息系统（本地、AMQP、MQTT） |
| [est-features-monitor](./est-features-monitor/) | 监控系统（JVM、系统） |
| [est-features-scheduler](./est-features-scheduler/) | 调度系统（Cron、固定间隔） |

## 通用设计模式

每个功能模块都遵循统一的结构：

```
est-features-xxx/
├── est-features-xxx-api/      # 接口定义
└── est-features-xxx-yyy/      # 具体实现
```

## 快速示例

### 缓存

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.MemoryCaches;

Cache<String, String> cache = MemoryCaches.create();
cache.put("key", "value");
```

### 事件

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

EventBus eventBus = LocalEvents.create();
eventBus.subscribe(String.class, event -> System.out.println(event));
eventBus.publish("Hello!");
```

### 日志

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

Logger logger = ConsoleLogs.create(MyClass.class);
logger.info("Hello, logging!");
```

## 相关文档

- [API 文档](../docs/api/features/)
