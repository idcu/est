package ltd.idcu.est.mcp.api;

import java.util.List;
import java.util.Map;

public class McpPrompt {
    
    private String name;
    private String description;
    private List<PromptArgument> arguments;
    private Map<String, Object> metadata;
    
    public McpPrompt() {
    }
    
    public McpPrompt(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<PromptArgument> getArguments() {
        return arguments;
    }
    
    public void setArguments(List<PromptArgument> arguments) {
        this.arguments = arguments;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
    
    public static class PromptArgument {
        private String name;
        private String description;
        private boolean required;
        private String type;
        
        public PromptArgument() {
        }
        
        public PromptArgument(String name, String description, boolean required) {
            this.name = name;
            this.description = description;
            this.required = required;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public boolean isRequired() {
            return required;
        }
        
        public void setRequired(boolean required) {
            this.required = required;
        }
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
    }
}
