# EST 框架 AI 代码生成提示词模板

> 为 AI coder 准备的高质量提示词集合

## 通用提示词模板

### 1. 创建基础 EST 项目

**提示词：**
```
使用 EST 框架创建一个 Java 项目。EST 是一个零依赖的 Java 框架，版本 1.3.0-SNAPSHOT。

要求：
1. 创建标准的 Maven 项目结构
2. 包含必要的 EST 依赖
3. 使用 Java 21+
4. 包含一个简单的 Main 类，演示 EST 容器的基本用法

参考文档：
- 框架主页：https://github.com/idcu/est
- AI Coder 指南：docs/AI_CODER_GUIDE.md
- 快速参考：docs/QUICK_REFERENCE.md

请使用 EST 框架的最佳实践。
```

---

### 2. 创建 Web 应用

**提示词：**
```
使用 EST 框架创建一个 Web 应用项目。

功能需求：
1. 启动一个 HTTP 服务器在 8080 端口
2. 实现以下路由：
   - GET / - 返回欢迎页面（HTML）
   - GET /hello/:name - 返回个性化问候
   - GET /api/status - 返回 JSON 状态信息
3. 添加日志中间件
4. 服务器启动时打印可用路由信息

EST Web 模块关键类：
- WebApplication 接口
- DefaultWebApplication 实现类
- Request 和 Response 接口
- Router 接口

请按照 EST 框架的标准模式编写代码。
```

---

### 3. 创建 REST API 服务

**提示词：**
```
使用 EST 框架创建一个完整的 REST API 服务，实现用户管理功能。

API 端点设计：
- GET    /api/users          - 获取所有用户列表
- GET    /api/users/:id      - 根据 ID 获取单个用户
- POST   /api/users          - 创建新用户
- PUT    /api/users/:id      - 更新用户信息
- DELETE /api/users/:id      - 删除用户

用户数据模型字段：
- id (String)
- name (String)
- email (String)
- createdAt (String)

要求：
1. 使用内存存储（ConcurrentHashMap）
2. 返回标准的 JSON 响应
3. 适当的 HTTP 状态码（200, 201, 400, 404, 500）
4. 包含错误处理
5. 添加示例数据（2个用户）

请参考 EST 框架的 REST API 模式实现。
```

---

### 4. 添加依赖注入服务

**提示词：**
```
在现有的 EST 项目中添加以下服务，使用 EST 的依赖注入容器：

1. UserService 接口
2. UserServiceImpl 实现类
3. UserRepository 接口
4. UserRepositoryImpl 实现类（内存存储）

功能要求：
- UserService 提供：getUser, createUser, updateUser, deleteUser, listUsers
- 使用 @Component 注解标记服务类
- 使用 @Inject 注解进行依赖注入
- 在 Main 类中从容器获取服务并演示使用

EST 容器关键类：
- Container 接口
- DefaultContainer 实现类
- @Component, @Service, @Repository 注解
- @Inject 注解

请按照 EST 依赖注入的最佳实践编写。
```

---

### 5. 添加缓存功能

**提示词：**
```
为 EST 项目添加缓存功能，使用 EST 框架的缓存模块。

需求：
1. 创建内存缓存（Caches.newMemoryCache()）
2. 缓存键值对数据
3. 实现：
   - 存储数据到缓存
   - 从缓存读取数据
   - 删除缓存项
   - 清空缓存
4. 添加缓存过期策略（可选）

EST 缓存关键类：
- Cache<K, V> 接口
- Caches 工厂类
- est-features-cache-api 和 est-features-cache-memory 依赖

请提供完整的可运行示例代码。
```

---

### 6. 添加事件驱动功能

