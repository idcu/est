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

### 阶段九：短期目标 - 测试与官方插件 ✅

**目标**: 提升测试覆盖率，开发官方插件，建设生态基础

**完成内容**:
- ✅ 提升测试覆盖率 - 配置管理模块测试（ConfigValidatorTest、ConfigTemplateTest）
- ✅ 提升测试覆盖率 - 搜索模块测试（SearchHistoryTest、SearchFilterTest）
- ✅ 提升测试覆盖率 - 插件模块测试（PluginManagerTest）
- ✅ 提升测试覆盖率 - 缓存模块测试（LruCacheTest）
- ✅ 提升测试覆盖率 - 国际化模块测试（I18nManagerTest）
- ✅ 开发官方插件 - Git 集成插件（GitPlugin，完整Git功能）
- ✅ 开发官方插件 - 数据库管理插件（DatabasePlugin，JDBC集成）
- ✅ 开发官方插件 - API 测试插件（ApiTestPlugin，REST API测试）
- ✅ 开发官方插件 - 日志分析插件（LogAnalysisPlugin，日志解析和统计）
- ✅ 开发官方插件 - 文件管理插件（FileManagerPlugin，文件操作）
- ✅ 创建全面评估文档（EST_Code_CLI_Comprehensive_Evaluation.md）

**创建的文件**:
- `test/java/config/ConfigValidatorTest.java` - 配置验证测试
- `test/java/config/ConfigTemplateTest.java` - 配置模板测试
- `test/java/search/SearchHistoryTest.java` - 搜索历史测试
- `test/java/search/SearchFilterTest.java` - 搜索筛选测试
- `test/java/plugin/PluginManagerTest.java` - 插件管理测试
- `test/java/cache/LruCacheTest.java` - LRU缓存测试
- `test/java/i18n/I18nManagerTest.java` - 国际化测试
- `plugin/official/GitPlugin.java` - Git集成官方插件
- `plugin/official/DatabasePlugin.java` - 数据库管理官方插件
- `plugin/official/ApiTestPlugin.java` - API测试官方插件
- `plugin/official/LogAnalysisPlugin.java` - 日志分析官方插件
- `plugin/official/FileManagerPlugin.java` - 文件管理官方插件
- `docs/EST_Code_CLI_Comprehensive_Evaluation.md` - 全面评估文档

**更新的文件**:
- `PROJECT_SUMMARY.md` - 更新项目总结

---

## 官方插件

### 1. Git 集成插件 (GitPlugin)
- Git 仓库检测
- 状态查看、提交、推送、拉取
- 分支管理（创建、切换、合并）
- 历史查看、差异比较
- 暂存功能、远程管理
- 克隆和初始化

### 2. 数据库管理插件 (DatabasePlugin)
- JDBC 连接管理
- SQL 查询执行
- 表结构查看
- 数据导出（CSV/JSON）
- 表列表获取

### 3. API 测试插件 (ApiTestPlugin)
- REST API 测试（GET/POST/PUT/DELETE）
- 请求/响应记录
- 性能测试（并发、响应时间统计）
- 断言验证
- 请求历史管理

### 4. 日志分析插件 (LogAnalysisPlugin)
- 日志格式解析
- 级别过滤（ERROR/WARN/INFO等）
- 时间范围过滤
- 关键词搜索
- 统计分析（级别统计、Top错误、小时统计）
- 日志尾部查看

### 5. 文件管理插件 (FileManagerPlugin)
- 文件浏览和目录切换
- 文件搜索（名称和内容）
- 文件复制、移动、删除
- 目录大小计算
- 隐藏文件过滤

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
- 官方插件（Git、Database、ApiTest、LogAnalysis、FileManager）

### 9. 性能优化
- LRU缓存（支持TTL过期）
- 性能监控（操作统计、耗时分析）
- 性能报告生成

### 10. 国际化支持
- 多语言框架（I18nManager）
- 中文/英文双语支持
- 语言切换监听
- 参数化消息格式化

### 11. 官方插件
- Git 版本控制集成
- 数据库管理
- API 测试
- 日志分析
- 文件管理

### 12. 高级功能
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
- **阶段九**: 16 个文件（10个Java + 6个测试类）
- **总计**: 86 个 Java 文件 + 7 个文档文件 + 5 个资源文件

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
- **插件**: 11 个类（EstPlugin、PluginContext、PluginManager、5个官方插件）
- **缓存**: 1 个类（LruCache）
- **性能**: 1 个类（PerformanceMonitor）
- **国际化**: 1 个类（I18nManager）
- **核心**: 2 个类
- **测试**: 10 个测试类

