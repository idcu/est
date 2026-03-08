package ltd.idcu.est.ai.impl.skill;

import ltd.idcu.est.ai.api.skill.SkillResult;
import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.util.List;
import java.util.Map;

public class SkillTest {

    @Test
    public void testGenerateEntitySkill() {
        GenerateEntitySkill skill = new GenerateEntitySkill();
        
        Map<String, Object> inputs = Map.of(
                "className", "Product",
                "packageName", "com.example",
                "fields", List.of("id:Long", "name:String", "price:Double")
        );
        
        assertTrue(skill.canExecute(inputs));
        
        SkillResult result = skill.execute(inputs);
        assertTrue(result.isSuccess());
        assertNotNull(result.getOutputs());
        assertTrue(result.getOutputs().containsKey("code"));
        
        String code = (String) result.getOutputs().get("code");
        assertTrue(code.contains("class Product"));
        assertTrue(code.contains("package com.example"));
        assertTrue(code.contains("getId"));
        assertTrue(code.contains("getName"));
    }

    @Test
    public void testGenerateServiceSkill() {
        GenerateServiceSkill skill = new GenerateServiceSkill();
        
        Map<String, Object> inputs = Map.of(
                "serviceName", "Product",
                "packageName", "com.example.service",
                "entityName", "Product"
        );
        
        assertTrue(skill.canExecute(inputs));
        
        SkillResult result = skill.execute(inputs);
        assertTrue(result.isSuccess());
        assertNotNull(result.getOutputs());
        
        String code = (String) result.getOutputs().get("code");
        assertTrue(code.contains("interface ProductService"));
        assertTrue(code.contains("class ProductServiceImpl"));
    }

    @Test
    public void testCodeReviewSkill() {
        CodeReviewSkill skill = new CodeReviewSkill();
        
        String badCode = """
            public class BadClass {
                public void badMethod() {
                    System.out.println("Bad code");
                    try {
                    } catch (Exception e) {}
                }
            }
            """;
        
        Map<String, Object> inputs = Map.of("code", badCode);
        assertTrue(skill.canExecute(inputs));
        
        SkillResult result = skill.execute(inputs);
        assertTrue(result.isSuccess());
        
        @SuppressWarnings("unchecked")
        List<String> issues = (List<String>) result.getOutputs().get("issues");
        assertNotNull(issues);
        assertFalse(issues.isEmpty());
        
        @SuppressWarnings("unchecked")
        Integer score = (Integer) result.getOutputs().get("score");
        assertNotNull(score);
        assertTrue(score < 100);
    }

    @Test
    public void testSkillRegistry() {
        DefaultSkillRegistry registry = new DefaultSkillRegistry();
        
        assertEquals(4, registry.listAllSkills().size());
        assertFalse(registry.listSkillsByCategory("code-generation").isEmpty());
        
        assertTrue(registry.getSkill("generate-entity").isPresent());
        
        SkillResult result = registry.execute("generate-entity", 
                Map.of("className", "TestEntity"));
        assertTrue(result.isSuccess());
    }
}
