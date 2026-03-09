# EST Web Middleware Web 中间件模�?- 小白从入门到精�?
## 目录
1. [什么是 EST Web Middleware？](#什么是-est-web-middleware)
2. [快速入门：5分钟上手](#快速入�?分钟上手)
3. [基础篇](#基础�?
4. [进阶篇](#进阶�?
5. [最佳实践](#最佳实�?

---

## 什么是 EST Web Middleware�?### 用大白话理解

EST Web Middleware 就像是一�?**检查站**。想象一下机场安检，乘客在登机前要经过安检�?
**传统方式**：每个航班都要自己安排安检，重复工作！

**EST Web Middleware 方式**：给你一套中间件管道，在请求到达处理器之前和响应返回之后做处理：
- 📝 **日志中间�?* - 记录请求日志
- 🔐 **认证中间�?* - 验证用户身份
- 🌐 **CORS 中间�?* - 处理跨域请求
- 🛡�?**安全中间�?* - 防护 XSS、CSRF 攻击
- 📦 **压缩中间�?* - Gzip 压缩响应

### 核心特�?
- 🎯 **简单易�?* - 几行代码就能添加中间�?- 📦 **高性能** - 优化的中间件管道
- 🎨 **灵活强大** - 支持自定义中间件
- 📋 **功能完整** - 内置常用中间�?
---

## 快速入门：5分钟上手

### 第一步：引入依赖

在你�?Maven pom.xml 中添加：

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web-middleware-api</artifactId>
        <version>2.3.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web-middleware-impl</artifactId>
        <version>2.3.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### 第二步：你的第一个中间件应用

```java
import ltd.idcu.est.web.router.Router;
import ltd.idcu.est.web.router.Routers;
import ltd.idcu.est.web.middleware.Middleware;
import ltd.idcu.est.web.middleware.MiddlewareChain;
import ltd.idcu.est.web.middleware.Middlewares;

public class FirstMiddlewareApp {
    public static void main(String[] args) {
        System.out.println("=== EST Web Middleware 第一个示�?===\n");
        
        Router router = Routers.create();
        
        Middleware loggingMiddleware = (req, res, chain) -> {
            System.out.println(req.method() + " " + req.path());
            long start = System.currentTimeMillis();
            
            chain.proceed();
            
            long duration = System.currentTimeMillis() - start;
            System.out.println("耗时: " + duration + "ms");
        };
        
        router.use(loggingMiddleware);
        router.use(Middlewares.cors());
        router.use(Middlewares.security());
        
        router.get("/", (req, res) -> {
            res.send("Hello, Middleware!");
        });
        
        System.out.println("中间件已配置�?);
    }
}
```

---

## 基础�?### 1. 内置中间�?#### 日志中间�?```java
import ltd.idcu.est.web.middleware.Middlewares;

Router router = Routers.create();
router.use(Middlewares.logging());
```

#### CORS 中间�?```java
import ltd.idcu.est.web.middleware.Middlewares;
import ltd.idcu.est.web.middleware.CorsConfig;

CorsConfig config = CorsConfig.builder()
    .allowOrigin("*")
    .allowMethods("GET", "POST", "PUT", "DELETE")
    .allowHeaders("Content-Type", "Authorization")
    .maxAge(3600)
    .build();

Router router = Routers.create();
router.use(Middlewares.cors(config));
```

#### 安全中间�?```java
import ltd.idcu.est.web.middleware.Middlewares;
import ltd.idcu.est.web.middleware.SecurityConfig;

SecurityConfig config = SecurityConfig.builder()
    .xssProtection(true)
    .frameOptions("DENY")
    .contentTypeOptions(true)
    .hsts(true)
    .build();

Router router = Routers.create();
router.use(Middlewares.security(config));
```

#### 压缩中间�?```java
import ltd.idcu.est.web.middleware.Middlewares;
import ltd.idcu.est.web.middleware.CompressionConfig;

CompressionConfig config = CompressionConfig.builder()
    .minSize(1024)
    .mimeTypes("text/plain", "text/html", "application/json")
    .build();

Router router = Routers.create();
router.use(Middlewares.gzip(config));
```

#### 限流中间�?```java
import ltd.idcu.est.web.middleware.Middlewares;
import ltd.idcu.est.web.middleware.RateLimitConfig;

RateLimitConfig config = RateLimitConfig.builder()
    .requestsPerMinute(100)
    .burst(20)
    .build();

Router router = Routers.create();
router.use(Middlewares.rateLimit(config));
```

### 2. 自定义中间件

#### 简单中间件

```java
import ltd.idcu.est.web.middleware.Middleware;
import ltd.idcu.est.web.middleware.MiddlewareChain;

Middleware myMiddleware = (req, res, chain) -> {
    System.out.println("请求�?);
    
    chain.proceed();
    
    System.out.println("响应�?);
};

Router router = Routers.create();
router.use(myMiddleware);
```

#### 认证中间�?```java
import ltd.idcu.est.web.middleware.Middleware;
import ltd.idcu.est.web.middleware.MiddlewareChain;

Middleware authMiddleware = (req, res, chain) -> {
    String token = req.header("Authorization");
    
    if (token == null || !validateToken(token)) {
        res.status(401).send("未授�?);
        return;
    }
    
    req.setAttribute("user", getUserFromToken(token));
    chain.proceed();
};

Router router = Routers.create();
router.use("/api/**", authMiddleware);
```

### 3. 中间件顺�?#### 执行顺序

```java
Router router = Routers.create();

router.use(loggingMiddleware);      // 1. 最先执�?router.use(corsMiddleware);         // 2.
router.use(authMiddleware);         // 3.
router.use(compressionMiddleware);  // 4.

router.get("/", handler);           // 最后执�?```

#### 条件中间�?```java
Router router = Routers.create();

router.use("/api/**", authMiddleware);
router.use("/admin/**", adminMiddleware);

router.get("/public", publicHandler);
```

---

## 进阶�?### 1. 中间件链控制

#### 中断中间件链

```java
Middleware earlyReturnMiddleware = (req, res, chain) -> {
    if (someCondition) {
        res.send("提前返回");
        return;
    }
    
    chain.proceed();
};
```

#### 错误处理中间�?```java
Middleware errorHandlerMiddleware = (req, res, chain) -> {
    try {
        chain.proceed();
    } catch (Exception e) {
        res.status(500).json(Map.of(
            "error", "服务器错�?,
            "message", e.getMessage()
        ));
    }
};

Router router = Routers.create();
router.use(errorHandlerMiddleware);
```

### 2. 中间件组�?#### 组合多个中间�?```java
import ltd.idcu.est.web.middleware.Middleware;
import ltd.idcu.est.web.middleware.MiddlewareChain;

Middleware combined = Middlewares.combine(
    loggingMiddleware,
    corsMiddleware,
    authMiddleware,
    compressionMiddleware
);

Router router = Routers.create();
router.use(combined);
```

### 3. 中间件优先级

#### 设置优先�?```java
import ltd.idcu.est.web.middleware.Middleware;

Middleware highPriority = Middlewares.withPriority(
    loggingMiddleware,
    100
);

Middleware lowPriority = Middlewares.withPriority(
    compressionMiddleware,
    1
);

Router router = Routers.create();
router.use(highPriority);
router.use(lowPriority);
```

---

## 最佳实�?### 1. 中间件顺�?```java
// �?推荐：合理的中间件顺�?router.use(loggingMiddleware);      // 1. 记录请求
router.use(corsMiddleware);         // 2. 处理 CORS
router.use(authMiddleware);         // 3. 认证
router.use(compressionMiddleware);  // 4. 压缩响应
router.use(errorHandlerMiddleware); // 5. 错误处理

// �?不推荐：错误的顺�?router.use(compressionMiddleware);
router.use(authMiddleware);
router.use(corsMiddleware);
router.use(loggingMiddleware);
```

### 2. 中间件粒�?```java
// �?推荐：单一职责的中间件
router.use(loggingMiddleware);
router.use(authMiddleware);
router.use(corsMiddleware);

// �?不推荐：大而全的中间件
router.use(giantMiddleware); // 什么都能做，难以维�?```

### 3. 条件使用

```java
// �?推荐：只在需要的路径使用中间�?router.use("/api/**", authMiddleware);
router.use("/admin/**", adminMiddleware);

// �?不推荐：所有路径都使用
router.use(authMiddleware);
```

---

## 模块结构

```
est-web-middleware/
├── est-web-middleware-api/    # 中间�?API
└── est-web-middleware-impl/   # 中间件实�?```

---

## 相关资源

- [est-web-router README](../est-web-router/README.md) - 路由文档
- [est-web-session README](../est-web-session/README.md) - 会话管理文档
- [EST Web Group README](../README.md) - Web 模块组文�?- [示例代码](../../../est-examples/est-examples-web/) - Web 示例代码

---

**文档版本**: 2.0  
**最后更�?*: 2026-03-08
