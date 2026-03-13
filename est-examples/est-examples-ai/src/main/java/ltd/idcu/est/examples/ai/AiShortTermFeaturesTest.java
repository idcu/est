package ltd.idcu.est.examples.ai;

import ltd.idcu.est.ai.api.*;
import ltd.idcu.est.ai.impl.DefaultAiAssistant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class AiShortTermFeaturesTest {
    
    private static final Logger logger = LoggerFactory.getLogger(AiShortTermFeaturesTest.class);
    
    public static void main(String[] args) {
        logger.info("=== EST AI Short-Term Features Validation Test");
        logger.info("===================================");
        
        AiAssistant aiAssistant = new DefaultAiAssistant();
        
        logger.info("\n--- 1. Requirement Parser Test");
        testRequirementParser(aiAssistant);
        
        logger.info("\n--- 2. Architecture Designer Test");
        testArchitectureDesigner(aiAssistant);
        
        logger.info("\n--- 3. Test and Deploy Manager Test");
        testTestAndDeployManager(aiAssistant);
        
        logger.info("\n=== Short-Term Features Validation Test Complete");
    }
    
    private static void testRequirementParser(AiAssistant aiAssistant) {
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
        
        logger.info("[X] Requirement parser test complete");
    }
    
    private static void testArchitectureDesigner(AiAssistant aiAssistant) {
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
        
        logger.info("[X] Architecture designer test complete");
    }
    
    private static void testTestAndDeployManager(AiAssistant aiAssistant) {
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
        
        logger.info("[X] Test and deploy manager test complete");
    }
}
