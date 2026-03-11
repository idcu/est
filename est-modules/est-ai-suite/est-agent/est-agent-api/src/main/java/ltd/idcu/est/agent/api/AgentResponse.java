package ltd.idcu.est.agent.api;

import java.util.List;
import java.util.Map;

public class AgentResponse {
    
    private boolean success;
    private String result;
    private List<AgentStep> steps;
    private int totalSteps;
    private String error;
    private Map<String, Object> metadata;
    
    public AgentResponse() {
    }
    
    public AgentResponse(boolean success) {
        this.success = success;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public List<AgentStep> getSteps() {
        return steps;
    }
    
    public void setSteps(List<AgentStep> steps) {
        this.steps = steps;
    }
    
    public int getTotalSteps() {
        return totalSteps;
    }
    
    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
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
    
    public static class AgentStep {
        private int stepNumber;
        private String action;
        private String thought;
        private String observation;
        private String toolName;
        private Object toolInput;
        private Object toolOutput;
        private long duration;
        
        public AgentStep() {
        }
        
        public int getStepNumber() {
            return stepNumber;
        }
        
        public void setStepNumber(int stepNumber) {
            this.stepNumber = stepNumber;
        }
        
        public String getAction() {
            return action;
        }
        
        public void setAction(String action) {
            this.action = action;
        }
        
        public String getThought() {
            return thought;
        }
        
        public void setThought(String thought) {
            this.thought = thought;
        }
        
        public String getObservation() {
            return observation;
        }
        
        public void setObservation(String observation) {
            this.observation = observation;
        }
        
        public String getToolName() {
            return toolName;
        }
        
        public void setToolName(String toolName) {
            this.toolName = toolName;
        }
        
        public Object getToolInput() {
            return toolInput;
        }
        
        public void setToolInput(Object toolInput) {
            this.toolInput = toolInput;
        }
        
        public Object getToolOutput() {
            return toolOutput;
        }
        
        public void setToolOutput(Object toolOutput) {
            this.toolOutput = toolOutput;
        }
        
        public long getDuration() {
            return duration;
        }
        
        public void setDuration(long duration) {
            this.duration = duration;
        }
    }
}
