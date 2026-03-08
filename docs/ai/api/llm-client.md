# LLM 客户端 API 参考

## LlmClient 接口

```java
public interface LlmClient {
    String complete(String prompt);
    String complete(String prompt, LlmOptions options);
    List<ChatMessage> chat(List<ChatMessage> messages);
    List<ChatMessage> chat(List<ChatMessage> messages, LlmOptions options);
    boolean isAvailable();
}
```

### 方法说明

#### complete(String prompt)
发送提示词并获取完成结果。

**参数：**
- `prompt` - 提示词

**返回值：** 完成结果

**示例：**
```java
String response = client.complete("写一首关于春天的诗");
```

---

#### complete(String prompt, LlmOptions options)
发送提示词并获取完成结果，使用自定义选项。

**参数：**
- `prompt` - 提示词
- `options` - LLM 选项

**返回值：** 完成结果

**示例：**
```java
LlmOptions options = new LlmOptions();
options.setTemperature(0.7);
options.setMaxTokens(1000);
String response = client.complete("写一首关于春天的诗", options);
```

---

#### chat(List&lt;ChatMessage&gt; messages)
进行多轮对话。

**参数：**
- `messages` - 消息列表

**返回值：** 更新后的消息列表

**示例：**
```java
List<ChatMessage> messages = new ArrayList<>();
messages.add(new ChatMessage("user", "你好"));
messages = client.chat(messages);
String assistantReply = messages.get(messages.size() - 1).getContent();
```

---

#### chat(List&lt;ChatMessage&gt; messages, LlmOptions options)
进行多轮对话，使用自定义选项。

**参数：**
- `messages` - 消息列表
- `options` - LLM 选项

**返回值：** 更新后的消息列表

---

#### isAvailable()
检查 LLM 服务是否可用。

**返回值：** 如果可用返回 true，否则返回 false

**示例：**
```java
if (client.isAvailable()) {
    // 使用 client
}
```

---

## LlmConfig 类

LLM 配置类。

### 构造函数

```java
public LlmConfig()
```

### 属性

| 属性 | 类型 | 说明 |
|------|------|------|
| provider | String | 提供商名称 |
| apiKey | String | API 密钥 |
| model | String | 模型名称 |
| baseUrl | String | API 基础 URL |
| timeout | int | 超时时间（毫秒） |

### 示例

```java
LlmConfig config = new LlmConfig();
config.setProvider("openai");
config.setApiKey("sk-xxx");
config.setModel("gpt-4");
config.setBaseUrl("https://api.openai.com/v1");
config.setTimeout(30000);
```

---

## LlmOptions 类

LLM 请求选项类。

### 构造函数

```java
public LlmOptions()
```

### 属性

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| temperature | double | 0.7 | 温度参数 |
| maxTokens | int | 2000 | 最大 token 数 |
| topP | double | 1.0 | top-p 参数 |
| frequencyPenalty | double | 0.0 | 频率惩罚 |
| presencePenalty | double | 0.0 | 存在惩罚 |

### 示例

```java
LlmOptions options = new LlmOptions();
options.setTemperature(0.5);
options.setMaxTokens(500);
```

---

## ChatMessage 类

对话消息类。

### 构造函数

```java
public ChatMessage(String role, String content)
```

### 属性

| 属性 | 类型 | 说明 |
|------|------|------|
| role | String | 角色（user/assistant/system） |
| content | String | 消息内容 |

### 示例

```java
ChatMessage message = new ChatMessage("user", "你好");
```

---

## LlmClientFactory 类

LLM 客户端工厂类。

### 方法

#### create(LlmConfig config)
根据配置创建 LLM 客户端。

**参数：**
- `config` - LLM 配置

**返回值：** LLM 客户端实例

**示例：**
```java
LlmClient client = LlmClientFactory.create(config);
```

---

## 提供商实现

### OpenAiLlmClient
OpenAI 客户端实现。

```java
LlmConfig config = new LlmConfig();
config.setApiKey("sk-xxx");
config.setModel("gpt-4");

LlmClient client = new OpenAiLlmClient(config);
```

### ZhipuLlmClient
智谱 AI 客户端实现。

```java
LlmConfig config = new LlmConfig();
config.setApiKey("your-api-key");
config.setModel("glm-4");

LlmClient client = new ZhipuLlmClient(config);
```

### QwenLlmClient
通义千问客户端实现。

```java
LlmConfig config = new LlmConfig();
config.setApiKey("your-api-key");
config.setModel("qwen-turbo");

LlmClient client = new QwenLlmClient(config);
```

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
