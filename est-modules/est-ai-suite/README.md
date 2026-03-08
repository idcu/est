# EST AI Suite AI 套件 - 小白从入门到精通

## 目录
1. [什么是 EST AI Suite？](#什么是-est-ai-suite)
2. [快速入门：5分钟上手](#快速入门5分钟上手)
3. [基础篇](#基础篇)
4. [进阶篇](#进阶篇)
5. [最佳实践](#最佳实践)

---

## 什么是 EST AI Suite？

### 用大白话理解

EST AI Suite 就像是一个"AI 工具箱"。想象一下你要在应用中集成 AI 功能：

**传统方式**：每个 AI 提供商都要写不同的代码，还要自己处理 API 调用、错误重试、Token 计费... 很麻烦！

**EST AI Suite 方式**：给你一套统一的 AI 工具，里面有：
- 🧠 **LLM 核心抽象** - 统一的大语言模型接口
- 🤖 **多提供商支持** - 支持 OpenAI、智谱、通义千问、文心一言、豆包、Kimi、Ollama
- 💬 **AI 助手** - 对话式 AI 助手
- 📝 **代码生成** - 自动生成代码

### 核心特点

- 🎯 **简单易用** - 统一的 API，不用关心底层实现
- ⚡ **高性能** - 优化的 API 调用，支持重试和缓存
- 🔧 **灵活扩展** - 可以自定义 LLM 提供商
- 🎨 **功能完整** - LLM、AI 助手、代码生成一应俱全

---

## 快速入门：5分钟上手

### 第一步：引入依赖

在你的 Maven pom.xml 中添加：

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-ai-assistant</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-llm</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>
```

### 第二步：你的第一个 AI 应用

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.LlmClients;
import ltd.idcu.est.llm.ChatMessage;
import ltd.idcu.est.llm.ChatRequest;
import ltd.idcu.est.llm.ChatResponse;
import ltd.idcu.est.llm.openai.OpenAiLlmClient;

public class FirstAiApp {
    public static void main(String[] args) {
        System.out.println("=== EST AI Suite 第一个示例 ===\n");
        
        LlmClient client = OpenAiLlmClient.builder()
            .apiKey("your-api-key")
            .model("gpt-4")
            .build();
        
        ChatRequest request = ChatRequest.builder()
            .addMessage(ChatMessage.user("你好，请介绍一下你自己"))
            .temperature(0.7)
            .maxTokens(500)
            .build();
        
        ChatResponse response = client.chat(request);
        System.out.println("AI 回复: " + response.getContent());
    }
}
```

---

## 基础篇

### 1. est-llm-core LLM 核心抽象

#### 统一的 LLM 接口

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.ChatMessage;
import ltd.idcu.est.llm.ChatRequest;
import ltd.idcu.est.llm.ChatResponse;

public interface LlmClient {
    ChatResponse chat(ChatRequest request);
    
    CompletionResponse complete(CompletionRequest request);
    
    EmbeddingResponse embed(EmbeddingRequest request);
}
```

#### 消息类型

```java
import ltd.idcu.est.llm.ChatMessage;

ChatMessage userMessage = ChatMessage.user("用户的问题");
ChatMessage assistantMessage = ChatMessage.assistant("AI 的回答");
ChatMessage systemMessage = ChatMessage.system("你是一个 helpful 的助手");
```

### 2. est-llm 多提供商支持

#### OpenAI

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.openai.OpenAiLlmClient;

LlmClient client = OpenAiLlmClient.builder()
    .apiKey("sk-your-api-key")
    .model("gpt-4")
    .baseUrl("https://api.openai.com/v1")
    .build();
```

#### 智谱 AI (GLM)

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.zhipu.ZhipuLlmClient;

LlmClient client = ZhipuLlmClient.builder()
    .apiKey("your-api-key")
    .model("glm-4")
    .build();
```

#### 通义千问 (Qwen)

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.qwen.QwenLlmClient;

LlmClient client = QwenLlmClient.builder()
    .apiKey("your-api-key")
    .model("qwen-turbo")
    .build();
```

#### 文心一言 (Ernie)

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.ernie.ErnieLlmClient;

LlmClient client = ErnieLlmClient.builder()
    .apiKey("your-api-key")
    .secretKey("your-secret-key")
    .model("ernie-4.0")
    .build();
```

#### 豆包 (Doubao)

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.doubao.DoubaoLlmClient;

LlmClient client = DoubaoLlmClient.builder()
    .apiKey("your-api-key")
    .model("doubao-pro-4k")
    .build();
```

#### Kimi (Moonshot)

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.kimi.KimiLlmClient;

LlmClient client = KimiLlmClient.builder()
    .apiKey("your-api-key")
    .model("moonshot-v1-8k")
    .build();
```

#### Ollama (本地模型)

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.ollama.OllamaLlmClient;

LlmClient client = OllamaLlmClient.builder()
    .baseUrl("http://localhost:11434")
    .model("llama2")
    .build();
```

### 3. est-ai-config AI 配置管理

#### 配置 AI 提供商

```java
import ltd.idcu.est.ai.config.AiConfig;
import ltd.idcu.est.ai.config.AiConfigs;
import ltd.idcu.est.ai.config.ProviderConfig;

AiConfig config = AiConfigs.load("ai-config.properties");

ProviderConfig openaiConfig = config.getProvider("openai");
String apiKey = openaiConfig.getApiKey();
String model = openaiConfig.getModel();
```

#### YAML 配置示例

```yaml
ai:
  providers:
    openai:
      api-key: sk-your-api-key
      model: gpt-4
      base-url: https://api.openai.com/v1
      enabled: true
    
    zhipu:
      api-key: your-api-key
      model: glm-4
      enabled: true
    
    ollama:
      base-url: http://localhost:11434
      model: llama2
      enabled: true
```

### 4. est-ai-assistant AI 助手

#### 创建对话助手

```java
import ltd.idcu.est.ai.assistant.AiAssistant;
import ltd.idcu.est.ai.assistant.AiAssistants;
import ltd.idcu.est.llm.LlmClient;

LlmClient llmClient = OpenAiLlmClient.builder()
    .apiKey("your-api-key")
    .model("gpt-4")
    .build();

AiAssistant assistant = AiAssistants.create(llmClient);

assistant.setSystemMessage("你是一个专业的编程助手");

String response = assistant.chat("如何用 Java 写一个 Hello World?");
System.out.println(response);
```

#### 多轮对话

```java
import ltd.idcu.est.ai.assistant.AiAssistant;
import ltd.idcu.est.ai.assistant.Conversation;

AiAssistant assistant = AiAssistants.create(llmClient);
Conversation conversation = assistant.createConversation();

String response1 = conversation.send("你好");
String response2 = conversation.send("我叫什么名字?"); // AI 会记住上下文

System.out.println("回复 1: " + response1);
System.out.println("回复 2: " + response2);
```

#### 代码生成

```java
import ltd.idcu.est.ai.codegen.CodeGenerator;
import ltd.idcu.est.ai.codegen.CodeGenerators;

CodeGenerator generator = CodeGenerators.create(llmClient);

String code = generator.generateJavaClass("User", 
    "包含 id、name、email 字段的用户类");

System.out.println("生成的代码:\n" + code);
```

---

## 进阶篇

### 1. 流式响应

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.ChatRequest;
import ltd.idcu.est.llm.ChatChunk;

LlmClient client = OpenAiLlmClient.builder()
    .apiKey("your-api-key")
    .model("gpt-4")
    .build();

ChatRequest request = ChatRequest.builder()
    .addMessage(ChatMessage.user("写一个关于人工智能的故事"))
    .stream(true)
    .build();

client.chatStream(request, chunk -> {
    if (chunk.getContent() != null) {
        System.out.print(chunk.getContent());
    }
});
```

### 2. 嵌入向量

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.EmbeddingRequest;
import ltd.idcu.est.llm.EmbeddingResponse;

LlmClient client = OpenAiLlmClient.builder()
    .apiKey("your-api-key")
    .model("text-embedding-ada-002")
    .build();

EmbeddingRequest request = EmbeddingRequest.builder()
    .input("这是一段需要嵌入的文本")
    .build();

EmbeddingResponse response = client.embed(request);
float[] embedding = response.getEmbedding();
System.out.println("嵌入向量长度: " + embedding.length);
```

### 3. 工具调用 (Function Calling)

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.ChatRequest;
import ltd.idcu.est.llm.Tool;
import ltd.idcu.est.llm.ToolCall;

LlmClient client = OpenAiLlmClient.builder()
    .apiKey("your-api-key")
    .model("gpt-4")
    .build();

Tool weatherTool = Tool.builder()
    .name("getWeather")
    .description("获取指定城市的天气")
    .parameter("city", "string", "城市名称", true)
    .build();

ChatRequest request = ChatRequest.builder()
    .addMessage(ChatMessage.user("北京今天天气怎么样?"))
    .addTool(weatherTool)
    .build();

ChatResponse response = client.chat(request);

if (response.hasToolCalls()) {
    ToolCall toolCall = response.getToolCalls().get(0);
    String city = toolCall.getArgument("city");
    String weatherResult = getWeather(city);
    
    ChatRequest followUp = ChatRequest.builder()
        .addMessages(response.getMessages())
        .addToolResult(toolCall.getId(), weatherResult)
        .build();
    
    ChatResponse finalResponse = client.chat(followUp);
    System.out.println(finalResponse.getContent());
}
```

---

## 最佳实践

### 1. API Key 安全

```java
// ✅ 推荐：使用环境变量
String apiKey = System.getenv("OPENAI_API_KEY");

LlmClient client = OpenAiLlmClient.builder()
    .apiKey(apiKey)
    .build();

// ❌ 不推荐：硬编码 API Key
LlmClient badClient = OpenAiLlmClient.builder()
    .apiKey("sk-this-is-a-secret") // 不要提交到代码仓库
    .build();
```

### 2. Token 计费

```java
import ltd.idcu.est.llm.ChatResponse;
import ltd.idcu.est.llm.Usage;

ChatResponse response = client.chat(request);
Usage usage = response.getUsage();

int promptTokens = usage.getPromptTokens();
int completionTokens = usage.getCompletionTokens();
int totalTokens = usage.getTotalTokens();

System.out.println("Prompt Tokens: " + promptTokens);
System.out.println("Completion Tokens: " + completionTokens);
System.out.println("Total Tokens: " + totalTokens);
```

### 3. 错误处理和重试

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.retry.RetryPolicy;

RetryPolicy retryPolicy = RetryPolicy.builder()
    .maxRetries(3)
    .backoff(1000)
    .retryOn(ApiException.class)
    .build();

LlmClient client = OpenAiLlmClient.builder()
    .apiKey("your-api-key")
    .retryPolicy(retryPolicy)
    .build();

try {
    ChatResponse response = client.chat(request);
} catch (ApiException e) {
    System.err.println("API 调用失败: " + e.getMessage());
}
```

### 4. 提示词工程

```java
// ✅ 推荐：清晰的系统提示和用户提示
ChatRequest request = ChatRequest.builder()
    .addMessage(ChatMessage.system(
        "你是一个专业的 Java 开发专家。" +
        "请给出清晰、可运行的代码示例。" +
        "代码中应该包含必要的注释。"
    ))
    .addMessage(ChatMessage.user(
        "请写一个 Java 线程池的使用示例"
    ))
    .build();

// ❌ 不推荐：模糊的提示
ChatRequest badRequest = ChatRequest.builder()
    .addMessage(ChatMessage.user("写代码"))
    .build();
```

---

## 模块结构

```
est-ai-suite/
├── est-ai-config/     # AI 配置管理
├── est-llm-core/      # LLM 核心抽象
├── est-llm/           # LLM 提供商实现（OpenAI、Zhipu、Qwen、Ernie、Doubao、Kimi、Ollama）
└── est-ai-assistant/  # AI 助手和代码生成
```

---

## 相关资源

- [est-ai-assistant README](./est-ai-assistant/README.md) - AI 助手详细文档
- [est-llm README](./est-llm/README.md) - LLM 详细文档
- [示例代码](../../est-examples/est-examples-ai/) - AI 示例代码
- [EST Core](../../est-core/README.md) - 核心模块

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
