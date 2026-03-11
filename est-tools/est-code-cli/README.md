# EST Code CLI - AI 驱动的命令行工具

EST Code CLI 是一个 AI 驱动的命令行工具，为 EST 框架开发者提供智能化的开发辅助。

## 特性

- 🤖 **AI 对话** - 自然语言交互，智能回答问题
- 📝 **项目初始化** - 一键初始化工作区，生成 EST.md 项目合约
- 🏗️ **项目脚手架** - 快速创建 EST 项目
- 💻 **代码生成** - 智能生成符合 EST 规范的代码
- 🔍 **代码分析** - 分析项目结构和代码
- 📋 **合约管理** - EST.md 项目合约文档支持
- 🔧 **MCP 工具** - 内置多种 MCP 工具，直接调用
- 📁 **文件操作** - 读写文件、列出目录
- 🚀 **EST 技能** - 5个专业技能（代码审查、重构、架构、性能优化、安全审计）
- 🔍 **智能技能联动** - 自动检测并推荐适用技能
- ⚙️ **配置管理** - 完整的配置持久化支持
- 🌐 **Web 界面** - 美观的浏览器操作界面
- 🤖 **AI Agent** - 集成多步推理的智能体系统
- 🛠️ **Agent 技能** - 代码生成、解释、优化、Bug 修复、文档生成
- 🧠 **记忆系统** - 支持上下文记忆的多轮对话

## 快速开始

### 1. 构建

```bash
cd est-tools/est-code-cli
mvn clean package
```

### 2. 配置

复制示例配置文件：

```bash
cp src/main/resources/est-code-cli.yml.example est-code-cli.yml
```

编辑 `est-code-cli.yml`，配置你的 LLM API：

```yaml
solon:
  code:
    cli:
      nickname: "EST"
      workDir: "."
      planningMode: true
      hitlEnabled: true
      chatModel:
        apiUrl: "https://api.deepseek.com/v1/chat/completions"
        apiKey: "sk-your-api-key"
        model: "deepseek-chat"
```

### 3. 运行

```bash
java -jar target/est-code-cli-2.3.0-SNAPSHOT.jar
```

或者使用 Maven 直接运行：

```bash
mvn exec:java -Dexec.mainClass="ltd.idcu.est.codecli.EstCodeCliMain"
```

## 使用指南

### 启动界面

```
EST v1.0.0
/path/to/your/project

Type 'help' for available commands.

> 
```

### 可用命令

| 命令 | 说明 |
|------|------|
| `help` | 显示帮助信息 |
| `init` | 初始化工作区，创建 EST.md |
| `contract` | 显示当前项目合约 |
| `tools` | 列出所有可用的 MCP 工具 |
| `skills` | 列出所有可用的 EST 技能 |
| `templates` | 列出所有可用的提示模板 |
| `history` | 显示命令历史记录 |
| `test` | 运行 Maven 测试 |
| `compile` | 运行 Maven 编译 |
| `config` | 管理配置 |
| `/<tool>` | 直接调用 MCP 工具（如 `/list_dir`） |
| `web` | 启动 Web 模式（浏览器界面） |
| `acp` | 启动 ACP 协议服务（IDE 集成） |
| `agent` | Agent 相关命令（键入 `agent` 查看详情） |
| `exit` / `quit` | 退出程序 |

### 基本使用示例

#### 1. 初始化工作区

```
> init
Re-initializing workspace...
Workspace initialized successfully!
Project: my-project
Type: Maven
EST.md created.
```

#### 2. 查看项目合约

```
> contract
# EST Project Contract

## Project Name
my-project

## Project Type
Maven

## Framework Version
2.3.0-SNAPSHOT

## Build Commands
- mvn clean compile
- mvn clean package

## Test Commands
- mvn test

...
```

#### 3. 查看可用工具

