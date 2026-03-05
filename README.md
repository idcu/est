# EST

EST 是一个**零依赖**的现代 Java 框架，采用递进式模块结构设计，特别适合初学者和 AI 开发者进行代码生成。

- **版本**: 1.3.0-SNAPSHOT
- **项目**: EST
- **作者**: idcu
- **许可证**: MIT License

## 🌟 什么是 EST 框架？

EST 是一个专为 Java 开发者设计的轻量级企业级应用开发框架。它的特点是：

- **零依赖**：不需要任何第三方库，所有功能都用 Java 标准库实现
- **简单易用**：API 设计简洁直观，几分钟就能上手
- **功能强大**：包含 Web 开发、缓存、日志、数据库访问等企业级功能
- **模块化设计**：想用哪个功能就引入哪个模块，不会有多余的代码

## 🚀 快速开始（3 分钟上手）

### 📋 环境准备

**还没有配置开发环境？请先查看：**

👉 [**小白友好环境准备完整指南**](docs/ENVIRONMENT_PREPARATION.md) - 从零开始，手把手教你安装 JDK、Maven、Git 和 IDE

---

**快速检查：** 确保你的电脑安装了：
- **JDK 21 或更高版本**（必须）
- **Maven 3.6 或更高版本**（用来构建项目）

### 第一个 Web 应用

只需要几行代码，你就能创建一个运行在 8080 端口的 Web 服务器：

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class HelloWorld {
    public static void main(String[] args) {
        // 1. 创建一个 Web 应用，名字叫 "我的第一个应用"，版本 1.0.0
        WebApplication app = Web.create("我的第一个应用", "1.0.0");
        
        // 2. 添加一个路由：当访问 http://localhost:8080/ 时，返回 "Hello, World!"
        app.get("/", (req, res) -> {
            res.send("Hello, World!");
        });
        
        // 3. 添加另一个路由：返回 JSON 格式的数据
        app.get("/api/greeting", (req, res) -> {
            res.json(java.util.Map.of(
                "message", "你好，欢迎使用 EST 框架！",
                "version", "1.3.0"
            ));
        });
        
        // 4. 启动服务器，监听 8080 端口
        System.out.println("服务器启动了！访问 http://localhost:8080 试试吧");
        app.run(8080);
    }
}
```

运行这个程序，然后在浏览器打开 `http://localhost:8080`，你就能看到 "Hello, World!" 了！

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

这意味着：
- ✅ 不会有依赖冲突问题
- ✅ 项目体积更小
- ✅ 部署更简单
- ✅ 不需要担心第三方库的安全漏洞

### 2. Web 开发

EST 提供了完整的 Web 开发功能：

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class WebExample {
    public static void main(String[] args) {
        WebApplication app = Web.create("Web 示例", "1.0.0");
        
        // 简单路由
        app.get("/", (req, res) -> res.html("<h1>首页</h1>"));
        app.get("/about", (req, res) -> res.send("关于我们"));
        
        // 路由参数
        app.get("/user/:id", (req, res) -> {
            String userId = req.param("id");
            res.send("用户 ID: " + userId);
        });
        
        // 表单提交
        app.post("/login", (req, res) -> {
            String username = req.formParam("username");
            String password = req.formParam("password");
            res.json(java.util.Map.of("success", true, "user", username));
        });
        
        // 路由分组
        app.routes(router -> {
            router.group("/api", (r, group) -> {
                r.get("/users", (req, res) -> res.json(/* 用户列表 */));
                r.post("/users", (req, res) -> res.json(/* 创建用户 */));
            });
        });
        
        app.run(8080);
    }
}
```

### 3. 依赖注入容器

EST 有一个简单但强大的依赖注入容器：

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

// 定义一个服务接口
interface UserService {
    String getUserName(String id);
}

// 实现服务
class UserServiceImpl implements UserService {
    @Override
    public String getUserName(String id) {
        return "用户 " + id;
    }
}

public class DiExample {
    public static void main(String[] args) {
        // 创建容器
        Container container = new DefaultContainer();
        
        // 注册服务
        container.register(UserService.class, UserServiceImpl.class);
        
        // 获取服务并使用
        UserService service = container.get(UserService.class);
        System.out.println(service.getUserName("123")); // 输出：用户 123
    }
}
```

### 4. 配置管理

