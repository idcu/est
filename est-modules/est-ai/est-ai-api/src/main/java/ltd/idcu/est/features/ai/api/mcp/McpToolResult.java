package ltd.idcu.est.features.ai.api.mcp;

import java.util.Map;

public class McpToolResult {

    private final boolean success;
    private final String content;
    private final Map<String, Object> metadata;
    private final boolean isError;

    public McpToolResult(boolean success, String content, Map<String, Object> metadata, boolean isError) {
        this.success = success;
        this.content = content;
        this.metadata = metadata;
        this.isError = isError;
    }

    public static McpToolResult success(String content) {
        return new McpToolResult(true, content, null, false);
    }

    public static McpToolResult success(String content, Map<String, Object> metadata) {
        return new McpToolResult(true, content, metadata, false);
    }

    public static McpToolResult error(String errorMessage) {
        return new McpToolResult(false, errorMessage, null, true);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getContent() {
        return content;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public boolean isError() {
        return isError;
    }
}
