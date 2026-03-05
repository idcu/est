# EST 框架快速参考卡片

> 为开发者准备的一分钟速查表，快速查找常用 API 和代码片段

---

## 📋 目录

- [核心概念](#核心概念)
- [Web 开发](#web-开发)
- [依赖注入](#依赖注入)
- [配置管理](#配置管理)
- [常用代码片段](#常用代码片段)
- [Maven 依赖](#maven-依赖)
- [导入语句](#导入语句)
- [常见问题](#常见问题)

---

## 🎯 核心概念

| 概念 | 说明 | 常用类/接口 |
|------|------|------------|
| Web应用 | HTTP服务 | `WebApplication`, `Web` |
| 路由 | 请求映射 | `Router` |
| 请求 | HTTP请求 | `Request` |
| 响应 | HTTP响应 | `Response` |
| 中间件 | 请求处理链 | `Middleware` |
| 容器 | 依赖注入 | `Container`, `DefaultContainer` |
| 配置 | 配置管理 | `Config` |

---

## 🌐 Web 开发

### 创建 Web 应用

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

// 创建默认应用
WebApplication app = Web.create();

// 创建带名称和版本的应用
WebApplication app = Web.create("我的应用", "1.0.0");
```

### 添加路由

```java
// GET 请求
app.get("/path", (req, res) -> { /* 处理逻辑 */ });

// POST 请求
app.post("/path", (req, res) -> { /* 处理逻辑 */ });

// PUT 请求
app.put("/path", (req, res) -> { /* 处理逻辑 */ });

// DELETE 请求
app.delete("/path", (req, res) -> { /* 处理逻辑 */ });

// PATCH 请求
app.patch("/path", (req, res) -> { /* 处理逻辑 */ });
```

### 请求（Request）

```java
// 获取路径参数（如 /user/:id）
String id = req.param("id");

// 获取查询参数（如 ?name=xxx）
String name = req.queryParam("name");
String name = req.queryParam("name", "默认值");

// 获取表单参数
String username = req.formParam("username");

// 获取请求头
String userAgent = req.header("User-Agent");

// 获取请求方法
String method = req.getMethod();

// 获取请求路径
String path = req.getPath();
```

### 响应（Response）

```java
// 发送纯文本
res.send("Hello, World!");

// 发送 HTML
res.html("<h1>标题</h1>");

// 发送 JSON
res.json(Map.of("key", "value"));

// 发送错误
res.sendError(404, "页面未找到");
res.sendError(500, "服务器错误");

// 设置状态码
res.status(201);

// 设置响应头
res.header("Content-Type", "application/json");
```

### 路由分组

```java
app.routes(router -> {
    // API 分组
    router.group("/api", (r, group) -> {
        r.get("/users", (req, res) -> res.json(users));
        r.post("/users", (req, res) -> res.json(createUser(req)));
    });
    
    // 管理后台分组
    router.group("/admin", (r, group) -> {
        r.middleware("auth");
        r.get("/dashboard", (req, res) -> res.render("admin/dashboard"));
    });
});
```

### 启动服务器

```java
// 使用默认端口 8080
app.run();

// 指定端口
app.run(8080);

// 指定主机和端口
app.run("0.0.0.0", 8080);

// 启动回调
app.onStartup(() -> {
    System.out.println("应用启动成功！");
});

// 关闭回调
app.onShutdown(() -> {
    System.out.println("应用正在关闭...");
});
```

---

## 📦 依赖注入

### 创建容器

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

Container container = new DefaultContainer();
```

### 注册服务

```java
// 注册接口和实现
container.register(MyService.class, MyServiceImpl.class);

// 注册单例
container.registerSingleton(Config.class, new MyConfig());

// 直接注册实例
container.registerInstance(new MyService());
```

### 获取服务

```java
// 获取服务
MyService service = container.get(MyService.class);

// 按名称获取
MyService service = container.get("myService", MyService.class);
```

---

## ⚙️ 配置管理

### 获取配置对象

```java
// 从 Web 应用获取
Config config = app.getConfig();

// 或者单独创建
Config config = new DefaultConfig();
```

### 设置配置

```java
config.set("app.name", "我的应用");
config.set("server.port", 8080);
config.set("debug", true);
```

### 获取配置

```java
// 获取字符串
String appName = config.getString("app.name");
String appName = config.getString("app.name", "默认值");

// 获取整数
int port = config.getInt("server.port");
int port = config.getInt("server.port", 8080);

// 获取布尔值
boolean debug = config.getBoolean("debug");
boolean debug = config.getBoolean("debug", false);

// 获取长整型
long timeout = config.getLong("timeout");
long timeout = config.getLong("timeout", 30000L);

// 获取双精度浮点数
double ratio = config.getDouble("ratio");
double ratio = config.getDouble("ratio", 0.5);
```

---

## 💡 常用代码片段

### 完整 Web 应用示例

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        WebApplication app = Web.create("我的应用", "1.0.0");
        
        // 首页
        app.get("/", (req, res) -> {
            res.html("<h1>欢迎使用 EST 框架！</h1>");
        });
        
        // API 接口
        app.get("/api/hello", (req, res) -> {
            String name = req.queryParam("name", "访客");
            res.json(Map.of(
                "message", "你好，" + name + "！",
                "timestamp", System.currentTimeMillis()
            ));
        });
        
        // 带路径参数
        app.get("/user/:id", (req, res) -> {
            String id = req.param("id");
            res.json(Map.of(
                "id", id,
                "name", "用户" + id
            ));
        });
        
        app.onStartup(() -> {
            System.out.println("🚀 服务器启动成功！");
            System.out.println("📍 访问地址：http://localhost:8080");
        });
        
        app.run(8080);
    }
}
```

### REST API CRUD 示例

```java
// 模拟数据存储
Map<String, User> users = new ConcurrentHashMap<>();

// 获取所有用户
app.get("/api/users", (req, res) -> {
    res.json(users.values());
});

// 获取单个用户
app.get("/api/users/:id", (req, res) -> {
    String id = req.param("id");
    User user = users.get(id);
    if (user != null) {
        res.json(user);
    } else {
        res.sendError(404, "用户不存在");
    }
});

// 创建用户
app.post("/api/users", (req, res) -> {
    String name = req.formParam("name");
    String email = req.formParam("email");
    User user = new User(UUID.randomUUID().toString(), name, email);
    users.put(user.getId(), user);
    res.status(201).json(user);
});

// 更新用户
app.put("/api/users/:id", (req, res) -> {
    String id = req.param("id");
    User user = users.get(id);
    if (user != null) {
        user.setName(req.formParam("name"));
        user.setEmail(req.formParam("email"));
        res.json(user);
    } else {
        res.sendError(404, "用户不存在");
    }
});

// 删除用户
app.delete("/api/users/:id", (req, res) -> {
    String id = req.param("id");
    users.remove(id);
    res.status(204).send("");
});
```

---

## 📦 Maven 依赖

### 最小 Web 应用（推荐）

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web-impl</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### 仅核心模块

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-core-impl</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### 完整功能（Web + 缓存 + 日志）

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web-impl</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-features-cache-memory</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-features-logging-console</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

---

## 📥 导入语句

### Web 模块

```java
// Web 应用
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

// 请求和响应
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

// 路由
import ltd.idcu.est.web.api.Router;
```

### 核心模块

```java
// 容器
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

// 配置
import ltd.idcu.est.core.api.Config;
import ltd.idcu.est.core.impl.DefaultConfig;
```

---

## 🔧 常见问题

### Q: 如何获取请求体？

**A:**
```java
String body = req.body();
```

### Q: 如何设置响应状态码？

**A:**
```java
res.status(201); // 创建成功
res.status(404); // 未找到
res.status(500); // 服务器错误
```

### Q: 如何获取查询参数？

**A:**
```java
String value = req.queryParam("paramName");
String value = req.queryParam("paramName", "默认值");
```

### Q: 如何添加响应头？

**A:**
```java
res.header("Content-Type", "application/json");
res.header("Cache-Control", "no-cache");
```

### Q: 如何启用 CORS？

**A:**
```java
// 启用默认 CORS
app.enableCors();

// 自定义 CORS 配置
app.enableCors(Map.of(
    "origins", List.of("*"),
    "methods", List.of("GET", "POST", "PUT", "DELETE"),
    "headers", List.of("*"),
    "credentials", true
));
```

### Q: 如何添加静态文件服务？

**A:**
```java
// 将 /static 路径映射到 src/main/resources/static 目录
app.staticFiles("/static", "src/main/resources/static");
```

### Q: 如何处理异常？

**A:**
```java
// 全局异常处理
app.errorHandler(e -> {
    System.err.println("发生错误：" + e.getMessage());
});

// 特定异常处理
app.exceptionHandler(NotFoundException.class, e -> {
    // 处理 404 错误
});
```

---

## 📚 更多资源

- [完整文档](./README.md) - 文档索引
- [AI Coder 指南](./AI_CODER_GUIDE.md) - AI 编程指南
- [示例代码](../est-examples/) - 可运行的示例
- [API 文档](./api/) - 详细 API 参考
- [教程](./tutorials/) - 从入门到精通

---

**祝你编码愉快！** 🎉
