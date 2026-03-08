# est-cli - 小白从入门到精通

## 目录
- [什么是 est-cli](#什么是-est-cli)
- [快速入门](#快速入门)
- [核心功能](#核心功能)
- [模块结构](#模块结构)
- [相关资源](#相关资源)

---

## 什么是 est-cli

### 用大白话理解
est-cli 就像"命令行工具箱"，通过命令行快速创建项目、生成代码、运行任务。

### 核心特点
- **项目脚手架**：一键创建 EST 项目
- **代码生成**：按模板生成代码
- **命令管理**：自定义命令扩展
- **交互式**：友好的交互体验

---

## 快速入门

### 1. 安装 CLI
```bash
# 下载并安装
curl -sSL https://est.example.com/install.sh | bash

# 验证安装
est --version
```

### 2. 创建项目
```bash
# 创建新项目
est new my-project

# 进入项目
cd my-project

# 运行项目
est run
```

---

## 核心功能

### 常用命令
```bash
# 创建项目
est new <project-name>

# 生成代码
est generate controller User

# 运行应用
est run

# 打包构建
est build

# 数据库迁移
est migrate

# 查看帮助
est help
```

### 自定义命令
```java
@Command(name = "my-command", description = "我的自定义命令")
public class MyCommand {
    
    @Execute
    public void execute() {
        System.out.println("执行自定义命令");
    }
}
```

---

## 模块结构

```
est-cli/
├── src/main/java/
│   └── ltd/idcu/est/tools/cli/
│       ├── commands/
│       └── EstCliMain.java
├── README.md
└── pom.xml
```

---

## 相关资源

- [父模块文档](../README.md)
- [代码生成](../est-codegen/README.md)
