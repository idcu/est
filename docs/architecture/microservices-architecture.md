# EST 框架微服务架构设计

## 1. 架构概述

### 1.1 设计原则

- **零依赖优先**：核心微服务基础设施保持零依赖，可选引入第三方组件
- **渐进式迁移**：与现有架构完全兼容，支持逐步迁移
- **可插拔设计**：微服务组件可根据需要选择性使用
- **接口抽象**：所有微服务功能通过清晰的接口定义
- **API优先**：基于RESTful API和事件驱动的服务间通信

### 1.2 架构目标

- 服务独立部署和扩展
- 服务发现与注册
- 负载均衡
- 容错与熔断
- 分布式追踪
- 统一配置管理
- API网关统一入口

## 2. 整体架构图

```
┌─────────────────────────────────────────────────────────────┐
│                         客户端层                              │
│              (Web, Mobile, IoT, Third-party)                │
└──────────────────────────────┬──────────────────────────────┘
                               │
┌──────────────────────────────▼──────────────────────────────┐
│                        API 网关层                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │  路由转发    │  │  负载均衡    │  │  安全认证    │     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │  限流熔断    │  │  日志聚合    │  │  监控追踪    │     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
└──────────────────────────────┬──────────────────────────────┘
                               │
           ┌───────────────────┼───────────────────┐
           │                   │                   │
┌──────────▼──────────┐ ┌─────▼──────┐ ┌────────▼──────────┐
│   服务发现与注册    │ │ 配置中心    │ │  消息中间件        │
│   (Discovery)       │ │ (Config)    │ │  (Messaging)       │
└──────────┬──────────┘ └─────┬──────┘ └────────┬──────────┘
           │                   │                   │
           └───────────────────┼───────────────────┘
                               │
        ┌──────────────────────┼──────────────────────┐
        │                      │                      │
   ┌────▼─────┐          ┌────▼─────┐          ┌────▼─────┐
   │ 用户服务  │          │ 订单服务  │          │ 支付服务  │
   │ (User)   │          │ (Order)   │          │ (Payment) │
   └────┬─────┘          └────┬─────┘          └────┬─────┘
        │                      │                      │
        └──────────────────────┼──────────────────────┘
                               │
                    ┌──────────▼──────────┐
                    │  数据访问层          │
                    │  (JDBC/Mongo/Redis) │
                    └─────────────────────┘
```

## 3. 模块结构设计

### 3.1 新增顶层模块

```
est1.3/
├── est-microservices/              # 微服务基础设施（新增）
│   ├── est-microservices-api/      # 微服务核心接口
│   ├── est-microservices-discovery/ # 服务发现与注册
│   ├── est-microservices-gateway/   # API网关
│   ├── est-microservices-config/    # 分布式配置中心
│   ├── est-microservices-circuit/   # 熔断器
│   ├── est-microservices-tracing/   # 分布式追踪
│   ├── est-microservices-loadbalancer/ # 负载均衡
│   └── pom.xml
├── ... (现有模块保持不变)
```

### 3.2 微服务基础设施模块详解

#### 3.2.1 est-microservices-api - 微服务核心接口

**接口定义：**

```java
// 服务实例信息
public interface ServiceInstance {
    String getServiceId();
    String getInstanceId();
    String getHost();
    int getPort();
    Map<String, String> getMetadata();
    boolean isHealthy();
}

// 服务注册接口
public interface ServiceRegistry {
    void register(ServiceInstance instance);
    void deregister(String instanceId);
    void heartbeat(String instanceId);
}

// 服务发现接口
public interface ServiceDiscovery {
    List<ServiceInstance> getInstances(String serviceId);
    List<String> getServices();
}

// 服务配置接口
public interface ServiceConfig {
    String get(String key);
    String get(String key, String defaultValue);
    void watch(String key, ConfigChangeListener listener);
}

// 断路器接口
public interface CircuitBreaker {
    <T> T execute(Supplier<T> supplier, Supplier<T> fallback);
    CircuitBreaker.State getState();
    void reset();
}
```

#### 3.2.2 est-microservices-discovery - 服务发现与注册

**实现方案：**

- **零依赖实现**：基于 HTTP 的简单服务注册中心
- **可选集成**：支持 Consul、Eureka、Nacos 等（可选依赖）
- **本地缓存**：服务列表本地缓存，减少网络请求
- **健康检查**：定期检查服务健康状态

**核心组件：**

