# EST框架详细用户指南

## 目录

- [1. 快速入门](#1-快速入门)
- [2. 核心模块](#2-核心模块)
- [3. 功能模块](#3-功能模块)
- [4. Web模块](#4-Web模块)
- [5. 高级特性](#5-高级特性)
- [6. 最佳实践](#6-最佳实践)
- [7. 常见问题](#7-常见问题)
- [8. 示例项目](#8-示例项目)

---

## 1. 快速入门

### 1.1 基本使用示例

```java
import ltd.idcu.est.core.DefaultEstApplication;
import ltd.idcu.est.core.api.EstApplication;

public class FirstApp {
    public static void main(String[] args) {
        // 创建EST应用
        EstApplication app = DefaultEstApplication.create();
        
        // 启动应用
        app.run();
        
        System.out.println("EST Application started!");
    }
}
```

### 1.2 项目结构

推荐的EST项目结构：

```
src/main/java/
└── com/yourcompany/
    ├── config/         # 配置类
    ├── service/        # 业务逻辑
    ├── model/          # 数据模型
    ├── repository/     # 数据访问
    ├── middleware/     # 中间件
    ├── util/           # 工具类
    └── Application.java  # 应用入口
```

---

## 2. 核心模块

### 2.1 依赖注入容器

```java
import ltd.idcu.est.core.DefaultEstApplication;
import ltd.idcu.est.core.api.EstApplication;
import ltd.idcu.est.core.api.Container;

public class DependencyInjectionExample {
    public static void main(String[] args) {
        EstApplication app = DefaultEstApplication.create();
        Container container = app.getContainer();
        
        // 注册服务
        container.register(UserService.class, new UserServiceImpl());
        
        // 获取服务
        UserService userService = container.get(UserService.class);
        
        app.run();
    }
    
    interface UserService {
        void doSomething();
    }
    
    static class UserServiceImpl implements UserService {
        @Override
        public void doSomething() {
            System.out.println("UserService doing something");
        }
    }
}
```

### 2.2 配置管理

```java
import ltd.idcu.est.core.DefaultEstApplication;
import ltd.idcu.est.core.api.EstApplication;
import ltd.idcu.est.core.api.Configuration;

public class ConfigurationExample {
    public static void main(String[] args) {
        EstApplication app = DefaultEstApplication.create();
        Configuration config = app.getConfiguration();
        
        // 设置配置
        config.set("app.name", "My EST App");
        config.set("app.version", "1.0.0");
        config.set("server.port", 8080);
        
        // 获取配置
        String appName = config.getString("app.name");
        int port = config.getInt("server.port", 8080);
        
        System.out.println("App Name: " + appName);
        System.out.println("Server Port: " + port);
        
        app.run();
    }
}
```

### 2.3 模块管理

```java
import ltd.idcu.est.core.DefaultEstApplication;
import ltd.idcu.est.core.api.EstApplication;
import ltd.idcu.est.core.api.Module;

public class ModuleManagementExample {
    public static void main(String[] args) {
        EstApplication app = DefaultEstApplication.create();
        
        // 注册模块
        app.registerModule(new CustomModule());
        
        // 启动应用
        app.run();
    }
    
    static class CustomModule implements Module {
        @Override
        public String getName() {
            return "custom-module";
        }
        
        @Override
        public void initialize(EstApplication application) {
            System.out.println("Custom module initialized");
        }
        
        @Override
        public void shutdown() {
            System.out.println("Custom module shutdown");
        }
    }
}
```

---

## 3. 功能模块

### 3.1 缓存系统

```java
import ltd.idcu.est.core.DefaultEstApplication;
import ltd.idcu.est.core.api.EstApplication;
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.DefaultMemoryCache;

public class CacheExample {
    public static void main(String[] args) {
        EstApplication app = DefaultEstApplication.create();
        
        // 创建内存缓存
        Cache<String, String> cache = new DefaultMemoryCache<>();
        
        // 设置缓存
        cache.put("key1", "value1", 3600); // 1小时过期
        
        // 获取缓存
        String value = cache.get("key1");
        System.out.println("Cache value: " + value);
        
        // 删除缓存
        cache.remove("key1");
        
        app.run();
    }
}
```

### 3.2 事件系统

```java
import ltd.idcu.est.core.DefaultEstApplication;
import ltd.idcu.est.core.api.EstApplication;
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.DefaultLocalEventBus;

public class EventExample {
    public static void main(String[] args) {
        EstApplication app = DefaultEstApplication.create();
        
        // 创建事件总线
        EventBus eventBus = new DefaultLocalEventBus();
        
        // 订阅事件
        eventBus.subscribe(String.class, event -> {
            System.out.println("Received event: " + event);
        });
        
        // 发布事件
        eventBus.publish("Hello, Event System!");
        
        app.run();
    }
}
```

### 3.3 日志系统

```java
import ltd.idcu.est.core.DefaultEstApplication;
import ltd.idcu.est.core.api.EstApplication;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.DefaultConsoleLogger;

public class LoggingExample {
    public static void main(String[] args) {
        EstApplication app = DefaultEstApplication.create();
        
        // 创建日志器
        Logger logger = new DefaultConsoleLogger(LoggingExample.class);
        
        // 日志级别
        logger.debug("Debug message");
        logger.info("Info message");
        logger.warn("Warn message");
        logger.error("Error message");
        
        app.run();
    }
}
```

### 3.4 安全系统

```java
import ltd.idcu.est.core.DefaultEstApplication;
import ltd.idcu.est.core.api.EstApplication;
import ltd.idcu.est.features.security.api.SecurityManager;
import ltd.idcu.est.features.security.basic.DefaultBasicSecurityManager;

public class SecurityExample {
    public static void main(String[] args) {
        EstApplication app = DefaultEstApplication.create();
        
        // 创建安全管理器
        SecurityManager securityManager = new DefaultBasicSecurityManager();
        
        // 添加用户
        securityManager.addUser("admin", "password123");
        
        // 验证用户
        boolean authenticated = securityManager.authenticate("admin", "password123");
        System.out.println("Authenticated: " + authenticated);
        
        app.run();
    }
}
```

### 3.5 调度系统

```java
import ltd.idcu.est.core.DefaultEstApplication;
import ltd.idcu.est.core.api.EstApplication;
import ltd.idcu.est.features.scheduler.api.Scheduler;
import ltd.idcu.est.features.scheduler.cron.DefaultCronScheduler;

public class SchedulerExample {
    public static void main(String[] args) {
        EstApplication app = DefaultEstApplication.create();
        
        // 创建调度器
        Scheduler scheduler = new DefaultCronScheduler();
        
        // 定时任务（每秒钟执行一次）
        scheduler.schedule("* * * * * *", () -> {
            System.out.println("Task executed at: " + System.currentTimeMillis());
        });
        
        app.run();
    }
}
```

### 3.6 监控系统

```java
import ltd.idcu.est.core.DefaultEstApplication;
import ltd.idcu.est.core.api.EstApplication;
import ltd.idcu.est.features.monitor.api.Monitor;
import ltd.idcu.est.features.monitor.jvm.DefaultJvmMonitor;

public class MonitorExample {
    public static void main(String[] args) {
        EstApplication app = DefaultEstApplication.create();
        
        // 创建JVM监控
        Monitor jvmMonitor = new DefaultJvmMonitor();
        
        // 获取监控数据
        System.out.println("JVM Memory: " + jvmMonitor.getMetrics().get("memory.used"));
        System.out.println("JVM Threads: " + jvmMonitor.getMetrics().get("threads.count"));
        
        app.run();
    }
}
```

---

## 4. Web模块

### 4.1 基础Web应用

```java
import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.api.WebApplication;

public class BasicWebApp {
    public static void main(String[] args) {
        // 创建Web应用
        WebApplication app = DefaultWebApplication.create();
        
        // 配置路由
        app.getRouter().get("/", (req, res) -> {
            res.html("<h1>Hello, EST Web!</h1>");
        });
        
        // 启动服务器
        app.run(8080);
        System.out.println("Server started at http://localhost:8080");
    }
}
```

### 4.2 路由系统

```java
import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.api.WebApplication;

public class RoutingExample {
    public static void main(String[] args) {
        WebApplication app = DefaultWebApplication.create();
        
        // 基本路由
        app.getRouter().get("/", (req, res) -> res.text("Home"));
        app.getRouter().post("/submit", (req, res) -> res.text("Form submitted"));
        
        // 路径参数
        app.getRouter().get("/users/:id", (req, res) -> {
            String id = req.getPathVariable("id");
            res.text("User: " + id);
        });
        
        // 路由组
        app.getRouter().group("/api", (group) -> {
            group.get("/users", (req, res) -> res.text("API Users"));
            group.get("/posts", (req, res) -> res.text("API Posts"));
        });
        
        app.run(8080);
    }
}
```

### 4.3 中间件

```java
import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Middleware;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

public class MiddlewareExample {
    public static void main(String[] args) {
        WebApplication app = DefaultWebApplication.create();
        
        // 自定义中间件
        app.use((Middleware) new Middleware() {
            @Override
            public String getName() {
                return "custom-middleware";
            }
            
            @Override
            public int getPriority() {
                return 100;
            }
            
            @Override
            public boolean before(Request request, Response response) {
                System.out.println("Request received: " + request.getPath());
                return true;
            }
        });
        
        app.getRouter().get("/", (req, res) -> res.text("Hello with middleware"));
        
        app.run(8080);
    }
}
```

### 4.4 会话管理

```java
import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Session;

public class SessionExample {
    public static void main(String[] args) {
        WebApplication app = DefaultWebApplication.create();
        
        // 登录
        app.getRouter().post("/login", (req, res) -> {
            String username = req.getParameter("username");
            if ("admin".equals(username)) {
                Session session = req.getSession(true);
                session.setAttribute("username", username);
                res.text("Login successful");
            } else {
                res.status(401).text("Unauthorized");
            }
        });
        
        // 个人资料
        app.getRouter().get("/profile", (req, res) -> {
            Session session = req.getSession(false);
            if (session != null) {
                String username = (String) session.getAttribute("username");
                res.text("Welcome, " + username);
            } else {
                res.status(401).text("Please login");
            }
        });
        
        app.run(8080);
    }
}
```

### 4.5 控制器

```java
import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Controller;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

public class ControllerExample {
    public static void main(String[] args) {
        WebApplication app = DefaultWebApplication.create();
        
        // 注册控制器
        app.controller("/users", UserController.class);
        
        app.run(8080);
    }
    
    static class UserController implements Controller {
        @Override
        public void handle(Request request, Response response) {
            String method = request.getMethod().getMethod();
            if ("GET".equals(method)) {
                response.text("List users");
            } else if ("POST".equals(method)) {
                response.text("Create user");
            }
        }
    }
}
```

### 4.6 静态文件服务

```java
import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.api.WebApplication;

public class StaticFilesExample {
    public static void main(String[] args) {
        WebApplication app = DefaultWebApplication.create();
        
        // 配置静态文件
        app.staticFiles("/static", "./public");
        
        app.getRouter().get("/", (req, res) -> {
            res.html("<h1>Static Files Example</h1><img src='/static/images/logo.png'>");
        });
        
        app.run(8080);
    }
}
```

### 4.7 错误处理

```java
import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.api.WebApplication;

public class ErrorHandlingExample {
    public static void main(String[] args) {
        WebApplication app = DefaultWebApplication.create();
        
        // 全局错误处理
        app.errorHandler(ex -> {
            System.err.println("Error: " + ex.getMessage());
        });
        
        // 测试错误
        app.getRouter().get("/error", (req, res) -> {
            throw new RuntimeException("Test error");
        });
        
        // 404处理
        app.getRouter().get("/*", (req, res) -> {
            res.status(404).text("Page not found");
        });
        
        app.run(8080);
    }
}
```

---

## 5. 高级特性

### 5.1 虚拟线程支持

```java
import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.api.WebApplication;

public class VirtualThreadsExample {
    public static void main(String[] args) {
        WebApplication app = DefaultWebApplication.create();
        
        // 使用虚拟线程处理请求
        app.getRouter().get("/virtual", (req, res) -> {
            // 模拟耗时操作
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            res.text("Processed with virtual thread: " + Thread.currentThread().getName());
        });
        
        app.run(8080);
    }
}
```

### 5.2 插件系统

```java
import ltd.idcu.est.core.DefaultEstApplication;
import ltd.idcu.est.core.api.EstApplication;
import ltd.idcu.est.plugin.api.Plugin;
import ltd.idcu.est.plugin.api.PluginContext;

public class PluginExample {
    public static void main(String[] args) {
        EstApplication app = DefaultEstApplication.create();
        
        // 加载插件
        app.getPluginManager().loadPlugin(new CustomPlugin());
        
        app.run();
    }
    
    static class CustomPlugin implements Plugin {
        @Override
        public String getName() {
            return "custom-plugin";
        }
        
        @Override
        public String getVersion() {
            return "1.0.0";
        }
        
        @Override
        public void initialize(PluginContext context) {
            System.out.println("Custom plugin initialized");
        }
        
        @Override
        public void shutdown() {
            System.out.println("Custom plugin shutdown");
        }
    }
}
```

### 5.3 性能优化

```java
import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.features.cache.memory.DefaultMemoryCache;

public class PerformanceOptimizationExample {
    public static void main(String[] args) {
        WebApplication app = DefaultWebApplication.create();
        
        // 缓存热点数据
        DefaultMemoryCache<String, String> cache = new DefaultMemoryCache<>();
        
        app.getRouter().get("/cached", (req, res) -> {
            String key = "data";
            String data = cache.get(key);
            
            if (data == null) {
                // 模拟数据加载
                data = "Expensive data to load";
                cache.put(key, data, 3600);
            }
            
            res.text("Cached data: " + data);
        });
        
        app.run(8080);
    }
}
```

---

## 6. 最佳实践

### 6.1 代码组织

- **分层架构**：控制器 → 服务 → 仓库 → 数据模型
- **接口优先**：通过接口定义服务契约
- **依赖注入**：使用容器管理依赖关系
- **模块化**：按功能划分模块
- **代码风格**：统一的命名规范和代码格式

### 6.2 错误处理

- **统一异常处理**：使用全局错误处理器
- **详细日志**：记录错误详情便于调试
- **友好错误提示**：对用户展示友好的错误信息
- **异常分类**：根据不同类型的异常采取不同处理策略

### 6.3 安全性

- **输入验证**：验证所有用户输入
- **密码加密**：使用安全的密码哈希算法
- **CORS 配置**：正确配置跨域资源共享
- **认证授权**：实现适当的认证和授权机制
- **HTTPS**：在生产环境使用 HTTPS
- **防止 SQL 注入**：使用参数化查询
- **防止 XSS 攻击**：对输出进行转义

---

## 7. 常见问题

### 7.1 依赖注入

**Q: 如何在控制器中注入服务？**

A: 可以通过构造函数注入或使用容器获取：

```java
public class UserController implements Controller {
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @Override
    public void handle(Request request, Response response) {
        // 使用 userService
    }
}
```

### 7.2 性能问题

**Q: 应用启动慢怎么办？**

A: 检查是否有耗时的初始化操作，考虑延迟加载非关键组件，使用缓存减少重复计算。

### 7.3 部署问题

**Q: 如何在生产环境部署EST应用？**

A: 可以打包为可执行JAR，使用systemd管理服务，或使用Docker容器化部署。

### 7.4 内存泄漏

**Q: 应用内存使用持续增长怎么办？**

A: 检查是否有未关闭的资源，使用内存分析工具定位泄漏点，确保会话和缓存有适当的过期策略。

---

## 8. 示例项目

### 8.1 基础示例

**est-examples-basic**：展示EST框架的基本使用，包括核心模块和基础功能。

### 8.2 高级示例

**est-examples-advanced**：展示EST框架的高级特性，包括虚拟线程、插件系统和性能优化。

### 8.3 功能示例

**est-examples-features**：展示各种功能模块的使用，包括缓存、事件、日志、安全、调度和监控。

### 8.4 Web示例

**est-examples-web**：展示Web模块的使用，包括路由、中间件、控制器、会话管理等。

### 8.5 完整应用示例

```java
import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.LoggingMiddleware;

public class CompleteApplication {
    public static void main(String[] args) {
        WebApplication app = DefaultWebApplication.create();
        
        // 添加中间件
        app.use(new LoggingMiddleware());
        app.enableCors();
        
        // 配置路由
        app.getRouter().get("/", (req, res) -> {
            res.html("<h1>EST Framework</h1><p>Complete application example</p>");
        });
        
        // API路由
        app.getRouter().group("/api", (api) -> {
            api.get("/status", (req, res) -> {
                res.json(java.util.Map.of("status", "ok", "time", System.currentTimeMillis()));
            });
        });
        
        // 静态文件
        app.staticFiles("/static", "./public");
        
        // 启动服务器
        app.run(8080);
        System.out.println("Server started at http://localhost:8080");
    }
}
```

---

## 9. 总结

EST框架是一个现代化的Java框架，提供了丰富的功能和灵活的架构。通过本指南，您应该已经了解了如何使用EST框架构建各种应用。

### 后续步骤

1. **探索示例**：查看`est-examples`目录中的各种示例
2. **阅读API文档**：使用`mvn javadoc:aggregate`生成详细的API文档
3. **构建应用**：使用EST框架构建您的下一个项目
4. **贡献代码**：如果您有改进建议，欢迎贡献代码

EST框架持续发展中，我们期待您的反馈和贡献！