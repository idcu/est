# EST - Enterprise Services Toolkit

EST 是一个零依赖的现代 Java 框架，采用递进式模块结构设计，**特别适合 AI coder 进行代码生成**。

- **版本**: 1.3.0-SNAPSHOT
- **项目**: EST
- **作者**: idcu
- **许可证**: MIT License

## 核心特性

- **零依赖**：不依赖任何第三方库，避免依赖冲突和版本管理问题
- **递进式模块**：每个层级的模块都可以独立被其他项目引用
- **低耦合设计**：模块间通过接口通信，减少直接依赖
- **现代 Java**：基于 Java 21+，支持虚拟线程等现代特性
- **高性能**：优化核心组件性能，支持并发操作
- **Collection 增强**：提供类似 Laravel Collection 的链式数据处理能力
- **AI友好**：标准化的API、丰富的示例、脚手架工具，让AI可以轻松生成高质量代码

## AI Coder 首选

EST 框架专为 AI 代码生成优化：

- 🚀 **脚手架工具** - 一键生成标准项目模板
- 📚 **标准化模式** - 可预测的代码结构
- 🔧 **清晰的API** - 简单一致的接口设计
- 🎯 **丰富示例** - AI可以学习的完整参考
- 💡 **提示词模板** - 专门为AI设计的代码生成提示词

开始使用：[AI Coder 指南](docs/AI_CODER_GUIDE.md) | [快速参考](docs/QUICK_REFERENCE.md) | [提示词模板](docs/AI_PROMPTS.md)

## 技术栈

| 技术 | 说明 |
|------|------|
| Java 21+ | 核心编程语言 |
| Maven | 项目构建和依赖管理 |
| Java 标准库 | 所有功能的实现基础 |

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.6+

### 构建

```bash
# 构建所有模块
mvn clean install

# 构建指定模块
mvn clean install -pl est-core

# 跳过测试
mvn clean install -DskipTests
```

### 使用示例

```xml
<!-- 只引用核心接口 -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-api</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>

<!-- 引用Collection功能 -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-collection-impl</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>

<!-- 引用Web功能 -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-web-impl</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
```

## 设计原则

### 零依赖原则

所有功能使用 Java 标准库实现：

| 功能 | 实现方式 |
|------|----------|
| JSON处理 | javax.json 包 |
| XML处理 | javax.xml 包 |
| 加密 | javax.crypto 包 |
| JDBC | java.sql 包 |
| 并发 | java.util.concurrent 包 |
| JVM监控 | java.lang.management 包 |
| HTTP服务器 | com.sun.net.httpserver 包 |

### 递进式模块设计

采用清晰的层级结构，每个模块都可以独立被其他项目引用，模块间通过接口通信。

### 低耦合设计

模块间通过接口通信，接口与实现分离，支持多种实现方式。

## 模块层级架构

EST 采用清晰的递进式层级结构，每个模块都采用 **API/Impl 分离** 设计，模块间通过接口通信，支持独立引用。

