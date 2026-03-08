# LLM 瀹㈡埛绔?API 鍙傝€?
## LlmClient 鎺ュ彛

```java
public interface LlmClient {
    String complete(String prompt);
    String complete(String prompt, LlmOptions options);
    List<ChatMessage> chat(List<ChatMessage> messages);
    List<ChatMessage> chat(List<ChatMessage> messages, LlmOptions options);
    boolean isAvailable();
}
```

### 鏂规硶璇存槑

#### complete(String prompt)
鍙戦€佹彁绀鸿瘝骞惰幏鍙栧畬鎴愮粨鏋溿€?
**鍙傛暟锛?*
- `prompt` - 鎻愮ず璇?
**杩斿洖鍊硷細** 瀹屾垚缁撴灉

**绀轰緥锛?*
```java
String response = client.complete("鍐欎竴棣栧叧浜庢槬澶╃殑璇?);
```

---

#### complete(String prompt, LlmOptions options)
鍙戦€佹彁绀鸿瘝骞惰幏鍙栧畬鎴愮粨鏋滐紝浣跨敤鑷畾涔夐€夐」銆?
**鍙傛暟锛?*
- `prompt` - 鎻愮ず璇?- `options` - LLM 閫夐」

**杩斿洖鍊硷細** 瀹屾垚缁撴灉

**绀轰緥锛?*
```java
LlmOptions options = new LlmOptions();
options.setTemperature(0.7);
options.setMaxTokens(1000);
String response = client.complete("鍐欎竴棣栧叧浜庢槬澶╃殑璇?, options);
```

---

#### chat(List&lt;ChatMessage&gt; messages)
杩涜澶氳疆瀵硅瘽銆?
**鍙傛暟锛?*
- `messages` - 娑堟伅鍒楄〃

**杩斿洖鍊硷細** 鏇存柊鍚庣殑娑堟伅鍒楄〃

**绀轰緥锛?*
```java
List<ChatMessage> messages = new ArrayList<>();
messages.add(new ChatMessage("user", "浣犲ソ"));
messages = client.chat(messages);
String assistantReply = messages.get(messages.size() - 1).getContent();
```

---

#### chat(List&lt;ChatMessage&gt; messages, LlmOptions options)
杩涜澶氳疆瀵硅瘽锛屼娇鐢ㄨ嚜瀹氫箟閫夐」銆?
**鍙傛暟锛?*
- `messages` - 娑堟伅鍒楄〃
- `options` - LLM 閫夐」

**杩斿洖鍊硷細** 鏇存柊鍚庣殑娑堟伅鍒楄〃

---

#### isAvailable()
妫€鏌?LLM 鏈嶅姟鏄惁鍙敤銆?
**杩斿洖鍊硷細** 濡傛灉鍙敤杩斿洖 true锛屽惁鍒欒繑鍥?false

**绀轰緥锛?*
```java
if (client.isAvailable()) {
    // 浣跨敤 client
}
```

---

## LlmConfig 绫?
LLM 閰嶇疆绫汇€?
### 鏋勯€犲嚱鏁?
```java
public LlmConfig()
```

### 灞炴€?
| 灞炴€?| 绫诲瀷 | 璇存槑 |
|------|------|------|
| provider | String | 鎻愪緵鍟嗗悕绉?|
| apiKey | String | API 瀵嗛挜 |
| model | String | 妯″瀷鍚嶇О |
| baseUrl | String | API 鍩虹 URL |
| timeout | int | 瓒呮椂鏃堕棿锛堟绉掞級 |

### 绀轰緥

```java
LlmConfig config = new LlmConfig();
config.setProvider("openai");
config.setApiKey("sk-xxx");
config.setModel("gpt-4");
config.setBaseUrl("https://api.openai.com/v1");
config.setTimeout(30000);
```

---

## LlmOptions 绫?
LLM 璇锋眰閫夐」绫汇€?
### 鏋勯€犲嚱鏁?
```java
public LlmOptions()
```

### 灞炴€?
| 灞炴€?| 绫诲瀷 | 榛樿鍊?| 璇存槑 |
|------|------|--------|------|
| temperature | double | 0.7 | 娓╁害鍙傛暟 |
| maxTokens | int | 2000 | 鏈€澶?token 鏁?|
| topP | double | 1.0 | top-p 鍙傛暟 |
| frequencyPenalty | double | 0.0 | 棰戠巼鎯╃綒 |
| presencePenalty | double | 0.0 | 瀛樺湪鎯╃綒 |

### 绀轰緥

```java
LlmOptions options = new LlmOptions();
options.setTemperature(0.5);
options.setMaxTokens(500);
```

---

## ChatMessage 绫?
瀵硅瘽娑堟伅绫汇€?
### 鏋勯€犲嚱鏁?
```java
public ChatMessage(String role, String content)
```

### 灞炴€?
| 灞炴€?| 绫诲瀷 | 璇存槑 |
|------|------|------|
| role | String | 瑙掕壊锛坲ser/assistant/system锛?|
| content | String | 娑堟伅鍐呭 |

### 绀轰緥

```java
ChatMessage message = new ChatMessage("user", "浣犲ソ");
```

---

## LlmClientFactory 绫?
LLM 瀹㈡埛绔伐鍘傜被銆?
### 鏂规硶

#### create(LlmConfig config)
鏍规嵁閰嶇疆鍒涘缓 LLM 瀹㈡埛绔€?
**鍙傛暟锛?*
- `config` - LLM 閰嶇疆

**杩斿洖鍊硷細** LLM 瀹㈡埛绔疄渚?
**绀轰緥锛?*
```java
LlmClient client = LlmClientFactory.create(config);
```

---

## 鎻愪緵鍟嗗疄鐜?
### OpenAiLlmClient
OpenAI 瀹㈡埛绔疄鐜般€?
```java
LlmConfig config = new LlmConfig();
config.setApiKey("sk-xxx");
config.setModel("gpt-4");

LlmClient client = new OpenAiLlmClient(config);
```

### ZhipuLlmClient
鏅鸿氨 AI 瀹㈡埛绔疄鐜般€?
```java
LlmConfig config = new LlmConfig();
config.setApiKey("your-api-key");
config.setModel("glm-4");

LlmClient client = new ZhipuLlmClient(config);
```

### QwenLlmClient
閫氫箟鍗冮棶瀹㈡埛绔疄鐜般€?
```java
LlmConfig config = new LlmConfig();
config.setApiKey("your-api-key");
config.setModel("qwen-turbo");

LlmClient client = new QwenLlmClient(config);
```

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
