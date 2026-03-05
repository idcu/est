# EST 架构设计文档

## 1. 项目概述

EST 是一个零依赖的现代 Java 框架，采用递进式模块结构设计，基于 Java 21+ 开发。

### 1.1 设计理念

- **零依赖**：所有功能基于 Java 标准库实现
- **渐进式模块**：每个模块可独立使用
- **接口与实现分离**：支持多实现和灵活扩展
- **现代 Java**：充分利用 Java 21+ 特性（虚拟线程等）
- **AI友好**：标准化API、清晰模式、丰富示例，便于AI代码生成

### 1.2 核心特性

- 依赖注入容器
- 模块化系统
- 生命周期管理
- 事件驱动
- 缓存系统
- 数据访问
- Web 框架
- 插件系统
- 工具集
- 测试框架

---

## 2. 核心模块详解

### 2.1 est-core - 核心模块

#### 2.1.1 组件架构

```
est-core/
├── est-core-api/              # 核心接口
│   ├── Container.java         # 依赖注入容器接口
│   ├── Config.java            # 配置接口
│   ├── Module.java            # 模块接口
│   ├── annotation/            # 注解定义
│   │   ├── Component.java
│   │   ├── Service.java
│   │   ├── Repository.java
│   │   ├── Inject.java
│   │   ├── Qualifier.java
│   │   ├── Primary.java
│   │   ├── Value.java
│   │   └── ConditionalOnProperty.java
│   ├── lifecycle/             # 生命周期相关
│   │   ├── Lifecycle.java
│   │   ├── LifecycleListener.java
│   │   ├── InitializingBean.java
│   │   ├── DisposableBean.java
│   │   ├── PostConstruct.java
│   │   └── PreDestroy.java
│   ├── scope/                 # 作用域
│   │   └── Scope.java
│   └── processor/             # Bean处理器
│       └── BeanPostProcessor.java
└── est-core-impl/             # 核心实现
    ├── DefaultContainer.java  # 默认容器实现
    ├── DefaultConfig.java     # 默认配置实现
    ├── AbstractModule.java    # 抽象模块基类
    ├── ModuleManager.java     # 模块管理器
    ├── DefaultLifecycle.java  # 默认生命周期
    ├── LifecycleManager.java  # 生命周期管理器
    ├── inject/                # 依赖注入实现
    │   ├── ConstructorInjector.java
    │   ├── FieldInjector.java
    │   └── MethodInjector.java
    ├── scope/                 # 作用域策略
    │   └── ScopeStrategy.java
    └── scan/                  # 组件扫描
        ├── ComponentScanner.java
        └── ClassPathScanner.java
```

#### 2.1.2 DefaultContainer 设计

容器使用三层 Map 实现高效的依赖注入：

```java
public class DefaultContainer implements Container {
    private final Map<String, Registration> registrations;  // 注册信息
    private final Map<String, Object> instances;            // 单例实例
    private final ScopeStrategy scopeStrategy;              // 作用域策略
    private final ConstructorInjector constructorInjector;
    private final FieldInjector fieldInjector;
    private final MethodInjector methodInjector;
    private final List<BeanPostProcessor> beanPostProcessors;
    private final List<DisposableBean> disposableBeans;
    private final List<Object> preDestroyBeans;
}
```

**注册方式**：
- `register(type, implementation)`：注册类型与实现类
- `registerSingleton(type, instance)`：注册单例
- `registerSupplier(type, supplier)`：注册供应者
- 支持 Scope 和 Qualifier 参数

**获取方式**：
- `get(type)`：获取实例，不存在则创建
- `getIfPresent(type)`：安全获取，返回 Optional
- `contains(type)`：检查是否已注册

**Bean 生命周期流程**：
1. 创建实例（ConstructorInjector）
2. BeanPostProcessor.postProcessBeforeInitialization
3. 字段注入（FieldInjector）
4. 方法注入（MethodInjector）
5. @PostConstruct 方法调用
6. InitializingBean.afterPropertiesSet
7. BeanPostProcessor.postProcessAfterInitialization
8. 注册为 DisposableBean（如果是）
9. 注册 @PreDestroy 方法（如果有）

#### 2.1.3 作用域策略

