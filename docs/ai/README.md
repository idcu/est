# EST AI 文档

欢迎来到 EST AI 文档专区，这里提供关于 EST 框架 AI 功能的完整文档。

## 目录

- [AI 介绍](#ai-介绍)
- [快速开始](#快速开始)
- [核心功能](#核心功能)
- [LLM 提供商](#llm-提供商)
- [示例代码](#示例代码)
- [API 参考](#api-参考)

---

## AI 介绍

EST AI Suite 是 EST 框架的 AI 和 LLM（大语言模型）相关组件集合，提供了开箱即用的 AI 功能支持。

### 主要特点
- 🛠️ **简单易用** - 几行代码就能用上 AI
- 🌐 **多提供商支持** - 支持 OpenAI、智谱、通义千问、文心一言等主流 LLM
- 🧩 **模块化设计** - 按需引入，灵活组合
- 👨‍💻 **高级功能** - 代码生成、提示词模板、AI 助手等
- 🔌 **可扩展** - 轻松添加自定义 LLM 提供商

---

## 快速开始
### 1. 添加依赖

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-ai-assistant</artifactId>
    <version>2.3.0-SNAPSHOT</version>
</dependency>
```

### 2. 创建 AI 助手

```java
import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.impl.DefaultAiAssistant;

public class AiQuickStart {
    public static void main(String[] args) {
        AiAssistant assistant = new DefaultAiAssistant();
        
        String result = assistant.chat("你好，请介绍一下 EST 框架");
        System.out.println(result);
    }
}
```

### 3. 配置 LLM

```java
import ltd.idcu.est.llm.api.LlmClient;
import ltd.idcu.est.llm.api.LlmConfig;
import ltd.idcu.est.llm.impl.openai.OpenAiLlmClient;

LlmConfig config = new LlmConfig();
config.setApiKey("your-api-key");
config.setModel("gpt-4");

LlmClient client = new OpenAiLlmClient(config);
String response = client.complete("Hello, world!");
```

---

## 核心功能

### 1. AI 助手

提供快速参考、最佳实践、教程获取、代码建议、代码解释、代码优化等功能。
```java
AiAssistant assistant = new DefaultAiAssistant();

// 获取快速参考
String ref = assistant.getQuickReference("web开发");

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

### 2. 代码生成

自动生成 Entity、Service、Controller、Repository、pom.xml 等代码。
```java
import ltd.idcu.est.ai.api.CodeGenerator;
import ltd.idcu.est.ai.impl.DefaultCodeGenerator;

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

### 3. 提示词模板

使用预定义的提示词模板，提升 AI 输出质量。
```java
import ltd.idcu.est.ai.api.PromptTemplate;
import ltd.idcu.est.ai.api.PromptTemplateEngine;
import ltd.idcu.est.ai.impl.DefaultPromptTemplateEngine;

PromptTemplateEngine engine = new DefaultPromptTemplateEngine();

// 使用内置模板
String prompt = engine.render("code-review", Map.of(
    "code", codeToReview,
    "language", "Java"
));

// 自定义模板
PromptTemplate template = new PromptTemplate();
template.setName("my-template");
template.setContent("你是一个 ${role}，请处理以下内容：{content}");
engine.registerTemplate(template);
```

---

## LLM 提供商

EST AI Suite 支持以下 LLM 提供商：

| 提供商 | 模型 | 状态 |
|--------|------|------|
| OpenAI | GPT-4, GPT-3.5 | ✅ 已支持 |
| 智谱 AI | GLM-4, GLM-3 | ✅ 已支持 |
| 通义千问 | Qwen-Turbo, Qwen-Plus | ✅ 已支持 |
| 文心一言 | Ernie-4.0, Ernie-3.5 | ✅ 已支持 |
| 豆包 | Doubao-Pro | ✅ 已支持 |
| Kimi | Moonshot-v1 | ✅ 已支持 |
| Ollama | 本地模型 | ✅ 已支持 |

### 通用使用方式

```java
import ltd.idcu.est.llm.api.LlmClient;
import ltd.idcu.est.llm.api.LlmConfig;

// 配置
LlmConfig config = new LlmConfig();
config.setProvider("openai"); // 或 zhipu, qwen, ernie, doubao, kimi, ollama
config.setApiKey("your-api-key");
config.setModel("gpt-4");
config.setBaseUrl("https://api.openai.com/v1");

// 创建客户端
LlmClient client = LlmClientFactory.create(config);

// 发送请求
String response = client.complete("你好");
```

---

## 示例代码

查看以下示例了解更多用法：
- [AI 快速开始示例](../../est-examples/est-examples-ai/src/main/java/ltd/idcu/est/examples/ai/AiQuickStartExample.java)
- [综合 AI 示例](../../est-examples/est-examples-ai/src/main/java/ltd/idcu/est/examples/ai/ComprehensiveAiExample.java)
- [AI 助手 Web 应用](../../est-examples/est-examples-ai/src/main/java/ltd/idcu/est/examples/ai/AiAssistantWebExample.java)
- [代码生成示例](../../est-examples/est-examples-ai/src/main/java/ltd/idcu/est/examples/ai/CodeGeneratorExample.java)

---

## API 参考

详细的 API 文档请参考：

- [AI 助手 API](./api/ai-assistant.md)
- [代码生成器 API](./api/code-generator.md)
- [LLM 客户端 API](./api/llm-client.md)
- [提示词模板 API](./api/prompt-template.md)

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
