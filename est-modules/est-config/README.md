# EST Config 配置中心模块 - 小白从入门到精通

## 目录
1. [什么是 EST Config？](#什么是-est-config)
2. [快速入门：5分钟上手](#快速入门5分钟上手)
3. [基础篇](#基础篇)
4. [进阶篇](#进阶篇)
5. [高级篇](#高级篇)
6. [与其他模块集成](#与其他模块集成)
7. [最佳实践](#最佳实践)
8. [常见问题](#常见问题)
9. [下一步](#下一步)

---

## 什么是 EST Config？

### 用大白话理解

EST Config 就像是一个"智能配置管理中心"。想象一下你在管理一个大型应用，有很多配置项分散在各个地方：

**传统方式**：配置写死在代码里，改个配置还要重新编译部署，太麻烦！

**EST Config 方式**：统一管理所有配置，支持动态更新，还有监听器通知变化！
- 类型安全：支持 String、int、long、double、boolean 等类型
- 动态监听：配置变化时自动通知
- 统一管理：所有配置在一个地方
- 简单易用：几行代码就能搞定

它支持多种操作：获取、设置、删除、监听，想用哪个用哪个！

### 核心特点

- 🎯 **简单易用** - 几行代码就能创建和使用配置中心
- 🚀 **类型安全** - 支持多种数据类型
- 🔄 **变更监听** - 配置变化时自动通知
- 📊 **统一管理** - 所有配置集中管理
- 💾 **持久化支持** - 支持Properties和YAML格式的配置持久化
- 🔄 **自动保存** - 配置变更时自动保存到文件
- 📈 **可扩展** - 轻松添加自定义功能

---

## 快速入门：5分钟上手

### 第一步：引入依赖

在你的 Maven pom.xml 中添加：

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-config-api</artifactId>
        <version>2.0.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-config-impl</artifactId>
        <version>2.0.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### 第二步：你的第一个配置中心

```java
import ltd.idcu.est.config.api.ConfigCenter;
import ltd.idcu.est.config.impl.DefaultConfigCenter;

public class FirstConfigExample {
    public static void main(String[] args) {
        System.out.println("=== EST Config 第一个示例 ===\n");
        
        ConfigCenter configCenter = new DefaultConfigCenter();
        
        configCenter.setProperty("app.name", "My Application");
        configCenter.setProperty("app.port", 8080);
        configCenter.setProperty("app.debug", true);
        
        System.out.println("应用名称: " + configCenter.getString("app.name", "Unknown"));
        System.out.println("应用端口: " + configCenter.getInt("app.port", 80));
        System.out.println("调试模式: " + configCenter.getBoolean("app.debug", false));
        
        System.out.println("\n恭喜你！你已经成功使用 EST Config 了！");
    }
}
```

运行这个程序，你会看到：
```
=== EST Config 第一个示例 ===

应用名称: My Application
应用端口: 8080
调试模式: true

恭喜你！你已经成功使用 EST Config 了！
```

---

## 基础篇

### 1. 什么是 ConfigCenter？

ConfigCenter 就是一个"配置中心"接口，它的核心操作非常简单：

```java
public interface ConfigCenter {
    void setProperty(String key, Object value);                  // 设置属性
    Optional<Object> getProperty(String key);                     // 获取属性
    <T> Optional<T> getProperty(String key, Class<T> type);      // 获取属性（指定类型）
    String getString(String key, String defaultValue);            // 获取字符串
    int getInt(String key, int defaultValue);                     // 获取整数
    long getLong(String key, long defaultValue);                  // 获取长整数
    double getDouble(String key, double defaultValue);            // 获取浮点数
    boolean getBoolean(String key, boolean defaultValue);         // 获取布尔值
    boolean containsProperty(String key);                          // 检查是否存在
    void removeProperty(String key);                               // 删除属性
    Map<String, Object> getAllProperties();                       // 获取所有属性
    void addChangeListener(ConfigChangeListener listener);        // 添加变更监听器
    void removeChangeListener(ConfigChangeListener listener);     // 删除变更监听器
    List<ConfigChangeListener> getChangeListeners();              // 获取所有监听器
    void clear();                                                  // 清空配置
}
```

### 2. 创建配置中心的几种方式

```java
import ltd.idcu.est.config.api.ConfigCenter;
import ltd.idcu.est.config.impl.DefaultConfigCenter;

public class CreateConfigExample {
    public static void main(String[] args) {
        System.out.println("--- 方式一：默认配置中心 ---");
        ConfigCenter config1 = new DefaultConfigCenter();
        System.out.println("默认配置中心创建成功");
        
        System.out.println("\n--- 方式二：添加初始配置 ---");
        ConfigCenter config2 = new DefaultConfigCenter();
        config2.setProperty("key1", "value1");
        config2.setProperty("key2", 123);
        System.out.println("带初始配置的配置中心创建成功");
    }
}
```

### 3. 基本操作

```java
import ltd.idcu.est.config.api.ConfigCenter;
import ltd.idcu.est.config.impl.DefaultConfigCenter;

import java.util.Map;
import java.util.Optional;

public class BasicOperations {
    public static void main(String[] args) {
        ConfigCenter configCenter = new DefaultConfigCenter();
        
        System.out.println("--- 1. 设置配置 ---");
        configCenter.setProperty("string.key", "Hello World");
        configCenter.setProperty("int.key", 42);
        configCenter.setProperty("long.key", 1000000L);
        configCenter.setProperty("double.key", 3.14);
        configCenter.setProperty("boolean.key", true);
        System.out.println("设置了 5 个配置项");
        
        System.out.println("\n--- 2. 获取配置 ---");
        System.out.println("字符串: " + configCenter.getString("string.key", "default"));
        System.out.println("整数: " + configCenter.getInt("int.key", 0));
        System.out.println("长整数: " + configCenter.getLong("long.key", 0L));
        System.out.println("浮点数: " + configCenter.getDouble("double.key", 0.0));
        System.out.println("布尔值: " + configCenter.getBoolean("boolean.key", false));
        
        System.out.println("\n--- 3. 检查配置是否存在 ---");
        System.out.println("string.key 存在: " + configCenter.containsProperty("string.key"));
        System.out.println("not.exists 存在: " + configCenter.containsProperty("not.exists"));
        
        System.out.println("\n--- 4. 获取所有配置 ---");
        Map<String, Object> all = configCenter.getAllProperties();
        System.out.println("配置总数: " + all.size());
        all.forEach((k, v) -> System.out.println("  " + k + " = " + v));
        
        System.out.println("\n--- 5. 删除配置 ---");
        configCenter.removeProperty("string.key");
        System.out.println("删除后 string.key 存在: " + configCenter.containsProperty("string.key"));
        
        System.out.println("\n--- 6. 清空配置 ---");
        configCenter.clear();
        System.out.println("清空后配置总数: " + configCenter.getAllProperties().size());
    }
}
```

---

## 进阶篇

### 1. 配置变更监听

你可以监听配置的变化：

```java
import ltd.idcu.est.config.api.ConfigCenter;
import ltd.idcu.est.config.api.ConfigChangeListener;
import ltd.idcu.est.config.api.ConfigChangeEvent;
import ltd.idcu.est.config.impl.DefaultConfigCenter;

public class ListenerExample {
    public static void main(String[] args) {
        System.out.println("--- 配置变更监听示例 ---");
        
        ConfigCenter configCenter = new DefaultConfigCenter();
        
        configCenter.addChangeListener(new ConfigChangeListener() {
            @Override
            public void onChange(ConfigChangeEvent event) {
                System.out.println("[变更] " + event.getKey() + 
                    " 从 " + event.getOldValue() + 
                    " 变为 " + event.getNewValue());
            }
        });
        
        configCenter.setProperty("test.key", "value1");
        configCenter.setProperty("test.key", "value2");
        configCenter.setProperty("another.key", 123);
    }
}
```

### 2. 类型安全的配置获取

```java
import ltd.idcu.est.config.api.ConfigCenter;
import ltd.idcu.est.config.impl.DefaultConfigCenter;

import java.util.Optional;

public class TypeSafeExample {
    public static void main(String[] args) {
        System.out.println("--- 类型安全示例 ---");
        
        ConfigCenter configCenter = new DefaultConfigCenter();
        configCenter.setProperty("string.key", "Hello");
        configCenter.setProperty("int.key", 42);
        
        Optional<String> strValue = configCenter.getProperty("string.key", String.class);
        strValue.ifPresent(v -> System.out.println("字符串: " + v));
        
        Optional<Integer> intValue = configCenter.getProperty("int.key", Integer.class);
        intValue.ifPresent(v -> System.out.println("整数: " + v));
        
        Optional<Double> doubleValue = configCenter.getProperty("int.key", Double.class);
        System.out.println("转换为 Double: " + doubleValue.orElse(null));
    }
}
```

---

## 高级篇

### 1. 多个监听器

```java
import ltd.idcu.est.config.api.ConfigCenter;
import ltd.idcu.est.config.api.ConfigChangeListener;
import ltd.idcu.est.config.api.ConfigChangeEvent;
import ltd.idcu.est.config.impl.DefaultConfigCenter;

public class MultipleListenersExample {
    public static void main(String[] args) {
        System.out.println("--- 多个监听器示例 ---");
        
        ConfigCenter configCenter = new DefaultConfigCenter();
        
        ConfigChangeListener listener1 = event -> 
            System.out.println("[监听器1] " + event.getKey());
        
        ConfigChangeListener listener2 = event -> 
            System.out.println("[监听器2] " + event.getKey() + " changed");
        
        configCenter.addChangeListener(listener1);
        configCenter.addChangeListener(listener2);
        
        System.out.println("当前监听器数: " + configCenter.getChangeListeners().size());
        
        configCenter.setProperty("test.key", "value");
        
        configCenter.removeChangeListener(listener1);
        System.out.println("\n删除监听器1后，监听器数: " + configCenter.getChangeListeners().size());
        
        configCenter.setProperty("test.key", "new value");
    }
}
```

---

## 高级篇

### 1. 配置持久化

DefaultConfigCenter 支持 Properties 和 YAML 格式的持久化：

```java
import ltd.idcu.est.config.api.ConfigCenter;
import ltd.idcu.est.config.impl.DefaultConfigCenter;

import java.io.File;

public class PersistenceExample {
    public static void main(String[] args) {
        System.out.println("--- 配置中心持久化示例 ---");
        
        File propertiesFile = new File("config.properties");
        File yamlFile = new File("config.yml");
        
        ConfigCenter config1 = new DefaultConfigCenter(propertiesFile);
        config1.setProperty("app.name", "My Application");
        config1.setProperty("app.port", 8080);
        System.out.println("配置已保存到 Properties 文件");
        
        ConfigCenter config2 = new DefaultConfigCenter(yamlFile);
        config2.setProperty("app.name", "My Application");
        config2.setProperty("app.port", 8080);
        System.out.println("配置已保存到 YAML 文件");
    }
}
```

---

## 与其他模块集成

EST Config 和其他模块都是绝配！让我们看看它们如何配合使用：

### 场景：配置 + 网关

```java
import ltd.idcu.est.config.api.ConfigCenter;
import ltd.idcu.est.config.impl.DefaultConfigCenter;
import ltd.idcu.est.gateway.api.ApiGateway;
import ltd.idcu.est.gateway.api.Route;
import ltd.idcu.est.gateway.impl.DefaultApiGateway;
import ltd.idcu.est.gateway.impl.DefaultRoute;

public class GatewayIntegrationExample {
    public static void main(String[] args) {
        System.out.println("=== EST Config + EST Gateway 集成示例 ===\n");
        
        ConfigCenter configCenter = new DefaultConfigCenter();
        configCenter.setProperty("gateway.port", 8080);
        configCenter.setProperty("user.service.url", "http://localhost:8081");
        
        ApiGateway gateway = new DefaultApiGateway();
        
        int port = configCenter.getInt("gateway.port", 80);
        String userServiceUrl = configCenter.getString("user.service.url", "http://localhost:8080");
        
        Route route = new DefaultRoute("/api/users", userServiceUrl);
        gateway.getRouter().addRoute(route);
        
        System.out.println("网关配置完成，端口: " + port);
        System.out.println("用户服务地址: " + userServiceUrl);
    }
}
```

---

## 最佳实践

### 1. 合理规划配置键

```java
// ✓ 推荐：使用分层的键名
configCenter.setProperty("app.name", "MyApp");
configCenter.setProperty("app.port", 8080);
configCenter.setProperty("db.url", "jdbc:mysql://localhost:3306/db");

// ✗ 不推荐：键名混乱
configCenter.setProperty("name", "MyApp");
configCenter.setProperty("p", 8080);
configCenter.setProperty("database", "jdbc:mysql://localhost:3306/db");
```

### 2. 使用默认值

```java
// ✓ 推荐：总是提供默认值
String name = configCenter.getString("app.name", "Default App");
int port = configCenter.getInt("app.port", 8080);

// ✗ 不推荐：直接 get 可能返回 null
String name = configCenter.getProperty("app.name").orElse(null);
```

### 3. 使用监听器动态更新

```java
// ✓ 推荐：使用监听器响应配置变化
configCenter.addChangeListener(event -> {
    if ("app.port".equals(event.getKey())) {
        System.out.println("端口已变更，重新加载...");
    }
});
```

---

## 常见问题

### Q: 配置会持久化吗？

A: DefaultConfigCenter 是内存存储，程序重启后数据会丢失。后续版本会支持持久化。

### Q: 支持配置文件吗？

A: 当前版本支持内存配置，后续版本会支持 properties、yaml 等配置文件。

### Q: 多线程环境下安全吗？

A: 是的！DefaultConfigCenter 是线程安全的。

---

## 下一步

- 学习 [est-gateway](../est-gateway/README.md) 进行网关路由
- 查看 [est-discovery](../est-discovery/) 了解服务发现
- 尝试自定义配置源
- 阅读 [API 文档](../../docs/api/config/) 了解更多细节

---

**文档版本**: 2.0  
**最后更新**: 2026-03-06  
**维护者**: EST 架构团队