```java
// 服务注册中心实现
public class DefaultServiceRegistry implements ServiceRegistry {
    private final Map<String, ServiceInstance> instances = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    public DefaultServiceRegistry() {
        // 启动健康检查
        scheduler.scheduleAtFixedRate(this::checkHealth, 10, 30, TimeUnit.SECONDS);
    }
    
    @Override
    public void register(ServiceInstance instance) {
        instances.put(instance.getInstanceId(), instance);
    }
    
    private void checkHealth() {
        // 实现健康检查逻辑
    }
}

// 服务发现客户端
public class DefaultServiceDiscovery implements ServiceDiscovery {
    private final ServiceRegistry registry;
    
    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        return registry.getInstances().stream()
            .filter(i -> i.getServiceId().equals(serviceId))
            .filter(ServiceInstance::isHealthy)
            .collect(Collectors.toList());
    }
}
```

#### 3.2.3 est-microservices-gateway - API网关

**核心功能：**

- 路由转发
- 负载均衡
- 安全认证
- 限流熔断
- 请求/响应转换
- 日志记录

**路由配置示例：**

```java
public class GatewayRouter {
    private final Map<String, Route> routes = new ConcurrentHashMap<>();
    
    public void addRoute(String path, String serviceId) {
        routes.put(path, new Route(path, serviceId));
    }
    
    public Route match(String path) {
        return routes.entrySet().stream()
            .filter(e -> path.startsWith(e.getKey()))
            .map(Map.Entry::getValue)
            .findFirst()
            .orElse(null);
    }
}
```

#### 3.2.4 est-microservices-config - 分布式配置中心

**特性：**

- 配置分层（应用、环境、版本）
- 配置热更新
- 配置版本管理
- 配置加密

**实现：**

```java
public class DefaultServiceConfig implements ServiceConfig {
    private final Map<String, String> configs = new ConcurrentHashMap<>();
    private final Map<String, List<ConfigChangeListener>> listeners = new ConcurrentHashMap<>();
    
    @Override
    public void watch(String key, ConfigChangeListener listener) {
        listeners.computeIfAbsent(key, k -> new CopyOnWriteArrayList<>())
                 .add(listener);
    }
    
    public void update(String key, String value) {
        configs.put(key, value);
        notifyListeners(key, value);
    }
}
```

#### 3.2.5 est-microservices-circuit - 熔断器

**状态机：**

```
CLOSED → OPEN → HALF_OPEN → CLOSED
         ↓              ↑
      失败计数       半开测试
```

**实现：**

```java
public class DefaultCircuitBreaker implements CircuitBreaker {
    private enum State { CLOSED, OPEN, HALF_OPEN }
    
    private volatile State state = State.CLOSED;
    private final int failureThreshold;
    private final long timeoutMillis;
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private volatile long lastFailureTime = 0;
    
    @Override
    public <T> T execute(Supplier<T> supplier, Supplier<T> fallback) {
        if (state == State.OPEN) {
            if (System.currentTimeMillis() - lastFailureTime > timeoutMillis) {
                state = State.HALF_OPEN;
            } else {
                return fallback.get();
            }
        }
        
        try {
            T result = supplier.get();
            onSuccess();
            return result;
        } catch (Exception e) {
            onFailure();
            return fallback.get();
        }
    }
    
    private void onSuccess() {
        failureCount.set(0);
        state = State.CLOSED;
    }
    
    private void onFailure() {
        int count = failureCount.incrementAndGet();
        lastFailureTime = System.currentTimeMillis();
        if (count >= failureThreshold) {
            state = State.OPEN;
        }
    }
}
```

#### 3.2.6 est-microservices-tracing - 分布式追踪

**核心概念：**

- Trace：一次完整的分布式调用链
- Span：单个服务内的调用片段
- Context：传递的追踪上下文

**实现：**

```java
public class DefaultTracer implements Tracer {
    private final ThreadLocal<SpanContext> currentSpan = new ThreadLocal<>();
    
    @Override
    public Span startSpan(String name) {
        SpanContext parent = currentSpan.get();
        SpanContext context = new SpanContext(
            generateTraceId(parent),
            generateSpanId(),
            parent != null ? parent.getSpanId() : null
        );
        currentSpan.set(context);
        return new DefaultSpan(name, context, this);
    }
    
    @Override
    public void finishSpan(Span span) {
        // 记录 span 数据
        currentSpan.set(null);
    }
}
```

#### 3.2.7 est-microservices-loadbalancer - 负载均衡

**策略：**

- Round Robin（轮询）
- Random（随机）
- Weighted（权重）
- Consistent Hash（一致性哈希）

**实现：**

```java
public interface LoadBalancer {
    ServiceInstance choose(List<ServiceInstance> instances);
}

public class RoundRobinLoadBalancer implements LoadBalancer {
    private final AtomicInteger counter = new AtomicInteger(0);
    
    @Override
    public ServiceInstance choose(List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            return null;
        }
        int index = Math.abs(counter.getAndIncrement() % instances.size());
        return instances.get(index);
    }
}
```

