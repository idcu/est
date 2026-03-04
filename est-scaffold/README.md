# EST Scaffold - 脚手架工具模块

为 EST 框架提供项目模板生成和代码生成工具。

## 功能特性

- **基本项目模板** - 快速创建基础 EST 框架项目
- **Web 应用模板** - 快速创建 Web 应用项目
- **REST API 模板** - 快速创建 REST API 项目

## 快速开始

### 编译

```bash
mvn clean install
```

### 使用

```bash
# 生成基本项目
java -jar est-scaffold.jar new my-project

# 生成 Web 应用项目
java -jar est-scaffold.jar web my-web-app

# 生成 REST API 项目
java -jar est-scaffold.jar api my-api-service
```

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
│   │       └── ScaffoldGenerator.java
│   └── test/
└── pom.xml
```
