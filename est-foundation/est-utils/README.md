# EST Utils 工具模块

提供通用工具类，包括通用工具、IO工具和格式化工具。

## 模块结构

```
est-utils/
├── est-utils-common/      # 通用工具
├── est-utils-io/        # IO工具
├── est-utils-format/  # 格式化工具
│   ├── est-utils-format-json/
│   ├── est-utils-format-xml/
│   └── est-utils-format-yaml/
└── pom.xml
```

## 通用工具 (est-utils-common)

### 字符串工具

```java
import ltd.idcu.est.utils.common.Strings;

// 字符串检查
boolean empty = Strings.isEmpty(str);
boolean blank = Strings.isBlank(str);

// 字符串操作
String reversed = Strings.reverse(str);
String capitalized = Strings.capitalize(str);

// 字符串转换
List<String> parts = Strings.split(str, ",");
String joined = Strings.join(list, ", ");
```

### 日期时间工具

```java
import ltd.idcu.est.utils.common.Dates;

// 当前时间
LocalDateTime now = Dates.now();
String formatted = Dates.format(now, "yyyy-MM-dd HH:mm:ss");

// 日期计算
LocalDateTime tomorrow = Dates.addDays(now, 1);
LocalDateTime yesterday = Dates.subtractDays(now, 1);

// 日期解析
LocalDateTime parsed = Dates.parse("2024-01-01", "yyyy-MM-dd");
```

### 集合工具

```java
import ltd.idcu.est.utils.common.Collections2;

// 集合检查
boolean empty = Collections2.isEmpty(collection);

// 集合操作
List<T> first = Collections2.first(collection);
List<T> last = Collections2.last(collection);

// 集合转换
List<T> filtered = Collections2.filter(collection, predicate);
```

## IO工具 (est-utils-io)

```java
import ltd.idcu.est.utils.io.Files;

// 文件读取
String content = Files.readString("./file.txt");
List<String> lines = Files.readLines("./file.txt");
byte[] bytes = Files.readBytes("./file.bin");

// 文件写入
Files.writeString("./file.txt", content);
Files.writeBytes("./file.bin", bytes);

// 文件操作
Files.createDirectories("./path/to/dir");
Files.deleteIfExists("./file.txt");
boolean exists = Files.exists("./file.txt");
```

## 格式化工具

### JSON 格式化

```java
import ltd.idcu.est.utils.format.json.Json;

// JSON解析
User user = Json.parse(jsonString, User.class);
String json = Json.stringify(user);

// 格式化
String pretty = Json.prettyPrint(jsonString);
```

### XML 格式化

```java
import ltd.idcu.est.utils.format.xml.Xml;

// XML解析
User user = Xml.parse(xmlString, User.class);
String xml = Xml.stringify(user);
```

### YAML 格式化

```java
import ltd.idcu.est.utils.format.yaml.Yaml;

// YAML解析
User user = Yaml.parse(yamlString, User.class);
String yaml = Yaml.stringify(user);
```

## 依赖

```xml
<!-- 通用工具 -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-utils-common</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>

<!-- IO工具 -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-utils-io</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>

<!-- JSON格式化 -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-utils-format-json</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
```
