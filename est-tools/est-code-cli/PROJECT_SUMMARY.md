# EST Code CLI 项目完成总结

## 项目概述

EST Code CLI 是一个 AI 驱动的命令行工具，为 EST 框架开发者提供智能化的开发辅助。

## 完成的阶段

### 阶段一：基础功能 ✅

**目标：** 建立基础 CLI 框架和 AI 对话能力

**完成内容：**
- ✅ 创建 est-code-cli 模块目录结构和 pom.xml
- ✅ 创建 EST.md 项目合约文档相关类
- ✅ 创建命令行交互处理器
- ✅ 创建 EstCodeCliMain 主入口类
- ✅ 整合 AiAssistant 实现对话功能
- ✅ 添加 README.md 文档和示例配置

**创建的文件：**
- `pom.xml` - Maven 配置
- `config/CliConfig.java` - 配置管理
- `contract/ProjectContract.java` - 项目合约模型
- `contract/ContractManager.java` - 合约管理器
- `CliInteractionHandler.java` - 交互处理器
- `EstCodeCliMain.java` - 主入口类
- `src/main/resources/est-code-cli.yml.example` - 示例配置

---

### 阶段二：工具集成 ✅

**目标：** 集成 MCP 工具和文件操作能力

**完成内容：**
- ✅ 创建文件读写 MCP 工具（ReadFile、WriteFile、ListDir）
- ✅ 封装 est-scaffold 为 MCP 工具
- ✅ 封装 est-codegen 为 MCP 工具
- ✅ 创建工具注册和管理系统（EstCodeCliMcpServer）
- ✅ 更新交互处理器支持工具调用
- ✅ 更新 README 文档

**创建的文件：**
- `mcp/EstCodeCliMcpServer.java` - MCP 服务器
- `mcp/ReadFileMcpTool.java` - 读文件工具
- `mcp/WriteFileMcpTool.java` - 写文件工具
- `mcp/ListDirMcpTool.java` - 列目录工具
- `mcp/ScaffoldMcpTool.java` - 脚手架工具
- `mcp/CodeGenMcpTool.java` - 代码生成工具

---

### 阶段三：高级功能 ✅

**目标：** 添加搜索、HITL、Web 模式和 ACP 协议

**完成内容：**
- ✅ 实现项目索引和纯 Java 搜索（倒排索引）
- ✅ 创建搜索 MCP 工具（est_search、est_index_project）
- ✅ 添加 HITL 安全策略（HitlSecurityPolicy、ApprovalManager）
- ✅ 支持 Web 模式（浏览器界面 - EstWebServer）
- ✅ 支持 ACP 协议（IDE 集成 - AcpServer）
- ✅ 集成所有新功能到现有代码
- ✅ 更新 README 文档

**创建的文件：**
- `search/FileIndex.java` - 文件索引（倒排索引）
- `search/ProjectIndexer.java` - 项目索引器
- `mcp/SearchMcpTool.java` - 搜索工具
- `mcp/IndexProjectMcpTool.java` - 项目索引工具
- `security/HitlSecurityPolicy.java` - HITL 安全策略
- `security/ApprovalManager.java` - 审批管理器
- `web/EstWebServer.java` - Web 服务器
- `acp/AcpServer.java` - ACP 协议服务器

---

### 阶段四：完善生态 ✅

**目标：** 添加技能、提示模板、测试能力和用户体验优化

**完成内容：**
- ✅ 添加 EST 专属技能（代码审查、重构建议、架构分析）
- ✅ 创建技能管理器（SkillManager）
- ✅ 完善提示模板库（6个专业提示模板）
- ✅ 添加测试验证能力（TestRunner）
- ✅ 优化用户体验（CommandHistory）
- ✅ 创建新 MCP 工具（ListSkills、ListTemplates、RunTests）
- ✅ 集成技能和提示模板到交互处理器
- ✅ 新增命令（skills、templates、history、test、compile）
- ✅ 更新项目结构文档
- ✅ 更新 README 文档（包含所有新功能示例）

**创建的文件：**
- `skills/EstSkill.java` - 技能接口
- `skills/SkillManager.java` - 技能管理器
- `skills/CodeReviewSkill.java` - 代码审查技能
- `skills/RefactorSkill.java` - 重构技能
- `skills/ArchitectureSkill.java` - 架构分析技能
- `prompts/PromptTemplate.java` - 提示模板
- `prompts/PromptLibrary.java` - 提示模板库
- `testing/TestRunner.java` - 测试运行器
- `ux/CommandHistory.java` - 命令历史记录
- `mcp/ListSkillsMcpTool.java` - 技能列表工具
- `mcp/ListTemplatesMcpTool.java` - 模板列表工具
- `mcp/RunTestsMcpTool.java` - 测试运行工具

