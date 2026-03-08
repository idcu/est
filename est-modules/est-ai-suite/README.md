# EST AI Suite AI 濂椾欢 - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
1. [浠€涔堟槸 EST AI Suite锛焆(#浠€涔堟槸-est-ai-suite)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [鍩虹绡嘳(#鍩虹绡?
4. [杩涢樁绡嘳(#杩涢樁绡?
5. [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 浠€涔堟槸 EST AI Suite锛?
### 鐢ㄥぇ鐧借瘽鐞嗚В

EST AI Suite 灏卞儚鏄竴涓?AI 宸ュ叿绠?銆傛兂璞′竴涓嬩綘瑕佸湪搴旂敤涓泦鎴?AI 鍔熻兘锛?
**浼犵粺鏂瑰紡**锛氭瘡涓?AI 鎻愪緵鍟嗛兘瑕佸啓涓嶅悓鐨勪唬鐮侊紝杩樿鑷繁澶勭悊 API 璋冪敤銆侀敊璇噸璇曘€乀oken 璁¤垂... 寰堥夯鐑︼紒

**EST AI Suite 鏂瑰紡**锛氱粰浣犱竴濂楃粺涓€鐨?AI 宸ュ叿锛岄噷闈㈡湁锛?- 馃 **LLM 鏍稿績鎶借薄** - 缁熶竴鐨勫ぇ璇█妯″瀷鎺ュ彛
- 馃 **澶氭彁渚涘晢鏀寔** - 鏀寔 OpenAI銆佹櫤璋便€侀€氫箟鍗冮棶銆佹枃蹇冧竴瑷€銆佽眴鍖呫€並imi銆丱llama
- 馃挰 **AI 鍔╂墜** - 瀵硅瘽寮?AI 鍔╂墜
- 馃摑 **浠ｇ爜鐢熸垚** - 鑷姩鐢熸垚浠ｇ爜

### 鏍稿績鐗圭偣

- 馃幆 **绠€鍗曟槗鐢?* - 缁熶竴鐨?API锛屼笉鐢ㄥ叧蹇冨簳灞傚疄鐜?- 鈿?**楂樻€ц兘** - 浼樺寲鐨?API 璋冪敤锛屾敮鎸侀噸璇曞拰缂撳瓨
- 馃敡 **鐏垫椿鎵╁睍** - 鍙互鑷畾涔?LLM 鎻愪緵鍟?- 馃帹 **鍔熻兘瀹屾暣** - LLM銆丄I 鍔╂墜銆佷唬鐮佺敓鎴愪竴搴斾勘鍏?
---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細寮曞叆渚濊禆

鍦ㄤ綘鐨?Maven pom.xml 涓坊鍔狅細

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-ai-assistant</artifactId>
        <version>2.1.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-llm</artifactId>
        <version>2.1.0</version>
    </dependency>
</dependencies>
```

### 绗簩姝ワ細浣犵殑绗竴涓?AI 搴旂敤

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.LlmClients;
import ltd.idcu.est.llm.ChatMessage;
import ltd.idcu.est.llm.ChatRequest;
import ltd.idcu.est.llm.ChatResponse;
import ltd.idcu.est.llm.openai.OpenAiLlmClient;

public class FirstAiApp {
    public static void main(String[] args) {
        System.out.println("=== EST AI Suite 绗竴涓ず渚?===\n");
        
        LlmClient client = OpenAiLlmClient.builder()
            .apiKey("your-api-key")
            .model("gpt-4")
            .build();
        
        ChatRequest request = ChatRequest.builder()
            .addMessage(ChatMessage.user("浣犲ソ锛岃浠嬬粛涓€涓嬩綘鑷繁"))
            .temperature(0.7)
            .maxTokens(500)
            .build();
        
        ChatResponse response = client.chat(request);
        System.out.println("AI 鍥炲: " + response.getContent());
    }
}
```

---

## 鍩虹绡?
### 1. est-llm-core LLM 鏍稿績鎶借薄

#### 缁熶竴鐨?LLM 鎺ュ彛

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.ChatMessage;
import ltd.idcu.est.llm.ChatRequest;
import ltd.idcu.est.llm.ChatResponse;

public interface LlmClient {
    ChatResponse chat(ChatRequest request);
    
    CompletionResponse complete(CompletionRequest request);
    
    EmbeddingResponse embed(EmbeddingRequest request);
}
```

#### 娑堟伅绫诲瀷

```java
import ltd.idcu.est.llm.ChatMessage;

ChatMessage userMessage = ChatMessage.user("鐢ㄦ埛鐨勯棶棰?);
ChatMessage assistantMessage = ChatMessage.assistant("AI 鐨勫洖绛?);
ChatMessage systemMessage = ChatMessage.system("浣犳槸涓€涓?helpful 鐨勫姪鎵?);
```

### 2. est-llm 澶氭彁渚涘晢鏀寔

#### OpenAI

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.openai.OpenAiLlmClient;

LlmClient client = OpenAiLlmClient.builder()
    .apiKey("sk-your-api-key")
    .model("gpt-4")
    .baseUrl("https://api.openai.com/v1")
    .build();
```

#### 鏅鸿氨 AI (GLM)

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.zhipu.ZhipuLlmClient;

LlmClient client = ZhipuLlmClient.builder()
    .apiKey("your-api-key")
    .model("glm-4")
    .build();
```

#### 閫氫箟鍗冮棶 (Qwen)

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.qwen.QwenLlmClient;

LlmClient client = QwenLlmClient.builder()
    .apiKey("your-api-key")
    .model("qwen-turbo")
    .build();
```

#### 鏂囧績涓€瑷€ (Ernie)

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.ernie.ErnieLlmClient;

LlmClient client = ErnieLlmClient.builder()
    .apiKey("your-api-key")
    .secretKey("your-secret-key")
    .model("ernie-4.0")
    .build();
```

#### 璞嗗寘 (Doubao)

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.doubao.DoubaoLlmClient;

LlmClient client = DoubaoLlmClient.builder()
    .apiKey("your-api-key")
    .model("doubao-pro-4k")
    .build();
```

#### Kimi (Moonshot)

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.kimi.KimiLlmClient;

LlmClient client = KimiLlmClient.builder()
    .apiKey("your-api-key")
    .model("moonshot-v1-8k")
    .build();
```

#### Ollama (鏈湴妯″瀷)

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.ollama.OllamaLlmClient;

LlmClient client = OllamaLlmClient.builder()
    .baseUrl("http://localhost:11434")
    .model("llama2")
    .build();
```

### 3. est-ai-config AI 閰嶇疆绠＄悊

#### 閰嶇疆 AI 鎻愪緵鍟?
```java
import ltd.idcu.est.ai.config.AiConfig;
import ltd.idcu.est.ai.config.AiConfigs;
import ltd.idcu.est.ai.config.ProviderConfig;

AiConfig config = AiConfigs.load("ai-config.properties");

ProviderConfig openaiConfig = config.getProvider("openai");
String apiKey = openaiConfig.getApiKey();
String model = openaiConfig.getModel();
```

#### YAML 閰嶇疆绀轰緥

```yaml
ai:
  providers:
    openai:
      api-key: sk-your-api-key
      model: gpt-4
      base-url: https://api.openai.com/v1
      enabled: true
    
    zhipu:
      api-key: your-api-key
      model: glm-4
      enabled: true
    
    ollama:
      base-url: http://localhost:11434
      model: llama2
      enabled: true
```

### 4. est-ai-assistant AI 鍔╂墜

#### 鍒涘缓瀵硅瘽鍔╂墜

```java
import ltd.idcu.est.ai.assistant.AiAssistant;
import ltd.idcu.est.ai.assistant.AiAssistants;
import ltd.idcu.est.llm.LlmClient;

LlmClient llmClient = OpenAiLlmClient.builder()
    .apiKey("your-api-key")
    .model("gpt-4")
    .build();

AiAssistant assistant = AiAssistants.create(llmClient);

assistant.setSystemMessage("浣犳槸涓€涓笓涓氱殑缂栫▼鍔╂墜");

String response = assistant.chat("濡備綍鐢?Java 鍐欎竴涓?Hello World?");
System.out.println(response);
```

#### 澶氳疆瀵硅瘽

```java
import ltd.idcu.est.ai.assistant.AiAssistant;
import ltd.idcu.est.ai.assistant.Conversation;

AiAssistant assistant = AiAssistants.create(llmClient);
Conversation conversation = assistant.createConversation();

String response1 = conversation.send("浣犲ソ");
String response2 = conversation.send("鎴戝彨浠€涔堝悕瀛?"); // AI 浼氳浣忎笂涓嬫枃

System.out.println("鍥炲 1: " + response1);
System.out.println("鍥炲 2: " + response2);
```

#### 浠ｇ爜鐢熸垚

```java
import ltd.idcu.est.ai.codegen.CodeGenerator;
import ltd.idcu.est.ai.codegen.CodeGenerators;

CodeGenerator generator = CodeGenerators.create(llmClient);

String code = generator.generateJavaClass("User", 
    "鍖呭惈 id銆乶ame銆乪mail 瀛楁鐨勭敤鎴风被");

System.out.println("鐢熸垚鐨勪唬鐮?\n" + code);
```

---

## 杩涢樁绡?
### 1. 娴佸紡鍝嶅簲

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.ChatRequest;
import ltd.idcu.est.llm.ChatChunk;

LlmClient client = OpenAiLlmClient.builder()
    .apiKey("your-api-key")
    .model("gpt-4")
    .build();

ChatRequest request = ChatRequest.builder()
    .addMessage(ChatMessage.user("鍐欎竴涓叧浜庝汉宸ユ櫤鑳界殑鏁呬簨"))
    .stream(true)
    .build();

client.chatStream(request, chunk -> {
    if (chunk.getContent() != null) {
        System.out.print(chunk.getContent());
    }
});
```

### 2. 宓屽叆鍚戦噺

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.EmbeddingRequest;
import ltd.idcu.est.llm.EmbeddingResponse;

LlmClient client = OpenAiLlmClient.builder()
    .apiKey("your-api-key")
    .model("text-embedding-ada-002")
    .build();

EmbeddingRequest request = EmbeddingRequest.builder()
    .input("杩欐槸涓€娈甸渶瑕佸祵鍏ョ殑鏂囨湰")
    .build();

EmbeddingResponse response = client.embed(request);
float[] embedding = response.getEmbedding();
System.out.println("宓屽叆鍚戦噺闀垮害: " + embedding.length);
```

### 3. 宸ュ叿璋冪敤 (Function Calling)

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.ChatRequest;
import ltd.idcu.est.llm.Tool;
import ltd.idcu.est.llm.ToolCall;

LlmClient client = OpenAiLlmClient.builder()
    .apiKey("your-api-key")
    .model("gpt-4")
    .build();

Tool weatherTool = Tool.builder()
    .name("getWeather")
    .description("鑾峰彇鎸囧畾鍩庡競鐨勫ぉ姘?)
    .parameter("city", "string", "鍩庡競鍚嶇О", true)
    .build();

ChatRequest request = ChatRequest.builder()
    .addMessage(ChatMessage.user("鍖椾含浠婂ぉ澶╂皵鎬庝箞鏍?"))
    .addTool(weatherTool)
    .build();

ChatResponse response = client.chat(request);

if (response.hasToolCalls()) {
    ToolCall toolCall = response.getToolCalls().get(0);
    String city = toolCall.getArgument("city");
    String weatherResult = getWeather(city);
    
    ChatRequest followUp = ChatRequest.builder()
        .addMessages(response.getMessages())
        .addToolResult(toolCall.getId(), weatherResult)
        .build();
    
    ChatResponse finalResponse = client.chat(followUp);
    System.out.println(finalResponse.getContent());
}
```

---

## 鏈€浣冲疄璺?
### 1. API Key 瀹夊叏

```java
// 鉁?鎺ㄨ崘锛氫娇鐢ㄧ幆澧冨彉閲?String apiKey = System.getenv("OPENAI_API_KEY");

LlmClient client = OpenAiLlmClient.builder()
    .apiKey(apiKey)
    .build();

// 鉂?涓嶆帹鑽愶細纭紪鐮?API Key
LlmClient badClient = OpenAiLlmClient.builder()
    .apiKey("sk-this-is-a-secret") // 涓嶈鎻愪氦鍒颁唬鐮佷粨搴?    .build();
```

### 2. Token 璁¤垂

```java
import ltd.idcu.est.llm.ChatResponse;
import ltd.idcu.est.llm.Usage;

ChatResponse response = client.chat(request);
Usage usage = response.getUsage();

int promptTokens = usage.getPromptTokens();
int completionTokens = usage.getCompletionTokens();
int totalTokens = usage.getTotalTokens();

System.out.println("Prompt Tokens: " + promptTokens);
System.out.println("Completion Tokens: " + completionTokens);
System.out.println("Total Tokens: " + totalTokens);
```

### 3. 閿欒澶勭悊鍜岄噸璇?
```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.retry.RetryPolicy;

RetryPolicy retryPolicy = RetryPolicy.builder()
    .maxRetries(3)
    .backoff(1000)
    .retryOn(ApiException.class)
    .build();

LlmClient client = OpenAiLlmClient.builder()
    .apiKey("your-api-key")
    .retryPolicy(retryPolicy)
    .build();

try {
    ChatResponse response = client.chat(request);
} catch (ApiException e) {
    System.err.println("API 璋冪敤澶辫触: " + e.getMessage());
}
```

### 4. 鎻愮ず璇嶅伐绋?
```java
// 鉁?鎺ㄨ崘锛氭竻鏅扮殑绯荤粺鎻愮ず鍜岀敤鎴锋彁绀?ChatRequest request = ChatRequest.builder()
    .addMessage(ChatMessage.system(
        "浣犳槸涓€涓笓涓氱殑 Java 寮€鍙戜笓瀹躲€? +
        "璇风粰鍑烘竻鏅般€佸彲杩愯鐨勪唬鐮佺ず渚嬨€? +
        "浠ｇ爜涓簲璇ュ寘鍚繀瑕佺殑娉ㄩ噴銆?
    ))
    .addMessage(ChatMessage.user(
        "璇峰啓涓€涓?Java 绾跨▼姹犵殑浣跨敤绀轰緥"
    ))
    .build();

// 鉂?涓嶆帹鑽愶細妯＄硦鐨勬彁绀?ChatRequest badRequest = ChatRequest.builder()
    .addMessage(ChatMessage.user("鍐欎唬鐮?))
    .build();
```

---

## 妯″潡缁撴瀯

```
est-ai-suite/
鈹溾攢鈹€ est-ai-config/     # AI 閰嶇疆绠＄悊
鈹溾攢鈹€ est-llm-core/      # LLM 鏍稿績鎶借薄
鈹溾攢鈹€ est-llm/           # LLM 鎻愪緵鍟嗗疄鐜帮紙OpenAI銆乑hipu銆丵wen銆丒rnie銆丏oubao銆並imi銆丱llama锛?鈹斺攢鈹€ est-ai-assistant/  # AI 鍔╂墜鍜屼唬鐮佺敓鎴?```

---

## 鐩稿叧璧勬簮

- [est-ai-assistant README](./est-ai-assistant/README.md) - AI 鍔╂墜璇︾粏鏂囨。
- [est-llm README](./est-llm/README.md) - LLM 璇︾粏鏂囨。
- [绀轰緥浠ｇ爜](../../est-examples/est-examples-ai/) - AI 绀轰緥浠ｇ爜
- [EST Core](../../est-core/README.md) - 鏍稿績妯″潡

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
