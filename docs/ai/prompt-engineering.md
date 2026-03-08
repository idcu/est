# EST 提示词工程指南

## 什么是提示词工程？

提示词工程是设计和优化提示词，以从 AI 模型获得最佳输出的过程。良好的提示词可以显著提高 AI 的输出质量、一致性和可靠性。

EST 框架内置了强大的提示词模板系统，帮助你轻松应用提示词工程的最佳实践。

---

## 提示词设计原则

### 1. 清晰明确

**不好的示例**：
```
写点代码
```

**好的示例**：
```
请写一个 Java 方法，接受一个字符串列表，返回长度最长的字符串。
要求：
- 使用 Java 8 Stream API
- 处理空列表的情况
- 添加 Javadoc 注释
```

### 2. 提供上下文

**不好的示例**：
```
优化这段代码
```

**好的示例**：
```
这是一个电商系统的订单查询方法，请优化它的性能：

代码：
${code}

上下文信息：
- 数据库是 MySQL 8.0
- 订单表有 100 万+ 数据
- 需要支持按时间范围查询
```

### 3. 指定输出格式

**不好的示例**：
```
分析这个问题
```

**好的示例**：
```
请分析以下问题，并按以下格式返回：

【问题分析】
...

【解决方案】
...

【代码示例】
...

【注意事项】
...
```

### 4. 设定角色

**不好的示例**：
```
帮我写代码
```

**好的示例**：
```
你是一位有 10 年经验的 Java 后端架构师，精通 Spring Boot、EST 框架和微服务架构。
请以专家的身份帮我设计并实现以下功能...
```

---

## EST 提示词模板系统

### 使用内置模板

EST 提供了多种专业级的提示词模板：

```java
PromptTemplateEngine engine = new DefaultPromptTemplateEngine();

// 代码审查
String reviewPrompt = engine.render("code-review", Map.of(
    "code", yourCode,
    "language", "Java",
    "focus", "performance"
));

// 代码生成
String generatePrompt = engine.render("code-generate", Map.of(
    "requirement", "用户管理模块",
    "framework", "EST 2.0",
    "database", "MySQL"
));

// 测试生成
String testPrompt = engine.render("test-generate", Map.of(
    "code", yourCode,
    "framework", "JUnit 5",
    "mock": "Mockito"
));
```

### 内置模板列表

| 模板名称 | 分类 | 说明 |
|---------|------|------|
| code-review | code | 代码审查模板 |
| code-explain | code | 代码解释模板 |
| code-optimize | code | 代码优化模板 |
| code-generate | code | 代码生成模板 |
| test-generate | test | 测试代码生成模板 |
| doc-generate | doc | 文档生成模板 |
| bug-fix | debug | Bug 修复模板 |
| refactor | refactor | 重构模板 |
| architecture | design | 架构设计模板 |

### 创建自定义模板

```java
PromptTemplate template = new PromptTemplate();
template.setName("rest-api-design");
template.setCategory("design");
template.setDescription("REST API 设计模板");
template.setContent("""
    你是一位 REST API 设计专家。
    
    请为以下需求设计 REST API：
    
    需求描述：${requirement}
    
    请提供：
    1. API 端点列表（HTTP 方法 + URL）
    2. 请求和响应格式
    3. 错误处理设计
    4. 安全性考虑
    
    技术约束：
    - 框架：EST Framework 2.0
    - 认证：JWT
    - 文档：Swagger/OpenAPI 3.0
    """);

engine.registerTemplate(template);

// 使用自定义模板
String prompt = engine.render("rest-api-design", Map.of(
    "requirement", "订单管理 API"
));
```

---

## 高级提示词技巧

### 1. 思维链 (Chain of Thought)

引导 AI 逐步思考：

```java
String prompt = """
    请解决以下问题，但在给出最终答案之前，请先解释你的思考过程。
    
    问题：${problem}
    
    请按以下格式回答：
    【思考过程】
    1. 首先，我需要理解...
    2. 然后，我应该考虑...
    3. 最后，我可以得出...
    
    【最终答案】
    ...
    """;
```

### 2. 少样本学习 (Few-Shot Learning)

提供几个示例帮助 AI 理解期望：

