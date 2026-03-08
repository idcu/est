# EST LLM Core

大语言模型（LLM）核心抽象层，提供统一的 LLM 接口和抽象。

## 功能特性

- 统一的 LLM 客户端接口
- 消息对话抽象
- 流式响应支持
- 函数调用支持
- Token 计数
- 异常处理

## 模块结构

```
est-llm-core/
├── est-llm-core-api/    # LLM 核心 API
└── est-llm-core-impl/   # LLM 核心实现
```

## 快速开始

### 引入依赖

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-llm-core-api</artifactId>
    <version>2.0.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-llm-core-impl</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 基本使用

```java
LlmClient client = LlmClients.create("openai");

LlmMessage message = LlmMessage.user("Hello, how are you?");
LlmResponse response = client.chat(Collections.singletonList(message));

System.out.println(response.getContent());
```

### 流式响应

```java
LlmClient client = LlmClients.create("openai");

client.chatStream(
    Collections.singletonList(LlmMessage.user("Tell me a story")),
    new StreamCallback() {
        @Override
        public void onChunk(String chunk) {
            System.out.print(chunk);
        }
        
        @Override
        public void onComplete(LlmResponse response) {
            System.out.println("\nComplete!");
        }
        
        @Override
        public void onError(LlmException e) {
            e.printStackTrace();
        }
    }
);
```

## 核心 API

### LlmClient

LLM 客户端主接口。

```java
public interface LlmClient {
    LlmResponse chat(List<LlmMessage> messages);
    LlmResponse chat(List<LlmMessage> messages, LlmOptions options);
    void chatStream(List<LlmMessage> messages, StreamCallback callback);
    void chatStream(List<LlmMessage> messages, LlmOptions options, StreamCallback callback);
    int countTokens(String text);
}
```

### LlmMessage

消息对象。

```java
public class LlmMessage {
    public static LlmMessage system(String content);
    public static LlmMessage user(String content);
    public static LlmMessage assistant(String content);
    public static LlmMessage function(String name, String content);
    
    public String getRole();
    public String getContent();
}
```

### LlmOptions

LLM 调用选项。

```java
public class LlmOptions {
    public static LlmOptions builder()
        .model(String model)
        .temperature(double temperature)
        .maxTokens(int maxTokens)
        .topP(double topP)
        .frequencyPenalty(double penalty)
        .presencePenalty(double penalty)
        .build();
}
```

## 功能说明

### 对话模式

支持多轮对话，通过消息列表维护对话历史。

### 流式输出

支持实时流式输出，适合长文本生成场景。

### 函数调用

支持函数调用功能，可以让 LLM 调用外部工具。

### Token 管理

提供 Token 计数功能，帮助控制成本。

## 相关模块

- [est-ai-config](../est-ai-config/): AI 配置管理
- [est-llm](../est-llm/): LLM 提供商实现
- [est-ai-assistant](../est-ai-assistant/): AI 助手功能
