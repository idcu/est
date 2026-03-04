# Web 教程 01: 基础 Web 应用

在本教程中，你将学习如何使用 EST Web 模块创建一个简单的 Web 应用。

## 步骤 1: 添加 Web 依赖

在你的 `pom.xml` 中添加 EST Web 模块依赖：

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web-impl</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

## 步骤 2: 创建简单的 Web 应用

创建 `BasicWebApp.java`：

```java
package com.example.web;

import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.api.WebApplication;

public class BasicWebApp {
    public static void main(String[] args) {
        // 创建 Web 应用
        WebApplication app = DefaultWebApplication.create();
        
        // 配置根路由
        app.getRouter().get("/", (req, res) -> {
            res.html("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>EST Web App</title>
                    <style>
                        body { 
                            font-family: Arial, sans-serif; 
                            max-width: 800px; 
                            margin: 50px auto; 
                            padding: 20px;
                        }
                        h1 { color: #2c3e50; }
                        .welcome { 
                            background: #ecf0f1; 
                            padding: 20px; 
                            border-radius: 8px; 
                        }
                    </style>
                </head>
                <body>
                    <h1>欢迎使用 EST Web 框架！</h1>
                    <div class="welcome">
                        <p>这是你的第一个 EST Web 应用。</p>
                        <p>访问下面的链接查看更多功能：</p>
                        <ul>
                            <li><a href="/hello">Hello 页面</a></li>
                            <li><a href="/api/status">API 状态</a></li>
                        </ul>
                    </div>
                </body>
                </html>
                """);
        });
        
        // 配置 Hello 路由
        app.getRouter().get("/hello", (req, res) -> {
            String name = req.getParameter("name", "Guest");
            res.text("Hello, " + name + "!");
        });
        
        // 配置 API 路由
        app.getRouter().get("/api/status", (req, res) -> {
            res.json(java.util.Map.of(
                "status", "ok",
                "timestamp", System.currentTimeMillis(),
                "server", "EST Web"
            ));
        });
        
        // 启动服务器，监听 8080 端口
        int port = 8080;
        app.run(port);
        
        System.out.println("========================================");
        System.out.println("  EST Web 服务器已启动");
        System.out.println("  访问地址: http://localhost:" + port);
        System.out.println("========================================");
    }
}
```

## 步骤 3: 运行 Web 应用

编译并运行：

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.example.web.BasicWebApp"
```

## 步骤 4: 测试应用

打开浏览器访问以下 URL：

1. **首页**: http://localhost:8080
2. **Hello 页面**: http://localhost:8080/hello?name=World
3. **API 状态**: http://localhost:8080/api/status

你也可以使用 `curl` 测试：

```bash
# 测试首页
curl http://localhost:8080

# 测试 Hello 页面
curl http://localhost:8080/hello?name=EST

# 测试 API（JSON 响应）
curl http://localhost:8080/api/status
```

## 响应格式

EST Web 支持多种响应格式：

```java
// 纯文本响应
res.text("Hello, World!");

// HTML 响应
res.html("<h1>Hello</h1>");

// JSON 响应
res.json(Map.of("key", "value"));

// 设置状态码
res.status(404).text("Not Found");

// 设置响应头
res.header("Content-Type", "application/xml")
   .body("<xml>data</xml>");
```

## 下一步

在下一个教程中，我们将学习更多关于 [路由与控制器](./02-routing-controllers.md) 的内容。
