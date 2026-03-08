# EST AI Assistant

AI 鍔╂墜鍜屼唬鐮佺敓鎴愬姛鑳芥ā鍧楋紝鎻愪緵鏅鸿兘寮€鍙戣緟鍔╄兘鍔涖€?

## 鍔熻兘鐗规€?

- 鏅鸿兘瀵硅瘽鍔╂墜
- 浠ｇ爜鐢熸垚涓庤ˉ鍏?
- 鏋舵瀯璁捐涓庤瘎瀹?
- 閲嶆瀯寤鸿涓庝紭鍖?
- 闇€姹傝В鏋愪笌椤圭洰鑴氭墜鏋?
- 鎻愮ず妯℃澘绯荤粺
- 鍑芥暟璋冪敤鏀寔

## 妯″潡缁撴瀯

```
est-ai-assistant/
鈹溾攢鈹€ est-ai-api/    # AI 鍔╂墜 API
鈹斺攢鈹€ est-ai-impl/   # AI 鍔╂墜瀹炵幇
```

## 馃摎 鐩綍

- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鍩虹绡嘳(#鍩虹绡?
- [杩涢樁绡嘳(#杩涢樁绡?
- [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 馃殌 蹇€熷叆闂?

### 浠€涔堟槸 AI 鍔╂墜锛?

**EST AI Assistant**鏄竴涓櫤鑳藉紑鍙戝姪鎵嬶紝瀹冩彁渚涳細
- 浠ｇ爜鐢熸垚锛圕ode Generator锛?
- 鎻愮ず妯℃澘锛圥rompt Template锛?
- 椤圭洰鑴氭墜鏋讹紙Project Scaffold锛?
- AI 瀵硅瘽鍔熻兘
- 鏋舵瀯璁捐涓庤瘎瀹?

璁?AI 甯姪浣犳洿楂樻晥鍦板紑鍙戯紒

### 绗竴涓緥瀛?

璁╂垜浠敤 3 鍒嗛挓鍐欎竴涓畝鍗曠殑 AI 绋嬪簭锛?

棣栧厛锛屽湪浣犵殑 `pom.xml` 涓坊鍔犱緷璧栵細

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-ai-api</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-ai-impl</artifactId>
    <version>2.1.0</version>
</dependency>
```

鐒跺悗鍒涘缓涓€涓畝鍗曠殑 Java 绫伙細

```java
import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.impl.Ai;
import ltd.idcu.est.ai.impl.DefaultAiAssistant;

public class AiFirstExample {
    public static void main(String[] args) {
        System.out.println("=== AI 鍔╂墜绀轰緥 ===\n");
        
        // 鍒涘缓 AI 鍔╂墜
        AiAssistant assistant = new DefaultAiAssistant();
        
        // 浣跨敤 AI 鐢熸垚浠ｇ爜
        String code = assistant.generateCode("鍐欎竴涓?Java 绫伙紝鍖呭惈 main 鏂规硶锛屾墦鍗?Hello World");
        System.out.println("鐢熸垚鐨勪唬鐮?\n" + code);
        
        System.out.println("\n鉁?AI 绀轰緥瀹屾垚锛?);
    }
}
```

杩愯杩欎釜绋嬪簭锛屼綋楠?AI 鐨勫己澶э紒

馃帀 鎭枩浣狅紒浣犲凡缁忓浼氫簡浣跨敤 AI 鍔╂墜锛?

---

## 馃摉 鍩虹绡?

### 1. 鏍稿績姒傚康

| 姒傚康 | 璇存槑 | 鐢熸椿绫绘瘮 |
|------|------|----------|
| **AI 鍔╂墜** | 鏅鸿兘瀵硅瘽鍜屼唬鐮佺敓鎴?| 鑱槑鐨勫姪鎵?|
| **浠ｇ爜鐢熸垚鍣?* | 鏍规嵁闇€姹傜敓鎴愪唬鐮?| 浠ｇ爜鎵撳瓧鍛?|
| **鎻愮ず妯℃澘** | 棰勫畾涔夌殑鎻愮ず鏍煎紡 | 濉┖琛ㄦ牸 |
| **椤圭洰鑴氭墜鏋?* | 蹇€熺敓鎴愰」鐩粨鏋?| 寤虹瓚钃濆浘 |

### 2. 浠ｇ爜鐢熸垚

```java
import ltd.idcu.est.ai.api.CodeGenerator;
import ltd.idcu.est.ai.impl.DefaultCodeGenerator;

public class CodeGeneratorExample {
    public static void main(String[] args) {
        CodeGenerator generator = new DefaultCodeGenerator();
        
        // 鐢熸垚瀹炰綋绫?
        String entityCode = generator.generateEntity("User", 
                List.of("id:Long", "name:String", "email:String"));
        System.out.println("鐢熸垚鐨勫疄浣撶被:\n" + entityCode);
        
        // 鐢熸垚 Repository 鎺ュ彛
        String repoCode = generator.generateRepository("User", "UserRepository");
        System.out.println("\n鐢熸垚鐨?Repository:\n" + repoCode);
        
        // 鐢熸垚 Service 绫?
        String serviceCode = generator.generateService("User", "UserService");
        System.out.println("\n鐢熸垚鐨?Service:\n" + serviceCode);
    }
}
```

### 3. 鎻愮ず妯℃澘

```java
import ltd.idcu.est.ai.api.PromptTemplate;
import ltd.idcu.est.ai.api.PromptTemplateRegistry;
import ltd.idcu.est.ai.impl.DefaultPromptTemplate;
import ltd.idcu.est.ai.impl.DefaultPromptTemplateRegistry;

import java.util.HashMap;
import java.util.Map;

public class PromptTemplateExample {
    public static void main(String[] args) {
        PromptTemplateRegistry registry = new DefaultPromptTemplateRegistry();
        
        // 鍒涘缓鎻愮ず妯℃澘
        PromptTemplate template = new DefaultPromptTemplate(
                "generate-controller",
                "璇蜂负 {entityName} 鐢熸垚涓€涓?Spring Boot Controller锛屽寘鍚?CRUD 鎿嶄綔"
        );
        registry.register(template);
        
        // 浣跨敤妯℃澘
        Map<String, String> variables = new HashMap<>();
        variables.put("entityName", "User");
        
        String prompt = registry.apply("generate-controller", variables);
        System.out.println("鐢熸垚鐨勬彁绀?\n" + prompt);
    }
}
```

---

## 馃敡 杩涢樁绡?

### 1. 椤圭洰鑴氭墜鏋?

```java
import ltd.idcu.est.ai.api.ProjectScaffold;
import ltd.idcu.est.ai.impl.DefaultProjectScaffold;

import java.nio.file.Paths;
import java.util.List;

public class ProjectScaffoldExample {
    public static void main(String[] args) {
        ProjectScaffold scaffold = new DefaultProjectScaffold();
        
        // 鐢熸垚椤圭洰缁撴瀯
        scaffold.generate(
                Paths.get("./my-project"),
                "com.example",
                "my-project",
                List.of("User", "Product", "Order")
        );
        
        System.out.println("椤圭洰鑴氭墜鏋剁敓鎴愬畬鎴愶紒");
    }
}
```

### 2. 涓?EST Collection 闆嗘垚

```java
import ltd.idcu.est.collection.api.Seqs;
import ltd.idcu.est.ai.api.PromptTemplate;
import ltd.idcu.est.ai.impl.DefaultPromptTemplate;

import java.util.List;
import java.util.Map;

public class AiCollectionIntegrationExample {
    public static void main(String[] args) {
        List<String> entities = List.of("User", "Product", "Order");
        
        // 浣跨敤 Collection 鎵归噺鐢熸垚浠ｇ爜鎻愮ず
        Seqs.of(entities)
                .map(entity -> {
                    PromptTemplate template = new DefaultPromptTemplate(
                            "entity-" + entity,
                            "涓?" + entity + " 鐢熸垚瀹屾暣鐨勪唬鐮?
                    );
                    return template.apply(Map.of());
                })
                .forEach(prompt -> System.out.println("鎻愮ず: " + prompt));
    }
}
```

---

## 馃挕 鏈€浣冲疄璺?

### 1. 濂界殑鎻愮ず鎶€宸?

```java
import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.impl.DefaultAiAssistant;

public class GoodPromptExample {
    public static void main(String[] args) {
        AiAssistant assistant = new DefaultAiAssistant();
        
        // 鉂?涓嶅ソ鐨勬彁绀?
        String badPrompt = "鍐欎釜浠ｇ爜";
        
        // 鉁?濂界殑鎻愮ず锛氭槑纭€佸叿浣撱€佹湁涓婁笅鏂?
        String goodPrompt = """
                璇峰府鎴戝啓涓€涓?Java 绫伙紝瑕佹眰锛?
                1. 绫诲悕锛歎serService
                2. 鍖呭惈浠ヤ笅鏂规硶锛?
                   - getUserById(Long id)
                   - createUser(User user)
                   - updateUser(User user)
                   - deleteUser(Long id)
                3. 浣跨敤 Spring Framework 鐨?@Service 娉ㄨВ
                4. 娣诲姞蹇呰鐨勬敞閲?
                """;
        
        String code = assistant.generateCode(goodPrompt);
        System.out.println("鐢熸垚鐨勪唬鐮?\n" + code);
    }
}
```

## 鏍稿績 API

### AiAssistant

AI 鍔╂墜涓绘帴鍙ｃ€?

```java
public interface AiAssistant {
    String chat(String message);
    String generateCode(String requirement);
    String completeCode(String code, CompletionOptions options);
    ArchitectureDesign designArchitecture(String requirement);
    RefactorSuggestion suggestRefactor(String code, RefactorOptions options);
}
```

### 鏋舵瀯璁捐

```java
ArchitectureDesigner designer = new DefaultArchitectureDesigner();
ArchitectureDesign design = designer.design("鐢靛晢骞冲彴绯荤粺");

System.out.println("鏋舵瀯妯″紡: " + design.getPattern());
System.out.println("妯″潡鍒掑垎: " + design.getModules());
```

### 閲嶆瀯寤鸿

```java
RefactorAssistant assistant = new DefaultRefactorAssistant();
RefactorSuggestion suggestion = assistant.suggest("闇€瑕佷紭鍖栫殑浠ｇ爜");

System.out.println("闂: " + suggestion.getIssues());
System.out.println("寤鸿: " + suggestion.getSuggestions());
```

## 鐩稿叧妯″潡

- [est-ai-config](../est-ai-config/): AI 閰嶇疆绠＄悊
- [est-llm-core](../est-llm-core/): LLM 鏍稿績鎶借薄
- [est-llm](../est-llm/): LLM 鎻愪緵鍟嗗疄鐜?

---

## 馃幆 鎬荤粨

EST AI 灏卞儚浣犵殑"鏅鸿兘鍔╂墜"锛屽府鍔╀綘鏇撮珮鏁堝湴寮€鍙戜唬鐮侊紒

鑷虫锛屾垜浠凡缁忓畬鎴愪簡 EST 妗嗘灦鎵€鏈夋牳蹇冩ā鍧楃殑瀛︿範锛侌煄?

绁濅綘鍦ㄤ娇鐢?EST 妗嗘灦鐨勫紑鍙戣繃绋嬩腑涓€鍒囬『鍒╋紒
