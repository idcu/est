# EST Web 框架使用示例

## 基础使用

### 1. 简单的 Web 应用

```java
import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

public class SimpleWebApp {
    public static void main(String[] args) {
        DefaultWebApplication app = DefaultWebApplication.create("My App", "1.0.0");
        
        // GET 路由
        app.get("/", (req, res) -> {
            res.text("Hello, EST!");
        });
        
        // JSON 响应
        app.get("/api/user", (req, res) -> {
            res.json(Map.of(
                "id", 1,
                "name", "John Doe",
                "email", "john@example.com"
            ));
        });
        
        // POST 路由
        app.post("/api/user", (req, res) -> {
            String body = req.getBody();
            res.json(Map.of(
                "success", true,
                "received", body
            ));
        });
        
        // 路径参数
        app.get("/api/user/{id}", (req, res) -> {
            String userId = ((DefaultRequest) req).getPathVariable("id");
            res.json(Map.of(
                "userId", userId,
                "action", "get"
            ));
        });
        
        // 启动服务器
        app.run(8080);
    }
}
```

### 2. 会话管理

```java
app.get("/login", (req, res) -> {
    // 获取或创建会话
    Session session = req.getSession(true);
    session.setAttribute("userId", 123);
    session.setAttribute("username", "john");
    
    res.json(Map.of("message", "Logged in successfully"));
});

app.get("/profile", (req, res) -> {
    Session session = req.getSession(false);
    if (session == null || !session.isValid()) {
        res.sendError(401, "Not logged in");
        return;
    }
    
    Integer userId = (Integer) session.getAttribute("userId");
    String username = (String) session.getAttribute("username");
    
    res.json(Map.of(
        "userId", userId,
        "username", username
    ));
});

app.get("/logout", (req, res) -> {
    Session session = req.getSession(false);
    if (session != null) {
        session.invalidate();
    }
    res.json(Map.of("message", "Logged out successfully"));
});
```

### 3. 会话自动清理

会话管理已自动配置了定期清理功能：

- 默认清理间隔：1 分钟
- 使用守护线程运行
- 服务器启动时自动启动
- 服务器停止时自动停止

也可以手动控制：

```java
import ltd.idcu.est.web.DefaultSessionManager;
import java.util.concurrent.TimeUnit;

DefaultSessionManager sessionManager = new DefaultSessionManager();

// 使用默认间隔（1分钟）启动
sessionManager.startCleanupTask();

// 或指定自定义间隔
sessionManager.startCleanupTask(5, TimeUnit.MINUTES);

// 检查清理任务状态
if (sessionManager.isCleanupStarted()) {
    System.out.println("Cleanup task is running");
}

// 停止清理任务
sessionManager.stopCleanupTask();
```

### 4. 中间件使用

```java
import ltd.idcu.est.web.LoggingMiddleware;

// 添加日志中间件
app.use(new LoggingMiddleware());

// 路径特定的中间件
app.use("/api", new SecurityMiddleware());

// 自定义 before 过滤器
app.before("/api/*", (req) -> {
    System.out.println("Request to: " + req.getPath());
});

// 自定义 after 过滤器
app.after("/api/*", (res) -> {
    res.setHeader("X-Powered-By", "EST Framework");
});
```

### 5. CORS 配置

```java
// 启用默认 CORS
app.enableCors();

// 或自定义 CORS
app.enableCors(Map.of(
    "origins", List.of("http://localhost:3000", "https://example.com"),
    "methods", List.of("GET", "POST", "PUT", "DELETE"),
    "headers", List.of("Content-Type", "Authorization"),
    "credentials", true,
    "maxAge", 3600L
));
```

### 6. 静态文件服务

```java
// 服务静态文件
app.staticFiles("/static", "src/main/resources/static");

// 仅服务特定扩展名
app.staticFiles("/images", "src/main/resources/images", "jpg", "png", "gif");
```

### 7. 错误处理

```java
// 特定异常处理
app.exceptionHandler(IllegalArgumentException.class, (e) -> {
    System.err.println("Validation error: " + e.getMessage());
});

// 全局错误处理
app.errorHandler((e) -> {
    System.err.println("Unexpected error: " + e.getMessage());
    e.printStackTrace();
});
```

### 8. 生命周期钩子

```java
app.onStartup(() -> {
    System.out.println("Application starting...");
    // 初始化数据库连接等
});

app.onShutdown(() -> {
    System.out.println("Application shutting down...");
    // 清理资源
});
```

## 完整示例

```java
import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.DefaultRequest;
import ltd.idcu.est.web.api.Session;
import java.util.Map;

public class CompleteExample {
    public static void main(String[] args) {
        DefaultWebApplication app = DefaultWebApplication.create();
        
        // 启用 CORS
        app.enableCors();
        
        // 首页
        app.get("/", (req, res) -> {
            res.html("""
                <html>
                    <body>
                        <h1>Welcome to EST Web Framework</h1>
                        <ul>
                            <li><a href="/api/hello">Hello API</a></li>
                            <li><a href="/api/user/123">User API</a></li>
                        </ul>
                    </body>
                </html>
            """);
        });
        
        // Hello API
        app.get("/api/hello", (req, res) -> {
            String name = req.getParameterOrDefault("name", "World");
            res.json(Map.of(
                "message", "Hello, " + name + "!",
                "timestamp", System.currentTimeMillis()
            ));
        });
        
        // 用户 API
        app.get("/api/user/{id}", (req, res) -> {
            String userId = ((DefaultRequest) req).getPathVariable("id");
            res.json(Map.of(
                "id", userId,
                "name", "User " + userId,
                "email", "user" + userId + "@example.com"
            ));
        });
        
        // 会话演示
        app.get("/api/session/set", (req, res) -> {
            Session session = req.getSession(true);
            session.setAttribute("visited", true);
            session.setAttribute("visitCount", 
                (Integer) session.getAttributeOrDefault("visitCount", 0) + 1);
            res.json(Map.of(
                "sessionId", session.getId(),
                "visitCount", session.getAttribute("visitCount")
            ));
        });
        
        app.get("/api/session/get", (req, res) -> {
            Session session = req.getSession(false);
            if (session == null) {
                res.json(Map.of("message", "No session found"));
                return;
            }
            res.json(Map.of(
                "sessionId", session.getId(),
                "isValid", session.isValid(),
                "attributes", session.getAttributeNames()
            ));
        });
        
        // 启动服务器
        System.out.println("Starting server on http://localhost:8080");
        app.run(8080);
    }
}
```

## 新增功能说明

### 1. 路由处理器（RouteHandler）

新增的 `RouteHandler` 函数式接口允许直接使用 Lambda 表达式定义路由处理逻辑：

```java
@FunctionalInterface
public interface RouteHandler {
    void handle(Request request, Response response) throws Exception;
}
```

### 2. 自动会话清理

`DefaultSessionManager` 现在包含：
- `startCleanupTask()`: 启动定期清理（默认 1 分钟间隔）
- `startCleanupTask(long interval, TimeUnit unit)`: 自定义间隔
- `stopCleanupTask()`: 停止清理任务
- `isCleanupStarted()`: 检查状态

### 3. 流畅的 API

`DefaultWebApplication` 新增了链式调用方法：
- `get(path, handler)`
- `post(path, handler)`
- `put(path, handler)`
- `delete(path, handler)`
- `patch(path, handler)`

这些方法返回 `this`，支持链式调用。
