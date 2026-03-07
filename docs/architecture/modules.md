# EST 模块关系与项目搭建指�?
## 📋 目录

1. [🎯 小白必读：为什么要了解模块关系？](#小白必读为什么要了解模块关系)
2. [🌳 EST 完整模块生态图谱](#est-完整模块生态图�?
3. [🔗 模块间依赖关系详解](#模块间依赖关系详�?
4. [🏗�?项目搭建：从0�?的完整教程](#项目搭建�?�?的完整教�?
5. [🎮 实战场景：不同类型项目的模块组合](#实战场景不同类型项目的模块组�?
6. [�?常见问题](#常见问题)
7. [🎯 下一步](#下一�?

---

## 🎯 小白必读：为什么要了解模块关系�?
### 一句话总结

了解模块关系就像**了解工具箱里每个工具的用途和它们怎么配合使用**�?
### 为什么重要？

**场景类比**�?想象你要装修房子�?- 你需要知道：锤子是用来敲钉子的，锯子是用来锯木头�?- 你还需要知道：先用电钻打孔，再用锤子敲钉子，最后用螺丝刀拧紧
- 如果你顺序搞反了，就会事倍功半！

**EST 模块也是一�?*�?- 你需要知道：每个模块是做什么的
- 你还需要知道：模块之间怎么配合
- 只有这样，你才能高效地搭建项目！

### 你将学到什�?
读完这篇指南，你将：
1. �?清楚 EST 有哪些模块，每个是做什么的
2. �?理解模块之间是怎么配合�?3. �?知道根据你的需求，应该选哪些模�?4. �?学会从零开始，一步步搭建项目
5. �?明白"为什么要这么搭建"，而不只是"怎么搭建"

---

## 🌳 EST 完整模块生态图�?
### 整体架构图（大白话版�?
```
┌─────────────────────────────────────────────────────────────────────�?�?                        应用层（est-app�?                           �?�? 直接给用户用�?成品工具"，比�?Web 框架、控制台应用、微服务框架      �?├─────────────────────────────────────────────────────────────────────�?�?                        模块层（est-modules�?                       �?�? 各种"功能工具"：缓存、日志、数据库、安全认证、消息队列、监�?..     �?├─────────────────────────────────────────────────────────────────────�?�?                        核心层（est-core�?                          �?�? "基础设施"：依赖注入容器、配置管理、生命周期管�?.. 所有模块的基础   �?├─────────────────────────────────────────────────────────────────────�?�?                        基础层（est-base�?                          �?�? "底层零件"：工具类、集合增强、设计模式、测试框�?..                �?└─────────────────────────────────────────────────────────────────────�?```

### 详细模块列表

#### 应用层（est-app�? 直接用的成品

| 模块 | 大白话说�?| 什么时候用 |
|------|----------|-----------|
| est-web | Web 开发框架，�?Express/Spring MVC | 要做网站、API �?|
| est-console | 控制台应用框�?| 要做命令行工具时 |
| est-microservice | 微服务框�?| 要做微服务架构时 |

#### 模块层（est-modules�? 各种功能工具

| 模块 | 大白话说�?| 什么时候用 |
|------|----------|-----------|
| est-cache | 缓存，像储物�?| 需要存常用数据，加速访问时 |
| est-logging | 日志，像日记�?| 需要记录程序运行情况时 |
| est-data | 数据访问，像档案管理�?| 需要存数据库、Redis �?|
| est-security | 安全认证，像门卫 | 需要登录、权限控制时 |
| est-messaging | 消息系统，像邮局 | 需要异步处理、解耦时 |
| est-monitor | 监控，像仪表�?| 需要看程序运行状态时 |
| est-scheduler | 调度，像闹钟 | 需要定时执行任务时 |
| est-ai | AI 助手 | 需�?AI 功能�?|
| est-event | 事件总线，像广播 | 需要模块间通信�?|
| est-circuitbreaker | 熔断器，像保险丝 | 需要防止雪崩时 |
| est-discovery | 服务发现 | 微服务时找服务地址 |
| est-config | 配置中心 | 需要分布式配置时 |
| est-gateway | API 网关 | 需要路由、限流时 |
| est-hotreload | 热重载 | 开发时需要自动重新加载 |
| est-performance | 性能监控 | 需要性能指标时 |
| est-plugin | 插件系统 | 需要动态加载功能时 |
| est-workflow | 工作流引擎 | 需要编排业务流程时 |

#### 核心层（est-core�? 所有模块的基础

| 模块 | 大白话说�?| 为什么需�?|
|------|----------|-----------|
| est-core-container | 依赖注入容器，像管家 | 管理各个组件，让它们配合工作 |
| est-core-config | 配置管理 | 管理配置文件、环境变�?|
| est-core-lifecycle | 生命周期管理 | 管理组件的启动、关�?|
| est-core-module | 模块管理 | 管理各个模块的加�?|
| est-core-aop | AOP 支持 | 切面编程，比如日志、事�?|
| est-core-tx | 事务管理 | 数据库事务，保证数据一致�?|

#### 基础层（est-base�? 底层零件

| 模块 | 大白话说�?| 什么时候用 |
|------|----------|-----------|
| est-utils | 工具类（JSON、XML、IO等） | 需要处�?JSON、XML、文件时 |
| est-collection | 集合增强 | 需要更强大的集合操作时 |
| est-patterns | 设计模式 | 需要用到设计模式时 |
| est-test | 测试框架 | 需要写单元测试�?|

---

## 🔗 模块间依赖关系详�?
### 依赖原则（为什么这样设计）

**原则1：下层不依赖上层**
- 基础�?�?核心�?�?模块�?�?应用�?- 就像：地�?�?框架 �?墙壁 �?装修
- 好处：下层稳定，上层可以灵活变化

**原则2：同层之间尽量松耦合**
- 模块层的各个模块，通过核心层的容器配合
- 就像：各个电器通过插座（核心层）配合工�?- 好处：换掉一个模块，不影响其他模�?
**原则3：接口与实现分离**
- 每个模块都有 api（接口）�?impl（实现）
- 就像：插头（接口）和插座（实现）
- 好处：可以换不同的实现，不用改代�?
### 典型依赖关系�?
```
est-web (应用�?
    �?    ├───> est-cache (模块�?
    �?      └──> est-core (核心�?
    �?              └──> est-utils (基础�?
    �?    ├───> est-security (模块�?
    �?      └──> est-core (核心�?
    �?    ├───> est-data (模块�?
    �?      └──> est-core (核心�?
    �?    └───> est-logging (模块�?
            └──> est-core (核心�?
```

### 各层的职责（为什么要分层�?
#### 基础层（est-base�?**职责**：提供通用的工具和基础功能

**为什么需�?*�?- 所有模块都可能需�?JSON 处理、文件操作等
- 放在这一层，大家都能用，不用重复�?
**类比**：就像建筑用的砖块、水�?
---

#### 核心层（est-core�?**职责**：提供框架的核心能力，让各个模块能配合工�?
**为什么需�?*�?- 各个模块需要配合，需要一�?管家"来协�?- 比如：依赖注入、生命周期管�?
**类比**：就像建筑的框架结构、水电管�?
---

#### 模块层（est-modules�?**职责**：提供各种具体的功能

**为什么需�?*�?- 不同项目需要不同的功能
- 想要什么功能，就引入什么模块，灵活组合

**类比**：就像建筑的各种设备（空调、电梯、消防系统）

---

#### 应用层（est-app�?**职责**：提供最终的应用框架，让用户直接�?
**为什么需�?*�?- 很多用户不想自己组装模块
- 直接给个成品，开箱即�?
**类比**：就像精装修的房子，拎包入住

---

## 🏗�?项目搭建：从0�?的完整教�?
### 第一步：明确需求（先想清楚要做什么）

**场景**：我要做一�?[项目类型]，需�?[功能1]、[功能2]、[功能3]

**需求分析表**�?
| 功能需�?| 需要哪个模�?| 为什么选它 | 有没有其他选择 |
|---------|------------|----------|--------------|
| [功能1] | [模块1] | [原因] | [其他选择，什么时候用] |
| [功能2] | [模块2] | [原因] | [其他选择，什么时候用] |
| [功能3] | [模块3] | [原因] | [其他选择，什么时候用] |

---

### 第二步：创建项目结构（搭架子�?
#### 2.1 创建 Maven 项目

```
my-project/
├── pom.xml                 # Maven 配置文件
└── src/
    ├── main/
    �?  ├── java/
    �?  �?  └── com/
    �?  �?      └── example/
    �?  �?          └── myproject/
    �?  �?              ├── MyApplication.java  # 主程�?    �?  �?              ├── service/            # 服务�?    �?  �?              ├── repository/         # 数据访问�?    �?  �?              └── controller/         # 控制器（Web项目�?    �?  └── resources/
    �?      └── application.properties          # 配置文件
    └── test/
        └── java/
            └── com/
                └── example/
                    └── myproject/
                        └── MyApplicationTest.java
```

---

#### 2.2 配置 pom.xml（引入依赖）

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>my-project</artifactId>
    <version>1.0.0</version>

    <properties>
        <est.version>2.0.0</est.version>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
    </properties>

    <dependencies>
        <!-- 核心层：必须引入 -->
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

        <!-- 模块1：根据需求引�?-->
        <dependency>
            <groupId>ltd.idcu</groupId>
            <artifactId>est-[模块1]-api</artifactId>
            <version>${est.version}</version>
        </dependency>
        <dependency>
            <groupId>ltd.idcu</groupId>
            <artifactId>est-[模块1]-[实现]</artifactId>
            <version>${est.version}</version>
        </dependency>

        <!-- 模块2：根据需求引�?-->
        <dependency>
            <groupId>ltd.idcu</groupId>
            <artifactId>est-[模块2]-api</artifactId>
            <version>${est.version}</version>
        </dependency>
        <dependency>
            <groupId>ltd.idcu</groupId>
            <artifactId>est-[模块2]-[实现]</artifactId>
            <version>${est.version}</version>
        </dependency>

        <!-- 测试依赖 -->
        <dependency>
            <groupId>ltd.idcu</groupId>
            <artifactId>est-test-api</artifactId>
            <version>${est.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

**为什么要�?api �?impl**�?- api 是接口定义，稳定不变
- impl 是具体实现，可以替换
- 好处：想换实现时，只需要改 impl 的依赖，不用改代�?
---

### 第三步：编写主程序（把模块组装起来）

```java
package com.example.myproject;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;
import ltd.idcu.est.[模块1].api.[接口1];
import ltd.idcu.est.[模块1].[实现].[工厂�?];
import ltd.idcu.est.[模块2].api.[接口2];
import ltd.idcu.est.[模块2].[实现].[工厂�?];

public class MyApplication {
    public static void main(String[] args) {
        System.out.println("=== 项目启动�?.. ===\n");

        // 1. 创建容器（就像准备一个工具箱�?        // 为什么用容器？让各个模块能配合工�?        Container container = new DefaultContainer();

        // 2. 创建各个模块（就像准备工具）
        // 为什么先创建模块？需要先有工具，才能放进工具�?        [接口1] module1 = [工厂�?].new[实现1]();
        [接口2] module2 = [工厂�?].new[实现2]();

        // 3. 注册到容器（就像把工具放进工具箱�?        // 为什么要注册？让容器知道这些模块，需要时可以拿出�?        container.registerSingleton([接口1].class, module1);
        container.registerSingleton([接口2].class, module2);

        // 4. 使用模块（就像从工具箱拿工具干活�?        System.out.println("=== 项目启动成功！开始使�?.. ===\n");

        // 使用模块1
        [接口1] m1 = container.get([接口1].class);
        m1.[方法]();

        // 使用模块2
        [接口2] m2 = container.get([接口2].class);
        m2.[方法]();

        System.out.println("\n=== 程序执行完成�?==");
    }
}
```

---

### 第四步：让模块配合工作（模块间协作）

**场景**：模�? 产生的数据，传给模块2 处理

```java
package com.example.myproject;

import ltd.idcu.est.[模块1].api.[接口1];
import ltd.idcu.est.[模块1].[实现].[工厂�?];
import ltd.idcu.est.[模块2].api.[接口2];
import ltd.idcu.est.[模块2].[实现].[工厂�?];

public class ModuleIntegrationExample {
    public static void main(String[] args) {
        System.out.println("=== 模块协作示例 ===\n");

        // 1. 创建两个模块
        [接口1] module1 = [工厂�?].new[实现1]();
        [接口2] module2 = [工厂�?].new[实现2]();

        // 2. 模块1 产生数据
        System.out.println("--- 步骤1：模�? 产生数据 ---");
        [数据类型] data = module1.[产生数据的方法]();
        System.out.println("产生的数据：" + data);

        // 3. 模块2 处理数据
        System.out.println("\n--- 步骤2：模�? 处理数据 ---");
        [结果类型] result = module2.[处理数据的方法](data);
        System.out.println("处理结果�? + result);

        System.out.println("\n�?模块协作成功�?);
    }
}
```

**为什么这样设计模块协�?*�?1. **单一职责**：每个模块只做一件事
2. **松耦合**：模块之间通过接口通信，不依赖具体实现
3. **易替�?*：换掉一个模块，不影响其他模�?4. **易测�?*：每个模块可以单独测�?
---

### 第五步：添加配置（让项目更灵活）

#### 5.1 创建配置文件

`src/main/resources/application.properties`�?```properties
# 应用配置
app.name=我的应用
app.version=1.0.0

# 模块1配置
[模块1].[配置�?]=[�?]
[模块1].[配置�?]=[�?]

# 模块2配置
[模块2].[配置�?]=[�?]
```

#### 5.2 在代码中使用配置

```java
package com.example.myproject;

import ltd.idcu.est.core.api.Config;
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultConfig;
import ltd.idcu.est.core.impl.DefaultContainer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigExample {
    public static void main(String[] args) throws IOException {
        // 1. 加载配置文件
        Properties properties = new Properties();
        try (InputStream is = ConfigExample.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {
            properties.load(is);
        }

        // 2. 创建 Config 对象
        Config config = new DefaultConfig(properties);

        // 3. 创建容器，传入配�?        Container container = new DefaultContainer(config);

        // 4. 使用配置
        String appName = config.getString("app.name", "默认应用�?);
        System.out.println("应用名：" + appName);

        // 5. 也可以把 Config 注册到容器，让其他模块使�?        container.registerSingleton(Config.class, config);
    }
}
```

**为什么用配置文件**�?- �?不用改代码，就能调整参数
- �?不同环境（开发、测试、生产）用不同配�?- �?更灵活，更易维护

---

## 🎮 实战场景：不同类型项目的模块组合

### 场景1：简�?Web 应用（入门级�?
**需�?*：做一个简单的网站，有基本�?Web 功能

**需要的模块**�?
| 模块 | 为什么需�?| 推荐实现 |
|------|----------|---------|
| est-core | 核心，必�?| est-core-impl |
| est-web | Web 框架 | est-web-impl |
| est-logging | 记录日志 | est-logging-console |

**项目结构**�?```
simple-web-app/
├── pom.xml
└── src/
    └── main/
        ├── java/
        �?  └── com/
        �?      └── example/
        �?          └── simpleweb/
        �?              └── SimpleWebApp.java
        └── resources/
            └── application.properties
```

**代码示例**�?```java
package com.example.simpleweb;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class SimpleWebApp {
    public static void main(String[] args) {
        // 1. 创建 Web 应用
        WebApplication app = Web.create("简单网�?, "1.0.0");

        // 2. 添加路由
        app.get("/", (req, res) -> res.send("Hello, World!"));
        app.get("/hello/:name", (req, res) -> 
            res.send("你好�? + req.pathParam("name")));

        // 3. 启动
        app.run(8080);
        System.out.println("访问 http://localhost:8080");
    }
}
```

---

### 场景2：带数据库的 Web 应用（进阶级�?
**需�?*：做一个网站，需要存数据到数据库，需要缓存，需要安全认�?
**需要的模块**�?
| 模块 | 为什么需�?| 推荐实现 |
|------|----------|---------|
| est-core | 核心，必�?| est-core-impl |
| est-web | Web 框架 | est-web-impl |
| est-data | 数据访问 | est-data-jdbc |
| est-cache | 缓存 | est-cache-memory |
| est-security | 安全认证 | est-security-jwt |
| est-logging | 记录日志 | est-logging-file |

**为什么这样组�?*�?1. **est-data-jdbc**：用数据库存数据，持久化
2. **est-cache-memory**：缓存常用数据，加速访�?3. **est-security-jwt**：JWT Token 认证，适合前后端分�?4. **est-logging-file**：日志写文件，方便查问题

---

### 场景3：微服务应用（高级级�?
**需�?*：做微服务架构，需要服务发现、消息队列、熔断器、监�?
**需要的模块**�?
| 模块 | 为什么需�?| 推荐实现 |
|------|----------|---------|
| est-core | 核心，必�?| est-core-impl |
| est-web | Web 框架 | est-web-impl |
| est-data | 数据访问 | est-data-jdbc |
| est-cache | 缓存 | est-cache-redis |
| est-security | 安全认证 | est-security-oauth2 |
| est-messaging | 消息队列 | est-messaging-kafka |
| est-discovery | 服务发现 | est-discovery-[实现] |
| est-circuitbreaker | 熔断�?| est-circuitbreaker-[实现] |
| est-monitor | 监控 | est-monitor-jvm |
| est-logging | 记录日志 | est-logging-file |

**为什么这样组�?*�?1. **est-cache-redis**：Redis 缓存，多实例共享
2. **est-security-oauth2**：OAuth2，适合微服�?3. **est-messaging-kafka**：Kafka，高吞吐消息队列
4. **est-circuitbreaker**：熔断器，防止雪�?5. **est-monitor**：监控，看服务状�?
---

## �?常见问题

### Q1：我应该从哪一层开始学�?
**A**：推荐顺序：
1. **先看 est-base**：了解基础工具，容易上�?2. **再看 est-core**：理解核心概念，这是框架的灵�?3. **然后�?est-modules**：根据需求学具体功能
4. **最后看 est-app**：直接用成品框架

**为什么这�?*：就像学开车，先学认识零件，再学发动机原理，然后学各种功能，最后直接开整车�?
---

### Q2：我可以只用某一个模块吗�?
**A**：当然可以！

**EST 的设计理念就是模块化**�?- 想用哪个模块，就引入哪个
- 不需要引入整个框�?- 甚至可以只引�?est-utils，用它的工具�?
**例子**�?```xml
<!-- 只引入工具类 -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-util-common</artifactId>
    <version>2.0.0</version>
</dependency>
```

---

### Q3：怎么判断我需要哪些模块？

**A**：按这个步骤�?
1. **列需�?*：把你需要的功能列出�?2. **找模�?*：看模块列表，找对应的模�?3. **选实�?*：看有哪些实现，根据需求�?4. **画依赖图**：看这些模块怎么配合

**举个例子**�?- 需求：做个网站，要存数据，要登�?- 模块：est-web + est-data + est-security
- 实现：est-web-impl + est-data-jdbc + est-security-jwt

---

### Q4：模块之间会冲突吗？

**A**：不会！

**为什�?*�?1. **下层不依赖上�?*：基础层不依赖模块层，模块层不依赖应用�?2. **接口与实现分�?*：用接口通信，不依赖具体实现
3. **容器协调**：est-core 的容器负责协调各个模�?
**就像**：各个电器通过插座配合，不会冲突�?
---

## 🎯 下一�?
### 如果你想继续学习

- 📖 �?[具体模块的文档](../modules/) 深入学习
- 🎓 跟着 [教程](../tutorials/) 一步步�?- 💻 �?[示例代码](../../est-examples/) 有更多例�?
### 如果你想动手实践

- 🚀 跟着 [项目搭建教程](#项目搭建�?�?的完整教�? 做个项目
- 🔧 尝试 [不同的模块组合](#实战场景不同类型项目的模块组�?
- 💡 自己想个需求，试着�?EST 实现

---

**文档版本**: 2.0  
**最后更�?*: 2026-03-07  
**维护�?*: EST 架构团队  
**适合人群**: 小白 �?中级 �?高级开发�?