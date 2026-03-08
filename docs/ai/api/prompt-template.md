# 提示词模板 API 参考

## PromptTemplateEngine 接口

```java
public interface PromptTemplateEngine {
    String render(String templateName, Map<String, Object> variables);
    String render(PromptTemplate template, Map<String, Object> variables);
    void registerTemplate(PromptTemplate template);
    PromptTemplate getTemplate(String templateName);
    List<String> listTemplates();
}
```

### 方法说明

#### render(String templateName, Map&lt;String, Object&gt; variables)
渲染指定名称的模板。

**参数：**
- `templateName` - 模板名称
- `variables` - 变量映射

**返回值：** 渲染后的提示词

**示例：**
```java
String prompt = engine.render("code-review", Map.of(
    "code", codeToReview,
    "language", "Java"
));
```

---

#### render(PromptTemplate template, Map&lt;String, Object&gt; variables)
渲染给定的模板对象。

**参数：**
- `template` - 模板对象
- `variables` - 变量映射

**返回值：** 渲染后的提示词

**示例：**
```java
PromptTemplate template = new PromptTemplate();
template.setName("custom");
template.setContent("你好，${name}！");

String prompt = engine.render(template, Map.of("name", "张三"));
```

---

#### registerTemplate(PromptTemplate template)
注册一个自定义模板。

**参数：**
- `template` - 模板对象

**示例：**
```java
PromptTemplate template = new PromptTemplate();
template.setName("my-template");
template.setContent("你是一个 ${role}，请处理以下内容：${content}");
template.setDescription("我的自定义模板");

engine.registerTemplate(template);
```

---

#### getTemplate(String templateName)
获取指定名称的模板。

**参数：**
- `templateName` - 模板名称

**返回值：** 模板对象，如果不存在返回 null

**示例：**
```java
PromptTemplate template = engine.getTemplate("code-review");
```

---

#### listTemplates()
列出所有可用的模板名称。

**返回值：** 模板名称列表

**示例：**
```java
List<String> templates = engine.listTemplates();
```

---

## PromptTemplate 类

提示词模板类。

### 构造函数

```java
public PromptTemplate()
public PromptTemplate(String name, String content)
```

### 属性

| 属性 | 类型 | 说明 |
|------|------|------|
| name | String | 模板名称 |
| content | String | 模板内容 |
| description | String | 模板描述 |
| category | String | 模板分类 |

### 示例

```java
PromptTemplate template = new PromptTemplate();
template.setName("code-review");
template.setContent("""
    你是一个代码审查专家。
    请审查以下代码，指出问题并提供改进建议。
    
    代码：
    ${code}
    
    语言：${language}
    """);
template.setDescription("代码审查模板");
template.setCategory("code");
```

---

## DefaultPromptTemplateEngine 实现

默认的提示词模板引擎实现类。

### 构造函数

```java
public DefaultPromptTemplateEngine()
```

### 内置模板

| 模板名称 | 分类 | 说明 |
|----------|------|------|
| code-review | code | 代码审查模板 |
| code-explain | code | 代码解释模板 |
| code-optimize | code | 代码优化模板 |
| code-generate | code | 代码生成模板 |
| test-generate | test | 测试代码生成模板 |
| doc-generate | doc | 文档生成模板 |
| bug-fix | debug | Bug 修复模板 |

### 示例

```java
PromptTemplateEngine engine = new DefaultPromptTemplateEngine();

// 使用内置模板
String prompt = engine.render("code-review", Map.of(
    "code", code,
    "language", "Java"
));

// 注册自定义模板
PromptTemplate custom = new PromptTemplate("my-template", "Hello, ${name}!");
engine.registerTemplate(custom);
```

---

## 模板语法

模板使用 `${variableName}` 语法来引用变量。

### 示例

```
你好，${userName}！

请处理以下内容：
${content}

时间：${timestamp}
```

渲染：
```java
Map<String, Object> variables = Map.of(
    "userName", "张三",
    "content", "这是要处理的内容",
    "timestamp", LocalDateTime.now().toString()
);
String result = engine.render(template, variables);
```

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
