# EST AI 鏂囨。

娆㈣繋鏉ュ埌 EST AI 鏂囨。涓撳尯锛佽繖閲屾彁渚涘叧浜?EST 妗嗘灦 AI 鍔熻兘鐨勫畬鏁存枃妗ｃ€?

## 鐩綍

- [AI 姒傝堪](#ai-姒傝堪)
- [蹇€熷紑濮媇(#蹇€熷紑濮?
- [鏍稿績鍔熻兘](#鏍稿績鍔熻兘)
- [LLM 鎻愪緵鍟哴(#llm-鎻愪緵鍟?
- [绀轰緥浠ｇ爜](#绀轰緥浠ｇ爜)
- [API 鍙傝€僝(#api-鍙傝€?

---

## AI 姒傝堪

EST AI Suite 鏄?EST 妗嗘灦鐨?AI 鍜?LLM锛堝ぇ璇█妯″瀷锛夌浉鍏虫ā鍧楅泦鍚堬紝鎻愪緵浜嗗紑绠卞嵆鐢ㄧ殑 AI 鍔熻兘鏀寔銆?

### 涓昏鐗规€?

- 馃幆 **绠€鍗曟槗鐢?* - 鍑犺浠ｇ爜灏辫兘鐢ㄤ笂 AI
- 馃攲 **澶氭彁渚涘晢鏀寔** - 鏀寔 OpenAI銆佹櫤璋便€侀€氫箟鍗冮棶銆佹枃蹇冧竴瑷€绛変富娴?LLM
- 馃З **妯″潡鍖栬璁?* - 鎸夐渶寮曞叆锛岀伒娲荤粍鍚?
- 馃洜锔?**楂樼骇鍔熻兘** - 浠ｇ爜鐢熸垚銆佹彁绀鸿瘝妯℃澘銆丄I 鍔╂墜绛?
- 馃敡 **鍙墿灞?* - 杞绘澗娣诲姞鑷畾涔?LLM 鎻愪緵鍟?

---

## 蹇€熷紑濮?

### 1. 娣诲姞渚濊禆

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-ai-assistant</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 2. 鍒涘缓 AI 鍔╂墜

```java
import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.impl.DefaultAiAssistant;

public class AiQuickStart {
    public static void main(String[] args) {
        AiAssistant assistant = new DefaultAiAssistant();
        
        String result = assistant.chat("浣犲ソ锛岃浠嬬粛涓€涓?EST 妗嗘灦");
        System.out.println(result);
    }
}
```

### 3. 閰嶇疆 LLM

```java
import ltd.idcu.est.llm.api.LlmClient;
import ltd.idcu.est.llm.api.LlmConfig;
import ltd.idcu.est.llm.impl.openai.OpenAiLlmClient;

LlmConfig config = new LlmConfig();
config.setApiKey("your-api-key");
config.setModel("gpt-4");

LlmClient client = new OpenAiLlmClient(config);
String response = client.complete("Hello, world!");
```

---

## 鏍稿績鍔熻兘

### 1. AI 鍔╂墜

鎻愪緵蹇€熷弬鑰冦€佹渶浣冲疄璺点€佹暀绋嬭幏鍙栥€佷唬鐮佸缓璁€佷唬鐮佽В閲娿€佷唬鐮佷紭鍖栫瓑鍔熻兘銆?

```java
AiAssistant assistant = new DefaultAiAssistant();

// 鑾峰彇蹇€熷弬鑰?
String ref = assistant.getQuickReference("web寮€鍙?);

// 鑾峰彇鏈€浣冲疄璺?
String bestPractice = assistant.getBestPractice("浠ｇ爜椋庢牸");

// 鑾峰彇鏁欑▼
String tutorial = assistant.getTutorial("绗竴涓簲鐢?);

// 寤鸿浠ｇ爜
String code = assistant.suggestCode("鍒涘缓涓€涓?UserService");

// 瑙ｉ噴浠ｇ爜
String explanation = assistant.explainCode(code);

// 浼樺寲浠ｇ爜
String optimized = assistant.optimizeCode(code);
```

### 2. 浠ｇ爜鐢熸垚鍣?

鑷姩鐢熸垚 Entity銆丼ervice銆丆ontroller銆丷epository銆乸om.xml 绛変唬鐮併€?

```java
import ltd.idcu.est.ai.api.CodeGenerator;
import ltd.idcu.est.ai.impl.DefaultCodeGenerator;

CodeGenerator generator = new DefaultCodeGenerator();

// 鐢熸垚 Entity
String entity = generator.generateEntity("Product", "com.example.entity",
    Map.of("fields", List.of("id:Long", "name:String", "price:BigDecimal")));

// 鐢熸垚 Repository
String repo = generator.generateRepository("ProductRepository", "com.example.repository", Map.of());

// 鐢熸垚 Service
String service = generator.generateService("ProductService", "com.example.service", Map.of());

// 鐢熸垚 pom.xml
String pom = generator.generatePomXml("MyProject", "com.example", "my-app", "1.0.0");
```

### 3. 鎻愮ず璇嶆ā鏉?

浣跨敤棰勫畾涔夌殑鎻愮ず璇嶆ā鏉匡紝鎻愬崌 AI 杈撳嚭璐ㄩ噺銆?

```java
import ltd.idcu.est.ai.api.PromptTemplate;
import ltd.idcu.est.ai.api.PromptTemplateEngine;
import ltd.idcu.est.ai.impl.DefaultPromptTemplateEngine;

PromptTemplateEngine engine = new DefaultPromptTemplateEngine();

// 浣跨敤鍐呯疆妯℃澘
String prompt = engine.render("code-review", Map.of(
    "code", codeToReview,
    "language", "Java"
));

// 鑷畾涔夋ā鏉?
PromptTemplate template = new PromptTemplate();
template.setName("my-template");
template.setContent("浣犳槸涓€涓?${role}锛岃澶勭悊浠ヤ笅鍐呭锛?{content}");
engine.registerTemplate(template);
```

---

## LLM 鎻愪緵鍟?

EST AI Suite 鏀寔浠ヤ笅 LLM 鎻愪緵鍟嗭細

| 鎻愪緵鍟?| 妯″瀷 | 鐘舵€?|
|--------|------|------|
| OpenAI | GPT-4, GPT-3.5 | 鉁?宸叉敮鎸?|
| 鏅鸿氨 AI | GLM-4, GLM-3 | 鉁?宸叉敮鎸?|
| 閫氫箟鍗冮棶 | Qwen-Turbo, Qwen-Plus | 鉁?宸叉敮鎸?|
| 鏂囧績涓€瑷€ | Ernie-4.0, Ernie-3.5 | 鉁?宸叉敮鎸?|
| 璞嗗寘 | Doubao-Pro | 鉁?宸叉敮鎸?|
| Kimi | Moonshot-v1 | 鉁?宸叉敮鎸?|
| Ollama | 鏈湴妯″瀷 | 鉁?宸叉敮鎸?|

### 閫氱敤浣跨敤鏂瑰紡

```java
import ltd.idcu.est.llm.api.LlmClient;
import ltd.idcu.est.llm.api.LlmConfig;

// 閰嶇疆
LlmConfig config = new LlmConfig();
config.setProvider("openai"); // 鎴?zhipu, qwen, ernie, doubao, kimi, ollama
config.setApiKey("your-api-key");
config.setModel("gpt-4");
config.setBaseUrl("https://api.openai.com/v1");

// 鍒涘缓瀹㈡埛绔?
LlmClient client = LlmClientFactory.create(config);

// 鍙戦€佽姹?
String response = client.complete("浣犲ソ锛?);
```

---

## 绀轰緥浠ｇ爜

鏌ョ湅浠ヤ笅绀轰緥浜嗚В鏇村鐢ㄦ硶锛?

- [AI 蹇€熷紑濮嬬ず渚媇(../../est-examples/est-examples-ai/src/main/java/ltd/idcu/est/examples/ai/AiQuickStartExample.java)
- [缁煎悎 AI 绀轰緥](../../est-examples/est-examples-ai/src/main/java/ltd/idcu/est/examples/ai/ComprehensiveAiExample.java)
- [AI 鍔╂墜 Web 搴旂敤](../../est-examples/est-examples-ai/src/main/java/ltd/idcu/est/examples/ai/AiAssistantWebExample.java)
- [浠ｇ爜鐢熸垚绀轰緥](../../est-examples/est-examples-ai/src/main/java/ltd/idcu/est/examples/ai/CodeGeneratorExample.java)

---

## API 鍙傝€?

璇︾粏鐨?API 鏂囨。璇峰弬鑰冿細

- [AI 鍔╂墜 API](./api/ai-assistant.md)
- [浠ｇ爜鐢熸垚鍣?API](./api/code-generator.md)
- [LLM 瀹㈡埛绔?API](./api/llm-client.md)
- [鎻愮ず璇嶆ā鏉?API](./api/prompt-template.md)

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