### 测试覆盖率
- 配置管理模块: ✅ ConfigValidatorTest、ConfigTemplateTest
- 搜索模块: ✅ FileIndexTest、SearchHistoryTest、SearchFilterTest
- 插件模块: ✅ PluginManagerTest
- 缓存模块: ✅ LruCacheTest
- 国际化模块: ✅ I18nManagerTest
- 提示模块: ✅ PromptLibraryTest、PromptTemplateTest
- 技能模块: ✅ SkillManagerTest

### 官方插件
- Git 集成插件: ✅ 完整功能
- 数据库管理插件: ✅ 完整功能
- API 测试插件: ✅ 完整功能
- 日志分析插件: ✅ 完整功能
- 文件管理插件: ✅ 完整功能

---

## 文档资源

- [README.md](./README.md) - 项目主文档
- [ROADMAP.md](./ROADMAP.md) - 开发路线图
- [docs/usage-examples.md](./docs/usage-examples.md) - 使用案例
- [docs/best-practices.md](./docs/best-practices.md) - 最佳实践
- [docs/faq.md](./docs/faq.md) - 常见问题
- [docs/EST_Code_CLI_Comprehensive_Evaluation.md](./docs/EST_Code_CLI_Comprehensive_Evaluation.md) - 全面评估与发展建议

---

## 贡献指南

### 如何贡献
1. 选择任务：从路线图中选择感兴趣的任务
2. 创建 Issue：说明计划如何实现
3. Fork 仓库：创建开发分支
4. 提交 PR：遵循贡献规范

### 建议的贡献方向
- **新手友好**: 文档完善、示例代码、Bug 修复、翻译贡献、插件开发
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

### 阶段十：中期目标 - 移动端优化 ✅

**目标**: 优化移动端体验，提升响应式设计

**完成内容**:
- ✅ 优化 Web 界面响应式设计（1024px/768px/480px 断点）
- ✅ 开发移动端专属布局（粘性头部、滚动优化）
- ✅ 触摸操作优化（-webkit-tap-highlight-color、touch-action、最小点击区域44px）
- ✅ 移动端性能优化（-webkit-overflow-scrolling、快速响应动画）
- ✅ PWA 支持（apple-mobile-web-app-capable、theme-color）
- ✅ 更新评估文档记录中期目标进展

**更新的文件**:
- `resources/web/index.html` - 添加移动端 meta 标签和 PWA 支持
- `resources/web/style.css` - 完整的响应式设计优化
- `docs/EST_Code_CLI_Comprehensive_Evaluation.md` - 更新评估文档

---

### 阶段十一：性能基准测试 ✅

**目标**: 建立性能基准测试套件，进行性能测试

**完成内容**:
- ✅ 创建性能基准测试套件（PerformanceBenchmarkTest）
- ✅ 索引性能测试 - 100/1000/5000 文件规模
- ✅ 搜索性能测试 - 100/1000/5000 文件规模
- ✅ 混合操作性能测试
- ✅ 内存使用测试
- ✅ 并发搜索测试
- ✅ 索引增量更新测试
- ✅ 搜索结果排序测试
- ✅ 使用 PerformanceMonitor 进行性能监控
- ✅ 自动生成性能报告

**创建的文件**:
- `test/java/performance/PerformanceBenchmarkTest.java` - 性能基准测试套件

**更新的文件**:
- `docs/EST_Code_CLI_Comprehensive_Evaluation.md` - 更新评估文档

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
- 官方插件（Git、Database、ApiTest、LogAnalysis、FileManager）

### 9. 性能优化
- LRU缓存（支持TTL过期）
- 性能监控（操作统计、耗时分析）
- 性能报告生成
- 性能基准测试套件

### 10. 国际化支持
- 多语言框架（I18nManager）
- 中文/英文双语支持
- 语言切换监听
- 参数化消息格式化

### 11. 官方插件
- Git 版本控制集成
- 数据库管理
- API 测试
- 日志分析
- 文件管理

