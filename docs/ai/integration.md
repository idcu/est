# �?AI 编程工具集成

学习如何�?EST 与主�?AI 编程工具（如 GitHub Copilot、Cursor、Windsurf 等）配合使用�?
---

## �?GitHub Copilot 配合使用

### 1. �?Copilot 设置中添�?EST 文档

�?Copilot 索引 EST 的文档和示例�?
1. 打开 VS Code 设置
2. 搜索 "Copilot"
3. �?"Copilot: Include" 中添�?EST 项目路径
4. 确保 Copilot 可以访问 `est-examples/` �?`docs/` 目录

这样可以提高 Copilot 生成 EST 代码的准确率�?
### 2. 创建 EST 专用的提示词

在代码文件开头添�?EST 专用的提示词�?
```java
// 使用 EST 框架
// 参�? https://github.com/idcu/est
// 遵循 EST 代码风格
// 使用 EST 零依赖设�?
package com.example;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class Main {
    // Copilot 会根据上面的提示词生成更好的 EST 代码
}
```

### 3. 利用 AI 助手获取建议

先用 EST AI 助手获取基础代码，再�?Copilot 进行微调�?
```java
// 第一步：�?EST AI 助手生成基础代码
AiAssistant assistant = new DefaultAiAssistant();
String baseCode = assistant.suggestCode("创建一�?UserService");

// 第二步：�?baseCode 复制�?IDE，让 Copilot 进行微调
// Copilot 会根据上下文提供更好的建�?```

### 4. 使用 Copilot Chat 询问 EST 问题

�?Copilot Chat 中：

```
@workspace 如何�?EST 中使用依赖注入？
```

```
@workspace 给我一�?EST Web 应用的示�?```

---

## �?Cursor / Windsurf 配合

### 1. 配置 EST 代码�?
�?EST 代码库添加到项目上下文：

1. 打开 Cursor / Windsurf
2. 添加 EST 项目到工作区
3. 确保索引完整

这样 AI 工具能更好地理解 EST 架构�?
### 2. 使用 @est 标签

在对话中使用 @est 引用 EST 文档�?
```
@est 如何创建一�?Web 应用�?```

```
@est 给我看一�?Collection 模块的使用示�?```

### 3. 创建 EST 专用规则

在设置中添加 EST 代码风格规则�?
```json
{
  "rules": {
    "est": {
      "useZeroDependency": true,
      "preferEstCollection": true,
      "useDependencyInjection": true,
      "codeStyle": "est"
    }
  }
}
```

### 4. 使用 Cmd+K 快速生�?
选中需求描述，�?Cmd+K�?
```
创建一�?EST UserService，包�?CRUD 操作
```

AI 会根�?EST 示例生成符合规范的代码�?
---

## 与国内主流 AI 工具集成

### 1. 智谱 AI (GLM) 集成

智谱 AI 提供强大的代码生成和理解能力，与 EST 框架配合使用效果极佳。

#### 基本配置
```java
import ltd.idcu.est.features.ai.api.*;
import ltd.idcu.est.features.ai.impl.DefaultAiAssistant;

public class ZhipuAiIntegration {
    private final AiAssistant estAiAssistant;
    
    public ZhipuAiIntegration() {
        this.estAiAssistant = new DefaultAiAssistant();
    }
    
    public String generateCodeWithGLM(String requirement) {
        String estBaseCode = estAiAssistant.suggestCode(requirement);
        
        String prompt = """
            你是一个专业的 Java 开发专家，精通 EST 框架。
            
            EST 框架特点：
            1. 零依赖 - 仅使用 Java 标准库
            2. 递进式模块结构
            3. 简洁直观的 API
            4. 依赖注入容器
            5. Web 框架
            
            以下是 EST AI 助手提供的基础代码：
            %s
            
            请根据以下需求完善代码：
            %s
            """.formatted(estBaseCode, requirement);
        
        return callGLM(prompt);
    }
    
    private String callGLM(String prompt) {
        // 调用智谱 AI API
        // 参考：https://open.bigmodel.cn/
        return "智谱 AI 生成的代码";
    }
}
```

