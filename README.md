# EST

EST 是一个**零依赖**的现代 Java 框架，采用递进式模块结构设计，特别适合初学者和 AI 开发者进行代码生成。

- **版本**: 1.3.0-SNAPSHOT
- **项目**: EST
- **作者**: idcu
- **许可证**: MIT License

---

## 🌟 什么是 EST 框架？

EST 是一个专为 Java 开发者设计的轻量级企业级应用开发框架。它的特点是：

- **零依赖**：不需要任何第三方库，所有功能都用 Java 标准库实现
- **简单易用**：API 设计简洁直观，几分钟就能上手
- **功能强大**：包含 Web 开发、缓存、日志、数据库访问等企业级功能
- **模块化设计**：想用哪个功能就引入哪个模块，不会有多余的代码

---

## 🚀 快速开始（3 分钟上手）

### 第一步：环境准备

👉 [**小白友好环境准备完整指南**](docs/setup.md) - 从零开始，手把手教你安装 JDK、Maven、Git 和 IDE

**快速检查：** 确保你的电脑安装了：
- **JDK 21 或更高版本**（必须）
- **Maven 3.6 或更高版本**（用来构建项目）

### 第二步：构建项目

```bash
# 克隆项目
git clone https://github.com/idcu/est.git
cd est

# 构建所有模块
mvn clean install

# 跳过测试（更快）
mvn clean install -DskipTests
```

### 第三步：第一个 Web 应用

只需要几行代码，你就能创建一个运行在 8080 端口的 Web 服务器：

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class HelloWorld {
    public static void main(String[] args) {
        // 1. 创建一个 Web 应用
        WebApplication app = Web.create("我的第一个应用", "1.0.0");
        
        // 2. 添加路由
        app.get("/", (req, res) -> res.send("Hello, World!"));
        
        // 3. 启动服务器
        app.run(8080);
    }
}
```

运行这个程序，然后在浏览器打开 `http://localhost:8080` 试试吧！

---

## 📦 核心特性

### 1. 零依赖设计

EST 框架不依赖任何第三方库，所有功能都是用 Java 标准库实现的：

| 功能 | 实现方式 |
|------|----------|
| JSON处理 | javax.json 包 |
| XML处理 | javax.xml 包 |
| 加密 | javax.crypto 包 |
| 数据库访问 | java.sql 包 |
| 并发编程 | java.util.concurrent 包 |
| HTTP服务器 | com.sun.net.httpserver 包 |

### 2. Web 开发

EST 提供了完整的 Web 开发功能，包括路由、请求响应处理、模板引擎等。

### 3. 依赖注入容器

简单但强大的依赖注入容器，帮助你管理组件。

### 4. 配置管理

轻松管理应用配置。

---

## 📁 项目结构

EST 采用清晰的递进式模块设计：

```
est/
├── est-core/              # 核心模块（依赖注入、配置管理等）
├── est-foundation/        # 基础设施层
│   ├── est-patterns/      # 设计模式
│   ├── est-utils/         # 工具模块（JSON、XML、IO等）
│   ├── est-test/          # 测试框架
│   └── est-collection/    # 集合增强
├── est-features/          # 企业级功能
│   ├── est-features-cache/     # 缓存
│   ├── est-features-logging/   # 日志
│   ├── est-features-data/      # 数据访问
│   ├── est-features-security/  # 安全认证
│   ├── est-features-messaging/ # 消息系统
│   ├── est-features-monitor/   # 监控
│   ├── est-features-scheduler/ # 调度
│   ├── est-features-ai/        # AI助手
│   └── ...
├── est-extensions/        # 扩展层
│   ├── est-plugin/        # 插件系统
│   └── est-web/           # Web 框架
├── est-tools/             # 工具层
│   └── est-scaffold/      # 脚手架生成器
├── est-examples/          # 示例代码
├── deploy/                # 部署配置
├── .github/               # GitHub Actions CI/CD
└── docs/                  # 文档
```

---

## 📚 文档导航

### 入门指南
- [环境准备与安装](docs/setup.md) - 从零开始搭建开发环境
- [快速开始](docs/guides/getting-started.md) - 创建你的第一个应用
- [部署指南](docs/deployment.md) - 本地运行、Docker、Kubernetes 部署

### 教程系列
- [入门教程](docs/tutorials/beginner/) - 适合初学者
- [Web 开发教程](docs/tutorials/web/) - 学习 Web 开发

### API 参考
- [API 文档中心](docs/README.md) - 完整的文档导航

### 更多
- [示例代码](est-examples/) - 丰富的示例代码
- [最佳实践](docs/best-practices/) - 代码组织、性能优化、安全等
- [AI 开发者专区](docs/AI_CODER_GUIDE.md) - 专为 AI 设计的指南

---

## 🤝 贡献

欢迎贡献代码！请查看 [贡献指南](docs/CONTRIBUTING.md) 了解如何参与项目开发。

---

## 📄 许可证

[MIT License](LICENSE)

---

**祝你使用 EST 框架愉快！** 🎉
