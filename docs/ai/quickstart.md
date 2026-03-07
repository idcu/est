# 快速入门
本指南将帮助你在 5 分钟内开始使用 EST AI 功能。
---

## 前置条件

确保你已经：
- ✓ 安装了 JDK 21 或更高版本
- ✓ 安装了 Maven 3.6 或更高版本
- ✓ 了解 EST 框架的基础知识

---

## 第一步：引入依赖

在你的 `pom.xml` 中添加 EST AI 模块：
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-ai-api</artifactId>
    <version>2.0.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-ai-impl</artifactId>
    <version>2.0.0</version>
</dependency>
```

---

## 第二步：创建你的第一个 AI 程序

创建一个简单的 Java 类：

```java
import ltd.idcu.est.features.ai.api.AiAssistant;
import ltd.idcu.est.features.ai.impl.DefaultAiAssistant;

public class FirstAiProgram {
    public static void main(String[] args) {
        System.out.println("=== EST AI 快速开始 ===\n");
        
        // 创建 AI 助手
        AiAssistant assistant = new DefaultAiAssistant();
        
        // 1. 生成代码
        System.out.println("--- 1. 生成代码 ---");
        String code = assistant.suggestCode(
            "创建一个 UserService，包含增删改查方法"
        );
        System.out.println("生成的代码:\n" + code);
        
        // 2. 解释代码
        System.out.println("\n--- 2. 解释代码 ---");
        String explanation = assistant.explainCode(code);
        System.out.println("代码解释:\n" + explanation);
        
        // 3. 优化代码
        System.out.println("\n--- 3. 优化代码 ---");
        String optimized = assistant.optimizeCode(code);
        System.out.println("优化后的代码:\n" + optimized);
        
        System.out.println("\n✅ 恭喜！你已经完成了 EST AI 快速开始！");
    }
}
```

---

## 第三步：运行示例

你也可以直接运行 EST 提供的 AI 示例：
```bash
cd est-examples/est-examples-ai
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.AiQuickStartExample"
```

---

## 第四步：探索高级功能

### 4.1 使用 Skill 系统生成实体类

```java
import ltd.idcu.est.features.ai.api.AiAssistant;
import ltd.idcu.est.features.ai.api.skill.SkillResult;
import ltd.idcu.est.features.ai.impl.DefaultAiAssistant;

import java.util.List;
import java.util.Map;

public class SkillExample {
    public static void main(String[] args) {
        AiAssistant assistant = new DefaultAiAssistant();
        
        // 生成实体类
        SkillResult result = assistant.getSkillRegistry().execute("generate-entity",
            Map.of("className", "Product",
                   "packageName", "com.example.entity",
                   "fields", List.of("id:Long", "name:String", "price:Double")));
        
        if (result.isSuccess()) {
            System.out.println(result.getOutputs().get("code"));
        }
    }
}
```

### 4.2 进行代码审查

```java
String codeToReview = """
    public class BadExample {
        public void doSomething() {
            System.out.println("Hello");
            try {
            } catch (Exception e) {}
        }
    }
    """;

SkillResult reviewResult = assistant.getSkillRegistry().execute("code-review",
        Map.of("code", codeToReview));

if (reviewResult.isSuccess()) {
    System.out.println("问题: " + reviewResult.getOutputs().get("issues"));
    System.out.println("评分: " + reviewResult.getOutputs().get("score") + "/100");
}
```

### 4.3 使用 MCP Server

```java
import ltd.idcu.est.features.ai.api.mcp.McpServer;
import ltd.idcu.est.features.ai.api.mcp.McpToolResult;
import ltd.idcu.est.features.ai.impl.mcp.DefaultMcpServer;

import java.util.Map;

public class McpExample {
    public static void main(String[] args) {
        McpServer mcpServer = new DefaultMcpServer();
        
        // 列出可用工具
        System.out.println("可用工具:");
        mcpServer.getTools().forEach(tool -> 
            System.out.println("- " + tool.getName() + ": " + tool.getDescription()));
            
        // 调用工具
        McpToolResult result = mcpServer.callTool("est_generate_controller",
            Map.of("controllerName", "Product",
                   "packageName", "com.example.controller"));
            
        if (result.isSuccess()) {
            System.out.println(result.getContent());
        }
    }
}
```

### 4.4 集成智谱 AI LLM

```java
import ltd.idcu.est.features.ai.api.AiAssistant;
import ltd.idcu.est.features.ai.impl.DefaultAiAssistant;
import ltd.idcu.est.features.ai.impl.llm.ZhipuAiLlmClient;

public class LlmExample {
    public static void main(String[] args) {
        AiAssistant assistant = new DefaultAiAssistant();
        
        // 设置智谱 AI 客户端
        ZhipuAiLlmClient llmClient = new ZhipuAiLlmClient();
        llmClient.setApiKey("your-api-key-here");
        assistant.setLlmClient(llmClient);
        
        // 使用 LLM 进行对话
        String response = assistant.chat("你好，请介绍一下 EST 框架");
        System.out.println(response);
    }
}
```

---

## 下一步
现在你已经完成了快速入门，可以：
- 📖 了解 [AI 模块架构](architecture.md)
- 💬 学习 [AI 助手使用指南](ai-assistant.md)
- 🎨 探索 [代码生成器指南](code-generator.md)
- 🧩 学习 [Skill 系统](skill-system.md)
- 🔗 使用 [MCP Server](mcp-server.md)
- 🤖 集成 [LLM](llm-integration.md)
- 📝 研究 [提示词工程](prompt-engineering.md)
- 🔗 查看 [与 AI 编程工具集成](integration.md)
- 💡 阅读 [最佳实践](best-practices.md)

---

**文档版本**: 2.0  
**最后更新**: 2026-03-07  
**维护者**: EST 架构团队
