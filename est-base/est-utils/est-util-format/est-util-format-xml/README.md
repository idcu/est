# est-util-format-xml - 小白从入门到精通

## 目录
- [什么是 est-util-format-xml](#什么是-est-util-format-xml)
- [快速入门](#快速入门)
- [核心功能](#核心功能)
- [相关资源](#相关资源)

---

## 什么是 est-util-format-xml

### 用大白话理解
est-util-format-xml 就像"XML 翻译官"，帮你在 Java 对象和 XML 之间互相转换。

### 核心特点
- **JAXB 支持**：标准 JAXB 注解
- **多引擎**：JAXB、Jackson XML
- **简单易用**：统一的 API

---

## 快速入门

### 1. 添加依赖
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-util-format-xml</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 2. 基本使用
```java
@Service
public class XmlService {
    
    @Inject
    private XmlService xmlService;
    
    public String toXml(User user) {
        return xmlService.toXml(user);
    }
    
    public User fromXml(String xml) {
        return xmlService.fromXml(xml, User.class);
    }
}
```

---

## 相关资源

- [父模块文档](../README.md)
- [EST 工具指南](../../docs/utils/README.md)
