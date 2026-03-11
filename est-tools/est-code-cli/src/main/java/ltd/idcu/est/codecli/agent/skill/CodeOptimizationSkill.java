package ltd.idcu.est.codecli.agent.skill;

import ltd.idcu.est.agent.api.*;

import java.nio.file.Files;
import java.nio.file.Paths;

public class CodeOptimizationSkill implements Skill {
    
    @Override
    public String getName() {
        return "code_optimization";
    }
    
    @Override
    public String getDescription() {
        return "提供代码优化建议";
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
            
            String suggestions = optimizeCode(code);
            return SkillResult.success(suggestions);
        } catch (Exception e) {
            return SkillResult.failure("Failed to optimize code: " + e.getMessage());
        }
    }
    
    private String optimizeCode(String code) {
        StringBuilder suggestions = new StringBuilder();
        
        suggestions.append("=== 代码优化建议 ===\n\n");
        
        if (code.contains("System.out.println")) {
            suggestions.append("💡 建议 1: 使用日志框架代替 System.out.println\n");
            suggestions.append("   - 考虑使用 est-logging 模块\n");
            suggestions.append("   - 支持不同日志级别（DEBUG, INFO, WARN, ERROR）\n\n");
        }
        
        if (code.contains("new ArrayList()")) {
            suggestions.append("💡 建议 2: 考虑指定初始容量\n");
            suggestions.append("   - new ArrayList<>(100) 比 new ArrayList() 更高效\n\n");
        }
        
        if (code.contains("for (int i = 0; i < list.size(); i++)")) {
            suggestions.append("💡 建议 3: 考虑使用增强型 for 循环\n");
            suggestions.append("   - for (Item item : list) 更简洁\n\n");
        }
        
        if (!code.contains("@Override") && code.contains("public void ")) {
            suggestions.append("💡 建议 4: 添加 @Override 注解\n");
            suggestions.append("   - 提高代码可读性和编译器检查\n\n");
        }
        
        if (code.contains("catch (Exception e) { }")) {
            suggestions.append("⚠️ 警告: 空的 catch 块\n");
            suggestions.append("   - 至少应该记录日志\n\n");
        }
        
        int suggestionCount = (suggestions.length() - "=== 代码优化建议 ===\n\n".length()) / 100;
        if (suggestionCount == 0) {
            suggestions.append("✅ 代码质量良好，未发现明显优化点\n");
        }
        
        return suggestions.toString();
    }
    
    @Override
    public void cleanup(SkillContext context) {
    }
}
