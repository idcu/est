# LLM 集成

EST AI 支持与多种大语言模型（LLM）集成，目前已实现智谱 AI（GLM）客户端。

---

## 支持的 LLM

| 提供商 | 模型 | 状态 |
|--------|------|------|
| 智谱 AI | GLM-4, GLM-3-Turbo | ✅ 已实现 |
| OpenAI | GPT-4, GPT-3.5 | 📋 计划中 |
| Anthropic | Claude | 📋 计划中 |
| 通义千问 | Qwen | 📋 计划中 |

---

## 智谱 AI LLM 客户端

### 基本使用

```java
import ltd.idcu.est.features.ai.api.AiAssistant;
import ltd.idcu.est.features.ai.impl.DefaultAiAssistant;
import ltd.idcu.est.features.ai.impl.llm.ZhipuAiLlmClient;

public class ZhipuAiExample {
    public static void main(String[] args) {
        // 创建 LLM 客户端
        ZhipuAiLlmClient llmClient = new ZhipuAiLlmClient();
        
        // 设置 API Key
        llmClient.setApiKey("your-api-key-here");
        
        // 设置模型（可选，默认 GLM-4）
        llmClient.setModel("GLM-4");
        
        // 检查是否可用
        if (llmClient.isAvailable()) {
            System.out.println("LLM 客户端可用");
        }
        
        // 设置到 AiAssistant
        AiAssistant aiAssistant = new DefaultAiAssistant();
        aiAssistant.setLlmClient(llmClient);
    }
}
```

### 生成文本

```java
String prompt = "请介绍一下 EST 框架";
String response = llmClient.generate(prompt);
System.out.println(response);
```

### 对话功能

```java
String userMessage = "你好，我想学习 EST 框架";
String reply = llmClient.chat(userMessage);
System.out.println(reply);
```

### 带选项的生成

```java
import ltd.idcu.est.features.ai.api.LlmOptions;

LlmOptions options = new LlmOptions()
    .setTemperature(0.7)
    .setMaxTokens(1000)
    .setTopP(0.9);

String response = llmClient.generate(prompt, options);
```

---

## LlmClient 接口

```java
package ltd.idcu.est.features.ai.api;

public interface LlmClient {
    String getName();
    String getModel();
    void setModel(String model);
    boolean isAvailable();
    String generate(String prompt);
    String generate(String prompt, LlmOptions options);
    String chat(String message);
    String chat(String message, LlmOptions options);
}
```

---

## LlmOptions 配置

```java
LlmOptions options = new LlmOptions()
    .setTemperature(0.7)        // 温度，0-2，越高越随机
    .setMaxTokens(1000)         // 最大 token 数
    .setTopP(0.9)               // 核采样参数
    .setTopK(50)                // Top-K 采样
    .setPresencePenalty(0.0)    // 存在惩罚
    .setFrequencyPenalty(0.0)   // 频率惩罚
    .setStopSequences(List.of("END")); // 停止序列
```

---

## 获取 API Key

### 智谱 AI

1. 访问 [智谱 AI 开放平台](https://open.bigmodel.cn/)
2. 注册账号并登录
3. 在控制台创建 API Key
4. 复制 API Key 配置到你的代码中

---

## 自定义 LLM 客户端

你可以实现自己的 LLM 客户端：

```java
import ltd.idcu.est.features.ai.api.LlmClient;
import ltd.idcu.est.features.ai.api.LlmOptions;

public class CustomLlmClient implements LlmClient {
    private String apiKey;
    private String model = "custom-model";

    @Override
    public String getName() {
        return "Custom LLM";
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public boolean isAvailable() {
        return apiKey != null && !apiKey.isEmpty();
    }

    @Override
    public String generate(String prompt) {
        return generate(prompt, new LlmOptions());
    }

    @Override
    public String generate(String prompt, LlmOptions options) {
        // 实现你的 LLM 调用逻辑
        return "Generated response";
    }

    @Override
    public String chat(String message) {
        return chat(message, new LlmOptions());
    }

    @Override
    public String chat(String message, LlmOptions options) {
        // 实现你的对话逻辑
        return "Chat response";
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
```

---

## 最佳实践

1. **安全存储**: 不要在代码中硬编码 API Key，使用环境变量或配置文件
2. **错误处理**: 处理网络错误和 API 限流
3. **成本控制**: 设置合理的 maxTokens 限制
4. **缓存机制**: 对相同请求进行缓存，减少 API 调用
5. **监控**: 监控 API 使用情况和成本

---

## 环境变量配置

推荐使用环境变量存储 API Key：

```java
String apiKey = System.getenv("ZHIPU_API_KEY");
if (apiKey != null) {
    llmClient.setApiKey(apiKey);
}
```

设置环境变量（Windows）：
```cmd
set ZHIPU_API_KEY=your-api-key-here
```

设置环境变量（Linux/Mac）：
```bash
export ZHIPU_API_KEY=your-api-key-here
```

---

**文档版本**: 2.0  
**最后更新**: 2026-03-07  
**维护者**: EST 架构团队
