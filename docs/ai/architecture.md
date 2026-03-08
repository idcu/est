# EST AI 架构设计

## 概述

EST AI 模块采用模块化、可扩展的架构设计，将 AI 能力无缝集成到 Java 企业级应用开发中。该架构遵循关注点分离原则，确保各组件高内聚、低耦合，便于维护和扩展。

---

## 整体架构图

```
┌─────────────────────────────────────────────────────────────┐
│                     应用层                        │
│  ┌──────────────────────┐ ┌──────────────────────┐  │
│  │   Web / REST API     │ │   命令行工具 (CLI)   │  │
│  └──────────────────────┘ └──────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                   AI 服务层                      │
│  ┌──────────────────┐ ┌──────────────────┐      │
│  │   AiAssistant    │ │  CodeGenerator   │      │
│  └──────────────────┘ └──────────────────┘      │
│  ┌──────────────────────────────────────────┐   │
│  │     PromptTemplateEngine                  │   │
│  └──────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                 LLM 客户端层                    │
│  ┌──────────────────────────────────────────┐   │
│  │      LlmClientFactory                    │   │
│  └──────────────────────────────────────────┘   │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐       │
│  │ OpenAI   │ │  智谱    │ │ 通义千问 │ ...   │
│  │ Client   │ │  Client  │ │  Client  │       │
│  └──────────┘ └──────────┘ └──────────┘       │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                  外部 LLM 服务                    │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐       │
│  │ OpenAI   │ │  Zhipu   │ │  Qwen    │ ...   │
│  │  API     │ │   API    │ │   API    │       │
│  └──────────┘ └──────────┘ └──────────┘       │
└─────────────────────────────────────────────────────────────┘
```

---

## 核心模块设计

### 1. AI 助手模块 (AiAssistant)

**职责**：提供高级 AI 交互能力，包括对话、代码建议、代码解释、代码优化等。

**核心接口**：
```java
public interface AiAssistant {
    String chat(String message);
    String chat(String message, Map<String, Object> context);
    String getQuickReference(String topic);
    String getBestPractice(String topic);
    String getTutorial(String topic);
    String suggestCode(String requirement);
    String explainCode(String code);
    String optimizeCode(String code);
}
```

**设计特点**：
- 内置 EST 框架知识库
- 支持上下文对话
- 提供专门的代码相关功能
- 易于扩展自定义功能

---

### 2. 代码生成器模块 (CodeGenerator)

**职责**：自动生成标准的 Java 代码，包括 Entity、Repository、Service、Controller 等。

**核心接口**：
```java
public interface CodeGenerator {
    String generateEntity(String entityName, String packageName, Map<String, Object> options);
    String generateRepository(String repositoryName, String packageName, Map<String, Object> options);
    String generateService(String serviceName, String packageName, Map<String, Object> options);
    String generateController(String controllerName, String packageName, Map<String, Object> options);
    String generatePomXml(String projectName, String groupId, String artifactId, String version);
}
```

**设计特点**：
- 模板驱动的代码生成
- 可定制的代码风格
- 支持多种技术栈配置
- 完整的 CRUD 代码生成

---

### 3. 提示词模板引擎模块 (PromptTemplateEngine)

**职责**：管理和渲染提示词模板，提高 AI 输出质量。

**核心接口**：
```java
public interface PromptTemplateEngine {
    String render(String templateName, Map<String, Object> variables);
    String render(PromptTemplate template, Map<String, Object> variables);
    void registerTemplate(PromptTemplate template);
    PromptTemplate getTemplate(String templateName);
    List<String> listTemplates();
}
```

**设计特点**：
- 内置常用提示词模板
- 支持自定义模板注册
- 简单的变量替换语法
- 模板分类管理

---

### 4. LLM 客户端模块 (LlmClient)

**职责**：提供统一的 LLM 访问接口，支持多种 LLM 提供商。

**核心接口**：
```java
public interface LlmClient {
    String complete(String prompt);
    String complete(String prompt, LlmOptions options);
    List<ChatMessage> chat(List<ChatMessage> messages);
    List<ChatMessage> chat(List<ChatMessage> messages, LlmOptions options);
    boolean isAvailable();
}
```

**支持的提供商**：
- OpenAI (GPT-4, GPT-3.5)
- 智谱 AI (GLM-4, GLM-3)
- 通义千问 (Qwen-Turbo, Qwen-Plus)
- 文心一言 (Ernie-4.0, Ernie-3.5)
- 豆包 (Doubao-Pro)
- Kimi (Moonshot-v1)
- Ollama (本地模型)

**设计特点**：
- 统一的 API 接口
- 工厂模式创建客户端
- 可配置的请求选项
- 内置重试和错误处理

---

## 设计原则

### 1. 接口与实现分离

所有核心功能都通过接口定义，实现类可以灵活替换：