---

## 核心功能

### 1. AI 对话
- 自然语言交互
- 智能回答问题
- 与 EST AI Suite 集成

### 2. 项目合约（EST.md）
- 项目信息管理
- 构建和测试命令记录
- 环境变量配置
- 编码规范文档

### 3. MCP 工具
#### 文件操作
- `est_read_file` - 读取文件
- `est_write_file` - 写入文件
- `est_list_dir` - 列出目录

#### 开发工具
- `est_scaffold` - 项目脚手架
- `est_codegen` - 代码生成

#### 搜索工具
- `est_index_project` - 项目索引
- `est_search` - 文件搜索

#### 管理工具
- `est_list_skills` - 列出技能
- `est_list_templates` - 列出提示模板
- `est_run_tests` - 运行测试

### EST 技能
- `code_review` - 代码审查
- `refactor` - 重构建议
- `architecture` - 架构分析
- `performance_optimization` - 性能优化
- `security_audit` - 安全审计

### 5. 提示模板库
- `est_code_generator` - 代码生成
- `est_bug_fixer` - Bug 修复
- `est_test_generator` - 测试生成
- `est_documentation` - 文档生成
- `est_performance_optimization` - 性能优化
- `est_security_audit` - 安全审计

### 6. 高级功能
- 项目搜索（倒排索引）
- HITL 安全策略
- Web 模式（浏览器界面）
- ACP 协议（IDE 集成）
- 命令历史记录
- 测试运行器

---

## 命令列表

| 命令 | 说明 |
|------|------|
| `help` | 显示帮助信息 |
| `init` | 初始化工作区，创建 EST.md |
| `contract` | 显示当前项目合约 |
| `tools` | 列出所有可用的 MCP 工具 |
| `skills` | 列出所有可用的 EST 技能 |
| `templates` | 列出所有可用的提示模板 |
| `history` | 显示命令历史记录 |
| `context` | 显示对话上下文 |
| `clear` / `reset` | 清除对话上下文 |
| `test` | 运行 Maven 测试 |
| `compile` | 运行 Maven 编译 |
| `config` | 管理配置 |
| `web` | 启动 Web 服务器（浏览器界面） |
| `acp` | 启动 ACP 服务器（IDE 集成） |
| `/<tool>` | 直接调用 MCP 工具 |
| `exit` / `quit` | 退出程序 |

---

## 技术特点

### 零依赖原则
- 纯 Java 实现，无外部依赖
- 使用 EST 自有工具库
- 符合 EST 框架设计理念

### 模块化设计
- 清晰的包结构
- 职责分离
- 易于扩展

### AI 原生
- 深度集成 EST AI Suite
- 技能系统
- 提示模板库
- MCP 协议支持

---

## 项目统计

### 文件数量
- **阶段一**: 7 个文件
- **阶段二**: 6 个文件
- **阶段三**: 8 个文件
- **阶段四**: 12 个文件
- **阶段五**: 7 个文件（2个Java + 3个Web资源 + 2个更新）
- **阶段六**: 8 个文件（3个Java + 5个测试类）
- **总计**: 43 个 Java 文件

### 功能模块
- **配置管理**: 1 个类
- **合约管理**: 2 个类
- **MCP 工具**: 10 个工具
- **搜索**: 2 个类
- **安全**: 2 个类
- **技能**: 7 个类
- **提示**: 2 个类
- **测试**: 1 个类
- **UX**: 1 个类
- **Web**: 2 个类
- **ACP**: 1 个类
- **上下文**: 1 个类
- **核心**: 2 个类

---

### 阶段五：持续优化 ✅

**目标**: 完善系统功能和用户体验

**完成内容**:
- ✅ 添加 2 个新技能（性能优化、安全审计）
- ✅ 配置持久化功能（支持本地和用户配置保存）
- ✅ 技能与 AI 对话智能联动（自动检测和推荐技能）
- ✅ 完善 Web 界面（5个功能标签页、美观UI）
- ✅ 新增 config 命令支持配置管理
- ✅ 更新帮助信息

**创建的文件**:
- `skills/PerformanceOptimizationSkill.java` - 性能优化技能
- `skills/SecurityAuditSkill.java` - 安全审计技能
- `resources/web/index.html` - Web界面首页
- `resources/web/style.css` - Web界面样式
- `resources/web/script.js` - Web界面交互

