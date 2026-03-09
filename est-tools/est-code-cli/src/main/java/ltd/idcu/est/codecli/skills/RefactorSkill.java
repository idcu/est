package ltd.idcu.est.codecli.skills;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RefactorSkill implements EstSkill {
    
    private static final Pattern TRIGGER_PATTERN = Pattern.compile(
        "(?i)(refactor|重构|优化代码|improve\\s*code|代码优化)",
        Pattern.CASE_INSENSITIVE
    );
    
    @Override
    public String getName() {
        return "refactor";
    }
    
    @Override
    public String getDescription() {
        return "提供代码重构和优化建议";
    }
    
    @Override
    public String getPromptTemplate() {
        return "你是一位专业的 EST 框架重构专家。请分析以下代码，提供重构建议：\n" +
               "\n" +
               "重构目标：\n" +
               "1. 提高代码可读性和可维护性\n" +
               "2. 遵循 EST 框架最佳实践\n" +
               "3. 改善性能\n" +
               "4. 增强代码复用性\n" +
               "\n" +
               "请从以下方面进行分析：\n" +
               "\n" +
               "**1. 设计模式应用**\n" +
               "- 是否可以应用合适的设计模式\n" +
               "- 单例、工厂、策略、观察者等模式的适用场景\n" +
               "\n" +
               "**2. 代码结构优化**\n" +
               "- 方法拆分建议\n" +
               "- 类职责划分\n" +
               "- 包结构优化\n" +
               "\n" +
               "**3. EST 特定优化**\n" +
               "- EST 注解的正确使用\n" +
               "- 依赖注入优化\n" +
               "- AOP 应用建议\n" +
               "\n" +
               "**4. 性能优化**\n" +
               "- 集合操作优化\n" +
               "- 避免不必要的计算\n" +
               "- 懒加载建议\n" +
               "\n" +
               "**5. 代码示例**\n" +
               "- 提供重构后的代码示例\n" +
               "- 对比说明改进点\n" +
               "\n" +
               "请使用 Markdown 格式，结构清晰地给出重构建议。\n" +
               "\n" +
               "待重构的代码：\n" +
               "{{code}}";
    }
    
    @Override
    public boolean canHandle(String userInput) {
        return TRIGGER_PATTERN.matcher(userInput).find();
    }
    
    @Override
    public Map<String, Object> extractParameters(String userInput) {
        Map<String, Object> params = new HashMap<>();
        params.put("skill", getName());
        return params;
    }
}