```java
String prompt = """
    请将以下自然语言转换为 SQL 查询。
    
    示例 1：
    输入：查找所有价格大于 100 的产品
    输出：SELECT * FROM product WHERE price > 100
    
    示例 2：
    输入：统计每个类别的产品数量
    输出：SELECT category, COUNT(*) FROM product GROUP BY category
    
    现在请处理：
    输入：${input}
    输出：
    """;
```

### 3. 自我一致性 (Self-Consistency)

让 AI 生成多个答案，然后选择最佳的：

```java
String prompt = """
    请为以下问题提供 3 个不同的解决方案，然后选择最佳的一个并说明理由。
    
    问题：${problem}
    
    方案 1：
    ...
    
    方案 2：
    ...
    
    方案 3：
    ...
    
    最佳方案：方案 X
    理由：...
    """;
```

### 4. 提示词分解

将复杂任务分解为多个步骤：

```java
// 第一步：理解需求
String step1 = aiAssistant.chat("请分析以下需求，列出核心功能点：" + requirement);

// 第二步：设计架构
String step2 = aiAssistant.chat("基于以下功能点，设计系统架构：" + step1);

// 第三步：生成代码
String step3 = aiAssistant.chat("基于以下架构设计，生成代码：" + step2);
```

---

## 代码生成提示词最佳实践

### 1. 详细的需求描述

```java
String prompt = """
    请用 EST 框架实现一个用户认证模块。
    
    详细需求：
    1. 用户注册（用户名、邮箱、密码）
    2. 用户登录（邮箱/密码 + JWT Token）
    3. 密码重置
    4. 邮箱验证
    
    技术要求：
    - 使用 Spring Security
    - 密码使用 BCrypt 加密
    - JWT Token 有效期 2 小时
    - 使用 EST 的数据访问模块
    
    请生成：
    - Entity 类
    - Repository 接口
    - Service 类
    - Controller 类
    - DTO 类
    - 配置类
    """;
```

### 2. 指定代码风格

```java
String prompt = """
    请生成符合以下代码风格的 Java 代码：
    
    代码风格规范：
    - 使用 Lombok 注解
    - 遵循 Google Java Style
    - 类名使用大驼峰
    - 方法名使用小驼峰
    - 常量使用全大写下划线分隔
    - 添加 Javadoc 注释
    - 使用 @Override 注解
    - 异常处理要完善
    
    需求：${requirement}
    """;
```

### 3. 包含测试要求

```java
String prompt = """
    请实现以下功能，并包含完整的单元测试：
    
    功能需求：${requirement}
    
    测试要求：
    - 使用 JUnit 5
    - 使用 Mockito 进行 Mock
    - 测试覆盖率 > 80%
    - 包含正常流程测试
    - 包含边界条件测试
    - 包含异常情况测试
    """;
```

---

## 调试和改进提示词

### 1. 迭代式改进

```java
// 第一轮
String v1 = aiAssistant.chat(initialPrompt);

// 获得反馈并改进
String feedback = """
    这是第一次的结果：
    ${v1}
    
    请根据以下反馈改进：
    1. 缺少异常处理
    2. 需要添加日志
    3. 性能可以进一步优化
    """;

String v2 = aiAssistant.chat(feedback);
```

### 2. A/B 测试

同时测试多个提示词版本，选择效果最好的：

```java
String promptA = "版本 A 的提示词...";
String promptB = "版本 B 的提示词...";

String resultA = aiAssistant.chat(promptA);
String resultB = aiAssistant.chat(promptB);

// 对比并选择最佳结果
```

### 3. 温度参数调整

根据任务类型调整温度参数：

```java
// 创造性任务（较高温度）
LlmOptions creativeOptions = new LlmOptions();
creativeOptions.setTemperature(0.8);
String creativeResult = client.complete(creativePrompt, creativeOptions);

// 精确性任务（较低温度）
LlmOptions preciseOptions = new LlmOptions();
preciseOptions.setTemperature(0.2);
String preciseResult = client.complete(precisePrompt, preciseOptions);
```

---

## 总结

提示词工程是一门艺术也是一门科学。通过：
1. 使用 EST 的提示词模板系统
2. 遵循提示词设计原则
3. 应用高级提示词技巧
4. 持续迭代和改进

你可以充分发挥 AI 的潜力，显著提升开发效率和代码质量。

记住：好的提示词 = 清晰的指令 + 充分的上下文 + 明确的格式！

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
