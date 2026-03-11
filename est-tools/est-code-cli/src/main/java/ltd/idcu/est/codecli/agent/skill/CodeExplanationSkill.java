package ltd.idcu.est.codecli.agent.skill;

import ltd.idcu.est.agent.api.*;

import java.nio.file.Files;
import java.nio.file.Paths;

public class CodeExplanationSkill implements Skill {
    
    @Override
    public String getName() {
        return "code_explanation";
    }
    
    @Override
    public String getDescription() {
        return "解释代码的功能和结构";
    }
    
    @Override
    public void initialize(SkillContext context) {
    }
    
    @Override
    public SkillResult execute(SkillInput input, SkillContext context) {
        String filePath = input.getParameter("filePath", String.class, "");
        String code = input.getParameter("code", String.class, "");
        
        try {
            if (!filePath.isEmpty()) {
                code = Files.readString(Paths.get(filePath));
            }
            
            String explanation = explainCode(code);
            return SkillResult.success(explanation);
        } catch (Exception e) {
            return SkillResult.failure("Failed to explain code: " + e.getMessage());
        }
    }
    
    private String explainCode(String code) {
        StringBuilder explanation = new StringBuilder();
        
        explanation.append("=== 代码分析 ===\n\n");
        
        if (code.contains("@Entity")) {
            explanation.append("📦 这是一个 EST 实体类（Entity）\n");
            explanation.append("   - 使用 @Entity 注解标记\n");
            explanation.append("   - 通常对应数据库表\n\n");
        } else if (code.contains("@Controller")) {
            explanation.append("🎮 这是一个 EST 控制器类（Controller）\n");
            explanation.append("   - 使用 @Controller 注解标记\n");
            explanation.append("   - 处理 HTTP 请求\n\n");
        } else if (code.contains("@Service")) {
            explanation.append("⚙️ 这是一个 EST 服务类（Service）\n");
            explanation.append("   - 使用 @Service 注解标记\n");
            explanation.append("   - 包含业务逻辑\n\n");
        } else if (code.contains("@Mapper")) {
            explanation.append("🗄️ 这是一个 EST 数据访问接口（Mapper）\n");
            explanation.append("   - 使用 @Mapper 注解标记\n");
            explanation.append("   - 用于数据库操作\n\n");
        } else {
            explanation.append("📝 通用 Java 类\n\n");
        }
        
        int lineCount = code.split("\n").length;
        explanation.append("📊 代码统计:\n");
        explanation.append("   - 总行数: ").append(lineCount).append("\n");
        explanation.append("   - 包含 import 语句: ").append(code.contains("import ")).append("\n");
        explanation.append("   - 包含注释: ").append(code.contains("//") || code.contains("/*")).append("\n");
        
        return explanation.toString();
    }
    
    @Override
    public void cleanup(SkillContext context) {
    }
}
