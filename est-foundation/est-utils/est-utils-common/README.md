# EST Utils Common - 通用工具集

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

EST Utils Common 是 EST 框架的通用工具模块，提供了一系列开箱即用的工具类，帮助你解决日常开发中的各种常见问题，避免重复造轮子。

---

## 📚 目录

- [快速入门](#快速入门)
- [基础篇：字符串工具](#基础篇字符串工具)
- [基础篇：对象工具](#基础篇对象工具)
- [基础篇：断言工具](#基础篇断言工具)
- [进阶篇：数组工具](#进阶篇数组工具)
- [进阶篇：数值工具](#进阶篇数值工具)
- [最佳实践](#最佳实践)

---

## 🚀 快速入门

### 什么是通用工具？

想象你在做菜，每次做饭都需要菜刀、菜板、锅碗瓢盆。这些工具不是菜本身，但没有它们你很难做出菜来。

**EST Utils Common** 就是编程中的"厨房工具包"，它提供了：
- 🔪 **字符串处理** - 像切菜一样处理文本
- 🍳 **对象操作** - 像摆盘一样整理数据
- 🥄 **断言验证** - 像尝咸淡一样检查参数
- 等等...

### 5分钟上手

#### 1. 添加依赖

在你的 `pom.xml` 中添加：

```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-utils-common</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### 2. 第一个工具使用

让我们用 `StringUtils` 来处理字符串：

```java
import ltd.idcu.est.utils.common.StringUtils;

public class FirstExample {
    public static void main(String[] args) {
        // 检查字符串是否为空
        String name = "  EST Framework  ";
        System.out.println("是否为空: " + StringUtils.isEmpty(name));
        
        // 去除空格
        String trimmed = StringUtils.trim(name);
        System.out.println("去除空格: " + trimmed);
        
        // 首字母大写
        String capitalized = StringUtils.capitalize(trimmed);
        System.out.println("首字母大写: " + capitalized);
    }
}
```

运行结果：
```
是否为空: false
去除空格: EST Framework
首字母大写: Est Framework
```

恭喜！你已经学会使用 EST 工具了！ 🎉

---

## 🔰 基础篇：字符串工具

### 生活类比

字符串就像一段文字信息，比如短信、邮件内容。`StringUtils` 就是帮你处理这些文字的"秘书"，帮你检查、修改、整理文字。

### 常用功能

#### 1. 空值检查

```java
import ltd.idcu.est.utils.common.StringUtils;

public class StringEmptyExample {
    public static void main(String[] args) {
        // isEmpty - 检查是否为 null 或 ""
        System.out.println(StringUtils.isEmpty(null));      // true
        System.out.println(StringUtils.isEmpty(""));        // true
        System.out.println(StringUtils.isEmpty("  "));      // false (有空格)
        
        // isBlank - 检查是否为 null、"" 或全是空格
        System.out.println(StringUtils.isBlank(null));       // true
        System.out.println(StringUtils.isBlank(""));         // true
        System.out.println(StringUtils.isBlank("  "));       // true
        System.out.println(StringUtils.isBlank("EST"));      // false
    }
}
```

**什么时候用哪个？**
- `isEmpty`：严格检查，只有 null 或 "" 才算空
- `isBlank`：宽松检查，全是空格也算空（更常用）

#### 2. 去除空格

```java
import ltd.idcu.est.utils.common.StringUtils;

public class StringTrimExample {
    public static void main(String[] args) {
        String str = "   Hello EST   ";
        
        // trim - 去除首尾空格
        System.out.println(StringUtils.trim(str));                 // "Hello EST"
        
        // trimToNull - 去除空格后如果是空则返回 null
        System.out.println(StringUtils.trimToNull("   "));         // null
        
        // trimToEmpty - 去除空格后如果是空则返回 ""
        System.out.println(StringUtils.trimToEmpty(null));         // ""
    }
}
```

---

## 🔰 基础篇：对象工具

### 生活类比

对象就像各种物品，`ObjectUtils` 就是帮你检查、比较、复制这些物品的"管家"。

### 常用功能

#### 1. 空值检查

```java
import ltd.idcu.est.utils.common.ObjectUtils;
import java.util.ArrayList;
import java.util.List;

public class ObjectEmptyExample {
    public static void main(String[] args) {
        // isEmpty - 检查对象是否为空（支持多种类型）
        System.out.println(ObjectUtils.isEmpty(null));              // true
        System.out.println(ObjectUtils.isEmpty(""));                // true
        System.out.println(ObjectUtils.isEmpty(new Object[0]));     // true
        System.out.println(ObjectUtils.isEmpty(new ArrayList<>()));  // true
    }
}
```

---

## 🔰 基础篇：断言工具

### 生活类比

断言就像"安检"，在进入重要区域前检查是否符合要求。`AssertUtils` 帮助你在代码执行前验证参数，提前发现问题。

### 常用断言

```java
import ltd.idcu.est.utils.common.AssertUtils;

public class AssertExample {
    public static void main(String[] args) {
        // notNull - 检查不为 null
        String name = "EST";
        AssertUtils.notNull(name, "名称不能为空");
        
        // isPositive - 检查必须为正数
        int count = 10;
        AssertUtils.isPositive(count, "数量必须大于0");
    }
}
```

---

## 📈 进阶篇：数组工具

### 生活类比

数组就像一排储物柜，每个柜子放一个东西。`ArrayUtils` 帮你管理这些储物柜。

### 常用功能

```java
import ltd.idcu.est.utils.common.ArrayUtils;
import java.util.Arrays;

public class ArrayExample {
    public static void main(String[] args) {
        String[] fruits = {"apple", "banana", "orange"};
        
        // isEmpty - 检查数组是否为空
        System.out.println(ArrayUtils.isEmpty(fruits));  // false
        
        // contains - 检查是否包含元素
        System.out.println(ArrayUtils.contains(fruits, "banana"));  // true
        
        // add - 添加元素
        String[] moreFruits = ArrayUtils.add(fruits, "grape");
        System.out.println(Arrays.toString(moreFruits));  // [apple, banana, orange, grape]
    }
}
```

---

## 📈 进阶篇：数值工具

### 生活类比

数值就像数学题中的数字，`NumberUtils` 就是你的"计算器"，帮你转换、计算、验证数字。

### 常用功能

```java
import ltd.idcu.est.utils.common.NumberUtils;

public class NumberExample {
    public static void main(String[] args) {
        // 字符串转数字（带默认值）
        String str = "123";
        int num = NumberUtils.toInt(str, 0);
        System.out.println(num);  // 123
        
        // 安全转换
        String invalid = "abc";
        int safeNum = NumberUtils.toInt(invalid, 0);
        System.out.println(safeNum);  // 0
        
        // 求最大最小值
        int[] scores = {85, 92, 78, 90};
        System.out.println(NumberUtils.max(scores));  // 92
        System.out.println(NumberUtils.min(scores));  // 78
    }
}
```

---

## ✨ 最佳实践

### 1. 优先使用 `isBlank` 而不是 `isEmpty`

```java
// ✅ 推荐
if (StringUtils.isBlank(username)) {
    // 处理空值
}

// ❌ 不推荐（需要额外处理空格）
if (StringUtils.isEmpty(username) || username.trim().isEmpty()) {
    // 处理空值
}
```

### 2. 使用断言提前验证参数

```java
// ✅ 推荐
public void process(String data, int count) {
    AssertUtils.hasText(data, "数据不能为空");
    AssertUtils.isPositive(count, "数量必须大于0");
    // 业务逻辑
}
```

### 3. 使用默认值避免空指针

```java
// ✅ 推荐
String name = ObjectUtils.defaultIfNull(user.getName(), "未知");

// ❌ 不推荐
String name = user.getName() != null ? user.getName() : "未知";
```

---

## 📦 模块集成

### 与 est-collection 集成

```java
import ltd.idcu.est.utils.common.StringUtils;
import ltd.idcu.est.collection.impl.Seqs;

public class CollectionIntegration {
    public static void main(String[] args) {
        Seqs.of("apple", "banana", "cherry")
            .filter(StringUtils::isNotBlank)
            .map(StringUtils::capitalize)
            .forEach(System.out::println);
    }
}
```

---

## 📚 更多内容

- [EST 项目主页](https://github.com/idcu/est)
- [EST Core](../est-core/README.md)
- [EST Collection](../est-collection/README.md)

---

**祝你使用愉快！** 🎉