#### 在 IDE 中使用
- 安装支持智谱 AI 的 IDE 插件
- 将 EST 项目添加到工作区
- 使用 @est 标签引用 EST 文档

---

### 2. 通义千问 (Qwen) 集成

阿里巴巴通义千问提供优秀的代码理解和生成能力。

#### 快速开始
```java
import ltd.idcu.est.features.ai.api.*;

public class QwenIntegration {
    
    public static String buildEstPrompt(String requirement) {
        AiAssistant assistant = new DefaultAiAssistant();
        
        String quickRef = assistant.getQuickReference("web");
        String baseCode = assistant.suggestCode(requirement);
        
        return """
            # 角色
            你是一位精通 EST 框架的 Java 架构师
            
            # EST 框架参考
            %s
            
            # 基础代码
            %s
            
            # 任务
            %s
            
            请生成符合 EST 风格的代码。
            """.formatted(quickRef, baseCode, requirement);
    }
}
```

#### 通义千问 IDE 插件配置
1. 安装通义千问 IDE 插件
2. 在插件设置中添加 EST 项目路径
3. 配置 EST 专用提示词模板

---

### 3. 文心一言 (ERNIE) 集成

百度文心一言在企业级应用开发方面有独特优势。

#### 集成示例
```java
public class ErnieIntegration {
    
    private final AiAssistant estAi;
    
    public ErnieIntegration() {
        this.estAi = new DefaultAiAssistant();
    }
    
    public void generateModule(String moduleName) {
        System.out.println("=== 生成 " + moduleName + " 模块 ===\n");
        
        // 1. 使用 EST AI 助手生成实体
        System.out.println("--- 1. 生成 Entity ---");
        String entityCode = estAi.suggestCode("创建 " + moduleName + " 实体");
        System.out.println(entityCode);
        
        // 2. 生成 Repository
        System.out.println("\n--- 2. 生成 Repository ---");
        String repoCode = estAi.suggestCode("创建 " + moduleName + "Repository");
        System.out.println(repoCode);
        
        // 3. 将代码传给文心一言进行完善
        System.out.println("\n--- 3. 调用文心一言完善代码 ---");
        String enhancedCode = callErnie(entityCode + "\n\n" + repoCode);
        System.out.println(enhancedCode);
    }
    
    private String callErnie(String code) {
        // 调用文心一言 API
        // 参考：https://cloud.baidu.com/product/wenxin
        return "文心一言增强后的代码";
    }
}
```

---

### 4. 豆包 (Doubao) 集成

字节跳动豆包提供快速响应和良好的代码生成质量。

#### 使用指南
1. 在豆包 IDE 插件中添加 EST 项目
2. 创建 `.ai/prompts.md` 文件，包含 EST 框架说明
3. 使用豆包的智能补全功能

#### 提示词模板
```markdown
# EST 框架提示词
- 零依赖设计
- 使用 @Inject 注解进行依赖注入
- 使用 EST Collection 处理集合
- 参考 est-examples/ 目录下的示例
- 遵循 docs/ 中的架构文档
```

---

### 5. 月之暗面 (Moonshot) / Kimi 集成

Kimi 以长文本理解能力著称，适合处理大型代码库。

#### 长代码分析
```java
public class KimiIntegration {
    
    public String analyzeArchitecture(String projectPath) {
        AiAssistant estAi = new DefaultAiAssistant();
        
        String architectureDoc = estAi.getArchitectureAdvisor()
            .getSuggestions(new ArchitectureOptions())
            .stream()
            .map(s -> s.getTitle() + ": " + s.getDescription())
            .collect(Collectors.joining("\n"));
        
        String prompt = """
            请分析以下 EST 项目的架构：
            
            # EST 框架架构建议
            %s
            
            # 项目代码（长文本）
            [在这里粘贴项目代码]
            
            请提供：
            1. 架构评估
            2. 改进建议
            3. 性能优化方向
            """.formatted(architectureDoc);
        
        return callKimi(prompt);
    }
    
    private String callKimi(String prompt) {
        // 调用 Moonshot API
        // 参考：https://platform.moonshot.cn/
        return "Kimi 分析结果";
    }
}
```