**更新的文件**:
- `skills/SkillManager.java` - 注册新技能
- `config/CliConfig.java` - 添加配置保存功能
- `CliInteractionHandler.java` - 技能联动和 config 命令

---

### 阶段六：核心功能完善 ✅

**目标**: 解决高优先级问题，完善核心功能

**完成内容**:
- ✅ Web 后端 REST API 框架搭建（9个完整API端点）
- ✅ 实现聊天 API 端点
- ✅ 实现工具调用 API 端点
- ✅ 实现技能调用 API 端点
- ✅ 实现搜索 API 端点
- ✅ 实现配置 API 端点
- ✅ ACP 协议集成真实 MCP 工具
- ✅ ACP 协议实现工具调用功能
- ✅ 单元测试 - 核心类测试（5个测试类）
- ✅ 技能智能增强 - 对话上下文管理
- ✅ 技能智能增强 - 多轮对话支持

**创建的文件**:
- `web/ApiResponse.java` - API响应类
- `web/EstWebServer.java` - 增强Web服务器（完整REST API）
- `context/ConversationContext.java` - 对话上下文管理
- `test/java/**/*.java` - 单元测试（5个测试类）

**更新的文件**:
- `acp/AcpServer.java` - 集成真实MCP工具，实现完整工具调用
- `CliInteractionHandler.java` - 集成对话上下文，添加clear/context命令
- `pom.xml` - 添加测试依赖

---

## 贡献指南

### 如何贡献
1. 选择任务：从路线图中选择感兴趣的任务
2. 创建 Issue：说明计划如何实现
3. Fork 仓库：创建开发分支
4. 提交 PR：遵循贡献规范

### 建议的贡献方向
- **新手友好**: 文档完善、示例代码、Bug 修复
- **中级**: 功能增强、测试编写、性能优化
- **高级**: 架构设计、核心模块开发、AI 功能

---

## 相关资源

- [EST AI Suite](../est-ai-suite/README.md) - AI 套件文档
- [EST Scaffold](../est-scaffold/README.md) - 脚手架工具
- [EST CodeGen](../est-codegen/README.md) - 代码生成工具
- [EST Framework](../../README.md) - 框架主文档

---

## 许可证

MIT License

---

---

### 阶段七：用户体验增强 ✅

**目标**: 解决中优先级问题，增强用户体验

**完成内容**:
- ✅ 配置管理增强 - 配置验证（ConfigValidator）
- ✅ 配置管理增强 - 配置导入/导出（CliConfig.import/export）
- ✅ 配置管理增强 - 配置版本管理（ConfigVersionManager）
- ✅ 配置管理增强 - 配置模板支持（ConfigTemplate）
- ✅ 搜索功能增强 - 模糊搜索（Levenshtein距离算法）
- ✅ 搜索功能增强 - 搜索历史（SearchHistory）
- ✅ 搜索功能增强 - 高级筛选（SearchFilter，含预设筛选器）
- ✅ 搜索功能增强 - 搜索高亮（highlightedSnippet）
- ✅ 文档完善 - 使用案例收集（usage-examples.md）
- ✅ 文档完善 - 最佳实践编写（best-practices.md）
- ✅ 文档完善 - FAQ 文档整理（faq.md）
- ✅ 项目总结更新

**创建的文件**:
- `config/ConfigValidator.java` - 配置验证器
- `config/ConfigVersionManager.java` - 配置版本管理器
- `config/ConfigTemplate.java` - 配置模板
- `search/SearchHistory.java` - 搜索历史管理
- `search/SearchFilter.java` - 搜索筛选器
- `docs/usage-examples.md` - 使用案例文档
- `docs/best-practices.md` - 最佳实践文档
- `docs/faq.md` - 常见问题文档

**更新的文件**:
- `config/CliConfig.java` - 添加验证、导入/导出、模板应用功能
- `search/FileIndex.java` - 添加模糊搜索、搜索历史、高亮功能
- `pom.xml` - 更新项目信息

---

## 核心功能

### 1. AI 对话
- 自然语言交互
- 智能回答问题
- 与 EST AI Suite 集成

### 2. 项目合约（EST.md）
- 项目信息管理
- 构建和测试命令记录
- 环境变量配置
- 编码规范文档

### 3. MCP 工具
#### 文件操作
- `est_read_file` - 读取文件
- `est_write_file` - 写入文件
- `est_list_dir` - 列出目录

