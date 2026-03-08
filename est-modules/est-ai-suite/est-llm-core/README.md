# EST LLM Core

澶ц瑷€妯″瀷锛圠LM锛夋牳蹇冩娊璞″眰锛屾彁渚涚粺涓€鐨?LLM 鎺ュ彛鍜屾娊璞°€?
## 鍔熻兘鐗规€?
- 缁熶竴鐨?LLM 瀹㈡埛绔帴鍙?- 娑堟伅瀵硅瘽鎶借薄
- 娴佸紡鍝嶅簲鏀寔
- 鍑芥暟璋冪敤鏀寔
- Token 璁℃暟
- 寮傚父澶勭悊

## 妯″潡缁撴瀯

```
est-llm-core/
鈹溾攢鈹€ est-llm-core-api/    # LLM 鏍稿績 API
鈹斺攢鈹€ est-llm-core-impl/   # LLM 鏍稿績瀹炵幇
```

## 蹇€熷紑濮?
### 寮曞叆渚濊禆

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-llm-core-api</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-llm-core-impl</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 鍩烘湰浣跨敤

```java
LlmClient client = LlmClients.create("openai");

LlmMessage message = LlmMessage.user("Hello, how are you?");
LlmResponse response = client.chat(Collections.singletonList(message));

System.out.println(response.getContent());
```

### 娴佸紡鍝嶅簲

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

## 鏍稿績 API

### LlmClient

LLM 瀹㈡埛绔富鎺ュ彛銆?
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

娑堟伅瀵硅薄銆?
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

LLM 璋冪敤閫夐」銆?
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

## 鍔熻兘璇存槑

### 瀵硅瘽妯″紡

鏀寔澶氳疆瀵硅瘽锛岄€氳繃娑堟伅鍒楄〃缁存姢瀵硅瘽鍘嗗彶銆?
### 娴佸紡杈撳嚭

鏀寔瀹炴椂娴佸紡杈撳嚭锛岄€傚悎闀挎枃鏈敓鎴愬満鏅€?
### 鍑芥暟璋冪敤

鏀寔鍑芥暟璋冪敤鍔熻兘锛屽彲浠ヨ LLM 璋冪敤澶栭儴宸ュ叿銆?
### Token 绠＄悊

鎻愪緵 Token 璁℃暟鍔熻兘锛屽府鍔╂帶鍒舵垚鏈€?
## 鐩稿叧妯″潡

- [est-ai-config](../est-ai-config/): AI 閰嶇疆绠＄悊
- [est-llm](../est-llm/): LLM 鎻愪緵鍟嗗疄鐜?- [est-ai-assistant](../est-ai-assistant/): AI 鍔╂墜鍔熻兘
