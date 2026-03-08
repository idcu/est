# EST Framework 2.0

EST 是一个**零依赖**的现代 Java 框架，采用递进式模块结构设计，支持企业级应用开发、AI 集成、微服务架构等多种场景。

- **版本**: 2.0.0
- **项目**: EST
- **作者**: idcu
- **许可证**: MIT License

---

## ✨ 核心特性

### 🏗️ 模块化架构
- **分层设计**：核心层 → 基础层 → 模块层 → 应用层 → 工具层
- **按需引入**：想用哪个功能就引入哪个模块，不会有多余的代码
- **API 与实现分离**：所有模块都采用 api/impl 分离的设计模式

### 🚀 零依赖设计
EST 框架不依赖任何第三方库，所有功能都是用 Java 标准库实现的：
- JSON处理、XML处理、YAML处理
- 加密、数据库访问、并发编程
- HTTP服务器、WebSocket支持

### 🤖 AI 原生支持
- LLM 大语言模型集成
- AI 助手对话
- 代码生成与优化
- 提示模板管理

### 🌐 完整的 Web 框架
- RESTful API 支持
- 路由系统、中间件支持
- 模板引擎、会话管理
- CORS 跨域支持

### 🔐 企业级安全
- JWT、OAuth2、API Key 认证
- RBAC 权限管理
- 审计日志
- 多租户支持

### 📦 丰富的功能模块
- **数据访问**：JDBC、Redis、MongoDB、内存存储
- **缓存系统**：内存缓存、文件缓存、Redis 缓存
- **消息系统**：Kafka、RabbitMQ、WebSocket、本地事件
- **任务调度**：固定频率、Cron 表达式
- **监控追踪**：JVM 监控、系统监控、分布式追踪
- **微服务**：熔断器、服务发现、API 网关
- **工作流**：工作流引擎支持

### 🛠️ 开发工具
- CLI 命令行工具
- 代码生成器
- 脚手架生成器
- 迁移工具

### 🎨 后台管理系统
- 基于 Vue 3 + Element Plus 的现代化 UI
- 用户、角色、菜单、部门、租户管理
- 操作日志、登录日志
- 系统监控、缓存监控
- 第三方集成配置（邮件、短信、OSS）
- AI 助手集成

### 🚀 部署支持
- Docker 容器化部署
- Docker Compose 编排
- Kubernetes 部署配置
- Service Mesh 支持
- CI/CD 流水线

---

## 🚀 快速开始

### 环境要求
- **JDK 21 或更高版本**（必须）
- **Maven 3.6 或更高版本**（用来构建项目）
- **Node.js 18+**（可选，用于前端开发）

### 构建项目
```bash
# 克隆项目
git clone https://github.com/idcu/est.git
cd est

# 构建所有模块
mvn clean install

# 跳过测试（更快）
mvn clean install -DskipTests
```

### 运行 Demo 应用
```bash
cd est-demo
mvn exec:java -Dexec.mainClass="ltd.idcu.est.demo.EstDemoApplication"
```

访问 http://localhost:8080 即可看到 Demo 应用。

### 运行后台管理系统
```bash
# 启动后端
cd est-app/est-admin
# （待实现）

# 启动前端
cd est-admin-ui
npm install
npm run dev
```

访问 http://localhost:3000，使用账号 `admin` / `admin123` 登录。

---

## 📁 项目结构

