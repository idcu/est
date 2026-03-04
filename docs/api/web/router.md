# Router API

`Router` 接口提供了强大的路由管理功能，支持多种 HTTP 方法、路径参数、路由组等。

## 接口定义

```java
package ltd.idcu.est.web.api;

public interface Router {
    Route get(String path, RouteHandler handler);
    Route post(String path, RouteHandler handler);
    Route put(String path, RouteHandler handler);
    Route patch(String path, RouteHandler handler);
    Route delete(String path, RouteHandler handler);
    Route head(String path, RouteHandler handler);
    Route options(String path, RouteHandler handler);
    Route trace(String path, RouteHandler handler);
    
    Route route(HttpMethod method, String path, RouteHandler handler);
    
    void group(String prefix, Consumer<Router> groupConfigurer);
    
    void use(Middleware middleware);
    
    Route findRoute(HttpMethod method, String path);
    
    List<Route> getRoutes();
    
    void clear();
}
```

## HTTP 方法路由

### get

注册 GET 请求路由。

**参数：**
- `path` - 路径模式
- `handler` - 路由处理器

**返回：**
- 注册的路由

**示例：**
```java
router.get("/", (req, res) -> {
    res.text("Hello, World!");
});

router.get("/users/:id", (req, res) -> {
    String id = req.getPathVariable("id");
    res.text("User: " + id);
});
```

---

### post

注册 POST 请求路由。

**参数：**
- `path` - 路径模式
- `handler` - 路由处理器

**返回：**
- 注册的路由

**示例：**
```java
router.post("/users", (req, res) -> {
    Map<String, Object> body = req.getJsonBody();
    // 创建用户
    res.status(201).json(body);
});
```

---

### put

注册 PUT 请求路由。

**参数：**
- `path` - 路径模式
- `handler` - 路由处理器

**返回：**
- 注册的路由

**示例：**
```java
router.put("/users/:id", (req, res) -> {
    String id = req.getPathVariable("id");
    Map<String, Object> body = req.getJsonBody();
    // 更新用户
    res.json(body);
});
```

---

### patch

注册 PATCH 请求路由。

**参数：**
- `path` - 路径模式
- `handler` - 路由处理器

**返回：**
- 注册的路由

**示例：**
```java
router.patch("/users/:id", (req, res) -> {
    String id = req.getPathVariable("id");
    Map<String, Object> body = req.getJsonBody();
    // 部分更新用户
    res.json(body);
});
```

---

### delete

注册 DELETE 请求路由。

**参数：**
- `path` - 路径模式
- `handler` - 路由处理器

**返回：**
- 注册的路由

**示例：**
```java
router.delete("/users/:id", (req, res) -> {
    String id = req.getPathVariable("id");
    // 删除用户
    res.status(204);
});
```

---

### head

注册 HEAD 请求路由。

**参数：**
- `path` - 路径模式
- `handler` - 路由处理器

**返回：**
- 注册的路由

**示例：**
```java
router.head("/users/:id", (req, res) -> {
    String id = req.getPathVariable("id");
    // 检查用户是否存在
    if (userExists(id)) {
        res.status(200);
    } else {
        res.status(404);
    }
});
```

---

### options

注册 OPTIONS 请求路由。

**参数：**
- `path` - 路径模式
- `handler` - 路由处理器

**返回：**
- 注册的路由

**示例：**
```java
router.options("/users", (req, res) -> {
    res.header("Allow", "GET,POST,OPTIONS");
    res.header("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
    res.status(200);
});
```

---

### route

注册任意 HTTP 方法的路由。

**参数：**
- `method` - HTTP 方法
- `path` - 路径模式
- `handler` - 路由处理器

**返回：**
- 注册的路由

**示例：**
```java
router.route(HttpMethod.GET, "/users", (req, res) -> {
    res.text("List users");
});

router.route(HttpMethod.POST, "/users", (req, res) -> {
    res.status(201).text("User created");
});
```

## 路径模式

### 基本路径

```java
// 精确匹配
router.get("/", handler);           // 匹配 "/"
router.get("/users", handler);      // 匹配 "/users"
```

