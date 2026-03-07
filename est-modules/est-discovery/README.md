# EST Discovery 服务发现模块 - 小白从入门到精通

## 目录
1. [什么是 EST Discovery？](#什么是-est-discovery)
2. [快速入门：5分钟上手](#快速入门5分钟上手)
3. [基础篇](#基础篇)
4. [进阶篇](#进阶篇)
5. [高级篇](#高级篇)
6. [与其他模块集成](#与其他模块集成)
7. [最佳实践](#最佳实践)
8. [常见问题](#常见问题)
9. [下一步](#下一步)

---

## 什么是 EST Discovery？

### 用大白话理解

EST Discovery 就像是一个"服务通讯录"。想象一下你在一个大公司里，有很多部门（服务），你需要找某个部门办事：

**传统方式**：你必须记住每个部门的具体位置和电话，换位置了还要重新记，太麻烦！

**EST Discovery 方式**：有一个统一的通讯录，服务启动时自动登记，你要找谁就去通讯录查！
- 服务注册：服务启动时自动报到
- 心跳检测：定期确认服务还活着
- 负载均衡：多个实例时自动选择
- 服务发现：快速找到需要的服务

它支持多种负载均衡策略：随机、轮询，想用哪种用哪种！

### 核心特点

- 🎯 **简单易用** - 几行代码就能注册和发现服务
- 🚀 **高性能** - 内存存储，查询速度快
- 🔄 **心跳机制** - 自动检测服务状态
- 📊 **负载均衡** - 支持多种策略
- 💾 **持久化支持** - 支持JSON格式的服务实例持久化
- 🔄 **自动保存** - 服务变更时自动保存到文件
- 📈 **可扩展** - 轻松添加自定义负载均衡器

---

## 快速入门：5分钟上手

### 第一步：引入依赖

在你的 Maven pom.xml 中添加：

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-discovery-api</artifactId>
        <version>2.0.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-discovery-impl</artifactId>
        <version>2.0.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### 第二步：你的第一个服务注册

```java
import ltd.idcu.est.discovery.api.ServiceRegistry;
import ltd.idcu.est.discovery.api.ServiceInstance;
import ltd.idcu.est.discovery.impl.DefaultServiceRegistry;

public class FirstDiscoveryExample {
    public static void main(String[] args) {
        System.out.println("=== EST Discovery 第一个示例 ===\n");
        
        ServiceRegistry registry = new DefaultServiceRegistry();
        
        ServiceInstance instance = new ServiceInstance("user-service", "instance-1", "localhost", 8081);
        registry.register(instance);
        
        System.out.println("服务注册成功: " + instance);
        System.out.println("当前服务数: " + registry.getServiceIds().size());
        
        System.out.println("\n恭喜你！你已经成功使用 EST Discovery 了！");
    }
}
```

运行这个程序，你会看到：
```
=== EST Discovery 第一个示例 ===

服务注册成功: ServiceInstance{serviceId='user-service', instanceId='instance-1', host='localhost', port=8081}
当前服务数: 1

恭喜你！你已经成功使用 EST Discovery 了！
```

---

## 基础篇

### 1. 什么是 ServiceRegistry？

ServiceRegistry 就是一个"服务注册中心"接口，它的核心操作非常简单：

```java
public interface ServiceRegistry {
    void register(ServiceInstance instance);              // 注册服务
    void unregister(String serviceId, String instanceId); // 注销服务
    void heartbeat(String serviceId, String instanceId);  // 发送心跳
    Optional<ServiceInstance> getInstance(String serviceId, String instanceId); // 获取实例
    List<ServiceInstance> getInstances(String serviceId); // 获取服务的所有实例
    List<String> getServiceIds();                          // 获取所有服务ID
    void clear();                                           // 清空所有服务
}
```

### 2. 创建注册中心的几种方式

```java
import ltd.idcu.est.discovery.api.ServiceRegistry;
import ltd.idcu.est.discovery.api.ServiceInstance;
import ltd.idcu.est.discovery.impl.DefaultServiceRegistry;

public class CreateRegistryExample {
    public static void main(String[] args) {
        System.out.println("--- 方式一：默认注册中心 ---");
        ServiceRegistry registry1 = new DefaultServiceRegistry();
        System.out.println("默认注册中心创建成功");
        
        System.out.println("\n--- 方式二：注册服务 ---");
        ServiceRegistry registry2 = new DefaultServiceRegistry();
        ServiceInstance instance = new ServiceInstance("order-service", "instance-1", "localhost", 8082);
        registry2.register(instance);
        System.out.println("服务注册成功");
    }
}
```

### 3. 基本操作

```java
import ltd.idcu.est.discovery.api.ServiceRegistry;
import ltd.idcu.est.discovery.api.ServiceInstance;
import ltd.idcu.est.discovery.impl.DefaultServiceRegistry;

import java.util.List;
import java.util.Optional;

public class BasicOperations {
    public static void main(String[] args) {
        ServiceRegistry registry = new DefaultServiceRegistry();
        
        System.out.println("--- 1. 注册服务 ---");
        ServiceInstance instance1 = new ServiceInstance("user-service", "instance-1", "localhost", 8081);
        ServiceInstance instance2 = new ServiceInstance("user-service", "instance-2", "localhost", 8082);
        ServiceInstance instance3 = new ServiceInstance("order-service", "instance-1", "localhost", 8083);
        registry.register(instance1);
        registry.register(instance2);
        registry.register(instance3);
        System.out.println("注册了 3 个服务实例");
        
        System.out.println("\n--- 2. 获取所有服务ID ---");
        List<String> serviceIds = registry.getServiceIds();
        System.out.println("服务列表: " + serviceIds);
        
        System.out.println("\n--- 3. 获取服务的所有实例 ---");
        List<ServiceInstance> userInstances = registry.getInstances("user-service");
        System.out.println("user-service 实例数: " + userInstances.size());
        
        System.out.println("\n--- 4. 获取特定实例 ---");
        Optional<ServiceInstance> found = registry.getInstance("user-service", "instance-1");
        found.ifPresent(i -> System.out.println("找到实例: " + i));
        
        System.out.println("\n--- 5. 发送心跳 ---");
        registry.heartbeat("user-service", "instance-1");
        System.out.println("心跳发送成功");
        
        System.out.println("\n--- 6. 注销服务 ---");
        registry.unregister("order-service", "instance-1");
        System.out.println("注销了 order-service");
    }
}
```

---

## 进阶篇

### 1. 负载均衡（LoadBalancer）

你可以使用负载均衡器来选择服务实例：

```java
import ltd.idcu.est.discovery.api.ServiceRegistry;
import ltd.idcu.est.discovery.api.ServiceInstance;
import ltd.idcu.est.discovery.api.LoadBalancer;
import ltd.idcu.est.discovery.impl.DefaultServiceRegistry;
import ltd.idcu.est.discovery.impl.RandomLoadBalancer;
import ltd.idcu.est.discovery.impl.RoundRobinLoadBalancer;

import java.util.List;

public class LoadBalancerExample {
    public static void main(String[] args) {
        System.out.println("--- 负载均衡示例 ---");
        
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(new ServiceInstance("user-service", "instance-1", "localhost", 8081));
        registry.register(new ServiceInstance("user-service", "instance-2", "localhost", 8082));
        registry.register(new ServiceInstance("user-service", "instance-3", "localhost", 8083));
        
        List<ServiceInstance> instances = registry.getInstances("user-service");
        
        System.out.println("\n--- 随机策略 ---");
        LoadBalancer randomBalancer = new RandomLoadBalancer();
        for (int i = 0; i < 5; i++) {
            ServiceInstance selected = randomBalancer.select(instances);
            System.out.println("选择: " + selected.getInstanceId());
        }
        
        System.out.println("\n--- 轮询策略 ---");
        LoadBalancer roundRobinBalancer = new RoundRobinLoadBalancer();
        for (int i = 0; i < 5; i++) {
            ServiceInstance selected = roundRobinBalancer.select(instances);
            System.out.println("选择: " + selected.getInstanceId());
        }
    }
}
```

### 2. 自定义负载均衡器

```java
import ltd.idcu.est.discovery.api.LoadBalancer;
import ltd.idcu.est.discovery.api.ServiceInstance;

import java.util.List;

public class WeightedLoadBalancer implements LoadBalancer {
    @Override
    public ServiceInstance select(List<ServiceInstance> instances) {
        if (instances == null || instances.isEmpty()) {
            return null;
        }
        return instances.get(0);
    }
}
```

---

## 高级篇

### 1. 心跳管理

```java
import ltd.idcu.est.discovery.api.ServiceRegistry;
import ltd.idcu.est.discovery.api.ServiceInstance;
import ltd.idcu.est.discovery.impl.DefaultServiceRegistry;

public class HeartbeatExample {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- 心跳管理示例 ---");
        
        ServiceRegistry registry = new DefaultServiceRegistry();
        ServiceInstance instance = new ServiceInstance("user-service", "instance-1", "localhost", 8081);
        registry.register(instance);
        
        System.out.println("服务已注册");
        
        for (int i = 0; i < 3; i++) {
            Thread.sleep(1000);
            registry.heartbeat("user-service", "instance-1");
            System.out.println("发送心跳 " + (i + 1));
        }
        
        System.out.println("心跳管理完成");
    }
}
```

---

## 高级篇

### 1. 服务实例持久化

DefaultServiceRegistry 支持 JSON 格式的持久化，程序重启后可以恢复服务实例数据：

```java
import ltd.idcu.est.discovery.api.ServiceRegistry;
import ltd.idcu.est.discovery.api.ServiceInstance;
import ltd.idcu.est.discovery.impl.DefaultServiceRegistry;

import java.io.File;

public class PersistenceExample {
    public static void main(String[] args) {
        System.out.println("--- 服务发现持久化示例 ---");
        
        File dataFile = new File("service-registry.json");
        
        ServiceRegistry registry = new DefaultServiceRegistry(dataFile);
        
        ServiceInstance instance = new ServiceInstance("user-service", "instance-1", "localhost", 8081);
        instance.setTags(java.util.Arrays.asList("production", "v1.0"));
        registry.register(instance);
        
        System.out.println("服务已注册，数据已自动保存到: " + dataFile.getAbsolutePath());
        
        ServiceRegistry newRegistry = new DefaultServiceRegistry(dataFile);
        System.out.println("从文件加载后，服务数: " + newRegistry.getServiceIds().size());
    }
}
```

### 2. 服务实例标签

ServiceInstance 支持添加标签，方便进行服务分组和筛选：

```java
import ltd.idcu.est.discovery.api.ServiceInstance;

public class TagsExample {
    public static void main(String[] args) {
        ServiceInstance instance = new ServiceInstance("user-service", "instance-1", "localhost", 8081);
        instance.setTags(java.util.Arrays.asList("production", "v1.0", "primary"));
        
        System.out.println("服务标签: " + instance.getTags());
    }
}
```

---

## 与其他模块集成

EST Discovery 和 est-gateway 是绝配！让我们看看它们如何配合使用：

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

public class GatewayIntegrationExample {
    public static void main(String[] args) {
        System.out.println("=== EST Discovery + EST Gateway 集成示例 ===\n");
        
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

### 1. 合理设置服务ID

```java
// ✓ 推荐：使用有意义的服务ID
ServiceInstance instance = new ServiceInstance("user-service", "instance-1", "localhost", 8081);

// ✗ 不推荐：服务ID太随意
ServiceInstance badInstance = new ServiceInstance("srv1", "i1", "localhost", 8081);
```

### 2. 定期发送心跳

```java
// ✓ 推荐：定期发送心跳保持服务活跃
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
scheduler.scheduleAtFixedRate(() -> {
    registry.heartbeat("user-service", "instance-1");
}, 0, 30, TimeUnit.SECONDS);
```

### 3. 监控服务状态

```java
List<String> serviceIds = registry.getServiceIds();
System.out.println("当前在线服务数: " + serviceIds.size());
```

---

## 常见问题

### Q: 服务信息会持久化吗？

A: DefaultServiceRegistry 是内存存储，程序重启后数据会丢失。后续版本会支持持久化。

### Q: 如何实现服务健康检查？

A: 可以通过心跳机制来实现，定期发送心跳确认服务存活。

### Q: 支持跨机房部署吗？

A: 当前版本支持同一网络内的服务发现，后续版本会支持跨机房。

---

## 下一步

- 学习 [est-gateway](../est-gateway/README.md) 进行网关路由
- 查看 [est-circuitbreaker](../est-circuitbreaker/) 了解熔断保护
- 尝试自定义负载均衡器
- 阅读 [API 文档](../../docs/api/discovery/) 了解更多细节

---

**文档版本**: 2.0  
**最后更新**: 2026-03-06  
**维护者**: EST 架构团队
