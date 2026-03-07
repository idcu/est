# EST AI - 人工智能助手

## 📚 目录

- [快速入门](#快速入门)
- [基础篇](#基础篇)
- [进阶篇](#进阶篇)
- [最佳实践](#最佳实践)

---

## 🚀 快速入门

### 什么是 AI 助手？

想象一下，你有一个聪明的助手：
- 你告诉它需求，它帮你写代码
- 你问它问题，它给你答案
- 它可以帮你生成项目脚手架

**EST AI**就是这样的智能助手，它提供：
- 代码生成（Code Generator）
- 提示模板（Prompt Template）
- 项目脚手架（Project Scaffold）
- AI 对话功能

让 AI 帮助你更高效地开发！

### 第一个例子

让我们用 3 分钟写一个简单的 AI 程序！

首先，在你的 `pom.xml` 中添加依赖：

```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-ai-api</artifactId>
    <version>1.3.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-ai-impl</artifactId>
    <version>1.3.0</version>
</dependency>
```

然后创建一个简单的 Java 类：

```java
import ltd.idcu.est.features.ai.api.AiAssistant;
import ltd.idcu.est.features.ai.impl.Ai;
import ltd.idcu.est.features.ai.impl.DefaultAiAssistant;

public class AiFirstExample {
    public static void main(String[] args) {
        System.out.println("=== AI 助手示例 ===\n");
        
        // 创建 AI 助手
        AiAssistant assistant = new DefaultAiAssistant();
        
        // 使用 AI 生成代码
        String code = assistant.generateCode("写一个 Java 类，包含 main 方法，打印 Hello World");
        System.out.println("生成的代码:\n" + code);
        
        System.out.println("\n✅ AI 示例完成！");
    }
}
```

运行这个程序，体验 AI 的强大！

🎉 恭喜你！你已经学会了使用 AI 助手！

---

## 📖 基础篇

### 1. 核心概念

| 概念 | 说明 | 生活类比 |
|------|------|----------|
| **AI 助手** | 智能对话和代码生成 | 聪明的助手 |
| **代码生成器** | 根据需求生成代码 | 代码打字员 |
| **提示模板** | 预定义的提示格式 | 填空表格 |
| **项目脚手架** | 快速生成项目结构 | 建筑蓝图 |

### 2. 代码生成

```java
import ltd.idcu.est.features.ai.api.CodeGenerator;
import ltd.idcu.est.features.ai.impl.DefaultCodeGenerator;

public class CodeGeneratorExample {
    public static void main(String[] args) {
        CodeGenerator generator = new DefaultCodeGenerator();
        
        // 生成实体类
        String entityCode = generator.generateEntity("User", 
                List.of("id:Long", "name:String", "email:String"));
        System.out.println("生成的实体类:\n" + entityCode);
        
        // 生成 Repository 接口
        String repoCode = generator.generateRepository("User", "UserRepository");
        System.out.println("\n生成的 Repository:\n" + repoCode);
        
        // 生成 Service 类
        String serviceCode = generator.generateService("User", "UserService");
        System.out.println("\n生成的 Service:\n" + serviceCode);
    }
}
```

### 3. 提示模板

```java
import ltd.idcu.est.features.ai.api.PromptTemplate;
import ltd.idcu.est.features.ai.api.PromptTemplateRegistry;
import ltd.idcu.est.features.ai.impl.DefaultPromptTemplate;
import ltd.idcu.est.features.ai.impl.DefaultPromptTemplateRegistry;

import java.util.HashMap;
import java.util.Map;

public class PromptTemplateExample {
    public static void main(String[] args) {
        PromptTemplateRegistry registry = new DefaultPromptTemplateRegistry();
        
        // 创建提示模板
        PromptTemplate template = new DefaultPromptTemplate(
                "generate-controller",
                "请为 {entityName} 生成一个 Spring Boot Controller，包含 CRUD 操作"
        );
        registry.register(template);
        
        // 使用模板
        Map<String, String> variables = new HashMap<>();
        variables.put("entityName", "User");
        
        String prompt = registry.apply("generate-controller", variables);
        System.out.println("生成的提示:\n" + prompt);
    }
}
```

---

## 🔧 进阶篇

### 1. 项目脚手架

```java
import ltd.idcu.est.features.ai.api.ProjectScaffold;
import ltd.idcu.est.features.ai.impl.DefaultProjectScaffold;

import java.nio.file.Paths;
import java.util.List;

public class ProjectScaffoldExample {
    public static void main(String[] args) {
        ProjectScaffold scaffold = new DefaultProjectScaffold();
        
        // 生成项目结构
        scaffold.generate(
                Paths.get("./my-project"),
                "com.example",
                "my-project",
                List.of("User", "Product", "Order")
        );
        
        System.out.println("项目脚手架生成完成！");
    }
}
```

### 2. 与 EST Collection 集成

```java
import ltd.idcu.est.collection.api.Seqs;
import ltd.idcu.est.features.ai.api.PromptTemplate;
import ltd.idcu.est.features.ai.impl.DefaultPromptTemplate;

import java.util.List;
import java.util.Map;

public class AiCollectionIntegrationExample {
    public static void main(String[] args) {
        List<String> entities = List.of("User", "Product", "Order");
        
        // 使用 Collection 批量生成代码提示
        Seqs.of(entities)
                .map(entity -> {
                    PromptTemplate template = new DefaultPromptTemplate(
                            "entity-" + entity,
                            "为 " + entity + " 生成完整的代码"
                    );
                    return template.apply(Map.of());
                })
                .forEach(prompt -> System.out.println("提示: " + prompt));
    }
}
```

---

## 💡 最佳实践

### 1. 好的提示技巧

```java
import ltd.idcu.est.features.ai.api.AiAssistant;
import ltd.idcu.est.features.ai.impl.DefaultAiAssistant;

public class GoodPromptExample {
    public static void main(String[] args) {
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
    }
}
```

---

## 🎯 总结

EST AI 就像你的"智能助手"，帮助你更高效地开发代码！

至此，我们已经完成了 EST 框架所有核心模块的学习！🎉

祝你在使用 EST 框架的开发过程中一切顺利！