ScopeStrategy 管理不同作用域的Bean实例创建和缓存：
- SINGLETON：单例模式，全局唯一实例
- PROTOTYPE：原型模式，每次获取创建新实例
- 可扩展自定义作用域

#### 2.1.4 组件扫描

ComponentScanner 支持自动扫描指定包下的组件：
- @Component
- @Service
- @Repository
- 自动注册到容器

---

### 2.2 est-web - Web 模块

#### 2.2.1 Web 架构

```
┌─────────────────────────────────────────────────────────┐
│                    HTTP 请求                               │
└────────────────────┬────────────────────────────────────┘
                     │
        ┌────────────▼────────────┐
        │   WebServer (HttpServerImpl) │
        └────────────┬────────────┘
                     │
        ┌────────────▼────────────┐
        │   Middleware Pipeline    │
        │  (Cors → Logging → ...) │
        └────────────┬────────────┘
                     │
        ┌────────────▼────────────┐
        │       Router             │
        └────────────┬────────────┘
                     │
        ┌────────────▼────────────┐
        │   Route Handler          │
        │  (Controller/Action)     │
        └────────────┬────────────┘
                     │
        ┌────────────▼────────────┐
        │      Response            │
        └─────────────────────────┘
```

#### 2.2.2 DefaultRouter 设计

功能强大的路由系统：

```java
public class DefaultRouter implements Router {
    private final List<Route> routes;
    private final Map<String, Route> namedRoutes;
    private final Map<HttpMethod, Map<String, Route>> routeCache;
    private String currentPrefix;
    private String currentName;
    private List<String> currentMiddleware;
}
```

**核心功能**：
- 支持所有HTTP方法（GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS）
- 路由分组（group）
- 路由前缀（prefix）
- 路由命名（name）
- 中间件绑定（middleware）
- 路由缓存优化
- 路径参数匹配

#### 2.2.3 DefaultSessionManager 设计

会话管理采用双层存储：

```java
public class DefaultSessionManager implements SessionManager {
    private final Map<String, Session> sessions;           // 内存缓存（用于快速访问）
    private final SessionStore sessionStore;                // 持久化存储
    private final SessionConfig sessionConfig;              // 配置
    private final SecureRandom secureRandom;                // 会话ID生成器
}
```

**会话流程**：
1. 创建会话 → 生成随机ID → 存入内存和存储
2. 获取会话 → 先查内存 → 再查存储 → 更新访问时间
3. 销毁会话 → 从内存和存储中删除
4. 清理过期 → 定期扫描并清理无效会话

#### 2.2.4 DefaultWebApplication 设计

Web 应用主类，整合所有组件：

```java
public class DefaultWebApplication implements WebApplication {
    private final String name;
    private final String version;
    private WebServer server;              // HTTP 服务器
    private Config config;
    private Router router;                 // 路由器
    private final List<Middleware> middlewares;  // 中间件列表
    private final List<LifecycleListener> lifecycleListeners;
    private final Map<Class<? extends Exception>, Consumer<Exception>> exceptionHandlers;
    private Consumer<Exception> globalErrorHandler;
    private Runnable startupCallback;
    private Runnable shutdownCallback;
    private View.ViewResolver viewResolver;
}
```

**启动流程**：
1. 初始化服务器（HttpServerImpl）
2. 配置路由
3. 注册中间件
4. 配置视图解析器
5. 配置异常处理器
6. 启动监听
7. 执行 startupCallback
8. 注册关闭钩子

**内置中间件**：
- DefaultCorsMiddleware - CORS支持
- LoggingMiddleware - 请求日志
- PerformanceMonitorMiddleware - 性能监控
- SecurityMiddleware - 安全检查

---

### 2.3 est-features - 功能模块

#### 2.3.1 模块列表

| 子模块 | 说明 |
|--------|------|
| est-features-cache | 缓存系统（内存、文件、Redis） |
| est-features-event | 事件总线（本地、异步） |
| est-features-logging | 日志系统（控制台、文件） |
| est-features-data | 数据访问（JDBC、内存、Redis、MongoDB） |
| est-features-security | 安全认证（基础、JWT、API Key、OAuth2、策略引擎） |
| est-features-messaging | 消息系统（本地、AMQP、MQTT） |
| est-features-monitor | 监控系统（JVM、系统） |
| est-features-scheduler | 调度系统（Cron、固定间隔） |

