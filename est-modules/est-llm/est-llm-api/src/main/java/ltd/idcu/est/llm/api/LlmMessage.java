package ltd.idcu.est.llm.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LlmMessage {
    
    private String role;
    private String content;
    private Map<String, Object> metadata;
    private List<ToolCall> toolCalls;
    private String toolCallId;
    private String toolName;
    
    public LlmMessage() {
    }
    
    public LlmMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
    
    public static LlmMessage system(String content) {
        return new LlmMessage("system", content);
    }
    
    public static LlmMessage user(String content) {
        return new LlmMessage("user", content);
    }
    
    public static LlmMessage assistant(String content) {
        return new LlmMessage("assistant", content);
    }
    
    public static LlmMessage tool(String toolCallId, String toolName, String content) {
        LlmMessage message = new LlmMessage("tool", content);
        message.toolCallId = toolCallId;
        message.toolName = toolName;
        return message;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
    
    public List<ToolCall> getToolCalls() {
        return toolCalls;
    }
    
    public void setToolCalls(List<ToolCall> toolCalls) {
        this.toolCalls = toolCalls;
    }
    
    public void addToolCall(ToolCall toolCall) {
        if (this.toolCalls == null) {
            this.toolCalls = new ArrayList<>();
        }
        this.toolCalls.add(toolCall);
    }
    
    public String getToolCallId() {
        return toolCallId;
    }
    
    public void setToolCallId(String toolCallId) {
        this.toolCallId = toolCallId;
    }
    
    public String getToolName() {
        return toolName;
    }
    
    public void setToolName(String toolName) {
        this.toolName = toolName;
    }
    
    public static class ToolCall {
        private String id;
        private String type;
        private Function function;
        
        public ToolCall() {
        }
        
        public ToolCall(String id, String name, String arguments) {
            this.id = id;
            this.type = "function";
            this.function = new Function(name, arguments);
        }
        
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
        
        public Function getFunction() {
            return function;
        }
        
        public void setFunction(Function function) {
            this.function = function;
        }
    }
    
    public static class Function {
        private String name;
        private String arguments;
        
        public Function() {
        }
        
        public Function(String name, String arguments) {
            this.name = name;
            this.arguments = arguments;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getArguments() {
            return arguments;
        }
        
        public void setArguments(String arguments) {
            this.arguments = arguments;
        }
    }
}