### 12. 高级功能
- 项目搜索（倒排索引）
- HITL 安全策略
- Web 模式（浏览器界面 + REST API + 响应式设计 + PWA）
- ACP 协议（IDE 集成）
- 命令历史记录
- 测试运行器
- 对话上下文管理
- 移动端优化（响应式设计、触摸优化、PWA）
- 性能基准测试

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
- **阶段九**: 16 个文件（10个Java + 6个测试类）
- **阶段十**: 3 个文件（2个Web资源 + 1个更新）
- **阶段十一**: 2 个文件（1个测试类 + 1个更新）
- **总计**: 90 个 Java 文件 + 8 个文档文件 + 5 个资源文件

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
- **插件**: 11 个类（EstPlugin、PluginContext、PluginManager、5个官方插件）
- **缓存**: 1 个类（LruCache）
- **性能**: 2 个类（PerformanceMonitor、PerformanceBenchmarkTest）
- **国际化**: 1 个类（I18nManager）
- **核心**: 2 个类
- **测试**: 11 个测试类

### 测试覆盖率
- 配置管理模块: ✅ ConfigValidatorTest、ConfigTemplateTest
- 搜索模块: ✅ FileIndexTest、SearchHistoryTest、SearchFilterTest
- 插件模块: ✅ PluginManagerTest
- 缓存模块: ✅ LruCacheTest
- 国际化模块: ✅ I18nManagerTest
- 提示模块: ✅ PromptLibraryTest、PromptTemplateTest
- 技能模块: ✅ SkillManagerTest
- 性能模块: ✅ PerformanceBenchmarkTest

### 官方插件
- Git 集成插件: ✅ 完整功能
- 数据库管理插件: ✅ 完整功能
- API 测试插件: ✅ 完整功能
- 日志分析插件: ✅ 完整功能
- 文件管理插件: ✅ 完整功能

---

## 文档资源

- [README.md](./README.md) - 项目主文档
- [ROADMAP.md](./ROADMAP.md) - 开发路线图
- [docs/usage-examples.md](./docs/usage-examples.md) - 使用案例
- [docs/best-practices.md](./docs/best-practices.md) - 最佳实践
- [docs/faq.md](./docs/faq.md) - 常见问题
- [docs/EST_Code_CLI_Comprehensive_Evaluation.md](./docs/EST_Code_CLI_Comprehensive_Evaluation.md) - 全面评估与发展建议

---

## 贡献指南

### 如何贡献
1. 选择任务：从路线图中选择感兴趣的任务
2. 创建 Issue：说明计划如何实现
3. Fork 仓库：创建开发分支
4. 提交 PR：遵循贡献规范

### 建议的贡献方向
- **新手友好**: 文档完善、示例代码、Bug 修复、翻译贡献、插件开发
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

### 阶段十二：插件市场集成 ✅

**目标**: 集成EST框架的插件市场模块，推进生态系统建设

**完成内容**:
- ✅ 添加 est-plugin-marketplace 依赖到 pom.xml
- ✅ 创建 PluginMarketplaceManager 集成类
- ✅ 集成 PluginMarketplaceManager 到 CliInteractionHandler
- ✅ 添加 plugins 命令（插件管理功能）
- ✅ 添加 marketplace 命令（插件市场浏览功能）
- ✅ 实现插件市场完整交互功能（搜索、浏览、安装、更新）
- ✅ 支持流行插件、最新插件、认证插件浏览
- ✅ 支持按分类和标签搜索插件
- ✅ 支持插件安装、更新、检查更新
- ✅ 更新帮助信息

**创建的文件**:
- `plugin/PluginMarketplaceManager.java` - 插件市场管理器

**更新的文件**:
- `pom.xml` - 添加 est-plugin-marketplace 依赖
- `CliInteractionHandler.java` - 添加插件市场集成和命令处理

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
- 官方插件（Git、Database、ApiTest、LogAnalysis、FileManager）
- 插件市场集成（搜索、安装、更新）

### 9. 性能优化
- LRU缓存（支持TTL过期）
- 性能监控（操作统计、耗时分析）
- 性能报告生成
- 性能基准测试套件

### 10. 国际化支持
- 多语言框架（I18nManager）
- 中文/英文双语支持
- 语言切换监听
- 参数化消息格式化

### 11. 官方插件
- Git 版本控制集成
- 数据库管理
- API 测试
- 日志分析
- 文件管理

### 12. 插件市场
- 插件搜索（关键词搜索）
- 流行插件浏览
- 最新插件浏览
- 认证插件浏览
- 分类浏览
- 标签搜索
- 插件安装
- 插件更新
- 可用更新检查

