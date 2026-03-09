package ltd.idcu.est.codecli.mcp;

import ltd.idcu.est.ai.api.mcp.McpTool;
import ltd.idcu.est.ai.api.mcp.McpToolResult;

import java.util.List;
import java.util.Map;

public class CodeGenMcpTool implements McpTool {
    
    @Override
    public String getName() {
        return "est_codegen";
    }
    
    @Override
    public String getDescription() {
        return "Generates code (Entity, Controller, Service, Mapper) for EST framework";
    }
    
    @Override
    public Map<String, Object> getInputSchema() {
        return Map.of(
            "type", "object",
            "properties", Map.of(
                "generateType", Map.of(
                    "type", "string",
                    "description", "Type of code to generate: entity, controller, service, mapper, all",
                    "enum", List.of("entity", "controller", "service", "mapper", "all")
                ),
                "tableName", Map.of(
                    "type", "string",
                    "description", "Database table name (for database-driven generation)"
                ),
                "className", Map.of(
                    "type", "string",
                    "description", "Class name (e.g., User)"
                ),
                "packageName", Map.of(
                    "type", "string",
                    "description", "Java package name (default: com.example)"
                ),
                "fields", Map.of(
                    "type", "array",
                    "description", "List of fields in format 'name:type' (e.g., ['id:Long', 'name:String'])"
                )
            ),
            "required", List.of("generateType")
        );
    }
    
    @Override
    public McpToolResult execute(Map<String, Object> arguments) {
        try {
            String generateType = (String) arguments.get("generateType");
            String tableName = (String) arguments.get("tableName");
            String className = (String) arguments.get("className");
            String packageName = (String) arguments.getOrDefault("packageName", "com.example");
            
            StringBuilder result = new StringBuilder();
            result.append("Generating code:\n");
            result.append("  Type: ").append(generateType).append("\n");
            if (tableName != null) {
                result.append("  Table: ").append(tableName).append("\n");
            }
            if (className != null) {
                result.append("  Class: ").append(className).append("\n");
            }
            result.append("  Package: ").append(packageName).append("\n\n");
            
            if ("all".equals(generateType)) {
                result.append("Will generate: Entity, Controller, Service, Mapper\n\n");
            } else {
                result.append("Will generate: ").append(generateType).append("\n\n");
            }
            
            result.append("Note: Full codegen integration coming soon!\n");
            result.append("For now, use the est-codegen module directly.");
            
            return McpToolResult.success(result.toString(), 
                Map.of(
                    "generateType", generateType,
                    "tableName", tableName,
                    "className", className,
                    "packageName", packageName
                ));
            
        } catch (Exception e) {
            return McpToolResult.error("Error generating code: " + e.getMessage());
        }
    }
}
