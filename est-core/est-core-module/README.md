# est-core-module - 小白从入门到精通

## 目录
- [什么是 est-core-module](#什么是-est-core-module)
- [快速入门](#快速入门)
- [核心功能](#核心功能)
- [相关资源](#相关资源)

---

## 什么是 est-core-module

### 用大白话理解
est-core-module 就像"模块管理器"，帮你管理框架的各个模块，支持模块的加载、卸载、启用、禁用。

### 核心特点
- **模块化架构**：支持模块化开发
- **模块加载**：自动发现和加载模块
- **模块依赖**：支持模块间依赖管理
- **模块状态**：模块生命周期管理

---

## 快速入门

### 1. 添加依赖
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-core-module</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 2. 定义模块
```java
@Module(name = "my-module", version = "1.0.0")
public class MyModule implements EstModule {
    
    @Override
    public void initialize(ModuleContext context) {
        System.out.println("模块初始化");
    }
    
    @Override
    public void destroy(ModuleContext context) {
        System.out.println("模块销毁");
    }
}
```

---

## 核心功能

### 模块依赖
```java
@Module(name = "order-module", dependencies = {"user-module", "product-module"})
public class OrderModule implements EstModule {
}
```

### 模块配置
```java
@Module(name = "configurable-module")
@ModuleProperties(prefix = "my-module")
public class ConfigurableModule implements EstModule {
    
    @Value("${my-module.enabled:true}")
    private boolean enabled;
    
    @Override
    public void initialize(ModuleContext context) {
        if (enabled) {
            System.out.println("模块已启用");
        }
    }
}
```

---

## 相关资源

- [父模块文档](../README.md)
- [依赖注入容器](../est-core-container/README.md)
