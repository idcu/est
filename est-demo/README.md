# EST Demo - EST 框架演示应用

这是一个EST框架的完整演示应用，展示了EST框架的核心功能和使用方法。
## 项目介绍

EST Demo是一个功能完备的Web应用，展示了EST框架的以下功能：
- 开箱即用的Web服务器
- 强大的路由系统和中间件支持
- RESTful API支持
- JSON响应处理
- 查询参数和路径参数处理
- CORS跨域支持
- **日志系统**（控制台和文件日志）
- **缓存系统**（内存和文件缓存）
- **事件总线**（同步和异步事件）
- **任务调度**
- **系统监控**（JVM和系统指标）
- **数据存储**（内存存储库）
- **完整的Web UI界面**

## 快速开始
### 环境要求
- JDK 21+
- Maven 3.6+

### 构建项目
```bash
cd est-demo
mvn clean compile
```

### 运行应用
```bash
mvn exec:java -Dexec.mainClass="ltd.idcu.est.demo.EstDemoApplication"
```

或者直接运行编译后的类：
```bash
java -cp target/classes ltd.idcu.est.demo.EstDemoApplication
```

## 功能特性
### 1. Web UI 界面
访问 http://localhost:8080 即可看到一个美观的Web界面，包含：
- 应用统计展示
- 用户管理功能
- 待办事项管理
- 缓存操作
- 事件发布
- 监控指标查看

### 2. 可用的API端点

#### 核心 API
- **GET /** - 主页（Web UI）
- **GET /api/hello** - 问候接口（支持name参数）
#### 用户管理 API
- **GET /api/users** - 获取所有用户
- **GET /api/users/:id** - 获取单个用户
- **POST /api/users** - 创建用户
- **PUT /api/users/:id** - 更新用户
- **DELETE /api/users/:id** - 删除用户

#### 待办事项 API
- **GET /api/todos** - 获取所有待办
- **GET /api/todos/:id** - 获取单个待办
- **POST /api/todos** - 创建待办
- **PUT /api/todos/:id** - 更新待办
- **DELETE /api/todos/:id** - 删除待办
- **PATCH /api/todos/:id/complete** - 切换完成状态
#### 缓存 API
- **GET /api/cache/memory** - 列出所有内存缓存键
- **GET /api/cache/memory/:key** - 获取内存缓存
- **PUT /api/cache/memory/:key** - 设置内存缓存
- **DELETE /api/cache/memory/:key** - 删除内存缓存
- **GET /api/cache/file/:key** - 获取文件缓存
- **PUT /api/cache/file/:key** - 设置文件缓存

#### 事件 API
- **POST /api/events/local** - 发布本地事件
- **POST /api/events/async** - 发布异步事件

#### 监控 API
- **GET /api/monitor/jvm** - 获取JVM指标
- **GET /api/monitor/system** - 获取系统指标
- **GET /api/monitor/stats** - 获取应用统计

## 代码示例

### 创建Web应用和中间件
```java
WebApplication app = Web.create("EST Demo", "2.1.0");

app.use((req, res, next) -> {
    requestCount.incrementAndGet();
    long startTime = System.currentTimeMillis();
    consoleLogger.info("Request: {} {}", req.getMethod(), req.getPath());
    next.handle();
    long duration = System.currentTimeMillis() - startTime;
    consoleLogger.info("Response: {} {} - {}ms", req.getMethod(), req.getPath(), duration);
});
```

### 日志系统
```java
Logger consoleLogger = ConsoleLogs.getLogger(EstDemoApplication.class);
Logger fileLogger = FileLogs.getLogger("est-demo.log");

consoleLogger.info("这是一条控制台日志");
fileLogger.info("这是一条文件日志");
```

### 缓存系统
```java
Cache<String, Object> memoryCache = new MemoryCache<>();
Cache<String, Object> fileCache = new FileCache<>("est-demo-cache");

memoryCache.put("key", "value");
Object value = memoryCache.get("key");
```

### 事件总线
```java
EventBus localEventBus = new LocalEventBus();
EventBus asyncEventBus = new AsyncEventBus();

localEventBus.subscribe(UserCreatedEvent.class, event -> {
    consoleLogger.info("收到用户创建事件: {}", event.getUsername());
});

localEventBus.publish(new UserCreatedEvent("username", "email"));
```

### 任务调度
```java
Scheduler scheduler = new FixedScheduler();

scheduler.scheduleAtFixedRate(() -> {
    consoleLogger.debug("定时任务执行");
}, 10, 30, TimeUnit.SECONDS);
```

### 系统监控
```java
Monitor jvmMonitor = new JvmMonitor();
Monitor systemMonitor = new SystemMonitor();

Object heapMemory = jvmMonitor.getMetric("heapMemoryUsage");
Object processors = systemMonitor.getMetric("availableProcessors");
```

### 数据存储
```java
Repository<String, User> userRepository = new MemoryRepository<>();

userRepository.save("1", new User("1", "Alice", "alice@example.com", "admin"));
User user = userRepository.findById("1").orElse(null);
List<User> allUsers = userRepository.findAll();
```

## 项目结构

```
est-demo/
├── pom.xml                          # Maven配置文件
├── README.md                        # 本文档
├── QUICKSTART.md                    # 快速开始指南
├── run.bat                          # Windows运行脚本
├── run.ps1                          # PowerShell运行脚本
├── run-with-classes.ps1             # 类路径运行脚本
└── src/
    └── main/
        └── java/
            └── ltd/
                └── idcu/
                    └── est/
                        └── demo/
                            └── EstDemoApplication.java  # 主应用类
```

## 依赖的EST模块

演示应用依赖以下EST模块：
- `est-web-impl` - Web容器实现
- `est-util-common` - 通用工具
- `est-util-format-json` - JSON格式处理
- `est-logging-console` - 控制台日志
- `est-logging-file` - 文件日志
- `est-cache-memory` - 内存缓存
- `est-cache-file` - 文件缓存
- `est-event-local` - 本地事件总线
- `est-event-async` - 异步事件总线
- `est-scheduler-fixed` - 固定频率调度
- `est-scheduler-cron` - Cron表达式调度
- `est-monitor-jvm` - JVM监控
- `est-monitor-system` - 系统监控
- `est-data-memory` - 内存数据存储
- `est-config-impl` - 配置管理

## 下一步
查看EST框架的其他模块，了解更多功能：
- Redis缓存
- 安全认证（Basic, JWT, OAuth2）
- 消息系统（Kafka, RabbitMQ等）
- 工作流引擎
- 断路器
- 服务发现
- API网关
- 等等

## 许可证
MIT License

## 相关链接

- [EST框架文档](../README.md)
- [EST Web模块文档](../est-app/est-web/README.md)