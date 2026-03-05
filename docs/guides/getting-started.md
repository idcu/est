# 🚀 快速开始指南

本指南将帮助你从零开始，快速上手 EST 框架！

---

## 📋 前置要求

在开始之前，请确保你的电脑已经安装了以下软件：

- **JDK 21 或更高版本**（必须）
  - 检查方法：打开终端，输入 `java -version`
- **Maven 3.6 或更高版本**（用来构建项目）
  - 检查方法：打开终端，输入 `mvn -version`

如果你还没有安装这些软件，请先查看 [安装与配置](./installation.md) 文档。

---

## 🎯 第一步：构建 EST 框架

首先，我们需要把 EST 框架构建到你的本地 Maven 仓库中。

### 1. 克隆项目

```bash
git clone https://github.com/idcu/est.git
cd est
```

### 2. 构建所有模块

```bash
mvn clean install
```

这个过程可能需要几分钟，请耐心等待。看到 `BUILD SUCCESS` 就说明构建成功了！

---

## 💻 第二步：创建你的第一个 Web 应用

现在让我们创建一个简单的 Web 应用！

### 1. 创建一个新的 Maven 项目

你可以使用 IDE（如 IntelliJ IDEA 或 Eclipse）创建一个新的 Maven 项目，或者手动创建。

### 2. 配置 pom.xml

在项目的 `pom.xml` 文件中添加 EST 依赖：

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
        <!-- EST Web 模块 -->
        <dependency>
            <groupId>ltd.idcu</groupId>
            <artifactId>est-web-impl</artifactId>
            <version>1.3.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
</project>
```

### 3. 编写主类

在 `src/main/java/com/example/` 目录下创建 `HelloWorld.java`：

```java
package com.example;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class HelloWorld {
    public static void main(String[] args) {
        // 1. 创建一个 Web 应用
        // 第一个参数是应用名称，第二个参数是版本号
        WebApplication app = Web.create("我的第一个应用", "1.0.0");
        
        // 2. 添加路由
        // 当访问 http://localhost:8080/ 时，返回 "Hello, World!"
        app.get("/", (req, res) -> {
            res.send("Hello, World!");
        });
        
        // 3. 添加另一个路由，返回 JSON 数据
        app.get("/api/greeting", (req, res) -> {
            res.json(java.util.Map.of(
                "message", "你好，欢迎使用 EST 框架！",
                "version", "1.3.0",
                "author", "idcu"
            ));
        });
        
        // 4. 添加一个带参数的路由
        app.get("/user/:name", (req, res) -> {
            // 从路由中获取参数
            String name = req.param("name");
            res.send("你好，" + name + "！");
        });
        
        // 5. 启动服务器，监听 8080 端口
        System.out.println("========================================");
        System.out.println("  服务器启动成功！");
        System.out.println("  访问地址：http://localhost:8080");
        System.out.println("  按 Ctrl+C 停止服务器");
        System.out.println("========================================");
        
        app.run(8080);
    }
}
```

### 4. 运行应用

在终端中执行：

```bash
# 编译项目
mvn clean compile

# 运行应用
mvn exec:java -Dexec.mainClass="com.example.HelloWorld"
```

### 5. 测试应用

打开浏览器，访问以下地址试试：

- http://localhost:8080 - 应该看到 "Hello, World!"
- http://localhost:8080/api/greeting - 应该看到 JSON 格式的问候语
- http://localhost:8080/user/张三 - 应该看到 "你好，张三！"

---

## 🎓 第三步：学习核心概念

现在你已经运行了第一个 EST 应用，让我们了解一些核心概念。

### 1. WebApplication（Web 应用）

`WebApplication` 是 EST Web 框架的核心类，用来创建和管理你的 Web 应用。

```java
// 创建一个默认的 Web 应用
WebApplication app = Web.create();

// 创建一个带名称和版本的 Web 应用
WebApplication app = Web.create("我的应用", "1.0.0");
```

### 2. 路由（Router）

路由用来定义 URL 和处理函数的对应关系。

```java
// GET 请求
app.get("/path", (req, res) -> { /* 处理逻辑 */ });

// POST 请求
app.post("/path", (req, res) -> { /* 处理逻辑 */ });

// PUT 请求
app.put("/path", (req, res) -> { /* 处理逻辑 */ });

// DELETE 请求
app.delete("/path", (req, res) -> { /* 处理逻辑 */ });
```

### 3. 请求（Request）和响应（Response）

每个路由处理函数都会接收两个参数：`Request` 和 `Response`。

```java
app.get("/example", (req, res) -> {
    // 获取请求参数
    String name = req.param("name");              // 路由参数
    String query = req.queryParam("keyword");    // 查询参数
    String form = req.formParam("username");     // 表单参数
    
    // 发送响应
    res.send("普通文本");                          // 发送纯文本
    res.html("<h1>HTML</h1>");                    // 发送 HTML
    res.json(java.util.Map.of("key", "value"));  // 发送 JSON
    res.sendError(404, "未找到");                 // 发送错误
});
```

### 4. 配置（Config）

你可以通过 `Config` 对象来管理应用配置：

```java
// 设置配置
app.getConfig().set("app.name", "我的应用");
app.getConfig().set("server.port", 8080);

// 获取配置
String appName = app.getConfig().getString("app.name");
int port = app.getConfig().getInt("server.port", 8080); // 第二个参数是默认值
```

---

## 📚 下一步

恭喜你！你已经成功创建并运行了你的第一个 EST 应用！

接下来，你可以：

1. **继续学习教程**
   - [入门教程](../tutorials/beginner/) - 深入学习核心概念
   - [Web 开发教程](../tutorials/web/) - 学习更多 Web 开发技巧

2. **查看示例代码**
   - 项目根目录的 `est-examples/` 文件夹中有很多示例代码

3. **查阅 API 文档**
   - [API 参考](../api/README.md) - 完整的 API 文档

4. **尝试更多功能**
   - 添加缓存
   - 使用日志
   - 连接数据库
   - 等等...

---

## 💡 小贴士

- **遇到问题？** 先看看 [示例代码](../../est-examples/) 里有没有类似的用法
- **想了解更多？** 查阅 [API 参考](../api/README.md)
- **想提升代码质量？** 参考 [最佳实践](../best-practices/README.md)

---

**祝你使用 EST 框架愉快！** 🎉
