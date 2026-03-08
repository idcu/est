package ltd.idcu.est.examples.ai;

import ltd.idcu.est.ai.api.*;
import ltd.idcu.est.ai.impl.DefaultAiAssistant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class LongTermFeaturesExample {
    
    private static final Logger logger = LoggerFactory.getLogger(LongTermFeaturesExample.class);
    
    public static void main(String[] args) {
        logger.info("=== EST AI 长期目标功能示例");
        logger.info("================================");
        
        AiAssistant aiAssistant = new DefaultAiAssistant();
        
        logger.info("\n--- 1. 需求解析器演示");
        demonstrateRequirementParser(aiAssistant);
        
        logger.info("\n--- 2. 架构设计器演�?);
        demonstrateArchitectureDesigner(aiAssistant);
        
        logger.info("\n--- 3. 测试和部署管理器演示");
        demonstrateTestAndDeployManager(aiAssistant);
        
        logger.info("\n--- 4. 完整流程演示");
        demonstrateCompleteWorkflow(aiAssistant);
        
        logger.info("\n=== 长期目标功能示例完成");
    }
    
    private static void demonstrateRequirementParser(AiAssistant aiAssistant) {
        logger.info("测试需求解析器...");
        
        RequirementParser parser = aiAssistant.getRequirementParser();
        
        String requirement = "我需要一个电商平台，包含用户管理、商品管理、订单管理和支付功能。需要支持用户注册登录、商品CRUD操作、订单创建和支付集成�?;
        
        logger.info("输入需�? {}", requirement);
        
        ParsedRequirement parsed = parser.parse(requirement);
        
        logger.info("解析结果:");
        logger.info("  项目名称: {}", parsed.getProjectName());
        logger.info("  项目类型: {}", parsed.getProjectType());
        logger.info("  原始需�? {}", parsed.getOriginalRequirement());
        logger.info("  组件数量: {}", parsed.getComponents().size());
        
        for (ParsedRequirement.RequirementComponent component : parsed.getComponents()) {
            logger.info("    - {}: {} ({})", component.getName(), component.getDescription(), component.getType());
        }
        
        logger.info("  功能列表: {}", parsed.getFeatures());
        logger.info("  技术需�? {}", parsed.getTechnicalRequirements());
        logger.info("  复杂度分�? {}", parsed.getComplexityScore());
        
        ParsedRequirement.EstimatedTimeline timeline = parsed.getEstimatedTimeline();
        if (timeline != null) {
            logger.info("  预估时间: 总计={}小时, 设计={}小时, 编码={}小时, 测试={}小时, 部署={}小时",
                timeline.getTotalHours(),
                timeline.getDesignHours(),
                timeline.getCodingHours(),
                timeline.getTestingHours(),
                timeline.getDeploymentHours());
        }
        
        List<String> componentNames = parser.extractComponents(requirement);
        logger.info("  提取的组件名�? {}", componentNames);
        
        Map<String, Object> metadata = parser.getRequirementsMetadata(requirement);
        logger.info("  需求元数据: {}", metadata);
    }
    
    private static void demonstrateArchitectureDesigner(AiAssistant aiAssistant) {
        logger.info("测试架构设计�?..");
        
        ArchitectureDesigner designer = aiAssistant.getArchitectureDesigner();
        
        String requirementText = "电商平台系统";
        ParsedRequirement requirement = aiAssistant.getRequirementParser().parse(requirementText);
        
        ArchitectureDesign design = designer.designArchitecture(requirement);
        
        logger.info("架构设计结果:");
        logger.info("  名称: {}", design.getDesignName());
        logger.info("  描述: {}", design.getDescription());
        logger.info("  模块数量: {}", design.getModules().size());
        
        for (ArchitectureDesign.ModuleDesign module : design.getModules()) {
            logger.info("    - {}: {}", module.getName(), module.getDescription());
            logger.info("      职责: {}", module.getResponsibility());
        }
        
        logger.info("  组件数量: {}", design.getComponents().size());
        for (ArchitectureDesign.ComponentDesign component : design.getComponents()) {
            logger.info("    - {}: {} ({})", component.getName(), component.getDescription(), component.getType());
        }
        
        logger.info("  架构模式数量: {}", design.getPatterns().size());
        for (ArchitecturePattern pattern : design.getPatterns()) {
            logger.info("    - {}: {} ({})", pattern.getName(), pattern.getDescription(), pattern.getCategory());
        }
        
        List<ArchitecturePattern> recommendedPatterns = designer.recommendPatterns(requirement);
        logger.info("  推荐的模�? {}", recommendedPatterns.size());
        
        Map<String, Object> validation = designer.validateArchitecture(design);
        logger.info("  架构验证: {}", validation);
        
        logger.info("  评分: 可扩展�?{}, 可维护�?{}, 性能={}",
            design.getScalabilityScore(),
            design.getMaintainabilityScore(),
            design.getPerformanceScore());
    }
    
    private static void demonstrateTestAndDeployManager(AiAssistant aiAssistant) {
        logger.info("测试测试和部署管理器...");
        
        TestAndDeployManager manager = aiAssistant.getTestAndDeployManager();
        
        String code = "public class UserService { ... }";
        String context = "Service";
        
        logger.info("生成测试套件...");
        TestSuite testSuite = manager.generateTests(code, context);
        
        logger.info("测试套件:");
        logger.info("  名称: {}", testSuite.getName());
        logger.info("  目标�? {}", testSuite.getTargetClass());
        logger.info("  测试用例数量: {}", testSuite.getTestCases().size());
        
        for (TestSuite.TestCase testCase : testSuite.getTestCases()) {
            logger.info("    - [{}] {} ({})", testCase.getType(), testCase.getName(), testCase.getDescription());
        }
        
        logger.info("运行测试...");
        boolean testResult = manager.runTests(testSuite);
        logger.info("  测试运行结果: {}", testResult);
        
        TestReport report = manager.getTestReport();
        if (report != null) {
            logger.info("  测试报告: 总数={}, 通过={}, 失败={}, 跳过={}, 通过�?{}%",
                report.getTotalTests(),
                report.getPassedTests(),
                report.getFailedTests(),
                report.getSkippedTests(),
                String.format("%.1f", report.getPassRate()));
        }
        
        logger.info("创建部署计划...");
        Map<String, Object> config = Map.of(
            "version", "1.0.0",
            "environment", "DEVELOPMENT"
        );
        DeploymentPlan plan = manager.createDeploymentPlan("电商平台", config);
        
        logger.info("部署计划:");
        logger.info("  项目名称: {}", plan.getProjectName());
        logger.info("  版本: {}", plan.getVersion());
        logger.info("  环境: {}", plan.getEnvironment());
        logger.info("  部署步骤数量: {}", plan.getSteps().size());
        
        for (DeploymentPlan.DeploymentStep step : plan.getSteps()) {
            logger.info("    - [{}] {}: {}", step.getOrder(), step.getName(), step.getDescription());
        }
        
        logger.info("  预估时间: {}分钟", plan.getEstimatedTimeMinutes());
        logger.info("  依赖�? {}", plan.getDependencies());
        
        logger.info("执行部署...");
        boolean deployResult = manager.deploy(plan);
        logger.info("  部署结果: {}", deployResult);
        
        DeploymentStatus status = manager.getDeploymentStatus();
        if (status != null) {
            logger.info("  部署状�? {}", status.getState());
            logger.info("  部署ID: {}", status.getDeploymentId());
        }
    }
    
    private static void demonstrateCompleteWorkflow(AiAssistant aiAssistant) {
        logger.info("完整流程演示: 从需求到部署");
        
        String projectRequirement = "开发一个在线学习平台，包含课程管理、用户学习、作业提交和成绩管理功能�?;
        
        logger.info("步骤1: 解析需�?);
        RequirementParser parser = aiAssistant.getRequirementParser();
        ParsedRequirement parsed = parser.parse(projectRequirement);
        logger.info("  解析完成: {}", parsed.getProjectName());
        
        logger.info("步骤2: 设计架构");
        ArchitectureDesigner designer = aiAssistant.getArchitectureDesigner();
        ArchitectureDesign design = designer.designArchitecture(parsed);
        logger.info("  架构设计完成: {}", design.getDesignName());
        
        logger.info("步骤3: 生成测试");
        TestAndDeployManager manager = aiAssistant.getTestAndDeployManager();
        TestSuite tests = manager.generateTests("code", "context");
        logger.info("  测试生成完成: {}个测试用�?, tests.getTestCases().size());
        
        logger.info("步骤4: 创建部署计划");
        Map<String, Object> config = Map.of("version", "1.0.0");
        DeploymentPlan plan = manager.createDeploymentPlan(parsed.getProjectName(), config);
        logger.info("  部署计划完成: {}个步�?, plan.getSteps().size());
        
        logger.info("完整流程演示结束�?);
    }
}
