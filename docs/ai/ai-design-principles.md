# EST AI 设计原则

## 概述

EST AI 模块的设计遵循一系列核心原则，这些原则指导了我们的架构决策、API 设计和功能开发。理解这些原则可以帮助你更好地使用和扩展 EST AI。

---

## 核心设计原则

### 1. 简单性原则 (Simplicity)

**核心理念**：简单易用是最高优先级。

**体现**：
- 零配置即可使用的开箱即用体验
- 简洁直观的 API 设计
- 最小化的学习曲线

**示例**：
```java
// 仅需一行代码创建 AI 助手
AiAssistant assistant = new DefaultAiAssistant();

// 直接使用，无需复杂配置
String response = assistant.chat("你好！");
```

**为什么重要**：
- 降低入门门槛
- 减少误用可能性
- 提高开发效率

---

### 2. 模块化原则 (Modularity)

**核心理念**：将系统分解为高内聚、低耦合的模块。

**体现**：
- 独立的 AI 助手模块
- 独立的代码生成器模块
- 独立的提示词模板模块
- 独立的 LLM 客户端模块

**模块依赖关系**：
```
应用层
    ↓
AiAssistant ─┐
CodeGenerator ─┼→ PromptTemplateEngine
              └→ LlmClient
```

**为什么重要**：
- 便于单独测试
- 便于单独升级
- 便于按需使用

---

### 3. 可扩展性原则 (Extensibility)

**核心理念**：系统应该易于扩展，而无需修改核心代码。

**体现**：
- 接口与实现分离
- 工厂模式
- 插件机制
- 自定义模板支持

**扩展点**：
```java
// 1. 自定义 AiAssistant
public class MyAiAssistant implements AiAssistant { ... }

// 2. 自定义 LlmClient
public class MyLlmClient implements LlmClient { ... }
LlmClientFactory.registerProvider("my-provider", MyLlmClient.class);

// 3. 自定义提示词模板
engine.registerTemplate(myCustomTemplate);
```

**为什么重要**：
- 适应变化的需求
- 支持自定义场景
- 保护核心代码稳定性

---

### 4. 一致性原则 (Consistency)

**核心理念**：API 设计和行为应该保持一致。

**体现**：
- 统一的命名约定
- 相似的方法签名
- 一致的错误处理
- 统一的配置方式

**示例**：
```java
// 所有生成方法都有相似的签名
generator.generateEntity(name, packageName, options);
generator.generateRepository(name, packageName, options);
generator.generateService(name, packageName, options);
generator.generateController(name, packageName, options);

// 所有 LLM 客户端都实现相同的接口
client.complete(prompt);
client.complete(prompt, options);
client.chat(messages);
client.chat(messages, options);
```

**为什么重要**：
- 降低学习成本
- 减少记忆负担
- 提高代码可读性

---

### 5. 可靠性原则 (Reliability)

**核心理念**：系统应该在各种情况下都能可靠运行。

**体现**：
- 完善的错误处理
- 内置重试机制
- 请求超时控制
- 降级策略

**示例**：
```java
@Retryable(
    retryFor = {TimeoutException.class, IOException.class},
    maxAttempts = 3,
    backoff = @Backoff(delay = 1000, multiplier = 2)
)
public String chatWithRetry(String message) {
    try {
        return aiAssistant.chat(message);
    } catch (Exception e) {
        if (fallbackEnabled) {
            return getFallbackResponse(message);
        }
        throw e;
    }
}
```

**为什么重要**：
- 生产环境稳定性
- 用户体验保障
- 系统容错能力

---

### 6. 安全性原则 (Security)

**核心理念**：安全是设计的基本考虑，不是事后添加的。

**体现**：
- API 密钥安全管理
- 输入验证和过滤
- 输出清理
- 请求审计日志

**安全措施**：
```java
// 1. 密钥不从代码中硬编码
config.setApiKey(System.getenv("LLM_API_KEY"));

// 2. 输入验证
if (containsSensitiveInfo(input)) {
    throw new SecurityException("敏感信息检测");
}

// 3. 输出清理
output = sanitizeOutput(output);

// 4. 审计日志
auditLog.log(request, response, duration);
```

**为什么重要**：
- 数据保护
- 合规性要求
- 信任建立

---

### 7. 性能原则 (Performance)

**核心理念**：在保持功能完整性的同时，追求最佳性能。

