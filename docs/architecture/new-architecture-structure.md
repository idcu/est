# 新架构目录结构示例

```
est2.0/
├── pom.xml                                  # 根 POM
├── README.md
├── LICENSE
├── .gitignore
├── .editorconfig
├── .config/
│   └── checkstyle.xml
├── .github/
│   └── workflows/
│       └── ci-cd.yml
├── docs/
│   ├── ARCHITECTURE.md
│   ├── ARCHITECTURE_REDESIGN.md
│   ├── GETTING_STARTED.md
│   └── ...
├── deploy/
│   ├── docker/
│   ├── k8s/
│   └── servicemesh/
│
├── est-base/                                # 基础层
│   ├── pom.xml
│   ├── est-utils/
│   │   ├── pom.xml
│   │   ├── est-util-common/
│   │   │   ├── pom.xml
│   │   │   └── src/main/java/
│   │   ├── est-util-io/
│   │   │   ├── pom.xml
│   │   │   └── src/main/java/
│   │   └── est-util-format/
│   │       ├── pom.xml
│   │       ├── est-util-format-json/
│   │       ├── est-util-format-xml/
│   │       └── est-util-format-yaml/
│   ├── est-collection/
│   │   ├── pom.xml
│   │   ├── est-collection-api/
│   │   └── est-collection-impl/
│   ├── est-patterns/
│   │   ├── pom.xml
│   │   ├── est-patterns-api/
│   │   └── est-patterns-impl/
│   └── est-test/
│       ├── pom.xml
│       ├── est-test-api/
│       ├── est-test-impl/
│       └── est-test-benchmark/
│
├── est-core/                                # 核心层
│   ├── pom.xml
│   ├── est-core-container/
│   │   ├── pom.xml
│   │   ├── est-core-container-api/
│   │   │   └── src/main/java/ltd/idcu/est/core/container/api/
│   │   │       ├── Container.java
│   │   │       ├── annotation/
│   │   │       │   ├── Component.java
│   │   │       │   ├── Service.java
│   │   │       │   ├── Repository.java
│   │   │       │   ├── Inject.java
│   │   │       │   ├── Qualifier.java
│   │   │       │   ├── Primary.java
│   │   │       │   ├── Value.java
│   │   │       │   ├── Lazy.java
│   │   │       │   └── ConditionalOnProperty.java
│   │   │       ├── scope/
│   │   │       │   └── Scope.java
│   │   │       └── processor/
│   │   │           └── BeanPostProcessor.java
│   │   └── est-core-container-impl/
│   │       └── src/main/java/ltd/idcu/est/core/container/impl/
│   │           ├── DefaultContainer.java
│   │           ├── inject/
│   │           ├── scope/
│   │           └── scan/
│   ├── est-core-config/
│   │   ├── pom.xml
│   │   ├── est-core-config-api/
│   │   └── est-core-config-impl/
│   ├── est-core-lifecycle/
│   │   ├── pom.xml
│   │   ├── est-core-lifecycle-api/
│   │   │   └── src/main/java/ltd/idcu/est/core/lifecycle/api/
│   │   │       ├── Lifecycle.java
│   │   │       ├── LifecycleListener.java
│   │   │       ├── InitializingBean.java
│   │   │       ├── DisposableBean.java
│   │   │       ├── PostConstruct.java
│   │   │       └── PreDestroy.java
│   │   └── est-core-lifecycle-impl/
│   │       └── src/main/java/ltd/idcu/est/core/lifecycle/impl/
│   │           ├── DefaultLifecycle.java
│   │           └── LifecycleManager.java
│   ├── est-core-module/
│   │   ├── pom.xml
│   │   ├── est-core-module-api/
│   │   │   └── src/main/java/ltd/idcu/est/core/module/api/
│   │   │       └── Module.java
│   │   └── est-core-module-impl/
│   │       └── src/main/java/ltd/idcu/est/core/module/impl/
│   │           ├── AbstractModule.java
│   │           └── ModuleManager.java
│   ├── est-core-aop/
│   │   ├── pom.xml
│   │   ├── est-core-aop-api/
│   │   │   └── src/main/java/ltd/idcu/est/core/aop/api/
│   │   │       ├── JoinPoint.java
│   │   │       ├── ProceedingJoinPoint.java
│   │   │       └── annotation/
│   │   │           ├── Aspect.java
│   │   │           ├── Before.java
│   │   │           ├── After.java
│   │   │           ├── AfterReturning.java
│   │   │           ├── AfterThrowing.java
│   │   │           ├── Around.java
│   │   │           └── Pointcut.java
│   │   └── est-core-aop-impl/
│   └── est-core-tx/
│       ├── pom.xml
│       ├── est-core-tx-api/
│       │   └── src/main/java/ltd/idcu/est/core/tx/api/
│       │       ├── PlatformTransactionManager.java
│       │       ├── TransactionDefinition.java
│       │       ├── TransactionStatus.java
│       │       └── annotation/
│       │           └── Transactional.java
│       └── est-core-tx-impl/
│
├── est-modules/                             # 模块层
│   ├── pom.xml
│   ├── est-cache/
│   │   ├── pom.xml
│   │   ├── est-cache-api/
│   │   │   └── src/main/java/ltd/idcu/est/cache/api/
│   │   │       ├── Cache.java
│   │   │       └── CacheStats.java
│   │   ├── est-cache-memory/
│   │   │   └── src/main/java/ltd/idcu/est/cache/memory/
│   │   │       └── MemoryCache.java
│   │   ├── est-cache-file/
│   │   └── est-cache-redis/
│   ├── est-logging/
│   │   ├── pom.xml
│   │   ├── est-logging-api/
│   │   ├── est-logging-console/
│   │   └── est-logging-file/
│   ├── est-data/
│   │   ├── pom.xml
│   │   ├── est-data-api/
│   │   │   └── src/main/java/ltd/idcu/est/data/api/
│   │   │       ├── Repository.java
│   │   │       └── annotation/
│   │   │           ├── Entity.java
│   │   │           ├── Id.java
│   │   │           └── Column.java
│   │   ├── est-data-jdbc/
│   │   ├── est-data-memory/
│   │   ├── est-data-redis/
│   │   └── est-data-mongodb/
│   ├── est-security/
│   │   ├── pom.xml
│   │   ├── est-security-api/
│   │   ├── est-security-basic/
│   │   ├── est-security-jwt/
│   │   ├── est-security-apikey/
│   │   ├── est-security-oauth2/
│   │   └── est-security-policy/
│   ├── est-messaging/
│   │   ├── pom.xml
│   │   ├── est-messaging-api/
│   │   ├── est-messaging-local/
│   │   ├── est-messaging-activemq/
│   │   ├── est-messaging-amqp/
│   │   ├── est-messaging-kafka/
│   │   ├── est-messaging-mqtt/
│   │   ├── est-messaging-nats/
│   │   ├── est-messaging-pulsar/
│   │   ├── est-messaging-redis/
│   │   ├── est-messaging-rocketmq/
│   │   ├── est-messaging-stomp/
│   │   ├── est-messaging-websocket/
│   │   └── est-messaging-zeromq/
│   ├── est-monitor/
│   │   ├── pom.xml
│   │   ├── est-monitor-api/
│   │   ├── est-monitor-jvm/
│   │   └── est-monitor-system/
│   ├── est-scheduler/
│   │   ├── pom.xml
│   │   ├── est-scheduler-api/
│   │   ├── est-scheduler-cron/
│   │   └── est-scheduler-fixed/
│   ├── est-event/
│   │   ├── pom.xml
│   │   ├── est-event-api/
│   │   ├── est-event-local/
│   │   └── est-event-async/
│   ├── est-circuitbreaker/
│   │   ├── pom.xml
│   │   └── est-circuitbreaker-api/
│   ├── est-discovery/
│   │   ├── pom.xml
│   │   └── est-discovery-api/
│   ├── est-config/
│   │   ├── pom.xml
│   │   └── est-config-api/
│   ├── est-performance/
│   │   ├── pom.xml
│   │   └── est-performance-api/
│   ├── est-plugin/
│   │   ├── pom.xml
│   │   ├── est-plugin-api/
│   │   └── est-plugin-impl/
│   └── est-ai/
│       ├── pom.xml
│       ├── est-ai-api/
│       └── est-ai-impl/
│
├── est-app/                                 # 应用层
│   ├── pom.xml
│   ├── est-web/
│   │   ├── pom.xml
│   │   ├── est-web-api/
│   │   │   └── src/main/java/ltd/idcu/est/web/api/
│   │   │       ├── WebApplication.java
│   │   │       ├── Router.java
│   │   │       ├── Request.java
│   │   │       ├── Response.java
│   │   │       ├── Middleware.java
│   │   │       ├── Session.java
│   │   │       └── View.java
│   │   └── est-web-impl/
│   │       └── src/main/java/ltd/idcu/est/web/impl/
│   │           ├── DefaultWebApplication.java
│   │           ├── DefaultRouter.java
│   │           ├── DefaultRequest.java
│   │           ├── DefaultResponse.java
│   │           ├── HttpServerImpl.java
│   │           ├── middleware/
│   │           │   ├── DefaultCorsMiddleware.java
│   │           │   ├── LoggingMiddleware.java
│   │           │   ├── PerformanceMonitorMiddleware.java
│   │           │   └── SecurityMiddleware.java
│   │           └── Web.java
│   ├── est-microservice/
│   │   ├── pom.xml
│   │   ├── est-microservice-api/
│   │   └── est-microservice-impl/
│   └── est-console/
│       ├── pom.xml
│       ├── est-console-api/
│       └── est-console-impl/
│
├── est-tools/                               # 工具层
│   ├── pom.xml
│   ├── est-scaffold/
│   │   ├── pom.xml
│   │   └── src/main/java/ltd/idcu/est/scaffold/
│   │       ├── ScaffoldGenerator.java
│   │       ├── ProjectConfig.java
│   │       └── ...
│   ├── est-migration/
│   │   ├── pom.xml
│   │   └── src/main/java/ltd/idcu/est/migration/
│   │       ├── MigrationTool.java
│   │       ├── MigrationEngine.java
│   │       └── ...
│   ├── est-codegen/
│   │   ├── pom.xml
│   │   └── src/main/java/ltd/idcu/est/codegen/
│   │       ├── CodeGenerator.java
│   │       └── templates/
│   └── est-cli/
│       ├── pom.xml
│       └── src/main/java/ltd/idcu/est/cli/
│           └── EstCli.java
│
└── est-examples/                            # 示例层
    ├── pom.xml
    ├── est-examples-basic/
    ├── est-examples-web/
    ├── est-examples-advanced/
    ├── est-examples-ai/
    ├── est-examples-features/
    ├── est-examples-graalvm/
    └── est-examples-microservices/
```

## 关键改进点

1. **层级更清晰**：从 est-base（基础）→ est-core（核心）→ est-modules（功能）→ est-app（应用）→ est-tools（工具）→ est-examples（示例）
2. **命名更简洁**：去掉了冗余的 "features" 前缀
3. **职责更明确**：每个层级和模块都有清晰的定义
4. **扩展性更强**：为新增模块预留了明确的位置
