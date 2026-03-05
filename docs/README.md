# 📚 EST 框架文档中心

欢迎来到 EST 框架的完整文档体系！这里有从入门到精通的所有资料，无论你是初学者还是有经验的开发者，都能找到需要的内容。

---

## 🌱 新手必读（按顺序阅读）

如果你是第一次接触 EST 框架，建议按以下顺序阅读：

1. **[环境准备与安装](../docs/setup.md)** - 从零开始，搭建完整开发环境
2. **[快速开始](../docs/guides/getting-started.md)** - 创建你的第一个应用
3. **[入门教程](../docs/tutorials/beginner/)** - 从零开始学习核心概念
4. **[Web 开发教程](../docs/tutorials/web/)** - 学习如何构建 Web 应用

---

## 📋 文档导航

### 🚀 快速入门

| 文档 | 说明 | 难度 |
|------|------|------|
| [根目录 README](../README.md) | EST 框架简介，3 分钟上手 | ⭐ |
| [环境准备与安装](./setup.md) | 从零开始搭建开发环境 | ⭐ |
| [快速开始指南](./guides/getting-started.md) | 创建你的第一个应用 | ⭐ |
| [部署指南](./deployment.md) | 本地运行、Docker、Kubernetes 部署 | ⭐⭐ |

### 🎓 教程系列

#### 入门教程（初学者必看）
- [教程 01：第一个 EST 应用](./tutorials/beginner/01-first-app.md) - 创建并运行你的第一个应用
- [教程 02：依赖注入容器](./tutorials/beginner/02-dependency-injection.md) - 学习如何管理组件
- [教程 03：配置管理](./tutorials/beginner/03-configuration.md) - 轻松管理应用配置

#### Web 开发教程
- [教程 01：基础 Web 应用](./tutorials/web/01-basic-web-app.md) - 创建你的第一个 Web 服务器
- [教程 02：路由与控制器](./tutorials/web/02-routing-controllers.md) - 深入学习路由和控制器

#### 功能模块教程
- [缓存系统](./tutorials/features/01-cache.md) - 学习使用缓存提升性能

### 📖 API 参考手册

#### 核心模块
- [核心模块概览](./api/core/README.md)
- [容器（Container）](./api/core/container.md) - 依赖注入容器
- [配置（Configuration）](./api/core/configuration.md) - 配置管理

#### Web 模块
- [Web 模块概览](./api/web/README.md)
- [路由（Router）](./api/web/router.md) - 路由系统
- [请求与响应](./api/web/request-response.md) - HTTP 请求响应处理

#### 功能模块
- [功能模块概览](./api/features/README.md)
- [缓存系统](./api/features/cache.md)
- [事件系统](./api/features/event.md)
- [日志系统](./api/features/logging.md)
- [数据访问](./api/features/data.md)
- [安全认证](./api/features/security.md)
- [监控系统](./api/features/monitor.md)
- [调度系统](./api/features/scheduler.md)
- [消息系统](./api/features/messaging.md)
- [AI助手](./api/features/ai.md)

#### 其他模块
- [集合模块](./api/collection/README.md)
- [设计模式模块](./api/patterns/README.md)
- [测试模块](./api/test/README.md)
- [脚手架生成器](./api/scaffold/README.md)

### ✅ 最佳实践

- [代码组织](./best-practices/code-organization.md) - 如何组织你的代码
- [性能优化](./best-practices/performance.md) - 提升应用性能的技巧
- [安全性](./best-practices/security.md) - 安全编程指南
- [测试指南](./TESTING_GUIDE.md) - 如何编写和运行测试
- [调试技巧](./best-practices/debugging.md) - 调试方法和工具

### 🤖 AI 开发者专区

专为 AI 代码生成优化的文档：

- [AI Coder 指南](./AI_CODER_GUIDE.md) - 专为 AI 设计的完整使用指南
- [快速参考卡片](./QUICK_REFERENCE.md) - 一分钟速查表，AI 必备
- [代码生成提示词模板](./AI_PROMPTS.md) - 高质量的 AI 提示词集合

### 🏗️ 架构与设计

- [整体架构](./ARCHITECTURE.md) - 框架的设计理念和架构说明
- [贡献指南](./CONTRIBUTING.md) - 如何参与项目开发

---

## 💡 如何使用这些文档？

### 初学者
1. 先看 [根目录 README](../README.md) 了解 EST 是什么
2. 跟着 [环境准备与安装](./setup.md) 搭建环境
3. 跟着 [入门教程](./tutorials/beginner/) 一步步学习
4. 遇到问题查 [API 参考](./api/README.md)

### 有经验的开发者
1. 直接看 [快速参考卡片](./QUICK_REFERENCE.md) 了解 API
2. 根据需要查阅 [API 参考](./api/README.md)
3. 参考 [最佳实践](./best-practices/README.md) 提升代码质量

### AI 开发者
1. 必读 [AI Coder 指南](./AI_CODER_GUIDE.md)
2. 使用 [快速参考卡片](./QUICK_REFERENCE.md) 快速查找 API
3. 参考 [提示词模板](./AI_PROMPTS.md) 生成高质量代码

---

## 📂 示例代码

EST 框架提供了丰富的示例代码，位于项目根目录的 `est-examples/` 文件夹：

- **est-examples-basic** - 基础示例，适合初学者
- **est-examples-web** - Web 开发示例（包含Todo、博客、看板、聊天等应用）
- **est-examples-features** - 功能模块使用示例
- **est-examples-advanced** - 高级用法示例
- **est-examples-ai** - AI助手使用示例

---

## ❓ 遇到问题？

如果在使用过程中遇到问题：

1. 先查 [API 参考](./api/README.md) 看是否有相关说明
2. 查看 [示例代码](../../est-examples/) 是否有类似用法
3. 到 [GitHub Issues](https://github.com/idcu/est/issues) 提问

---

## 🔗 相关链接

- [GitHub 仓库](https://github.com/idcu/est)
- [问题反馈](https://github.com/idcu/est/issues)
- [许可证](../LICENSE)

---

**祝你学习愉快！** 🎉