### 路径参数

```java
// 单参数
router.get("/users/:id", handler);
// 匹配 "/users/123", "/users/abc"

// 多参数
router.get("/users/:userId/posts/:postId", handler);
// 匹配 "/users/123/posts/456"

// 可选参数
router.get("/users/:id?", handler);
// 匹配 "/users" 和 "/users/123"
```

### 通配符

```java
// 匹配任意单段路径
router.get("/users/*/posts", handler);
// 匹配 "/users/123/posts", "/users/abc/posts"

// 匹配任意路径
router.get("/files/**", handler);
// 匹配 "/files/doc.txt", "/files/images/photo.jpg"
```

## 路由组

### 基本路由组

```java
router.group("/api", (group) -> {
    // 这些路由的前缀都是 /api
    group.get("/users", (req, res) -> res.text("API Users"));
    group.post("/users", (req, res) -> res.text("Create User"));
    group.get("/users/:id", (req, res) -> res.text("Get User"));
});
// 路由: /api/users, /api/users/123
```

### 嵌套路由组

```java
router.group("/api", (api) -> {
    api.group("/v1", (v1) -> {
        v1.get("/users", (req, res) -> res.text("API v1 Users"));
        v1.get("/posts", (req, res) -> res.text("API v1 Posts"));
    });
    
    api.group("/v2", (v2) -> {
        v2.get("/users", (req, res) -> res.text("API v2 Users"));
        v2.get("/posts", (req, res) -> res.text("API v2 Posts"));
    });
});
// 路由: /api/v1/users, /api/v2/users
```

### 路由组中间件

```java
router.group("/api", (group) -> {
    // 应用到整个组的中间件
    group.use(new AuthMiddleware());
    group.use(new LoggingMiddleware());
    
    group.get("/users", (req, res) -> res.text("Protected Users"));
    group.get("/admin", (req, res) -> res.text("Admin only"));
});
```

## 中间件

### use

添加中间件。

**参数：**
- `middleware` - 中间件实例

**示例：**
```java
// 添加日志中间件
router.use(new LoggingMiddleware());

// 添加认证中间件
router.use(new AuthMiddleware());

// 添加 CORS 中间件
router.use(new CorsMiddleware());
```

## 路由查找

### findRoute

查找匹配的路由。

**参数：**
- `method` - HTTP 方法
- `path` - 请求路径

**返回：**
- 匹配的路由，未找到返回 null

**示例：**
```java
Route route = router.findRoute(HttpMethod.GET, "/users/123");
if (route != null) {
    // 处理路由
}
```

### getRoutes

获取所有注册的路由。

**返回：**
- 路由列表

**示例：**
```java
List<Route> routes = router.getRoutes();
for (Route route : routes) {
    System.out.println(route.getMethod() + " " + route.getPath());
}
```

### clear

清空所有路由和中间件。

**示例：**
```java
router.clear();
```

## DefaultRouter 实现

`DefaultRouter` 是 `Router` 接口的默认实现。

**特性：**
- 高效的路由匹配
- 支持路径参数
- 支持路由组
- 支持中间件链
- 线程安全

**使用示例：**
```java
Router router = new DefaultRouter();

// 注册路由
router.get("/", (req, res) -> res.text("Home"));
router.get("/users/:id", (req, res) -> {
    String id = req.getPathVariable("id");
    res.text("User: " + id);
});

// 查找路由
Route route = router.findRoute(HttpMethod.GET, "/users/123");
if (route != null) {
    // 处理请求
}
```

## 最佳实践

1. **使用 RESTful 设计**
   - 使用名词表示资源（/users, /products）
   - 使用 HTTP 方法表示动作
   - 使用适当的状态码

2. **组织路由**
   - 使用路由组组织相关路由
   - 版本化 API（/api/v1, /api/v2）

3. **路径参数验证**
   - 验证路径参数的格式和范围
   - 处理无效参数

4. **中间件顺序**
   - 按照正确的顺序添加中间件
   - 认证中间件应该在日志中间件之后
   - 错误处理中间件应该在最后
