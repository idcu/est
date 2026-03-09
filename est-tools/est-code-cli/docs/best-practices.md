# EST Code CLI 最佳实践

## 目录
- [配置管理最佳实践](#配置管理最佳实践)
- [搜索功能最佳实践](#搜索功能最佳实践)
- [技能使用最佳实践](#技能使用最佳实践)
- [对话交互最佳实践](#对话交互最佳实践)
- [安全最佳实践](#安全最佳实践)
- [性能优化最佳实践](#性能优化最佳实践)

---

## 配置管理最佳实践

### 1. 配置初始化

**推荐做法：**
```bash
# 首次使用时使用合适的模板初始化
# 开发人员推荐使用 developer 模板
est-code-cli
> config
# 选择配置模板或手动配置后立即保存
```

**避免：**
- 不要使用默认配置进行生产环境工作
- 不要忘记保存配置更改

### 2. 定期验证配置

**推荐做法：**
```java
// 每次修改配置后进行验证
CliConfig config = CliConfig.load();
ConfigValidator.ValidationResult result = config.validate();

if (!result.isValid()) {
    System.err.println("配置验证失败:");
    result.getErrors().forEach(err -> System.err.println("  - " + err));
    System.exit(1);
}

if (result.hasWarnings()) {
    System.out.println("配置警告:");
    result.getWarnings().forEach(warn -> System.out.println("  - " + warn));
}
```

### 3. 配置备份策略

**推荐做法：**
```java
ConfigVersionManager versionManager = new ConfigVersionManager("est-code-cli.yml");

// 每次重要配置变更前创建备份
versionManager.createBackup();

// 定期清理旧备份，保留最近10个
versionManager.deleteOldBackups(10);

// 导出配置到版本控制系统
config.exportTo(Paths.get("config/est-code-cli.prod.yml"));
```

### 4. 多环境配置管理

**推荐做法：**
```java
// 为不同环境准备不同的配置模板
ConfigTemplate devTemplate = ConfigTemplate.createDeveloper();
ConfigTemplate secureTemplate = ConfigTemplate.createSecure();

// 导出配置文件
CliConfig devConfig = CliConfig.fromTemplate(devTemplate);
devConfig.setWorkDir(Paths.get("/projects/dev"));
devConfig.exportTo(Paths.get("config/development.yml"));

CliConfig prodConfig = CliConfig.fromTemplate(secureTemplate);
prodConfig.setWorkDir(Paths.get("/projects/prod"));
prodConfig.exportTo(Paths.get("config/production.yml"));
```

---

## 搜索功能最佳实践

### 1. 索引优化

**推荐做法：**
```java
FileIndex index = new FileIndex();

// 只索引源代码文件，排除不需要的文件
Set<String> sourceExtensions = Set.of("java", "js", "ts", "py", "go", "rs");
Set<String> excludePaths = Set.of("target", "build", "node_modules", ".git");

try (Stream<Path> paths = Files.walk(projectDir)) {
    paths
        .filter(Files::isRegularFile)
        .filter(path -> {
            String fileName = path.toString().toLowerCase();
            return sourceExtensions.stream().anyMatch(ext -> fileName.endsWith("." + ext));
        })
        .filter(path -> {
            String pathStr = path.toString();
            return excludePaths.stream().noneMatch(exclude -> pathStr.contains(exclude));
        })
        .forEach(path -> {
            try {
                String content = Files.readString(path);
                index.indexFile(path.toString(), content);
            } catch (IOException e) {
                System.err.println("无法索引: " + path);
            }
        });
}
```

### 2. 搜索策略

**推荐做法：**
```java
// 阶段1: 精确搜索
SearchFilter exactFilter = new SearchFilter()
    .setCaseSensitive(false)
    .setMaxResults(20);

List<FileIndex.SearchResult> exactResults = index.search("UserService", exactFilter);

if (exactResults.isEmpty()) {
    // 阶段2: 模糊搜索
    SearchFilter fuzzyFilter = new SearchFilter()
        .setFuzzySearch(true)
        .setFuzzyDistance(2)
        .setMaxResults(20);
    
    List<FileIndex.SearchResult> fuzzyResults = index.search("UserServce", fuzzyFilter);
}

// 阶段3: 扩大搜索范围
SearchFilter broadFilter = SearchFilter.sourceFilesOnly()
    .setMinScore(1)
    .setMaxResults(50);

List<FileIndex.SearchResult> broadResults = index.search("user", broadFilter);
```

### 3. 使用预设筛选器

**推荐做法：**
```java
// Java项目搜索
SearchFilter javaFilter = SearchFilter.javaFilesOnly()
    .addExcludePath("test")
    .setMaxResults(30);

// Web前端项目搜索
SearchFilter webFilter = new SearchFilter()
    .addIncludeExtension("js")
    .addIncludeExtension("ts")
    .addIncludeExtension("tsx")
    .addIncludeExtension("vue")
    .addExcludePath("node_modules")
    .setMaxResults(30);

// 生产代码搜索（排除测试）
SearchFilter prodFilter = SearchFilter.excludeTestFiles()
    .setMinScore(3)
    .setMaxResults(20);
```

### 4. 搜索历史利用

**推荐做法：**
```java
SearchHistory history = index.getSearchHistory();

// 查看最近常用查询
List<String> recent = history.getRecentQueries(10);
System.out.println("常用查询: " + recent);

// 复用之前的有效查询
Optional<SearchHistory.SearchEntry> bestMatch = history.findSimilar("service")
    .stream()
    .max(Comparator.comparingInt(SearchHistory.SearchEntry::getResultCount));

if (bestMatch.isPresent()) {
    System.out.println("推荐查询: " + bestMatch.get().getQuery());
}
```

---

## 技能使用最佳实践

### 1. 技能选择策略

**推荐做法：**
- **代码审查**：新代码提交前、PR审查时使用
- **重构**：代码异味明显、需要改进结构时使用
- **架构分析**：项目初期、大规模重构前使用
- **性能优化**：发现性能瓶颈、进行性能调优时使用
- **安全审计**：敏感代码、生产代码发布前使用

### 2. 代码审查技能使用

**推荐做法：**
```bash
# 使用代码审查技能时提供完整的上下文
> 请帮我审查这个类
💡 检测到您可能需要使用技能: code_review
是否要使用该技能？(y/n) > y

请提供要处理的代码文件路径，或直接粘贴代码：
> src/main/java/com/example/UserService.java
> 这是我们的核心用户服务类，主要处理用户CRUD操作
```

**审查后处理：**
1. 优先处理高优先级问题
2. 对于建议，评估实际影响后再实施
3. 保留代码的业务逻辑正确性
4. 审查修改后再次运行审查验证

### 3. 组合使用技能

**推荐工作流：**
```
1. 架构分析技能 → 了解整体结构
2. 代码审查技能 → 发现具体问题
3. 重构技能 → 改进代码结构
4. 性能优化技能 → 优化性能
5. 安全审计技能 → 检查安全性
6. 代码审查技能 → 最终验证
```

### 4. 技能结果记录

**推荐做法：**
```java
// 编程方式使用技能并记录结果
SkillManager skillManager = new SkillManager();

EstSkill codeReview = skillManager.getSkill("code_review");
String reviewResult = codeReview.execute(codeContent);

// 保存审查结果
Files.writeString(
    Paths.get("docs/code-review-" + System.currentTimeMillis() + ".md"),
    reviewResult
);
```

---

## 对话交互最佳实践

### 1. 上下文管理

**推荐做法：**
```bash
# 相关任务保持在同一会话中
> 帮我创建用户服务类
[创建 UserService.java]

> 请添加分页查询方法
[在 UserService.java 中添加方法]

> 再添加删除方法
[继续添加]

# 切换不相关任务时清除上下文
> clear
> 现在帮我写一个工具类
```

### 2. 提示语技巧

**有效提示示例：**
```bash
# 好的提示 - 具体明确
> "请创建一个Spring Boot的REST Controller，处理用户CRUD操作，使用JPA Repository，包含异常处理和验证"

# 更好的提示 - 包含约束
> "创建一个用户服务类，要求：
   1. 使用Spring框架
   2. 包含完整的单元测试
   3. 遵循SOLID原则
   4. 包含日志记录
   5. 线程安全"

# 避免的提示 - 过于模糊
> "帮我写点代码"
```

### 3. 多轮对话优化

**推荐做法：**
```bash
# 第一轮 - 提出整体需求
> "我需要创建一个订单管理系统，包含订单、商品、用户三个核心实体"

# 第二轮 - 细化第一个组件
> "先创建订单实体类，包含订单号、用户ID、商品列表、总价、状态等字段"

# 第三轮 - 添加功能
> "现在为订单实体添加JPA注解和验证注解"

# 第四轮 - 创建服务层
> "创建订单服务类，实现订单的创建、查询、更新、取消功能"
```

### 4. 错误处理

**推荐做法：**
```bash
# 遇到错误时提供清晰的反馈
> "刚才生成的代码编译失败了，错误信息是：
   找不到符号: 方法 getUserById(Long)
   位置: 类 com.example.UserRepository"

# 或者提供更多上下文
> "这段代码在运行时抛出NullPointerException，堆栈跟踪显示在第45行，
   这是user对象可能为null的位置"
```

---

## 安全最佳实践

### 1. HITL（Human-in-the-Loop）安全策略

**推荐做法：**
```java
// 生产环境始终启用HITL
CliConfig config = CliConfig.load();
config.setHitlEnabled(true);
config.save();

// 配置敏感操作审批
HitlSecurityPolicy policy = new HitlSecurityPolicy();
policy.requireApprovalFor(HitlSecurityPolicy.Operation.WRITE_FILE);
policy.requireApprovalFor(HitlSecurityPolicy.Operation.DELETE_FILE);
policy.requireApprovalFor(HitlSecurityPolicy.Operation.RUN_COMMAND);
```

### 2. 文件访问安全

**推荐做法：**
```java
// 限制工作目录
config.setWorkDir(Paths.get("/safe/work/directory"));

// 不要在配置中存储敏感信息
// 使用环境变量或安全的密钥管理服务
String apiKey = System.getenv("EST_API_KEY");
```

### 3. 命令执行安全

**推荐做法：**
```bash
# 谨慎使用运行命令功能
> test
[运行测试前查看将要执行的命令]

# 避免执行未知或危险命令
> compile
# 只执行经过验证的构建命令
```

### 4. 网络安全

**推荐做法：**
```bash
# Web模式使用安全配置
> web
Enter port (default 8080): 8080
# 在生产环境中：
# - 使用HTTPS
# - 配置认证
# - 限制访问IP
# - 使用反向代理
```

---

## 性能优化最佳实践

### 1. 索引性能优化

**推荐做法：**
```java
// 增量索引而不是全量重新索引
FileIndex index = new FileIndex();

// 只索引变更的文件
Set<Path> allFiles = collectAllFiles(projectDir);
Set<Path> indexedFiles = index.getIndexedFiles();

Set<Path> newFiles = new HashSet<>(allFiles);
newFiles.removeAll(indexedFiles);

Set<Path> deletedFiles = new HashSet<>(indexedFiles);
deletedFiles.removeAll(allFiles);

// 添加新文件
for (Path file : newFiles) {
    index.indexFile(file.toString(), Files.readString(file));
}

// 删除旧文件
for (Path file : deletedFiles) {
    index.removeFile(file.toString());
}
```

### 2. 搜索性能优化

**推荐做法：**
```java
// 使用筛选器减少搜索范围
SearchFilter efficientFilter = new SearchFilter()
    .addIncludeExtension("java")
    .addExcludePath("target")
    .addExcludePath("build")
    .setMinScore(2)
    .setMaxResults(20);

// 分页获取结果
List<FileIndex.SearchResult> allResults = index.search("query", efficientFilter);
int pageSize = 10;
int page = 1;
List<FileIndex.SearchResult> pageResults = allResults.stream()
    .skip((page - 1) * pageSize)
    .limit(pageSize)
    .collect(Collectors.toList());
```

### 3. 内存管理

**推荐做法：**
```java
// 对于大型项目，考虑分段索引
List<Path> fileBatches = partitionFiles(allFiles, 100);

for (List<Path> batch : fileBatches) {
    FileIndex batchIndex = new FileIndex();
    for (Path file : batch) {
        batchIndex.indexFile(file.toString(), Files.readString(file));
    }
    // 处理批次结果
    // ...
    // 批次处理完后释放内存
}
```

### 4. 缓存策略

**推荐做法：**
```java
// 缓存常用搜索结果
Map<String, List<FileIndex.SearchResult>> searchCache = new LinkedHashMap<>() {
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > 100;
    }
};

public List<FileIndex.SearchResult> cachedSearch(String query, SearchFilter filter) {
    String cacheKey = query + ":" + filter.hashCode();
    if (searchCache.containsKey(cacheKey)) {
        return searchCache.get(cacheKey);
    }
    List<FileIndex.SearchResult> results = index.search(query, filter);
    searchCache.put(cacheKey, results);
    return results;
}
```

---

## 团队协作最佳实践

### 1. 共享配置

**推荐做法：**
```bash
# 在团队中共享配置模板
# 将配置模板提交到版本控制
config/
  ├── team-template.yml
  └── README.md

# 新成员可以导入配置
est-code-cli
> config
# 使用导入功能加载团队模板
```

### 2. 统一工作流

**推荐做法：**
```
1. 创建功能分支
2. 启动 EST Code CLI
3. 使用架构分析技能了解项目
4. 开发新功能
5. 使用代码审查技能检查代码
6. 运行测试
7. 提交代码
8. 创建PR
```

### 3. 文档协作

**推荐做法：**
- 记录常用的提示语模板
- 分享成功的技能使用案例
- 维护项目特定的最佳实践
- 定期更新配置模板

---

*文档版本: 1.0.0*  
*最后更新: 2026-03-09*