```
接口层 (API)        → 实现层 (Impl)
AiAssistant         → DefaultAiAssistant
CodeGenerator       → DefaultCodeGenerator
PromptTemplateEngine→ DefaultPromptTemplateEngine
LlmClient           → OpenAiLlmClient, ZhipuLlmClient, ...
```

### 2. 依赖注入友好

所有组件都设计为可以与 Spring 等依赖注入框架无缝集成：

```java
@Configuration
public class EstAiAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public AiAssistant aiAssistant(LlmClient llmClient) {
        return new DefaultAiAssistant(llmClient);
    }
    
    @Bean
    @ConditionalOnMissingBean
    public LlmClient llmClient(LlmConfig config) {
        return LlmClientFactory.create(config);
    }
}
```

### 3. 可扩展性

通过以下方式确保系统的可扩展性：
- 接口设计允许自定义实现
- 工厂模式支持动态创建组件
- 提示词模板系统支持自定义模板
- LLM 提供商可以轻松添加

### 4. 错误处理

统一的错误处理策略：
- 检查型异常用于可恢复的错误
- 运行时异常用于编程错误
- 所有 LLM 调用都有超时和重试机制
- 详细的错误日志便于调试

---

## 数据流

### 典型的 AI 对话流程

```
用户输入
    ↓
Controller (REST API / CLI)
    ↓
AiAssistant.chat()
    ↓
PromptTemplateEngine (如果需要)
    ↓
LlmClient.chat()
    ↓
具体的 LLM 提供商客户端
    ↓
外部 LLM API
    ↓
返回响应
    ↓
结果处理和格式化
    ↓
返回给用户
```

### 代码生成流程

```
需求描述
    ↓
CodeGenerator.generateXxx()
    ↓
构建详细的提示词
    ↓
AiAssistant.suggestCode()
    ↓
LlmClient 调用
    ↓
外部 LLM API
    ↓
获取生成的代码
    ↓
代码格式化和验证
    ↓
返回给用户
```

---

## 扩展点

### 1. 自定义 AiAssistant

```java
public class CustomAiAssistant implements AiAssistant {
    
    private final LlmClient llmClient;
    private final KnowledgeBase knowledgeBase;
    
    public CustomAiAssistant(LlmClient llmClient, KnowledgeBase knowledgeBase) {
        this.llmClient = llmClient;
        this.knowledgeBase = knowledgeBase;
    }
    
    @Override
    public String chat(String message) {
        // 自定义实现
        String enhancedPrompt = enhanceWithKnowledge(message);
        return llmClient.complete(enhancedPrompt);
    }
    
    // ... 实现其他方法
}
```

### 2. 自定义 LLM 提供商

```java
public class CustomLlmClient implements LlmClient {
    
    private final LlmConfig config;
    
    public CustomLlmClient(LlmConfig config) {
        this.config = config;
    }
    
    @Override
    public String complete(String prompt) {
        // 自定义 LLM API 调用
        return callCustomApi(prompt);
    }
    
    // ... 实现其他方法
}

// 注册到工厂
LlmClientFactory.registerProvider("custom", CustomLlmClient.class);
```

### 3. 自定义提示词模板

```java
PromptTemplate customTemplate = new PromptTemplate();
customTemplate.setName("my-custom-template");
customTemplate.setCategory("custom");
customTemplate.setDescription("我的自定义模板");
customTemplate.setContent("自定义模板内容: ${variable}");

PromptTemplateEngine engine = new DefaultPromptTemplateEngine();
engine.registerTemplate(customTemplate);
```

---

## 性能考虑

### 1. 缓存策略

- LLM 响应可以缓存以提高重复查询的性能
- 使用 Caffeine 等高性能缓存库
- 配置合理的过期时间

### 2. 异步处理

- 长时间运行的 AI 任务应该异步执行
- 使用 @Async 或 CompletableFuture
- 提供进度跟踪机制

### 3. 批量处理

- 合并多个小请求为批量请求
- 减少 API 调用次数
- 提高吞吐量

---

## 安全考虑

### 1. API 密钥管理

- 从不硬编码 API 密钥
- 使用环境变量或密钥管理服务
- 密钥轮换机制

### 2. 输入验证

- 验证所有用户输入
- 防止提示词注入攻击
- 过滤敏感信息

### 3. 输出清理

- 清理 AI 输出中的潜在恶意内容
- 移除敏感信息
- 代码执行前进行审查

### 4. 审计日志

- 记录所有 AI 请求和响应
- 包括时间戳、用户、请求内容
- 便于问题追踪和合规

---

## 总结

EST AI 架构设计充分考虑了：
- 模块化和可扩展性
- 与现有 Java 生态的集成
- 性能和安全性
- 开发体验和维护性

这种设计使得 EST 能够成为 AI 编程工具的首选 Java 框架，为开发者提供强大、灵活、可靠的 AI 能力。

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
