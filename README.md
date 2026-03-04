# EST

EST 是一个零依赖的现代 Java 框架，采用递进式模块结构设计。

- **版本**: 1.0.0-SNAPSHOT
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

## 技术栈

| 技术 | 说明 |
|------|------|
| Java 21+ | 核心编程语言 |
| Maven | 项目构建和依赖管理 |
| Java 标准库 | 所有功能的实现基础 |

## 项目结构

```
est/
├── est-core/                              # 核心模块
│   ├── est-core-api/                      # 核心接口
│   └── est-core-impl/                     # 核心实现
├── est-patterns/                          # 设计模式模块
│   ├── est-patterns-api/                  # 设计模式接口
│   └── est-patterns-impl/                 # 设计模式实现
├── est-utils/                             # 工具模块
│   ├── est-utils-common/                  # 通用工具
│   ├── est-utils-io/                      # IO工具
│   └── est-utils-format/                  # 格式化工具
│       ├── est-utils-format-json/         # JSON格式化
│       ├── est-utils-format-yaml/         # YAML格式化
│       └── est-utils-format-xml/          # XML格式化
├── est-test/                              # 测试模块
│   ├── est-test-api/                      # 测试接口（注解、断言）
│   └── est-test-impl/                     # 测试实现（运行器、报告器）
├── est-collection/                        # Collection模块
│   ├── est-collection-api/                # Collection接口
│   └── est-collection-impl/               # Collection实现
├── est-features/                          # 功能模块
│   ├── est-features-cache/                # 缓存功能
│   ├── est-features-event/                # 事件功能
│   ├── est-features-logging/              # 日志功能
│   ├── est-features-data/                 # 数据功能
│   ├── est-features-security/             # 安全功能
│   ├── est-features-messaging/            # 消息功能
│   ├── est-features-monitor/              # 监控功能
│   └── est-features-scheduler/            # 调度功能
├── est-plugin/                            # 插件模块
│   ├── est-plugin-api/                    # 插件接口
│   └── est-plugin-impl/                   # 插件实现
├── est-web/                               # Web模块
│   ├── est-web-api/                       # Web接口
│   └── est-web-impl/                      # Web实现
└── est-examples/                          # 示例模块
    ├── est-examples-basic/                # 基础示例
    ├── est-examples-advanced/             # 高级示例
    ├── est-examples-features/             # 功能示例
    └── est-examples-web/                  # Web示例
```

## 模块层级架构

```
┌─────────────────────────────────────────────────────────────────┐
│                        est-examples (示例层)                      │
├─────────────────────────────────────────────────────────────────┤
│                         est-web (Web层)                          │
├─────────────────────────────────────────────────────────────────┤
│                       est-plugin (插件层)                         │
├─────────────────────────────────────────────────────────────────┤
│                      est-features (功能层)                        │
├─────────────────────────────────────────────────────────────────┤
│                     est-collection (集合层)                       │
├─────────────────────────────────────────────────────────────────┤
│                         est-test (测试层)                         │
├─────────────────────────────────────────────────────────────────┤
│                        est-utils (工具层)                         │
├─────────────────────────────────────────────────────────────────┤
│                      est-patterns (模式层)                        │
├─────────────────────────────────────────────────────────────────┤
│                        est-core (核心层)                          │
└─────────────────────────────────────────────────────────────────┘
```

### 模块依赖关系

```
est-test-api  ─────────────────────────────────────────────────────►  零依赖
      │
      └──► est-test-impl ──► est-utils-common (复用 AssertUtils)
```

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
    <version>1.0.0-SNAPSHOT</version>
</dependency>

<!-- 引用Collection功能 -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-collection-impl</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>

<!-- 引用Web功能 -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-web-impl</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## 设计原则

### 零依赖原则

不依赖任何第三方库，所有功能使用 Java 标准库实现，充分利用 Java 21+ 特性。

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

## 功能模块

| 模块 | 说明 |
|------|------|
| est-core | 核心接口与实现，依赖注入容器、配置管理、模块管理、生命周期 |
| est-patterns | 设计模式接口与实现，创建型、结构型、行为型模式 |
| est-utils | 工具类，字符串、日期、IO、格式化等 |
| est-test | 测试框架，注解、断言、测试运行器、Mock支持、参数化测试 |
| est-collection | Collection接口与实现，链式数据处理、格式转换 |
| est-features-cache | 缓存功能，内存缓存、文件缓存、Redis缓存 |
| est-features-event | 事件功能，本地事件、异步事件 |
| est-features-logging | 日志功能，控制台日志、文件日志 |
| est-features-data | 数据功能，JDBC、内存、Redis数据访问 |
| est-features-security | 安全功能，基础认证、JWT认证 |
| est-features-messaging | 消息功能，本地消息、AMQP消息、MQTT消息 |
| est-features-monitor | 监控功能，JVM监控、系统监控 |
| est-features-scheduler | 调度功能，Cron调度、固定间隔调度 |
| est-plugin | 插件系统，插件接口、插件加载器（类加载、JAR加载）、插件管理器、依赖管理 |
| est-web | Web功能，HTTP服务器、路由、中间件、MVC |