#### 开发工具
- `est_scaffold` - 项目脚手架
- `est_codegen` - 代码生成

#### 搜索工具
- `est_index_project` - 项目索引
- `est_search` - 文件搜索

#### 管理工具
- `est_list_skills` - 列出技能
- `est_list_templates` - 列出提示模板
- `est_run_tests` - 运行测试

### 4. EST 技能
- `code_review` - 代码审查
- `refactor` - 重构建议
- `architecture` - 架构分析
- `performance_optimization` - 性能优化
- `security_audit` - 安全审计

### 5. 配置管理
- 配置验证
- 配置导入/导出
- 配置版本管理（备份/恢复）
- 配置模板（default/minimal/developer/secure）

### 6. 搜索功能
- 倒排索引搜索
- 模糊搜索（Levenshtein距离）
- 搜索历史管理
- 高级筛选（扩展名、路径、得分等）
- 搜索结果高亮
- 预设筛选器（javaFilesOnly、excludeTestFiles、sourceFilesOnly）

### 7. 提示模板库
- `est_code_generator` - 代码生成
- `est_bug_fixer` - Bug 修复
- `est_test_generator` - 测试生成
- `est_documentation` - 文档生成
- `est_performance_optimization` - 性能优化
- `est_security_audit` - 安全审计

### 8. 高级功能
- 项目搜索（倒排索引）
- HITL 安全策略
- Web 模式（浏览器界面 + REST API）
- ACP 协议（IDE 集成）
- 命令历史记录
- 测试运行器
- 对话上下文管理

---

## 命令列表

| 命令 | 说明 |
|------|------|
| `help` | 显示帮助信息 |
| `init` | 初始化工作区，创建 EST.md |
| `contract` | 显示当前项目合约 |
| `tools` | 列出所有可用的 MCP 工具 |
| `skills` | 列出所有可用的 EST 技能 |
| `templates` | 列出所有可用的提示模板 |
| `history` | 显示命令历史记录 |
| `context` | 显示对话上下文 |
| `clear` / `reset` | 清除对话上下文 |
| `test` | 运行 Maven 测试 |
| `compile` | 运行 Maven 编译 |
| `config` | 管理配置 |
| `web` | 启动 Web 服务器（浏览器界面） |
| `acp` | 启动 ACP 服务器（IDE 集成） |
| `/<tool>` | 直接调用 MCP 工具 |
| `exit` / `quit` | 退出程序 |

---

## 技术特点

### 零依赖原则
- 纯 Java 实现，无外部依赖
- 使用 EST 自有工具库
- 符合 EST 框架设计理念

### 模块化设计
- 清晰的包结构
- 职责分离
- 易于扩展

### AI 原生
- 深度集成 EST AI Suite
- 技能系统
- 提示模板库
- MCP 协议支持

---

## 项目统计

### 文件数量
- **阶段一**: 7 个文件
- **阶段二**: 6 个文件
- **阶段三**: 8 个文件
- **阶段四**: 12 个文件
- **阶段五**: 7 个文件（2个Java + 3个Web资源 + 2个更新）
- **阶段六**: 8 个文件（3个Java + 5个测试类）
- **阶段七**: 11 个文件（5个Java + 3个文档 + 3个更新）
- **总计**: 59 个 Java 文件 + 6 个文档文件

### 功能模块
- **配置管理**: 4 个类（CliConfig、ConfigValidator、ConfigVersionManager、ConfigTemplate）
- **合约管理**: 2 个类
- **MCP 工具**: 10 个工具
- **搜索**: 4 个类（FileIndex、ProjectIndexer、SearchHistory、SearchFilter）
- **安全**: 2 个类
- **技能**: 7 个类
- **提示**: 2 个类
- **测试**: 1 个类
- **UX**: 1 个类
- **Web**: 2 个类
- **ACP**: 1 个类
- **上下文**: 1 个类
- **核心**: 2 个类

---

## 文档资源

- [README.md](./README.md) - 项目主文档
- [ROADMAP.md](./ROADMAP.md) - 开发路线图
- [docs/usage-examples.md](./docs/usage-examples.md) - 使用案例
- [docs/best-practices.md](./docs/best-practices.md) - 最佳实践
- [docs/faq.md](./docs/faq.md) - 常见问题

---

## 贡献指南

### 如何贡献
1. 选择任务：从路线图中选择感兴趣的任务
2. 创建 Issue：说明计划如何实现
3. Fork 仓库：创建开发分支
4. 提交 PR：遵循贡献规范

