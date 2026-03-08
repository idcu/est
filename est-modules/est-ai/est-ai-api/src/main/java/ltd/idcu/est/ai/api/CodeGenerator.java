package ltd.idcu.est.ai.api;

import java.util.Map;

public interface CodeGenerator {
    
    LlmClient getLlmClient();
    
    void setLlmClient(LlmClient llmClient);
    
    String generateWebApp(String projectName, String packageName, Map<String, Object> options);
    
    String generateController(String className, String packageName, Map<String, Object> options);
    
    String generateService(String className, String packageName, Map<String, Object> options);
    
    String generateRepository(String className, String packageName, Map<String, Object> options);
    
    String generateEntity(String className, String packageName, Map<String, Object> options);
    
    String generateTest(String className, String packageName, Map<String, Object> options);
    
    String generatePomXml(String projectName, String groupId, String artifactId, String version);
    
    String generateFromRequirement(String requirement);
}
