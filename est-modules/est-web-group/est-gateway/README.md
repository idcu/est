# EST Gateway 网关模块 - 小白从入门到精通

## 目录
1. [什么是 EST Gateway？](#什么是-est-gateway)
2. [快速入门：5分钟上手](#快速入门5分钟上手)
3. [基础篇](#基础篇)
4. [进阶篇](#进阶篇)
5. [高级篇](#高级篇)
6. [与其他模块集成](#与其他模块集成)
7. [最佳实践](#最佳实践)
8. [常见问题](#常见问题)
9. [下一步](#下一步)

---

## 什么是 EST Gateway？

### 用大白话理解

EST Gateway 就像是一个"智能交通指挥中心"。想象一下你在运营一个大型购物中心，里面有很多店铺（服务），顾客（请求）需要去不同的店铺：

**传统方式**：顾客必须知道每家店铺的具体位置，自己找路过去，很麻烦！

**EST Gateway 方式**：你有一个统一的入口，告诉指挥中心你想去哪里，它自动帮你导航过去！
- 支持路由规则：把 `/api/users` 转发到用户服务
- 支持中间件：可以在请求前后做处理（如日志、CORS）
- 动态配置：随时添加新的路由规则

它支持多种中间件：日志、CORS，你也可以自己写！

### 核心特点

- 🎯 **简单易用** - 几行代码就能创建和启动网关
- 🚀 **高性能** - 基于 HTTP 服务器，转发速度快
- 🔄 **中间件支持** - 灵活的请求处理链
- 📊 **路由管理** - 支持动态路由配置
- 🚦 **限流支持** - 内置令牌桶限流器
- 💾 **持久化支持** - 限流器状态支持JSON持久化
- 📈 **可扩展** - 轻松添加自定义中间件

---

## 快速入门：5分钟上手

### 第一步：引入依赖

在你的 Maven pom.xml 中添加：

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-gateway-api</artifactId>
        <version>2.0.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-gateway-impl</artifactId>
        <version>2.0.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### 第二步：你的第一个网关

```java
import ltd.idcu.est.gateway.api.ApiGateway;
import ltd.idcu.est.gateway.api.Route;
import ltd.idcu.est.gateway.impl.DefaultApiGateway;
import ltd.idcu.est.gateway.impl.DefaultRoute;

public class FirstGatewayExample {
    public static void main(String[] args) {
        System.out.println("=== EST Gateway 第一个示例 ===\n");
        
        ApiGateway gateway = new DefaultApiGateway();
        
        Route route = new DefaultRoute("/api/users", "http://localhost:8081/users");
        gateway.getRouter().addRoute(route);
        
        gateway.start(8080);
        System.out.println("网关已启动，监听端口 8080");
        System.out.println("访问 http://localhost:8080/api/users 将转发到 http://localhost:8081/users");
        
        System.out.println("\n恭喜你！你已经成功使用 EST Gateway 了！");
    }
}
```

运行这个程序，你会看到：
```
=== EST Gateway 第一个示例 ===

网关已启动，监听端口 8080
访问 http://localhost:8080/api/users 将转发到 http://localhost:8081/users

恭喜你！你已经成功使用 EST Gateway 了！
```

---

## 基础篇

### 1. 什么是 ApiGateway？

ApiGateway 就是一个"统一入口"接口，它的核心操作非常简单：

```java
public interface ApiGateway {
    void start(int port);                    // 启动网关
    void stop();                              // 停止网关
    GatewayRouter getRouter();                // 获取路由管理器
    void addMiddleware(GatewayMiddleware middleware);  // 添加中间件
    void removeMiddleware(String name);       // 移除中间件
}
```

### 2. 创建网关的几种方式

```java
import ltd.idcu.est.gateway.api.ApiGateway;
import ltd.idcu.est.gateway.api.Route;
import ltd.idcu.est.gateway.impl.DefaultApiGateway;
import ltd.idcu.est.gateway.impl.DefaultRoute;

public class CreateGatewayExample {
    public static void main(String[] args) {
        System.out.println("--- 方式一：默认网关 ---");
        ApiGateway gateway1 = new DefaultApiGateway();
        System.out.println("默认网关创建成功");
        
        System.out.println("\n--- 方式二：添加路由 ---");
        ApiGateway gateway2 = new DefaultApiGateway();
        Route route = new DefaultRoute("/api", "http://localhost:8081");
        gateway2.getRouter().addRoute(route);
        System.out.println("带路由的网关创建成功");
    }
}
```

### 3. 基本操作

```java
import ltd.idcu.est.gateway.api.ApiGateway;
import ltd.idcu.est.gateway.api.GatewayRouter;
import ltd.idcu.est.gateway.api.Route;
import ltd.idcu.est.gateway.impl.DefaultApiGateway;
import ltd.idcu.est.gateway.impl.DefaultRoute;

public class BasicOperations {
    public static void main(String[] args) {
        ApiGateway gateway = new DefaultApiGateway();
        GatewayRouter router = gateway.getRouter();
        
        System.out.println("--- 1. 添加路由 ---");
        Route route1 = new DefaultRoute("/api/users", "http://localhost:8081/users");
        Route route2 = new DefaultRoute("/api/orders", "http://localhost:8082/orders");
        router.addRoute(route1);
        router.addRoute(route2);
        System.out.println("添加了 2 个路由");
        
        System.out.println("\n--- 2. 查找路由 ---");
        Route found = router.findRoute("/api/users/123");
        System.out.println("找到路由: " + found);
        
        System.out.println("\n--- 3. 删除路由 ---");
        router.removeRoute("/api/orders");
        System.out.println("删除了订单路由");
        
        System.out.println("\n--- 4. 获取所有路由 ---");
        System.out.println("当前路由数: " + router.getRoutes().size());
    }
}
```

---

## 进阶篇

### 1. 中间件（Middleware）

你可以添加中间件来处理请求：

```java
import ltd.idcu.est.gateway.api.ApiGateway;
import ltd.idcu.est.gateway.api.GatewayMiddleware;
import ltd.idcu.est.gateway.api.GatewayContext;
import ltd.idcu.est.gateway.impl.DefaultApiGateway;
import ltd.idcu.est.gateway.impl.middleware.LoggingMiddleware;
import ltd.idcu.est.gateway.impl.middleware.CorsMiddleware;

public class MiddlewareExample {
    public static void main(String[] args) {
        System.out.println("--- 中间件示例 ---");
        
        ApiGateway gateway = new DefaultApiGateway();
        
        gateway.addMiddleware(new LoggingMiddleware());
        gateway.addMiddleware(new CorsMiddleware());
        
        System.out.println("添加了日志和 CORS 中间件");
    }
}
```

### 2. 自定义中间件

```java
import ltd.idcu.est.gateway.api.GatewayMiddleware;
import ltd.idcu.est.gateway.api.GatewayContext;

public class CustomMiddleware implements GatewayMiddleware {
    @Override
    public String getName() {
        return "custom";
    }
    
    @Override
    public void before(GatewayContext context) {
        System.out.println("[Custom] 请求前处理: " + context.getRequestPath());
    }
    
    @Override
    public void after(GatewayContext context) {
        System.out.println("[Custom] 请求后处理，状态码: " + context.getResponseStatusCode());
    }
}
```

---

## 高级篇

### 1. 动态路由配置

```java
import ltd.idcu.est.gateway.api.ApiGateway;
import ltd.idcu.est.gateway.api.GatewayRouter;
import ltd.idcu.est.gateway.api.Route;
import ltd.idcu.est.gateway.impl.DefaultApiGateway;
import ltd.idcu.est.gateway.impl.DefaultRoute;

public class DynamicRoutingExample {
    public static void main(String[] args) {
        System.out.println("--- 动态路由配置示例 ---");
        
        ApiGateway gateway = new DefaultApiGateway();
        GatewayRouter router = gateway.getRouter();
        
        String[][] routes = {
            {"/api/users", "http://localhost:8081"},
            {"/api/orders", "http://localhost:8082"},
            {"/api/products", "http://localhost:8083"}
        };
        
        for (String[] r : routes) {
            Route route = new DefaultRoute(r[0], r[1]);
            router.addRoute(route);
            System.out.println("添加路由: " + r[0] + " -> " + r[1]);
        }
        
        System.out.println("\n当前路由配置完成");
    }
}
```

---

## 高级篇

### 1. 令牌桶限流器

Gateway 内置令牌桶限流器，支持限流状态持久化：

```java
import ltd.idcu.est.gateway.api.RateLimiter;
import ltd.idcu.est.gateway.api.RateLimiterRegistry;
import ltd.idcu.est.gateway.impl.TokenBucketRateLimiter;
import ltd.idcu.est.gateway.impl.DefaultRateLimiterRegistry;

import java.io.File;

public class RateLimiterExample {
    public static void main(String[] args) {
        System.out.println("--- 限流器示例 ---");
        
        File dataFile = new File("rate-limiters.json");
        RateLimiterRegistry registry = new DefaultRateLimiterRegistry(dataFile);
        
        RateLimiter limiter = new TokenBucketRateLimiter("api-gateway", 100, 10);
        registry.register(limiter);
        
        for (int i = 0; i < 5; i++) {
            boolean allowed = limiter.tryAcquire();
            System.out.println("请求 " + (i + 1) + ": " + (allowed ? "允许" : "拒绝"));
        }
        
        System.out.println("限流器状态已自动保存");
    }
}
```

---

## 与其他模块集成

EST Gateway 和 est-discovery 是绝配！让我们看看它们如何配合使用：

### 场景：服务发现 + 网关路由

```java
import ltd.idcu.est.discovery.api.ServiceRegistry;
import ltd.idcu.est.discovery.api.ServiceInstance;
import ltd.idcu.est.discovery.api.LoadBalancer;
import ltd.idcu.est.discovery.impl.DefaultServiceRegistry;
import ltd.idcu.est.discovery.impl.RoundRobinLoadBalancer;
import ltd.idcu.est.gateway.api.ApiGateway;
import ltd.idcu.est.gateway.api.Route;
import ltd.idcu.est.gateway.impl.DefaultApiGateway;
import ltd.idcu.est.gateway.impl.DefaultRoute;

import java.util.List;

public class DiscoveryIntegrationExample {
    public static void main(String[] args) {
        System.out.println("=== EST Gateway + EST Discovery 集成示例 ===\n");
        
        ServiceRegistry registry = new DefaultServiceRegistry();
        LoadBalancer loadBalancer = new RoundRobinLoadBalancer();
        
        ServiceInstance instance1 = new ServiceInstance("user-service", "instance-1", "localhost", 8081);
        ServiceInstance instance2 = new ServiceInstance("user-service", "instance-2", "localhost", 8082);
        registry.register(instance1);
        registry.register(instance2);
        
        ApiGateway gateway = new DefaultApiGateway();
        
        List<ServiceInstance> instances = registry.getInstances("user-service");
        ServiceInstance selected = loadBalancer.select(instances);
        
        Route route = new DefaultRoute("/api/users", 
            "http://" + selected.getHost() + ":" + selected.getPort());
        gateway.getRouter().addRoute(route);
        
        System.out.println("路由已配置，指向: " + selected);
    }
}
```

---

## 最佳实践

### 1. 合理规划路由前缀

```java
// ✓ 推荐：使用统一的前缀分组
Route userRoute = new DefaultRoute("/api/users", "http://user-service");
Route orderRoute = new DefaultRoute("/api/orders", "http://order-service");

// ✗ 不推荐：路由混乱
Route route1 = new DefaultRoute("/users", "http://user-service");
Route route2 = new DefaultRoute("/api-orders", "http://order-service");
```

### 2. 使用中间件链

```java
// ✓ 推荐：按顺序添加中间件
gateway.addMiddleware(new LoggingMiddleware());
gateway.addMiddleware(new CorsMiddleware());
gateway.addMiddleware(customMiddleware);
```

### 3. 监控路由状态

```java
GatewayRouter router = gateway.getRouter();
System.out.println("当前路由数: " + router.getRoutes().size());
```

---

## 常见问题

### Q: 网关支持 HTTPS 吗？

A: 当前版本默认使用 HTTP，后续版本会支持 HTTPS 配置。

### Q: 路由匹配规则是什么？

A: 使用前缀匹配，`/api/users` 会匹配 `/api/users/123` 等路径。

### Q: 可以同时运行多个网关吗？

A: 可以，每个网关监听不同的端口即可。

---

## 下一步

- 学习 [est-discovery](../est-discovery/README.md) 进行服务发现
- 查看 [est-circuitbreaker](../est-circuitbreaker/) 了解熔断保护
- 尝试自定义中间件实现
- 阅读 [API 文档](../../docs/api/gateway/) 了解更多细节

---

**文档版本**: 2.0  
**最后更新**: 2026-03-06  
**维护者**: EST 架构团队
