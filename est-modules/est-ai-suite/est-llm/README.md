# EST LLM

大语言模型（LLM）提供商实现模块，支持多个主流 LLM 服务提供商。

## 功能特性

- 多提供商支持
- 统一的接口
- 自动重试机制
- 请求超时控制
- 负载均衡

## 支持的提供商

| 提供商 | 模型示例 | 说明 |
|--------|----------|------|
| OpenAI | GPT-4, GPT-3.5 | OpenAI 官方 API |
| Zhipu AI | GLM-4, GLM-3 | 智谱 AI |
| Alibaba Qwen | Qwen-Turbo, Qwen-Plus | 阿里云通义千问 |
| Baidu Ernie | Ernie-4.0, Ernie-3.5 | 百度文心一言 |
| ByteDance Doubao | Doubao-Pro, Doubao-Lite | 字节跳动豆包 |
| Moonshot Kimi | Kimi-Max, Kimi-Mid | 月之暗面 Kimi |
| Ollama | Llama3, Mistral | 本地模型运行 |

## 模块结构

```
est-llm/
├── est-llm-api/    # LLM 提供商 API
└── est-llm-impl/   # LLM 提供商实现
```

## 快速开始

### 引入依赖

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-llm-api</artifactId>
    <version>2.0.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-llm-impl</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 使用 OpenAI

```java
LlmClient client = OpenAiLlmClient.builder()
    .apiKey("your-api-key")
    .model("gpt-4")
    .build();

LlmResponse response = client.chat(
    Collections.singletonList(LlmMessage.user("Hello!"))
);
```

### 使用 Zhipu AI

```java
LlmClient client = ZhipuLlmClient.builder()
    .apiKey("your-api-key")
    .model("glm-4")
    .build();
```

### 使用 Ollama（本地模型）

```java
LlmClient client = OllamaLlmClient.builder()
    .baseUrl("http://localhost:11434")
    .model("llama3")
    .build();
```

### 使用配置自动创建

```java
LlmClient client = LlmProviders.create("openai");
```

## 提供商配置

### OpenAI

```yaml
ai:
  providers:
    openai:
      api-key: ${OPENAI_API_KEY}
      base-url: https://api.openai.com/v1
      model: gpt-4
      timeout: 30s
      retries: 3
```

### Zhipu AI

```yaml
ai:
  providers:
    zhipu:
      api-key: ${ZHIPU_API_KEY}
      model: glm-4
      timeout: 30s
```

### Ollama

```yaml
ai:
  providers:
    ollama:
      base-url: http://localhost:11434
      model: llama3
```

## 高级功能

### 自动重试

所有提供商都支持自动重试机制，可以在配置中设置重试次数。

### 请求超时

可以为每个请求设置独立的超时时间。

### 负载均衡

支持多个 API Key 的负载均衡，提高可用性。

### 自定义 HTTP 客户端

```java
LlmClient client = OpenAiLlmClient.builder()
    .apiKey("your-key")
    .httpClient(customHttpClient)
    .build();
```

## 错误处理

```java
try {
    LlmResponse response = client.chat(messages);
} catch (LlmException e) {
    switch (e.getErrorCode()) {
        case AUTHENTICATION:
            System.err.println("API Key 无效");
            break;
        case RATE_LIMIT:
            System.err.println("请求频率超限");
            break;
        case TIMEOUT:
            System.err.println("请求超时");
            break;
        default:
            System.err.println("未知错误: " + e.getMessage());
    }
}
```

## 相关模块

- [est-ai-config](../est-ai-config/): AI 配置管理
- [est-llm-core](../est-llm-core/): LLM 核心抽象
- [est-ai-assistant](../est-ai-assistant/): AI 助手功能
