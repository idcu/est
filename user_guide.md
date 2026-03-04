# EST 用户指南

## 目录

- [简介](#简介)
- [环境要求](#环境要求)
- [安装与构建](#安装与构建)
- [快速开始](#快速开始)
- [核心模块](#核心模块)
- [功能模块](#功能模块)
- [Web模块](#web模块)
- [最佳实践](#最佳实践)
- [常见问题](#常见问题)

---

## 简介

EST 是一个零依赖的现代 Java 框架，采用递进式模块结构设计。

### 核心特性

- **零依赖**：不依赖任何第三方库，避免依赖冲突和版本管理问题
- **递进式模块**：每个层级的模块都可以独立被其他项目引用
- **低耦合设计**：模块间通过接口通信，减少直接依赖
- **现代 Java**：基于 Java 21+，支持虚拟线程等现代特性
- **高性能**：优化核心组件性能，支持并发操作
- **Collection 增强**：提供类似 Laravel Collection 的链式数据处理能力

### 项目结构

```
est/
├── est-core/          # 核心模块
├── est-patterns/      # 设计模式模块
├── est-utils/         # 工具模块
├── est-test/          # 测试模块
├── est-collection/    # Collection模块
├── est-features/      # 功能模块
├── est-plugin/        # 插件模块
├── est-web/           # Web模块
└── est-examples/      # 示例模块
```

---

## 环境要求

- **JDK**: 21 或更高版本
- **Maven**: 3.6 或更高版本
- **操作系统**: Windows、Linux 或 macOS

---

## 安装与构建

### 克隆项目

```bash
git clone https://github.com/idcu/est.git
cd est
```

### 构建项目

```bash
# 构建所有模块
mvn clean install

# 构建指定模块
mvn clean install -pl est-core

# 跳过测试
mvn clean install -DskipTests

# 生成Javadoc
mvn javadoc:aggregate
```

### 添加依赖

在您的项目的 `pom.xml` 中添加 EST 框架的依赖：

```xml
<!-- 只引用核心接口 -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-api</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>

<!-- 引用核心实现 -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-impl</artifactId>
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

---

## 快速开始

### Hello World 示例

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

public class HelloWorld {
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        container.registerSingleton(String.class, "Hello, EST Framework!");
        
        String message = container.get(String.class);
        System.out.println(message);
    }
}
```

### 运行示例

EST 框架提供了丰富的示例代码，位于 `est-examples` 模块中：

```bash
# 编译并运行示例
cd est-examples/est-examples-basic
mvn compile exec:java -Dexec.mainClass="ltd.idcu.est.examples.basic.Main"
```

---

## 核心模块

### 1. 依赖注入容器

EST 提供了轻量级的依赖注入容器：

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

public class ContainerExample {
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        
        // 注册单例服务
        container.registerSingleton(MyService.class, new MyServiceImpl());
        
        // 注册工厂服务
        container.register(MyService.class, () -> new MyServiceImpl());
        
        // 获取服务
        MyService service = container.get(MyService.class);
        service.doSomething();
    }
}
```

### 2. 配置管理

```java
import ltd.idcu.est.core.api.Config;
import ltd.idcu.est.core.impl.DefaultConfig;

public class ConfigExample {
    public static void main(String[] args) {
        Config config = new DefaultConfig();
        
        // 设置配置
        config.set("app.name", "My Application");
        config.set("app.port", 8080);
        config.set("app.debug", true);
        
        // 获取配置
        String appName = config.getString("app.name");
        int port = config.getInt("app.port", 80);
        boolean debug = config.getBoolean("app.debug", false);
        
        System.out.println("App Name: " + appName);
    }
}
```

### 3. 模块管理

```java
import ltd.idcu.est.core.api.Module;
import ltd.idcu.est.core.impl.AbstractModule;

public class ModuleExample {
    public static void main(String[] args) {
        Module module = new AbstractModule("my-module", "1.0.0") {
            @Override
            protected void doInitialize() {
                System.out.println("Module initialized");
            }

            @Override
            protected void doStart() {
                System.out.println("Module started");
            }

            @Override
            protected void doStop() {
                System.out.println("Module stopped");
            }
        };
        
        module.initialize();
        module.start();
        module.stop();
    }
}
```

---

## 功能模块

### 1. 缓存系统

EST 提供多种缓存实现：

#### 内存缓存

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.MemoryCache;

public class CacheExample {
    public static void main(String[] args) {
        Cache<String, String> cache = new MemoryCache<>();
        
        // 存储数据
        cache.put("key1", "value1");
        cache.put("key2", "value2", 3600); // 1小时过期
        
        // 获取数据
        String value = cache.get("key1");
        
        // 删除数据
        cache.remove("key1");
        
        // 清空缓存
        cache.clear();
    }
}
```

#### 文件缓存

```java
import ltd.idcu.est.features.cache.file.FileCache;

public class FileCacheExample {
    public static void main(String[] args) {
        Cache<String, String> cache = new FileCache<>("./cache");
        
        cache.put("key", "value");
        String value = cache.get("key");
    }
}
```

#### Redis 缓存

```java
import ltd.idcu.est.features.cache.redis.RedisCache;

public class RedisCacheExample {
    public static void main(String[] args) {
        Cache<String, String> cache = new RedisCache<>("localhost", 6379);
        
        cache.put("key", "value");
        String value = cache.get("key");
    }
}
```

### 2. 事件系统

#### 本地事件

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEventBus;

public class EventExample {
    public static void main(String[] args) {
        EventBus eventBus = new LocalEventBus();
        
        // 注册监听器
        eventBus.subscribe(MyEvent.class, event -> {
            System.out.println("Received event: " + event.getMessage());
        });
        
        // 发布事件
        eventBus.publish(new MyEvent("Hello, Event!"));
    }
    
    static class MyEvent {
        private final String message;
        
        public MyEvent(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
```

#### 异步事件

```java
import ltd.idcu.est.features.event.async.AsyncEventBus;

public class AsyncEventExample {
    public static void main(String[] args) {
        EventBus eventBus = new AsyncEventBus();
        
        eventBus.subscribe(MyEvent.class, event -> {
            System.out.println("Async received: " + event.getMessage());
        });
        
        eventBus.publish(new MyEvent("Hello, Async!"));
    }
}
```

### 3. 日志系统

#### 控制台日志

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.api.LogLevel;
import ltd.idcu.est.features.logging.console.ConsoleLogger;

public class LoggingExample {
    public static void main(String[] args) {
        Logger logger = new ConsoleLogger();
        
        logger.log(LogLevel.INFO, "Application started");
        logger.debug("Debug message");
        logger.info("Info message");
        logger.warn("Warning message");
        logger.error("Error message");
    }
}
```

#### 文件日志

```java
import ltd.idcu.est.features.logging.file.FileLogger;

public class FileLoggingExample {
    public static void main(String[] args) {
        Logger logger = new FileLogger("./logs/app.log");
        
        logger.info("This will be written to file");
    }
}
```

### 4. 安全系统

#### 基础认证

```java
import ltd.idcu.est.features.security.api.*;
import ltd.idcu.est.features.security.basic.*;

public class SecurityExample {
    public static void main(String[] args) {
        UserDetailsService userService = new InMemoryUserDetailsService();
        userService.createUser(new DefaultUser("admin", "password123"));
        
        AuthenticationProvider authProvider = new BasicAuthenticationProvider(userService);
        Authentication auth = authProvider.authenticate("admin", "password123");
        
        if (auth.isAuthenticated()) {
            System.out.println("User authenticated: " + auth.getUsername());
        }
    }
}
```

#### JWT 认证

```java
import ltd.idcu.est.features.security.jwt.JwtTokenProvider;

public class JwtExample {
    public static void main(String[] args) {
        TokenProvider tokenProvider = new JwtTokenProvider("my-secret-key");
        
        // 生成token
        String token = tokenProvider.generateToken("user123");
        
        // 验证token
        Token parsedToken = tokenProvider.validateToken(token);
        if (parsedToken.isValid()) {
            System.out.println("Username: " + parsedToken.getSubject());
        }
    }
}
```

### 5. 调度系统

#### Cron 调度

```java
import ltd.idcu.est.features.scheduler.api.Scheduler;
import ltd.idcu.est.features.scheduler.cron.CronScheduler;

public class CronSchedulerExample {
    public static void main(String[] args) throws Exception {
        Scheduler scheduler = new CronScheduler();
        
        // 每分钟执行一次
        scheduler.schedule("0 * * * * ?", () -> {
            System.out.println("Task executed at: " + System.currentTimeMillis());
        });
        
        scheduler.start();
        Thread.sleep(60000);
        scheduler.stop();
    }
}
```

#### 固定间隔调度

```java
import ltd.idcu.est.features.scheduler.fixed.FixedRateScheduler;

public class FixedRateExample {
    public static void main(String[] args) throws Exception {
        Scheduler scheduler = new FixedRateScheduler();
        
        // 每5秒执行一次
        scheduler.schedule(5000, () -> {
            System.out.println("Fixed rate task executed");
        });
        
        scheduler.start();
        Thread.sleep(30000);
        scheduler.stop();
    }
}
```

---

## Web模块

### 基础 Web 应用

```java
import ltd.idcu.est.web.api.*;
import ltd.idcu.est.web.impl.*;

public class WebAppExample {
    public static void main(String[] args) throws Exception {
        WebApplication app = new DefaultWebApplication();
        
        // 设置路由
        Router router = app.getRouter();
        
        router.get("/", (request, response) -> {
            response.setBody("Hello, EST Web!");
            response.setContentType("text/plain");
        });
        
        router.get("/hello/:name", (request, response) -> {
            String name = request.getPathParam("name");
            response.setBody("Hello, " + name + "!");
            response.setContentType("text/plain");
        });
        
        // 启动服务器
        app.start(8080);
        System.out.println("Server started on http://localhost:8080");
    }
}
```

### RESTful API

```java
import ltd.idcu.est.web.api.*;
import ltd.idcu.est.web.impl.*;
import ltd.idcu.est.utils.format.json.JsonUtils;

public class RestApiExample {
    public static void main(String[] args) throws Exception {
        WebApplication app = new DefaultWebApplication();
        Router router = app.getRouter();
        
        // GET /api/users
        router.get("/api/users", (request, response) -> {
            response.setBody(JsonUtils.toJson(new String[]{"user1", "user2", "user3"}));
            response.setContentType("application/json");
        });
        
        // POST /api/users
        router.post("/api/users", (request, response) -> {
            String body = request.getBody();
            response.setStatus(HttpStatus.CREATED);
            response.setBody("{\"success\": true}");
            response.setContentType("application/json");
        });
        
        app.start(8080);
    }
}
```

### 中间件

```java
import ltd.idcu.est.web.api.*;
import ltd.idcu.est.web.impl.*;

public class MiddlewareExample {
    public static void main(String[] args) throws Exception {
        WebApplication app = new DefaultWebApplication();
        
        // 添加日志中间件
        app.use(new LoggingMiddleware());
        
        // 添加CORS中间件
        app.use(new DefaultCorsMiddleware());
        
        // 添加自定义中间件
        app.use((request, response, next) -> {
            System.out.println("Request: " + request.getMethod() + " " + request.getPath());
            long startTime = System.currentTimeMillis();
            
            next.handle(request, response);
            
            long duration = System.currentTimeMillis() - startTime;
            System.out.println("Response: " + response.getStatus() + " (" + duration + "ms)");
        });
        
        Router router = app.getRouter();
        router.get("/", (req, res) -> res.setBody("Hello with middleware!"));
        
        app.start(8080);
    }
}
```

### 会话管理

```java
import ltd.idcu.est.web.api.*;
import ltd.idcu.est.web.impl.*;

public class SessionExample {
    public static void main(String[] args) throws Exception {
        WebApplication app = new DefaultWebApplication();
        Router router = app.getRouter();
        
        // 设置会话
        router.get("/login", (request, response) -> {
            Session session = request.getSession(true);
            session.setAttribute("username", "admin");
            response.setBody("Logged in");
        });
        
        // 获取会话
        router.get("/profile", (request, response) -> {
            Session session = request.getSession(false);
            if (session != null && session.getAttribute("username") != null) {
                String username = (String) session.getAttribute("username");
                response.setBody("Profile: " + username);
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED);
                response.setBody("Please login first");
            }
        });
        
        // 销毁会话
        router.get("/logout", (request, response) -> {
            Session session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.setBody("Logged out");
        });
        
        app.start(8080);
    }
}
```

### 静态文件服务

```java
import ltd.idcu.est.web.api.*;
import ltd.idcu.est.web.impl.*;

public class StaticFileExample {
    public static void main(String[] args) throws Exception {
        WebApplication app = new DefaultWebApplication();
        
        // 配置静态文件服务
        app.setStaticFileHandler(new DefaultStaticFileHandler("./public"));
        
        app.start(8080);
        System.out.println("Static files served from ./public");
    }
}
```

---

## 最佳实践

### 1. 模块划分

- 按照功能划分模块，保持模块的单一职责
- 优先使用接口，而非直接依赖实现
- 合理使用依赖注入，降低耦合度

### 2. 性能优化

- 使用内存缓存提升高频访问数据的性能
- 合理使用异步事件处理耗时操作
- 对于Web应用，启用静态文件缓存

### 3. 代码组织

```
src/main/java/
└── com/yourcompany/
    ├── config/        # 配置类
    ├── controller/    # 控制器
    ├── service/       # 业务逻辑
    ├── repository/    # 数据访问
    ├── model/         # 数据模型
    └── util/          # 工具类
```

### 4. 错误处理

- 统一的异常处理机制
- 合理的日志记录
- 友好的错误信息返回

---

## 常见问题

### Q: EST 框架真的零依赖吗？

A: 是的，EST 框架的所有功能都使用 Java 标准库实现，不依赖任何第三方库。

### Q: 如何只使用框架的部分功能？

A: EST 采用递进式模块设计，您可以只引用需要的模块。例如，只需要缓存功能，只需引用 `est-features-cache-memory`。

### Q: 支持哪些 Java 版本？

A: EST 框架基于 Java 21+ 开发，需要 Java 21 或更高版本。

### Q: 如何贡献代码？

A: 请查看项目根目录下的 `contributing.md` 文件，了解详细的贡献指南。

### Q: 框架是否支持虚拟线程？

A: 是的，EST 框架充分利用了 Java 21 的特性，包括虚拟线程支持。

---

## 获取帮助

- GitHub: https://github.com/idcu/est
- 问题反馈: https://github.com/idcu/est/issues
- 示例代码: 查看 `est-examples` 模块

---

## 许可证

MIT License
