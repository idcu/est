package ltd.idcu.est.codecli.mcp;

import ltd.idcu.est.ai.api.mcp.McpTool;
import ltd.idcu.est.ai.api.mcp.McpToolResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

public class WriteFileMcpTool implements McpTool {
    
    private final Path workDir;
    
    public WriteFileMcpTool(Path workDir) {
        this.workDir = workDir;
    }
    
    public WriteFileMcpTool(String workDir) {
        this(Paths.get(workDir));
    }
    
    @Override
    public String getName() {
        return "est_write_file";
    }
    
    @Override
    public String getDescription() {
        return "Writes content to a file in the workspace";
    }
    
    @Override
    public Map<String, Object> getInputSchema() {
        return Map.of(
            "type", "object",
            "properties", Map.of(
                "path", Map.of(
                    "type", "string",
                    "description", "Path to the file (relative to workspace)"
                ),
                "content", Map.of(
                    "type", "string",
                    "description", "Content to write to the file"
                ),
                "append", Map.of(
                    "type", "boolean",
                    "description", "Whether to append to the file (default: false)"
                )
            ),
            "required", List.of("path", "content")
        );
    }
    
    @Override
    public McpToolResult execute(Map<String, Object> arguments) {
        try {
            String filePath = (String) arguments.get("path");
            String content = (String) arguments.get("content");
            Boolean append = (Boolean) arguments.getOrDefault("append", false);
            
            Path fullPath = workDir.resolve(filePath).normalize();
            
            if (!fullPath.startsWith(workDir)) {
                return McpToolResult.error("Access denied: Path outside workspace");
            }
            
            Path parentDir = fullPath.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
            
            if (append) {
                Files.writeString(fullPath, content, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } else {
                Files.writeString(fullPath, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            }
            
            return McpToolResult.success("File written successfully", 
                Map.of("path", filePath, "size", content.length(), "appended", append));
            
        } catch (Exception e) {
            return McpToolResult.error("Error writing file: " + e.getMessage());
        }
    }
}
