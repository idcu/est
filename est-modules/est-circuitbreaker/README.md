# EST CircuitBreaker 熔断器模块 - 小白从入门到精通

## 目录
1. [什么是 EST CircuitBreaker？](#什么是-est-circuitbreaker)
2. [快速入门：5分钟上手](#快速入门5分钟上手)
3. [基础篇](#基础篇)
4. [进阶篇](#进阶篇)
5. [高级篇](#高级篇)
6. [与其他模块集成](#与其他模块集成)
7. [最佳实践](#最佳实践)
8. [常见问题](#常见问题)
9. [下一步](#下一步)

---

## 什么是 EST CircuitBreaker？

### 用大白话理解

EST CircuitBreaker 就像是一个"电路保护器"。想象一下你家里的电路，如果电器太多或者有故障，保险丝会自动断开，防止更严重的问题：

**传统方式**：服务一直调用失败的接口，导致整个系统变慢甚至崩溃！

**EST CircuitBreaker 方式**：当失败次数达到阈值时，自动"跳闸"，停止调用，给系统恢复时间！
- 熔断状态：关闭、开启、半开三种状态
- 自动恢复：一段时间后尝试恢复
- 失败统计：记录失败次数和成功率
- 可配置：灵活设置各种阈值

它支持自定义配置：失败率、超时时间、恢复时间，想怎么设就怎么设！

### 核心特点

- 🎯 **简单易用** - 几行代码就能创建和使用熔断器
- 🚀 **保护系统** - 防止级联失败
- 🔄 **自动恢复** - 支持自动检测和恢复
- 📊 **状态监控** - 提供详细的指标统计
- 💾 **持久化支持** - 支持JSON格式的熔断器状态持久化
- 🔄 **自动保存** - 状态变更时自动保存到文件
- 📈 **可配置** - 灵活的参数配置

---

## 快速入门：5分钟上手

### 第一步：引入依赖

在你的 Maven pom.xml 中添加：

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-circuitbreaker-api</artifactId>
        <version>2.0.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-circuitbreaker-impl</artifactId>
        <version>2.0.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### 第二步：你的第一个熔断器

```java
import ltd.idcu.est.circuitbreaker.api.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerConfig;
import ltd.idcu.est.circuitbreaker.impl.DefaultCircuitBreaker;

public class FirstCircuitBreakerExample {
    public static void main(String[] args) throws Exception {
        System.out.println("=== EST CircuitBreaker 第一个示例 ===\n");
        
        CircuitBreakerConfig config = CircuitBreakerConfig.builder()
            .failureThreshold(5)
            .timeout(1000)
            .waitDuration(5000)
            .build();
        
        CircuitBreaker circuitBreaker = new DefaultCircuitBreaker("my-service", config);
        
        String result = circuitBreaker.execute(() -> "Hello, CircuitBreaker!");
        System.out.println("执行结果: " + result);
        System.out.println("当前状态: " + circuitBreaker.getState());
        
        System.out.println("\n恭喜你！你已经成功使用 EST CircuitBreaker 了！");
    }
}
```

运行这个程序，你会看到：
```
=== EST CircuitBreaker 第一个示例 ===

执行结果: Hello, CircuitBreaker!
当前状态: CLOSED

恭喜你！你已经成功使用 EST CircuitBreaker 了！
```

---

## 基础篇

### 1. 什么是 CircuitBreaker？

CircuitBreaker 就是一个"熔断器"接口，它的核心操作非常简单：

```java
public interface CircuitBreaker {
    String getName();                                      // 获取名称
    CircuitState getState();                               // 获取状态
    <T> T execute(Supplier<T> supplier) throws Exception; // 执行带返回值的操作
    void execute(Runnable runnable) throws Exception;     // 执行无返回值的操作
    void reset();                                           // 重置熔断器
    CircuitBreakerMetrics getMetrics();                    // 获取指标
}
```

### 2. 创建熔断器的几种方式

```java
import ltd.idcu.est.circuitbreaker.api.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerConfig;
import ltd.idcu.est.circuitbreaker.impl.DefaultCircuitBreaker;

public class CreateCircuitBreakerExample {
    public static void main(String[] args) {
        System.out.println("--- 方式一：默认配置 ---");
        CircuitBreakerConfig config1 = CircuitBreakerConfig.builder().build();
        CircuitBreaker cb1 = new DefaultCircuitBreaker("service1", config1);
        System.out.println("默认熔断器创建成功");
        
        System.out.println("\n--- 方式二：自定义配置 ---");
        CircuitBreakerConfig config2 = CircuitBreakerConfig.builder()
            .failureThreshold(10)
            .timeout(2000)
            .waitDuration(10000)
            .build();
        CircuitBreaker cb2 = new DefaultCircuitBreaker("service2", config2);
        System.out.println("自定义熔断器创建成功");
    }
}
```

### 3. 基本操作

```java
import ltd.idcu.est.circuitbreaker.api.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerConfig;
import ltd.idcu.est.circuitbreaker.api.CircuitState;
import ltd.idcu.est.circuitbreaker.impl.DefaultCircuitBreaker;

public class BasicOperations {
    public static void main(String[] args) throws Exception {
        CircuitBreakerConfig config = CircuitBreakerConfig.builder()
            .failureThreshold(3)
            .build();
        
        CircuitBreaker circuitBreaker = new DefaultCircuitBreaker("test-service", config);
        
        System.out.println("--- 1. 执行成功操作 ---");
        String result = circuitBreaker.execute(() -> "Success!");
        System.out.println("结果: " + result);
        System.out.println("状态: " + circuitBreaker.getState());
        
        System.out.println("\n--- 2. 查看状态 ---");
        CircuitState state = circuitBreaker.getState();
        System.out.println("熔断器名称: " + circuitBreaker.getName());
        System.out.println("当前状态: " + state);
        
        System.out.println("\n--- 3. 查看指标 ---");
        System.out.println("指标: " + circuitBreaker.getMetrics());
        
        System.out.println("\n--- 4. 重置熔断器 ---");
        circuitBreaker.reset();
        System.out.println("熔断器已重置");
    }
}
```

---

## 进阶篇

### 1. 熔断状态（CircuitState）

熔断器有三种状态：

```java
import ltd.idcu.est.circuitbreaker.api.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerConfig;
import ltd.idcu.est.circuitbreaker.api.CircuitState;
import ltd.idcu.est.circuitbreaker.impl.DefaultCircuitBreaker;

public class CircuitStateExample {
    public static void main(String[] args) throws Exception {
        System.out.println("--- 熔断状态示例 ---");
        
        CircuitBreakerConfig config = CircuitBreakerConfig.builder()
            .failureThreshold(2)
            .waitDuration(1000)
            .build();
        
        CircuitBreaker circuitBreaker = new DefaultCircuitBreaker("test-service", config);
        
        System.out.println("初始状态: " + circuitBreaker.getState());
        
        try {
            circuitBreaker.execute(() -> {
                throw new RuntimeException("失败");
            });
        } catch (Exception e) {
            System.out.println("第一次失败");
        }
        
        try {
            circuitBreaker.execute(() -> {
                throw new RuntimeException("失败");
            });
        } catch (Exception e) {
            System.out.println("第二次失败，熔断器开启");
        }
        
        System.out.println("当前状态: " + circuitBreaker.getState());
    }
}
```

### 2. 熔断配置（CircuitBreakerConfig）

```java
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerConfig;

public class ConfigExample {
    public static void main(String[] args) {
        System.out.println("--- 熔断配置示例 ---");
        
        CircuitBreakerConfig config = CircuitBreakerConfig.builder()
            .failureThreshold(5)           // 失败阈值
            .timeout(1000)                  // 超时时间（毫秒）
            .waitDuration(5000)             // 等待恢复时间（毫秒）
            .successThreshold(2)            // 半开状态成功阈值
            .build();
        
        System.out.println("配置创建成功: " + config);
    }
}
```

---

## 高级篇

### 1. 熔断器注册中心（CircuitBreakerRegistry）

```java
import ltd.idcu.est.circuitbreaker.api.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerConfig;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerRegistry;
import ltd.idcu.est.circuitbreaker.impl.DefaultCircuitBreaker;
import ltd.idcu.est.circuitbreaker.impl.DefaultCircuitBreakerRegistry;

public class RegistryExample {
    public static void main(String[] args) {
        System.out.println("--- 熔断器注册中心示例 ---");
        
        CircuitBreakerRegistry registry = new DefaultCircuitBreakerRegistry();
        
        CircuitBreakerConfig config = CircuitBreakerConfig.builder().build();
        CircuitBreaker cb1 = new DefaultCircuitBreaker("service1", config);
        CircuitBreaker cb2 = new DefaultCircuitBreaker("service2", config);
        
        registry.register(cb1);
        registry.register(cb2);
        
        System.out.println("注册了 2 个熔断器");
        System.out.println("所有熔断器: " + registry.getAllCircuitBreakers());
        
        CircuitBreaker found = registry.getCircuitBreaker("service1");
        System.out.println("找到 service1: " + (found != null));
    }
}
```

---

## 高级篇

### 1. 熔断器状态持久化

DefaultCircuitBreakerRegistry 支持 JSON 格式的持久化，程序重启后可以恢复熔断器状态：

```java
import ltd.idcu.est.circuitbreaker.api.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerConfig;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerRegistry;
import ltd.idcu.est.circuitbreaker.impl.DefaultCircuitBreaker;
import ltd.idcu.est.circuitbreaker.impl.DefaultCircuitBreakerRegistry;

import java.io.File;

public class PersistenceExample {
    public static void main(String[] args) {
        System.out.println("--- 熔断器持久化示例 ---");
        
        File dataFile = new File("circuit-breakers.json");
        
        CircuitBreakerRegistry registry = new DefaultCircuitBreakerRegistry(dataFile);
        
        CircuitBreakerConfig config = CircuitBreakerConfig.builder()
            .failureThreshold(5)
            .build();
        CircuitBreaker cb = new DefaultCircuitBreaker("user-service", config);
        registry.register(cb);
        
        System.out.println("熔断器已注册，数据已自动保存到: " + dataFile.getAbsolutePath());
        
        CircuitBreakerRegistry newRegistry = new DefaultCircuitBreakerRegistry(dataFile);
        System.out.println("从文件加载后，熔断器数: " + newRegistry.getAllCircuitBreakers().size());
    }
}
```

---

## 与其他模块集成

EST CircuitBreaker 和 est-gateway 是绝配！让我们看看它们如何配合使用：

### 场景：网关 + 熔断器保护

```java
import ltd.idcu.est.circuitbreaker.api.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerConfig;
import ltd.idcu.est.circuitbreaker.impl.DefaultCircuitBreaker;
import ltd.idcu.est.gateway.api.ApiGateway;
import ltd.idcu.est.gateway.api.GatewayMiddleware;
import ltd.idcu.est.gateway.api.GatewayContext;
import ltd.idcu.est.gateway.impl.DefaultApiGateway;

public class GatewayIntegrationExample {
    public static void main(String[] args) {
        System.out.println("=== EST CircuitBreaker + EST Gateway 集成示例 ===\n");
        
        ApiGateway gateway = new DefaultApiGateway();
        
        CircuitBreakerConfig config = CircuitBreakerConfig.builder()
            .failureThreshold(5)
            .build();
        CircuitBreaker circuitBreaker = new DefaultCircuitBreaker("gateway-service", config);
        
        gateway.addMiddleware(new GatewayMiddleware() {
            @Override
            public String getName() {
                return "circuit-breaker";
            }
            
            @Override
            public void before(GatewayContext context) {
                try {
                    circuitBreaker.execute(() -> {
                        System.out.println("请求通过熔断器检查");
                        return null;
                    });
                } catch (Exception e) {
                    System.out.println("熔断器已开启，阻止请求");
                }
            }
            
            @Override
            public void after(GatewayContext context) {
            }
        });
        
        System.out.println("熔断器中间件已添加到网关");
    }
}
```

---

## 最佳实践

### 1. 合理设置阈值

```java
// ✓ 推荐：根据实际情况设置合理的阈值
CircuitBreakerConfig config = CircuitBreakerConfig.builder()
    .failureThreshold(10)
    .timeout(2000)
    .waitDuration(30000)
    .build();

// ✗ 不推荐：阈值太敏感或太宽松
CircuitBreakerConfig badConfig = CircuitBreakerConfig.builder()
    .failureThreshold(1)
    .build();
```

### 2. 为每个服务单独配置

```java
// ✓ 推荐：每个服务有独立的熔断器
CircuitBreaker userServiceCb = new DefaultCircuitBreaker("user-service", userConfig);
CircuitBreaker orderServiceCb = new DefaultCircuitBreaker("order-service", orderConfig);
```

### 3. 监控熔断状态

```java
CircuitBreakerMetrics metrics = circuitBreaker.getMetrics();
System.out.println("失败次数: " + metrics.getFailureCount());
System.out.println("成功率: " + metrics.getSuccessRate());
```

---

## 常见问题

### Q: 熔断器开启后会自动恢复吗？

A: 会的！等待 `waitDuration` 时间后会进入半开状态，尝试放行请求。

### Q: 如何手动重置熔断器？

A: 调用 `circuitBreaker.reset()` 方法可以手动重置。

### Q: 支持多个熔断器吗？

A: 支持！可以使用 CircuitBreakerRegistry 管理多个熔断器。

---

## 下一步

- 学习 [est-gateway](../est-gateway/README.md) 进行网关路由
- 查看 [est-discovery](../est-discovery/) 了解服务发现
- 尝试自定义熔断配置
- 阅读 [API 文档](../../docs/api/circuitbreaker/) 了解更多细节

---

**文档版本**: 2.0  
**最后更新**: 2026-03-06  
**维护者**: EST 架构团队
