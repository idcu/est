package ltd.idcu.est.agent.api;

import java.util.Map;

public class SkillResult {
    
    private boolean success;
    private String output;
    private Map<String, Object> data;
    private String error;
    
    public SkillResult() {
    }
    
    public SkillResult(boolean success) {
        this.success = success;
    }
    
    public static SkillResult success(String output) {
        SkillResult result = new SkillResult(true);
        result.setOutput(output);
        return result;
    }
    
    public static SkillResult failure(String error) {
        SkillResult result = new SkillResult(false);
        result.setError(error);
        return result;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getOutput() {
        return output;
    }
    
    public void setOutput(String output) {
        this.output = output;
    }
    
    public Map<String, Object> getData() {
        return data;
    }
    
    public void setData(Map<String, Object> data) {
        this.data = data;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
}