### 13. 高级功能
- 项目搜索（倒排索引）
- HITL 安全策略
- Web 模式（浏览器界面 + REST API + 响应式设计 + PWA）
- ACP 协议（IDE 集成）
- 命令历史记录
- 测试运行器
- 对话上下文管理
- 移动端优化（响应式设计、触摸优化、PWA）
- 性能基准测试

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
| `plugins` | 管理插件 |
| `marketplace` | 浏览插件市场 |
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

### 插件化架构
- 标准插件接口
- 热加载支持
- 服务注册机制
- 沙箱安全模型
- 插件市场集成

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
- **阶段九**: 16 个文件（10个Java + 6个测试类）
- **阶段十**: 3 个文件（2个Web资源 + 1个更新）
- **阶段十一**: 2 个文件（1个测试类 + 1个更新）
- **阶段十二**: 3 个文件（1个Java + 2个更新）
- **总计**: 94 个 Java 文件 + 8 个文档文件 + 5 个资源文件

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
- **插件**: 12 个类（EstPlugin、PluginContext、PluginManager、5个官方插件、PluginMarketplaceManager）
- **缓存**: 1 个类（LruCache）
- **性能**: 2 个类（PerformanceMonitor、PerformanceBenchmarkTest）
- **国际化**: 1 个类（I18nManager）
- **核心**: 2 个类
- **测试**: 11 个测试类

### 测试覆盖率
- 配置管理模块: ✅ ConfigValidatorTest、ConfigTemplateTest
- 搜索模块: ✅ FileIndexTest、SearchHistoryTest、SearchFilterTest
- 插件模块: ✅ PluginManagerTest
- 缓存模块: ✅ LruCacheTest
- 国际化模块: ✅ I18nManagerTest
- 提示模块: ✅ PromptLibraryTest、PromptTemplateTest
- 技能模块: ✅ SkillManagerTest
- 性能模块: ✅ PerformanceBenchmarkTest

### 官方插件
- Git 集成插件: ✅ 完整功能
- 数据库管理插件: ✅ 完整功能
- API 测试插件: ✅ 完整功能
- 日志分析插件: ✅ 完整功能
- 文件管理插件: ✅ 完整功能

---

## 文档资源

- [README.md](./README.md) - 项目主文档
- [ROADMAP.md](./ROADMAP.md) - 开发路线图
- [docs/usage-examples.md](./docs/usage-examples.md) - 使用案例
- [docs/best-practices.md](./docs/best-practices.md) - 最佳实践
- [docs/faq.md](./docs/faq.md) - 常见问题
- [docs/EST_Code_CLI_Comprehensive_Evaluation.md](./docs/EST_Code_CLI_Comprehensive_Evaluation.md) - 全面评估与发展建议

---

## 贡献指南

### 如何贡献
1. 选择任务：从路线图中选择感兴趣的任务
2. 创建 Issue：说明计划如何实现
3. Fork 仓库：创建开发分支
4. 提交 PR：遵循贡献规范

### 建议的贡献方向
- **新手友好**: 文档完善、示例代码、Bug 修复、翻译贡献、插件开发
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

### 阶段十三：云原生部署功能 ✅

**目标**: 为EST Code CLI添加云原生部署支持，推进云原生增强

**完成内容**:
- ✅ 为Web界面添加"部署"标签页
- ✅ 添加6个部署平台支持（Docker、Kubernetes、AWS Lambda、Azure Functions、阿里云函数计算、Google Cloud）
- ✅ 为每个平台提供预置部署配置模板
- ✅ 实现部署配置一键复制功能
- ✅ 添加响应式设计，支持移动端
- ✅ 添加美观的UI样式和交互效果

**更新的文件**:
- `resources/web/index.html` - 添加部署标签页和UI
- `resources/web/style.css` - 添加部署功能样式
- `resources/web/script.js` - 添加部署功能实现

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
- 官方插件（Git、Database、ApiTest、LogAnalysis、FileManager）
- 插件市场集成（搜索、安装、更新）

### 9. 性能优化
- LRU缓存（支持TTL过期）
- 性能监控（操作统计、耗时分析）
- 性能报告生成
- 性能基准测试套件

### 10. 国际化支持
- 多语言框架（I18nManager）
- 中文/英文双语支持
- 语言切换监听
- 参数化消息格式化

