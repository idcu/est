package ltd.idcu.est.codecli.contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectContract {
    
    private String projectName;
    private String projectType;
    private String frameworkVersion;
    private List<String> buildCommands = new ArrayList<>();
    private List<String> testCommands = new ArrayList<>();
    private List<String> runCommands = new ArrayList<>();
    private Map<String, String> environmentVariables = new HashMap<>();
    private List<String> codingStandards = new ArrayList<>();
    private List<String> importantFiles = new ArrayList<>();
    private String description;
    
    public ProjectContract() {
    }
    
    public String getProjectName() {
        return projectName;
    }
    
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    public String getProjectType() {
        return projectType;
    }
    
    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }
    
    public String getFrameworkVersion() {
        return frameworkVersion;
    }
    
    public void setFrameworkVersion(String frameworkVersion) {
        this.frameworkVersion = frameworkVersion;
    }
    
    public List<String> getBuildCommands() {
        return buildCommands;
    }
    
    public void setBuildCommands(List<String> buildCommands) {
        this.buildCommands = buildCommands;
    }
    
    public void addBuildCommand(String command) {
        this.buildCommands.add(command);
    }
    
    public List<String> getTestCommands() {
        return testCommands;
    }
    
    public void setTestCommands(List<String> testCommands) {
        this.testCommands = testCommands;
    }
    
    public void addTestCommand(String command) {
        this.testCommands.add(command);
    }
    
    public List<String> getRunCommands() {
        return runCommands;
    }
    
    public void setRunCommands(List<String> runCommands) {
        this.runCommands = runCommands;
    }
    
    public void addRunCommand(String command) {
        this.runCommands.add(command);
    }
    
    public Map<String, String> getEnvironmentVariables() {
        return environmentVariables;
    }
    
    public void setEnvironmentVariables(Map<String, String> environmentVariables) {
        this.environmentVariables = environmentVariables;
    }
    
    public void addEnvironmentVariable(String key, String value) {
        this.environmentVariables.put(key, value);
    }
    
    public List<String> getCodingStandards() {
        return codingStandards;
    }
    
    public void setCodingStandards(List<String> codingStandards) {
        this.codingStandards = codingStandards;
    }
    
    public void addCodingStandard(String standard) {
        this.codingStandards.add(standard);
    }
    
    public List<String> getImportantFiles() {
        return importantFiles;
    }
    
    public void setImportantFiles(List<String> importantFiles) {
        this.importantFiles = importantFiles;
    }
    
    public void addImportantFile(String file) {
        this.importantFiles.add(file);
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("# EST Project Contract\n\n");
        
        if (projectName != null) {
            sb.append("## Project Name\n").append(projectName).append("\n\n");
        }
        
        if (description != null) {
            sb.append("## Description\n").append(description).append("\n\n");
        }
        
        if (projectType != null) {
            sb.append("## Project Type\n").append(projectType).append("\n\n");
        }
        
        if (frameworkVersion != null) {
            sb.append("## Framework Version\n").append(frameworkVersion).append("\n\n");
        }
        
        if (!buildCommands.isEmpty()) {
            sb.append("## Build Commands\n");
            for (String cmd : buildCommands) {
                sb.append("- ").append(cmd).append("\n");
            }
            sb.append("\n");
        }
        
        if (!testCommands.isEmpty()) {
            sb.append("## Test Commands\n");
            for (String cmd : testCommands) {
                sb.append("- ").append(cmd).append("\n");
            }
            sb.append("\n");
        }
        
        if (!runCommands.isEmpty()) {
            sb.append("## Run Commands\n");
            for (String cmd : runCommands) {
                sb.append("- ").append(cmd).append("\n");
            }
            sb.append("\n");
        }
        
        if (!environmentVariables.isEmpty()) {
            sb.append("## Environment Variables\n");
            for (Map.Entry<String, String> entry : environmentVariables.entrySet()) {
                sb.append("- ").append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
            }
            sb.append("\n");
        }
        
        if (!codingStandards.isEmpty()) {
            sb.append("## Coding Standards\n");
            for (String standard : codingStandards) {
                sb.append("- ").append(standard).append("\n");
            }
            sb.append("\n");
        }
        
        if (!importantFiles.isEmpty()) {
            sb.append("## Important Files\n");
            for (String file : importantFiles) {
                sb.append("- ").append(file).append("\n");
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
}