```
> tools

Available tools:

  - est_read_file
    Reads the content of a file from the workspace

  - est_write_file
    Writes content to a file in the workspace

  - est_list_dir
    Lists the contents of a directory in the workspace

  - est_scaffold
    Creates a new EST project using scaffold templates

  - est_codegen
    Generates code (Entity, Controller, Service, Mapper) for EST framework
```

#### 4. 直接调用工具

列出目录内容：
```
> /list_dir {"path":"."}

Calling tool: est_list_dir

Contents of .:

[DIR]  est-app
[DIR]  est-base
[DIR]  est-core
[FILE] pom.xml
...

Metadata: {path=., entries=[...]}
```

读取文件：
```
> /read_file {"path":"README.md"}

Calling tool: est_read_file

# EST Framework 2.0
...

Metadata: {path=README.md, size=1234}
```

创建项目：
```
> /est_scaffold {"projectType":"web","projectName":"my-web-app"}

Calling tool: est_scaffold

Creating EST project:
  Type: web
  Name: my-web-app
  GroupId: com.example
  Package: com.example.mywebapp
  Java Version: 21
  Git Init: false

Note: Full scaffold integration coming soon!
For now, use the est-scaffold module directly.
```

#### 5. 项目索引和搜索

索引项目文件：
```
> /est_index_project {"path":"."}

Calling tool: est_index_project

Project indexed successfully!
Indexed files: 156
Included extensions: .java, .xml, .yml, .yaml, .json, .md, .txt, .properties
```

搜索项目文件：
```
> /est_search {"query":"McpTool","limit":5}

Calling tool: est_search

Search results for: McpTool

[1] d:\os\proj\java\est2.0\est-tools\est-code-cli\src\main\java\ltd\idcu\est\codecli\mcp\ReadFileMcpTool.java (score: 3)
    ...import ltd.idcu.est.ai.api.mcp.McpTool;...

[2] d:\os\proj\java\est2.0\est-tools\est-code-cli\src\main\java\ltd\idcu\est\codecli\mcp\WriteFileMcpTool.java (score: 3)
    ...import ltd.idcu.est.ai.api.mcp.McpTool;...

Metadata: {query=McpTool, totalResults=8, returnedResults=5}
```

#### 6. Web 模式

启动 Web 服务器：
```
> web

Starting EST Code CLI Web Server on http://localhost:8080...
Press Ctrl+C to stop the server
```

然后在浏览器中打开 http://localhost:8080 即可使用 Web 界面。

#### 7. ACP 协议（IDE 集成）

启动 ACP 服务器：
```
> acp

Starting EST Code CLI ACP Server on port 3000...
Press Ctrl+C to stop the server
```

ACP 服务器可以与支持 ACP 协议的 IDE 进行集成。

#### 8. 查看可用技能

```
> skills

Available EST Skills:

  - code_review
    审查代码质量，提供改进建议

  - refactor
    提供代码重构和优化建议

  - architecture
    分析项目架构，提供架构设计建议

  - performance_optimization
    分析和优化代码性能，提供性能改进建议

  - security_audit
    审计代码安全性，识别安全漏洞和风险
```

#### 9. 查看提示模板

```
> templates

Available Prompt Templates:

  - est_code_generator
    EST 代码生成提示模板

  - est_bug_fixer
    EST Bug 修复提示模板

  - est_test_generator
    EST 测试生成提示模板

  - est_documentation
    EST 文档生成提示模板

  - est_performance_optimization
    EST 性能优化提示模板

  - est_security_audit
    EST 安全审计提示模板
```

#### 10. 查看命令历史

```
> history

Command History:
===============
  1. help
  2. init
  3. tools
  4. skills

Total: 4 commands
```

#### 11. 运行测试和编译

运行测试：
```
> test

Running test...

Test Results:
=============

Test: Maven Test Suite
Status: PASSED
Duration: 12345ms

...

Summary:
  Passed: 1
  Failed: 0
  Total: 1
  Total Duration: 12345ms
```

