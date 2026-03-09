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
| `/<tool>` | 直接调用 MCP 工具（如 `/list_dir`） |
| `web` | 启动 Web 模式（浏览器界面） |
| `acp` | 启动 ACP 协议服务（IDE 集成） |
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

#### 12. 与 AI 对话

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
│   │   └── ArchitectureSkill.java       # 架构分析技能
│   ├── prompts/
│   │   ├── PromptTemplate.java          # 提示模板
│   │   └── PromptLibrary.java           # 提示模板库
│   ├── testing/
│   │   └── TestRunner.java              # 测试运行器
│   ├── ux/
│   │   └── CommandHistory.java          # 命令历史记录
│   ├── web/
│   │   └── EstWebServer.java            # Web 服务器
│   └── acp/
│       └── AcpServer.java               # ACP 协议服务器
├── src/main/resources/
│   └── est-code-cli.yml.example         # 示例配置
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

## 后续计划

### 阶段五：持续优化
- [ ] 添加更多技能（性能优化、安全审计等）
- [ ] 完善 Web 界面
- [ ] 深入 ACP 协议集成
- [ ] 添加配置持久化
- [ ] 技能与 AI 对话的智能联动

## 相关资源

- [EST AI Suite](../est-ai-suite/README.md) - AI 套件文档
- [EST Scaffold](../est-scaffold/README.md) - 脚手架工具
- [EST CodeGen](../est-codegen/README.md) - 代码生成工具
- [EST Framework](../../README.md) - 框架主文档

## 许可证

MIT License
