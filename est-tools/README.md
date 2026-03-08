# EST Tools 工具模块 - 小白从入门到精通
## 目录
1. [什么是 EST Tools？](#什么是-est-tools)
2. [快速入门：5分钟上手](#快速入门-5分钟上手)
3. [核心组件](#核心组件)
4. [进阶指南](#进阶指南)
5. [最佳实践](#最佳实践)

---

## 什么是 EST Tools？
### 用大白话理解

EST Tools 就像是一个「工具箱」。想象一下你要做木工，需要各种工具：锤子、电锯、钉子、尺子...

**传统方式**：每次做东西都要自己准备工具，很麻烦。
**EST Tools 方式**：给你一个装满工具的工具箱，里面有：
- 🧰 **脚手架** - 快速创建项目
- 📝 **代码生成** - 根据数据库生成代码
- 🔄 **迁移工具** - 从其他框架迁移
- 🖥️ **命令行工具** - CLI 命令行工具
### 核心特点

- 📦 **简单易用** - 图形化界面，开箱即用
- 🔧 **快速高效** - 一键生成，节省时间
- 👨‍💻 **灵活配置** - 可自定义模板
- 🚀 **多种工具** - 脚手架、代码生成、迁移等

---

## 快速入门：5分钟上手

### 第一步：引入依赖

在你的 Maven pom.xml 中添加：

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-scaffold</artifactId>
        <version>2.1.0</version>
    </dependency>
</dependencies>
```

### 第二步：使用脚手架创建项目
```java
import ltd.idcu.est.scaffold.ScaffoldGenerator;
import ltd.idcu.est.scaffold.ProjectConfig;
import ltd.idcu.est.scaffold.ProjectType;

public class FirstScaffold {
    public static void main(String[] args) {
        System.out.println("=== EST Tools 第一个示例 ===\n");
        
        ProjectConfig config = new ProjectConfig();
        config.setProjectName("my-project");
        config.setGroupId("com.example");
        config.setArtifactId("my-app");
        config.setVersion("1.0.0");
        config.setProjectType(ProjectType.WEB);
        
        ScaffoldGenerator generator = new ScaffoldGenerator();
        generator.generate(config, Paths.get("./my-project"));
        
        System.out.println("项目已生成：./my-project");
    }
}
```

或者使用 Web 界面：
```java
import ltd.idcu.est.scaffold.ScaffoldWebServer;

public class ScaffoldWebApp {
    public static void main(String[] args) {
        ScaffoldWebServer server = new ScaffoldWebServer();
        server.start(8080);
        System.out.println("脚手架 Web 界面已启动：http://localhost:8080");
    }
}
```

---

## 核心组件
### 1. est-scaffold 脚手架
详细文档请参考：[est-scaffold README](./est-scaffold/README.md)

#### 项目类型

EST Scaffold 支持多种项目类型：
```java
import ltd.idcu.est.scaffold.ProjectType;

// Web 应用
ProjectType.WEB

// API 应用
ProjectType.API

// 基础应用
ProjectType.BASIC

// 命令行应用
ProjectType.CLI

// 微服务应用
ProjectType.MICROSERVICE

// 库项目
ProjectType.LIBRARY

// 插件项目
ProjectType.PLUGIN
```

#### 生成项目

```java
import ltd.idcu.est.scaffold.ScaffoldGenerator;
import ltd.idcu.est.scaffold.ProjectConfig;

ProjectConfig config = new ProjectConfig();
config.setProjectName("My Web App");
config.setGroupId("com.example");
config.setArtifactId("my-web-app");
config.setVersion("1.0.0");
config.setPackageName("com.example.myapp");
config.setProjectType(ProjectType.WEB);
config.setDescription("我的 Web 应用");
config.setAuthor("Your Name");

// 添加依赖
config.addDependency("est-web");
config.addDependency("est-data-jdbc");
config.addDependency("est-cache");

// 生成项目
ScaffoldGenerator generator = new ScaffoldGenerator();
generator.generate(config, Paths.get("./my-web-app"));
```

#### 自定义模板
```java
import ltd.idcu.est.scaffold.TemplateEngine;
import ltd.idcu.est.scaffold.TemplateFileSystem;

// 加载自定义模板
TemplateFileSystem templateFs = new TemplateFileSystem(Paths.get("./my-templates"));
TemplateEngine engine = new TemplateEngine(templateFs);

// 使用自定义模板生成
engine.render("my-template.ftl", Map.of("name", "张三"));
```

### 2. est-codegen 代码生成

EST CodeGen 用于根据数据库表生成代码。
```java
import ltd.idcu.est.codegen.CodeGenerator;
import ltd.idcu.est.codegen.db.DatabaseCodeGenerator;
import ltd.idcu.est.codegen.pojo.PojoGenerator;

// 从数据库生成代码
DatabaseCodeGenerator dbGenerator = new DatabaseCodeGenerator();
dbGenerator.setJdbcUrl("jdbc:mysql://localhost:3306/mydb");
dbGenerator.setUsername("root");
dbGenerator.setPassword("password");
dbGenerator.setPackageName("com.example.entity");
dbGenerator.setOutputDir(Paths.get("./src/main/java"));

dbGenerator.generate();

// 生成 POJO
PojoGenerator pojoGenerator = new PojoGenerator();
pojoGenerator.setClassName("User");
pojoGenerator.setPackageName("com.example.entity");
pojoGenerator.addField("id", "Long");
pojoGenerator.addField("name", "String");
pojoGenerator.addField("email", "String");

String code = pojoGenerator.generate();
System.out.println(code);
```

### 3. est-migration 迁移工具

EST Migration 用于从其他框架迁移到 EST。
```java
import ltd.idcu.est.migration.MigrationEngine;
import ltd.idcu.est.migration.MigrationConfig;
import ltd.idcu.est.migration.rules.SpringBootAnnotationMigrationRule;
import ltd.idcu.est.migration.rules.SpringBootImportMigrationRule;

// 配置迁移
MigrationConfig config = new MigrationConfig();
config.setSourceDir(Paths.get("./spring-boot-app"));
config.setTargetDir(Paths.get("./est-app"));
config.setSourceFramework("spring-boot");
config.setTargetFramework("est");

// 创建迁移引擎
MigrationEngine engine = new MigrationEngine(config);

// 添加迁移规则
engine.addRule(new SpringBootAnnotationMigrationRule());
engine.addRule(new SpringBootImportMigrationRule());
engine.addRule(new SpringBootMainClassMigrationRule());

// 执行迁移
MigrationResult result = engine.migrate();
System.out.println("迁移完成: " + result.getMigratedFiles() + " 个文件");
```

### 4. est-cli 命令行工具
EST CLI 提供命令行工具。
```java
import ltd.idcu.est.cli.EstCliMain;

public class CliApp {
    public static void main(String[] args) {
        EstCliMain.main(args);
    }
}
```

使用命令行：

```bash
# 创建新项目
java -jar est-cli.jar create --name my-app --type web

# 生成代码
java -jar est-cli.jar generate --entity User --fields id:Long,name:String

# 迁移项目
java -jar est-cli.jar migrate --source ./spring-app --target ./est-app
```

---

## 进阶指南
### 1. 脚手架进阶
详细内容请参考：[est-scaffold 进阶指南](./est-scaffold/README.md)

#### 创建自定义项目类型
```java
import ltd.idcu.est.scaffold.ProjectType;
import ltd.idcu.est.scaffold.ScaffoldGenerator;

// 注册自定义项目类型
ProjectType customType = new ProjectType("custom", "自定义项目");
ScaffoldGenerator.registerProjectType(customType);
```

#### 代码片段

```java
import ltd.idcu.est.scaffold.CodeSnippetGenerator;

CodeSnippetGenerator snippetGen = new CodeSnippetGenerator();

// 生成 Controller 代码
String controllerCode = snippetGen.generateController("User", "com.example.controller");

// 生成 Service 代码
String serviceCode = snippetGen.generateService("User", "com.example.service");

// 生成 Repository 代码
String repoCode = snippetGen.generateRepository("User", "com.example.repository");
```

### 2. 代码生成进阶

#### 自定义代码模板
```java
import ltd.idcu.est.codegen.CodeGenerator;

// 自定义 Entity 模板
String entityTemplate = """
    package ${packageName};
    
    public class ${className} {
        ${fields}
        
        ${gettersSetters}
    }
    """;

CodeGenerator generator = new CodeGenerator();
generator.setEntityTemplate(entityTemplate);
```

### 3. 迁移工具进阶

#### 自定义迁移规则
```java
import ltd.idcu.est.migration.AbstractMigrationRule;
import ltd.idcu.est.migration.MigrationResult;

public class MyMigrationRule extends AbstractMigrationRule {
    
    @Override
    public MigrationResult apply(String content) {
        String migrated = content
            .replace("@Autowired", "@Inject")
            .replace("@RestController", "@Component");
        
        return MigrationResult.success(migrated);
    }
}
```

---

## 最佳实践
### 1. 使用脚手架快速开始
```java
// ✅ 推荐：使用脚手架创建项目
ProjectConfig config = new ProjectConfig();
config.setProjectType(ProjectType.WEB);
generator.generate(config, outputDir);

// ❌ 不推荐：手动创建所有文件
// 容易出错，效率低
```

### 2. 合理使用代码生成

```java
// ✅ 推荐：生成基础代码，然后手动修改
String entityCode = generator.generateEntity("User");
// 然后添加自定义业务逻辑

// ❌ 不推荐：完全依赖代码生成
// 生成的代码可能不符合特定需求
```

### 3. 迁移前备份
```java
// ✅ 推荐：迁移前备份原项目
Path sourceDir = Paths.get("./old-project");
Path backupDir = Paths.get("./old-project-backup");
Files.copy(sourceDir, backupDir);

// 然后执行迁移
engine.migrate();
```

---

## 模块结构

```
est-tools/
├── est-scaffold/     # 脚手架生成器
├── est-codegen/      # 代码生成器
├── est-migration/    # 迁移工具
└── est-cli/          # 命令行工具
```

---

## 相关资源

- [est-scaffold README](./est-scaffold/README.md) - 脚手架详细文档
- [示例代码](../est-examples/est-examples-basic/) - 基础示例
- [EST App](../est-app/README.md) - 应用模块
- [EST Core](../est-core/README.md) - 核心模块

---

**文档版本**：2.0  
**最后更新**：2026-03-08