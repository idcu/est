# API 参考文档

本目录包含 EST 框架各模块的详细 API 参考文档。

## 目录

- [核心模块 API](./core/README.md)
  - [Container API](./core/container.md)
  - [Configuration API](./core/configuration.md)
  - [Module API](./core/module.md)
  - [Lifecycle API](./core/lifecycle.md)

- [Web 模块 API](./web/README.md)
  - [WebApplication API](./web/web-application.md)
  - [Router API](./web/router.md)
  - [Middleware API](./web/middleware.md)
  - [Request & Response API](./web/request-response.md)

- [功能模块 API](./features/README.md)
  - [Cache API](./features/cache.md)
  - [Event API](./features/event.md)
  - [Logging API](./features/logging.md)
  - [Data API](./features/data.md)
  - [Security API](./features/security.md)
  - [Scheduler API](./features/scheduler.md)
  - [Monitor API](./features/monitor.md)

- [Collection API](./collection/README.md)
- [Utils API](./utils/README.md)
- [Test API](./test/README.md)

## 生成 Javadoc

除了 Markdown 文档外，你还可以通过以下命令生成完整的 Javadoc：

```bash
mvn javadoc:aggregate
```

生成的文档位于 `target/site/apidocs` 目录。
