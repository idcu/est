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

### 1. AssertUtils - 断言工具类

用于参数验证和前置条件检查。

```java
import ltd.idcu.est.utils.common.AssertUtils;

public class UserService {
    
    public void createUser(User user) {
        // 检查参数不为 null
        AssertUtils.notNull(user, "User cannot be null");
        AssertUtils.notNull(user.getEmail(), "User email cannot be null");
        
        // 检查字符串有内容
        AssertUtils.hasText(user.getName(), "User name cannot be blank");
        AssertUtils.hasLength(user.getEmail(), "User email cannot be empty");
        
        // 检查条件是否满足
        AssertUtils.isTrue(user.getAge() > 0, "Age must be positive");
        AssertUtils.isPositive(user.getAge(), "Age must be greater than zero");
        
        // 检查范围
        AssertUtils.inRange(user.getAge(), 0, 150, "Age must be between 0 and 150");
        
        // 检查类型
        AssertUtils.isInstanceOf(User.class, user, "Must be a User instance");
        
        // 状态检查
        AssertUtils.state(user.isActive(), "User must be active");
        
        // 创建用户...
    }
    
    public void updateUser(String userId, User updateData) {
        // 检查集合不为空
        AssertUtils.notEmpty(userId, "User ID cannot be empty");
        
        // 检查数组不为空且没有 null 元素
        String[] roles = updateData.getRoles();
        AssertUtils.notEmpty(roles, "Roles cannot be empty");
        AssertUtils.noNullElements(roles, "Roles cannot contain null elements");
        
        // 更新用户...
    }
}
```

**完整方法列表：**

| 方法 | 说明 |
|------|------|
| `isTrue(expression, message)` | 检查表达式是否为 true |
| `isFalse(expression, message)` | 检查表达式是否为 false |
| `isNull(object, message)` | 检查对象是否为 null |
| `notNull(object, message)` | 检查对象不为 null |
| `requireNonNull(object, message)` | 要求对象不为 null，返回对象 |
| `hasLength(text, message)` | 检查字符串有长度 |
| `hasText(text, message)` | 检查字符串有内容（非空白） |
| `notEmpty(array/collection/map, message)` | 检查集合/数组/Map 不为空 |
| `noNullElements(array/collection, message)` | 检查没有 null 元素 |
| `isInstanceOf(type, obj, message)` | 检查类型实例 |
| `isAssignable(superType, subType, message)` | 检查类型可赋值 |
| `state(expression, message)` | 状态检查（抛 IllegalStateException） |
| `inRange(value, min, max, message)` | 范围检查 |
| `isPositive(value, message)` | 正数检查 |
| `isNegative(value, message)` | 负数检查 |
| `notBlank(str, message)` | 非空字符串检查 |
| `matches(str, regex, message)` | 正则匹配检查 |
| `equals(obj1, obj2, message)` | 相等检查 |
| `notEquals(obj1, obj2, message)` | 不等检查 |

### 2. ObjectUtils - 对象工具类

用于对象 null 检查、比较、转换等操作。

```java
import ltd.idcu.est.utils.common.ObjectUtils;

public class DataProcessor {
    
    public void processData(Object data) {
        // null 检查
        if (ObjectUtils.isNull(data)) {
            System.out.println("Data is null");
            return;
        }
        
        if (ObjectUtils.isNotNull(data)) {
            System.out.println("Data is not null");
        }
        
        // 空值检查（支持字符串、集合、数组、Map、Optional）
        if (ObjectUtils.isEmpty(data)) {
            System.out.println("Data is empty");
        }
        
        if (ObjectUtils.isNotEmpty(data)) {
            System.out.println("Data is not empty");
        }
        
        // 处理数据...
    }
    
    public String formatUser(User user) {
        // 默认值处理
        String name = ObjectUtils.defaultIfNull(user.getName(), "Unknown");
        String email = ObjectUtils.defaultIfNull(user.getEmail(), () -> "default@example.com");
        
        // 获取第一个非 null 值
        String displayName = ObjectUtils.firstNonNull(
            user.getNickname(),
            user.getFullName(),
            user.getUsername(),
            "Anonymous"
        );
        
        return displayName;
    }
    
    public boolean compareUsers(User user1, User user2) {
        // 安全的 equals 比较
        return ObjectUtils.equals(user1, user2);
    }
    
    public String safeToString(Object obj) {
        // 安全的 toString
        return ObjectUtils.toString(obj, "N/A");
    }
    
    public int getLength(Object obj) {
        // 获取长度（支持字符串、数组、集合、Map、Optional）
        return ObjectUtils.length(obj);
    }
    
    public <T> java.util.Optional<T> toOptional(T obj) {
        // 转换为 Optional
        return ObjectUtils.toOptional(obj);
    }
}
```

**完整方法列表：**