运行编译：
```
> compile

Running compile...

Test Results:
=============

Test: Maven Compile
Status: PASSED
Duration: 5678ms

...
```

#### 12. 配置管理

```
> config

Current Configuration:
========================================
  Nickname:     EST
  Work Dir:     d:\os\proj\java\est2.0
  Planning Mode: true
  HITL Enabled: true
========================================

Options:
  1. Set Nickname
  2. Toggle Planning Mode
  3. Toggle HITL Security
  4. Save Configuration
  5. Save to User Config
  0. Back

Choose an option: 1
Enter new nickname: MyEST
Nickname updated to: MyEST
```

#### 13. Web 模式

```
> web

Starting EST Code CLI Web Server...
Enter port (default 8080): 

EST Code CLI Web Server started on http://localhost:8080
Press Ctrl+C to stop the server
```

然后在浏览器中打开 http://localhost:8080 即可使用 Web 界面，包含 5 个功能标签页：
- AI 对话
- EST 技能
- MCP 工具
- 项目搜索
- 配置

#### 14. ACP 协议（IDE 集成）

```
> acp

Starting EST Code CLI ACP Server...
Enter port (default 3000): 

EST Code CLI ACP Server started on port 3000
Press Ctrl+C to stop the server
```

ACP 服务器可以与支持 ACP 协议的 IDE 进行集成。

#### 15. 技能智能联动

当你输入相关问题时，系统会自动检测并推荐适用的技能：

```
> 帮我审查一下这个代码的性能问题

EST

💡 检测到您可能需要使用技能: performance_optimization
   分析和优化代码性能，提供性能改进建议

是否要使用该技能？(y/n，或直接继续对话)
> y

使用技能: performance_optimization
==================================================

请提供要处理的代码文件路径，或直接粘贴代码：
> src/main/java/Example.java
已读取文件: d:\os\proj\java\est2.0\src\main\java\Example.java

正在处理...

[AI 给出的性能优化分析结果...]
```

#### 16. 与 AI 对话

直接输入你的问题或需求：

```
> 帮我创建一个 EST Web 项目

EST

好的！我可以帮你创建一个 EST Web 项目。你可以使用 `est new` 命令，或者我来指导你完成...
```

## EST.md 项目合约

EST.md 是项目的合约文档，记录了项目的关键信息：

- 项目名称和类型
- 构建和测试命令
- 环境变量
- 编码规范
- 重要文件列表

这个文档不仅是给 AI 看的，也是给团队成员看的，成为项目的一份活文档。

## 配置说明

### 基础配置

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `nickname` | AI 助手昵称 | "EST" |
| `workDir` | 工作目录 | 当前目录 |
| `planningMode` | 是否启用规划模式 | true |
| `hitlEnabled` | 是否启用人工确认 | true |

### LLM 配置

| 配置项 | 说明 |
|--------|------|
| `chatModel.apiUrl` | LLM API 地址 |
| `chatModel.apiKey` | API 密钥 |
| `chatModel.model` | 模型名称 |

### 支持的 LLM 提供商

EST Code CLI 支持多种 LLM 提供商（通过 EST AI Suite）：

- OpenAI (GPT-4, GPT-3.5)
- 智谱 AI (GLM-4)
- 通义千问 (Qwen)
- 文心一言 (Ernie)
- 豆包 (Doubao)
- Kimi (Moonshot)
- DeepSeek
- Anthropic (Claude)
- Gemini
- Mistral
- Ollama (本地模型)

## 项目结构

