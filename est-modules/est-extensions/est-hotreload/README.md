# est-hotreload - 小白从入门到精通

## 目录
- [什么是 est-hotreload](#什么是-est-hotreload)
- [快速入门：5分钟上手](#快速入门5分钟上手)
- [基础篇：核心功能](#基础篇核心功能)
- [进阶篇：高级用法](#进阶篇高级用法)
- [最佳实践](#最佳实践)
- [模块结构](#模块结构)
- [相关资源](#相关资源)

---

## 什么是 est-hotreload

### 用大白话理解
est-hotreload 就像"热插拔"功能。开发时修改代码不用重启应用，改完马上就能看到效果。就像给手机换电池不用关机，直接换完就能用。

### 核心特点
- **代码热加载**：修改 Java 代码无需重启
- **资源热刷新**：配置文件、模板文件自动刷新
- **类重载**：支持类定义动态替换
- **开发体验优化**：提升开发效率

---

## 快速入门：5分钟上手

### 1. 添加依赖
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-hotreload</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 2. 配置热加载
```yaml
est:
  hotreload:
    enabled: true
    watch-paths:
      - src/main/java
      - src/main/resources
    scan-interval: 1000  # 1秒扫描一次
```

### 3. 启动应用
```java
public class DevApplication {
    public static void main(String[] args) {
        EstApplication.run(DevApplication.class, args)
            .enableHotReload();
    }
}
```

---

## 基础篇：核心功能

### 1. 类热加载

#### 基本用法
```java
@Service
public class MyService {
    
    public String sayHello() {
        return "Hello World!";
    }
}

// 修改后保存，无需重启就能生效！
public String sayHello() {
    return "Hello EST!";
}
```

#### 限制说明
```java
// ✅ 可以热加载的
- 方法体修改
- 方法内新增/删除
- 字段新增/删除
- 注解修改

// ⚠️ 有限制的
- 类继承关系修改
- 接口实现修改

// ❌ 不能热加载的（需要重启）
- 类名修改
- 方法签名修改
- 字段类型修改
```

### 2. 资源文件热刷新

#### 配置文件刷新
```yaml
# application.yml
app:
  message: 初始消息
```

```java
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    
    @Value("${app.message}")
    @Refreshable
    private String message;
    
    public String getMessage() {
        return message;
    }
}
```

#### 模板文件刷新
```java
@Service
public class TemplateService {
    
    @Inject
    private TemplateEngine templateEngine;
    
    @Get("/template")
    public String renderTemplate() {
        return templateEngine.render("template.ftl");
    }
}
```

### 3. 热加载监听器

#### 监听类变更
```java
@Component
public class HotReloadListener {
    
    @OnClassReload
    public void onClassReload(ClassReloadEvent event) {
        Class<?> reloadedClass = event.getReloadedClass();
        System.out.println("类已重新加载: " + reloadedClass.getName());
    }
    
    @OnClassReload(classes = MyService.class)
    public void onMyServiceReload() {
        System.out.println("MyService 已更新了！");
        cacheService.clear();
    }
}
```

#### 监听资源变更
```java
@Component
public class ResourceChangeListener {
    
    @OnResourceChange("*.yml")
    public void onConfigChange(ResourceChangeEvent event) {
        System.out.println("配置文件变更: " + event.getResourcePath());
        configService.reload();
    }
    
    @OnResourceChange("*.ftl")
    public void onTemplateChange(ResourceChangeEvent event) {
        templateEngine.clearCache();
    }
}
```

---

## 模块结构

```
est-hotreload/
├── est-hotreload-api/        # API 接口定义
│   ├── src/main/java/
│   │   └── ltd/idcu/est/extensions/hotreload/
│   └── pom.xml
├── est-hotreload-impl/       # 实现模块
│   ├── src/main/java/
│   │   └── ltd/idcu/est/extensions/hotreload/
│   └── pom.xml
├── README.md
└── pom.xml
```

---

## 相关资源

- [父模块文档](../README.md)
- [插件系统](../est-plugin/README.md)
- [EST 开发指南](../../docs/development/README.md)
- [示例代码](../../est-examples/est-examples-basic/)