```
┌─────────────────────────────────────────────────────────────────┐
│                      est-examples (示例层)                        │
│  ├─ est-examples-basic                                           │
│  ├─ est-examples-web                                             │
│  ├─ est-examples-features                                        │
│  └─ est-examples-advanced                                        │
├─────────────────────────────────────────────────────────────────┤
│                      est-scaffold (脚手架层)                      │
│  └─ 项目模板生成器                                                 │
├─────────────────────────────────────────────────────────────────┤
│                        est-web (Web层)                            │
│  ├─ est-web-api (Web接口)                                         │
│  └─ est-web-impl (Web实现)                                        │
├─────────────────────────────────────────────────────────────────┤
│                      est-plugin (插件层)                          │
│  ├─ est-plugin-api (插件接口)                                     │
│  └─ est-plugin-impl (插件实现)                                    │
├─────────────────────────────────────────────────────────────────┤
│                     est-features (功能层)                         │
│  ├─ est-features-cache (缓存)                                     │
│  │  ├─ est-features-cache-api                                    │
│  │  ├─ est-features-cache-memory                                 │
│  │  ├─ est-features-cache-file                                   │
│  │  └─ est-features-cache-redis                                  │
│  ├─ est-features-event (事件)                                     │
│  │  ├─ est-features-event-api                                    │
│  │  ├─ est-features-event-local                                  │
│  │  └─ est-features-event-async                                  │
│  ├─ est-features-logging (日志)                                   │
│  │  ├─ est-features-logging-api                                  │
│  │  ├─ est-features-logging-console                              │
│  │  └─ est-features-logging-file                                 │
│  ├─ est-features-data (数据)                                      │
│  │  ├─ est-features-data-api                                     │
│  │  ├─ est-features-data-jdbc                                    │
│  │  ├─ est-features-data-memory                                  │
│  │  ├─ est-features-data-redis                                   │
│  │  └─ est-features-data-mongodb                                 │
│  ├─ est-features-security (安全)                                  │
│  │  ├─ est-features-security-api                                 │
│  │  ├─ est-features-security-basic                               │
│  │  ├─ est-features-security-jwt                                 │
│  │  ├─ est-features-security-apikey                              │
│  │  ├─ est-features-security-oauth2                              │
│  │  └─ est-features-security-policy                              │
│  ├─ est-features-messaging (消息)                                │
│  │  ├─ est-features-messaging-api                                │
│  │  ├─ est-features-messaging-local                              │
│  │  ├─ est-features-messaging-amqp                               │
│  │  └─ est-features-messaging-mqtt                               │
│  ├─ est-features-monitor (监控)                                   │
│  │  ├─ est-features-monitor-api                                  │
│  │  ├─ est-features-monitor-jvm                                  │
│  │  └─ est-features-monitor-system                               │
│  └─ est-features-scheduler (调度)                                 │
│     ├─ est-features-scheduler-api                                │
│     ├─ est-features-scheduler-fixed                              │
│     └─ est-features-scheduler-cron                               │
├─────────────────────────────────────────────────────────────────┤
│                    est-collection (集合层)                         │
│  ├─ est-collection-api (集合接口)                                 │
│  └─ est-collection-impl (集合实现)                                │
├─────────────────────────────────────────────────────────────────┤
│                        est-test (测试层)                           │
│  ├─ est-test-api (测试接口)                                       │
│  └─ est-test-impl (测试实现)                                      │
├─────────────────────────────────────────────────────────────────┤
│                       est-utils (工具层)                           │
│  ├─ est-utils-common (通用工具)                                   │
│  ├─ est-utils-io (IO工具)                                         │
│  └─ est-utils-format (格式化工具)                                 │
│     ├─ est-utils-format-json                                      │
│     ├─ est-utils-format-xml                                       │
│     └─ est-utils-format-yaml                                      │
├─────────────────────────────────────────────────────────────────┤
│                      est-patterns (模式层)                         │
│  ├─ est-patterns-api (模式接口)                                   │
│  └─ est-patterns-impl (模式实现)                                  │
├─────────────────────────────────────────────────────────────────┤
│                        est-core (核心层)                           │
│  ├─ est-core-api (核心接口)                                       │
│  └─ est-core-impl (核心实现)                                      │
└─────────────────────────────────────────────────────────────────┘
```

### 模块依赖关系

- **est-core**：无依赖，是所有模块的基础
- **est-patterns**：依赖 est-core
- **est-utils**：依赖 est-core
- **est-test**：依赖 est-core
- **est-collection**：依赖 est-core、est-utils
- **est-features**：各子模块按需依赖 est-core、est-utils、est-collection
- **est-plugin**：依赖 est-core、est-utils
- **est-web**：依赖 est-core、est-utils、est-collection、est-features（部分）
- **est-scaffold**：依赖 est-core、est-utils
- **est-examples**：依赖所有上层模块

### API/Impl 分离设计原则

每个功能模块都严格遵循 API/Impl 分离：
- ***-api** 模块：仅包含接口定义、注解、数据模型，无具体实现
- ***-impl** 模块：包含具体实现逻辑，依赖对应的 api 模块

使用时只需引用所需的 api 模块，在运行时再引入具体的 impl 模块，实现完全的解耦。

## 功能模块详解

### est-core - 核心模块
提供框架的核心基础设施：
- **依赖注入容器** (DefaultContainer) - 三层Map实现的高效DI容器，支持构造器/字段/方法注入
- **配置管理** (DefaultConfig) - 统一配置接口
- **模块管理** (ModuleManager) - 模块化生命周期管理
- **组件扫描** (ComponentScanner) - 自动扫描和注册组件
- **生命周期管理** (LifecycleManager) - 完整的Bean生命周期（@PostConstruct, @PreDestroy, InitializingBean, DisposableBean）
- **作用域策略** (ScopeStrategy) - 支持单例、原型等多种作用域
- **注解支持** - @Component, @Service, @Repository, @Inject, @Qualifier, @Primary, @Value

### est-patterns - 设计模式模块
提供常用设计模式的实现：
- 创建型模式
- 结构型模式
- 行为型模式

### est-utils - 工具模块
提供通用工具类：
- 通用工具 (est-utils-common)
- IO工具 (est-utils-io)
- 格式化工具 (est-utils-format) - JSON、XML、YAML

### est-test - 测试模块
提供测试支持框架：
- 断言工具 (Assertions)
- 测试运行器
- 测试注解支持

### est-collection - 集合模块
提供类似Laravel Collection的链式数据处理能力。

### est-features - 功能模块
提供企业级功能组件：

