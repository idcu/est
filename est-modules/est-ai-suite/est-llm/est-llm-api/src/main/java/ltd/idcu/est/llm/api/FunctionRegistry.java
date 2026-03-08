package ltd.idcu.est.llm.api;

import java.util.List;

public interface FunctionRegistry {
    
    void register(FunctionTool tool);
    
    void unregister(String name);
    
    FunctionTool getTool(String name);
    
    List<FunctionTool> listTools();
}
