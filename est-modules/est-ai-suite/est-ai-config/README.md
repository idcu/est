# EST AI Config

AI 配置管理模块，用于统一管理 EST 框架中的 AI 和 LLM 相关配置。

## 功能特性

- 统一的 AI 配置管理
- YAML 格式配置支持
- 多提供商配置
- 运行时配置更新

## 模块结构

```
est-ai-config/
├── est-ai-config-api/    # 配置 API 接口
└── est-ai-config-impl/   # 配置实现
```

## 快速开始

### 引入依赖

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-ai-config-api</artifactId>
    <version>2.0.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-ai-config-impl</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 配置示例

在 `est-ai-config.yml` 中配置：

```yaml
ai:
  providers:
    openai:
      api-key: ${OPENAI_API_KEY}
      base-url: https://api.openai.com/v1
      model: gpt-4
    zhipu:
      api-key: ${ZHIPU_API_KEY}
      model: glm-4
  defaults:
    provider: openai
    temperature: 0.7
    max-tokens: 2000
```

## API 说明

### 配置接口

```java
public interface AiConfig {
    String getProvider();
    String getApiKey();
    String getBaseUrl();
    String getModel();
    double getTemperature();
    int getMaxTokens();
}
```

### 配置管理器

```java
public interface AiConfigManager {
    AiConfig getConfig(String provider);
    AiConfig getDefaultConfig();
    void reloadConfig();
}
```

## 配置项说明

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| provider | 默认提供商 | openai |
| api-key | API 密钥 | - |
| base-url | API 基础地址 | - |
| model | 默认模型 | - |
| temperature | 温度参数 | 0.7 |
| max-tokens | 最大 token 数 | 2000 |

## 相关模块

- [est-llm-core](../est-llm-core/): LLM 核心抽象
- [est-llm](../est-llm/): LLM 提供商实现
- [est-ai-assistant](../est-ai-assistant/): AI 助手功能
