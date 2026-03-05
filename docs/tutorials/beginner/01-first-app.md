# 教程 01：第一个 EST 应用

在本教程中，你将学习如何创建和运行你的第一个 EST 应用！

---

## 🎯 学习目标

完成本教程后，你将学会：
- ✅ 如何创建一个基本的 EST 应用
- ✅ 如何配置应用信息
- ✅ 如何运行和停止应用
- ✅ 如何使用应用的基本功能

---

## 📋 前置准备

在开始之前，请确保：
- ✅ 你已经完成了 [安装与配置](../guides/installation.md)
- ✅ 你已经构建了 EST 框架（执行过 `mvn clean install`）

---

## 🏗️ 步骤 1：创建项目

首先，让我们创建一个新的 Maven 项目。

### 1.1 创建项目结构

你可以使用 IDE（如 IntelliJ IDEA）创建 Maven 项目，或者手动创建以下目录结构：

```
my-first-est-app/
├── pom.xml
└── src/
    └── main/
        └── java/
            └── com/
                └── example/
                    └── FirstApp.java
```

### 1.2 配置 pom.xml

在项目根目录下创建 `pom.xml` 文件：

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>my-first-est-app</artifactId>
    <version>1.0.0</version>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- EST Web 模块 - 包含我们需要的所有功能 -->
        <dependency>
            <groupId>ltd.idcu</groupId>
            <artifactId>est-web-impl</artifactId>
            <version>1.3.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
</project>
```

---

## 💻 步骤 2：编写你的第一个应用

现在让我们创建一个简单的 Web 应用！

### 2.1 创建主类

在 `src/main/java/com/example/` 目录下创建 `FirstApp.java`：

```java
package com.example;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class FirstApp {
    public static void main(String[] args) {
        // ==========================================
        // 第一步：创建 Web 应用
        // ==========================================
        
        // 使用 Web.create() 方法创建一个 Web 应用
        // 第一个参数是应用名称，第二个参数是版本号
        WebApplication app = Web.create("我的第一个 EST 应用", "1.0.0");
        
        // ==========================================
        // 第二步：添加一些路由
        // ==========================================
        
        // 添加一个首页路由
        app.get("/", (req, res) -> {
            res.send("欢迎来到我的第一个 EST 应用！");
        });
        
        // 添加一个关于页面
        app.get("/about", (req, res) -> {
            res.html("""
                <h1>关于我们</h1>
                <p>这是一个使用 EST 框架构建的简单 Web 应用。</p>
                <p>EST 框架是一个零依赖的现代 Java 框架！</p>
            """);
        });
        
        // 添加一个 API 接口，返回 JSON 数据
        app.get("/api/info", (req, res) -> {
            res.json(java.util.Map.of(
                "appName", "我的第一个 EST 应用",
                "version", "1.0.0",
                "author", "我",
                "framework", "EST 1.3.0"
            ));
        });
        
        // ==========================================
        // 第三步：配置和启动
        // ==========================================
        
        // 设置一些配置
        app.getConfig().set("app.name", "我的第一个 EST 应用");
        app.getConfig().set("debug", true);
        
        // 获取并打印配置
        String appName = app.getConfig().getString("app.name");
        boolean isDebug = app.getConfig().getBoolean("debug", false);
        
        System.out.println("========================================");
        System.out.println("  " + appName);
        System.out.println("  调试模式: " + (isDebug ? "开启" : "关闭"));
        System.out.println("========================================");
        
        // 添加启动回调
        app.onStartup(() -> {
            System.out.println("\n🎉 应用启动成功！");
            System.out.println("📍 访问地址：http://localhost:8080");
            System.out.println("📖 API 地址：http://localhost:8080/api/info");
            System.out.println("⏹️  按 Ctrl+C 停止应用\n");
        });
        
        // 添加关闭回调
        app.onShutdown(() -> {
            System.out.println("\n👋 应用正在关闭...");
            System.out.println("感谢使用 EST 框架！");
        });
        
        // 启动应用，监听 8080 端口
        app.run(8080);
    }
}
```

---

## 🚀 步骤 3：编译和运行

### 3.1 编译项目

在项目根目录下打开终端，执行：

```bash
mvn clean compile
```

看到 `BUILD SUCCESS` 说明编译成功！

### 3.2 运行应用

```bash
mvn exec:java -Dexec.mainClass="com.example.FirstApp"
```

### 3.3 查看输出

你应该看到类似以下的输出：

```
========================================
  我的第一个 EST 应用
  调试模式: 开启
========================================

🎉 应用启动成功！
📍 访问地址：http://localhost:8080
📖 API 地址：http://localhost:8080/api/info
⏹️  按 Ctrl+C 停止应用
```

---

## 🌐 步骤 4：测试你的应用

打开浏览器，访问以下地址：

### 4.1 访问首页

打开 http://localhost:8080

你应该看到：
```
欢迎来到我的第一个 EST 应用！
```

### 4.2 访问关于页面

打开 http://localhost:8080/about

你应该看到一个漂亮的 HTML 页面。

### 4.3 访问 API 接口

打开 http://localhost:8080/api/info

你应该看到 JSON 格式的数据：
```json
{
  "appName": "我的第一个 EST 应用",
  "version": "1.0.0",
  "author": "我",
  "framework": "EST 1.3.0"
}
```

---

## 🛑 步骤 5：停止应用

在终端中按 `Ctrl+C` 来停止应用。

你应该看到：
```
👋 应用正在关闭...
感谢使用 EST 框架！
```

---

## 📚 代码详解

让我们来解释一下代码中的关键部分：

### 1. 创建应用

```java
WebApplication app = Web.create("我的第一个 EST 应用", "1.0.0");
```

- `Web.create()` 是一个静态方法，用来创建 Web 应用
- 第一个参数是应用的名称
- 第二个参数是应用的版本号
- 返回一个 `WebApplication` 对象，这是我们操作应用的主要接口

### 2. 添加路由

```java
app.get("/", (req, res) -> {
    res.send("欢迎来到我的第一个 EST 应用！");
});
```

- `app.get()` 用来添加一个 GET 请求的路由
- 第一个参数是 URL 路径
- 第二个参数是一个函数，用来处理请求
  - `req` 是请求对象，包含请求的信息
  - `res` 是响应对象，用来发送响应

### 3. 发送不同类型的响应

```java
res.send("文本内容");           // 发送纯文本
res.html("<h1>HTML</h1>");      // 发送 HTML
res.json(Map.of("key", "value"));// 发送 JSON
```

### 4. 配置管理

```java
app.getConfig().set("key", "value");           // 设置配置
String value = app.getConfig().getString("key"); // 获取配置
```

### 5. 生命周期回调

```java
app.onStartup(() -> {
    // 应用启动时执行
});

app.onShutdown(() -> {
    // 应用关闭时执行
});
```

---

## 💡 小练习

试试修改代码，完成以下练习：

1. **添加一个新路由**：添加一个 `/hello` 路由，返回 "Hello, EST!"
2. **添加路由参数**：添加一个 `/user/:name` 路由，返回 "你好，xxx！"（xxx 是路径中的名字）
3. **修改配置**：添加一个配置项，然后在启动时打印出来

---

## 🎓 下一步

恭喜你！你已经成功创建了你的第一个 EST 应用！

在下一个教程中，我们将学习如何使用 [依赖注入容器](./02-dependency-injection.md) 来管理我们的组件。

---

**做得好！继续加油！** 🎉
