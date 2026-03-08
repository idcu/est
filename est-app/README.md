# EST App 应用模块 - 小白从入门到精通

## 目录
1. [什么是 EST App？](#什么是-est-app)
2. [快速入门：5分钟上手](#快速入门5分钟上手)
3. [基础篇](#基础篇)
4. [进阶篇](#进阶篇)
5. [最佳实践](#最佳实践)

---

## 什么是 EST App？

### 用大白话理解

EST App 就像是一个"应用模板"。想象一下你要盖房子，需要打好地基、搭好框架、装好门窗...

**传统方式**：每次盖房子都要从头开始，很麻烦。

**EST App 方式**：给你现成的应用模板，里面有：
- 🌐 **Web 应用框架** - 快速创建 Web 应用
- 🔧 **管理后台** - 完整的前后端分离管理系统
- 💻 **控制台应用** - 命令行应用支持

### 核心特点

- 🎯 **简单易用** - 几行代码就能创建应用
- ⚡ **开箱即用** - 预置常用功能
- 🔧 **灵活扩展** - 可以自定义和扩展
- 🎨 **多种类型** - 支持 Web、管理后台、控制台应用

---

## 快速入门：5分钟上手

### 第一步：引入依赖

在你的 Maven pom.xml 中添加：

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>
```

### 第二步：你的第一个 Web 应用

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class FirstWebApp {
    public static void main(String[] args) {
        System.out.println("=== EST App 第一个示例 ===\n");
        
        WebApplication app = Web.create("我的第一个 Web 应用", "1.0.0");
        
        app.get("/", (req, res) -> res.send("Hello, EST App!"));
        
        app.get("/hello/{name}", (req, res) -> {
            String name = req.pathParam("name");
            res.send("Hello, " + name + "!");
        });
        
        app.get("/api/user", (req, res) -> {
            res.json(Map.of(
                "name", "张三",
                "age", 25,
                "email", "zhangsan@example.com"
            ));
        });
        
        app.run(8080);
        System.out.println("应用已启动: http://localhost:8080");
    }
}
```

运行这个程序，然后访问：
- http://localhost:8080 - 首页
- http://localhost:8080/hello/张三 - 个性化问候
- http://localhost:8080/api/user - JSON API

---

## 基础篇

### 1. est-web Web 应用框架

详细文档请参考：[est-web README](./est-web/README.md)

#### 创建 Web 应用

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

WebApplication app = Web.create("我的应用", "1.0.0");
```

#### 添加路由

```java
// GET 请求
app.get("/users", (req, res) -> {
    List<User> users = userService.findAll();
    res.json(users);
});

// POST 请求
app.post("/users", (req, res) -> {
    User user = req.bodyAs(User.class);
    User saved = userService.save(user);
    res.status(201).json(saved);
});

// PUT 请求
app.put("/users/{id}", (req, res) -> {
    Long id = Long.parseLong(req.pathParam("id"));
    User user = req.bodyAs(User.class);
    User updated = userService.update(id, user);
    res.json(updated);
});

// DELETE 请求
app.delete("/users/{id}", (req, res) -> {
    Long id = Long.parseLong(req.pathParam("id"));
    userService.delete(id);
    res.status(204).send();
});
```

#### 请求和响应

```java
app.get("/example", (req, res) -> {
    // 获取路径参数
    String id = req.pathParam("id");
    
    // 获取查询参数
    String name = req.queryParam("name");
    int page = req.queryParamAsInt("page", 1);
    
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
});
```

### 2. est-admin 管理后台

EST Admin 是一个完整的前后端分离管理系统。

#### 启动管理后台

```java
import ltd.idcu.est.admin.Admin;

public class AdminApp {
    public static void main(String[] args) {
        Admin admin = Admin.create();
        admin.run(8080);
        System.out.println("管理后台已启动: http://localhost:8080");
    }
}
```

#### 管理后台功能

- 👥 **用户管理** - 用户增删改查
- 🏢 **部门管理** - 部门组织架构
- 👔 **角色管理** - 角色权限配置
- 📋 **菜单管理** - 菜单权限控制
- 🏢 **租户管理** - 多租户支持
- 📊 **在线用户** - 在线用户监控
- 📝 **操作日志** - 操作记录审计
- 📈 **登录日志** - 登录记录查询
- 🔧 **系统监控** - 系统状态监控

#### 前端

管理后台前端使用 Vue 3 + Element Plus，详细信息请参考：[est-admin-ui README](../est-admin-ui/README.md)

### 3. est-console 控制台应用

EST Console 用于创建命令行应用。

```java
import ltd.idcu.est.console.Console;

public class ConsoleApp {
    public static void main(String[] args) {
        Console console = Console.create("我的控制台应用", "1.0.0");
        
        console.addCommand("hello", () -> {
            System.out.println("Hello, Console!");
        });
        
        console.addCommand("greet", (name) -> {
            System.out.println("Hello, " + name + "!");
        });
        
        console.run();
    }
}
```

---

## 进阶篇

### 1. Web 应用进阶

详细内容请参考：[est-web 进阶篇](./est-web/README.md)

#### 中间件

```java
import ltd.idcu.est.web.api.Middleware;

// 日志中间件
Middleware loggingMiddleware = (req, res, next) -> {
    System.out.println(req.method() + " " + req.path());
    next.proceed();
};

app.use(loggingMiddleware);

// CORS 中间件
app.use(Web.corsMiddleware());

// 安全中间件
app.use(Web.securityMiddleware());
```

#### 静态文件

```java
// 服务静态文件
app.staticFiles("/static", "src/main/resources/static");

// 首页
app.get("/", (req, res) -> {
    res.render("index.html", Map.of("title", "首页"));
});
```

#### 会话管理

```java
app.get("/login", (req, res) -> {
    req.session().set("user", user);
    res.send("登录成功");
});

app.get("/profile", (req, res) -> {
    User user = req.session().get("user");
    res.json(user);
});

app.get("/logout", (req, res) -> {
    req.session().invalidate();
    res.send("登出成功");
});
```

### 2. 管理后台进阶

#### 自定义服务

```java
import ltd.idcu.est.admin.Admin;
import ltd.idcu.est.admin.api.UserService;

Admin admin = Admin.create();

// 自定义用户服务
admin.setUserService(new MyUserService());

admin.run(8080);
```

#### 添加自定义 API

```java
Admin admin = Admin.create();

WebApplication app = admin.getWebApplication();

app.get("/api/custom", (req, res) -> {
    res.json(Map.of("message", "自定义 API"));
});

admin.run(8080);
```

---

## 最佳实践

### 1. 合理组织路由

```java
// ✅ 推荐：按功能分组
app.group("/api/users", () -> {
    app.get("", listUsers);
    app.get("/{id}", getUser);
    app.post("", createUser);
    app.put("/{id}", updateUser);
    app.delete("/{id}", deleteUser);
});

// ❌ 不推荐：零散的路由
app.get("/api/users", listUsers);
app.get("/api/users/:id", getUser);
// ...
```

### 2. 使用中间件

```java
// ✅ 推荐：使用中间件处理横切关注点
app.use(loggingMiddleware);
app.use(authMiddleware);
app.use(corsMiddleware);

// ❌ 不推荐：每个路由都重复写
app.get("/api/users", (req, res) -> {
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

---

## 模块结构

```
est-app/
├── est-web/          # Web 应用框架
├── est-admin/        # 管理后台
└── est-console/      # 控制台应用
```

---

## 相关资源

- [est-web README](./est-web/README.md) - Web 应用详细文档
- [est-admin-ui README](../est-admin-ui/README.md) - 管理后台前端文档
- [示例代码](../est-examples/est-examples-web/) - Web 示例代码
- [示例代码](../est-examples/est-examples-advanced/) - 高级示例
- [EST Core](../est-core/README.md) - 核心模块
- [EST Modules](../est-modules/README.md) - 功能模块

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
