# EST 框架快速参考卡片

> 为 AI coder 准备的一分钟速查表

## 核心概念

| 概念 | 说明 | 常用类/接口 |
|------|------|------------|
| 依赖注入 | 服务注册与获取 | `Container`, `DefaultContainer` |
| Web应用 | HTTP服务 | `WebApplication`, `DefaultWebApplication` |
| 路由 | 请求映射 | `Router` |
| 中间件 | 请求处理链 | `Middleware` |
| 缓存 | 数据缓存 | `Cache`, `Caches` |
| 事件 | 事件驱动 | `EventBus`, `EventBuses` |
| 配置 | 配置管理 | `Config`, `DefaultConfig` |

## 常用代码片段

### 1. 创建基础应用
```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

Container container = new DefaultContainer();
container.register(MyService.class, MyServiceImpl.class);
MyService service = container.get(MyService.class);
```

### 2. 创建 Web 应用
```java
import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

WebApplication app = new DefaultWebApplication();
app.routes(router -> {
    router.get("/", (req, res) -> res.html("<h1>Hello</h1>"));
    router.get("/api/data", (req, res) -> res.json(Map.of("key", "value")));
});
app.run(8080);
```

### 3. REST API CRUD
```java
// 定义路由
router.get("/api/items", this::list);
router.get("/api/items/:id", this::get);
router.post("/api/items", this::create);
router.put("/api/items/:id", this::update);
router.delete("/api/items/:id", this::delete);

// 处理请求
String id = request.param("id");
String body = request.body();
response.status(200).json(result);
```

### 4. 缓存
```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.Caches;

Cache<String, Object> cache = Caches.newMemoryCache();
cache.put("key", "value");
Object value = cache.get("key");
```

### 5. 事件
```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.EventBuses;

EventBus bus = EventBuses.newLocalEventBus();
bus.subscribe(MyEvent.class, event -> handle(event));
bus.publish(new MyEvent());
```

## 导入语句速查

### 核心模块
```java
import ltd.idcu.est.core.api.*;
import ltd.idcu.est.core.impl.*;
import ltd.idcu.est.core.api.annotation.*;
```

### Web 模块
```java
import ltd.idcu.est.web.api.*;
import ltd.idcu.est.web.*;
```

### 功能模块
```java
import ltd.idcu.est.features.cache.api.*;
import ltd.idcu.est.features.cache.memory.*;
import ltd.idcu.est.features.event.api.*;
import ltd.idcu.est.features.event.local.*;
import ltd.idcu.est.features.logging.api.*;
import ltd.idcu.est.features.logging.console.*;
```

## Maven 依赖速查

### 最小依赖
```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-api</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-impl</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
```

### Web 应用
```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-web-api</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-web-impl</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
```

## 响应状态码

| 状态码 | 用途 |
|--------|------|
| 200 | 成功 |
| 201 | 创建成功 |
| 400 | 请求错误 |
| 404 | 未找到 |
| 500 | 服务器错误 |

## 请求方法

| 方法 | Router 方法 |
|------|------------|
| GET | `router.get(path, handler)` |
| POST | `router.post(path, handler)` |
| PUT | `router.put(path, handler)` |
| DELETE | `router.delete(path, handler)` |
| PATCH | `router.patch(path, handler)` |

## 项目生成命令

```bash
# 基础项目
java -jar est-scaffold.jar new my-project

# Web 应用
java -jar est-scaffold.jar web my-web-app

# REST API
java -jar est-scaffold.jar api my-api
```

## 常见问题速查

**Q: 如何读取请求体？**
A: `String body = request.body();`

**Q: 如何设置响应状态？**
A: `response.status(404);`

**Q: 如何获取查询参数？**
A: `String value = request.query("param");`

**Q: 如何添加请求头？**
A: `response.header("Content-Type", "application/json");`

**Q: 如何使用中间件？**
A: `app.use(new LoggingMiddleware());`

## 更多资源

- [AI Coder 指南](./AI_CODER_GUIDE.md) - 完整指南
- [示例代码](../est-examples/) - 可运行示例
- [API 文档](./api/) - 详细 API 参考
