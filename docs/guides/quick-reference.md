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

---

## 📚 更多资源

- [完整文档](../README.md) - 文档索引
- [AI Coder 指南](../ai/ai-coder-guide.md) - AI 编程指南
- [示例代码](../../est-examples/) - 可运行的示例
- [API 文档](../api/) - 详细 API 参考
- [教程](../tutorials/) - 从入门到精通

---

**祝你编码愉快！** 🎉
