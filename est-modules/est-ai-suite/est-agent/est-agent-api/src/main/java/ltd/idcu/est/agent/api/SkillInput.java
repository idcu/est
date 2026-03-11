package ltd.idcu.est.agent.api;

import java.util.Map;

public class SkillInput {
    
    private String task;
    private Map<String, Object> parameters;
    private Map<String, Object> context;
    
    public SkillInput() {
    }
    
    public SkillInput(String task) {
        this.task = task;
    }
    
    public String getTask() {
        return task;
    }
    
    public void setTask(String task) {
        this.task = task;
    }
    
    public Map<String, Object> getParameters() {
        return parameters;
    }
    
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
    
    public Map<String, Object> getContext() {
        return context;
    }
    
    public void setContext(Map<String, Object> context) {
        this.context = context;
    }
    
    public Object getParameter(String key) {
        return parameters != null ? parameters.get(key) : null;
    }
    
    public String getParameterAsString(String key) {
        Object value = getParameter(key);
        return value != null ? value.toString() : null;
    }
}
