# EST Microservices 微服务模块 - 小白从入门到精通

## 目录
1. [什么是 EST Microservices？](#什么是-est-microservices)
2. [快速入门：5分钟上手](#快速入门5分钟上手)
3. [基础篇](#基础篇)
4. [进阶篇](#进阶篇)
5. [最佳实践](#最佳实践)

---

## 什么是 EST Microservices？

### 用大白话理解

EST Microservices 就像是一个"微服务工具箱"。想象一下你有一个大型系统，拆分成多个小服务：

**传统方式**：服务之间找不到对方，一个服务挂了整个系统都瘫了，不知道性能怎么样... 很麻烦！

**EST Microservices 方式**：给你一套完整的微服务工具，里面有：
- 📡 **服务发现** - 服务之间能自动找到对方
- 🛡️ **熔断器** - 一个服务挂了不会影响其他服务
- 📊 **性能监控** - 实时监控服务的性能指标

### 核心特点

- 🎯 **简单易用** - 几行代码就能启用微服务功能
- ⚡ **高性能** - 基于高性能的服务发现和熔断实现
- 🔧 **灵活扩展** - 可以自定义服务发现策略和熔断规则
- 🎨 **功能完整** - 服务发现、熔断器、性能监控一应俱全

---

## 快速入门：5分钟上手

### 第一步：引入依赖

在你的 Maven pom.xml 中添加：

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-discovery</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-circuitbreaker</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-performance</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>
```

### 第二步：你的第一个微服务应用

```java
import ltd.idcu.est.discovery.ServiceDiscovery;
import ltd.idcu.est.discovery.ServiceInstance;
import ltd.idcu.est.discovery.ServiceRegistry;
import ltd.idcu.est.circuitbreaker.CircuitBreaker;
import ltd.idcu.est.performance.PerformanceMonitor;

public class FirstMicroserviceApp {
    public static void main(String[] args) {
        System.out.println("=== EST Microservices 第一个示例 ===\n");
        
        ServiceRegistry registry = ServiceRegistry.create();
        ServiceInstance instance = ServiceInstance.builder()
            .serviceName("user-service")
            .host("localhost")
            .port(8081)
            .build();
        registry.register(instance);
        System.out.println("服务已注册: user-service");
        
        ServiceDiscovery discovery = ServiceDiscovery.create();
        ServiceInstance found = discovery.findOne("user-service");
        System.out.println("发现服务: " + found.getHost() + ":" + found.getPort());
        
        CircuitBreaker cb = CircuitBreaker.create("user-service-call");
        String result = cb.call(() -> {
            return "调用成功！";
        });
        System.out.println("熔断器调用结果: " + result);
        
        PerformanceMonitor monitor = PerformanceMonitor.create();
        monitor.start("api-call");
        Thread.sleep(100);
        monitor.stop("api-call");
        System.out.println("性能监控: " + monitor.getStats("api-call"));
    }
}
```

---

## 基础篇

### 1. est-discovery 服务发现

#### 注册服务

```java
import ltd.idcu.est.discovery.ServiceRegistry;
import ltd.idcu.est.discovery.ServiceInstance;

ServiceRegistry registry = ServiceRegistry.create();

ServiceInstance instance = ServiceInstance.builder()
    .serviceName("user-service")
    .instanceId("user-service-1")
    .host("192.168.1.100")
    .port(8081)
    .healthCheckUrl("http://192.168.1.100:8081/health")
    .metadata(Map.of(
        "version", "1.0.0",
        "zone", "us-east-1"
    ))
    .build();

registry.register(instance);
System.out.println("服务已注册");
```

#### 发现服务

```java
import ltd.idcu.est.discovery.ServiceDiscovery;
import ltd.idcu.est.discovery.ServiceInstance;
import ltd.idcu.est.collection.api.Seq;

ServiceDiscovery discovery = ServiceDiscovery.create();

ServiceInstance one = discovery.findOne("user-service");
System.out.println("找到一个实例: " + one.getHost() + ":" + one.getPort());

Seq<ServiceInstance> all = discovery.findAll("user-service");
System.out.println("找到 " + all.size() + " 个实例");
all.forEach(instance -> {
    System.out.println("- " + instance.getHost() + ":" + instance.getPort());
});
```

#### 心跳和健康检查

```java
import ltd.idcu.est.discovery.ServiceRegistry;
import ltd.idcu.est.discovery.Heartbeat;

ServiceRegistry registry = ServiceRegistry.create();
Heartbeat heartbeat = registry.createHeartbeat(instance, 30); // 30秒心跳
heartbeat.start();

Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    heartbeat.stop();
    registry.deregister(instance);
}));
```

### 2. est-circuitbreaker 熔断器

#### 创建熔断器

```java
import ltd.idcu.est.circuitbreaker.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.CircuitBreakerConfig;

CircuitBreakerConfig config = CircuitBreakerConfig.builder()
    .failureThreshold(5) // 5次失败后打开熔断器
    .waitDuration(30000) // 30秒后尝试半开状态
    .successThreshold(3) // 3次成功后关闭熔断器
    .build();

CircuitBreaker cb = CircuitBreaker.create("user-service-call", config);
```

#### 使用熔断器

```java
import ltd.idcu.est.circuitbreaker.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.CircuitBreakerException;

CircuitBreaker cb = CircuitBreaker.create("user-service-call");

try {
    String result = cb.call(() -> {
        return userService.getUser(userId);
    });
    System.out.println("调用成功: " + result);
} catch (CircuitBreakerException e) {
    System.out.println("熔断器已打开，使用降级方案");
    return getFallbackUser(userId);
}
```

#### 熔断器状态

```java
import ltd.idcu.est.circuitbreaker.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.CircuitState;

CircuitBreaker cb = CircuitBreaker.create("user-service-call");

CircuitState state = cb.getState();
System.out.println("熔断器状态: " + state); // CLOSED, OPEN, HALF_OPEN

if (state == CircuitState.OPEN) {
    System.out.println("熔断器已打开，服务不可用");
}
```

### 3. est-performance 性能监控

#### 监控方法执行时间

```java
import ltd.idcu.est.performance.PerformanceMonitor;
import ltd.idcu.est.performance.PerformanceStats;

PerformanceMonitor monitor = PerformanceMonitor.create();

monitor.start("getUser");
User user = userService.getUser(userId);
monitor.stop("getUser");

monitor.start("getOrder");
Order order = orderService.getOrder(orderId);
monitor.stop("getOrder");
```

#### 获取性能统计

```java
import ltd.idcu.est.performance.PerformanceMonitor;
import ltd.idcu.est.performance.PerformanceStats;

PerformanceMonitor monitor = PerformanceMonitor.create();

PerformanceStats stats = monitor.getStats("getUser");
System.out.println("调用次数: " + stats.getCount());
System.out.println("平均耗时: " + stats.getAverageTime() + "ms");
System.out.println("最大耗时: " + stats.getMaxTime() + "ms");
System.out.println("最小耗时: " + stats.getMinTime() + "ms");
System.out.println("总耗时: " + stats.getTotalTime() + "ms");
```

#### 导出性能数据

```java
import ltd.idcu.est.performance.PerformanceMonitor;
import ltd.idcu.est.performance.PerformanceExporter;

PerformanceMonitor monitor = PerformanceMonitor.create();
PerformanceExporter exporter = new PerformanceExporter();

String json = exporter.exportToJson(monitor);
System.out.println(json);

exporter.exportToCsv(monitor, Paths.get("performance.csv"));
```

---

## 进阶篇

### 1. 自定义服务发现

```java
import ltd.idcu.est.discovery.ServiceDiscovery;
import ltd.idcu.est.discovery.ServiceInstance;
import ltd.idcu.est.discovery.LoadBalancer;

public class CustomLoadBalancer implements LoadBalancer {
    @Override
    public ServiceInstance choose(Seq<ServiceInstance> instances) {
        return instances
            .sortBy(instance -> instance.getMetadata().getOrDefault("weight", "1"))
            .first()
            .orElse(null);
    }
}

ServiceDiscovery discovery = ServiceDiscovery.create();
discovery.setLoadBalancer(new CustomLoadBalancer());
```

### 2. 自定义熔断规则

```java
import ltd.idcu.est.circuitbreaker.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.FallbackStrategy;

public class CustomFallbackStrategy implements FallbackStrategy {
    @Override
    public Object fallback(String operation, Throwable error) {
        if ("getUser".equals(operation)) {
            return new User("fallback", "Fallback User");
        }
        if ("getOrder".equals(operation)) {
            return new Order("fallback", "Fallback Order");
        }
        return null;
    }
}

CircuitBreaker cb = CircuitBreaker.create("user-service-call");
cb.setFallbackStrategy(new CustomFallbackStrategy());
```

### 3. 性能监控集成

```java
import ltd.idcu.est.performance.PerformanceMonitor;
import ltd.idcu.est.performance.PerformanceReporter;
import ltd.idcu.est.monitor.MetricRegistry;

public class PrometheusPerformanceReporter implements PerformanceReporter {
    private final MetricRegistry registry;
    
    public PrometheusPerformanceReporter(MetricRegistry registry) {
        this.registry = registry;
    }
    
    @Override
    public void report(PerformanceMonitor monitor) {
        monitor.getAllStats().forEach((name, stats) -> {
            registry.gauge("performance." + name + ".count", () -> stats.getCount());
            registry.gauge("performance." + name + ".avg", () -> stats.getAverageTime());
            registry.gauge("performance." + name + ".max", () -> stats.getMaxTime());
        });
    }
}

PerformanceMonitor monitor = PerformanceMonitor.create();
monitor.setReporter(new PrometheusPerformanceReporter(registry));
```

---

## 最佳实践

### 1. 服务命名规范

```java
// ✅ 推荐：使用有意义的服务名
ServiceInstance.builder()
    .serviceName("user-service")
    .instanceId("user-service-" + UUID.randomUUID())
    .build();

// ❌ 不推荐：模糊的服务名
ServiceInstance.builder()
    .serviceName("service1")
    .instanceId("instance1")
    .build();
```

### 2. 合理设置熔断器参数

```java
// ✅ 推荐：根据实际情况调整
CircuitBreakerConfig config = CircuitBreakerConfig.builder()
    .failureThreshold(10)
    .waitDuration(60000)
    .successThreshold(5)
    .build();

// ❌ 不推荐：太敏感或太迟钝
CircuitBreakerConfig badConfig = CircuitBreakerConfig.builder()
    .failureThreshold(1) // 1次失败就熔断，太敏感
    .waitDuration(3600000) // 1小时才尝试恢复，太久
    .build();
```

### 3. 性能监控关键指标

```java
// ✅ 推荐：监控关键操作
monitor.start("db.query");
monitor.start("api.call");
monitor.start("cache.get");

// ❌ 不推荐：监控太多细枝末节
monitor.start("for.loop.iteration");
monitor.start("variable.assignment");
```

### 4. 服务健康检查

```java
// ✅ 推荐：实现健康检查
ServiceInstance.builder()
    .healthCheckUrl("http://localhost:8080/health")
    .build();

// ❌ 不推荐：不提供健康检查
ServiceInstance.builder()
    .build();
```

---

## 模块结构

```
est-microservices/
├── est-discovery/      # 服务发现
├── est-circuitbreaker/ # 熔断器模式
└── est-performance/    # 性能监控
```

---

## 相关资源

- [est-discovery README](./est-discovery/README.md) - 服务发现详细文档
- [est-circuitbreaker README](./est-circuitbreaker/README.md) - 熔断器详细文档
- [示例代码](../../est-examples/est-examples-microservices/) - 微服务示例代码
- [EST Web Group](../est-web-group/README.md) - Web 模块
- [EST Core](../../est-core/README.md) - 核心模块

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