**提示词：**
```
在 EST 项目中实现事件驱动架构。

需求：
1. 定义事件类：
   - UserCreatedEvent
   - UserUpdatedEvent
   - UserDeletedEvent
2. 创建本地事件总线（EventBuses.newLocalEventBus()）
3. 实现事件订阅者，监听以上事件
4. 实现事件发布功能
5. 在事件处理器中打印日志信息

EST 事件关键类：
- EventBus 接口
- EventBuses 工厂类
- est-features-event-api 和 est-features-event-local 依赖

请提供完整的事件驱动实现示例。
```

---

### 7. 添加中间件

**提示词：**
```
为 EST Web 应用创建自定义中间件。

中间件需求：
1. 性能监控中间件：
   - 记录请求开始时间
   - 计算请求处理耗时
   - 在响应头中添加 X-Response-Time
   - 在控制台打印请求日志
2. CORS 中间件：
   - 添加必要的 CORS 响应头
   - 支持跨域请求

中间件实现要点：
- 实现 Middleware 接口
- 实现 getName(), getPriority(), before(), after(), onError() 方法
- 使用 app.use(middleware) 注册中间件
- 中间件按优先级顺序执行

请提供完整的中间件实现代码。
```

---

### 8. 集成多个功能模块

**提示词：**
```
创建一个综合 EST 应用，集成以下功能模块：

1. Web 服务（est-web）
2. 依赖注入（est-core）
3. 缓存（est-features-cache）
4. 事件（est-features-event）
5. 日志（est-features-logging）

应用架构：
- Controller 层：处理 HTTP 请求
- Service 层：业务逻辑，使用依赖注入
- Repository 层：数据访问，使用缓存
- 事件驱动：用户操作时发布事件

请按照分层架构设计，确保模块间低耦合。
```

---

## 特定场景提示词

### 场景：创建微服务

**提示词：**
```
使用 EST 框架创建一个订单微服务。

服务功能：
1. REST API 管理订单
2. 使用缓存加速查询
3. 事件驱动处理订单状态变更
4. 日志记录所有操作

技术要求：
- EST Web 模块提供 API
- EST 缓存模块存储订单
- EST 事件模块处理状态变更
- EST 日志模块记录操作

请按照微服务最佳实践设计。
```

---

### 场景：添加数据持久化

**提示词：**
```
为 EST 项目添加 JDBC 数据持久化，使用 EST 的数据模块。

需求：
1. 使用 est-features-data-jdbc
2. 实现 User 实体的 CRUD 操作
3. 配置数据库连接
4. 提供事务支持（如可用）

EST 数据模块关键类：
- DataRepository 接口
- est-features-data-api 和 est-features-data-jdbc 依赖

请提供完整的数据持久化实现。
```

---

## 代码审查提示词

**提示词：**
```
请审查以下 EST 框架代码，检查是否符合最佳实践：

[在此处粘贴代码]

检查清单：
1. 是否正确使用了 EST 的 API？
2. 依赖注入是否正确？
3. 错误处理是否完善？
4. 命名是否遵循约定？
5. 是否有可以改进的地方？

请提供详细的反馈和改进建议。
```

---

## 调试提示词

**提示词：**
```
我在使用 EST 框架时遇到了问题，请帮我调试：

问题描述：
[在此处描述问题]

错误信息：
[在此处粘贴错误信息]

相关代码：
[在此处粘贴相关代码]

请分析可能的原因并提供解决方案。
```

---

## 使用提示词的最佳实践

1. **明确需求**：在提示词中清晰描述功能需求
2. **提供上下文**：提到使用 EST 框架和版本
3. **参考文档**：引用相关的 EST 文档
4. **指定约束**：说明技术约束和要求
5. **示例优先**：优先使用 EST 的标准模式和示例

## 快速提示词索引

| 任务 | 提示词编号 |
|------|-----------|
| 创建基础项目 | 1 |
| 创建 Web 应用 | 2 |
| 创建 REST API | 3 |
| 依赖注入 | 4 |
| 缓存 | 5 |
| 事件驱动 | 6 |
| 中间件 | 7 |
| 多模块集成 | 8 |
