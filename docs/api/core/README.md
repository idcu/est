# 核心模块 API

核心模块是 EST 框架的基础，提供依赖注入容器、配置管理、模块管理和生命周期管理等核心功能。

## 包结构

```
ltd.idcu.est.core
├── api              # 核心接口
│   ├── Container
│   ├── Configuration
│   ├── Module
│   ├── EstApplication
│   └── lifecycle    # 生命周期相关
└── impl             # 核心实现
    ├── DefaultContainer
    ├── DefaultConfiguration
    ├── DefaultEstApplication
    └── lifecycle
```

## 快速导航

| 接口 | 说明 |
|------|------|
| [Container](./container.md) | 依赖注入容器 |
| [Configuration](./configuration.md) | 配置管理 |
| [Module](./module.md) | 模块接口 |
| [EstApplication](./application.md) | 应用入口 |

## 使用示例

### 创建应用

```java
import ltd.idcu.est.core.DefaultEstApplication;
import ltd.idcu.est.core.api.EstApplication;

EstApplication app = DefaultEstApplication.create();
app.run();
```

### 使用容器

```java
Container container = app.getContainer();
container.register(UserService.class, UserServiceImpl.class);
UserService service = container.get(UserService.class);
```
