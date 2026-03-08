package ltd.idcu.est.examples.ai;

import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.api.CodeGenerator;
import ltd.idcu.est.ai.api.PromptTemplate;
import ltd.idcu.est.ai.api.skill.Skill;
import ltd.idcu.est.ai.api.skill.SkillResult;
import ltd.idcu.est.ai.impl.DefaultAiAssistant;
import ltd.idcu.est.ai.impl.DefaultPromptTemplate;

import java.util.List;
import java.util.Map;

public class ComprehensiveAiExample {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("EST AI 综合示例");
        System.out.println("=".repeat(60));
        System.out.println();
        System.out.println("本示例展�?EST AI 的所有核心功能：");
        System.out.println("  1. AI 助手基础功能");
        System.out.println("  2. 代码生成�?);
        System.out.println("  3. 提示词模�?);
        System.out.println("  4. Skill 系统");
        System.out.println("  5. 知识查询");
        System.out.println("  6. 代码操作");
        System.out.println();

        System.out.println("=".repeat(60));
        System.out.println("第一部分：AI 助手初始�?);
        System.out.println("=".repeat(60));

        AiAssistant aiAssistant = new DefaultAiAssistant();
        System.out.println("�?AI 助手已创�?);
        System.out.println();

        System.out.println("=".repeat(60));
        System.out.println("第二部分：知识查�?);
        System.out.println("=".repeat(60));

        knowledgeQueryExample(aiAssistant);

        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("第三部分：代码操�?);
        System.out.println("=".repeat(60));

        codeOperationExample(aiAssistant);

        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("第四部分：代码生成器");
        System.out.println("=".repeat(60));

        codeGeneratorExample(aiAssistant);

        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("第五部分：提示词模板");
        System.out.println("=".repeat(60));

        promptTemplateExample(aiAssistant);

        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("第六部分：Skill 系统");
        System.out.println("=".repeat(60));

        skillSystemExample(aiAssistant);

        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("综合示例运行完成�?);
        System.out.println("=".repeat(60));
        System.out.println();
        System.out.println("更多示例�?);
        System.out.println("  - StorageExample: 存储系统");
        System.out.println("  - ConfigExample: 配置管理");
        System.out.println("  - LlmIntegrationExample: LLM 集成");
        System.out.println("  - MidTermFeaturesExample: 中期功能");
        System.out.println("  - LongTermFeaturesExample: 长期功能");
        System.out.println("  - AiAssistantWebExample: Web 助手");
    }

    public static void run() {
        main(new String[]{});
    }

    private static void knowledgeQueryExample(AiAssistant aiAssistant) {
        System.out.println("\n--- 知识查询示例 ---");
        System.out.println("AI 助手提供丰富的知识查询功能\n");

        System.out.println("1. 获取快速参�?(Quick Reference)");
        System.out.println("   主题：web 开�?);
        String webRef = aiAssistant.getQuickReference("web");
        System.out.println("   " + webRef.substring(0, Math.min(150, webRef.length())) + "...");
        System.out.println();

        System.out.println("2. 获取最佳实�?(Best Practice)");
        System.out.println("   类别：代码风�?);
        String bestPractice = aiAssistant.getBestPractice("code-style");
        System.out.println("   " + bestPractice.substring(0, Math.min(150, bestPractice.length())) + "...");
        System.out.println();

        System.out.println("3. 获取教程 (Tutorial)");
        System.out.println("   主题：第一个应�?);
        String tutorial = aiAssistant.getTutorial("first-app");
        System.out.println("   " + tutorial.substring(0, Math.min(150, tutorial.length())) + "...");
        System.out.println();

        System.out.println("可用的快速参考主题：");
        System.out.println("  - web, cache, data, event, logging, security");
        System.out.println("  - config, monitor, scheduler, messaging, workflow");
        System.out.println();

        System.out.println("可用的最佳实践类别：");
        System.out.println("  - code-style, error-handling, performance");
        System.out.println("  - security, testing, architecture");
        System.out.println();

        System.out.println("�?知识查询示例完成\n");
    }

    private static void codeOperationExample(AiAssistant aiAssistant) {
        System.out.println("\n--- 代码操作示例 ---");
        System.out.println("AI 助手提供强大的代码操作功能\n");

        System.out.println("1. 代码建议 (Suggest Code)");
        System.out.println("   需求：创建一个用户管理的 Service");
        String suggestion = aiAssistant.suggestCode("创建一个用户管理的 Service，包�?CRUD 操作");
        System.out.println("   建议的代码：\n" + suggestion);
        System.out.println();

        System.out.println("2. 代码解释 (Explain Code)");
        String codeToExplain = """
                public class UserService {
                    private UserRepository repository;
                    
                    public User getUserById(Long id) {
                        return repository.findById(id);
                    }
                }
                """;
        System.out.println("   待解释的代码：\n" + codeToExplain);
        String explanation = aiAssistant.explainCode(codeToExplain);
        System.out.println("   解释：\n" + explanation);
        System.out.println();

        System.out.println("3. 代码优化 (Optimize Code)");
        String codeToOptimize = """
                public List<User> getUsers() {
                    List<User> users = new ArrayList<>();
                    for (int i = 0; i < 100; i++) {
                        users.add(repository.findById((long) i));
                    }
                    return users;
                }
                """;
        System.out.println("   待优化的代码：\n" + codeToOptimize);
        String optimized = aiAssistant.optimizeCode(codeToOptimize);
        System.out.println("   优化后的代码：\n" + optimized);
        System.out.println();

        System.out.println("�?代码操作示例完成\n");
    }

    private static void codeGeneratorExample(AiAssistant aiAssistant) {
        System.out.println("\n--- 代码生成器示�?---");
        System.out.println("使用 CodeGenerator 生成各种代码\n");

        CodeGenerator generator = aiAssistant.getCodeGenerator();

        System.out.println("1. 生成 Entity �?);
        String entityCode = generator.generateEntity(
                "Product",
                "com.example.entity",
                Map.of("fields", List.of("id:Long", "name:String", "price:BigDecimal", "createdAt:LocalDateTime"))
        );
        System.out.println(entityCode);
        System.out.println();

        System.out.println("2. 生成 Repository 接口");
        String repoCode = generator.generateRepository(
                "ProductRepository",
                "com.example.repository",
                Map.of("entityName", "Product")
        );
        System.out.println(repoCode);
        System.out.println();

        System.out.println("3. 生成 Service �?);
        String serviceCode = generator.generateService(
                "ProductService",
                "com.example.service",
                Map.of("entityName", "Product")
        );
        System.out.println(serviceCode);
        System.out.println();

        System.out.println("4. 生成 Controller �?);
        String controllerCode = generator.generateController(
                "ProductController",
                "com.example.controller",
                Map.of("entityName", "Product")
        );
        System.out.println(controllerCode);
        System.out.println();

        System.out.println("5. 生成 POM.xml");
        String pomXml = generator.generatePomXml(
                "ProductService",
                "com.example",
                "product-service",
                "1.0.0"
        );
        System.out.println(pomXml);
        System.out.println();

        System.out.println("6. 生成完整 Web 应用");
        String webAppCode = generator.generateWebApp(
                "ProductApp",
                "com.example",
                Map.of("entities", List.of("Product", "Order", "Customer"))
        );
        System.out.println(webAppCode.substring(0, Math.min(300, webAppCode.length())) + "...");
        System.out.println();

        System.out.println("�?代码生成器示例完成\n");
    }

    private static void promptTemplateExample(AiAssistant aiAssistant) {
        System.out.println("\n--- 提示词模板示�?---");
        System.out.println("使用提示词模板生成标准化的提示词\n");

        System.out.println("1. 查看可用的模板分�?);
        List<String> categories = aiAssistant.getTemplateRegistry().getCategories();
        System.out.println("   分类列表�?);
        for (String category : categories) {
            System.out.println("   - " + category);
        }
        System.out.println();

        System.out.println("2. 查看某个分类的模�?);
        String targetCategory = categories.isEmpty() ? "general" : categories.get(0);
        List<PromptTemplate> templates = aiAssistant.getTemplateRegistry().getTemplatesByCategory(targetCategory);
        System.out.println("   分类 \"" + targetCategory + "\" 的模板：");
        for (PromptTemplate template : templates) {
            System.out.println("   - " + template.getName() + ": " + template.getDescription());
        }
        System.out.println();

        System.out.println("3. 创建自定义模�?);
        PromptTemplate customTemplate = new DefaultPromptTemplate(
                "custom-code-review",
                "code-review",
                "自定义代码审查模�?,
                """
                请作为资�?Java 代码审查专家，审查以下代码：
                
                代码�?                ${code}
                
                请按以下格式输出�?                1. 代码质量评分 (0-100)
                2. 主要问题列表
                3. 改进建议
                4. 优化后的代码
                """,
                List.of("code")
        );
        aiAssistant.getTemplateRegistry().register(customTemplate);
        System.out.println("   自定义模板已注册�? + customTemplate.getName());
        System.out.println();

        System.out.println("4. 使用模板生成提示�?);
        String sampleCode = """
                public class BadExample {
                    public void doSomething() {
                        System.out.println("Hello");
                    }
                }
                """;
        String prompt = aiAssistant.generatePrompt("custom-code-review", Map.of("code", sampleCode));
        System.out.println("   生成的提示词：\n" + prompt);
        System.out.println();

        System.out.println("�?提示词模板示例完成\n");
    }

    private static void skillSystemExample(AiAssistant aiAssistant) {
        System.out.println("\n--- Skill 系统示例 ---");
        System.out.println("使用 Skill 系统执行可组合的 AI 能力\n");

        System.out.println("1. 查看可用�?Skills");
        List<Skill> skills = aiAssistant.getSkillRegistry().listAll();
        System.out.println("   可用�?Skills�?);
        for (Skill skill : skills) {
            System.out.println("   - " + skill.getName() + " (" + skill.getCategory() + ")");
            System.out.println("     " + skill.getDescription());
        }
        System.out.println();

        if (!skills.isEmpty()) {
            System.out.println("2. 执行 Skill");
            Skill firstSkill = skills.get(0);
            System.out.println("   执行 Skill�? + firstSkill.getName());

            Map<String, Object> inputs = Map.of(
                    "className", "Order",
                    "packageName", "com.example.entity",
                    "fields", List.of("id:Long", "orderNo:String", "totalAmount:BigDecimal", "status:String")
            );

            SkillResult result = aiAssistant.getSkillRegistry().execute(firstSkill.getId(), inputs);

            if (result.isSuccess()) {
                System.out.println("   �?Skill 执行成功�?);
                System.out.println("   输出�?);
                for (Map.Entry<String, Object> entry : result.getOutputs().entrySet()) {
                    System.out.println("   - " + entry.getKey() + ": " + 
                            (entry.getValue().toString().length() > 100 ? 
                                    entry.getValue().toString().substring(0, 100) + "..." : 
                                    entry.getValue()));
                }
            } else {
                System.out.println("   �?Skill 执行失败�? + result.getErrorMessage());
            }
        }
        System.out.println();

        System.out.println("Skill 系统特点�?);
        System.out.println("  - 可组合的 AI 能力单元");
        System.out.println("  - 标准化的输入输出");
        System.out.println("  - 可扩展的注册机制");
        System.out.println("  - 支持自定�?Skill");
        System.out.println();

        System.out.println("�?Skill 系统示例完成\n");
    }
}
