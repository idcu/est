# est-codegen - 小白从入门到精通

## 目录
- [什么是 est-codegen](#什么是-est-codegen)
- [快速入门](#快速入门)
- [核心功能](#核心功能)
- [模块结构](#模块结构)
- [相关资源](#相关资源)

---

## 什么是 est-codegen

### 用大白话理解
est-codegen 就像"代码生成器"，根据数据库表或配置自动生成 Entity、Controller、Service、Mapper 等代码。

### 核心特点
- **数据库表生成**：从数据库表生成代码
- **模板自定义**：支持自定义代码模板
- **多模块生成**：支持多种代码风格
- **增量更新**：支持增量代码更新

---

## 快速入门

### 1. 添加依赖
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-codegen</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 2. 配置生成
```yaml
est:
  codegen:
    database:
      url: jdbc:mysql://localhost:3306/est
      username: root
      password: root
    output:
      dir: ./src/main/java
    template:
      prefix: ltd.idcu.est.demo
```

### 3. 生成代码
```bash
# 生成单表代码
est codegen User

# 生成所有表代码
est codegen --all
```

---

## 核心功能

### 代码生成器
```java
@Service
public class CodeGenerator {
    
    public void generate(String tableName) {
        TableInfo table = databaseService.getTableInfo(tableName);
        
        EntityCode entityCode = templateEngine.render("entity", table);
        MapperCode mapperCode = templateEngine.render("mapper", table);
        ServiceCode serviceCode = templateEngine.render("service", table);
        ControllerCode controllerCode = templateEngine.render("controller", table);
        
        fileService.save(entityCode);
        fileService.save(mapperCode);
        fileService.save(serviceCode);
        fileService.save(controllerCode);
    }
}
```

### 自定义模板
```java
@Template(name = "my-entity", path = "templates/entity.java.template")
public class MyEntityTemplate {
    
    @Render
    public String render(TableInfo table) {
        return templateEngine.render(table);
    }
}
```

---

## 模块结构

```
est-codegen/
├── src/main/java/
│   └── ltd/idcu/est/tools/codegen/
│       ├── generator/
│       ├── template/
│       └── database/
├── src/main/resources/
│   └── templates/
├── README.md
└── pom.xml
```

---

## 相关资源

- [父模块文档](../README.md)
- [CLI 工具](../est-cli/README.md)
