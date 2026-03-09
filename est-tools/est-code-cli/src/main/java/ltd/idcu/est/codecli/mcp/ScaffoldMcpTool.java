package ltd.idcu.est.codecli.mcp;

import ltd.idcu.est.ai.api.mcp.McpTool;
import ltd.idcu.est.ai.api.mcp.McpToolResult;

import java.util.List;
import java.util.Map;

public class ScaffoldMcpTool implements McpTool {
    
    @Override
    public String getName() {
        return "est_scaffold";
    }
    
    @Override
    public String getDescription() {
        return "Creates a new EST project using scaffold templates";
    }
    
    @Override
    public Map<String, Object> getInputSchema() {
        return Map.of(
            "type", "object",
            "properties", Map.of(
                "projectType", Map.of(
                    "type", "string",
                    "description", "Project type: basic, web, api, cli, library, plugin, microservice",
                    "enum", List.of("basic", "web", "api", "cli", "library", "plugin", "microservice")
                ),
                "projectName", Map.of(
                    "type", "string",
                    "description", "Name of the project"
                ),
                "groupId", Map.of(
                    "type", "string",
                    "description": "Maven groupId (default: com.example)"
                ),
                "packageName", Map.of(
                    "type", "string",
                    "description": "Java package name (auto-generated if not specified)"
                ),
                "javaVersion", Map.of(
                    "type", "string",
                    "description", "Java version (default: 21)"
                ),
                "initGit", Map.of(
                    "type", "boolean",
                    "description", "Initialize Git repository (default: false)"
                )
            ),
            "required", List.of("projectType", "projectName")
        );
    }
    
    @Override
    public McpToolResult execute(Map<String, Object> arguments) {
        try {
            String projectType = (String) arguments.get("projectType");
            String projectName = (String) arguments.get("projectName");
            String groupId = (String) arguments.getOrDefault("groupId", "com.example");
            String packageName = (String) arguments.get("packageName");
            String javaVersion = (String) arguments.getOrDefault("javaVersion", "21");
            Boolean initGit = (Boolean) arguments.getOrDefault("initGit", false);
            
            if (packageName == null || packageName.isEmpty()) {
                packageName = groupId + "." + projectName.replace("-", "").toLowerCase();
            }
            
            StringBuilder result = new StringBuilder();
            result.append("Creating EST project:\n");
            result.append("  Type: ").append(projectType).append("\n");
            result.append("  Name: ").append(projectName).append("\n");
            result.append("  GroupId: ").append(groupId).append("\n");
            result.append("  Package: ").append(packageName).append("\n");
            result.append("  Java Version: ").append(javaVersion).append("\n");
            result.append("  Git Init: ").append(initGit).append("\n\n");
            
            result.append("Note: Full scaffold integration coming soon!\n");
            result.append("For now, use the est-scaffold module directly.");
            
            return McpToolResult.success(result.toString(), 
                Map.of(
                    "projectType", projectType,
                    "projectName", projectName,
                    "groupId", groupId,
                    "packageName", packageName,
                    "javaVersion", javaVersion,
                    "initGit", initGit
                ));
            
        } catch (Exception e) {
            return McpToolResult.error("Error creating project: " + e.getMessage());
        }
    }
}
