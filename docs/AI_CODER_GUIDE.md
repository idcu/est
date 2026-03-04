# AI Coder 指南

> EST Framework - 让 AI 编码变得简单高效

## 简介

EST 是专为 AI coder 设计的零依赖 Java 框架，具有以下特点，让 AI 可以轻松生成高质量代码：

- **零依赖**：无需管理复杂的依赖树
- **清晰的 API**：简单、一致的接口设计
- **标准化模式**：可预测的代码结构
- **丰富的示例**：AI 可以学习的完整参考
- **脚手架工具**：一键生成项目模板

## 快速开始

### 1. 使用脚手架生成项目

EST 提供三种标准项目模板，AI 可以根据需求选择：

#### 基础项目 (Basic)
```bash
# 在 est-scaffold 目录下编译后运行
java -jar est-scaffold.jar new my-project
```

#### Web 应用项目
```bash
java -jar est-scaffold.jar web my-web-app
```

#### REST API 项目
```bash
java -jar est-scaffold.jar api my-api-service
```

### 2. 标准项目结构

所有 EST 项目都遵循统一的目录结构，AI 可以轻松理解和导航：

```
my-project/
├── src/
│   ├── main/
│   │   ├── java/com/example/myproject/
│   │   │   ├── Main.java              # 入口文件
│   │   │   ├── controller/            # 控制器（Web/API）
│   │   │   ├── service/               # 业务逻辑
│   │   │   └── model/                 # 数据模型
│   │   └── resources/                  # 配置文件
│   └── test/
│       ├── java/com/example/myproject/
│       └── resources/
├── pom.xml                              # Maven 配置
└── README.md
```

## AI 代码生成模式

### 模式 1: 依赖注入容器

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

public class App {
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        
        // 注册服务
        container.register(MyService.class, MyServiceImpl.class);
        container.registerSingleton(Config.class, new AppConfig());
        
        // 获取服务
        MyService service = container.get(MyService.class);
        service.doWork();
    }
}
```

### 模式 2: Web 应用

```java
import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

public class WebApp {
    public static void main(String[] args) {
        WebApplication app = new DefaultWebApplication();
        
        app.routes(router -> {
            router.get("/", (req, res) -> res.html("<h1>Hello</h1>"));
            router.get("/api/data", (req, res) -> res.json(Map.of("key", "value")));
        });
        
        app.run(8080);
    }
}
```

### 模式 3: REST API 控制器

```java
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserController {
    private final Map<String, User> users = new ConcurrentHashMap<>();
    
    public void registerRoutes(WebApplication app) {
        app.routes(router -> {
            router.get("/api/users", this::list);
            router.get("/api/users/:id", this::get);
            router.post("/api/users", this::create);
            router.put("/api/users/:id", this::update);
            router.delete("/api/users/:id", this::delete);
        });
    }
    
    public void list(Request req, Response res) {
        res.json(users.values());
    }
    
    public void get(Request req, Response res) {
        String id = req.param("id");
        User user = users.get(id);
        if (user != null) {
            res.json(user);
        } else {
            res.status(404).json(Map.of("error", "Not found"));
        }
    }
    
    public void create(Request req, Response res) {
        // 实现创建逻辑
        res.status(201).json(newUser);
    }
    
    public void update(Request req, Response res) {
        // 实现更新逻辑
    }
    
    public void delete(Request req, Response res) {
        // 实现删除逻辑
    }
}
```

### 模式 4: 缓存使用

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.Caches;

public class CacheExample {
    public static void main(String[] args) {
        Cache<String, Object> cache = Caches.newMemoryCache();
        
        cache.put("key", "value");
        Object value = cache.get("key");
        cache.remove("key");
    }
}
```

### 模式 5: 事件驱动

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.EventBuses;

public class EventExample {
    public static void main(String[] args) {
        EventBus bus = EventBuses.newLocalEventBus();
        
        // 订阅事件
        bus.subscribe(UserCreatedEvent.class, event -> {
            System.out.println("User created: " + event.getUserId());
        });
        
        // 发布事件
        bus.publish(new UserCreatedEvent("123"));
    }
}
```

## AI 编码最佳实践

### 1. 遵循命名约定

- 类名：大驼峰命名 (UserService, OrderController)
- 方法名：小驼峰命名 (getUser, createOrder)
- 常量：全大写下划线分隔 (MAX_RETRIES, DEFAULT_TIMEOUT)
- 包名：全小写 (com.example.service, com.example.controller)

### 2. 使用注解进行组件扫描

```java
import ltd.idcu.est.core.api.annotation.Component;
import ltd.idcu.est.core.api.annotation.Inject;

@Component
public class UserService {
    
    @Inject
    private UserRepository repository;
    
    public User getUser(String id) {
        return repository.findById(id);
    }
}
```

### 3. 错误处理模式

```java
public void handleRequest(Request req, Response res) {
    try {
        // 业务逻辑
        res.json(result);
    } catch (IllegalArgumentException e) {
        res.status(400).json(Map.of("error", e.getMessage()));
    } catch (Exception e) {
        res.status(500).json(Map.of("error", "Internal server error"));
    }
}
```

### 4. 配置管理

```java
import ltd.idcu.est.core.api.Config;
import ltd.idcu.est.core.impl.DefaultConfig;

public class AppConfig {
    public static void main(String[] args) {
        Config config = new DefaultConfig();
        
        config.set("app.name", "My App");
        config.set("app.port", 8080);
        
        String name = config.getString("app.name");
        int port = config.getInt("app.port", 8080);
    }
}
```

## 常用模块引用

### Web 开发
```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-web-api</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-web-impl</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
```

### 缓存
```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-features-cache-api</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-features-cache-memory</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
```

### 日志
```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-features-logging-api</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-features-logging-console</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
```

## 完整示例参考

查看 `est-examples` 目录获取完整的可运行示例：
- `est-examples-basic` - 基础功能示例
- `est-examples-web` - Web 应用示例
- `est-examples-features` - 各功能模块示例
- `est-examples-advanced` - 高级用法示例

## 快速问题解决

### Q: 如何启动 Web 服务器？
A: 使用 `app.run(8080)`，默认端口 8080

### Q: 如何返回 JSON 响应？
A: 使用 `response.json(object)`，对象会自动序列化为 JSON

### Q: 如何获取路径参数？
A: 使用 `request.param("name")`，路由定义为 `/hello/:name`

### Q: 如何添加中间件？
A: 使用 `app.use(middleware)`，中间件会按顺序执行

## 下一步

1. 查看 [入门指南](./guides/getting-started.md)
2. 浏览 [API 参考](./api/)
3. 研究 [示例代码](../est-examples/)
4. 阅读 [最佳实践](./best-practices/)
