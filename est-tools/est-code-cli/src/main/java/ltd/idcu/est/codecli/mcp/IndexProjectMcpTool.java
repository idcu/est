package ltd.idcu.est.codecli.mcp;

import ltd.idcu.est.ai.api.mcp.McpTool;
import ltd.idcu.est.ai.api.mcp.McpToolResult;
import ltd.idcu.est.codecli.search.FileIndex;
import ltd.idcu.est.codecli.search.ProjectIndexer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IndexProjectMcpTool implements McpTool {
    
    private final FileIndex fileIndex;
    private final ProjectIndexer indexer;
    private final String workDir;
    
    public IndexProjectMcpTool(FileIndex fileIndex, ProjectIndexer indexer, String workDir) {
        this.fileIndex = fileIndex;
        this.indexer = indexer;
        this.workDir = workDir;
    }
    
    @Override
    public String getName() {
        return "est_index_project";
    }
    
    @Override
    public String getDescription() {
        return "Indexes the project files for search capabilities";
    }
    
    @Override
    public String getInputSchema() {
        return "{\n" +
               "  \"type\": \"object\",\n" +
               "  \"properties\": {\n" +
               "    \"path\": {\n" +
               "      \"type\": \"string\",\n" +
               "      \"description\": \"Path to the project directory (default: current work dir)\"\n" +
               "    }\n" +
               "  }\n" +
               "}";
    }
    
    @Override
    public McpToolResult execute(Map<String, Object> args) {
        try {
            String path = args.get("path") != null ? (String) args.get("path") : workDir;
            
            indexer.indexProject(path);
            
            Set<String> indexedFiles = fileIndex.getAllIndexedFiles();
            
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("path", path);
            metadata.put("indexedFiles", indexedFiles.size());
            
            return McpToolResult.success(
                "Project indexed successfully!\n" +
                "Indexed files: " + indexedFiles.size() + "\n" +
                "Included extensions: " + String.join(", ", indexer.getIncludedExtensions()),
                metadata
            );
            
        } catch (Exception e) {
            return McpToolResult.error("Indexing failed: " + e.getMessage());
        }
    }
}