```
est-code-cli/
├── src/main/java/ltd/idcu/est/codecli/
│   ├── EstCodeCliMain.java              # 主入口类
│   ├── CliInteractionHandler.java       # 交互处理器
│   ├── config/
│   │   └── CliConfig.java               # 配置管理
│   ├── contract/
│   │   ├── ProjectContract.java         # 项目合约模型
│   │   └── ContractManager.java         # 合约管理器
│   ├── mcp/
│   │   ├── EstCodeCliMcpServer.java     # MCP 服务器
│   │   ├── ReadFileMcpTool.java         # 读文件工具
│   │   ├── WriteFileMcpTool.java        # 写文件工具
│   │   ├── ListDirMcpTool.java          # 列目录工具
│   │   ├── ScaffoldMcpTool.java         # 脚手架工具
│   │   ├── CodeGenMcpTool.java          # 代码生成工具
│   │   ├── SearchMcpTool.java           # 搜索工具
│   │   ├── IndexProjectMcpTool.java     # 项目索引工具
│   │   ├── ListSkillsMcpTool.java       # 技能列表工具
│   │   ├── ListTemplatesMcpTool.java    # 模板列表工具
│   │   └── RunTestsMcpTool.java         # 测试运行工具
│   ├── search/
│   │   ├── FileIndex.java               # 文件索引（倒排索引）
│   │   └── ProjectIndexer.java          # 项目索引器
│   ├── security/
│   │   ├── HitlSecurityPolicy.java      # HITL 安全策略
│   │   └── ApprovalManager.java         # 审批管理器
│   ├── skills/
│   │   ├── EstSkill.java                # 技能接口
│   │   ├── SkillManager.java            # 技能管理器
│   │   ├── CodeReviewSkill.java         # 代码审查技能
│   │   ├── RefactorSkill.java           # 重构技能
│   │   ├── ArchitectureSkill.java       # 架构分析技能
│   │   ├── PerformanceOptimizationSkill.java # 性能优化技能
│   │   └── SecurityAuditSkill.java      # 安全审计技能
│   ├── prompts/
│   │   ├── PromptTemplate.java          # 提示模板
│   │   └── PromptLibrary.java           # 提示模板库
│   ├── testing/
│   │   └── TestRunner.java              # 测试运行器
│   ├── ux/
│   │   └── CommandHistory.java          # 命令历史记录
│   ├── web/
│   │   └── EstWebServer.java            # Web 服务器
│   ├── acp/
│   │   └── AcpServer.java               # ACP 协议服务器
│   └── agent/
│       ├── AgentManager.java            # Agent 管理器（单例）
│       ├── AgentCommandHandler.java     # Agent 命令处理器
│       └── skill/
│           ├── CodeGenerationSkill.java # 代码生成技能
│           ├── CodeExplanationSkill.java # 代码解释技能
│           ├── CodeOptimizationSkill.java # 代码优化技能
│           ├── BugFixSkill.java          # Bug 修复技能
│           └── DocumentationSkill.java   # 文档生成技能
├── src/main/resources/
│   ├── est-code-cli.yml.example         # 示例配置
│   └── web/
│       ├── index.html                   # Web界面首页
│       ├── style.css                    # Web界面样式
│       └── script.js                    # Web界面交互
└── README.md
```

## 阶段一功能完成度

✅ 创建 est-code-cli 模块目录结构和 pom.xml  
✅ 创建 EST.md 项目合约文档相关类  
✅ 创建命令行交互处理器  
✅ 创建 EstCodeCliMain 主入口类  
✅ 整合 AiAssistant 实现对话功能  
✅ 添加 README.md 文档和示例配置  

## 阶段二功能完成度

✅ 创建文件读写 MCP 工具（ReadFile、WriteFile、ListDir）  
✅ 封装 est-scaffold 为 MCP 工具  
✅ 封装 est-codegen 为 MCP 工具  
✅ 创建工具注册和管理系统（EstCodeCliMcpServer）  
✅ 更新交互处理器支持工具调用  
✅ 更新 README 文档  

## 阶段三功能完成度

