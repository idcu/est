# est-util-format-json - 小白从入门到精通

## 目录
- [什么是 est-util-format-json](#什么是-est-util-format-json)
- [快速入门](#快速入门)
- [核心功能](#核心功能)
- [最佳实践](#最佳实践)
- [相关资源](#相关资源)

---

## 什么是 est-util-format-json

### 用大白话理解
est-util-format-json 就像"JSON 翻译官"，帮你把 Java 对象转换成 JSON 字符串，或者把 JSON 字符串转回 Java 对象。

### 核心特点
- **简单易用**：一行代码搞定序列化/反序列化
- **多引擎支持**：Jackson、Gson、Fastjson
- **高性能**：优化的序列化性能
- **灵活配置**：支持自定义序列化规则

---

## 快速入门

### 1. 添加依赖
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-util-format-json</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 2. 基本使用
```java
@Service
public class UserService {
    
    @Inject
    private JsonService jsonService;
    
    public String toJson(User user) {
        return jsonService.toJson(user);
    }
    
    public User fromJson(String json) {
        return jsonService.fromJson(json, User.class);
    }
}
```

---

## 相关资源

- [父模块文档](../README.md)
- [EST 工具指南](../../docs/utils/README.md)
