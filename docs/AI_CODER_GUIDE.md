# AI Coder 指南

> EST Framework - 让 AI 编码变得简单高效

---

## 📋 目录

- [简介](#简介)
- [核心优势](#核心优势)
- [快速开始](#快速开始)
- [AI 代码生成模式](#ai-代码生成模式)
- [最佳实践](#最佳实践)
- [常用参考](#常用参考)
- [常见问题](#常见问题)

---

## 🎯 简介

EST 是专为 AI coder 设计的零依赖 Java 框架，具有以下特点，让 AI 可以轻松生成高质量代码：

- **零依赖**：无需管理复杂的依赖树，所有功能都用 Java 标准库实现
- **清晰的 API**：简单、一致的接口设计，易于理解和使用
- **标准化模式**：可预测的代码结构，AI 可以快速学习
- **丰富的示例**：完整的可运行示例代码，AI 可以参考
- **模块化设计**：按需引入模块，代码简洁高效

---

## ✨ 核心优势

### 为什么 EST 特别适合 AI 编程？

| 特性 | 说明 | AI 友好度 |
|------|------|-----------|
| 零依赖 | 不需要引入第三方库，避免版本冲突 | ⭐⭐⭐⭐⭐ |
| 简洁 API | 方法名直观，参数简单，易于理解 | ⭐⭐⭐⭐⭐ |
| 标准结构 | 所有项目都遵循相同的结构模式 | ⭐⭐⭐⭐⭐ |
| 丰富示例 | 每个功能都有完整的可运行示例 | ⭐⭐⭐⭐⭐ |
| 快速参考 | 提供一分钟速查表，快速查找 API | ⭐⭐⭐⭐⭐ |
| 提示词模板 | 专门为 AI 设计的代码生成提示词 | ⭐⭐⭐⭐⭐ |

---

## 🚀 快速开始

### 最简单的 Web 应用

AI 可以直接使用这个模板生成代码：

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class App {
    public static void main(String[] args) {
        WebApplication app = Web.create("我的应用", "1.0.0");
        
        app.get("/", (req, res) -> {
            res.send("Hello, World!");
        });
        
        app.onStartup(() -> {
            System.out.println("🚀 服务器启动成功！");
            System.out.println("📍 访问地址：http://localhost:8080");
        });
        
        app.run(8080);
    }
}
```

### 常用 Maven 依赖

```xml
<!-- 最小 Web 应用（推荐） -->
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web-impl</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </dependency>
</dependencies>

<!-- 完整功能：Web + 缓存 + 日志 -->
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

## 💡 AI 代码生成模式

### 模式 1：基础 Web 应用

**需求描述**：创建一个简单的 Web 应用，有首页和 API 接口

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import java.util.Map;

public class SimpleWebApp {
    public static void main(String[] args) {
        WebApplication app = Web.create("简单应用", "1.0.0");
        
        // 首页 - 返回 HTML
        app.get("/", (req, res) -> {
            res.html("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>首页</title>
                    <style>
                        body { font-family: Arial, sans-serif; padding: 50px; }
                        h1 { color: #2c3e50; }
                    </style>
                </head>
                <body>
                    <h1>欢迎使用 EST 框架！</h1>
                    <p>这是一个简单的 Web 应用。</p>
                </body>
                </html>
            """);
        });
        
        // API 接口 - 返回 JSON
        app.get("/api/hello", (req, res) -> {
            String name = req.queryParam("name", "访客");
            res.json(Map.of(
                "message", "你好，" + name + "！",
                "timestamp", System.currentTimeMillis()
            ));
        });
        
        // 带路径参数的接口
        app.get("/user/:id", (req, res) -> {
            String id = req.param("id");
            res.json(Map.of(
                "id", id,
                "name", "用户" + id,
                "email", "user" + id + "@example.com"
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

---

### 模式 2：REST API CRUD

**需求描述**：创建一个完整的 REST API，包含增删改查功能

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserApi {
    
    // 模拟数据存储
    private static final Map<String, User> users = new ConcurrentHashMap<>();
    
    public static void main(String[] args) {
        WebApplication app = Web.create("用户 API", "1.0.0");
        
        // 启用 CORS 支持前端访问
        app.enableCors();
        
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
            
            if (name == null || email == null) {
                res.sendError(400, "姓名和邮箱不能为空");
                return;
            }
            
            User user = new User(UUID.randomUUID().toString(), name, email);
            users.put(user.getId(), user);
            res.status(201).json(user);
        });
        
        // 更新用户
        app.put("/api/users/:id", (req, res) -> {
            String id = req.param("id");
            User user = users.get(id);
            
            if (user == null) {
                res.sendError(404, "用户不存在");
                return;
            }
            
            String name = req.formParam("name");
            String email = req.formParam("email");
            
            if (name != null) user.setName(name);
            if (email != null) user.setEmail(email);
            
            res.json(user);
        });
        
        // 删除用户
        app.delete("/api/users/:id", (req, res) -> {
            String id = req.param("id");
            users.remove(id);
            res.status(204).send("");
        });
        
        app.onStartup(() -> {
            System.out.println("🚀 用户 API 启动成功！");
            System.out.println("📍 API 地址：http://localhost:8080/api/users");
        });
        
        app.run(8080);
    }
    
    // 用户数据模型
    static class User {
        private String id;
        private String name;
        private String email;
        private long createdAt;
        
        public User(String id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.createdAt = System.currentTimeMillis();
        }
        
        // Getters and Setters
        public String getId() { return id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public long getCreatedAt() { return createdAt; }
    }
}
```

---

### 模式 3：依赖注入容器

**需求描述**：使用依赖注入容器管理服务

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

public class DiExample {
    
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        
        // 注册服务
        container.register(UserService.class, UserServiceImpl.class);
        container.register(EmailService.class, EmailServiceImpl.class);
        
        // 注册单例配置
        AppConfig config = new AppConfig();
        config.setDatabaseUrl("jdbc:mysql://localhost:3306/mydb");
        container.registerSingleton(AppConfig.class, config);
        
        // 获取服务并使用
        UserService userService = container.get(UserService.class);
        User user = userService.getUser("123");
        System.out.println("用户：" + user.getName());
        
        // 发送邮件
        EmailService emailService = container.get(EmailService.class);
        emailService.sendEmail(user.getEmail(), "欢迎", "欢迎加入我们！");
    }
    
    // 服务接口
    interface UserService {
        User getUser(String id);
    }
    
    interface EmailService {
        void sendEmail(String to, String subject, String body);
    }
    
    // 服务实现
    static class UserServiceImpl implements UserService {
        @Override
        public User getUser(String id) {
            return new User(id, "张三", "zhangsan@example.com");
        }
    }
    
    static class EmailServiceImpl implements EmailService {
        @Override
        public void sendEmail(String to, String subject, String body) {
            System.out.println("发送邮件到：" + to);
            System.out.println("主题：" + subject);
            System.out.println("内容：" + body);
        }
    }
    
    // 配置类
    static class AppConfig {
        private String databaseUrl;
        
        public String getDatabaseUrl() { return databaseUrl; }
        public void setDatabaseUrl(String databaseUrl) { this.databaseUrl = databaseUrl; }
    }
    
    // 数据模型
    static class User {
        private String id;
        private String name;
        private String email;
        
        public User(String id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
    }
}
```

---

### 模式 4：配置管理

**需求描述**：使用配置管理应用设置

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.core.api.Config;

public class ConfigExample {
    public static void main(String[] args) {
        WebApplication app = Web.create("配置示例", "1.0.0");
        Config config = app.getConfig();
        
        // 设置配置
        config.set("app.name", "我的应用");
        config.set("app.version", "1.0.0");
        config.set("server.port", 8080);
        config.set("debug", true);
        config.set("timeout", 30000L);
        config.set("maxConnections", 100);
        config.set("ratio", 0.75);
        
        // 获取配置
        String appName = config.getString("app.name");
        String appVersion = config.getString("app.version", "1.0.0"); // 默认值
        int port = config.getInt("server.port", 8080);
        boolean debug = config.getBoolean("debug", false);
        long timeout = config.getLong("timeout", 30000L);
        int maxConn = config.getInt("maxConnections", 50);
        double ratio = config.getDouble("ratio", 0.5);
        
        // 使用配置
        System.out.println("应用名称：" + appName);
        System.out.println("版本：" + appVersion);
        System.out.println("端口：" + port);
        System.out.println("调试模式：" + (debug ? "开启" : "关闭"));
        System.out.println("超时时间：" + timeout + "ms");
        System.out.println("最大连接数：" + maxConn);
        System.out.println("比率：" + ratio);
        
        // 在路由中使用配置
        app.get("/api/config", (req, res) -> {
            res.json(java.util.Map.of(
                "appName", config.getString("app.name"),
                "version", config.getString("app.version"),
                "debug", config.getBoolean("debug")
            ));
        });
        
        app.onStartup(() -> {
            System.out.println("🚀 " + appName + " 启动成功！");
            System.out.println("📍 访问地址：http://localhost:" + port);
        });
        
        app.run(port);
    }
}
```

---

## ✅ AI 编码最佳实践

### 1. 先看快速参考

AI 在生成代码前，应该先查看 [QUICK_REFERENCE.md](./QUICK_REFERENCE.md)，了解常用 API。

### 2. 遵循命名约定

- 类名：大驼峰命名 (UserService, OrderController)
- 方法名：小驼峰命名 (getUser, createOrder)
- 常量：全大写下划线分隔 (MAX_RETRIES, DEFAULT_TIMEOUT)
- 包名：全小写 (com.example.service, com.example.controller)

### 3. 参考示例代码

EST 框架提供了丰富的示例代码，位于 `est-examples/` 目录：

- `est-examples-basic` - 基础功能示例
- `est-examples-web` - Web 应用示例
- `est-examples-features` - 各功能模块示例
- `est-examples-advanced` - 高级用法示例

AI 可以参考这些示例来生成高质量的代码。

### 4. 错误处理模式

```java
app.get("/api/data", (req, res) -> {
    try {
        // 业务逻辑
        String id = req.param("id");
        if (id == null) {
            res.sendError(400, "缺少必要参数：id");
            return;
        }
        
        Object data = fetchData(id);
        if (data == null) {
            res.sendError(404, "数据不存在");
            return;
        }
        
        res.json(data);
    } catch (IllegalArgumentException e) {
        res.sendError(400, e.getMessage());
    } catch (Exception e) {
        System.err.println("服务器错误：" + e.getMessage());
        res.sendError(500, "服务器内部错误");
    }
});
```

### 5. 使用启动和关闭回调

```java
app.onStartup(() -> {
    System.out.println("========================================");
    System.out.println("  🚀 应用启动成功！");
    System.out.println("  📍 访问地址：http://localhost:8080");
    System.out.println("  ⏹️  按 Ctrl+C 停止应用");
    System.out.println("========================================");
});

app.onShutdown(() -> {
    System.out.println("\n👋 应用正在关闭...");
    System.out.println("清理资源...");
    System.out.println("再见！");
});
```

---

## 📚 常用参考

### 导入语句速查

```java
// Web 应用
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

// 请求和响应
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

// 核心模块
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;
import ltd.idcu.est.core.api.Config;
import ltd.idcu.est.core.impl.DefaultConfig;
```

### 常用 API 速查

| 功能 | 代码示例 |
|------|----------|
| 创建 Web 应用 | `WebApplication app = Web.create("名称", "版本");` |
| 添加 GET 路由 | `app.get("/path", (req, res) -> { ... });` |
| 获取路径参数 | `String id = req.param("id");` |
| 获取查询参数 | `String name = req.queryParam("name", "默认值");` |
| 发送文本 | `res.send("文本");` |
| 发送 HTML | `res.html("<h1>标题</h1>");` |
| 发送 JSON | `res.json(Map.of("key", "value"));` |
| 发送错误 | `res.sendError(404, "未找到");` |
| 启动服务器 | `app.run(8080);` |
| 设置配置 | `config.set("key", "value");` |
| 获取配置 | `String value = config.getString("key", "默认值");` |

---

## ❓ 常见问题

### Q: 如何启动 Web 服务器？

**A:**
```java
// 使用默认端口 8080
app.run();

// 指定端口
app.run(8080);

// 指定主机和端口
app.run("0.0.0.0", 8080);
```

### Q: 如何返回 JSON 响应？

**A:**
```java
res.json(Map.of("key", "value"));
res.json(List.of(item1, item2));
res.json(myObject); // 自定义对象
```

### Q: 如何获取路径参数？

**A:**
```java
// 路由定义：/user/:id
app.get("/user/:id", (req, res) -> {
    String id = req.param("id");
    // 使用 id
});
```

### Q: 如何获取查询参数？

**A:**
```java
// URL：/hello?name=张三
app.get("/hello", (req, res) -> {
    String name = req.queryParam("name"); // 没有返回 null
    String name = req.queryParam("name", "访客"); // 没有返回默认值
});
```

### Q: 如何启用 CORS？

**A:**
```java
// 启用默认 CORS（允许所有来源）
app.enableCors();

// 自定义 CORS
app.enableCors(Map.of(
    "origins", List.of("http://localhost:3000", "https://myapp.com"),
    "methods", List.of("GET", "POST", "PUT", "DELETE"),
    "headers", List.of("*"),
    "credentials", true
));
```

### Q: 如何处理异常？

**A:**
```java
// 全局异常处理
app.errorHandler(e -> {
    System.err.println("发生错误：" + e.getMessage());
    e.printStackTrace();
});

// 或者在路由中 try-catch
app.get("/api/data", (req, res) -> {
    try {
        // 业务逻辑
    } catch (Exception e) {
        res.sendError(500, "服务器错误");
    }
});
```

---

## 🎯 下一步

1. **查看快速参考** - [QUICK_REFERENCE.md](./QUICK_REFERENCE.md) - 一分钟速查表
2. **阅读入门指南** - [guides/getting-started.md](./guides/getting-started.md) - 从零开始
3. **浏览示例代码** - [est-examples/](../est-examples/) - 可运行的示例
4. **查阅 API 文档** - [api/](./api/) - 详细 API 参考
5. **学习教程** - [tutorials/](./tutorials/) - 从入门到精通

---

**祝你 AI 编程愉快！** 🎉
