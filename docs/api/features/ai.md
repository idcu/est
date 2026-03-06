# AI 人工智能助手 API

AI 助手提供代码生成、提示模板、项目脚手架等功能，帮助开发者更高效地开发。

## 核心接口

```java
public interface AiAssistant {
    String chat(String message);
    String generateCode(String prompt);
    String explainCode(String code);
    String refactorCode(String code, String instruction);
}

public interface CodeGenerator {
    String generateEntity(String className, List<String> fields);
    String generateRepository(String entityName, String repoName);
    String generateService(String entityName, String serviceName);
    String generateController(String entityName, String controllerName);
}

public interface PromptTemplate {
    String getName();
    String getTemplate();
    String apply(Map<String, String> variables);
}

public interface PromptTemplateRegistry {
    void register(PromptTemplate template);
    PromptTemplate get(String name);
    String apply(String name, Map<String, String> variables);
}

public interface ProjectScaffold {
    void generate(Path targetDir, String groupId, String artifactId, List<String> entities);
}
```

## AI 助手 (AiAssistant)

```java
import ltd.idcu.est.features.ai.api.AiAssistant;
import ltd.idcu.est.features.ai.impl.DefaultAiAssistant;

// 创建 AI 助手
AiAssistant assistant = new DefaultAiAssistant();

// 使用 AI 生成代码
String code = assistant.generateCode("写一个 Java 类，包含 main 方法，打印 Hello World");
System.out.println("生成的代码:\n" + code);

// 解释代码
String explanation = assistant.explainCode(code);
System.out.println("代码解释:\n" + explanation);

// 重构代码
String refactored = assistant.refactorCode(code, "添加注释并改进命名");
System.out.println("重构后代码:\n" + refactored);

// 对话
String response = assistant.chat("什么是设计模式？");
System.out.println("AI 回答: " + response);
```

## 代码生成器 (CodeGenerator)

```java
import ltd.idcu.est.features.ai.api.CodeGenerator;
import ltd.idcu.est.features.ai.impl.DefaultCodeGenerator;
import java.util.List;

CodeGenerator generator = new DefaultCodeGenerator();

// 生成实体类
String entityCode = generator.generateEntity("User", 
        List.of("id:Long", "name:String", "email:String", "age:Integer"));
System.out.println("生成的实体类:\n" + entityCode);

// 生成 Repository 接口
String repoCode = generator.generateRepository("User", "UserRepository");
System.out.println("\n生成的 Repository:\n" + repoCode);

// 生成 Service 类
String serviceCode = generator.generateService("User", "UserService");
System.out.println("\n生成的 Service:\n" + serviceCode);

// 生成 Controller 类
String controllerCode = generator.generateController("User", "UserController");
System.out.println("\n生成的 Controller:\n" + controllerCode);
```

## 提示模板 (PromptTemplate)

```java
import ltd.idcu.est.features.ai.api.PromptTemplate;
import ltd.idcu.est.features.ai.api.PromptTemplateRegistry;
import ltd.idcu.est.features.ai.impl.DefaultPromptTemplate;
import ltd.idcu.est.features.ai.impl.DefaultPromptTemplateRegistry;
import java.util.HashMap;
import java.util.Map;

PromptTemplateRegistry registry = new DefaultPromptTemplateRegistry();

// 创建提示模板
PromptTemplate template = new DefaultPromptTemplate(
        "generate-controller",
        "请为 {entityName} 生成一个 Controller，包含 CRUD 操作"
);
registry.register(template);

// 使用模板
Map<String, String> variables = new HashMap<>();
variables.put("entityName", "User");

String prompt = registry.apply("generate-controller", variables);
System.out.println("生成的提示:\n" + prompt);

// 获取模板
PromptTemplate retrieved = registry.get("generate-controller");
System.out.println("模板内容: " + retrieved.getTemplate());
```

## 项目脚手架 (ProjectScaffold)

```java
import ltd.idcu.est.features.ai.api.ProjectScaffold;
import ltd.idcu.est.features.ai.impl.DefaultProjectScaffold;
import java.nio.file.Paths;
import java.util.List;

ProjectScaffold scaffold = new DefaultProjectScaffold();

// 生成项目结构
scaffold.generate(
        Paths.get("./my-project"),
        "com.example",
        "my-project",
        List.of("User", "Product", "Order")
);

System.out.println("项目脚手架生成完成！");
```

## 便捷工具类 (Ai)

```java
import ltd.idcu.est.features.ai.impl.Ai;

// 快速创建 AI 助手
AiAssistant assistant = Ai.create();

// 快速生成代码
String code = Ai.generateCode("写一个单例模式");

// 快速对话
String answer = Ai.chat("解释一下 Spring Boot 的工作原理");
```

## 提示最佳实践

```java
import ltd.idcu.est.features.ai.api.AiAssistant;
import ltd.idcu.est.features.ai.impl.DefaultAiAssistant;

AiAssistant assistant = new DefaultAiAssistant();

// ❌ 不好的提示
String badPrompt = "写个代码";

// ✅ 好的提示：明确、具体、有上下文
String goodPrompt = """
        请帮我写一个 Java 类，要求：
        1. 类名：UserService
        2. 包含以下方法：
           - getUserById(Long id)
           - createUser(User user)
           - updateUser(User user)
           - deleteUser(Long id)
        3. 使用 Spring Framework 的 @Service 注解
        4. 添加必要的注释
        """;

String code = assistant.generateCode(goodPrompt);
System.out.println("生成的代码:\n" + code);
```
