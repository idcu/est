# 阶段四代码改进示例

## 1. Lambda 表达式路由处理器示例

```java
import ltd.idcu.est.web.api.*;
import ltd.idcu.est.web.impl.*;

public class WebAppExample {
    public static void main(String[] args) throws Exception {
        WebApplication app = DefaultWebApplication.create();
        
        Router router = app.getRouter();
        
        // 使用 Lambda 表达式作为路由处理器
        router.get("/", (req, res) -> {
            res.text("Hello, EST Web!");
        });
        
        // 使用路径参数的 Lambda 处理器
        router.get("/hello/:name", (req, res) -> {
            String name = req.getPathVariable("name");
            res.text("Hello, " + name + "!");
        });
        
        // JSON 响应示例
        router.get("/api/user", (req, res) -> {
            res.json(Map.of(
                "id", 1,
                "name", "John Doe",
                "email", "john@example.com"
            ));
        });
        
        // POST 请求处理
        router.post("/api/user", (req, res) -> {
            String body = req.getBody();
            res.status(HttpStatus.CREATED);
            res.json(Map.of("success", true, "data", body));
        });
        
        app.start(8080);
        System.out.println("Server started on http://localhost:8080");
    }
}
```

## 2. 会话管理示例

```java
router.get("/login", (req, res) -> {
    Session session = req.getSession(true);
    session.setAttribute("userId", 123);
    session.setAttribute("username", "john");
    res.text("Logged in successfully! Session ID: " + session.getId());
});

router.get("/profile", (req, res) -> {
    Session session = req.getSession(false);
    if (session != null && session.getAttribute("username") != null) {
        String username = (String) session.getAttribute("username");
        res.json(Map.of(
            "username", username,
            "userId", session.getAttribute("userId")
        ));
    } else {
        res.status(HttpStatus.UNAUTHORIZED);
        res.text("Please login first");
    }
});

router.get("/logout", (req, res) -> {
    Session session = req.getSession(false);
    if (session != null) {
        session.invalidate();
    }
    res.text("Logged out");
});
```

## 3. 静态文件服务配置

```java
DefaultStaticFileHandler staticHandler = new DefaultStaticFileHandler("/assets", "./public");
staticHandler.setCacheEnabled(true);
staticHandler.setCacheMaxAge(3600); // 1小时
staticHandler.setEtagEnabled(true);

app.setStaticFileHandler(staticHandler);
```

静态文件服务现在支持：
- **ETag 缓存验证**：基于文件路径、修改时间和大小生成 ETag
- **条件请求处理**：支持 If-None-Match 和 If-Modified-Since 头
- **304 响应**：当文件未修改时返回 304 Not Modified
- **分块传输**：使用 8KB 缓冲区优化大文件传输

## 4. 中间件优先级

中间件已按优先级自动排序：
- **DefaultCorsMiddleware**: 优先级 50（最早执行）
- **LoggingMiddleware**: 优先级 200
- **SecurityMiddleware**: 优先级 300

CORS 中间件会优先于其他中间件执行，确保 CORS 头部正确设置。

## 5. JarPluginLoader 改进

JarPluginLoader 现在使用更简单、更可靠的方式检测插件类：
- 优先查找类名包含 "Plugin" 的类
- 使用 URLClassLoader 直接加载类并验证
- 不再依赖复杂的字节码解析

这大大提高了插件检测的可靠性和稳定性。