| 方法 | 说明 |
|------|------|
| `isNull(obj)` | 检查是否为 null |
| `isNotNull(obj)` | 检查是否不为 null |
| `isEmpty(obj)` | 检查是否为空（支持多种类型） |
| `isNotEmpty(obj)` | 检查是否不为空 |
| `allNull(array)` | 检查是否全部为 null |
| `allNotNull(array)` | 检查是否全部不为 null |
| `anyNull(array)` | 检查是否有任何为 null |
| `equals(o1, o2)` | 安全的 equals 比较 |
| `notEqual(o1, o2)` | 安全的 not equals 比较 |
| `hashCode(obj)` | 安全的 hashCode |
| `hash(values)` | 多个值的 hashCode |
| `identityToString(obj)` | 身份字符串（类名@hash） |
| `defaultIfNull(object, defaultValue)` | null 时返回默认值 |
| `defaultIfNull(object, supplier)` | null 时通过 supplier 获取默认值 |
| `firstNonNull(values)` | 获取第一个非 null 值 |
| `clone(obj)` | 克隆对象 |
| `cloneSerializable(obj)` | 序列化克隆 |
| `serialize(obj)` | 序列化 |
| `deserialize(data)` | 反序列化 |
| `toString(obj)` | 安全的 toString |
| `toString(obj, nullStr)` | 带 null 替代的 toString |
| `nullToEmpty(array/charSequence)` | null 转为空 |
| `emptyToNull(array/charSequence)` | 空转为 null |
| `compare(c1, c2)` | 比较 |
| `min(values)` | 获取最小值 |
| `max(values)` | 获取最大值 |
| `length(obj)` | 获取长度 |
| `isArray(obj)` | 检查是否为数组 |
| `contains(array, value)` | 检查数组是否包含值 |
| `indexOf(array, value)` | 查找值在数组中的索引 |
| `requireNonNull(obj)` | 要求非 null |
| `isSameType(o1, o2)` | 检查是否同类型 |
| `isInstanceOf(type, obj)` | 检查是否是实例 |
| `toOptional(obj)` | 转为 Optional |
| `toOptionalIfEmpty(obj)` | 空值时转为 empty Optional |

### 3. StringUtils - 字符串工具类

用于字符串处理、检查、转换等操作。

```java
import ltd.idcu.est.utils.common.StringUtils;

public class StringProcessor {
    
    public void validateInput(String input) {
        // 字符串检查
        boolean isEmpty = StringUtils.isEmpty(input);           // 检查是否为空
        boolean isNotEmpty = StringUtils.isNotEmpty(input);     // 检查是否不为空
        boolean isBlank = StringUtils.isBlank(input);           // 检查是否为空或仅空白字符
        boolean isNotBlank = StringUtils.isNotBlank(input);     // 检查是否不为空且不只是空白字符
        
        System.out.println("isEmpty: " + isEmpty);
        System.out.println("isBlank: " + isBlank);
    }
    
    public String cleanString(String str) {
        // 字符串修剪
        String trimmed = StringUtils.trim(str);                // 去除首尾空白
        String trimToNull = StringUtils.trimToNull(str);       // 修剪后如果为空返回 null
        String trimToEmpty = StringUtils.trimToEmpty(str);     // 修剪后如果为空返回 ""
        
        // 默认值处理
        String defaultIfEmpty = StringUtils.defaultIfEmpty(str, "default");
        String defaultIfBlank = StringUtils.defaultIfBlank(str, "default");
        
        return trimmed;
    }
    
    public String formatName(String firstName, String lastName) {
        // 字符串比较
        boolean equals = StringUtils.equals(firstName, lastName);
        boolean equalsIgnoreCase = StringUtils.equalsIgnoreCase(firstName, lastName);
        
        // 大小写转换
        String upper = StringUtils.upperCase(firstName);
        String lower = StringUtils.lowerCase(lastName);
        String capitalized = StringUtils.capitalize(firstName);
        String uncapitalized = StringUtils.uncapitalize(firstName);
        
        return capitalized + " " + capitalized;
    }
    
    public void checkPrefixSuffix(String str) {
        // 前缀后缀检查
        boolean startsWith = StringUtils.startsWith(str, "http");
        boolean startsWithIgnoreCase = StringUtils.startsWithIgnoreCase(str, "HTTP");
        boolean endsWith = StringUtils.endsWith(str, ".com");
        boolean endsWithIgnoreCase = StringUtils.endsWithIgnoreCase(str, ".COM");
        
        // 包含检查
        boolean contains = StringUtils.contains(str, "@");
        boolean containsIgnoreCase = StringUtils.containsIgnoreCase(str, "EST");
        
        // 索引查找
        int indexOf = StringUtils.indexOf(str, "@");
        int lastIndexOf = StringUtils.lastIndexOf(str, ".");
    }
    
    public String extractSubstring(String str) {
        // 子字符串操作
        String substring = StringUtils.substring(str, 5);        // 从索引 5 开始
        String substringRange = StringUtils.substring(str, 0, 10);  // 从 0 到 10
        String left = StringUtils.left(str, 5);                 // 左边 5 个字符
        String right = StringUtils.right(str, 5);               // 右边 5 个字符
        String mid = StringUtils.mid(str, 2, 3);                // 从索引 2 开始取 3 个字符
        
        return left;
    }
    
    public String[] splitAndJoin(String str) {
        // 字符串分割
        String[] parts = StringUtils.split(str, ",");            // 用逗号分割
        String[] partsWithLimit = StringUtils.split(str, ",", 3, false); // 最多分割 3 次
        
        // 字符串拼接
        String[] array = {"a", "b", "c"};
        String joinedArray = StringUtils.join(array, ", ");
        
        java.util.List<String> list = java.util.Arrays.asList("x", "y", "z");
        String joinedList = StringUtils.join(list, " - ");
        
        return parts;
    }
    
    public String modifyString(String str) {
        // 字符串修改
        String reversed = StringUtils.reverse(str);              // 反转
        String repeated = StringUtils.repeat(str, 3);            // 重复 3 次
        String replaced = StringUtils.replace(str, "old", "new"); // 替换
        String replacedAll = StringUtils.replaceAll(str, "\\d", "*"); // 正则替换全部
        String replacedFirst = StringUtils.replaceFirst(str, "\\d", "*"); // 正则替换第一个
        
        // 移除
        String removed = StringUtils.remove(str, "remove");
        String removedStart = StringUtils.removeStart(str, "prefix-");
        String removedEnd = StringUtils.removeEnd(str, "-suffix");
        
        return replaced;
    }
    
    public String padString(String str) {
        // 填充
        String padLeft = StringUtils.padLeft(str, 10, '0');      // 左填充到 10 位，用 0
        String padRight = StringUtils.padRight(str, 10, ' ');    // 右填充到 10 位，用空格
        String center = StringUtils.center(str, 10, '-');        // 居中，用 - 填充
        
        return padLeft;
    }
    
    public String truncateString(String str) {
        // 截断
        String truncated = StringUtils.truncate(str, 20);         // 截断到 20 字符，默认用 ...
        String truncatedWithIndicator = StringUtils.truncate(str, 20, "[...]"); // 自定义截断符
        
        return truncated;
    }
    
    public boolean checkStringType(String str) {
        // 字符串类型检查
        boolean isNumeric = StringUtils.isNumeric(str);           // 是否纯数字
        boolean isAlpha = StringUtils.isAlpha(str);               // 是否纯字母
        boolean isAlphanumeric = StringUtils.isAlphanumeric(str); // 是否字母数字
        boolean isWhitespace = StringUtils.isWhitespace(str);     // 是否纯空白
        
        return isNumeric;
    }
    
    public int countOccurrences(String str, String sub) {
        // 统计出现次数
        return StringUtils.countMatches(str, sub);
    }
}
```

