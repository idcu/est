# EST API 文档

欢迎来到 EST 框架 API 文档中心！这里提供 EST 框架各模块的完整 API 参考。

## 目录

- [概述](#概述)
- [核心模块 API](#核心模块-api)
- [功能模块 API](#功能模块-api)
- [Web 模块 API](#web-模块-api)
- [AI 模块 API](#ai-模块-api)

---

## 概述

EST 框架采用模块化设计，各模块提供清晰的 API 接口。以下是各主要模块的 API 文档索引。

---

## 核心模块 API

### est-core 核心层

- [容器 API](./core/container.md) - 依赖注入容器
- [配置 API](./core/config.md) - 配置管理
- [生命周期 API](./core/lifecycle.md) - 生命周期管理
- [模块 API](./core/module.md) - 模块管理
- [AOP API](./core/aop.md) - 面向切面编程
- [事务 API](./core/tx.md) - 事务管理

### est-base 基础层

- [工具 API](./base/utils.md) - 工具类（JSON、XML、IO 等）
- [设计模式 API](./base/patterns.md) - 设计模式实现
- [集合 API](./base/collection.md) - 集合框架扩展
- [测试 API](./base/test.md) - 测试支持

---

## 功能模块 API

### est-foundation 基础设施

- [缓存 API](./foundation/cache.md) - 缓存系统
- [配置 API](./foundation/config.md) - 配置中心
- [事件 API](./foundation/event.md) - 事件总线
- [日志 API](./foundation/logging.md) - 日志系统
- [监控 API](./foundation/monitor.md) - 监控系统
- [追踪 API](./foundation/tracing.md) - 分布式追踪

### est-data-group 数据访问

- [数据 API](./data/data.md) - 数据访问（JDBC、内存、MongoDB、Redis）
- [工作流 API](./data/workflow.md) - 工作流引擎

### est-security-group 安全权限

- [审计 API](./security/audit.md) - 审计日志
- [RBAC API](./security/rbac.md) - 基于角色的访问控制
- [安全 API](./security/security.md) - 安全认证（JWT 等）

### est-integration-group 消息集成

- [集成 API](./integration/integration.md) - 系统集成
- [消息 API](./integration/messaging.md) - 消息系统（ActiveMQ、Kafka、RabbitMQ、Redis 等）

### est-web-group Web 框架

- [网关 API](./web/gateway.md) - API 网关
- [路由 API](./web/router.md) - Web 路由
- [中间件 API](./web/middleware.md) - Web 中间件
- [会话 API](./web/session.md) - 会话管理
- [模板 API](./web/template.md) - 模板引擎

### est-microservices 微服务

- [熔断器 API](./microservices/circuitbreaker.md) - 熔断器
- [服务发现 API](./microservices/discovery.md) - 服务发现
- [性能 API](./microservices/performance.md) - 性能优化

### est-extensions 扩展功能

- [热加载 API](./extensions/hotreload.md) - 热加载
- [插件 API](./extensions/plugin.md) - 插件系统
- [调度 API](./extensions/scheduler.md) - 调度系统

---

## Web 模块 API

- [Web 应用 API](./app/web.md) - Web 应用框架
- [管理后台 API](./app/admin.md) - 管理后台 API
- [控制台 API](./app/console.md) - 控制台应用

---

## AI 模块 API

AI 模块的详细 API 文档请参考：[AI API 文档](../ai/api/)

- [AI 助手 API](../ai/api/ai-assistant.md)
- [代码生成器 API](../ai/api/code-generator.md)
- [LLM 客户端 API](../ai/api/llm-client.md)
- [提示词模板 API](../ai/api/prompt-template.md)

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