---

### 6. 零一万物 (Yi) 集成

零一万物提供高效的代码生成能力。

#### 快速集成
```java
public class YiIntegration {
    
    public static void main(String[] args) {
        AiAssistant estAi = new DefaultAiAssistant();
        
        String requirement = "创建一个电商订单管理系统";
        
        // 1. 从 EST 获取基础代码结构
        String baseStructure = estAi.suggestCode(requirement);
        
        // 2. 从 EST 获取架构建议
        List<ArchitectureSuggestion> suggestions = 
            estAi.getArchitectureAdvisor().getSuggestions(
                new ArchitectureOptions()
                    .setProjectType("web")
                    .setScalabilityRequired(true)
            );
        
        // 3. 组合提示词
        String fullPrompt = buildPrompt(requirement, baseStructure, suggestions);
        
        // 4. 调用 Yi API
        String result = callYi(fullPrompt);
        
        System.out.println(result);
    }
}
```

---

## 与自定义 AI 工具集成

### 基本集成示例

```java
import ltd.idcu.est.features.ai.api.AiAssistant;
import ltd.idcu.est.features.ai.impl.DefaultAiAssistant;

public class CustomAiToolIntegration {
    private final AiAssistant estAiAssistant;
    
    public CustomAiToolIntegration() {
        this.estAiAssistant = new DefaultAiAssistant();
    }
    
    public String generateEstCode(String requirement) {
        // 1. 使用 EST 内置模板
        String baseCode = estAiAssistant.suggestCode(requirement);
        
        // 2. 应用你的自定义逻辑
        String enhancedCode = enhanceWithYourLogic(baseCode);
        
        return enhancedCode;
    }
    
    public String getEstDocumentation(String topic) {
        return estAiAssistant.getQuickReference(topic);
    }
    
    private String enhanceWithYourLogic(String code) {
        // 添加你的自定义逻辑
        return code;
    }
}
```

### 创建 EST 专用�?LLM 提示�?
```java
public class EstPromptBuilder {
    
    public static String buildCodeGenerationPrompt(String requirement) {
        return """
            你是一个专业的 EST 框架开发专家�?            
            EST 框架特点�?            1. 零依�?- 仅使�?Java 标准�?            2. 递进式模块结�?            3. 简洁直观的 API
            4. 依赖注入容器
            5. Web 框架
            
            请参考以�?EST 代码风格�?            
            示例 1 - Web 应用�?            import ltd.idcu.est.web.Web;
            import ltd.idcu.est.web.api.WebApplication;
            
            public class Main {
                public static void main(String[] args) {
                    WebApplication app = Web.create("我的应用", "1.0.0");
                    app.get("/", (req, res) -> res.send("Hello"));
                    app.run(8080);
                }
            }
            
            示例 2 - 依赖注入�?            public class UserService {
                private final UserRepository repo;
                
                @Inject
                public UserService(UserRepository repo) {
                    this.repo = repo;
                }
            }
            
            现在请根据以下需求生成代码：
            
            %s
            """.formatted(requirement);
    }
}
```

### 集成 LLM API