## 模块统计

| 类型 | 数量 |
|------|------|
| 顶层模块 | 9 |
| 二级模块 | 27 |
| 三级模块 | 30 |
| **总计** | **66** |

## 开发计划

- [x] 阶段一：基础架构搭建
  - [x] 递进式项目结构搭建
  - [x] 核心模块 - Container、Config、Module、Lifecycle 接口与实现
  - [x] 设计模式模块 - Singleton、Factory、Builder、Adapter、Decorator、Proxy、Observer、Strategy、Command 接口与实现
  - [x] 基础工具模块 - StringUtils、DateUtils、AssertUtils、ClassUtils、FileUtils、IOUtils、ResourceUtils、PathUtils、JsonUtils、YamlUtils、XmlUtils
- [x] 阶段二：Collection模块实现
  - [x] Collection 接口定义 - Collection、Collector、Pair、Collections 工厂类
  - [x] 链式调用功能实现 - DefaultCollection、SingletonCollection、CollectionFactory
  - [x] 格式转换功能实现 - toJson、toYaml、toXml、fromJson、fromYaml、fromXml
- [x] 阶段三：功能模块实现
  - [x] 缓存系统 - Cache接口、CacheEntry、CacheConfig、CacheStats、CacheLoader、CacheListener
    - [x] 内存缓存 - MemoryCache、LruCacheStrategy、Caches工厂类
    - [x] 文件缓存 - FileCache、FileCaches工厂类
    - [x] Redis缓存 - RedisClient（零依赖Socket实现）、RedisCache、RedisCaches工厂类
  - [x] 事件系统 - EventBus接口、Event、EventListener、EventConfig、EventStats
    - [x] 本地事件 - LocalEventBus、LocalEvents工厂类
    - [x] 异步事件 - AsyncEventBus、AsyncEvents工厂类（支持虚拟线程）
  - [x] 日志系统 - Logger接口、LogLevel、LogRecord、LogFormatter、LogAppender、LogConfig、LogStats
    - [x] 控制台日志 - ConsoleLogger、ConsoleLogAppender、ConsoleLogFormatter、ConsoleLogs工厂类
    - [x] 文件日志 - FileLogger、FileLogAppender、FileLogFormatter、FileLogs工厂类（支持文件滚动和日期滚动）
  - [x] 数据系统（JDBC、内存、Redis）
    - [x] 数据接口 - Repository、Query、TransactionManager、Transaction、ConnectionPool、Orm、RedisClient、EntityMapper
    - [x] 内存数据 - MemoryRepository、MemoryTransactionManager、MemoryQuery、MemoryOrm、MemoryData工厂类
    - [x] JDBC数据 - DefaultConnectionPool、JdbcTransactionManager、JdbcQuery、JdbcRepository、JdbcOrm、JdbcData工厂类
    - [x] Redis数据 - DefaultRedisClient、RedisRepository、RedisOrm、RedisData工厂类（零依赖Socket实现）
  - [x] 安全系统（基础、JWT）
    - [x] 安全接口 - User、Role、Permission、Authentication、Authorization、Crypto、PasswordEncoder、SecurityContext、UserDetailsService、TokenProvider、Token
    - [x] 基础安全 - DefaultUser、DefaultRole、DefaultPermission、DefaultAuthentication、DefaultAuthorization、BasicCrypto、BCryptPasswordEncoder、InMemoryUserDetailsService、BasicAuthenticationProvider、BasicTokenProvider、BasicSecurity工厂类
    - [x] JWT安全 - JwtTokenProvider（零依赖实现）、JwtAuthenticationProvider、JwtSecurityContext、JwtSecurity工厂类
  - [x] 消息系统（本地、AMQP、MQTT）
    - [x] 消息接口 - Message、MessageProducer、MessageConsumer、MessageQueue、MessageTopic、QueueConfig、MessageStats、MessagingConfig
    - [x] 本地消息 - LocalMessageQueue、LocalMessageTopic、LocalMessageProducer、LocalMessageConsumer、LocalMessageBroker、LocalMessages工厂类
    - [x] AMQP消息 - AmqpConnection（零依赖Socket实现）、AmqpMessageQueue、AmqpMessageProducer、AmqpMessageConsumer、AmqpMessages工厂类
    - [x] MQTT消息 - MqttConnection（零依赖Socket实现）、MqttMessageProducer、MqttMessageConsumer、MqttMessages工厂类
  - [x] 监控系统（JVM、系统）
    - [x] 监控接口 - Metrics、HealthCheck、HealthStatus、HealthCheckResult、Metric、MonitorException
    - [x] JVM监控 - JvmMetrics（内存、线程、类加载、GC、CPU）、JvmHealthCheck、JvmMonitor单例
    - [x] 系统监控 - SystemMetrics（OS、CPU、内存、交换区、磁盘）、SystemHealthCheck、SystemMonitor单例
  - [x] 调度系统（Cron、固定间隔）
    - [x] 调度接口 - Scheduler、Task、TaskState、ScheduleConfig、ScheduleType、ScheduleResult、SchedulerStats、SchedulerException、TaskListener
    - [x] Cron调度 - CronExpression（零依赖实现）、CronTask、CronScheduler、CronSchedulers工厂类
    - [x] 固定间隔调度 - FixedRateTask、FixedRateScheduler、FixedRateSchedulers工厂类（支持固定速率和固定延迟）
  - [x] 测试框架（零依赖实现）
    - [x] 测试接口 - @Test、@BeforeEach、@AfterEach、@BeforeAll、@AfterAll、@DisplayName、@Disabled
    - [x] 断言类 - assertEquals、assertTrue、assertNull、assertThrows、assertTimeout
    - [x] 参数化测试 - @ParameterizedTest、@ValueSource、@CsvSource、@MethodSource
    - [x] 测试运行器 - TestRunner、TestScanner、TestExecutor
    - [x] Mock支持 - @Mock、mock()、when().thenReturn()、verify()
    - [x] 异步测试 - AsyncAssertions.await()、until()