✅ 实现项目索引和纯 Java 搜索（倒排索引）  
✅ 创建搜索 MCP 工具（est_search、est_index_project）  
✅ 添加 HITL 安全策略（HitlSecurityPolicy、ApprovalManager）  
✅ 支持 Web 模式（浏览器界面 - EstWebServer）  
✅ 支持 ACP 协议（IDE 集成 - AcpServer）  
✅ 集成所有新功能到现有代码  
✅ 更新 README 文档  

## 阶段四功能完成度

✅ 添加 EST 专属技能（代码审查、重构建议、架构分析）  
✅ 创建技能管理器（SkillManager）  
✅ 完善提示模板库（6个专业提示模板）  
✅ 添加测试验证能力（TestRunner）  
✅ 优化用户体验（CommandHistory）  
✅ 创建新 MCP 工具（ListSkills、ListTemplates、RunTests）  
✅ 集成技能和提示模板到交互处理器  
✅ 新增命令（skills、templates、history、test、compile）  
✅ 更新项目结构文档  
✅ 更新 README 文档（包含所有新功能示例）  

## 阶段五功能完成度

✅ 添加 2 个新技能（性能优化、安全审计）  
✅ 配置持久化功能（支持本地和用户配置保存）  
✅ 技能与 AI 对话智能联动（自动检测和推荐技能）  
✅ 完善 Web 界面（5个功能标签页、美观UI）  
✅ 新增 config 命令支持配置管理  
✅ 更新帮助信息  

## 阶段六功能完成度

✅ 集成 est-agent 模块（AI Agent 智能体系统）  
✅ 创建 Agent 管理器（AgentManager 单例）  
✅ 创建 5 个 Agent 专属技能（代码生成、解释、优化、Bug 修复、文档生成）  
✅ 创建 Agent 命令处理器（AgentCommandHandler）  
✅ 集成 Agent 命令到 CliInteractionHandler  
✅ 更新项目结构文档  
✅ 更新 README 文档  

## 后续计划

### 阶段七：未来增强
- [ ] Web 界面后端 API 集成
- [ ] ACP 协议深度集成
- [ ] 更多高级技能
- [ ] 插件系统支持

## AI Agent 功能使用指南

EST Code CLI 集成了强大的 AI Agent 系统，支持多步推理和专业技能。

### Agent 命令列表

| 命令 | 说明 |
|------|------|
| `agent` | 显示 Agent 帮助信息 |
| `agent skills` | 列出所有可用的 Agent 技能 |
| `agent clear` | 清除 Agent 的记忆 |
| `agent generate <desc>` | 生成代码（支持 entity/controller/service/mapper） |
| `agent explain <file>` | 解释代码文件的功能 |
| `agent optimize <file>` | 分析并提供代码优化建议 |
| `agent bugfix <file>` | 分析并修复代码中的 Bug |
| `agent document <file>` | 为代码生成文档（javadoc/readme） |
| `agent <query>` | 向 Agent 提问（使用多步推理） |

### Agent 技能说明

EST Agent 内置了 7 个专业技能：

1. **web_search** - 网络搜索技能
2. **calculator** - 计算器技能
3. **code_generation** - 代码生成技能
4. **code_explanation** - 代码解释技能
5. **code_optimization** - 代码优化技能
6. **bug_fix** - Bug 修复技能
7. **documentation** - 文档生成技能

### 使用示例

#### 1. 查看 Agent 帮助

```
> agent

=== EST Agent Commands ===

agent skills          - List all available agent skills
agent clear           - Clear agent memory

agent generate <desc> - Generate code (type: entity/controller/service/mapper)
agent explain <file>  - Explain code from file
agent optimize <file> - Analyze and suggest code optimizations
agent bugfix <file>   - Analyze and fix bugs in code
agent document <file> - Generate documentation for code

agent <query>         - Ask agent a question (uses multi-step reasoning)
```

#### 2. 查看可用技能

