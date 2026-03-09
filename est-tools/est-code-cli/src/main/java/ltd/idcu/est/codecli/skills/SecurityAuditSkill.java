package ltd.idcu.est.codecli.skills;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SecurityAuditSkill implements EstSkill {
    
    private static final Pattern TRIGGER_PATTERN = Pattern.compile(
        "(?i)(security|安全|audit|审计|vulnerability|漏洞|hack|攻击|injection|注入|xss|csrf)",
        Pattern.CASE_INSENSITIVE
    );
    
    @Override
    public String getName() {
        return "security_audit";
    }
    
    @Override
    public String getDescription() {
        return "审计代码安全性，识别安全漏洞和风险";
    }
    
    @Override
    public String getPromptTemplate() {
        return "你是一位专业的 EST 框架安全审计专家。请对以下代码进行全面的安全审计：\n" +
               "\n" +
               "1. **输入验证**\n" +
               "   - 所有用户输入是否经过验证\n" +
               "   - 数据类型验证\n" +
               "   - 长度和格式验证\n" +
               "   - 边界值检查\n" +
               "\n" +
               "2. **注入攻击防护**\n" +
               "   - SQL 注入风险\n" +
               "   - NoSQL 注入风险\n" +
               "   - 命令注入风险\n" +
               "   - LDAP 注入风险\n" +
               "   - 是否使用参数化查询\n" +
               "\n" +
               "3. **XSS 防护**\n" +
               "   - 输出编码是否正确\n" +
               "   - 用户数据渲染是否安全\n" +
               "   - DOM 操作安全性\n" +
               "\n" +
               "4. **CSRF 防护**\n" +
               "   - 是否有 CSRF Token 机制\n" +
               "   - 状态改变操作的安全性\n" +
               "\n" +
               "5. **认证和授权**\n" +
               "   - 认证机制安全性\n" +
               "   - 权限检查是否完善\n" +
               "   - 会话管理安全性\n" +
               "   - 密码存储安全性\n" +
               "\n" +
               "6. **敏感数据保护**\n" +
               "   - 敏感数据是否加密\n" +
               "   - 日志中是否泄露敏感信息\n" +
               "   - 错误信息是否泄露系统详情\n" +
               "\n" +
               "7. **EST 框架安全最佳实践**\n" +
               "   - 是否正确使用 EST 安全工具\n" +
               "   - 配置文件安全性\n" +
               "   - 依赖库安全性\n" +
               "\n" +
               "8. **修复建议**\n" +
               "   - 具体的安全修复方案\n" +
               "   - 提供修复后的代码示例\n" +
               "   - 安全加固建议\n" +
               "   - 安全测试建议\n" +
               "\n" +
               "请以结构化的方式给出审计结果，使用 Markdown 格式。\n" +
               "\n" +
               "待审计的代码：\n" +
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
