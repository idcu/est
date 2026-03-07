package ltd.idcu.est.features.ai.api;

import java.util.Map;

public interface ProjectScaffold {
    
    void createProject(String basePath, String projectName, String packageName, ProjectType type);
    
    void addModule(String projectPath, String moduleName);
    
    void addWebFeature(String projectPath);
    
    void addDataFeature(String projectPath, String databaseType);
    
    void addSecurityFeature(String projectPath, String authType);
    
    Map<String, String> getProjectStructure(String projectPath);
    
    enum ProjectType {
        BASIC,
        WEB,
        REST_API,
        FULL_STACK
    }
}
