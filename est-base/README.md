# EST Base 基础模块 - 小白从入门到精通

## 目录
1. [什么是 EST Base？](#什么是-est-base)
2. [快速入门：5分钟上手](#快速入门5分钟上手)
3. [基础篇](#基础篇)
4. [进阶篇](#进阶篇)
5. [最佳实践](#最佳实践)

---

## 什么是 EST Base？

### 用大白话理解

EST Base 就像是一个"工具箱"。想象一下你在做家务，需要各种工具：螺丝刀、扳手、卷尺、剪刀...

**传统方式**：每次需要什么工具都自己去买，很麻烦。

**EST Base 方式**：给你一个装满常用工具的工具箱，里面有：
- 🔧 **工具集** - JSON、XML、YAML、IO 等常用工具
- 📐 **设计模式** - 单例、工厂、观察者等常用设计模式
- 📦 **集合框架** - 更强大的集合操作
- 🧪 **测试支持** - 单元测试和基准测试

### 核心特点

- 🎯 **简单易用** - 开箱即用，零依赖
- ⚡ **高性能** - 纯 Java 实现，性能优秀
- 🔒 **零依赖** - 不依赖任何第三方库
- 🎨 **丰富功能** - 涵盖常用工具和设计模式

---

## 快速入门：5分钟上手

### 第一步：引入依赖

在你的 Maven pom.xml 中添加：

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-util-common</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-collection</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>
```

### 第二步：你的第一个程序

```java
import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;
import ltd.idcu.est.util.common.StringUtils;

public class FirstExample {
    public static void main(String[] args) {
        System.out.println("=== EST Base 第一个示例 ===\n");
        
        System.out.println("--- 1. 使用工具类 ---");
        String str = "  Hello, EST!  ";
        System.out.println("原字符串: '" + str + "'");
        System.out.println("去除空白: '" + StringUtils.trim(str) + "'");
        System.out.println("是否为空: " + StringUtils.isEmpty(str));
        
        System.out.println("\n--- 2. 使用集合框架 ---");
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Seq<Integer> evenNumbers = numbers.where(n -> n % 2 == 0);
        System.out.println("偶数: " + evenNumbers.toList());
        
        System.out.println("\n恭喜你！你已经成功使用 EST Base 了！");
    }
}
```

运行这个程序，你会看到：
```
=== EST Base 第一个示例 ===

--- 1. 使用工具类 ---
原字符串: '  Hello, EST!  '
去除空白: 'Hello, EST!'
是否为空: false

--- 2. 使用集合框架 ---
偶数: [2, 4]

恭喜你！你已经成功使用 EST Base 了！
```

---

## 基础篇

### 1. est-utils 工具模块

EST Utils 提供了常用的工具类，让你不用重复造轮子。

#### est-util-common 常用工具

```java
import ltd.idcu.est.util.common.StringUtils;
import ltd.idcu.est.util.common.NumberUtils;
import ltd.idcu.est.util.common.DateUtils;

// 字符串工具
String str = "Hello, World!";
boolean isBlank = StringUtils.isBlank(str);
String reversed = StringUtils.reverse(str);

// 数字工具
String numStr = "123";
int num = NumberUtils.toInt(numStr, 0);
boolean isNumber = NumberUtils.isNumber(numStr);

// 日期工具
String now = DateUtils.formatNow("yyyy-MM-dd HH:mm:ss");
```

#### est-util-format 格式工具

```java
import ltd.idcu.est.util.format.json.JsonUtils;
import ltd.idcu.est.util.format.xml.XmlUtils;
import ltd.idcu.est.util.format.yaml.YamlUtils;

// JSON 处理
String json = JsonUtils.toJson(user);
User user = JsonUtils.fromJson(json, User.class);

// XML 处理
String xml = XmlUtils.toXml(user);
User user = XmlUtils.fromXml(xml, User.class);

// YAML 处理
String yaml = YamlUtils.toYaml(config);
Config config = YamlUtils.fromYaml(yaml, Config.class);
```

#### est-util-io IO 工具

```java
import ltd.idcu.est.util.io.FileUtils;
import ltd.idcu.est.util.io.IOUtils;

// 文件操作
String content = FileUtils.readFileToString("data.txt");
FileUtils.writeStringToFile("output.txt", "Hello!");

// IO 流操作
InputStream is = ...;
String text = IOUtils.toString(is);
```

### 2. est-collection 集合模块

EST Collection 提供了更强大的集合操作，像说话一样写代码！

详细文档请参考：[est-collection README](./est-collection/README.md)

```java
import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;

// 创建序列
Seq<String> fruits = Seqs.of("苹果", "香蕉", "橙子", "葡萄");

// 链式操作
Seq<String> result = fruits
    .where(fruit -> fruit.length() > 2)  // 筛选
    .sorted()                               // 排序
    .take(3);                               // 取前3个

System.out.println(result.toList());  // [橙子, 葡萄, 香蕉]
```

### 3. est-patterns 设计模式模块

EST Patterns 提供了常用设计模式的开箱即用实现。

详细文档请参考：[est-patterns README](./est-patterns/README.md)

```java
import ltd.idcu.est.patterns.api.creational.Singleton;
import ltd.idcu.est.patterns.impl.creational.DefaultSingleton;

// 单例模式
Singleton<DatabaseConnection> singleton = DefaultSingleton.of(DatabaseConnection::new);
DatabaseConnection conn1 = singleton.getInstance();
DatabaseConnection conn2 = singleton.getInstance();
System.out.println(conn1 == conn2);  // true
```

### 4. est-test 测试模块

EST Test 提供了测试支持和基准测试。

详细文档请参考：[est-test README](./est-test/README.md)

```java
import ltd.idcu.est.test.api.Assertions;
import ltd.idcu.est.test.api.Tests;

// 断言
Assertions.assertEquals(2, 1 + 1);
Assertions.assertTrue(true);

// 测试运行
Tests.run(MyTestSuite.class);
```

---

## 进阶篇

### 1. 工具模块进阶

#### 自定义工具类

你可以基于 EST Utils 创建自己的工具类：

```java
public class MyStringUtils {
    
    public static String capitalize(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
```

#### 链式工具调用

```java
String result = StringUtils.trim(str)
    .toLowerCase()
    .replace(" ", "-");
```

### 2. 集合模块进阶

详细内容请参考：[est-collection 进阶篇](./est-collection/README.md)

### 3. 设计模式进阶

详细内容请参考：[est-patterns 进阶篇](./est-patterns/README.md)

---

## 最佳实践

### 1. 优先使用工具类

```java
// ✅ 推荐：使用工具类
if (StringUtils.isBlank(str)) {
    // ...
}

// ❌ 不推荐：自己写
if (str == null || str.trim().isEmpty()) {
    // ...
}
```

### 2. 合理使用集合操作

```java
// ✅ 推荐：使用链式操作
List<String> result = users
    .where(User::isActive)
    .sortBy(User::getName)
    .pluck(User::getEmail)
    .toList();

// ❌ 不推荐：手写循环
List<String> result = new ArrayList<>();
for (User user : users) {
    if (user.isActive()) {
        result.add(user.getEmail());
    }
}
Collections.sort(result);
```

### 3. 不要过度设计

```java
// ✅ 简单直接
if (type == TYPE_A) {
    doA();
} else {
    doB();
}

// ❌ 过度设计（除非确实需要）
// 为简单场景使用复杂的设计模式
```

---

## 模块结构

```
est-base/
├── est-utils/              # 工具模块
│   ├── est-util-common/   # 常用工具
│   ├── est-util-format/   # 格式工具（JSON、XML、YAML）
│   └── est-util-io/       # IO 工具
├── est-patterns/           # 设计模式模块
├── est-collection/         # 集合框架模块
└── est-test/               # 测试支持模块
```

---

## 相关资源

- [est-utils README](./est-utils/README.md) - 工具模块详细文档
- [est-collection README](./est-collection/README.md) - 集合模块详细文档
- [est-patterns README](./est-patterns/README.md) - 设计模式详细文档
- [est-test README](./est-test/README.md) - 测试模块详细文档
- [EST Core](../est-core/README.md) - 核心模块
- [示例代码](../est-examples/est-examples-basic/) - 示例代码

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
