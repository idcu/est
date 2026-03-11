package ltd.idcu.est.agent.api;

import java.util.Map;

public class SkillContext {
    
    private Map<String, Object> config;
    private Map<String, Object> state;
    private Agent agent;
    
    public SkillContext() {
    }
    
    public Map<String, Object> getConfig() {
        return config;
    }
    
    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }
    
    public Map<String, Object> getState() {
        return state;
    }
    
    public void setState(Map<String, Object> state) {
        this.state = state;
    }
    
    public Agent getAgent() {
        return agent;
    }
    
    public void setAgent(Agent agent) {
        this.agent = agent;
    }
    
    public Object getConfig(String key) {
        return config != null ? config.get(key) : null;
    }
    
    public void setState(String key, Object value) {
        if (state != null) {
            state.put(key, value);
        }
    }
    
    public Object getState(String key) {
        return state != null ? state.get(key) : null;
    }
}