#### 2.3.2 通用设计模式

每个功能模块遵循统一的结构：

```
est-features-xxx/
├── est-features-xxx-api/      # 接口定义
└── est-features-xxx-yyy/      # 具体实现 (如 memory/file/redis)
```

#### 2.3.3 est-features-cache 设计

```java
public interface Cache<K, V> {
    V get(K key);
    void put(K key, V value);
    void put(K key, V value, long ttl);
    void remove(K key);
    boolean contains(K key);
    void clear();
    CacheStats stats();
}
```

实现：
- MemoryCache - LRU策略，ConcurrentHashMap
- FileCache - 文件系统持久化
- RedisCache - Redis集成

#### 2.3.4 est-features-data 设计

统一的数据访问层：

```java
public interface Repository<T, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void deleteById(ID id);
    boolean existsById(ID id);
    long count();
}
```

实现：
- JdbcRepository - JDBC + 连接池
- MemoryRepository - 内存存储
- RedisRepository - Redis存储
- MongoRepository - MongoDB存储

ORM注解：
- @Entity
- @Id
- @Column
- @CreatedAt
- @UpdatedAt
- @Version
- @Transient

#### 2.3.5 est-features-security 设计

多层次安全架构：

```
Authentication（认证） → Authorization（授权） → Policy（策略）
```

认证方式：
- BasicAuthentication - 用户名密码
- JwtAuthentication - JWT Token
- ApiKeyAuthentication - API Key
- OAuth2Authentication - OAuth2

授权方式：
- Role-based - 基于角色
- Permission-based - 基于权限
- Attribute-based - 基于属性（PolicyEngine）

---

## 3. 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Java | 21+ | 核心语言 |
| Maven | 3.6+ | 构建工具 |
| Javadoc | - | API 文档 |

---

## 4. 扩展指南

### 4.1 添加新功能模块

1. 创建 `est-features-xxx` 目录
2. 按照 api/impl 结构组织
3. 在根 pom.xml 中添加模块
4. 实现接口并提供工厂类

### 4.2 扩展 Web 中间件

实现 `Middleware` 接口：

```java
public interface Middleware {
    String getName();
    int getPriority();
    boolean before(Request request, Response response);
    void after(Request request, Response response);
    void onError(Request request, Response response, Exception e);
    boolean shouldApply(Request request);
}
```

### 4.3 自定义作用域

实现自定义Scope和ScopeStrategy扩展。

---

## 5. 性能考量

- **容器**：使用 ConcurrentHashMap 实现线程安全
- **会话**：双层存储，内存缓存 + 持久化
- **事件**：支持虚拟线程的异步事件
- **Web**：使用 Java 内置 HttpServer，轻量高效
- **路由**：路由缓存，减少匹配开销
- **数据**：连接池，减少连接创建开销

---

## 6. 目录结构

```
est1.3/
├── est-core/              # 核心模块（DI、配置、生命周期）
├── est-foundation/        # 基础设施层
│   ├── est-patterns/      # 设计模式
│   ├── est-utils/         # 工具类
│   ├── est-test/          # 测试框架
│   └── est-collection/    # Collection增强
├── est-features/          # 功能模块
│   ├── est-features-cache/
│   ├── est-features-event/
│   ├── est-features-logging/
│   ├── est-features-data/
│   ├── est-features-security/
│   ├── est-features-messaging/
│   ├── est-features-monitor/
│   ├── est-features-scheduler/
│   └── est-features-ai/
├── est-extensions/        # 扩展层
│   ├── est-plugin/        # 插件系统
│   └── est-web/           # Web框架
├── est-tools/             # 工具层
│   └── est-scaffold/      # 脚手架
├── est-examples/          # 示例代码
├── deploy/                # 部署配置
│   ├── docker/            # Docker配置
│   └── k8s/               # Kubernetes配置
├── .github/               # GitHub Actions CI/CD
├── docs/                  # 文档
├── pom.xml                # 父POM
└── README.md
```
