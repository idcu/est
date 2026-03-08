# EST Web Router Web 路由模块 - 小白从入门到精通

## 目录
1. [什么是 EST Web Router？](#什么是-est-web-router)
2. [快速入门：5分钟上手](#快速入门5分钟上手)
3. [基础篇](#基础篇)
4. [进阶篇](#进阶篇)
5. [最佳实践](#最佳实践)

---

## 什么是 EST Web Router？

### 用大白话理解

EST Web Router 就像是一个"交通指挥中心"。想象一下你的网站有很多页面，用户访问不同的 URL 需要找到对应的处理程序：

**传统方式**：你需要自己写一大堆 if-else 或者 switch 来判断 URL 要调用哪个函数，很麻烦！

**EST Web Router 方式**：给你一套智能的路由系统，支持：
- 🛣️ **路径参数** - `/users/{id}` 匹配 `/users/123`
- 🔍 **查询参数** - `?page=1&limit=10`
- 📦 **路由分组** - 把相关的路由组织在一起
- 🏗️ **路由树** - 高性能的 Trie 树实现

### 核心特点

- 🎯 **简单易用** - 几行代码就能定义路由
- ⚡ **高性能** - 基于 Trie 树的路由匹配
- 🔧 **灵活强大** - 支持路径参数、通配符、路由分组
- 🎨 **RESTful 友好** - 支持 GET、POST、PUT、DELETE 等 HTTP 方法

---

## 快速入门：5分钟上手

### 第一步：引入依赖

在你的 Maven pom.xml 中添加：

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web-router-api</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web-router-impl</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>
```

### 第二步：你的第一个路由应用

```java
import ltd.idcu.est.web.router.Router;
import ltd.idcu.est.web.router.Routers;
import ltd.idcu.est.web.router.Request;
import ltd.idcu.est.web.router.Response;

public class FirstRouterApp {
    public static void main(String[] args) {
        System.out.println("=== EST Web Router 第一个示例 ===\n");
        
        Router router = Routers.create();
        
        router.get("/", (req, res) -> {
            res.send("Hello, EST Router!");
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
        
        System.out.println("路由已配置！");
        System.out.println("访问 http://localhost:8080/ 查看首页");
        System.out.println("访问 http://localhost:8080/hello/张三 查看个性化问候");
    }
}
```

---

## 基础篇

### 1. 定义路由

#### 基本路由

```java
import ltd.idcu.est.web.router.Router;
import ltd.idcu.est.web.router.Routers;

Router router = Routers.create();

router.get("/", (req, res) -> res.send("首页"));
router.post("/users", (req, res) -> res.send("创建用户"));
router.put("/users/{id}", (req, res) -> res.send("更新用户"));
router.delete("/users/{id}", (req, res) -> res.send("删除用户"));
```

#### HTTP 方法支持

```java
Router router = Routers.create();

router.get("/users", listUsers);
router.post("/users", createUser);
router.put("/users/{id}", updateUser);
router.delete("/users/{id}", deleteUser);
router.patch("/users/{id}", patchUser);
router.head("/users/{id}", headUser);
router.options("/users", optionsUser);
```

### 2. 路径参数

#### 简单路径参数

```java
Router router = Routers.create();

router.get("/users/{id}", (req, res) -> {
    String id = req.pathParam("id");
    res.send("用户 ID: " + id);
});

router.get("/posts/{postId}/comments/{commentId}", (req, res) -> {
    String postId = req.pathParam("postId");
    String commentId = req.pathParam("commentId");
    res.send("文章 " + postId + " 的评论 " + commentId);
});
```

#### 通配符匹配

```java
Router router = Routers.create();

router.get("/files/*", (req, res) -> {
    String path = req.pathParam("*");
    res.send("文件路径: " + path);
});

router.get("/api/**", (req, res) -> {
    String path = req.path();
    res.send("API 路径: " + path);
});
```

### 3. 查询参数

#### 获取查询参数

```java
Router router = Routers.create();

router.get("/search", (req, res) -> {
    String keyword = req.queryParam("keyword", "");
    int page = req.queryParamAsInt("page", 1);
    int limit = req.queryParamAsInt("limit", 10);
    
    res.json(Map.of(
        "keyword", keyword,
        "page", page,
        "limit", limit
    ));
});

router.get("/users", (req, res) -> {
    String sort = req.queryParam("sort", "id");
    boolean ascending = req.queryParamAsBoolean("asc", true);
    
    res.send("排序字段: " + sort + ", 升序: " + ascending);
});
```

### 4. 请求和响应

#### 请求对象

```java
import ltd.idcu.est.web.router.Request;

router.get("/example", (req, res) -> {
    String method = req.method();
    String path = req.path();
    String userAgent = req.header("User-Agent");
    String contentType = req.header("Content-Type");
    
    String body = req.bodyAsString();
    User user = req.bodyAs(User.class);
    
    res.send("请求信息已获取");
});
```

#### 响应对象

```java
import ltd.idcu.est.web.router.Response;

router.get("/response", (req, res) -> {
    res.send("文本响应");
    res.json(Map.of("key", "value"));
    res.status(201).send("已创建");
    res.status(404).send("未找到");
    
    res.header("Content-Type", "application/json");
    res.cookie("sessionId", "abc123");
    res.cookie("rememberMe", "true", 86400);
});
```

---

## 进阶篇

### 1. 路由分组

#### 分组路由

```java
Router router = Routers.create();

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

#### 分组中间件

```java
Router router = Routers.create();

router.group("/api", () -> {
    router.use(authMiddleware);
    router.use(loggingMiddleware);
    
    router.get("/users", listUsers);
    router.get("/orders", listOrders);
});

router.get("/public", publicHandler);
```

### 2. 路由前缀和子路由

#### 子路由挂载

```java
Router apiRouter = Routers.create();
apiRouter.get("/users", listUsers);
apiRouter.get("/orders", listOrders);

Router mainRouter = Routers.create();
mainRouter.mount("/api/v1", apiRouter);
```

### 3. 路由正则表达式

#### 正则路径参数

```java
Router router = Routers.create();

router.get("/users/{id:\\d+}", (req, res) -> {
    String id = req.pathParam("id");
    res.send("数字 ID: " + id);
});

router.get("/files/{path:.+\\.(png|jpg|gif)}", (req, res) -> {
    String path = req.pathParam("path");
    res.send("图片文件: " + path);
});
```

---

## 最佳实践

### 1. 路由组织

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
router.get("/api/orders", listOrders);
```

### 2. 路径参数命名

```java
// ✅ 推荐：清晰的参数名
router.get("/users/{userId}/posts/{postId}", handler);

// ❌ 不推荐：模糊的参数名
router.get("/users/{a}/posts/{b}", handler);
```

### 3. RESTful 风格

```java
// ✅ 推荐：RESTful 路由设计
router.get("/users", listUsers);          // 获取列表
router.get("/users/{id}", getUser);      // 获取单个
router.post("/users", createUser);       // 创建
router.put("/users/{id}", updateUser);   // 更新
router.delete("/users/{id}", deleteUser);// 删除

// ❌ 不推荐：动词在路径中
router.get("/getUsers", listUsers);
router.post("/createUser", createUser);
```

---

## 模块结构

```
est-web-router/
├── est-web-router-api/    # 路由 API
└── est-web-router-impl/   # 路由实现
```

---

## 相关资源

- [est-web-middleware README](../est-web-middleware/README.md) - 中间件文档
- [est-web-session README](../est-web-session/README.md) - 会话管理文档
- [EST Web Group README](../README.md) - Web 模块组文档
- [示例代码](../../../est-examples/est-examples-web/) - Web 示例代码

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
