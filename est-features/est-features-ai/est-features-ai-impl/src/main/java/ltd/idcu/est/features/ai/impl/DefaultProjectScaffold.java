package ltd.idcu.est.features.ai.impl;

import ltd.idcu.est.features.ai.api.ProjectScaffold;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DefaultProjectScaffold implements ProjectScaffold {
    
    private final CodeGenerator codeGenerator = new DefaultCodeGenerator();
    
    @Override
    public void createProject(String basePath, String projectName, String packageName, ProjectType type) {
        try {
            Path projectPath = Paths.get(basePath, projectName);
            Files.createDirectories(projectPath);
            
            createDirectories(projectPath);
            createPomXml(projectPath, projectName, packageName);
            
            String mainJavaPath = "src/main/java/" + packageName.replace('.', '/');
            Files.createDirectories(projectPath.resolve(mainJavaPath));
            
            if (type == ProjectType.WEB || type == ProjectType.REST_API || type == ProjectType.FULL_STACK) {
                createWebApp(projectPath, projectName, packageName, mainJavaPath);
            }
            
            if (type == ProjectType.FULL_STACK) {
                createDataAccess(projectPath, packageName, mainJavaPath);
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to create project", e);
        }
    }
    
    private void createDirectories(Path projectPath) throws IOException {
        Files.createDirectories(projectPath.resolve("src/main/java"));
        Files.createDirectories(projectPath.resolve("src/main/resources"));
        Files.createDirectories(projectPath.resolve("src/test/java"));
    }
    
    private void createPomXml(Path projectPath, String projectName, String packageName) throws IOException {
        String pomContent = codeGenerator.generatePomXml(
            projectName, 
            packageName, 
            projectName.toLowerCase(), 
            "1.0.0"
        );
        Files.writeString(projectPath.resolve("pom.xml"), pomContent);
    }
    
    private void createWebApp(Path projectPath, String projectName, String packageName, String mainJavaPath) throws IOException {
        String appContent = codeGenerator.generateWebApp(projectName, packageName, Map.of());
        Files.writeString(
            projectPath.resolve(mainJavaPath + "/" + projectName + ".java"), 
            appContent
        );
    }
    
    private void createDataAccess(Path projectPath, String packageName, String mainJavaPath) throws IOException {
        String entityContent = codeGenerator.generateEntity("User", packageName + ".entity", Map.of());
        Files.createDirectories(projectPath.resolve(mainJavaPath + "/entity"));
        Files.writeString(projectPath.resolve(mainJavaPath + "/entity/User.java"), entityContent);
    }
    
    @Override
    public void addModule(String projectPath, String moduleName) {
        
    }
    
    @Override
    public void addWebFeature(String projectPath) {
        
    }
    
    @Override
    public void addDataFeature(String projectPath, String databaseType) {
        
    }
    
    @Override
    public void addSecurityFeature(String projectPath, String authType) {
        
    }
    
    @Override
    public Map<String, String> getProjectStructure(String projectPath) {
        Map<String, String> structure = new HashMap<>();
        return structure;
    }
}
