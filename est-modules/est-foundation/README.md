# EST Foundation 基础设施模块 - 小白从入门到精通

## 目录
1. [什么是 EST Foundation？](#什么是-est-foundation)
2. [快速入门：5分钟上手](#快速入门5分钟上手)
3. [基础篇](#基础篇)
4. [进阶篇](#进阶篇)
5. [最佳实践](#最佳实践)

---

## 什么是 EST Foundation？

### 用大白话理解

EST Foundation 就像是一个"基础设施工具箱"。想象一下你要建房子，需要水电、通讯、监控等基础设施：

**传统方式**：每个应用都要自己写缓存、日志、配置、监控... 重复造轮子！

**EST Foundation 方式**：给你一套完整的基础设施，里面有：
- 💾 **缓存系统** - 多种缓存实现（内存、文件、Redis）
- 📢 **事件总线** - 事件驱动架构
- 📝 **日志框架** - 支持控制台和文件日志
- ⚙️ **配置管理** - 统一的配置管理
- 📊 **监控系统** - 监控和指标收集
- 🔍 **分布式追踪** - 分布式系统追踪支持

### 核心特点

- 🎯 **简单易用** - 几行代码就能启用基础设施
- ⚡ **高性能** - 优化的缓存、日志、监控实现
- 🔧 **灵活扩展** - 可以自定义各种实现
- 🎨 **功能完整** - 缓存、事件、日志、配置、监控、追踪一应俱全

---

## 快速入门：5分钟上手

### 第一步：引入依赖

在你的 Maven pom.xml 中添加：

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-cache</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-logging</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-config</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>
```

### 第二步：你的第一个基础设施应用

```java
import ltd.idcu.est.cache.Cache;
import ltd.idcu.est.cache.memory.Caches;
import ltd.idcu.est.logging.Logger;
import ltd.idcu.est.logging.Loggers;
import ltd.idcu.est.config.Config;
import ltd.idcu.est.config.Configs;

public class FirstFoundationApp {
    public static void main(String[] args) {
        System.out.println("=== EST Foundation 第一个示例 ===\n");
        
        Logger logger = Loggers.getLogger("my-app");
        logger.info("应用启动");
        
        Config config = Configs.load("application.properties");
        String appName = config.getString("app.name", "My App");
        logger.info("应用名称: " + appName);
        
        Cache<String, String> cache = Caches.newMemoryCache();
        cache.put("key", "value");
        logger.info("缓存值: " + cache.get("key").orElse("未找到"));
        
        logger.info("应用运行完成");
    }
}
```

---

## 基础篇

### 1. est-cache 缓存系统

详细文档请参考：[est-cache README](./est-cache/README.md)

#### 基本使用

```java
import ltd.idcu.est.cache.Cache;
import ltd.idcu.est.cache.memory.Caches;

Cache<String, String> cache = Caches.newMemoryCache();

cache.put("user:1", "张三");
cache.put("user:2", "李四");

String user1 = cache.get("user:1").orElse("未找到");
String user3 = cache.get("user:3", "默认值");

System.out.println("缓存大小: " + cache.size());
```

#### TTL 过期

```java
import java.util.concurrent.TimeUnit;

cache.put("temp:1", "临时数据", 10, TimeUnit.SECONDS);
```

### 2. est-event 事件总线

#### 定义事件

```java
import ltd.idcu.est.event.Event;

public class UserCreatedEvent implements Event {
    private final String userId;
    private final String username;
    
    public UserCreatedEvent(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }
    
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
}
```

#### 发布和订阅事件

```java
import ltd.idcu.est.event.EventBus;
import ltd.idcu.est.event.EventBuses;
import ltd.idcu.est.event.EventListener;

EventBus eventBus = EventBuses.create();

eventBus.subscribe(UserCreatedEvent.class, event -> {
    System.out.println("用户创建事件: " + event.getUsername());
});

eventBus.publish(new UserCreatedEvent("123", "张三"));
```

### 3. est-logging 日志框架

#### 使用日志

```java
import ltd.idcu.est.logging.Logger;
import ltd.idcu.est.logging.Loggers;
import ltd.idcu.est.logging.LogLevel;

Logger logger = Loggers.getLogger("my-app");

logger.trace("这是 trace 日志");
logger.debug("这是 debug 日志");
logger.info("这是 info 日志");
logger.warn("这是 warn 日志");
logger.error("这是 error 日志", new Exception("出错了"));
```

#### 配置日志

```java
import ltd.idcu.est.logging.LoggerConfig;
import ltd.idcu.est.logging.console.ConsoleLogger;
import ltd.idcu.est.logging.file.FileLogger;

LoggerConfig config = LoggerConfig.builder()
    .level(LogLevel.INFO)
    .addLogger(new ConsoleLogger())
    .addLogger(new FileLogger("app.log"))
    .build();

Loggers.configure(config);
```

### 4. est-config 配置管理

#### 加载配置

```java
import ltd.idcu.est.config.Config;
import ltd.idcu.est.config.Configs;

Config config = Configs.load("application.properties");

String appName = config.getString("app.name", "My App");
int port = config.getInt("server.port", 8080);
boolean debug = config.getBoolean("debug", false);
double timeout = config.getDouble("timeout", 30.0);
```

#### 多种配置源

```java
import ltd.idcu.est.config.Config;
import ltd.idcu.est.config.Configs;
import ltd.idcu.est.config.source.PropertiesConfigSource;
import ltd.idcu.est.config.source.EnvironmentConfigSource;
import ltd.idcu.est.config.source.SystemConfigSource;

Config config = Configs.builder()
    .addSource(new SystemConfigSource())
    .addSource(new EnvironmentConfigSource())
    .addSource(new PropertiesConfigSource("application.properties"))
    .build();
```

### 5. est-monitor 监控系统

#### 收集指标

```java
import ltd.idcu.est.monitor.Monitor;
import ltd.idcu.est.monitor.Monitors;
import ltd.idcu.est.monitor.Metric;

Monitor monitor = Monitors.create();

monitor.gauge("memory.used", () -> Runtime.getRuntime().totalMemory());
monitor.counter("requests.total").increment();
monitor.timer("request.time").record(100);
```

#### 导出指标

```java
import ltd.idcu.est.monitor.Monitor;
import ltd.idcu.est.monitor.MetricExporter;
import ltd.idcu.est.monitor.exporter.PrometheusExporter;

Monitor monitor = Monitors.create();
MetricExporter exporter = new PrometheusExporter();

String metrics = exporter.export(monitor);
System.out.println(metrics);
```

### 6. est-tracing 分布式追踪

#### 创建和传播追踪

```java
import ltd.idcu.est.tracing.Tracer;
import ltd.idcu.est.tracing.Tracers;
import ltd.idcu.est.tracing.Span;

Tracer tracer = Tracers.create();

try (Span span = tracer.startSpan("my-operation")) {
    span.setAttribute("user.id", "123");
    
    try (Span childSpan = tracer.startSpan("child-operation")) {
        childSpan.setAttribute("step", "1");
    }
}
```

---

## 进阶篇

### 1. 自定义缓存实现

```java
import ltd.idcu.est.cache.Cache;
import ltd.idcu.est.cache.AbstractCache;

public class CustomCache<K, V> extends AbstractCache<K, V> {
    
    @Override
    protected V doGet(K key) {
        // 自定义获取逻辑
        return null;
    }
    
    @Override
    protected void doPut(K key, V value) {
        // 自定义存放逻辑
    }
    
    @Override
    protected void doRemove(K key) {
        // 自定义删除逻辑
    }
}
```

### 2. 异步事件处理

```java
import ltd.idcu.est.event.EventBus;
import ltd.idcu.est.event.EventBuses;
import ltd.idcu.est.event.async.AsyncEventBus;

EventBus eventBus = new AsyncEventBus(Executors.newFixedThreadPool(4));

eventBus.subscribe(UserCreatedEvent.class, event -> {
    // 异步处理事件
});
```

### 3. 配置热更新

```java
import ltd.idcu.est.config.Config;
import ltd.idcu.est.config.Configs;
import ltd.idcu.est.config.watcher.FileConfigWatcher;

Config config = Configs.load("application.properties");
FileConfigWatcher watcher = new FileConfigWatcher(config, Paths.get("application.properties"));

watcher.addListener(newConfig -> {
    System.out.println("配置已更新");
});

watcher.start();
```

---

## 最佳实践

### 1. 合理使用缓存

```java
// ✅ 推荐：缓存热点数据
Cache<String, Product> productCache = Caches.newMemoryCache();
productCache.put("product:1", product);

// ❌ 不推荐：缓存所有数据
// 会占用太多内存
```

### 2. 日志级别使用

```java
// ✅ 推荐：合理使用日志级别
logger.debug("调试信息");
logger.info("重要信息");
logger.warn("警告信息");
logger.error("错误信息", exception);

// ❌ 不推荐：所有都用 info
logger.info("调试信息");
logger.info("重要信息");
```

### 3. 配置优先级

```java
// ✅ 推荐：系统属性 > 环境变量 > 配置文件
Config config = Configs.builder()
    .addSource(new SystemConfigSource())
    .addSource(new EnvironmentConfigSource())
    .addSource(new PropertiesConfigSource("application.properties"))
    .build();
```

---

## 模块结构

```
est-foundation/
├── est-cache/      # 缓存系统（内存、文件、Redis）
├── est-event/      # 事件总线
├── est-logging/    # 日志框架（控制台、文件）
├── est-config/     # 配置管理
├── est-monitor/    # 监控系统
└── est-tracing/    # 分布式追踪
```

---

## 相关资源

- [est-cache README](./est-cache/README.md) - 缓存详细文档
- [示例代码](../../est-examples/est-examples-basic/) - 基础示例
- [EST Core](../../est-core/README.md) - 核心模块

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