**完整方法列表（部分）：**

| 分类 | 方法 |
|------|------|
| **检查** | `isEmpty`, `isNotEmpty`, `isBlank`, `isNotBlank` |
| **修剪** | `trim`, `trimToNull`, `trimToEmpty` |
| **默认值** | `defaultIfEmpty`, `defaultIfBlank` |
| **比较** | `equals`, `equalsIgnoreCase`, `compare`, `compareIgnoreCase` |
| **前缀后缀** | `startsWith`, `startsWithIgnoreCase`, `endsWith`, `endsWithIgnoreCase` |
| **包含** | `contains`, `containsIgnoreCase`, `indexOf`, `lastIndexOf` |
| **子字符串** | `substring`, `left`, `right`, `mid` |
| **分割拼接** | `split`, `join` |
| **大小写** | `upperCase`, `lowerCase`, `capitalize`, `uncapitalize` |
| **修改** | `reverse`, `repeat`, `replace`, `replaceAll`, `replaceFirst` |
| **移除** | `remove`, `removeStart`, `removeEnd` |
| **填充** | `padLeft`, `padRight`, `center` |
| **截断** | `truncate`, `abbreviate` |
| **类型检查** | `isNumeric`, `isAlpha`, `isAlphanumeric`, `isWhitespace` |
| **其他** | `length`, `countMatches`, `strip`, `stripStart`, `stripEnd`, `chomp`, `chop` |

### 4. 其他工具类

- **ArrayUtils**：数组工具类
- **NumberUtils**：数字工具类
- **DateUtils**：日期工具类
- **ClassUtils**：类工具类

## IO工具 (est-utils-io)

```java
import ltd.idcu.est.utils.io.Files;

// 文件读取
String content = Files.readString("./file.txt");
java.util.List<String> lines = Files.readLines("./file.txt");
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

## 最佳实践

1. **优先使用工具类**：避免手写 null 检查、字符串处理等通用逻辑
2. **代码审查**：审查代码时确保使用了现有的工具类
3. **不要重复造轮子**：在写通用逻辑前，先检查是否已有工具类方法
4. **保持一致性**：团队统一使用这些工具类，保持代码风格一致
