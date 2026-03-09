# EST Code CLI 使用案例

## 目录
- [基础使用](#基础使用)
- [配置管理](#配置管理)
- [搜索功能](#搜索功能)
- [技能使用](#技能使用)
- [Web模式](#web模式)
- [ACP协议](#acp协议)

---

## 基础使用

### 启动EST Code CLI

```bash
# 使用默认配置启动
est-code-cli

# 指定工作目录
est-code-cli --work-dir /path/to/project

# 指定昵称
est-code-cli --nickname MyDevBot
```

### 基础对话

```
> 你好，帮我创建一个Hello World程序
EST: 你好！我来帮你创建一个Hello World程序...

> 请解释一下这段代码
EST: 好的，让我分析一下这段代码...
```

---

## 配置管理

### 查看当前配置

```
> config

Current Configuration:
========================================
  Nickname:     EST
  Work Dir:     /path/to/project
  Planning Mode: true
  HITL Enabled: true
========================================
```

### 修改配置

```
> config

Options:
  1. Set Nickname
  2. Toggle Planning Mode
  3. Toggle HITL Security
  4. Save Configuration
  5. Save to User Config
  0. Back

Choose an option: 1
Enter new nickname: MyDevBot
Nickname updated to: MyDevBot
```

### 配置验证

```java
// 编程方式验证配置
CliConfig config = CliConfig.load();
ConfigValidator.ValidationResult result = config.validate();

if (result.isValid()) {
    System.out.println("配置有效！");
} else {
    System.out.println("配置错误:");
    for (String error : result.getErrors()) {
        System.out.println("  - " + error);
    }
}

if (result.hasWarnings()) {
    System.out.println("配置警告:");
    for (String warning : result.getWarnings()) {
        System.out.println("  - " + warning);
    }
}
```

### 配置导入/导出

```java
// 导出配置
CliConfig config = CliConfig.load();
config.exportTo(Paths.get("/path/to/backup.yml"));

// 导入配置
CliConfig imported = CliConfig.importFrom(Paths.get("/path/to/backup.yml"));
```

### 配置模板

```java
// 使用预设模板
CliConfig config = CliConfig.fromTemplate(ConfigTemplate.createDeveloper());

// 应用模板到现有配置
ConfigTemplate secureTemplate = ConfigTemplate.createSecure();
config.applyTemplate(secureTemplate);

// 列出所有模板
List<ConfigTemplate> templates = ConfigTemplate.getAllTemplates();
for (ConfigTemplate template : templates) {
    System.out.println(template.getName() + ": " + template.getDescription());
}
```

### 配置版本管理

```java
ConfigVersionManager versionManager = new ConfigVersionManager("est-code-cli.yml");

// 创建备份
Path backup = versionManager.createBackup();
System.out.println("备份已创建: " + backup);

// 列出所有备份
List<Path> backups = versionManager.listBackups();
System.out.println("找到 " + backups.size() + " 个备份");

// 恢复备份
versionManager.restoreBackup(backups.get(0));

// 删除旧备份（保留最近5个）
versionManager.deleteOldBackups(5);
```

---

## 搜索功能

### 基础搜索

```java
FileIndex index = new FileIndex();

// 索引文件
index.indexFile("/path/to/File1.java", "public class File1 { ... }");
index.indexFile("/path/to/File2.java", "public class File2 { ... }");

// 基础搜索
List<FileIndex.SearchResult> results = index.search("class");
for (FileIndex.SearchResult result : results) {
    System.out.println("文件: " + result.getDocument().getFilePath());
    System.out.println("得分: " + result.getScore());
    System.out.println("摘要: " + result.getSnippet());
    System.out.println();
}
```

### 模糊搜索

```java
SearchFilter filter = new SearchFilter()
    .setFuzzySearch(true)
    .setFuzzyDistance(2);

List<FileIndex.SearchResult> results = index.search("clas", filter);
// 会匹配 "class", "classes", "classic" 等
```

### 高级筛选

```java
// 只搜索Java文件，排除测试文件
SearchFilter filter = new SearchFilter()
    .addIncludeExtension("java")
    .addExcludePath("test")
    .setMinScore(2)
    .setMaxResults(10);

List<FileIndex.SearchResult> results = index.search("service", filter);
```

### 使用预设筛选器

```java
// Java文件专用
SearchFilter javaFilter = SearchFilter.javaFilesOnly();

// 排除测试文件
SearchFilter noTestFilter = SearchFilter.excludeTestFiles();

// 源文件专用（Java, JS, TS, Python）
SearchFilter sourceFilter = SearchFilter.sourceFilesOnly();
```

### 搜索历史

```java
SearchHistory history = index.getSearchHistory();

// 查看最近查询
List<String> recentQueries = history.getRecentQueries(10);
System.out.println("最近查询:");
for (String query : recentQueries) {
    System.out.println("  - " + query);
}

// 查找相似查询
List<SearchHistory.SearchEntry> similar = history.findSimilar("class");

// 查看完整历史
for (SearchHistory.SearchEntry entry : history.getAll()) {
    System.out.println(entry.getQuery() + " (" + entry.getResultCount() + " 结果)");
}
```

### 搜索高亮

```java
SearchFilter filter = new SearchFilter();
List<FileIndex.SearchResult> results = index.search("test", filter);

for (FileIndex.SearchResult result : results) {
    System.out.println("普通摘要: " + result.getSnippet());
    System.out.println("高亮摘要: " + result.getHighlightedSnippet());
    // 高亮部分用 << 和 >> 标记
}
```

---

## 技能使用

### 查看可用技能

```
> skills

Available EST Skills:

  - code_review
    代码审查技能 - 分析代码质量和潜在问题

  - refactor
    重构技能 - 提供代码重构建议

  - architecture
    架构分析技能 - 分析项目架构设计

  - performance_optimization
    性能优化技能 - 分析和优化代码性能

  - security_audit
    安全审计技能 - 审计代码安全性
```

### 使用代码审查技能

```
> 请帮我审查这段代码
💡 检测到您可能需要使用技能: code_review
   代码审查技能 - 分析代码质量和潜在问题

是否要使用该技能？(y/n，或直接继续对话)
> y

使用技能: code_review
==================================================

请提供要处理的代码文件路径，或直接粘贴代码：
> /path/to/MyClass.java
已读取文件: /path/to/MyClass.java

正在处理...

[代码审查结果...]
```

### 编程方式使用技能

```java
SkillManager skillManager = new SkillManager();

// 查找匹配的技能
EstSkill skill = skillManager.findMatchingSkill("请优化这段代码的性能");
if (skill != null) {
    String result = skill.execute("这里是代码内容...");
    System.out.println(result);
}

// 获取特定技能
EstSkill codeReview = skillManager.getSkill("code_review");
String review = codeReview.execute("public class MyClass { ... }");
```

---

## Web模式

### 启动Web服务器

```
> web

Starting EST Code CLI Web Server...
Enter port (default 8080): [回车]

EST Code CLI Web Server started on http://localhost:8080
Press Ctrl+C to stop the server
```

### Web API使用

```bash
# 查看服务器状态
curl http://localhost:8080/api/status

# 发送聊天请求
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message":"你好"}'

# 列出可用工具
curl http://localhost:8080/api/tools

# 调用工具
curl -X POST http://localhost:8080/api/tools/call \
  -H "Content-Type: application/json" \
  -d '{"name":"est_list_dir","arguments":{"path":"src"}}'

# 列出技能
curl http://localhost:8080/api/skills

# 列出提示模板
curl http://localhost:8080/api/templates

# 搜索文件
curl -X POST http://localhost:8080/api/search \
  -H "Content-Type: application/json" \
  -d '{"query":"class"}'

# 获取配置
curl http://localhost:8080/api/config

# 更新配置
curl -X POST http://localhost:8080/api/config \
  -H "Content-Type: application/json" \
  -d '{"nickname":"MyBot"}'
```

---

## ACP协议

### 启动ACP服务器

```
> acp

Starting EST Code CLI ACP Server...
Enter port (default 3000): [回车]

EST Code CLI ACP Server started on port 3000
Available tools: 10 MCP tools
Press Ctrl+C to stop the server
```

### ACP协议使用

ACP协议使用JSON-RPC 2.0格式：

```json
// 初始化
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "initialize"
}

// 响应
{
  "jsonrpc": "2.0",
  "result": {
    "serverInfo": {
      "name": "est-code-cli-acp",
      "version": "1.1.0"
    },
    "capabilities": {
      "tools": {}
    }
  },
  "id": 1
}

// 列出工具
{
  "jsonrpc": "2.0",
  "id": 2,
  "method": "tools/list"
}

// 调用工具
{
  "jsonrpc": "2.0",
  "id": 3,
  "method": "tools/call",
  "params": {
    "name": "est_read_file",
    "arguments": {
      "path": "src/Test.java"
    }
  }
}
```

---

## 对话上下文管理

### 查看对话上下文

```
> context

Conversation Context:
==================================================
Message count: 3

[USER]
你好，帮我写一个类

[ASSISTANT]
好的，我来帮你创建一个Java类...

[USER]
请添加一个方法

==================================================
```

### 清除对话上下文

```
> clear

Conversation context cleared.
The AI will no longer remember previous messages.
```

### 多轮对话示例

```
> 帮我创建一个用户服务类
EST: 好的，我来帮你创建一个用户服务类...
[创建 UserService.java]

> 请添加一个根据ID查询用户的方法
EST: 好的，我来添加这个方法...
[在 UserService.java 中添加 getUserById 方法]

> 再添加一个更新用户的方法
EST: 好的，继续添加更新用户的方法...
[在 UserService.java 中添加 updateUser 方法]
```

---

## 命令历史

### 查看命令历史

```
> history

Command History:
===============
  1. help
  2. init
  3. tools
  4. 帮我创建一个类
  5. clear

Total: 5 commands
```

---

## 最佳实践

### 1. 项目初始化

```bash
# 在新项目中首先初始化
est-code-cli
> init
```

### 2. 配置管理

```bash
# 配置后立即保存
> config
[修改配置后选择 4 或 5 保存]

# 定期备份配置
# 使用 ConfigVersionManager 编程方式备份
```

### 3. 搜索优化

```java
// 使用适当的筛选器提高搜索效率
SearchFilter filter = SearchFilter.sourceFilesOnly()
    .setMaxResults(20);

// 使用模糊搜索处理拼写错误
SearchFilter fuzzyFilter = new SearchFilter()
    .setFuzzySearch(true)
    .setFuzzyDistance(1);
```

### 4. 技能使用

- 使用技能处理复杂的代码任务
- 优先考虑使用专门的技能而不是通用对话
- 技能处理完成后可以继续多轮对话

### 5. 对话上下文

- 相关联的任务在同一会话中完成
- 切换不相关任务时使用 `clear` 清除上下文
- 使用 `context` 查看当前对话状态

---

## 常见工作流

### 新项目开发工作流

1. 启动 EST Code CLI
2. 运行 `init` 初始化项目
3. 使用自然语言描述需求
4. 使用代码审查技能检查生成的代码
5. 运行 `test` 执行测试
6. 使用 `compile` 编译项目

### 代码审查工作流

1. 使用代码审查技能分析代码
2. 根据建议进行修改
3. 使用重构技能优化代码
4. 使用安全审计技能检查安全性
5. 使用性能优化技能提升性能

### 搜索工作流

1. 使用基础搜索定位相关文件
2. 使用筛选器缩小范围
3. 使用模糊搜索处理拼写变体
4. 利用搜索历史重复常用查询
5. 查看高亮结果快速定位匹配位置

---

*文档版本: 1.0.0*  
*最后更新: 2026-03-09*
