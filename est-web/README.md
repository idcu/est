# EST Web 模块

完整的Web应用框架，基于Java内置HttpServer，提供路由、中间件、会话管理等功能。

## 模块结构

```
est-web/
├── est-web-api/      # Web接口定义
├── est-web-impl/     # Web实现
└── pom.xml
```

## 快速开始

```java
import ltd.idcu.est.web.WebApplication;

public class HelloWorld {
    public static void main(String[] args) {
        WebApplication app = WebApplication.create("My App", "1.0.0");
        
        app.get("/", (req, res) -> {
            res.send("Hello, World!");
        });
        
        app.run(8080);
    }
}
```

## 主要功能

### 路由系统

```java
app.get("/users", (req, res) -> res.json(users));
app.post("/users", (req, res) -> res.json(createUser(req)));
app.put("/users/:id", (req, res) -> res.json(updateUser(req)));
app.delete("/users/:id", (req, res) -> res.status(204));

// 路由分组
app.routes(router -> {
    router.group("/api", (r, group) -> {
        r.get("/users", (req, res) -> res.json(users));
        r.middleware("auth");
    });
});
```

### 中间件

```java
app.middleware((req, res) -> {
    System.out.println("Request: " + req.getMethod() + " " + req.getPath());
    return true;
});

// 内置中间件
app.useCors();
app.useLogging();
app.usePerformanceMonitor();
```

### 会话管理

```java
app.get("/login", (req, res) -> {
    Session session = req.getSession(true);
    session.setAttribute("user", userId);
    res.send("Logged in");
});

app.get("/profile", (req, res) -> {
    Session session = req.getSession(false);
    if (session != null) {
        String userId = (String) session.getAttribute("user");
        res.json(getUser(userId));
    } else {
        res.status(401).send("Not logged in");
    }
});
```

### 模板引擎

```java
app.setViewEngine(new EstTemplateEngine("./templates"));

app.get("/home", (req, res) -> {
    res.render("home", Map.of(
        "title", "Welcome",
        "user", currentUser
    ));
});
```

## 依赖

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-web-api</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
```

## 相关文档

- [API 文档](../docs/api/web/)
- [教程](../docs/tutorials/web/)