### 11. 官方插件
- Git 版本控制集成
- 数据库管理
- API 测试
- 日志分析
- 文件管理

### 12. 插件市场
- 插件搜索（关键词搜索）
- 流行插件浏览
- 最新插件浏览
- 认证插件浏览
- 分类浏览
- 标签搜索
- 插件安装
- 插件更新
- 可用更新检查

### 13. 云原生部署
- Docker 容器化部署配置
- Kubernetes 容器编排配置
- AWS Lambda Serverless 配置
- Azure Functions Serverless 配置
- 阿里云函数计算 Serverless 配置
- Google Cloud Functions Serverless 配置
- 部署配置一键复制
- 响应式设计支持

### 14. 高级功能
- 项目搜索（倒排索引）
- HITL 安全策略
- Web 模式（浏览器界面 + REST API + 响应式设计 + PWA + 部署）
- ACP 协议（IDE 集成）
- 命令历史记录
- 测试运行器
- 对话上下文管理
- 移动端优化（响应式设计、触摸优化、PWA）
- 性能基准测试

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
| `plugins` | 管理插件 |
| `marketplace` | 浏览插件市场 |
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

### 插件化架构
- 标准插件接口
- 热加载支持
- 服务注册机制
- 沙箱安全模型
- 插件市场集成

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
- **阶段九**: 16 个文件（10个Java + 6个测试类）
- **阶段十**: 3 个文件（2个Web资源 + 1个更新）
- **阶段十一**: 2 个文件（1个测试类 + 1个更新）
- **阶段十二**: 3 个文件（1个Java + 2个更新）
- **阶段十三**: 3 个文件（3个Web资源更新）
- **总计**: 94 个 Java 文件 + 8 个文档文件 + 8 个资源文件

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
- **插件**: 12 个类（EstPlugin、PluginContext、PluginManager、5个官方插件、PluginMarketplaceManager）
- **缓存**: 1 个类（LruCache）
- **性能**: 2 个类（PerformanceMonitor、PerformanceBenchmarkTest）
- **国际化**: 1 个类（I18nManager）
- **核心**: 2 个类
- **测试**: 11 个测试类

### 测试覆盖率
- 配置管理模块: ✅ ConfigValidatorTest、ConfigTemplateTest
- 搜索模块: ✅ FileIndexTest、SearchHistoryTest、SearchFilterTest
- 插件模块: ✅ PluginManagerTest
- 缓存模块: ✅ LruCacheTest
- 国际化模块: ✅ I18nManagerTest
- 提示模块: ✅ PromptLibraryTest、PromptTemplateTest
- 技能模块: ✅ SkillManagerTest
- 性能模块: ✅ PerformanceBenchmarkTest

### 官方插件
- Git 集成插件: ✅ 完整功能
- 数据库管理插件: ✅ 完整功能
- API 测试插件: ✅ 完整功能
- 日志分析插件: ✅ 完整功能
- 文件管理插件: ✅ 完整功能

### 部署平台
- Docker: ✅ 容器化部署配置
- Kubernetes: ✅ 容器编排配置
- AWS Lambda: ✅ Serverless 配置
- Azure Functions: ✅ Serverless 配置
- 阿里云函数计算: ✅ Serverless 配置
- Google Cloud Functions: ✅ Serverless 配置

---

## 文档资源

- [README.md](./README.md) - 项目主文档
- [ROADMAP.md](./ROADMAP.md) - 开发路线图
- [docs/usage-examples.md](./docs/usage-examples.md) - 使用案例
- [docs/best-practices.md](./docs/best-practices.md) - 最佳实践
- [docs/faq.md](./docs/faq.md) - 常见问题
- [docs/EST_Code_CLI_Comprehensive_Evaluation.md](./docs/EST_Code_CLI_Comprehensive_Evaluation.md) - 全面评估与发展建议

---

## 贡献指南

### 如何贡献
1. 选择任务：从路线图中选择感兴趣的任务
2. 创建 Issue：说明计划如何实现
3. Fork 仓库：创建开发分支
4. 提交 PR：遵循贡献规范

### 建议的贡献方向
- **新手友好**: 文档完善、示例代码、Bug 修复、翻译贡献、插件开发
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

### 阶段十四：多语言 SDK 示例 ✅

**目标**: 为EST Code CLI添加多语言SDK示例，推进多语言支持

