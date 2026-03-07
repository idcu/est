# EST 2.0.0 发布说明

## 发布日期
2026-03-07

## 版本概述
EST 2.0.0 是一个重大架构重构版本，采用分层模块化设计，提供更好的可扩展性和可维护性。

## 主要变更

### 架构重构
- 重新组织模块结构，采用分层架构
- 核心层拆分为多个独立子模块
- 提供清晰的模块依赖关系
- 保留1.X兼容层，确保平滑迁移

### 新模块结构
```
est/
├── est-core/              # 核心层
│   ├── est-core-container/    # 容器模块
│   ├── est-core-config/       # 配置模块
│   ├── est-core-lifecycle/    # 生命周期模块
│   ├── est-core-module/       # 模块管理
│   ├── est-core-aop/          # AOP模块
│   └── est-core-tx/           # 事务模块
├── est-base/              # 基础层
│   ├── est-utils/          # 工具模块
│   ├── est-patterns/       # 设计模式
│   ├── est-collection/     # 集合框架
│   └── est-test/           # 测试支持
├── est-modules/           # 功能模块
│   ├── est-cache/          # 缓存
│   ├── est-logging/        # 日志
│   ├── est-data/           # 数据访问
│   ├── est-security/       # 安全
│   ├── est-messaging/      # 消息
│   ├── est-monitor/        # 监控
│   ├── est-scheduler/      # 调度
│   ├── est-event/          # 事件
│   ├── est-circuitbreaker/ # 熔断器
│   ├── est-discovery/      # 服务发现
│   ├── est-config/         # 配置中心
│   ├── est-performance/    # 性能
│   ├── est-hotreload/      # 热加载
│   ├── est-ai/             # AI支持
│   ├── est-plugin/         # 插件
│   ├── est-gateway/        # 网关
│   ├── est-tracing/        # 分布式追踪
│   └── est-workflow/       # 工作流引擎
├── est-app/               # 应用层
│   ├── est-web/            # Web应用
│   ├── est-microservice/   # 微服务
│   └── est-console/        # 控制台应用
└── est-tools/             # 工具层
    ├── est-cli/            # 命令行工具
    ├── est-codegen/        # 代码生成
    ├── est-migration/      # 迁移工具
    └── est-scaffold/       # 脚手架
```

### 新特性
- 分层模块化架构设计
- 清晰的模块依赖关系
- 改进的容器实现
- 增强的AOP支持
- 更完善的事务管理
- 丰富的功能模块
- 完整的微服务支持
- 强大的工具链
- 新增工作流引擎模块
- 新增分布式追踪模块

### 性能改进
- 核心模块性能保持稳定
- 无明显性能回退
- 优化的容器操作
- 高效的缓存实现

## 兼容性说明
- 提供1.X兼容层
- 推荐使用新的模块化API
- 旧API标记为@Deprecated
- 详见MIGRATION_GUIDE.md

## 已知问题
暂无

## 贡献者
感谢所有为EST 2.0.0做出贡献的开发者！

## 许可证
MIT License
