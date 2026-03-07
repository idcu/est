
# 🚀 EST 2.0.0 - 5分钟快速开始

欢迎使用 EST 框架！本指南将帮助你在 5 分钟内开始使用 EST。

---

## 📋 前置要求

在开始之前，请确保你的电脑已安装：

- **JDK 21 或更高版本**
  - 检查：打开终端输入 `java -version`
- **Maven 3.6 或更高版本**
  - 检查：打开终端输入 `mvn -version`

---

## ⚡ 快速开始（Windows 用户）

### 方式一：使用快速开始脚本（推荐）

1. **双击运行** `quickstart.bat`
2. **选择选项 1** 构建项目
3. **构建完成后**，选择其他选项运行示例

就是这么简单！🎉

### 方式二：手动操作

#### 1. 构建项目

```bash
mvn clean install -DskipTests
```

#### 2. 运行演示应用

```bash
cd est-demo
run.bat
```

或者直接在 IDE 中运行：
- 打开 `est-demo/src/main/java/ltd/idcu/est/demo/EstDemoApplication.java`
- 右键点击 `main` 方法，选择 "Run"

---

## 🎯 可用的示例

EST 框架提供了丰富的示例供你学习：

### 1. EST Demo（推荐新手）
- **位置**: `est-demo/`
- **运行**: 双击 `est-demo/run.bat`
- **功能**: 完整的 Web 应用演示，包含用户管理、Todo、缓存、事件等
- **访问**: http://localhost:8080

### 2. Web 基础示例
- **位置**: `est-examples/est-examples-web/`
- **运行**: 双击 `est-examples/est-examples-web/run-basic-web.bat`
- **功能**: 学习 Web 开发基础
- **访问**: http://localhost:8080

### 3. 基础示例
- **位置**: `est-examples/est-examples-basic/`
- **运行**: 双击 `est-examples/est-examples-basic/run-basic-example.bat`
- **功能**: 学习核心容器、配置管理等基础功能

### 4. 微服务示例
- **位置**: `est-examples/est-examples-microservices/`
- **运行**: 双击 `est-examples/est-examples-microservices/run-microservices.bat`
- **功能**: 学习微服务架构（用户服务、订单服务、API网关）

---

## 📚 下一步学习

### 1. 阅读文档
- [环境准备指南](docs/getting-started/setup.md) - 详细的环境搭建说明
- [快速开始指南](docs/guides/getting-started.md) - 深入学习框架
- [教程系列](docs/tutorials/) - 从入门到高级的完整教程

### 2. 查看示例代码
- `est-examples/` 目录包含各种示例
- 每个示例都有详细的注释和说明

### 3. API 文档
- [API 文档中心](docs/README.md) - 完整的 API 参考

---

## 💡 小贴士

1. **首次使用**：建议先运行 EST Demo，体验完整功能
2. **遇到问题**：先看看示例代码中有没有类似的用法
3. **IDE 开发**：推荐使用 IntelliJ IDEA，体验更好
4. **构建失败**：确保 JDK 和 Maven 版本符合要求

---

## 🤝 获取帮助

- 查看 [FAQ](docs/faq/README.md)
- 阅读 [最佳实践](docs/best-practices/README.md)
- 查看 [示例代码](est-examples/)

---

## 🎉 开始吧！

现在，双击 `quickstart.bat`，开始你的 EST 之旅！

祝你使用 EST 框架愉快！🚀

