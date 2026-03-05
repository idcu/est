# Web 模块 API

Web 模块提供完整的 Web 开发功能，包括 HTTP 服务器、路由、中间件、控制器等。

## 包结构

```
ltd.idcu.est.web
├── api              # Web 接口
│   ├── WebApplication
│   ├── Router
│   ├── Route
│   ├── Middleware
│   ├── Request
│   ├── Response
│   ├── Session
│   └── Controller
└── impl             # Web 实现
    ├── DefaultWebApplication
    ├── DefaultRouter
    ├── DefaultRequest
    ├── DefaultResponse
    └── ...
```

## 快速导航

| 接口 | 说明 |
|------|------|
| [WebApplication](./web-application.md) | Web 应用入口 |
| [Router](./router.md) | 路由管理 |
| [Middleware](./middleware.md) | 中间件 |
| [Request & Response](./request-response.md) | 请求与响应 |

## 使用示例

### 创建 Web 应用

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

WebApplication app = Web.create("我的Web应用", "1.0.0");
app.run(8080);
```

### 配置路由

```java
app.get("/", (req, res) -> {
    res.html("<h1>Hello EST!</h1>");
});

app.post("/api/users", (req, res) -> {
    Map<String, Object> body = req.getJsonBody();
    res.status(201).json(body);
});

app.get("/users/:id", (req, res) -> {
    String id = req.getPathVariable("id");
    res.text("用户 ID: " + id);
});
```

### 使用路由组

```java
app.group("/api/v1", (api) -> {
    api.get("/users", (req, res) -> res.text("用户列表"));
    api.post("/users", (req, res) -> res.status(201).text("创建用户"));
    api.get("/users/:id", (req, res) -> res.text("用户 " + req.getPathVariable("id")));
});
```
