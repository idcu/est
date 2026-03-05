# EST Scaffold - 脚手架工具模块

为 EST 框架提供项目模板生成和代码生成工具。

## 功能特性

- **多种项目模板** - 支持 8 种不同类型的项目模板
- **灵活的配置选项** - 支持自定义 groupId、包名、Java 版本等
- **交互式配置向导** - 使用友好的交互式界面创建项目
- **Git 初始化** - 可选自动初始化 Git 仓库
- **目录冲突检测** - 自动检测并防止覆盖已存在的目录
- **彩色进度输出** - 美观的进度显示和彩色输出
- **完善的测试覆盖** - 包含单元测试确保代码质量

### 支持的项目类型

| 类型 | 描述 |
|------|------|
| `basic` | 基础 EST 框架应用 |
| `web` | Web 应用项目 |
| `api` | REST API 项目 |
| `cli` | 命令行工具 |
| `library` | 库项目 |
| `plugin` | EST 插件项目 |
| `microservice` | 微服务项目 |

## 快速开始

### 编译

```bash
mvn clean install
```

### 使用

#### 快速模式

```bash
# 生成基本项目
java -jar est-scaffold.jar basic my-project

# 生成 Web 应用项目
java -jar est-scaffold.jar web my-web-app

# 生成 REST API 项目
java -jar est-scaffold.jar api my-api-service

# 生成命令行工具
java -jar est-scaffold.jar cli my-cli-tool

# 生成库项目
java -jar est-scaffold.jar library my-library

# 生成插件项目
java -jar est-scaffold.jar plugin my-plugin

# 生成微服务项目
java -jar est-scaffold.jar microservice my-microservice
```

#### 交互式模式

```bash
# 启动交互式配置向导
java -jar est-scaffold.jar interactive
```

### 高级用法 - 自定义配置

```bash
# 使用自定义 groupId
java -jar est-scaffold.jar web my-app --groupId=com.mycompany

# 使用自定义包名
java -jar est-scaffold.jar api my-api --package=myapi

# 指定 Java 版本
java -jar est-scaffold.jar basic my-project --java=17

# 初始化 Git 仓库
java -jar est-scaffold.jar web my-app --git

# 组合多个选项
java -jar est-scaffold.jar web my-app --groupId=com.mycompany --package=mywebapp --java=17 --git
```

## 命令行选项

| 选项 | 说明 | 默认值 |
|------|------|---------|
| `--groupId=<group>` | 设置项目的 groupId | `com.example` |
| `--version=<ver>` | 设置项目版本 | `1.0.0-SNAPSHOT` |
| `--package=<pkg>` | 设置自定义包名 | 从项目名自动生成 |
| `--java=<version>` | 设置 Java 版本 | `21` |
| `--git` | 初始化 Git 仓库 | `false` |

## 依赖

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-scaffold</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
```

## 模块结构

```
est-scaffold/
├── src/
│   ├── main/
│   │   └── java/ltd/idcu/est/scaffold/
│   │       ├── ScaffoldGenerator.java    # 主生成器
│   │       ├── ProjectConfig.java         # 项目配置类
│   │       ├── ProjectType.java           # 项目类型枚举
│   │       ├── ConsoleColors.java         # 控制台颜色工具
│   │       └── FileWriterUtil.java        # 文件工具类
│   └── test/
│       └── java/ltd/idcu/est/scaffold/
│           ├── ProjectConfigTest.java     # 配置类测试
│           ├── ProjectTypeTest.java       # 项目类型测试
│           └── ScaffoldTestsRunner.java   # 测试运行器
├── pom.xml
├── README.md
└── OPTIMIZATION_SUGGESTIONS.md          # 优化建议文档
```

## 运行测试

```bash
cd src/test/java/ltd/idcu/est/scaffold
javac ScaffoldTestsRunner.java
java ScaffoldTestsRunner
```

## 已完成的优化

根据 OPTIMIZATION_SUGGESTIONS.md 文档，已完成以下优化：

### 高优先级

✅ 交互式配置向导 - `interactive` 命令
✅ 更多项目模板类型 - CLI、Library、Plugin、Microservice
✅ Git 初始化 - `--git` 选项
✅ 彩色输出 - ConsoleColors 类
✅ 进度指示器 - 显示生成进度
✅ 完善的错误处理 - 参数验证和异常处理

### 中优先级

✅ 完善的单元测试 - ProjectConfigTest 和 ProjectTypeTest

### 技术改进

✅ 代码结构优化 - 删除重复代码，统一实现
✅ 类型安全 - ProjectType 枚举
✅ 灵活配置 - ProjectConfig 类

## 进一步优化建议

如需进一步优化，可参考 OPTIMIZATION_SUGGESTIONS.md 文档，其中包含：

1. 模板文件系统 - 使用外部模板文件
2. 配置文件支持 - YAML/JSON 配置
3. 代码片段生成器 - 独立的代码片段生成
4. Docker 支持 - 生成 Docker 相关文件
5. CI/CD 配置 - 生成 CI/CD 配置文件
6. 插件系统 - 允许第三方扩展模板
7. 项目升级工具 - 升级现有项目
8. dry-run 模式 - 预览功能
9. 国际化支持 - 多语言输出

## 许可证

本项目采用 MIT 许可证。
