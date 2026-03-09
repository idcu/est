package ltd.idcu.est.codecli.mcp;

import ltd.idcu.est.ai.api.mcp.McpTool;
import ltd.idcu.est.ai.api.mcp.McpToolResult;
import ltd.idcu.est.codecli.search.FileIndex;
import ltd.idcu.est.codecli.search.ProjectIndexer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchMcpTool implements McpTool {
    
    private final FileIndex fileIndex;
    private final ProjectIndexer indexer;
    
    public SearchMcpTool(FileIndex fileIndex, ProjectIndexer indexer) {
        this.fileIndex = fileIndex;
        this.indexer = indexer;
    }
    
    @Override
    public String getName() {
        return "est_search";
    }
    
    @Override
    public String getDescription() {
        return "Searches the indexed project files using keyword search";
    }
    
    @Override
    public String getInputSchema() {
        return "{\n" +
               "  \"type\": \"object\",\n" +
               "  \"properties\": {\n" +
               "    \"query\": {\n" +
               "      \"type\": \"string\",\n" +
               "      \"description\": \"Search query keywords\"\n" +
               "    },\n" +
               "    \"limit\": {\n" +
               "      \"type\": \"integer\",\n" +
               "      \"description\": \"Maximum number of results to return (default: 10)\"\n" +
               "    }\n" +
               "  },\n" +
               "  \"required\": [\"query\"]\n" +
               "}";
    }
    
    @Override
    public McpToolResult execute(Map<String, Object> args) {
        try {
            String query = (String) args.get("query");
            Integer limit = args.get("limit") != null ? ((Number) args.get("limit")).intValue() : 10;
            
            if (query == null || query.trim().isEmpty()) {
                return McpToolResult.error("Query is required");
            }
            
            List<FileIndex.SearchResult> results = fileIndex.search(query);
            
            if (results.isEmpty()) {
                return McpToolResult.success("No results found for: " + query, new HashMap<>());
            }
            
            List<FileIndex.SearchResult> limitedResults = results.size() > limit ? results.subList(0, limit) : results;
            
            StringBuilder sb = new StringBuilder();
            sb.append("Search results for: ").append(query).append("\n\n");
            
            for (int i = 0; i < limitedResults.size(); i++) {
                FileIndex.SearchResult result = limitedResults.get(i);
                sb.append(String.format("[%d] %s (score: %d)\n", i + 1, result.getDocument().getFilePath(), result.getScore()));
                sb.append("    ").append(result.getSnippet()).append("\n\n");
            }
            
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("query", query);
            metadata.put("totalResults", results.size());
            metadata.put("returnedResults", limitedResults.size());
            
            return McpToolResult.success(sb.toString(), metadata);
            
        } catch (Exception e) {
            return McpToolResult.error("Search failed: " + e.getMessage());
        }
    }
}