#### est-features-cache - 缓存系统
- 内存缓存 (MemoryCache) - LRU策略
- 文件缓存 (FileCache)
- Redis缓存 (RedisCache)
- 缓存事件监听和统计

#### est-features-event - 事件总线
- 本地事件 (LocalEventBus)
- 异步事件 (AsyncEventBus) - 支持虚拟线程
- 事件监听器和统计

#### est-features-logging - 日志系统
- 控制台日志 (ConsoleLogger)
- 文件日志 (FileLogger)
- 多种日志级别和格式化

#### est-features-data - 数据访问
- JDBC数据访问 (JdbcData) - 连接池、ORM、事务管理
- 内存数据存储 (MemoryData)
- Redis数据访问 (RedisData)
- MongoDB数据访问 (MongoData)
- 统一Repository模式

#### est-features-security - 安全认证
- 基础认证 (BasicSecurity) - 用户、角色、权限、BCrypt密码加密
- JWT认证 (JwtSecurity)
- API Key认证 (ApiKeySecurity)
- OAuth2认证 (OAuth2Security)
- 策略引擎 (PolicyEngine) - 基于属性的授权

#### est-features-messaging - 消息系统
- 本地消息 (LocalMessages) - 队列和主题
- AMQP消息 (AmqpMessages)
- MQTT消息 (MqttMessages)
- 消息生产者和消费者

#### est-features-monitor - 监控系统
- JVM监控 (JvmMonitor) - 内存、线程、GC等
- 系统监控 (SystemMonitor) - CPU、磁盘、网络
- 健康检查 (HealthCheck)

#### est-features-scheduler - 调度系统
- 固定间隔调度 (FixedRateScheduler)
- Cron表达式调度 (CronScheduler)
- 任务监听和统计

### est-plugin - 插件模块
提供插件化支持：
- 插件接口
- 插件加载器（类加载、JAR加载）
- 插件管理器
- 依赖管理

### est-web - Web模块
完整的Web应用框架，基于Java内置HttpServer：
- **WebApplication** (DefaultWebApplication) - Web应用主入口
- **HttpServer** (HttpServerImpl) - 轻量级HTTP服务器
- **Router** (DefaultRouter) - 功能强大的路由系统，支持路由分组、前缀、命名、中间件、缓存
- **Request/Response** - HTTP请求响应处理
- **Middleware** - 中间件系统（CORS、日志、性能监控、安全）
- **Session管理** (DefaultSessionManager) - 双层存储（内存缓存+持久化）
- **WebSocket** (WebSocketServerManager) - WebSocket支持
- **模板引擎** (EstTemplateEngine, StringTemplateEngine) - 内置模板引擎
- **静态文件** (DefaultStaticFileHandler) - 静态资源服务
- **MVC支持** - Controller和RestController
- **异常处理** - 全局和特定异常处理器

### est-scaffold - 脚手架工具
提供项目模板生成功能：
- 项目模板生成器
- 标准化代码结构生成
- AI友好的代码生成辅助

### est-examples - 示例模块
丰富的示例代码：
- 基础示例 (est-examples-basic)
- Web示例 (est-examples-web)
- 功能示例 (est-examples-features)
- 高级示例 (est-examples-advanced)

## 部署支持

- **Docker** - 部署配置位于 `deploy/docker/` 目录
- **Kubernetes** - 部署配置位于 `deploy/k8s/` 目录
- **CI/CD** - GitHub Actions工作流
- **部署文档** - 详见 [docs/CLOUD_DEPLOYMENT.md](docs/CLOUD_DEPLOYMENT.md)

## 文档

EST 提供了完善的文档体系，请查看 [docs/README.md](docs/README.md) 获取完整文档索引。

## 代码示例

### 简单Web应用
```java
import ltd.idcu.est.web.WebApplication;

public class HelloWorld {
    public static void main(String[] args) {
        WebApplication app = WebApplication.create("My App", "1.0.0");
        
        app.get("/", (req, res) -> {
            res.send("Hello, World!");
        });
        
        app.run(8080);
    }
}
```

### 依赖注入容器
```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

public class DiExample {
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        
        container.register(MyService.class, MyServiceImpl.class);
        container.registerSingleton(Config.class, new MyConfig());
        
        MyService service = container.get(MyService.class);
        service.doSomething();
    }
}
```

### 路由分组
```java
app.routes(router -> {
    router.group("/api", (r, group) -> {
        r.get("/users", (req, res) -> res.json(users));
        r.post("/users", (req, res) -> res.json(createUser(req)));
    });
    
    router.group("/admin", (r, group) -> {
        r.middleware("auth");
        r.get("/dashboard", (req, res) -> res.render("admin/dashboard"));
    });
});
```

## License

[MIT License](LICENSE)
