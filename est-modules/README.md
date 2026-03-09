# EST Modules 功能模块 - 小白从入门到精通

## 目录
1. [什么是 EST Modules？](#什么是-est-modules)
2. [快速入门：5分钟上手](#快速入门5分钟上手)
3. [模块概览](#模块概览)
4. [最佳实践](#最佳实践)

---

## 什么是 EST Modules？

### 用大白话理解

EST Modules 就像是一个「功能超市」。想象一下你要做饭，需要各种食材和调料：蔬菜、肉类、油盐酱醋...

**传统方式**：每次做饭都要自己采购所有东西，很麻烦！

**EST Modules 方式**：给你一个装满功能模块的超市，里面有：
- 🧰 **核心设施** - 缓存、事件、日志、配置、监控、追踪
- 📊 **数据访问** - 数据存储、工作流编排
- 🔒 **安全权限** - 认证授权、RBAC、审计
- 📱 **消息集成** - 消息队列、系统集成
- 🏪 **Web 容器** - 路由、网关、中间件
- 🤖 **AI 套件** - AI 助手、LLM 集成
- 🔄 **微服务** - 断路器、服务发现、性能优化
- 🧩 **扩展功能** - 插件、调度、热加载

### 核心特点

- 📦 **模块化设计** - 按需引入，灵活组合
- 🔧 **开箱即用** - 配置常用功能
- 👨‍💻 **可扩展** - 可以自定义和扩展
- 🔄 **纯 Java** - 纯 Java 实现

---

## 快速入门：5分钟上手

### 第一步：引入依赖

在你的 Maven pom.xml 中添加需要的模块：

```xml
<dependencies>
    <!-- 缓存模块 -->
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-cache</artifactId>
        <version>2.3.0-SNAPSHOT</version>
    </dependency>
    
    <!-- 日志模块 -->
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-logging</artifactId>
        <version>2.3.0-SNAPSHOT</version>
    </dependency>
    
    <!-- 数据访问模块 -->
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-data</artifactId>
        <version>2.3.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### 第二步：使用模块

```java
import ltd.idcu.est.cache.api.Cache;
import ltd.idcu.est.cache.memory.Caches;
import ltd.idcu.est.logging.api.Logger;
import ltd.idcu.est.logging.api.Loggers;
import ltd.idcu.est.data.api.Repository;
import ltd.idcu.est.data.memory.MemoryData;

public class FirstExample {
    public static void main(String[] args) {
        System.out.println("=== EST Modules 第一个示例 ===\n");
        
        // 1. 使用缓存
        Cache<String, String> cache = Caches.newMemoryCache();
        cache.put("key", "value");
        System.out.println("缓存: " + cache.get("key").orElse("null"));
        
        // 2. 使用日志
        Logger logger = Loggers.getLogger(FirstExample.class);
        logger.info("这是一条日志");
        
        // 3. 使用数据访问
        Repository<User, Long> repo = MemoryData.newRepository();
        User user = new User();
        user.setName("张三");
        repo.save(user);
        System.out.println("用户: " + repo.findAll());
        
        System.out.println("\n喜欢你！你已经成功使用 EST Modules 了！");
    }
}
```

---

## 模块概览

### 1. est-foundation 核心设施模块

提供企业级应用开发所需的基础设施。
详细文档请参考：[est-foundation README](./est-foundation/README.md)

#### 包含模块

- **est-cache** - 缓存系统（内存、文件、Redis）
- **est-event** - 事件总线（本地、异步）
- **est-logging** - 日志系统（控制台、文件）
- **est-config** - 配置管理
- **est-monitor** - 监控系统（JVM、系统）
- **est-tracing** - 分布式追踪

#### 快速示例

```java
// 缓存
Cache<String, User> userCache = Caches.newMemoryCache();
userCache.put("user:1", user);

// 日志
Logger logger = Loggers.getLogger(MyClass.class);
logger.info("应用启动");

// 事件
EventBus eventBus = EventBus.create();
eventBus.subscribe(UserCreatedEvent.class, event -> {
    System.out.println("用户创建: " + event.getUserId());
});
```

### 2. est-data-group 数据访问模块

提供数据持久化和工作流功能。
详细文档请参考：[est-data-group README](./est-data-group/README.md)

#### 包含模块

- **est-data** - 数据访问（JDBC、内存、MongoDB、Redis）
- **est-workflow** - 工作流引擎

#### 快速示例

```java
// 数据访问
Repository<User, Long> userRepo = MemoryData.newRepository();
User user = userRepo.findById(1L).orElse(null);

// 工作流
WorkflowEngine engine = WorkflowEngine.create();
Workflow workflow = engine.getWorkflow("order-process");
workflow.start(orderData);
```

### 3. est-security-group 安全权限模块

提供认证、授权和审计功能。
详细文档请参考：[est-security-group README](./est-security-group/README.md)

#### 包含模块

- **est-security** - 安全认证（JWT、Basic、OAuth2、API Key）
- **est-rbac** - 基于角色的访问控制
- **est-audit** - 审计日志

#### 快速示例

```java
// 安全认证
JwtService jwtService = JwtService.create();
String token = jwtService.generateToken(user);
User authenticated = jwtService.validateToken(token);

// RBAC
Role adminRole = Role.create("admin");
adminRole.addPermission("user:create");
adminRole.addPermission("user:delete");
```

### 4. est-integration-group 消息集成模块

提供消息队列和系统集成功能。
详细文档请参考：[est-integration-group README](./est-integration-group/README.md)

#### 包含模块

- **est-messaging** - 消息系统（Kafka、RabbitMQ、ActiveMQ、Redis、WebSocket 等）
- **est-integration** - 系统集成

#### 快速示例

```java
// 消息队列
MessageProducer producer = MessageProducer.create("kafka");
producer.send("orders", orderMessage);

MessageConsumer consumer = MessageConsumer.create("kafka");
consumer.subscribe("orders", message -> {
    System.out.println("收到消息: " + message);
});
```

### 5. est-web-group Web 容器模块

提供 Web 开发相关功能。
详细文档请参考：[est-web-group README](./est-web-group/README.md)

#### 包含模块

- **est-web-router** - Web 路由
- **est-web-middleware** - Web 中间件
- **est-web-session** - 会话管理
- **est-web-template** - 模板引擎
- **est-gateway** - API 网关

#### 快速示例

```java
// API 网关
Gateway gateway = Gateway.create();
gateway.route("/api/users", "http://user-service:8080");
gateway.route("/api/orders", "http://order-service:8080");
gateway.start(8080);
```

### 6. est-ai-suite AI 套件模块

提供 AI 和 LLM 相关功能。
详细文档请参考：[est-ai-suite README](./est-ai-suite/README.md)

#### 包含模块

- **est-ai-config** - AI 配置管理
- **est-llm-core** - 核心 LLM 抽象
- **est-llm** - LLM 提供商实现（OpenAI、智谱、自定义问答、文本统一、语音、Dimi、llama）
- **est-ai-assistant** - AI 助手和代码生成

#### 快速示例

```java
// AI 助手
AiAssistant assistant = new DefaultAiAssistant();
String response = assistant.chat("你好，请介绍一下 EST 框架");

// 代码生成
CodeGenerator generator = new DefaultCodeGenerator();
String entityCode = generator.generateEntity("User", "com.example.entity", options);
```

### 7. est-microservices 微服务模块

提供微服务相关功能。
详细文档请参考：[est-microservices README](./est-microservices/README.md)

#### 包含模块

- **est-circuitbreaker** - 断路器
- **est-discovery** - 服务发现
- **est-performance** - 性能优化

#### 快速示例

```java
// 断路器
CircuitBreaker breaker = CircuitBreaker.create();
breaker.execute(() -> {
    return remoteService.call();
});

// 服务发现
ServiceDiscovery discovery = ServiceDiscovery.create();
List<ServiceInstance> instances = discovery.getInstances("user-service");
```

### 8. est-extensions 扩展功能模块

提供扩展功能。
详细文档请参考：[est-extensions README](./est-extensions/README.md)

#### 包含模块

- **est-plugin** - 插件系统
- **est-scheduler** - 调度系统（固定、Cron）
- **est-hotreload** - 热加载

#### 快速示例

```java
// 调度
Scheduler scheduler = Scheduler.create();
scheduler.scheduleAtFixedRate(() -> {
    System.out.println("定时任务执行");
}, 1, TimeUnit.HOURS);

// 插件
PluginManager pluginManager = PluginManager.create();
pluginManager.loadPlugin(Paths.get("./plugins/my-plugin.jar"));
```

---

## 最佳实践

### 1. 按需引入模块

```xml
<!-- ✅ 推荐：只引入需要的模块 -->
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-cache</artifactId>
        <version>2.3.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-logging</artifactId>
        <version>2.3.0-SNAPSHOT</version>
    </dependency>
</dependencies>

<!-- ❌ 不推荐：引入所有模块 -->
<!-- 会增加不必要的依赖和体积 -->
```

### 2. 合理组合使用多个模块

```java
// ✅ 推荐：组合使用多个模块
Cache<String, User> cache = Caches.newMemoryCache();
Logger logger = Loggers.getLogger(MyClass.class);

public User getUser(Long id) {
    logger.info("查询用户: " + id);
    
    User user = cache.get("user:" + id).orElse(null);
    if (user == null) {
        user = repository.findById(id).orElse(null);
        if (user != null) {
            cache.put("user:" + id, user);
        }
    }
    return user;
}
```

### 3. 理解模块依赖关系

```
est-modules/
├── est-foundation/    # 基础层（其他模块可能依赖）
├── est-data-group/    # 数据层
├── est-security-group/
├── est-integration-group/
├── est-web-group/
├── est-ai-suite/
├── est-microservices/
└── est-extensions/
```

---

## 模块结构

```
est-modules/
├── est-foundation/        # 核心设施
├── est-data-group/        # 数据访问
├── est-security-group/    # 安全权限
├── est-integration-group/ # 消息集成
├── est-web-group/         # Web 容器
├── est-ai-suite/          # AI 套件
├── est-microservices/     # 微服务
└── est-extensions/        # 扩展功能
```

---

## 相关资源

- [est-foundation README](./est-foundation/README.md) - 核心设施详细文档
- [est-data-group README](./est-data-group/README.md) - 数据访问详细文档
- [est-security-group README](./est-security-group/README.md) - 安全权限详细文档
- [est-web-group README](./est-web-group/README.md) - Web 容器详细文档
- [est-ai-suite README](./est-ai-suite/README.md) - AI 套件详细文档
- [示例代码](../est-examples/) - 示例代码
- [EST Core](../est-core/README.md) - 核心模块
- [EST App](../est-app/README.md) - 应用模块

---

**文档版本**：2.1.0  
**最后更新**：2026-03-09