**完成内容**:
- ✅ 为Web界面添加"SDK示例"标签页
- ✅ 支持4种主流编程语言（TypeScript、Python、Go、Kotlin）
- ✅ 为每种语言提供完整的使用示例
- ✅ TypeScript SDK：安装、基础使用、插件市场
- ✅ Python SDK：安装、基础使用、插件市场
- ✅ Go SDK：安装、基础使用、插件市场
- ✅ Kotlin DSL：协程支持、EST DSL、Flow支持
- ✅ 添加语言切换标签页功能
- ✅ 添加响应式设计，支持移动端
- ✅ 添加美观的UI样式和交互效果

**更新的文件**:
- `resources/web/index.html` - 添加SDK示例标签页和UI
- `resources/web/style.css` - 添加SDK示例功能样式
- `resources/web/script.js` - 添加语言切换功能

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
- 官方插件（Git、Database、ApiTest、LogAnalysis、FileManager）
- 插件市场集成（搜索、安装、更新）

### 9. 性能优化
- LRU缓存（支持TTL过期）
- 性能监控（操作统计、耗时分析）
- 性能报告生成
- 性能基准测试套件

### 10. 国际化支持
- 多语言框架（I18nManager）
- 中文/英文双语支持
- 语言切换监听
- 参数化消息格式化

### 11. 官方插件
- Git 版本控制集成
- 数据库管理
- API 测试
- 日志分析
- 文件管理

### 12. 插件市场
- 插件搜索（关键词搜索）
- 流行插件浏览
- 最新插件浏览
- 认证插件浏览
- 分类浏览
- 标签搜索
- 插件安装
- 插件更新
- 可用更新检查

### 13. 云原生部署
- Docker 容器化部署配置
- Kubernetes 容器编排配置
- AWS Lambda Serverless 配置
- Azure Functions Serverless 配置
- 阿里云函数计算 Serverless 配置
- Google Cloud Functions Serverless 配置
- 部署配置一键复制
- 响应式设计支持

### 14. 多语言 SDK
- TypeScript / JavaScript SDK 示例
- Python SDK 示例
- Go SDK 示例
- Kotlin DSL 示例
- 语言切换标签页
- 响应式设计支持

### 15. 高级功能
- 项目搜索（倒排索引）
- HITL 安全策略
- Web 模式（浏览器界面 + REST API + 响应式设计 + PWA + 部署 + SDK示例）
- ACP 协议（IDE 集成）
- 命令历史记录
- 测试运行器
- 对话上下文管理
- 移动端优化（响应式设计、触摸优化、PWA）
- 性能基准测试

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
| `plugins` | 管理插件 |
| `marketplace` | 浏览插件市场 |
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

### 插件化架构
- 标准插件接口
- 热加载支持
- 服务注册机制
- 沙箱安全模型
- 插件市场集成

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
- **阶段九**: 16 个文件（10个Java + 6个测试类）
- **阶段十**: 3 个文件（2个Web资源 + 1个更新）
- **阶段十一**: 2 个文件（1个测试类 + 1个更新）
- **阶段十二**: 3 个文件（1个Java + 2个更新）
- **阶段十三**: 3 个文件（3个Web资源更新）
- **阶段十四**: 3 个文件（3个Web资源更新）
- **总计**: 94 个 Java 文件 + 8 个文档文件 + 11 个资源文件

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
- **插件**: 12 个类（EstPlugin、PluginContext、PluginManager、5个官方插件、PluginMarketplaceManager）
- **缓存**: 1 个类（LruCache）
- **性能**: 2 个类（PerformanceMonitor、PerformanceBenchmarkTest）
- **国际化**: 1 个类（I18nManager）
- **核心**: 2 个类
- **测试**: 11 个测试类

### 测试覆盖率
- 配置管理模块: ✅ ConfigValidatorTest、ConfigTemplateTest
- 搜索模块: ✅ FileIndexTest、SearchHistoryTest、SearchFilterTest
- 插件模块: ✅ PluginManagerTest
- 缓存模块: ✅ LruCacheTest
- 国际化模块: ✅ I18nManagerTest
- 提示模块: ✅ PromptLibraryTest、PromptTemplateTest
- 技能模块: ✅ SkillManagerTest
- 性能模块: ✅ PerformanceBenchmarkTest

