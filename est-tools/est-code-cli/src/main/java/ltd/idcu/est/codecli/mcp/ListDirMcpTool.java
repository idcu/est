package ltd.idcu.est.codecli.mcp;

import ltd.idcu.est.ai.api.mcp.McpTool;
import ltd.idcu.est.ai.api.mcp.McpToolResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ListDirMcpTool implements McpTool {
    
    private final Path workDir;
    
    public ListDirMcpTool(Path workDir) {
        this.workDir = workDir;
    }
    
    public ListDirMcpTool(String workDir) {
        this(Paths.get(workDir));
    }
    
    @Override
    public String getName() {
        return "est_list_dir";
    }
    
    @Override
    public String getDescription() {
        return "Lists the contents of a directory in the workspace";
    }
    
    @Override
    public Map<String, Object> getInputSchema() {
        return Map.of(
            "type", "object",
            "properties", Map.of(
                "path", Map.of(
                    "type", "string",
                    "description", "Path to the directory (relative to workspace, default: .)"
                )
            )
        );
    }
    
    @Override
    public McpToolResult execute(Map<String, Object> arguments) {
        try {
            String dirPath = (String) arguments.getOrDefault("path", ".");
            Path fullPath = workDir.resolve(dirPath).normalize();
            
            if (!fullPath.startsWith(workDir)) {
                return McpToolResult.error("Access denied: Path outside workspace");
            }
            
            if (!Files.exists(fullPath)) {
                return McpToolResult.error("Directory not found: " + dirPath);
            }
            
            if (!Files.isDirectory(fullPath)) {
                return McpToolResult.error("Path is not a directory: " + dirPath);
            }
            
            List<Map<String, Object>> entries = new ArrayList<>();
            
            try (Stream<Path> stream = Files.list(fullPath)) {
                stream.forEach(path -> {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("name", path.getFileName().toString());
                    entry.put("type", Files.isDirectory(path) ? "directory" : "file");
                    try {
                        entry.put("size", Files.size(path));
                    } catch (Exception e) {
                        entry.put("size", 0L);
                    }
                    entries.add(entry);
                });
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append("Contents of ").append(dirPath).append(":\n\n");
            
            for (Map<String, Object> entry : entries) {
                String type = (String) entry.get("type");
                String name = (String) entry.get("name");
                String prefix = "directory".equals(type) ? "[DIR]  " : "[FILE] ";
                sb.append(prefix).append(name).append("\n");
            }
            
            return McpToolResult.success(sb.toString(), 
                Map.of("path", dirPath, "entries", entries));
            
        } catch (Exception e) {
            return McpToolResult.error("Error listing directory: " + e.getMessage());
        }
    }
}
