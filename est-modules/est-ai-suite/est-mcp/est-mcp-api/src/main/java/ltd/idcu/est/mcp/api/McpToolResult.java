package ltd.idcu.est.mcp.api;

import java.util.List;
import java.util.Map;

public class McpToolResult {
    
    private boolean success;
    private List<Content> content;
    private String error;
    private Map<String, Object> metadata;
    
    public McpToolResult() {
    }
    
    public McpToolResult(boolean success) {
        this.success = success;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public List<Content> getContent() {
        return content;
    }
    
    public void setContent(List<Content> content) {
        this.content = content;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
    
    public static class Content {
        private String type;
        private String text;
        private Map<String, Object> data;
        
        public Content() {
        }
        
        public Content(String type, String text) {
            this.type = type;
            this.text = text;
        }
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
        
        public String getText() {
            return text;
        }
        
        public void setText(String text) {
            this.text = text;
        }
        
        public Map<String, Object> getData() {
            return data;
        }
        
        public void setData(Map<String, Object> data) {
            this.data = data;
        }
    }
}
