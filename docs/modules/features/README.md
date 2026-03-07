# 功能模块

欢迎来到 EST 功能模块文档！功能模块提供了各种具体的功能，就像房子里的各种设备（空调、电梯、消防系统）�?
## 📋 目录

1. [功能模块概览](#功能模块概览)
2. [est-cache](#est-cache)
3. [est-logging](#est-logging)
4. [est-data](#est-data)
5. [est-security](#est-security)
6. [其他功能模块](#其他功能模块)
7. [快速开始](#快速开�?
8. [下一步](#下一�?

---

## 功能模块概览

**一句话总结**：功能模块就�?*乐高积木**，你需要什么功能，就引入什么模块，灵活组合�?
### 为什么需要功能模块？

**场景**�?- 你需要缓存，但不需要消息队�?- 你需要日志，但不需�?AI
- 你需要数据库访问，但不需要服务发�?
**解决方案**�?- 按需引入，需要什么用什�?- 每个模块独立，互不影�?- 多种实现可选，灵活选择

---

## est-cache

### 一句话总结

**缓存，就像一个储物柜**，把常用的数据存起来，下次用的时候直接拿，不用每次都去数据库查�?
### 小白必读：什么是缓存�?
**场景类比**�?- 你经常看的书，放在书桌上（缓存）
- 不经常看的书，放在书柜里（数据库�?- 下次要看，直接从书桌上拿，不用去书柜�?
**为什么需要缓�?*�?- 快：内存读写比数据库�?100 倍以�?- 减轻数据库压力：减少数据库查�?- 提升用户体验：页面加载更�?
### 快速示�?
```java
import ltd.idcu.est.cache.api.Cache;
import ltd.idcu.est.cache.api.Caches;

// 1. 创建内存缓存
Cache<String, User> userCache = Caches.newMemoryCache();

// 2. 存数�?userCache.put("user:123", new User("张三"));

// 3. 取数�?Optional<User> user = userCache.get("user:123");

// 4. 如果没有，从数据库加�?User cachedUser = userCache.get("user:456")
    .orElseGet(() -> {
        User dbUser = userRepository.findById("456");
        userCache.put("user:456", dbUser);
        return dbUser;
    });
```

### 多种实现可�?
| 实现 | 说明 | 什么时候用 |
|-----|------|-----------|
| est-cache-memory | 内存缓存 | 单机应用，数据量�?|
| est-cache-redis | Redis 缓存 | 分布式应用，需要共享缓�?|
| est-cache-file | 文件缓存 | 需要持久化，数据量�?|

---

## est-logging

### 一句话总结

**日志，就像一本日记本**，记录程序运行时发生的事情，出问题时可以查日志找原因�?
### 小白必读：什么是日志�?
**场景类比**�?- 飞机有黑匣子，记录飞行数�?- 程序有日志，记录运行数据
- 出问题时，看日志就能知道发生了什�?
**为什么需要日�?*�?- 调试：开发时看程序执行流�?- 排查问题：线上出问题时查原因
- 监控：看程序运行状�?- 审计：记录关键操�?
### 快速示�?
```java
import ltd.idcu.est.logging.api.Logger;
import ltd.idcu.est.logging.api.Loggers;

// 1. 获取 Logger
Logger logger = Loggers.getLogger(MyService.class);

// 2. 记录日志
logger.debug("调试信息：{}", value);      // 调试�?logger.info("普通信息：{}", value);       // 重要信息
logger.warn("警告信息：{}", value);       // 警告
logger.error("错误信息：{}", error);     // 错误
```

### 多种实现可�?
| 实现 | 说明 | 什么时候用 |
|-----|------|-----------|
| est-logging-console | 控制台输�?| 开发调试时 |
| est-logging-file | 文件输出 | 生产环境，需要保存日�?|
| est-logging-slf4j | SLF4J 桥接 | 已有 SLF4J 项目 |

---

## est-data

### 一句话总结

**数据访问，就像一个档案管理员**，帮你存取数据库、Redis 等各种数据源�?
### 小白必读：什么是数据访问�?
**场景类比**�?- 你要存东西，找档案管理员
- 你要取东西，也找档案管理�?- 档案管理员知道东西存哪，怎么�?
**为什么需要数据访问模�?*�?- 统一接口：不管存数据库还�?Redis，用法一�?- 简化代码：不用写重复的 JDBC 代码
- 多种实现：支�?MySQL、PostgreSQL、Redis �?
### 快速示�?
```java
import ltd.idcu.est.data.api.Repository;
import ltd.idcu.est.data.api.Repositories;

// 1. 创建仓库
Repository<String, User> userRepo = Repositories.newJdbcRepository(dataSource);

// 2. 存数�?userRepo.save("user:123", new User("张三"));

// 3. 取数�?Optional<User> user = userRepo.findById("user:123");

// 4. 查询
List<User> users = userRepo.findAll();
```

### 多种实现可�?
| 实现 | 说明 | 什么时候用 |
|-----|------|-----------|
| est-data-jdbc | JDBC 实现 | 关系型数据库（MySQL、PostgreSQL�?|
| est-data-memory | 内存实现 | 测试、临时数�?|
| est-data-mongodb | MongoDB 实现 | NoSQL 数据�?|
| est-data-redis | Redis 实现 | 缓存、KV 存储 |

---

## est-security

### 一句话总结

**安全认证，就像一个门�?*，检查谁能进，谁不能进，谁能做什么�?
### 小白必读：什么是安全认证�?
**场景类比**�?- 你进小区，门卫查门禁卡（认证�?- 你进家门，用钥匙（认证）
- 你去银行，还要验证身份（授权�?
**为什么需要安全模�?*�?- 认证：确认用户是�?- 授权：确认用户能做什�?- 安全：防止未授权访问

### 快速示�?
```java
import ltd.idcu.est.security.api.Authenticator;
import ltd.idcu.est.security.api.Security;

// 1. 创建认证�?Authenticator authenticator = Security.newJwtAuthenticator();

// 2. 登录，生�?Token
String token = authenticator.login("zhangsan", "password123");

// 3. 验证 Token
Optional<User> user = authenticator.authenticate(token);

// 4. 检查权�?if (authenticator.hasPermission(user.get(), "user:create")) {
    // 有权限，执行操作
}
```

### 多种实现可�?
| 实现 | 说明 | 什么时候用 |
|-----|------|-----------|
| est-security-basic | Basic Auth | 简单场�?|
| est-security-jwt | JWT Token | 前后端分离、API |
| est-security-oauth2 | OAuth2 | 第三方登录、微服务 |
| est-security-apikey | API Key | 服务间调�?|

---

## 其他功能模块

| 模块 | 说明 | 什么时候用 |
|------|------|-----------|
| [est-messaging](messaging.md) | 消息系统 | 需要异步处理、解耦时 |
| [est-monitor](monitor.md) | 监控 | 需要看程序运行状态时 |
| [est-scheduler](scheduler.md) | 调度 | 需要定时执行任务时 |
| [est-ai](ai.md) | AI 助手 | 需�?AI 功能�?|
| [est-event](event.md) | 事件总线 | 需要模块间通信�?|
| [est-circuitbreaker](circuitbreaker.md) | 熔断�?| 需要防止雪崩时 |
| [est-discovery](discovery.md) | 服务发现 | 微服务时找服务地址 |
| [est-plugin](plugin.md) | 插件系统 | 需要动态加载功能时 |
| [est-workflow](workflow.md) | 工作流引擎 | 需要编排业务流程时 |

---

## 快速开�?
### 引入缓存和日�?
```xml
<dependencies>
    <!-- 核心模块（必须） -->
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-core-api</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-core-impl</artifactId>
        <version>2.0.0</version>
    </dependency>
    
    <!-- 缓存 - 内存实现 -->
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-cache-api</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-cache-memory</artifactId>
        <version>2.0.0</version>
    </dependency>
    
    <!-- 日志 - 控制台实�?-->
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-logging-api</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-logging-console</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>
```

---

## 下一�?
- 📖 继续阅读各个功能模块的详细文�?- 🎓 查看 [最佳实践课程](../../best-practices/course/README.md)
- 💻 研究 [示例代码](../../../est-examples/) 看实际应�?
---

**文档版本**: 2.0  
**最后更�?*: 2026-03-07  
**维护�?*: EST 架构团队
