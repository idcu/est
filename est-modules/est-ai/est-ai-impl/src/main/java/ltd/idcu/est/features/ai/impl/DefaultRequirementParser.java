package ltd.idcu.est.features.ai.impl;

import ltd.idcu.est.features.ai.api.ParsedRequirement;
import ltd.idcu.est.features.ai.api.RequirementParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultRequirementParser implements RequirementParser {
    
    @Override
    public ParsedRequirement parse(String naturalLanguageRequirement) {
        
        ParsedRequirement parsed = new ParsedRequirement();
        parsed.setOriginalRequirement(naturalLanguageRequirement);
        
        String projectName = extractProjectName(naturalLanguageRequirement);
        parsed.setProjectName(projectName);
        
        String projectType = determineProjectType(naturalLanguageRequirement);
        parsed.setProjectType(projectType);
        
        parsed.setDescription(naturalLanguageRequirement);
        
        List<ParsedRequirement.RequirementComponent> components = extractComponentsList(naturalLanguageRequirement);
        parsed.setComponents(components);
        
        List<String> features = extractFeatures(naturalLanguageRequirement);
        parsed.setFeatures(features);
        
        List<String> techRequirements = extractTechnicalRequirements(naturalLanguageRequirement);
        parsed.setTechnicalRequirements(techRequirements);
        
        int complexity = calculateComplexity(naturalLanguageRequirement);
        parsed.setComplexityScore(complexity);
        
        ParsedRequirement.EstimatedTimeline timeline = estimateTimeline(complexity);
        parsed.setEstimatedTimeline(timeline);
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("parser", "DefaultRequirementParser");
        metadata.put("timestamp", System.currentTimeMillis());
        parsed.setMetadata(metadata);
        
        return parsed;
    }
    
    @Override
    public List<String> extractComponents(String requirement) {
        List<String> components = new ArrayList<>();
        
        if (requirement.contains("用户") || requirement.contains("user")) {
            components.add("用户管理");
        }
        if (requirement.contains("商品") || requirement.contains("product")) {
            components.add("商品管理");
        }
        if (requirement.contains("订单") || requirement.contains("order")) {
            components.add("订单管理");
        }
        if (requirement.contains("支付") || requirement.contains("payment")) {
            components.add("支付功能");
        }
        if (requirement.contains("数据") || requirement.contains("data")) {
            components.add("数据管理");
        }
        if (requirement.contains("界面") || requirement.contains("UI") || requirement.contains("界面")) {
            components.add("用户界面");
        }
        
        if (components.isEmpty()) {
            components.add("核心功能");
        }
        
        return components;
    }
    
    @Override
    public Map<String, Object> getRequirementsMetadata(String requirement) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("length", requirement.length());
        metadata.put("wordCount", requirement.split("\\s+").length);
        metadata.put("hasChinese", requirement.matches(".*[\\u4e00-\\u9fa5].*"));
        metadata.put("hasEnglish", requirement.matches(".*[a-zA-Z].*"));
        return metadata;
    }
    
    private String extractProjectName(String requirement) {
        String[] words = requirement.split("[，。！？,.!?\\s]+");
        if (words.length > 0) {
            StringBuilder name = new StringBuilder();
            for (int i = 0; i < Math.min(3, words.length); i++) {
                if (!words[i].isEmpty()) {
                    if (name.length() > 0) name.append("");
                    name.append(words[i]);
                }
            }
            return name.toString() + "系统";
        }
        return "未命名项目";
    }
    
    private String determineProjectType(String requirement) {
        String lowerReq = requirement.toLowerCase();
        if (lowerReq.contains("电商") || lowerReq.contains("购物") || lowerReq.contains("ecommerce")) {
            return "电商系统";
        }
        if (lowerReq.contains("学习") || lowerReq.contains("教育") || lowerReq.contains("education")) {
            return "在线学习系统";
        }
        if (lowerReq.contains("管理") || lowerReq.contains("管理系统")) {
            return "管理系统";
        }
        if (lowerReq.contains("社交") || lowerReq.contains("social")) {
            return "社交系统";
        }
        return "通用系统";
    }
    
    private List<ParsedRequirement.RequirementComponent> extractComponentsList(String requirement) {
        List<ParsedRequirement.RequirementComponent> components = new ArrayList<>();
        
        components.add(new ParsedRequirement.RequirementComponent(
            "核心功能",
            "functional",
            "系统的主要功能模块",
            1
        ));
        
        if (requirement.contains("用户") || requirement.contains("user")) {
            components.add(new ParsedRequirement.RequirementComponent(
                "用户管理",
                "user",
                "用户注册、登录、信息管理",
                2
            ));
        }
        
        if (requirement.contains("数据") || requirement.contains("data")) {
            components.add(new ParsedRequirement.RequirementComponent(
                "数据管理",
                "data",
                "数据存储、查询、处理",
                3
            ));
        }
        
        return components;
    }
    
    private List<String> extractFeatures(String requirement) {
        List<String> features = new ArrayList<>();
        
        if (requirement.contains("CRUD") || requirement.contains("增删改查")) {
            features.add("基本CRUD操作");
        }
        if (requirement.contains("搜索") || requirement.contains("search")) {
            features.add("搜索功能");
        }
        if (requirement.contains("分页") || requirement.contains("pagination")) {
            features.add("分页查询");
        }
        if (requirement.contains("验证") || requirement.contains("validation")) {
            features.add("数据验证");
        }
        
        if (features.isEmpty()) {
            features.add("核心业务功能");
        }
        
        return features;
    }
    
    private List<String> extractTechnicalRequirements(String requirement) {
        List<String> techReqs = new ArrayList<>();
        
        if (requirement.contains("Web") || requirement.contains("web") || requirement.contains("网页")) {
            techReqs.add("Web应用");
        }
        if (requirement.contains("API") || requirement.contains("接口")) {
            techReqs.add("RESTful API");
        }
        if (requirement.contains("数据库") || requirement.contains("database")) {
            techReqs.add("数据库支持");
        }
        if (requirement.contains("安全") || requirement.contains("security")) {
            techReqs.add("安全认证");
        }
        
        return techReqs;
    }
    
    private int calculateComplexity(String requirement) {
        int length = requirement.length();
        if (length < 100) return 1;
        if (length < 300) return 2;
        if (length < 500) return 3;
        if (length < 1000) return 4;
        return 5;
    }
    
    private ParsedRequirement.EstimatedTimeline estimateTimeline(int complexity) {
        ParsedRequirement.EstimatedTimeline timeline = new ParsedRequirement.EstimatedTimeline();
        
        int baseHours = complexity * 16;
        timeline.setDesignHours(baseHours / 4);
        timeline.setCodingHours(baseHours / 2);
        timeline.setTestingHours(baseHours / 4);
        timeline.setDeploymentHours(4);
        timeline.setTotalHours(timeline.getDesignHours() + timeline.getCodingHours() + 
                               timeline.getTestingHours() + timeline.getDeploymentHours());
        
        return timeline;
    }
}
