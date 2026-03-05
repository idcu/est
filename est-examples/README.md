# EST Examples 示例模块

包含EST框架的各种使用示例。

## 模块结构

```
est-examples/
├── est-examples-basic/      # 基础示例
├── est-examples-web/        # Web示例
├── est-examples-features/ # 功能示例
├── est-examples-advanced/ # 高级示例
└── pom.xml
```

## 示例列表

### 基础示例 (est-examples-basic)

- 依赖注入容器使用
- 配置管理
- 集合操作
- 设计模式使用

### Web示例 (est-examples-web)

- Hello World Web应用
- RESTful API
- 路由和控制器
- 中间件使用
- 会话管理
- 模板引擎

### 功能示例 (est-examples-features)

- 缓存系统
- 事件总线
- 日志系统
- 数据访问
- 安全认证
- 调度系统
- 监控系统

### 高级示例 (est-examples-advanced)

- 插件系统
- 完整应用
- 性能优化
- 测试实践

## 运行示例

```bash
# 运行Web示例
cd est-examples-web
mvn exec:java

# 运行功能示例
cd est-examples-features
./run-examples.bat
```

## 依赖

示例模块依赖EST框架的其他模块。
