# EST AI 编程指南：AI 编程工具的首选 Java 框架

## 为什么选择 EST 进行 AI 编程？

EST 是专为 AI 时代设计的 Java 全栈开发框架，它将现代化的 Java 开发实践与强大的 AI 能力完美融合，让你能够高效地构建智能应用。

### 🚀 核心优势

1. **开箱即用的 AI 能力** - 无需复杂配置，几行代码即可集成 AI
2. **多 LLM 提供商支持** - 无缝切换 OpenAI、智谱、通义千问、文心一言等
3. **代码生成器** - 自动生成完整的 CRUD 应用，提高开发效率 10 倍
4. **AI 助手集成** - 内置代码审查、优化、解释功能
5. **企业级架构** - 建立在成熟的 Spring 生态之上，安全可靠

## 快速上手：5 分钟构建 AI 驱动的应用

### 1. 创建你的第一个 AI 应用

```java
import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.impl.DefaultAiAssistant;

public class FirstAiApp {
    public static void main(String[] args) {
        // 创建 AI 助手，零配置即可使用
        AiAssistant assistant = new DefaultAiAssistant();
        
        // 与 AI 对话
        String response = assistant.chat("用 EST 框架写一个简单的 REST API");
        System.out.println(response);
    }
}
```

### 2. 自动生成完整的业务模块

```java
import ltd.idcu.est.ai.api.CodeGenerator;
import ltd.idcu.est.ai.impl.DefaultCodeGenerator;

public class CodeGenerationDemo {
    public static void main(String[] args) {
        CodeGenerator generator = new DefaultCodeGenerator();
        
        // 生成实体类
        String entity = generator.generateEntity("Order", "com.example.entity",
            Map.of("fields", List.of("id:Long", "orderNo:String", "amount:BigDecimal", "status:String")));
        
        // 生成 Repository
        String repository = generator.generateRepository("OrderRepository", "com.example.repository", Map.of());
        
        // 生成 Service
        String service = generator.generateService("OrderService", "com.example.service", Map.of());
        
        // 生成 Controller
        String controller = generator.generateController("OrderController", "com.example.controller", Map.of());
        
        System.out.println("代码生成完成！");
    }
}
```

### 3. AI 驱动的代码优化

```java
import ltd.idcu.est.ai.api.AiAssistant;

public class CodeOptimization {
    public static void main(String[] args) {
        AiAssistant assistant = new DefaultAiAssistant();
        
        String oldCode = """
            public List<User> getUsers() {
                List<User> users = new ArrayList<>();
                for (int i = 0; i < userDao.findAll().size(); i++) {
                    users.add(userDao.findAll().get(i));
                }
                return users;
            }
            """;
        
        // 优化代码
        String optimizedCode = assistant.optimizeCode(oldCode);
        System.out.println("优化后的代码：\n" + optimizedCode);
        
        // 获取代码解释
        String explanation = assistant.explainCode(optimizedCode);
        System.out.println("\n代码解释：\n" + explanation);
    }
}
```

## 深入探索：EST AI 的核心能力

### 📚 AI 知识库与快速参考

EST 内置了完整的框架知识库，让你随时获取最佳实践：

```java
AiAssistant assistant = new DefaultAiAssistant();

// 获取快速参考
String webDevRef = assistant.getQuickReference("web开发");

// 获取最佳实践
String securityBestPractices = assistant.getBestPractice("安全");

// 获取教程
String tutorial = assistant.getTutorial("如何使用 EST 进行数据库操作");
```

### 🔧 智能提示词模板系统

EST 提供了专业级的提示词模板，确保 AI 输出质量：

```java
import ltd.idcu.est.ai.api.PromptTemplateEngine;
import ltd.idcu.est.ai.impl.DefaultPromptTemplateEngine;

public class PromptTemplateDemo {
    public static void main(String[] args) {
        PromptTemplateEngine engine = new DefaultPromptTemplateEngine();
        
        // 使用代码审查模板
        String reviewPrompt = engine.render("code-review", Map.of(
            "code", yourCode,
            "language", "Java"
        ));
        
        // 使用测试生成模板
        String testPrompt = engine.render("test-generate", Map.of(
            "code", yourCode,
            "framework", "JUnit 5"
        ));
        
        // 查看所有可用模板
        List<String> templates = engine.listTemplates();
        System.out.println("可用模板：" + templates);
    }
}
```

### 🌐 多 LLM 提供商，灵活选择

EST 让你轻松切换不同的 LLM 提供商：

