package ltd.idcu.est.agent.api;

import java.util.Map;

public class AgentRequest {
    
    private String task;
    private Map<String, Object> context;
    private int maxSteps;
    private boolean enableReflection;
    
    public AgentRequest() {
        this.maxSteps = 10;
        this.enableReflection = true;
    }
    
    public AgentRequest(String task) {
        this();
        this.task = task;
    }
    
    public String getTask() {
        return task;
    }
    
    public void setTask(String task) {
        this.task = task;
    }
    
    public Map<String, Object> getContext() {
        return context;
    }
    
    public void setContext(Map<String, Object> context) {
        this.context = context;
    }
    
    public int getMaxSteps() {
        return maxSteps;
    }
    
    public void setMaxSteps(int maxSteps) {
        this.maxSteps = maxSteps;
    }
    
    public boolean isEnableReflection() {
        return enableReflection;
    }
    
    public void setEnableReflection(boolean enableReflection) {
        this.enableReflection = enableReflection;
    }
}
