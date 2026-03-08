package ltd.idcu.est.ai.api;

import java.util.Map;

public interface FunctionTool {
    
    String getName();
    
    String getDescription();
    
    Map<String, Object> getParameters();
    
    Object execute(Map<String, Object> arguments);
}
