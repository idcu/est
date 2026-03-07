# EST Tracing 分布式追踪模块 - 小白从入门到精通

## 目录
1. [什么是 EST Tracing？](#什么是-est-tracing)
2. [快速入门：5分钟上手](#快速入门5分钟上手)
3. [基础篇](#基础篇)
4. [进阶篇](#进阶篇)
5. [高级篇](#高级篇)
6. [与其他模块集成](#与其他模块集成)
7. [最佳实践](#最佳实践)
8. [常见问题](#常见问题)
9. [下一步](#下一步)

---

## 什么是 EST Tracing？

### 用大白话理解

EST Tracing 就像是一个"请求追踪器"。想象一下你在送快递，每个快递都有一个追踪号，你可以随时查看快递的位置和状态：

**传统方式**：请求在多个服务间流转，出了问题根本不知道在哪一步卡住！

**EST Tracing 方式**：每个请求都有一个唯一的追踪ID，记录请求经过的每个服务和每个步骤！
- 分布式追踪：跨服务追踪请求链路
- Span管理：记录每个操作的开始和结束时间
- 标签支持：为追踪添加自定义标签
- 多种导出：支持日志、文件、OpenTelemetry导出

它支持多种导出器：日志、文件、OpenTelemetry，想用哪个用哪个！

### 核心特点

- 🎯 **简单易用** - 几行代码就能创建和使用追踪器
- 🚀 **分布式追踪** - 支持跨服务的请求链路追踪
- 🔄 **Span管理** - 记录每个操作的开始和结束时间
- 📊 **标签支持** - 为追踪添加自定义标签
- 💾 **持久化支持** - 支持JSON格式的追踪数据持久化
- 📈 **可扩展** - 轻松添加自定义导出器

---

## 快速入门：5分钟上手

### 第一步：引入依赖

在你的 Maven pom.xml 中添加：

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-tracing-api</artifactId>
        <version>2.0.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-tracing-impl</artifactId>
        <version>2.0.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### 第二步：你的第一个追踪

```java
import ltd.idcu.est.tracing.api.Tracer;
import ltd.idcu.est.tracing.api.TraceContext;
import ltd.idcu.est.tracing.impl.DefaultTracer;

public class FirstTracingExample {
    public static void main(String[] args) {
        System.out.println("=== EST Tracing 第一个示例 ===\n");
        
        Tracer tracer = new DefaultTracer("my-service");
        
        TraceContext span = tracer.startSpan("process-request");
        System.out.println("开始追踪: " + span.getSpanId());
        
        try {
            Thread.sleep(100);
            System.out.println("处理请求...");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            tracer.endSpan(span);
            System.out.println("结束追踪，耗时: " + (span.getEndTime() - span.getStartTime()) + "ms");
        }
        
        System.out.println("\n恭喜你！你已经成功使用 EST Tracing 了！");
    }
}
```

运行这个程序，你会看到：
```
=== EST Tracing 第一个示例 ===

开始追踪: [span-id]
处理请求...
结束追踪，耗时: 100ms

恭喜你！你已经成功使用 EST Tracing 了！
```

---

## 基础篇

### 1. 什么是 Tracer？

Tracer 就是一个"追踪器"接口，它的核心操作非常简单：

```java
public interface Tracer {
    String getServiceName();                              // 获取服务名称
    TraceContext startSpan(String operationName);        // 开始一个新的Span
    TraceContext startSpan(String operationName, TraceContext parent); // 开始一个子Span
    void endSpan(TraceContext context);                  // 结束Span
    void setSpanExporter(SpanExporter exporter);        // 设置Span导出器
}
```

### 2. 创建追踪器的几种方式

```java
import ltd.idcu.est.tracing.api.Tracer;
import ltd.idcu.est.tracing.impl.DefaultTracer;
import ltd.idcu.est.tracing.impl.LoggingSpanExporter;

public class CreateTracerExample {
    public static void main(String[] args) {
        System.out.println("--- 方式一：默认追踪器 ---");
        Tracer tracer1 = new DefaultTracer("service-a");
        System.out.println("默认追踪器创建成功");
        
        System.out.println("\n--- 方式二：带导出器的追踪器 ---");
        Tracer tracer2 = new DefaultTracer("service-b");
        tracer2.setSpanExporter(new LoggingSpanExporter());
        System.out.println("带日志导出器的追踪器创建成功");
    }
}
```

### 3. 基本操作

```java
import ltd.idcu.est.tracing.api.Tracer;
import ltd.idcu.est.tracing.api.TraceContext;
import ltd.idcu.est.tracing.impl.DefaultTracer;

public class BasicOperations {
    public static void main(String[] args) {
        Tracer tracer = new DefaultTracer("user-service");
        
        System.out.println("--- 1. 开始一个Span ---");
        TraceContext span = tracer.startSpan("login");
        System.out.println("TraceId: " + span.getTraceId());
        System.out.println("SpanId: " + span.getSpanId());
        
        System.out.println("\n--- 2. 添加标签 ---");
        span.setTags(java.util.Arrays.asList("user-id=123", "auth-method=password"));
        System.out.println("标签: " + span.getTags());
        
        System.out.println("\n--- 3. 结束Span ---");
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            tracer.endSpan(span);
            System.out.println("Span结束，耗时: " + (span.getEndTime() - span.getStartTime()) + "ms");
        }
    }
}
```

---

## 进阶篇

### 1. 嵌套Span（子Span）

你可以创建嵌套的Span来表示操作的层次结构：

```java
import ltd.idcu.est.tracing.api.Tracer;
import ltd.idcu.est.tracing.api.TraceContext;
import ltd.idcu.est.tracing.impl.DefaultTracer;

public class NestedSpanExample {
    public static void main(String[] args) {
        System.out.println("--- 嵌套Span示例 ---");
        
        Tracer tracer = new DefaultTracer("order-service");
        
        TraceContext parentSpan = tracer.startSpan("create-order");
        System.out.println("父Span: " + parentSpan.getSpanId());
        
        try {
            TraceContext childSpan1 = tracer.startSpan("validate-order", parentSpan);
            System.out.println("子Span1: " + childSpan1.getSpanId());
            Thread.sleep(30);
            tracer.endSpan(childSpan1);
            
            TraceContext childSpan2 = tracer.startSpan("save-order", parentSpan);
            System.out.println("子Span2: " + childSpan2.getSpanId());
            Thread.sleep(50);
            tracer.endSpan(childSpan2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            tracer.endSpan(parentSpan);
            System.out.println("父Span结束");
        }
    }
}
```

### 2. Span导出器

EST Tracing 支持多种Span导出器：

```java
import ltd.idcu.est.tracing.api.Tracer;
import ltd.idcu.est.tracing.api.TraceContext;
import ltd.idcu.est.tracing.impl.DefaultTracer;
import ltd.idcu.est.tracing.impl.LoggingSpanExporter;
import ltd.idcu.est.tracing.impl.FileSpanExporter;

import java.io.File;

public class ExporterExample {
    public static void main(String[] args) {
        System.out.println("--- Span导出器示例 ---");
        
        Tracer tracer1 = new DefaultTracer("service-a");
        tracer1.setSpanExporter(new LoggingSpanExporter());
        
        TraceContext span1 = tracer1.startSpan("operation-1");
        tracer1.endSpan(span1);
        System.out.println("日志导出器使用完成");
        
        File dataFile = new File("traces.json");
        Tracer tracer2 = new DefaultTracer("service-b");
        tracer2.setSpanExporter(new FileSpanExporter(dataFile));
        
        TraceContext span2 = tracer2.startSpan("operation-2");
        tracer2.endSpan(span2);
        System.out.println("文件导出器使用完成，数据已保存到: " + dataFile.getAbsolutePath());
    }
}
```

---

## 高级篇

### 1. 追踪器注册表（TracerRegistry）

使用TracerRegistry管理多个追踪器：

```java
import ltd.idcu.est.tracing.api.Tracer;
import ltd.idcu.est.tracing.api.TracerRegistry;
import ltd.idcu.est.tracing.impl.DefaultTracer;
import ltd.idcu.est.tracing.impl.DefaultTracerRegistry;

public class RegistryExample {
    public static void main(String[] args) {
        System.out.println("--- 追踪器注册表示例 ---");
        
        TracerRegistry registry = new DefaultTracerRegistry();
        
        Tracer tracer1 = new DefaultTracer("user-service");
        Tracer tracer2 = new DefaultTracer("order-service");
        
        registry.register(tracer1);
        registry.register(tracer2);
        
        System.out.println("注册了 2 个追踪器");
        System.out.println("所有追踪器: " + registry.getAllTracers());
        
        Tracer found = registry.getTracer("user-service");
        System.out.println("找到 user-service: " + (found != null));
    }
}
```

### 2. 文件Span导出器

FileSpanExporter 支持异步批量保存追踪数据：

```java
import ltd.idcu.est.tracing.api.Tracer;
import ltd.idcu.est.tracing.api.TraceContext;
import ltd.idcu.est.tracing.impl.DefaultTracer;
import ltd.idcu.est.tracing.impl.FileSpanExporter;

import java.io.File;

public class FileExporterExample {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- 文件导出器示例 ---");
        
        File dataFile = new File("traces-batch.json");
        FileSpanExporter exporter = new FileSpanExporter(dataFile, 10, 5000);
        
        Tracer tracer = new DefaultTracer("batch-service");
        tracer.setSpanExporter(exporter);
        
        for (int i = 0; i < 15; i++) {
            TraceContext span = tracer.startSpan("batch-operation-" + i);
            Thread.sleep(10);
            tracer.endSpan(span);
        }
        
        System.out.println("已创建15个Span，批量导出中...");
        Thread.sleep(6000);
        
        exporter.shutdown();
        System.out.println("导出完成");
    }
}
```

---

## 与其他模块集成

EST Tracing 和 est-gateway 是绝配！让我们看看它们如何配合使用：

### 场景：网关 + 分布式追踪

```java
import ltd.idcu.est.tracing.api.Tracer;
import ltd.idcu.est.tracing.api.TraceContext;
import ltd.idcu.est.tracing.impl.DefaultTracer;
import ltd.idcu.est.tracing.impl.LoggingSpanExporter;
import ltd.idcu.est.gateway.api.ApiGateway;
import ltd.idcu.est.gateway.api.GatewayMiddleware;
import ltd.idcu.est.gateway.api.GatewayContext;
import ltd.idcu.est.gateway.impl.DefaultApiGateway;

public class GatewayIntegrationExample {
    public static void main(String[] args) {
        System.out.println("=== EST Tracing + EST Gateway 集成示例 ===\n");
        
        ApiGateway gateway = new DefaultApiGateway();
        
        Tracer tracer = new DefaultTracer("api-gateway");
        tracer.setSpanExporter(new LoggingSpanExporter());
        
        gateway.addMiddleware(new GatewayMiddleware() {
            @Override
            public String getName() {
                return "tracing";
            }
            
            @Override
            public void before(GatewayContext context) {
                TraceContext span = tracer.startSpan("gateway-request");
                context.setAttribute("trace-span", span);
                System.out.println("开始追踪请求: " + span.getTraceId());
            }
            
            @Override
            public void after(GatewayContext context) {
                TraceContext span = (TraceContext) context.getAttribute("trace-span");
                if (span != null) {
                    tracer.endSpan(span);
                    System.out.println("结束追踪请求");
                }
            }
        });
        
        System.out.println("追踪中间件已添加到网关");
    }
}
```

---

## 最佳实践

### 1. 合理命名操作

```java
// ✓ 推荐：使用有意义的操作名
TraceContext span = tracer.startSpan("user-login");

// ✗ 不推荐：操作名太随意
TraceContext badSpan = tracer.startSpan("op1");
```

### 2. 添加关键标签

```java
// ✓ 推荐：添加关键信息标签
span.setTags(java.util.Arrays.asList("user-id=123", "request-id=abc"));

// ✗ 不推荐：不添加任何标签
// span没有标签
```

### 3. 监控追踪性能

```java
// ✓ 推荐：注意Span的创建和结束
TraceContext span = tracer.startSpan("operation");
try {
    // 执行操作
} finally {
    tracer.endSpan(span);
}
```

---

## 常见问题

### Q: 追踪数据会持久化吗？

A: 是的！可以使用 FileSpanExporter 将追踪数据保存到 JSON 文件。

### Q: 支持跨服务追踪吗？

A: 支持！通过传递 TraceContext 可以实现跨服务的分布式追踪。

### Q: 如何导出追踪数据？

A: 可以使用 LoggingSpanExporter、FileSpanExporter 或 OpenTelemetrySpanExporter。

---

## 下一步

- 学习 [est-gateway](../est-gateway/README.md) 进行网关路由
- 查看 [est-discovery](../est-discovery/) 了解服务发现
- 尝试自定义 SpanExporter
- 阅读 [API 文档](../../docs/api/tracing/) 了解更多细节

---

**文档版本**: 2.0  
**最后更新**: 2026-03-06  
**维护者**: EST 架构团队