### 建议的贡献方向
- **新手友好**: 文档完善、示例代码、Bug 修复
- **中级**: 功能增强、测试编写、性能优化
- **高级**: 架构设计、核心模块开发、AI 功能

---

## 相关资源

- [EST AI Suite](../est-ai-suite/README.md) - AI 套件文档
- [EST Scaffold](../est-scaffold/README.md) - 脚手架工具
- [EST CodeGen](../est-codegen/README.md) - 代码生成工具
- [EST Framework](../../README.md) - 框架主文档

---

---

### 阶段八：性能与生态 ✅

**目标**: 解决低优先级问题，建设生态

**完成内容**:
- ✅ 插件系统 - 插件架构设计（EstPlugin接口）
- ✅ 插件系统 - 插件加载机制（PluginManager，支持JAR和目录加载）
- ✅ 插件系统 - 插件 API 规范（PluginContext、BaseEstPlugin）
- ✅ 插件系统 - 示例插件开发（WelcomePlugin）
- ✅ 性能优化 - 缓存机制（LruCache，支持TTL）
- ✅ 性能优化 - 性能监控（PerformanceMonitor，统计分析）
- ✅ 国际化支持 - 多语言框架（I18nManager）
- ✅ 国际化支持 - 英文翻译（messages_en.properties）
- ✅ 国际化支持 - 中文翻译（messages_zh.properties）
- ✅ 国际化支持 - 语言切换功能（LanguageChangeListener）

**创建的文件**:
- `plugin/EstPlugin.java` - 插件接口定义
- `plugin/PluginContext.java` - 插件上下文接口
- `plugin/PluginException.java` - 插件异常类
- `plugin/PluginManager.java` - 插件管理器（加载/卸载/生命周期）
- `plugin/BaseEstPlugin.java` - 插件基类
- `plugin/examples/WelcomePlugin.java` - 示例欢迎插件
- `cache/LruCache.java` - LRU缓存实现
- `performance/PerformanceMonitor.java` - 性能监控器
- `i18n/I18nManager.java` - 国际化管理器
- `resources/messages_zh.properties` - 中文语言包
- `resources/messages_en.properties` - 英文语言包

**更新的文件**:
- `PROJECT_SUMMARY.md` - 更新项目总结

---

## 核心功能

### 1. AI 对话
- 自然语言交互
- 智能回答问题
- 与 EST AI Suite 集成

### 2. 项目合约（EST.md）
- 项目信息管理
- 构建和测试命令记录
- 环境变量配置
- 编码规范文档

### 3. MCP 工具
#### 文件操作
- `est_read_file` - 读取文件
- `est_write_file` - 写入文件
- `est_list_dir` - 列出目录

#### 开发工具
- `est_scaffold` - 项目脚手架
- `est_codegen` - 代码生成

#### 搜索工具
- `est_index_project` - 项目索引
- `est_search` - 文件搜索

#### 管理工具
- `est_list_skills` - 列出技能
- `est_list_templates` - 列出提示模板
- `est_run_tests` - 运行测试

### 4. EST 技能
- `code_review` - 代码审查
- `refactor` - 重构建议
- `architecture` - 架构分析
- `performance_optimization` - 性能优化
- `security_audit` - 安全审计

### 5. 配置管理
- 配置验证
- 配置导入/导出
- 配置版本管理（备份/恢复）
- 配置模板（default/minimal/developer/secure）

### 6. 搜索功能
- 倒排索引搜索
- 模糊搜索（Levenshtein距离）
- 搜索历史管理
- 高级筛选（扩展名、路径、得分等）
- 搜索结果高亮
- 预设筛选器（javaFilesOnly、excludeTestFiles、sourceFilesOnly）

### 7. 提示模板库
- `est_code_generator` - 代码生成
- `est_bug_fixer` - Bug 修复
- `est_test_generator` - 测试生成
- `est_documentation` - 文档生成
- `est_performance_optimization` - 性能优化
- `est_security_audit` - 安全审计

### 8. 插件系统
- 插件接口定义（EstPlugin）
- 插件生命周期管理
- JAR和目录插件加载
- 插件服务注册
- 插件上下文（配置、日志、服务）
- 示例插件（WelcomePlugin）

### 9. 性能优化
- LRU缓存（支持TTL过期）
- 性能监控（操作统计、耗时分析）
- 性能报告生成

### 10. 国际化支持
- 多语言框架（I18nManager）
- 中文/英文双语支持
- 语言切换监听
- 参数化消息格式化

