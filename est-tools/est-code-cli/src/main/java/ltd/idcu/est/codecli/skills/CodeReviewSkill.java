package ltd.idcu.est.codecli.skills;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CodeReviewSkill implements EstSkill {
    
    private static final Pattern TRIGGER_PATTERN = Pattern.compile(
        "(?i)(code\\s*review|审查代码|code\\s*check|检查代码|review\\s*code)",
        Pattern.CASE_INSENSITIVE
    );
    
    @Override
    public String getName() {
        return "code_review";
    }
    
    @Override
    public String getDescription() {
        return "审查代码质量，提供改进建议";
    }
    
    @Override
    public String getPromptTemplate() {
        return "你是一位经验丰富的 EST 框架代码审查专家。请对以下代码进行全面审查，重点关注：\n" +
               "\n" +
               "1. **EST 框架规范符合性**\n" +
               "   - 是否正确使用 EST 注解和 API\n" +
               "   - 是否遵循 EST 最佳实践\n" +
               "   - 是否合理使用 EST 模块和功能\n" +
               "\n" +
               "2. **代码质量**\n" +
               "   - 代码可读性和可维护性\n" +
               "   - 命名规范\n" +
               "   - 异常处理\n" +
               "   - 资源管理\n" +
               "\n" +
               "3. **性能考虑**\n" +
               "   - 潜在的性能问题\n" +
               "   - 不必要的对象创建\n" +
               "   - 集合使用优化建议\n" +
               "\n" +
               "4. **安全性**\n" +
               "   - 输入验证\n" +
               "   - SQL 注入风险\n" +
               "   - XSS 防护\n" +
               "\n" +
               "5. **改进建议**\n" +
               "   - 具体的代码改进建议\n" +
               "   - 重构建议\n" +
               "   - 提供优化后的代码示例\n" +
               "\n" +
               "请以结构化的方式给出审查结果，使用 Markdown 格式。\n" +
               "\n" +
               "待审查的代码：\n" +
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
