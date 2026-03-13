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
        logger.info("=== EST AI Long-Term Features Example");
        logger.info("================================");
        
        AiAssistant aiAssistant = new DefaultAiAssistant();
        
        logger.info("\n--- 1. Requirement Parser Demonstration");
        demonstrateRequirementParser(aiAssistant);
        
        logger.info("\n--- 2. Architecture Designer Demonstration");
        demonstrateArchitectureDesigner(aiAssistant);
        
        logger.info("\n--- 3. Test and Deploy Manager Demonstration");
        demonstrateTestAndDeployManager(aiAssistant);
        
        logger.info("\n--- 4. Complete Workflow Demonstration");
        demonstrateCompleteWorkflow(aiAssistant);
        
        logger.info("\n=== Long-Term Features Example Complete");
    }
    
    private static void demonstrateRequirementParser(AiAssistant aiAssistant) {
        logger.info("Testing requirement parser...");
        
        RequirementParser parser = aiAssistant.getRequirementParser();
        
        String requirement = "I need an e-commerce platform with user management, product management, order management, and payment functionality. Should support user registration/login, product CRUD operations, order creation, and payment integration.";
        
        logger.info("Input requirement: {}", requirement);
        
        ParsedRequirement parsed = parser.parse(requirement);
        
        logger.info("Parse result:");
        logger.info("  Project Name: {}", parsed.getProjectName());
        logger.info("  Project Type: {}", parsed.getProjectType());
        logger.info("  Original Requirement: {}", parsed.getOriginalRequirement());
        logger.info("  Component Count: {}", parsed.getComponents().size());
        
        for (ParsedRequirement.RequirementComponent component : parsed.getComponents()) {
            logger.info("    - {}: {} ({})", component.getName(), component.getDescription(), component.getType());
        }
        
        logger.info("  Feature List: {}", parsed.getFeatures());
        logger.info("  Technical Requirements: {}", parsed.getTechnicalRequirements());
        logger.info("  Complexity Score: {}", parsed.getComplexityScore());
        
        ParsedRequirement.EstimatedTimeline timeline = parsed.getEstimatedTimeline();
        if (timeline != null) {
            logger.info("  Estimated Time: Total={}h, Design={}h, Coding={}h, Testing={}h, Deployment={}h",
                timeline.getTotalHours(),
                timeline.getDesignHours(),
                timeline.getCodingHours(),
                timeline.getTestingHours(),
                timeline.getDeploymentHours());
        }
        
        List<String> componentNames = parser.extractComponents(requirement);
        logger.info("  Extracted component names: {}", componentNames);
        
        Map<String, Object> metadata = parser.getRequirementsMetadata(requirement);
        logger.info("  Requirement metadata: {}", metadata);
    }
    
    private static void demonstrateArchitectureDesigner(AiAssistant aiAssistant) {
        logger.info("Testing architecture designer...");
        
        ArchitectureDesigner designer = aiAssistant.getArchitectureDesigner();
        
        String requirementText = "E-commerce platform system";
        ParsedRequirement requirement = aiAssistant.getRequirementParser().parse(requirementText);
        
        ArchitectureDesign design = designer.designArchitecture(requirement);
        
        logger.info("Architecture design result:");
        logger.info("  Name: {}", design.getDesignName());
        logger.info("  Description: {}", design.getDescription());
        logger.info("  Module count: {}", design.getModules().size());
        
        for (ArchitectureDesign.ModuleDesign module : design.getModules()) {
            logger.info("    - {}: {}", module.getName(), module.getDescription());
            logger.info("      Responsibility: {}", module.getResponsibility());
        }
        
        logger.info("  Component count: {}", design.getComponents().size());
        for (ArchitectureDesign.ComponentDesign component : design.getComponents()) {
            logger.info("    - {}: {} ({})", component.getName(), component.getDescription(), component.getType());
        }
        
        logger.info("  Architecture pattern count: {}", design.getPatterns().size());
        for (ArchitecturePattern pattern : design.getPatterns()) {
            logger.info("    - {}: {} ({})", pattern.getName(), pattern.getDescription(), pattern.getCategory());
        }
        
        List<ArchitecturePattern> recommendedPatterns = designer.recommendPatterns(requirement);
        logger.info("  Recommended patterns: {}", recommendedPatterns.size());
        
        Map<String, Object> validation = designer.validateArchitecture(design);
        logger.info("  Architecture validation: {}", validation);
        
        logger.info("  Scores: Scalability={}, Maintainability={}, Performance={}",
            design.getScalabilityScore(),
            design.getMaintainabilityScore(),
            design.getPerformanceScore());
    }
    
    private static void demonstrateTestAndDeployManager(AiAssistant aiAssistant) {
        logger.info("Testing test and deploy manager...");
        
        TestAndDeployManager manager = aiAssistant.getTestAndDeployManager();
        
        String code = "public class UserService { ... }";
        String context = "Service";
        
        logger.info("Generating test suite...");
        TestSuite testSuite = manager.generateTests(code, context);
        
        logger.info("Test suite:");
        logger.info("  Name: {}", testSuite.getName());
        logger.info("  Target class: {}", testSuite.getTargetClass());
        logger.info("  Test case count: {}", testSuite.getTestCases().size());
        
        for (TestSuite.TestCase testCase : testSuite.getTestCases()) {
            logger.info("    - [{}] {} ({})", testCase.getType(), testCase.getName(), testCase.getDescription());
        }
        
        logger.info("Running tests...");
        boolean testResult = manager.runTests(testSuite);
        logger.info("  Test run result: {}", testResult);
        
        TestReport report = manager.getTestReport();
        if (report != null) {
            logger.info("  Test report: Total={}, Passed={}, Failed={}, Skipped={}, Pass Rate={}%",
                report.getTotalTests(),
                report.getPassedTests(),
                report.getFailedTests(),
                report.getSkippedTests(),
                String.format("%.1f", report.getPassRate()));
        }
        
        logger.info("Creating deployment plan...");
        Map<String, Object> config = Map.of(
            "version", "1.0.0",
            "environment", "DEVELOPMENT"
        );
        DeploymentPlan plan = manager.createDeploymentPlan("E-commerce Platform", config);
        
        logger.info("Deployment plan:");
        logger.info("  Project name: {}", plan.getProjectName());
        logger.info("  Version: {}", plan.getVersion());
        logger.info("  Environment: {}", plan.getEnvironment());
        logger.info("  Deployment step count: {}", plan.getSteps().size());
        
        for (DeploymentPlan.DeploymentStep step : plan.getSteps()) {
            logger.info("    - [{}] {}: {}", step.getOrder(), step.getName(), step.getDescription());
        }
        
        logger.info("  Estimated time: {} minutes", plan.getEstimatedTimeMinutes());
        logger.info("  Dependencies: {}", plan.getDependencies());
        
        logger.info("Executing deployment...");
        boolean deployResult = manager.deploy(plan);
        logger.info("  Deployment result: {}", deployResult);
        
        DeploymentStatus status = manager.getDeploymentStatus();
        if (status != null) {
            logger.info("  Deployment status: {}", status.getState());
            logger.info("  Deployment ID: {}", status.getDeploymentId());
        }
    }
    
    private static void demonstrateCompleteWorkflow(AiAssistant aiAssistant) {
        logger.info("Complete workflow demonstration: From requirement to deployment");
        
        String projectRequirement = "Develop an online learning platform with course management, user learning, assignment submission, and grade management features.";
        
        logger.info("Step 1: Parse requirement");
        RequirementParser parser = aiAssistant.getRequirementParser();
        ParsedRequirement parsed = parser.parse(projectRequirement);
        logger.info("  Parse complete: {}", parsed.getProjectName());
        
        logger.info("Step 2: Design architecture");
        ArchitectureDesigner designer = aiAssistant.getArchitectureDesigner();
        ArchitectureDesign design = designer.designArchitecture(parsed);
        logger.info("  Architecture design complete: {}", design.getDesignName());
        
        logger.info("Step 3: Generate tests");
        TestAndDeployManager manager = aiAssistant.getTestAndDeployManager();
        TestSuite tests = manager.generateTests("code", "context");
        logger.info("  Test generation complete: {} test cases", tests.getTestCases().size());
        
        logger.info("Step 4: Create deployment plan");
        Map<String, Object> config = Map.of("version", "1.0.0");
        DeploymentPlan plan = manager.createDeploymentPlan(parsed.getProjectName(), config);
        logger.info("  Deployment plan complete: {} steps", plan.getSteps().size());
        
        logger.info("Complete workflow demonstration finished");
    }
}
