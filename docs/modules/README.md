# EST 模块文档

欢迎来到 EST 模块文档中心！这里详细介绍了 EST 框架的各个模块�?
## 📋 目录

1. [模块分类](#模块分类)
2. [核心模块](#核心模块)
3. [基础模块](#基础模块)
4. [功能模块](#功能模块)
5. [应用模块](#应用模块)
6. [下一步](#下一�?

---

## 模块分类

EST 框架的模块按照功能和层次分为四大类：

| 类别 | 目录 | 说明 |
|------|------|------|
| 核心模块 | `core/` | 框架的基础设施，所有模块的基础 |
| 基础模块 | `base/` | 通用工具和基础功能 |
| 功能模块 | `features/` | 各种具体功能模块 |
| 应用模块 | `app/` | 直接可用的应用框�?|

---

## 核心模块

**est-core** �?EST 框架的灵魂，提供了让所有模块配合工作的基础设施�?
### 包含的子模块

| 模块 | 说明 | 小白必读 |
|------|------|---------|
| [est-core-container](core/container.md) | 依赖注入容器 | 就像管家，管理各个组�?|
| [est-core-config](core/config.md) | 配置管理 | 管理配置文件和环境变�?|
| [est-core-lifecycle](core/lifecycle.md) | 生命周期管理 | 管理组件的启动和关闭 |
| [est-core-module](core/module.md) | 模块管理 | 管理各个模块的加�?|
| [est-core-aop](core/aop.md) | AOP 支持 | 切面编程，比如日志、事�?|
| [est-core-tx](core/tx.md) | 事务管理 | 数据库事务，保证数据一致�?|

### 为什么核心模块重要？

**类比理解**�?- 核心模块就像房子�?*地基和水电管�?*
- 没有它，其他模块就无法正常工�?- 虽然你不直接看到它，但它是一切的基础

**什么时候需�?*�?- �?几乎总是需要！核心模块是必选的
- �?只要你用 EST，就需要引入核心模�?
---

## 基础模块

**est-base** 提供了通用的工具和基础功能，就像建筑用的砖块和水泥�?
### 包含的子模块

| 模块 | 说明 | 什么时候用 |
|------|------|-----------|
| [est-utils](base/utils.md) | 工具类（JSON、XML、IO等） | 需要处�?JSON、XML、文件时 |
| [est-collection](base/collection.md) | 集合增强 | 需要更强大的集合操作时 |
| [est-patterns](base/patterns.md) | 设计模式 | 需要用到设计模式时 |
| [est-test](base/test.md) | 测试框架 | 需要写单元测试�?|

### 特点

- �?可以单独使用，不需要引入整个框�?- �?轻量级，依赖�?- �?性能优化过的工具�?
---

## 功能模块

**est-modules** 提供了各种具体的功能，就像房子里的各种设备（空调、电梯、消防系统）�?
### 包含的子模块

| 模块 | 说明 | 什么时候用 |
|------|------|-----------|
| [est-cache](features/cache.md) | 缓存 | 需要存常用数据，加速访问时 |
| [est-logging](features/logging.md) | 日志 | 需要记录程序运行情况时 |
| [est-data](features/data.md) | 数据访问 | 需要存数据库、Redis �?|
| [est-security](features/security.md) | 安全认证 | 需要登录、权限控制时 |
| [est-messaging](features/messaging.md) | 消息系统 | 需要异步处理、解耦时 |
| [est-monitor](features/monitor.md) | 监控 | 需要看程序运行状态时 |
| [est-scheduler](features/scheduler.md) | 调度 | 需要定时执行任务时 |
| [est-ai](features/ai.md) | AI 助手 | 需�?AI 功能�?|
| [est-event](features/event.md) | 事件总线 | 需要模块间通信�?|
| [est-circuitbreaker](features/circuitbreaker.md) | 熔断�?| 需要防止雪崩时 |
| [est-discovery](features/discovery.md) | 服务发现 | 微服务时找服务地址 |
| [est-config](features/config.md) | 配置中心 | 需要分布式配置时 |
| [est-gateway](features/gateway.md) | API 网关 | 需要路由、限流时 |
| [est-hotreload](features/hotreload.md) | 热重载 | 开发时需要自动重新加载 |
| [est-performance](features/performance.md) | 性能监控 | 需要性能指标时 |
| [est-plugin](features/plugin.md) | 插件系统 | 需要动态加载功能时 |
| [est-workflow](features/workflow.md) | 工作流引擎 | 需要编排业务流程时 |

### 特点

- �?模块化，按需引入
- �?每个模块都有多种实现可�?- �?接口与实现分离，易于替换

---

## 应用模块

**est-app** 提供了直接可用的应用框架，就像精装修的房子，拎包入住�?
### 包含的子模块

| 模块 | 说明 | 什么时候用 |
|------|------|-----------|
| [est-web](app/web.md) | Web 开发框�?| 要做网站、API �?|
| [est-console](app/console.md) | 控制台应用框�?| 要做命令行工具时 |
| [est-microservice](app/microservice.md) | 微服务框�?| 要做微服务架构时 |

### 特点

- �?开箱即用，不需要自己组装模�?- �?内置最佳实践配�?- �?快速上手，适合新手

---

## 下一�?
- 🚀 如果你是新手，从 [应用模块](app/) 开�?- 🔧 如果你想了解底层，从 [核心模块](core/) 开�?- 📦 如果你需要具体功能，查看 [功能模块](features/)
- 🛠�?如果你需要工具，查看 [基础模块](base/)

---

**文档版本**: 2.0  
**最后更�?*: 2026-03-07  
**维护�?*: EST 架构团队