### 官方插件
- Git 集成插件: ✅ 完整功能
- 数据库管理插件: ✅ 完整功能
- API 测试插件: ✅ 完整功能
- 日志分析插件: ✅ 完整功能
- 文件管理插件: ✅ 完整功能

### 部署平台
- Docker: ✅ 容器化部署配置
- Kubernetes: ✅ 容器编排配置
- AWS Lambda: ✅ Serverless 配置
- Azure Functions: ✅ Serverless 配置
- 阿里云函数计算: ✅ Serverless 配置
- Google Cloud Functions: ✅ Serverless 配置

### 多语言 SDK
- TypeScript / JavaScript: ✅ 完整示例
- Python: ✅ 完整示例
- Go: ✅ 完整示例
- Kotlin DSL: ✅ 完整示例

---

## 文档资源

- [README.md](./README.md) - 项目主文档
- [ROADMAP.md](./ROADMAP.md) - 开发路线图
- [docs/usage-examples.md](./docs/usage-examples.md) - 使用案例
- [docs/best-practices.md](./docs/best-practices.md) - 最佳实践
- [docs/faq.md](./docs/faq.md) - 常见问题
- [docs/EST_Code_CLI_Comprehensive_Evaluation.md](./docs/EST_Code_CLI_Comprehensive_Evaluation.md) - 全面评估与发展建议

---

## 贡献指南

### 如何贡献
1. 选择任务：从路线图中选择感兴趣的任务
2. 创建 Issue：说明计划如何实现
3. Fork 仓库：创建开发分支
4. 提交 PR：遵循贡献规范

### 建议的贡献方向
- **新手友好**: 文档完善、示例代码、Bug 修复、翻译贡献、插件开发
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

### 阶段十五：Web界面插件市场集成 ✅

**目标**: 为Web界面添加插件市场功能，完善生态系统

**完成内容**:
- ✅ 为Web界面添加"插件市场"标签页
- ✅ 实现插件市场UI（搜索框、分类筛选、排序选项）
- ✅ 添加三个视图标签（浏览市场、已安装、可更新）
- ✅ 预置8个示例插件（5个官方+3个社区）
- ✅ 实现插件卡片展示（图标、名称、描述、版本、下载量、评分、作者）
- ✅ 实现插件搜索功能（关键词搜索）
- ✅ 实现插件分类筛选（全部分类、开发工具、系统集成、数据分析、效率提升）
- ✅ 实现插件排序（最流行、最新、评分最高）
- ✅ 实现插件安装/卸载/更新功能
- ✅ 添加响应式设计，支持移动端
- ✅ 添加美观的UI样式和交互效果

**更新的文件**:
- `resources/web/index.html` - 添加插件市场标签页和UI
- `resources/web/style.css` - 添加插件市场功能样式
- `resources/web/script.js` - 添加插件市场功能实现

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
- 官方插件（Git、Database、ApiTest、LogAnalysis、FileManager）
- 插件市场集成（搜索、安装、更新）
- Web界面插件市场（浏览、安装、更新、卸载）

### 9. 性能优化
- LRU缓存（支持TTL过期）
- 性能监控（操作统计、耗时分析）
- 性能报告生成
- 性能基准测试套件

### 10. 国际化支持
- 多语言框架（I18nManager）
- 中文/英文双语支持
- 语言切换监听
- 参数化消息格式化

### 11. 官方插件
- Git 版本控制集成
- 数据库管理
- API 测试
- 日志分析
- 文件管理

### 12. 插件市场
- 插件搜索（关键词搜索）
- 流行插件浏览
- 最新插件浏览
- 认证插件浏览
- 分类浏览
- 标签搜索
- 插件安装
- 插件更新
- 可用更新检查
- Web界面插件市场（8个示例插件）

### 13. 云原生部署
- Docker 容器化部署配置
- Kubernetes 容器编排配置
- AWS Lambda Serverless 配置
- Azure Functions Serverless 配置
- 阿里云函数计算 Serverless 配置
- Google Cloud Functions Serverless 配置
- 部署配置一键复制
- 响应式设计支持

### 14. 多语言 SDK
- TypeScript / JavaScript SDK 示例
- Python SDK 示例
- Go SDK 示例
- Kotlin DSL 示例
- 语言切换标签页
- 响应式设计支持

