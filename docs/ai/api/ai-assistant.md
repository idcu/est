# AI 助手 API 参考

## AiAssistant 接口

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

### 方法说明

#### chat(String message)
与 AI 进行对话。

**参数：**
- `message` - 用户消息

**返回值：** AI 的回复

**示例：**
```java
String response = assistant.chat("你好，请介绍一下 EST 框架");
```

---

#### chat(String message, Map&lt;String, Object&gt; context)
与 AI 进行对话，附带上下文信息。

**参数：**
- `message` - 用户消息
- `context` - 上下文信息

**返回值：** AI 的回复

**示例：**
```java
Map<String, Object> context = Map.of(
    "projectType", "web",
    "frameworkVersion", "2.0.0"
);
String response = assistant.chat("如何创建一个 REST API？", context);
```

---

#### getQuickReference(String topic)
获取指定主题的快速参考。

**参数：**
- `topic` - 主题名称，如 "web开发"、"依赖注入"、"配置管理"

**返回值：** 快速参考内容

**示例：**
```java
String ref = assistant.getQuickReference("web开发");
```

---

#### getBestPractice(String topic)
获取指定主题的最佳实践。

**参数：**
- `topic` - 主题名称，如 "代码风格"、"性能优化"、"安全"

**返回值：** 最佳实践内容

**示例：**
```java
String bestPractice = assistant.getBestPractice("代码风格");
```

---

#### getTutorial(String topic)
获取指定主题的教程。

**参数：**
- `topic` - 主题名称，如 "第一个应用"、"REST API"、"数据库访问"

**返回值：** 教程内容

**示例：**
```java
String tutorial = assistant.getTutorial("第一个应用");
```

---

#### suggestCode(String requirement)
根据需求建议代码。

**参数：**
- `requirement` - 需求描述

**返回值：** 建议的代码

**示例：**
```java
String code = assistant.suggestCode("创建一个 UserService，包含增删改查方法");
```

---

#### explainCode(String code)
解释代码的功能。

**参数：**
- `code` - 要解释的代码

**返回值：** 代码解释

**示例：**
```java
String explanation = assistant.explainCode(code);
```

---

#### optimizeCode(String code)
优化提供的代码。

**参数：**
- `code` - 要优化的代码

**返回值：** 优化后的代码

**示例：**
```java
String optimized = assistant.optimizeCode(code);
```

---

## DefaultAiAssistant 实现

默认的 AI 助手实现类。

### 构造函数

```java
public DefaultAiAssistant()
public DefaultAiAssistant(LlmClient llmClient)
```

**示例：**
```java
// 使用默认配置
AiAssistant assistant = new DefaultAiAssistant();

// 使用自定义 LLM 客户端
LlmClient client = new OpenAiLlmClient(config);
AiAssistant assistant = new DefaultAiAssistant(client);
```

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
