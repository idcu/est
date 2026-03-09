# EST Observability

可观测性模块，提供Metrics、Logs、Traces的完整集成。

## 功能特性

- **Metrics集成** - Prometheus指标收集和暴露
- **Logs集成** - ELK Stack日志统一收集
- **Traces集成** - OpenTelemetry分布式链路追踪
- **统一接口** - 一套API，多种实现

## 模块结构

```
est-observability/
├── est-observability-api/          # 可观测性API
├── est-observability-prometheus/   # Prometheus集成
├── est-observability-elk/          # ELK集成
├── est-observability-opentelemetry/ # OpenTelemetry集成
└── README.md
```

## 快速开始

### 添加依赖

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-observability-api</artifactId>
    <version>2.4.0-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-observability-prometheus</artifactId>
    <version>2.4.0-SNAPSHOT</version>
</dependency>
```

### 使用Prometheus

```java
import ltd.idcu.est.observability.prometheus.*;

PrometheusExporter exporter = new PrometheusExporter(9090);
exporter.start();

// 注册指标
MetricsCollector collector = exporter.getMetricsCollector();
collector.registerCounter("http_requests_total", "Total HTTP requests");
collector.incrementCounter("http_requests_total");
```

### 使用ELK日志

```java
import ltd.idcu.est.observability.elk.*;

ElkLogExporter elkExporter = new ElkLogExporter("http://localhost:9200");
elkExporter.start();

// 发送日志
elkExporter.log(LogLevel.INFO, "Application started", 
    Map.of("service", "my-app", "version", "1.0.0"));
```

### 使用OpenTelemetry

```java
import ltd.idcu.est.observability.opentelemetry.*;

OpenTelemetryTracer tracer = new OpenTelemetryTracer("my-service");
tracer.start();

// 创建Span
try (TraceScope scope = tracer.startSpan("process-request")) {
    tracer.addTag("user.id", "123");
    // 处理逻辑
}
```

## 文档

- [Prometheus集成指南](docs/prometheus.md)
- [ELK集成指南](docs/elk.md)
- [OpenTelemetry集成指南](docs/opentelemetry.md)
- [Grafana仪表板模板](docs/grafana-dashboards.md)

## 许可证

MIT License
