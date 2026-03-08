# EST Web Group Web 模块组 - 小白从入门到精通

## 目录
1. [什么是 EST Web Group？](#什么是-est-web-group)
2. [快速入门：5分钟上手](#快速入门5分钟上手)
3. [基础篇](#基础篇)
4. [进阶篇](#进阶篇)
5. [最佳实践](#最佳实践)

---

## 什么是 EST Web Group？

### 用大白话理解

EST Web Group 就像是一个"Web 工具包"。想象一下你要建一个网站，需要各种工具：

**传统方式**：你需要自己搭建路由、处理中间件、管理会话、渲染模板... 很麻烦！

**EST Web Group 方式**：给你一套完整的 Web 开发工具包，里面有：
- 🛣️ **路由系统** - 定义 URL 路由，处理请求
- 🔧 **中间件管道** - 在请求前后做处理（日志、认证、CORS 等）
- 🍪 **会话管理** - 管理用户会话，记住登录状态
- 🎨 **模板引擎** - 渲染动态网页
- 🚪 **API 网关** - 统一管理 API 入口，路由转发

### 核心特点

- 🎯 **简单易用** - 几行代码就能启动 Web 服务
- ⚡ **高性能** - 基于高性能的底层实现
- 🔧 **灵活扩展** - 可以自定义中间件、模板等
- 🎨 **功能完整** - 路由、中间件、会话、模板一应俱全

---

## 快速入门：5分钟上手

### 第一步：引入依赖

在你的 Maven pom.xml 中添加：

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web-router</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web-middleware</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>
```

### 第二步：你的第一个 Web 应用

```java
import ltd.idcu.est.web.router.WebRouter;
import ltd.idcu.est.web.middleware.Middleware;
import ltd.idcu.est.web.middleware.MiddlewareChain;

public class FirstWebApp {
    public static void main(String[] args) {
        System.out.println("=== EST Web Group 第一个示例 ===\n");
        
        WebRouter router = WebRouter.create();
        
        router.get("/", (req, res) -> {
            res.send("Hello, EST Web!");
        });
        
        router.get("/hello/{name}", (req, res) -> {
            String name = req.pathParam("name");
            res.send("Hello, " + name + "!");
        });
        
        router.get("/api/user", (req, res) -> {
            res.json(Map.of(
                "name", "张三",
                "age", 25,
                "email", "zhangsan@example.com"
            ));
        });
        
        router.listen(8080);
        System.out.println("Web 服务已启动: http://localhost:8080");
    }
}
```

运行这个程序，然后访问：
- http://localhost:8080 - 首页
- http://localhost:8080/hello/张三 - 个性化问候
- http://localhost:8080/api/user - JSON API

---

## 基础篇

### 1. est-web-router 路由系统

#### 定义路由

```java
import ltd.idcu.est.web.router.WebRouter;

WebRouter router = WebRouter.create();

// GET 请求
router.get("/users", (req, res) -> {
    List<User> users = userService.findAll();
    res.json(users);
});

// POST 请求
router.post("/users", (req, res) -> {
    User user = req.bodyAs(User.class);
    User saved = userService.save(user);
    res.status(201).json(saved);
});

// PUT 请求
router.put("/users/{id}", (req, res) -> {
    Long id = Long.parseLong(req.pathParam("id"));
    User user = req.bodyAs(User.class);
    User updated = userService.update(id, user);
    res.json(updated);
});

// DELETE 请求
router.delete("/users/{id}", (req, res) -> {
    Long id = Long.parseLong(req.pathParam("id"));
    userService.delete(id);
    res.status(204).send();
});
```

#### 路径参数和查询参数

```java
router.get("/users/{id}/posts/{postId}", (req, res) -> {
    // 路径参数
    String userId = req.pathParam("id");
    String postId = req.pathParam("postId");
    
    // 查询参数
    String page = req.queryParam("page", "1");
    int limit = req.queryParamAsInt("limit", 10);
    
    res.send("用户 " + userId + " 的文章 " + postId + ", 第 " + page + " 页");
});
```

#### 请求和响应

```java
router.get("/example", (req, res) -> {
    // 获取请求头
    String userAgent = req.header("User-Agent");
    
    // 获取请求体
    User user = req.bodyAs(User.class);
    
    // 发送文本响应
    res.send("Hello!");
    
    // 发送 JSON 响应
    res.json(user);
    
    // 设置状态码
    res.status(404).send("Not Found");
    
    // 设置响应头
    res.header("Content-Type", "application/json");
    
    // 设置 Cookie
    res.cookie("sessionId", "abc123");
});
```

### 2. est-web-middleware 中间件

#### 什么是中间件？

中间件就像是"过滤器"，可以在请求到达处理器之前和响应返回之后做一些处理。

```java
import ltd.idcu.est.web.middleware.Middleware;
import ltd.idcu.est.web.middleware.MiddlewareChain;

// 日志中间件
Middleware loggingMiddleware = (req, res, chain) -> {
    System.out.println(req.method() + " " + req.path());
    long start = System.currentTimeMillis();
    
    chain.proceed();
    
    long duration = System.currentTimeMillis() - start;
    System.out.println("耗时: " + duration + "ms");
};

// 使用中间件
router.use(loggingMiddleware);
```

#### 常用中间件

```java
import ltd.idcu.est.web.middleware.Middlewares;

// CORS 中间件
router.use(Middlewares.cors());

// 安全中间件（XSS、CSRF 防护）
router.use(Middlewares.security());

// 压缩中间件
router.use(Middlewares.gzip());

// 限流中间件
router.use(Middlewares.rateLimit(100, 60)); // 每分钟 100 次请求
```

### 3. est-web-session 会话管理

#### 启用会话

```java
import ltd.idcu.est.web.session.SessionManager;
import ltd.idcu.est.web.session.MemorySessionStore;

SessionManager sessionManager = new SessionManager(new MemorySessionStore());
router.use(sessionManager.middleware());

router.get("/login", (req, res) -> {
    req.session().set("userId", 123L);
    req.session().set("username", "张三");
    res.send("登录成功");
});

router.get("/profile", (req, res) -> {
    Long userId = req.session().get("userId");
    String username = req.session().get("username");
    res.json(Map.of("userId", userId, "username", username));
});

router.get("/logout", (req, res) -> {
    req.session().invalidate();
    res.send("登出成功");
});
```

### 4. est-web-template 模板引擎

#### 渲染模板

```java
import ltd.idcu.est.web.template.TemplateEngine;
import ltd.idcu.est.web.template.ThymeleafEngine;

TemplateEngine templateEngine = new ThymeleafEngine("templates/");
router.setTemplateEngine(templateEngine);

router.get("/", (req, res) -> {
    res.render("index.html", Map.of(
        "title", "首页",
        "message", "欢迎来到 EST Web!"
    ));
});
```

### 5. est-gateway API 网关

#### 配置网关

```java
import ltd.idcu.est.web.gateway.ApiGateway;
import ltd.idcu.est.web.gateway.Route;

ApiGateway gateway = ApiGateway.create();

// 配置路由
gateway.route("/api/users/**")
    .to("http://user-service:8081")
    .stripPrefix(1); // 去掉 /api/users 前缀

gateway.route("/api/orders/**")
    .to("http://order-service:8082")
    .stripPrefix(1);

// 添加网关中间件
gateway.use(loggingMiddleware);
gateway.use(authMiddleware);

// 启动网关
gateway.listen(9000);
```

---

## 进阶篇

### 1. 路由分组

```java
router.group("/api", () -> {
    router.group("/users", () -> {
        router.get("", listUsers);
        router.get("/{id}", getUser);
        router.post("", createUser);
        router.put("/{id}", updateUser);
        router.delete("/{id}", deleteUser);
    });
    
    router.group("/orders", () -> {
        router.get("", listOrders);
        router.get("/{id}", getOrder);
        router.post("", createOrder);
    });
});
```

### 2. 自定义会话存储

```java
import ltd.idcu.est.web.session.SessionStore;
import ltd.idcu.est.web.session.Session;

public class RedisSessionStore implements SessionStore {
    private final RedisClient redis;
    
    public RedisSessionStore(RedisClient redis) {
        this.redis = redis;
    }
    
    @Override
    public Session get(String sessionId) {
        return redis.get("session:" + sessionId);
    }
    
    @Override
    public void save(Session session) {
        redis.setex("session:" + session.getId(), 3600, session);
    }
    
    @Override
    public void delete(String sessionId) {
        redis.del("session:" + sessionId);
    }
}

SessionManager sessionManager = new SessionManager(new RedisSessionStore(redis));
```

### 3. 网关负载均衡

```java
import ltd.idcu.est.web.gateway.LoadBalancer;
import ltd.idcu.est.web.gateway.RoundRobinLoadBalancer;

LoadBalancer loadBalancer = new RoundRobinLoadBalancer(
    "http://user-service-1:8081",
    "http://user-service-2:8081",
    "http://user-service-3:8081"
);

gateway.route("/api/users/**")
    .to(loadBalancer)
    .stripPrefix(1);
```

---

## 最佳实践

### 1. 合理组织路由

```java
// ✅ 推荐：按功能分组
router.group("/api", () -> {
    router.group("/users", () -> {
        // 用户相关路由
    });
    router.group("/orders", () -> {
        // 订单相关路由
    });
});

// ❌ 不推荐：零散的路由
router.get("/api/users", listUsers);
router.get("/api/users/:id", getUser);
// ...
```

### 2. 使用中间件处理横切关注点

```java
// ✅ 推荐：使用中间件
router.use(loggingMiddleware);
router.use(authMiddleware);
router.use(corsMiddleware);

// ❌ 不推荐：每个路由都重复写
router.get("/api/users", (req, res) -> {
    // 重复的日志代码
    // 重复的认证代码
    // ...
});
```

### 3. 合理使用会话

```java
// ✅ 推荐：只在会话中存储必要信息
req.session().set("userId", user.getId());
req.session().set("username", user.getName());

// ❌ 不推荐：在会话中存储大量数据
req.session().set("user", user);  // 整个用户对象
req.session().set("permissions", permissions);  // 大量权限数据
```

### 4. 网关安全

```java
// ✅ 推荐：在网关层统一处理认证和限流
gateway.use(authMiddleware);
gateway.use(rateLimitMiddleware);
gateway.use(securityMiddleware);

// 后端服务只关注业务逻辑
```

---

## 模块结构

```
est-web-group/
├── est-web-router/      # 路由系统
├── est-web-middleware/  # 中间件管道
├── est-web-session/     # 会话管理
├── est-web-template/    # 模板引擎
└── est-gateway/         # API 网关
```

---

## 相关资源

- [est-gateway README](./est-gateway/README.md) - API 网关详细文档
- [示例代码](../../est-examples/est-examples-web/) - Web 示例代码
- [EST App](../../est-app/README.md) - 应用模块
- [EST Core](../../est-core/README.md) - 核心模块

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
