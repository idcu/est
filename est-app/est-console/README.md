# est-console - 小白从入门到精通
## 目录
- [什么是 est-console](#什么是-est-console)
- [快速入门](#快速入门)
- [核心功能](#核心功能)
- [模块结构](#模块结构)
- [相关资源](#相关资源)

---

## 什么是 est-console

### 用大白话理解
est-console 就像"应用控制台"，提供应用监控、配置管理、日志查看等运维功能。
### 核心特点
- **应用监控**：JVM 监控、系统监控
- **配置管理**：在线配置、热更新
- **日志查看**：实时日志、日志查询
- **健康检查**：应用健康状态
---

## 快速入门
### 1. 添加依赖
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-console</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 2. 启用控制台
```java
@EnableConsole
@SpringBootApplication
public class ConsoleApplication {
    public static void main(String[] args) {
        EstApplication.run(ConsoleApplication.class, args);
    }
}
```

---

## 核心功能

### 应用监控
```java
@Controller
public class MonitorController {
    
    @Get("/console/health")
    public Health checkHealth() {
        return healthService.check();
    }
    
    @Get("/console/metrics")
    public Metrics getMetrics() {
        return metricsService.collect();
    }
}
```

### 配置管理
```java
@Controller
public class ConfigController {
    
    @Get("/console/config")
    public Map<String, Object> getConfig() {
        return configService.getAll();
    }
    
    @Post("/console/config")
    public void updateConfig(@Body Map<String, Object> config) {
        configService.update(config);
    }
}
```

---

## 模块结构

```
est-console/
├── est-console-api/        # API 接口定义
│   ├── src/main/java/
│   │   └── ltd/idcu/est/app/console/
│   └── pom.xml
├── est-console-impl/       # 实现模块
│   ├── src/main/java/
│   │   └── ltd/idcu/est/app/console/
│   └── pom.xml
├── README.md
└── pom.xml
```

---

## 相关资源

- [父模块文档](../README.md)
- [监控模块](../../est-modules/est-foundation/est-monitor/README.md)