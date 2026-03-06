# EST [项目名称]

EST 是一个**零依赖**的现代 Java 框架，采用递进式模块结构设计，特别适合初学者和 AI 开发者进行代码生成。

- **版本**: 2.0.0
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

### 第三步：第一个 [应用类型] 应用

只需要几行代码，你就能创建一个运行在 [端口] 端口的 [应用类型]：

```java
import ltd.idcu.est.[应用类型].[入口类];
import ltd.idcu.est.[应用类型].api.[应用接口];

public class HelloWorld {
    public static void main(String[] args) {
        // 1. 创建一个 [应用类型] 应用
        [应用接口] app = [入口类].create("我的第一个应用", "1.0.0");
        
        // 2. 添加路由/功能
        app.[方法]("/", (req, res) -> res.send("Hello, World!"));
        
        // 3. 启动服务器
        app.run([端口]);
    }
}
```

运行这个程序，然后在浏览器打开 `http://localhost:[端口]` 试试吧！

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

### 2. [核心特性1]

[核心特性1描述]

### 3. [核心特性2]

[核心特性2描述]

### 4. [核心特性3]

[核心特性3描述]

---

## 📁 项目结构

EST 采用清晰的递进式模块设计：

```
est/
├── est-base/              # 基础层
│   ├── est-patterns/      # 设计模式
│   ├── est-utils/         # 工具模块（JSON、XML、IO等）
│   ├── est-test/          # 测试框架
│   └── est-collection/    # 集合增强
├── est-core/              # 核心层
│   ├── est-core-container/ # 依赖注入容器
│   ├── est-core-config/   # 核心配置
│   ├── est-core-lifecycle/ # 生命周期管理
│   ├── est-core-module/   # 模块管理
│   ├── est-core-aop/      # AOP 支持
│   └── est-core-tx/       # 事务管理
├── est-modules/           # 模块层
│   ├── est-cache/         # 缓存
│   ├── est-logging/       # 日志
│   ├── est-data/          # 数据访问
│   ├── est-security/      # 安全认证
│   ├── est-messaging/     # 消息系统
│   ├── est-monitor/       # 监控
│   ├── est-scheduler/     # 调度
│   ├── est-ai/            # AI助手
│   └── ...
├── est-app/               # 应用层
│   ├── est-web/           # Web 框架
│   ├── est-microservice/  # 微服务框架
│   └── est-console/       # 控制台应用框架
├── est-tools/             # 工具层
│   ├── est-scaffold/      # 脚手架生成器
│   ├── est-migration/     # 迁移工具
│   ├── est-codegen/       # 代码生成器
│   └── est-cli/           # 命令行工具
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
