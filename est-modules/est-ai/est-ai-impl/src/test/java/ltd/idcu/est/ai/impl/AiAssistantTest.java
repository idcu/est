package ltd.idcu.est.ai.impl;

import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.util.List;

public class AiAssistantTest {
    
    @Test
    public void testCreateAiAssistant() {
        AiAssistant assistant = new DefaultAiAssistant();
        assertNotNull(assistant);
    }
    
    @Test
    public void testGetQuickReference() {
        AiAssistant assistant = new DefaultAiAssistant();
        
        String ref = assistant.getQuickReference("web");
        assertNotNull(ref);
        assertFalse(ref.isEmpty());
        assertTrue(ref.contains("web") || ref.contains("Web"));
    }
    
    @Test
    public void testGetBestPractice() {
        AiAssistant assistant = new DefaultAiAssistant();
        
        String bestPractice = assistant.getBestPractice("error-handling");
        assertNotNull(bestPractice);
        assertFalse(bestPractice.isEmpty());
    }
    
    @Test
    public void testGetTutorial() {
        AiAssistant assistant = new DefaultAiAssistant();
        
        String tutorial = assistant.getTutorial("first-app");
        assertNotNull(tutorial);
        assertFalse(tutorial.isEmpty());
    }
    
    @Test
    public void testSuggestCode() {
        AiAssistant assistant = new DefaultAiAssistant();
        
        String code = assistant.suggestCode("创建一个UserService");
        assertNotNull(code);
        assertFalse(code.isEmpty());
    }
    
    @Test
    public void testExplainCode() {
        AiAssistant assistant = new DefaultAiAssistant();
        
        String code = """
            public class UserService {
                public User findById(Long id) {
                    return null;
                }
            }
            """;
        
        String explanation = assistant.explainCode(code);
        assertNotNull(explanation);
        assertFalse(explanation.isEmpty());
    }
    
    @Test
    public void testOptimizeCode() {
        AiAssistant assistant = new DefaultAiAssistant();
        
        String code = """
            public List<User> getUsers() {
                List<User> users = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    users.add(new User());
                }
                return users;
            }
            """;
        
        String optimized = assistant.optimizeCode(code);
        assertNotNull(optimized);
        assertFalse(optimized.isEmpty());
    }
    
    @Test
    public void testGetCodeGenerator() {
        AiAssistant assistant = new DefaultAiAssistant();
        assertNotNull(assistant.getCodeGenerator());
    }
    
    @Test
    public void testGetProjectScaffold() {
        AiAssistant assistant = new DefaultAiAssistant();
        assertNotNull(assistant.getProjectScaffold());
    }
    
    @Test
    public void testGetTemplateRegistry() {
        AiAssistant assistant = new DefaultAiAssistant();
        assertNotNull(assistant.getTemplateRegistry());
    }
    
    @Test
    public void testGetLlmClient() {
        AiAssistant assistant = new DefaultAiAssistant();
        assertNotNull(assistant.getLlmClient());
    }
    
    @Test
    public void testChatWithString() {
        AiAssistant assistant = new DefaultAiAssistant();
        
        String response = assistant.chat("你好");
        assertNotNull(response);
    }
    
    @Test
    public void testChatWithMessages() {
        AiAssistant assistant = new DefaultAiAssistant();
        
        String response = assistant.chat(List.of(
            new ltd.idcu.est.ai.api.LlmMessage("user", "你好")
        ));
        assertNotNull(response);
    }
    
    @Test
    public void testGetRequirementParser() {
        AiAssistant assistant = new DefaultAiAssistant();
        assertNotNull(assistant.getRequirementParser());
    }
    
    @Test
    public void testGetArchitectureDesigner() {
        AiAssistant assistant = new DefaultAiAssistant();
        assertNotNull(assistant.getArchitectureDesigner());
    }
    
    @Test
    public void testGetTestAndDeployManager() {
        AiAssistant assistant = new DefaultAiAssistant();
        assertNotNull(assistant.getTestAndDeployManager());
    }
    
    @Test
    public void testGetConfig() {
        AiAssistant assistant = new DefaultAiAssistant();
        assertNotNull(assistant.getConfig());
        assertNotNull(assistant.getConfig().getDefaultLlmProvider());
    }
    
    @Test
    public void testSetConfig() {
        AiAssistant assistant = new DefaultAiAssistant();
        ltd.idcu.est.ai.impl.config.DefaultAiConfig newConfig = 
            new ltd.idcu.est.ai.impl.config.DefaultAiConfig()
                .defaultLlmProvider("openai");
        assistant.setConfig(newConfig);
        assertEquals("openai", assistant.getConfig().getDefaultLlmProvider());
    }
}
