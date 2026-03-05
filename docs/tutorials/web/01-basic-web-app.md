# Web 教程 01: 基础 Web 应用

在本教程中，你将学习如何使用 EST Web 模块创建一个功能完整的 Web 应用！

---

## 🎯 学习目标

完成本教程后，你将学会：
- ✅ 如何创建一个基础的 Web 应用
- ✅ 如何添加路由和处理请求
- ✅ 如何返回不同格式的响应（文本、HTML、JSON）
- ✅ 如何获取请求参数
- ✅ 如何使用静态文件

---

## 📋 前置准备

- ✅ 你已经完成了 [安装与配置](../guides/installation.md)
- ✅ 你已经构建了 EST 框架

---

## 🏗️ 步骤 1：创建项目

### 1.1 配置 pom.xml

创建一个新的 Maven 项目，在 `pom.xml` 中添加 EST Web 依赖：

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>basic-web-app</artifactId>
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

---

## 💻 步骤 2：创建你的 Web 应用

### 2.1 编写主类

在 `src/main/java/com/example/` 目录下创建 `BasicWebApp.java`：

```java
package com.example;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class BasicWebApp {
    public static void main(String[] args) {
        // ==========================================
        // 第一步：创建 Web 应用
        // ==========================================
        
        WebApplication app = Web.create("我的 Web 应用", "1.0.0");
        
        // ==========================================
        // 第二步：添加路由
        // ==========================================
        
        // 1. 首页 - 返回漂亮的 HTML
        app.get("/", (req, res) -> {
            res.html("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>EST Web 应用</title>
                    <style>
                        body { 
                            font-family: 'Microsoft YaHei', Arial, sans-serif; 
                            max-width: 800px; 
                            margin: 50px auto; 
                            padding: 20px;
                            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                            min-height: 100vh;
                        }
                        .container {
                            background: white;
                            padding: 40px;
                            border-radius: 15px;
                            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
                        }
                        h1 { 
                            color: #2c3e50; 
                            text-align: center;
                            margin-bottom: 30px;
                        }
                        .welcome { 
                            background: #f8f9fa; 
                            padding: 25px; 
                            border-radius: 10px; 
                            margin-bottom: 20px;
                        }
                        .links {
                            display: grid;
                            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                            gap: 15px;
                            margin-top: 25px;
                        }
                        .link-card {
                            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                            color: white;
                            padding: 20px;
                            border-radius: 10px;
                            text-align: center;
                            text-decoration: none;
                            transition: transform 0.3s, box-shadow 0.3s;
                        }
                        .link-card:hover {
                            transform: translateY(-5px);
                            box-shadow: 0 5px 20px rgba(0,0,0,0.3);
                        }
                        .link-card h3 {
                            margin: 0 0 10px 0;
                            font-size: 18px;
                        }
                        .link-card p {
                            margin: 0;
                            font-size: 14px;
                            opacity: 0.9;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>🎉 欢迎使用 EST Web 框架！</h1>
                        <div class="welcome">
                            <p>这是你的第一个 EST Web 应用。</p>
                            <p>EST 是一个零依赖的现代 Java 框架，让 Web 开发变得简单有趣！</p>
                        </div>
                        <h3 style="color: #2c3e50;">试试下面的功能：</h3>
                        <div class="links">
                            <a href="/hello" class="link-card">
                                <h3>👋 问候页面</h3>
                                <p>向你打招呼</p>
                            </a>
                            <a href="/hello?name=张三" class="link-card">
                                <h3>🎯 带参数</h3>
                                <p>自定义名字</p>
                            </a>
                            <a href="/api/status" class="link-card">
                                <h3>📊 API 状态</h3>
                                <p>查看 JSON 响应</p>
                            </a>
                            <a href="/user/1001" class="link-card">
                                <h3>👤 用户信息</h3>
                                <p>路径参数示例</p>
                            </a>
                        </div>
                    </div>
                </body>
                </html>
                """);
        });
        
        // 2. 问候页面 - 获取查询参数
        app.get("/hello", (req, res) -> {
            // 获取 name 参数，如果没有则用 "访客" 作为默认值
            String name = req.queryParam("name", "访客");
            
            res.html("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>问候页面</title>
                    <style>
                        body { 
                            font-family: 'Microsoft YaHei', Arial, sans-serif; 
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            min-height: 100vh;
                            margin: 0;
                            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
                        }
                        .greeting-card {
                            background: white;
                            padding: 50px;
                            border-radius: 20px;
                            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
                            text-align: center;
                        }
                        h1 { 
                            color: #2c3e50; 
                            font-size: 48px;
                            margin: 0 0 20px 0;
                        }
                        p {
                            color: #7f8c8d;
                            font-size: 18px;
                        }
                        .back-link {
                            display: inline-block;
                            margin-top: 30px;
                            padding: 12px 30px;
                            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                            color: white;
                            text-decoration: none;
                            border-radius: 30px;
                            transition: transform 0.3s;
                        }
                        .back-link:hover {
                            transform: scale(1.05);
                        }
                    </style>
                </head>
                <body>
                    <div class="greeting-card">
                        <h1>你好，%s！👋</h1>
                        <p>很高兴见到你！</p>
                        <a href="/" class="back-link">← 返回首页</a>
                    </div>
                </body>
                </html>
                """.formatted(name));
        });
        
        // 3. API 接口 - 返回 JSON
        app.get("/api/status", (req, res) -> {
            res.json(java.util.Map.of(
                "status", "ok",
                "message", "服务器运行正常",
                "timestamp", System.currentTimeMillis(),
                "server", "EST Web 1.3.0",
                "endpoints", java.util.List.of("/", "/hello", "/api/status", "/user/:id")
            ));
        });
        
        // 4. 用户信息 - 使用路径参数
        app.get("/user/:id", (req, res) -> {
            String userId = req.param("id");
            
            res.json(java.util.Map.of(
                "id", userId,
                "name", "用户" + userId,
                "email", "user" + userId + "@example.com",
                "role", "member",
                "createdAt", "2024-01-01"
            ));
        });
        
        // ==========================================
        // 第三步：启动服务器
        // ==========================================
        
        int port = 8080;
        
        app.onStartup(() -> {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("  🚀 EST Web 服务器已启动！");
            System.out.println("  📍 访问地址: http://localhost:" + port);
            System.out.println("  ⏹️  按 Ctrl+C 停止服务器");
            System.out.println("=".repeat(50) + "\n");
        });
        
        app.run(port);
    }
}
```

