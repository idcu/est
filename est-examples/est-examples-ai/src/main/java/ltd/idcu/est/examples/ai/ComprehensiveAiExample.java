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
        System.out.println("EST AI Comprehensive Example");
        System.out.println("=".repeat(60));
        System.out.println();
        System.out.println("This example demonstrates all core features of EST AI:");
        System.out.println("  1. AI Assistant basic functionality");
        System.out.println("  2. Code generation");
        System.out.println("  3. Prompt templates");
        System.out.println("  4. Skill system");
        System.out.println("  5. Knowledge query");
        System.out.println("  6. Code operations");
        System.out.println();

        System.out.println("=".repeat(60));
        System.out.println("Part 1: AI Assistant Initialization");
        System.out.println("=".repeat(60));

        AiAssistant aiAssistant = new DefaultAiAssistant();
        System.out.println("[X] AI Assistant created");
        System.out.println();

        System.out.println("=".repeat(60));
        System.out.println("Part 2: Knowledge Query");
        System.out.println("=".repeat(60));

        knowledgeQueryExample(aiAssistant);

        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("Part 3: Code Operations");
        System.out.println("=".repeat(60));

        codeOperationExample(aiAssistant);

        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("Part 4: Code Generator");
        System.out.println("=".repeat(60));

        codeGeneratorExample(aiAssistant);

        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("Part 5: Prompt Templates");
        System.out.println("=".repeat(60));

        promptTemplateExample(aiAssistant);

        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("Part 6: Skill System");
        System.out.println("=".repeat(60));

        skillSystemExample(aiAssistant);

        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("Comprehensive example run complete");
        System.out.println("=".repeat(60));
        System.out.println();
        System.out.println("More examples:");
        System.out.println("  - StorageExample: Storage system");
        System.out.println("  - ConfigExample: Configuration management");
        System.out.println("  - LlmIntegrationExample: LLM integration");
        System.out.println("  - MidTermFeaturesExample: Mid-term features");
        System.out.println("  - LongTermFeaturesExample: Long-term features");
        System.out.println("  - AiAssistantWebExample: Web assistant");
    }

    public static void run() {
        main(new String[]{});
    }

    private static void knowledgeQueryExample(AiAssistant aiAssistant) {
        System.out.println("\n--- Knowledge Query Example ---");
        System.out.println("AI Assistant provides rich knowledge query functionality\n");

        System.out.println("1. Get Quick Reference");
        System.out.println("   Topic: web development");
        String webRef = aiAssistant.getQuickReference("web");
        System.out.println("   " + webRef.substring(0, Math.min(150, webRef.length())) + "...");
        System.out.println();

        System.out.println("2. Get Best Practice");
        System.out.println("   Category: code style");
        String bestPractice = aiAssistant.getBestPractice("code-style");
        System.out.println("   " + bestPractice.substring(0, Math.min(150, bestPractice.length())) + "...");
        System.out.println();

        System.out.println("3. Get Tutorial");
        System.out.println("   Topic: first application");
        String tutorial = aiAssistant.getTutorial("first-app");
        System.out.println("   " + tutorial.substring(0, Math.min(150, tutorial.length())) + "...");
        System.out.println();

        System.out.println("Available quick reference topics:");
        System.out.println("  - web, cache, data, event, logging, security");
        System.out.println("  - config, monitor, scheduler, messaging, workflow");
        System.out.println();

        System.out.println("Available best practice categories:");
        System.out.println("  - code-style, error-handling, performance");
        System.out.println("  - security, testing, architecture");
        System.out.println();

        System.out.println("[X] Knowledge query example complete\n");
    }

    private static void codeOperationExample(AiAssistant aiAssistant) {
        System.out.println("\n--- Code Operation Example ---");
        System.out.println("AI Assistant provides powerful code operation functionality\n");

        System.out.println("1. Code Suggestion");
        System.out.println("   Requirement: Create a user management Service");
        String suggestion = aiAssistant.suggestCode("Create a user management Service with CRUD operations");
        System.out.println("   Suggested code:\n" + suggestion);
        System.out.println();

        System.out.println("2. Code Explanation");
        String codeToExplain = """
                public class UserService {
                    private UserRepository repository;
                    
                    public User getUserById(Long id) {
                        return repository.findById(id);
                    }
                }
                """;
        System.out.println("   Code to explain:\n" + codeToExplain);
        String explanation = aiAssistant.explainCode(codeToExplain);
        System.out.println("   Explanation:\n" + explanation);
        System.out.println();

        System.out.println("3. Code Optimization");
        String codeToOptimize = """
                public List<User> getUsers() {
                    List<User> users = new ArrayList<>();
                    for (int i = 0; i < 100; i++) {
                        users.add(repository.findById((long) i));
                    }
                    return users;
                }
                """;
        System.out.println("   Code to optimize:\n" + codeToOptimize);
        String optimized = aiAssistant.optimizeCode(codeToOptimize);
        System.out.println("   Optimized code:\n" + optimized);
        System.out.println();

        System.out.println("[X] Code operation example complete\n");
    }

    private static void codeGeneratorExample(AiAssistant aiAssistant) {
        System.out.println("\n--- Code Generator Example ---");
        System.out.println("Use CodeGenerator to generate various code\n");

        CodeGenerator generator = aiAssistant.getCodeGenerator();

        System.out.println("1. Generate Entity class");
        String entityCode = generator.generateEntity(
                "Product",
                "com.example.entity",
                Map.of("fields", List.of("id:Long", "name:String", "price:BigDecimal", "createdAt:LocalDateTime"))
        );
        System.out.println(entityCode);
        System.out.println();

        System.out.println("2. Generate Repository interface");
        String repoCode = generator.generateRepository(
                "ProductRepository",
                "com.example.repository",
                Map.of("entityName", "Product")
        );
        System.out.println(repoCode);
        System.out.println();

        System.out.println("3. Generate Service class");
        String serviceCode = generator.generateService(
                "ProductService",
                "com.example.service",
                Map.of("entityName", "Product")
        );
        System.out.println(serviceCode);
        System.out.println();

        System.out.println("4. Generate Controller class");
        String controllerCode = generator.generateController(
                "ProductController",
                "com.example.controller",
                Map.of("entityName", "Product")
        );
        System.out.println(controllerCode);
        System.out.println();

        System.out.println("5. Generate POM.xml");
        String pomXml = generator.generatePomXml(
                "ProductService",
                "com.example",
                "product-service",
                "1.0.0"
        );
        System.out.println(pomXml);
        System.out.println();

        System.out.println("6. Generate complete Web application");
        String webAppCode = generator.generateWebApp(
                "ProductApp",
                "com.example",
                Map.of("entities", List.of("Product", "Order", "Customer"))
        );
        System.out.println(webAppCode.substring(0, Math.min(300, webAppCode.length())) + "...");
        System.out.println();

        System.out.println("[X] Code generator example complete\n");
    }

    private static void promptTemplateExample(AiAssistant aiAssistant) {
        System.out.println("\n--- Prompt Template Example ---");
        System.out.println("Use prompt templates to generate standardized prompts\n");

        System.out.println("1. View available template categories");
        List<String> categories = aiAssistant.getTemplateRegistry().getCategories();
        System.out.println("   Category list:");
        for (String category : categories) {
            System.out.println("   - " + category);
        }
        System.out.println();

        System.out.println("2. View templates in a category");
        String targetCategory = categories.isEmpty() ? "general" : categories.get(0);
        List<PromptTemplate> templates = aiAssistant.getTemplateRegistry().getTemplatesByCategory(targetCategory);
        System.out.println("   Category \"" + targetCategory + "\" templates:");
        for (PromptTemplate template : templates) {
            System.out.println("   - " + template.getName() + ": " + template.getDescription());
        }
        System.out.println();

        System.out.println("3. Create custom template");
        PromptTemplate customTemplate = new DefaultPromptTemplate(
                "custom-code-review",
                "code-review",
                "Custom code review template",
                """
                Please act as a senior Java code review expert and review the following code:
                
                Code:
                ${code}
                
                Please output in the following format:
                1. Code quality score (0-100)
                2. Main issue list
                3. Improvement suggestions
                4. Optimized code
                """,
                List.of("code")
        );
        aiAssistant.getTemplateRegistry().register(customTemplate);
        System.out.println("   Custom template registered: " + customTemplate.getName());
        System.out.println();

        System.out.println("4. Use template to generate prompt");
        String sampleCode = """
                public class BadExample {
                    public void doSomething() {
                        System.out.println("Hello");
                    }
                }
                """;
        String prompt = aiAssistant.generatePrompt("custom-code-review", Map.of("code", sampleCode));
        System.out.println("   Generated prompt:\n" + prompt);
        System.out.println();

        System.out.println("[X] Prompt template example complete\n");
    }

    private static void skillSystemExample(AiAssistant aiAssistant) {
        System.out.println("\n--- Skill System Example ---");
        System.out.println("Use Skill system to execute composable AI capabilities\n");

        System.out.println("1. View available Skills");
        List<Skill> skills = aiAssistant.getSkillRegistry().listAll();
        System.out.println("   Available Skills:");
        for (Skill skill : skills) {
            System.out.println("   - " + skill.getName() + " (" + skill.getCategory() + ")");
            System.out.println("     " + skill.getDescription());
        }
        System.out.println();

        if (!skills.isEmpty()) {
            System.out.println("2. Execute Skill");
            Skill firstSkill = skills.get(0);
            System.out.println("   Executing Skill: " + firstSkill.getName());

            Map<String, Object> inputs = Map.of(
                    "className", "Order",
                    "packageName", "com.example.entity",
                    "fields", List.of("id:Long", "orderNo:String", "totalAmount:BigDecimal", "status:String")
            );

            SkillResult result = aiAssistant.getSkillRegistry().execute(firstSkill.getId(), inputs);

            if (result.isSuccess()) {
                System.out.println("   [X] Skill executed successfully");
                System.out.println("   Outputs:");
                for (Map.Entry<String, Object> entry : result.getOutputs().entrySet()) {
                    System.out.println("   - " + entry.getKey() + ": " + 
                            (entry.getValue().toString().length() > 100 ? 
                                    entry.getValue().toString().substring(0, 100) + "..." : 
                                    entry.getValue()));
                }
            } else {
                System.out.println("   [ ] Skill execution failed: " + result.getErrorMessage());
            }
        }
        System.out.println();

        System.out.println("Skill system features:");
        System.out.println("  - Composable AI capability units");
        System.out.println("  - Standardized input/output");
        System.out.println("  - Extensible registration mechanism");
        System.out.println("  - Supports custom Skills");
        System.out.println();

        System.out.println("[X] Skill system example complete\n");
    }
}
