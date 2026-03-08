# AI 助手使用指南

学习如何使用 EST AI 助手的各种功能！

---

## 基本用法

### 创建 AI 助手

首先，创建一个 `AiAssistant` 实例：

```java
import ltd.idcu.est.features.ai.api.AiAssistant;
import ltd.idcu.est.features.ai.impl.DefaultAiAssistant;

public class AiAssistantUsage {
    public static void main(String[] args) {
        AiAssistant assistant = new DefaultAiAssistant();
        
        // 使用 assistant...
    }
}
```

### 获取知识

#### 1. 获取快速参考
```java
String ref = assistant.getQuickReference("web开发");
System.out.println("快速参考:\n" + ref);
```

支持的主题：
- `web开发` - Web 开发快速参考
- `依赖注入` - 依赖注入使用指南
- `集合操作` - Collection 模块使用指南
- `配置管理` - 配置管理快速参考
- 等等

#### 2. 获取最佳实践
```java
String bestPractice = assistant.getBestPractice("代码风格");
System.out.println("\n最佳实践:\n" + bestPractice);
```

支持的类别：
- `代码风格` - 代码风格最佳实践
- `性能优化` - 性能优化建议
- `安全实践` - 安全最佳实践
- `测试` - 测试最佳实践

#### 3. 获取教程

```java
String tutorial = assistant.getTutorial("依赖注入");
System.out.println("\n教程:\n" + tutorial);
```

---

## 代码操作

### 代码建议

让 AI 助手根据需求建议代码：

```java
String suggestion = assistant.suggestCode("""
    我需要一个用户管理服务，要求：
    1. 使用依赖注入
    2. 包含 CRUD 操作
    3. 使用 EST 的 Collection 模块
    """);

System.out.println("建议的代码:\n" + suggestion);
```

#### 好的需求描述

**推荐** - 清晰、具体、有上下文：
```java
"""
请帮我创建一个 UserService 类，要求：
1. 类名：UserService
2. 包名：com.example.service
3. 功能要求：
   - getUserById(Long id) - 根据 ID 查询用户
   - createUser(User user) - 创建用户
   - updateUser(User user) - 更新用户
   - deleteUser(Long id) - 删除用户
   - listUsers() - 获取所有用户列表
4. 技术要求：
   - 使用 EST 框架的依赖注入
   - 使用 EST Collection 处理集合
   - 添加必要的注释
   - 遵循 EST 代码风格

5. 参考：
   - 查看 est-examples-basic 中的示例代码
   - 遵循最佳实践指南
"""
```

**不推荐** - 模糊、缺少上下文：
```java
"写个代码"
"帮我做个功能"
"写个 User 相关的"
```

---

### 代码解释

让 AI 助手解释代码：
```java
String code = """
    public class UserService {
        private final UserRepository repo;
        
        @Inject
        public UserService(UserRepository repo) {
            this.repo = repo;
        }
        
        public User create(User user) {
            return repo.save(user);
        }
    }
    """;

String explanation = assistant.explainCode(code);
System.out.println("代码解释:\n" + explanation);
```

---

### 代码优化

让 AI 助手优化代码：
```java
String unoptimizedCode = """
    public List<User> getActiveUsers() {
        List<User> all = userRepository.findAll();
        List<User> active = new ArrayList<>();
        for (User user : all) {
            if (user.isActive()) {
                active.add(user);
            }
        }
        return active;
    }
    """;

String optimizedCode = assistant.optimizeCode(unoptimizedCode);
System.out.println("优化后的代码:\n" + optimizedCode);
```

优化后的代码可能使用 EST Collection 模块：
```java
public List<User> getActiveUsers() {
    return Seqs.of(userRepository.findAll())
        .filter(User::isActive)
        .toList();
}
```

---

## 使用提示词模板

### 生成提示词
使用注册的模板生成提示词：
```java
Map<String, String> variables = new HashMap<>();
variables.put("entityName", "User");
variables.put("packageName", "com.example.service");

String prompt = assistant.generatePrompt("generate-service", variables);
System.out.println("生成的提示词:\n" + prompt);
```

### 获取模板注册表
```java
PromptTemplateRegistry registry = assistant.getTemplateRegistry();

// 列出所有模板
List<PromptTemplate> allTemplates = registry.listTemplates();

// 列出某个分类的模板
List<PromptTemplate> codeGenTemplates = registry.listTemplatesByCategory("code-generation");
```

---

## 获取其他组件

### 获取代码生成器
```java
CodeGenerator generator = assistant.getCodeGenerator();

String entityCode = generator.generateEntity(
    "Product",
    "com.example.entity",
    Map.of(
        "fields", List.of("id:Long", "name:String", "price:BigDecimal")
    )
);
```

### 获取项目脚手架
```java
ProjectScaffold scaffold = assistant.getProjectScaffold();

scaffold.generate(
    Paths.get("./my-project"),
    "com.example",
    "my-project",
    List.of("User", "Product", "Order")
);
```

---

## LLM 对话功能

### 单轮对话
```java
// 设置 API 密钥
assistant.getLlmClient().setApiKey("your-api-key");

// 进行对话
String response = assistant.chat("你好，请介绍一下EST框架");
System.out.println(response);
```

### 多轮对话
```java
import ltd.idcu.est.features.ai.api.LlmMessage;
import java.util.List;

List<LlmMessage> messages = List.of(
    new LlmMessage("user", "什么是依赖注入？"),
    new LlmMessage("assistant", "依赖注入是一种设计模式..."),
    new LlmMessage("user", "EST框架如何实现依赖注入？")
);

String response = assistant.chat(messages);
System.out.println(response);
```

### 清除对话历史
```java
assistant.clearConversationHistory();
```

---

## 中期目标功能