```
> agent skills

=== Available Agent Skills ===

  - web_search
    网络搜索技能

  - calculator
    计算器技能

  - code_generation
    根据描述生成符合 EST 规范的代码

  - code_explanation
    解释代码的功能和结构

  - code_optimization
    提供代码优化建议

  - bug_fix
    分析和修复代码中的 Bug

  - documentation
    为代码生成文档

Total: 7 skills
```

#### 3. 代码生成

```
> agent generate user entity

=== Code Generation ===

Enter code type (entity/controller/service/mapper) [entity]: entity

Generating entity code...

Generated Code:
===============
package com.example.entity;

import ltd.idcu.est.data.annotation.Entity;
import ltd.idcu.est.data.annotation.Id;
import ltd.idcu.est.data.annotation.Column;

@Entity
public class User {
    @Id
    private Long id;

    @Column
    private String name;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
```

#### 4. 代码解释

```
> agent explain src/main/java/com/example/entity/User.java

=== Code Explanation ===

Analyzing file: src/main/java/com/example/entity/User.java

=== 代码分析 ===

📦 这是一个 EST 实体类（Entity）
   - 使用 @Entity 注解标记
   - 通常对应数据库表

📊 代码统计:
   - 总行数: 25
   - 包含 import 语句: true
   - 包含注释: false
```

#### 5. 代码优化

```
> agent optimize src/main/java/Example.java

=== Code Optimization ===

Analyzing file: src/main/java/Example.java

=== 代码优化建议 ===

💡 建议 1: 使用日志框架代替 System.out.println
   - 考虑使用 est-logging 模块
   - 支持不同日志级别（DEBUG, INFO, WARN, ERROR）

💡 建议 2: 考虑指定初始容量
   - new ArrayList<>(100) 比 new ArrayList() 更高效
```

#### 6. Bug 修复

```
> agent bugfix src/main/java/Buggy.java

=== Bug Fix Analysis ===

Enter file path: src/main/java/Buggy.java
Enter error message (optional): NullPointerException

Analyzing file: src/main/java/Buggy.java

=== Bug 分析报告 ===

📋 错误信息:
   NullPointerException

🔍 可能问题: NullPointerException
💡 建议:
   - 添加 null 检查
   - 使用 Optional 类型
   - 初始化对象
```

#### 7. 文档生成

```
> agent document src/main/java/com/example/entity/User.java

=== Documentation Generation ===

Enter file path: src/main/java/com/example/entity/User.java
Enter document type (javadoc/readme) [javadoc]: javadoc

Generating documentation for: src/main/java/com/example/entity/User.java

Generated Documentation:
======================
/**
 * User 类
 *
 * 功能描述: [在此添加类的功能描述]
 *
 * @author EST Code CLI
 * @version 1.0.0
 * @since 2026-03-11
 */
```

#### 8. 多步推理查询

```
> agent 计算 100 的平方根，然后搜索北京的天气

=== Agent Processing ===

Query: 计算 100 的平方根，然后搜索北京的天气

Processing... (this may take a moment)

=== Execution Steps ===

Step 1: use_calculator
  计算结果: 10.0

Step 2: use_web_search
  搜索结果: 北京天气: 晴朗，15°C

=== Final Answer ===
100 的平方根是 10.0。北京今天天气晴朗，气温 15°C。
```

#### 9. 清除记忆

```
> agent clear
Agent memory cleared.
```

## 相关资源

- [EST AI Suite](../est-ai-suite/README.md) - AI 套件文档
- [EST Agent](../est-ai-suite/est-agent/README.md) - Agent 模块文档
- [EST RAG](../est-ai-suite/est-rag/README.md) - RAG 模块文档
- [EST MCP](../est-ai-suite/est-mcp/README.md) - MCP 模块文档
- [EST Scaffold](../est-scaffold/README.md) - 脚手架工具
- [EST CodeGen](../est-codegen/README.md) - 代码生成工具
- [EST Framework](../../README.md) - 框架主文档

## 许可证

MIT License
