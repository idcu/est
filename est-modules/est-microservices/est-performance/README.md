# EST Performance 性能优化模块 - 小白从入门到精通

## 目录
1. [什么是 EST Performance？](#什么是-est-performance)
2. [快速入门：5分钟上手](#快速入门5分钟上手)
3. [基础篇](#基础篇)
4. [进阶篇](#进阶篇)
5. [高级篇](#高级篇)
6. [与其他模块集成](#与其他模块集成)
7. [最佳实践](#最佳实践)
8. [常见问题](#常见问题)
9. [下一步](#下一步)

---

## 什么是 EST Performance？

### 用大白话理解

EST Performance 就像是一个"性能优化专家"。想象一下你的应用跑得很慢，你不知道问题出在哪里：

**传统方式**：盲目优化，不知道哪里有问题，效果很差！

**EST Performance 方式**：帮你监控 GC、优化 HTTP 服务器、统计请求指标，让应用飞起来！
- GC 调优：监控垃圾回收，给出优化建议
- HTTP 优化：智能配置 HTTP 服务器参数
- 请求统计：记录请求次数、响应时间、成功率
- JVM 信息：获取当前 JVM 的详细信息

它支持多种功能：GC 调优、HTTP 优化、请求监控，想用哪个用哪个！

### 核心特点

- 🎯 **简单易用** - 几行代码就能开始性能优化
- 🚀 **实用有效** - 基于实际场景的优化建议
- 🔄 **实时监控** - 实时收集性能指标
- 📊 **详细统计** - 提供全面的性能数据
- 📈 **智能推荐** - 根据指标给出优化建议

---

## 快速入门：5分钟上手

### 第一步：引入依赖

在你的 Maven pom.xml 中添加：

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-performance-api</artifactId>
        <version>2.0.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-performance-impl</artifactId>
        <version>2.0.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### 第二步：你的第一个性能优化

```java
import ltd.idcu.est.performance.api.GCTuner;
import ltd.idcu.est.performance.api.GCMetrics;
import ltd.idcu.est.performance.api.GCRecommendation;
import ltd.idcu.est.performance.impl.DefaultGCTuner;

public class FirstPerformanceExample {
    public static void main(String[] args) {
        System.out.println("=== EST Performance 第一个示例 ===\n");
        
        GCTuner gcTuner = new DefaultGCTuner();
        
        System.out.println("JVM 信息:\n" + gcTuner.getJVMInfo());
        
        GCMetrics metrics = gcTuner.collectMetrics();
        System.out.println("\nGC 指标:\n" + metrics);
        
        GCRecommendation recommendation = gcTuner.getRecommendation(metrics);
        System.out.println("\n优化建议:\n" + recommendation);
        
        System.out.println("\n恭喜你！你已经成功使用 EST Performance 了！");
    }
}
```

运行这个程序，你会看到类似这样的输出：
```
=== EST Performance 第一个示例 ===

JVM 信息:
JVM Version: 17.0.1
Heap Size: 256 MB
...

GC 指标:
Collection Count: 10
Collection Time: 500ms
...

优化建议:
建议增加堆内存大小
...

恭喜你！你已经成功使用 EST Performance 了！
```

---

## 基础篇

### 1. 什么是 GCTuner？

GCTuner 就是一个"GC 调优器"接口，它的核心操作非常简单：

```java
public interface GCTuner {
    GCMetrics collectMetrics();          // 收集 GC 指标
    GCRecommendation getRecommendation(GCMetrics metrics); // 获取优化建议
    String getJVMInfo();                   // 获取 JVM 信息
}
```

### 2. 创建性能优化工具的几种方式

```java
import ltd.idcu.est.performance.api.GCTuner;
import ltd.idcu.est.performance.api.HttpServerOptimizer;
import ltd.idcu.est.performance.api.RequestMetrics;
import ltd.idcu.est.performance.impl.DefaultGCTuner;
import ltd.idcu.est.performance.impl.DefaultHttpServerOptimizer;
import ltd.idcu.est.performance.impl.DefaultRequestMetrics;

public class CreatePerformanceExample {
    public static void main(String[] args) {
        System.out.println("--- 方式一：GC 调优器 ---");
        GCTuner gcTuner = new DefaultGCTuner();
        System.out.println("GC 调优器创建成功");
        
        System.out.println("\n--- 方式二：HTTP 服务器优化器 ---");
        HttpServerOptimizer devOptimizer = DefaultHttpServerOptimizer.forDevelopment();
        HttpServerOptimizer prodOptimizer = DefaultHttpServerOptimizer.forProduction();
        System.out.println("HTTP 优化器创建成功");
        
        System.out.println("\n--- 方式三：请求指标统计 ---");
        RequestMetrics requestMetrics = new DefaultRequestMetrics();
        System.out.println("请求指标统计创建成功");
    }
}
```

### 3. 基本操作

```java
import ltd.idcu.est.performance.api.*;
import ltd.idcu.est.performance.impl.*;

import java.util.Map;

public class BasicOperations {
    public static void main(String[] args) {
        System.out.println("--- 1. GC 调优 ---");
        GCTuner gcTuner = new DefaultGCTuner();
        System.out.println("JVM 信息:\n" + gcTuner.getJVMInfo());
        GCMetrics metrics = gcTuner.collectMetrics();
        System.out.println("GC 指标: " + metrics);
        GCRecommendation recommendation = gcTuner.getRecommendation(metrics);
        System.out.println("优化建议: " + recommendation);
        
        System.out.println("\n--- 2. HTTP 服务器优化 ---");
        HttpServerOptimizer devOptimizer = DefaultHttpServerOptimizer.forDevelopment();
        System.out.println("开发环境配置: " + devOptimizer);
        HttpServerOptimizer prodOptimizer = DefaultHttpServerOptimizer.forProduction();
        System.out.println("生产环境配置: " + prodOptimizer);
        Map<String, Object> configMap = prodOptimizer.toMap();
        System.out.println("配置项数量: " + configMap.size());
        
        System.out.println("\n--- 3. 请求指标统计 ---");
        RequestMetrics requestMetrics = new DefaultRequestMetrics();
        requestMetrics.recordRequest("/api/users", 200, 50);
        requestMetrics.recordRequest("/api/orders", 200, 100);
        requestMetrics.recordRequest("/api/users", 500, 200);
        System.out.println("总请求数: " + requestMetrics.getTotalRequests());
        System.out.println("成功率: " + String.format("%.1f%%", requestMetrics.getSuccessRate() * 100));
        System.out.println("平均响应时间: " + requestMetrics.getAverageResponseTime() + "ms");
    }
}
```

---

## 进阶篇

### 1. HTTP 服务器优化配置

你可以根据环境选择不同的配置，也可以自定义：

```java
import ltd.idcu.est.performance.api.HttpServerOptimizer;
import ltd.idcu.est.performance.impl.DefaultHttpServerOptimizer;

public class HttpServerOptimizerExample {
    public static void main(String[] args) {
        System.out.println("--- HTTP 服务器优化示例 ---");
        
        System.out.println("1. 开发环境配置:");
        HttpServerOptimizer dev = DefaultHttpServerOptimizer.forDevelopment();
        System.out.println(dev);
        
        System.out.println("\n2. 生产环境配置:");
        HttpServerOptimizer prod = DefaultHttpServerOptimizer.forProduction();
        System.out.println(prod);
        
        System.out.println("\n3. 自定义配置:");
        HttpServerOptimizer custom = new DefaultHttpServerOptimizer()
            .setBacklog(200)
            .setThreadPoolSize(Runtime.getRuntime().availableProcessors() * 8)
            .setUseVirtualThreads(true)
            .setConnectionTimeout(120000)
            .setRequestTimeout(60000)
            .setMaxRequestSize(100 * 1024 * 1024)
            .setEnableCompression(true)
            .setEnableCaching(true)
            .setCacheMaxAge(14400);
        System.out.println(custom);
    }
}
```

### 2. 请求指标详细统计

```java
import ltd.idcu.est.performance.api.RequestMetrics;
import ltd.idcu.est.performance.impl.DefaultRequestMetrics;

import java.util.Map;

public class RequestMetricsExample {
    public static void main(String[] args) {
        System.out.println("--- 请求指标统计示例 ---");
        
        RequestMetrics metrics = new DefaultRequestMetrics();
        
        for (int i = 0; i < 100; i++) {
            String path = i % 3 == 0 ? "/api/users" : (i % 3 == 1 ? "/api/orders" : "/api/products");
            int status = i % 20 == 0 ? 500 : (i % 10 == 0 ? 404 : 200);
            long responseTime = (long) (Math.random() * 200 + 20);
            metrics.recordRequest(path, status, responseTime);
        }
        
        System.out.println("\n--- 概览 ---");
        System.out.println("总请求数: " + metrics.getTotalRequests());
        System.out.println("成功率: " + String.format("%.1f%%", metrics.getSuccessRate() * 100));
        System.out.println("平均响应时间: " + String.format("%.2fms", metrics.getAverageResponseTime()));
        System.out.println("最大响应时间: " + metrics.getMaxResponseTime() + "ms");
        System.out.println("最小响应时间: " + metrics.getMinResponseTime() + "ms");
        System.out.println("每秒请求数: " + String.format("%.2f", metrics.getRequestsPerSecond()));
        
        System.out.println("\n--- 状态码统计 ---");
        Map<Integer, Long> statusCodes = metrics.getStatusCodes();
        statusCodes.forEach((code, count) -> 
            System.out.println("  " + code + ": " + count));
        
        System.out.println("\n--- 路径统计 ---");
        Map<String, Long> pathCounts = metrics.getPathCounts();
        pathCounts.forEach((path, count) -> 
            System.out.println("  " + path + ": " + count));
    }
}
```

---

## 高级篇

### 1. GC 监控和优化建议

```java
import ltd.idcu.est.performance.api.GCTuner;
import ltd.idcu.est.performance.api.GCMetrics;
import ltd.idcu.est.performance.api.GCRecommendation;
import ltd.idcu.est.performance.impl.DefaultGCTuner;

public class GCMonitoringExample {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- GC 监控示例 ---");
        
        GCTuner gcTuner = new DefaultGCTuner();
        
        System.out.println("初始 GC 指标:");
        GCMetrics initial = gcTuner.collectMetrics();
        System.out.println(initial);
        
        System.out.println("\n模拟一些工作负载...");
        for (int i = 0; i < 10000; i++) {
            byte[] temp = new byte[1024];
        }
        Thread.sleep(100);
        
        System.out.println("\n更新后的 GC 指标:");
        GCMetrics updated = gcTuner.collectMetrics();
        System.out.println(updated);
        
        System.out.println("\n优化建议:");
        GCRecommendation recommendation = gcTuner.getRecommendation(updated);
        System.out.println(recommendation);
    }
}
```

---

## 与其他模块集成

EST Performance 和其他模块都是绝配！让我们看看它们如何配合使用：

### 场景：网关 + 性能监控

```java
import ltd.idcu.est.performance.api.RequestMetrics;
import ltd.idcu.est.performance.impl.DefaultRequestMetrics;
import ltd.idcu.est.gateway.api.ApiGateway;
import ltd.idcu.est.gateway.api.GatewayMiddleware;
import ltd.idcu.est.gateway.api.GatewayContext;
import ltd.idcu.est.gateway.impl.DefaultApiGateway;

public class GatewayIntegrationExample {
    public static void main(String[] args) {
        System.out.println("=== EST Performance + EST Gateway 集成示例 ===\n");
        
        ApiGateway gateway = new DefaultApiGateway();
        RequestMetrics requestMetrics = new DefaultRequestMetrics();
        
        gateway.addMiddleware(new GatewayMiddleware() {
            private long startTime;
            
            @Override
            public String getName() {
                return "performance-monitor";
            }
            
            @Override
            public void before(GatewayContext context) {
                startTime = System.currentTimeMillis();
            }
            
            @Override
            public void after(GatewayContext context) {
                long duration = System.currentTimeMillis() - startTime;
                int statusCode = context.getResponseStatusCode();
                String path = context.getRequestPath();
                requestMetrics.recordRequest(path, statusCode, duration);
                System.out.println("[监控] " + path + " - " + statusCode + " - " + duration + "ms");
            }
        });
        
        System.out.println("性能监控中间件已添加到网关");
    }
}
```

---

## 最佳实践

### 1. 选择合适的 HTTP 配置

```java
// ✓ 推荐：根据环境选择配置
HttpServerOptimizer optimizer;
if (isDevelopment) {
    optimizer = DefaultHttpServerOptimizer.forDevelopment();
} else {
    optimizer = DefaultHttpServerOptimizer.forProduction();
}

// ✓ 推荐：自定义优化配置
HttpServerOptimizer custom = new DefaultHttpServerOptimizer()
    .setThreadPoolSize(Runtime.getRuntime().availableProcessors() * 8)
    .setUseVirtualThreads(true);
```

### 2. 定期监控 GC

```java
// ✓ 推荐：定期收集 GC 指标
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
scheduler.scheduleAtFixedRate(() -> {
    GCMetrics metrics = gcTuner.collectMetrics();
    if (metrics.getCollectionTime() > tooHigh) {
        GCRecommendation recommendation = gcTuner.getRecommendation(metrics);
        System.out.println("需要优化: " + recommendation);
    }
}, 0, 1, TimeUnit.MINUTES);
```

### 3. 分析请求指标

```java
// ✓ 推荐：定期分析请求指标
if (requestMetrics.getSuccessRate() < 0.9) {
    System.out.println("成功率太低，需要检查");
    Map<Integer, Long> statusCodes = requestMetrics.getStatusCodes();
    statusCodes.forEach((code, count) -> {
        if (code >= 400) {
            System.out.println("状态码 " + code + " 出现 " + count + " 次");
        }
    });
}
```

---

## 常见问题

### Q: GC 优化建议总是准确吗？

A: 建议是基于一般情况给出的，具体还需要结合你的应用场景来调整。

### Q: 虚拟线程一定更好吗？

A: 不一定！虚拟线程适合 I/O 密集型应用，CPU 密集型应用还是用平台线程更好。

### Q: 请求指标会占用很多内存吗？

A: DefaultRequestMetrics 使用合理的数据结构，内存占用可控。

---

## 下一步

- 学习 [est-gateway](../est-gateway/README.md) 进行网关路由
- 查看 [est-config](../est-config/) 了解配置管理
- 尝试自定义性能优化策略
- 阅读 [API 文档](../../docs/api/performance/) 了解更多细节

---

**文档版本**: 2.0  
**最后更新**: 2026-03-06  
**维护者**: EST 架构团队