- [x] 阶段四：插件与Web模块实现
  - [x] 插件接口 - Plugin、PluginManager、PluginState、PluginInfo、PluginStats、PluginListener、PluginLoader、PluginConfig、PluginException、PluginContext、AbstractPlugin
  - [x] 插件管理 - DefaultPluginManager、DefaultPluginLoader、JarPluginLoader、Plugins工厂类
  - [x] Web接口 - HttpMethod、HttpStatus、Request、Response、Session、Controller、RestController、Router、Route、Middleware、CorsMiddleware、View、WebException、WebServer、WebApplication、Filter、SessionManager、StaticFileHandler、MimeType
  - [x] Web实现 - DefaultWebServer、DefaultRouter、DefaultRoute、DefaultSession、DefaultSessionManager、DefaultWebApplication、DefaultStaticFileHandler
  - [x] Web中间件实现 - DefaultCorsMiddleware、LoggingMiddleware、SecurityMiddleware
  - [x] Web路由实现 - 支持RESTful API、参数绑定、路径匹配
  - [x] Web控制器实现 - 支持注解驱动、请求处理、响应渲染
  - [x] Web会话管理 - 支持内存会话、持久化会话
  - [x] 静态文件处理 - 支持静态资源访问、缓存控制
  - [x] Web应用配置 - 支持端口配置、线程池配置、SSL配置
- [x] 阶段五：示例和文档
  - [x] 基础示例实现
    - [x] 核心功能示例 - 依赖注入、配置管理、模块管理
    - [x] 设计模式示例 - 单例、工厂、建造者、适配器等模式的使用
    - [x] 工具类示例 - 字符串处理、日期处理、IO操作等
  - [x] 高级示例实现
    - [x] 多模块集成示例 - 如何整合多个功能模块
    - [x] 性能优化示例 - 如何优化框架使用性能
    - [x] 自定义扩展示例 - 如何扩展框架功能
  - [x] 功能示例实现
    - [x] 缓存系统示例 - 内存缓存、文件缓存、Redis缓存的使用
    - [x] 事件系统示例 - 本地事件、异步事件的使用
    - [x] 日志系统示例 - 控制台日志、文件日志的配置和使用
    - [x] 安全系统示例 - 基础认证、JWT认证的使用
    - [x] 调度系统示例 - Cron调度、固定间隔调度的使用
    - [x] 监控系统示例 - JVM监控、系统监控的使用
  - [x] Web示例实现
    - [x] 基础Web应用 - 简单的HTTP服务器和路由
    - [x] RESTful API应用 - 构建RESTful风格的API
    - [x] MVC应用 - 模型-视图-控制器架构的应用
    - [x] 中间件应用 - 使用和自定义中间件
  - [x] 文档编写
    - [x] API文档 - 使用Javadoc生成详细的API文档
    - [x] 用户指南 - 框架的安装、配置和使用指南
    - [x] 示例文档 - 各个示例的详细说明文档
    - [x] 架构文档 - 框架的设计理念和架构说明
    - [x] 贡献指南 - 如何为框架贡献代码
- [ ] 阶段六：发布和维护

## License

[MIT License](LICENSE)

## 文档

EST框架提供了完整的文档支持，包括：

### 架构设计文档
- 位置：`architecture_design.md`
- 内容：框架的设计理念、架构说明、模块结构和开发计划

### API文档
- 生成方式：使用Javadoc生成
- 命令：`mvn javadoc:aggregate`
- 输出位置：`target/site/apidocs`

### 用户指南
- 内容：框架的安装、配置和使用指南
- 位置：`user_guide.md`目录下

### 示例文档
- 内容：各个示例的详细说明文档
- 位置：`est-examples`目录下的各个示例模块中

### 贡献指南
- 内容：如何为框架贡献代码
- 位置：`contributing.md`

## Author

idcu