```java
import ltd.idcu.est.llm.api.LlmClient;
import ltd.idcu.est.llm.api.LlmConfig;
import ltd.idcu.est.llm.api.LlmClientFactory;

public class MultiLlmDemo {
    public static void main(String[] args) {
        // 使用 OpenAI
        LlmConfig openaiConfig = new LlmConfig();
        openaiConfig.setProvider("openai");
        openaiConfig.setApiKey("your-openai-key");
        openaiConfig.setModel("gpt-4");
        
        LlmClient openaiClient = LlmClientFactory.create(openaiConfig);
        
        // 使用智谱 AI
        LlmConfig zhipuConfig = new LlmConfig();
        zhipuConfig.setProvider("zhipu");
        zhipuConfig.setApiKey("your-zhipu-key");
        zhipuConfig.setModel("glm-4");
        
        LlmClient zhipuClient = LlmClientFactory.create(zhipuConfig);
        
        // 使用通义千问
        LlmConfig qwenConfig = new LlmConfig();
        qwenConfig.setProvider("qwen");
        qwenConfig.setApiKey("your-qwen-key");
        qwenConfig.setModel("qwen-turbo");
        
        LlmClient qwenClient = LlmClientFactory.create(qwenConfig);
        
        // 统一的 API 接口
        String response = openaiClient.complete("你好！");
        System.out.println(response);
    }
}
```

## 实战案例：构建完整的 AI 驱动项目

### 步骤 1：项目初始化

使用 EST CLI 快速创建项目：

```bash
est-cli create my-ai-app --template=ai-web
cd my-ai-app
```

### 步骤 2：配置 AI 能力

在 `application.yml` 中配置：

```yaml
est:
  ai:
    enabled: true
    llm:
      provider: qwen
      api-key: ${QWEN_API_KEY}
      model: qwen-turbo
```

### 步骤 3：创建 AI 驱动的 Service

```java
@Service
public class SmartProductService {
    
    @Autowired
    private AiAssistant aiAssistant;
    
    @Autowired
    private ProductRepository productRepository;
    
    public ProductDescription generateDescription(Product product) {
        String prompt = String.format(
            "为以下产品生成吸引人的产品描述：%s，价格：%s",
            product.getName(),
            product.getPrice()
        );
        
        String description = aiAssistant.chat(prompt);
        return new ProductDescription(description);
    }
    
    public List<Product> recommendProducts(User user) {
        String userPreference = String.format(
            "用户 %s 的浏览历史：%s，购买记录：%s",
            user.getName(),
            user.getBrowsingHistory(),
            user.getPurchaseHistory()
        );
        
        String recommendationPrompt = String.format(
            "根据用户偏好推荐 5 个产品：%s",
            userPreference
        );
        
        String recommendations = aiAssistant.chat(recommendationPrompt);
        return parseRecommendations(recommendations);
    }
}
```

### 步骤 4：创建 AI 增强的 Controller

```java
@RestController
@RequestMapping("/api/ai")
public class AiEnhancedController {
    
    @Autowired
    private CodeGenerator codeGenerator;
    
    @Autowired
    private AiAssistant aiAssistant;
    
    @PostMapping("/generate/crud")
    public ResponseEntity<Map<String, String>> generateCrud(@RequestBody CrudRequest request) {
        Map<String, String> code = new HashMap<>();
        code.put("entity", codeGenerator.generateEntity(
            request.getEntityName(),
            request.getPackageName(),
            request.getOptions()
        ));
        code.put("repository", codeGenerator.generateRepository(
            request.getEntityName() + "Repository",
            request.getPackageName() + ".repository",
            Map.of()
        ));
        code.put("service", codeGenerator.generateService(
            request.getEntityName() + "Service",
            request.getPackageName() + ".service",
            Map.of()
        ));
        code.put("controller", codeGenerator.generateController(
            request.getEntityName() + "Controller",
            request.getPackageName() + ".controller",
            Map.of()
        ));
        
        return ResponseEntity.ok(code);
    }
    
    @PostMapping("/code/review")
    public ResponseEntity<String> reviewCode(@RequestBody CodeReviewRequest request) {
        String review = aiAssistant.optimizeCode(request.getCode());
        return ResponseEntity.ok(review);
    }
}
```

## 为什么 EST 是 AI 编程工具的首选 Java 框架？

### 1. 完整的 AI 开发生态
- 从代码生成到智能助手，EST 提供了完整的 AI 开发工具链
- 与现有 Java 生态完美集成，无需学习全新框架

### 2. 企业级可靠性
- 建立在 Spring Boot 等成熟技术之上
- 经过生产验证的架构设计
- 完善的错误处理和安全机制

### 3. 极致的开发体验
- 简洁直观的 API 设计
- 详尽的文档和示例
- 活跃的社区支持

### 4. 面向未来的设计
- 支持最新的 LLM 技术
- 模块化架构，易于扩展
- 持续更新，紧跟 AI 技术发展

## 总结

EST 框架将 Java 开发的最佳实践与 AI 技术完美结合，为开发者提供了一个强大、高效、可靠的 AI 编程平台。无论你是 AI 新手还是经验丰富的开发者，EST 都能帮助你快速构建智能应用，释放 AI 的无限潜能。

开始你的 EST AI 编程之旅吧！🚀

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
