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
import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.api.WebApplication;

WebApplication app = DefaultWebApplication.create();
app.run(8080);
```

### 配置路由

```java
app.getRouter().get("/", (req, res) -> {
    res.html("<h1>Hello EST!</h1>");
});

app.getRouter().post("/api/users", (req, res) -> {
    // 处理 POST 请求
});
```

### 使用中间件

```java
app.use(new LoggingMiddleware());
app.enableCors();
```
