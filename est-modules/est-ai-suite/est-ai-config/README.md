# EST AI Config

AI 閰嶇疆绠＄悊妯″潡锛岀敤浜庣粺涓€绠＄悊 EST 妗嗘灦涓殑 AI 鍜?LLM 鐩稿叧閰嶇疆銆?
## 鍔熻兘鐗规€?
- 缁熶竴鐨?AI 閰嶇疆绠＄悊
- YAML 鏍煎紡閰嶇疆鏀寔
- 澶氭彁渚涘晢閰嶇疆
- 杩愯鏃堕厤缃洿鏂?
## 妯″潡缁撴瀯

```
est-ai-config/
鈹溾攢鈹€ est-ai-config-api/    # 閰嶇疆 API 鎺ュ彛
鈹斺攢鈹€ est-ai-config-impl/   # 閰嶇疆瀹炵幇
```

## 蹇€熷紑濮?
### 寮曞叆渚濊禆

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-ai-config-api</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-ai-config-impl</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 閰嶇疆绀轰緥

鍦?`est-ai-config.yml` 涓厤缃細

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

## API 璇存槑

### 閰嶇疆鎺ュ彛

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

### 閰嶇疆绠＄悊鍣?
```java
public interface AiConfigManager {
    AiConfig getConfig(String provider);
    AiConfig getDefaultConfig();
    void reloadConfig();
}
```

## 閰嶇疆椤硅鏄?
| 閰嶇疆椤?| 璇存槑 | 榛樿鍊?|
|--------|------|--------|
| provider | 榛樿鎻愪緵鍟?| openai |
| api-key | API 瀵嗛挜 | - |
| base-url | API 鍩虹鍦板潃 | - |
| model | 榛樿妯″瀷 | - |
| temperature | 娓╁害鍙傛暟 | 0.7 |
| max-tokens | 鏈€澶?token 鏁?| 2000 |

## 鐩稿叧妯″潡

- [est-llm-core](../est-llm-core/): LLM 鏍稿績鎶借薄
- [est-llm](../est-llm/): LLM 鎻愪緵鍟嗗疄鐜?- [est-ai-assistant](../est-ai-assistant/): AI 鍔╂墜鍔熻兘
