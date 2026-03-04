# EST 架构设计文档

## 1. 项目概述

EST 是一个零依赖的现代 Java 框架，采用递进式模块结构设计，基于 Java 21+ 开发。

### 1.1 设计理念

- **零依赖**：所有功能基于 Java 标准库实现
- **渐进式模块**：每个模块可独立使用
- **接口与实现分离**：支持多实现和灵活扩展
- **现代 Java**：充分利用 Java 21+ 特性（虚拟线程等）

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

## 2. 整体架构

### 2.1 架构层级

```
┌─────────────────────────────────────────────────────────────────────┐
│                        est-examples (示例层)                           │
├─────────────────────────────────────────────────────────────────────┤
│                          est-web (Web层)                              │
├─────────────────────────────────────────────────────────────────────┤
│                        est-plugin (插件层)                            │
├─────────────────────────────────────────────────────────────────────┤
│                       est-features (功能层)                           │
├─────────────────────────────────────────────────────────────────────┤
│                      est-collection (集合层)                          │
├─────────────────────────────────────────────────────────────────────┤
│                         est-test (测试层)                              │
├─────────────────────────────────────────────────────────────────────┤
│                        est-utils (工具层)                             │
├─────────────────────────────────────────────────────────────────────┤
│                       est-patterns (模式层)                           │
├─────────────────────────────────────────────────────────────────────┤
│                         est-core (核心层)                              │
└─────────────────────────────────────────────────────────────────────┘
```

### 2.2 模块关系

- **核心层** (est-core)：所有模块的基础，提供容器、配置、模块管理
- **模式层** (est-patterns)：设计模式实现
- **工具层** (est-utils)：通用工具类
- **测试层** (est-test)：测试框架
- **集合层** (est-collection)：增强型集合操作
- **功能层** (est-features)：各功能模块（缓存、事件、日志、数据等）
- **插件层** (est-plugin)：插件系统
- **Web层** (est-web)：Web 框架
- **示例层** (est-examples)：使用示例

---

## 3. 核心模块详解

### 3.1 est-core - 核心模块

#### 3.1.1 组件架构

```
est-core/
├── est-core-api/              # 核心接口
│   ├── Container.java         # 依赖注入容器接口
│   ├── Config.java            # 配置接口
│   ├── Module.java            # 模块接口
│   └── lifecycle/             # 生命周期相关
│       ├── Lifecycle.java
│       └── LifecycleListener.java
└── est-core-impl/             # 核心实现
    ├── DefaultContainer.java  # 默认容器实现
    ├── DefaultConfig.java     # 默认配置实现
    ├── ModuleManager.java     # 模块管理器
    └── LifecycleManager.java  # 生命周期管理器
```

#### 3.1.2 DefaultContainer 设计

容器使用三层 Map 实现高效的依赖注入：

```java
public class DefaultContainer implements Container {
    private final Map<Class<?>, Supplier<?>> registrations;  // 注册信息
    private final Map<Class<?>, Object> instances;            // 单例实例
    private final Map<Class<?>, Object> cachedInstances;      // 缓存实例
}
```

**注册方式**：
- `register(type, implementation)`：注册类型与实现类
- `registerSingleton(type, instance)`：注册单例
- `registerSupplier(type, supplier)`：注册供应者

**获取方式**：
- `get(type)`：获取实例，不存在则创建
- `getIfPresent(type)`：安全获取，返回 Optional
- `contains(type)`：检查是否已注册

#### 3.1.3 生命周期管理

组件生命周期：
```
INITIALIZING → INITIALIZED → STARTING → STARTED → STOPPING → STOPPED → DESTROYED
```

### 3.2 est-features - 功能模块

#### 3.2.1 模块列表

| 子模块 | 说明 |
|--------|------|
| est-features-cache | 缓存系统（内存、文件、Redis） |
| est-features-event | 事件总线（本地、异步） |
| est-features-logging | 日志系统（控制台、文件） |
| est-features-data | 数据访问（JDBC、内存、Redis） |
| est-features-security | 安全认证（基础、JWT） |
| est-features-messaging | 消息系统（本地、AMQP、MQTT） |
| est-features-monitor | 监控系统（JVM、系统） |
| est-features-scheduler | 调度系统（Cron、固定间隔） |

#### 3.2.2 通用设计模式

每个功能模块遵循统一的结构：

```
est-features-xxx/
├── est-features-xxx-api/      # 接口定义
└── est-features-xxx-yyy/      # 具体实现 (如 memory/file/redis)
```

### 3.3 est-web - Web 模块

#### 3.3.1 Web 架构

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

#### 3.3.2 DefaultSessionManager 设计

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

#### 3.3.3 DefaultWebApplication 设计

Web 应用主类，整合所有组件：

```java
public class DefaultWebApplication implements WebApplication {
    private WebServer server;              // HTTP 服务器
    private Router router;                 // 路由器
    private List<Middleware> middlewares;  // 中间件列表
    private Map<Class<? extends Exception>, Consumer<Exception>> exceptionHandlers;
}
```

**启动流程**：
1. 初始化服务器
2. 配置路由
3. 注册中间件
4. 启动监听
5. 注册关闭钩子

---

## 4. 设计原则

### 4.1 零依赖原则

所有功能均使用 Java 标准库实现：

| 功能 | 标准库 |
|------|--------|
| JSON | javax.json |
| XML | javax.xml |
| 加密 | javax.crypto |
| JDBC | java.sql |
| HTTP | com.sun.net.httpserver |
| 并发 | java.util.concurrent |

### 4.2 接口与实现分离

每个模块都分为 api 和 impl 两部分：
- api 模块：定义接口，零依赖
- impl 模块：提供实现，可替换

### 4.3 渐进式模块设计

模块间依赖清晰，上层依赖下层，下层不依赖上层：
- est-core → est-patterns → est-utils → ... → est-web → est-examples

---

## 5. 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Java | 21+ | 核心语言 |
| Maven | 3.6+ | 构建工具 |
| Javadoc | - | API 文档 |

---

## 6. 扩展指南

### 6.1 添加新功能模块

1. 创建 `est-features-xxx` 目录
2. 按照 api/impl 结构组织
3. 在根 pom.xml 中添加模块
4. 实现接口并提供工厂类

### 6.2 扩展 Web 中间件

实现 `Middleware` 接口：

```java
public interface Middleware {
    String getName();
    int getPriority();
    boolean before(Request request, Response response);
    void after(Request request, Response response);
    void onError(Request request, Response response, Exception e);
}
```

---

## 7. 性能考量

- **容器**：使用 ConcurrentHashMap 实现线程安全
- **会话**：双层存储，内存缓存 + 持久化
- **事件**：支持虚拟线程的异步事件
- **Web**：使用 Java 内置 HttpServer，轻量高效

---

## 8. 项目统计

| 类型 | 数量 |
|------|------|
| 顶层模块 | 9 |
| 二级模块 | 27 |
| 三级模块 | 30 |
| 总计 | 66 |

---

## 9. 开发路线图

- [x] 基础架构搭建
- [x] Collection 模块
- [x] 功能模块（缓存、事件、日志、数据、安全、消息、监控、调度）
- [x] 测试框架
- [x] 插件与 Web 模块
- [x] 示例和文档
- [ ] 发布和维护

---

## 10. 参考文档

- [README.md](./README.md) - 项目概述
- [contributing.md](./contributing.md) - 贡献指南
- Javadoc - API 文档（`mvn javadoc:aggregate`）