### 智能代码补全
```java
import ltd.idcu.est.features.ai.api.CompletionOptions;
import ltd.idcu.est.features.ai.api.CompletionSuggestion;

String context = """
    package com.example;
    public class UserService {
        private UserRepository userRepository;
        public List<User> findAll() {
    """;

// 基础补全
CompletionOptions options = new CompletionOptions();
List<CompletionSuggestion> suggestions = assistant.completeCode(context, options);

// LLM驱动补全
CompletionOptions llmOptions = new CompletionOptions().setUseLlm(true);
List<CompletionSuggestion> llmSuggestions = assistant.completeCode(context, llmOptions);
```

### AI重构助手
```java
import ltd.idcu.est.features.ai.api.RefactorOptions;
import ltd.idcu.est.features.ai.api.RefactorSuggestion;

String code = """
    public class OrderProcessor {
        public void processOrders(List<String> orders) {
            // 需要重构的代码
        }
    }
    """;

// 基础分析
RefactorOptions options = RefactorOptions.defaults();
List<RefactorSuggestion> suggestions = assistant.analyzeCode(code, options);

// LLM驱动分析
RefactorOptions llmOptions = RefactorOptions.defaults().useLlm(true);
List<RefactorSuggestion> llmSuggestions = assistant.analyzeCode(code, llmOptions);
```

### 架构顾问
```java
import ltd.idcu.est.features.ai.api.ArchitectureOptions;
import ltd.idcu.est.features.ai.api.ArchitectureSuggestion;

String requirement = "构建一个电商平台，包含用户管理、商品管理、订单管理";

// 基础建议
ArchitectureOptions options = ArchitectureOptions.defaults();
ArchitectureSuggestion suggestion = assistant.suggestArchitecture(requirement, options);

// LLM驱动建议
ArchitectureOptions llmOptions = ArchitectureOptions.defaults().useLlm(true);
ArchitectureSuggestion llmSuggestion = assistant.suggestArchitecture(requirement, llmOptions);
```

---

## 长期目标功能

### 需求解析器
```java
import ltd.idcu.est.features.ai.api.ParsedRequirement;
import ltd.idcu.est.features.ai.api.RequirementParser;

String requirement = """
    我需要构建一个电商平台，包含用户管理、商品管理、
    订单管理功能。要求支持高并发，使用微服务架构。
    """;

RequirementParser parser = assistant.getRequirementParser();
ParsedRequirement parsed = parser.parse(requirement);

// 提取组件
List<String> components = parser.extractComponents(parsed);

// 获取元数据
Map<String, Object> metadata = parser.getRequirementsMetadata(parsed);
```

### 架构设计器
```java
import ltd.idcu.est.features.ai.api.ArchitectureDesign;
import ltd.idcu.est.features.ai.api.ArchitectureDesigner;

ArchitectureDesigner designer = assistant.getArchitectureDesigner();

// 设计架构
ArchitectureDesign design = designer.designArchitecture(parsedRequirement);

// 推荐架构模式
List<String> patterns = designer.recommendPatterns(parsedRequirement);

// 验证架构
boolean isValid = designer.validateArchitecture(design);
```

### 测试和部署管理器
```java
import ltd.idcu.est.features.ai.api.TestSuite;
import ltd.idcu.est.features.ai.api.DeploymentPlan;
import ltd.idcu.est.features.ai.api.TestAndDeployManager;

TestAndDeployManager manager = assistant.getTestAndDeployManager();

// 生成测试
TestSuite testSuite = manager.generateTests(code, context);

// 创建部署计划
DeploymentPlan plan = manager.createDeploymentPlan("MyProject", config);

// 运行测试
boolean testPassed = manager.runTests(testSuite);

// 执行部署
boolean deploySuccess = manager.executeDeployment(plan);
```

### 完整工作流程
```java
// 1. 解析需求
ParsedRequirement requirement = assistant.getRequirementParser()
    .parse("构建一个用户管理系统");

// 2. 设计架构
ArchitectureDesign design = assistant.getArchitectureDesigner()
    .designArchitecture(requirement);

// 3. 生成代码（使用代码生成器）
String code = assistant.getCodeGenerator()
    .generateFromRequirement(requirement.getOriginalRequirement());

// 4. 生成测试
TestSuite tests = assistant.getTestAndDeployManager()
    .generateTests(code, "用户管理系统");

// 5. 创建部署计划
DeploymentPlan plan = assistant.getTestAndDeployManager()
    .createDeploymentPlan("UserManagement", config);
```

---

## 完整示例

```java
import ltd.idcu.est.features.ai.api.AiAssistant;
import ltd.idcu.est.features.ai.impl.DefaultAiAssistant;

public class CompleteAiAssistantExample {
    public static void main(String[] args) {
        AiAssistant assistant = new DefaultAiAssistant();
        
        System.out.println("=== EST AI 助手完整示例 ===\n");
        
        // 1. 获取知识
        System.out.println("--- 1. 获取快速参考 ---");
        String ref = assistant.getQuickReference("web开发");
        System.out.println(ref);
        
        // 2. 代码建议
        System.out.println("\n--- 2. 代码建议 ---");
        String suggestion = assistant.suggestCode("创建一个 OrderService");
        System.out.println(suggestion);
        
        // 3. 代码解释
        System.out.println("\n--- 3. 代码解释 ---");
        String explanation = assistant.explainCode(suggestion);
        System.out.println(explanation);
        
        // 4. 代码优化
        System.out.println("\n--- 4. 代码优化 ---");
        String optimized = assistant.optimizeCode(suggestion);
        System.out.println(optimized);
        
        System.out.println("\n✅ 完成！");
    }
}
```

---

**下一页**: 探索 [代码生成器指南](code-generator.md)