```
est/
├── est-core/              # 核心层
│   ├── est-core-container/    # 依赖注入容器
│   ├── est-core-config/       # 配置管理
│   ├── est-core-lifecycle/    # 生命周期管理
│   ├── est-core-module/       # 模块管理
│   ├── est-core-aop/          # AOP支持
│   └── est-core-tx/           # 事务管理
├── est-base/              # 基础层
│   ├── est-utils/         # 工具模块（JSON、XML、IO等）
│   ├── est-patterns/      # 设计模式
│   ├── est-collection/    # 集合框架
│   └── est-test/          # 测试支持
├── est-modules/           # 功能模块层
│   ├── est-foundation/    # 基础设施（缓存、日志、事件、监控等）
│   ├── est-data-group/    # 数据访问（JDBC、Redis、MongoDB、工作流）
│   ├── est-security-group/ # 安全权限（认证、RBAC、审计）
│   ├── est-web-group/     # Web框架（路由、网关、会话、模板）
│   ├── est-ai-suite/      # AI套件（LLM、AI助手、AI配置）
│   ├── est-microservices/ # 微服务（熔断器、服务发现、性能）
│   ├── est-integration-group/ # 集成（消息系统、第三方集成）
│   └── est-extensions/    # 扩展功能（热加载、插件、调度）
├── est-app/               # 应用层
│   ├── est-web/           # Web应用框架
│   ├── est-admin/         # 后台管理系统后端
│   └── est-console/       # 控制台应用
├── est-admin-ui/          # 后台管理系统前端（Vue 3）
├── est-tools/             # 工具层
│   ├── est-cli/           # 命令行工具
│   ├── est-codegen/       # 代码生成
│   ├── est-migration/     # 迁移工具
│   └── est-scaffold/      # 脚手架生成器
├── est-examples/          # 示例代码
│   ├── est-examples-basic/      # 基础示例
│   ├── est-examples-web/        # Web示例
│   ├── est-examples-ai/         # AI示例
│   ├── est-examples-advanced/   # 高级示例
│   ├── est-examples-features/   # 功能示例
│   ├── est-examples-microservices/ # 微服务示例
│   └── est-examples-graalvm/    # GraalVM示例
├── est-demo/              # 演示应用
├── deploy/                # 部署配置
│   ├── docker/            # Docker配置
│   ├── k8s/               # Kubernetes配置
│   └── servicemesh/       # Service Mesh配置
├── .github/               # GitHub Actions CI/CD
├── .config/               # 代码质量配置（Checkstyle、PMD、SpotBugs）
└── docs/                  # 文档
```

---

## 📚 文档导航

### 入门指南
- [快速开始](docs/getting-started/README.md) - 创建你的第一个应用
- [演示应用](est-demo/README.md) - 完整的功能演示
- [快速开始指南](est-demo/QUICKSTART.md) - 5分钟上手

### 教程系列
- [入门教程](docs/tutorials/beginner/) - 适合初学者
- [Web 开发教程](docs/tutorials/) - 学习 Web 开发

### 模块文档
- [核心模块](est-core/README.md)
- [基础模块](est-base/README.md)
- [功能模块](est-modules/README.md)
- [应用模块](est-app/README.md)

### AI 开发
- [AI 文档](docs/ai/README.md)
- [AI 助手 API](docs/ai/api/ai-assistant.md)
- [LLM 客户端](docs/ai/api/llm-client.md)

### 部署指南
- [Docker 部署](deploy/README.md)
- [Kubernetes 部署](deploy/k8s/)
- [Service Mesh](deploy/servicemesh/)

### API 参考
- [API 文档中心](docs/README.md) - 完整的文档导航
- [示例代码](est-examples/) - 丰富的示例代码

### 更多
- [最佳实践](docs/best-practices/) - 代码组织、性能优化、安全等
- [架构设计](docs/architecture/README.md)

---

## 🎯 代码示例

### 第一个 Web 应用
```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class HelloWorld {
    public static void main(String[] args) {
        WebApplication app = Web.create("我的第一个应用", "1.0.0");
        
        app.get("/", (req, res) -> res.send("Hello, World!"));
        app.get("/api/user/:name", (req, res) -> 
            res.json(Map.of("name", req.getPathParam("name"))));
        
        app.run(8080);
    }
}
```

### 使用缓存
```java
import ltd.idcu.est.cache.memory.MemoryCache;
import ltd.idcu.est.cache.api.Cache;

Cache<String, Object> cache = new MemoryCache<>();
cache.put("key", "value");
Object value = cache.get("key");
```

### 使用日志
```java
import ltd.idcu.est.logging.console.ConsoleLogs;
import ltd.idcu.est.logging.api.Logger;

Logger logger = ConsoleLogs.getLogger(HelloWorld.class);
logger.info("这是一条日志");
```

### 使用事件总线
```java
import ltd.idcu.est.event.local.LocalEventBus;
import ltd.idcu.est.event.api.EventBus;

EventBus eventBus = new LocalEventBus();
eventBus.subscribe(UserCreatedEvent.class, event -> 
    logger.info("用户创建: {}", event.getUsername()));
eventBus.publish(new UserCreatedEvent("张三"));
```

---

## 🤝 贡献

欢迎贡献代码！请查看项目文档了解如何参与项目开发。

---

## 📄 许可证

[MIT License](LICENSE)

---

**祝你使用 EST 框架愉快！** 🎉
