package ltd.idcu.est.codecli.skills;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class PerformanceOptimizationSkill implements EstSkill {
    
    private static final Pattern TRIGGER_PATTERN = Pattern.compile(
        "(?i)(performance|性能|optimize|优化|speed|速度|memory|内存|latency|延迟|throughput|吞吐量)",
        Pattern.CASE_INSENSITIVE
    );
    
    @Override
    public String getName() {
        return "performance_optimization";
    }
    
    @Override
    public String getDescription() {
        return "分析和优化代码性能，提供性能改进建议";
    }
    
    @Override
    public String getPromptTemplate() {
        return "你是一位专业的 EST 框架性能优化专家。请对以下代码进行全面的性能分析和优化建议：\n" +
               "\n" +
               "1. **性能瓶颈分析**\n" +
               "   - 识别潜在的性能瓶颈\n" +
               "   - 分析时间复杂度问题\n" +
               "   - 分析空间复杂度问题\n" +
               "   - 识别不必要的计算\n" +
               "\n" +
               "2. **内存使用优化**\n" +
               "   - 不必要的对象创建\n" +
               "   - 集合使用优化\n" +
               "   - 字符串操作优化\n" +
               "   - 缓存建议\n" +
               "\n" +
               "3. **EST 框架特定优化**\n" +
               "   - 合理使用 EST 性能工具类\n" +
               "   - 数据库查询优化\n" +
               "   - 网络请求优化\n" +
               "   - 异步处理建议\n" +
               "\n" +
               "4. **具体优化建议**\n" +
               "   - 提供优化后的代码示例\n" +
               "   - 性能对比数据（预期提升）\n" +
               "   - 基准测试建议\n" +
               "   - 监控和测量建议\n" +
               "\n" +
               "5. **最佳实践**\n" +
               "   - 性能测试策略\n" +
               "   - 性能监控方法\n" +
               "   - 持续性能优化流程\n" +
               "\n" +
               "请以结构化的方式给出分析结果，使用 Markdown 格式。\n" +
               "\n" +
               "待分析的代码：\n" +
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
