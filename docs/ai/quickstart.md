# EST AI 蹇€熷叆闂?
## 5 鍒嗛挓寮€濮嬩娇鐢?EST AI

鏈寚鍗楀皢甯姪浣犲湪 5 鍒嗛挓鍐呭揩閫熶笂鎵?EST 妗嗘灦鐨?AI 鍔熻兘銆?
---

## 鍓嶇疆鏉′欢

- JDK 17+
- Maven 3.8+ 鎴?Gradle 7+
- 涓€涓?LLM 鎻愪緵鍟嗙殑 API Key锛堝彲閫夛紝寮€绠卞嵆鐢ㄦā寮忓彲鐢ㄤ簬娴嬭瘯锛?
---

## 绗竴姝ワ細娣诲姞渚濊禆

### Maven

鍦ㄤ綘鐨?`pom.xml` 涓坊鍔狅細

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-ai-assistant</artifactId>
    <version>2.1.0</version>
</dependency>
```

### Gradle

鍦ㄤ綘鐨?`build.gradle` 涓坊鍔狅細

```groovy
implementation 'ltd.idcu:est-ai-assistant:2.1.0'
```

---

## 绗簩姝ワ細浣犵殑绗竴涓?AI 瀵硅瘽

鍒涘缓涓€涓畝鍗曠殑 Java 绫伙細

```java
import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.impl.DefaultAiAssistant;

