# EST AI 编程最佳实践

## 目录
- [代码生成最佳实践](#代码生成最佳实践)
- [AI 助手使用最佳实践](#ai-助手使用最佳实践)
- [提示词工程最佳实践](#提示词工程最佳实践)
- [性能优化最佳实践](#性能优化最佳实践)
- [安全最佳实践](#安全最佳实践)
- [架构设计最佳实践](#架构设计最佳实践)

---

## 代码生成最佳实践

### 1. 结构化需求描述

在使用代码生成器时，提供清晰、结构化的需求描述可以显著提高代码质量：

```java
// 好的实践
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

### 2. 迭代式代码生成

采用小步迭代的方式生成代码，而不是一次性生成所有内容：

```java
// 第一步：生成基础实体
String entity = codeGenerator.generateEntity("Product", "com.example.entity", options);

// 第二步：生成 Repository
String repository = codeGenerator.generateRepository("ProductRepository", "com.example.repository",
    Map.of("entityClass", "com.example.entity.Product"));

// 第三步：生成包含业务 Service
String service = codeGenerator.generateService("ProductService", "com.example.service",
    Map.of("repository", "ProductRepository"));

// 第四步：生成 Controller
String controller = codeGenerator.generateController("ProductController", "com.example.controller",
    Map.of("service", "ProductService"));
```

### 3. 代码审查与优化

生成代码后，务必使用 AI 助手进行代码审查：

```java
// 生成的代码
String generatedCode = "...";

// 代码审查
String review = aiAssistant.explainCode(generatedCode);

// 代码优化
String optimized = aiAssistant.optimizeCode(generatedCode);

// 最终使用优化后的代码
```

---

## AI 助手使用最佳实践

### 1. 提供充分上下文

在使用 AI 助手时，提供充分的上下文信息可以获得更准确的结果：

```java
Map<String, Object> context = Map.of(
    "projectType", "e-commerce",
    "frameworkVersion", "2.0.0",
    "database", "MySQL 8.0",
    "existingCode", existingCodeSnippet,
    "requirements", requirements
);

String response = aiAssistant.chat("请帮我实现用户认证模块", context);
```

### 2. 明确的任务分解

将复杂任务分解为多个小任务：

```java
// 第一步：获取设计思路
String design = aiAssistant.chat("设计一个电商订单系统的架构");

// 第二步：生成实体设计
String entities = aiAssistant.chat("根据以上设计，生成核心实体类");

// 第三步：生成业务逻辑
String service = aiAssistant.chat("根据实体，生成 Service 层");

// 第四步：生成 API 接口
String controller = aiAssistant.chat("根据 Service，生成 Controller 层");
```

### 3. 渐进式完善

使用对话历史进行渐进式完善：

```java
// 第一轮
String v1 = aiAssistant.chat("写一个用户登录功能");

// 第二轮，基于第一轮结果
String feedback = "这是第一轮的实现，请添加 JWT 认证支持：\n" + v1;
String v2 = aiAssistant.chat(feedback);

// 第三轮
String feedback2 = "很好！请添加限流和安全检查：\n" + v2;
String v3 = aiAssistant.chat(feedback2);
```

---

## 提示词工程最佳实践

### 1. 使用提示词模板

利用 EST 内置的提示词模板，而不是每次都重新编写：

```java
PromptTemplateEngine engine = new DefaultPromptTemplateEngine();

// 使用代码审查模板
String reviewPrompt = engine.render("code-review", Map.of(
    "code", yourCode,
    "language", "Java",
    "focus", "performance"
));

LlmClient client = LlmClientFactory.create(config);
String reviewResult = client.complete(reviewPrompt);
```

### 2. 创建自定义模板

为常见任务创建自定义提示词模板：

```java
PromptTemplate template = new PromptTemplate();
template.setName("rest-api-generator");
template.setCategory("code");
template.setDescription("REST API 生成模板");
template.setContent("""
    你是一位专业的 Java 后端开发专家。
    
    请根据以下需求，生成一个完整的 REST API：
    
    需求描述：${requirement}
    
    技术栈：
    - 框架：EST Framework 2.0
    - 数据库：${database}
    - 认证方式：${authType}
    
    请生成：
    1. Entity 类
    2. Repository 接口
    3. Service 类
    4. Controller 类
    
    要求：
    - 代码规范
    - 包含异常处理
    - 包含单元测试
    - 包含 Swagger 注解
    """);

engine.registerTemplate(template);
```

### 3. 结构化输出格式

要求 AI 返回结构化的输出格式：

```java
String prompt = """
    请分析以下代码，并按以下格式返回结果：
    
    【问题分析】
    ...
    
    【优化建议】
    ...
    
    【优化后的代码】
    ...
    
    【性能影响】
    ...
    
    代码：
    ${code}
    """;

String structuredOutput = aiAssistant.chat(prompt);
```

---

## 性能优化最佳实践

### 1. LLM 请求优化

#### 缓存常用查询

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

#### 批量处理

```java
// 不好的实践
for (String code : codeList) {
    String optimized = aiAssistant.optimizeCode(code);
    results.add(optimized);
}

// 好的实践
String combinedCode = String.join("\n---\n", codeList);
String prompt = """
    请优化以下代码片段，每个片段用 --- 分隔：
    ${combinedCode}
    
    请按顺序返回优化后的代码，同样用 --- 分隔。
    """;

String result = aiAssistant.chat(prompt);
List<String> optimizedResults = Arrays.asList(result.split("---"));
```

### 2. 模型选择策略

根据任务复杂度选择合适的模型：

```java
public class SmartLlmRouter {
    public LlmClient getClientForTask(String taskType) {
        LlmConfig config = new LlmConfig();
        
        switch (taskType) {
            case "simple-chat":
                // 简单对话，使用快模型
                config.setModel("qwen-turbo");
                break;
            case "code-generation":
                // 代码生成，使用强模型
                config.setModel("gpt-4");
                break;
            case "code-review":
                // 代码审查，平衡模型
                config.setModel("glm-4");
                break;
            default:
                config.setModel("qwen-plus");
        }
        
        return LlmClientFactory.create(config);
    }
}
```

### 3. 请求超时与重试

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

## 安全最佳实践

### 1. API 密钥管理

```yaml
# application.yml
est:
  ai:
    llm:
      api-key: ${LLM_API_KEY}  # 从环境变量读取
```

```java
// 生产环境使用密钥管理服务
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

### 2. 输入验证与过滤

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
            throw new SecurityException("输入包含敏感信息");
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

### 3. 输出 sanitization

```java
public class SanitizedAiAssistant {
    private final AiAssistant delegate;
    
    public String chat(String message) {
        String response = delegate.chat(message);
        return sanitizeOutput(response);
    }
    
    private String sanitizeOutput(String output) {
        // 移除潜在的恶意代码
        output = output.replaceAll("(?s)```bash.*?```", "");
        
        // 移除敏感信息模式
        output = output.replaceAll("sk-[a-zA-Z0-9]{32,}", "[REDACTED]");
        
        return output;
    }
}
```

### 4. 请求审计

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

## 架构设计最佳实践

### 1. 分层架构

```
┌─────────────────────────────────────────┐
│         Controller 层              │
│  (REST API, WebSocket)          │
├─────────────────────────────────────────┤
│         Service 层                 │
│  (业务逻辑, AI 集成)          │
├─────────────────────────────────────────┤
│         AI Service 层                │
│  (AiAssistant, CodeGenerator)  │
├─────────────────────────────────────────┤
│         LLM Client 层              │
│  (LlmClient, LlmConfig)        │
├─────────────────────────────────────────┤
│         Repository 层               │
│  (数据访问)                       │
└─────────────────────────────────────────┘
```

### 2. 异步处理

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

// 使用
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

### 3. 事件驱动架构

```java
// 事件定义
public class CodeGenerationEvent {
    private final String requirement;
    private final String result;
    // ...
}

// 事件监听器
@Component
public class CodeGenerationListener {
    
    @EventListener
    public void handleCodeGeneration(CodeGenerationEvent event) {
        // 记录生成历史
        // 发送通知
        // 更新统计
    }
}

// 发布事件
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

## 总结

遵循这些最佳实践，可以帮助你：
- 提高 AI 生成代码的质量
- 优化性能和资源使用
- 确保系统安全性
- 构建可维护、可扩展的架构

EST AI 模块的设计理念就是让这些最佳实践的体现，充分利用它可以让你的 AI 编程之旅更加顺畅！

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