---

## 🚀 步骤 3：运行 Web 应用

### 3.1 编译项目

```bash
mvn clean compile
```

### 3.2 运行应用

```bash
mvn exec:java -Dexec.mainClass="com.example.BasicWebApp"
```

你应该看到类似以下的输出：

```
==================================================
  🚀 EST Web 服务器已启动！
  📍 访问地址: http://localhost:8080
  ⏹️  按 Ctrl+C 停止服务器
==================================================
```

---

## 🌐 步骤 4：测试你的应用

打开浏览器，访问以下页面：

### 4.1 首页

打开 http://localhost:8080

你会看到一个漂亮的首页，上面有几个功能链接。

### 4.2 问候页面

- http://localhost:8080/hello - 使用默认名字"访客"
- http://localhost:8080/hello?name=张三 - 使用自定义名字

### 4.3 API 状态

打开 http://localhost:8080/api/status

你会看到 JSON 格式的响应：

```json
{
  "status": "ok",
  "message": "服务器运行正常",
  "timestamp": 1704067200000,
  "server": "EST Web 1.3.0",
  "endpoints": ["/", "/hello", "/api/status", "/user/:id"]
}
```

### 4.4 用户信息

打开 http://localhost:8080/user/1001

你会看到用户的 JSON 信息。

---

## 📚 代码详解

让我们来学习一些核心概念：

### 1. 创建 Web 应用

```java
WebApplication app = Web.create("应用名称", "版本号");
```

- `Web.create()` 是创建 Web 应用的快捷方法
- 第一个参数是应用名称
- 第二个参数是版本号

### 2. 添加路由

EST 支持多种 HTTP 方法：

```java
// GET 请求 - 获取数据
app.get("/path", (req, res) -> { /* 处理逻辑 */ });

// POST 请求 - 提交数据
app.post("/path", (req, res) -> { /* 处理逻辑 */ });

// PUT 请求 - 更新数据
app.put("/path", (req, res) -> { /* 处理逻辑 */ });

// DELETE 请求 - 删除数据
app.delete("/path", (req, res) -> { /* 处理逻辑 */ });

// PATCH 请求 - 部分更新
app.patch("/path", (req, res) -> { /* 处理逻辑 */ });
```

### 3. 获取请求参数

#### 查询参数（Query Parameters）

URL 中 `?` 后面的参数，例如 `/hello?name=张三`

```java
// 获取 name 参数，默认值是 "访客"
String name = req.queryParam("name", "访客");
```

#### 路径参数（Path Parameters）

URL 路径中的参数，例如 `/user/:id`

```java
// 定义路由时使用 :参数名
app.get("/user/:id", (req, res) -> {
    // 获取路径参数
    String userId = req.param("id");
});
```

#### 表单参数（Form Parameters）

POST 请求中的表单数据

```java
app.post("/login", (req, res) -> {
    String username = req.formParam("username");
    String password = req.formParam("password");
});
```

### 4. 发送响应

EST 支持多种响应格式：

```java
// 发送纯文本
res.send("Hello, World!");

// 发送 HTML
res.html("<h1>标题</h1><p>段落</p>");

// 发送 JSON（自动转换）
res.json(Map.of("key", "value"));

// 发送错误
res.sendError(404, "页面未找到");
res.sendError(500, "服务器错误");
```

---

## 💡 小练习

试试完成以下练习：

1. **添加一个新路由**：创建一个 `/about` 页面，介绍你自己
2. **添加一个 POST 路由**：创建一个 `/submit` 路由，接收表单数据并返回确认信息
3. **创建一个待办事项 API**：
   - `GET /api/todos` - 返回待办事项列表
   - `GET /api/todos/:id` - 返回单个待办事项
4. **添加更多样式**：让页面看起来更漂亮

---

## 🎓 下一步

恭喜你！你已经创建了一个功能完整的 Web 应用！

在下一个教程中，我们将学习更多关于 [路由与控制器](./02-routing-controllers.md) 的高级用法。

---

**做得好！继续探索 EST Web 框架的更多功能！** 🎉
