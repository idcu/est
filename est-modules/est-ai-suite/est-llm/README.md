# EST LLM

澶ц瑷€妯″瀷锛圠LM锛夋彁渚涘晢瀹炵幇妯″潡锛屾敮鎸佸涓富娴?LLM 鏈嶅姟鎻愪緵鍟嗐€?
## 鍔熻兘鐗规€?
- 澶氭彁渚涘晢鏀寔
- 缁熶竴鐨勬帴鍙?- 鑷姩閲嶈瘯鏈哄埗
- 璇锋眰瓒呮椂鎺у埗
- 璐熻浇鍧囪　

## 鏀寔鐨勬彁渚涘晢

| 鎻愪緵鍟?| 妯″瀷绀轰緥 | 璇存槑 |
|--------|----------|------|
| OpenAI | GPT-4, GPT-3.5 | OpenAI 瀹樻柟 API |
| Zhipu AI | GLM-4, GLM-3 | 鏅鸿氨 AI |
| Alibaba Qwen | Qwen-Turbo, Qwen-Plus | 闃块噷浜戦€氫箟鍗冮棶 |
| Baidu Ernie | Ernie-4.0, Ernie-3.5 | 鐧惧害鏂囧績涓€瑷€ |
| ByteDance Doubao | Doubao-Pro, Doubao-Lite | 瀛楄妭璺冲姩璞嗗寘 |
| Moonshot Kimi | Kimi-Max, Kimi-Mid | 鏈堜箣鏆楅潰 Kimi |
| Ollama | Llama3, Mistral | 鏈湴妯″瀷杩愯 |

## 妯″潡缁撴瀯

```
est-llm/
鈹溾攢鈹€ est-llm-api/    # LLM 鎻愪緵鍟?API
鈹斺攢鈹€ est-llm-impl/   # LLM 鎻愪緵鍟嗗疄鐜?```

## 蹇€熷紑濮?
### 寮曞叆渚濊禆

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-llm-api</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-llm-impl</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 浣跨敤 OpenAI

```java
LlmClient client = OpenAiLlmClient.builder()
    .apiKey("your-api-key")
    .model("gpt-4")
    .build();

LlmResponse response = client.chat(
    Collections.singletonList(LlmMessage.user("Hello!"))
);
```

### 浣跨敤 Zhipu AI

```java
LlmClient client = ZhipuLlmClient.builder()
    .apiKey("your-api-key")
    .model("glm-4")
    .build();
```

### 浣跨敤 Ollama锛堟湰鍦版ā鍨嬶級

```java
LlmClient client = OllamaLlmClient.builder()
    .baseUrl("http://localhost:11434")
    .model("llama3")
    .build();
```

### 浣跨敤閰嶇疆鑷姩鍒涘缓

```java
LlmClient client = LlmProviders.create("openai");
```

## 鎻愪緵鍟嗛厤缃?
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

## 楂樼骇鍔熻兘

### 鑷姩閲嶈瘯

鎵€鏈夋彁渚涘晢閮芥敮鎸佽嚜鍔ㄩ噸璇曟満鍒讹紝鍙互鍦ㄩ厤缃腑璁剧疆閲嶈瘯娆℃暟銆?
### 璇锋眰瓒呮椂

鍙互涓烘瘡涓姹傝缃嫭绔嬬殑瓒呮椂鏃堕棿銆?
### 璐熻浇鍧囪　

鏀寔澶氫釜 API Key 鐨勮礋杞藉潎琛★紝鎻愰珮鍙敤鎬с€?
### 鑷畾涔?HTTP 瀹㈡埛绔?
```java
LlmClient client = OpenAiLlmClient.builder()
    .apiKey("your-key")
    .httpClient(customHttpClient)
    .build();
```

## 閿欒澶勭悊

```java
try {
    LlmResponse response = client.chat(messages);
} catch (LlmException e) {
    switch (e.getErrorCode()) {
        case AUTHENTICATION:
            System.err.println("API Key 鏃犳晥");
            break;
        case RATE_LIMIT:
            System.err.println("璇锋眰棰戠巼瓒呴檺");
            break;
        case TIMEOUT:
            System.err.println("璇锋眰瓒呮椂");
            break;
        default:
            System.err.println("鏈煡閿欒: " + e.getMessage());
    }
}
```

## 鐩稿叧妯″潡

- [est-ai-config](../est-ai-config/): AI 閰嶇疆绠＄悊
- [est-llm-core](../est-llm-core/): LLM 鏍稿績鎶借薄
- [est-ai-assistant](../est-ai-assistant/): AI 鍔╂墜鍔熻兘