轻松管理应用配置：

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class ConfigExample {
    public static void main(String[] args) {
        WebApplication app = Web.create("配置示例", "1.0.0");
        
        // 设置配置
        app.getConfig().set("app.name", "我的应用");
        app.getConfig().set("server.port", 8080);
        app.getConfig().set("debug", true);
        
        // 获取配置
        String appName = app.getConfig().getString("app.name");
        int port = app.getConfig().getInt("server.port", 8080); // 第二个参数是默认值
        boolean isDebug = app.getConfig().getBoolean("debug", false);
        
        System.out.println("应用名: " + appName);
        System.out.println("端口: " + port);
        
        app.run(port);
    }
}
```

## 📁 项目结构

EST 采用清晰的递进式模块设计：

```
est/
├── est-core/          # 核心模块（依赖注入、配置管理等）
├── est-utils/         # 工具模块（JSON、XML、IO等）
├── est-collection/    # 集合增强（类似 Laravel Collection）
├── est-features/      # 企业级功能
│   ├── est-features-cache/     # 缓存
│   ├── est-features-logging/   # 日志
│   ├── est-features-data/      # 数据访问
│   ├── est-features-security/  # 安全认证
│   ├── est-features-messaging/ # 消息系统
│   ├── est-features-monitor/   # 监控
│   ├── est-features-scheduler/ # 调度
│   └── ...
├── est-patterns/      # 设计模式
├── est-plugin/        # 插件系统
├── est-test/          # 测试框架
├── est-scaffold/      # 脚手架生成器
├── est-web/           # Web 框架
├── est-examples/      # 示例代码
├── deploy/            # 部署配置（Docker、Kubernetes）
├── .github/           # GitHub Actions CI/CD
└── docs/              # 文档
```

## 🎯 模块选择指南

根据你的需求选择合适的模块：

### 最小配置（只用核心）

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-impl</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
```

### Web 开发

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-web-impl</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
```

### 完整 Web 应用（包含缓存和日志）

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web-impl</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-features-cache-memory</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-features-logging-console</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

## 🛠️ 构建项目

### 克隆并构建

```bash
# 克隆项目
git clone https://github.com/idcu/est.git
cd est

# 构建所有模块
mvn clean install

# 跳过测试（更快）
mvn clean install -DskipTests

# 只构建某个模块（比如 web 模块）
mvn clean install -pl est-web
```

## 📚 文档

EST 提供了完整的文档体系，帮助你快速上手：

### 入门指南
- [小白友好环境准备指南](docs/ENVIRONMENT_PREPARATION.md) - 从零开始，手把手教你安装 JDK、Maven、Git 和 IDE（专为初学者设计）
- [快速开始](docs/guides/getting-started.md) - 从零开始使用 EST 框架
- [安装与配置](docs/guides/installation.md) - 环境准备和项目配置

### 教程系列
- [入门教程](docs/tutorials/beginner/) - 适合初学者的教程
  - [第一个 EST 应用](docs/tutorials/beginner/01-first-app.md)
  - [依赖注入容器](docs/tutorials/beginner/02-dependency-injection.md)
  - [配置管理](docs/tutorials/beginner/03-configuration.md)
- [Web 开发教程](docs/tutorials/web/)
  - [基础 Web 应用](docs/tutorials/web/01-basic-web-app.md)
  - [路由与控制器](docs/tutorials/web/02-routing-controllers.md)

### API 参考
- [核心模块 API](docs/api/core/)
- [Web 模块 API](docs/api/web/)
- [功能模块 API](docs/api/features/)

### AI 开发者专区
- [AI Coder 指南](docs/AI_CODER_GUIDE.md) - 专为 AI 设计的完整使用指南
- [快速参考卡片](docs/QUICK_REFERENCE.md) - 一分钟速查表
- [代码生成提示词模板](docs/AI_PROMPTS.md) - 高质量的 AI 提示词集合

## 💡 更多示例

EST 框架提供了丰富的示例代码，位于 `est-examples/` 目录：

- **est-examples-basic** - 基础示例
- **est-examples-web** - Web 开发示例
- **est-examples-features** - 功能模块示例
- **est-examples-advanced** - 高级用法示例

## 🤝 贡献

欢迎贡献代码！请查看 [贡献指南](docs/CONTRIBUTING.md) 了解如何参与项目开发。

## 📄 许可证

[MIT License](LICENSE)

---

**祝你使用 EST 框架愉快！** 🎉
