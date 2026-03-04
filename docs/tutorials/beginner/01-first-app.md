# 教程 01: 第一个 EST 应用

在本教程中，你将学习如何创建和运行你的第一个 EST 应用。

## 步骤 1: 创建项目

首先，创建一个新的 Maven 项目。在 `pom.xml` 中添加 EST 依赖：

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>first-est-app</artifactId>
    <version>1.0.0</version>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>ltd.idcu</groupId>
            <artifactId>est-core-impl</artifactId>
            <version>1.3.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
</project>
```

## 步骤 2: 创建主类

在 `src/main/java/com/example/` 目录下创建 `FirstApp.java`：

```java
package com.example;

import ltd.idcu.est.core.DefaultEstApplication;
import ltd.idcu.est.core.api.EstApplication;

public class FirstApp {
    public static void main(String[] args) {
        // 创建 EST 应用实例
        EstApplication app = DefaultEstApplication.create();
        
        // 获取配置并设置应用名称
        app.getConfiguration().set("app.name", "My First EST App");
        app.getConfiguration().set("app.version", "1.0.0");
        
        // 打印应用信息
        String appName = app.getConfiguration().getString("app.name");
        String appVersion = app.getConfiguration().getString("app.version");
        
        System.out.println("================================");
        System.out.println("  " + appName);
        System.out.println("  Version: " + appVersion);
        System.out.println("================================");
        
        // 启动应用
        app.run();
        
        System.out.println("\nApplication is running...");
        System.out.println("Press Ctrl+C to stop.");
    }
}
```

## 步骤 3: 添加生命周期监听器

让我们添加一个生命周期监听器来观察应用的启动和关闭过程：

```java
package com.example;

import ltd.idcu.est.core.DefaultEstApplication;
import ltd.idcu.est.core.api.EstApplication;
import ltd.idcu.est.core.api.lifecycle.LifecycleEvent;
import ltd.idcu.est.core.api.lifecycle.LifecycleListener;

public class FirstAppWithLifecycle {
    public static void main(String[] args) {
        EstApplication app = DefaultEstApplication.create();
        
        // 添加生命周期监听器
        app.addLifecycleListener(new LifecycleListener() {
            @Override
            public void onEvent(LifecycleEvent event) {
                System.out.println("[Lifecycle] " + event.getType() + 
                                   " - " + event.getSource());
            }
        });
        
        // 设置配置
        app.getConfiguration().set("app.name", "Lifecycle Demo");
        
        // 启动应用
        System.out.println("Starting application...");
        app.run();
        System.out.println("Application started!");
        
        // 添加关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down...");
            app.shutdown();
        }));
    }
}
```

## 步骤 4: 编译和运行

使用 Maven 编译项目：

```bash
mvn clean compile
```

运行应用：

```bash
mvn exec:java -Dexec.mainClass="com.example.FirstApp"
```

或者，你可以先打包，然后运行：

```bash
mvn clean package
java -cp target/classes:target/dependency/* com.example.FirstApp
```

## 预期输出

当你运行应用时，你应该看到类似以下的输出：

```
================================
  My First EST App
  Version: 1.0.0
================================
[Lifecycle] INITIALIZING - com.example.FirstApp
[Lifecycle] INITIALIZED - com.example.FirstApp
[Lifecycle] STARTING - com.example.FirstApp
[Lifecycle] STARTED - com.example.FirstApp

Application is running...
Press Ctrl+C to stop.
```

## 下一步

恭喜你！你已经成功创建了你的第一个 EST 应用。

在下一个教程中，我们将学习如何使用 [依赖注入容器](./02-dependency-injection.md)。
