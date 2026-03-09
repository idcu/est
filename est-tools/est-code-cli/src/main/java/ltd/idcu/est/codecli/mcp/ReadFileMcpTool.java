package ltd.idcu.est.codecli.mcp;

import ltd.idcu.est.ai.api.mcp.McpTool;
import ltd.idcu.est.ai.api.mcp.McpToolResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class ReadFileMcpTool implements McpTool {
    
    private final Path workDir;
    
    public ReadFileMcpTool(Path workDir) {
        this.workDir = workDir;
    }
    
    public ReadFileMcpTool(String workDir) {
        this(Paths.get(workDir));
    }
    
    @Override
    public String getName() {
        return "est_read_file";
    }
    
    @Override
    public String getDescription() {
        return "Reads the content of a file from the workspace";
    }
    
    @Override
    public Map<String, Object> getInputSchema() {
        return Map.of(
            "type", "object",
            "properties", Map.of(
                "path", Map.of(
                    "type", "string",
                    "description", "Path to the file (relative to workspace)"
                )
            ),
            "required", List.of("path")
        );
    }
    
    @Override
    public McpToolResult execute(Map<String, Object> arguments) {
        try {
            String filePath = (String) arguments.get("path");
            Path fullPath = workDir.resolve(filePath).normalize();
            
            if (!fullPath.startsWith(workDir)) {
                return McpToolResult.error("Access denied: Path outside workspace");
            }
            
            if (!Files.exists(fullPath)) {
                return McpToolResult.error("File not found: " + filePath);
            }
            
            if (Files.isDirectory(fullPath)) {
                return McpToolResult.error("Path is a directory, not a file: " + filePath);
            }
            
            String content = Files.readString(fullPath);
            return McpToolResult.success(content, Map.of("path", filePath, "size", content.length()));
            
        } catch (Exception e) {
            return McpToolResult.error("Error reading file: " + e.getMessage());
        }
    }
}
