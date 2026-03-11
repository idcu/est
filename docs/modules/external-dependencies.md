# 外部依赖模块文档

## 概述

EST Framework 大部分模块设计为零依赖架构，只依赖项目内部模块。然而，为了与第三方系统集成、使用业界标准库或提供特定功能，部分模块需要引入外部依赖。本文档详细列出所有有外部依赖的模块及其依赖说明。

## 依赖模块清单

### 1. 基础工具类模块

#### est-util-format-json

**模块路径：** `est-base/est-utils/est-util-format/est-util-format-json`

**外部依赖：**
| 依赖库 | 版本 | 用途 |
|--------|------|------|
| org.ow2.asm:asm | 9.7 | Java 字节码操作核心库 |
| org.ow2.asm:asm-commons | 9.7 | ASM 字节码操作通用工具 |

**依赖原因：**
- JSON 序列化/反序列化功能需要动态生成字节码
- ASM 是 Java 生态中最成熟、性能最好的字节码操作库
- 用于在运行时动态生成 JSON 序列化器和反序列化器

---

### 2. 性能测试模块

#### est-test-benchmark

**模块路径：** `est-base/est-test/est-test-benchmark`

**外部依赖：**
| 依赖库 | 版本 | Scope | 用途 |
|--------|------|-------|------|
| org.openjdk.jmh:jmh-core | 1.37 | compile | JMH 性能测试核心框架 |
| org.openjdk.jmh:jmh-generator-annprocess | 1.37 | compile | JMH 注解处理器 |
| com.h2database:h2 | 2.2.224 | test | 内存数据库（用于测试） |

**依赖原因：**
- JMH (Java Microbenchmark Harness) 是 OpenJDK 官方维护的 Java 微基准测试框架
- 提供精确的性能测量、避免 JVM 优化干扰、统计分析等功能
- H2 数据库用于 JDBC 相关性能测试场景

---

### 3. 可观测性模块

#### est-observability-prometheus

**模块路径：** `est-modules/est-integration-group/est-observability/est-observability-prometheus`

**外部依赖：**
| 依赖库 | 版本 | 用途 |
|--------|------|------|
| io.prometheus:simpleclient | 0.16.0 | Prometheus 客户端核心库 |
| io.prometheus:simpleclient_hotspot | 0.16.0 | JVM 指标收集 |
| io.prometheus:simpleclient_httpserver | 0.16.0 | HTTP 服务暴露指标 |

**依赖原因：**
- Prometheus 是云原生监控的事实标准
- 需要使用官方客户端库来暴露符合 Prometheus 格式的指标
- simpleclient_hotspot 提供开箱即用的 JVM 指标（堆内存、GC、线程等）

---

#### est-observability-opentelemetry

**模块路径：** `est-modules/est-integration-group/est-observability/est-observability-opentelemetry`

**外部依赖：**
| 依赖库 | 版本 | 用途 |
|--------|------|------|
| io.opentelemetry:opentelemetry-api | 1.32.0 | OpenTelemetry API 定义 |
| io.opentelemetry:opentelemetry-sdk | 1.32.0 | OpenTelemetry SDK 实现 |
| io.opentelemetry:opentelemetry-exporter-otlp | 1.32.0 | OTLP 协议导出器 |

**依赖原因：**
- OpenTelemetry 是 CNCF 托管的可观测性标准，统一了追踪、指标、日志
- 需要官方 SDK 来创建和管理分布式追踪 Span
- OTLP (OpenTelemetry Protocol) 是标准的导出协议，用于与 Jaeger、Zipkin 等后端集成

---

#### est-observability-elk

**模块路径：** `est-modules/est-integration-group/est-observability/est-observability-elk`

**外部依赖：**
| 依赖库 | 版本 | 用途 |
|--------|------|------|
| co.elastic.clients:elasticsearch-java | 8.11.0 | Elasticsearch 官方 Java 客户端 |

**依赖原因：**
- ELK Stack (Elasticsearch, Logstash, Kibana) 是流行的日志收集和分析平台
- elasticsearch-java 是 Elastic 官方提供的新一代 Java 客户端
- 提供类型安全的 API、自动重试、连接池等企业级特性

---

### 4. Kotlin 支持模块

#### est-kotlin-impl

**模块路径：** `est-modules/est-kotlin-support/est-kotlin-impl`

**外部依赖：**
| 依赖库 | 用途 |
|--------|------|
| org.jetbrains.kotlin:kotlin-stdlib | Kotlin 标准库 |
| org.jetbrains.kotlinx:kotlinx-coroutines-core | Kotlin 协程核心库 |
| org.jetbrains.kotlinx:kotlinx-coroutines-jdk8 | 协程与 JDK 8 CompletableFuture 集成 |

**依赖原因：**
- 提供 Kotlin 语言支持，包括标准库函数、扩展函数等
- Kotlin 协程是 Kotlin 生态中处理异步编程的标准方式
- 协程可以与 Java 的 CompletableFuture 无缝集成

---

### 5. gRPC 模块

#### est-grpc-core

**模块路径：** `est-modules/est-microservices/est-grpc/est-grpc-core`

