# EST 框架代码风格规范
本文档定义了 EST 框架项目的代码风格规范，确保代码的一致性、可读性和可维护性。

## 目录

- [1. 基本原则](#1-基本原则)
- [2. 命名规范](#2-命名规范)
- [3. 代码格式](#3-代码格式)
- [4. 注释规范](#4-注释规范)
- [5. 导入规范](#5-导入规范)
- [6. 异常处理](#6-异常处理)
- [7. 最佳实践](#7-最佳实践)
- [8. 工具配置](#8-工具配置)

---

## 1. 基本原则

### 1.1 代码可读性
- **代码应该像散文一样易于阅读**
- 优先考虑可读性，其次是性能（除非性能确实是瓶颈）
- 使用有意义的变量名和函数名

### 1.2 一致性
- 在整个项目中保持一致的代码风格
- 如果修改现有代码，保持与周围代码风格一致
- 使用自动化工具（Checkstyle、PMD、SpotBugs）强制执行规范

### 1.3 简洁性
- 避免不必要的复杂性
- 不要过度设计
- 删除未使用的代码和导入

---

## 2. 命名规范

### 2.1 包名
- 全部小写
- 使用反向域名格式：`ltd.idcu.est.[模块名].[功能]`
- 使用单个单词，避免缩写（除非是广为人知的缩写）

**示例：**
```java
ltd.idcu.est.core.container
ltd.idcu.est.cache.api
ltd.idcu.est.web.impl
```

### 2.2 类名和接口名
- 使用 PascalCase（大驼峰命名法）
- 使用名词或名词短语
- 接口名通常以接口描述命名，不强制使用 `I` 前缀
- 实现类通常以 `Impl` 后缀结尾

**示例：**
```java
// 接口
public interface Container
public interface Cache
public interface UserService

// 实现类
public class DefaultContainer implements Container
public class MemoryCache implements Cache
public class UserServiceImpl implements UserService
```

### 2.3 方法名
- 使用 camelCase（小驼峰命名法）
- 使用动词或动词短语
- 布尔值方法通常以 `is`、`has`、`can`、`should` 开头

**示例：**
```java
public void start()
public User getUserById(String id)
public boolean isActive()
```

### 2.4 变量名
- 使用 camelCase（小驼峰命名法）
- 使用有意义的名字，避免单字母变量名（循环变量除外）
- 常量使用 UPPER_SNAKE_CASE（全大写下划线分隔）

**示例：**
```java
// 普通变量
private String userName
private int maxRetryCount

// 常量
public static final String DEFAULT_ENCODING = "UTF-8"
```

### 2.5 泛型类型参数
- 使用单个大写字母，通常为：
  - `T` - Type（类型）
  - `E` - Element（元素）
  - `K` - Key（键）
  - `V` - Value（值）
  - `N` - Number（数字）
  - `S`, `U`, `V` - 第二、第三、第四个类型

**示例：**
```java
public interface List<E>
public interface Map<K, V>
public class Container<T>
```

---

## 3. 代码格式

### 3.1 缩进和空格
- 使用 4 个空格缩进（不使用 Tab）
- 文件使用 UTF-8 编码
- 行尾使用 LF（Linux 风格）
- 每行不超过 120 个字符
- 文件末尾添加空行

### 3.2 大括号
- 使用 K&R 风格（左大括号不换行）
- 即使只有一条语句也要使用大括号

**示例：**
```java
// ✅ 正确
if (condition) {
    doSomething();
}

// ❌ 错误 - 缺少大括号
if (condition)
    doSomething();
```

### 3.3 空行
- 方法之间使用 1-2 个空行
- 逻辑相关的代码块之间使用空行分隔
- 避免连续多个空行（不超过 2 个）

### 3.4 空格
- 运算符两侧添加空格
- 逗号后添加空格
- 冒号前后添加空格（for-each 循环、switch 语句）

---

## 4. 注释规范

### 4.1 Javadoc 注释
- 所有公共 API 必须有 Javadoc 注释
- Javadoc 包含 `@param`、`@return`、`@throws` 标签
- 使用中文编写（面向中国开发者）

**示例：**
```java
/**
 * 根据用户 ID 获取用户信息。
 *
 * @param id 用户 ID，不能为 null 或空字符串
 * @return 用户对象，如果不存在返回 null
 * @throws IllegalArgumentException 如果 id 为 null 或空字符串
 * @throws UserNotFoundException 如果用户不存在
 */
public User getUserById(String id) {
    // 实现
}
```

### 4.2 行内注释
- 使用 `//` 注释
- 解释为什么这样做，而不是做了什么
- 避免显而易见的注释

### 4.3 TODO 和 FIXME
- 使用 `TODO` 标记待完成的功能
- 使用 `FIXME` 标记需要修复的问题
- 包含作者和日期（可选）

---

## 5. 导入规范

### 5.1 导入顺序
- 分组导入，按以下顺序：
  1. Java 标准库
  2. 第三方库
  3. EST 框架内部包
  4. 当前项目包
- 组之间使用空行分隔
- 按字母顺序排序
- 避免使用通配符导入（`*`）

### 5.2 静态导入
- 静态导入单独分组
- 只在频繁使用时使用静态导入

---

## 6. 异常处理

### 6.1 异常类型
- 使用检查型异常（Checked Exception）表示可恢复的错误
- 使用非检查型异常（Unchecked Exception）表示编程错误
- 不要捕获 `Exception` 或 `Throwable`（除非在顶层）

### 6.2 异常信息
- 异常消息应该清晰、具体
- 包含相关的上下文信息
- 总是记录异常堆栈信息

### 6.3 资源管理
- 使用 try-with-resources 管理资源
- 确保资源总是被关闭

---

## 7. 最佳实践

### 7.1 不可变性
- 优先使用不可变对象
- 将字段声明为 `final`（除非必须可变）
- 使用防御性复制

### 7.2 空值处理
- 使用 `Optional` 表示可能为 null 的返回值
- 使用 `Objects.requireNonNull()` 或 `AssertUtils.notNull()` 验证参数
- 避免返回 null

### 7.3 equals 和 hashCode
- 总是同时重写 `equals()` 和 `hashCode()`
- 使用 `Objects.equals()` 和 `Objects.hash()`
- 考虑使用 IDE 自动生成

### 7.4 字符串处理
- 使用 `EST` 框架的 `StringUtils` 而不是手动处理
- 使用 `StringBuilder` 进行大量字符串拼接
- 优先使用 `equals()` 而不是 `==` 比较字符串

---

## 8. 工具配置

### 8.1 EditorConfig
项目根目录的 `.editorconfig` 文件定义了基本的编辑器配置：

```ini
root = true

[*]
charset = utf-8
end_of_line = lf
indent_size = 4
indent_style = space
insert_final_newline = true
trim_trailing_whitespace = true

[*.md]
max_line_length = off
trim_trailing_whitespace = false

[*.xml]
indent_size = 2

[*.yml]
indent_size = 2

[*.yaml]
indent_size = 2

[*.json]
indent_size = 2

[*.bat]
end_of_line = crlf

[*.ps1]
end_of_line = crlf
```

### 8.2 Checkstyle
Checkstyle 配置位于 `.config/checkstyle.xml`，包含以下检查：
- 命名规范
- 代码格式
- 空块检查
- 导入规范
- 修饰符顺序
- 方法长度限制
- 参数数量限制

运行 Checkstyle：
```bash
mvn checkstyle:check
```

### 8.3 PMD
PMD 配置位于 `.config/pmd.xml`，包含以下规则集：
- 最佳实践
- 代码风格
- 设计原则
- 错误检测
- 性能优化

运行 PMD：
```bash
mvn pmd:check
```

### 8.4 SpotBugs
SpotBugs 配置位于 `.config/spotbugs-exclude.xml`，用于静态 bug 检测。

运行 SpotBugs：
```bash
mvn spotbugs:check
```

### 8.5 IDE 配置
- IntelliJ IDEA：导入 `.editorconfig` 并启用 Checkstyle/PMD 插件
- Eclipse：安装 Checkstyle/PMD 插件并导入配置
- VS Code：安装 EditorConfig 和 Java 扩展

---

## 总结

遵循本代码风格规范可以：
- 📝 提高代码可读性和可维护性
- 🤝 促进团队协作
- 🐛 减少常见错误
- ✅ 确保代码一致性

记住：规范是指导原则，不是铁律。在特殊情况下，可以适当调整，但必须有充分的理由。

---

**文档版本**: 2.0
**最后更新**: 2026-03-06
**维护者**: EST 架构团队