**体现**：
- 缓存机制
- 异步处理
- 批量操作
- 资源池化

**性能优化**：
```java
// 1. 缓存常用查询
Cache<String, String> cache = Caffeine.newBuilder()
    .maximumSize(1000)
    .expireAfterWrite(1, TimeUnit.HOURS)
    .build();

// 2. 异步处理
@Async
public CompletableFuture<String> generateCodeAsync(String requirement) {
    return CompletableFuture.completedFuture(
        aiAssistant.suggestCode(requirement)
    );
}

// 3. 批量处理
String combined = String.join("\n---\n", codeList);
String result = aiAssistant.chat(buildBatchPrompt(combined));
```

**为什么重要**：
- 用户体验
- 成本控制
- 可扩展性

---

### 8. 可测试性原则 (Testability)

**核心理念**：设计时就考虑如何测试。

**体现**：
- 依赖注入友好
- 接口抽象
- 可 Mock 的依赖
- 测试工具提供

**可测试的设计**：
```java
// 依赖注入，便于 Mock
public class DefaultAiAssistant implements AiAssistant {
    private final LlmClient llmClient;
    
    // 构造函数注入
    public DefaultAiAssistant(LlmClient llmClient) {
        this.llmClient = llmClient;
    }
}

// 测试时可以使用 Mock
@Test
void testChat() {
    LlmClient mockClient = mock(LlmClient.class);
    when(mockClient.complete(any())).thenReturn("Mock response");
    
    AiAssistant assistant = new DefaultAiAssistant(mockClient);
    String result = assistant.chat("Hello");
    
    assertEquals("Mock response", result);
}
```

**为什么重要**：
- 质量保障
- 重构信心
- 开发效率

---

## 架构设计原则

### 分层架构

```
┌─────────────────────────┐
│   应用层 (Application)  │  → Controller, CLI
├─────────────────────────┤
│   服务层 (Service)      │  → AiAssistant, CodeGenerator
├─────────────────────────┤
│   客户端层 (Client)     │  → LlmClient
├─────────────────────────┤
│   基础设施层 (Infra)    │  → HTTP, Cache, Config
└─────────────────────────┘
```

### 关注点分离

- **AI 助手**：处理高级 AI 交互
- **代码生成**：专注于代码生成逻辑
- **提示词模板**：管理提示词
- **LLM 客户端**：处理 LLM API 通信

### 依赖倒置

- 高层模块不依赖低层模块，都依赖抽象
- 抽象不依赖细节，细节依赖抽象

---

## API 设计原则

### 1. 最小惊讶原则 (Principle of Least Surprise)

API 行为应该符合用户的直觉预期。

### 2. 流畅接口 (Fluent Interface)

方法应该可以链式调用：

```java
// 好的设计
LlmConfig config = new LlmConfig()
    .setProvider("qwen")
    .setApiKey("key")
    .setModel("qwen-turbo");
```

### 3. 合理的默认值

提供合理的默认值，减少配置负担：

```java
// 默认值已经设置好
LlmOptions options = new LlmOptions();
// 不需要设置 temperature = 0.7, maxTokens = 2000 等
```

### 4. 明确的失败

失败时应该提供清晰的错误信息：

```java
try {
    client.complete(prompt);
} catch (LlmApiException e) {
    System.out.println("错误代码: " + e.getErrorCode());
    System.out.println("错误消息: " + e.getMessage());
    System.out.println("建议: " + e.getSuggestion());
}
```

---

## 演进原则

### 1. 向后兼容

新版本应该尽可能保持向后兼容。

### 2. 渐进式 deprecation

废弃功能时应该：
1. 添加 @Deprecated 注解
2. 提供迁移指南
3. 保留至少一个主要版本

### 3. 语义化版本

遵循语义化版本规范：
- MAJOR：不兼容的 API 变更
- MINOR：向后兼容的功能新增
- PATCH：向后兼容的问题修复

---

## 总结

EST AI 的设计原则可以概括为：

**核心价值观**：
- 🎯 简单易用
- 🧩 模块化
- 🔧 可扩展
- ✅ 一致性
- 🔒 安全性
- ⚡ 高性能
- 🧪 可测试

这些原则不是相互独立的，而是相互补充、相互支持的。在实际设计中，我们需要权衡各种原则，根据具体场景做出最佳决策。

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
