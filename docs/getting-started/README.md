# 入门指南

欢迎来到 EST 框架的入门指南！无论你是刚接触编程的小白，还是有经验的开发者，这里都能帮你快速上手 EST 框架。

## 📋 目录

1. [环境准备](#环境准备)
2. [快速开始](#快速开始)
3. [第一个应用](#第一个应用)
4. [下一步](#下一步)

---

## 环境准备

### 你需要安装的软件

在开始之前，确保你的电脑上安装了以下软件：

| 软件 | 版本要求 | 为什么需要 | 下载地址 |
|------|---------|-----------|---------|
| **JDK** | 21+ | Java 开发工具包，运行 Java 程序必须 | [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) 或 [OpenJDK](https://adoptium.net/) |
| **Maven** | 3.6+ | 构建工具，管理依赖和编译项目 | [Maven 官网](https://maven.apache.org/download.cgi) |
| **IDE** (可选) | 任意 | 推荐 IntelliJ IDEA 或 VS Code | [IntelliJ IDEA](https://www.jetbrains.com/idea/) |

### 验证安装

打开命令行（Windows 用 PowerShell 或 CMD，Mac/Linux 用 Terminal），输入以下命令验证：

```bash
# 验证 Java
java -version

# 验证 Maven
mvn -version
```

如果都显示版本号，说明安装成功！

---

## 快速开始

### 第一步：获取 EST 源码

```bash
git clone https://github.com/idcu/est.git
cd est2.0
```

### 第二步：构建项目

```bash
mvn clean install
```

这会下载所有依赖并编译整个项目，可能需要几分钟时间。

### 第三步：运行示例

```bash
cd est-examples
cd est-examples-basic
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.basic.Main"
```

恭喜！你已经成功运行了你的第一个 EST 程序！

---

## 第一个应用

让我们从零开始，创建一个简单的 Web 应用。

### 1. 创建项目结构

```
my-first-est-app/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── example/
        │           └── MyFirstApp.java
        └── resources/
            └── application.properties
```

### 2. 配置 pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>my-first-est-app</artifactId>
    <version>1.0.0</version>

    <properties>
        <est.version>2.0.0</est.version>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
    </properties>

    <dependencies>
        <!-- EST 核心 -->
        <dependency>
            <groupId>ltd.idcu</groupId>
            <artifactId>est-core-api</artifactId>
            <version>${est.version}</version>
        </dependency>
        <dependency>
            <groupId>ltd.idcu</groupId>
            <artifactId>est-core-impl</artifactId>
            <version>${est.version}</version>
        </dependency>

        <!-- EST Web -->
        <dependency>
            <groupId>ltd.idcu</groupId>
            <artifactId>est-web-api</artifactId>
            <version>${est.version}</version>
        </dependency>
        <dependency>
            <groupId>ltd.idcu</groupId>
            <artifactId>est-web-impl</artifactId>
            <version>${est.version}</version>
        </dependency>
    </dependencies>
</project>
```

### 3. 编写主程序

```java
package com.example;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class MyFirstApp {
    public static void main(String[] args) {
        // 1. 创建 Web 应用
        WebApplication app = Web.create("我的第一个 EST 应用", "1.0.0");

        // 2. 添加路由 - 访问首页
        app.get("/", (req, res) -> {
            res.send("""
                <h1>欢迎来到 EST</h1>
                <p>这是你的第一个 EST Web 应用！</p>
                <p>试试访问：<a href="/hello/张三">/hello/你的名字</a></p>
                """);
        });

        // 3. 添加路由 - 带参数
        app.get("/hello/:name", (req, res) -> {
            String name = req.pathParam("name");
            res.send("<h1>你好，" + name + "！</h1>");
        });

        // 4. 启动应用
        app.run(8080);
        System.out.println("应用已启动！访问 http://localhost:8080");
    }
}
```

### 4. 运行应用

```bash
mvn compile exec:java -Dexec.mainClass="com.example.MyFirstApp"
```

然后打开浏览器访问：http://localhost:8080

---

## 下一步

太棒了！你已经成功创建了第一个 EST 应用！接下来你可以：

- 📖 阅读 [架构文档](../architecture/README.md) 了解 EST 的整体架构
- 🎓 跟着 [教程](../tutorials/beginner/README.md) 学习更多功能
- 🔍 查看 [模块文档](../modules/README.md) 了解各个模块的用法
- 💡 尝试 [最佳实践课程](../best-practices/course/README.md) 学习如何用好 EST
- 🚀 学习 [前后端联调](../guides/admin-integration.md) 搭建完整的管理系统

---

**文档版本**: 2.0  
**最后更新**: 2026-03-07  
**维护者**: EST 架构团队