public class FirstAiApp {
    public static void main(String[] args) {
        AiAssistant assistant = new DefaultAiAssistant();
        
        String response = assistant.chat("浣犲ソ锛岃浠嬬粛涓€涓?EST 妗嗘灦锛?);
        System.out.println("AI 鍥炲锛?);
        System.out.println(response);
    }
}
```

杩愯杩欎釜绋嬪簭锛屼綘灏嗙湅鍒?AI 鐨勫洖澶嶏紒

---

## 绗笁姝ワ細閰嶇疆浣犵殑 LLM

铏界劧 EST 鎻愪緵浜嗗紑绠卞嵆鐢ㄧ殑娴嬭瘯妯″紡锛屼絾涓轰簡鑾峰緱鏈€浣充綋楠岋紝寤鸿閰嶇疆浣犺嚜宸辩殑 LLM銆?
### 鏂瑰紡 1锛氱紪绋嬮厤缃?
```java
import ltd.idcu.est.llm.api.LlmClient;
import ltd.idcu.est.llm.api.LlmConfig;
import ltd.idcu.est.llm.api.LlmClientFactory;

public class LlmConfigDemo {
    public static void main(String[] args) {
        LlmConfig config = new LlmConfig();
        config.setProvider("qwen"); // 鎴?openai, zhipu, ernie 绛?        config.setApiKey("your-api-key");
        config.setModel("qwen-turbo");
        
        LlmClient client = LlmClientFactory.create(config);
        AiAssistant assistant = new DefaultAiAssistant(client);
        
        String response = assistant.chat("浣犲ソ锛?);
        System.out.println(response);
    }
}
```

### 鏂瑰紡 2锛歋pring Boot 閰嶇疆

鍦?`application.yml` 涓細

```yaml
est:
  ai:
    enabled: true
    llm:
      provider: qwen
      api-key: ${QWEN_API_KEY}
      model: qwen-turbo
```

鐒跺悗鍦ㄤ綘鐨勪唬鐮佷腑娉ㄥ叆锛?
```java
@Service
public class MyService {
    
    @Autowired
    private AiAssistant aiAssistant;
    
    public String askAi(String question) {
        return aiAssistant.chat(question);
    }
}
```

---

## 绗洓姝ワ細浣跨敤浠ｇ爜鐢熸垚鍣?
EST 鐨勪唬鐮佺敓鎴愬櫒鍙互甯綘蹇€熺敓鎴愬畬鏁寸殑 CRUD 浠ｇ爜锛?
```java
import ltd.idcu.est.ai.api.CodeGenerator;
import ltd.idcu.est.ai.impl.DefaultCodeGenerator;

public class CodeGenDemo {
    public static void main(String[] args) {
        CodeGenerator generator = new DefaultCodeGenerator();
        
        // 鐢熸垚瀹炰綋绫?        String entity = generator.generateEntity(
            "Product",
            "com.example.entity",
            Map.of(
                "fields", List.of("id:Long", "name:String", "price:BigDecimal"),
                "useLombok", true
            )
        );
        
        System.out.println("鐢熸垚鐨?Entity锛歕n" + entity);
        
        // 鐢熸垚 Repository
        String repo = generator.generateRepository(
            "ProductRepository",
            "com.example.repository",
            Map.of()
        );
        
        // 鐢熸垚 Service
        String service = generator.generateService(
            "ProductService",
            "com.example.service",
            Map.of()
        );
        
        // 鐢熸垚 Controller
        String controller = generator.generateController(
            "ProductController",
            "com.example.controller",
            Map.of()
        );
        
        System.out.println("浠ｇ爜鐢熸垚瀹屾垚锛?);
    }
}
```

---

## 绗簲姝ワ細浼樺寲浣犵殑浠ｇ爜

浣跨敤 AI 鍔╂墜鏉ヤ紭鍖栧拰瀹℃煡浣犵殑浠ｇ爜锛?
```java
public class CodeOptimizeDemo {
    public static void main(String[] args) {
        AiAssistant assistant = new DefaultAiAssistant();
        
        String yourCode = """
            public List<User> getUsers() {
                List<User> users = new ArrayList<>();
                for (int i = 0; i < userDao.findAll().size(); i++) {
                    users.add(userDao.findAll().get(i));
                }
                return users;
            }
            """;
        
        // 浼樺寲浠ｇ爜
        String optimized = assistant.optimizeCode(yourCode);
        System.out.println("浼樺寲鍚庣殑浠ｇ爜锛歕n" + optimized);
        
        // 瑙ｉ噴浠ｇ爜
        String explanation = assistant.explainCode(optimized);
        System.out.println("\n浠ｇ爜瑙ｉ噴锛歕n" + explanation);
    }
}
```

---

## 绗叚姝ワ細浣跨敤鎻愮ず璇嶆ā鏉?
EST 鎻愪緵浜嗕笓涓氱殑鎻愮ず璇嶆ā鏉匡紝纭繚 AI 杈撳嚭璐ㄩ噺锛?
```java
import ltd.idcu.est.ai.api.PromptTemplateEngine;
import ltd.idcu.est.ai.impl.DefaultPromptTemplateEngine;

public class TemplateDemo {
    public static void main(String[] args) {
        PromptTemplateEngine engine = new DefaultPromptTemplateEngine();
        
        // 鏌ョ湅鎵€鏈夊彲鐢ㄦā鏉?        System.out.println("鍙敤妯℃澘锛? + engine.listTemplates());
        
        // 浣跨敤浠ｇ爜瀹℃煡妯℃澘
        String reviewPrompt = engine.render("code-review", Map.of(
            "code", yourCode,
            "language", "Java"
        ));
        
        System.out.println("鎻愮ず璇嶏細\n" + reviewPrompt);
    }
}
```

---

## 涓嬩竴姝?
鎭枩浣狅紒浣犲凡缁忓畬鎴愪簡 EST AI 鐨勫揩閫熷叆闂ㄣ€傜幇鍦ㄤ綘鍙互锛?
1. 闃呰 [AI 缂栫▼鎸囧崡](./ai-coder-guide.md) 娣卞叆浜嗚В EST AI 鐨勫己澶у姛鑳?2. 鏌ョ湅 [鏈€浣冲疄璺礭(./best-practices.md) 瀛︿範濡備綍楂樻晥浣跨敤 AI
3. 鎺㈢储 [鏋舵瀯璁捐](./architecture.md) 浜嗚В EST AI 鐨勫唴閮ㄥ伐浣滃師鐞?4. 鏌ラ槄 [API 鍙傝€僝(./api/) 浜嗚В璇︾粏鐨?API 鏂囨。

---

## 甯歌闂蹇€熻В绛?
**Q: 鎴戦渶瑕佷粯璐逛娇鐢ㄥ悧锛?*

A: EST 妗嗘灦鏈韩鏄紑婧愬厤璐圭殑銆備娇鐢?LLM 鏈嶅姟鍙兘闇€瑕佹敮浠樼浉搴旀彁渚涘晢鐨勮垂鐢紝浣嗕篃鏈夊厤璐归搴﹀彲鐢ㄣ€?
**Q: 鏀寔鍝簺 LLM 鎻愪緵鍟嗭紵**

A: 鏀寔 OpenAI銆佹櫤璋?AI銆侀€氫箟鍗冮棶銆佹枃蹇冧竴瑷€銆佽眴鍖呫€並imi銆丱llama 绛変富娴佹彁渚涘晢銆?
**Q: 鍙互鍦ㄤ紒涓氱敓浜х幆澧冧娇鐢ㄥ悧锛?*

A: 褰撶劧鍙互锛丒ST 妗嗘灦鏄负浼佷笟绾у簲鐢ㄨ璁＄殑锛屽叿鏈夊畬鍠勭殑瀹夊叏鏈哄埗鍜岄敊璇鐞嗐€?
**Q: 濡備綍鑾峰緱甯姪锛?*

A: 璇锋煡鐪?[FAQ](./faq.md) 鏂囨。锛屾垨鍦?GitHub 涓婃彁浜?Issue銆?
---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
