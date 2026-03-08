# AI 鍔╂墜 API 鍙傝€?
## AiAssistant 鎺ュ彛

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

### 鏂规硶璇存槑

#### chat(String message)
涓?AI 杩涜瀵硅瘽銆?
**鍙傛暟锛?*
- `message` - 鐢ㄦ埛娑堟伅

**杩斿洖鍊硷細** AI 鐨勫洖澶?
**绀轰緥锛?*
```java
String response = assistant.chat("浣犲ソ锛岃浠嬬粛涓€涓?EST 妗嗘灦");
```

---

#### chat(String message, Map&lt;String, Object&gt; context)
涓?AI 杩涜瀵硅瘽锛岄檮甯︿笂涓嬫枃淇℃伅銆?
**鍙傛暟锛?*
- `message` - 鐢ㄦ埛娑堟伅
- `context` - 涓婁笅鏂囦俊鎭?
**杩斿洖鍊硷細** AI 鐨勫洖澶?
**绀轰緥锛?*
```java
Map<String, Object> context = Map.of(
    "projectType", "web",
    "frameworkVersion", "2.1.0"
);
String response = assistant.chat("濡備綍鍒涘缓涓€涓?REST API锛?, context);
```

---

#### getQuickReference(String topic)
鑾峰彇鎸囧畾涓婚鐨勫揩閫熷弬鑰冦€?
**鍙傛暟锛?*
- `topic` - 涓婚鍚嶇О锛屽 "web寮€鍙?銆?渚濊禆娉ㄥ叆"銆?閰嶇疆绠＄悊"

**杩斿洖鍊硷細** 蹇€熷弬鑰冨唴瀹?
**绀轰緥锛?*
```java
String ref = assistant.getQuickReference("web寮€鍙?);
```

---

#### getBestPractice(String topic)
鑾峰彇鎸囧畾涓婚鐨勬渶浣冲疄璺点€?
**鍙傛暟锛?*
- `topic` - 涓婚鍚嶇О锛屽 "浠ｇ爜椋庢牸"銆?鎬ц兘浼樺寲"銆?瀹夊叏"

**杩斿洖鍊硷細** 鏈€浣冲疄璺靛唴瀹?
**绀轰緥锛?*
```java
String bestPractice = assistant.getBestPractice("浠ｇ爜椋庢牸");
```

---

#### getTutorial(String topic)
鑾峰彇鎸囧畾涓婚鐨勬暀绋嬨€?
**鍙傛暟锛?*
- `topic` - 涓婚鍚嶇О锛屽 "绗竴涓簲鐢?銆?REST API"銆?鏁版嵁搴撹闂?

**杩斿洖鍊硷細** 鏁欑▼鍐呭

**绀轰緥锛?*
```java
String tutorial = assistant.getTutorial("绗竴涓簲鐢?);
```

---

#### suggestCode(String requirement)
鏍规嵁闇€姹傚缓璁唬鐮併€?
**鍙傛暟锛?*
- `requirement` - 闇€姹傛弿杩?
**杩斿洖鍊硷細** 寤鸿鐨勪唬鐮?
**绀轰緥锛?*
```java
String code = assistant.suggestCode("鍒涘缓涓€涓?UserService锛屽寘鍚鍒犳敼鏌ユ柟娉?);
```

---

#### explainCode(String code)
瑙ｉ噴浠ｇ爜鐨勫姛鑳姐€?
**鍙傛暟锛?*
- `code` - 瑕佽В閲婄殑浠ｇ爜

**杩斿洖鍊硷細** 浠ｇ爜瑙ｉ噴

**绀轰緥锛?*
```java
String explanation = assistant.explainCode(code);
```

---

#### optimizeCode(String code)
浼樺寲鎻愪緵鐨勪唬鐮併€?
**鍙傛暟锛?*
- `code` - 瑕佷紭鍖栫殑浠ｇ爜

**杩斿洖鍊硷細** 浼樺寲鍚庣殑浠ｇ爜

**绀轰緥锛?*
```java
String optimized = assistant.optimizeCode(code);
```

---

## DefaultAiAssistant 瀹炵幇

榛樿鐨?AI 鍔╂墜瀹炵幇绫汇€?
### 鏋勯€犲嚱鏁?
```java
public DefaultAiAssistant()
public DefaultAiAssistant(LlmClient llmClient)
```

**绀轰緥锛?*
```java
// 浣跨敤榛樿閰嶇疆
AiAssistant assistant = new DefaultAiAssistant();

// 浣跨敤鑷畾涔?LLM 瀹㈡埛绔?LlmClient client = new OpenAiLlmClient(config);
AiAssistant assistant = new DefaultAiAssistant(client);
```

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
