# EST 示例代码

欢迎来到 EST 示例代码中心！这里有丰富的示例代码，帮助你学习和使用 EST 框架。

## 📋 目录

1. [什么是 EST 示例代码？](#什么是-est-示例代码)
2. [快速入门：5分钟上手](#快速入门5分钟上手)
3. [基础示例](#基础示例)
4. [Web 示例](#web-示例)
5. [功能示例](#功能示例)
6. [高级示例](#高级示例)
7. [AI 示例](#ai-示例)
8. [微服务示例](#微服务示例)
9. [GraalVM 示例](#graalvm-示例)
10. [Admin 示例](#admin-示例)
11. [如何运行示例](#如何运行示例)
12. [最佳实践](#最佳实践)
13. [常见问题](#常见问题)
14. [下一步](#下一步)

---

## 什么是 EST 示例代码？

### 用大白话理解

EST 示例代码就像是一本"代码菜谱"。想象一下你在学习做菜：

**传统方式**：看着菜谱文字描述，自己脑补怎么做，经常出错。
**EST 示例代码方式**：直接给你完整的可运行代码，你可以：
- 直接运行看效果
- 修改代码看变化
- 理解每个部分的作用

它支持多种难度和场景：入门级、进阶级、高级级、专家级，想用哪种用哪种。

### 核心特点

- 🎯 **简单易懂** - 每个示例都有清晰的注释和说明
- 🚀 **可直接运行** - 复制就能跑，不用额外配置
- 📚 **覆盖全面** - 从基础到高级，从简单到复杂
- 🎓 **循序渐进** - 按照难度分级，适合不同水平
- 📈 **真实场景** - 示例来自实际项目需求

---

## 快速入门：5分钟上手

### 第一步：克隆项目

```bash
git clone https://github.com/idcu/est.git
cd est2.0
```

### 第二步：构建项目

```bash
mvn clean install -DskipTests
```

### 第三步：运行你的第一个示例

```java
import ltd.idcu.est.core.container.api.Container;
import ltd.idcu.est.core.container.impl.DefaultContainer;

public class FirstExample {
    public static void main(String[] args) {
        System.out.println("=== EST 第一个示例 ===\n");
        
        // 1. 创建容器
        Container container = new DefaultContainer();
        
        // 2. 注册一个字符串
        container.registerSingleton(String.class, "你好，EST！");
        
        // 3. 获取并使用
        String message = container.get(String.class);
        System.out.println("结果：" + message);
        
        System.out.println("\n恭喜你！你已经成功使用 EST 了！");
    }
}
```

运行这个程序，你会看到：
```
=== EST 第一个示例 ===

结果：你好，EST！
恭喜你！你已经成功使用 EST 了！
```

---

## 基础示例

**难度**: ⭐ (新手友好)

### 包含的示例

| 示例 | 说明 | 知识点 | 难度 |
|------|------|--------|------|
| [Core01_FirstExample](../../est-examples/est-examples-basic/src/main/java/ltd/idcu/est/examples/basic/core/Core01_FirstExample.java) | 第一个容器示例 | 容器基础 | ⭐ |
| [Core02_BasicRegistration](../../est-examples/est-examples-basic/src/main/java/ltd/idcu/est/examples/basic/core/Core02_BasicRegistration.java) | 基础注册 | 依赖注册 | ⭐ |
| [Core03_ConstructorInjection](../../est-examples/est-examples-basic/src/main/java/ltd/idcu/est/examples/basic/core/Core03_ConstructorInjection.java) | 构造器注入 | 构造器注入 | ⭐ |
| [Core04_AnnotationInjection](../../est-examples/est-examples-basic/src/main/java/ltd/idcu/est/examples/basic/core/Core04_AnnotationInjection.java) | 注解注入 | 注解注入 | ⭐ |
| [Core05_ScopeExample](../../est-examples/est-examples-basic/src/main/java/ltd/idcu/est/examples/basic/core/Core05_ScopeExample.java) | Bean 作用域 | Singleton/Prototype | ⭐ |
| [Core06_LifecycleExample](../../est-examples/est-examples-basic/src/main/java/ltd/idcu/est/examples/basic/core/Core06_LifecycleExample.java) | 生命周期 | 初始化/销毁 | ⭐ |
| [Core07_ConfigExample](../../est-examples/est-examples-basic/src/main/java/ltd/idcu/est/examples/basic/core/Core07_ConfigExample.java) | 配置管理 | 配置读写 | ⭐ |
| [Core08_CollectionIntegration](../../est-examples/est-examples-basic/src/main/java/ltd/idcu/est/examples/basic/core/Core08_CollectionIntegration.java) | 集合集成 | est-collection 集成 | ⭐⭐ |
| [Cache01_FirstExample](../../est-examples/est-examples-basic/src/main/java/ltd/idcu/est/examples/basic/cache/Cache01_FirstExample.java) | 缓存第一个示例 | 缓存基础 | ⭐ |
| [Cache02_BasicOperations](../../est-examples/est-examples-basic/src/main/java/ltd/idcu/est/examples/basic/cache/Cache02_BasicOperations.java) | 缓存基础操作 | CRUD 操作 | ⭐ |
| [Event01_FirstExample](../../est-examples/est-examples-basic/src/main/java/ltd/idcu/est/examples/basic/event/Event01_FirstExample.java) | 事件第一个示例 | 事件总线 | ⭐ |
| [Data01_FirstExample](../../est-examples/est-examples-basic/src/main/java/ltd/idcu/est/examples/basic/data/Data01_FirstExample.java) | 数据访问第一个示例 | 数据访问 | ⭐ |
| [Collection01_FirstExample](../../est-examples/est-examples-basic/src/main/java/ltd/idcu/est/examples/basic/collection/Collection01_FirstExample.java) | 集合第一个示例 | 集合操作 | ⭐ |
| [Web01_FirstExample](../../est-examples/est-examples-basic/src/main/java/ltd/idcu/est/examples/basic/web/Web01_FirstExample.java) | Web 第一个示例 | Web 基础 | ⭐ |
| [Patterns01_FirstExample](../../est-examples/est-examples-basic/src/main/java/ltd/idcu/est/examples/basic/patterns/Patterns01_FirstExample.java) | 设计模式第一个示例 | 设计模式 | ⭐ |
| [Utils01_FirstExample](../../est-examples/est-examples-basic/src/main/java/ltd/idcu/est/examples/basic/utils/Utils01_FirstExample.java) | 工具类第一个示例 | 工具类 | ⭐ |

### 如何运行

```bash
cd est-examples/est-examples-basic
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.basic.Main"
```

---

## Web 示例

**难度**: ⭐⭐ (有一定基础)

### 包含的示例

| 示例 | 说明 | 知识点 | 难度 |
|------|------|--------|------|
| [BasicWebAppExample](../../est-examples/est-examples-web/src/main/java/ltd/idcu/est/examples/web/BasicWebAppExample.java) | 基础 Web 应用 | 路由、请求响应 | ⭐⭐ |
| [TodoAppExample](../../est-examples/est-examples-web/src/main/java/ltd/idcu/est/examples/web/TodoAppExample.java) | Todo 应用 | CRUD、表单 | ⭐⭐ |
| [BlogAppExample](../../est-examples/est-examples-web/src/main/java/ltd/idcu/est/examples/web/BlogAppExample.java) | 博客应用 | 模板引擎 | ⭐⭐ |
| [KanbanAppExample](../../est-examples/est-examples-web/src/main/java/ltd/idcu/est/examples/web/KanbanAppExample.java) | 看板应用 | 复杂交互 | ⭐⭐⭐ |
| [ChatAppExample](../../est-examples/est-examples-web/src/main/java/ltd/idcu/est/examples/web/ChatAppExample.java) | 聊天应用 | WebSocket | ⭐⭐⭐ |
| [MvcExample](../../est-examples/est-examples-web/src/main/java/ltd/idcu/est/examples/web/MvcExample.java) | MVC 示例 | MVC 架构 | ⭐⭐ |
| [RestApiExample](../../est-examples/est-examples-web/src/main/java/ltd/idcu/est/examples/web/RestApiExample.java) | REST API 示例 | RESTful API | ⭐⭐ |
| [FileUploadExample](../../est-examples/est-examples-web/src/main/java/ltd/idcu/est/examples/web/FileUploadExample.java) | 文件上传示例 | 文件处理 | ⭐⭐ |
| [MiddlewareExample](../../est-examples/est-examples-web/src/main/java/ltd/idcu/est/examples/web/MiddlewareExample.java) | 中间件示例 | 中间件 | ⭐⭐ |
| [EnhancedWebExample](../../est-examples/est-examples-web/src/main/java/ltd/idcu/est/examples/web/EnhancedWebExample.java) | 增强 Web 示例 | 高级特性 | ⭐⭐⭐ |

### 如何运行

```bash
cd est-examples/est-examples-web
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.web.BasicWebAppExample"
```

然后访问 http://localhost:8080

---

## 功能示例

**难度**: ⭐⭐ (有一定基础)

### 包含的示例

| 示例 | 说明 | 知识点 | 难度 |
|------|------|--------|------|
| [CacheExample](../../est-examples/est-examples-features/src/main/java/ltd/idcu/est/examples/features/CacheExample.java) | 缓存示例 | 缓存使用 | ⭐⭐ |
| [EnhancedCacheExample](../../est-examples/est-examples-features/src/main/java/ltd/idcu/est/examples/features/EnhancedCacheExample.java) | 增强缓存示例 | 缓存高级特性 | ⭐⭐⭐ |
| [EventExample](../../est-examples/est-examples-features/src/main/java/ltd/idcu/est/examples/features/EventExample.java) | 事件示例 | 事件总线 | ⭐⭐ |
| [LoggingExample](../../est-examples/est-examples-features/src/main/java/ltd/idcu/est/examples/features/LoggingExample.java) | 日志示例 | 日志配置 | ⭐⭐ |
| [DataExample](../../est-examples/est-examples-features/src/main/java/ltd/idcu/est/examples/features/DataExample.java) | 数据示例 | 数据访问 | ⭐⭐ |
| [EnhancedDataExample](../../est-examples/est-examples-features/src/main/java/ltd/idcu/est/examples/features/EnhancedDataExample.java) | 增强数据示例 | 数据高级特性 | ⭐⭐⭐ |
| [CompleteDataExample](../../est-examples/est-examples-features/src/main/java/ltd/idcu/est/examples/features/CompleteDataExample.java) | 完整数据示例 | 数据完整功能 | ⭐⭐⭐ |
| [SecurityExample](../../est-examples/est-examples-features/src/main/java/ltd/idcu/est/examples/features/SecurityExample.java) | 安全示例 | 安全基础 | ⭐⭐ |
| [CompleteSecurityExample](../../est-examples/est-examples-features/src/main/java/ltd/idcu/est/examples/features/CompleteSecurityExample.java) | 完整安全示例 | 安全完整功能 | ⭐⭐⭐ |
| [SchedulerExample](../../est-examples/est-examples-features/src/main/java/ltd/idcu/est/examples/features/SchedulerExample.java) | 调度示例 | 定时任务 | ⭐⭐ |
| [CompleteSchedulerExample](../../est-examples/est-examples-features/src/main/java/ltd/idcu/est/examples/features/CompleteSchedulerExample.java) | 完整调度示例 | 调度完整功能 | ⭐⭐⭐ |
| [MonitorExample](../../est-examples/est-examples-features/src/main/java/ltd/idcu/est/examples/features/MonitorExample.java) | 监控示例 | 监控基础 | ⭐⭐ |
| [EnhancedMonitorExample](../../est-examples/est-examples-features/src/main/java/ltd/idcu/est/examples/features/EnhancedMonitorExample.java) | 增强监控示例 | 监控高级特性 | ⭐⭐⭐ |
| [CompleteMonitorExample](../../est-examples/est-examples-features/src/main/java/ltd/idcu/est/examples/features/CompleteMonitorExample.java) | 完整监控示例 | 监控完整功能 | ⭐⭐⭐ |
| [MessagingExample](../../est-examples/est-examples-features/src/main/java/ltd/idcu/est/examples/features/MessagingExample.java) | 消息示例 | 消息队列 | ⭐⭐ |
| [CompleteMessagingExample](../../est-examples/est-examples-features/src/main/java/ltd/idcu/est/examples/features/CompleteMessagingExample.java) | 完整消息示例 | 消息完整功能 | ⭐⭐⭐ |
| [ComprehensiveFeaturesExample](../../est-examples/est-examples-features/src/main/java/ltd/idcu/est/examples/features/ComprehensiveFeaturesExample.java) | 综合功能示例 | 多模块集成 | ⭐⭐⭐ |

### 如何运行

```bash
cd est-examples/est-examples-features
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.features.CacheExample"
```

---

## 高级示例

**难度**: ⭐⭐⭐ (有经验的开发者)

### 包含的示例

| 示例 | 说明 | 知识点 | 难度 |
|------|------|--------|------|
| [CompleteApplicationExample](../../est-examples/est-examples-advanced/src/main/java/ltd/idcu/est/examples/advanced/CompleteApplicationExample.java) | 完整应用示例 | 完整应用架构 | ⭐⭐⭐ |
| [ModuleIntegrationExample](../../est-examples/est-examples-advanced/src/main/java/ltd/idcu/est/examples/advanced/ModuleIntegrationExample.java) | 模块集成示例 | 模块集成 | ⭐⭐⭐ |
| [MultiModuleIntegrationExample](../../est-examples/est-examples-advanced/src/main/java/ltd/idcu/est/examples/advanced/MultiModuleIntegrationExample.java) | 多模块集成示例 | 多模块集成 | ⭐⭐⭐ |
| [CustomExtensionExample](../../est-examples/est-examples-advanced/src/main/java/ltd/idcu/est/examples/advanced/CustomExtensionExample.java) | 自定义扩展示例 | 框架扩展 | ⭐⭐⭐ |
| [NewArchitectureExample](../../est-examples/est-examples-advanced/src/main/java/ltd/idcu/est/examples/advanced/NewArchitectureExample.java) | 新架构示例 | 新架构 | ⭐⭐⭐ |
| [PerformanceOptimizationExample](../../est-examples/est-examples-advanced/src/main/java/ltd/idcu/est/examples/advanced/PerformanceOptimizationExample.java) | 性能优化示例 | 性能调优 | ⭐⭐⭐ |

### 如何运行

```bash
cd est-examples/est-examples-advanced
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.advanced.CompleteApplicationExample"
```

---

## AI 示例

**难度**: ⭐⭐⭐ (有经验的开发者)

### 包含的示例

| 示例 | 说明 | 知识点 | 难度 |
|------|------|--------|------|
| [AiQuickStartExample](../../est-examples/est-examples-ai/src/main/java/ltd/idcu/est/examples/ai/AiQuickStartExample.java) | AI 快速开始 | AI 基础 | ⭐⭐ |
| [AiAssistantWebExample](../../est-examples/est-examples-ai/src/main/java/ltd/idcu/est/examples/ai/AiAssistantWebExample.java) | AI 助手 Web 应用 | AI + Web | ⭐⭐⭐ |
| [CodeGeneratorExample](../../est-examples/est-examples-ai/src/main/java/ltd/idcu/est/examples/ai/CodeGeneratorExample.java) | 代码生成示例 | 代码生成 | ⭐⭐⭐ |
| [PromptTemplateExample](../../est-examples/est-examples-ai/src/main/java/ltd/idcu/est/examples/ai/PromptTemplateExample.java) | 提示词模板示例 | 提示词工程 | ⭐⭐⭐ |

### 如何运行

```bash
cd est-examples/est-examples-ai
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.AiQuickStartExample"
```

---

## 微服务示例

**难度**: ⭐⭐⭐⭐ (高级开发者)

### 包含的示例

| 示例 | 说明 | 知识点 | 难度 |
|------|------|--------|------|
| [Gateway](../../est-examples/est-examples-microservices/est-examples-microservices-gateway/) | API 网关 | 网关服务 | ⭐⭐⭐⭐ |
| [User Service](../../est-examples/est-examples-microservices/est-examples-microservices-user/) | 用户服务 | 业务服务 | ⭐⭐⭐⭐ |
| [Order Service](../../est-examples/est-examples-microservices/est-examples-microservices-order/) | 订单服务 | 业务服务 | ⭐⭐⭐⭐ |

### 如何运行

```bash
cd est-examples/est-examples-microservices
mvn clean install
```

然后分别启动各个服务。

---

## GraalVM 示例

**难度**: ⭐⭐⭐ (有经验的开发者)

### 包含的示例

| 示例 | 说明 | 知识点 | 难度 |
|------|------|--------|------|
| [HelloWorldNative](../../est-examples/est-examples-graalvm/src/main/java/ltd/idcu/est/examples/graalvm/HelloWorldNative.java) | Hello World 原生镜像 | 原生镜像 | ⭐⭐⭐ |
| [WebAppNative](../../est-examples/est-examples-graalvm/src/main/java/ltd/idcu/est/examples/graalvm/WebAppNative.java) | Web 应用原生镜像 | Web + 原生 | ⭐⭐⭐ |

### 如何运行

```bash
cd est-examples/est-examples-graalvm
mvn native:compile
```

---

## Admin 示例

**难度**: ⭐⭐⭐ (有经验的开发者)

### 包含的内容

EST Admin 是一个完整的前后端分离的管理系统示例，包含：

| 组件 | 说明 | 位置 |
|------|------|------|
| 后端服务 | 提供 RESTful API 和 JWT 认证 | [est-app/est-admin](../../est-app/est-admin/) |
| 前端 UI | Vue 3 + Element Plus 管理界面 | [est-admin-ui](../../est-admin-ui/) |
| 联调文档 | 详细的前后端联调指南 | [admin-integration.md](../guides/admin-integration.md) |

### 功能特性

- ✅ 用户认证（JWT Token）
- ✅ 用户管理（CRUD）
- ✅ 角色管理（CRUD）
- ✅ 菜单管理（CRUD）
- ✅ 部门管理（CRUD）
- ✅ 租户管理（CRUD）
- ✅ 路由守卫
- ✅ 权限控制

### 如何运行

#### 1. 启动后端服务

```bash
cd est-app/est-admin/est-admin-impl
mvn compile exec:java -Dexec.mainClass="ltd.idcu.est.admin.DefaultAdminApplication"
```

后端服务将在 http://localhost:8080 启动

#### 2. 启动前端项目

```bash
cd est-admin-ui
npm install
npm run dev
```

前端项目将在 http://localhost:5173 启动

#### 3. 访问应用

打开浏览器访问：http://localhost:5173

默认登录账号：
- 用户名：`admin`
- 密码：`admin123`

详细文档请参考：[EST Admin 前后端联调指南](../guides/admin-integration.md)

---

## 如何运行示例

### 前置条件

- ✅ JDK 21+
- ✅ Maven 3.6+
- ✅ 已经构建了 EST 项目（`mvn clean install`）

### 克隆项目

```bash
git clone https://github.com/idcu/est.git
cd est2.0
```

### 构建项目

```bash
mvn clean install -DskipTests
```

### 运行示例

```bash
# 进入示例目录
cd est-examples/est-examples-basic

# 运行示例
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.basic.Main"
```

### 在 IDE 中运行

1. 用 IDE 打开项目
2. 找到示例类
3. 直接运行 main 方法

---

## 最佳实践

### 1. 从简单开始

```java
// ✅ 推荐：先用简单实现
Container container = new DefaultContainer();
container.registerSingleton(String.class, "Hello");

// ❌ 不推荐：一上来就用复杂配置
Container container = DefaultContainer.builder()
    .scanPackages("com.example")
    .enableLazyLoading()
    .build();
```

**为什么？**
- 先跑通，再优化
- 简单实现更容易理解和调试
- 等你熟悉了，再考虑高级配置

### 2. 有意义的命名

```java
// ✅ 推荐：有意义的变量名
Cache<String, User> userCache = Caches.newMemoryCache();
userCache.put("user:1", new User("张三"));

// ❌ 不推荐：变量名看不懂
Cache<String, User> c = Caches.newMemoryCache();
c.put("u:1", new User("zs"));
```

### 3. 正确使用 Optional

```java
// ✅ 推荐：使用 Optional 处理可能为空的情况
Optional<User> userOpt = userCache.get("user:1");
userOpt.ifPresent(user -> System.out.println(user));

// 或提供默认值
User user = userOpt.orElse(defaultUser);

// ❌ 不推荐：直接 get，可能抛异常
User user = userCache.get("user:1").get();
```

### 4. 记得清理资源

```java
// ✅ 推荐：使用 try-with-resources
try (Container container = new DefaultContainer()) {
    container.registerSingleton(String.class, "Hello");
    String message = container.get(String.class);
    System.out.println(message);
}

// ❌ 不推荐：不关闭资源，可能导致泄漏
Container container = new DefaultContainer();
// 使用完就不管了
```

---

## 常见问题

### Q: 示例运行失败怎么办？

A: 首先确保：
1. 你已经运行了 `mvn clean install`
2. JDK 版本是 21+
3. Maven 版本是 3.6+
4. 检查是否有编译错误

### Q: 如何找到我需要的示例？

A: 
1. 先看难度分级，选择适合你的
2. 看示例说明，找到相关功能
3. 从基础示例开始，循序渐进

### Q: 示例代码可以直接用到项目中吗？

A: 可以！但建议：
1. 先理解示例代码
2. 根据你的项目需求调整
3. 结合最佳实践使用

---

## 下一步

- 🚀 从 [基础示例](#基础示例) 开始
- 📖 看不懂的地方，看 [教程](../tutorials/README.md)
- 💡 想了解更多，看 [模块文档](../modules/README.md)
- 🎓 想学最佳实践，看 [最佳实践课程](../best-practices/course/README.md)
- 🏢 想搭建管理系统，看 [Admin 示例](#admin-示例)

---

**文档版本**: 2.0  
**最后更新**: 2026-03-07  
**维护者**: EST 架构团队
