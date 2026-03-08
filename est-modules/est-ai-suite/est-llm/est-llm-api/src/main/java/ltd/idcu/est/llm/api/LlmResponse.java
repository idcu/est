package ltd.idcu.est.llm.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LlmResponse {
    
    private String content;
    private List<LlmChoice> choices;
    private LlmUsage usage;
    private Map<String, Object> metadata;
    private boolean success;
    private String errorMessage;
    private List<LlmMessage.ToolCall> toolCalls;
    
    public LlmResponse() {
        this.choices = new ArrayList<>();
        this.success = true;
    }
    
    public static LlmResponse success(String content) {
        LlmResponse response = new LlmResponse();
        response.content = content;
        response.success = true;
        LlmChoice choice = new LlmChoice();
        choice.setText(content);
        choice.setIndex(0);
        response.choices.add(choice);
        return response;
    }
    
    public static LlmResponse error(String errorMessage) {
        LlmResponse response = new LlmResponse();
        response.success = false;
        response.errorMessage = errorMessage;
        return response;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public List<LlmChoice> getChoices() {
        return choices;
    }
    
    public void setChoices(List<LlmChoice> choices) {
        this.choices = choices;
    }
    
    public LlmUsage getUsage() {
        return usage;
    }
    
    public void setUsage(LlmUsage usage) {
        this.usage = usage;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public List<LlmMessage.ToolCall> getToolCalls() {
        return toolCalls;
    }
    
    public void setToolCalls(List<LlmMessage.ToolCall> toolCalls) {
        this.toolCalls = toolCalls;
    }
    
    public boolean hasToolCalls() {
        return toolCalls != null && !toolCalls.isEmpty();
    }
    
    public static class LlmChoice {
        private String text;
        private int index;
        private Map<String, Object> logprobs;
        private String finishReason;
        private List<LlmMessage.ToolCall> toolCalls;
        
        public String getText() {
            return text;
        }
        
        public void setText(String text) {
            this.text = text;
        }
        
        public int getIndex() {
            return index;
        }
        
        public void setIndex(int index) {
            this.index = index;
        }
        
        public Map<String, Object> getLogprobs() {
            return logprobs;
        }
        
        public void setLogprobs(Map<String, Object> logprobs) {
            this.logprobs = logprobs;
        }
        
        public String getFinishReason() {
            return finishReason;
        }
        
        public void setFinishReason(String finishReason) {
            this.finishReason = finishReason;
        }
        
        public List<LlmMessage.ToolCall> getToolCalls() {
            return toolCalls;
        }
        
        public void setToolCalls(List<LlmMessage.ToolCall> toolCalls) {
            this.toolCalls = toolCalls;
        }
    }
    
    public static class LlmUsage {
        private int promptTokens;
        private int completionTokens;
        private int totalTokens;
        
        public int getPromptTokens() {
            return promptTokens;
        }
        
        public void setPromptTokens(int promptTokens) {
            this.promptTokens = promptTokens;
        }
        
        public int getCompletionTokens() {
            return completionTokens;
        }
        
        public void setCompletionTokens(int completionTokens) {
            this.completionTokens = completionTokens;
        }
        
        public int getTotalTokens() {
            return totalTokens;
        }
        
        public void setTotalTokens(int totalTokens) {
            this.totalTokens = totalTokens;
        }
    }
}
