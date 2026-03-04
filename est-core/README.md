# EST Core 核心模块

EST框架的核心基础设施模块，提供依赖注入容器、配置管理、组件扫描等核心功能。

## 模块结构

```
est-core/
├── est-core-api/      # 核心接口定义
├── est-core-impl/     # 核心实现
└── pom.xml
```

## 主要功能

### 依赖注入容器 (Container)

三层Map实现的高效DI容器，支持多种注入方式：

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

Container container = new DefaultContainer();

// 注册组件
container.register(MyService.class, MyServiceImpl.class);
container.registerSingleton(Config.class, new MyConfig());

// 获取组件
MyService service = container.get(MyService.class);
```

### 配置管理 (Config)

统一的配置接口：

```java
import ltd.idcu.est.core.api.Config;
import ltd.idcu.est.core.impl.DefaultConfig;

Config config = DefaultConfig.load("application.properties");

String value = config.get("app.name");
int port = config.getInt("server.port", 8080);
```

### 组件扫描

自动扫描和注册组件：

```java
import ltd.idcu.est.core.impl.scan.ComponentScanner;

ComponentScanner scanner = new ComponentScanner(container);
scanner.scan("com.example.package");
```

## 注解支持

- `@Component` - 通用组件
- `@Service` - 服务层组件
- `@Repository` - 数据访问层组件
- `@Inject` - 依赖注入
- `@Qualifier` - 限定符
- `@Primary` - 主候选
- `@Value` - 配置值注入

## 生命周期管理

完整的Bean生命周期支持：

- `@PostConstruct` - 初始化回调
- `@PreDestroy` - 销毁回调
- `InitializingBean` - 初始化接口
- `DisposableBean` - 销毁接口

## 依赖

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-api</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
```

## 相关文档

- [API 文档](../docs/api/core/)
- [架构文档](../docs/ARCHITECTURE.md)