### 15. 高级功能
- 项目搜索（倒排索引）
- HITL 安全策略
- Web 模式（浏览器界面 + REST API + 响应式设计 + PWA + 部署 + SDK示例 + 插件市场）
- ACP 协议（IDE 集成）
- 命令历史记录
- 测试运行器
- 对话上下文管理
- 移动端优化（响应式设计、触摸优化、PWA）
- 性能基准测试

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
| `plugins` | 管理插件 |
| `marketplace` | 浏览插件市场 |
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

### 插件化架构
- 标准插件接口
- 热加载支持
- 服务注册机制
- 沙箱安全模型
- 插件市场集成

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
- **阶段九**: 16 个文件（10个Java + 6个测试类）
- **阶段十**: 3 个文件（2个Web资源 + 1个更新）
- **阶段十一**: 2 个文件（1个测试类 + 1个更新）
- **阶段十二**: 3 个文件（1个Java + 2个更新）
- **阶段十三**: 3 个文件（3个Web资源更新）
- **阶段十四**: 3 个文件（3个Web资源更新）
- **阶段十五**: 3 个文件（3个Web资源更新）
- **总计**: 94 个 Java 文件 + 8 个文档文件 + 14 个资源文件

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
- **插件**: 12 个类（EstPlugin、PluginContext、PluginManager、5个官方插件、PluginMarketplaceManager）
- **缓存**: 1 个类（LruCache）
- **性能**: 2 个类（PerformanceMonitor、PerformanceBenchmarkTest）
- **国际化**: 1 个类（I18nManager）
- **核心**: 2 个类
- **测试**: 11 个测试类

### 测试覆盖率
- 配置管理模块: ✅ ConfigValidatorTest、ConfigTemplateTest
- 搜索模块: ✅ FileIndexTest、SearchHistoryTest、SearchFilterTest
- 插件模块: ✅ PluginManagerTest
- 缓存模块: ✅ LruCacheTest
- 国际化模块: ✅ I18nManagerTest
- 提示模块: ✅ PromptLibraryTest、PromptTemplateTest
- 技能模块: ✅ SkillManagerTest
- 性能模块: ✅ PerformanceBenchmarkTest

### 官方插件
- Git 集成插件: ✅ 完整功能
- 数据库管理插件: ✅ 完整功能
- API 测试插件: ✅ 完整功能
- 日志分析插件: ✅ 完整功能
- 文件管理插件: ✅ 完整功能

### 部署平台
- Docker: ✅ 容器化部署配置
- Kubernetes: ✅ 容器编排配置
- AWS Lambda: ✅ Serverless 配置
- Azure Functions: ✅ Serverless 配置
- 阿里云函数计算: ✅ Serverless 配置
- Google Cloud Functions: ✅ Serverless 配置

### 多语言 SDK
- TypeScript / JavaScript: ✅ 完整示例
- Python: ✅ 完整示例
- Go: ✅ 完整示例
- Kotlin DSL: ✅ 完整示例

### Web界面插件市场
- 插件搜索: ✅ 关键词搜索
- 分类筛选: ✅ 5个分类
- 排序选项: ✅ 3种排序方式
- 插件浏览: ✅ 3个视图（浏览市场、已安装、可更新）
- 插件操作: ✅ 安装、卸载、更新
- 示例插件: ✅ 8个示例插件（5个官方+3个社区）
- 响应式设计: ✅ 移动端支持

---

## 文档资源

- [README.md](./README.md) - 项目主文档
- [ROADMAP.md](./ROADMAP.md) - 开发路线图
- [docs/usage-examples.md](./docs/usage-examples.md) - 使用案例
- [docs/best-practices.md](./docs/best-practices.md) - 最佳实践
- [docs/faq.md](./docs/faq.md) - 常见问题
- [docs/EST_Code_CLI_Comprehensive_Evaluation.md](./docs/EST_Code_CLI_Comprehensive_Evaluation.md) - 全面评估与发展建议

---

## 贡献指南

### 如何贡献
1. 选择任务：从路线图中选择感兴趣的任务
2. 创建 Issue：说明计划如何实现
3. Fork 仓库：创建开发分支
4. 提交 PR：遵循贡献规范

### 建议的贡献方向
- **新手友好**: 文档完善、示例代码、Bug 修复、翻译贡献、插件开发
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

**项目状态**: ✅ 阶段一至阶段十五已完成
**最后更新**: 2026-03-10
**EST 版本**: 2.4.0-SNAPSHOT
