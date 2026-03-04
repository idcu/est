# EST 示例文档

本文档提供了 EST 各个功能模块的详细使用示例，帮助开发者快速上手并熟练运用框架的各项特性。

## 目录

- [核心模块示例](#核心模块示例)
- [工具模块示例](#工具模块示例)
- [设计模式示例](#设计模式示例)
- [Collection 示例](#collection-示例)
- [缓存系统示例](#缓存系统示例)
- [事件系统示例](#事件系统示例)
- [日志系统示例](#日志系统示例)
- [安全系统示例](#安全系统示例)
- [调度系统示例](#调度系统示例)
- [Web 模块示例](#web-模块示例)

---

## 核心模块示例

### 依赖注入容器

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

public class ContainerExample {
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        
        // 注册单例服务
        container.registerSingleton(String.class, "Hello EST!");
        container.registerSingleton(Integer.class, 42);
        
        // 获取服务
        String message = container.get(String.class);
        Integer number = container.get(Integer.class);
        
        System.out.println("Message: " + message);
        System.out.println("Number: " + number);
    }
}
```

### 配置管理

```java
import ltd.idcu.est.core.api.Config;
import ltd.idcu.est.core.impl.DefaultConfig;

public class ConfigExample {
    public static void main(String[] args) {
        Config config = new DefaultConfig();
        
        // 设置配置
        config.set("app.name", "EST Example");
        config.set("app.version", "1.0.0");
        config.set("app.debug", true);
        config.set("app.port", 8080);
        
        // 获取配置
        String appName = config.getString("app.name");
        String appVersion = config.getString("app.version");
        boolean debug = config.getBoolean("app.debug");
        int port = config.getInt("app.port");
        
        System.out.println("App Name: " + appName);
        System.out.println("App Version: " + appVersion);
        System.out.println("Debug Mode: " + debug);
        System.out.println("Port: " + port);
    }
}
```

### 模块管理

```java
import ltd.idcu.est.core.api.Module;
import ltd.idcu.est.core.impl.AbstractModule;

public class ModuleExample {
    public static void main(String[] args) {
        Module module = new AbstractModule("example-module", "1.0.0") {
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
        
        // 初始化模块
        module.initialize();
        
        // 启动模块
        module.start();
        
        // 停止模块
        module.stop();
    }
}
```

---

## 工具模块示例

### 字符串工具

```java
import ltd.idcu.est.utils.common.StringUtils;

public class StringUtilsExample {
    public static void main(String[] args) {
        // 检查是否为空
        System.out.println(StringUtils.isEmpty(""));        // true
        System.out.println(StringUtils.isEmpty("hello"));    // false
        System.out.println(StringUtils.isBlank("  "));       // true
        
        // 字符串处理
        System.out.println(StringUtils.capitalize("hello")); // Hello
        System.out.println(StringUtils.reverse("hello"));    // olleh
        
        // 分隔与连接
        String[] parts = StringUtils.split("a,b,c", ",");
        System.out.println(StringUtils.join(parts, "-"));    // a-b-c
    }
}
```

### 文件工具

```java
import ltd.idcu.est.utils.io.FileUtils;
import java.io.File;

public class FileUtilsExample {
    public static void main(String[] args) throws Exception {
        // 写入文件
        FileUtils.writeString(new File("test.txt"), "Hello, EST!");
        
        // 读取文件
        String content = FileUtils.readString(new File("test.txt"));
        System.out.println(content);
        
        // 检查文件是否存在
        System.out.println(FileUtils.exists(new File("test.txt")));
    }
}
```

### JSON 工具

```java
import ltd.idcu.est.utils.format.json.JsonUtils;
import java.util.Map;

public class JsonUtilsExample {
    public static void main(String[] args) throws Exception {
        // 对象转 JSON
        Map<String, Object> data = Map.of(
            "name", "EST",
            "version", "1.0.0"
        );
        String json = JsonUtils.toJson(data);
        System.out.println(json);
        
        // JSON 转对象
        Map<String, Object> parsed = JsonUtils.fromJson(json, Map.class);
        System.out.println(parsed.get("name"));
    }
}
```

---

## Collection 示例

### 链式数据处理

```java
import ltd.idcu.est.collection.api.Collection;
import ltd.idcu.est.collection.impl.Collections;
import java.util.List;

public class CollectionExample {
    public static void main(String[] args) {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // 链式操作
        List<Integer> result = numbers
            .filter(n -> n % 2 == 0)      // 过滤偶数
            .map(n -> n * 2)               // 乘以 2
            .take(3)                        // 取前 3 个
            .toList();
        
        System.out.println(result); // [4, 8, 12]
        
        // 聚合操作
        int sum = numbers.sum();
        double avg = numbers.average();
        int max = numbers.max();
        int min = numbers.min();
        
        System.out.println("Sum: " + sum);
        System.out.println("Average: " + avg);
        System.out.println("Max: " + max);
        System.out.println("Min: " + min);
    }
}
```

### 格式转换

```java
import ltd.idcu.est.collection.api.Collection;
import ltd.idcu.est.collection.impl.Collections;

public class CollectionFormatExample {
    public static void main(String[] args) throws Exception {
        Collection<Map<String, Object>> users = Collections.of(
            Map.of("id", 1, "name", "Alice"),
            Map.of("id", 2, "name", "Bob")
        );
        
        // 转换为 JSON
        String json = users.toJson();
        System.out.println("JSON: " + json);
        
        // 转换为 YAML
        String yaml = users.toYaml();
        System.out.println("YAML: " + yaml);
        
        // 转换为 XML
        String xml = users.toXml("users");
        System.out.println("XML: " + xml);
    }
}
```

---

## 缓存系统示例

### 内存缓存

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.Caches;

public class MemoryCacheExample {
    public static void main(String[] args) {
        // 创建内存缓存
        Cache<String, String> cache = Caches.newMemoryCache();
        
        // 存储数据
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        
        // 获取数据
        System.out.println("key1: " + cache.get("key1"));
        System.out.println("key2: " + cache.get("key2"));
        
        // 检查键是否存在
        System.out.println("key1 exists: " + cache.containsKey("key1"));
        
        // 移除数据
        cache.remove("key1");
        System.out.println("key1 after remove: " + cache.get("key1"));
        
        // 清空缓存
        cache.clear();
        System.out.println("Cache size: " + cache.size());
    }
}
```

### LRU 缓存

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.Caches;

public class LruCacheExample {
    public static void main(String[] args) {
        // 创建 LRU 缓存，最大容量 3
        Cache<String, String> cache = Caches.newLruCache(3);
        
        cache.put("a", "1");
        cache.put("b", "2");
        cache.put("c", "3");
        
        // 访问 "a"，使其成为最近使用
        cache.get("a");
        
        // 添加新元素，"b" 会被淘汰（因为最近最少使用）
        cache.put("d", "4");
        
        System.out.println("a exists: " + cache.containsKey("a")); // true
        System.out.println("b exists: " + cache.containsKey("b")); // false
    }
}
```

---

## 事件系统示例

### 本地事件

```java
import ltd.idcu.est.features.event.api.Event;
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.api.EventListener;
import ltd.idcu.est.features.event.local.LocalEvents;

public class LocalEventExample {
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newEventBus();
        
        // 注册监听器
        eventBus.register(String.class, new EventListener<String>() {
            @Override
            public void onEvent(String event) {
                System.out.println("Received: " + event);
            }
        });
        
        // 发布事件
        eventBus.publish("Hello, Event!");
    }
}
```

### 异步事件

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.async.AsyncEvents;

public class AsyncEventExample {
    public static void main(String[] args) throws InterruptedException {
        EventBus eventBus = AsyncEvents.newEventBus();
        
        // 注册监听器
        eventBus.register(String.class, event -> {
            System.out.println("Processing: " + event + " on thread: " + Thread.currentThread().getName());
        });
        
        // 发布事件（异步处理）
        for (int i = 0; i < 5; i++) {
            eventBus.publish("Event " + i);
        }
        
        // 等待事件处理完成
        Thread.sleep(1000);
    }
}
```

---

## 日志系统示例

### 控制台日志

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.api.LogLevel;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class ConsoleLoggerExample {
    public static void main(String[] args) {
        Logger logger = ConsoleLogs.newLogger("MyApp");
        
        logger.log(LogLevel.INFO, "Application started");
        logger.log(LogLevel.DEBUG, "Debug information");
        logger.log(LogLevel.WARN, "Warning message");
        logger.log(LogLevel.ERROR, "Error occurred");
    }
}
```

### 文件日志

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.api.LogLevel;
import ltd.idcu.est.features.logging.file.FileLogs;
import java.io.File;

public class FileLoggerExample {
    public static void main(String[] args) {
        Logger logger = FileLogs.newLogger(new File("app.log"));
        
        logger.info("Application started");
        logger.debug("Processing request");
        logger.warn("Low memory");
        logger.error("Failed to save data");
    }
}
```

---

## 安全系统示例

### 基础认证

```java
import ltd.idcu.est.features.security.api.*;
import ltd.idcu.est.features.security.basic.BasicSecurity;

public class BasicAuthExample {
    public static void main(String[] args) {
        // 创建用户详情服务
        UserDetailsService userDetailsService = BasicSecurity.newInMemoryUserDetailsService();
        
        // 添加用户
        userDetailsService.createUser(BasicSecurity.newUser("admin", "admin123", "ADMIN"));
        userDetailsService.createUser(BasicSecurity.newUser("user", "user123", "USER"));
        
        // 认证
        AuthenticationProvider authProvider = BasicSecurity.newBasicAuthenticationProvider(userDetailsService);
        Authentication auth = authProvider.authenticate("admin", "admin123");
        
        System.out.println("Authenticated: " + auth.isAuthenticated());
        System.out.println("User: " + auth.getUser().getUsername());
    }
}
```

### JWT 认证

```java
import ltd.idcu.est.features.security.api.*;
import ltd.idcu.est.features.security.jwt.JwtSecurity;

public class JwtAuthExample {
    public static void main(String[] args) {
        // 创建 JWT Token 提供者
        TokenProvider tokenProvider = JwtSecurity.newTokenProvider("my-secret-key");
        
        // 生成 Token
        User user = JwtSecurity.newUser("john", "USER");
        Token token = tokenProvider.generateToken(user);
        
        System.out.println("Token: " + token.getValue());
        
        // 验证 Token
        Authentication auth = tokenProvider.validateToken(token.getValue());
        System.out.println("Valid: " + auth.isAuthenticated());
        System.out.println("User: " + auth.getUser().getUsername());
    }
}
```

---

## 调度系统示例

### 固定间隔调度

```java
import ltd.idcu.est.features.scheduler.api.Scheduler;
import ltd.idcu.est.features.scheduler.fixed.FixedRateSchedulers;

public class FixedRateSchedulerExample {
    public static void main(String[] args) throws InterruptedException {
        Scheduler scheduler = FixedRateSchedulers.newScheduler();
        
        // 每 1 秒执行一次
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Task executed at: " + System.currentTimeMillis());
        }, 0, 1000);
        
        // 运行 5 秒
        Thread.sleep(5000);
        
        scheduler.shutdown();
    }
}
```

### Cron 表达式调度

```java
import ltd.idcu.est.features.scheduler.api.Scheduler;
import ltd.idcu.est.features.scheduler.cron.CronSchedulers;

public class CronSchedulerExample {
    public static void main(String[] args) throws InterruptedException {
        Scheduler scheduler = CronSchedulers.newScheduler();
        
        // 每秒执行一次
        scheduler.schedule("* * * * * ?", () -> {
            System.out.println("Cron task executed");
        });
        
        Thread.sleep(5000);
        
        scheduler.shutdown();
    }
}
```

---

## Web 模块示例

### 基础 Web 应用

```java
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.impl.DefaultWebApplication;

public class BasicWebApp {
    public static void main(String[] args) {
        WebApplication app = new DefaultWebApplication();
        
        // 设置端口
        app.setPort(8080);
        
        // 添加路由
        app.get("/", (req, res) -> {
            res.send("Hello, EST Web!");
        });
        
        app.get("/api/users", (req, res) -> {
            res.json(Map.of(
                "users", List.of(
                    Map.of("id", 1, "name", "Alice"),
                    Map.of("id", 2, "name", "Bob")
                )
            ));
        });
        
        // 启动应用
        app.start();
        System.out.println("Server started on http://localhost:8080");
    }
}
```

### RESTful API

```java
import ltd.idcu.est.web.api.*;
import ltd.idcu.est.web.impl.DefaultWebApplication;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RestApiExample {
    private static final Map<Integer, Map<String, Object>> users = new ConcurrentHashMap<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(1);
    
    public static void main(String[] args) {
        WebApplication app = new DefaultWebApplication();
        app.setPort(8080);
        
        // GET /api/users - 获取所有用户
        app.get("/api/users", (req, res) -> {
            res.json(Map.of("users", users.values()));
        });
        
        // GET /api/users/:id - 获取单个用户
        app.get("/api/users/:id", (req, res) -> {
            int id = Integer.parseInt(req.param("id"));
            Map<String, Object> user = users.get(id);
            if (user != null) {
                res.json(user);
            } else {
                res.status(404).json(Map.of("error", "User not found"));
            }
        });
        
        // POST /api/users - 创建用户
        app.post("/api/users", (req, res) -> {
            Map<String, Object> body = req.bodyAsMap();
            int id = idGenerator.getAndIncrement();
            Map<String, Object> user = Map.of(
                "id", id,
                "name", body.get("name"),
                "email", body.get("email")
            );
            users.put(id, user);
            res.status(201).json(user);
        });
        
        // PUT /api/users/:id - 更新用户
        app.put("/api/users/:id", (req, res) -> {
            int id = Integer.parseInt(req.param("id"));
            Map<String, Object> existing = users.get(id);
            if (existing != null) {
                Map<String, Object> body = req.bodyAsMap();
                Map<String, Object> updated = Map.of(
                    "id", id,
                    "name", body.getOrDefault("name", existing.get("name")),
                    "email", body.getOrDefault("email", existing.get("email"))
                );
                users.put(id, updated);
                res.json(updated);
            } else {
                res.status(404).json(Map.of("error", "User not found"));
            }
        });
        
        // DELETE /api/users/:id - 删除用户
        app.delete("/api/users/:id", (req, res) -> {
            int id = Integer.parseInt(req.param("id"));
            users.remove(id);
            res.status(204).send("");
        });
        
        app.start();
        System.out.println("REST API server started on http://localhost:8080");
    }
}
```

### 中间件使用

```java
import ltd.idcu.est.web.api.*;
import ltd.idcu.est.web.impl.DefaultWebApplication;
import ltd.idcu.est.web.impl.LoggingMiddleware;

public class MiddlewareExample {
    public static void main(String[] args) {
        WebApplication app = new DefaultWebApplication();
        app.setPort(8080);
        
        // 添加日志中间件
        app.use(new LoggingMiddleware());
        
        // 添加自定义中间件
        app.use((req, res, next) -> {
            long startTime = System.currentTimeMillis();
            next.proceed();
            long duration = System.currentTimeMillis() - startTime;
            System.out.println("Request took " + duration + "ms");
        });
        
        // 添加路由
        app.get("/", (req, res) -> {
            res.send("Hello with middleware!");
        });
        
        app.start();
        System.out.println("Server started on http://localhost:8080");
    }
}
```

---

## 运行示例

### 使用 Maven 构建

```bash
# 构建整个项目
mvn clean install

# 运行基础示例
cd est-examples/est-examples-basic
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.basic.Main"

# 运行 Web 示例
cd ../est-examples-web
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.web.BasicWebAppExample"
```

---

## 更多示例

查看 `est-examples` 目录下的更多示例代码：

- `est-examples-basic/` - 基础功能示例
- `est-examples-advanced/` - 高级功能示例
- `est-examples-features/` - 各功能模块示例
- `est-examples-web/` - Web 应用示例
