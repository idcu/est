# EST 框架贡献指南

欢迎参与 EST 框架的开发！我们非常感谢您的贡献。

## 行为准则

在参与本项目之前，请阅读我们的 [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md)。

## 如何贡献

### 报告问题

1. 首先，搜索现有的 [Issues](../../issues)，确保没有重复的问题
2. 如果没有找到，创建一个新的 [Issue](../../issues/new/choose)，选择合适的模板

### 提交代码

1. Fork 本仓库
2. 克隆您的 Fork 仓库到本地
3. 创建一个新的分支：`git checkout -b feature/your-feature-name` 或 `git checkout -b fix/your-fix-name`
4. 进行您的修改
5. 确保代码通过所有测试：`mvn clean test`
6. 提交您的更改：`git commit -m 'feat: 添加了新功能'`（请使用约定式提交格式）
7. 推送到您的 Fork 仓库：`git push origin feature/your-feature-name`
8. 创建一个 Pull Request

## 开发环境设置

### 前提要求

- **JDK 21 或更高版本**
- **Maven 3.6 或更高版本**
- **Git**

### 克隆项目

```bash
git clone https://github.com/[your-username]/est.git
cd est
```

### 构建项目

```bash
# 完整构建（包含测试）
mvn clean install

# 快速构建（跳过测试）
mvn clean install -DskipTests

# 运行测试
mvn test

# 运行代码质量检查
mvn checkstyle:check
mvn pmd:check
mvn spotbugs:check
```

## 代码风格

我们使用以下工具来确保代码质量：

- **Checkstyle**: 代码风格检查
- **PMD**: 代码异味分析
- **SpotBugs**: Bug 检查

配置文件位于 `.config/` 目录下。

## 提交信息规范

我们使用 **约定式提交（Conventional Commits）** 规范：

```
<类型>(<范围>): <描述>

<正文>

<脚注>
```

### 类型

- `feat`: 新功能
- `fix`: Bug 修复
- `docs`: 文档更新
- `style`: 代码风格修改（不影响功能）
- `refactor`: 代码重构
- `perf`: 性能优化
- `test`: 测试相关
- `chore`: 构建/工具相关

### 示例

```
feat(ai): 添加新的 LLM 提供商支持

- 添加通用接口 LLM 集成
- 更新相关文档

Closes #123
```

## Pull Request 流程

1. **创建 PR**: 按照 [Pull Request 模板](PULL_REQUEST_TEMPLATE.md) 填写
2. **自动检查**: GitHub Actions 会自动运行测试和代码检查
3. **代码审查**: 项目维护者会进行代码审查
4. **合并**: 通过审查后，您的 PR 会被合并

## 获取帮助

如果您需要帮助，可以：
- 在 [Discussions](../../discussions) 中提问
- 查看 [文档](../docs/)
- 联系维护者

## 许可证

通过贡献代码，您同意您的贡献将根据项目的 [MIT 许可证](../LICENSE) 进行许可。

---

再次感谢您对 EST 框架的支持！🎉
