# EST 2.0 模块迁移指南

## 概述

EST 2.0 架构迁移已完成！所有功能模块已从 est-features/ 移动到 est-modules/。

## 模块重命名对照表

| 旧模块名 | 新模块名 |
|---------|---------|
| est-features-cache | est-cache |
| est-features-logging | est-logging |
| est-features-data | est-data |
| est-features-security | est-security |
| est-features-messaging | est-messaging |
| est-features-monitor | est-monitor |
| est-features-scheduler | est-scheduler |
| est-features-event | est-event |
| est-features-circuitbreaker | est-circuitbreaker |
| est-features-discovery | est-discovery |
| est-features-config | est-config |
| est-features-performance | est-performance |
| est-features-hotreload | est-hotreload |
| est-features-ai | est-ai |
| est-plugin (从 est-extensions/) | est-plugin (在 est-modules/) |
| est-microservices-gateway (从 est-microservices/) | est-gateway (在 est-modules/) |

## 新目录结构

```
est-modules/
├── est-cache/
├── est-logging/
├── est-data/
├── est-security/
├── est-messaging/
├── est-monitor/
├── est-scheduler/
├── est-event/
├── est-circuitbreaker/
├── est-discovery/
├── est-config/
├── est-performance/
├── est-hotreload/
├── est-ai/
├── est-plugin/
└── est-gateway/
```

## 兼容性说明

- 旧的 est-features/ 模块已被标记为 Deprecated
- 旧的 est-features/ 模块仍保留在项目中，作为兼容层，重定向到新的 est-modules/ 模块
- 建议逐步迁移到新的 est-modules/ 模块

## 迁移步骤

1. 更新 pom.xml 中的依赖，将 `est-features-*` 替换为对应的 `est-*`
2. 更新 import 语句（如需要）
3. 测试确保功能正常

## 验证

新模块已验证可以正常构建！

---
*最后更新: 2026-03-07*
