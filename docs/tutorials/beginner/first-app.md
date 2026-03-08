# 教程 1: 第一个 EST 应用

欢迎来到 EST 框架的第一个教程！本教程将带你创建你的第一个 EST 应用程序。

## 前置条件

- JDK 21 或更高版本
- Maven 3.6 或更高版本
- 一个文本编辑器或 IDE

## 第一步: 创建项目

首先，让我们创建一个 Maven 项目。

### 使用 Maven Archetype（推荐）

```bash
mvn archetype:generate \
  -DgroupId=com.example \
  -DartifactId=my-first-est-app \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false
```

### 或者手动创建

创建以下目录结构：

```
my-first-est-app/
├── pom.xml
└── src/
    └── main/
        └── java/
            └── com/
                └── example/
                    └── App.java
```

## 第二步: 配置 pom.xml

编辑 `pom.xml`，添加 EST 框架的依赖：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>my-first-est-app</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <est.version>2.0.0</est.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>ltd.idcu</groupId>
            <artifactId>est-web</artifactId>
            <version>${est.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <version>3.1.0</version>
                <configuration>
                    <mainClass>com.example.App</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

## 第三步: 编写代码

创建 `src/main/java/com/example/App.java`：

```java
package com.example;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class App {
    public static void main(String[] args) {
        // 1. 创建 Web 应用
        WebApplication app = Web.create("我的第一个 EST 应用", "1.0.0");
        
        // 2. 添加路由
        app.get("/", (req, res) -> {
            res.send("Hello, EST World!");
        });
        
        app.get("/hello/{name}", (req, res) -> {
            String name = req.pathParam("name");
            res.send("Hello, " + name + "!");
        });
        
        // 3. 启动服务器
        System.out.println("应用启动中...");
        app.run(8080);
        System.out.println("应用已启动，访问 http://localhost:8080");
    }
}
```

## 第四步: 编译和运行

### 编译项目

```bash
mvn clean compile
```

### 运行应用

```bash
mvn exec:java
```

或者直接运行：

```bash
java -cp target/classes com.example.App
```

## 第五步: 测试应用

打开浏览器，访问以下 URL：

1. **首页**: http://localhost:8080
   
   你应该会看到：`Hello, EST World!`

2. **个性化问候**: http://localhost:8080/hello/张三
   
   你应该会看到：`Hello, 张三!`

## 代码解释

让我们逐行解释代码：

```java
WebApplication app = Web.create("我的第一个 EST 应用", "1.0.0");
```

这行代码创建了一个 Web 应用实例，指定了应用名称和版本号。

```java
app.get("/", (req, res) -> {
    res.send("Hello, EST World!");
});
```

这行代码添加了一个 GET 路由，当访问根路径 `/` 时，返回 "Hello, EST World!"。

```java
app.get("/hello/{name}", (req, res) -> {
    String name = req.pathParam("name");
    res.send("Hello, " + name + "!");
});
```

这行代码添加了一个带路径参数的路由，`{name}` 是一个路径参数，可以通过 `req.pathParam("name")` 获取。

```java
app.run(8080);
```

这行代码启动了 Web 服务器，监听 8080 端口。

## 下一步

恭喜你！你已经成功创建了你的第一个 EST 应用！

接下来，你可以学习：
- [依赖注入](./dependency-injection.md) - 学习 EST 的依赖注入容器
- [路由和控制器](../web/routing.md) - 深入学习 Web 路由
- [配置管理](./configuration.md) - 学习如何管理应用配置

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
