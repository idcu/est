# EST Code CLI 常见问题

## 目录
- [安装与配置](#安装与配置)
- [基本使用](#基本使用)
- [配置管理](#配置管理)
- [搜索功能](#搜索功能)
- [技能使用](#技能使用)
- [安全相关](#安全相关)
- [性能问题](#性能问题)
- [故障排除](#故障排除)

---

## 安装与配置

### Q: EST Code CLI 需要什么系统要求？

**A:** 
- Java 17 或更高版本
- Maven 3.8+（用于构建）
- 至少 512MB 可用内存
- 操作系统：Windows、macOS 或 Linux

### Q: 如何安装 EST Code CLI？

**A:** 
```bash
# 1. 克隆项目
git clone <repository-url>
cd est2.0/est-tools/est-code-cli

# 2. 编译打包
mvn clean package

# 3. 运行
java -jar target/est-code-cli-<version>.jar
```

### Q: 首次启动需要配置什么？

**A:** 首次启动时，建议：
1. 设置一个昵称（默认是 "EST"）
2. 指定工作目录（默认是当前目录）
3. 根据需要选择配置模板
4. 保存配置

```bash
est-code-cli
> config
# 按照提示进行配置
```

---

## 基本使用

### Q: 如何查看所有可用命令？

**A:** 输入 `help` 命令可以查看所有可用命令：
```
> help
```

### Q: 如何退出 EST Code CLI？

**A:** 输入 `exit` 或 `quit` 命令，或者按 `Ctrl+C`：
```
> exit
```

### Q: 对话上下文会保存吗？

**A:** 当前会话的对话上下文会保存在内存中，但不会持久化到磁盘。退出程序后上下文会丢失。如果需要保存重要对话，可以手动复制保存。

### Q: 如何清除对话上下文？

**A:** 使用 `clear` 命令：
```
> clear
```

---

## 配置管理

### Q: 配置文件保存在哪里？

**A:** 配置文件位置：
- 用户级配置：`~/.est-code-cli.yml`
- 项目级配置：`<工作目录>/.est-code-cli.yml`

### Q: 如何备份我的配置？

**A:** 有两种方式：
1. 使用内置的版本管理功能：
```java
ConfigVersionManager manager = new ConfigVersionManager("est-code-cli.yml");
Path backup = manager.createBackup();
```

2. 手动导出配置：
```java
CliConfig config = CliConfig.load();
config.exportTo(Paths.get("my-config-backup.yml"));
```

### Q: 如何恢复之前的配置？

**A:** 
```java
ConfigVersionManager manager = new ConfigVersionManager("est-code-cli.yml");
List<Path> backups = manager.listBackups();
manager.restoreBackup(backups.get(0)); // 恢复最新的备份
```

或者导入之前导出的配置：
```java
CliConfig config = CliConfig.importFrom(Paths.get("my-config-backup.yml"));
config.save();
```

### Q: 配置验证失败怎么办？

**A:** 查看验证错误信息，然后修复问题：
```java
ConfigValidator.ValidationResult result = config.validate();
if (!result.isValid()) {
    result.getErrors().forEach(System.err::println);
    // 根据错误修复配置
}
```

常见问题：
- 工作目录不存在或不可写
- 昵称包含非法字符
- LLM API 配置缺失

### Q: 什么是配置模板？如何使用？

**A:** 配置模板是预设的配置集合，适用于不同场景：
- `default` - 默认配置，平衡功能和安全性
- `minimal` - 最小化配置，适合简单任务
- `developer` - 开发者配置，启用更多功能
- `secure` - 安全配置，适合生产环境

使用方式：
```bash
# 在交互模式中选择
> config
# 选择应用模板选项

# 或编程方式使用
CliConfig config = CliConfig.fromTemplate(ConfigTemplate.createDeveloper());
```

---

## 搜索功能

### Q: 搜索功能支持哪些文件类型？

**A:** 默认支持所有文本文件，但可以通过筛选器限制：
- Java: `.java`
- JavaScript: `.js`
- TypeScript: `.ts`, `.tsx`
- Python: `.py`
- Go: `.go`
- Rust: `.rs`
- 等等...

### Q: 什么是模糊搜索？如何启用？

**A:** 模糊搜索可以容忍拼写错误，使用 Levenshtein 距离算法。启用方式：
```java
SearchFilter filter = new SearchFilter()
    .setFuzzySearch(true)
    .setFuzzyDistance(2); // 允许最多2个字符的差异
```

### Q: 搜索结果太慢怎么办？

**A:** 优化建议：
1. 使用筛选器减少搜索范围
2. 限制最大结果数
3. 排除不需要的目录（如 target、node_modules）
4. 设置最小得分阈值

```java
SearchFilter filter = new SearchFilter()
    .addIncludeExtension("java")
    .addExcludePath("target")
    .addExcludePath("build")
    .setMinScore(2)
    .setMaxResults(20);
```

### Q: 搜索历史会保存多久？

**A:** 搜索历史保存在内存中，程序退出后会清除。如果需要持久化，可以手动保存：
```java
SearchHistory history = index.getSearchHistory();
// 可以序列化保存到文件
```

### Q: 高亮的文本如何显示？

**A:** 高亮部分用 `<<` 和 `>>` 标记，例如：
```
public <<class>> UserService { ... }
```

---

## 技能使用

### Q: 有哪些可用的技能？

**A:** 目前提供以下技能：
- `code_review` - 代码审查，分析代码质量和潜在问题
- `refactor` - 代码重构，提供重构建议
- `architecture` - 架构分析，分析项目架构设计
- `performance_optimization` - 性能优化，分析和优化性能
- `security_audit` - 安全审计，审计代码安全性

### Q: 如何触发技能？

**A:** 有两种方式：
1. 自动检测：当你输入相关请求时，系统会自动建议使用技能
2. 手动选择：输入 `skills` 查看所有技能，然后直接使用

```
> skills
# 查看可用技能
> 请帮我审查这段代码
💡 检测到您可能需要使用技能: code_review
是否要使用该技能？(y/n) > y
```

### Q: 技能执行需要联网吗？

**A:** 是的，技能通常需要调用 LLM API 来分析代码，因此需要网络连接。

### Q: 可以自定义技能吗？

**A:** 当前版本需要通过编程方式扩展。你可以实现 `EstSkill` 接口来创建自定义技能：
```java
public class MyCustomSkill implements EstSkill {
    @Override
    public String getName() { return "my_skill"; }
    
    @Override
    public String execute(String input) {
        // 实现你的技能逻辑
        return "结果";
    }
}
```

---

## 安全相关

### Q: 什么是 HITL（Human-in-the-Loop）？

**A:** HITL 是安全机制，要求在执行敏感操作前获得人工审批。敏感操作包括：
- 写入文件
- 删除文件
- 运行命令

启用方式：
```java
config.setHitlEnabled(true);
```

### Q: 文件操作安全吗？

**A:** EST Code CLI 有多层安全措施：
1. 工作目录限制 - 只能访问指定目录下的文件
2. HITL 审批 - 敏感操作需要确认
3. 操作日志 - 所有文件操作都有记录

建议：
- 始终设置明确的工作目录
- 生产环境启用 HITL
- 不要用管理员权限运行

### Q: 会收集我的代码数据吗？

**A:** EST Code CLI 是本地运行的工具，不会自动收集或上传你的代码。代码分析通过你配置的 LLM API 进行，请参考相关 LLM 服务的隐私政策。

### Q: Web 模式安全吗？

**A:** 默认 Web 服务器只监听 localhost，不对外暴露。生产环境使用时建议：
- 使用 HTTPS
- 配置认证
- 限制访问 IP
- 使用反向代理（如 Nginx）

---

## 性能问题

### Q: 大型项目索引很慢怎么办？

**A:** 优化建议：
1. 排除不需要的目录
2. 只索引源代码文件
3. 使用增量索引而不是全量重新索引
4. 考虑分批索引

```java
// 只索引 Java 文件，排除构建目录
Set<String> extensions = Set.of("java");
Set<String> excludePaths = Set.of("target", "build", ".git", "node_modules");
```

### Q: 内存占用太高怎么办？

**A:** 
1. 减少索引的文件数量
2. 分批处理大项目
3. 增加 JVM 堆内存：
```bash
java -Xmx2g -jar est-code-cli.jar
```

### Q: 搜索响应慢怎么办？

**A:** 
1. 使用更严格的筛选器
2. 减少最大结果数
3. 缓存常用搜索结果
4. 设置最小得分阈值

---

## 故障排除

### Q: 启动时报错 "ClassNotFoundException"

**A:** 确保：
1. 使用正确的 Java 版本（17+）
2. Maven 构建成功完成
3. 使用正确的 JAR 文件路径

### Q: 配置文件不生效

**A:** 检查：
1. 配置文件位置是否正确
2. 配置文件格式是否正确（YAML）
3. 是否有多个配置文件冲突
4. 配置修改后是否保存

### Q: LLM API 调用失败

**A:** 检查：
1. 网络连接是否正常
2. API Key 是否正确配置
3. API 服务是否可用
4. 是否达到 API 配额限制

### Q: 搜索没有结果

**A:** 检查：
1. 文件是否已被索引
2. 搜索关键词是否正确
3. 筛选器是否过于严格
4. 尝试使用模糊搜索

### Q: 如何获取更多帮助？

**A:** 
1. 查看 `README.md`
2. 查看 `docs/` 目录下的文档
3. 查看使用案例 `docs/usage-examples.md`
4. 查看最佳实践 `docs/best-practices.md`

---

## 更多资源

- [项目 README](../README.md)
- [使用案例](./usage-examples.md)
- [最佳实践](./best-practices.md)
- [路线图](../ROADMAP.md)

---

*文档版本: 1.0.0*  
*最后更新: 2026-03-09*
