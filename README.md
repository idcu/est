# EST

EST 是一个零依赖的现代 Java 框架，采用递进式模块结构设计。

## 核心特性

- **零依赖**：不依赖任何第三方库，避免依赖冲突和版本管理问题
- **递进式模块**：每个层级的模块都可以独立被其他项目引用
- **低耦合设计**：模块间通过接口通信，减少直接依赖
- **现代 Java**：基于 Java 21+，支持虚拟线程等现代特性
- **高性能**：优化核心组件性能，支持并发操作
- **Collection 增强**：提供类似 Laravel Collection 的链式数据处理能力

## 技术栈

| 技术 | 说明 |
|------|------|
| Java 21+ | 核心编程语言 |
| Maven | 项目构建和依赖管理 |
| Java 标准库 | 所有功能的实现基础 |

## 项目结构

```
est/
├── est-core/                              # 核心模块
│   ├── est-core-api/                      # 核心接口
│   └── est-core-impl/                     # 核心实现
├── est-collection/                        # Collection模块
│   ├── est-collection-api/                # Collection接口
│   └── est-collection-impl/               # Collection实现
├── est-patterns/                          # 设计模式模块
│   ├── est-patterns-api/                  # 设计模式接口
│   └── est-patterns-impl/                 # 设计模式实现
├── est-utils/                             # 工具模块
│   ├── est-utils-common/                  # 通用工具
│   ├── est-utils-io/                      # IO工具
│   └── est-utils-format/                  # 格式化工具
│       ├── est-utils-format-json/         # JSON格式化
│       ├── est-utils-format-yaml/         # YAML格式化
│       └── est-utils-format-xml/          # XML格式化
├── est-features/                          # 功能模块
│   ├── est-features-cache/                # 缓存功能
│   ├── est-features-event/                # 事件功能
│   ├── est-features-logging/              # 日志功能
│   ├── est-features-data/                 # 数据功能
│   ├── est-features-security/             # 安全功能
│   ├── est-features-messaging/            # 消息功能
│   ├── est-features-monitor/              # 监控功能
│   └── est-features-scheduler/            # 调度功能
├── est-plugin/                            # 插件模块
│   ├── est-plugin-api/                    # 插件接口
│   └── est-plugin-impl/                   # 插件实现
├── est-web/                               # Web模块
│   ├── est-web-api/                       # Web接口
│   └── est-web-impl/                      # Web实现
└── est-examples/                          # 示例模块
    ├── est-examples-basic/                # 基础示例
    ├── est-examples-advanced/             # 高级示例
    ├── est-examples-features/             # 功能示例
    └── est-examples-web/                  # Web示例
```

## 模块层级架构

```
┌─────────────────────────────────────────────────────────────────┐
│                        est-examples (示例层)                      │
├─────────────────────────────────────────────────────────────────┤
│                         est-web (Web层)                          │
├─────────────────────────────────────────────────────────────────┤
│                       est-plugin (插件层)                         │
├─────────────────────────────────────────────────────────────────┤
│                      est-features (功能层)                        │
├─────────────────────────────────────────────────────────────────┤
│                     est-collection (集合层)                       │
├─────────────────────────────────────────────────────────────────┤
│                        est-utils (工具层)                         │
├─────────────────────────────────────────────────────────────────┤
│                      est-patterns (模式层)                        │
├─────────────────────────────────────────────────────────────────┤
│                        est-core (核心层)                          │
└─────────────────────────────────────────────────────────────────┘
```

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.6+

### 构建

```bash
# 构建所有模块
mvn clean install

# 构建指定模块
mvn clean install -pl est-core

# 跳过测试
mvn clean install -DskipTests
```

### 使用示例

```xml
<!-- 只引用核心接口 -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-api</artifactId>
    <version>1.0.0</version>
</dependency>

<!-- 引用Collection功能 -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-collection-impl</artifactId>
    <version>1.0.0</version>
</dependency>

<!-- 引用Web功能 -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-web-impl</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 设计原则

### 零依赖原则

不依赖任何第三方库，所有功能使用 Java 标准库实现，充分利用 Java 21+ 特性。

| 功能 | 实现方式 |
|------|----------|
| JSON处理 | javax.json 包 |
| XML处理 | javax.xml 包 |
| 加密 | javax.crypto 包 |
| JDBC | java.sql 包 |
| 并发 | java.util.concurrent 包 |
| JVM监控 | java.lang.management 包 |
| HTTP服务器 | com.sun.net.httpserver 包 |

### 递进式模块设计

采用清晰的层级结构，每个模块都可以独立被其他项目引用，模块间通过接口通信。

### 低耦合设计

模块间通过接口通信，接口与实现分离，支持多种实现方式。

## 功能模块

| 模块 | 说明 |
|------|------|
| est-core | 核心接口与实现，依赖注入容器、配置管理、模块管理、生命周期 |
| est-collection | Collection接口与实现，链式数据处理、格式转换 |
| est-patterns | 设计模式接口与实现，创建型、结构型、行为型模式 |
| est-utils | 工具类，字符串、日期、IO、格式化等 |
| est-features-cache | 缓存功能，内存缓存、文件缓存、Redis缓存 |
| est-features-event | 事件功能，本地事件、异步事件 |
| est-features-logging | 日志功能，控制台日志、文件日志 |
| est-features-data | 数据功能，JDBC、内存、Redis数据访问 |
| est-features-security | 安全功能，基础认证、JWT认证 |
| est-features-messaging | 消息功能，本地消息、AMQP消息 |
| est-features-monitor | 监控功能，JVM监控、系统监控 |
| est-features-scheduler | 调度功能，Cron调度、固定间隔调度 |
| est-plugin | 插件系统，插件加载、管理 |
| est-web | Web功能，HTTP服务器、路由、中间件、MVC |

## 模块统计

| 类型 | 数量 |
|------|------|
| 顶层模块 | 8 |
| 二级模块 | 25 |
| 三级模块 | 30 |
| **总计** | **63** |

## 开发计划

- [x] 阶段一：基础架构搭建
  - [x] 递进式项目结构搭建
  - [x] 核心模块 - Container、Config、Module、Lifecycle 接口与实现
  - [x] 设计模式模块 - Singleton、Factory、Builder、Adapter、Decorator、Proxy、Observer、Strategy、Command 接口与实现
  - [x] 基础工具模块 - StringUtils、DateUtils、AssertUtils、ClassUtils、FileUtils、IOUtils、ResourceUtils、PathUtils、JsonUtils、YamlUtils、XmlUtils
- [ ] 阶段二：Collection模块实现
  - [x] Collection 接口定义 - Collection、Collector、Pair、Collections 工厂类
  - [x] 链式调用功能实现 - DefaultCollection、SingletonCollection、CollectionFactory
  - [ ] 格式转换功能实现
  - [ ] 单元测试
- [ ] 阶段三：功能模块实现
- [ ] 阶段四：插件与Web模块实现
- [ ] 阶段五：示例和文档
- [ ] 阶段六：发布和维护

## License

[MIT License](LICENSE)

## Author

idcu
