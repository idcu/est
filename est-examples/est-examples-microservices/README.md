# EST 微服务示例
这是一个完整的微服务架构示例，展示了如何使用 EST Framework 快速构建微服务应用。

## 架构概述

```
                    ┌─────────────────┐
                    │  API Gateway   │
                    │  (port 8080)   │
                    └────────┬────────┘
                             │
              ┌──────────────┼──────────────┐
              │              │              │
         ┌────▼────┐  ┌────▼────┐  ┌────▼────┐
         │User    │  │Order   │  │ ...    │
         │Service │  │Service │  │        │
         │8081)   │  │8082)   │  │        │
         └─────────┘  └─────────┘  └─────────┘
```

## 模块说明

### 1. API Gateway (`est-examples-microservices-gateway`)
- 统一入口，路由转发
- CORS 支持
- 请求日志
- 限流保护（令牌桶算法）
- 熔断保护
- HTTPS/TLS 支持
- WebSocket 支持（端口 8081）
- 金丝雀发布/流量切分
- 端口：8080

### 2. User Service (`est-examples-microservices-user`)
- 用户管理服务
- REST API：`/users`
- 端口：8081

### 3. Order Service (`est-examples-microservices-order`)
- 订单管理服务
- REST API：`/orders`
- 端口：8082

## 快速开始
### 编译项目

```bash
cd est-examples/est-examples-microservices
mvn clean install
```

### 启动服务

**1. 启动 User Service（终端 1）：**
```bash
cd est-examples-microservices-user
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.user.UserServiceApp"
```

**2. 启动 Order Service（终端 2）：**
```bash
cd est-examples-microservices-order
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.order.OrderServiceApp"
```

**3. 启动 API Gateway（终端 3）：**
```bash
cd est-examples-microservices-gateway
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.gateway.GatewayApp"
```

**4. 启动增强版 API Gateway（展示新功能）：**
```bash
cd est-examples-microservices-gateway
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.gateway.EnhancedGatewayApp"
```

## API 测试

### 通过网关访问（推荐）

```bash
# 获取所有用户
curl http://localhost:8080/api/users

# 获取单个用户
curl http://localhost:8080/api/users/1

# 获取所有订单
curl http://localhost:8080/api/orders

# 获取单个订单
curl http://localhost:8080/api/orders/1

# 创建订单
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"userId":"1","productName":"iPad","quantity":1,"price":3999}'
```

### 金丝雀发布测试

```bash
# 正常请求（大部分流量）
curl http://localhost:8080/api/users

# 强制使用金丝雀版本（通过 Header）
curl -H "X-Canary: true" http://localhost:8080/api/users
```

### WebSocket 聊天测试

使用浏览器或 WebSocket 客户端连接：
```
ws://localhost:8081/ws/chat
```

连接后可以发送消息，消息会广播给所有在线用户。

### 直接访问服务（不通过网关）
```bash
# User Service
curl http://localhost:8081/users
curl http://localhost:8081/users/1

# Order Service
curl http://localhost:8082/orders
curl http://localhost:8082/orders/1
curl http://localhost:8082/orders/user/1
```

## 增强功能说明

### 1. 配置加密

使用 AES 加密敏感配置：

```java
ConfigEncryptor encryptor = new AesConfigEncryptor("my-secret-key");
String encrypted = encryptor.encrypt("db-password-123");
String decrypted = encryptor.decrypt(encrypted);
```

### 2. 配置版本管理

支持配置版本创建、列表、回滚：

```java
ConfigVersionManager versionManager = new DefaultConfigVersionManager();
ConfigVersion v1 = versionManager.createVersion("config-id", configMap, "v1");
ConfigVersion v2 = versionManager.createVersion("config-id", newConfig, "v2");
ConfigVersion rolledBack = versionManager.rollback("config-id", "v1");
```

### 3. WebSocket 支持

支持 WebSocket 实时通信：

```java
gateway.webSocketRoute("/ws/chat", new WebSocketHandler() {
    @Override
    public void onOpen(WebSocketSession session, Map<String, List<String>> headers) {
        // 处理连接
    }
    
    @Override
    public void onMessage(WebSocketSession session, String message) {
        // 处理消息
    }
});
```

