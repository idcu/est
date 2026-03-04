# 快速开始

本指南将帮助你快速上手 EST 框架。

## 前置要求

- **JDK 21** 或更高版本
- **Maven 3.6** 或更高版本
- 熟悉 Java 编程语言

## 安装

### 1. 克隆或下载 EST 框架

```bash
git clone https://github.com/idcu/est.git
cd est
```

### 2. 构建框架

```bash
mvn clean install
```

## 你的第一个项目

### 方法一：使用现有示例

EST 框架提供了完整的示例项目：

```bash
cd est-examples/est-examples-basic
mvn clean compile
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.basic.BasicExample"
```

### 方法二：创建新项目

创建一个新的 Maven 项目，在 `pom.xml` 中添加：

```xml
<dependencies>
    <!-- 核心模块 -->
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-core-impl</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </dependency>
    
    <!-- Web 模块（可选） -->
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web-impl</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </dependency>
    
    <!-- 缓存模块（可选） -->
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-features-cache-memory</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

## 核心概念

### 1. EstApplication

`EstApplication` 是框架的入口点：

```java
import ltd.idcu.est.core.DefaultEstApplication;
import ltd.idcu.est.core.api.EstApplication;

public class App {
    public static void main(String[] args) {
        EstApplication app = DefaultEstApplication.create();
        app.run();
    }
}
```

### 2. 依赖注入容器

使用容器管理组件：

```java
// 注册服务
app.getContainer().register(UserService.class, UserServiceImpl.class);

// 获取服务
UserService service = app.getContainer().get(UserService.class);
```

### 3. 配置管理

```java
// 设置配置
app.getConfiguration().set("app.name", "My App");
app.getConfiguration().set("server.port", 8080);

// 获取配置
String appName = app.getConfiguration().getString("app.name");
int port = app.getConfiguration().getInt("server.port", 8080);
```

## Web 快速开始

创建一个简单的 Web 应用：

```java
import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.api.WebApplication;

public class WebApp {
    public static void main(String[] args) {
        WebApplication app = DefaultWebApplication.create();
        
        // 配置路由
        app.getRouter().get("/", (req, res) -> {
            res.html("<h1>Hello, EST!</h1>");
        });
        
        app.getRouter().get("/api/hello", (req, res) -> {
            res.json(Map.of("message", "Hello, World!"));
        });
        
        // 启动服务器
        app.run(8080);
        System.out.println("Server running at http://localhost:8080");
    }
}
```

访问 http://localhost:8080 查看效果！

## 下一步

- 阅读 [入门教程](../tutorials/beginner/01-first-app.md)
- 查看 [API 参考](../api/README.md)
- 探索 [示例项目](../../est-examples/)
- 学习 [最佳实践](../best-practices/README.md)
