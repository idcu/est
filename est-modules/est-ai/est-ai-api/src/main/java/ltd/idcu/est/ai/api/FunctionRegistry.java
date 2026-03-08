package ltd.idcu.est.ai.api;

import java.util.List;

public interface FunctionRegistry {
    
    void register(FunctionTool tool);
    
    void unregister(String name);
    
    FunctionTool getTool(String name);
    
    List<FunctionTool> listTools();
}