### 4. 金丝雀发布/流量切分

支持基于百分比、Header、Cookie、IP 的流量切分：

```java
gateway.withCanaryRelease()
    .registerService("user-service", "v1", "http://localhost:8081")
    .registerService("user-service", "v2", "http://localhost:8083")
    .addCanaryConfig("user-service", "v1", "v2", 10);

CanaryReleaseConfig config = new CanaryReleaseConfig("service", "v1", "v2", 5);
Map<String, List<String>> headers = new HashMap<>();
headers.put("X-Canary", Collections.singletonList("true"));
config.setHeaderMatchers(headers);
gateway.addCanaryConfig(config);
```

### 5. 配置中心持久化

支持配置保存到 Properties、YAML、JSON 文件：

```java
ConfigCenter configCenter = new DefaultConfigCenter();
configCenter.setProperty("app.name", "est-gateway");
configCenter.setProperty("app.version", "2.0.0");

// 保存到 YAML 文件
configCenter.saveToYaml("config.yaml");

// 从 YAML 文件加载
ConfigCenter restored = new DefaultConfigCenter();
restored.loadFromYaml("config.yaml");

// 自动保存
configCenter.setAutoSavePath("auto-save.yaml");
configCenter.setAutoSave(true);
```

### 6. 服务发现持久化

支持服务注册表保存到 JSON 文件：

```java
ServiceRegistry registry = new DefaultServiceRegistry();
registry.register(new ServiceInstance("user-service", "inst-1", "localhost", 8081));
registry.register(new ServiceInstance("order-service", "inst-1", "localhost", 8082));

// 保存到 JSON 文件
registry.saveToJson("services.json");

// 从 JSON 文件加载
ServiceRegistry restored = new DefaultServiceRegistry();
restored.loadFromJson("services.json");

// 自动保存
registry.setAutoSavePath("auto-save-services.json");
registry.setAutoSave(true);
```

### 7. 熔断器状态持久化

支持熔断器状态保存到 JSON 文件：

```java
CircuitBreakerRegistry registry = new DefaultCircuitBreakerRegistry();
registry.create("user-service");
registry.create("order-service");

// 保存到 JSON 文件
((DefaultCircuitBreakerRegistry) registry).saveToJson("circuit-breakers.json");

// 从 JSON 文件加载
CircuitBreakerRegistry restored = new DefaultCircuitBreakerRegistry();
((DefaultCircuitBreakerRegistry) restored).loadFromJson("circuit-breakers.json");
```

### 8. 追踪数据持久化

支持追踪数据异步批量保存到文件：

```java
FileSpanExporter exporter = new FileSpanExporter("traces.jsonl");
Tracer tracer = new DefaultTracer("gateway-service", exporter);

TraceContext span = tracer.startSpan("api-request");
tracer.addTag(span, "path", "/api/users");
tracer.addTag(span, "method", "GET");
tracer.endSpan(span, true);

exporter.flush();

// 加载已保存的追踪数据
List<TraceContext> spans = exporter.loadSpans();
```

## 依赖说明

每个微服务模块按需引入以下 EST 模块：

| 模块 | 用途 |
|------|------|
| `est-web-impl` | Web 框架，提供 REST API 支持 |
| `est-gateway` | API 网关（仅网关服务使用） |
| `est-discovery-api/impl` | 服务发现（支持持久化） |
| `est-config-api/impl` | 配置中心（支持加密、版本管理、持久化） |
| `est-circuitbreaker-api/impl` | 熔断器（支持状态持久化） |
| `est-tracing-api/impl` | 分布式追踪（支持数据持久化） |
| `est-logging-console` | 控制台日志 |

## 扩展建议

要添加新的微服务，只需：
1. 创建新的 Maven 模块
2. 引入 `est-web-impl` 依赖
3. 编写 `@RestController`
4. 启动服务
5. 在网关中配置路由

示例：
```java
@RestController
public class ProductController {
    @Get("/products")
    public List<Product> getAllProducts() {
        // ...
    }
}
```
