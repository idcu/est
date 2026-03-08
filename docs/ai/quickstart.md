# EST AI 快速入门

## 5 分钟开始使用 EST AI

本指南将帮助你在 5 分钟内快速上手 EST 框架的 AI 功能。

---

## 前置条件

- JDK 17+
- Maven 3.8+ 或 Gradle 7+
- 一个 LLM 提供商的 API Key（可选，开箱即用模式可用于测试）

---

## 第一步：添加依赖

### Maven

在你的 `pom.xml` 中添加：

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-ai-assistant</artifactId>
    <version>2.0.0</version>
</dependency>
```

### Gradle

在你的 `build.gradle` 中添加：

```groovy
implementation 'ltd.idcu:est-ai-assistant:2.0.0'
```

---

## 第二步：你的第一个 AI 对话

创建一个简单的 Java 类：

```java
import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.impl.DefaultAiAssistant;

public class FirstAiApp {
    public static void main(String[] args) {
        AiAssistant assistant = new DefaultAiAssistant();
        
        String response = assistant.chat("你好，请介绍一下 EST 框架！");
        System.out.println("AI 回复：");
        System.out.println(response);
    }
}
```

运行这个程序，你将看到 AI 的回复！

---

## 第三步：配置你的 LLM

虽然 EST 提供了开箱即用的测试模式，但为了获得最佳体验，建议配置你自己的 LLM。

### 方式 1：编程配置

```java
import ltd.idcu.est.llm.api.LlmClient;
import ltd.idcu.est.llm.api.LlmConfig;
import ltd.idcu.est.llm.api.LlmClientFactory;

public class LlmConfigDemo {
    public static void main(String[] args) {
        LlmConfig config = new LlmConfig();
        config.setProvider("qwen"); // 或 openai, zhipu, ernie 等
        config.setApiKey("your-api-key");
        config.setModel("qwen-turbo");
        
        LlmClient client = LlmClientFactory.create(config);
        AiAssistant assistant = new DefaultAiAssistant(client);
        
        String response = assistant.chat("你好！");
        System.out.println(response);
    }
}
```

### 方式 2：Spring Boot 配置

在 `application.yml` 中：

```yaml
est:
  ai:
    enabled: true
    llm:
      provider: qwen
      api-key: ${QWEN_API_KEY}
      model: qwen-turbo
```

然后在你的代码中注入：

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

## 第四步：使用代码生成器

EST 的代码生成器可以帮你快速生成完整的 CRUD 代码：

```java
import ltd.idcu.est.ai.api.CodeGenerator;
import ltd.idcu.est.ai.impl.DefaultCodeGenerator;

public class CodeGenDemo {
    public static void main(String[] args) {
        CodeGenerator generator = new DefaultCodeGenerator();
        
        // 生成实体类
        String entity = generator.generateEntity(
            "Product",
            "com.example.entity",
            Map.of(
                "fields", List.of("id:Long", "name:String", "price:BigDecimal"),
                "useLombok", true
            )
        );
        
        System.out.println("生成的 Entity：\n" + entity);
        
        // 生成 Repository
        String repo = generator.generateRepository(
            "ProductRepository",
            "com.example.repository",
            Map.of()
        );
        
        // 生成 Service
        String service = generator.generateService(
            "ProductService",
            "com.example.service",
            Map.of()
        );
        
        // 生成 Controller
        String controller = generator.generateController(
            "ProductController",
            "com.example.controller",
            Map.of()
        );
        
        System.out.println("代码生成完成！");
    }
}
```

---

## 第五步：优化你的代码

使用 AI 助手来优化和审查你的代码：

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
        
        // 优化代码
        String optimized = assistant.optimizeCode(yourCode);
        System.out.println("优化后的代码：\n" + optimized);
        
        // 解释代码
        String explanation = assistant.explainCode(optimized);
        System.out.println("\n代码解释：\n" + explanation);
    }
}
```

---

## 第六步：使用提示词模板

EST 提供了专业的提示词模板，确保 AI 输出质量：

```java
import ltd.idcu.est.ai.api.PromptTemplateEngine;
import ltd.idcu.est.ai.impl.DefaultPromptTemplateEngine;

public class TemplateDemo {
    public static void main(String[] args) {
        PromptTemplateEngine engine = new DefaultPromptTemplateEngine();
        
        // 查看所有可用模板
        System.out.println("可用模板：" + engine.listTemplates());
        
        // 使用代码审查模板
        String reviewPrompt = engine.render("code-review", Map.of(
            "code", yourCode,
            "language", "Java"
        ));
        
        System.out.println("提示词：\n" + reviewPrompt);
    }
}
```

---

## 下一步

恭喜你！你已经完成了 EST AI 的快速入门。现在你可以：

1. 阅读 [AI 编程指南](./ai-coder-guide.md) 深入了解 EST AI 的强大功能
2. 查看 [最佳实践](./best-practices.md) 学习如何高效使用 AI
3. 探索 [架构设计](./architecture.md) 了解 EST AI 的内部工作原理
4. 查阅 [API 参考](./api/) 了解详细的 API 文档

---

## 常见问题快速解答

**Q: 我需要付费使用吗？**

A: EST 框架本身是开源免费的。使用 LLM 服务可能需要支付相应提供商的费用，但也有免费额度可用。

**Q: 支持哪些 LLM 提供商？**

A: 支持 OpenAI、智谱 AI、通义千问、文心一言、豆包、Kimi、Ollama 等主流提供商。

**Q: 可以在企业生产环境使用吗？**

A: 当然可以！EST 框架是为企业级应用设计的，具有完善的安全机制和错误处理。

**Q: 如何获得帮助？**

A: 请查看 [FAQ](./faq.md) 文档，或在 GitHub 上提交 Issue。

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
