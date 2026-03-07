# EST AI 开发者专区
> EST - 为 AI 编程工具首选的 Java 开发框架
欢迎来到 EST AI 开发者专区！这里是你学习和使用 EST AI 功能的起点。
---

## 🌟 为什么选择 EST 进行 AI 开发？

EST 从设计之初就考虑了 AI 友好性：

| 特性 | 说明 |
|------|------|
| **零依赖** | 仅使用 Java 标准库，AI 更容易生成可运行的代码 |
| **递进式模块** | 清晰的模块层次结构，AI 能快速理解 |
| **简洁 API** | 直观的接口设计，AI 更容易学习和使用 |
| **完整示例** | 丰富的示例代码，AI 有大量参考 |
| **AI 原生模块** | 专门的 AI 功能模块，与 AI 编程工具深度集成 |

---

## 📚 文档导航

### 🚀 入门

- [为什么选择 EST 进行 AI 开发？](why-est.md) - 了解 EST 的 AI 友好特性
- [EST AI 发展规划](roadmap.md) - 了解 EST 的 AI 愿景
- [快速入门](quickstart.md) - 5 分钟开始使用 EST AI

### 📖 核心指南

- [AI 模块架构](architecture.md) - 了解 AI 模块的设计
- [AI 助手使用指南](ai-assistant.md) - 学习使用 AI 助手
- [代码生成器指南](code-generator.md) - 自动生成各种代码
- [提示词工程](prompt-engineering.md) - 编写有效的提示词

### 🔧 进阶

- [Skill 系统](skill-system.md) - 可组合的 AI 能力单元
- [MCP Server](mcp-server.md) - 与 Cursor、Windsurf 等 AI 工具集成
- [LLM 集成](llm-integration.md) - 与智谱 AI 等大语言模型集成
- [与 AI 编程工具集成](integration.md) - 与 Copilot、Cursor 等配合使用
- [最佳实践](best-practices.md) - AI 开发的最佳实践
- [常见问题](faq.md) - FAQ 解答

---

## 🎯 快速开始
### 三步开始使用 EST AI

#### 1. 添加依赖

```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-ai-api</artifactId>
    <version>2.0.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-ai-impl</artifactId>
    <version>2.0.0</version>
</dependency>
```

#### 2. 创建 AI 助手

```java
import ltd.idcu.est.features.ai.api.AiAssistant;
import ltd.idcu.est.features.ai.impl.DefaultAiAssistant;

public class FirstAiProgram {
    public static void main(String[] args) {
        AiAssistant assistant = new DefaultAiAssistant();
        
        String code = assistant.suggestCode("创建一个 UserService");
        System.out.println(code);
    }
}
```

#### 3. 运行示例

```bash
cd est-examples/est-examples-ai
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.AiQuickStartExample"
```

---

## 🗂️ 更多资源

- [EST 主 README](../../README.md) - 框架概览
- [AI 模块文档](../../est-modules/est-ai/README.md) - AI 模块详细介绍
- [AI 示例代码](../../est-examples/est-examples-ai/README.md) - AI 功能示例
- [架构文档](../architecture/README.md) - 深入了解 EST 架构
- [最佳实践课程](../best-practices/course/README.md) - 从入门到精通
---

## 🤝 参与贡献

我们欢迎社区参与 EST AI 的开发：

1. 试用 EST AI 模块，提供反馈
2. 提交 Issue 报告问题或提出想法
3. 提交 Pull Request 贡献代码
4. 分享你的使用经验
5. 宣传推广 EST

查看 [贡献指南](../contributing/README.md) 了解更多。
---

## 📄 许可证
MIT License

---

**文档版本**: 2.0  
**最后更新**: 2026-03-07  
**维护者**: EST 架构团队

> EST - 为成为 AI 编程工具首选的 Java 开发框架而努力！🚀