```java
import ltd.idcu.est.features.ai.api.AiAssistant;
import ltd.idcu.est.features.ai.impl.DefaultAiAssistant;

public class LlmIntegration {
    private final AiAssistant estAiAssistant;
    private final YourLlmClient llmClient;
    
    public LlmIntegration(YourLlmClient llmClient) {
        this.estAiAssistant = new DefaultAiAssistant();
        this.llmClient = llmClient;
    }
    
    public String generateCodeWithLlm(String requirement) {
        // 1. �?EST AI 助手获取基础提示�?        String estPrompt = estAiAssistant.suggestCode(requirement);
        
        // 2. 构建完整�?LLM 提示�?        String fullPrompt = EstPromptBuilder.buildCodeGenerationPrompt(
            requirement + "\n\n参考代�?\n" + estPrompt
        );
        
        // 3. 调用 LLM
        return llmClient.generate(fullPrompt);
    }
}
```

---

## 最佳实�?
### 1. �?AI 工具索引 EST 代码�?
确保 AI 工具可以访问�?- `est-modules/` - 模块源代�?- `est-examples/` - 示例代码
- `docs/` - 文档

### 2. 创建项目级别�?.ai 提示词文�?
在项目根目录创建 `.ai/prompts.md`�?
```markdown
# EST 项目 AI 提示�?
## 代码风格
- 使用 EST 零依赖设�?- 使用 @Inject 进行依赖注入
- 使用 EST Collection 处理集合
- 遵循 EST 代码规范

## 参�?- est-examples-basic/ 中的示例
- docs/ 中的文档
- est-modules/ 中的源代�?
## 常用导入
import ltd.idcu.est.core.container.api.Inject;
import ltd.idcu.est.collection.api.Seqs;
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
```

### 3. 分步骤生成代�?
不要�?AI 一次性生成整个应用：

```
第一步：生成 User 实体�?第二步：生成 UserRepository
第三步：生成 UserService
第四步：生成 UserController
```

### 4. 验证和测�?
AI 生成代码后：
1. 审查代码是否符合 EST 风格
2. 编译检�?3. 运行测试
4. 人工审查关键逻辑

---

## 完整集成示例

```java
import ltd.idcu.est.features.ai.api.AiAssistant;
import ltd.idcu.est.features.ai.impl.DefaultAiAssistant;

public class CompleteAiIntegrationExample {
    private final AiAssistant estAi;
    
    public CompleteAiIntegrationExample() {
        this.estAi = new DefaultAiAssistant();
    }
    
    public void generateCompleteModule(String entityName) {
        System.out.println("=== 生成 " + entityName + " 模块 ===\n");
        
        // 1. 生成 Entity
        System.out.println("--- 1. 生成 Entity ---");
        String entityPrompt = "创建一�?" + entityName + " 实体�?;
        String entityCode = estAi.suggestCode(entityPrompt);
        System.out.println(entityCode);
        
        // 2. 生成 Repository
        System.out.println("\n--- 2. 生成 Repository ---");
        String repoPrompt = "创建 " + entityName + "Repository 接口";
        String repoCode = estAi.suggestCode(repoPrompt);
        System.out.println(repoCode);
        
        // 3. 生成 Service
        System.out.println("\n--- 3. 生成 Service ---");
        String servicePrompt = "创建 " + entityName + "Service，使用依赖注�?;
        String serviceCode = estAi.suggestCode(servicePrompt);
        System.out.println(serviceCode);
        
        // 4. 生成 Controller
        System.out.println("\n--- 4. 生成 Controller ---");
        String controllerPrompt = "创建 " + entityName + "Controller，使�?EST Web";
        String controllerCode = estAi.suggestCode(controllerPrompt);
        System.out.println(controllerCode);
        
        System.out.println("\n�?模块生成完成�?);
        System.out.println("\n下一步：");
        System.out.println("1. 将代码复制到 IDE");
        System.out.println("2. �?AI 工具（Copilot/Cursor）进行微�?);
        System.out.println("3. 编译和测�?);
    }
    
    public static void main(String[] args) {
        CompleteAiIntegrationExample example = new CompleteAiIntegrationExample();
        example.generateCompleteModule("Product");
    }
}
```

---

**下一�?*: 阅读 [最佳实践](best-practices.md)
