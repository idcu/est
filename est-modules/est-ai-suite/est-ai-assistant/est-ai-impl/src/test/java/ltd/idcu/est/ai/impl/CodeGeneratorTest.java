package ltd.idcu.est.ai.impl;

import ltd.idcu.est.ai.api.CodeGenerator;
import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.util.List;
import java.util.Map;

public class CodeGeneratorTest {
    
    @Test
    public void testCreateCodeGenerator() {
        CodeGenerator generator = new DefaultCodeGenerator();
        assertNotNull(generator);
    }
    
    @Test
    public void testGenerateEntity() {
        CodeGenerator generator = new DefaultCodeGenerator();
        
        String code = generator.generateEntity("User", "com.example.entity", 
            Map.of("fields", List.of("id:Long", "name:String", "email:String")));
        
        assertNotNull(code);
        assertFalse(code.isEmpty());
        assertTrue(code.contains("class User"));
        assertTrue(code.contains("package com.example.entity"));
        assertTrue(code.contains("getId"));
        assertTrue(code.contains("getName"));
    }
    
    @Test
    public void testGenerateRepository() {
        CodeGenerator generator = new DefaultCodeGenerator();
        
        String code = generator.generateRepository("User", "com.example.repository", Map.of());
        
        assertNotNull(code);
        assertFalse(code.isEmpty());
        assertTrue(code.contains("UserRepository"));
        assertTrue(code.contains("package com.example.repository"));
    }
    
    @Test
    public void testGenerateService() {
        CodeGenerator generator = new DefaultCodeGenerator();
        
        String code = generator.generateService("User", "com.example.service", Map.of());
        
        assertNotNull(code);
        assertFalse(code.isEmpty());
        assertTrue(code.contains("UserService"));
        assertTrue(code.contains("package com.example.service"));
    }
    
    @Test
    public void testGenerateController() {
        CodeGenerator generator = new DefaultCodeGenerator();
        
        String code = generator.generateController("User", "com.example.controller", Map.of());
        
        assertNotNull(code);
        assertFalse(code.isEmpty());
        assertTrue(code.contains("UserController"));
        assertTrue(code.contains("package com.example.controller"));
    }
    
    @Test
    public void testGeneratePomXml() {
        CodeGenerator generator = new DefaultCodeGenerator();
        
        String code = generator.generatePomXml("MyProject", "com.example", "my-app", "1.0.0");
        
        assertNotNull(code);
        assertFalse(code.isEmpty());
        assertTrue(code.contains("MyProject"));
        assertTrue(code.contains("com.example"));
        assertTrue(code.contains("my-app"));
        assertTrue(code.contains("1.0.0"));
    }
    
    @Test
    public void testGenerateFromRequirement() {
        CodeGenerator generator = new DefaultCodeGenerator();
        
        String code = generator.generateFromRequirement("创建一个用户管理系�?);
        
        assertNotNull(code);
        assertFalse(code.isEmpty());
    }
    
    @Test
    public void testGetLlmClient() {
        CodeGenerator generator = new DefaultCodeGenerator();
        assertNotNull(generator.getLlmClient());
    }
}
