package ltd.idcu.est.features.ai.impl;

import ltd.idcu.est.features.ai.api.ArchitectureDesign;
import ltd.idcu.est.features.ai.api.ArchitectureDesigner;
import ltd.idcu.est.features.ai.api.ArchitecturePattern;
import ltd.idcu.est.features.ai.api.ParsedRequirement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultArchitectureDesigner implements ArchitectureDesigner {
    
    @Override
    public ArchitectureDesign designArchitecture(ParsedRequirement requirement) {
        
        ArchitectureDesign design = new ArchitectureDesign();
        design.setDesignName(requirement.getProjectName() + "架构设计");
        design.setDescription("基于需求分析的系统架构设计");
        
        List<ArchitectureDesign.ModuleDesign> modules = createModules(requirement);
        design.setModules(modules);
        
        List<ArchitectureDesign.ComponentDesign> components = createComponents(modules);
        design.setComponents(components);
        
        List<ArchitecturePattern> patterns = recommendPatterns(requirement);
        design.setPatterns(patterns);
        
        Map<String, String> techStack = createTechnologyStack(requirement);
        design.setTechnologyStack(techStack);
        
        List<ArchitectureDesign.DesignDecision> decisions = createDecisions(requirement);
        design.setDecisions(decisions);
        
        List<String> tradeoffs = createTradeoffs();
        design.setTradeoffs(tradeoffs);
        
        design.setScalabilityScore(calculateScalabilityScore(requirement));
        design.setMaintainabilityScore(calculateMaintainabilityScore(requirement));
        design.setPerformanceScore(calculatePerformanceScore(requirement));
        
        return design;
    }
    
    @Override
    public List<ArchitecturePattern> recommendPatterns(ParsedRequirement requirement) {
        List<ArchitecturePattern> patterns = new ArrayList<>();
        
        patterns.add(ArchitecturePattern.create("layered", "分层架构")
            .description("将系统分为展示层、业务层、数据层等")
            .category("架构风格")
            .recommended(true)
            .useCase("大多数企业应用"));
        
        if (requirement.getComplexityScore() >= 3) {
            patterns.add(ArchitecturePattern.create("mvc", "MVC模式")
                .description("分离模型、视图、控制器")
                .category("设计模式")
                .recommended(true)
                .useCase("Web应用"));
        }
        
        if (requirement.getTechnicalRequirements().contains("RESTful API")) {
            patterns.add(ArchitecturePattern.create("rest-api", "REST API模式")
                .description("提供标准化的API接口")
                .category("API设计")
                .recommended(true)
                .useCase("微服务和前后端分离"));
        }
        
        return patterns;
    }
    
    @Override
    public ArchitectureDesign refineDesign(ArchitectureDesign currentDesign, String feedback) {
        currentDesign.setDescription(currentDesign.getDescription() + " (已优化: " + feedback + ")");
        return currentDesign;
    }
    
    @Override
    public Map<String, Object> validateArchitecture(ArchitectureDesign design) {
        Map<String, Object> validation = new HashMap<>();
        validation.put("isValid", true);
        validation.put("moduleCount", design.getModules().size());
        validation.put("componentCount", design.getComponents().size());
        validation.put("overallScore", (design.getScalabilityScore() + 
                                        design.getMaintainabilityScore() + 
                                        design.getPerformanceScore()) / 3);
        return validation;
    }
    
    private List<ArchitectureDesign.ModuleDesign> createModules(ParsedRequirement requirement) {
        List<ArchitectureDesign.ModuleDesign> modules = new ArrayList<>();
        
        modules.add(new ArchitectureDesign.ModuleDesign(
            "Presentation Layer",
            "展示层",
            "处理用户界面和API请求"
        ));
        
        modules.add(new ArchitectureDesign.ModuleDesign(
            "Business Layer",
            "业务层",
            "实现核心业务逻辑"
        ));
        
        modules.add(new ArchitectureDesign.ModuleDesign(
            "Data Layer",
            "数据层",
            "负责数据持久化和访问"
        ));
        
        modules.add(new ArchitectureDesign.ModuleDesign(
            "Common Layer",
            "公共层",
            "提供通用工具和配置"
        ));
        
        return modules;
    }
    
    private List<ArchitectureDesign.ComponentDesign> createComponents(List<ArchitectureDesign.ModuleDesign> modules) {
        List<ArchitectureDesign.ComponentDesign> components = new ArrayList<>();
        
        components.add(new ArchitectureDesign.ComponentDesign(
            "REST Controller",
            "Controller",
            "处理HTTP请求",
            "Presentation Layer"
        ));
        
        components.add(new ArchitectureDesign.ComponentDesign(
            "Service",
            "Service",
            "业务逻辑实现",
            "Business Layer"
        ));
        
        components.add(new ArchitectureDesign.ComponentDesign(
            "Repository",
            "Repository",
            "数据访问组件",
            "Data Layer"
        ));
        
        components.add(new ArchitectureDesign.ComponentDesign(
            "Entity",
            "Entity",
            "数据实体",
            "Data Layer"
        ));
        
        return components;
    }
    
    private Map<String, String> createTechnologyStack(ParsedRequirement requirement) {
        Map<String, String> techStack = new HashMap<>();
        techStack.put("Framework", "EST Framework");
        techStack.put("Language", "Java");
        techStack.put("Build Tool", "Maven");
        
        if (requirement.getTechnicalRequirements().contains("RESTful API")) {
            techStack.put("API", "RESTful");
        }
        if (requirement.getTechnicalRequirements().contains("数据库支持")) {
            techStack.put("Database", "JDBC/JPA");
        }
        
        return techStack;
    }
    
    private List<ArchitectureDesign.DesignDecision> createDecisions(ParsedRequirement requirement) {
        List<ArchitectureDesign.DesignDecision> decisions = new ArrayList<>();
        
        decisions.add(new ArchitectureDesign.DesignDecision(
            "采用分层架构",
            "清晰的职责分离，便于维护和扩展",
            1
        ));
        
        decisions.add(new ArchitectureDesign.DesignDecision(
            "使用EST框架",
            "利用EST框架的快速开发能力",
            2
        ));
        
        decisions.add(new ArchitectureDesign.DesignDecision(
            "Maven构建",
            "标准化的依赖管理和构建流程",
            3
        ));
        
        return decisions;
    }
    
    private List<String> createTradeoffs() {
        List<String> tradeoffs = new ArrayList<>();
        tradeoffs.add("分层架构增加了一定的复杂性，但提高了可维护性");
        tradeoffs.add("使用框架加速开发，但有一定的学习曲线");
        return tradeoffs;
    }
    
    private int calculateScalabilityScore(ParsedRequirement requirement) {
        return Math.min(10, 5 + requirement.getComplexityScore());
    }
    
    private int calculateMaintainabilityScore(ParsedRequirement requirement) {
        return 8;
    }
    
    private int calculatePerformanceScore(ParsedRequirement requirement) {
        return 7;
    }
}