## 4. 服务间通信

### 4.1 同步通信（RESTful）

**服务调用客户端：**

```java
public interface ServiceClient {
    <T> T get(String serviceId, String path, Class<T> responseType);
    <T> T post(String serviceId, String path, Object body, Class<T> responseType);
    <T> T put(String serviceId, String path, Object body, Class<T> responseType);
    void delete(String serviceId, String path);
}

public class DefaultServiceClient implements ServiceClient {
    private final ServiceDiscovery discovery;
    private final LoadBalancer loadBalancer;
    
    @Override
    public <T> T get(String serviceId, String path, Class<T> responseType) {
        List<ServiceInstance> instances = discovery.getInstances(serviceId);
        ServiceInstance instance = loadBalancer.choose(instances);
        String url = "http://" + instance.getHost() + ":" + instance.getPort() + path;
        // 执行 HTTP GET 请求
        return executeRequest("GET", url, null, responseType);
    }
}
```

### 4.2 异步通信（消息驱动）

**基于现有 est-features-messaging 扩展：**

```java
// 事件发布
public class EventPublisher {
    private final MessageProducer producer;
    
    public void publish(String topic, Object event) {
        String payload = JsonUtils.toJson(event);
        Message message = new DefaultMessage(payload, Map.of(
            "event-type", event.getClass().getName(),
            "timestamp", String.valueOf(System.currentTimeMillis())
        ));
        producer.send(topic, message);
    }
}

// 事件监听
public interface EventListener<T> {
    void onEvent(T event);
    Class<T> getEventType();
}
```

## 5. 现有模块增强

### 5.1 est-features-security 增强

**新增微服务安全功能：**

- JWT 令牌传递
- 服务间认证
- API Key 管理
- OAuth2 集成

```java
public interface ServiceAuthenticator {
    String authenticate(ServiceInstance instance);
    boolean validate(String token);
}
```

### 5.2 est-features-monitor 增强

**新增微服务监控功能：**

- 服务健康指标
- 请求延迟统计
- 错误率监控
- 资源使用监控

### 5.3 est-web 增强

**新增微服务 Web 支持：**

- 服务健康检查端点
- 元数据端点
- 服务注册自动配置

```java
// 健康检查端点
app.get("/health", (req, res) -> {
    res.json(Map.of(
        "status", "UP",
        "serviceId", serviceConfig.getServiceId(),
        "version", serviceConfig.getVersion(),
        "timestamp", System.currentTimeMillis()
    ));
});
```

## 6. 与现有架构兼容性

### 6.1 保持现有架构不变

- 所有现有模块保持完全兼容
- 微服务功能为可选组件
- 不强制迁移现有应用

### 6.2 适配器层

**提供适配器，让现有应用逐步使用微服务功能：**

```java
// 兼容层 - 现有代码无需修改即可使用微服务
public class MicroserviceAdapter {
    public static void enableMicroservices(WebApplication app) {
        // 自动配置服务注册
        // 添加健康检查端点
        // 配置服务发现客户端
    }
}
```

### 6.3 渐进式迁移路径

1. **阶段 1**：单体应用，使用微服务基础设施
2. **阶段 2**：拆分部分模块为独立服务
3. **阶段 3**：完全微服务化

## 7. 部署架构

### 7.1 Docker 支持

**微服务 Dockerfile 模板：**

```dockerfile
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 7.2 Kubernetes 部署

**微服务 Deployment 示例：**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: user-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: user-service
  template:
    metadata:
      labels:
        app: user-service
    spec:
      containers:
      - name: user-service
        image: user-service:latest
        ports:
        - containerPort: 8080
        livenessProbe:
          httpGet:
            path: /health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /health
            port: 8080
          initialDelaySeconds: 5
          periodSeconds: 5
```

## 8. 最佳实践

### 8.1 服务设计原则

- 单一职责原则
- 独立部署原则
- 数据隔离原则
- 容错设计原则

### 8.2 API 设计规范

- RESTful API 设计
- 版本管理
- 错误处理
- 文档规范

### 8.3 数据管理

- 数据库 per 服务
- 最终一致性
- 事件溯源
- CQRS 模式

## 9. 总结

EST 框架的微服务架构设计遵循以下核心原则：

1. **零依赖优先**：核心基础设施保持零依赖
2. **渐进式迁移**：与现有架构完全兼容
3. **可插拔设计**：按需使用微服务功能
4. **接口抽象**：清晰的接口定义，便于扩展

通过这套架构设计，EST 框架可以：
- 支持从小型应用到大型微服务系统的演进
- 保持开发体验的一致性
- 提供企业级的微服务能力
- 确保与现有投资的兼容性
