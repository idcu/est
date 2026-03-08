# est-core-config - 小白从入门到精通

## 目录
- [什么是 est-core-config](#什么是-est-core-config)
- [快速入门](#快速入门)
- [核心功能](#核心功能)
- [相关资源](#相关资源)

---

## 什么是 est-core-config

### 用大白话理解
est-core-config 就像"配置管理器"，帮你统一管理应用的配置，支持多种配置源，方便切换环境。

### 核心特点
- **多源配置**：YAML、Properties、环境变量、系统属性
- **类型安全**：支持配置对象绑定
- **配置刷新**：支持运行时配置刷新
- **配置优先**：支持多配置源优先级

---

## 快速入门

### 1. 添加依赖
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-core-config</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 2. 创建配置类
```java
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    
    @Value("${app.name:est-app}")
    private String name;
    
    @Value("${app.port:8080}")
    private int port;
    
    public String getName() { return name; }
    public int getPort() { return port; }
}
```

### 3. 使用配置
```java
@Service
public class MyService {
    
    @Inject
    private AppConfig appConfig;
    
    public void start() {
        System.out.println("应用名: " + appConfig.getName());
        System.out.println("端口: " + appConfig.getPort());
    }
}
```

---

## 核心功能

### 多环境配置
```yaml
# application.yml
app:
  name: est-app

---
# application-dev.yml
app:
  port: 8080

---
# application-prod.yml
app:
  port: 80
```

### 配置刷新
```java
@Service
public class RefreshableService {
    
    @Value("${app.message}")
    @Refreshable
    private String message;
    
    public String getMessage() {
        return message;
    }
}
```

---

## 相关资源

- [父模块文档](../README.md)
- [依赖注入容器](../est-core-container/README.md)
