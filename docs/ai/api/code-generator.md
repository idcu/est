# 代码生成器 API 参考

## CodeGenerator 接口

```java
public interface CodeGenerator {
    String generateEntity(String className, String packageName, Map<String, Object> options);
    String generateRepository(String interfaceName, String packageName, Map<String, Object> options);
    String generateService(String className, String packageName, Map<String, Object> options);
    String generateController(String className, String packageName, Map<String, Object> options);
    String generatePomXml(String projectName, String groupId, String artifactId, String version);
}
```

### 方法说明

#### generateEntity(String className, String packageName, Map<String, Object> options)
生成实体类代码。
**参数：**
- `className` - 实体类名
- `packageName` - 包名
- `options` - 选项，包括：
  - `fields` - 字段列表，格式为 ["字段名:类型", ...]
  - `useLombok` - 是否使用 Lombok（可选，默认 false）
  - `tableName` - 数据库表名（可选）

**返回值：** 生成的实体类代码

**示例：**
```java
String entity = generator.generateEntity("Product", "com.example.entity",
    Map.of(
        "fields", List.of("id:Long", "name:String", "price:BigDecimal", "createTime:LocalDateTime"),
        "useLombok", true
    )
);
```

---

#### generateRepository(String interfaceName, String packageName, Map<String, Object> options)
生成 Repository 接口代码。
**参数：**
- `interfaceName` - 接口名
- `packageName` - 包名
- `options` - 选项

**返回值：** 生成的 Repository 接口代码

**示例：**
```java
String repo = generator.generateRepository("ProductRepository", "com.example.repository", Map.of());
```

---

#### generateService(String className, String packageName, Map<String, Object> options)
生成 Service 类代码。
**参数：**
- `className` - Service 类名
- `packageName` - 包名
- `options` - 选项
  - `entityName` - 实体类名（可选）
  - `repositoryName` - Repository 接口名（可选）

**返回值：** 生成的 Service 类代码
**示例：**
```java
String service = generator.generateService("ProductService", "com.example.service",
    Map.of(
        "entityName", "Product",
        "repositoryName", "ProductRepository"
    )
);
```

---

#### generateController(String className, String packageName, Map<String, Object> options)
生成 Controller 类代码。
**参数：**
- `className` - Controller 类名
- `packageName` - 包名
- `options` - 选项
  - `serviceName` - Service 类名（可选）
  - `basePath` - 基础路径（可选）

**返回值：** 生成的 Controller 类代码
**示例：**
```java
String controller = generator.generateController("ProductController", "com.example.controller",
    Map.of(
        "serviceName", "ProductService",
        "basePath", "/api/products"
    )
);
```

---

#### generatePomXml(String projectName, String groupId, String artifactId, String version)
生成 pom.xml 文件内容。
**参数：**
- `projectName` - 项目名称
- `groupId` - Maven groupId
- `artifactId` - Maven artifactId
- `version` - 项目版本

**返回值：** pom.xml 内容

**示例：**
```java
String pom = generator.generatePomXml("MyProject", "com.example", "my-app", "1.0.0");
```

---

## DefaultCodeGenerator 实现

默认的代码生成器实现类。

### 构造方法
```java
public DefaultCodeGenerator()
```

**示例：**
```java
CodeGenerator generator = new DefaultCodeGenerator();
```

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08