**外部依赖：**
| 依赖库 | 用途 |
|--------|------|
| io.grpc:grpc-netty-shaded | gRPC Netty 传输层（含 shaded 依赖） |
| io.grpc:grpc-protobuf | gRPC Protobuf 支持 |
| io.grpc:grpc-stub | gRPC Stub 生成支持 |
| com.google.protobuf:protobuf-java | Protobuf Java 运行时 |

**依赖原因：**
- gRPC 是 Google 开发的高性能 RPC 框架，基于 HTTP/2 和 Protobuf
- grpc-netty-shaded 提供 Netty 网络传输层，shaded 版本避免依赖冲突
- Protobuf 用于定义服务接口和消息格式，是 gRPC 的核心组成部分

---

### 6. Serverless 云函数模块

#### est-serverless-aws

**模块路径：** `est-modules/est-cloud-group/est-serverless/est-serverless-aws`

**外部依赖：**
| 依赖库 | 版本 | Scope | 用途 |
|--------|------|-------|------|
| com.amazonaws:aws-lambda-java-core | 1.2.3 | provided | AWS Lambda Java 核心库 |

**依赖原因：**
- 与 AWS Lambda 云函数平台集成
- provided scope 表示该依赖由云平台提供，不需要打包到部署包中
- 提供 Lambda 请求处理器接口和上下文对象

---

#### est-serverless-azure

**模块路径：** `est-modules/est-cloud-group/est-serverless/est-serverless-azure`

**外部依赖：**
| 依赖库 | 版本 | Scope | 用途 |
|--------|------|-------|------|
| com.microsoft.azure.functions:azure-functions-java-library | 3.0.0 | provided | Azure Functions Java 库 |

**依赖原因：**
- 与 Azure Functions 云函数平台集成
- provided scope 表示该依赖由云平台提供
- 提供 Azure Functions 的触发器和绑定注解

---

#### est-serverless-alibaba

**模块路径：** `est-modules/est-cloud-group/est-serverless/est-serverless-alibaba`

**外部依赖：**
| 依赖库 | 版本 | Scope | 用途 |
|--------|------|-------|------|
| com.aliyun.fc.runtime:fc-java-core | 1.4.0 | provided | 阿里云函数计算 Java 核心库 |

**依赖原因：**
- 与阿里云函数计算 (Function Compute) 集成
- provided scope 表示该依赖由云平台提供
- 提供函数计算的请求处理接口

---

#### est-serverless-google

**模块路径：** `est-modules/est-cloud-group/est-serverless/est-serverless-google`

**外部依赖：**
| 依赖库 | 版本 | Scope | 用途 |
|--------|------|-------|------|
| com.google.cloud.functions:functions-framework-api | 1.0.4 | provided | Google Cloud Functions API |

**依赖原因：**
- 与 Google Cloud Functions 集成
- provided scope 表示该依赖由云平台提供
- 提供 Cloud Functions 的 HTTP 触发器和后台事件处理接口

---

## 依赖分类总结

### 按依赖类型分类

| 类型 | 模块数量 | 说明 |
|------|---------|------|
| 第三方系统集成 | 7 | Prometheus、OpenTelemetry、ELK、4 个云厂商 Serverless |
| 特定领域标准库 | 3 | gRPC、Kotlin 标准库、ASM |
| 性能测试工具 | 1 | JMH |

### 按依赖 Scope 分类

| Scope | 模块 | 说明 |
|-------|------|------|
| compile | 大部分 | 运行时必需的依赖 |
| provided | 4 个 Serverless 模块 | 由云平台提供，不打包 |
| test | est-test-benchmark | 仅测试用 |

---

## 零依赖核心模块

EST Framework 的绝大多数核心模块都是零依赖的，包括：

- **est-core**：核心容器、生命周期、AOP、事务等
- **est-base**：集合、模式、工具类（除 JSON 格式外）
- **est-data-group**：数据访问抽象、JDBC、Redis、MongoDB 等实现
- **est-messaging**：消息抽象、Kafka、RabbitMQ、Redis 等实现
- **est-web-group**：Web 路由、中间件、会话等
- **est-security-group**：安全认证、RBAC、审计等
- 更多...

这些零依赖模块只依赖项目内部的其他模块，确保了框架的轻量性和可移植性。

---

## 版本管理策略

所有外部依赖的版本在根 `pom.xml` 的 `dependencyManagement` 中统一管理，确保：
1. 版本一致性
2. 便于升级
3. 避免依赖冲突

---

## 最佳实践

1. **优先使用零依赖模块**：核心功能尽量使用零依赖模块
2. **按需引入外部依赖模块**：只在需要特定功能时才引入有外部依赖的模块
3. **关注依赖更新**：定期检查外部依赖的安全更新和版本升级
4. **provided 依赖注意事项**：Serverless 模块的 provided 依赖在本地开发时需要确保可用

---

## 相关文档

- [轻量级模块指南](./../guides/lightweight-modules-guide.md)
- [模块文档](./README.md)
- [性能基准测试指南](./../testing/performance-benchmark-guide.md)

