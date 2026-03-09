package ltd.idcu.est.codecli.skills;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ArchitectureSkill implements EstSkill {
    
    private static final Pattern TRIGGER_PATTERN = Pattern.compile(
        "(?i)(architecture|架构|设计架构|系统设计|架构设计)",
        Pattern.CASE_INSENSITIVE
    );
    
    @Override
    public String getName() {
        return "architecture";
    }
    
    @Override
    public String getDescription() {
        return "分析项目架构，提供架构设计建议";
    }
    
    @Override
    public String getPromptTemplate() {
        return "你是一位资深的 EST 框架架构师。请分析当前项目的架构，并提供专业建议：\n" +
               "\n" +
               "**分析维度：**\n" +
               "\n" +
               "1. **分层架构**\n" +
               "   - Controller 层设计\n" +
               "   - Service 层职责\n" +
               "   - Repository/DAO 层实现\n" +
               "   - 各层之间的依赖关系\n" +
               "\n" +
               "2. **模块化设计**\n" +
               "   - 模块划分是否合理\n" +
               "   - 模块间耦合度\n" +
               "   - 模块职责单一性\n" +
               "\n" +
               "3. **EST 框架应用**\n" +
               "   - EST 核心模块的使用\n" +
               "   - 配置管理\n" +
               "   - 依赖注入实践\n" +
               "   - AOP 应用\n" +
               "\n" +
               "4. **扩展性**\n" +
               "   - 未来功能扩展的便利性\n" +
               "   - 插件化能力\n" +
               "   - 接口设计的灵活性\n" +
               "\n" +
               "5. **架构建议**\n" +
               "   - 具体的架构改进建议\n" +
               "   - 技术选型建议\n" +
               "   - 最佳实践推荐\n" +
               "\n" +
               "请结合项目实际情况，给出实用的架构建议。\n" +
               "\n" +
               "项目信息：\n" +
               "{{project_info}}";
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
