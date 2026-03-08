# EST AI 示例

欢迎来到 EST AI 示例模块！这里展示了如何使用 EST 框架的 AI 功能。

---

## 📋 目录

1. [什么是 EST AI 示例？](#什么是-est-ai-示例)
2. [快速入门](#快速入门)
3. [示例列表](#示例列表)
4. [如何运行示例](#如何运行示例)
5. [最佳实践](#最佳实践)
6. [下一步](#下一步)

---

## 什么是 EST AI 示例？

### 用大白话理解

EST AI 示例就像是一个"AI 工具箱"。想象一下你想在应用中加入 AI 功能：

**传统方式**：自己对接各种 AI API，处理认证、重试、错误处理，工作量巨大。

**EST AI 示例**：给你现成的工具，有快速开始、聊天助手、代码生成、提示词模板等，直接就能用！

它包含 EST AI 模块的各种用法：
- AI 功能快速上手
- AI 助手 Web 应用
- 代码生成示例
- 提示词模板使用
- 与其他模块集成

### 核心特点

- 🎯 **简单易用** - 几行代码就能用上 AI
- ⚡ **开箱即用** - 预置常用功能和模板
- 📚 **场景丰富** - 覆盖各种 AI 应用场景
- 🔧 **灵活扩展** - 可以自定义提示词和流程
- 📈 **最佳实践** - 体现 AI 应用的最佳实践

---

## 快速入门

### 前置条件

- ✅ 已经完成 [基础示例](../est-examples-basic/) 的学习
- ✅ 理解 EST 的核心概念
- ✅ JDK 21+ 和 Maven 3.6+

### 运行第一个示例

```bash
cd est-examples/est-examples-ai
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.AiQuickStartExample"
```

---

## 示例列表

| 示例 | 说明 | 知识点 | 难度 |
|------|------|--------|------|
| [AiQuickStartExample](src/main/java/ltd/idcu/est/examples/ai/AiQuickStartExample.java) | AI 快速开始 | AI 基础 | ⭐⭐ |
| [CompleteAiExample](src/main/java/ltd/idcu/est/examples/ai/CompleteAiExample.java) | 完整 AI 示例 | 所有 AI 功能 | ⭐⭐⭐ |
| [AiWebAssistantExample](src/main/java/ltd/idcu/est/examples/ai/AiWebAssistantExample.java) | AI 助手 Web 应用 | AI + Web | ⭐⭐⭐ |
| [CodeGeneratorExample](src/main/java/ltd/idcu/est/examples/ai/CodeGeneratorExample.java) | 代码生成示例 | 代码生成 | ⭐⭐⭐ |
| [PromptTemplateExample](src/main/java/ltd/idcu/est/examples/ai/PromptTemplateExample.java) | 提示词模板示例 | 提示词工程 | ⭐⭐⭐ |
| [MidtermFeaturesExample](src/main/java/ltd/idcu/est/examples/ai/MidtermFeaturesExample.java) | 中期功能演示 | 重构助手、架构顾问、LLM集成 | ⭐⭐⭐⭐ |
| [LongTermFeaturesExample](src/main/java/ltd/idcu/est/examples/ai/LongTermFeaturesExample.java) | 长期目标功能演示 | 需求解析、架构设计、测试部署 | ⭐⭐⭐⭐⭐ |

---

## 如何运行示例

### 运行 AiQuickStartExample（推荐）

```bash
cd est-examples/est-examples-ai
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.AiQuickStartExample"
```

### 运行 CompleteAiExample - 完整 AI 示例

```bash
cd est-examples/est-examples-ai
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.CompleteAiExample"
```

### 运行 AiWebAssistantExample - AI Web 助手

```bash
cd est-examples/est-examples-ai
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.AiWebAssistantExample"
```

然后访问：http://localhost:8080

这是一个完整的 Web 界面，提供：
- 快速参考查询
- 最佳实践获取
- 教程学习
- 代码建议
- 代码解释
- 代码优化
- Entity/Service/Controller 生成
- pom.xml 生成

### 运行 MidtermFeaturesExample - 中期功能演示

```bash
cd est-examples/est-examples-ai
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.MidtermFeaturesExample"
```

演示内容：
- 重构助手 - 自动识别重构机会
- 架构顾问 - 提供架构设计和优化建议
- 智能代码补全 - 上下文感知的代码建议
- LLM 客户端抽象层 - 统一的 LLM 集成接口

### 运行 LongTermFeaturesExample - 长期目标功能演示

```bash
cd est-examples/est-examples-ai
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.LongTermFeaturesExample"
```

演示内容：
- 需求解析器 - 解析自然语言需求、提取组件、评估复杂度
- 架构设计器 - 设计系统架构、推荐架构模式、验证架构设计
- 测试和部署管理器 - 生成测试用例、创建部署计划、运行测试、执行部署
- 完整工作流程 - 从需求到部署的全流程演示

---

## 最佳实践

### 1. 提示词工程

```java
// ✅ 推荐：清晰、具体的提示词
String prompt = """
    你是一个 Java 代码审查专家。
    请审查以下代码，指出问题并提供改进建议：
    
    代码：
    ${code}
    
    请按以下格式输出：
    1. 问题列表
    2. 改进建议
    3. 优化后的代码
    """;

// ❌ 不推荐：模糊的提示词
String prompt = "帮我看看这段代码";
```

### 2. 使用 AI 助手

```java
// 创建 AI 助手
AiAssistant assistant = new DefaultAiAssistant();

// 获取快速参考
String webRef = assistant.getQuickReference("web开发");

// 获取最佳实践
String bestPractice = assistant.getBestPractice("代码风格");

// 获取教程
String tutorial = assistant.getTutorial("第一个应用");

// 建议代码
String code = assistant.suggestCode("创建一个 UserService");

// 解释代码
String explanation = assistant.explainCode(code);

// 优化代码
String optimized = assistant.optimizeCode(code);
```

### 3. 使用代码生成器

```java
CodeGenerator generator = new DefaultCodeGenerator();

// 生成 Entity
String entity = generator.generateEntity("Product", "com.example.entity",
    Map.of("fields", List.of("id:Long", "name:String", "price:BigDecimal")));

// 生成 Repository
String repo = generator.generateRepository("ProductRepository", "com.example.repository", Map.of());

// 生成 Service
String service = generator.generateService("ProductService", "com.example.service", Map.of());

// 生成 pom.xml
String pom = generator.generatePomXml("MyProject", "com.example", "my-app", "1.0.0");
```

---

## 下一步

- 🚀 从 [AiQuickStartExample](src/main/java/ltd/idcu/est/examples/ai/AiQuickStartExample.java) 开始
- 🌟 探索 [AdvancedAiExample](src/main/java/ltd/idcu/est/examples/ai/AdvancedAiExample.java) 了解所有功能
- 🌐 体验 [AiAssistantWebExample](src/main/java/ltd/idcu/est/examples/ai/AiAssistantWebExample.java) Web 界面
- 🏗️ 查看中期功能 [MidTermFeaturesExample](src/main/java/ltd/idcu/est/examples/ai/MidTermFeaturesExample.java)
- 🚀 体验长期目标 [LongTermFeaturesExample](src/main/java/ltd/idcu/est/examples/ai/LongTermFeaturesExample.java)
- 📖 想看 Web 应用，看 [Web 示例](../est-examples-web/)
- 💡 想了解 AI 模块详情，看 [AI 模块文档](../../est-modules/est-ai/README.md)
- 📚 深入学习，看 [AI 开发者专区](../../docs/ai/README.md)

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08  
**维护者**: EST 架构团队
