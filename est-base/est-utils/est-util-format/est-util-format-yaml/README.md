# est-util-format-yaml - 小白从入门到精通

## 目录
- [什么是 est-util-format-yaml](#什么是-est-util-format-yaml)
- [快速入门](#快速入门)
- [核心功能](#核心功能)
- [相关资源](#相关资源)

---

## 什么是 est-util-format-yaml

### 用大白话理解
est-util-format-yaml 就像"YAML 翻译官"，帮你在 Java 对象和 YAML 之间互相转换。

### 核心特点
- **SnakeYAML 支持**：高性能 YAML 处理
- **配置友好**：适合配置文件读写
- **类型安全**：完整的类型支持

---

## 快速入门

### 1. 添加依赖
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-util-format-yaml</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 2. 基本使用
```java
@Service
public class YamlService {
    
    @Inject
    private YamlService yamlService;
    
    public String toYaml(Config config) {
        return yamlService.toYaml(config);
    }
    
    public Config fromYaml(String yaml) {
        return yamlService.fromYaml(yaml, Config.class);
    }
}
```

---

## 相关资源

- [父模块文档](../README.md)
- [EST 工具指南](../../docs/utils/README.md)
