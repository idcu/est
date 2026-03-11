package ltd.idcu.est.codecli.agent.skill;

import ltd.idcu.est.agent.api.*;

import java.nio.file.Files;
import java.nio.file.Paths;

public class BugFixSkill implements Skill {
    
    @Override
    public String getName() {
        return "bug_fix";
    }
    
    @Override
    public String getDescription() {
        return "分析和修复代码中的 Bug";
    }
    
    @Override
    public void initialize(SkillContext context) {
    }
    
    @Override
    public SkillResult execute(SkillInput input, SkillContext context) {
        String filePath = input.getParameter("filePath", String.class, "");
        String code = input.getParameter("code", String.class, "");
        String errorMessage = input.getParameter("errorMessage", String.class, "");
        
        try {
            if (!filePath.isEmpty()) {
                code = Files.readString(Paths.get(filePath));
            }
            
            String analysis = analyzeBug(code, errorMessage);
            return SkillResult.success(analysis);
        } catch (Exception e) {
            return SkillResult.failure("Failed to analyze bug: " + e.getMessage());
        }
    }
    
    private String analyzeBug(String code, String errorMessage) {
        StringBuilder analysis = new StringBuilder();
        
        analysis.append("=== Bug 分析报告 ===\n\n");
        
        if (!errorMessage.isEmpty()) {
            analysis.append("📋 错误信息:\n");
            analysis.append("   ").append(errorMessage).append("\n\n");
        }
        
        if (code.contains("NullPointerException") || errorMessage.contains("NullPointerException")) {
            analysis.append("🔍 可能问题: NullPointerException\n");
            analysis.append("💡 建议:\n");
            analysis.append("   - 添加 null 检查\n");
            analysis.append("   - 使用 Optional 类型\n");
            analysis.append("   - 初始化对象\n\n");
        }
        
        if (code.contains("IndexOutOfBounds") || errorMessage.contains("IndexOutOfBounds")) {
            analysis.append("🔍 可能问题: IndexOutOfBoundsException\n");
            analysis.append("💡 建议:\n");
            analysis.append("   - 检查数组/集合边界\n");
            analysis.append("   - 确保索引在有效范围内\n\n");
        }
        
        if (code.contains("ClassCast") || errorMessage.contains("ClassCast")) {
            analysis.append("🔍 可能问题: ClassCastException\n");
            analysis.append("💡 建议:\n");
            analysis.append("   - 使用 instanceof 检查类型\n");
            analysis.append("   - 考虑使用泛型\n\n");
        }
        
        if (code.contains("NumberFormat") || errorMessage.contains("NumberFormat")) {
            analysis.append("🔍 可能问题: NumberFormatException\n");
            analysis.append("💡 建议:\n");
            analysis.append("   - 验证输入格式\n");
            analysis.append("   - 使用 try-catch 处理转换\n\n");
        }
        
        if (code.contains("equals(") && !code.contains("!= null")) {
            analysis.append("⚠️ 潜在问题: equals 调用前未检查 null\n");
            analysis.append("💡 建议:\n");
            analysis.append("   - 使用 Objects.equals()\n");
            analysis.append("   - 或先进行 null 检查\n\n");
        }
        
        if (code.contains("SimpleDateFormat")) {
            analysis.append("⚠️ 潜在问题: SimpleDateFormat 不是线程安全的\n");
            analysis.append("💡 建议:\n");
            analysis.append("   - 使用 DateTimeFormatter (Java 8+)\n");
            analysis.append("   - 或使用 ThreadLocal 包装\n\n");
        }
        
        if (analysis.length() == "=== Bug 分析报告 ===\n\n".length()) {
            analysis.append("✅ 未发现明显的 Bug 模式\n");
            analysis.append("💡 建议提供更多上下文信息\n");
        }
        
        return analysis.toString();
    }
    
    @Override
    public void cleanup(SkillContext context) {
    }
}
