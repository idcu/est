# EST AI 缂栫▼鏈€浣冲疄璺?
## 鐩綍
- [浠ｇ爜鐢熸垚鏈€浣冲疄璺礭(#浠ｇ爜鐢熸垚鏈€浣冲疄璺?
- [AI 鍔╂墜浣跨敤鏈€浣冲疄璺礭(#ai-鍔╂墜浣跨敤鏈€浣冲疄璺?
- [鎻愮ず璇嶅伐绋嬫渶浣冲疄璺礭(#鎻愮ず璇嶅伐绋嬫渶浣冲疄璺?
- [鎬ц兘浼樺寲鏈€浣冲疄璺礭(#鎬ц兘浼樺寲鏈€浣冲疄璺?
- [瀹夊叏鏈€浣冲疄璺礭(#瀹夊叏鏈€浣冲疄璺?
- [鏋舵瀯璁捐鏈€浣冲疄璺礭(#鏋舵瀯璁捐鏈€浣冲疄璺?

---

## 浠ｇ爜鐢熸垚鏈€浣冲疄璺?
### 1. 缁撴瀯鍖栭渶姹傛弿杩?
鍦ㄤ娇鐢ㄤ唬鐮佺敓鎴愬櫒鏃讹紝鎻愪緵娓呮櫚銆佺粨鏋勫寲鐨勯渶姹傛弿杩板彲浠ユ樉钁楁彁楂樹唬鐮佽川閲忥細

```java
// 濂界殑瀹炶返
String entityCode = codeGenerator.generateEntity("Order", "com.example.entity",
    Map.of(
        "fields", List.of(
            "id:Long:primaryKey:autoIncrement",
            "orderNo:String:unique:length=50",
            "customerId:Long:foreignKey=Customer.id",
            "amount:BigDecimal:scale=2",
            "status:String:enum=PENDING,PAID,SHIPPED,DELIVERED",
            "createdAt:LocalDateTime:autoTimestamp",
            "updatedAt:LocalDateTime:autoUpdateTimestamp"
        ),
        "tableName", "t_order",
        "useLombok", true,
        "addJpaAnnotations", true
    ));
```

### 2. 杩唬寮忎唬鐮佺敓鎴?
閲囩敤灏忔杩唬鐨勬柟寮忕敓鎴愪唬鐮侊紝鑰屼笉鏄竴娆℃€х敓鎴愭墍鏈夊唴瀹癸細

```java
// 绗竴姝ワ細鐢熸垚鍩虹瀹炰綋
String entity = codeGenerator.generateEntity("Product", "com.example.entity", options);

// 绗簩姝ワ細鐢熸垚 Repository
String repository = codeGenerator.generateRepository("ProductRepository", "com.example.repository",
    Map.of("entityClass", "com.example.entity.Product"));

// 绗笁姝ワ細鐢熸垚鍖呭惈涓氬姟 Service
String service = codeGenerator.generateService("ProductService", "com.example.service",
    Map.of("repository", "ProductRepository"));

// 绗洓姝ワ細鐢熸垚 Controller
String controller = codeGenerator.generateController("ProductController", "com.example.controller",
    Map.of("service", "ProductService"));
```

### 3. 浠ｇ爜瀹℃煡涓庝紭鍖?
鐢熸垚浠ｇ爜鍚庯紝鍔″繀浣跨敤 AI 鍔╂墜杩涜浠ｇ爜瀹℃煡锛?
```java
// 鐢熸垚鐨勪唬鐮?String generatedCode = "...";

// 浠ｇ爜瀹℃煡
String review = aiAssistant.explainCode(generatedCode);

// 浠ｇ爜浼樺寲
String optimized = aiAssistant.optimizeCode(generatedCode);

// 鏈€缁堜娇鐢ㄤ紭鍖栧悗鐨勪唬鐮?```

---

## AI 鍔╂墜浣跨敤鏈€浣冲疄璺?
### 1. 鎻愪緵鍏呭垎涓婁笅鏂?
鍦ㄤ娇鐢?AI 鍔╂墜鏃讹紝鎻愪緵鍏呭垎鐨勪笂涓嬫枃淇℃伅鍙互鑾峰緱鏇村噯纭殑缁撴灉锛?
```java
Map<String, Object> context = Map.of(
    "projectType", "e-commerce",
    "frameworkVersion", "2.1.0",
    "database", "MySQL 8.0",
    "existingCode", existingCodeSnippet,
    "requirements", requirements
);

String response = aiAssistant.chat("璇峰府鎴戝疄鐜扮敤鎴疯璇佹ā鍧?, context);
```

### 2. 鏄庣‘鐨勪换鍔″垎瑙?
灏嗗鏉備换鍔″垎瑙ｄ负澶氫釜灏忎换鍔★細

```java
// 绗竴姝ワ細鑾峰彇璁捐鎬濊矾
String design = aiAssistant.chat("璁捐涓€涓數鍟嗚鍗曠郴缁熺殑鏋舵瀯");

// 绗簩姝ワ細鐢熸垚瀹炰綋璁捐
String entities = aiAssistant.chat("鏍规嵁浠ヤ笂璁捐锛岀敓鎴愭牳蹇冨疄浣撶被");

// 绗笁姝ワ細鐢熸垚涓氬姟閫昏緫
String service = aiAssistant.chat("鏍规嵁瀹炰綋锛岀敓鎴?Service 灞?);

// 绗洓姝ワ細鐢熸垚 API 鎺ュ彛
String controller = aiAssistant.chat("鏍规嵁 Service锛岀敓鎴?Controller 灞?);
```

### 3. 娓愯繘寮忓畬鍠?
浣跨敤瀵硅瘽鍘嗗彶杩涜娓愯繘寮忓畬鍠勶細

```java
// 绗竴杞?String v1 = aiAssistant.chat("鍐欎竴涓敤鎴风櫥褰曞姛鑳?);

// 绗簩杞紝鍩轰簬绗竴杞粨鏋?String feedback = "杩欐槸绗竴杞殑瀹炵幇锛岃娣诲姞 JWT 璁よ瘉鏀寔锛歕n" + v1;
String v2 = aiAssistant.chat(feedback);

// 绗笁杞?String feedback2 = "寰堝ソ锛佽娣诲姞闄愭祦鍜屽畨鍏ㄦ鏌ワ細\n" + v2;
String v3 = aiAssistant.chat(feedback2);
```

---

## 鎻愮ず璇嶅伐绋嬫渶浣冲疄璺?
### 1. 浣跨敤鎻愮ず璇嶆ā鏉?
鍒╃敤 EST 鍐呯疆鐨勬彁绀鸿瘝妯℃澘锛岃€屼笉鏄瘡娆￠兘閲嶆柊缂栧啓锛?
```java
PromptTemplateEngine engine = new DefaultPromptTemplateEngine();

// 浣跨敤浠ｇ爜瀹℃煡妯℃澘
String reviewPrompt = engine.render("code-review", Map.of(
    "code", yourCode,
    "language", "Java",
    "focus", "performance"
));

LlmClient client = LlmClientFactory.create(config);
String reviewResult = client.complete(reviewPrompt);
```

### 2. 鍒涘缓鑷畾涔夋ā鏉?
涓哄父瑙佷换鍔″垱寤鸿嚜瀹氫箟鎻愮ず璇嶆ā鏉匡細

```java
PromptTemplate template = new PromptTemplate();
template.setName("rest-api-generator");
template.setCategory("code");
template.setDescription("REST API 鐢熸垚妯℃澘");
template.setContent("""
    浣犳槸涓€浣嶄笓涓氱殑 Java 鍚庣寮€鍙戜笓瀹躲€?    
    璇锋牴鎹互涓嬮渶姹傦紝鐢熸垚涓€涓畬鏁寸殑 REST API锛?    
    闇€姹傛弿杩帮細${requirement}
    
    鎶€鏈爤锛?    - 妗嗘灦锛欵ST Framework 2.0
    - 鏁版嵁搴擄細${database}
    - 璁よ瘉鏂瑰紡锛?{authType}
    
    璇风敓鎴愶細
    1. Entity 绫?    2. Repository 鎺ュ彛
    3. Service 绫?    4. Controller 绫?    
    瑕佹眰锛?    - 浠ｇ爜瑙勮寖
    - 鍖呭惈寮傚父澶勭悊
    - 鍖呭惈鍗曞厓娴嬭瘯
    - 鍖呭惈 Swagger 娉ㄨВ
    """);

engine.registerTemplate(template);
```

### 3. 缁撴瀯鍖栬緭鍑烘牸寮?
瑕佹眰 AI 杩斿洖缁撴瀯鍖栫殑杈撳嚭鏍煎紡锛?
```java
String prompt = """
    璇峰垎鏋愪互涓嬩唬鐮侊紝骞舵寜浠ヤ笅鏍煎紡杩斿洖缁撴灉锛?    
    銆愰棶棰樺垎鏋愩€?    ...
    
    銆愪紭鍖栧缓璁€?    ...
    
    銆愪紭鍖栧悗鐨勪唬鐮併€?    ...
    
    銆愭€ц兘褰卞搷銆?    ...
    
    浠ｇ爜锛?    ${code}
    """;

String structuredOutput = aiAssistant.chat(prompt);
```

---

## 鎬ц兘浼樺寲鏈€浣冲疄璺?
### 1. LLM 璇锋眰浼樺寲

#### 缂撳瓨甯哥敤鏌ヨ

```java
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class CachedAiAssistant {
    private final AiAssistant aiAssistant;
    private final Cache<String, String> cache;
    
    public CachedAiAssistant(AiAssistant aiAssistant) {
        this.aiAssistant = aiAssistant;
        this.cache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();
    }
    
    public String chat(String message) {
        return cache.get(message, aiAssistant::chat);
    }
}
```

#### 鎵归噺澶勭悊

```java
// 涓嶅ソ鐨勫疄璺?for (String code : codeList) {
    String optimized = aiAssistant.optimizeCode(code);
    results.add(optimized);
}

// 濂界殑瀹炶返
String combinedCode = String.join("\n---\n", codeList);
String prompt = """
    璇蜂紭鍖栦互涓嬩唬鐮佺墖娈碉紝姣忎釜鐗囨鐢?--- 鍒嗛殧锛?    ${combinedCode}
    
    璇锋寜椤哄簭杩斿洖浼樺寲鍚庣殑浠ｇ爜锛屽悓鏍风敤 --- 鍒嗛殧銆?    """;

String result = aiAssistant.chat(prompt);
List<String> optimizedResults = Arrays.asList(result.split("---"));
```

### 2. 妯″瀷閫夋嫨绛栫暐

鏍规嵁浠诲姟澶嶆潅搴﹂€夋嫨鍚堥€傜殑妯″瀷锛?
```java
public class SmartLlmRouter {
    public LlmClient getClientForTask(String taskType) {
        LlmConfig config = new LlmConfig();
        
        switch (taskType) {
            case "simple-chat":
                // 绠€鍗曞璇濓紝浣跨敤蹇ā鍨?                config.setModel("qwen-turbo");
                break;
            case "code-generation":
                // 浠ｇ爜鐢熸垚锛屼娇鐢ㄥ己妯″瀷
                config.setModel("gpt-4");
                break;
            case "code-review":
                // 浠ｇ爜瀹℃煡锛屽钩琛℃ā鍨?                config.setModel("glm-4");
                break;
            default:
                config.setModel("qwen-plus");
        }
        
        return LlmClientFactory.create(config);
    }
}
```

### 3. 璇锋眰瓒呮椂涓庨噸璇?
```java
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

@Service
public class ReliableAiService {
    
    @Retryable(
        retryFor = {TimeoutException.class, IOException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public String chatWithRetry(String message) {
        return aiAssistant.chat(message);
    }
}
```

---

## 瀹夊叏鏈€浣冲疄璺?
### 1. API 瀵嗛挜绠＄悊

```yaml
# application.yml
est:
  ai:
    llm:
      api-key: ${LLM_API_KEY}  # 浠庣幆澧冨彉閲忚鍙?```

```java
// 鐢熶骇鐜浣跨敤瀵嗛挜绠＄悊鏈嶅姟
@Configuration
public class LlmSecurityConfig {
    
    @Value("${vault.llm.api-key-path}")
    private String apiKeyPath;
    
    @Bean
    public LlmConfig llmConfig(VaultTemplate vaultTemplate) {
        LlmConfig config = new LlmConfig();
        String apiKey = vaultTemplate.read(apiKeyPath, Map.class).getData().get("apiKey");
        config.setApiKey(apiKey);
        return config;
    }
}
```

### 2. 杈撳叆楠岃瘉涓庤繃婊?
```java
public class SafeAiAssistant {
    private final AiAssistant delegate;
    private final Set<String> sensitivePatterns;
    
    public SafeAiAssistant(AiAssistant delegate) {
        this.delegate = delegate;
        this.sensitivePatterns = Set.of(
            "password", "secret", "api[-_]?key", "token",
            "private[-_]?key"
        );
    }
    
    public String chat(String message) {
        if (containsSensitiveInfo(message)) {
            throw new SecurityException("杈撳叆鍖呭惈鏁忔劅淇℃伅");
        }
        return delegate.chat(message);
    }
    
    private boolean containsSensitiveInfo(String message) {
        return sensitivePatterns.stream()
            .anyMatch(pattern -> 
                Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)
                    .matcher(message)
                    .find());
    }
}
```

### 3. 杈撳嚭 sanitization

```java
public class SanitizedAiAssistant {
    private final AiAssistant delegate;
    
    public String chat(String message) {
        String response = delegate.chat(message);
        return sanitizeOutput(response);
    }
    
    private String sanitizeOutput(String output) {
        // 绉婚櫎娼滃湪鐨勬伓鎰忎唬鐮?        output = output.replaceAll("(?s)```bash.*?```", "");
        
        // 绉婚櫎鏁忔劅淇℃伅妯″紡
        output = output.replaceAll("sk-[a-zA-Z0-9]{32,}", "[REDACTED]");
        
        return output;
    }
}
```

### 4. 璇锋眰瀹¤

```java
@Aspect
@Component
public class AiRequestAuditAspect {
    
    @Autowired
    private AuditLogService auditLogService;
    
    @Around("execution(* ltd.idcu.est.ai.api.AiAssistant.*(..))")
    public Object auditAiRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            
            auditLogService.logSuccess(
                methodName,
                Arrays.toString(args),
                duration
            );
            
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            auditLogService.logFailure(
                methodName,
                Arrays.toString(args),
                duration,
                e.getMessage()
            );
            throw e;
        }
    }
}
```

---

## 鏋舵瀯璁捐鏈€浣冲疄璺?
### 1. 鍒嗗眰鏋舵瀯

```
鈹屸攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹?鈹?        Controller 灞?             鈹?鈹? (REST API, WebSocket)          鈹?鈹溾攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹?鈹?        Service 灞?                鈹?鈹? (涓氬姟閫昏緫, AI 闆嗘垚)          鈹?鈹溾攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹?鈹?        AI Service 灞?               鈹?鈹? (AiAssistant, CodeGenerator)  鈹?鈹溾攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹?鈹?        LLM Client 灞?             鈹?鈹? (LlmClient, LlmConfig)        鈹?鈹溾攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹?鈹?        Repository 灞?              鈹?鈹? (鏁版嵁璁块棶)                       鈹?鈹斺攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹?```

### 2. 寮傛澶勭悊

```java
@Service
public class AsyncAiService {
    
    @Autowired
    private AiAssistant aiAssistant;
    
    @Async
    public CompletableFuture<String> generateCodeAsync(String requirement) {
        return CompletableFuture.completedFuture(
            aiAssistant.suggestCode(requirement)
        );
    }
    
    @Async
    public CompletableFuture<String> optimizeCodeAsync(String code) {
        return CompletableFuture.completedFuture(
            aiAssistant.optimizeCode(code)
        );
    }
}

// 浣跨敤
@RestController
public class AiController {
    
    @Autowired
    private AsyncAiService asyncAiService;
    
    @PostMapping("/generate")
    public CompletableFuture<ResponseEntity<String>> generateCode(
            @RequestBody String requirement) {
        return asyncAiService.generateCodeAsync(requirement)
            .thenApply(ResponseEntity::ok);
    }
}
```

### 3. 浜嬩欢椹卞姩鏋舵瀯

```java
// 浜嬩欢瀹氫箟
public class CodeGenerationEvent {
    private final String requirement;
    private final String result;
    // ...
}

// 浜嬩欢鐩戝惉鍣?@Component
public class CodeGenerationListener {
    
    @EventListener
    public void handleCodeGeneration(CodeGenerationEvent event) {
        // 璁板綍鐢熸垚鍘嗗彶
        // 鍙戦€侀€氱煡
        // 鏇存柊缁熻
    }
}

// 鍙戝竷浜嬩欢
@Service
public class EventDrivenAiService {
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    public String generateCode(String requirement) {
        String result = aiAssistant.suggestCode(requirement);
        eventPublisher.publishEvent(new CodeGenerationEvent(requirement, result));
        return result;
    }
}
```

---

## 鎬荤粨

閬靛惊杩欎簺鏈€浣冲疄璺碉紝鍙互甯姪浣狅細
- 鎻愰珮 AI 鐢熸垚浠ｇ爜鐨勮川閲?- 浼樺寲鎬ц兘鍜岃祫婧愪娇鐢?- 纭繚绯荤粺瀹夊叏鎬?- 鏋勫缓鍙淮鎶ゃ€佸彲鎵╁睍鐨勬灦鏋?
EST AI 妯″潡鐨勮璁＄悊蹇靛氨鏄杩欎簺鏈€浣冲疄璺电殑浣撶幇锛屽厖鍒嗗埄鐢ㄥ畠鍙互璁╀綘鐨?AI 缂栫▼涔嬫梾鏇村姞椤虹晠锛?
---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
