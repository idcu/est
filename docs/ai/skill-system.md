# Skill 系统

Skill 系统是 EST AI 中的可组合 AI 能力单元，用于封装特定的 AI 功能。

---

## 核心概念

| 概念 | 说明 |
|------|------|
| **Skill** | 封装特定功能的技能单元 |
| **SkillResult** | 技能执行结果 |
| **SkillRegistry** | 技能注册和管理中心 |

---

## Skill 接口

```java
package ltd.idcu.est.features.ai.api.skill;

import java.util.Map;

public interface Skill {
    String getName();
    String getDescription();
    String getCategory();
    SkillResult execute(Map<String, Object> inputs);
}
```

---

## 内置 Skill

EST AI 提供了以下内置 Skill：

| Skill 名称 | 功能描述 | 类别 |
|------------|----------|------|
| generate-entity | 生成实体类代码 | code-generation |
| generate-service | 生成服务类代码 | code-generation |
| generate-controller | 生成控制器代码 | code-generation |
| code-review | 代码质量审查 | code-analysis |

---

## 使用 Skill 系统

### 获取 SkillRegistry

```java
import ltd.idcu.est.features.ai.api.AiAssistant;
import ltd.idcu.est.features.ai.api.skill.SkillRegistry;
import ltd.idcu.est.features.ai.impl.DefaultAiAssistant;

AiAssistant aiAssistant = new DefaultAiAssistant();
SkillRegistry registry = aiAssistant.getSkillRegistry();
```

### 列出可用 Skill

```java
registry.getSkills().forEach(skill -> {
    System.out.println("- " + skill.getName() + ": " + skill.getDescription());
});
```

### 执行 Skill

```java
import ltd.idcu.est.features.ai.api.skill.SkillResult;
import java.util.Map;
import java.util.List;

SkillResult result = registry.execute("generate-entity",
    Map.of("className", "User",
           "packageName", "com.example.entity",
           "fields", List.of("id:Long", "name:String", "email:String")));

if (result.isSuccess()) {
    System.out.println("生成的代码:\n" + result.getOutputs().get("code"));
} else {
    System.out.println("错误: " + result.getErrorMessage());
}
```

---

## 代码审查 Skill 示例

```java
String code = """
    public class Example {
        public void doSomething() {
            System.out.println("Hello");
            try {
            } catch (Exception e) {}
        }
    }
    """;

SkillResult reviewResult = registry.execute("code-review",
    Map.of("code", code));

if (reviewResult.isSuccess()) {
    System.out.println("问题: " + reviewResult.getOutputs().get("issues"));
    System.out.println("建议: " + reviewResult.getOutputs().get("suggestions"));
    System.out.println("评分: " + reviewResult.getOutputs().get("score") + "/100");
}
```

---

## 创建自定义 Skill

```java
import ltd.idcu.est.features.ai.api.skill.Skill;
import ltd.idcu.est.features.ai.api.skill.SkillResult;
import java.util.Map;

public class CustomSkill implements Skill {
    @Override
    public String getName() {
        return "custom-skill";
    }

    @Override
    public String getDescription() {
        return "自定义技能描述";
    }

    @Override
    public String getCategory() {
        return "custom";
    }

    @Override
    public SkillResult execute(Map<String, Object> inputs) {
        SkillResult result = new SkillResult();
        result.setSuccess(true);
        result.getOutputs().put("message", "Hello from custom skill!");
        return result;
    }
}

// 注册自定义 Skill
registry.register(new CustomSkill());
```

---

## 最佳实践

1. **命名规范**: Skill 名称使用 kebab-case，如 `generate-dto`
2. **单一职责**: 每个 Skill 只负责一个特定功能
3. **输入验证**: 在 execute 方法中验证输入参数
4. **错误处理**: 提供清晰的错误信息
5. **文档完善**: 详细描述 Skill 的功能和输入输出

---

**文档版本**: 2.0  
**最后更新**: 2026-03-07  
**维护者**: EST 架构团队
