package ltd.idcu.est.codecli.prompts;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PromptLibrary {
    
    private final Map<String, PromptTemplate> templates = new ConcurrentHashMap<>();
    
    public PromptLibrary() {
        registerDefaultTemplates();
    }
    
    private void registerDefaultTemplates() {
        
        registerTemplate(new PromptTemplate(
            "est_code_generator",
            "你是一位专业的 EST 框架代码生成专家。请根据以下需求生成符合 EST 框架规范的代码：\n\n" +
            "需求描述：{{requirement}}\n\n" +
            "要求：\n" +
            "1. 使用 EST 框架的标准注解和 API\n" +
            "2. 遵循 EST 项目结构规范\n" +
            "3. 包含必要的导入语句\n" +
            "4. 添加适当的注释\n" +
            "5. 代码应具有良好的可读性和可维护性\n\n" +
            "请生成完整的代码。",
            "EST 代码生成提示模板"
        ));
        
        registerTemplate(new PromptTemplate(
            "est_bug_fixer",
            "你是一位经验丰富的 EST 框架调试专家。请分析以下错误并提供修复方案：\n\n" +
            "错误信息：{{error_message}}\n\n" +
            "相关代码：\n{{code}}\n\n" +
            "请提供：\n" +
            "1. 错误原因分析\n" +
            "2. 具体的修复方案\n" +
            "3. 修复后的代码示例\n" +
            "4. 预防此类问题的建议",
            "EST Bug 修复提示模板"
        ));
        
        registerTemplate(new PromptTemplate(
            "est_test_generator",
            "你是一位专业的 EST 框架测试专家。请为以下代码编写单元测试：\n\n" +
            "待测试的代码：\n{{code}}\n\n" +
            "要求：\n" +
            "1. 使用 EST 测试框架（如果适用）\n" +
            "2. 覆盖主要功能路径\n" +
            "3. 包含边界条件测试\n" +
            "4. 测试异常情况\n" +
            "5. 提供清晰的测试说明\n\n" +
            "请生成完整的测试代码。",
            "EST 测试生成提示模板"
        ));
        
        registerTemplate(new PromptTemplate(
            "est_documentation",
            "你是一位专业的技术文档专家。请为以下 EST 代码编写文档：\n\n" +
            "代码内容：\n{{code}}\n\n" +
            "请提供：\n" +
            "1. 类/方法功能说明\n" +
            "2. 参数说明\n" +
            "3. 返回值说明\n" +
            "4. 使用示例\n" +
            "5. 注意事项\n\n" +
            "请使用 Markdown 格式。",
            "EST 文档生成提示模板"
        ));
        
        registerTemplate(new PromptTemplate(
            "est_performance_optimization",
            "你是一位 EST 框架性能优化专家。请分析以下代码的性能问题并提供优化建议：\n\n" +
            "待优化的代码：\n{{code}}\n\n" +
            "请从以下方面分析：\n" +
            "1. 算法复杂度\n" +
            "2. 内存使用\n" +
            "3. 数据库查询优化\n" +
            "4. 集合操作优化\n" +
            "5. 并发处理\n\n" +
            "提供优化后的代码示例和性能对比说明。",
            "EST 性能优化提示模板"
        ));
        
        registerTemplate(new PromptTemplate(
            "est_security_audit",
            "你是一位 EST 框架安全审计专家。请审计以下代码的安全性：\n\n" +
            "待审计的代码：\n{{code}}\n\n" +
            "检查要点：\n" +
            "1. 输入验证\n" +
            "2. SQL 注入防护\n" +
            "3. XSS 防护\n" +
            "4. 认证和授权\n" +
            "5. 敏感数据处理\n" +
            "6. 依赖安全\n\n" +
            "提供安全问题清单和修复建议。",
            "EST 安全审计提示模板"
        ));
    }
    
    public void registerTemplate(PromptTemplate template) {
        templates.put(template.getName(), template);
    }
    
    public void unregisterTemplate(String name) {
        templates.remove(name);
    }
    
    public PromptTemplate getTemplate(String name) {
        return templates.get(name);
    }
    
    public List<PromptTemplate> getAllTemplates() {
        return new ArrayList<>(templates.values());
    }
    
    public boolean hasTemplate(String name) {
        return templates.containsKey(name);
    }
    
    public String listTemplates() {
        StringBuilder sb = new StringBuilder();
        sb.append("Available Prompt Templates:\n\n");
        
        for (PromptTemplate template : templates.values()) {
            sb.append("  - ").append(template.getName()).append("\n");
            sb.append("    ").append(template.getDescription()).append("\n\n");
        }
        
        return sb.toString();
    }
}