### 11. 高级功能
- 项目搜索（倒排索引）
- HITL 安全策略
- Web 模式（浏览器界面 + REST API）
- ACP 协议（IDE 集成）
- 命令历史记录
- 测试运行器
- 对话上下文管理

---

## 命令列表

| 命令 | 说明 |
|------|------|
| `help` | 显示帮助信息 |
| `init` | 初始化工作区，创建 EST.md |
| `contract` | 显示当前项目合约 |
| `tools` | 列出所有可用的 MCP 工具 |
| `skills` | 列出所有可用的 EST 技能 |
| `templates` | 列出所有可用的提示模板 |
| `history` | 显示命令历史记录 |
| `context` | 显示对话上下文 |
| `clear` / `reset` | 清除对话上下文 |
| `test` | 运行 Maven 测试 |
| `compile` | 运行 Maven 编译 |
| `config` | 管理配置 |
| `web` | 启动 Web 服务器（浏览器界面） |
| `acp` | 启动 ACP 服务器（IDE 集成） |
| `plugins` | 管理插件 |
| `performance` | 显示性能监控 |
| `language` | 切换语言 |
| `/<tool>` | 直接调用 MCP 工具 |
| `exit` / `quit` | 退出程序 |

---

## 技术特点

### 零依赖原则
- 纯 Java 实现，无外部依赖
- 使用 EST 自有工具库
- 符合 EST 框架设计理念

### 模块化设计
- 清晰的包结构
- 职责分离
- 易于扩展

### AI 原生
- 深度集成 EST AI Suite
- 技能系统
- 提示模板库
- MCP 协议支持

### 插件化架构
- 标准插件接口
- 热加载支持
- 服务注册机制
- 沙箱安全模型

---

## 项目统计

### 文件数量
- **阶段一**: 7 个文件
- **阶段二**: 6 个文件
- **阶段三**: 8 个文件
- **阶段四**: 12 个文件
- **阶段五**: 7 个文件（2个Java + 3个Web资源 + 2个更新）
- **阶段六**: 8 个文件（3个Java + 5个测试类）
- **阶段七**: 11 个文件（5个Java + 3个文档 + 3个更新）
- **阶段八**: 11 个文件（9个Java + 2个资源文件）
- **总计**: 70 个 Java 文件 + 6 个文档文件 + 5 个资源文件

### 功能模块
- **配置管理**: 4 个类（CliConfig、ConfigValidator、ConfigVersionManager、ConfigTemplate）
- **合约管理**: 2 个类
- **MCP 工具**: 10 个工具
- **搜索**: 4 个类（FileIndex、ProjectIndexer、SearchHistory、SearchFilter）
- **安全**: 2 个类
- **技能**: 7 个类
- **提示**: 2 个类
- **测试**: 1 个类
- **UX**: 1 个类
- **Web**: 2 个类
- **ACP**: 1 个类
- **上下文**: 1 个类
- **插件**: 6 个类（EstPlugin、PluginContext、PluginManager等）
- **缓存**: 1 个类（LruCache）
- **性能**: 1 个类（PerformanceMonitor）
- **国际化**: 1 个类（I18nManager）
- **核心**: 2 个类

---

## 文档资源

- [README.md](./README.md) - 项目主文档
- [ROADMAP.md](./ROADMAP.md) - 开发路线图
- [docs/usage-examples.md](./docs/usage-examples.md) - 使用案例
- [docs/best-practices.md](./docs/best-practices.md) - 最佳实践
- [docs/faq.md](./docs/faq.md) - 常见问题

---

## 贡献指南

### 如何贡献
1. 选择任务：从路线图中选择感兴趣的任务
2. 创建 Issue：说明计划如何实现
3. Fork 仓库：创建开发分支
4. 提交 PR：遵循贡献规范

### 建议的贡献方向
- **新手友好**: 文档完善、示例代码、Bug 修复、翻译贡献
- **中级**: 功能增强、测试编写、性能优化、插件开发
- **高级**: 架构设计、核心模块开发、AI 功能

---

## 相关资源

- [EST AI Suite](../est-ai-suite/README.md) - AI 套件文档
- [EST Scaffold](../est-scaffold/README.md) - 脚手架工具
- [EST CodeGen](../est-codegen/README.md) - 代码生成工具
- [EST Framework](../../README.md) - 框架主文档

---

## 许可证

MIT License

---

**项目状态**: ✅ 阶段一至阶段八已完成
**最后更新**: 2026-03-09
**EST 版本**: 2.3.0-SNAPSHOT
