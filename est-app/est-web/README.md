# EST Web - Web 应用框架

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

EST Web 是 EST 框架的 Web 应用模块，基于 Java 内置 HttpServer，提供完整的 Web 应用开发功能。

---

## 📚 目录

- [快速入门](#快速入门)
- [基础篇：路由系统](#基础篇路由系统)
- [基础篇：请求响应](#基础篇请求响应)
- [进阶篇：中间件](#进阶篇中间件)
- [进阶篇：会话管理](#进阶篇会话管理)
- [最佳实践](#最佳实践)

---

## 🚀 快速入门

### 什么是 Web 框架？

想象你要开一家商店，需要：
- 🏪 门店（服务器）
- 📋 菜单（路由）
- 👨‍🍳 后厨（处理逻辑）
- 🧾 账单（响应）

**EST Web** 就是帮你开店的工具包！

### 5分钟上手

```java
import ltd.idcu.est.web.WebApplication;

public class FirstExample {
    public static void main(String[] args) {
        System.out.println("=== EST Web 5分钟上手 ===");
        System.out.println();

        WebApplication app = WebApplication.create("我的第一个网站", "1.0.0");
        
        app.get("/", (req, res) -> {
            res.send("Hello, EST Web!");
        });
        
        System.out.println("服务器已启动，访问: http://localhost:8080");
        app.run(8080);
    }
}
```

运行后，在浏览器打开 http://localhost:8080 就能看到你的第一个网页了！ 🎉

---

## 🔰 基础篇：路由系统

### 生活类比

路由就像商店的菜单，客人点什么菜，你就给什么菜。

### 基本路由

```java
import ltd.idcu.est.web.WebApplication;

public class RouterExample {
    public static void main(String[] args) {
        WebApplication app = WebApplication.create("路由示例", "1.0.0");
        
        app.get("/", (req, res) -> {
            res.send("首页");
        });
        
        app.get("/about", (req, res) -> {
            res.send("关于我们");
        });
        
        app.get("/contact", (req, res) -> {
            res.send("联系方式");
        });
        
        app.run(8080);
    }
}
```

### HTTP 方法

```java
app.get("/users", (req, res) -> {
    res.send("获取用户列表");
});

app.post("/users", (req, res) -> {
    res.send("创建新用户");
});

app.put("/users/:id", (req, res) -> {
    String id = req.getPathParam("id");
    res.send("更新用户: " + id);
});

app.delete("/users/:id", (req, res) -> {
    String id = req.getPathParam("id");
    res.send("删除用户: " + id);
});
```

---

## 🔰 基础篇：请求响应

### 生活类比

请求就是客人点的菜，响应就是你端上桌的菜。

### 获取请求参数

```java
import ltd.idcu.est.web.WebApplication;

public class RequestExample {
    public static void main(String[] args) {
        WebApplication app = WebApplication.create("请求示例", "1.0.0");
        
        app.get("/greet", (req, res) -> {
            String name = req.getQueryParam("name", "访客");
            res.send("你好, " + name + "!");
        });
        
        app.get("/user/:id", (req, res) -> {
            String userId = req.getPathParam("id");
            res.send("用户ID: " + userId);
        });
        
        app.run(8080);
    }
}
```

### 返回响应

```java
app.get("/text", (req, res) -> {
    res.send("普通文本");
});

app.get("/json", (req, res) -> {
    res.json(Map.of(
        "name", "EST",
        "version", "1.0.0"
    ));
});

app.get("/status", (req, res) -> {
    res.status(200).send("成功");
});

app.get("/error", (req, res) -> {
    res.status(500).send("服务器错误");
});
```

---

## 📈 进阶篇：中间件

### 生活类比

中间件就像餐厅的服务员，在客人点单和上菜之间做一些处理：比如记录订单、检查座位等。

### 日志中间件

```java
import ltd.idcu.est.web.WebApplication;

public class MiddlewareExample {
    public static void main(String[] args) {
        WebApplication app = WebApplication.create("中间件示例", "1.0.0");
        
        app.middleware((req, res) -> {
            System.out.println("请求: " + req.getMethod() + " " + req.getPath());
            return true;
        });
        
        app.get("/", (req, res) -> {
            res.send("首页");
        });
        
        app.useLogging();
        app.run(8080);
    }
}
```

### 内置中间件

```java
app.useLogging();           // 日志中间件
app.useCors();              // CORS 跨域中间件
app.usePerformanceMonitor(); // 性能监控中间件
```

---

## 📈 进阶篇：会话管理

### 生活类比

会话就像餐厅的会员卡，记录客人的信息，下次来还能认出你。

### 登录和会话

```java
import ltd.idcu.est.web.WebApplication;
import ltd.idcu.est.web.api.Session;

public class SessionExample {
    public static void main(String[] args) {
        WebApplication app = WebApplication.create("会话示例", "1.0.0");
        
        app.get("/login", (req, res) -> {
            Session session = req.getSession(true);
            session.setAttribute("username", "小明");
            res.send("登录成功！");
        });
        
        app.get("/profile", (req, res) -> {
            Session session = req.getSession(false);
            if (session != null) {
                String username = (String) session.getAttribute("username");
                res.send("欢迎回来, " + username);
            } else {
                res.status(401).send("请先登录");
            }
        });
        
        app.get("/logout", (req, res) -> {
            Session session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            res.send("已退出登录");
        });
        
        app.run(8080);
    }
}
```

---

## ✨ 最佳实践

### 1. 路由分组

```java
app.routes(router -> {
    router.group("/api", (r, group) -> {
        r.get("/users", (req, res) -> res.send("用户列表"));
        r.post("/users", (req, res) -> res.send("创建用户"));
    });
});
```

### 2. 错误处理

```java
app.error((req, res, e) -> {
    res.status(500).send("出错了: " + e.getMessage());
});
```

### 3. 静态文件

```java
app.serveStatic("/static", "./public");
```

---

## 📦 模块集成

### 与 est-collection 集成

```java
import ltd.idcu.est.web.WebApplication;
import ltd.idcu.est.collection.impl.Seqs;

public class CollectionIntegration {
    public static void main(String[] args) {
        WebApplication app = WebApplication.create("集成示例", "1.0.0");
        
        app.get("/users", (req, res) -> {
            List<User> users = getUsersFromDB();
            List<String> names = Seqs.of(users)
                .map(User::getName)
                .toList();
            res.json(names);
        });
        
        app.run(8080);
    }
}
```

---

## 📚 更多内容

- [EST 项目主页](https://github.com/idcu/est)
- [EST Core](../../est-foundation/est-core/README.md)
- [EST Collection](../../est-foundation/est-collection/README.md)

---

**祝你使用愉快！** 🎉
