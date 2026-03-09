package ltd.idcu.est.ai.impl;

import ltd.idcu.est.ai.api.ParsedRequirement;
import ltd.idcu.est.ai.api.RequirementParser;

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
    
    @Override
    public List<String> extractKeywords(String requirement) {
        List<String> keywords = new ArrayList<>();
        
        String[] techKeywords = {"数据库", "database", "API", "接口", "Web", "网页", "微服务", 
                                  "microservice", "缓存", "cache", "安全", "security", "认证", 
                                  "authentication", "授权", "authorization", "消息", "message", 
                                  "队列", "queue", "搜索", "search", "报表", "report", "支付", 
                                  "payment", "订单", "order", "用户", "user", "商品", "product"};
        
        for (String keyword : techKeywords) {
            if (requirement.toLowerCase().contains(keyword.toLowerCase())) {
                keywords.add(keyword);
            }
        }
        
        if (keywords.isEmpty()) {
            keywords.add("系统");
            keywords.add("功能");
        }
        
        return keywords;
    }
    
    @Override
    public Map<String, Integer> analyzeSentiment(String requirement) {
        Map<String, Integer> sentiment = new HashMap<>();
        
        String[] positiveWords = {"好", "优秀", "快速", "高效", "稳定", "可靠", "简单", "易用", 
                                   "good", "excellent", "fast", "efficient", "stable", "reliable", 
                                   "simple", "easy", "powerful"};
        String[] negativeWords = {"复杂", "困难", "慢", "不稳定", "问题", "bug", "error", "复杂", 
                                   "complex", "difficult", "slow", "unstable", "problem", "bug"};
        
        int positiveCount = 0;
        int negativeCount = 0;
        
        for (String word : positiveWords) {
            if (requirement.toLowerCase().contains(word.toLowerCase())) {
                positiveCount++;
            }
        }
        
        for (String word : negativeWords) {
            if (requirement.toLowerCase().contains(word.toLowerCase())) {
                negativeCount++;
            }
        }
        
        sentiment.put("positive", positiveCount);
        sentiment.put("negative", negativeCount);
        sentiment.put("neutral", positiveCount == 0 && negativeCount == 0 ? 1 : 0);
        
        return sentiment;
    }
    
    @Override
    public List<String> extractEntities(String requirement) {
        List<String> entities = new ArrayList<>();
        
        String[] entityPatterns = {"用户管理", "商品管理", "订单管理", "支付系统", "库存管理", 
                                    "客户服务", "财务管理", "报表系统", "数据统计", "系统配置"};
        
        for (String entity : entityPatterns) {
            if (requirement.contains(entity)) {
                entities.add(entity);
            }
        }
        
        if (entities.isEmpty()) {
            entities.add("核心业务实体");
        }
        
        return entities;
    }
    
    @Override
    public Map<String, List<String>> classifyRequirements(String requirement) {
        Map<String, List<String>> classification = new HashMap<>();
        
        List<String> functional = new ArrayList<>();
        List<String> nonFunctional = new ArrayList<>();
        List<String> technical = new ArrayList<>();
        
        if (requirement.contains("用户") || requirement.contains("功能") || 
            requirement.contains("查询") || requirement.contains("添加") ||
            requirement.contains("修改") || requirement.contains("删除")) {
            functional.add("功能需求");
        }
        
        if (requirement.contains("性能") || requirement.contains("速度") ||
            requirement.contains("响应时间") || requirement.contains("并发")) {
            nonFunctional.add("性能需求");
        }
        
        if (requirement.contains("安全") || requirement.contains("认证") ||
            requirement.contains("权限")) {
            nonFunctional.add("安全需求");
        }
        
        if (requirement.contains("技术") || requirement.contains("架构") ||
            requirement.contains("数据库") || requirement.contains("API")) {
            technical.add("技术需求");
        }
        
        if (functional.isEmpty()) {
            functional.add("通用功能需求");
        }
        
        classification.put("functional", functional);
        classification.put("nonFunctional", nonFunctional);
        classification.put("technical", technical);
        
        return classification;
    }
    
    @Override
    public List<String> suggestPriorities(String requirement) {
        List<String> priorities = new ArrayList<>();
        
        int complexity = calculateComplexity(requirement);
        
        if (complexity >= 4) {
            priorities.add("高优先级 - 核心功能");
            priorities.add("中优先级 - 辅助功能");
            priorities.add("低优先级 - 增强功能");
        } else if (complexity >= 2) {
            priorities.add("高优先级 - 主要功能");
            priorities.add("中优先级 - 次要功能");
        } else {
            priorities.add("高优先级 - 全部功能");
        }
        
        return priorities;
    }
    
    private String extractProjectName(String requirement) {
        String[] words = requirement.split("[，。！�?.!?\\s]+");
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
        return "未命名项�?;
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
            "系统的主要功能模�?,
            1
        ));
        
        if (requirement.contains("用户") || requirement.contains("user")) {
            components.add(new ParsedRequirement.RequirementComponent(
                "用户管理",
                "user",
                "用户注册、登录、信息管�?,
                2
            ));
        }
        
        if (requirement.contains("数据") || requirement.contains("data")) {
            components.add(new ParsedRequirement.RequirementComponent(
                "数据管理",
                "data",
                "数据存储、查询、处�?,
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
        if (requirement.contains("数据�?) || requirement.contains("database")) {
            techReqs.add("数据库支�?);
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
