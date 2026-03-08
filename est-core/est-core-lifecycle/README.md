# est-core-lifecycle - 小白从入门到精通

## 目录
- [什么是 est-core-lifecycle](#什么是-est-core-lifecycle)
- [快速入门](#快速入门)
- [核心功能](#核心功能)
- [相关资源](#相关资源)

---

## 什么是 est-core-lifecycle

### 用大白话理解
est-core-lifecycle 就像"应用生命周期管家"，管理应用从启动到关闭的整个生命周期，在各个阶段执行相应的操作。

### 核心特点
- **生命周期事件**：启动、就绪、关闭等事件
- **启动钩子**：应用启动时执行初始化
- **关闭钩子**：应用关闭时执行清理
- **状态管理**：应用状态追踪

---

## 快速入门

### 1. 添加依赖
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-core-lifecycle</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 2. 生命周期监听器
```java
@Component
public class MyLifecycleListener {
    
    @OnApplicationStart
    public void onStart() {
        System.out.println("应用启动了");
    }
    
    @OnApplicationReady
    public void onReady() {
        System.out.println("应用就绪了");
    }
    
    @OnApplicationStop
    public void onStop() {
        System.out.println("应用关闭了");
    }
}
```

---

## 核心功能

### 启动任务
```java
@Component
@Order(1)
public class DatabaseInitTask implements StartupTask {
    
    @Override
    public void run() {
        System.out.println("初始化数据库...");
    }
}

@Component
@Order(2)
public class CacheWarmupTask implements StartupTask {
    
    @Override
    public void run() {
        System.out.println("预热缓存...");
    }
}
```

### 关闭任务
```java
@Component
public class CleanupTask implements ShutdownTask {
    
    @Override
    public void run() {
        System.out.println("清理资源...");
    }
}
```

---

## 相关资源

- [父模块文档](../README.md)
- [依赖注入容器](../est-core-container/README.md)
