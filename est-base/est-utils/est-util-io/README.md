# est-util-io - 小白从入门到精通

## 目录
- [什么是 est-util-io](#什么是-est-util-io)
- [快速入门](#快速入门)
- [核心功能](#核心功能)
- [相关资源](#相关资源)

---

## 什么是 est-util-io

### 用大白话理解
est-util-io 就像"文件操作小助手"，帮你读写文件、复制文件、处理流，简单又安全。

### 核心特点
- **文件操作**：读写、复制、删除
- **流处理**：InputStream/OutputStream 工具
- **资源管理**：自动关闭资源
- **异常处理**：统一的 IO 异常

---

## 快速入门

### 1. 添加依赖
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-util-io</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 2. 基本使用
```java
@Service
public class FileService {
    
    public String readFile(String path) {
        return IoUtil.readString(path);
    }
    
    public void writeFile(String path, String content) {
        IoUtil.writeString(path, content);
    }
    
    public void copyFile(String from, String to) {
        IoUtil.copy(from, to);
    }
}
```

---

## 相关资源

- [父模块文档](../README.md)
- [EST 工具指南](../../docs/utils/README.md)
