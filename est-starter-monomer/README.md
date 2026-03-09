# EST Monomer - 单体应用启动器

EST Monomer 是 EST 框架的单体应用启动器，为开发者提供一个开箱即用的应用模板，可以快速开始您的项目开发。

## 项目介绍

EST Monomer 是一个功能完备的单体应用，整合了 EST 框架的核心模块，包括：

- **Web 服务** - HTTP 服务和 RESTful API
- **日志系统** - 控制台和文件日志
- **缓存系统** - 内存缓存
- **事件总线** - 本地事件总线
- **任务调度** - 固定频率调度
- **系统监控** - JVM 和系统指标监控
- **管理后台** - est-admin 集成（可选）

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.6+

### 构建项目

在项目根目录执行：

```bash
cd est-starter-monomer
mvn clean compile
```

### 运行应用

#### Windows

```bash
run.bat
```

#### Linux/Mac

```bash
chmod +x run.sh
./run.sh
```

或者直接使用 Maven：

```bash
mvn exec:java -Dexec.mainClass="ltd.idcu.est.starter.monomer.EstMonomerApplication"
```

### 访问应用

启动成功后，在浏览器中访问：

- **首页**: http://localhost:8080
- **健康检查**: http://localhost:8080/api/system/health
- **系统信息**: http://localhost:8080/api/system/info

## 项目结构

```
est-starter-monomer/
├── pom.xml                          # Maven 配置文件
├── README.md                        # 本文档
├── QUICKSTART.md                    # 快速开始指南
├── run.bat                          # Windows 运行脚本
├── run.sh                           # Linux/Mac 运行脚本
└── src/
    └── main/
        └── java/
            └── ltd/
                └── idcu/
                    └── est/
                        └── starter/
                            └── monomer/
                                └── EstMonomerApplication.java  # 主应用类
```

## 核心功能

### 1. Web 服务

使用 EST Web 模块提供 HTTP 服务：

```java
WebApplication app = Web.create("EST Monomer", "2.3.0-SNAPSHOT");

app.use((req, res, next) -> {
    // 中间件
    next.handle();
});

app.routes(router -> {
    router.get("/", (req, res) -> {
        res.text("Hello World!");
    });
});

app.run(8080);
```

### 2. 日志系统

集成控制台和文件日志：

```java
Logger consoleLogger = ConsoleLogs.getLogger(EstMonomerApplication.class);
Logger fileLogger = FileLogs.getLogger("est-monomer.log");

consoleLogger.info("这是一条控制台日志");
fileLogger.info("这是一条文件日志");
```

### 3. 缓存系统

使用内存缓存：

```java
Cache<String, Object> cache = new MemoryCache<>();

cache.put("key", "value");
Object value = cache.get("key");
```

### 4. 事件总线

发布和订阅事件：

```java
EventBus eventBus = new LocalEventBus();

eventBus.subscribe(MyEvent.class, event -> {
    System.out.println("收到事件: " + event.getMessage());
});

eventBus.publish(new MyEvent("Hello Event!"));
```

### 5. 任务调度

定时执行任务：

```java
Scheduler scheduler = new FixedScheduler();

scheduler.scheduleAtFixedRate(() -> {
    System.out.println("定时任务执行");
}, 10, 30, TimeUnit.SECONDS);
```

## API 端点

### 系统 API

- `GET /api/system/health` - 健康检查
- `GET /api/system/info` - 系统信息

### 缓存 API

- `GET /api/cache/memory` - 列出所有缓存键
- `GET /api/cache/memory/:key` - 获取缓存值
- `PUT /api/cache/memory/:key` - 设置缓存值
- `DELETE /api/cache/memory/:key` - 删除缓存

### 监控 API

- `GET /api/monitor/jvm` - JVM 指标
- `GET /api/monitor/system` - 系统指标
- `GET /api/monitor/stats` - 应用统计

### 示例 API

- `GET /api/hello?name=xxx` - 问候接口

## 集成 est-admin

要集成管理后台，需要在 pom.xml 中添加 est-admin 依赖（已配置），然后参考 [est-admin 文档](../est-app/est-admin/README.md) 进行配置。

## 下一步

- 查看 [EST 框架文档](../README.md) 了解更多功能
- 探索 [EST 模块](../est-modules/) 扩展您的应用
- 参考 [est-admin](../est-app/est-admin/) 构建管理后台
- 查看 [前端 UI](../est-admin-ui/) 了解前端集成

## 许可证

MIT License

## 相关链接

- [EST 框架主页](https://github.com/idcu/est)
- [est-admin 管理后台](../est-app/est-admin/README.md)
- [est-admin-ui 前端](../est-admin-ui/README.md)
